package realizations;

import model.EdgeInducedSubgraph;
import view.GraphView;
import view.error.RealizationErrorView;
import y.algo.Cycles;
import y.algo.Paths;
import y.base.*;
import y.geom.YPoint;

import java.util.ArrayList;

/**
 * Created by chris on 02.02.2018.
 */
public class RealizationHamiltonianPathGraph extends Realization{

    private final double SHORT_EDGE_SCALING_FACTOR = 30;

    private EdgeInducedSubgraph shortEdgesInducedSubgraph;
    private Graph inducedSubgraph;

    private YPoint centerPoint;

    private NodeList graphNodesOrdered;

    public RealizationHamiltonianPathGraph(GraphView graphView) {
        super(graphView);

        this.initializeInducedSubgraph();

        if( isGraphType()) {

            this.calculateRealization();

            this.graphView.fitContent();
            this.basicGraph.updateViews();
        }

    }

    private void calculateRealization(){
        double worldHeight = this.graphView.getWorldRect2D().getHeight();
        double worldWidth = this.graphView.getWorldRect2D().getWidth();
        this.centerPoint = new YPoint(0.5*worldWidth,0.5*worldHeight);

        double lengthShortEdges = SHORT_EDGE_SCALING_FACTOR * graph2D.nodeCount();

        this.genericPathPlacement(graphNodesOrdered,centerPoint,lengthShortEdges);
    }

    @Override
    public boolean isGraphType() {

        if( inducedSubgraph != null) {
            if (isDichotomos()) {

                EdgeList shortPathEdges = Paths.findLongPath(inducedSubgraph);

                if (shortPathEdges.size() == graph2D.nodeCount()-1) {


                    this.graphNodesOrdered = new NodeList();
                    NodeList inducedGraphNodes = Paths.constructNodePath(shortPathEdges);
                    for( NodeCursor v = inducedGraphNodes.nodes(); v.ok() ; v.next()){
                        Node currentInducedNode = v.node();
                        Node currentNode = shortEdgesInducedSubgraph.getGraphNodeByInducedNode(currentInducedNode);
                        graphNodesOrdered.add(currentNode);
                    }
                    
                    return true;
                } else {
                    RealizationErrorView error = new RealizationErrorView(RealizationError.ERROR_HAMILTONIAN);
                    return false;
                }
            } else {
                RealizationErrorView error = new RealizationErrorView(RealizationError.ERROR_PARTITION);
                return false;
            }
        }
        else{
            RealizationErrorView error = new RealizationErrorView(RealizationError.ERROR_PARTITION);
            return false;
        }

    }

    private void initializeInducedSubgraph(){
        ArrayList<Edge> shortEdges = this.graphView.getRelationships().getPartitions().get(1);
        this.shortEdgesInducedSubgraph = new EdgeInducedSubgraph(this.graph2D,shortEdges);
        this.inducedSubgraph = shortEdgesInducedSubgraph.getInducedSubgraph();
    }


    private void genericPathPlacement(NodeList nodes, YPoint origin, double distance){


        double alpha = 360 / (double) (nodes.size()+1);
        double currentAngle = 0;
        double currentAngleClockwise = 180-alpha;
        double finalAngleOffset = 0;



        for( int i= 0 ; i < nodes.size()+1 ; i++){

            if( i == nodes.size()){
                break;
            }
            Node currentNode = (Node) nodes.get(i);

            if( currentAngle > 180) {

                double tempAngle = Math.toRadians(currentAngleClockwise);
                double tempX =  ( distance * Math.cos(tempAngle) ) + origin.x ;
                double tempY =  origin.y - ( distance * Math.sin(tempAngle) )  ;
                YPoint destination = new YPoint(tempX,tempY);

                this.basicGraph.moveNode(currentNode,destination);
                currentAngleClockwise = currentAngleClockwise-alpha;

            }
            else{

                double tempAngle = Math.toRadians(currentAngle);
                double tempX = origin.x + ( distance * Math.cos(tempAngle) );
                double tempY = origin.y + ( distance * Math.sin(tempAngle) );
                YPoint destination = new YPoint(tempX,tempY);

                this.basicGraph.moveNode(currentNode,destination);
                currentAngle = currentAngle + alpha;

                if( currentAngle > 180 ) {

                    finalAngleOffset = 180 - (currentAngle - alpha);
                    currentAngleClockwise -= (alpha-finalAngleOffset);
                    currentAngleClockwise +=alpha;
                }


            }

        }
    }

}
