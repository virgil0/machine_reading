package edu.cmu.cs.vlis.visual;

import edu.uci.ics.jung.algorithms.layout.*;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer;
import org.apache.commons.collections15.Transformer;

import javax.swing.*;
import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: morefree
 * Date: 11/12/13
 * Time: 10:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class LayoutGraphVisualizer<V, E> implements GraphVisualizer<V, E> {
    private int initialSizeX = 300;
    private int initialSizeY = 300;
    private int preferredSizeX = 350;
    private int preferredSizeY = 350;

    private String title;
    private Layout<V, E> layout;

    private boolean isEdgeLabelled = false;

    public LayoutGraphVisualizer(String title, Layout<V, E> layout) {
        this.title = title;
        this.layout = layout;
    }

    public void setInitialSizeX(int initialSizeX) {
        this.initialSizeX = initialSizeX;
    }

    public void setInitialSizeY(int initialSizeY) {
        this.initialSizeY = initialSizeY;
    }

    public void setPreferredSizeX(int preferredSizeX) {
        this.preferredSizeX = preferredSizeX;
    }

    public void setPreferredSizeY(int preferredSizeY) {
        this.preferredSizeY = preferredSizeY;
    }

    public void enableEdgeLabels() {
        isEdgeLabelled = true;
    }

    public void disableEdgeLabels() {
        isEdgeLabelled = false;
    }

    public void show(Graph<V, E> graph) {
        layout.setSize(new Dimension(initialSizeX, initialSizeY));
        BasicVisualizationServer<V, E> server = new BasicVisualizationServer<V, E>(layout);
        server.setPreferredSize(new Dimension(preferredSizeX, preferredSizeY));

        // Setup up a new vertex to paint transformer...
        Transformer<V, Paint> vertexPaint = new Transformer<V, Paint>() {
            public Paint transform(V i) {
                return Color.GREEN;
            }
        };

        server.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
        server.getRenderContext().setVertexLabelTransformer(new ToStringLabeller());

        if(isEdgeLabelled)
            server.getRenderContext().setEdgeLabelTransformer(new ToStringLabeller());

        server.getRenderer().getVertexLabelRenderer().setPosition(Renderer.VertexLabel.Position.CNTR);

        JFrame frame = new JFrame(title);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().add(server);
        frame.pack();
        frame.setVisible(true);
    }
}
