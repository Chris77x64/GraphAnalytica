package realizations;

import view.GraphView;
import view.error.RealizationErrorView;
import y.algo.Cycles;
import y.algo.Paths;
import y.base.*;
import y.geom.AffineLine;
import y.geom.YPoint;

import java.util.ArrayList;
import java.util.Collections;

import static y.algo.Cycles.findCycle;
import static y.algo.Paths.constructNodePath;

/**
 * Created by chris on 22.12.2017.
 */
public class RealizationCircleGraph extends Realization {

    private final double CLOSED_NEIBOURHOOD_DISTANCE = 5;

    private final double LENGTH_SHORT_EDGE = 50;
    private final double LENGTH_LONG_EDGE = 400;

    private final double CONSTRUCTION_ANGLE = 120;
    private final double NUM_CONSTRUCTION_POINTS = 3;

    private ArrayList<YPoint> longRealizationSpots;
    private ArrayList<YPoint> shortRealizationSpots;

    private ArrayList<AffineLine> helpLines;

    private final YPoint centerPoint = new YPoint(0,0);


    private EdgeList cycleEdges;
    private NodeList vertexOrder;

    public RealizationCircleGraph(GraphView graphView){
        super(graphView);


        this.initializeVertexOrder();
        this.calculateRealization(200);

        /*
        if( isGraphType()) {
            this.longRealizationSpots = new ArrayList<>();
            this.shortRealizationSpots = new ArrayList<>();
            this.helpLines = new ArrayList<>();

            this.centerPoint = calculateCenterGraphView();
            this.initializeAssets();
            this.initializeGraph();

            this.basicGraph.updateViews();
            this.graphView.fitContent();
        }
        else{

        }

        */
    }

    private void initializeVertexOrder(){
        this.cycleEdges = findCycle(graph2D,false);
        this.vertexOrder = constructNodePath(cycleEdges);
        this.vertexOrder.remove(vertexOrder.size()-1);
    }

    private void calculateRealization(double length){

        ArrayList<YPoint> points = new ArrayList<>();
        double s = calculateSumPartitions();
        System.out.println(s);
        double alpha = 360/s;

        double r = (length/ (2* Math.sin(Math.toRadians(180/s))));


        double gamma = 0;

        for( int i=0; i < s ; i++){
            YPoint point = calculateIntersectionUpSide(centerPoint,gamma,r);
            points.add(point);
            gamma = gamma+alpha;
        }

        int index = 0;
        Node firstVertex = (Node) vertexOrder.get(0);
        basicGraph.moveNode(firstVertex,points.get(0));

        for( int i= 1 ; i < vertexOrder.size(); i++){
            Node currentNode = (Node) vertexOrder.get(i);
            Node previousNode = (Node) vertexOrder.get(i-1);

            Edge currentEdge =currentNode.getEdge(previousNode);

            int j = graphView.getRelationships().getPartitionIndexEdge(currentEdge);

            int z = index + j;

            YPoint destination = points.get(z);
            basicGraph.moveNode(currentNode,destination);

            index = z;

        }

    }

    private int calculateSumPartitions(){
        int sum=0;
        for(EdgeCursor e= cycleEdges.edges(); e.ok() ; e.next()){
            Edge currentEdge = e.edge();

            Node source = currentEdge.source();
            Node target = currentEdge.target();

            sum += this.graphView.getRelationships().getPartitionIndexEdge(currentEdge);

        }
        return sum;
    }

    private void initializeAssets(){

        this.initializeLongRealizationSpots();
        this.initializeShortRealizationSpots();

    }

    private YPoint getNextPointOnLine( int line , YPoint source){
        switch (line){

            case 0:{
                YPoint[] intersectionPoint = this.calculateIntersectionCircleLine(source.getX(), source.getY(), LENGTH_SHORT_EDGE, helpLines.get(0));
                return intersectionPoint[1];
            }
            case 1:{
                YPoint[] intersectionPoint = this.calculateIntersectionCircleLine(source.getX(), source.getY(), LENGTH_SHORT_EDGE, helpLines.get(1));
                return intersectionPoint[1];
            }
            case 2:{
                YPoint[] intersectionPoint = this.calculateIntersectionCircleLine(source.getX(), source.getY(), LENGTH_SHORT_EDGE, helpLines.get(2));
                return intersectionPoint[0];
            }
        }
        return null;
    }

    private void initializeGraph(){


        ArrayList<Node> circleNodes = this.getGraphNodes();
        ArrayList<Edge> shortEdges = this.getEdgesFromPartition(1);
        ArrayList<Edge> longEdges = this.getEdgesFromPartition(2);

        circleNodes = sortCircleNodes(circleNodes,longEdges);
        System.out.println(circleNodes);
        int currentLine = 0;


        if( longEdges.size() % 2 == 0){

            System.out.println("FALL 1");
            Node firstNode = circleNodes.get(0);
            Node secondNode = circleNodes.get(1);
            Edge firstSecond = this.getEdgeFromVertex(firstNode,secondNode);

            moveNode(firstNode,this.getNextPointOnLine(2,centerPoint));
            YPoint previousPointLine0 = null;
            YPoint previousPointLine1 = null;

            if( longEdges.contains(firstSecond) ) {
                YPoint placementSecondNode = getNextPointOnLine(1,centerPoint);
                this.moveNode(secondNode,placementSecondNode);
                previousPointLine1 = placementSecondNode;
                currentLine = 1;

                for( int i=2 ; i < circleNodes.size(); i++){
                    System.out.println(currentLine);
                    Node currentCircleNode = circleNodes.get(i);
                    Node nextCircleNode = circleNodes.get(i - 1);

                    Edge edgeCircle = this.getEdgeFromVertex(currentCircleNode, nextCircleNode);

                    if( longEdges.contains(edgeCircle)) {
                        currentLine = (currentLine + 1) % 2;

                        if (currentLine == 0) {
                            YPoint position = null;
                            if (previousPointLine0 == null) {
                                position = getNextPointOnLine(0, centerPoint);
                            } else {
                                position = getNextPointOnLine(0, previousPointLine0);
                            }
                            previousPointLine0 = position;
                            this.moveNode(currentCircleNode, position);
                        }
                        else if( currentLine == 1){
                            YPoint position = null;
                            if (previousPointLine1 == null) {
                                position = getNextPointOnLine(1, centerPoint);
                            } else {
                                position = getNextPointOnLine(1, previousPointLine1);
                            }
                            previousPointLine1 = position;
                            this.moveNode(currentCircleNode, position);
                        }

                    }
                    else{

                        if (currentLine == 0) {
                            YPoint position = null;
                            if (previousPointLine0 == null) {
                                position = getNextPointOnLine(0, centerPoint);
                            } else {
                                position = getNextPointOnLine(0, previousPointLine0);
                            }
                            previousPointLine0 = position;
                            this.moveNode(currentCircleNode, position);
                        }
                        else if( currentLine == 1){
                            YPoint position = null;
                            if (previousPointLine1 == null) {
                                position = getNextPointOnLine(1, centerPoint);
                            } else {
                                position = getNextPointOnLine(1, previousPointLine1);
                            }
                            previousPointLine1 = position;
                            this.moveNode(currentCircleNode, position);
                        }


                    }
                }

            }
            else{
                moveNode(secondNode,centerPoint);

                Node thirdNode = circleNodes.get(2);
                Edge secondThird = this.getEdgeFromVertex(secondNode,thirdNode);
                if( longEdges.contains(secondThird)){
                    YPoint p1 = this.getNextPointOnLine(1,centerPoint);
                    YPoint p2 = this.getNextPointOnLine(1,p1);
                    this.moveNode(thirdNode,p2);
                    previousPointLine1 = p2;
                    currentLine = 1;
                }
                else{
                    YPoint p1 = this.getNextPointOnLine(0,centerPoint);
                    this.moveNode(thirdNode,p1);
                    previousPointLine0 = p1;
                    currentLine = 0;
                }

                for( int i= 3; i < circleNodes.size() ; i++){
                    Node currentCircleNode = circleNodes.get(i);
                    Node nextCircleNode = circleNodes.get(i - 1);

                    Edge edgeCircle = this.getEdgeFromVertex(currentCircleNode, nextCircleNode);
                    if( longEdges.contains(edgeCircle)) {
                        currentLine = (currentLine + 1) % 2;

                        if (currentLine == 0) {
                            YPoint position = null;
                            if (previousPointLine0 == null) {
                                position = getNextPointOnLine(0, centerPoint);
                            } else {
                                position = getNextPointOnLine(0, previousPointLine0);
                            }
                            previousPointLine0 = position;
                            this.moveNode(currentCircleNode, position);
                        }
                        else if( currentLine == 1){
                                YPoint position = null;
                                if (previousPointLine1 == null) {
                                    position = getNextPointOnLine(1, centerPoint);
                                } else {
                                    position = getNextPointOnLine(1, previousPointLine1);
                                }
                                previousPointLine1 = position;
                                this.moveNode(currentCircleNode, position);
                            }

                    }
                    else{

                        if (currentLine == 0) {
                            YPoint position = null;
                            if (previousPointLine0 == null) {
                                position = getNextPointOnLine(0, centerPoint);
                            } else {
                                position = getNextPointOnLine(0, previousPointLine0);
                            }
                            previousPointLine0 = position;
                            this.moveNode(currentCircleNode, position);
                        }
                        else if( currentLine == 1){
                            YPoint position = null;
                            if (previousPointLine1 == null) {
                                position = getNextPointOnLine(1, centerPoint);
                            } else {
                                position = getNextPointOnLine(1, previousPointLine1);
                            }
                            previousPointLine1 = position;
                            this.moveNode(currentCircleNode, position);
                        }


                        }


                }

            }



        }
        else if( longEdges.size() == 1){

            System.out.println("FALL 3:");
            Node firstNode = circleNodes.get(0);
            moveNode(firstNode,this.getNextPointOnLine(1,centerPoint));
            Node secondNode = circleNodes.get(1);
            moveNode(secondNode,centerPoint);

            YPoint previousPosition = this.getNextPointOnLine(0,centerPoint);
            for( int i=2; i < circleNodes.size() ; i++){
                Node currentCircleNode = circleNodes.get(i);
                moveNode(currentCircleNode,previousPosition);
                previousPosition = this.getNextPointOnLine(0,previousPosition);
            }

        }
        else{
            System.out.println("FALL 2:");
            YPoint previousPointLine0 = null;
            YPoint previousPointLine1 = null;

            Node firstNode = circleNodes.get(0);
            moveNode(firstNode,this.getNextPointOnLine(0,centerPoint));
            previousPointLine0 = this.getNextPointOnLine(0,centerPoint);


            Edge secondLastEdge = this.secondLastEdgeInCircle(circleNodes,longEdges);

            int secondLastIndex = this.secondLastVertexIndexInCircle(circleNodes,longEdges) +1;

            currentLine = 0;
            for( int i= 1; i < circleNodes.size() ; i++) {
                Node currentCircleNode = circleNodes.get(i);
                Node nextCircleNode = circleNodes.get(i - 1);

                Edge edgeCircle = this.getEdgeFromVertex(currentCircleNode, nextCircleNode);
                System.out.println("Index :"+secondLastIndex);


                    System.out.println("CURRENT INDEX: "+currentCircleNode.index());
                    if( longEdges.contains(edgeCircle)) {
                        currentLine = (currentLine + 1) % 2;

                        System.out.println("CURRENTLINE "+currentLine);
                        if (currentLine == 0) {
                            YPoint position = null;
                            if (previousPointLine0 == null) {
                                position = getNextPointOnLine(0, centerPoint);
                            } else {
                                position = getNextPointOnLine(0, previousPointLine0);
                            }
                            previousPointLine0 = position;
                            this.moveNode(currentCircleNode, position);
                        }
                        else if( currentLine == 1){
                            YPoint position = null;
                            if (previousPointLine1 == null) {
                                position = getNextPointOnLine(1, centerPoint);
                            } else {
                                position = getNextPointOnLine(1, previousPointLine1);
                            }
                            previousPointLine1 = position;
                            this.moveNode(currentCircleNode, position);
                        }

                    }
                    else {
                        System.out.println("CURRENTLINE "+currentLine);
                        if (currentLine == 0) {
                            YPoint position = null;
                            if (previousPointLine0 == null) {
                                position = getNextPointOnLine(0, centerPoint);
                            } else {
                                position = getNextPointOnLine(0, previousPointLine0);
                            }
                            previousPointLine0 = position;
                            this.moveNode(currentCircleNode, position);
                        } else if (currentLine == 1) {
                            YPoint position = null;
                            if (previousPointLine1 == null) {
                                position = getNextPointOnLine(1, centerPoint);
                            } else {
                                position = getNextPointOnLine(1, previousPointLine1);
                            }
                            previousPointLine1 = position;
                            this.moveNode(currentCircleNode, position);
                        }
                    }


            }

            /*
            int distanceSecondLatestEnd = circleNodes.size()-1- secondLastIndex;
            YPoint previousPoint = this.getNextPointOnLine(2,centerPoint);
            for( int i = circleNodes.size()-1 ; i > distanceSecondLatestEnd; i--){
                System.out.println(i);
                Node currentNode = circleNodes.get(i);
                System.out.println(circleNodes);
                moveNode(currentNode,previousPoint);
                previousPoint = this.getNextPointOnLine(2,previousPoint);
            }
            */


        }
        this.graphView.getRelationshipsView().updateEdgeColoring();

    }


    public void moveNode(Node vertex, YPoint newLocation){
        this.graph2D.setCenter(vertex,newLocation);
    }

    private ArrayList<Node> sortCircleNodes(ArrayList<Node> circleNode, ArrayList<Edge> longEdges){
        Edge latestEdge = null;
        int index = -1;

        ArrayList<Node> result = circleNode;
        for( int i=0; i < circleNode.size() ; i++){
            Node currentNode = circleNode.get(i);
            Node nextNode;
            if( i+1 == circleNode.size() ){
                nextNode = circleNode.get(0);
            }
            else{
                nextNode = circleNode.get(i+1);
            }
            Edge currentEdge = this.getEdgeFromVertex(currentNode,nextNode);
            if(longEdges.contains(currentEdge) ){
                if( latestEdge == null){
                    latestEdge = currentEdge;
                    index = i;
                }
            }
        }
        System.out.println("LAST INDEX SORT: "+index);
        System.out.println(result);
        Collections.rotate(result,circleNode.size()-index-1);
        System.out.println(result);
        return result;
    }

    private void initializeShortRealizationSpots(){

        double circleX = this.centerPoint.getX();
        double circleY = this.centerPoint.getY();

        for( int i=0; i < helpLines.size() ; i++){
            AffineLine helpLine = helpLines.get(i);
            YPoint[] intersections = this.calculateIntersectionCircleLine(circleX,circleY,LENGTH_SHORT_EDGE,helpLine);

            switch(i){
                case 0:{
                    YPoint intersectionPoint = intersections[1];
                    this.shortRealizationSpots.add(intersectionPoint);
                    //this.basicGraph.createNode(intersectionPoint);
                    break;
                }
                case 1:{
                    YPoint intersectionPoint = intersections[1];
                    this.shortRealizationSpots.add(intersectionPoint);
                    //this.basicGraph.createNode(intersectionPoint);
                    break;
                }
                case 2:{
                    YPoint intersectionPoint = intersections[0];
                    this.shortRealizationSpots.add(intersectionPoint);
                    //this.basicGraph.createNode(intersectionPoint);
                    break;
                }
            }
        }
    }

    private void initializeLongRealizationSpots(){

        double currentAngle = 0;
        double currentAngleClockwise = 180-CONSTRUCTION_ANGLE;
        double finalAngleOffset = 0;

        double circleX = this.centerPoint.getX();
        double circleY = this.centerPoint.getY();

        for( int i= 0 ; i < NUM_CONSTRUCTION_POINTS ; i++){

            if( currentAngle > 180) {
                double tempAngle = Math.toRadians(currentAngleClockwise);
                double tempX =  ( LENGTH_LONG_EDGE * Math.cos(tempAngle) ) + circleX ;
                double tempY =  circleY - ( LENGTH_LONG_EDGE * Math.sin(tempAngle) )  ;
                YPoint tempPoint = new YPoint(tempX,tempY);
                AffineLine line = new AffineLine(centerPoint,tempPoint);
                //this.basicGraph.createNode(tempPoint);
                longRealizationSpots.add(tempPoint);
                helpLines.add(line);

                currentAngleClockwise = currentAngleClockwise-CONSTRUCTION_ANGLE;
            }
            else{
                double tempAngle = Math.toRadians(currentAngle);
                double tempX = circleX + ( LENGTH_LONG_EDGE * Math.cos(tempAngle) );
                double tempY = circleY + ( LENGTH_LONG_EDGE * Math.sin(tempAngle) );
                YPoint tempPoint = new YPoint(tempX,tempY);
                AffineLine line = new AffineLine(centerPoint,tempPoint);
                helpLines.add(line);
                //this.basicGraph.createNode(tempPoint);
                longRealizationSpots.add(tempPoint);
                currentAngle = currentAngle + CONSTRUCTION_ANGLE;

                if( currentAngle > 180 ) {
                    finalAngleOffset = 180 - (currentAngle - CONSTRUCTION_ANGLE);
                    currentAngleClockwise -= (CONSTRUCTION_ANGLE-finalAngleOffset);
                    currentAngleClockwise +=CONSTRUCTION_ANGLE;
                }

            }

        }
    }

    private YPoint calculateCenterGraphView(){
        double worldHeight = this.graphView.getWorldRect2D().getHeight();
        double worldWidth = this.graphView.getWorldRect2D().getWidth();
        return new YPoint(worldWidth/2,worldHeight/2);
    }

    @Override
    public boolean isGraphType() {

        if( graph2D.nodeCount() >=4 ) {
            if (isDichotomos()) {

                EdgeList cycle = findCycle(graph2D, false);
                NodeList cycleNodes = constructNodePath(cycle);
                if (cycleNodes.size() == graph2D.nodeCount() + 1) {
                    return true;
                } else {
                    RealizationErrorView error = new RealizationErrorView(RealizationError.ERROR_NOT_CIRCULAR);
                    return false;
                }

            } else {
                RealizationErrorView error = new RealizationErrorView(RealizationError.ERROR_PARTITION);
                return false;
            }
        }
        else{
            RealizationErrorView errorView = new RealizationErrorView(RealizationError.ERROR_SIZE);
            return false;
        }

    }

    private Edge secondLastEdgeInCircle(ArrayList<Node> circleNode, ArrayList<Edge> longEdges){

        Edge secondLatestEdge = null;

        for( int i=0; i < circleNode.size() ; i++){
            Node currentNode = circleNode.get(i);
            Node nextNode;
            if( i+1 < circleNode.size() ){
                nextNode = circleNode.get(i+1);
            }
            else{
                break;
            }
            Edge currentEdge = this.getEdgeFromVertex(currentNode,nextNode);
            if(longEdges.contains(currentEdge) ){

                secondLatestEdge = currentEdge;

            }
        }

        return secondLatestEdge;
    }

    private int secondLastVertexIndexInCircle(ArrayList<Node> circleNode, ArrayList<Edge> longEdges){


        int index = -1;
        for( int i=0; i < circleNode.size() ; i++){
            Node currentNode = circleNode.get(i);
            Node nextNode;
            if( i+1 < circleNode.size() ){
                nextNode = circleNode.get(i+1);
            }
            else{
                break;
            }
            Edge currentEdge = this.getEdgeFromVertex(currentNode,nextNode);
            if(longEdges.contains(currentEdge) ){
                index = i;
            }

        }

        return index;
    }



}
