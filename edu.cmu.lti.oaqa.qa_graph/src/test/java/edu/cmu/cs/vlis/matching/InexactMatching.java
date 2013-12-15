package edu.cmu.cs.vlis.matching;

import java.util.*;

import edu.cmu.cs.vlis.util.Utils;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;

/**
 * a.k.a : inexact matching algorithm
 * It extends the VF2 algorithm (for exact matching), and return
 * a MatchingGraph as its matching result.
 *
 * the matching relies on equals() on vertex type V
 *
 * @author morefree
 *
 */

public class InexactMatching<V, E> extends Matching<V, E> {
    public InexactMatching(Graph<V, E> pattern, Graph<V, E> target) {
        super(pattern, target);
    }

    public InexactMatching() {

    }

    public List<Map<V, V>> match() {
        mappings = new ArrayList<Map<V, V>>();
        int nEdge = pattern.getEdgeCount();
        for(int i = 0; i < nEdge; i++) {
            matchSubGraph(i);

            // found the largest matched sub-graph
            if(mappings.size() != 0)
                break;
        }

        return mappings;
    }

    // delete nDeletedEdges edges
    // for each connected components
    // do an exact matching
    private void matchSubGraph(int nDelete) {
        List<E> allEdges = Utils.<E> toList(pattern.getEdges());
        List<List<Integer>> deleteGroups = Utils.<E> combinations(allEdges, nDelete);

        for(List<Integer> deleteGroup : deleteGroups) {
            List<DirectedEdge> backup = buildDirectEdges(deleteGroup, allEdges, pattern);
            deleteEdges(backup, pattern);

            // for each connected components, do the exact matching and print the result
            ExactMatching matcher = new ExactMatching(pattern, target);
            List<Map<V, V>> subGraphMappings = matcher.match();
            mappings.addAll(subGraphMappings);

            addBackEdges(pattern, backup);
        }
    }

    private List<DirectedEdge> buildDirectEdges(List<Integer> edgeIndex,
                                                List<E> edges,
                                                Graph<V, E> graph) {
        List<DirectedEdge> directedEdges = new ArrayList<DirectedEdge>();

        for(Integer i : edgeIndex) {
            E edge = edges.get(i);
            directedEdges.add(new DirectedEdge(edge, graph.getSource(edge), graph.getDest(edge)));
        }

        return directedEdges;
    }

    private void deleteEdges(final List<DirectedEdge> edges, Graph<V, E> graph) {
        for(DirectedEdge edge : edges) {
            if(graph.containsEdge(edge.name))
                graph.removeEdge(edge.name);

            deleteIsolatedVertex(edge.from, graph);
            deleteIsolatedVertex(edge.to, graph);
        }
    }

    private void deleteIsolatedVertex(V vertex, Graph<V, E> graph) {
        Collection<E> outEdges = graph.getOutEdges(vertex);
        if(outEdges == null || outEdges.size() > 0) return;

        Collection<E> inEdges = graph.getInEdges(vertex);
        if(inEdges.size() == 0)
            graph.removeVertex(vertex);
    }

    private void addBackEdges(Graph<V, E> graph, List<DirectedEdge> edges) {
        for(DirectedEdge edge : edges) {
            if(!graph.containsVertex(edge.from))
                graph.addVertex(edge.from);
            if(!graph.containsVertex(edge.to))
                graph.addVertex(edge.to);
            if(!graph.containsEdge(edge.name))
                graph.addEdge(edge.name, edge.from, edge.to, EdgeType.DIRECTED);
        }
    }

    private class DirectedEdge {
        E name;
        V from, to;
        DirectedEdge(E name, V from, V to) {
            this.name = name;
            this.from = from;
            this.to = to;
        }
    }
}
