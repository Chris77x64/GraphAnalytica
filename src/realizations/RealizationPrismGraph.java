package realizations;

import algo.*;
import graphClasses.HanoiGraph;
import graphClasses.PrismGraph;
import model.EdgeInducedSubgraph;
import model.MetricCollection;
import view.GraphView;
import y.algo.Cycles;
import y.algo.Paths;
import y.base.*;
import y.geom.AffineLine;
import y.geom.YPoint;
import y.util.Maps;

import java.util.ArrayList;
import java.util.Collections;

import static y.util.Generics.nodes;

/**
 * Created by chris on 01.02.2018.
 */
public class RealizationPrismGraph extends Realization{

    private final double CYCLE_RADIUS = 300;

    private final double LENGTH_SHORT_EDGES = 300;
    private final double LENGTH_LONG_EDGES = 400;

    private NodeList innerCycle;
    private NodeList outerCycle;

    private NodeMap shortRealizationSpots;
    private NodeMap longRealizationSpots;

    private EdgeInducedSubgraph longEdgesInducedSubgraph;
    private Graph inducedSubgraph;

    private NodeList vertexOrder;

    private NodeMap cycleMapping;

    private ArrayList<YPoint> realizationSpots;

    private YPoint topLeftLong;
    private YPoint topLeftShort;
    private YPoint topRightLong;
    private YPoint topRightShort;

    private YPoint leftLeftLong;
    private YPoint leftLeftShort;
    private YPoint leftRightLong;
    private YPoint leftRightShort;

    private YPoint botLeftLong;
    private YPoint botLeftShort;
    private YPoint botRightLong;
    private YPoint botRightShort;

    private YPoint rightLeftLong;
    private YPoint rightLeftShort;
    private YPoint rightRightLong;
    private YPoint rightRightShort;

    private ArrayList<Edge> shortEdges;
    private ArrayList<Edge> longEdges;

    public RealizationPrismGraph(GraphView graphView) {
        super(graphView);
        //this.initializeInducedSubgraph();


        /*
        if( isGraphType()){

        }
        */


      //  RealizationBipartitGraph realizationBipartitGraph = new RealizationBipartitGraph(graphView);
        //BCTree asd = new BCTree(graph2D);
        YPoint circleCenter = new YPoint(20,10);
        this.graphView.setAddVertexDistanceCircle(circleCenter,100);

        YPoint p2 = new YPoint(40,50);
        YPoint p3 = new YPoint(120,120);

        basicGraph.createNode(circleCenter);
        basicGraph.createNode(p2);
        basicGraph.createNode(p3);

        YPoint[] intersections = calculateIntersectionCircleLine(p3,p2,circleCenter,100);
        basicGraph.createNode(intersections[0]);
        basicGraph.createNode(intersections[1]);


       // HanoiGraph hh = new HanoiGraph(graphView,3);
       // HanoiLabeling hanoiLabeling = new HanoiLabeling(graph2D);
        //PlanarShiftGridDrawing drawing = new PlanarShiftGridDrawing(graphView);
        //CanonicalVertexOrder canonicalVertexOrder = new CanonicalVertexOrder(graph2D);

        //PlanarGraphTriangulation asd = new PlanarGraphTriangulation(graph2D);
        //ArticulationPoints ap = new ArticulationPoints(graph2D);
        //System.out.println(ap.getArticulationPoints());


        // RealizationPrismGraph999999 realizationPrismGraph999999 = new RealizationPrismGraph999999(graphView,true);
      // RealizationPrismGraph2 realizationPrismGraph2 = new RealizationPrismGraph2(graphView);


        /*
        this.initializeAssets();
        this.initializeVertexOrder();

        this.initializeInnerCycle();
        this.initializeOuterCycle();

        this.realizeInnerCycle();
        */


       // this.realizeOuterCycle();


        /*
     double lengthShortEdges = getLengthShortEdges();
        double lengthLongEdges = getLengthLongEdges();
        double length2ndLongEdges = getLength2ndLongEdges();

        /*

        this.initializeAssets();



        this.realizeInnerCycle();
        this.initializeFirstOuterVertex();



        Node firstInnerCycle = (Node) innerCycle.get(0);
        Node secondInnerCycle = (Node) innerCycle.get(1);

        YPoint positionFirstInnerCycle = graph2D.getCenter(firstInnerCycle);
        YPoint positionSecondInnerCycle = graph2D.getCenter(secondInnerCycle);


        double distanceLongShortDifference = Math.abs(lengthLongEdges-lengthShortEdges)+ (0.1 * lengthShortEdges);


        this.realizeOuterCycle();

        */


        //this.graphView.setAddVertexDistanceCircle( positionFirstInnerCycle,lengthShortEdges);
       // this.graphView.setAddVertexDistanceCircle2( positionSecondInnerCycle,length2ndLongEdges);







/*
        YPoint p1 = new YPoint(-0.8333333333333712,297.6666666666667);
        YPoint p2 = new YPoint(149.16666666666663,349.3333333333333);

        YPoint closest = new YPoint(-299.1666666666667,2.6666666666666856);
        YPoint rel = new YPoint(-148.33333333333337,262.6666666666667);

        basicGraph.createNode(p1);
        basicGraph.createNode(p2);
        basicGraph.createNode(rel);
        basicGraph.createNode(closest);


     basicGraph.createNode(calculateClosestPointBiggerCircleCircleIntersection(p1,240.34,p2,155.29,rel,closest,155.29,240.34+155.29));

        this.graphView.setAddVertexDistanceCircle(p1,240.34);
       this.graphView.setAddVertexDistanceCircle2(p2,155.29);
*/



        //basicGraph.createNode(calculateClosestPointBiggerCircleCircleIntersection(p2,300,p1,155.29,rel));


      //  String result = " ";
       // for( NodeCursor v = vertexOrder.nodes() ; v.ok() ; v.next()){
       //     result += " "+v.node();
       // }
      //  System.out.println(result);

     //   this.initializeInnerCycle();
        /*
        this.initializeOuterCycle();

        this.initializeAssets();
        System.out.println(calculateNumLongEdgesInnerCycle());
        this.realizeInnerCycle();
        this.realizeOuterCycle();

*/
        /*
        this.initializeRealizationSpots();


        this.calculateRealization();
        */

        /*
        Node topLeft = (Node) vertexOrder.get(0);
        Node botLeft = (Node) vertexOrder.get(1);
        Node topRight = (Node) vertexOrder.get(2);
        Node botRight = (Node) vertexOrder.get(3);

        this.placeFirstTwoVertices(topLeft,botLeft);
        this.genericPlacement(topLeft,botLeft,topRight,botRight);
        */

        /*
        YPoint p1 = new YPoint(2.69999999999993,-3.400000000000034);
        YPoint p2 = calculateIntersectionUpSide(p1,-20,500);
        //  YPoint p2=  new YPoint(-152.10000000000002,260.6);

        //YPoint relatedPoint = new YPoint(-496.5,298.5);

        this.graphView.setAddVertexDistanceCircle(p1, 300);
       // this.graphView.setAddVertexDistanceCircle2(p2,300);
        this.graphView.setAddVertexDistanceCircle2(p2,500);
        YPoint relatedPoint = new YPoint(-147.30000000000007,-257.8);

        basicGraph.resetGraph();
        basicGraph.createNode(calculateClosestPointBiggerCircleCircleIntersection(p2,500,p1,300,relatedPoint));
    //    basicGraph.createNode(calculateClosestPointCircleCircleIntersection(p1,300,p2,300,relatedPoint));


        basicGraph.createNode(p1);
        basicGraph.createNode(p2);
        //basicGraph.createNode(relatedPoint);

        */

    }

    private DataMap createInnerCycleMapping(){

        int currentIndex = 1;

        DataMap result = Maps.createHashedDataMap();


        for( int i = 0; i < innerCycle.size(); i++){
            Node currentNode = (Node) innerCycle.get(i);
            Node nextNode;

            if( i+1 == innerCycle.size()){
                nextNode = (Node) innerCycle.get(0);
            }
            else{
                nextNode = (Node) innerCycle.get(i+1);
            }

            Edge currentEdge = currentNode.getEdge(nextNode);

            if( longEdges.contains(currentEdge)){
                result.set(currentIndex,currentNode);
                currentIndex += 2;
            }
            else if( shortEdges.contains(currentEdge)){
                result.set(currentIndex,currentNode);
                currentIndex += 1;
            }
        }

        return result;
    }

    private void realizeOuterCycle(){

        Node firstOuterVertex = (Node) outerCycle.get(0);
        Node firstInnerVertex = (Node) innerCycle.get(0);

        boolean firstInnerFirstOuterLong = isEdgeLong(firstInnerVertex,firstOuterVertex);

        if( firstInnerFirstOuterLong){
            YPoint destination = (YPoint) longRealizationSpots.get(firstInnerVertex);
            basicGraph.moveNode(firstOuterVertex,destination);
        }
        else{
            YPoint destination = (YPoint) shortRealizationSpots.get(firstInnerVertex);
            basicGraph.moveNode(firstOuterVertex,destination);
        }

        double lengthLongEdges = getLengthLongEdges();
        double lengthShortEdges = getLengthShortEdges();
        double lengthLongestShortEdge = getLengthLongestShortEdge();

        for( int i= 1; i < outerCycle.size(); i++) {

                Node previousOuterNode = (Node) outerCycle.get(i - 1);
                Node currentOuterNode = (Node) outerCycle.get(i);

                Node currentInnerNode = (Node) innerCycle.get(i);

                boolean previousOuterCurrentOuterLong = isEdgeLong(previousOuterNode, currentOuterNode);
                boolean currentOuterCurrentInnerLong = isEdgeLong(currentOuterNode, currentInnerNode);

                if (previousOuterCurrentOuterLong) {

                    if (currentOuterCurrentInnerLong) {
                        YPoint destination = (YPoint) longRealizationSpots.get(currentInnerNode);
                        basicGraph.moveNode(currentOuterNode, destination);
                    } else {
                        YPoint destination = (YPoint) shortRealizationSpots.get(currentInnerNode);
                        basicGraph.moveNode(currentOuterNode, destination);
                    }
                } else {

                    YPoint positionCurrentInner = graph2D.getCenter(currentInnerNode);
                    YPoint positionPreviousOuter = graph2D.getCenter(previousOuterNode);

                    if (currentOuterCurrentInnerLong) {
                        YPoint[] intersections = calculateCircleCircleIntersection(positionPreviousOuter, lengthLongestShortEdge, positionCurrentInner, lengthLongEdges);
                        YPoint destination = intersections[1];
                        System.out.println(destination);
                           basicGraph.moveNode(currentOuterNode, destination);
                    } else {
                        YPoint[] intersections = calculateCircleCircleIntersection(positionPreviousOuter, lengthLongestShortEdge, positionCurrentInner, lengthShortEdges);
                        YPoint destination = intersections[1];
                        System.out.println(destination);
                        basicGraph.moveNode(currentOuterNode, destination);
                    }


            }
        }

        /*
        this.initializeFirstOuterVertex();

        double lengthShortEdges = getLengthShortEdges();
        double lengthLongEdges = getLengthLongEdges();
        double length2ndLongEdges = getLength2ndLongEdges();

        for( int i=1 ; i < outerCycle.size() ; i++) {
            if (i + 2 < outerCycle.size()) {

                Node currentOuterNode = (Node) outerCycle.get(i);
                Node previousOuterNode = (Node) outerCycle.get(i - 1);
                Node nextOuterNode = (Node) outerCycle.get(i+1);

                Node currentInnerNode = (Node) innerCycle.get(i);
                Node nextInnerNode = (Node) innerCycle.get(i+1);
                Node nextNextInnerNode = (Node) innerCycle.get(i+2);

                boolean topLeftTopRightLong = isEdgeLong(previousOuterNode, currentOuterNode);
                boolean topRightBotRightLong = isEdgeLong(currentInnerNode, currentOuterNode);

                YPoint destination = new YPoint(0,0);

                YPoint positionPreviousOuter = graph2D.getCenter(previousOuterNode);
                YPoint positionCurrentInner = graph2D.getCenter(currentInnerNode);
                YPoint positionNextInner = graph2D.getCenter(nextInnerNode);
                YPoint positionNextNextInner = graph2D.getCenter(nextNextInnerNode);

                boolean currentOuterNextOuterLong = isEdgeLong(currentOuterNode,nextOuterNode);
                boolean nextInnerNextOuterLong = isEdgeLong(nextInnerNode,nextOuterNode);


                if( topLeftTopRightLong && topRightBotRightLong){
                    /*
                    YPoint[] intersection = calculateCircleCircleIntersection(positionPreviousOuter,lengthLongEdges,positionCurrentInner,lengthLongEdges);
                    YPoint intersection1 = intersection[0];
                    YPoint intersection2 = intersection[1];

                    double distClosestPointIntersection1 = MetricCollection.euklideanDistanceR2(intersection1.x,intersection1.y,closestPoint.x,closestPoint.y);
                    double distClosestPointIntersection2 = MetricCollection.euklideanDistanceR2(intersection2.x,intersection2.y,closestPoint.x,closestPoint.y);

                    if( distClosestPointIntersection1 < distClosestPointIntersection2){
                        destination = intersection1;
                    }
                    else{
                        destination = intersection2;
                    }
                    */
        /*
                }
                else if( topLeftTopRightLong && !topRightBotRightLong){

                    double lowerBound;
                    double upperBound;

                    if( nextInnerNextOuterLong && currentOuterNextOuterLong){
                        lowerBound = 1;
                        upperBound = length2ndLongEdges+length2ndLongEdges;
                    }
                    else if( (nextInnerNextOuterLong && !currentOuterNextOuterLong) || ( !nextInnerNextOuterLong && currentOuterNextOuterLong) ){
                        lowerBound = lengthShortEdges;
                        upperBound = lengthShortEdges+length2ndLongEdges;
                    }
                    else{
                        lowerBound = 1;
                        upperBound = lengthShortEdges+lengthShortEdges;
                    }

                    destination = calculateClosestPointBiggerCircleCircleIntersection(positionPreviousOuter,length2ndLongEdges,positionCurrentInner,lengthShortEdges,positionNextInner,positionNextNextInner,lowerBound,upperBound);
                }
                else if( !topLeftTopRightLong && topRightBotRightLong ){

                    double lowerBound;
                    double upperBound;

                    if( nextInnerNextOuterLong && currentOuterNextOuterLong){
                        lowerBound = 1;
                        upperBound = length2ndLongEdges+length2ndLongEdges;
                    }
                    else if( (nextInnerNextOuterLong && !currentOuterNextOuterLong) || ( !nextInnerNextOuterLong && currentOuterNextOuterLong) ){
                        lowerBound = lengthShortEdges;
                        upperBound = lengthShortEdges+length2ndLongEdges;
                    }
                    else{
                        lowerBound = 1;
                        upperBound = lengthShortEdges+lengthShortEdges;
                    }

                    destination = calculateClosestPointBiggerCircleCircleIntersection(positionCurrentInner,length2ndLongEdges,positionPreviousOuter,lengthShortEdges,positionNextInner,positionNextNextInner,lowerBound,upperBound);
                }
                else{
                    //destination = calculateClosestPointCircleCircleIntersection(positionPreviousOuter,lengthShortEdges,positionCurrentInner,lengthShortEdges,closestPoint,relativePoint,lengthLongEdges,lengthShortEdges);
                }

              //  System.out.println(destination);
                this.basicGraph.moveNode(currentOuterNode,destination);
            }
        }
        */
        /*
        for( int i=1 ; i < outerCycle.size() ; i++) {

            if (i + 1 < outerCycle.size()) {

                Node currentOuterNode = (Node) outerCycle.get(i);
                Node previousOuterNode = (Node) outerCycle.get(i - 1);

                Node currentInnerNode = (Node) innerCycle.get(i);
                Node previousInnerNode = (Node) innerCycle.get(i-1);
                Node nextInnerNode = (Node) innerCycle.get(i+1);


                YPoint positionCurrentInnerNode = graph2D.getCenter(currentInnerNode);
                YPoint positionNextInnerNode = graph2D.getCenter(nextInnerNode);
                YPoint positionPreviousOuterNode = graph2D.getCenter(previousOuterNode);

                double currentInnerNodeX = positionCurrentInnerNode.getX();
                double currentInnerNodeY = positionCurrentInnerNode.getY();
                double nextInnerNodeX = positionNextInnerNode.getX();
                double nextInnerNodeY = positionNextInnerNode.getY();

                YPoint destination;

                boolean topLeftTopRightLong = isEdgeLong(previousOuterNode, currentOuterNode);
                boolean topRightBotRightLong = isEdgeLong(currentInnerNode, currentOuterNode);
                boolean botLeftBotNextLong = isEdgeLong(currentInnerNode,nextInnerNode);

                if( nextInnerNodeX < currentInnerNodeX && nextInnerNodeY < currentInnerNodeY){

                    if( !topLeftTopRightLong && !topRightBotRightLong && !botLeftBotNextLong){
                        double distance = lengthLongEdges-1;
                        YPoint[] intersection = calculateCircleCircleIntersection(positionPreviousOuterNode,distance,positionCurrentInnerNode,distance);
                        destination = intersection[0];
                        this.basicGraph.moveNode(currentOuterNode,destination);
                        break;
                    }

                }



            /*
            if( topLeftTopRightLong && topRightBotRightLong){

                YPoint[] intersection = calculateCircleCircleIntersection(positionPreviousOuterNode,lengthLongEdges,positionCorrespondingInnerNode,lengthLongEdges);
                double dist1 = MetricCollection.euklideanDistanceR2(positionCorrespondingInnerNode.x,positionCorrespondingInnerNode.y,intersection[0].x,intersection[0].y);
                double dist2 = MetricCollection.euklideanDistanceR2(positionCorrespondingInnerNode.x,positionCorrespondingInnerNode.y,intersection[1].x,intersection[1].y);
                if( dist1 <= dist2) {
                    destination = intersection[0];
                }
                else{
                    destination = intersection[1];
                }
            }
            else if( topLeftTopRightLong && !topRightBotRightLong){
                YPoint[] intersection = calculateCircleCircleIntersection(positionPreviousOuterNode,lengthLongEdges,positionCorrespondingInnerNode,lengthShortEdges);
                double dist1 = MetricCollection.euklideanDistanceR2(positionCorrespondingInnerNode.x,positionCorrespondingInnerNode.y,intersection[0].x,intersection[0].y);
                double dist2 = MetricCollection.euklideanDistanceR2(positionCorrespondingInnerNode.x,positionCorrespondingInnerNode.y,intersection[1].x,intersection[1].y);
                if( dist1 <= dist2) {
                    destination = intersection[0];
                }
                else{
                    destination = intersection[1];
                }

            }

            else if( !topLeftTopRightLong && topRightBotRightLong ){
                YPoint[] intersection = calculateCircleCircleIntersection(positionPreviousOuterNode,lengthShortEdges,positionCorrespondingInnerNode,lengthLongEdges);
                double dist1 = MetricCollection.euklideanDistanceR2(positionCorrespondingInnerNode.x,positionCorrespondingInnerNode.y,intersection[0].x,intersection[0].y);
                double dist2 = MetricCollection.euklideanDistanceR2(positionCorrespondingInnerNode.x,positionCorrespondingInnerNode.y,intersection[1].x,intersection[1].y);
                if( dist1 <= dist2) {
                    destination = intersection[0];
                }
                else{
                    destination = intersection[1];
                }


            }
            else{
                YPoint[] intersection = calculateCircleCircleIntersection(positionPreviousOuterNode,lengthShortEdges,positionCorrespondingInnerNode,lengthShortEdges);
                double dist1 = MetricCollection.euklideanDistanceR2(positionCorrespondingInnerNode.x,positionCorrespondingInnerNode.y,intersection[0].x,intersection[0].y);
                double dist2 = MetricCollection.euklideanDistanceR2(positionCorrespondingInnerNode.x,positionCorrespondingInnerNode.y,intersection[1].x,intersection[1].y);
                if( dist1 <= dist2) {
                    destination = intersection[0];
                }
                else{
                    destination = intersection[1];
                }
            }

            System.out.println(destination);
            this.basicGraph.moveNode(currentOuterNode,destination);

            }
        }
        */

    }

    private double getLengthLongestShortEdge(){
        int numLongEdges = calculateNumLongEdgesInnerCycle();
        int n = this.innerCycle.size() + numLongEdges;
        return 2 * CYCLE_RADIUS * Math.sin( Math.toRadians(270/ (double) n));
    }

    private double getLengthShortEdges(){
        int numLongEdges = calculateNumLongEdgesInnerCycle();
        int n = this.innerCycle.size() + numLongEdges;
        return 2 * CYCLE_RADIUS * Math.sin( Math.toRadians(180/ (double) n));
    }

    private double getLengthLongEdges(){
        int numLongEdges = calculateNumLongEdgesInnerCycle();
        int n = this.innerCycle.size() + numLongEdges;
        return 2 * CYCLE_RADIUS * Math.sin( Math.toRadians(360/ (double) n));
    }

    private double getLength2ndLongEdges(){
        int numLongEdges = calculateNumLongEdgesInnerCycle();
        int n = this.innerCycle.size() + numLongEdges;
        return 2 * CYCLE_RADIUS * Math.sin( Math.toRadians(270/ (double) n));
    }

    private void realizeInnerCycle(){

        this.shortRealizationSpots = Maps.createHashedNodeMap();
        this.longRealizationSpots = Maps.createHashedNodeMap();

        int numLongEdges = calculateNumLongEdgesInnerCycle();
        int n = this.innerCycle.size() + numLongEdges;
        DataMap indexNodeMapping = createInnerCycleMapping();

        double alpha = 360 / (double) n;
        double currentAngle = 0;
        double currentAngleClockwise = 180-alpha;

        double distance = CYCLE_RADIUS;
        YPoint origin = new YPoint(0,0);

        double lengthLongEdges = getLengthLongEdges();
        double lengthShortEdges = getLengthShortEdges();

        for( int i=1 ; i <= n ; i++){

            YPoint destination;

            if( currentAngle > 180) {

                double tempAngle = Math.toRadians(currentAngleClockwise);
                double tempX =  ( distance * Math.cos(tempAngle) ) + origin.x ;
                double tempY =  origin.y - ( distance * Math.sin(tempAngle) )  ;
                destination = new YPoint(tempX,tempY);
            }
            else{

                double tempAngle = Math.toRadians(currentAngle);
                double tempX = origin.x + ( distance * Math.cos(tempAngle) );
                double tempY = origin.y + ( distance * Math.sin(tempAngle) );
                destination = new YPoint(tempX,tempY);
            }

            Node currentNode = (Node) indexNodeMapping.get(i);

            if( currentNode != null) {

                YPoint shortRealizationSpot;
                YPoint longRealizationSpot;

                if( currentAngle > 180) {

                    double tempAngle = Math.toRadians(currentAngleClockwise);

                    double shortX =  ( (distance-lengthShortEdges) * Math.cos(tempAngle) ) + origin.x ;
                    double shortY =  origin.y - ( (distance-lengthShortEdges) * Math.sin(tempAngle) )  ;

                    double longX =  ( (distance-lengthLongEdges) * Math.cos(tempAngle) ) + origin.x ;
                    double longY =  origin.y - ( (distance-lengthLongEdges) * Math.sin(tempAngle) )  ;

                    double short2X =  ( (distance+lengthShortEdges) * Math.cos(tempAngle) ) + origin.x ;
                    double short2Y =  origin.y - ( (distance+lengthShortEdges) * Math.sin(tempAngle) )  ;

                    double long2X =  ( (distance+lengthLongEdges) * Math.cos(tempAngle) ) + origin.x ;
                    double long2Y =  origin.y - ( (distance+lengthLongEdges) * Math.sin(tempAngle) )  ;

                    basicGraph.createNode(new YPoint(shortX,shortY));
                    basicGraph.createNode(new YPoint(short2X,short2Y));
                    basicGraph.createNode(new YPoint(longX,longY));
                    basicGraph.createNode(new YPoint(long2X,long2Y));

                    shortRealizationSpot = new YPoint(shortX,shortY);
                    longRealizationSpot = new YPoint(longX,longY);
                }
                else{

                    double tempAngle = Math.toRadians(currentAngle);
                    double shortX = origin.x + ( (distance-lengthShortEdges) * Math.cos(tempAngle) );
                    double shortY = origin.y + ( (distance-lengthShortEdges) * Math.sin(tempAngle) );

                    double longX = origin.x + ( (distance-lengthLongEdges) * Math.cos(tempAngle) );
                    double longY = origin.y + ( (distance-lengthLongEdges) * Math.sin(tempAngle) );

                    double short2X = origin.x + ( (distance+lengthShortEdges) * Math.cos(tempAngle) );
                    double short2Y = origin.y + ( (distance+lengthShortEdges) * Math.sin(tempAngle) );

                    double long2X = origin.x + ( (distance+lengthLongEdges) * Math.cos(tempAngle) );
                    double long2Y = origin.y + ( (distance+lengthLongEdges) * Math.sin(tempAngle) );

                    basicGraph.createNode(new YPoint(shortX,shortY));
                    basicGraph.createNode(new YPoint(short2X,short2Y));
                    basicGraph.createNode(new YPoint(longX,longY));
                    basicGraph.createNode(new YPoint(long2X,long2Y));

                    shortRealizationSpot = new YPoint(shortX,shortY);
                    longRealizationSpot = new YPoint(longX,longY);
                }
                this.basicGraph.moveNode(currentNode, destination);
                this.shortRealizationSpots.set(currentNode,shortRealizationSpot);
                this.longRealizationSpots.set(currentNode,longRealizationSpot);
            }
            else{
                basicGraph.createNode(destination);

            }

            if( currentAngle > 180){
                currentAngleClockwise = currentAngleClockwise-alpha;
            }
            else{
                currentAngle = currentAngle + alpha;

                if( currentAngle > 180 ) {

                    double finalAngleOffset = 180 - (currentAngle - alpha);
                    currentAngleClockwise -= (alpha-finalAngleOffset);
                    currentAngleClockwise +=alpha;
                }
            }


        }

    }

    private int calculateNumLongEdgesInnerCycle(){
        int result = 0;

        for( int i=0; i < innerCycle.size(); i++){

            Node currentNode = (Node) innerCycle.get(i);
            Node nextNode;

            if( i+1 == innerCycle.size()){
                nextNode = (Node) innerCycle.get(0);
            }
            else{
                nextNode = (Node) innerCycle.get(i+1);
            }

            Edge currentEdge = currentNode.getEdge(nextNode);

            if(  longEdges.contains(currentEdge)){
                result++;
            }
        }

        return result;
    }

    private void initializeOuterCycle(){
        this.outerCycle = new NodeList();
        for( int i=0 ; i < vertexOrder.size(); i +=2 ) {

            Node outerNode = (Node) vertexOrder.get(i);
            outerCycle.add(outerNode);
        }
    }

    private void initializeInnerCycle(){
        this.innerCycle = new NodeList();

        for( int i=1 ; i < vertexOrder.size(); i +=2 ) {

            Node innerNode = (Node) vertexOrder.get(i);
            innerCycle.add(innerNode);
        }
        System.out.println(innerCycle);
    }

    private void  initializeFirstOuterVertex(){

        double lengthShortEdges = getLengthShortEdges();
        double lengthLongEdges = getLengthLongEdges();
        double length2ndLongEdges = getLength2ndLongEdges();

        Node firstInnerVertex = (Node) innerCycle.get(0);
        Node secondInnerVertex = (Node) innerCycle.get(1);
        Node thirdInnerVertex = (Node) innerCycle.get(2);

        Node firstOuterVertex = (Node) outerCycle.get(0);
        Node secondOuterVertex = (Node) outerCycle.get(1);

        boolean secondInnerSecondOuterLong = isEdgeLong(secondInnerVertex,secondOuterVertex);
        boolean firstOuterSecondOuterVertexLong = isEdgeLong(firstOuterVertex,secondOuterVertex);
        boolean firstOuterFirstInnerVertexLong = isEdgeLong(firstOuterVertex,firstInnerVertex);

        YPoint positionFirstInnerVertex = graph2D.getCenter(firstInnerVertex);
        YPoint positionSecondInnerVertex = graph2D.getCenter(secondInnerVertex);
        YPoint positionThirdInnerVertex = graph2D.getCenter(thirdInnerVertex);

        YPoint destination;

        if( firstOuterSecondOuterVertexLong && secondInnerSecondOuterLong){
            if( firstOuterFirstInnerVertexLong) {
                destination = closestCircleCircleIntersectionPointToRelativePoint(positionFirstInnerVertex, length2ndLongEdges, positionSecondInnerVertex, length2ndLongEdges, positionThirdInnerVertex);
            }
            else{
                destination = closestCircleCircleIntersectionPointToRelativePoint(positionFirstInnerVertex, lengthShortEdges, positionSecondInnerVertex, length2ndLongEdges, positionThirdInnerVertex);

            }
        }
        else if ( firstOuterSecondOuterVertexLong && !secondInnerSecondOuterLong || !firstOuterSecondOuterVertexLong && secondInnerSecondOuterLong ){
            double distanceLongShortDifference = lengthShortEdges*1.1;

            if( firstOuterFirstInnerVertexLong){

                boolean firstInnerSecondInnerLong = isEdgeLong(firstInnerVertex,secondInnerVertex);
                boolean secondInnerThirdInnerLong = isEdgeLong(secondInnerVertex,thirdInnerVertex);

                if( !firstInnerSecondInnerLong && !secondInnerThirdInnerLong){
                    destination = closestCircleCircleIntersectionPointToRelativePoint( positionFirstInnerVertex,length2ndLongEdges,positionSecondInnerVertex,distanceLongShortDifference,positionThirdInnerVertex);
                }
                else{
                    destination = closestCircleCircleIntersectionPointToRelativePoint( positionFirstInnerVertex,lengthLongEdges,positionSecondInnerVertex,distanceLongShortDifference,positionThirdInnerVertex);
                }
            }
            else{
                destination = closestCircleCircleIntersectionPointToRelativePoint(positionFirstInnerVertex, lengthShortEdges, positionSecondInnerVertex, distanceLongShortDifference, positionThirdInnerVertex);
            }

        }
        else{

            if( firstOuterFirstInnerVertexLong){

                boolean firstInnerSecondInnerLong = isEdgeLong(firstInnerVertex,secondInnerVertex);
                boolean secondInnerThirdInnerLong = isEdgeLong(secondInnerVertex,thirdInnerVertex);

                if( !firstInnerSecondInnerLong && !secondInnerThirdInnerLong){
                    destination = closestCircleCircleIntersectionPointToRelativePoint( positionFirstInnerVertex,length2ndLongEdges,positionSecondInnerVertex,lengthShortEdges,positionThirdInnerVertex);
                }
                else {
                    destination = closestCircleCircleIntersectionPointToRelativePoint(positionFirstInnerVertex, lengthLongEdges, positionSecondInnerVertex, lengthShortEdges, positionThirdInnerVertex);
                }
            }
            else{
                destination = calculateClosestPointCircleCircleIntersectionWithoutRel(positionFirstInnerVertex,lengthShortEdges,positionSecondInnerVertex,lengthShortEdges,positionThirdInnerVertex);
                //destination = closestCircleCircleIntersectionPointToRelativePoint( positionFirstInnerVertex,lengthShortEdges,positionSecondInnerVertex,lengthShortEdges,positionThirdInnerVertex);

            }

        }

        basicGraph.moveNode(firstOuterVertex,destination);

        /*

        boolean topLeftBotLeftLong = isEdgeLong(topLeft,botLeft);

        double lengthShortEdge = getLengthShortEdges();
        double lengthLongEdge = getLengthLongEdges();

        YPoint destination;


        YPoint positionFirstInnerVertex = graph2D.getCenter(botLeft);
        YPoint positionThirdInnerVertex = graph2D.getCenter(thirdInnerVertex);

        double firstInnerVertexX = positionFirstInnerVertex.getX();
        double firstInnerVertexY = positionFirstInnerVertex.getY();


        AffineLine lineFirstInnerThirdInner = new AffineLine(positionFirstInnerVertex,positionThirdInnerVertex);

        if( topLeftBotLeftLong ){

            YPoint[] intersection = calculateIntersectionCircleLine(firstInnerVertexX,firstInnerVertexY,lengthLongEdge,lineFirstInnerThirdInner);
            YPoint intersection1 = intersection[0];
            YPoint intersection2 = intersection[1];

            double distClosestPointIntersection1 = MetricCollection.euklideanDistanceR2(intersection1.x,intersection1.y,positionThirdInnerVertex.x,positionThirdInnerVertex.y);
            double distClosestPointIntersection2 = MetricCollection.euklideanDistanceR2(intersection2.x,intersection2.y,positionThirdInnerVertex.x,positionThirdInnerVertex.y);

            if( distClosestPointIntersection1 < distClosestPointIntersection2){
                destination = intersection1;
            }
            else{
                destination = intersection2;
            }

            basicGraph.moveNode(topLeft,destination);

        }
        else{
            YPoint[] intersection = calculateIntersectionCircleLine(firstInnerVertexX,firstInnerVertexY,lengthShortEdge,lineFirstInnerThirdInner);

            YPoint intersection1 = intersection[0];
            YPoint intersection2 = intersection[1];

            double distClosestPointIntersection1 = MetricCollection.euklideanDistanceR2(intersection1.x,intersection1.y,positionThirdInnerVertex.x,positionThirdInnerVertex.y);
            double distClosestPointIntersection2 = MetricCollection.euklideanDistanceR2(intersection2.x,intersection2.y,positionThirdInnerVertex.x,positionThirdInnerVertex.y);

            if( distClosestPointIntersection1 < distClosestPointIntersection2){
                destination = intersection1;
            }
            else{
                destination = intersection2;
            }

            basicGraph.moveNode(topLeft,destination);


        }




        /*
        double yOffset = positionBotRight.getY() - positionBotLeft.getY();
        double interiorAngleLong = Math.toDegrees(Math.asin( yOffset / lengthLongEdge));
        double exteriorAngleLong = 180 - interiorAngleLong;

        double interiorAngleShort = Math.toDegrees(Math.asin( yOffset / lengthShortEdge));
        double exteriorAngleShort = 180 - interiorAngleShort;


        if( botLeftBotRightLong && !topLeftBotLeftLong){

            destination = calculateIntersectionDownSide(positionBotLeft,exteriorAngleLong,lengthShortEdge);
            basicGraph.moveNode(topLeft,destination);
        }
        else if( botLeftBotRightLong && topLeftBotLeftLong){

            double distance = lengthLongEdge + ( lengthLongEdge-lengthShortEdge);
            destination = calculateIntersectionDownSide(positionBotLeft,exteriorAngleLong,distance);
            basicGraph.moveNode(topLeft,destination);
        }
        else if( !botLeftBotRightLong && !topLeftBotLeftLong){

            double angle = 150;
            destination = calculateIntersectionDownSide(positionBotLeft,angle,lengthShortEdge);
            basicGraph.moveNode(topLeft,destination);
        }
        else{
            destination = calculateIntersectionDownSide(positionBotLeft,exteriorAngleShort,lengthLongEdge);
            basicGraph.moveNode(topLeft,destination);
        }
        */


    }

    private void transformToWheelGraph(){

        ArrayList<Node> centerNodes = new ArrayList<>();
        ArrayList<Node> outerCycleNodes = new ArrayList<>();

        for( int i=2 ; i < vertexOrder.size(); i +=2 ){

            Node topLeft = (Node) vertexOrder.get(i-2);
            Node botLeft = (Node) vertexOrder.get(i-1);
            Node topRight = (Node) vertexOrder.get(i);
            Node botRight = (Node) vertexOrder.get(i+1);

            boolean topLeftBotLeftLong = isEdgeLong(topLeft,botLeft);
            boolean topLeftTopRightLong = isEdgeLong(topLeft,topRight);
            boolean botLeftBotRightLong = isEdgeLong(botLeft,botRight);
            boolean topRightBotRightLong = isEdgeLong(topRight,botRight);
        }
    }

    private void calculateRealization(){

        Node firstTop = (Node) vertexOrder.get(0);
        Node firstBot = (Node) vertexOrder.get(1);

        this.placeFirstTwoVertices(firstTop,firstBot);


        for( int i= 2 ; i < vertexOrder.size()-1; i +=2 ){
            Node topLeft = (Node) vertexOrder.get(i-2);
            Node botLeft = (Node) vertexOrder.get(i-1);
            Node topRight = (Node) vertexOrder.get(i);
            Node botRight = (Node) vertexOrder.get(i+1);
            System.out.println("TOPLEFT: "+topLeft+" BOTLEFT: "+botLeft+" POS: "+position(topLeft,botLeft));
            this.genericPlacement(topLeft,botLeft,topRight,botRight);

        }

    }

    private void initializeAssets(){
        this.shortEdges = graphView.getRelationships().getEdgesFromPartition(1);
        this.longEdges = graphView.getRelationships().getEdgesFromPartition(2);
    }


    private void initializeRealizationSpots(){
        YPoint center = calculateCenterGraphView();

        double centerX = center.getX();
        double centerY = center.getY();

        double leftX = centerX - (0.5 * LENGTH_LONG_EDGES);
        double rightX = centerX + (0.5 * LENGTH_LONG_EDGES);

        double topY = centerY - (0.5 * LENGTH_LONG_EDGES);
        double botY = centerY + (0.5 * LENGTH_LONG_EDGES);

        this.topLeftLong = new YPoint(leftX,topY);
        this.topRightLong = new YPoint(rightX,topY);

        this.leftLeftLong = new YPoint(leftX,topY);
        this.leftRightLong = new YPoint(leftX,botY);

        this.botLeftLong = new YPoint(leftX,botY);
        this.botRightLong = new YPoint(rightX,botY);

        this.rightLeftLong = new YPoint(rightX,topY);
        this.rightRightLong = new YPoint(rightX,botY);

        double differenceLongShort = LENGTH_LONG_EDGES-LENGTH_SHORT_EDGES;

        this.topLeftShort = new YPoint(leftX+differenceLongShort,topY);
        this.topRightShort = new YPoint(rightX-differenceLongShort,topY);

        this.leftLeftShort = new YPoint(leftX,topY+differenceLongShort);
        this.leftRightShort = new YPoint(leftX,botY-differenceLongShort);

        this.botLeftShort = new YPoint(leftX+differenceLongShort,botY);
        this.botRightShort = new YPoint(rightX-differenceLongShort,botY);

        this.rightLeftShort = new YPoint(rightX,topY+differenceLongShort);
        this.rightRightShort = new YPoint(rightX,botY-differenceLongShort);

        this.realizationSpots = new ArrayList<>();

        realizationSpots.add(topLeftLong);
        realizationSpots.add(topLeftShort);
        realizationSpots.add(topRightLong);
        realizationSpots.add(topRightShort);

        realizationSpots.add(leftLeftLong);
        realizationSpots.add(leftLeftShort);
        realizationSpots.add(leftRightLong);
        realizationSpots.add(leftRightShort);

        realizationSpots.add(botLeftLong);
        realizationSpots.add(botLeftShort);
        realizationSpots.add(botRightLong);
        realizationSpots.add(botRightShort);

        realizationSpots.add(rightLeftLong);
        realizationSpots.add(rightLeftShort);
        realizationSpots.add(rightRightLong);
        realizationSpots.add(rightRightShort);

    }

    private String position( Node topLeft, Node botLeft){

        YPoint positionTopLeft = graph2D.getCenter(topLeft);
        YPoint positionBotLeft = graph2D.getCenter(botLeft);

        ArrayList<YPoint> topRealizationSpots = new ArrayList<>(this.realizationSpots.subList(0,4));
        ArrayList<YPoint> leftRealizationSpots = new ArrayList<>( this.realizationSpots.subList(4,8));
        ArrayList<YPoint> botRealizationSpots = new ArrayList<>(this.realizationSpots.subList(8,12));
        ArrayList<YPoint> rightRealizationSpots = new ArrayList<>( this.realizationSpots.subList(12,16));

        if( topRealizationSpots.contains(positionTopLeft) && topRealizationSpots.contains(positionBotLeft)){
            return "TOP";
        }
        else if( leftRealizationSpots.contains(positionTopLeft) && leftRealizationSpots.contains(positionBotLeft) ){
            return "LEFT";
        }
        else if( botRealizationSpots.contains(positionTopLeft) && botRealizationSpots.contains(positionBotLeft) ){
            return "BOT";
        }
        else if( rightRealizationSpots.contains(positionTopLeft) && rightRealizationSpots.contains(positionBotLeft)){
            return "RIGHT";
        }
        else{
            return "DIAGONAL";
        }

    }

    private boolean isEdgeLong(Node source, Node target){
        Edge edge = source.getEdge(target);
        if( longEdges.contains(edge)){
            return true;
        }
        else{
            return false;
        }
    }


    private void placeFirstTwoVertices(Node topLeft, Node botLeft){
        boolean topLeftBotLeftLong = isEdgeLong(topLeft,botLeft);

        /*
        if( topLeftBotLeftLong){
            this.basicGraph.moveNode(topLeft,this.topRightLong);
            this.basicGraph.moveNode(botLeft,this.topLeftLong);
        }
        else{
            this.basicGraph.moveNode(topLeft,this.topLeftLong);
            this.basicGraph.moveNode(botLeft,this.topRightShort);
        }
        */


        if( topLeftBotLeftLong){
            this.basicGraph.moveNode(topLeft,this.topLeftLong);
            this.basicGraph.moveNode(botLeft,this.topRightLong);
        }
        else{
            this.basicGraph.moveNode(topLeft,this.topLeftLong);
            this.basicGraph.moveNode(botLeft,this.topRightShort);
        }

    }

    private void genericPlacement(  Node topLeft, Node botLeft, Node topRight, Node botRight){
        String positioning = position(topLeft,botLeft);

        boolean topLeftBotLeftLong = isEdgeLong(topLeft,botLeft);
        boolean topLeftTopRightLong = isEdgeLong(topLeft,topRight);
        boolean botLeftBotRightLong = isEdgeLong(botLeft,botRight);
        boolean topRightBotRightLong = isEdgeLong(topRight,botRight);

        YPoint positionTopLeft = graph2D.getCenter(topLeft);
        YPoint positionBotLeft = graph2D.getCenter(botLeft);
        YPoint positionTopRight = null;
        YPoint positionBotRight = null;

        System.out.println(positioning);
        switch (positioning){
            case "TOP":{
                System.out.println("TOPCASE");
                if( topLeftBotLeftLong) {

                    if (!topLeftTopRightLong && botLeftBotRightLong && !topRightBotRightLong) {



                        if( positionTopLeft.equals(this.topLeftLong)){
                            positionTopRight = positionTopLeft;
                            positionBotRight = this.leftRightShort;
                        }
                        else{
                            positionTopRight = positionTopLeft;
                            positionBotRight = this.rightRightShort;
                        }


                    } else if (topLeftTopRightLong && !botLeftBotRightLong && !topRightBotRightLong) {


                        if( positionTopLeft.equals(this.topLeftLong)){
                            positionTopRight =  this.rightRightShort;
                            positionBotRight = positionBotLeft;
                        }
                        else{
                            positionTopRight = this.leftRightShort;
                            positionBotRight = positionBotLeft;
                        }


                    } else if (!topLeftTopRightLong && !botLeftBotRightLong && !topRightBotRightLong) {

                        if( positionTopLeft.equals(this.topLeftLong)){
                            positionTopRight =  this.topLeftShort;
                            positionBotRight = positionBotLeft;
                        }
                        else{
                            positionTopRight =  this.topRightShort;
                            positionBotRight = positionBotLeft;
                        }

                    } else if( !topLeftTopRightLong && !botLeftBotRightLong && topRightBotRightLong ){

                        positionTopRight =  positionTopLeft;
                        positionBotRight =  positionBotLeft;

                    } else if( !topLeftTopRightLong && botLeftBotRightLong && topRightBotRightLong){



                        if( positionTopLeft.equals(this.topLeftLong)){
                            positionTopRight =  positionTopLeft;
                            positionBotRight =  this.leftRightLong;
                        }
                        else{
                            positionTopRight =  positionTopLeft;
                            positionBotRight = this.rightRightLong;
                        }

                    } else if( topLeftTopRightLong && !botLeftBotRightLong && topRightBotRightLong ){



                        if( positionTopLeft.equals(this.topLeftLong)){
                            positionTopRight = this.rightRightLong;
                            positionBotRight =  positionBotLeft;
                        }
                        else{
                            positionTopRight = this.leftRightLong;
                            positionBotRight =  positionBotLeft;
                        }

                    }


                }
                else{

                   if (!topLeftTopRightLong && !botLeftBotRightLong && !topRightBotRightLong){
                       positionTopRight =  positionTopLeft;
                       positionBotRight =  positionBotLeft;
                   }
                   else if( !topLeftTopRightLong && !botLeftBotRightLong && topRightBotRightLong ){

                       if( positionTopLeft.equals(this.topLeftLong) && positionBotLeft.equals(this.topRightShort)){
                           positionTopRight = positionTopLeft;
                           positionBotRight = this.topRightLong;
                       }
                       else if ( positionTopLeft.equals(this.topRightShort) && positionBotLeft.equals(this.topLeftLong)){
                           positionTopRight = this.topRightLong;
                           positionBotRight = positionBotLeft;
                       }
                       else if( positionTopLeft.equals(this.topLeftShort) && positionBotLeft.equals(this.topRightLong) ){
                           positionTopRight = this.topLeftLong;
                           positionBotRight = positionBotLeft;
                       }
                       else if( positionTopLeft.equals(this.topRightLong) && positionBotLeft.equals(this.topLeftShort) ){
                           positionTopRight = positionTopLeft;
                           positionBotRight = this.topLeftLong;
                       }
                   }
                   else if( !topLeftTopRightLong && botLeftBotRightLong && !topRightBotRightLong){

                       if( positionTopLeft.equals(this.topLeftLong) && positionBotLeft.equals(this.topRightShort)){
                           positionTopRight = positionTopLeft;
                           positionBotRight =  this.leftRightShort;
                       }
                       else if( positionTopLeft.equals(this.topRightShort) && positionBotLeft.equals(this.topLeftLong)) {
                           positionTopRight = this.topRightLong;
                           positionBotRight =  this.rightRightShort;
                       }
                       else if( positionTopLeft.equals(this.topLeftShort) && positionBotLeft.equals(this.topRightLong) ){
                           positionTopRight = this.topLeftLong;
                           positionBotRight =  this.leftRightShort;
                       }
                       else if( positionTopLeft.equals(this.topRightLong) && positionBotLeft.equals(this.topLeftShort) ){
                           positionTopRight = positionTopLeft;
                           positionBotRight =  this.rightRightShort;
                       }

                   }
                   else if( !topLeftTopRightLong && botLeftBotRightLong && topRightBotRightLong){

                       if( positionTopLeft.equals(this.topLeftLong) && positionBotLeft.equals(this.topRightShort)){
                           positionTopRight = positionTopLeft;
                           positionBotRight = this.leftRightLong;
                       }
                       else if( positionTopLeft.equals(this.topRightShort) && positionBotLeft.equals(this.topLeftLong)) {
                           positionTopRight = this.topRightLong;
                           positionBotRight = this.rightRightLong;
                       }
                       else if( positionTopLeft.equals(this.topLeftShort) && positionBotLeft.equals(this.topRightLong) ){
                           positionTopRight = this.topLeftLong;
                           positionBotRight = this.leftRightLong;
                       }
                       else if( positionTopLeft.equals(this.topRightLong) && positionBotLeft.equals(this.topLeftShort) ){
                           positionTopRight = positionTopLeft;
                           positionBotRight = this.rightRightLong;
                       }

                   } else if( topLeftTopRightLong && !botLeftBotRightLong && !topRightBotRightLong ){
                       if( positionTopLeft.equals(this.topLeftLong) && positionBotLeft.equals(this.topRightShort)){
                           positionTopRight = this.rightRightShort;
                           positionBotRight = this.topRightLong;
                       }
                       else if( positionTopLeft.equals(this.topRightShort) && positionBotLeft.equals(this.topLeftLong)) {
                           positionTopRight = this.leftRightShort;
                           positionBotRight = positionBotLeft;
                       }
                       else if( positionTopLeft.equals(this.topLeftShort) && positionBotLeft.equals(this.topRightLong) ){
                           positionTopRight = this.rightRightShort;
                           positionBotRight = positionBotLeft;
                       }
                       else if( positionTopLeft.equals(this.topRightLong) && positionBotLeft.equals(this.topLeftShort) ){
                           positionTopRight = leftRightShort;
                           positionBotRight = topLeftLong;
                       }
                   } else if( topLeftTopRightLong && !botLeftBotRightLong && topRightBotRightLong){

                       if( positionTopLeft.equals(this.topLeftLong) && positionBotLeft.equals(this.topRightShort)){
                           positionTopRight = this.rightRightLong;
                           positionBotRight = this.topRightLong;
                       }
                       else if( positionTopLeft.equals(this.topRightShort) && positionBotLeft.equals(this.topLeftLong)) {
                           positionTopRight = this.leftRightLong;
                           positionBotRight = positionBotLeft;
                       }
                       else if( positionTopLeft.equals(this.topLeftShort) && positionBotLeft.equals(this.topRightLong) ){
                           positionTopRight = this.rightRightLong;
                           positionBotRight = positionBotLeft;
                       }
                       else if( positionTopLeft.equals(this.topRightLong) && positionBotLeft.equals(this.topLeftShort) ) {
                           positionTopRight = this.leftRightLong;
                           positionBotRight = this.topLeftLong;
                       }


                   }




                }


                break;
            }
            case "BOT":{

                if( topLeftBotLeftLong) {

                    if (!topLeftTopRightLong && botLeftBotRightLong && !topRightBotRightLong) {



                        if( positionTopLeft.equals(this.botRightLong)){
                            positionTopRight = positionTopLeft;
                            positionBotRight = this.rightLeftShort;
                        }
                        else{
                            positionTopRight = positionTopLeft;
                            positionBotRight = this.leftLeftShort;
                        }


                    } else if (topLeftTopRightLong && !botLeftBotRightLong && !topRightBotRightLong) {


                        if( positionTopLeft.equals(this.botRightLong)){
                            positionTopRight =  this.leftLeftShort;
                            positionBotRight = positionBotLeft;
                        }
                        else{
                            positionTopRight = this.rightLeftShort;
                            positionBotRight = positionBotLeft;
                        }


                    } else if (!topLeftTopRightLong && !botLeftBotRightLong && !topRightBotRightLong) {

                        if( positionTopLeft.equals(this.botRightLong)){
                            positionTopRight =  this.botRightShort;
                            positionBotRight = positionBotLeft;
                        }
                        else{
                            positionTopRight =  this.botLeftShort;
                            positionBotRight = positionBotLeft;
                        }

                    } else if( !topLeftTopRightLong && !botLeftBotRightLong && topRightBotRightLong ){

                        positionTopRight =  positionTopLeft;
                        positionBotRight =  positionBotLeft;

                    } else if( !topLeftTopRightLong && botLeftBotRightLong && topRightBotRightLong){



                        if( positionTopLeft.equals(this.botRightLong)){
                            positionTopRight =  positionTopLeft;
                            positionBotRight =  this.rightLeftLong;
                        }
                        else{
                            positionTopRight =  positionTopLeft;
                            positionBotRight = this.leftLeftLong;
                        }

                    } else if( topLeftTopRightLong && !botLeftBotRightLong && topRightBotRightLong ){



                        if( positionTopLeft.equals(this.botRightLong)){
                            positionTopRight = this.leftLeftLong;
                            positionBotRight =  positionBotLeft;
                        }
                        else{
                            positionTopRight = this.rightLeftLong;
                            positionBotRight =  positionBotLeft;
                        }

                    }


                }
                else{

                    if (!topLeftTopRightLong && !botLeftBotRightLong && !topRightBotRightLong){
                        positionTopRight =  positionTopLeft;
                        positionBotRight =  positionBotLeft;
                    }
                    else if( !topLeftTopRightLong && !botLeftBotRightLong && topRightBotRightLong ){

                        if( positionTopLeft.equals(this.botRightLong) && positionBotLeft.equals(this.botLeftShort)){
                            positionTopRight = positionTopLeft;
                            positionBotRight = this.botLeftLong;
                        }
                        else if ( positionTopLeft.equals(this.botLeftShort) && positionBotLeft.equals(this.botRightLong)){
                            positionTopRight = this.botLeftLong;
                            positionBotRight = positionBotLeft;
                        }
                        else if( positionTopLeft.equals(this.botRightShort) && positionBotLeft.equals(this.botLeftLong) ){
                            positionTopRight = this.botRightLong;
                            positionBotRight = positionBotLeft;
                        }
                        else if( positionTopLeft.equals(this.botLeftLong) && positionBotLeft.equals(this.botRightShort) ){
                            positionTopRight = positionTopLeft;
                            positionBotRight = this.botRightLong;
                        }
                    }
                    else if( !topLeftTopRightLong && botLeftBotRightLong && !topRightBotRightLong){

                        if( positionTopLeft.equals(this.botRightLong) && positionBotLeft.equals(this.botLeftShort)){
                            positionTopRight = positionTopLeft;
                            positionBotRight =  this.rightLeftShort;
                        }
                        else if( positionTopLeft.equals(this.botLeftShort) && positionBotLeft.equals(this.botRightLong)) {
                            positionTopRight = this.botLeftLong;
                            positionBotRight =  this.leftLeftShort;
                        }
                        else if( positionTopLeft.equals(this.botRightShort) && positionBotLeft.equals(this.botLeftLong) ){
                            positionTopRight = this.botRightLong;
                            positionBotRight =  this.rightLeftShort;
                        }
                        else if( positionTopLeft.equals(this.botLeftLong) && positionBotLeft.equals(this.botRightShort) ){
                            positionTopRight = positionTopLeft;
                            positionBotRight =  this.leftLeftShort;
                        }

                    }
                    else if( !topLeftTopRightLong && botLeftBotRightLong && topRightBotRightLong){

                        if( positionTopLeft.equals(this.botRightLong) && positionBotLeft.equals(this.botLeftShort)){
                            positionTopRight = positionTopLeft;
                            positionBotRight = this.rightLeftLong;
                        }
                        else if( positionTopLeft.equals(this.botLeftShort) && positionBotLeft.equals(this.botRightLong)) {
                            positionTopRight = this.botLeftLong;
                            positionBotRight = this.leftLeftLong;
                        }
                        else if( positionTopLeft.equals(this.botRightShort) && positionBotLeft.equals(this.botLeftLong) ){
                            positionTopRight = this.botRightLong;
                            positionBotRight = this.rightLeftLong;
                        }
                        else if( positionTopLeft.equals(this.botLeftLong) && positionBotLeft.equals(this.botRightShort) ){
                            positionTopRight = positionTopLeft;
                            positionBotRight = this.leftLeftLong;
                        }

                    } else if( topLeftTopRightLong && !botLeftBotRightLong && !topRightBotRightLong ){
                        if( positionTopLeft.equals(this.botRightLong) && positionBotLeft.equals(this.botLeftShort)){
                            positionTopRight = this.leftLeftShort;
                            positionBotRight = this.botLeftLong;
                        }
                        else if( positionTopLeft.equals(this.botLeftShort) && positionBotLeft.equals(this.botRightLong)) {
                            positionTopRight = this.rightLeftShort;
                            positionBotRight = positionBotLeft;
                        }
                        else if( positionTopLeft.equals(this.botRightShort) && positionBotLeft.equals(this.botLeftLong) ){
                            positionTopRight = this.leftLeftShort;
                            positionBotRight = positionBotLeft;
                        }
                        else if( positionTopLeft.equals(this.botLeftLong) && positionBotLeft.equals(this.botRightShort) ){
                            positionTopRight = this.rightLeftShort;
                            positionBotRight = this.botRightLong;
                        }
                    } else if( topLeftTopRightLong && !botLeftBotRightLong && topRightBotRightLong){

                        if( positionTopLeft.equals(this.botRightLong) && positionBotLeft.equals(this.botLeftShort)) {
                            positionTopRight = this.leftLeftLong;
                            positionBotRight = this.botLeftLong;
                        }
                        else if( positionTopLeft.equals(this.botLeftShort) && positionBotLeft.equals(this.botRightLong)) {
                            positionTopRight = this.rightLeftLong;
                            positionBotRight = positionBotLeft;
                        }
                        else if( positionTopLeft.equals(this.botRightShort) && positionBotLeft.equals(this.botLeftLong) ){
                            positionTopRight = this.leftLeftLong;
                            positionBotRight = positionBotLeft;
                        }
                        else if( positionTopLeft.equals(this.botLeftLong) && positionBotLeft.equals(this.botRightShort) ) {
                            positionTopRight = this.rightLeftLong;
                            positionBotRight = this.botRightLong;
                        }


                    }




                }

                break;
            }
            case "LEFT":{


                if( topLeftBotLeftLong) {

                    if (!topLeftTopRightLong && botLeftBotRightLong && !topRightBotRightLong) {

                        if( positionTopLeft.equals(this.leftRightLong)){
                            positionTopRight = positionTopLeft;
                            positionBotRight = this.botRightShort;
                        }
                        else{
                            positionTopRight = positionTopLeft;
                            positionBotRight = this.topRightShort;
                        }


                    } else if (topLeftTopRightLong && !botLeftBotRightLong && !topRightBotRightLong) {


                        if( positionTopLeft.equals(this.leftRightLong)){
                            positionTopRight =  this.topRightShort;
                            positionBotRight = positionBotLeft;
                        }
                        else{
                            positionTopRight = this.botRightShort;
                            positionBotRight = positionBotLeft;
                        }


                    } else if (!topLeftTopRightLong && !botLeftBotRightLong && !topRightBotRightLong) {

                        if( positionTopLeft.equals(this.leftRightLong)){
                            positionTopRight =  this.leftRightShort;
                            positionBotRight = positionBotLeft;
                        }
                        else{
                            positionTopRight =  this.leftLeftShort;
                            positionBotRight = positionBotLeft;
                        }

                    } else if( !topLeftTopRightLong && !botLeftBotRightLong && topRightBotRightLong ){

                        positionTopRight =  positionTopLeft;
                        positionBotRight =  positionBotLeft;

                    } else if( !topLeftTopRightLong && botLeftBotRightLong && topRightBotRightLong){

                        if( positionTopLeft.equals(this.leftRightLong)){
                            positionTopRight =  positionTopLeft;
                            positionBotRight =  this.botRightLong;
                        }
                        else{
                            positionTopRight =  positionTopLeft;
                            positionBotRight = this.topRightLong;
                        }

                    } else if( topLeftTopRightLong && !botLeftBotRightLong && topRightBotRightLong ){



                        if( positionTopLeft.equals(this.leftRightLong)){
                            positionTopRight = this.topRightLong;
                            positionBotRight =  positionBotLeft;
                        }
                        else{
                            positionTopRight = this.botRightLong;
                            positionBotRight =  positionBotLeft;
                        }

                    }


                }
                else {

                    if (!topLeftTopRightLong && !botLeftBotRightLong && !topRightBotRightLong) {
                        positionTopRight = positionTopLeft;
                        positionBotRight = positionBotLeft;
                    } else if (!topLeftTopRightLong && !botLeftBotRightLong && topRightBotRightLong) {

                        if (positionTopLeft.equals(this.leftRightLong) && positionBotLeft.equals(this.leftLeftShort)) {
                            positionTopRight = positionTopLeft;
                            positionBotRight = this.leftLeftLong;
                        } else if (positionTopLeft.equals(this.leftLeftShort) && positionBotLeft.equals(this.leftRightLong)) {
                            positionTopRight = this.leftLeftLong;
                            positionBotRight = positionBotLeft;
                        } else if (positionTopLeft.equals(this.leftRightShort) && positionBotLeft.equals(this.leftLeftLong)) {
                            positionTopRight = this.leftRightLong;
                            positionBotRight = positionBotLeft;
                        } else if (positionTopLeft.equals(this.leftLeftLong) && positionBotLeft.equals(this.leftRightShort)) {
                            positionTopRight = positionTopLeft;
                            positionBotRight = this.leftRightLong;
                        }
                    } else if (!topLeftTopRightLong && botLeftBotRightLong && !topRightBotRightLong) {

                        if (positionTopLeft.equals(this.leftRightLong) && positionBotLeft.equals(this.leftLeftShort)) {
                            positionTopRight = positionTopLeft;
                            positionBotRight = this.botRightShort;
                        } else if (positionTopLeft.equals(this.leftLeftShort) && positionBotLeft.equals(this.leftRightLong)) {
                            positionTopRight = this.leftLeftLong;
                            positionBotRight = this.topRightShort;
                        } else if (positionTopLeft.equals(this.leftRightShort) && positionBotLeft.equals(this.leftLeftLong)) {
                            positionTopRight = this.leftRightLong;
                            positionBotRight = this.botRightShort;
                        } else if (positionTopLeft.equals(this.leftLeftLong) && positionBotLeft.equals(this.leftRightShort)) {
                            positionTopRight = positionTopLeft;
                            positionBotRight = this.topRightShort;
                        }

                    } else if (!topLeftTopRightLong && botLeftBotRightLong && topRightBotRightLong) {

                        if (positionTopLeft.equals(this.leftRightLong) && positionBotLeft.equals(this.leftLeftShort)) {
                            positionTopRight = positionTopLeft;
                            positionBotRight = this.botRightLong;
                        } else if (positionTopLeft.equals(this.leftLeftShort) && positionBotLeft.equals(this.leftRightLong)) {
                            positionTopRight = this.leftLeftLong;
                            positionBotRight = this.topRightLong;
                        } else if (positionTopLeft.equals(this.leftRightShort) && positionBotLeft.equals(this.leftLeftLong)) {
                            positionTopRight = this.leftRightLong;
                            positionBotRight = this.botRightLong;
                        } else if (positionTopLeft.equals(this.leftLeftLong) && positionBotLeft.equals(this.leftRightShort)) {
                            positionTopRight = positionTopLeft;
                            positionBotRight = this.topRightLong;
                        }

                    } else if (topLeftTopRightLong && !botLeftBotRightLong && !topRightBotRightLong) {
                        if (positionTopLeft.equals(this.leftRightLong) && positionBotLeft.equals(this.leftLeftShort)) {
                            positionTopRight = this.topRightShort;
                            positionBotRight = this.leftLeftLong;
                        } else if (positionTopLeft.equals(this.leftLeftShort) && positionBotLeft.equals(this.leftRightLong)) {
                            positionTopRight = this.botRightShort;
                            positionBotRight = positionBotLeft;
                        } else if (positionTopLeft.equals(this.leftRightShort) && positionBotLeft.equals(this.leftLeftLong)) {
                            positionTopRight = this.topRightShort;
                            positionBotRight = positionBotLeft;
                        } else if (positionTopLeft.equals(this.leftLeftLong) && positionBotLeft.equals(this.leftRightShort)) {
                            positionTopRight = botRightShort;
                            positionBotRight = leftRightLong;
                        }
                    } else if (topLeftTopRightLong && !botLeftBotRightLong && topRightBotRightLong) {

                        if (positionTopLeft.equals(this.leftRightLong) && positionBotLeft.equals(this.leftLeftShort)) {
                            positionTopRight = this.topRightLong;
                            positionBotRight = this.leftLeftLong;
                        } else if (positionTopLeft.equals(this.leftLeftShort) && positionBotLeft.equals(this.leftRightLong)) {
                            positionTopRight = this.botRightLong;
                            positionBotRight = positionBotLeft;
                        } else if (positionTopLeft.equals(this.leftRightShort) && positionBotLeft.equals(this.leftLeftLong)) {
                            positionTopRight = this.topRightLong;
                            positionBotRight = positionBotLeft;
                        } else if (positionTopLeft.equals(this.leftLeftLong) && positionBotLeft.equals(this.leftRightShort)) {
                            positionTopRight = this.botRightLong;
                            positionBotRight = this.leftRightLong;
                        }


                    }
                }


                break;
            }
            case "RIGHT":{

                if( topLeftBotLeftLong) {

                    if (!topLeftTopRightLong && botLeftBotRightLong && !topRightBotRightLong) {



                        if( positionTopLeft.equals(this.rightLeftLong)){
                            positionTopRight = positionTopLeft;
                            positionBotRight = this.topLeftShort;
                        }
                        else{
                            positionTopRight = positionTopLeft;
                            positionBotRight = this.botLeftShort;
                        }


                    } else if (topLeftTopRightLong && !botLeftBotRightLong && !topRightBotRightLong) {


                        if( positionTopLeft.equals(this.rightLeftLong)){
                            positionTopRight =  this.botLeftShort;
                            positionBotRight = positionBotLeft;
                        }
                        else{
                            positionTopRight = this.leftRightShort;
                            positionBotRight = this.topLeftShort;
                        }


                    } else if (!topLeftTopRightLong && !botLeftBotRightLong && !topRightBotRightLong) {

                        if( positionTopLeft.equals(this.rightLeftLong)){
                            positionTopRight =  this.rightLeftShort;
                            positionBotRight = positionBotLeft;
                        }
                        else{
                            positionTopRight =  this.rightRightShort;
                            positionBotRight = positionBotLeft;
                        }

                    } else if( !topLeftTopRightLong && !botLeftBotRightLong && topRightBotRightLong ){

                        positionTopRight =  positionTopLeft;
                        positionBotRight =  positionBotLeft;

                    } else if( !topLeftTopRightLong && botLeftBotRightLong && topRightBotRightLong){



                        if( positionTopLeft.equals(this.rightLeftLong)){
                            positionTopRight =  positionTopLeft;
                            positionBotRight =  this.topLeftLong;
                        }
                        else{
                            positionTopRight =  positionTopLeft;
                            positionBotRight = this.botLeftLong;
                        }

                    } else if( topLeftTopRightLong && !botLeftBotRightLong && topRightBotRightLong ){



                        if( positionTopLeft.equals(this.rightLeftLong)){
                            positionTopRight = this.botLeftLong;
                            positionBotRight =  positionBotLeft;
                        }
                        else{
                            positionTopRight = this.topLeftLong;
                            positionBotRight =  positionBotLeft;
                        }

                    }


                }
                else{

                    if (!topLeftTopRightLong && !botLeftBotRightLong && !topRightBotRightLong){
                        positionTopRight =  positionTopLeft;
                        positionBotRight =  positionBotLeft;
                    }
                    else if( !topLeftTopRightLong && !botLeftBotRightLong && topRightBotRightLong ){

                        if( positionTopLeft.equals(this.rightLeftLong) && positionBotLeft.equals(this.rightRightShort)){
                            positionTopRight = positionTopLeft;
                            positionBotRight = this.rightRightLong;
                        }
                        else if ( positionTopLeft.equals(this.rightRightShort) && positionBotLeft.equals(this.rightLeftLong)){
                            positionTopRight = this.rightRightLong;
                            positionBotRight = positionBotLeft;
                        }
                        else if( positionTopLeft.equals(this.rightLeftShort) && positionBotLeft.equals(this.rightRightLong) ){
                            positionTopRight = this.rightLeftLong;
                            positionBotRight = positionBotLeft;
                        }
                        else if( positionTopLeft.equals(this.rightRightLong) && positionBotLeft.equals(this.rightLeftShort) ){
                            positionTopRight = this.rightLeftLong;
                            positionBotRight = this.topLeftLong;
                        }
                    }
                    else if( !topLeftTopRightLong && botLeftBotRightLong && !topRightBotRightLong){

                        if( positionTopLeft.equals(this.rightLeftLong) && positionBotLeft.equals(this.rightRightShort)){
                            positionTopRight = positionTopLeft;
                            positionBotRight =  this.topLeftShort;
                        }
                        else if( positionTopLeft.equals(this.rightRightShort) && positionBotLeft.equals(this.rightLeftLong)) {
                            positionTopRight = this.rightRightLong;
                            positionBotRight =  this.botLeftShort;
                        }
                        else if( positionTopLeft.equals(this.rightLeftShort) && positionBotLeft.equals(this.rightRightLong) ){
                            positionTopRight = this.rightLeftLong;
                            positionBotRight =  this.topLeftShort;
                        }
                        else if( positionTopLeft.equals(this.rightRightLong) && positionBotLeft.equals(this.rightLeftShort) ){
                            positionTopRight = positionTopLeft;
                            positionBotRight =  this.botLeftShort;
                        }

                    }
                    else if( !topLeftTopRightLong && botLeftBotRightLong && topRightBotRightLong){

                        if( positionTopLeft.equals(this.rightLeftLong) && positionBotLeft.equals(this.rightRightShort)){
                            positionTopRight = positionTopLeft;
                            positionBotRight = this.topLeftLong;
                        }
                        else if( positionTopLeft.equals(this.rightRightShort) && positionBotLeft.equals(this.rightLeftLong)) {
                            positionTopRight = this.rightRightLong;
                            positionBotRight = this.botLeftLong;
                        }
                        else if( positionTopLeft.equals(this.rightLeftShort) && positionBotLeft.equals(this.rightRightLong) ){
                            positionTopRight = this.rightLeftLong;
                            positionBotRight = this.topLeftLong;
                        }
                        else if( positionTopLeft.equals(this.rightRightLong) && positionBotLeft.equals(this.rightLeftShort) ){
                            positionTopRight = positionTopLeft;
                            positionBotRight = this.botLeftLong;
                        }

                    } else if( topLeftTopRightLong && !botLeftBotRightLong && !topRightBotRightLong ){
                        if( positionTopLeft.equals(this.rightLeftLong) && positionBotLeft.equals(this.rightRightShort)){
                            positionTopRight = this.botLeftShort;
                            positionBotRight = this.rightRightLong;
                        }
                        else if( positionTopLeft.equals(this.rightRightShort) && positionBotLeft.equals(this.rightLeftLong)) {
                            positionTopRight = this.topLeftShort;
                            positionBotRight = positionBotLeft;
                        }
                        else if( positionTopLeft.equals(this.rightLeftShort) && positionBotLeft.equals(this.rightRightLong) ){
                            positionTopRight = this.botLeftShort;
                            positionBotRight = positionBotLeft;
                        }
                        else if( positionTopLeft.equals(this.rightRightLong) && positionBotLeft.equals(this.rightLeftShort) ){
                            positionTopRight = this.topLeftShort;
                            positionBotRight = this.rightLeftLong;
                        }
                    } else if( topLeftTopRightLong && !botLeftBotRightLong && topRightBotRightLong){

                        if( positionTopLeft.equals(this.rightLeftLong) && positionBotLeft.equals(this.rightRightShort)){
                            positionTopRight = this.botLeftLong;
                            positionBotRight = this.rightRightLong;
                        }
                        else if( positionTopLeft.equals(this.rightRightShort) && positionBotLeft.equals(this.rightLeftLong)) {
                            positionTopRight = this.topLeftLong;
                            positionBotRight = positionBotLeft;
                        }
                        else if( positionTopLeft.equals(this.rightLeftShort) && positionBotLeft.equals(this.rightRightLong) ){
                            positionTopRight = this.botLeftLong;
                            positionBotRight = positionBotLeft;
                        }
                        else if( positionTopLeft.equals(this.rightRightLong) && positionBotLeft.equals(this.rightLeftShort) ) {
                            positionTopRight = this.topLeftLong;
                            positionBotRight = this.rightLeftLong;
                        }


                    }


                }

                break;
            }
        }

        if( positionTopRight !=null && positionBotRight != null){
            this.basicGraph.moveNode(topRight,positionTopRight);
            this.basicGraph.moveNode(botRight,positionBotRight);
        }
    }



    private YPoint calculateCenterGraphView(){
        double worldHeight = this.graphView.getWorldRect2D().getHeight();
        double worldWidth = this.graphView.getWorldRect2D().getWidth();
        return new YPoint(worldWidth/2,worldHeight/2);
    }

    @Override
    public boolean isGraphType() {

        /*
        int nodeCount = graph2D.nodeCount();
        int inducedNodeCount = inducedSubgraph.nodeCount();
        System.out.println(inducedNodeCount+" "+nodeCount);

        if( nodeCount == 2* inducedNodeCount){

            EdgeList cycle = Cycles.findCycle(inducedSubgraph,false);
            NodeList cycleNodes = Paths.constructNodePath(cycle);

            System.out.println(cycleNodes);
            return true;
        }
        else{
            return false;
            //error graph type
        }
        */
        return true;

    }

    private boolean vertexListsDistinct(NodeList list1 , NodeList list2){

        for(NodeCursor v = list1.nodes() ; v.ok() ; v.next() ){
            Node currentNode = v.node();
            if( list2.contains(currentNode)){
                return false;
            }
        }
        return true;
    }


    private ArrayList<Node> getDistinctNeighbors( NodeList comparison, NodeList neighbors){
        ArrayList<Node> result = new ArrayList<>();
        for(NodeCursor v = neighbors.nodes() ; v.ok() ; v.next() ){
            Node currentNeighbor = v.node();
            if( ! comparison.contains(currentNeighbor)){
                result.add(currentNeighbor);
            }
        }
        return result;
    }

    private boolean isOuterCycle( NodeList innerCycle){

        this.cycleMapping = Maps.createHashedNodeMap();

        for(NodeCursor v = innerCycle.nodes() ; v.ok() ; v.next() ){
            Node currentNode = v.node();
            NodeList currentNeighbors = new NodeList(currentNode.neighbors());

            ArrayList<Node> distinctNeighbors = getDistinctNeighbors(innerCycle,currentNeighbors);
            if( distinctNeighbors.size() == 1){
                Node distinctNeighbor = distinctNeighbors.get(0);
              //  System.out.println("DISTINCT NEIGHBOR: "+distinctNeighbor+" INNERNODE: "+currentNode);
                cycleMapping.set(currentNode,distinctNeighbor);
            }
            else{
                return false;
            }

        }
        return true;

    }

    private NodeList prepareVertexOrder(NodeList innerCycle){
        NodeList result = new NodeList();
        for( NodeCursor v = innerCycle.nodes() ; v.ok() ; v.next() ){
            Node currentInnerNode = v.node();
            Node currentOuterNode = (Node) this.cycleMapping.get(currentInnerNode);
            //System.out.println(currentInnerNode);
            result.add(currentOuterNode);
            result.add(currentInnerNode);
        }
        return result;
    }


    private NodeList complementaryNodes( NodeList list){
        NodeList result = new NodeList();

        for( Node vertex: nodes(graph2D)){
            if( !list.contains(vertex)){
                result.add(vertex);
            }
        }
        return result;
    }


    private void initializeVertexOrder(){

        PrismGraphCycles prismGraphCycles = new PrismGraphCycles(graph2D);
        this.vertexOrder = prismGraphCycles.getVertexOrder();

        /*
        int cycleLength = graph2D.nodeCount() / 2;

        Node startNode = graph2D.firstNode();
        NodeList startNodeNeighbors = new NodeList(startNode.neighbors());

        Path path = new Path(graph2D,startNode,cycleLength-1);

        NodeList[] allPathsOfLength = path.getAllPathsOfLength();

        for( int i=0; i < allPathsOfLength.length; i++){

            NodeList currentPath = allPathsOfLength[i];
            System.out.println(currentPath);
            /*
            Node lastNodePath = (Node) currentPath.get(currentPath.size()-1);

            if( startNodeNeighbors.contains(lastNodePath)) {

                if( isOuterCycle(currentPath)){
                    this.vertexOrder = prepareVertexOrder(currentPath);
                    break;
                }
            }
            else{
                continue;
            }



        }


        /*
        int cycleLength = graph2D.nodeCount() / 2;
        Cycle cycle = new Cycle(graph2D,cycleLength);

        NodeList[] allCycles = cycle.detectAllCycles(cycleLength);

        for( int i= 0; i < allCycles.length ; i++){
            NodeList currentCycle = allCycles[i];
            for( int k= 0; k < allCycles.length ; k++){
                if( i== k){
                    continue;
                }
                else{
                    NodeList comparisionCycle = allCycles[k];
                    if( vertexListsDistinct(currentCycle,comparisionCycle) && isOuterCycle(currentCycle)){
                        this.vertexOrder = prepareVertexOrder(currentCycle);
                        return;
                    }
                }
            }
        }
        */


    }





    private void initializeInducedSubgraph(){
        ArrayList<Edge> longEdges = this.graphView.getRelationships().getPartitions().get(2);
        this.longEdgesInducedSubgraph = new EdgeInducedSubgraph(this.graph2D,longEdges);
        this.inducedSubgraph = longEdgesInducedSubgraph.getInducedSubgraph();
    }






}
