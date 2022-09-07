package graphClasses;

import model.BasicGraph;
import realizations.Realization2Tree;
import view.GraphView;
import y.base.*;
import y.geom.YPoint;
import y.view.Graph2D;

import java.util.ArrayList;

/**
 * Created by chris on 16.02.2018.
 */
public class kRectangularGraph {


    private final int LAYOUT_SCALING_FACTOR = 10;

    private int circleSize;
    private int k;

    private GraphView graphView;
    private Graph2D graph2D;
    private BasicGraph basicGraph;

    private ArrayList<ArrayList<Node>> cycles;

    private EdgeList artificialEdges;


    public kRectangularGraph(GraphView graphView, int circleSize, int k) {

        this.circleSize = circleSize;
        this.k = k;

        this.graphView = graphView;
        this.graph2D = graphView.getGraph2D();
        this.basicGraph = graphView.getGraph();


        this.basicGraph.resetGraph();

        this.initializeVertices();
        this.initializeEdges();
        this.initializeLayout();

        this.createArtificialEdges();
        this.graphView.getRelationships().generateRandomPartitions();
        Realization2Tree realization2Tree = new Realization2Tree(graphView);
        this.deleteArtificialEdges();

        this.graphView.updateView();


    }

    private void initializeVertices(){

        this.cycles = new ArrayList<>();

        for( int i=0; i < k ; i++){
            ArrayList<Node> cycle = new ArrayList<>();
            cycles.add(cycle);
        }

        for( int i= 0; i < circleSize ; i++){

            for( int j = 0; j < k ; j++){
                ArrayList<Node> currentCycle = cycles.get(j);
                Node currentNode = basicGraph.createNode();
                currentCycle.add(currentNode);
            }
        }
    }

    private void initializeEdges(){


        for( int i=0; i < circleSize ; i++){

            if( i+1 < circleSize){

                for( int j=0; j < k ; j++){
                    ArrayList<Node> currentCycle = cycles.get(j);
                    Node currentNode = currentCycle.get(i);
                    Node nextNode = currentCycle.get(i+1);
                    this.basicGraph.createEdge(currentNode,nextNode);
                }

            }

        }

        for( int i= 0; i < circleSize ; i++) {

            for (int j = 0; j < k; j++) {
                if( j+1 < k) {
                    ArrayList<Node> currentCycle = cycles.get(j);
                    ArrayList<Node> nextCycle = cycles.get(j+1);

                    Node currentNode = currentCycle.get(i);
                    Node nextNode = nextCycle.get(i);
                    this.basicGraph.createEdge(currentNode, nextNode);
                }
            }
        }

    }

    private void initializeLayout(){

        int numVertices = 2* circleSize;

        double baseRadius = numVertices * LAYOUT_SCALING_FACTOR;
        YPoint center = calculateCenterGraphView();

        for( int i=0; i < k; i++){
            ArrayList<Node> currentCycle = cycles.get(i);
            NodeList currentNodeList = new NodeList();
            currentNodeList.addAll(currentCycle);
            System.out.println(currentNodeList);
            double radius = baseRadius * (i+1);
            this.genericCyclePlacement(currentNodeList,center,radius);
        }
    }

    private void genericCyclePlacement(NodeList nodes, YPoint origin, double distance){


        double alpha = 360 / (double) (nodes.size());
        double currentAngle = 0;
        double currentAngleClockwise = 180-alpha;
        double finalAngleOffset = 0;


        for( int i= 0 ; i < nodes.size() ; i++){

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

    private void createArtificialEdges(){

        this.artificialEdges = new EdgeList();

        for( int i=0; i < circleSize ; i++){
            if( i+1 < circleSize) {

                for( int j=0; j < k; j++){
                    if( j+1 < k) {
                        ArrayList<Node> currentCycle = cycles.get(j);
                        ArrayList<Node> nextCycle = cycles.get(j+1);

                        Node currentNode = currentCycle.get(i);
                        Node correspondingNode = nextCycle.get(i+1);

                        Edge createdEdge = this.basicGraph.createEdgeReturned(currentNode,correspondingNode);
                        artificialEdges.add(createdEdge);
                    }
                }
            }
        }
    }


    private YPoint calculateCenterGraphView(){
        double worldHeight = this.graphView.getWorldRect2D().getHeight();
        double worldWidth = this.graphView.getWorldRect2D().getWidth();
        return new YPoint(worldWidth/2,worldHeight/2);
    }

    private void deleteArtificialEdges(){
        for(EdgeCursor e = artificialEdges.edges(); e.ok(); e.next()){
            Edge currentEdge = e.edge();
            this.basicGraph.deleteEdge(currentEdge);
        }
    }

}
