package edu.cmu.cs.vlis.adapter;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.graph.util.EdgeType;
import java.util.ArrayList;
import java.util.List;

/**
 * used for work with Titan only.
 * see edu.cmu.cs.vlis.examples.IndriGraphMatchWithTitan for sample usage.
 * see Fei's code for more info.
 */

public class TitanAdapter implements Adapter {
    private IndriGraphNodeFactory nodeFactory;
    private IndriGraphEdgeFactory edgeFactory;

    public TitanAdapter(IndriGraphNodeFactory nodeFactory, IndriGraphEdgeFactory edgeFactory) {
        this.nodeFactory = nodeFactory;
        this.edgeFactory = edgeFactory;
    }

    public List<Graph<IndriGraphNode, IndriGraphEdge>> convert(List<Graph<Vertex, Object>> graphs) {
        List<Graph<IndriGraphNode, IndriGraphEdge>> convertedGraphs =
                new ArrayList<Graph<IndriGraphNode, IndriGraphEdge>>();
        for(Graph<Vertex, Object> graph : graphs)
            convertedGraphs.add(convert(graph));

        return convertedGraphs;
    }

    public Graph<IndriGraphNode, IndriGraphEdge> convert(Graph<Vertex, Object> graph) {
        Graph<IndriGraphNode, IndriGraphEdge> convertedGraph =
                new SparseMultigraph<IndriGraphNode, IndriGraphEdge>();

        for(Vertex src : graph.getVertices()) {
            IndriGraphNode srcNode = convert(src);
            if(!convertedGraph.containsVertex(srcNode))
                convertedGraph.addVertex(srcNode);

            for(Object outEdge : graph.getOutEdges(src)) {
                Vertex dst = graph.getDest(outEdge);
                IndriGraphNode dstNode = convert(dst);
                if(!convertedGraph.containsVertex(dstNode))
                    convertedGraph.addVertex(dstNode);

                IndriGraphEdge edge = edgeFactory.createEdge(srcNode, dstNode);
                if(!convertedGraph.containsEdge(edge))
                    convertedGraph.addEdge(edge, srcNode, dstNode, EdgeType.DIRECTED);
            }
        }

        return convertedGraph;
    }

    public IndriGraphNode convert(Vertex vertex) {
        IndriGraphNode node = nodeFactory.createEmptyNode();
        node.docID = (Integer) vertex.getProperty("DocNo");
        node.tagID = (Integer) vertex.getProperty("tagID");
        node.tagValue = (String) vertex.getProperty("tagValue");
        node.offset = (Integer) vertex.getProperty("offset");
        node.length = (Integer) vertex.getProperty("length");
        node.parentTagID = (Integer) vertex.getProperty("parentTagID");
        node.text = (String) vertex.getProperty("pieceOfText");

        return node;
    }
}
