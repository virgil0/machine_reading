package edu.cmu.cs.vlis.matcher;

import edu.cmu.cs.vlis.label.AbstractLabelComparator;
import edu.cmu.cs.vlis.result.MatchingResult;
import edu.uci.ics.jung.graph.Graph;
import edu.cmu.cs.vlis.matching.Matching;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;

import java.util.*;

/**
 * it matches pattern with target, and return a list of matching results,
 * where a matching result is a sub-graph of target, which can be fully or
 * partially matches with pattern (depending on which matching algorithm is
 * passed into the matcher when creating a matcher instance).
 *
 * @param <V>
 * @param <E>
 */
public final class ArbitraryGraphMatcher<V, E> extends GraphMatcher<V, E, List<Graph<V, E>>> {
    /**
     * @param matching a user-specified matching algorithm
     *                 all implemented Matching classes are located in
     *                 edu.cmu.cs.vlis.matching
     */
    public ArbitraryGraphMatcher(Matching<V, E> matching) {
        this.matching = matching;
    }

    public ArbitraryGraphMatcher(Matching<V, E> matching, AbstractLabelComparator comparator) {
        this.matching = matching;
        this.labelComparator = comparator;
    }

    /**
     * set a matching algorithm using setMatching() is required before invoking the
     * method match(Graph, Graph)
     */
    public ArbitraryGraphMatcher() {

    }

	@Override
    public MatchingResult<List<Graph<V, E>>> match(Graph<V, E> pattern, Graph<V, E> target) {
        matching.setPattern(pattern);
        matching.setTarget(target);

        List<Map<V, V>> mappings = matching.match();
        this.mappings = mappings;

        List<Graph<V, E>> graphs = new ArrayList<Graph<V, E>>();
        for(Map<V, V> mapping : mappings)
            graphs.add(buildGraph(mapping, target));

        GraphMatchingResult graphMatchingResult = new GraphMatchingResult();
        graphMatchingResult.graphs = graphs;
        return graphMatchingResult;
    }

    private Graph<V, E> buildGraph(Map<V, V> mapping, Graph<V, E> target) {
        Graph<V, E> graph = new SparseMultigraph<V, E>();
        Set<V> vertex = new HashSet<V>();
        for(V v : mapping.values()) vertex.add(v);

        for(V v : vertex) {
            if(!graph.containsVertex(v))
                graph.addVertex(v);

            for(E e : target.getOutEdges(v)) {
                V dst = target.getDest(e);
                if(vertex.contains(dst)) {
                    if(!graph.containsVertex(dst))
                        graph.addVertex(dst);
                    graph.addEdge(e, v, dst, EdgeType.DIRECTED);
                }
            }
        }

        return graph;
    }

    private class GraphMatchingResult implements MatchingResult<List<Graph<V, E>>> {
        List<Graph<V, E>> graphs;

        public List<Graph<V, E>> getMatchResult() {
            return graphs;
        }
    }
		
}
