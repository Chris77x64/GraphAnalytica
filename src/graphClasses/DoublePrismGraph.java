package graphClasses;

import model.BasicGraph;
import realizations.Realization;
import realizations.Realization2Tree;
import view.GraphView;
import y.base.*;
import y.geom.YPoint;
import y.view.Graph2D;

/**
 * Created by chris on 16.02.2018.
 */
public class DoublePrismGraph {

    private final int LAYOUT_SCALING_FACTOR = 10;

    private int circleSize;

    private GraphView graphView;
    private Graph2D graph2D;
    private BasicGraph basicGraph;

    private NodeList firstCycle;
    private NodeList secondCycle;
    private NodeList thirdCycle;

    private boolean outerPlanar;

    private EdgeList artificialEdges;

    public DoublePrismGraph(GraphView graphView, int circleSize, boolean outerPlanar) {

        this.circleSize = circleSize;
        this.outerPlanar = outerPlanar;

        this.graphView = graphView;
        this.graph2D = graphView.getGraph2D();
        this.basicGraph = graphView.getGraph();

        this.basicGraph.resetGraph();

        this.initializeVertices();
        this.initializeEdges();
        this.initializeLayout();

        if( outerPlanar){
            createArtificialEdges();
            this.graphView.getRelationships().generateRandomPartitions();
            Realization2Tree realization2Tree = new Realization2Tree(graphView);
            this.deleteArtificialEdges();
        }
        else{
            this.basicGraph.updateViews();
            this.graphView.fitContent();
        }


    }


    private void initializeVertices(){

        this.firstCycle = new NodeList();
        this.secondCycle = new NodeList();
        this.thirdCycle = new NodeList();

        for( int i= 0; i < circleSize ; i++){
            Node innerNode = basicGraph.createNode();
            Node outerNode = basicGraph.createNode();
            Node secondOuterNode = basicGraph.createNode();

            firstCycle.add(innerNode);
            secondCycle.add(outerNode);
            thirdCycle.add(secondOuterNode);
        }
    }

    private void initializeEdges(){


        for( int i=0; i < circleSize ; i++){

            Node currentInnerNode = (Node) firstCycle.get(i);
            Node currentOuterNode = (Node) secondCycle.get(i);
            Node current2ndOuterNode = (Node) thirdCycle.get(i);

            Node nextInnerNode;
            Node nextOuterNode;
            Node next2ndOuterNode;

            if( i+1 == circleSize){

                nextInnerNode = (Node) firstCycle.get(0);
                nextOuterNode = (Node) secondCycle.get(0);
                next2ndOuterNode = (Node) thirdCycle.get(0);
            }
            else{
                nextInnerNode = (Node) firstCycle.get(i+1);
                nextOuterNode = (Node) secondCycle.get(i+1);
                next2ndOuterNode = (Node) thirdCycle.get(i+1);
            }
            if( i+1 == circleSize && this.outerPlanar){

            }
            else {
                this.basicGraph.createEdge(currentInnerNode, nextInnerNode);
                this.basicGraph.createEdge(currentOuterNode, nextOuterNode);
                this.basicGraph.createEdge(current2ndOuterNode, next2ndOuterNode);
            }

        }




        // initialize connection Edges between inner and outer cycle
        for( int i= 0; i < circleSize ; i++){
            Node currentInnerNode = (Node) firstCycle.get(i);
            Node currentOuterNode = (Node) secondCycle.get(i);
            Node current2ndOuterNode = (Node) thirdCycle.get(i);

            this.basicGraph.createEdge(currentInnerNode,currentOuterNode);
            this.basicGraph.createEdge(currentOuterNode,current2ndOuterNode);
        }
    }


    private void initializeLayout(){

        int numVertices = 2* circleSize;
        double radiusInnerCycle = numVertices * LAYOUT_SCALING_FACTOR;
        double radiusOuterCycle = 2* radiusInnerCycle;
        double radius2ndOuterCycle = 3* radiusInnerCycle;
        YPoint center = calculateCenterGraphView();

        this.genericCyclePlacement(firstCycle,center,radiusInnerCycle);
        this.genericCyclePlacement(secondCycle,center,radiusOuterCycle);
        this.genericCyclePlacement(thirdCycle,center,radius2ndOuterCycle);


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

    private YPoint calculateCenterGraphView(){
        double worldHeight = this.graphView.getWorldRect2D().getHeight();
        double worldWidth = this.graphView.getWorldRect2D().getWidth();
        return new YPoint(worldWidth/2,worldHeight/2);
    }

    private void createArtificialEdges(){

        this.artificialEdges = new EdgeList();

        for( int i=0; i < circleSize ; i++){
            if( i+1 < circleSize) {
                Node currentInnerNode = (Node) firstCycle.get(i);
                Node currentOuterNode = (Node) secondCycle.get(i);

                Node nextOuterNode = (Node) secondCycle.get(i+1);
                Node next2ndOuterNode = (Node) thirdCycle.get(i+1);

                Edge innerOuterEdge = this.basicGraph.createEdgeReturned(currentInnerNode,nextOuterNode);
                Edge outer2ndOuterEdge = this.basicGraph.createEdgeReturned(currentOuterNode,next2ndOuterNode);

                artificialEdges.add(innerOuterEdge);
                artificialEdges.add(outer2ndOuterEdge);
            }
        }
    }


    private void deleteArtificialEdges(){
        for(EdgeCursor e = artificialEdges.edges(); e.ok(); e.next()){
            Edge currentEdge = e.edge();
            this.basicGraph.deleteEdge(currentEdge);
        }
    }

}
