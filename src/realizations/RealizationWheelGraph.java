package realizations;

import model.EdgeInducedSubgraph;
import model.NodeCounterClockwiseComparator;
import view.GraphView;
import view.error.RealizationErrorView;
import y.algo.Cycles;
import y.algo.Paths;
import y.base.*;
import y.geom.AffineLine;
import y.geom.YPoint;

import java.util.ArrayList;
import java.util.Collections;

import static y.util.Generics.nodes;

/**
 * Created by chris on 21.12.2017.
 */
public class RealizationWheelGraph extends Realization {

    private final double CLOSED_NEIBOURHOOD_DISTANCE = 5;

    private final double LENGTH_SHORT_EDGE = 250;
    private final double LENGTH_LONG_EDGE = 400;
    private final double LENGTH_CASE3 = 350.5;

    private final double SMALL_NEIGHBOURHOOD_OFFSET = 10;

    private final double CONSTRUCTION_ANGLE = 120;
    private final double NUM_CONSTRUCTION_POINTS = 3;

    private ArrayList<YPoint> longRealizationSpots;
    private ArrayList<YPoint> shortRealizationSpots;

    private ArrayList<AffineLine> helpLines;

    private NodeList circleNodes;
    private ArrayList<Edge> circleEdges;
    private Node centerNode;

    private ArrayList<Edge> shortEdges;
    private ArrayList<Edge> longEdges;

    private YPoint centerPoint;

    ArrayList<Node> smallNodesSpot1;
    ArrayList<Node> smallNodesSpot2;
    ArrayList<Node> smallNodesSpot3;

    ArrayList<Node> longNodesSpot1;
    ArrayList<Node> longNodesSpot2;
    ArrayList<Node> longNodesSpot3;

    public RealizationWheelGraph(GraphView graphView){
        super(graphView);

        this.longRealizationSpots = new ArrayList<>();
        this.shortRealizationSpots = new ArrayList<>();
        this.helpLines = new ArrayList<>();

        this.centerPoint = calculateCenterGraphView();

        if( isGraphType()) {
            this.initializeAssets();
            this.initializeLongShortEdges();
            this.initializeCircleNodes();
            this.calculateRealization();

            this.graphView.fitContent();
            this.basicGraph.updateViews();
        }
    }


    private YPoint calculateCenterGraphView(){
            double worldHeight = this.graphView.getWorldRect2D().getHeight();
            double worldWidth = this.graphView.getWorldRect2D().getWidth();
            return new YPoint(worldWidth/2,worldHeight/2);
    }

    private void initializeAssets(){

        this.initializeLongRealizationSpots();
        this.initializeShortRealizationSpots();

    }

    private void initializeLongShortEdges(){
        this.shortEdges = this.getEdgesFromPartition(1);
        this.longEdges = this.getEdgesFromPartition(2);
    }

    private void initializeCircleNodes(){

        int n = graph2D.nodeCount();

        for(NodeCursor v = graph2D.nodes(); v.ok() ; v.next()){
            Node currentNode = v.node();
            if( currentNode.degree() == (n-1) ){
                this.centerNode = currentNode;
            }
        }

        ArrayList<Edge> inducedEdges = new ArrayList<>();

        for( EdgeCursor e = graph2D.edges(); e.ok() ; e.next()){
            Edge edge = e.edge();

            Node source = edge.source();
            Node target = edge.target();

            if( source.index() != centerNode.index() && target.index() != centerNode.index()){
                inducedEdges.add(edge);
            }
        }

        this.circleEdges = inducedEdges;

        EdgeInducedSubgraph edgeInducedSubgraph = new EdgeInducedSubgraph(this.graph2D,inducedEdges);
        Graph inducedGraph = edgeInducedSubgraph.getInducedSubgraph();

        EdgeList cycle = Cycles.findCycle(inducedGraph,false);
        NodeList inducedCycleNodes = Paths.constructNodePath(cycle);

        this.circleNodes = new NodeList();
        for( NodeCursor v = inducedCycleNodes.nodes(); v.ok() ; v.next()){
            Node currentInducedNode = v.node();
            Node graphNode = edgeInducedSubgraph.getGraphNodeByInducedNode(currentInducedNode);
            circleNodes.add(graphNode);
        }

        circleNodes.popLast();

    }

    private void changePartitions(ArrayList<Node> circleNode , ArrayList<Edge> longEdges){
        Edge firstLongEdge = null;
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
                if( firstLongEdge == null){
                    firstLongEdge = currentEdge;
                }
                else{
                    this.graphView.getRelationships().updateEdge(1,currentEdge);
                }
            }
        }
    }


    private void calculateRealization(){

        int numLongEdges = numLongEdgesCircle(this.circleEdges);

        smallNodesSpot1 = new ArrayList<>();
        smallNodesSpot2 = new ArrayList<>();
        smallNodesSpot3 = new ArrayList<>();

        longNodesSpot1 = new ArrayList<>();
        longNodesSpot2 = new ArrayList<>();
        longNodesSpot3 = new ArrayList<>();

        if(numLongEdges % 2 == 0){
            this.initializeFirstVertex();
            this.genericCircleVertexPlacement(1,circleNodes.size(),0);
        }
        else if( numLongEdges % 2 == 1 && numLongEdges > 1){


            this.initializeFirstVertex();

            Node uj = getSecondLastLongEdgeNode();
            int indexUJ = circleNodes.indexOf(uj);

            genericCircleVertexPlacement(1,indexUJ+1,0);

            int indexUJNext = indexUJ+1;
            // uj,ujnext is long
            Node ujNext = (Node) circleNodes.get(indexUJNext);

            Edge centerUJNext = centerNode.getEdge(ujNext);

            if( longEdges.contains(centerUJNext)){
                YPoint realizationSpot = this.longRealizationSpots.get(2);
                this.basicGraph.moveNode(ujNext,realizationSpot);
                this.longNodesSpot3.add(ujNext);
            }
            else{
                YPoint realizationSpot = this.shortRealizationSpots.get(2);
                this.basicGraph.moveNode(ujNext,realizationSpot);
                this.smallNodesSpot3.add(ujNext);
            }

            int currentHelpLine = 2;

            for( int i = indexUJNext+1; i < circleNodes.size() ; i++){
                Node currentCircleNode = (Node) circleNodes.get(i);
                Node previousCircleNode = (Node) circleNodes.get(i-1);

                Edge edgeCircle = currentCircleNode.getEdge(previousCircleNode);
                Edge edgeCenterCurrent = currentCircleNode.getEdge(centerNode);


                if (longEdges.contains(edgeCircle)) {
                    currentHelpLine = 0;
                }

                if (longEdges.contains(edgeCenterCurrent)) {
                    YPoint realizationSpot = this.longRealizationSpots.get(currentHelpLine);
                    this.basicGraph.moveNode(currentCircleNode, realizationSpot);
                    if( currentHelpLine == 0){
                        this.longNodesSpot1.add(currentCircleNode);
                    }
                    else if( currentHelpLine == 2){
                        this.longNodesSpot3.add(currentCircleNode);
                    }
                } else if( shortEdges.contains(edgeCenterCurrent)){

                    YPoint realizationSpot = this.shortRealizationSpots.get(currentHelpLine);
                    this.basicGraph.moveNode(currentCircleNode, realizationSpot);
                    if( currentHelpLine == 0){
                        this.smallNodesSpot1.add(currentCircleNode);
                    }
                    else if( currentHelpLine == 2){
                        this.smallNodesSpot3.add(currentCircleNode);
                    }
                }

            }


        }
        else{

            this.circleNodes = this.calculateNodeOrderCase3();
            this.initializeFirstVertex();

            this.genericCircleVertexPlacement(1,circleNodes.size()-1,0);

            Node lastNode = (Node) circleNodes.get(circleNodes.size()-1);
            Node previousNode = (Node) circleNodes.get(circleNodes.size()-2);
            Node nextNode = (Node) circleNodes.get(0);

            Edge centerLastNode = centerNode.getEdge(lastNode);

            boolean centerLastNodeLong = longEdges.contains(centerLastNode);

            YPoint positionPreviousNode = graph2D.getCenter(previousNode);
            YPoint positionNextNode = graph2D.getCenter(nextNode);

            YPoint[] intersection = calculateCircleCircleIntersection(positionNextNode,LENGTH_CASE3,positionPreviousNode,LENGTH_CASE3);

                if( centerLastNodeLong){
                    YPoint destination = intersection[0];
                    this.basicGraph.moveNode(lastNode,destination);
                }
                else {
                    YPoint destination = intersection[1];
                    this.basicGraph.moveNode(lastNode,destination);
                }


        }

        this.calculateSmallNeighbourhood();

    }


    private void genericSmallNeighbourhoodPlacement(ArrayList<Node> nodes, boolean longSpot, int spotNumber ){

        YPoint origin;

        if( longSpot){
            origin = this.longRealizationSpots.get(spotNumber);
        }
        else{
            origin = this.shortRealizationSpots.get(spotNumber);
        }

        double alpha = 360 / (double) (nodes.size());
        double currentAngle = 0;
        double currentAngleClockwise = 180-alpha;
        double finalAngleOffset = 0;


        for( int i= 0 ; i < nodes.size() ; i++){

            Node currentNode = nodes.get(i);

            if( currentAngle > 180) {

                double tempAngle = Math.toRadians(currentAngleClockwise);
                double tempX =  ( SMALL_NEIGHBOURHOOD_OFFSET * Math.cos(tempAngle) ) + origin.x ;
                double tempY =  origin.y - ( SMALL_NEIGHBOURHOOD_OFFSET * Math.sin(tempAngle) )  ;
                YPoint destination = new YPoint(tempX,tempY);

                this.basicGraph.moveNode(currentNode,destination);
                currentAngleClockwise = currentAngleClockwise-alpha;

            }
            else{

                double tempAngle = Math.toRadians(currentAngle);
                double tempX = origin.x + ( SMALL_NEIGHBOURHOOD_OFFSET * Math.cos(tempAngle) );
                double tempY = origin.y + ( SMALL_NEIGHBOURHOOD_OFFSET * Math.sin(tempAngle) );
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


    private void calculateSmallNeighbourhood(){

        this.genericSmallNeighbourhoodPlacement(this.smallNodesSpot1,false,0);
        this.genericSmallNeighbourhoodPlacement(this.smallNodesSpot2,false,1);
        this.genericSmallNeighbourhoodPlacement(this.smallNodesSpot3,false,2);

        this.genericSmallNeighbourhoodPlacement(this.longNodesSpot1,true,0);
        this.genericSmallNeighbourhoodPlacement(this.longNodesSpot2,true,1);
        this.genericSmallNeighbourhoodPlacement(this.longNodesSpot3,true,2);


    }

    private void initializeFirstVertex(){
        this.basicGraph.moveNode(centerNode,centerPoint);

        Node firstCycleNode = (Node) circleNodes.get(0);
        Edge centerFirst = centerNode.getEdge(firstCycleNode);

        if( longEdges.contains(centerFirst)){
            YPoint realizationSpot = this.longRealizationSpots.get(0);
            this.basicGraph.moveNode(firstCycleNode,realizationSpot);
            this.longNodesSpot1.add(firstCycleNode);
        }
        else{
            YPoint realizationSpot = this.shortRealizationSpots.get(0);
            this.basicGraph.moveNode(firstCycleNode,realizationSpot);
            this.smallNodesSpot1.add(firstCycleNode);
        }
    }




    private void initializeGraph2() {


        double[] offsetHelpLines = new double[]{0, 0, 0};

        // initialize central vertex
        Node centerNode = this.getCenterNode();
        this.moveNode(centerNode, centerPoint);

        ArrayList<Node> circleNodes = this.getGraphNodes();
        circleNodes.remove(centerNode);

        ArrayList<Edge> shortEdges = this.getEdgesFromPartition(1);
        ArrayList<Edge> longEdges = this.getEdgesFromPartition(2);


        circleNodes.sort(new NodeCounterClockwiseComparator(centerNode, graph2D));
        Collections.reverse(circleNodes);
        String res2 = " SHORT SPOTS";
        for (YPoint spot : shortRealizationSpots) {
            res2 += " " + spot.toString();
        }
        System.out.println(res2);


        /*
        this.changePartitions(circleNodes,longEdges);
        shortEdges = this.getEdgesFromPartition(1);
        longEdges = this.getEdgesFromPartition(2);
        circleNodes.sort(new NodeCounterClockwiseComparator(centerNode, graph2D));
        Collections.reverse(circleNodes);
        */

        int numLongEdgesCircle = 0;//this.numCircleLongEdges(circleNodes, longEdges);

        //  SHORT SPOTS X: 1175.0 Y: 500.0 X: 800.0000000000001 Y: 716.5063509461096 X: 799.9999999999999 Y: 283.49364905389

        int currentLine = 0;
        String res = "SORTIERUNG: ";
        System.out.println("LONG EDGES: " + longEdges + " size: " + longEdges.size());
        for (Node node : circleNodes) {
            res += " " + node.index();
        }
        System.out.println(res);
        if (numLongEdgesCircle % 2 == 0) {

            Node firstNode = circleNodes.get(0);
            Edge edgeCenterFirst = this.getEdgeFromVertex(centerNode, firstNode);
            if (longEdges.contains(edgeCenterFirst)) {
                YPoint realizationSpot = this.longRealizationSpots.get(currentLine);
                YPoint newDestination = this.calculateNeibourhood(currentLine, offsetHelpLines, realizationSpot);
                moveNode(firstNode, newDestination);
            } else {
                YPoint realizationSpot = this.shortRealizationSpots.get(currentLine);
                YPoint newDestination = this.calculateNeibourhood(currentLine, offsetHelpLines, realizationSpot);
                moveNode(firstNode, newDestination);
            }

            for (int i = 1; i < circleNodes.size(); i++) {

                for (Edge e : longEdges) {
                    System.out.println(e.toString());
                }

                Node currentCircleNode = circleNodes.get(i);

                Node nextCircleNode = circleNodes.get(i - 1);
                if (i + 1 < circleNodes.size()) {
                    //nextCircleNode = circleNodes.get(i-1);
                } else {
                    // nextCircleNode = circleNodes.get(0);
                }

                Edge edgeCircle = this.getEdgeFromVertex(currentCircleNode, nextCircleNode);
                System.out.println("EDGE " + edgeCircle.toString() + " with partition " + this.graphView.getRelationships().getPartitionIndexEdge(edgeCircle));
                Edge edgeCenterCurrent = this.getEdgeFromVertex(centerNode, currentCircleNode);

                System.out.println("Node INDEX: " + currentCircleNode.index());

                if (longEdges.contains(edgeCircle)) {
                    System.out.println("wechsel");
                    currentLine = (currentLine + 1) % 2;
                } else if (shortEdges.contains(edgeCircle)) {

                }

                if (longEdges.contains(edgeCenterCurrent)) {
                    YPoint realizationSpot = this.longRealizationSpots.get(currentLine);
                    YPoint newDestination = this.calculateNeibourhood(currentLine, offsetHelpLines, realizationSpot);
                    System.out.println("NEW DESTINATION " + newDestination + " ON HELPLINE " + currentLine);
                    moveNode(currentCircleNode, newDestination);
                } else {
                    YPoint realizationSpot = this.shortRealizationSpots.get(currentLine);
                    YPoint newDestination = this.calculateNeibourhood(currentLine, offsetHelpLines, realizationSpot);
                    System.out.println("NEW DESTINATION " + newDestination + " ON HELPLINE " + currentLine);
                    moveNode(currentCircleNode, newDestination);
                }
                offsetHelpLines[currentLine] = offsetHelpLines[currentLine] + 1;


            }


        }

       else if( numLongEdgesCircle % 2 == 1 && numLongEdgesCircle > 1){
            Edge[] lastEdges = this.last2EdgesInCircle(circleNodes,longEdges);
            Edge secondLatestEdge = lastEdges[0];
            Edge lastEdge = lastEdges[1];

            Node firstNode = circleNodes.get(0);
            Edge edgeCenterFirst = this.getEdgeFromVertex(centerNode, firstNode);
            if (longEdges.contains(edgeCenterFirst)) {
                YPoint realizationSpot = this.longRealizationSpots.get(currentLine);
                YPoint newDestination = this.calculateNeibourhood(currentLine, offsetHelpLines, realizationSpot);
                moveNode(firstNode, newDestination);
            } else {
                YPoint realizationSpot = this.shortRealizationSpots.get(currentLine);
                YPoint newDestination = this.calculateNeibourhood(currentLine, offsetHelpLines, realizationSpot);
                moveNode(firstNode, newDestination);
            }

            boolean flag = false;
            for (int i = 1; i < circleNodes.size(); i++) {
                Node currentCircleNode = circleNodes.get(i);
                Node nextCircleNode = circleNodes.get(i - 1);

                Edge edgeCircle = this.getEdgeFromVertex(currentCircleNode, nextCircleNode);
                Edge edgeCenterCurrent = this.getEdgeFromVertex(centerNode, currentCircleNode);


                if( edgeCircle.index() == secondLatestEdge.index()){
                    currentLine = 2;
                    flag=true;
                }
                else if( edgeCircle.index() == lastEdge.index()){
                    currentLine = 0;
                }
                else {
                    if (longEdges.contains(edgeCircle) && !flag) {
                        System.out.println("wechsel");
                        currentLine = (currentLine + 1) % 2;
                    }
                }
                if (longEdges.contains(edgeCenterCurrent)) {
                    YPoint realizationSpot = this.longRealizationSpots.get(currentLine);
                    YPoint newDestination = this.calculateNeibourhood(currentLine, offsetHelpLines, realizationSpot);
                    moveNode(currentCircleNode, newDestination);
                } else {
                    YPoint realizationSpot = this.shortRealizationSpots.get(currentLine);
                    YPoint newDestination = this.calculateNeibourhood(currentLine, offsetHelpLines, realizationSpot);
                                        moveNode(currentCircleNode, newDestination);
                }
                offsetHelpLines[currentLine] = offsetHelpLines[currentLine] + 1;

                }

            }


        else if( numLongEdgesCircle % 2 == 1 && numLongEdgesCircle == 1){
            //Edge[] lastEdges = this.last2EdgesInCircle(circleNodes,longEdges);
           // Edge lastEdge = lastEdges[0];


            circleNodes = this.sortCircleNodes(circleNodes,longEdges);
            System.out.println("SORTED: "+circleNodes);
            //Collections.sort(circleNodes,new NodePartitionComparator(this.graphView));

            YPoint destination;
            boolean firstNodeCenterLong = false;
            Node firstNode = circleNodes.get(0);
            Edge edgeCenterFirst = this.getEdgeFromVertex(centerNode, firstNode);
            if (longEdges.contains(edgeCenterFirst)) {
                YPoint realizationSpot = this.longRealizationSpots.get(0);
                YPoint newDestination = this.calculateNeibourhood(0, offsetHelpLines, realizationSpot);
                destination = newDestination;
                firstNodeCenterLong = true;
                moveNode(firstNode, newDestination);
            } else {
                YPoint realizationSpot = this.shortRealizationSpots.get(0);
                YPoint newDestination = this.calculateNeibourhood(0, offsetHelpLines, realizationSpot);
                destination = newDestination;
                moveNode(firstNode, newDestination);
            }

            boolean secondNodeCenterLong = false;
            Node secondNode = circleNodes.get(1);
            Edge edgeCenterSecond = this.getEdgeFromVertex(centerNode, secondNode);
            if (longEdges.contains(edgeCenterSecond)) {
                secondNodeCenterLong = true;
            }

            YPoint destinationThirdVertex;
            boolean thirdNodeCenterLong = false;
            Node thirdNode = circleNodes.get(2);
            Edge edgeCenterThird= this.getEdgeFromVertex(centerNode, thirdNode);
            if (longEdges.contains(edgeCenterThird)) {
                YPoint realizationSpot = this.longRealizationSpots.get(1);
                YPoint newDestination = this.calculateNeibourhood(1, offsetHelpLines, realizationSpot);
                destinationThirdVertex = newDestination;
                thirdNodeCenterLong = true;
                moveNode(thirdNode, newDestination);
            } else {
                YPoint realizationSpot = this.shortRealizationSpots.get(1);
                YPoint newDestination = this.calculateNeibourhood(1, offsetHelpLines, realizationSpot);
                destinationThirdVertex = newDestination;
                moveNode(thirdNode, newDestination);
            }


            if(  (firstNodeCenterLong && !thirdNodeCenterLong) || (!firstNodeCenterLong && thirdNodeCenterLong) ) {
                YPoint[] intersections = this.calculateCircleCircleIntersection(destination, LENGTH_LONG_EDGE-5, destinationThirdVertex, LENGTH_LONG_EDGE-5);
                System.out.println(intersections[0] + " " + intersections[1]);
                if (secondNodeCenterLong) {
                    System.out.println("hello");
                    moveNode(secondNode, intersections[0]);
                } else {
                    moveNode(secondNode, intersections[1]);
                }
            }
            else{
                double tempAngle = Math.toRadians(60);
                double tempX =  ( LENGTH_LONG_EDGE * Math.cos(tempAngle) ) + centerPoint.getX() ;
                double tempY =   ( LENGTH_LONG_EDGE * Math.sin(tempAngle) ) + centerPoint.getY()  ;
                YPoint tempPoint = new YPoint(tempX,tempY);
                AffineLine line = new AffineLine(centerPoint,tempPoint);

                YPoint[] intersections = this.calculateIntersectionCircleLine(destination.getX(),destination.getY(),LENGTH_LONG_EDGE,line);

                if (secondNodeCenterLong) {
                    System.out.println("hello");
                    moveNode(secondNode, intersections[1]);
                } else {
                    moveNode(secondNode, intersections[0]);
                }
            }

            currentLine = 1;
            for (int i = 3; i < circleNodes.size(); i++) {

                Node currentCircleNode = circleNodes.get(i);
                Node nextCircleNode = circleNodes.get(i - 1);
                if( i+1 == circleNodes.size()){
                    nextCircleNode = circleNodes.get(0);
                    currentLine = 1;
                }

                Edge edgeCircle = this.getEdgeFromVertex(currentCircleNode, nextCircleNode);
                Edge edgeCenterCurrent = this.getEdgeFromVertex(centerNode, currentCircleNode);

                if (longEdges.contains(edgeCenterCurrent)) {
                    YPoint realizationSpot = this.longRealizationSpots.get(currentLine);
                    YPoint newDestination = this.calculateNeibourhood(currentLine, offsetHelpLines, realizationSpot);
                    moveNode(currentCircleNode, newDestination);
                } else {
                    YPoint realizationSpot = this.shortRealizationSpots.get(currentLine);
                    YPoint newDestination = this.calculateNeibourhood(currentLine, offsetHelpLines, realizationSpot);
                    moveNode(currentCircleNode, newDestination);
                }
                offsetHelpLines[currentLine] = offsetHelpLines[currentLine] + 1;

            }



        }

        this.graphView.getRelationshipsView().updateEdgeColoring();

    }



    private void genericCircleVertexPlacement( int start, int stop, int initialHelpLine){

        int currentLine = initialHelpLine;
        int numCircleNodes = this.circleNodes.size();

        for (int i = start; i < stop; i++) {

            Node currentCircleNode = (Node) circleNodes.get(i);
            Node previousCircleNode = (Node) circleNodes.get(i-1);


            Edge edgeCircle = currentCircleNode.getEdge(previousCircleNode);
            Edge edgeCenterCurrent = currentCircleNode.getEdge(centerNode);


            if (longEdges.contains(edgeCircle)) {
                currentLine = (currentLine + 1) % 2;

            }

            if (longEdges.contains(edgeCenterCurrent)) {
                YPoint realizationSpot = this.longRealizationSpots.get(currentLine);
                this.basicGraph.moveNode(currentCircleNode, realizationSpot);

                if( currentLine == 0){
                    this.longNodesSpot1.add(currentCircleNode);
                }
                else if( currentLine == 1){
                    this.longNodesSpot2.add(currentCircleNode);
                }

            } else if( shortEdges.contains(edgeCenterCurrent)){

                YPoint realizationSpot = this.shortRealizationSpots.get(currentLine);
                this.basicGraph.moveNode(currentCircleNode, realizationSpot);

                if( currentLine == 0){
                    this.smallNodesSpot1.add(currentCircleNode);
                }
                else if( currentLine == 1){
                    this.smallNodesSpot2.add(currentCircleNode);
                }
            }

        }

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

    private Node getCenterNode(){
        ArrayList<Node> graphNodes = this.getGraphNodes();
        for( Node current : graphNodes){
            if( current.degree() == graph2D.nodes().size() -1){
                return current;
            }
        }
        return null;
    }


    private YPoint calculateNeibourhood( int helpLine, double[] offsetHelpLines, YPoint realizationSpot){

        double currentOffset = offsetHelpLines[helpLine] * CLOSED_NEIBOURHOOD_DISTANCE;
        if( currentOffset == 0){
            return realizationSpot;
        }
        AffineLine currentHelpline = this.helpLines.get(helpLine);

        switch (helpLine){
            case 0:{
                return this.calculateIntersectionCircleLine(realizationSpot.getX(),realizationSpot.getY(),currentOffset,currentHelpline)[0];
            }
            case 1:{
                return this.calculateIntersectionCircleLine(realizationSpot.getX(),realizationSpot.getY(),currentOffset,currentHelpline)[0];
            }
            case 2:{
                return this.calculateIntersectionCircleLine(realizationSpot.getX(),realizationSpot.getY(),currentOffset,currentHelpline)[1];
            }

        }
        return null;
    }


    private int numLongEdgesCircle( ArrayList<Edge> circleEdges){
        int result  = 0;
        for( Edge edge: circleEdges){
            if( longEdges.contains(edge)){
                result += 1;
            }
        }
        return result;
    }


    private int numCircleLongEdges2( ArrayList<Node> circleNode, ArrayList<Edge> longEdges){
        int count = 0;
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
                count += 1;
            }
        }
        return count;
    }


    private Node getSecondLastLongEdgeNode(){

        boolean lastLongEdgeDiscovered = false;

        NodeList nodes = this.circleNodes;
        nodes.addLast(nodes.firstNode());

        for( int i=nodes.size()-1 ; i >= 0 ; i--){

            Node currentNode = (Node) nodes.get(i);
            Node previousNode = (Node) nodes.get(i-1);

            Edge currentCircleEdge = currentNode.getEdge(previousNode);

            if( longEdges.contains(currentCircleEdge)){
                if( lastLongEdgeDiscovered){
                    return previousNode;
                }
                else{
                    lastLongEdgeDiscovered = true;
                }
            }
        }
        return null;
    }


    private Node[] getLongEdge(){
        for( int i = 0; i < circleNodes.size(); i++){
            Node currentNode = (Node) circleNodes.get(i);
            Node nextNode;

            if( i+1 == circleNodes.size()){
                nextNode = (Node) circleNodes.get(0);
            }
            else{
                nextNode = (Node) circleNodes.get(i+1);
            }

            Edge currentCircleEdge = currentNode.getEdge(nextNode);

            if( longEdges.contains(currentCircleEdge)){
                return new Node[]{currentNode,nextNode};
            }
        }
        return null;
    }

    private NodeList calculateNodeOrderCase3(){

        Node[] longEdge = getLongEdge();

        Node sourceLong = longEdge[0];
        Node targetLong = longEdge[1];

        int sourceLongIndex = circleNodes.indexOf(sourceLong);
        int targetLongIndex = circleNodes.indexOf(targetLong);


        int indexDesiredNode = -1;

        for( int i=0; i < circleNodes.size(); i++){
            if( i == sourceLongIndex || i== targetLongIndex){
                continue;
            }


            Node currentNode = (Node) circleNodes.get(i);
            Node previousNode = null;
            Node nextNode = null;

            if( i+1 == circleNodes.size()){
                previousNode = (Node) circleNodes.get(i-1);
                nextNode = (Node) circleNodes.get(0);
            }
            else if( i-1 < 0){
                previousNode = (Node) circleNodes.get(circleNodes.size()-1);
                nextNode = (Node) circleNodes.get(i+1);
            }
            else{
                previousNode = (Node) circleNodes.get(i-1);
                nextNode = (Node) circleNodes.get(i+1);

            }

            Edge centerCurrentNode = centerNode.getEdge(currentNode);
            Edge centerPreviousNode = centerNode.getEdge(previousNode);
            Edge centerNextNode = centerNode.getEdge(nextNode);

            boolean centerCurrentNodeLong = longEdges.contains(centerCurrentNode);
            boolean centerPreviousNodeLong = longEdges.contains(centerPreviousNode);
            boolean centerNextNodeLong = longEdges.contains(centerNextNode);

            if( !centerCurrentNodeLong && !centerNextNodeLong && !centerPreviousNodeLong){
                indexDesiredNode = i;
            }
        }


        int numShifts = (circleNodes.size()-1) - indexDesiredNode;
        NodeList result = circleNodes;


        for( int i=0; i < numShifts; i++){
            Node lastNode = (Node) result.popLast();
            result.addFirst(lastNode);
        }


        return result;

        /*
        Node longNode = getLongNode();
        int longNodeIndex = circleNodes.indexOf(longNode);

        if( longNodeIndex < circleNodes.size()-2){
            return circleNodes;
        }
        else if( longNodeIndex == circleNodes.size()-2){
            NodeList result = circleNodes;
            Node insertLast = (Node) circleNodes.pop();
            result.addLast(insertLast);
            return result;
        }
        else{
            NodeList result = circleNodes;
            Node last = (Node) circleNodes.pop();
            Node secondLast = (Node) circleNodes.pop();
            result.addLast(last);
            result.addLast(secondLast);
            return result;
        }
        */
    }


    private Edge[] last2EdgesInCircle(ArrayList<Node> circleNode, ArrayList<Edge> longEdges){

        Edge secondLatestEdge = null;
        Edge latestEdge = null;

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
                if(secondLatestEdge == null){
                    secondLatestEdge = currentEdge;
                }
                else if( latestEdge == null){
                    latestEdge = currentEdge;
                }
                else{
                    secondLatestEdge = latestEdge;
                    latestEdge = currentEdge;
                }
            }
        }

        return new Edge[]{secondLatestEdge,latestEdge};
    }

    private boolean isWheelGraph(){

        Node maxVertex = null;
        boolean first = false;

        for( Node node: nodes(graph2D)){



            if( node.degree() == graph2D.nodeCount()-1){
                if( first){
                    return false;
                }
                else {
                    maxVertex = node;
                    first = true;
                }
            }
        }

        if( maxVertex == null){
            return false;
        }
        else{
            for( Node node: nodes(graph2D)){
                if( node.index() == maxVertex.index()){
                    continue;
                }
                else{
                    if( node.degree() != 3){
                        return false;
                    }
                }
            }
            return true;
        }
    }

    @Override
    public boolean isGraphType() {

        if( graph2D.nodeCount() >= 4){
            if( isDichotomos()){
                if( isWheelGraph()){
                    return true;
                }
                else{
                    RealizationErrorView error = new RealizationErrorView(RealizationError.ERROR_NOT_WHEEL);
                    return false;
                }
            }
            else{
                RealizationErrorView error = new RealizationErrorView(RealizationError.ERROR_PARTITION);
                return false;
            }
        }
        else{
            RealizationErrorView error = new RealizationErrorView(RealizationError.ERROR_SIZE);
            return false;
        }

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
        Collections.rotate(result,circleNode.size()-index-1);
        return result;
    }


}

