package edu.cmu.cs.vlis.aggregater;

import edu.cmu.cs.vlis.matcher.GraphMatcher;
import edu.cmu.cs.vlis.result.MatchingResult;
import edu.uci.ics.jung.graph.Graph;

/**
 *
 * @deprecated
 *
 * @author morefree
 *
 * @param <V> type of graph nodes
 * @param <E> type of graph edges
 * @param <IN> type of match result generated by matcher
 * @param <OUT> type of result generated by combining match results from matcher (with type IN)
 */
public abstract class InexactMatchingAggregater<V, E, IN, OUT> {
	protected GraphMatcher<V, E, IN> matcher;
	
	public InexactMatchingAggregater(GraphMatcher<V, E, IN> matcher) {
		this.matcher = matcher;
	}
	
	public abstract MatchingResult<OUT> aggregatedMatch(Graph<V, E> pattern, Graph<V, E> target);
}
