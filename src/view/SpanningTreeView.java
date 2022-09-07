package view;

import y.algo.SpanningTrees;
import y.base.Edge;
import y.base.EdgeCursor;
import y.base.EdgeList;
import y.view.EdgeRealizer;
import y.view.Graph2D;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by chris on 15.12.2017.
 */
public class SpanningTreeView {

    private final Color EDGE_DEFAULT_COLOR = Color.BLACK;
    private final Color EDGE_SPANNING_COLOR = Color.BLUE;

    private Graph2D graph2D;
    private GraphView graphView;

    private ArrayList<Edge> spanningEdges;

    private boolean enableSpanningTreeView;

    public SpanningTreeView(GraphView graphView){
        this.graphView = graphView;
        this.graph2D = this.graphView.getGraph2D();
        this.spanningEdges = new ArrayList<>();
        this.enableSpanningTreeView = false;
    }

    public void reset(){
        for( Edge edge: spanningEdges){
            EdgeRealizer edgeRealizer = this.graph2D.getRealizer(edge);
            edgeRealizer.setLineColor(EDGE_DEFAULT_COLOR);
        }
        spanningEdges.clear();
        this.graphView.updateView();
    }


    public void update(){
        this.reset();
        if( enableSpanningTreeView){
            this.calculateSpanningTee();
            this.graphView.getRelationshipsView().setEnableColoring(false);
            this.graphView.getRelationshipsView().resetEdgeColoring();
            this.graphView.getMinimumSpanningTreeView().setEnableSpanningTreeView(false);
            this.graphView.getMinimumSpanningTreeView().reset();
            for( Edge edge: this.spanningEdges){
                EdgeRealizer edgeRealizer = this.graph2D.getRealizer(edge);
                edgeRealizer.setLineColor(EDGE_SPANNING_COLOR);
            }
            this.graphView.updateView();
        }
    }

    private void calculateSpanningTee(){
        EdgeList spanningEdges = SpanningTrees.uniform(this.graph2D);
        for(EdgeCursor e = spanningEdges.edges(); e.ok(); e.next()) {
            Edge currentEdge = e.edge();
            this.spanningEdges.add(currentEdge);
        }
    }

    public boolean isEnableSpanningTreeView() {
        return enableSpanningTreeView;
    }

    public void setEnableSpanningTreeView(boolean enableSpanningTreeView) {
        this.enableSpanningTreeView = enableSpanningTreeView;
    }
}
