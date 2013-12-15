package edu.cmu.cs.vlis.adapter;

/**
 * Created with IntelliJ IDEA.
 * User: morefree
 * Date: 11/18/13
 * Time: 7:48 PM
 * To change this template use File | Settings | File Templates.
 */
public abstract class IndriGraphEdgeFactory {
    public abstract IndriGraphEdge createEdge(IndriGraphNode src, IndriGraphNode dst);
}
