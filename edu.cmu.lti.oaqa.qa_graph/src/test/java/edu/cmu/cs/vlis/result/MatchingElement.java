package edu.cmu.cs.vlis.result;

import edu.cmu.cs.vlis.util.Utils.Pair;

/**
 * basic matching element of MatchingGraph
 * @author morefree
 *
 * @param <E>  matching element type. for vertex matching, it is the type of 
 * 				vertexes; for edge matching, it is the type of edges
 * @param <T>  similarity type between two matching elements. typically a double value
 */
public class MatchingElement <E, T>{
	private ElementPair elementPair;
	private MatchingResult<T> similarity;
	
	public MatchingElement(E patternElement, E targetElement, MatchingResult<T> similarity) {
		this.elementPair = new ElementPair(patternElement, targetElement);
		this.similarity = similarity;
	}
	
	public void setPatternElement(E patternElement) {
		elementPair.setPatternElement(patternElement);
	}
	
	public void setTargetElement(E targetElement) {
		elementPair.setTargetElement(targetElement);
	}
	
	public void setMatchingResult(MatchingResult<T> result) {
		similarity = result;
	}
	
	public E getPatternElement() {
		return elementPair.getPatternElement();
	}
	
	public E getTargetElement() {
		return elementPair.getTargetElement();
	}
	
	public MatchingResult<T> getMatchingResult() {
		return similarity;
	}
	
	public int hashCode() {
		return elementPair.hashCode() * 37 + similarity.hashCode();
	}
	
	@SuppressWarnings("unchecked")
	public boolean equals(Object obj) {
		if(!(obj instanceof MatchingElement)) return false;
		MatchingElement<E, T> res = (MatchingElement<E, T>) obj;
		return elementPair.equals(res.elementPair) && similarity.equals(res.similarity);
	}
	
	public String toString() {
		return "Matching :  "
				+ "pattern element = " + getPatternElement() + ", "
				+ "target element = " + getTargetElement() + ", " 
				+ "similarity score = " + similarity;
	}
	
	private class ElementPair extends Pair<E, E> {
		public ElementPair(E key, E value) {
			super(key, value);
		}
		
		public void setPatternElement(E patternElement) {
			setKey(patternElement);
		}
		
		public void setTargetElement(E targetElement) {
			setValue(targetElement);
		}
		
		public E getPatternElement() {
			return getKey();
		}
		
		public E getTargetElement() {
			return getValue();
		}
	}
}
