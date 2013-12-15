package edu.cmu.cs.vlis.examples;

import edu.cmu.cs.vlis.adapter.*;
import edu.cmu.cs.vlis.matcher.ArbitraryGraphMatcher;
import edu.cmu.cs.vlis.matcher.GraphMatcher;
import edu.cmu.cs.vlis.matching.Matching;
import edu.cmu.cs.vlis.matching.InexactMatching;
import edu.cmu.cs.vlis.visual.GraphVisualizer;
import edu.cmu.cs.vlis.visual.LayoutGraphVisualizer;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.graph.Graph;
import java.util.List;
import java.io.IOException;


/**
 * this example shows how to do a indri graph match in memory.
 * the constructor of this class accepts two arguments, the pattern file and the target file.
 * Both of them share the same format :
 * for each line in the file, it represents an annotation using the following format :
 * <DocNo> "TAG" <tagID> <tagValue> <offset> <length> <parentTagID> <piece of text>
 *  1	    TAG	   0	     A0	         0	     8		   1	        The film
 *
 * Change SimpleNode.equals() method to any metric you want for graph matching, ex.
 * you can specify a matching as :  pattern.tagID == taget.tagID,
 * or you can speficy a matching as : pattern's text field is a similar to target's text field, a simple example
 * could be :  pattern.text.contains(target.text) || target.text.contains(pattern.text);
 *
 * Change SimpleNode.toString() method to anything you want to view when visualizing the graph.
 *
 * Also, in line 134, you can specify other matching algorithms instead of the inexact matching algorithm.
 * Other algorithms are contained in edu.cmu.cs.vlis.matching,
 * ex. ExactMatching, InexactMatching, ExactIsomorphism, InexactIsomorphism
 *
 * @see IndriGraphMatchWithTitan
 * @see IndriGraphMatchWithHadoop
 * 
 * @author ke xu (morefree7@gmail.com)
 */
public class IndriGraphMatch {
    protected String patternFile, targetFile;
    protected IndriGraphNodeFactory nodeFactory;
    protected IndriGraphEdgeFactory edgeFactory;
    protected IndriAdapter adapter;

    public IndriGraphMatch(String patternFile, String targetFile) {
        this.patternFile = patternFile;
        this.targetFile = targetFile;
        this.nodeFactory = new SimpleNodeFactory();
        this.edgeFactory = new SimpleEdgeFactory();
        this.adapter = new IndriAdapter(nodeFactory, edgeFactory);
    }

    private class SimpleNode extends IndriGraphNode {
        public boolean equals(Object obj) {
            if(!(obj instanceof IndriGraphNode)) return false;
            IndriGraphNode node = (IndriGraphNode) obj;

            return equals(this, node);
        }

        // change here for different flavors of comparison
        private boolean equals(IndriGraphNode fir, IndriGraphNode sec) {
            return fir.text.contains(sec.text) || sec.text.contains(fir.text);
           //  return fir.tagID == sec.tagID;
        }

        // change here for different visualization
        public String toString() {
            return this.text;
            // return this.tagID + "";
        }
    }

    private class SimpleEdge extends IndriGraphEdge {
        public SimpleEdge(String name) {
            this.edgeName = name;
        }

        public String toString() {
            return edgeName;
        }
    }

    private class SimpleNodeFactory extends IndriGraphNodeFactory {
        public IndriGraphNode createEmptyNode() {
            return new SimpleNode();
        }
    }

    private class SimpleEdgeFactory extends IndriGraphEdgeFactory {
        public IndriGraphEdge createEdge(IndriGraphNode src, IndriGraphNode dst) {
            if(src == null || dst == null) return null;
            return new SimpleEdge(src.tagID + "->" + dst.tagID);
        }
    }

    /**
     * sample program for matching
     */
    public void match(Matching<IndriGraphNode, IndriGraphEdge> matching) throws IOException {
        List<Graph<IndriGraphNode, IndriGraphEdge>> patterns = adapter.convert(patternFile);
        Graph<IndriGraphNode, IndriGraphEdge> pattern = patterns.get(0);
        showGraph(pattern, "pattern");

        List<Graph<IndriGraphNode, IndriGraphEdge>> targets = adapter.convert(targetFile);
        System.out.println("load target file completed");

        GraphMatcher<IndriGraphNode, IndriGraphEdge, List<Graph<IndriGraphNode, IndriGraphEdge>>>
                matcher = new ArbitraryGraphMatcher<IndriGraphNode, IndriGraphEdge>(
                                matching
                            );

        int cnt = 1;
        for(Graph<IndriGraphNode, IndriGraphEdge> target : targets) {
            for(Graph<IndriGraphNode, IndriGraphEdge> graph : matcher.match(pattern, target).getMatchResult()) {
                showGraph(graph, "find match " + cnt++ + " from target");
            }
        }
    }

    private void showGraph(Graph<IndriGraphNode, IndriGraphEdge> graph, String title) {
        GraphVisualizer<IndriGraphNode, IndriGraphEdge> gv =
                new LayoutGraphVisualizer<IndriGraphNode, IndriGraphEdge>(title,
                        new ISOMLayout<IndriGraphNode, IndriGraphEdge>(graph));
        gv.show(graph);
    }

    public static void main(String [] args) {
        try {
            IndriGraphMatch matcher = new IndriGraphMatch(
                    "/Users/virgil/Downloads/pattern.tsv",
                    "/Users/virgil/Documents/fall13/Capstone/srl_output.tsv");

            Matching<IndriGraphNode, IndriGraphEdge> matching =
                    new InexactMatching<IndriGraphNode, IndriGraphEdge>();
            matcher.match(matching);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
