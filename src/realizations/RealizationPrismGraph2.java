package realizations;

import algo.PrismGraphCycles;
import model.MetricCollection;
import view.GraphView;
import y.base.*;
import y.geom.YPoint;
import y.util.Maps;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by chris on 28.02.2018.
 */
public class RealizationPrismGraph2 extends Realization {

    private final double CYCLE_RADIUS = 300;

    private NodeList innerCycle;
    private NodeList outerCycle;

    private NodeMap shortRealizationSpots;
    private NodeMap longRealizationSpots;

    private NodeMap shortSecondRealizationSpots;
    private NodeMap longSecondRealizationSpots;

    private NodeList vertexOrder;

    private ArrayList<Edge> shortEdges;
    private ArrayList<Edge> longEdges;

    public RealizationPrismGraph2(GraphView graphView) {
        super(graphView);



        /*
        this.initializeAssets();
        this.initializeVertexOrder();

        this.initializeInnerCycle();
        this.initializeOuterCycle();

        this.shiftOuterCycle();

        this.realizeInnerCycle();
        this.realizeOuterCycle();
        */



        /*
        basicGraph.resetGraph();

        YPoint p1 = new YPoint(294.14227192617585,248.60619510031154);
        YPoint p2 = new YPoint(1.7763568394002505E-14,300.0);
        YPoint p3 = new YPoint(2.7878559441505592E-14,455.2914270615124);

        double lengthLongEdge =  169.04;
        double lengthShortEdge = 152.96;

        this.graphView.setAddVertexDistanceCircle( p1,lengthShortEdge);
        this.graphView.setAddVertexDistanceCircle2( p2,lengthShortEdge);


       // YPoint destination = calculateClosestPointBiggerCircleCircleIntersection(p2,lengthLongEdge,p1,lengthShortEdge,p3);
          YPoint destination =  calculateClosestPointCircleCircleIntersectionWithoutRel(p1,lengthShortEdge,p2,lengthShortEdge,p3);

        basicGraph.createNode(p1);
        basicGraph.createNode(p2);
        basicGraph.createNode(p3);
        basicGraph.createNode(destination);
        */


        this.graphView.fitContent();

    }

    private void shiftOuterCycle(){

        int shiftIndex = -1;

        for( int i=0; i < outerCycle.size(); i++){

            Node currentNode = (Node) outerCycle.get(i);
            Node nextNode;

            if( i+1 < outerCycle.size()){
                nextNode = (Node) outerCycle.get(i+1);
            }
            else{
                nextNode = (Node) outerCycle.get(0);
            }


            boolean edgeLong = isEdgeLong(currentNode,nextNode);

            if( edgeLong){
                shiftIndex = i;
                break;
            }

        }

        NodeList newOuterCycle = new NodeList();
        NodeList newInnerCycle = new NodeList();

        for( int i= shiftIndex+1 ; i < outerCycle.size(); i++){
            Node currentInnerNode = (Node) innerCycle.get(i);
            Node currentOuterNode = (Node) outerCycle.get(i);

            newInnerCycle.add(currentInnerNode);
            newOuterCycle.add(currentOuterNode);
        }

        for( int i=0; i < shiftIndex+1; i++){
            Node currentInnerNode = (Node) innerCycle.get(i);
            Node currentOuterNode = (Node) outerCycle.get(i);

            newInnerCycle.add(currentInnerNode);
            newOuterCycle.add(currentOuterNode);
        }
        System.out.println("SHIFT INDEX: "+shiftIndex);

        System.out.println(innerCycle);
        System.out.println(outerCycle);

        this.innerCycle = newInnerCycle;
        this.outerCycle = newOuterCycle;

        System.out.println(newInnerCycle);
        System.out.println(newOuterCycle);

    }

    private ArrayList<YPoint> getRealizationSpots(Node previousInnerNode){
        YPoint spot1 = (YPoint) shortRealizationSpots.get(previousInnerNode);
        YPoint spot2 = (YPoint) shortSecondRealizationSpots.get(previousInnerNode);
        YPoint spot3 = (YPoint) longRealizationSpots.get(previousInnerNode);
        YPoint spot4 = (YPoint) longSecondRealizationSpots.get(previousInnerNode);

        ArrayList<YPoint> result = new ArrayList<>();
        result.add(spot1);
        result.add(spot2);
        result.add(spot3);
        result.add(spot4);
        return result;
    }

    private ArrayList<YPoint> getFirstRealizationSpots(Node previousInnerNode){
        YPoint spot1 = (YPoint) shortRealizationSpots.get(previousInnerNode);
        YPoint spot2 = (YPoint) longRealizationSpots.get(previousInnerNode);
        ArrayList<YPoint> result = new ArrayList<>();
        result.add(spot1);
        result.add(spot2);
        return result;
    }

    private ArrayList<YPoint> getSecondRealizationSpots(Node previousInnerNode ){
        YPoint spot1 = (YPoint) shortSecondRealizationSpots.get(previousInnerNode);
        YPoint spot2 = (YPoint) longSecondRealizationSpots.get(previousInnerNode);
        ArrayList<YPoint> result = new ArrayList<>();
        result.add(spot1);
        result.add(spot2);
        return result;
    }

    private boolean isOnRealizationSpot( Node previousInnerNode, Node previousOuterNode){
        YPoint position = graph2D.getCenter(previousOuterNode);
        for( YPoint point: getRealizationSpots(previousInnerNode)){
            if( point.equals(position)){
                return true;
            }
        }
        return false;
    }

    private boolean isOnFirstRealizationSpot( Node previousInnerNode, Node previousOuterNode){
        YPoint position = graph2D.getCenter(previousOuterNode);
        for( YPoint point: getFirstRealizationSpots(previousInnerNode)){
            if( point.equals(position)){
                return true;
            }
        }
        return false;
    }


    private void realizeOuterCycle(){

        double lengthLongEdge = getLengthLongEdges();
        double lengthShortEdge = getLengthShortEdges();

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

        for( int i=1; i < outerCycle.size()-1; i++){

            Node previousOuterNode = (Node) outerCycle.get(i - 1);
            Node currentOuterNode = (Node) outerCycle.get(i);

            Node previousInnerNode = (Node) innerCycle.get(i-1);
            Node currentInnerNode = (Node) innerCycle.get(i);

            boolean previousOuterCurrentOuterLong = isEdgeLong(previousOuterNode, currentOuterNode);
            boolean currentOuterCurrentInnerLong = isEdgeLong(currentOuterNode, currentInnerNode);

            boolean previousInnerCurrentInnerLong = isEdgeLong(previousInnerNode,currentInnerNode);

            YPoint destination;

            YPoint posPreviousOuter = graph2D.getCenter(previousOuterNode);
            YPoint posCurrentInner = graph2D.getCenter(currentInnerNode);

            YPoint posRelative = (YPoint) longRealizationSpots.get(currentInnerNode);

                if( previousInnerCurrentInnerLong){


                    if( isOnRealizationSpot(previousInnerNode,previousOuterNode)){

                        if( isOnFirstRealizationSpot(previousInnerNode,previousOuterNode)){


                            if( previousOuterCurrentOuterLong && currentOuterCurrentInnerLong){

                                System.out.println(" PreviousOuterNode: "+previousOuterNode+" Current Outer: "+currentOuterNode+" Previous Inner Node: "+previousInnerNode+" Current innerNode "+currentInnerNode);
                                System.out.println("CASE: PREVIOUS INNER CURRENT INNER LOONG? "+previousInnerCurrentInnerLong+" ON REALIZATION SPOT? "+isOnRealizationSpot(previousInnerNode,previousOuterNode)+" ON FIRST? " +isOnFirstRealizationSpot(previousInnerNode,previousOuterNode)  );
                                System.out.println( " previousOuterCurrentOuterLong? "+previousOuterCurrentOuterLong+" currentOuterCurrentInnerLong? "+currentOuterCurrentInnerLong);
                                System.out.println(" POS PREVIOUS OUTER: "+posPreviousOuter+" POS Current Inner: "+posCurrentInner+" POS relative: "+posRelative);
                                System.out.println("----------------------------- \n");

                                destination = (YPoint) longSecondRealizationSpots.get(currentInnerNode);
                            }
                            else if( !previousOuterCurrentOuterLong && currentOuterCurrentInnerLong){

                                System.out.println(" PreviousOuterNode: "+previousOuterNode+" Current Outer: "+currentOuterNode+" Previous Inner Node: "+previousInnerNode+" Current innerNode "+currentInnerNode);
                                System.out.println("CASE: PREVIOUS INNER CURRENT INNER LOONG? "+previousInnerCurrentInnerLong+" ON REALIZATION SPOT? "+isOnRealizationSpot(previousInnerNode,previousOuterNode)+" ON FIRST? " +isOnFirstRealizationSpot(previousInnerNode,previousOuterNode)  );
                                System.out.println( " previousOuterCurrentOuterLong? "+previousOuterCurrentOuterLong+" currentOuterCurrentInnerLong? "+currentOuterCurrentInnerLong);
                                System.out.println(" POS PREVIOUS OUTER: "+posPreviousOuter+" POS Current Inner: "+posCurrentInner+" POS relative: "+posRelative);
                                System.out.println("----------------------------- \n");


                                YPoint positionPreviousOuterNode = graph2D.getCenter(previousOuterNode);
                                YPoint positionCurrentInnerNode = graph2D.getCenter(currentInnerNode);
                                YPoint relativePoint = (YPoint) longSecondRealizationSpots.get(currentInnerNode);

                                double distancePreviousOuterCurrentInner = MetricCollection.euklideanDistanceR2(positionPreviousOuterNode.x,positionPreviousOuterNode.y,positionCurrentInnerNode.x,positionCurrentInnerNode.y);


                                if( distancePreviousOuterCurrentInner < lengthLongEdge+lengthShortEdge) {

                                    destination = calculateClosestPointBiggerCircleCircleIntersection(positionCurrentInnerNode, lengthLongEdge, positionPreviousOuterNode, lengthShortEdge - 1, relativePoint);

                                }
                                else{
                                    double adjustedLongLength = (distancePreviousOuterCurrentInner-lengthLongEdge-lengthShortEdge)+1+lengthLongEdge;
                                    destination = calculateClosestPointBiggerCircleCircleIntersection(positionCurrentInnerNode, adjustedLongLength, positionPreviousOuterNode, lengthShortEdge - 1, relativePoint);

                                }

                                /*
                                YPoint positionPreviousOuterNode = graph2D.getCenter(previousOuterNode);
                                YPoint positionCurrentInnerNode = graph2D.getCenter(currentInnerNode);

                              //  this.graphView.setAddVertexDistanceCircle( positionPreviousOuterNode,lengthShortEdge);
                             //   this.graphView.setAddVertexDistanceCircle2( positionCurrentInnerNode,lengthLongEdge);

                                YPoint[] intersections = calculateCircleCircleIntersection(positionPreviousOuterNode,lengthShortEdge,positionCurrentInnerNode,lengthLongEdge);

                                if( positionPreviousOuterNode.x > positionCurrentInnerNode.x && positionPreviousOuterNode.y < positionCurrentInnerNode.y){
                                    destination = intersections[0];
                                }
                                else if(  positionPreviousOuterNode.x > positionCurrentInnerNode.x && positionPreviousOuterNode.y > positionCurrentInnerNode.y ){
                                    destination = intersections[1];
                                }
                                else if(  positionPreviousOuterNode.x <= positionCurrentInnerNode.x && positionPreviousOuterNode.y <= positionCurrentInnerNode.y ){
                                    destination = intersections[0];
                                }
                                else {
                                    destination = intersections[1];
                                }
                                */
                               // destination = (YPoint) longRealizationSpots.get(currentInnerNode);
                            }
                            else if(previousOuterCurrentOuterLong && !currentOuterCurrentInnerLong){

                                System.out.println(" PreviousOuterNode: "+previousOuterNode+" Current Outer: "+currentOuterNode+" Previous Inner Node: "+previousInnerNode+" Current innerNode "+currentInnerNode);
                                System.out.println("CASE: PREVIOUS INNER CURRENT INNER LOONG? "+previousInnerCurrentInnerLong+" ON REALIZATION SPOT? "+isOnRealizationSpot(previousInnerNode,previousOuterNode)+" ON FIRST? " +isOnFirstRealizationSpot(previousInnerNode,previousOuterNode)  );
                                System.out.println( " previousOuterCurrentOuterLong? "+previousOuterCurrentOuterLong+" currentOuterCurrentInnerLong? "+currentOuterCurrentInnerLong);
                                System.out.println(" POS PREVIOUS OUTER: "+posPreviousOuter+" POS Current Inner: "+posCurrentInner+" POS relative: "+posRelative);
                                System.out.println("----------------------------- \n");

                                destination = (YPoint) shortSecondRealizationSpots.get(currentInnerNode);
                            }
                            else{

                                System.out.println(" PreviousOuterNode: "+previousOuterNode+" Current Outer: "+currentOuterNode+" Previous Inner Node: "+previousInnerNode+" Current innerNode "+currentInnerNode);
                                System.out.println("CASE: PREVIOUS INNER CURRENT INNER LOONG? "+previousInnerCurrentInnerLong+" ON REALIZATION SPOT? "+isOnRealizationSpot(previousInnerNode,previousOuterNode)+" ON FIRST? " +isOnFirstRealizationSpot(previousInnerNode,previousOuterNode)  );
                                System.out.println( " previousOuterCurrentOuterLong? "+previousOuterCurrentOuterLong+" currentOuterCurrentInnerLong? "+currentOuterCurrentInnerLong);
                                System.out.println(" POS PREVIOUS OUTER: "+posPreviousOuter+" POS Current Inner: "+posCurrentInner+" POS relative: "+posRelative);
                                System.out.println("----------------------------- \n");

                                YPoint positionPreviousOuterNode = graph2D.getCenter(previousOuterNode);
                                YPoint positionCurrentInnerNode = graph2D.getCenter(currentInnerNode);


                                YPoint relativePoint = (YPoint) longSecondRealizationSpots.get(currentInnerNode);
                                destination = calculateClosestPointCircleCircleIntersectionWithoutRel(positionPreviousOuterNode,lengthShortEdge-1,positionCurrentInnerNode,lengthShortEdge-1,relativePoint);

                                /*
                                YPoint positionPreviousOuterNode = graph2D.getCenter(previousOuterNode);
                                YPoint positionCurrentInnerNode = graph2D.getCenter(currentInnerNode);
                                YPoint[] intersections = calculateCircleCircleIntersection(positionPreviousOuterNode,lengthShortEdge,positionCurrentInnerNode,lengthShortEdge);

                                if( positionPreviousOuterNode.x >= positionCurrentInnerNode.x && positionPreviousOuterNode.y <= positionCurrentInnerNode.y){
                                    destination = intersections[0];
                                }
                                else if(  positionPreviousOuterNode.x >= positionCurrentInnerNode.x && positionPreviousOuterNode.y >= positionCurrentInnerNode.y ){
                                    destination = intersections[1];
                                }
                                else if(  positionPreviousOuterNode.x <= positionCurrentInnerNode.x && positionPreviousOuterNode.y <= positionCurrentInnerNode.y ){
                                    destination = intersections[0];
                                }
                                else {
                                    destination = intersections[1];
                                }
                                //destination = (YPoint) shortRealizationSpots.get(currentInnerNode);
                                */
                            }

                        }
                        else{

                            if( previousOuterCurrentOuterLong && currentOuterCurrentInnerLong){
                                System.out.println(" PreviousOuterNode: "+previousOuterNode+" Current Outer: "+currentOuterNode+" Previous Inner Node: "+previousInnerNode+" Current innerNode "+currentInnerNode);
                                System.out.println("CASE: PREVIOUS INNER CURRENT INNER LOONG? "+previousInnerCurrentInnerLong+" ON REALIZATION SPOT? "+isOnRealizationSpot(previousInnerNode,previousOuterNode)+" ON FIRST? " +isOnFirstRealizationSpot(previousInnerNode,previousOuterNode)  );
                                System.out.println( " previousOuterCurrentOuterLong? "+previousOuterCurrentOuterLong+" currentOuterCurrentInnerLong? "+currentOuterCurrentInnerLong);
                                System.out.println(" POS PREVIOUS OUTER: "+posPreviousOuter+" POS Current Inner: "+posCurrentInner+" POS relative: "+posRelative);
                                System.out.println("----------------------------- \n");

                                destination = (YPoint) longSecondRealizationSpots.get(currentInnerNode);
                            }
                            else if( !previousOuterCurrentOuterLong && currentOuterCurrentInnerLong){

                                System.out.println(" PreviousOuterNode: "+previousOuterNode+" Current Outer: "+currentOuterNode+" Previous Inner Node: "+previousInnerNode+" Current innerNode "+currentInnerNode);
                                System.out.println("CASE: PREVIOUS INNER CURRENT INNER LOONG? "+previousInnerCurrentInnerLong+" ON REALIZATION SPOT? "+isOnRealizationSpot(previousInnerNode,previousOuterNode)+" ON FIRST? " +isOnFirstRealizationSpot(previousInnerNode,previousOuterNode)  );
                                System.out.println( " previousOuterCurrentOuterLong? "+previousOuterCurrentOuterLong+" currentOuterCurrentInnerLong? "+currentOuterCurrentInnerLong);
                                System.out.println(" POS PREVIOUS OUTER: "+posPreviousOuter+" POS Current Inner: "+posCurrentInner+" POS relative: "+posRelative);
                                System.out.println("----------------------------- \n");

                                YPoint positionPreviousOuterNode = graph2D.getCenter(previousOuterNode);
                                YPoint longRealizationSpot = (YPoint) longRealizationSpots.get(currentInnerNode);
                                double distanceSecondLongRealizationSpot = MetricCollection.euklideanDistanceR2(positionPreviousOuterNode.x,positionPreviousOuterNode.y,longRealizationSpot.x,longRealizationSpot.y);
                                if( distanceSecondLongRealizationSpot < lengthLongEdge){
                                    destination = longRealizationSpot;
                                }
                                else {
                                    YPoint positionCurrentInnerNode = graph2D.getCenter(currentInnerNode);

                                    YPoint relativePoint = (YPoint) longSecondRealizationSpots.get(currentInnerNode);

                                    double distancePreviousOuterCurrentInner = MetricCollection.euklideanDistanceR2(positionPreviousOuterNode.x,positionPreviousOuterNode.y,positionCurrentInnerNode.x,positionCurrentInnerNode.y);


                                    if( distancePreviousOuterCurrentInner < lengthLongEdge+lengthShortEdge) {

                                       destination = calculateClosestPointBiggerCircleCircleIntersection(positionCurrentInnerNode,lengthLongEdge,positionPreviousOuterNode,lengthShortEdge-1,relativePoint);

                                    }
                                    else{
                                        double adjustedLongLength = (distancePreviousOuterCurrentInner-lengthLongEdge-lengthShortEdge)+1+lengthLongEdge;
                                        destination = calculateClosestPointBiggerCircleCircleIntersection(positionCurrentInnerNode, adjustedLongLength, positionPreviousOuterNode, lengthShortEdge - 1, relativePoint);

                                    }
                                }
                            }
                            else if(previousOuterCurrentOuterLong && !currentOuterCurrentInnerLong){
                                System.out.println(" PreviousOuterNode: "+previousOuterNode+" Current Outer: "+currentOuterNode+" Previous Inner Node: "+previousInnerNode+" Current innerNode "+currentInnerNode);
                                System.out.println("CASE: PREVIOUS INNER CURRENT INNER LOONG? "+previousInnerCurrentInnerLong+" ON REALIZATION SPOT? "+isOnRealizationSpot(previousInnerNode,previousOuterNode)+" ON FIRST? " +isOnFirstRealizationSpot(previousInnerNode,previousOuterNode)  );
                                System.out.println( " previousOuterCurrentOuterLong? "+previousOuterCurrentOuterLong+" currentOuterCurrentInnerLong? "+currentOuterCurrentInnerLong);
                                System.out.println(" POS PREVIOUS OUTER: "+posPreviousOuter+" POS Current Inner: "+posCurrentInner+" POS relative: "+posRelative);
                                System.out.println("----------------------------- \n");

                                destination = (YPoint) shortSecondRealizationSpots.get(currentInnerNode);
                            }
                            else{
                                System.out.println(" PreviousOuterNode: "+previousOuterNode+" Current Outer: "+currentOuterNode+" Previous Inner Node: "+previousInnerNode+" Current innerNode "+currentInnerNode);
                                System.out.println("CASE: PREVIOUS INNER CURRENT INNER LOONG? "+previousInnerCurrentInnerLong+" ON REALIZATION SPOT? "+isOnRealizationSpot(previousInnerNode,previousOuterNode)+" ON FIRST? " +isOnFirstRealizationSpot(previousInnerNode,previousOuterNode)  );
                                System.out.println( " previousOuterCurrentOuterLong? "+previousOuterCurrentOuterLong+" currentOuterCurrentInnerLong? "+currentOuterCurrentInnerLong);
                                System.out.println(" POS PREVIOUS OUTER: "+posPreviousOuter+" POS Current Inner: "+posCurrentInner+" POS relative: "+posRelative);
                                System.out.println("----------------------------- \n");

                                YPoint positionPreviousOuterNode = graph2D.getCenter(previousOuterNode);
                                YPoint shortRealizationSpot = (YPoint) shortRealizationSpots.get(currentInnerNode);
                                double distanceSecondShortRealizationSpot = MetricCollection.euklideanDistanceR2(positionPreviousOuterNode.x,positionPreviousOuterNode.y,shortRealizationSpot.x,shortRealizationSpot.y);

                                if( distanceSecondShortRealizationSpot < lengthLongEdge){
                                    destination = shortRealizationSpot;
                                }
                                else {
                                    YPoint positionCurrentInnerNode = graph2D.getCenter(currentInnerNode);
                                    YPoint relativePoint = (YPoint) longSecondRealizationSpots.get(currentInnerNode);
                                    destination = calculateClosestPointCircleCircleIntersectionWithoutRel(positionPreviousOuterNode,lengthShortEdge-1,positionCurrentInnerNode,lengthShortEdge-1,relativePoint);

                                }
                            }

                        }
                    }
                    else{

                        if( previousOuterCurrentOuterLong && currentOuterCurrentInnerLong){

                            System.out.println(" PreviousOuterNode: "+previousOuterNode+" Current Outer: "+currentOuterNode+" Previous Inner Node: "+previousInnerNode+" Current innerNode "+currentInnerNode);
                            System.out.println("CASE: PREVIOUS INNER CURRENT INNER LOONG? "+previousInnerCurrentInnerLong+" ON REALIZATION SPOT? "+isOnRealizationSpot(previousInnerNode,previousOuterNode)+" ON FIRST? " +isOnFirstRealizationSpot(previousInnerNode,previousOuterNode)  );
                            System.out.println( " previousOuterCurrentOuterLong? "+previousOuterCurrentOuterLong+" currentOuterCurrentInnerLong? "+currentOuterCurrentInnerLong);
                            System.out.println(" POS PREVIOUS OUTER: "+posPreviousOuter+" POS Current Inner: "+posCurrentInner+" POS relative: "+posRelative);
                            System.out.println("----------------------------- \n");

                            destination = (YPoint) longSecondRealizationSpots.get(currentInnerNode);
                        }
                        else if( !previousOuterCurrentOuterLong && currentOuterCurrentInnerLong){

                            System.out.println(" PreviousOuterNode: "+previousOuterNode+" Current Outer: "+currentOuterNode+" Previous Inner Node: "+previousInnerNode+" Current innerNode "+currentInnerNode);
                            System.out.println("CASE: PREVIOUS INNER CURRENT INNER LOONG? "+previousInnerCurrentInnerLong+" ON REALIZATION SPOT? "+isOnRealizationSpot(previousInnerNode,previousOuterNode)+" ON FIRST? " +isOnFirstRealizationSpot(previousInnerNode,previousOuterNode)  );
                            System.out.println( " previousOuterCurrentOuterLong? "+previousOuterCurrentOuterLong+" currentOuterCurrentInnerLong? "+currentOuterCurrentInnerLong);
                            System.out.println(" POS PREVIOUS OUTER: "+posPreviousOuter+" POS Current Inner: "+posCurrentInner+" POS relative: "+posRelative);
                            System.out.println("----------------------------- \n");

                            YPoint positionPreviousOuterNode = graph2D.getCenter(previousOuterNode);
                            YPoint positionCurrentInnerNode = graph2D.getCenter(currentInnerNode);

                            YPoint relativePoint = (YPoint) longSecondRealizationSpots.get(currentInnerNode);

                            double distancePreviousOuterCurrentInner = MetricCollection.euklideanDistanceR2(positionPreviousOuterNode.x,positionPreviousOuterNode.y,positionCurrentInnerNode.x,positionCurrentInnerNode.y);


                            if( distancePreviousOuterCurrentInner < lengthLongEdge+lengthShortEdge) {

                                destination = calculateClosestPointBiggerCircleCircleIntersection(positionCurrentInnerNode, lengthLongEdge, positionPreviousOuterNode, lengthShortEdge - 1, relativePoint);

                            }
                            else{
                                double adjustedLongLength = (distancePreviousOuterCurrentInner-lengthLongEdge-lengthShortEdge)+1+lengthLongEdge;
                                destination = calculateClosestPointBiggerCircleCircleIntersection(positionCurrentInnerNode, adjustedLongLength, positionPreviousOuterNode, lengthShortEdge - 1, relativePoint);

                            }
                            /*
                            YPoint[] intersections = calculateCircleCircleIntersection(positionPreviousOuterNode,lengthShortEdge,positionCurrentInnerNode,lengthLongEdge);
                            if( positionPreviousOuterNode.x > positionCurrentInnerNode.x && positionPreviousOuterNode.y < positionCurrentInnerNode.y){
                                destination = intersections[0];
                            }
                            else if(  positionPreviousOuterNode.x > positionCurrentInnerNode.x && positionPreviousOuterNode.y > positionCurrentInnerNode.y ){
                                destination = intersections[1];
                            }
                            else if(  positionPreviousOuterNode.x <= positionCurrentInnerNode.x && positionPreviousOuterNode.y <= positionCurrentInnerNode.y ){
                                destination = intersections[0];
                            }
                            else {
                                destination = intersections[1];
                            }
                            //destination = (YPoint) longRealizationSpots.get(currentInnerNode);
                            */
                        }
                        else if(previousOuterCurrentOuterLong && !currentOuterCurrentInnerLong){
                            System.out.println(" PreviousOuterNode: "+previousOuterNode+" Current Outer: "+currentOuterNode+" Previous Inner Node: "+previousInnerNode+" Current innerNode "+currentInnerNode);
                            System.out.println("CASE: PREVIOUS INNER CURRENT INNER LOONG? "+previousInnerCurrentInnerLong+" ON REALIZATION SPOT? "+isOnRealizationSpot(previousInnerNode,previousOuterNode)+" ON FIRST? " +isOnFirstRealizationSpot(previousInnerNode,previousOuterNode)  );
                            System.out.println( " previousOuterCurrentOuterLong? "+previousOuterCurrentOuterLong+" currentOuterCurrentInnerLong? "+currentOuterCurrentInnerLong);
                            System.out.println(" POS PREVIOUS OUTER: "+posPreviousOuter+" POS Current Inner: "+posCurrentInner+" POS relative: "+posRelative);
                            System.out.println("----------------------------- \n");

                            destination = (YPoint) shortSecondRealizationSpots.get(currentInnerNode);
                        }
                        else{
                            System.out.println(" PreviousOuterNode: "+previousOuterNode+" Current Outer: "+currentOuterNode+" Previous Inner Node: "+previousInnerNode+" Current innerNode "+currentInnerNode);
                            System.out.println("CASE: PREVIOUS INNER CURRENT INNER LOONG? "+previousInnerCurrentInnerLong+" ON REALIZATION SPOT? "+isOnRealizationSpot(previousInnerNode,previousOuterNode)+" ON FIRST? " +isOnFirstRealizationSpot(previousInnerNode,previousOuterNode)  );
                            System.out.println( " previousOuterCurrentOuterLong? "+previousOuterCurrentOuterLong+" currentOuterCurrentInnerLong? "+currentOuterCurrentInnerLong);
                            System.out.println(" POS PREVIOUS OUTER: "+posPreviousOuter+" POS Current Inner: "+posCurrentInner+" POS relative: "+posRelative);
                            System.out.println("----------------------------- \n");

                            YPoint positionPreviousOuterNode = graph2D.getCenter(previousOuterNode);
                            YPoint positionCurrentInnerNode = graph2D.getCenter(currentInnerNode);

                            YPoint relativePoint = (YPoint) longSecondRealizationSpots.get(currentInnerNode);


                            destination = calculateClosestPointCircleCircleIntersectionWithoutRel(positionPreviousOuterNode,lengthShortEdge-1,positionCurrentInnerNode,lengthShortEdge-1,relativePoint);

                            /*
                            YPoint[] intersections = calculateCircleCircleIntersection(positionPreviousOuterNode,lengthShortEdge,positionCurrentInnerNode,lengthShortEdge);
                            if( positionPreviousOuterNode.x > positionCurrentInnerNode.x && positionPreviousOuterNode.y < positionCurrentInnerNode.y){
                                destination = intersections[0];
                            }
                            else if(  positionPreviousOuterNode.x > positionCurrentInnerNode.x && positionPreviousOuterNode.y > positionCurrentInnerNode.y ){
                                destination = intersections[0];
                            }
                            else if(  positionPreviousOuterNode.x <= positionCurrentInnerNode.x && positionPreviousOuterNode.y <= positionCurrentInnerNode.y ){
                                destination = intersections[0];
                            }
                            else {
                                destination = intersections[1];
                            }
                            */
                            //destination = (YPoint) shortRealizationSpots.get(currentInnerNode);
                        }

                    }

                }
                else{

                    if( isOnRealizationSpot(previousInnerNode,previousOuterNode)){


                        if( isOnFirstRealizationSpot(previousInnerNode,previousOuterNode)){


                            if( previousOuterCurrentOuterLong && currentOuterCurrentInnerLong){
                                System.out.println(" PreviousOuterNode: "+previousOuterNode+" Current Outer: "+currentOuterNode+" Previous Inner Node: "+previousInnerNode+" Current innerNode "+currentInnerNode);
                                System.out.println("CASE: PREVIOUS INNER CURRENT INNER LOONG? "+previousInnerCurrentInnerLong+" ON REALIZATION SPOT? "+isOnRealizationSpot(previousInnerNode,previousOuterNode)+" ON FIRST? " +isOnFirstRealizationSpot(previousInnerNode,previousOuterNode)  );
                                System.out.println( " previousOuterCurrentOuterLong? "+previousOuterCurrentOuterLong+" currentOuterCurrentInnerLong? "+currentOuterCurrentInnerLong);
                                System.out.println(" POS PREVIOUS OUTER: "+posPreviousOuter+" POS Current Inner: "+posCurrentInner+" POS relative: "+posRelative);
                                System.out.println("----------------------------- \n");

                                destination = (YPoint) longSecondRealizationSpots.get(currentInnerNode);
                            }
                            else if( !previousOuterCurrentOuterLong && currentOuterCurrentInnerLong){
                                System.out.println(" PreviousOuterNode: "+previousOuterNode+" Current Outer: "+currentOuterNode+" Previous Inner Node: "+previousInnerNode+" Current innerNode "+currentInnerNode);
                                System.out.println("CASE: PREVIOUS INNER CURRENT INNER LOONG? "+previousInnerCurrentInnerLong+" ON REALIZATION SPOT? "+isOnRealizationSpot(previousInnerNode,previousOuterNode)+" ON FIRST? " +isOnFirstRealizationSpot(previousInnerNode,previousOuterNode)  );
                                System.out.println( " previousOuterCurrentOuterLong? "+previousOuterCurrentOuterLong+" currentOuterCurrentInnerLong? "+currentOuterCurrentInnerLong);
                                System.out.println(" POS PREVIOUS OUTER: "+posPreviousOuter+" POS Current Inner: "+posCurrentInner+" POS relative: "+posRelative);
                                System.out.println("----------------------------- \n");

                                YPoint relizationSpot = (YPoint) longRealizationSpots.get(currentInnerNode);
                                YPoint positionPreviousOuter = graph2D.getCenter(previousOuterNode);

                                double distance = MetricCollection.euklideanDistanceR2(relizationSpot.x,relizationSpot.y,positionPreviousOuter.x,positionPreviousOuter.y);
                                if( distance < lengthLongEdge) {
                                    destination = (YPoint) longRealizationSpots.get(currentInnerNode);
                                }
                                else{
                                    YPoint positionPreviousOuterNode = graph2D.getCenter(previousOuterNode);
                                    YPoint positionCurrentInnerNode = graph2D.getCenter(currentInnerNode);

                                    YPoint relativePoint = (YPoint) longSecondRealizationSpots.get(currentInnerNode);
                                    double distancePreviousOuterCurrentInner = MetricCollection.euklideanDistanceR2(positionPreviousOuterNode.x,positionPreviousOuterNode.y,positionCurrentInnerNode.x,positionCurrentInnerNode.y);


                                    if( distancePreviousOuterCurrentInner < lengthLongEdge+lengthShortEdge) {

                                        destination = calculateClosestPointBiggerCircleCircleIntersection(positionCurrentInnerNode, lengthLongEdge, positionPreviousOuterNode, lengthShortEdge - 1, relativePoint);

                                    }
                                    else{
                                        double adjustedLongLength = (distancePreviousOuterCurrentInner-lengthLongEdge-lengthShortEdge)+1+lengthLongEdge;
                                        destination = calculateClosestPointBiggerCircleCircleIntersection(positionCurrentInnerNode, adjustedLongLength, positionPreviousOuterNode, lengthShortEdge - 1, relativePoint);

                                    }
                                }
                            }
                            else if(previousOuterCurrentOuterLong && !currentOuterCurrentInnerLong){
                                System.out.println(" PreviousOuterNode: "+previousOuterNode+" Current Outer: "+currentOuterNode+" Previous Inner Node: "+previousInnerNode+" Current innerNode "+currentInnerNode);
                                System.out.println("CASE: PREVIOUS INNER CURRENT INNER LOONG? "+previousInnerCurrentInnerLong+" ON REALIZATION SPOT? "+isOnRealizationSpot(previousInnerNode,previousOuterNode)+" ON FIRST? " +isOnFirstRealizationSpot(previousInnerNode,previousOuterNode)  );
                                System.out.println( " previousOuterCurrentOuterLong? "+previousOuterCurrentOuterLong+" currentOuterCurrentInnerLong? "+currentOuterCurrentInnerLong);
                                System.out.println(" POS PREVIOUS OUTER: "+posPreviousOuter+" POS Current Inner: "+posCurrentInner+" POS relative: "+posRelative);
                                System.out.println("----------------------------- \n");

                                destination = (YPoint) shortSecondRealizationSpots.get(currentInnerNode);
                            }
                            else{
                                System.out.println(" PreviousOuterNode: "+previousOuterNode+" Current Outer: "+currentOuterNode+" Previous Inner Node: "+previousInnerNode+" Current innerNode "+currentInnerNode);
                                System.out.println("CASE: PREVIOUS INNER CURRENT INNER LOONG? "+previousInnerCurrentInnerLong+" ON REALIZATION SPOT? "+isOnRealizationSpot(previousInnerNode,previousOuterNode)+" ON FIRST? " +isOnFirstRealizationSpot(previousInnerNode,previousOuterNode)  );
                                System.out.println( " previousOuterCurrentOuterLong? "+previousOuterCurrentOuterLong+" currentOuterCurrentInnerLong? "+currentOuterCurrentInnerLong);
                                System.out.println(" POS PREVIOUS OUTER: "+posPreviousOuter+" POS Current Inner: "+posCurrentInner+" POS relative: "+posRelative);
                                System.out.println("----------------------------- \n");

                                YPoint relizationSpot = (YPoint) shortRealizationSpots.get(currentInnerNode);
                                YPoint positionPreviousOuter = graph2D.getCenter(previousOuterNode);

                                double distance = MetricCollection.euklideanDistanceR2(relizationSpot.x,relizationSpot.y,positionPreviousOuter.x,positionPreviousOuter.y);

                                if( distance < lengthLongEdge) {
                                    destination = (YPoint) shortRealizationSpots.get(currentInnerNode);
                                }
                                else{
                                    YPoint positionPreviousOuterNode = graph2D.getCenter(previousOuterNode);
                                    YPoint positionCurrentInnerNode = graph2D.getCenter(currentInnerNode);

                                    YPoint relativePoint = (YPoint) longSecondRealizationSpots.get(currentInnerNode);
                                    destination = calculateClosestPointCircleCircleIntersectionWithoutRel(positionPreviousOuterNode,lengthShortEdge-1,positionCurrentInnerNode,lengthShortEdge-1,relativePoint);

                                }
                            }

                        }
                        else{

                            if( previousOuterCurrentOuterLong && currentOuterCurrentInnerLong){
                                System.out.println(" PreviousOuterNode: "+previousOuterNode+" Current Outer: "+currentOuterNode+" Previous Inner Node: "+previousInnerNode+" Current innerNode "+currentInnerNode);
                                System.out.println("CASE: PREVIOUS INNER CURRENT INNER LOONG? "+previousInnerCurrentInnerLong+" ON REALIZATION SPOT? "+isOnRealizationSpot(previousInnerNode,previousOuterNode)+" ON FIRST? " +isOnFirstRealizationSpot(previousInnerNode,previousOuterNode)  );
                                System.out.println( " previousOuterCurrentOuterLong? "+previousOuterCurrentOuterLong+" currentOuterCurrentInnerLong? "+currentOuterCurrentInnerLong);
                                System.out.println(" POS PREVIOUS OUTER: "+posPreviousOuter+" POS Current Inner: "+posCurrentInner+" POS relative: "+posRelative);
                                System.out.println("----------------------------- \n");


                                YPoint realizationSpot = (YPoint) longRealizationSpots.get(currentInnerNode);
                                YPoint positionPreviousOuterNode = graph2D.getCenter(previousOuterNode);
                                YPoint positionCurrentInnerNode = graph2D.getCenter(currentInnerNode);
                                double distancePreviousOuterDestination = MetricCollection.euklideanDistanceR2(posPreviousOuter.x,posPreviousOuter.y,realizationSpot.x,realizationSpot.y);
                                double distanceCurrentInnerDestination = MetricCollection.euklideanDistanceR2(posPreviousOuter.x,posPreviousOuter.y,realizationSpot.x,realizationSpot.y);

                                if( distanceCurrentInnerDestination >= lengthLongEdge && distancePreviousOuterDestination >= lengthLongEdge) {
                                    destination = realizationSpot;
                                }
                                else{
                                    destination = calculateClosestPointCircleCircleIntersectionWithoutRel(positionPreviousOuterNode,lengthLongEdge,positionCurrentInnerNode,lengthLongEdge,realizationSpot);
                                }

                            }
                            else if( !previousOuterCurrentOuterLong && currentOuterCurrentInnerLong){
                                System.out.println(" PreviousOuterNode: "+previousOuterNode+" Current Outer: "+currentOuterNode+" Previous Inner Node: "+previousInnerNode+" Current innerNode "+currentInnerNode);
                                System.out.println("CASE: PREVIOUS INNER CURRENT INNER LOONG? "+previousInnerCurrentInnerLong+" ON REALIZATION SPOT? "+isOnRealizationSpot(previousInnerNode,previousOuterNode)+" ON FIRST? " +isOnFirstRealizationSpot(previousInnerNode,previousOuterNode)  );
                                System.out.println( " previousOuterCurrentOuterLong? "+previousOuterCurrentOuterLong+" currentOuterCurrentInnerLong? "+currentOuterCurrentInnerLong);
                                System.out.println(" POS PREVIOUS OUTER: "+posPreviousOuter+" POS Current Inner: "+posCurrentInner+" POS relative: "+posRelative);
                                System.out.println("----------------------------- \n");

                                YPoint relizationSpot = (YPoint) longSecondRealizationSpots.get(currentInnerNode);
                                YPoint positionPreviousOuter = graph2D.getCenter(previousOuterNode);

                                double distance = MetricCollection.euklideanDistanceR2(relizationSpot.x,relizationSpot.y,positionPreviousOuter.x,positionPreviousOuter.y);
                                if( distance < lengthLongEdge) {
                                    destination = (YPoint) longSecondRealizationSpots.get(currentInnerNode);
                                }
                                else{
                                    YPoint positionPreviousOuterNode = graph2D.getCenter(previousOuterNode);
                                    YPoint positionCurrentInnerNode = graph2D.getCenter(currentInnerNode);

                                    YPoint relativePoint = (YPoint) longSecondRealizationSpots.get(currentInnerNode);
                                    double distancePreviousOuterCurrentInner = MetricCollection.euklideanDistanceR2(positionPreviousOuterNode.x,positionPreviousOuterNode.y,positionCurrentInnerNode.x,positionCurrentInnerNode.y);


                                    if( distancePreviousOuterCurrentInner < lengthLongEdge+lengthShortEdge) {

                                        destination = calculateClosestPointBiggerCircleCircleIntersection(positionCurrentInnerNode, lengthLongEdge, positionPreviousOuterNode, lengthShortEdge - 1, relativePoint);

                                    }
                                    else{
                                        double adjustedLongLength = (distancePreviousOuterCurrentInner-lengthLongEdge-lengthShortEdge)+1+lengthLongEdge;
                                        destination = calculateClosestPointBiggerCircleCircleIntersection(positionCurrentInnerNode, adjustedLongLength, positionPreviousOuterNode, lengthShortEdge - 1, relativePoint);

                                    }
                                      }

                            }
                            else if(previousOuterCurrentOuterLong && !currentOuterCurrentInnerLong){
                                System.out.println(" PreviousOuterNode: "+previousOuterNode+" Current Outer: "+currentOuterNode+" Previous Inner Node: "+previousInnerNode+" Current innerNode "+currentInnerNode);
                                System.out.println("CASE: PREVIOUS INNER CURRENT INNER LOONG? "+previousInnerCurrentInnerLong+" ON REALIZATION SPOT? "+isOnRealizationSpot(previousInnerNode,previousOuterNode)+" ON FIRST? " +isOnFirstRealizationSpot(previousInnerNode,previousOuterNode)  );
                                System.out.println( " previousOuterCurrentOuterLong? "+previousOuterCurrentOuterLong+" currentOuterCurrentInnerLong? "+currentOuterCurrentInnerLong);
                                System.out.println(" POS PREVIOUS OUTER: "+posPreviousOuter+" POS Current Inner: "+posCurrentInner+" POS relative: "+posRelative);
                                System.out.println("----------------------------- \n");

                                destination = (YPoint) shortRealizationSpots.get(currentInnerNode);
                            }
                            else{
                                System.out.println(" PreviousOuterNode: "+previousOuterNode+" Current Outer: "+currentOuterNode+" Previous Inner Node: "+previousInnerNode+" Current innerNode "+currentInnerNode);
                                System.out.println("CASE: PREVIOUS INNER CURRENT INNER LOONG? "+previousInnerCurrentInnerLong+" ON REALIZATION SPOT? "+isOnRealizationSpot(previousInnerNode,previousOuterNode)+" ON FIRST? " +isOnFirstRealizationSpot(previousInnerNode,previousOuterNode)  );
                                System.out.println( " previousOuterCurrentOuterLong? "+previousOuterCurrentOuterLong+" currentOuterCurrentInnerLong? "+currentOuterCurrentInnerLong);
                                System.out.println(" POS PREVIOUS OUTER: "+posPreviousOuter+" POS Current Inner: "+posCurrentInner+" POS relative: "+posRelative);
                                System.out.println("----------------------------- \n");


                                YPoint relizationSpot = (YPoint) shortSecondRealizationSpots.get(currentInnerNode);
                                YPoint positionPreviousOuter = graph2D.getCenter(previousOuterNode);

                                double distance = MetricCollection.euklideanDistanceR2(relizationSpot.x,relizationSpot.y,positionPreviousOuter.x,positionPreviousOuter.y);

                                if( distance < lengthLongEdge) {
                                    destination = (YPoint) shortSecondRealizationSpots.get(currentInnerNode);
                                }
                                else{
                                    YPoint positionPreviousOuterNode = graph2D.getCenter(previousOuterNode);
                                    YPoint positionCurrentInnerNode = graph2D.getCenter(currentInnerNode);

                                    YPoint relativePoint = (YPoint) shortSecondRealizationSpots.get(currentInnerNode);
                                    destination = calculateClosestPointCircleCircleIntersectionWithoutRel(positionPreviousOuterNode,lengthShortEdge-1,positionCurrentInnerNode,lengthShortEdge-1,relativePoint);

                                }

                            }

                        }
                    }
                    else{

                        if( previousOuterCurrentOuterLong && currentOuterCurrentInnerLong){
                            System.out.println(" PreviousOuterNode: "+previousOuterNode+" Current Outer: "+currentOuterNode+" Previous Inner Node: "+previousInnerNode+" Current innerNode "+currentInnerNode);
                            System.out.println("CASE: PREVIOUS INNER CURRENT INNER LOONG? "+previousInnerCurrentInnerLong+" ON REALIZATION SPOT? "+isOnRealizationSpot(previousInnerNode,previousOuterNode)+" ON FIRST? " +isOnFirstRealizationSpot(previousInnerNode,previousOuterNode)  );
                            System.out.println( " previousOuterCurrentOuterLong? "+previousOuterCurrentOuterLong+" currentOuterCurrentInnerLong? "+currentOuterCurrentInnerLong);
                            System.out.println(" POS PREVIOUS OUTER: "+posPreviousOuter+" POS Current Inner: "+posCurrentInner+" POS relative: "+posRelative);
                            System.out.println("----------------------------- \n");

                            YPoint realizationSpot = (YPoint) longSecondRealizationSpots.get(currentInnerNode);
                            YPoint positionPreviousOuterNode = graph2D.getCenter(previousOuterNode);
                            YPoint positionCurrentInnerNode = graph2D.getCenter(currentInnerNode);
                            double distancePreviousOuterDestination = MetricCollection.euklideanDistanceR2(posPreviousOuter.x,posPreviousOuter.y,realizationSpot.x,realizationSpot.y);
                            double distanceCurrentInnerDestination = MetricCollection.euklideanDistanceR2(posPreviousOuter.x,posPreviousOuter.y,realizationSpot.x,realizationSpot.y);

                            if( distanceCurrentInnerDestination >= lengthLongEdge && distancePreviousOuterDestination >= lengthLongEdge) {
                                destination = (YPoint) longSecondRealizationSpots.get(currentInnerNode);
                            }
                            else{
                                destination = calculateClosestPointCircleCircleIntersectionWithoutRel(positionPreviousOuterNode,lengthLongEdge,positionCurrentInnerNode,lengthLongEdge,realizationSpot);
                            }
                        }
                        else if( !previousOuterCurrentOuterLong && currentOuterCurrentInnerLong){
                            System.out.println(" PreviousOuterNode: "+previousOuterNode+" Current Outer: "+currentOuterNode+" Previous Inner Node: "+previousInnerNode+" Current innerNode "+currentInnerNode);
                            System.out.println("CASE: PREVIOUS INNER CURRENT INNER LOONG? "+previousInnerCurrentInnerLong+" ON REALIZATION SPOT? "+isOnRealizationSpot(previousInnerNode,previousOuterNode)+" ON FIRST? " +isOnFirstRealizationSpot(previousInnerNode,previousOuterNode)  );
                            System.out.println( " previousOuterCurrentOuterLong? "+previousOuterCurrentOuterLong+" currentOuterCurrentInnerLong? "+currentOuterCurrentInnerLong);
                            System.out.println(" POS PREVIOUS OUTER: "+posPreviousOuter+" POS Current Inner: "+posCurrentInner+" POS relative: "+posRelative);
                            System.out.println("----------------------------- \n");

                            YPoint relizationSpot = (YPoint) longRealizationSpots.get(currentInnerNode);
                            YPoint positionPreviousOuter = graph2D.getCenter(previousOuterNode);

                            double distance = MetricCollection.euklideanDistanceR2(relizationSpot.x,relizationSpot.y,positionPreviousOuter.x,positionPreviousOuter.y);
                            if( distance < lengthLongEdge) {
                                destination = (YPoint) longRealizationSpots.get(currentInnerNode);
                            }
                            else{
                                YPoint positionPreviousOuterNode = graph2D.getCenter(previousOuterNode);
                                YPoint positionCurrentInnerNode = graph2D.getCenter(currentInnerNode);

                                YPoint relativePoint = (YPoint) longRealizationSpots.get(currentInnerNode);

                                double distancePreviousOuterCurrentInner = MetricCollection.euklideanDistanceR2(positionPreviousOuterNode.x,positionPreviousOuterNode.y,positionCurrentInnerNode.x,positionCurrentInnerNode.y);


                                if( distancePreviousOuterCurrentInner < lengthLongEdge+lengthShortEdge) {

                                    destination = calculateClosestPointBiggerCircleCircleIntersection(positionCurrentInnerNode, lengthLongEdge, positionPreviousOuterNode, lengthShortEdge - 1, relativePoint);

                                }
                                else{
                                    double adjustedLongLength = (distancePreviousOuterCurrentInner-lengthLongEdge-lengthShortEdge)+1+lengthLongEdge;
                                    destination = calculateClosestPointBiggerCircleCircleIntersection(positionCurrentInnerNode, adjustedLongLength, positionPreviousOuterNode, lengthShortEdge - 1, relativePoint);

                                }
                            }

                        }
                        else if(previousOuterCurrentOuterLong && !currentOuterCurrentInnerLong){
                            System.out.println(" PreviousOuterNode: "+previousOuterNode+" Current Outer: "+currentOuterNode+" Previous Inner Node: "+previousInnerNode+" Current innerNode "+currentInnerNode);
                            System.out.println("CASE: PREVIOUS INNER CURRENT INNER LOONG? "+previousInnerCurrentInnerLong+" ON REALIZATION SPOT? "+isOnRealizationSpot(previousInnerNode,previousOuterNode)+" ON FIRST? " +isOnFirstRealizationSpot(previousInnerNode,previousOuterNode)  );
                            System.out.println( " previousOuterCurrentOuterLong? "+previousOuterCurrentOuterLong+" currentOuterCurrentInnerLong? "+currentOuterCurrentInnerLong);
                            System.out.println(" POS PREVIOUS OUTER: "+posPreviousOuter+" POS Current Inner: "+posCurrentInner+" POS relative: "+posRelative);
                            System.out.println("----------------------------- \n");


                            YPoint realizationSpot = (YPoint) shortSecondRealizationSpots.get(currentInnerNode);
                            YPoint positionPreviousOuterNode = graph2D.getCenter(previousOuterNode);
                            YPoint positionCurrentInnerNode = graph2D.getCenter(currentInnerNode);

                            double distancePreviousOuterDestination = MetricCollection.euklideanDistanceR2(posPreviousOuter.x,posPreviousOuter.y,realizationSpot.x,realizationSpot.y);
                            double distanceCurrentInnerDestination = MetricCollection.euklideanDistanceR2(posPreviousOuter.x,posPreviousOuter.y,realizationSpot.x,realizationSpot.y);

                            if( distanceCurrentInnerDestination >= lengthLongEdge && distancePreviousOuterDestination >= lengthLongEdge) {
                                destination = realizationSpot;
                            }
                            else{

                                double distancePreviousOuterCurrentInner = MetricCollection.euklideanDistanceR2(positionPreviousOuterNode.x,positionPreviousOuterNode.y,positionCurrentInnerNode.x,positionCurrentInnerNode.y);


                                if( distancePreviousOuterCurrentInner < lengthLongEdge+lengthShortEdge) {

                                    destination = calculateClosestPointBiggerCircleCircleIntersection(positionPreviousOuterNode,lengthLongEdge,positionCurrentInnerNode,lengthShortEdge-1,realizationSpot);

                                }
                                else{
                                    double adjustedLongLength = (distancePreviousOuterCurrentInner-lengthLongEdge-lengthShortEdge)+1+lengthLongEdge;
                                    destination = calculateClosestPointBiggerCircleCircleIntersection(positionPreviousOuterNode, adjustedLongLength, positionCurrentInnerNode, lengthShortEdge - 1, realizationSpot);

                                }
                            }
                        }
                        else{
                            System.out.println(" PreviousOuterNode: "+previousOuterNode+" Current Outer: "+currentOuterNode+" Previous Inner Node: "+previousInnerNode+" Current innerNode "+currentInnerNode);
                            System.out.println("CASE: PREVIOUS INNER CURRENT INNER LOONG? "+previousInnerCurrentInnerLong+" ON REALIZATION SPOT? "+isOnRealizationSpot(previousInnerNode,previousOuterNode)+" ON FIRST? " +isOnFirstRealizationSpot(previousInnerNode,previousOuterNode)  );
                            System.out.println( " previousOuterCurrentOuterLong? "+previousOuterCurrentOuterLong+" currentOuterCurrentInnerLong? "+currentOuterCurrentInnerLong);
                            System.out.println(" POS PREVIOUS OUTER: "+posPreviousOuter+" POS Current Inner: "+posCurrentInner+" POS relative: "+posRelative);
                            System.out.println("----------------------------- \n");


                            YPoint relizationSpot = (YPoint) shortRealizationSpots.get(currentInnerNode);
                            YPoint positionPreviousOuter = graph2D.getCenter(previousOuterNode);

                            double distance = MetricCollection.euklideanDistanceR2(relizationSpot.x,relizationSpot.y,positionPreviousOuter.x,positionPreviousOuter.y);

                            if( distance < lengthLongEdge) {
                                destination = (YPoint) shortRealizationSpots.get(currentInnerNode);
                            }
                            else{
                                YPoint positionPreviousOuterNode = graph2D.getCenter(previousOuterNode);
                                YPoint positionCurrentInnerNode = graph2D.getCenter(currentInnerNode);

                                YPoint relativePoint = (YPoint) shortRealizationSpots.get(currentInnerNode);
                                destination = calculateClosestPointCircleCircleIntersectionWithoutRel(positionPreviousOuterNode,lengthShortEdge-1,positionCurrentInnerNode,lengthShortEdge-1,relativePoint);

                            }

                        }

                    }

                }

            basicGraph.moveNode(currentOuterNode,destination);

        }
        this.realizeLastOuterVertex();
    }

    private void realizeLastOuterVertex(){

        double lengthShortEdge = getLengthShortEdges();
        double lengthLongEdge = getLengthLongEdges();

        int i = outerCycle.size()-1;

        Node previousOuterNode = (Node) outerCycle.get(i - 1);
        Node currentOuterNode = (Node) outerCycle.get(i);

        Node currentInnerNode = (Node) innerCycle.get(i);

        boolean previousOuterCurrentOuterLong = isEdgeLong(previousOuterNode, currentOuterNode);
        boolean currentOuterCurrentInnerLong = isEdgeLong(currentOuterNode, currentInnerNode);

        YPoint positionCurrentInnerVertex = graph2D.getCenter(currentInnerNode);
        YPoint positionPreviousOuterVertex = graph2D.getCenter(previousOuterNode);
        YPoint positionFirstOuterVertex = graph2D.getCenter((Node) outerCycle.get(0));

        YPoint destination;

        if( previousOuterCurrentOuterLong && currentOuterCurrentInnerLong){
            destination = farthestCircleCircleIntersectionPointToRelativePoint(positionCurrentInnerVertex,2*lengthLongEdge,positionPreviousOuterVertex,2*lengthLongEdge,positionFirstOuterVertex);
        }
        else if( previousOuterCurrentOuterLong && !currentOuterCurrentInnerLong){
            destination = calculateFarthestPointBiggerCircleCircleIntersection( positionPreviousOuterVertex, lengthLongEdge, positionCurrentInnerVertex,lengthShortEdge-1,positionFirstOuterVertex);
        }
        else if( !previousOuterCurrentOuterLong && currentOuterCurrentInnerLong){
            destination =  calculateFarthestPointBiggerCircleCircleIntersection( positionCurrentInnerVertex,lengthLongEdge, positionPreviousOuterVertex,lengthShortEdge-1,positionFirstOuterVertex);
        }
        else{
            destination = calculateFarthestPointCircleCircleIntersection(positionCurrentInnerVertex,lengthShortEdge-1,positionPreviousOuterVertex,lengthShortEdge-1,positionFirstOuterVertex);
        }

        System.out.println(destination);
        basicGraph.moveNode(currentOuterNode,destination);

    }

    private NodeMap createInnerCycleMapping(){

        int currentIndex = 1;

        NodeMap result = Maps.createHashedNodeMap();


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
                result.setInt(currentNode,currentIndex);
                currentIndex += 1;
            }
            else if( shortEdges.contains(currentEdge)){
                result.setInt(currentNode,currentIndex);
                currentIndex += 0;
            }
        }

        return result;
    }


    private ArrayList<Node> getNodesWithIndexMapping(int index, NodeMap indexMapping){
        ArrayList<Node> result = new ArrayList<>();
        for( NodeCursor v= innerCycle.nodes(); v.ok(); v.next()){
            Node currentNode = v.node();
            int currentIndex = indexMapping.getInt(currentNode);
            if( currentIndex == index){
                result.add(currentNode);
            }
        }
        return result;
    }

    private void realizeInnerCycle(){

        this.shortRealizationSpots = Maps.createHashedNodeMap();
        this.longRealizationSpots = Maps.createHashedNodeMap();

        this.shortSecondRealizationSpots = Maps.createHashedNodeMap();
        this.longSecondRealizationSpots = Maps.createHashedNodeMap();

        int numLongEdges = calculateNumLongEdgesInnerCycle();
        int n = numLongEdges;

        NodeMap indexNodeMapping = createInnerCycleMapping();

        double alpha = 360 / (double) n;
        double currentAngle = 0;
        double currentAngleClockwise = 180-alpha;

        double distance = CYCLE_RADIUS;
        YPoint origin = new YPoint(0,0);
        basicGraph.createNode(origin);

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

            ArrayList<Node> nodesWithIndexI = getNodesWithIndexMapping(i,indexNodeMapping);


            double basisAngle = 61;

            for( Node node: nodesWithIndexI) {
                basicGraph.moveNode(node, destination);

                YPoint shortRealizationSpot;
                YPoint longRealizationSpot;

                //YPoint extention = basisChange(extendLineSegment(origin,destination,-lengthLongEdges));
                              //basicGraph.createNode(intersections[0]);
               // basicGraph.createNode(intersections[1]);
                         //Node asd = basicGraph.createNode(extention);
                //graph2D.getRealizer(asd).setFillColor(Color.blue);

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

                /*
                if( currentAngle > 180) {
                    double tempAngle = Math.toRadians(currentAngleClockwise);

                    double shortX =  ( (distance+lengthShortEdges) * Math.cos(tempAngle) ) + origin.x ;
                    double shortY =  origin.y - ( (distance+lengthShortEdges) * Math.sin(tempAngle) )  ;

                    double longX =  ( (distance+lengthLongEdges) * Math.cos(tempAngle) ) + origin.x ;
                    double longY =  origin.y - ( (distance+lengthLongEdges) * Math.sin(tempAngle) )  ;

                    shortRealizationSpot = new YPoint(shortX,shortY);
                    longRealizationSpot = new YPoint(longX,longY);

                    if( i == 1){
                        YPoint shortSpot = calculateIntersectionDownSide(destination,basisAngle,lengthShortEdges);
                        YPoint longSpot = calculateIntersectionDownSide(destination,basisAngle,lengthLongEdges);
                        this.shortSecondRealizationSpots.set(node,shortSpot);
                        this.longSecondRealizationSpots.set(node,longSpot);
                       basicGraph.createNode(shortSpot);
                        basicGraph.createNode(longSpot);
                    }
                    else{
                        if( destination.x < shortRealizationSpot.x && destination.y < shortRealizationSpot.y){
                            double adjacent = shortRealizationSpot.y - destination.y;
                            double opposite = shortRealizationSpot.x - destination.x;
                            double pythagorasAngle = Math.toDegrees(Math.atan( opposite/adjacent));
                            double angleOffset = 90 - pythagorasAngle;

                            YPoint shortSpot = calculateIntersectionDownSide(destination,basisAngle+angleOffset,lengthShortEdges);
                            YPoint longSpot = calculateIntersectionDownSide(destination,basisAngle+angleOffset,lengthLongEdges);
                            this.shortSecondRealizationSpots.set(node,shortSpot);
                            this.longSecondRealizationSpots.set(node,longSpot);
                            basicGraph.createNode(shortSpot);
                           basicGraph.createNode(longSpot);
                        }
                        else if( destination.x > shortRealizationSpot.x && destination.y < shortRealizationSpot.y){
                            double adjacent = destination.x - shortRealizationSpot.x;
                            double opposite = shortRealizationSpot.y - destination.y;

                            double pythagorasAngle = Math.toDegrees(Math.atan( opposite/adjacent));
                            double angleOffset = 180 - pythagorasAngle;
                            YPoint shortSpot = calculateIntersectionDownSide(destination,basisAngle+angleOffset,lengthShortEdges);
                            YPoint longSpot = calculateIntersectionDownSide(destination,basisAngle+angleOffset,lengthLongEdges);
                            this.shortSecondRealizationSpots.set(node,shortSpot);
                            this.longSecondRealizationSpots.set(node,longSpot);
                            basicGraph.createNode(shortSpot);
                            basicGraph.createNode(longSpot);

                        }
                        else if( destination.x > shortRealizationSpot.x && destination.y > shortRealizationSpot.y){
                            double opposite = destination.y - shortRealizationSpot.y;
                            double adjacent = destination.x - shortRealizationSpot.x;
                            double pythagorasAngle = Math.toDegrees(Math.atan( opposite/adjacent));
                            double angleOffset = 180 - pythagorasAngle;

                            YPoint shortSpot = calculateIntersectionUpSide(destination,angleOffset-basisAngle,lengthShortEdges);
                            YPoint longSpot = calculateIntersectionUpSide(destination,angleOffset-basisAngle,lengthLongEdges);
                            this.shortSecondRealizationSpots.set(node,shortSpot);
                            this.longSecondRealizationSpots.set(node,longSpot);
                            basicGraph.createNode(shortSpot);
                          basicGraph.createNode(longSpot);

                        }
                        else if( destination.x < shortRealizationSpot.x && destination.y > shortRealizationSpot.y){
                            double opposite = shortRealizationSpot.x - destination.x;
                            double adjacent = destination.y - shortRealizationSpot.y;

                            double pythagorasAngle = Math.toDegrees(Math.atan( opposite/adjacent));
                            double angleOffset = 90 - pythagorasAngle;

                            YPoint shortSpot = calculateIntersectionUpSide(destination,angleOffset-basisAngle,lengthShortEdges);
                            YPoint longSpot = calculateIntersectionUpSide(destination,angleOffset-basisAngle,lengthLongEdges);
                            this.shortSecondRealizationSpots.set(node,shortSpot);
                            this.longSecondRealizationSpots.set(node,longSpot);
                            basicGraph.createNode(shortSpot);
                           basicGraph.createNode(longSpot);
                        }

                    }
                }
                else{
                    double tempAngle = Math.toRadians(currentAngle);
                    double shortX = origin.x + ( (distance+lengthShortEdges) * Math.cos(tempAngle) );
                    double shortY = origin.y + ( (distance+lengthShortEdges) * Math.sin(tempAngle) );

                    double longX = origin.x + ( (distance+lengthLongEdges) * Math.cos(tempAngle) );
                    double longY = origin.y + ( (distance+lengthLongEdges) * Math.sin(tempAngle) );

                    shortRealizationSpot = new YPoint(shortX,shortY);
                    longRealizationSpot = new YPoint(longX,longY);

                    if( i == 1){
                        YPoint shortSpot = calculateIntersectionDownSide(destination,basisAngle,lengthShortEdges);
                        YPoint longSpot = calculateIntersectionDownSide(destination,basisAngle,lengthLongEdges);
                        this.shortSecondRealizationSpots.set(node,shortSpot);
                        this.longSecondRealizationSpots.set(node,longSpot);
                       basicGraph.createNode(shortSpot);
                        basicGraph.createNode(longSpot);
                    }
                    else{
                        if( destination.x < shortRealizationSpot.x && destination.y < shortRealizationSpot.y){
                            double adjacent = shortRealizationSpot.y - destination.y;
                            double opposite = shortRealizationSpot.x - destination.x;
                            double pythagorasAngle = Math.toDegrees(Math.atan( opposite/adjacent));
                            double angleOffset = 90 - pythagorasAngle;

                            YPoint shortSpot = calculateIntersectionDownSide(destination,basisAngle+angleOffset,lengthShortEdges);
                            YPoint longSpot = calculateIntersectionDownSide(destination,basisAngle+angleOffset,lengthLongEdges);
                            this.shortSecondRealizationSpots.set(node,shortSpot);
                            this.longSecondRealizationSpots.set(node,longSpot);
                           basicGraph.createNode(shortSpot);
                           basicGraph.createNode(longSpot);
                        }
                        else if( destination.x > shortRealizationSpot.x && destination.y < shortRealizationSpot.y){
                            double adjacent = destination.x - shortRealizationSpot.x;
                            double opposite = shortRealizationSpot.y - destination.y;

                            double pythagorasAngle = Math.toDegrees(Math.atan( opposite/adjacent));
                            double angleOffset = 180 - pythagorasAngle;
                            YPoint shortSpot = calculateIntersectionDownSide(destination,basisAngle+angleOffset,lengthShortEdges);
                            YPoint longSpot = calculateIntersectionDownSide(destination,basisAngle+angleOffset,lengthLongEdges);
                            this.shortSecondRealizationSpots.set(node,shortSpot);
                            this.longSecondRealizationSpots.set(node,longSpot);
                            basicGraph.createNode(shortSpot);
                            basicGraph.createNode(longSpot);

                        }

                    }
                }



                this.shortRealizationSpots.set(node,shortRealizationSpot);
                this.longRealizationSpots.set(node,longRealizationSpot);

            }
            */



        }

        ArrayList<Node> nodesWithIndexI = getNodesWithIndexMapping(n+1,indexNodeMapping);

        Node firstInnerVertex = (Node) innerCycle.get(0);
        YPoint positionFirstInnerVertex = graph2D.getCenter( firstInnerVertex);
        String res = "";
        for( Node node: nodesWithIndexI){
            res+= " "+node;
        }
        System.out.println(res);

            for( int i = 0; i < nodesWithIndexI.size(); i++){
                Node currentNode = nodesWithIndexI.get(i);

                basicGraph.moveNode(currentNode,positionFirstInnerVertex);

                this.shortRealizationSpots.set(currentNode,(YPoint) shortRealizationSpots.get(firstInnerVertex));
                this.shortSecondRealizationSpots.set(currentNode,(YPoint) shortSecondRealizationSpots.get(firstInnerVertex));
                this.longRealizationSpots.set(currentNode,(YPoint) longRealizationSpots.get(firstInnerVertex));
                this.longSecondRealizationSpots.set(currentNode,(YPoint) longSecondRealizationSpots.get(firstInnerVertex));
            }


    }






    private YPoint calculateCenterGraphView(){
        double worldHeight = this.graphView.getWorldRect2D().getHeight();
        double worldWidth = this.graphView.getWorldRect2D().getWidth();
        return new YPoint(worldWidth/2,worldHeight/2);
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

    private double getLengthShortEdges(){
        int numLongEdges = calculateNumLongEdgesInnerCycle();
        int n = numLongEdges;
        return 2 * CYCLE_RADIUS * Math.sin( Math.toRadians(170/ (double) n));
    }

    private double getLengthLongEdges(){
        int numLongEdges = calculateNumLongEdgesInnerCycle();
        int n = numLongEdges;
        return 2 * CYCLE_RADIUS * Math.sin( Math.toRadians(180/ (double) n));
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


    private void initializeAssets(){
        this.shortEdges = graphView.getRelationships().getEdgesFromPartition(1);
        this.longEdges = graphView.getRelationships().getEdgesFromPartition(2);
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

    private void initializeVertexOrder() {

        PrismGraphCycles prismGraphCycles = new PrismGraphCycles(graph2D);
        this.vertexOrder = prismGraphCycles.getVertexOrder();
    }

    @Override
    public boolean isGraphType() {
        return false;
    }
}
