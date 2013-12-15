package edu.cmu.cs.vlis.examples;

import java.io.IOException;
import java.util.List;

import edu.cmu.cs.vlis.adapter.IndriGraphEdge;
import edu.cmu.cs.vlis.adapter.IndriGraphNode;
import edu.cmu.cs.vlis.matcher.ArbitraryGraphMatcher;
import edu.cmu.cs.vlis.matcher.GraphMatcher;
import edu.cmu.cs.vlis.matching.InexactMatching;
import edu.cmu.cs.vlis.matching.Matching;
import edu.uci.ics.jung.graph.Graph;

/**
 * Evaluate performance of Indri Graph Matching
 * 
 * @author ke xu
 *
 */
public class EvalIndriGraphMatch {
	
	private static class IndriMatchTester extends IndriGraphMatch {
		public IndriMatchTester(String patternFile, String targetFile) {
			super(patternFile, targetFile);
		}
		
		public void match(Matching<IndriGraphNode, IndriGraphEdge> matching)
				throws IOException {
			List<Graph<IndriGraphNode, IndriGraphEdge>> patterns = adapter
					.convert(patternFile);
			Graph<IndriGraphNode, IndriGraphEdge> pattern = patterns.get(0);
			
			List<Graph<IndriGraphNode, IndriGraphEdge>> targets = adapter
					.convert(targetFile);
			System.out.println("load target file completed");
			
			GraphMatcher<IndriGraphNode, IndriGraphEdge, List<Graph<IndriGraphNode, IndriGraphEdge>>> matcher = 
					new ArbitraryGraphMatcher<IndriGraphNode, IndriGraphEdge>(matching);
		}
	}
	
	public static void main(String [] args) {
		 try {
	            IndriGraphMatch matcher = new IndriMatchTester(
	                    "/Users/morefree/Downloads/pattern2.tsv",
	                    "/Users/morefree/Downloads/target-1.tsv");
	            
	            Matching<IndriGraphNode, IndriGraphEdge> matching =
	                    new InexactMatching<IndriGraphNode, IndriGraphEdge>();
	            
	            long startTime = System.currentTimeMillis();
	            matcher.match(matching);
	        	long endTime = System.currentTimeMillis();
				System.out.println("running time = " + (endTime - startTime) + "ms");
	            
	        } catch(Exception e) {
	            e.printStackTrace();
	        }
	}
}
