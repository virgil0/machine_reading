package edu.cmu.cs.vlis.adapter;

import java.util.*;
import edu.uci.ics.jung.graph.*;
import edu.uci.ics.jung.graph.util.EdgeType;

/**
 an IndriGraph object contains the graph structure represented by a
 given document, i.e., all graph nodes in this object come from the
 same document. The graph may contain multiple connected components.

 An example :
 Given this input file,
 1	TAG	0	A0	0	8	0	1	The film
 1	TAG	1	V	9	6	0	0	speaks
 1	TAG	2	A3	16	10	0	1	for itself
 1	TAG	3	A1	28	166	0	1	The only thing missing...
 1	TAG	4	A1	28	14	0	5	The only thing
 1	TAG	5	V	43	7	0	0	missing

 where each line represents
 <DocNo> "TAG" <tagID> <tagValue> <offset> <length> <parentTagID> <piece of text>
 1	     TAG	0	     A0	         0	     8		   1	         The film


 an IndriGraph object for this input file contains two annotation graphs,

                V-speaks
 /                 |                  \
 A0-film      A3-for itself        A1- The only thing missing...


    V-missing
       |
 A1- the only thing

 The method getConnectedComponents() is used to return these two graphs as a graph list.
 *
 */

class IndriGraph {
    /** mandatory document id */
    private int docID;

    /** mapping tagID to corresponding attributes.
     * the key is tagID, the value is an IndriGraphNode object
     * corresponding to the tagID. */
    private Map<Integer, IndriGraphNode> elements;

    /** graph structure composed by all elements.
     * the node type is Integer (tagID), the edge type
     * is IndriGraphEdge object. Detailed information
     * about a certain tagID can be acquired by calling
     * the get(int tagID) method. */
    private Graph<Integer, IndriGraphEdge> graph;

    /** used to generate an edge given two nodes */
    private IndriGraphEdgeFactory edgeFactory;

    /** by default, it creates a SparseGraph */
    public IndriGraph(int docID, IndriGraphEdgeFactory edgeFactory) {
        this.docID = docID;
        this.edgeFactory = edgeFactory;
        this.elements = new HashMap<Integer, IndriGraphNode>();
        this.graph = new SparseGraph<Integer, IndriGraphEdge>();
    }

    public IndriGraph(int docID, IndriGraphEdgeFactory edgeFactory,
                      Class<? extends Graph<Integer, IndriGraphEdge>> graphType)
            throws InstantiationException, IllegalAccessException
    {
        this.docID = docID;
        this.edgeFactory = edgeFactory;
        this.elements = new HashMap<Integer, IndriGraphNode>();
        this.graph = (Graph<Integer, IndriGraphEdge>) graphType.newInstance();
    }

    /** return document ID represented by the IndriGraph object */
    public int getDocID() {
        return docID;
    }

    /** add a new node to current graph structure */
    public void add(IndriGraphNode element) {
        elements.put(element.tagID, element);
    }

    /** return IndriGraphNode object for a given tag id */
    public IndriGraphNode get(int tagID) {
        return elements.get(tagID);
    }

    /** return number of nodes, which equals to the number of different tag ids */
    public int size() {
        return elements.size();
    }

    /** return all tag ids contained in the graph structure */
    public int [] getTagIDs() {
        int [] tags = new int [elements.size()];
        int idx = 0;
        for(Integer tag : elements.keySet())
            tags[idx++] = tag;
        return tags;
    }

    /** return all IndriGraphNode contained in the graph structure */
    public IndriGraphNode [] getNodes() {
        IndriGraphNode [] nodes = new IndriGraphNode[elements.size()];
        int idx = 0;
        for(IndriGraphNode node : elements.values())
            nodes[idx++] = node;
        return nodes;
    }

    /** return all edges from src to dst, the returned set may contain null */
    public Collection<IndriGraphEdge> getEdge(int src, int dst) {
        return graph.findEdgeSet(src, dst);
    }

    /** build a large graph which may contain multiple connected components.
     *  This method should only be invoked after all IndriGraphNode elements are added
     *  using add(IndriGraphNode) method.
     *  invoking this method will erase previous graph structure and rebuild a new
     *  graph structure based on current IndriGraphNodes */
    public void buildGraph() {
        for(Map.Entry<Integer, IndriGraphNode> entry : elements.entrySet()) {
            IndriGraphNode element = entry.getValue();

            if(!graph.containsVertex(element.tagID))
                graph.addVertex(element.tagID);
            if(!graph.containsVertex(element.parentTagID))
                graph.addVertex(element.parentTagID);

            // do not adding edge for vertex without any parent node
            if(hasNoParent(element)) continue;

            // create a directed edge using user-specified edge factory
            IndriGraphEdge edge =
                    edgeFactory.createEdge(elements.get(element.parentTagID), elements.get(element.tagID));

            if(edge != null && !graph.containsEdge(edge))
                graph.addEdge(edge, element.parentTagID, element.tagID, EdgeType.DIRECTED);
        }
    }

    private boolean hasNoParent(IndriGraphNode info) {
        return !elements.containsKey(0) && info.parentTagID == 0;
    }

    /**
     * @return connected components of current graph structure.
     */
    public List<Graph<IndriGraphNode, IndriGraphEdge>> getConnectedComponents() {
        // map tagID to label of connected component
        Map<Integer, Integer> labels = new HashMap<Integer, Integer>();
        for(Integer tag : elements.keySet())
            labels.put(tag, -1);

        // relabel each node. nodes with the same label fall into the same connected component
        int curLabel = 0;
        Map<Integer, Set<Integer>> undirectedGraph = deriveUndirectedGraph(graph);
        for(Integer tag : elements.keySet())
            setLabel(tag, labels, curLabel++, undirectedGraph);


        // collect all tags for each label
        // key = label , value = set of tags belonging to that label
        Map<Integer, Set<Integer>> components = new HashMap<Integer, Set<Integer>>();
        for(Map.Entry<Integer, Integer> entry : labels.entrySet()) {
            int tag = entry.getKey();
            int label = entry.getValue();

            Set<Integer> set = components.get(label);
            if(set == null) {
                set = new HashSet<Integer>();
                set.add(tag);
                components.put(label, set);
            } else
                set.add(tag);
        }

        // build all connected components based on labels
        List<Graph<IndriGraphNode, IndriGraphEdge>> connectedComponents =
                new ArrayList<Graph<IndriGraphNode, IndriGraphEdge>>();
        // tags that are already added into any connected component
        Set<Integer> used = new HashSet<Integer>();

        for(Set<Integer> tagSet : components.values()) {
            Graph<IndriGraphNode, IndriGraphEdge> connectedComponent =
                    new SparseGraph<IndriGraphNode, IndriGraphEdge>();
            for(Integer tag : tagSet) {
                IndriGraphNode node = elements.get(tag);

                // add current node
                if(!used.contains(tag)) {
                    used.add(tag);
                    connectedComponent.addVertex(node);
                }

                // add the children of current node and edges from current node to
                // its children
                for(Integer childTag : graph.getSuccessors(tag)) {
                    IndriGraphNode childNode = elements.get(childTag);
                    if(!used.contains(childTag)) {
                        used.add(childTag);
                        connectedComponent.addVertex(childNode);
                    }
                    for(IndriGraphEdge edge : graph.findEdgeSet(tag, childTag)) {
                        if(edge != null)
                            connectedComponent.addEdge(edge, node, childNode, EdgeType.DIRECTED);
                    }
                }
            }

            connectedComponents.add(connectedComponent);
        }

        return connectedComponents;
    }

    private Map<Integer, Set<Integer>> deriveUndirectedGraph(Graph<Integer, IndriGraphEdge> directedGraph) {
        Map<Integer, Set<Integer>> undirectedGraph = new HashMap<Integer, Set<Integer>>();
        for(Integer nodeID : directedGraph.getVertices()) {
            Set<Integer> neighborSet = undirectedGraph.get(nodeID);
            if(neighborSet == null) {
                neighborSet = new HashSet<Integer>();
            }

            for(Integer neighborID : directedGraph.getSuccessors(nodeID)) {
                neighborSet.add(neighborID);

                if(undirectedGraph.containsKey(neighborID)) {
                    Set<Integer> set = undirectedGraph.get(neighborID);
                    set.add(nodeID);
                } else {
                    Set<Integer> set = new HashSet<Integer>();
                    set.add(nodeID);
                    undirectedGraph.put(neighborID, set);
                }
            }

            undirectedGraph.put(nodeID, neighborSet);
        }

        return undirectedGraph;
    }

    private void setLabel(int tag, Map<Integer, Integer> labels, int curLabel,
                         Map<Integer, Set<Integer>> undirectedGraph) {
        int tagLabel = labels.get(tag);
        if(tagLabel != -1) return;

        labels.put(tag, curLabel);
        for(Integer neighbor : undirectedGraph.get(tag))
            setLabel(neighbor, labels, curLabel, undirectedGraph);
    }
}
