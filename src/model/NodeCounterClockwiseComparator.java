package model;

import y.base.Node;
import y.view.Graph2D;

import java.util.Comparator;

/**
 * Created by chris on 14.12.2017.
 */
public class NodeCounterClockwiseComparator implements Comparator<Node> {

    private Node source;
    private Graph2D graph2D;

    private double sourceX;
    private double sourceY;

    public NodeCounterClockwiseComparator(Node vertex,Graph2D graph2D){
        this.source = vertex;
        this.graph2D = graph2D;
        this.sourceX = this.graph2D.getCenterX(source);
        this.sourceY = this.graph2D.getCenterY(source);
    }

    @Override
    public int compare(Node O1, Node O2) {

        double nodeO1X = this.graph2D.getCenterX(O1);
        double nodeO1Y = this.graph2D.getCenterY(O1);
        double nodeO2X = this.graph2D.getCenterX(O2);
        double nodeO2Y = this.graph2D.getCenterY(O2);

        String locationComparison = MetricCollection.directionPointLine(nodeO1X,nodeO1Y,sourceX,sourceY,nodeO2X,nodeO2Y);

        switch( locationComparison){
            case "POINT_COUNTERCLOCKWISE":{
                return 1;
            }
            case "POINT_CLOCKWISE": {
                return -1;
            }
        }
        return 0;
    }


}
