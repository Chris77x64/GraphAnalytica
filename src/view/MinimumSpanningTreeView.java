package view;

import y.algo.SpanningTrees;
import y.base.DataProvider;
import y.base.Edge;
import y.base.EdgeCursor;
import y.base.EdgeList;
import y.util.Maps;
import y.view.EdgeRealizer;
import y.view.Graph2D;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by chris on 15.12.2017.
 */
public class MinimumSpanningTreeView {
    private final Color EDGE_DEFAULT_COLOR = Color.BLACK;
    private final Color EDGE_SPANNING_COLOR = Color.BLUE;

    private Graph2D graph2D;
    private GraphView graphView;

    private ArrayList<Edge> spanningEdges;

    private boolean enableSpanningTreeView;

    public MinimumSpanningTreeView(GraphView graphView){
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
            this.calculateMST();
            this.graphView.getRelationshipsView().setEnableColoring(false);
            this.graphView.getRelationshipsView().resetEdgeColoring();
            this.graphView.getSpanningTreeView().setEnableSpanningTreeView(false);
            this.graphView.getSpanningTreeView().reset();
            for( Edge edge: this.spanningEdges){
                EdgeRealizer edgeRealizer = this.graph2D.getRealizer(edge);
                edgeRealizer.setLineColor(EDGE_SPANNING_COLOR);
            }
            this.graphView.updateView();
        }
    }

    private void calculateMST(){
        DataProvider edgeCosts = this.generateEdgeCosts();
        EdgeList spanningEdgeList = SpanningTrees.kruskal(this.graph2D,edgeCosts);

        for(EdgeCursor e = spanningEdgeList.edges(); e.ok(); e.next()) {
            Edge currentEdge = e.edge();
            this.spanningEdges.add(currentEdge);
        }
    }


    private DataProvider generateEdgeCosts(){
        double costs[] = new double[this.graph2D.edgeCount()];
        for(EdgeCursor e = this.graph2D.edges(); e.ok(); e.next()) {
            Edge edge = e.edge();

            int partition = this.graphView.getRelationships().getPartitionIndexEdge(edge);

            if( partition == -1){
                costs[edge.index()] = 0;
            }
            else{
                costs[edge.index()] = partition;
            }
        }
        return  (DataProvider) Maps.createIndexEdgeMap(costs);
    }


    public boolean isEnableSpanningTreeView() {
        return enableSpanningTreeView;
    }

    public void setEnableSpanningTreeView(boolean enableSpanningTreeView) {
        this.enableSpanningTreeView = enableSpanningTreeView;
    }
}
