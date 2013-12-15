package edu.cmu.cs.vlis.matcher;

import java.net.URI;

import edu.cmu.cs.vlis.result.MatchingResult;
import edu.uci.ics.jung.graph.Graph;

public class LargeScaleGraphBatchMatcher<V, E, T> extends GraphMatcher<V, E, T> {
	/** implemetation will be based on Hadoop and HDFS */
	public URI match(URI patternFile, URI targetGraphFile) {
		// TODO 
		return null;
	}

	@Override
    public MatchingResult<T> match(Graph<V, E> pattern, Graph<V, E> target) {
	    // TODO Auto-generated method stub
	    return null;
    }
}
