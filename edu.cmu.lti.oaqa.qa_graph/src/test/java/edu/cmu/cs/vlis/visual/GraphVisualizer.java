package edu.cmu.cs.vlis.visual;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.algorithms.layout.Layout;

/**
 * Created with IntelliJ IDEA.
 * User: morefree
 * Date: 11/12/13
 * Time: 10:35 PM
 * To change this template use File | Settings | File Templates.
 */
public interface GraphVisualizer<V, E> {
    public void show(Graph<V, E> graph);
}
