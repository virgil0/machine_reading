package edu.cmu.cs.vlis.graph;

/**
 * @deprecated
 * UIMA compatible annotation class
 */
public abstract class Annotation {
	/** for arbitrary graph, the label could be the index of vertex number, 
	 *  for annotation graph, the value of label is defined by the type system */
	protected int label;
	
	public int getLabel() { return label; }
}
