package edu.cmu.cs.vlis.result;


import javax.swing.text.AbstractDocument.Content;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;

/**
 * a generic result type of all matching algorithms
 * 
 * @author morefree
 *
 * @param <V> type of vertex, typically an integer
 * @param <E> type of edge, typically an integer
 * @param <RV> type of MatchingResult between vertexes, typically a double 
 * @param <RE> type of MatchingResult between edges, typically a double
 */
public class MatchingGraph <V, E, RV, RE> {
	private Graph<MatchingElement<V, RV>, MatchingElement<E, RE>> matchingGraph;
	
	public MatchingGraph() {
		matchingGraph = new SparseMultigraph<MatchingElement<V, RV>, MatchingElement<E, RE>>();
	}
	
	public Graph<MatchingElement<V, RV>, MatchingElement<E, RE>> getContent() {
		return matchingGraph;
	}
	
	public String toString() {
		return getContent().toString();
	}
	
	public int hashCode() {
		return getContent().hashCode();
	}
	
	public boolean equals(Object obj) {
		return getContent().equals(obj);
	}
}
