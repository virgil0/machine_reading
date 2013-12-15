package edu.cmu.cs.vlis.examples;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Random;

import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;


/**
 * used for evaluating algorithms only. also used for generating the gold standard test set. 
 * @author ke xu
 *
 */
final class SerialGraph extends SparseMultigraph<Integer, Integer> implements java.io.Serializable {
	private static final long serialVersionUID = 1L;
	private static Random ran = new Random();
	
	public static void serialize(SerialGraph serialGraph, String serialFileName) throws IOException {
		FileOutputStream fileout = new FileOutputStream(serialFileName);
		ObjectOutputStream out = new ObjectOutputStream(fileout);
		out.writeObject(serialGraph);
		out.close();
		fileout.close();
	}
	
	public static SerialGraph deserialize(String serialFileName) throws IOException, ClassNotFoundException {
		FileInputStream filein = new FileInputStream(serialFileName);
		ObjectInputStream in = new ObjectInputStream(filein);
		SerialGraph serialGraph = (SerialGraph) (in.readObject());
		in.close();
		filein.close();
		return serialGraph;
	}
	
	/**
	 * 
	 * @param max number of nodes expected (the real number may be less than the expected max number)
	 * @param max number of edges expected (the real number may be less than the expected max number)
	 * @return a single directed graph (connected sparse multiple graph) 
	 */
	public static SerialGraph getRandomGraph(int node, int edge) {
		SerialGraph graph = new SerialGraph();
		
		int e = 0;
		graph.addVertex(0);
		for(int i = 1; i < node; i++) {
			int dst = ran.nextInt(graph.getVertexCount());
			
			// randomly shuffle dst and i(src)
			int src = i;
			int n = ran.nextInt(2);
			if(n == 0) {
				int tmp = src;
				src = dst;
				dst = tmp;
			}
			
			// add egde from src to dst
			graph.addEdge(e++, src, dst, EdgeType.DIRECTED);
		}
				
		for(int i = graph.getEdgeCount(); i < edge; i++) {
			// randomly choose source and destination
			int src = ran.nextInt(node);
			int dst = ran.nextInt(node);
			
			if(!graph.containsEdge(e)) 
				graph.addEdge(e++, src, dst, EdgeType.DIRECTED);
		}
			
		return graph;
	}
}