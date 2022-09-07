package view;

import model.MetricCollection;
import y.base.Edge;
import y.base.EdgeCursor;
import y.base.Node;
import y.layout.RotatedDiscreteEdgeLabelModel;
import y.view.*;

import java.awt.*;
import java.text.DecimalFormat;

/**
 * Created by chris on 13.12.2017.
 */
public class DistanceView {


    private final DecimalFormat DEFAULT_FORMAT = new DecimalFormat("#.##");

    private final int LABEL_FONT_SIZE = 20;
    private final Font LABEL_FONT = new Font("Arial",1,LABEL_FONT_SIZE);
    private final Color LABEL_COLOR = Color.BLUE;
    private final byte LABEL_LEFT_ALIGNMENT = 10;

    private boolean distanceViewEnabled;

    private Graph2D graph2D;
    private GraphView graphView;

    public DistanceView(GraphView graphView){
        this.graphView = graphView;
        this.graph2D = graphView.getGraph2D();
        this.distanceViewEnabled = false;
    }

    public void update(){
        if(distanceViewEnabled) {
            this.initializeDistanceLabels();
            this.graphView.updateView();
        }
    }

    public void reset(){
        this.clearDistances();
        this.graphView.updateView();
    }


    public void clearDistances(){
        for(EdgeCursor e = graph2D.edges(); e.ok(); e.next()) {
            Edge currentEdge = e.edge();
            EdgeRealizer edgeRealizer = graph2D.getRealizer(currentEdge);
            edgeRealizer.getLabel(1).setText("");
        }
    }

    public void initializeDistanceLabels(){
        for(EdgeCursor e = graph2D.edges(); e.ok(); e.next()) {
            Edge currentEdge = e.edge();
            this.initializeSingleDistance(currentEdge);
        }
    }

    public void initializeSingleDistance(Edge edge){

        Node source = edge.source();
        Node target = edge.target();

        double sourceX = this.graph2D.getCenterX(source);
        double sourceY = this.graph2D.getCenterY(source);
        double targetX = this.graph2D.getCenterX(target);
        double targetY = this.graph2D.getCenterY(target);

        double distance = MetricCollection.euklideanDistanceR2(sourceX,sourceY,targetX,targetY);
        this.initializeLabel(edge,distance);

    }


    private void initializeLabel( Edge edge, double distance){

        String distanceLabelString = DEFAULT_FORMAT.format(distance);

        EdgeRealizer edgeRealizer = graph2D.getRealizer(edge);
        this.createLabel(distanceLabelString,edgeRealizer);

    }

    private void createLabel(String text, EdgeRealizer edgeRealizer){

        RotatedDiscreteEdgeLabelModel model = new RotatedDiscreteEdgeLabelModel();
        model.setAutoFlippingEnabled(true);
        EdgeLabel currentLabel = edgeRealizer.getLabel(1);

        currentLabel.setLabelModel(model);
        currentLabel.setText(text);
        currentLabel.setTextColor(LABEL_COLOR);
        currentLabel.setFont(LABEL_FONT);
        currentLabel.setPosition(LABEL_LEFT_ALIGNMENT);

    }

    public boolean isDistanceViewEnabled() {
        return distanceViewEnabled;
    }

    public void setDistanceViewEnabled(boolean distanceViewEnabled) {
        this.distanceViewEnabled = distanceViewEnabled;
    }
}
