package graphClasses;

import model.BasicGraph;
import model.NodeCounterClockwiseComparator;
import view.GraphView;
import y.base.Node;
import y.geom.AffineLine;
import y.geom.YCircle;
import y.geom.YPoint;
import y.view.Graph2D;

import java.util.ArrayList;

/**
 * Created by chris on 21.12.2017.
 */

public class WheelGraph {

    private final double DISTANCE = 300;
    private int size;


    private GraphView graphView;
    private Graph2D graph2D;
    private BasicGraph basicGraph;

    private YPoint centerPoint;

    public WheelGraph(int size , GraphView graphView){
        this.size = size;
        this.graphView = graphView;
        this.graph2D = graphView.getGraph2D();
        this.basicGraph = graphView.getGraph();
        this.centerPoint = this.calculateCenterGraphView();
        this.initializeWheelGraph();
    }

    public WheelGraph(int size , YPoint centerPoint, GraphView graphView){
        this.size = size;
        this.graphView = graphView;
        this.graph2D = graphView.getGraph2D();
        this.basicGraph = graphView.getGraph();
        this.centerPoint = centerPoint;
        this.initializeWheelGraph();

        this.basicGraph.updateViews();
        this.graphView.fitContent();

    }


    private void initializeWheelGraph(){
        this.initializeConstructionLines();
        this.update();
    }


    private void update(){
        this.graphView.updateView();
    }

    private void initializeConstructionLines(){

        this.basicGraph.resetGraph();
        Node centerNode = this.basicGraph.createNode(centerPoint);

        ArrayList<Node> circleNodes = new ArrayList<>();

        double alpha = 360 / (double) (size-1);
        double currentAngle = 0;
        double currentAngleClockwise = 180-alpha;
        double finalAngleOffset = 0;

        double circleX = this.centerPoint.getX();
        double circleY = this.centerPoint.getY();

        for( int i= 1 ; i < size ; i++){

            if( currentAngle > 180) {

                //System.out.println("AKTUELLER WINKEL" +currentAngleClockwise);

                double tempAngle = Math.toRadians(currentAngleClockwise);
                double tempX =  ( DISTANCE * Math.cos(tempAngle) ) + circleX ;
                double tempY =  circleY - ( DISTANCE * Math.sin(tempAngle) )  ;
                YPoint tempPoint = new YPoint(tempX,tempY);

                Node circleNode = this.basicGraph.createNode(tempPoint);
                this.basicGraph.createEdge(centerNode,circleNode);
                circleNodes.add(circleNode);
                currentAngleClockwise = currentAngleClockwise-alpha;


            }
            else{
                double tempAngle = Math.toRadians(currentAngle);
                double tempX = circleX + ( DISTANCE * Math.cos(tempAngle) );
                double tempY = circleY + ( DISTANCE * Math.sin(tempAngle) );
                YPoint tempPoint = new YPoint(tempX,tempY);

                Node circleNode = this.basicGraph.createNode(tempPoint);
                this.basicGraph.createEdge(centerNode,circleNode);
                circleNodes.add(circleNode);
                currentAngle = currentAngle + alpha;

                //System.out.println("LOWER ANGLE:" +currentAngle);

                if( currentAngle > 180 ) {


                        finalAngleOffset = 180 - (currentAngle - alpha);
  //                      System.out.println("DIFFERENZ: "+finalAngleOffset);
                        currentAngleClockwise -= (alpha-finalAngleOffset);
                        currentAngleClockwise +=alpha;
//                        System.out.println("Angle: "+currentAngleClockwise);

                   // if (size % 2 == 1) {
                        //finalAngleOffset = 180 - (currentAngle - alpha);
                        //System.out.println("DIFFERENZ: "+finalAngleOffset);
                       // currentAngleClockwise += finalAngleOffset;
                       // System.out.println("Angle: "+currentAngleClockwise);
                   // }
                   // else{
                     //   finalAngleOffset = 180 - (currentAngle - alpha);
                      //  System.out.println("DIFFERENZ: "+finalAngleOffset);
                      //  currentAngleClockwise += finalAngleOffset;
                      //  System.out.println("Angle: "+currentAngleClockwise);
                   // }
                }


            }


        }


        int size = circleNodes.size();
        for(int i=0; i < size ; i++){
            if( i <  ( size -1) ){
                Node source = circleNodes.get(i);
                Node target = circleNodes.get(i+1);
                basicGraph.createEdge(source,target);

            }
            else{
                Node source = circleNodes.get(i);
                Node target = circleNodes.get(0);
                basicGraph.createEdge(source,target);
            }
        }

    }


    private YPoint calculateCenterGraphView(){
        double worldHeight = this.graphView.getWorldRect2D().getHeight();
        double worldWidth = this.graphView.getWorldRect2D().getWidth();
        return new YPoint(worldWidth/2,worldHeight/2);
    }



}
