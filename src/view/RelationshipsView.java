package view;

import model.BasicGraph;
import model.Relationships;
import y.base.Edge;
import y.base.EdgeCursor;
import y.base.Graph;
import y.layout.RotatedDiscreteEdgeLabelModel;
import y.view.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by chris on 08.12.2017.
 */
public class RelationshipsView {


    private final int LABEL_INDEX = 0;
    private final int LABEL_FONT_SIZE = 20;
    private final Font LABEL_FONT = new Font("Arial",1,LABEL_FONT_SIZE);
    private final byte LABEL_RIGHT_ALIGNMENT = 11;

    private final Color VIOLATION_COLOR = Color.orange;
    private final Color OKAY_COLOR = Color.green;
    private final Color LABEL_COLOR = Color.black;
    public final Color EDGE_DEFAULT_COLOR = Color.BLACK;

    private boolean enableLabels;
    private boolean enableColoring;

    private GraphView graphView;
    private Graph2D graph2D;

    private Relationships relationships;

    public RelationshipsView( GraphView view){
        this.graphView = view;
        this.graph2D = view.getGraph2D();
        this.relationships = view.getRelationships();
        this.enableLabels = true;
        this.enableColoring = false;
    }

    private void drawEdgeLabels(){

        HashMap<Integer, ArrayList<Edge>> partitions = this.relationships.getPartitions();
            for (int key : partitions.keySet()) {
                for (Edge e : partitions.get(key)) {
                    this.initializeLabel(e,key);
                }
            }
    }



    private void initializeLabel( Edge edge, int partition){

        String relationsshipLabelString = "Partition: "+Integer.toString(partition);
        EdgeRealizer edgeRealizer = graph2D.getRealizer(edge);
        this.createLabel(relationsshipLabelString,edgeRealizer);
    }

    private void createLabel(String text, EdgeRealizer edgeRealizer){

        RotatedDiscreteEdgeLabelModel model = new RotatedDiscreteEdgeLabelModel();
        model.setAutoFlippingEnabled(true);
        EdgeLabel currentLabel = edgeRealizer.getLabel(LABEL_INDEX);
        currentLabel.setLabelModel(model);
        currentLabel.setText(text);
        currentLabel.setTextColor(LABEL_COLOR);
        currentLabel.setFont(LABEL_FONT);
        currentLabel.setPosition(LABEL_RIGHT_ALIGNMENT);
    }

    private void drawColoring(){
        //this.graphView.resetEdgeColors();
      //  BasicGraph graph = this.graphView.getGraph();
      //  synchronized (graph) {

            ArrayList<Edge> violation_edges = this.relationships.violatingEdges();
            ArrayList<Edge> edgeSet = this.relationships.getAllEdgesInPartitions();
            edgeSet.removeAll(violation_edges);
            //System.out.println("VIOLATION: "+violation_edges.size());
            //System.out.println("EDGESET "+edgeSet.size());


            this.graphView.getSpanningTreeView().setEnableSpanningTreeView(false);
            this.graphView.getSpanningTreeView().reset();
            this.graphView.getMinimumSpanningTreeView().setEnableSpanningTreeView(false);
            this.graphView.getMinimumSpanningTreeView().reset();

            for (Edge edge : violation_edges) {
                EdgeRealizer edgeRealizer = this.graph2D.getRealizer(edge);

                edgeRealizer.setLineColor(VIOLATION_COLOR);

            }

            for (Edge edge : edgeSet) {
                EdgeRealizer edgeRealizer = this.graph2D.getRealizer(edge);
                edgeRealizer.setLineColor(OKAY_COLOR);
            }

    }

    public void resetPartitionLabels(){
        for(EdgeCursor e = graph2D.edges(); e.ok(); e.next()) {
            Edge currentEdge = e.edge();
            EdgeRealizer edgeRealizer = graph2D.getRealizer(currentEdge);
            edgeRealizer.getLabel(LABEL_INDEX).setText("");
        }
        this.graphView.updateView();
    }

    public void resetEdgeColoring(){
        for(EdgeCursor e = graph2D.edges(); e.ok(); e.next()) {
            Edge currentEdge = e.edge();
            EdgeRealizer edgeRealizer = graph2D.getRealizer(currentEdge);
            edgeRealizer.setLineColor(EDGE_DEFAULT_COLOR);
        }
        this.graphView.updateView();
    }

    public void updatePartitionLabels() {
        if (enableLabels) {
            this.drawEdgeLabels();
        }
    }

    public void updateEdgeColoring() {
        if( enableColoring){
            this.drawColoring();
        }
        this.graphView.updateView();
    }


    public boolean isEnableLabels() {
        return enableLabels;
    }

    public void setEnableLabels(boolean enableLabels) {
        this.enableLabels = enableLabels;
    }

    public boolean isEnableColoring() {
        return enableColoring;
    }

    public void setEnableColoring(boolean enableColoring) {
        this.enableColoring = enableColoring;
    }
}
