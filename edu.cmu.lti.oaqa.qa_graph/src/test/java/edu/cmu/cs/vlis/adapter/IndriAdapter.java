package edu.cmu.cs.vlis.adapter;

import java.util.*;
import java.io.*;

import edu.uci.ics.jung.graph.*;

/**
 * Created with IntelliJ IDEA.
 * User: morefree
 * Date: 11/18/13
 * Time: 3:42 PM
 * To change this template use File | Settings | File Templates.
 *
 * for output
 1	TAG	0	A0	0	8	0	1	The film
 1	TAG	1	V	9	6	0	0	speaks
 1	TAG	2	A3	16	10	0	1	for itself
 1	TAG	3	A1	28	166	0	1	The only thing missing...
 1	TAG	4	A1	28	14	0	5	The only thing
 1	TAG	5	V	43	7	0	0	missing


 we can build two annotation graphs,

                V-speaks
 /                 |                  \
 A0-film      A3-for itself        A1- The only thing missing...


 V-missing
    |
 A1- the only thing

 <DocNo> "TAG" <tagID> <tagValue> <offset> <length> <parentTagID> <piece of text>
 1	TAG	0	A0	0	8	0	1	The film
 */
public class IndriAdapter implements Adapter {
    private IndriGraphNodeFactory nodeFactory;
    private IndriGraphEdgeFactory edgeFactory;

    public IndriAdapter(IndriGraphNodeFactory nodeFactory, IndriGraphEdgeFactory edgeFactory) {
        this.nodeFactory = nodeFactory;
        this.edgeFactory = edgeFactory;
    }

    public List<Graph<IndriGraphNode, IndriGraphEdge>> convert(String filename) throws IOException {
        List<IndriGraph> bigGraphs = createGraphs(filename);
        List<Graph<IndriGraphNode, IndriGraphEdge>> smallGraphs =
                new ArrayList<Graph<IndriGraphNode, IndriGraphEdge>>();
        for(IndriGraph bigGraph : bigGraphs) {
            for(Graph<IndriGraphNode, IndriGraphEdge> g : bigGraph.getConnectedComponents())
                smallGraphs.add(g);
        }

        return smallGraphs;
    }
    
    public List<Graph<IndriGraphNode, IndriGraphEdge>> convert(List<String> lines) {
        List<IndriGraph> bigGraphs = createGraphs(lines);
        List<Graph<IndriGraphNode, IndriGraphEdge>> smallGraphs =
                new ArrayList<Graph<IndriGraphNode, IndriGraphEdge>>();
        for(IndriGraph bigGraph : bigGraphs) {
            for(Graph<IndriGraphNode, IndriGraphEdge> g : bigGraph.getConnectedComponents())
                smallGraphs.add(g);
        }

        return smallGraphs;
    }

    private List<IndriGraph> createGraphs(String filename) throws IOException {
        List<IndriGraph> graphs = new ArrayList<IndriGraph>();

        BufferedReader reader = new BufferedReader(new FileReader(new File(filename)));
        String line = null;
        int prevDocID = -1;
        IndriGraph graph = null;

        while((line = reader.readLine()) != null) {
            IndriGraphNode info = nodeFactory.createEmptyNode();
            int docID = buildInfo(line, info);

            if(docID == prevDocID)
                graph.add(info);
            else {
                if(graph != null) {
                    graph.buildGraph();
                    graphs.add(graph);
                }

                graph = new IndriGraph(docID, edgeFactory);
                prevDocID = docID;
                graph.add(info);
            }
        }

        if(graph != null) {
            graph.buildGraph();
            graphs.add(graph);
        }

        reader.close();

        return graphs;
    }
    
    private List<IndriGraph> createGraphs(List<String> lines) {
        List<IndriGraph> graphs = new ArrayList<IndriGraph>();

        int prevDocID = -1;
        IndriGraph graph = null;

        for(String line : lines) {
            IndriGraphNode info = nodeFactory.createEmptyNode();
            int docID = buildInfo(line, info);

            if(docID == prevDocID)
                graph.add(info);
            else {
                if(graph != null) {
                    graph.buildGraph();
                    graphs.add(graph);
                }

                graph = new IndriGraph(docID, edgeFactory);
                prevDocID = docID;
                graph.add(info);
            }
        }

        if(graph != null) {
            graph.buildGraph();
            graphs.add(graph);
        }

        return graphs;
    }
    
    
    private int buildInfo(String line, IndriGraphNode info) {
        String [] tokens = line.split("\t");
        if(tokens.length < 9) return -1;

        info.docID = Integer.parseInt(tokens[0]);
        info.tagID = Integer.parseInt(tokens[2]);
        info.tagValue = tokens[3];
        info.offset = Integer.parseInt(tokens[4]);
        info.length = Integer.parseInt(tokens[5]);
        info.parentTagID = Integer.parseInt(tokens[7]);
        info.text = tokens[8];

        return Integer.parseInt(tokens[0]);
    }
}
