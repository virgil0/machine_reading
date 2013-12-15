package edu.cmu.cs.vlis.examples;

import edu.cmu.cs.vlis.result.MatchingResult;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;

import java.util.List;
import java.util.Map;

import edu.cmu.cs.vlis.matcher.*;
import edu.cmu.cs.vlis.matching.*;
import edu.cmu.cs.vlis.visual.*;

/**
 * this class shows an example of doing arbitrary graph match.
 * Graph<Integer, String> is a generic graph interface (multiple implementations exist),
 * where Integer is the type of node , and String is the type of edge.
 *
 * In the first test case, all four matching algorithms can find some matched graph
 * the difference between exact matching and inexact matching is that,
 * exact matching tries to find the entire structure of pattern from target,
 * while inexact matching tries to return any sub-graph of target, which can
 * only partially matches the pattern.
 *
 * In the second test case, exact matching and inexact matching fail to find any result.
 * However, both exact isomorphism and inexact isomorphism can find out all results.
 * the difference between matching and isomorphism is that,
 * matching algorithms try to find exact matched results, i.e., in this example,
 * each node number in pattern must exactly match the node number in target;
 * on the other hand, isomorphism algorithms are more flexible, they try to find any
 * similar structure between pattern and target, i.e., in this example, isomorphism
 * algorithms will find some results as long as there exists identical graph structure
 * between pattern and target.
 *
 * Matching results will be shown in a straightforward graphic manner.
 * 
 * @author ke xu (morefree7@gmail.com)
 */
public class ArbitraryGraphMatch {
    // pattern graph and target graph. there are multiple implementations to Graph interface
    private static Graph<Integer, String> pattern, target;

    // Integer : type of node;
    // String  : type of edge
    // List<Graph<Integer, String>> : type of match result. each Graph in the list
    // is a sub-graph of target, which partially or fully matches the pattern.
    private static GraphMatcher<Integer, String, List<Graph<Integer, String>>> matcher;

    public static void main(String [] args) {
        /* a simple example where all four algorithms can find some results */
        testCase1();

        /* uncommend this line to see a more complicated example,
           where matching algorithm fails to find result, but isomorphism algorithm
           still find all results successfully */
        //testCase2();
    }

    // in this test case, all four matching algorithms can find some matched graph
    // the difference between exact matching and inexact matching is that,
    // exact matching tries to find the entire structure of pattern from target,
    // while inexact matching tries to return any sub-graph of target, which can
    // only partially matches the pattern
    private static void testCase1() {
        pattern = new SparseMultigraph<Integer, String>();
        pattern.addVertex(1);
        pattern.addVertex(2);
        pattern.addVertex(3);
        pattern.addVertex(4);

        pattern.addEdge("1->2", 1, 2, EdgeType.DIRECTED);
        pattern.addEdge("2->3", 2, 3, EdgeType.DIRECTED);
        pattern.addEdge("3->4", 3, 4, EdgeType.DIRECTED);

        target = new SparseMultigraph<Integer, String>();
        target.addVertex(1);
        target.addVertex(2);
        target.addVertex(3);
        target.addVertex(4);
        target.addVertex(5);
        target.addVertex(6);
        target.addVertex(7);

        target.addEdge("1->2", 1, 2, EdgeType.DIRECTED);
        target.addEdge("1->4", 1, 4, EdgeType.DIRECTED);
        target.addEdge("2->3", 2, 3, EdgeType.DIRECTED);
        target.addEdge("3->4", 3, 4, EdgeType.DIRECTED);
        target.addEdge("5->2", 5, 2, EdgeType.DIRECTED);
        target.addEdge("6->3", 6, 3, EdgeType.DIRECTED);
        target.addEdge("7->4", 7, 4, EdgeType.DIRECTED);


        showGraph(pattern, "pattern");
        showGraph(target, "target");

        matcher = new ArbitraryGraphMatcher<Integer, String>();

        testExactMatching();
        testInexactMatching();
        testExactIsomorphism();
        testInexactIsomorphism();
    }

    // in this test case, exact matching and inexact matching fail to find any result.
    // However, both exact isomorphism and inexact isomorphism can find out all results.
    // the difference between matching and isomorphism is that,
    // matching algorithms try to find exact matched results, i.e., in this example,
    // each node number in pattern must exactly match the node number in target;
    // on the other hand, isomorphism algorithms are more flexible, they try to find any
    // similar structure between pattern and target, i.e., in this example, isomorphism
    // algorithms will find some results as long as there exists identical graph structure
    // between pattern and target.
    private static void testCase2() {
        pattern = new SparseMultigraph<Integer, String>();
        pattern.addVertex(1);
        pattern.addVertex(2);
        pattern.addVertex(3);
        pattern.addVertex(4);
        pattern.addVertex(5);

        pattern.addEdge("1->2", 1, 2, EdgeType.DIRECTED);
        pattern.addEdge("2->3", 2, 3, EdgeType.DIRECTED);
        pattern.addEdge("3->4", 3, 4, EdgeType.DIRECTED);
        pattern.addEdge("4->5", 4, 5, EdgeType.DIRECTED);

        target = new SparseMultigraph<Integer, String>();
        target.addVertex(1);
        target.addVertex(2);
        target.addVertex(3);
        target.addVertex(4);
        target.addVertex(5);
        target.addVertex(6);
        target.addVertex(7);

        target.addEdge("1->2", 1, 2, EdgeType.DIRECTED);
        target.addEdge("1->4", 1, 4, EdgeType.DIRECTED);
        target.addEdge("2->3", 2, 3, EdgeType.DIRECTED);
        target.addEdge("3->4", 3, 4, EdgeType.DIRECTED);
        target.addEdge("5->2", 5, 2, EdgeType.DIRECTED);
        target.addEdge("6->3", 6, 3, EdgeType.DIRECTED);
        target.addEdge("4->3", 4, 3, EdgeType.DIRECTED);
        target.addEdge("7->4", 7, 4, EdgeType.DIRECTED);


        showGraph(pattern, "pattern");
        showGraph(target, "target");

        matcher = new ArbitraryGraphMatcher<Integer, String>();

        testExactMatching();
        testInexactMatching();
        testExactIsomorphism();
        testInexactIsomorphism();
    }

    private static void testExactMatching() {
        System.out.println("test exact matching ...");
        Matching<Integer, String> matching = new ExactMatching<Integer, String>(pattern, target);
        matcher.setMatching(matching);
        MatchingResult<List<Graph<Integer, String>>> result = matcher.match(pattern, target);

        int cnt = 1;
        for(Graph<Integer, String> matched : result.getMatchResult())
            showGraph(matched, "exact matching result : " + cnt++);

        System.out.println("all possible mappings");
        for(Map<Integer, Integer> mapping : matcher.getMappings())
            System.out.println(mapping);
    }

    private static void testInexactMatching() {
        System.out.println("test inexact matching ...");
        Matching<Integer, String> matching = new InexactMatching<Integer, String>(pattern, target);
        matcher.setMatching(matching);
        MatchingResult<List<Graph<Integer, String>>> result = matcher.match(pattern, target);

        int cnt = 1;
        for(Graph<Integer, String> matched : result.getMatchResult())
            showGraph(matched, "inexact matching result : " + cnt++);

        System.out.println("all possible mappings");
        for(Map<Integer, Integer> mapping : matcher.getMappings())
            System.out.println(mapping);
    }

    private static void testExactIsomorphism() {
        System.out.println("test exact isomorphism ...");
        Matching<Integer, String> matching = new ExactIsomorphism<Integer, String>(pattern, target);
        matcher.setMatching(matching);
        MatchingResult<List<Graph<Integer, String>>> result = matcher.match(pattern, target);

        int cnt = 1;
        for(Graph<Integer, String> matched : result.getMatchResult())
            showGraph(matched, "exact isomorphism result : " + cnt++);

        System.out.println("all possible mappings");
        for(Map<Integer, Integer> mapping : matcher.getMappings())
            System.out.println(mapping);
    }

    private static void testInexactIsomorphism() {
        System.out.println("test inexact isomorphism ...");
        Matching<Integer, String> matching = new InexactIsomorphism<Integer, String>(pattern, target);
        matcher.setMatching(matching);
        MatchingResult<List<Graph<Integer, String>>> result = matcher.match(pattern, target);

        int cnt = 1;
        for(Graph<Integer, String> matched : result.getMatchResult())
            showGraph(matched, "inexact isomorphism result : " + cnt++);

        System.out.println("all possible mappings");
        for(Map<Integer, Integer> mapping : matcher.getMappings())
            System.out.println(mapping);
    }

    private static void showGraph(Graph<Integer, String> graph, String title) {
        GraphVisualizer<Integer, String> gv = new LayoutGraphVisualizer<Integer, String>(title,
                new ISOMLayout<Integer, String>(graph));
        gv.show(graph);
    }
}