package edu.cmu.cs.vlis.examples;

import edu.cmu.cs.vlis.adapter.*;
import edu.cmu.cs.vlis.matcher.ArbitraryGraphMatcher;
import edu.cmu.cs.vlis.matcher.GraphMatcher;
import edu.cmu.cs.vlis.matching.Matching;
import edu.cmu.cs.vlis.visual.GraphVisualizer;
import edu.cmu.cs.vlis.visual.LayoutGraphVisualizer;
import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.graph.Graph;
import java.util.List;

/**
 * this example shows how to do graph match against Titan ( a graph database ).
 * the constructor of this class accepts two arguments, the pattern and the list of targets.
 * Both of these two arguments come from Fei's code (virgilxie@gmail.com).
 *
 * Change SimpleNode.equals() method to any metric you want for graph matching, ex.
 * you can specify a matching as :  pattern.tagID == taget.tagID,
 * or you can speficy a matching as : pattern's text field is a similar to target's text field, a simple example
 * could be :  pattern.text.contains(target.text) || target.text.contains(pattern.text);
 *
 * Change SimpleNode.toString() method to anything you want to view when visualizing the graph.
 *
 * NOTE : when integrating with Fei's code, edu.cmu.cs.vlis.TagInfo and edu.cmu.cs.vlis.Vertex
 *        may not be imported, because in Fei's code, there exists definition of TagInfo and Vertex.
 *        They are exactly the same.
 *
 * @see IndriGraphMatch
 * 
 * @author ke xu (morefree7@gmail.com)
 */

public class IndriGraphMatchWithTitan {
    private Graph<Vertex, Object> pattern;
    private List<Graph<Vertex, Object>> targets;

    private IndriGraphNodeFactory nodeFactory;
    private IndriGraphEdgeFactory edgeFactory;
    private IndriAdapter adapter;

    public IndriGraphMatchWithTitan(Graph<Vertex, Object> pattern, List<Graph<Vertex, Object>> targets) {
        this.pattern = pattern;
        this.targets = targets;
        this.nodeFactory = new SimpleNodeFactory();
        this.edgeFactory = new SimpleEdgeFactory();
        this.adapter = new IndriAdapter(nodeFactory, edgeFactory);
    }

    private class SimpleNode extends IndriGraphNode {
        public boolean equals(Object obj) {
            if(!(obj instanceof IndriGraphNode)) return false;
            IndriGraphNode node = (IndriGraphNode) obj;

            return equals(this, node);
        }

        // change here for different flavors of comparison
        private boolean equals(IndriGraphNode fir, IndriGraphNode sec) {
            return fir.text.contains(sec.text) || sec.text.contains(fir.text);
            // return fir.tagID == sec.tagID;
        }

        // change here for different visualization
        public String toString() {
            return this.text;
            // return this.tagID + "";
        }
    }

    private static class SimpleEdge extends IndriGraphEdge {
        public SimpleEdge(String name) {
            this.edgeName = name;
        }

        public String toString() {
            return edgeName;
        }
    }

    private class SimpleNodeFactory extends IndriGraphNodeFactory {
        public IndriGraphNode createEmptyNode() {
            return new SimpleNode();
        }
    }

    private class SimpleEdgeFactory extends IndriGraphEdgeFactory {
        public IndriGraphEdge createEdge(IndriGraphNode src, IndriGraphNode dst) {
            if(src == null || dst == null) return null;
            return new SimpleEdge(src.tagID + "->" + dst.tagID);
        }
    }

    /**
     * sample procedure for matching against Titan database
     * different matching algorithms (contained in edu.cmu.cs.vlis.matching)
     * can be applied to matching process.
     */
    public void match(Matching<IndriGraphNode, IndriGraphEdge> matching) {
        TitanAdapter ap = new TitanAdapter(nodeFactory, edgeFactory);
        Graph<IndriGraphNode, IndriGraphEdge> pattern = ap.convert(this.pattern);
        List<Graph<IndriGraphNode, IndriGraphEdge>> targets = ap.convert(this.targets);

        GraphMatcher<IndriGraphNode, IndriGraphEdge, List<Graph<IndriGraphNode, IndriGraphEdge>>>
                matcher = new ArbitraryGraphMatcher<IndriGraphNode, IndriGraphEdge>(
                                matching
                            );

        // the output might be very long
        int cnt = 1;
        for(Graph<IndriGraphNode, IndriGraphEdge> target : targets) {
            for(Graph<IndriGraphNode, IndriGraphEdge> graph : matcher.match(pattern, target).getMatchResult()) {
                showGraph(graph, "find match " + cnt++ + " from target");
            }
        }
    }

    private void showGraph(Graph<IndriGraphNode, IndriGraphEdge> graph, String title) {
        GraphVisualizer<IndriGraphNode, IndriGraphEdge> gv =
                new LayoutGraphVisualizer<IndriGraphNode, IndriGraphEdge>(title,
                        new ISOMLayout<IndriGraphNode, IndriGraphEdge>(graph));
        gv.show(graph);
    }
}
