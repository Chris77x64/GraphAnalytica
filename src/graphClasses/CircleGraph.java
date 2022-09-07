package graphClasses;

import model.BasicGraph;
import view.GraphView;
import y.base.Node;
import y.base.NodeList;
import y.geom.YPoint;
import y.layout.circular.CircularLayouter;
import y.layout.transformer.GraphTransformer;
import y.view.Graph2D;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by chris on 22.12.2017.
 */
public class CircleGraph {
    private final double DISTANCE = 300;
    private int size;

    private GraphView graphView;
    private Graph2D graph2D;
    private BasicGraph basicGraph;

    private NodeList vertices;
    private YPoint centerPoint;

    public CircleGraph(int size, GraphView graphView){
        this.size = size;
        this.graphView = graphView;
        this.graph2D = graphView.getGraph2D();
        this.basicGraph = graphView.getGraph();
        this.centerPoint = this.calculateCenterGraphView();

        this.basicGraph.resetGraph();
        this.initializeCircleGraph();




        /*

        this.initializeNodes();
        this.initializeEdges();
        this.initializeLayout();
        */

        this.basicGraph.updateViews();
        this.graphView.fitContent();
    }


    private void initializeNodes(){
        this.vertices = new NodeList();
        for( int i= 0; i < size ; i++){
            Node currentNode = this.basicGraph.createNode();
            vertices.add(currentNode);
        }
    }

    private void initializeEdges(){
        for( int i= 0; i < vertices.size() ; i++){
            Node currentNode = (Node) vertices.get(i);
            if( i+1 != vertices.size() ) {
                Node nextNode = (Node) vertices.get(i + 1);
                this.basicGraph.createEdge(currentNode, nextNode);
            }
            else{
                Node nextNode = (Node) vertices.get(0);
                this.basicGraph.createEdge(currentNode, nextNode);
            }
        }
    }

    private void initializeLayout(){
        double worldHeight = this.graphView.getWorldRect2D().getHeight();
        double worldWidth = this.graphView.getWorldRect2D().getWidth();

        Rectangle worldBoundingBox = new Rectangle(10,10,(int) worldWidth-20, (int) worldHeight-20);

        CircularLayouter circularLayouter = new CircularLayouter();
        circularLayouter.doLayout(this.graph2D);

        GraphTransformer graphTransformer = new GraphTransformer();
        graphTransformer.scaleToRect(this.graph2D,worldBoundingBox);

        this.basicGraph.updateViews();

    }


    private void initializeCircleGraph(){

        this.basicGraph.resetGraph();

        ArrayList<Node> circleNodes = new ArrayList<>();

        double alpha = 360 / (double) (size);
        double currentAngle = 0;
        double currentAngleClockwise = 180-alpha;
        double finalAngleOffset = 0;

        double circleX = this.centerPoint.getX();
        double circleY = this.centerPoint.getY();

        for( int i= 0 ; i < size ; i++){

            if( currentAngle > 180) {

                double tempAngle = Math.toRadians(currentAngleClockwise);
                double tempX =  ( DISTANCE * Math.cos(tempAngle) ) + circleX ;
                double tempY =  circleY - ( DISTANCE * Math.sin(tempAngle) )  ;
                YPoint tempPoint = new YPoint(tempX,tempY);
                Node circleNode = this.basicGraph.createNode(tempPoint);
                circleNodes.add(circleNode);
                currentAngleClockwise = currentAngleClockwise-alpha;


            }
            else{
                double tempAngle = Math.toRadians(currentAngle);
                double tempX = circleX + ( DISTANCE * Math.cos(tempAngle) );
                double tempY = circleY + ( DISTANCE * Math.sin(tempAngle) );
                YPoint tempPoint = new YPoint(tempX,tempY);

                Node circleNode = this.basicGraph.createNode(tempPoint);
                circleNodes.add(circleNode);
                currentAngle = currentAngle + alpha;

                if( currentAngle > 180 ) {


                    finalAngleOffset = 180 - (currentAngle - alpha);
                    currentAngleClockwise -= (alpha-finalAngleOffset);
                    currentAngleClockwise +=alpha;
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
