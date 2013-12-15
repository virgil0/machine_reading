package edu.cmu.cs.vlis.examples;

import edu.cmu.cs.vlis.result.MatchingResult;
import edu.cmu.cs.vlis.visual.GraphVisualizer;
import edu.cmu.cs.vlis.visual.LayoutGraphVisualizer;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;

import java.util.List;
import edu.cmu.cs.vlis.matcher.*;
import edu.cmu.cs.vlis.matching.*;

import java.io.*;

/**
 * 
 * Evaluate performance of arbitrary graph matching 
 * @author ke xu
 *
 */
public class EvalArbitraryGraphMatch {
	private static GraphMatcher<Integer, Integer, List<Graph<Integer, Integer>>> matcher
			= new ArbitraryGraphMatcher<Integer, Integer>();
	
	/**
	 * test for newly generated test set. Using SerialGraph to generate test set.
	 * @param patternScale
	 * @param targetScale
	 * @param matchingClass
	 * @throws IllegalAccessException 
	 * @throws InstantiationException 
	 * @throws IOException 
	 */
	
	@SuppressWarnings("unchecked")
	public static void testInexactMatching(int patternScale, int targetScale, 
							String patternSerialFile, String targetSerialFile, 
							Class<?> matchingClass) 
			throws InstantiationException, IllegalAccessException, IOException {
		
		Graph<Integer, Integer> pattern = SerialGraph.getRandomGraph(patternScale, patternScale * 2);
		Graph<Integer, Integer> target = SerialGraph.getRandomGraph(targetScale, targetScale * 2);
				
		System.out.println(pattern.getVertexCount() + " " + pattern.getEdgeCount());
		System.out.println(target.getVertexCount() + " " + target.getEdgeCount());
		
		// store pattern and target into files
		if(patternSerialFile != null) 
			SerialGraph.serialize((SerialGraph) pattern,  patternSerialFile);
		if(targetSerialFile != null) 
			SerialGraph.serialize((SerialGraph) target, targetSerialFile);
		
		long startTime = System.currentTimeMillis();
		
		Matching<Integer, Integer> matching = (Matching<Integer, Integer>) matchingClass.newInstance();
		matching.setPattern(pattern);
		matching.setTarget(target);
		matching.init();
		
        matcher.setMatching(matching);
        MatchingResult<List<Graph<Integer, Integer>>> result = matcher.match(pattern, target);
        
        // use the result graphs to check the correctness of any revised algorithm
        List<Graph<Integer, Integer>> resultGraphs = result.getMatchResult();
        System.out.println("found " + resultGraphs.size() + " results");
        
        long endTime = System.currentTimeMillis();
        System.out.println("running time = " + (endTime - startTime) + "ms");
	}

	@SuppressWarnings("unchecked")
	public static void testExactMatching(int patternScale, int targetScale, 
							String patternSerialFile, String targetSerialFile, 
							Class<?> matchingClass) 
			throws InstantiationException, IllegalAccessException, IOException {
		
		Graph<Integer, Integer> pattern = SerialGraph.getRandomGraph(patternScale, patternScale);
		Graph<Integer, Integer> target = SerialGraph.getRandomGraph(targetScale, targetScale * 2);
				
		System.out.println(pattern.getVertexCount() + " " + pattern.getEdgeCount());
		System.out.println(target.getVertexCount() + " " + target.getEdgeCount());
		
		// the following block ensures each sample matching returns at least one result
		int edgeNum = target.getEdgeCount() + 1;
		for(Integer src : pattern.getVertices()) {
			for(Integer dst : pattern.getNeighbors(src)) {		
				
				if(target.findEdge(src, dst) == null)
					target.addEdge(edgeNum++, src, dst, EdgeType.DIRECTED); 
			}
		}
		
		// store pattern and target into files
		if(patternSerialFile != null) 
			SerialGraph.serialize((SerialGraph) pattern,  patternSerialFile);
		if(targetSerialFile != null) 
			SerialGraph.serialize((SerialGraph) target, targetSerialFile);
		
		long startTime = System.currentTimeMillis();
		
		Matching<Integer, Integer> matching = (Matching<Integer, Integer>) matchingClass.newInstance();
		matching.setPattern(pattern);
		matching.setTarget(target);
		matching.init();
		
        matcher.setMatching(matching);
        MatchingResult<List<Graph<Integer, Integer>>> result = matcher.match(pattern, target);
        
        // use the result graphs to check the correctness of any revised algorithm
        List<Graph<Integer, Integer>> resultGraphs = result.getMatchResult();
        System.out.println("found " + resultGraphs.size() + " results");
        
        long endTime = System.currentTimeMillis();
        System.out.println("running time = " + (endTime - startTime) + "ms");
	}
	
	private static void showGraph(Graph<Integer, Integer> graph, String title) {
		GraphVisualizer<Integer, Integer> gv = new LayoutGraphVisualizer<Integer, Integer>(title,
	            new ISOMLayout<Integer, Integer>(graph));
	    gv.show(graph);
	}
	   
	
	public static void main(String [] args) {
		try {
			int patternScale = 12;
			int targetScale = 2000;
			
			String prefix = "gold-standard/";
			String patternSerialFile = "inexact_" + patternScale + "_" + targetScale + "_pattern.ser";
			String targetScaleFile = "inexact_" + patternScale + "_" + targetScale + "_target.ser";
			testInexactMatching(patternScale, targetScale, 
						prefix+patternSerialFile, prefix+targetScaleFile, 
						InexactMatching.class);	
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
}

