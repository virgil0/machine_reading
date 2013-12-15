package edu.cmu.lti.oaqa.graph.canonical;

import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Vertex;

public interface GraphSearch {
	//the point of this interface is so that each Graph that will be a kb can have a unique search function
	//that can be utilized by the KnowledgeBase
	public Iterable<Vertex> textSearch(String searchString);
	public Iterable<Vertex> typeSearch(String searchString);
	public void setGraph(KeyIndexableGraph graph); //use this to set the graph that this will search through

}
