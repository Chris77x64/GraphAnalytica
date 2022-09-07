package view;

import model.MetricCollection;
import model.NodeCounterClockwiseComparator;
import y.base.Edge;
import y.base.EdgeCursor;
import y.base.Node;
import y.base.NodeCursor;
import y.layout.RotatedDiscreteEdgeLabelModel;
import y.view.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.util.ArrayList;


public class AngleView {

    private final DecimalFormat DEFAULT_FORMAT = new DecimalFormat("#.#");

    private final int LABEL_INDEX_SOURCE = 2;
    private final int LABEL_INDEX_TARGET = 3;
    private final int LABEL_FONT_SIZE = 15;
    private final double LABEL_DISTANCE_TO_EDGE = 0.5;

    private final Font LABEL_FONT = new Font("Arial",1,LABEL_FONT_SIZE);
    private final Color DEGREE_COLOR = Color.BLACK;

    private final byte LABEL_SOURCE_HEAD = 13;
    private final byte LABEL_SOURCE_TAIL = 15;
    private final byte LABEL_TARGET_HEAD = 14;
    private final byte LABEL_TARGET_TAIL = 16;

    private GraphView graphView;
    private Graph2D graph2D;

    private boolean showAngels;


    public  AngleView( GraphView graphView){
        this.graphView = graphView;
        this.graph2D = graphView.getGraph2D();
        this.showAngels = false;
    }


    public void reset(){
        for(EdgeCursor e = graph2D.edges(); e.ok(); e.next()) {
            Edge currentEdge = e.edge();
            EdgeRealizer edgeRealizer = graph2D.getRealizer(currentEdge);
            edgeRealizer.getLabel(LABEL_INDEX_SOURCE).setText("");
            edgeRealizer.getLabel(LABEL_INDEX_TARGET).setText("");
        }
        this.graphView.updateView();
    }

    public void update() {
        if( this.showAngels) {
            this.initializeDegrees();
            this.graphView.updateView();
        }
    }

    private void initializeDegrees(){

        ArrayList<Node> finishedVertices = new ArrayList<>();

        for (NodeCursor v = graph2D.nodes(); v.ok(); v.next()) {
            Node currentVertex = v.node();
            if( currentVertex.degree() <= 1){
                continue;
            }
            else{
                ArrayList<Node> adjacentVertices = this.adjacentVertices(currentVertex);
                //int adjacentfinishedVertices = this.adjacentFinishedVertexCount(currentVertex,adjacentVertices);
                ArrayList<Node> orderedAdjacentVertices = this.getCounterClockwiseOrdering(currentVertex,adjacentVertices);

                //System.out.println("________________________________________");
                //System.out.println("CURRENT VERTEX: "+currentVertex.index());
               /*
                for( Node element: orderedAdjacentVertices) {
                    System.out.println(element.index());
                }
                System.out.println("________________________________________");
                */
                int size = orderedAdjacentVertices.size();

                for( int i=0; i < size; i++){
                    Node element = orderedAdjacentVertices.get(i);

                    if( i+1 == size && adjacentVertices.size() > 2){
                        Node closestNode = orderedAdjacentVertices.get(0);
                        this.drawDegree(currentVertex,element,closestNode);
                    }
                    else if( i+1 < size){
                        Node closestNode = orderedAdjacentVertices.get(i+1);
                        this.drawDegree(currentVertex,element,closestNode);
                    }

                }
                finishedVertices.add(currentVertex);
            }
        }
    }


    private boolean isSource(Node vertex1, Node vertex2){
        Edge currentEdge = vertex1.getEdgeTo(vertex2);
        if( currentEdge == null){
            return false;
        }
        else{
            return true;
        }
    }


    private Edge getAppropriateEdge( Node source, Node target){
        Edge currentEdge = source.getEdgeTo(target);
        if( currentEdge == null){
            return source.getEdgeFrom(target);
        }
        else{
            return currentEdge;
        }
    }

    private void drawDegree( Node source, Node target, Node next) {


        double node1X = graph2D.getCenterX(source);
        double node1Y = graph2D.getCenterY(source);
        double node2X = graph2D.getCenterX(target);
        double node2Y = graph2D.getCenterY(target);
        double node3X = graph2D.getCenterX(next);
        double node3Y = graph2D.getCenterY(next);

        double angle = MetricCollection.angelSideSideSide(node1X, node1Y, node2X, node2Y, node3X, node3Y);
        if( angle < 180) {
            String angleString = DEFAULT_FORMAT.format(angle) + "°";
           // System.out.println("Winkel: " + angleString + " zwischen Node " + target.index() + " und " + next.index());
            Edge currentEdge = this.getAppropriateEdge(source, target);
            EdgeRealizer edgeRealizer = this.graph2D.getRealizer(currentEdge);

            RotatedDiscreteEdgeLabelModel model = new RotatedDiscreteEdgeLabelModel();
            model.setAutoFlippingEnabled(true);

            boolean isSource = this.isSource(source, target);

            EdgeLabel currentLabel;
            if (isSource) {
                currentLabel = edgeRealizer.getLabel(LABEL_INDEX_SOURCE);
            } else {
                currentLabel = edgeRealizer.getLabel(LABEL_INDEX_TARGET);
            }

            currentLabel.setLabelModel(model);
            currentLabel.setText(angleString);
            currentLabel.setTextColor(DEGREE_COLOR);
            currentLabel.setFont(LABEL_FONT);
            currentLabel.setDistance(LABEL_DISTANCE_TO_EDGE);

            if (isSource) {
                this.setDirectionEdgeLabelSource(currentLabel, node1X, node1Y, node2X, node2Y, node3X, node3Y);
            } else {
                this.setDirectionEdgeLabelTail(currentLabel, node1X, node1Y, node2X, node2Y, node3X, node3Y);
            }
            //System.out.println(currentLabel.positionToStringMap());

        }

    }
    private void setDirectionEdgeLabelTail( EdgeLabel currentLabel,double node1X, double node1Y,double node2X, double node2Y,double node3X,double node3Y){

        // Fall 1: Node2 und Node3 überhalb Node1
        if ( node1Y >= node2Y && node1Y >= node3Y){

            if( node1X <= node2X && node1X >= node3X){
                //north
                currentLabel.setPosition( LABEL_TARGET_HEAD);
            }
            else if( node1X <= node2X && node1X <= node3X){
                //north east
                currentLabel.setPosition( LABEL_TARGET_HEAD);
            }
            else{
                //north west
                currentLabel.setPosition( LABEL_TARGET_TAIL);
            }

        } // Fall 2: Node 2 and Nod3 below Node1
        else if( node1Y <= node2Y && node1Y <= node3Y){

            if( node1X >= node2X && node1X <= node3X){
                //south
                currentLabel.setPosition( LABEL_TARGET_TAIL);
            }
            else if ( node1X < node2X && node1X < node3X){
                // south east
                currentLabel.setPosition( LABEL_TARGET_HEAD);
            }
            else{
                // südwesten
                currentLabel.setPosition( LABEL_TARGET_TAIL);
            }
        }
        else if( node1Y >= node2Y && node1Y <= node3Y ){
            //west
            if( node1X > node2X && node1X > node3X){
                currentLabel.setPosition( LABEL_TARGET_TAIL);
            }
            else if( node1X < node2X && node1X > node3X){
                currentLabel.setPosition( LABEL_TARGET_HEAD);
            }
            else if( node1X > node2X && node1X < node3X){
                currentLabel.setPosition( LABEL_TARGET_TAIL);
            }
        }
        else{
            if( node1X < node2X && node1X < node3X) {
                currentLabel.setPosition(LABEL_TARGET_HEAD);
            }
            else if( node1X < node2X && node1X > node3X){
                currentLabel.setPosition(LABEL_TARGET_HEAD);
            }
            else if( node1X > node2X && node1X < node3X){
                currentLabel.setPosition(LABEL_TARGET_TAIL);
            }
        }
    }

    private void setDirectionEdgeLabelSource( EdgeLabel currentLabel,double node1X, double node1Y,double node2X, double node2Y,double node3X,double node3Y){

        // Fall 1: Node2 und Node3 überhalb Node1
        if ( node1Y >= node2Y && node1Y >= node3Y){

            if( node1X <= node2X && node1X >= node3X){
                //north
                currentLabel.setPosition( LABEL_SOURCE_HEAD);
            }
            else if( node1X <= node2X && node1X <= node3X){
                //north east
                currentLabel.setPosition( LABEL_SOURCE_HEAD);
            }
            else{
                //north west
                currentLabel.setPosition( LABEL_SOURCE_TAIL);
            }

        } // Fall 2: Node 2 and Nod3 below Node1
        else if( node1Y <= node2Y && node1Y <= node3Y){

            if( node1X >= node2X && node1X <= node3X){
                //south
                currentLabel.setPosition( LABEL_SOURCE_TAIL);
            }
            else if ( node1X < node2X && node1X < node3X){
                // south east
                currentLabel.setPosition( LABEL_SOURCE_HEAD);
            }
            else{
                // südwesten
                currentLabel.setPosition( LABEL_SOURCE_TAIL);
            }
        }
        else if( node1Y >= node2Y && node1Y <= node3Y ){
            //west
            if( node1X > node2X && node1X > node3X){
                currentLabel.setPosition( LABEL_SOURCE_TAIL);
            }
            else if( node1X < node2X && node1X > node3X){
                currentLabel.setPosition( LABEL_SOURCE_HEAD);
            }
            else if( node1X > node2X && node1X < node3X){
                currentLabel.setPosition( LABEL_SOURCE_TAIL);
            }

        }
        else if(node1Y <= node2Y && node1Y >= node3Y) {
            if( node1X < node2X && node1X < node3X) {
                currentLabel.setPosition(LABEL_SOURCE_HEAD);
            }
            else if( node1X < node2X && node1X > node3X){
                currentLabel.setPosition(LABEL_SOURCE_HEAD);
            }
            else if( node1X > node2X && node1X < node3X){
                currentLabel.setPosition(LABEL_SOURCE_TAIL);
            }

        }
    }

    private ArrayList<Node> adjacentVertices ( Node source){
        EdgeCursor adjacentEdges = source.edges();
        ArrayList<Node> result = new ArrayList<>();

        for(EdgeCursor e = adjacentEdges; e.ok(); e.next()) {
            Edge currentEdge = e.edge();
            Node edgeSource = currentEdge.source();
            Node edgeTarget = currentEdge.target();

            if( edgeSource.index() == source.index()){
                result.add(edgeTarget);
            }
            else{
                result.add(edgeSource);
            }

        }
        return result;
    }

    private ArrayList<Node> getCounterClockwiseOrdering(Node source,ArrayList<Node> adjacentVertices){

        NodeCounterClockwiseComparator comparator = new NodeCounterClockwiseComparator(source,this.graph2D);
        adjacentVertices.sort(comparator);
        return adjacentVertices;
    }


    public boolean isShowAngels() {
        return showAngels;
    }

    public void setShowAngels(boolean showAngels) {
        this.showAngels = showAngels;
    }
}
