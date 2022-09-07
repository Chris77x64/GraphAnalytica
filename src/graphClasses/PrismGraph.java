package graphClasses;

import model.BasicGraph;
import realizations.Realization;
import view.GraphView;
import y.base.Node;
import y.base.NodeList;
import y.base.NodeMap;
import y.geom.YPoint;
import y.layout.circular.CircularLayouter;
import y.layout.grouping.RecursiveGroupLayouter;
import y.layout.radial.RadialLayouter;
import y.util.Maps;
import y.view.Graph2D;

import static y.util.Generics.nodes;

/**
 * Created by chris on 01.02.2018.
 */
public class PrismGraph {

    private final int LAYOUT_SCALING_FACTOR = 10;

    private int circleSize;

    private GraphView graphView;
    private Graph2D graph2D;
    private BasicGraph basicGraph;

    private NodeList firstCycle;
    private NodeList secondCycle;


    public PrismGraph(int circleSize, GraphView graphView) {
        this.circleSize = circleSize;

        this.graphView = graphView;
        this.graph2D = graphView.getGraph2D();
        this.basicGraph = graphView.getGraph();

        this.basicGraph.resetGraph();

        this.initializeVertices();
        this.initializeEdges();
        this.initializeLayout();

        this.basicGraph.updateViews();
        this.graphView.fitContent();


    }


    private void initializeVertices(){

        this.firstCycle = new NodeList();
        this.secondCycle = new NodeList();

        for( int i= 0; i < circleSize ; i++){
            Node innerNode = basicGraph.createNode();
            Node outerNode = basicGraph.createNode();

            firstCycle.add(innerNode);
            secondCycle.add(outerNode);
        }
    }

    private void initializeEdges(){


        // initialize Edges of inner cycle
        for( int i=0; i < circleSize ; i++){
            Node currentNode = (Node) firstCycle.get(i);
            Node nextNode;
            if( i+1 == circleSize){
                nextNode = (Node) firstCycle.get(0);
            }
            else{
                nextNode = (Node) firstCycle.get(i+1);
            }
            this.basicGraph.createEdge(currentNode,nextNode);
        }


        // initialize Edges of outer cycle
        for( int i=0; i < circleSize ; i++){
            Node currentNode = (Node) secondCycle.get(i);
            Node nextNode;
            if( i+1 == circleSize){
                nextNode = (Node) secondCycle.get(0);
            }
            else{
                nextNode = (Node) secondCycle.get(i+1);
            }
            this.basicGraph.createEdge(currentNode,nextNode);
        }


        // initialize connection Edges between inner and outer cycle
        for( int i= 0; i < circleSize ; i++){
            Node currentInnerNode = (Node) firstCycle.get(i);
            Node currentOuterNode = (Node) secondCycle.get(i);

            this.basicGraph.createEdge(currentInnerNode,currentOuterNode);
        }
    }

    private void initializeLayout(){

        int numVertices = 2* circleSize;
        double radiusInnerCycle = numVertices * LAYOUT_SCALING_FACTOR;
        double radiusOuterCycle = 2* radiusInnerCycle;
        YPoint center = calculateCenterGraphView();

        this.genericCyclePlacement(firstCycle,center,radiusInnerCycle);
        this.genericCyclePlacement(secondCycle,center,radiusOuterCycle);


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
}
