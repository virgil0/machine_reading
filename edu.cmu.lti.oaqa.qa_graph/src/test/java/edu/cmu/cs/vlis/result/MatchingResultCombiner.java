package edu.cmu.cs.vlis.result;

/**
 * @author morefree
 * @param <V>  input type of match result
 * @param <T>  output type (combined type) of match result 
 */
public abstract class MatchingResultCombiner<V, T> implements MatchingResult<T> {
	protected MatchingResult<V> matchResult;
	
	public MatchingResultCombiner(MatchingResult<V> result) {
		this.matchResult = result;
	}

    public T getMatchResult() {
	   return combine(this.matchResult);
    }
	
	public abstract T combine(MatchingResult<V> result);
}
