package edu.cmu.cs.vlis.result;

/**
 * 
 * @author morefree
 *
 * @param <T> type of matching result. 
 *        for 1-vs-1 matching, it might be integer indicating the occurrence of pattern in target graph;
 *        for 1-vs-1 matching, it also might be double which measures the similarity of pattern and 
 *        target (inexact matching);
 *        for n-vs-1 matching, it could be any kind of collection type, which is generated by proper
 *        result combiner. 
 */
public interface MatchingResult<T> {
	public T getMatchResult();
}
