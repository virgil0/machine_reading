package edu.cmu.cs.vlis.graph.filter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edu.cmu.cs.vlis.util.Utils;
import edu.uci.ics.jung.graph.Graph;

/**
 * @deprecated
 *
 * decompose the original graph based on number of vertexes.
 * this class can also be integrated into JUNG edu.uci.ics.jung.algorithms.filters
 * @author morefree
 *
 * @param <V>
 * @param <E>
 */
public class DecompositionFilter <V, E> {
	
	public Map<Integer, Collection<Graph<V, E>>> transform(Graph<V, E> graph, 
														   int minNumOfVertex,
                                                           int maxNumOfVertex) {
		Map<Integer, Collection<Graph<V, E>>> subGraphs =
				new HashMap<Integer, Collection<Graph<V, E>>>();
		
		for(int vertex = minNumOfVertex; vertex <= maxNumOfVertex; vertex++) {
			subGraphs.put(vertex, transform(graph, vertex));
		}
		
		return subGraphs;
	}
	
	@SuppressWarnings("unchecked")
	private Collection<Graph<V, E>> transform(Graph<V, E> graph, int vertex) {
		Collection<Graph<V, E>> subGraphs = new ArrayList<Graph<V, E>>();
		for(V start : graph.getVertices()) { 
			try {
				Graph<V, E> subGraph  = (Graph<V, E>) graph.getClass().newInstance();
                subGraph.addVertex(start);
                dfs(graph, start, vertex, new HashSet<V>(), subGraph, subGraphs);
			} catch (InstantiationException e) {
				e.printStackTrace(System.err);
			} catch (IllegalAccessException e) {
				e.printStackTrace(System.err);
			}
		}
		
		return subGraphs;
	}
	
	private void dfs(Graph<V, E> graph,
                     V start,
                     int maxDepth,
                     Set<V> visited,
					 Graph<V, E> subGraph,
					 Collection<Graph<V, E>> subGraphs)
								throws InstantiationException, IllegalAccessException {

        visited.add(start);

		if(maxDepth == 1) {
			subGraphs.add(Utils.shallowCopy(subGraph));
		} else {

            for(E outEdge : graph.getOutEdges(start)) {
                V end = graph.getDest(outEdge);
                if(visited.contains(end)) continue;

                subGraph.addVertex(end);
                subGraph.addEdge(outEdge, start, end, graph.getEdgeType(outEdge));
                dfs(graph, end, maxDepth - 1, visited, subGraph, subGraphs);
                subGraph.removeEdge(outEdge);
                subGraph.removeVertex(end);
            }
        }

        visited.remove(start);
	}
}
