package edu.cmu.lti.oaqa.graph.canonicalTest;

import java.util.Map;
import java.util.Map.Entry;

import com.tinkerpop.blueprints.KeyIndexableGraph;
import com.tinkerpop.blueprints.Vertex;
import com.tinkerpop.blueprints.impls.rexster.RexsterGraph;

import edu.cmu.lti.oaqa.graph.canonical.CanonicalQuery;
import edu.cmu.lti.oaqa.graph.canonical.GraphSearch;
import edu.cmu.lti.oaqa.graph.canonical.QueryHandler;
import edu.cmu.lti.oaqa.graph.canonical.QueryResult;
import edu.cmu.lti.oaqa.graph.core.tools.Search;
import edu.cmu.lti.oaqa.graph.exceptions.KBException;

public class QueryHandlerTest {

	/**
	 * @param args
	 * @throws KBException 
	 */
	public static void main(String[] args) throws KBException {
		GraphSearch searchClass = new ExampleSearchClass();
		CanonicalQuery query=createNewQuery();
		RexsterGraph graph = new RexsterGraph("http://peace.isri.cs.cmu.edu:8182/graphs/CMUGraph");
		QueryHandler hanlder = new QueryHandler(graph,searchClass);
		QueryResult results = hanlder.issueQuery(query);
		
		for(Vertex v:results.getVertices()){
			String info = v.toString();
			System.out.println(info);
		}
		for(String answer :results.getStrings()){
			System.out.println("answer: "+answer);
			
		}
	}

	private static CanonicalQuery createNewQuery() throws KBException {
		/*
		CanonicalChunk chunk1 = new CanonicalChunk(Verb.TEXT,"alan_black");
		CanonicalChunk chunk2 = new CanonicalChunk(Verb.TYPE,"person");
		CanonicalChunk chunk3 = new CanonicalChunk(Verb.TRAVERSE,null);
		CanonicalChunk chunk4 = new CanonicalChunk(Verb.TYPE,"email");
		
		List<CanonicalChunk> chunks = new LinkedList<CanonicalChunk>();
		chunks.add(chunk1);
		chunks.add(chunk2);
		chunks.add(chunk3);
		chunks.add(chunk4);
		
		return new CanonicalQuery(chunks);
		*/
		String query = "[[\"Text\",\"ALAN_BLACK\"],[\"Type\",\"person\"],[\"Traverse\",null],[\"Type\",\"email\"]]";
		return new CanonicalQuery(query);
		
	}
	
	
}

class ExampleSearchClass implements GraphSearch{
	
	private KeyIndexableGraph graph;
	
	public Iterable<Vertex> textSearch(String searchString) {
		// TODO add more indexes
		return Search.searchVertices(graph,searchString, 100, "name");
	}

	public Iterable<Vertex> typeSearch(String searchString) {
		return Search.searchVertices(graph,searchString, 100, "type");
	}

	public void setGraph(KeyIndexableGraph graph) {
		this.graph=graph;
	}
	
}
