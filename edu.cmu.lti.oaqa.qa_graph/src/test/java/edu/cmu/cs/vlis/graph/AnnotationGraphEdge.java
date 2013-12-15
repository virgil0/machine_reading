package edu.cmu.cs.vlis.graph;

/**
 * @deprecated
 * @param <E>
 */
public class AnnotationGraphEdge <E> {
	private Annotation annotation;
	private E edge;
	
	public AnnotationGraphEdge(Annotation annotation, E edge) {
		this.annotation = annotation;
		this.edge = edge;
	}
}
