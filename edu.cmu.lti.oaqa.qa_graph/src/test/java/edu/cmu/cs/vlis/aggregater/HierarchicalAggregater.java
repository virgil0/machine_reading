package edu.cmu.cs.vlis.aggregater;

import edu.cmu.cs.vlis.matcher.GraphMatcher;
import edu.cmu.cs.vlis.result.MatchingResult;
import edu.uci.ics.jung.graph.Graph;

/**
 * @deprecated
 * @param <V>
 * @param <E>
 * @param <IN>
 * @param <OUT>
 */
public class HierarchicalAggregater<V,E,IN,OUT> extends InexactMatchingAggregater<V,E,IN,OUT> {
	
	public HierarchicalAggregater(GraphMatcher<V, E, IN> matcher) {
	    super(matcher);
    }

	@Override
    public MatchingResult<OUT> aggregatedMatch(Graph<V, E> pattern,
            Graph<V, E> target) {
	    // TODO Auto-generated method stub
	    return null;
    }

}
