package edu.cmu.cs.vlis.graph;

/**
 * @deprecated
 * @param <V>
 */
public class AnnotationGraphNode <V> {
	private Annotation annotation;
	private V vertex;

	public AnnotationGraphNode(Annotation annotation, V vertex) {
		this.annotation = annotation;
		this.vertex = vertex;
	}
}
