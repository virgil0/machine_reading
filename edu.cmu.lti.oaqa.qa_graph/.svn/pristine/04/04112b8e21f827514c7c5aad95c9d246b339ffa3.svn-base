package edu.cmu.lti.oaqa.graph.canonical;

import com.tinkerpop.blueprints.KeyIndexableGraph;

import edu.cmu.lti.oaqa.graph.core.tools.CoreGraph;
import edu.cmu.lti.oaqa.graph.exceptions.KBException;



public class KGraph extends CoreGraph {

	private KeyIndexableGraph graph;
	private GraphSearch searchClass;
	private CustomNouns customizer;
	
	//constructors
	public KGraph(KeyIndexableGraph graph,GraphSearch searchClass, CustomNouns customizer){
		super(graph);
		this.graph=graph;
		this.searchClass=searchClass;
		this.searchClass.setGraph(this.graph);
		this.customizer=customizer;
	}
	
	public KGraph(KeyIndexableGraph graph,GraphSearch searchClass){
		super(graph);
		this.graph=graph;
		this.searchClass=searchClass;
		this.searchClass.setGraph(this.graph);
	}
	
	public KGraph(KeyIndexableGraph graph){
		super(graph);
		this.graph=graph;
	}

	
	//getter and setters
	public GraphSearch getSearchClass() {
		return searchClass;
	}

	public void setSearchClass(GraphSearch searchClass) {
		this.searchClass = searchClass;
		this.searchClass.setGraph(this.graph);
	}

	public CustomNouns getCustomizer() {
		return customizer;
	}

	public void setCustomizer(CustomNouns customizer) {
		this.customizer = customizer;
	}
	
	
	//new methods
	public QueryResult issueQuery(CanonicalQuery query) throws KBException{
		if (this.searchClass==null) throw new KBException("No GraphSearch class has been submitted " +
				"to this KnowledgeGraph. Run setSearchClass to submit one.");
		QueryHandler handler = new QueryHandler(this.graph,this.searchClass);
		return handler.issueQuery(query);
	}
	
}
