package realizations;

import model.BasicGraph;
import model.MetricCollection;
import model.Relationships;
import view.GraphView;
import y.base.*;
import y.geom.AffineLine;
import y.geom.YCircle;
import y.geom.YPoint;
import y.view.Graph2D;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import static y.util.Generics.edges;
import static y.util.Generics.nodes;

/**
 * Created by chris on 21.12.2017.
 */
public abstract class Realization {

    private final int SMALL_NEIGHBOURHOOD_OFFSET = 10;

    public GraphView graphView;
    public Graph2D graph2D;
    public BasicGraph basicGraph;

    public Realization(GraphView graphView){
        this.graphView = graphView;
        this.graph2D = graphView.getGraph2D();
        this.basicGraph = graphView.getGraph();
    }


    public abstract boolean isGraphType();

    public ArrayList<Node> getGraphNodes(){
        ArrayList<Node> result = new ArrayList<>();
        for(NodeCursor v = graph2D.nodes(); v.ok(); v.next()) {
            Node currentNode = v.node();
            result.add(currentNode);
        }
        return result;
    }

    public ArrayList<Edge> getGraphEdges(){
        ArrayList<Edge> result = new ArrayList<>();
        for(EdgeCursor e = graph2D.edges(); e.ok(); e.next()) {
            Edge currentEdge = e.edge();
            result.add(currentEdge);

        }
        return result;
    }




    public YPoint extendLineSegment(YPoint p1,YPoint p2, double distance){


        double point1X = p1.getX();
        double point1Y = p1.getY();
        double point2X = p2.getX();
        double point2Y = p2.getY();

        double angle;

        if (point2X - point1X == 0) {
            angle = 90;
        } else {

            double slope = (point2Y - point1Y) / (point2X - point1X);

            if (slope == 0) {
                angle = 0;
            } else {
                angle = Math.toDegrees(Math.atan(slope));
            }
        }

        System.out.println(angle);

        if( point1X <= point2X && point1Y < point2Y ){
            System.out.println("case1");
            return calculateIntersectionDownSide(p2, angle, distance);
        }
        if( point1X > point2X && point1Y < point2Y){
            System.out.println("case2");
            return calculateIntersectionDownSide(p2, 180+angle , distance);
        }
        else if( point1X >= point2X && point1Y > point2Y){
            System.out.println("case3");
            return calculateIntersectionUpSide(p2, 180-angle, distance);
        }
        else{
            System.out.println("case4"+" "+(-angle));
            return calculateIntersectionUpSide(p2, -angle, distance);
        }

    }

    public YPoint MinCircleCircleIntersection(YPoint p1, double radius1, YPoint p2, double radius2, YPoint rel){
        YPoint[] intersections = calculateCircleCircleIntersection(p1,radius1,p2,radius2);

        YPoint i1 = intersections[0];
        YPoint i2 = intersections[1];

        if( MetricCollection.euklideanDistanceR2(rel,i1) < MetricCollection.euklideanDistanceR2(rel,i2)){
            return i1;
        }
        else{
            return i2;
        }
    }

    public YPoint MaxCircleCircleIntersection(YPoint p1, double radius1, YPoint p2, double radius2, YPoint rel){
        YPoint[] intersections = calculateCircleCircleIntersection(p1,radius1,p2,radius2);

        YPoint i1 = intersections[0];
        YPoint i2 = intersections[1];

        if( MetricCollection.euklideanDistanceR2(rel,i1) > MetricCollection.euklideanDistanceR2(rel,i2)){
            return i1;
        }
        else{
            return i2;
        }
    }


    public YPoint basisChange(YPoint p){
        return new YPoint(p.getX(),-p.getY());
    }

    public YPoint calculatePointOnCircleCounterClockwiseR2(YPoint center, double angle , double distance){
        double tempAngle = Math.toRadians(angle);

        center = basisChange(center);
        double tempX =  center.x + ( distance * Math.cos(tempAngle) )  ;
        double tempY =  center.y + ( distance * Math.sin(tempAngle) ) ;

        YPoint tempPoint = new YPoint(tempX,tempY);
        return tempPoint;
    }

    public YPoint calculatePointOnCircleClockwiseR2(YPoint center, double angle , double distance){
        double tempAngle = Math.toRadians(angle);

        double tempX =  center.x + ( distance * Math.cos(tempAngle) )  ;
        double tempY =  center.y + ( distance * (-Math.sin(tempAngle)) ) ;

        YPoint tempPoint = new YPoint(tempX,tempY);
        return tempPoint;
    }

    public YPoint calculatePointOnCircleCounterClockwise(YPoint center, double angle , double distance){
        double tempAngle = Math.toRadians(angle);

        center = basisChange(center);
        double tempX =  center.x + ( distance * Math.cos(tempAngle) )  ;
        double tempY =  center.y + ( distance * Math.sin(tempAngle) ) ;

        YPoint tempPoint = new YPoint(tempX,tempY);
        return basisChange(tempPoint);
    }

    public YPoint calculatePointOnCircleClockwise(YPoint center, double angle , double distance){
        double tempAngle = Math.toRadians(angle);

        center = basisChange(center);
        double tempX =  center.x + ( distance * Math.cos(tempAngle) )  ;
        double tempY =  center.y + ( distance * (-Math.sin(tempAngle)) ) ;

        YPoint tempPoint = new YPoint(tempX,tempY);
        return basisChange(tempPoint);
    }


    public YPoint calculateIntersectionUpSide( YPoint center, double angle , double distance){
        double tempAngle = Math.toRadians(angle);

        double tempX =  center.x + ( distance * Math.cos(tempAngle) ) ;
        double tempY =  center.y - ( distance* Math.sin(tempAngle) )  ;
        return new YPoint(tempX,tempY);
    }

    public YPoint calculateIntersectionDownSide( YPoint center , double angle, double distance){
        double tempAngle = Math.toRadians(angle);

        double tempX =  center.x + ( distance * Math.cos(tempAngle) )  ;
        double tempY =  center.y + ( distance * Math.sin(tempAngle) ) ;

        return new YPoint(tempX,tempY);
    }


    public YPoint farthestCircleCircleIntersectionPointToRelativePoint( YPoint center1, double radius1 , YPoint center2, double radius2, YPoint relativePoint){

        YPoint[] intersections = calculateCircleCircleIntersection(center1,radius1,center2,radius2);

        YPoint intersection1 = intersections[0];
        YPoint intersection2 = intersections[1];

        double distClosestPointIntersection1 = MetricCollection.euklideanDistanceR2(intersection1.x,intersection1.y,relativePoint.x,relativePoint.y);
        double distClosestPointIntersection2 = MetricCollection.euklideanDistanceR2(intersection2.x,intersection2.y,relativePoint.x,relativePoint.y);

        if( distClosestPointIntersection1 > distClosestPointIntersection2){
            return intersection1;
        }
        else{
            return intersection2;
        }
    }

    public YPoint closestCircleCircleIntersectionPointToRelativePoint( YPoint center1, double radius1 , YPoint center2, double radius2, YPoint relativePoint){

        YPoint[] intersections = calculateCircleCircleIntersection(center1,radius1,center2,radius2);

        YPoint intersection1 = intersections[0];
        YPoint intersection2 = intersections[1];

        double distClosestPointIntersection1 = MetricCollection.euklideanDistanceR2(intersection1.x,intersection1.y,relativePoint.x,relativePoint.y);
        double distClosestPointIntersection2 = MetricCollection.euklideanDistanceR2(intersection2.x,intersection2.y,relativePoint.x,relativePoint.y);

        if( distClosestPointIntersection1 < distClosestPointIntersection2){
            System.out.println(intersection1);
            return intersection1;
        }
        else{
            System.out.println(intersection2);
            return intersection2;
        }
    }

    public YPoint farthestPointOnHalfEllipseToPoint(YPoint center, double rotationAngle, double a, double b, boolean intervalUpper,YPoint relativePoint){

        double minimalDistance = Double.NEGATIVE_INFINITY;;
        double resultX = 0;
        double resultY = 0;

        double angle;
        double upperBoundAngle;

        if( intervalUpper ){
            angle = 0;
            upperBoundAngle = 180;

        }
        else{
            angle = 180;
            upperBoundAngle = 360;
        }

        while( angle <= upperBoundAngle){
            angle += 0.5;

            double currentX = ((a * Math.cos(Math.toRadians(rotationAngle))) * Math.cos(Math.toRadians(angle))) + (-b * Math.sin(Math.toRadians(rotationAngle))*Math.sin(Math.toRadians(angle))) + center.getX();
            double currentY = ((a * Math.sin(Math.toRadians(rotationAngle))) * Math.cos(Math.toRadians(angle))) + (b * Math.cos(Math.toRadians(rotationAngle))*Math.sin(Math.toRadians(angle))) + center.getY();

            double currentDistance = MetricCollection.euklideanDistanceR2(currentX,currentY,relativePoint.x,relativePoint.y);

            if( ( currentDistance > minimalDistance) ){
                minimalDistance = currentDistance;
                resultX = currentX;
                resultY = currentY;
            }
        }

        YPoint result = new YPoint(resultX,resultY);
        return result;

    }


    public YPoint closestPointOnHalfEllipseToPoint(YPoint center, double rotationAngle, double a, double b, boolean intervalUpper, YPoint relativePoint){

        double minimalDistance = Double.POSITIVE_INFINITY;
        double resultX = 0;
        double resultY = 0;

        double angle;
        double upperBoundAngle;

        if( intervalUpper ){
            angle = 0;
            upperBoundAngle = 180;

        }
        else{
            angle = 180;
            upperBoundAngle = 360;
        }

        while( angle <= upperBoundAngle){
            angle += 0.5;

            double currentX = ((a * Math.cos(Math.toRadians(rotationAngle))) * Math.cos(Math.toRadians(angle))) + (-b * Math.sin(Math.toRadians(rotationAngle))*Math.sin(Math.toRadians(angle))) + center.getX();
            double currentY = ((a * Math.sin(Math.toRadians(rotationAngle))) * Math.cos(Math.toRadians(angle))) + (b * Math.cos(Math.toRadians(rotationAngle))*Math.sin(Math.toRadians(angle))) + center.getY();

            double currentDistance = MetricCollection.euklideanDistanceR2(currentX,currentY,relativePoint.x,relativePoint.y);

            if( currentDistance < minimalDistance){
                minimalDistance = currentDistance;
                resultX = currentX;
                resultY = currentY;
            }
        }

        YPoint result = new YPoint(resultX,resultY);
        return result;

    }

    public YPoint farthestPointOnEllipseToPoint( YPoint center, double rotationAngle, double a, double b, YPoint point){
        double pointX = point.getX();
        double pointY = point.getY();

        double angle = 0;
        double upperBound = 360;

        double minimalDistance = Double.NEGATIVE_INFINITY;
        double resultX = 0;
        double resultY = 0;

        while( angle <= upperBound){
            angle += 0.5;

            double currentX = ((a * Math.cos(Math.toRadians(rotationAngle))) * Math.cos(Math.toRadians(angle))) + (-b * Math.sin(Math.toRadians(rotationAngle))*Math.sin(Math.toRadians(angle))) + center.getX();
            double currentY = ((a * Math.sin(Math.toRadians(rotationAngle))) * Math.cos(Math.toRadians(angle))) + (b * Math.cos(Math.toRadians(rotationAngle))*Math.sin(Math.toRadians(angle))) + center.getY();

            double distanceCurrentPoint = MetricCollection.euklideanDistanceR2(currentX,currentY,pointX,pointY);

            if( distanceCurrentPoint > minimalDistance){
                minimalDistance = distanceCurrentPoint;
                resultX = currentX;
                resultY = currentY;
            }
        }

        YPoint result = new YPoint(resultX,resultY);
        return result;
    }

    public YPoint closestPointOnEllipseToPointWithoutRel(YPoint center, double rotationAngle, double a, double b, YPoint point){

        double pointX = point.getX();
        double pointY = point.getY();

        double angle = 0;
        double upperBound = 360;

        double minimalDistance = Double.POSITIVE_INFINITY;;
        double resultX = 0;
        double resultY = 0;

        while( angle <= upperBound){
            angle += 0.5;

            double currentX = ((a * Math.cos(Math.toRadians(rotationAngle))) * Math.cos(Math.toRadians(angle))) + (-b * Math.sin(Math.toRadians(rotationAngle))*Math.sin(Math.toRadians(angle))) + center.getX();
            double currentY = ((a * Math.sin(Math.toRadians(rotationAngle))) * Math.cos(Math.toRadians(angle))) + (b * Math.cos(Math.toRadians(rotationAngle))*Math.sin(Math.toRadians(angle))) + center.getY();

            double distanceCurrentPoint = MetricCollection.euklideanDistanceR2(currentX,currentY,pointX,pointY);

            if( distanceCurrentPoint < minimalDistance){
                minimalDistance = distanceCurrentPoint;
                resultX = currentX;
                resultY = currentY;
            }
        }

        YPoint result = new YPoint(resultX,resultY);
        System.out.println(result);
        return result;
    }

    public YPoint closestPointOnEllipseToPoint(YPoint center, double rotationAngle, double a, double b, YPoint point, YPoint relativePoint, double lengthLongEdge, double lengthShortEdge){

        double pointX = point.getX();
        double pointY = point.getY();

        double angle = 0;
        double upperBound = 360;

        double minimalDistance = Double.POSITIVE_INFINITY;;
        double resultX = 0;
        double resultY = 0;

        while( angle <= upperBound){
           angle += 0.5;

            double currentX = ((a * Math.cos(Math.toRadians(rotationAngle))) * Math.cos(Math.toRadians(angle))) + (-b * Math.sin(Math.toRadians(rotationAngle))*Math.sin(Math.toRadians(angle))) + center.getX();
            double currentY = ((a * Math.sin(Math.toRadians(rotationAngle))) * Math.cos(Math.toRadians(angle))) + (b * Math.cos(Math.toRadians(rotationAngle))*Math.sin(Math.toRadians(angle))) + center.getY();

            double distanceCurrentPoint = MetricCollection.euklideanDistanceR2(currentX,currentY,pointX,pointY);

            double distancePointToRelative = MetricCollection.euklideanDistanceR2(currentX,currentY,relativePoint.getX(),relativePoint.getY());
            double longShortDifference = Math.abs(lengthLongEdge-lengthShortEdge) + (0.1 * lengthShortEdge);
            double sumDistances = 2*lengthShortEdge;

          //  System.out.println("CURRENT DEGREE: "+angle+" DISTANCE: "+MetricCollection.euklideanDistanceR2(resultX,resultY,pointX,pointY));

           // basicGraph.createNode(new YPoint(currentX,currentY));
            if( distanceCurrentPoint < minimalDistance && (distancePointToRelative > longShortDifference) && (distancePointToRelative < sumDistances )){
                minimalDistance = distanceCurrentPoint;
                resultX = currentX;
                resultY = currentY;
            }
        }

        YPoint result = new YPoint(resultX,resultY);
        System.out.println(result);
        return result;
    }


    public YPoint calculateFarthestPointBiggerCircleCircleIntersection( YPoint centerBigger, double radiusBigger, YPoint center2, double radius2, YPoint relativePoint){
        YPoint[] circleCircleIntersectionPoints = calculateCircleCircleIntersection(centerBigger,radiusBigger,center2,radius2);

        YPoint intersection1 = circleCircleIntersectionPoints[0];
        YPoint intersection2 = circleCircleIntersectionPoints[1];


        double center1X = centerBigger.getX();
        double center1Y = centerBigger.getY();
        double center2X = center2.getX();
        double center2Y = center2.getY();

        double intersection1X = intersection1.getX();
        double intersection1Y = intersection1.getY();
        double intersection2X = intersection2.getX();
        double intersection2Y = intersection2.getY();

        double a = MetricCollection.euklideanDistanceR2(intersection1X,intersection1Y,intersection2X,intersection2Y) * 0.5;

        AffineLine center1Center2 = new AffineLine(centerBigger,center2);

        YPoint[] intersectionCircle1LineCenter1Center2 = calculateIntersectionCircleLine(center1X,center1Y,radiusBigger,center1Center2);


        YPoint firstCycleB;

        YPoint firstIntersection1 = intersectionCircle1LineCenter1Center2[0];
        YPoint secondIntersection1 = intersectionCircle1LineCenter1Center2[1];
        double distanceFirstCycle2 = MetricCollection.euklideanDistanceR2(firstIntersection1.x,firstIntersection1.y,center2X,center2Y);
        double distanceSecondCycle2 = MetricCollection.euklideanDistanceR2(secondIntersection1.x,secondIntersection1.y,center2X,center2Y);

        if( distanceFirstCycle2 < distanceSecondCycle2){
            firstCycleB = firstIntersection1;
        }
        else{
            firstCycleB = secondIntersection1;
        }

        YPoint middleCenter1Center2 = new YPoint( (intersection1X+intersection2X)*0.5, (intersection1Y+intersection2Y)*0.5);

        double distanceBiggerCycleBCenter = MetricCollection.euklideanDistanceR2(middleCenter1Center2.x,middleCenter1Center2.y,firstCycleB.x,firstCycleB.y);


        double b = distanceBiggerCycleBCenter;

        double middleX = middleCenter1Center2.getX();
        double middleY = middleCenter1Center2.getY();

        double rotationAngle = 0;


        if( intersection1X <= middleX && intersection1Y <= middleY ){
            YPoint imaginaryPoint = this.calculateIntersectionUpSide(middleCenter1Center2,180,a);
            double distImaginaryIntersection1 = MetricCollection.euklideanDistanceR2(imaginaryPoint.x,imaginaryPoint.y,intersection1X,intersection1Y);
            double lawOfCosines = Math.toDegrees(Math.acos( (Math.pow(a,2) + Math.pow(a,2) - Math.pow(distImaginaryIntersection1,2)) / (2*a*a) ));
            rotationAngle = lawOfCosines;
        }
        else if( intersection1X >= middleX && intersection1Y <= middleY){
            YPoint imaginaryPoint = this.calculateIntersectionUpSide(middleCenter1Center2,0,a);
            double distImaginaryIntersection1 = MetricCollection.euklideanDistanceR2(imaginaryPoint.x,imaginaryPoint.y,intersection1X,intersection1Y);
            double lawOfCosines = Math.toDegrees(Math.acos( (Math.pow(a,2) + Math.pow(a,2) - Math.pow(distImaginaryIntersection1,2)) / (2*a*a) ));
            rotationAngle = -lawOfCosines;
        }
        else if(  intersection1X <= middleX && intersection1Y >= middleY){
            YPoint imaginaryPoint = this.calculateIntersectionUpSide(middleCenter1Center2,180,a);
            double distImaginaryIntersection1 = MetricCollection.euklideanDistanceR2(imaginaryPoint.x,imaginaryPoint.y,intersection1X,intersection1Y);
            double lawOfCosines = Math.toDegrees(Math.acos( (Math.pow(a,2) + Math.pow(a,2) - Math.pow(distImaginaryIntersection1,2)) / (2*a*a) ));
            rotationAngle = -lawOfCosines;
        }
        else{
            YPoint imaginaryPoint = this.calculateIntersectionUpSide(middleCenter1Center2,0,a);
            double distImaginaryIntersection1 = MetricCollection.euklideanDistanceR2(imaginaryPoint.x,imaginaryPoint.y,intersection1X,intersection1Y);
            double lawOfCosines = Math.toDegrees(Math.acos( (Math.pow(a,2) + Math.pow(a,2) - Math.pow(distImaginaryIntersection1,2)) / (2*a*a) ));
            rotationAngle = lawOfCosines;
        }

        if( center2X <= centerBigger.getX() && center2Y <= centerBigger.getY()){
            return farthestPointOnHalfEllipseToPoint(middleCenter1Center2,rotationAngle,a,b,false,relativePoint);
        }
        else if(center2X >= centerBigger.getX() && center2Y <= centerBigger.getY()){
            return farthestPointOnHalfEllipseToPoint(middleCenter1Center2,rotationAngle,a,b,false,relativePoint);
        }
        else if(center2X <= centerBigger.getX() && center2Y >= centerBigger.getY()){
            return farthestPointOnHalfEllipseToPoint(middleCenter1Center2,rotationAngle,a,b,true,relativePoint);
        }
        else{
            return farthestPointOnHalfEllipseToPoint(middleCenter1Center2,rotationAngle,a,b,true,relativePoint);
        }


    }



    public YPoint calculateClosestPointBiggerCircleCircleIntersection( YPoint centerBigger, double radiusBigger, YPoint center2, double radius2, YPoint relativePoint){
        YPoint[] circleCircleIntersectionPoints = calculateCircleCircleIntersection(centerBigger,radiusBigger,center2,radius2);

        YPoint intersection1 = circleCircleIntersectionPoints[0];
        YPoint intersection2 = circleCircleIntersectionPoints[1];


        double center1X = centerBigger.getX();
        double center1Y = centerBigger.getY();
        double center2X = center2.getX();
        double center2Y = center2.getY();

        double intersection1X = intersection1.getX();
        double intersection1Y = intersection1.getY();
        double intersection2X = intersection2.getX();
        double intersection2Y = intersection2.getY();

        double a = MetricCollection.euklideanDistanceR2(intersection1X,intersection1Y,intersection2X,intersection2Y) * 0.5;

        AffineLine center1Center2 = new AffineLine(centerBigger,center2);

        YPoint[] intersectionCircle1LineCenter1Center2 = calculateIntersectionCircleLine(center1X,center1Y,radiusBigger,center1Center2);


        YPoint firstCycleB;

        YPoint firstIntersection1 = intersectionCircle1LineCenter1Center2[0];
        YPoint secondIntersection1 = intersectionCircle1LineCenter1Center2[1];
        double distanceFirstCycle2 = MetricCollection.euklideanDistanceR2(firstIntersection1.x,firstIntersection1.y,center2X,center2Y);
        double distanceSecondCycle2 = MetricCollection.euklideanDistanceR2(secondIntersection1.x,secondIntersection1.y,center2X,center2Y);

        if( distanceFirstCycle2 < distanceSecondCycle2){
            firstCycleB = firstIntersection1;
        }
        else{
            firstCycleB = secondIntersection1;
        }

        YPoint middleCenter1Center2 = new YPoint( (intersection1X+intersection2X)*0.5, (intersection1Y+intersection2Y)*0.5);

        double distanceBiggerCycleBCenter = MetricCollection.euklideanDistanceR2(middleCenter1Center2.x,middleCenter1Center2.y,firstCycleB.x,firstCycleB.y);


        double b = distanceBiggerCycleBCenter;

        double middleX = middleCenter1Center2.getX();
        double middleY = middleCenter1Center2.getY();

        double rotationAngle = 0;


        if( intersection1X <= middleX && intersection1Y <= middleY ){
            YPoint imaginaryPoint = this.calculateIntersectionUpSide(middleCenter1Center2,180,a);
            double distImaginaryIntersection1 = MetricCollection.euklideanDistanceR2(imaginaryPoint.x,imaginaryPoint.y,intersection1X,intersection1Y);
            double lawOfCosines = Math.toDegrees(Math.acos( (Math.pow(a,2) + Math.pow(a,2) - Math.pow(distImaginaryIntersection1,2)) / (2*a*a) ));
            rotationAngle = lawOfCosines;
        }
        else if( intersection1X >= middleX && intersection1Y <= middleY){
            YPoint imaginaryPoint = this.calculateIntersectionUpSide(middleCenter1Center2,0,a);
            double distImaginaryIntersection1 = MetricCollection.euklideanDistanceR2(imaginaryPoint.x,imaginaryPoint.y,intersection1X,intersection1Y);
            double lawOfCosines = Math.toDegrees(Math.acos( (Math.pow(a,2) + Math.pow(a,2) - Math.pow(distImaginaryIntersection1,2)) / (2*a*a) ));
            rotationAngle = -lawOfCosines;
        }
        else if(  intersection1X <= middleX && intersection1Y >= middleY){
            YPoint imaginaryPoint = this.calculateIntersectionUpSide(middleCenter1Center2,180,a);
            double distImaginaryIntersection1 = MetricCollection.euklideanDistanceR2(imaginaryPoint.x,imaginaryPoint.y,intersection1X,intersection1Y);
            double lawOfCosines = Math.toDegrees(Math.acos( (Math.pow(a,2) + Math.pow(a,2) - Math.pow(distImaginaryIntersection1,2)) / (2*a*a) ));
            rotationAngle = -lawOfCosines;
        }
        else{
            YPoint imaginaryPoint = this.calculateIntersectionUpSide(middleCenter1Center2,0,a);
            double distImaginaryIntersection1 = MetricCollection.euklideanDistanceR2(imaginaryPoint.x,imaginaryPoint.y,intersection1X,intersection1Y);
            double lawOfCosines = Math.toDegrees(Math.acos( (Math.pow(a,2) + Math.pow(a,2) - Math.pow(distImaginaryIntersection1,2)) / (2*a*a) ));
            rotationAngle = lawOfCosines;
        }

        if( center2X <= centerBigger.getX() && center2Y <= centerBigger.getY()){
            return closestPointOnHalfEllipseToPoint(middleCenter1Center2,rotationAngle,a,b,false,relativePoint);
        }
        else if(center2X >= centerBigger.getX() && center2Y <= centerBigger.getY()){
            return closestPointOnHalfEllipseToPoint(middleCenter1Center2,rotationAngle,a,b,false,relativePoint);
        }
        else if(center2X <= centerBigger.getX() && center2Y >= centerBigger.getY()){
            return closestPointOnHalfEllipseToPoint(middleCenter1Center2,rotationAngle,a,b,true,relativePoint);
        }
        else{
            return closestPointOnHalfEllipseToPoint(middleCenter1Center2,rotationAngle,a,b,true,relativePoint);
        }


    }


    public YPoint calculateFarthestPointCircleCircleIntersection( YPoint center1, double radius1, YPoint center2, double radius2, YPoint closestPoint){
        YPoint[] circleCircleIntersectionPoints = calculateCircleCircleIntersection(center1,radius1,center2,radius2);

        YPoint intersection1 = circleCircleIntersectionPoints[0];
        YPoint intersection2 = circleCircleIntersectionPoints[1];


        double center1X = center1.getX();
        double center1Y = center1.getY();
        double center2X = center2.getX();
        double center2Y = center2.getY();

        double intersection1X = intersection1.getX();
        double intersection1Y = intersection1.getY();
        double intersection2X = intersection2.getX();
        double intersection2Y = intersection2.getY();

        double a = MetricCollection.euklideanDistanceR2(intersection1X,intersection1Y,intersection2X,intersection2Y) * 0.5;

        AffineLine center1Center2 = new AffineLine(center1,center2);

        YPoint[] intersectionCircle1LineCenter1Center2 = calculateIntersectionCircleLine(center1X,center1Y,radius1,center1Center2);
        YPoint[] intersectionCircle2LineCenter1Center2 = calculateIntersectionCircleLine(center2X,center2Y,radius2,center1Center2);

        YPoint firstCycleB;
        YPoint secondCycleB;

        YPoint firstIntersection1 = intersectionCircle1LineCenter1Center2[0];
        YPoint secondIntersection1 = intersectionCircle1LineCenter1Center2[1];
        double distanceFirstCycle2 = MetricCollection.euklideanDistanceR2(firstIntersection1.x,firstIntersection1.y,center2X,center2Y);
        double distanceSecondCycle2 = MetricCollection.euklideanDistanceR2(secondIntersection1.x,secondIntersection1.y,center2X,center2Y);

        if( distanceFirstCycle2 < distanceSecondCycle2){
            firstCycleB = firstIntersection1;
        }
        else{
            firstCycleB = secondIntersection1;
        }


        YPoint firstIntersection2 = intersectionCircle2LineCenter1Center2[0];
        YPoint secondIntersection2 = intersectionCircle2LineCenter1Center2[1];
        double distanceFirstCycle1 = MetricCollection.euklideanDistanceR2(firstIntersection2.x,firstIntersection2.y,center1X,center1Y);
        double distanceSecondCycle1 = MetricCollection.euklideanDistanceR2(secondIntersection2.x,secondIntersection2.y,center1X,center1Y);

        if( distanceFirstCycle1 < distanceSecondCycle1){
            secondCycleB = firstIntersection2;
        }
        else{
            secondCycleB = secondIntersection2;
        }


        double b = MetricCollection.euklideanDistanceR2(firstCycleB.x,firstCycleB.y,secondCycleB.x,secondCycleB.y) * 0.5;

        YPoint middleCenter1Center2 = new YPoint( (center1X+center2X)*0.5, (center1Y+center2Y)*0.5);

        double middleX = middleCenter1Center2.getX();
        double middleY = middleCenter1Center2.getY();

        double rotationAngle = 0;


        if( intersection1X <= middleX && intersection1Y <= middleY ){
            YPoint imaginaryPoint = this.calculateIntersectionUpSide(middleCenter1Center2,180,a);
            double distImaginaryIntersection1 = MetricCollection.euklideanDistanceR2(imaginaryPoint.x,imaginaryPoint.y,intersection1X,intersection1Y);
            double lawOfCosines = Math.toDegrees(Math.acos( (Math.pow(a,2) + Math.pow(a,2) - Math.pow(distImaginaryIntersection1,2)) / (2*a*a) ));
            rotationAngle = lawOfCosines;
        }
        else if( intersection1X >= middleX && intersection1Y <= middleY){
            YPoint imaginaryPoint = this.calculateIntersectionUpSide(middleCenter1Center2,0,a);
            double distImaginaryIntersection1 = MetricCollection.euklideanDistanceR2(imaginaryPoint.x,imaginaryPoint.y,intersection1X,intersection1Y);
            double lawOfCosines = Math.toDegrees(Math.acos( (Math.pow(a,2) + Math.pow(a,2) - Math.pow(distImaginaryIntersection1,2)) / (2*a*a) ));
            rotationAngle = -lawOfCosines;
        }
        else if(  intersection1X <= middleX && intersection1Y >= middleY){
            YPoint imaginaryPoint = this.calculateIntersectionUpSide(middleCenter1Center2,180,a);
            double distImaginaryIntersection1 = MetricCollection.euklideanDistanceR2(imaginaryPoint.x,imaginaryPoint.y,intersection1X,intersection1Y);
            double lawOfCosines = Math.toDegrees(Math.acos( (Math.pow(a,2) + Math.pow(a,2) - Math.pow(distImaginaryIntersection1,2)) / (2*a*a) ));
            rotationAngle = -lawOfCosines;
        }
        else{
            YPoint imaginaryPoint = this.calculateIntersectionUpSide(middleCenter1Center2,0,a);
            double distImaginaryIntersection1 = MetricCollection.euklideanDistanceR2(imaginaryPoint.x,imaginaryPoint.y,intersection1X,intersection1Y);
            double lawOfCosines = Math.toDegrees(Math.acos( (Math.pow(a,2) + Math.pow(a,2) - Math.pow(distImaginaryIntersection1,2)) / (2*a*a) ));
            rotationAngle = lawOfCosines;
        }

        return  farthestPointOnEllipseToPoint(middleCenter1Center2,rotationAngle,a,b,closestPoint);
    }

    public YPoint calculateClosestPointCircleCircleIntersectionWithoutRel( YPoint center1, double radius1, YPoint center2, double radius2, YPoint closestPoint){
        YPoint[] circleCircleIntersectionPoints = calculateCircleCircleIntersection(center1,radius1,center2,radius2);

        YPoint intersection1 = circleCircleIntersectionPoints[0];
        YPoint intersection2 = circleCircleIntersectionPoints[1];


        double center1X = center1.getX();
        double center1Y = center1.getY();
        double center2X = center2.getX();
        double center2Y = center2.getY();

        double intersection1X = intersection1.getX();
        double intersection1Y = intersection1.getY();
        double intersection2X = intersection2.getX();
        double intersection2Y = intersection2.getY();

        double a = MetricCollection.euklideanDistanceR2(intersection1X,intersection1Y,intersection2X,intersection2Y) * 0.5;

        AffineLine center1Center2 = new AffineLine(center1,center2);

        YPoint[] intersectionCircle1LineCenter1Center2 = calculateIntersectionCircleLine(center1X,center1Y,radius1,center1Center2);
        YPoint[] intersectionCircle2LineCenter1Center2 = calculateIntersectionCircleLine(center2X,center2Y,radius2,center1Center2);

        YPoint firstCycleB;
        YPoint secondCycleB;

        YPoint firstIntersection1 = intersectionCircle1LineCenter1Center2[0];
        YPoint secondIntersection1 = intersectionCircle1LineCenter1Center2[1];
        double distanceFirstCycle2 = MetricCollection.euklideanDistanceR2(firstIntersection1.x,firstIntersection1.y,center2X,center2Y);
        double distanceSecondCycle2 = MetricCollection.euklideanDistanceR2(secondIntersection1.x,secondIntersection1.y,center2X,center2Y);

        if( distanceFirstCycle2 < distanceSecondCycle2){
            firstCycleB = firstIntersection1;
        }
        else{
            firstCycleB = secondIntersection1;
        }


        YPoint firstIntersection2 = intersectionCircle2LineCenter1Center2[0];
        YPoint secondIntersection2 = intersectionCircle2LineCenter1Center2[1];
        double distanceFirstCycle1 = MetricCollection.euklideanDistanceR2(firstIntersection2.x,firstIntersection2.y,center1X,center1Y);
        double distanceSecondCycle1 = MetricCollection.euklideanDistanceR2(secondIntersection2.x,secondIntersection2.y,center1X,center1Y);

        if( distanceFirstCycle1 < distanceSecondCycle1){
            secondCycleB = firstIntersection2;
        }
        else{
            secondCycleB = secondIntersection2;
        }


        double b = MetricCollection.euklideanDistanceR2(firstCycleB.x,firstCycleB.y,secondCycleB.x,secondCycleB.y) * 0.5;

        YPoint middleCenter1Center2 = new YPoint( (center1X+center2X)*0.5, (center1Y+center2Y)*0.5);

        double middleX = middleCenter1Center2.getX();
        double middleY = middleCenter1Center2.getY();

        double rotationAngle = 0;


        if( intersection1X <= middleX && intersection1Y <= middleY ){
            YPoint imaginaryPoint = this.calculateIntersectionUpSide(middleCenter1Center2,180,a);
            double distImaginaryIntersection1 = MetricCollection.euklideanDistanceR2(imaginaryPoint.x,imaginaryPoint.y,intersection1X,intersection1Y);
            double lawOfCosines = Math.toDegrees(Math.acos( (Math.pow(a,2) + Math.pow(a,2) - Math.pow(distImaginaryIntersection1,2)) / (2*a*a) ));
            rotationAngle = lawOfCosines;
        }
        else if( intersection1X >= middleX && intersection1Y <= middleY){
            YPoint imaginaryPoint = this.calculateIntersectionUpSide(middleCenter1Center2,0,a);
            double distImaginaryIntersection1 = MetricCollection.euklideanDistanceR2(imaginaryPoint.x,imaginaryPoint.y,intersection1X,intersection1Y);
            double lawOfCosines = Math.toDegrees(Math.acos( (Math.pow(a,2) + Math.pow(a,2) - Math.pow(distImaginaryIntersection1,2)) / (2*a*a) ));
            rotationAngle = -lawOfCosines;
        }
        else if(  intersection1X <= middleX && intersection1Y >= middleY){
            YPoint imaginaryPoint = this.calculateIntersectionUpSide(middleCenter1Center2,180,a);
            double distImaginaryIntersection1 = MetricCollection.euklideanDistanceR2(imaginaryPoint.x,imaginaryPoint.y,intersection1X,intersection1Y);
            double lawOfCosines = Math.toDegrees(Math.acos( (Math.pow(a,2) + Math.pow(a,2) - Math.pow(distImaginaryIntersection1,2)) / (2*a*a) ));
            rotationAngle = -lawOfCosines;
        }
        else{
            YPoint imaginaryPoint = this.calculateIntersectionUpSide(middleCenter1Center2,0,a);
            double distImaginaryIntersection1 = MetricCollection.euklideanDistanceR2(imaginaryPoint.x,imaginaryPoint.y,intersection1X,intersection1Y);
            double lawOfCosines = Math.toDegrees(Math.acos( (Math.pow(a,2) + Math.pow(a,2) - Math.pow(distImaginaryIntersection1,2)) / (2*a*a) ));
            rotationAngle = lawOfCosines;
        }

        return  closestPointOnEllipseToPointWithoutRel(middleCenter1Center2,rotationAngle,a,b,closestPoint);

    }


    public YPoint calculateClosestPointCircleCircleIntersection( YPoint center1, double radius1, YPoint center2, double radius2, YPoint closestPoint, YPoint relativePoint, double lengthLongEdge, double lengthShortEdge ){
        YPoint[] circleCircleIntersectionPoints = calculateCircleCircleIntersection(center1,radius1,center2,radius2);

        YPoint intersection1 = circleCircleIntersectionPoints[0];
        YPoint intersection2 = circleCircleIntersectionPoints[1];


        double center1X = center1.getX();
        double center1Y = center1.getY();
        double center2X = center2.getX();
        double center2Y = center2.getY();

        double intersection1X = intersection1.getX();
        double intersection1Y = intersection1.getY();
        double intersection2X = intersection2.getX();
        double intersection2Y = intersection2.getY();

        double a = MetricCollection.euklideanDistanceR2(intersection1X,intersection1Y,intersection2X,intersection2Y) * 0.5;

        AffineLine center1Center2 = new AffineLine(center1,center2);

        YPoint[] intersectionCircle1LineCenter1Center2 = calculateIntersectionCircleLine(center1X,center1Y,radius1,center1Center2);
        YPoint[] intersectionCircle2LineCenter1Center2 = calculateIntersectionCircleLine(center2X,center2Y,radius2,center1Center2);

        YPoint firstCycleB;
        YPoint secondCycleB;

        YPoint firstIntersection1 = intersectionCircle1LineCenter1Center2[0];
        YPoint secondIntersection1 = intersectionCircle1LineCenter1Center2[1];
        double distanceFirstCycle2 = MetricCollection.euklideanDistanceR2(firstIntersection1.x,firstIntersection1.y,center2X,center2Y);
        double distanceSecondCycle2 = MetricCollection.euklideanDistanceR2(secondIntersection1.x,secondIntersection1.y,center2X,center2Y);

        if( distanceFirstCycle2 < distanceSecondCycle2){
            firstCycleB = firstIntersection1;
        }
        else{
            firstCycleB = secondIntersection1;
        }


        YPoint firstIntersection2 = intersectionCircle2LineCenter1Center2[0];
        YPoint secondIntersection2 = intersectionCircle2LineCenter1Center2[1];
        double distanceFirstCycle1 = MetricCollection.euklideanDistanceR2(firstIntersection2.x,firstIntersection2.y,center1X,center1Y);
        double distanceSecondCycle1 = MetricCollection.euklideanDistanceR2(secondIntersection2.x,secondIntersection2.y,center1X,center1Y);

        if( distanceFirstCycle1 < distanceSecondCycle1){
            secondCycleB = firstIntersection2;
        }
        else{
            secondCycleB = secondIntersection2;
        }


        double b = MetricCollection.euklideanDistanceR2(firstCycleB.x,firstCycleB.y,secondCycleB.x,secondCycleB.y) * 0.5;

        YPoint middleCenter1Center2 = new YPoint( (center1X+center2X)*0.5, (center1Y+center2Y)*0.5);

        double middleX = middleCenter1Center2.getX();
        double middleY = middleCenter1Center2.getY();

        double rotationAngle = 0;


        if( intersection1X <= middleX && intersection1Y <= middleY ){
            YPoint imaginaryPoint = this.calculateIntersectionUpSide(middleCenter1Center2,180,a);
            double distImaginaryIntersection1 = MetricCollection.euklideanDistanceR2(imaginaryPoint.x,imaginaryPoint.y,intersection1X,intersection1Y);
            double lawOfCosines = Math.toDegrees(Math.acos( (Math.pow(a,2) + Math.pow(a,2) - Math.pow(distImaginaryIntersection1,2)) / (2*a*a) ));
            rotationAngle = lawOfCosines;
        }
        else if( intersection1X >= middleX && intersection1Y <= middleY){
            YPoint imaginaryPoint = this.calculateIntersectionUpSide(middleCenter1Center2,0,a);
            double distImaginaryIntersection1 = MetricCollection.euklideanDistanceR2(imaginaryPoint.x,imaginaryPoint.y,intersection1X,intersection1Y);
            double lawOfCosines = Math.toDegrees(Math.acos( (Math.pow(a,2) + Math.pow(a,2) - Math.pow(distImaginaryIntersection1,2)) / (2*a*a) ));
            rotationAngle = -lawOfCosines;
        }
        else if(  intersection1X <= middleX && intersection1Y >= middleY){
            YPoint imaginaryPoint = this.calculateIntersectionUpSide(middleCenter1Center2,180,a);
            double distImaginaryIntersection1 = MetricCollection.euklideanDistanceR2(imaginaryPoint.x,imaginaryPoint.y,intersection1X,intersection1Y);
            double lawOfCosines = Math.toDegrees(Math.acos( (Math.pow(a,2) + Math.pow(a,2) - Math.pow(distImaginaryIntersection1,2)) / (2*a*a) ));
            rotationAngle = -lawOfCosines;
        }
        else{
            YPoint imaginaryPoint = this.calculateIntersectionUpSide(middleCenter1Center2,0,a);
            double distImaginaryIntersection1 = MetricCollection.euklideanDistanceR2(imaginaryPoint.x,imaginaryPoint.y,intersection1X,intersection1Y);
            double lawOfCosines = Math.toDegrees(Math.acos( (Math.pow(a,2) + Math.pow(a,2) - Math.pow(distImaginaryIntersection1,2)) / (2*a*a) ));
            rotationAngle = lawOfCosines;
        }

        return  closestPointOnEllipseToPoint(middleCenter1Center2,rotationAngle,a,b,closestPoint,relativePoint,lengthLongEdge,lengthShortEdge);

    }

    public YPoint[] calculateIntersectionCircleLine( YPoint linePoint1, YPoint linePoint2, YPoint circleCenter, double radius){

        YPoint[] result = new YPoint[2];


        double xm = circleCenter.getX();
        double ym = circleCenter.getY();

        double x1 = linePoint1.getX() - xm;
        double y1 = linePoint1.getY() - ym;

        double x2 = linePoint2.getX() -xm;
        double y2 = linePoint2.getY() -ym;

        double A = y2-y1;
        double B = x1-x2;
        double C = (x2*y1)-(x1*y2);


        double d0= Math.abs(C) / Math.sqrt( (A*A)+(B*B) );



        if( d0 > radius) {
            return null;
        }
        double x0 = (-A*C) / ((A*A)+(B*B));
        double y0 = (-B*C) / ( (A*A)+(B*B));

        System.out.println(x0+" "+y0);

        if( d0 == radius){
            double resultX = x0 + xm;
            double resultY = y0 + ym;
            result[0] = new YPoint(resultX,resultY);
            result[1] = null;
            return result;
        }
        else{
            double d2 = Math.sqrt( (radius*radius)- Math.pow((Math.abs(C)/( Math.sqrt((A*A)+ (B*B))) ),2) );
            System.out.println(d2);
            double res1X = x0+xm+ ((d2 / Math.sqrt((A*A)+(B*B)))* -B);
            double res1Y = y0+ym+ ((d2 / Math.sqrt((A*A)+(B*B)))* A);

            double res2X = x0+ym -((d2 / Math.sqrt((A*A)+(B*B)))* -B);
            double res2Y = y0+ym- ((d2 / Math.sqrt((A*A)+(B*B)))* A);

            result[0] = new YPoint(res1X,res1Y);
            result[1] = new YPoint(res2X,res2Y);

            System.out.println(result[0]);
            System.out.println(result[1]);
            return result;

        }

    }



    public YPoint[] calculateIntersectionCircleLine( double centerX, double centerY, double radius, AffineLine line){
        YPoint centerPoint = new YPoint(centerX,centerY);
        YCircle distanceCircle = new YCircle(centerPoint,radius);
        return  distanceCircle.getCut(line);
    }


    /*
    Implementation from Stack Overflow, all credits belong to the respective creator
    https://stackoverflow.com/questions/3349125/circle-circle-intersection-points
     */

    public YPoint[] calculateCircleCircleIntersection(YPoint center1,double radius1,YPoint center2,double radius2){
        double point1X = center1.getX();
        double point1Y = center1.getY();
        double point2X = center2.getX();
        double point2Y = center2.getY();

        double distance = MetricCollection.euklideanDistanceR2(point1X,point1Y,point2X,point2Y);
        double a = (Math.pow(radius1,2) - Math.pow(radius2,2) + Math.pow(distance,2)) / (2*distance);

        Point2D pointOnLineWithDistance = MetricCollection.pointOnLineWithDistance(point1X,point1Y,point2X,point2Y,a);

        double height = Math.sqrt(Math.pow(radius1,2)-Math.pow(a,2));

        double firstIntersectionX = pointOnLineWithDistance.getX() + height * (point2Y-point1Y) / distance;
        double firstIntersectionY = pointOnLineWithDistance.getY() - height * (point2X-point1X) / distance;

        double secondIntersectionX = pointOnLineWithDistance.getX() - height * (point2Y-point1Y) / distance;
        double secondIntersectionY = pointOnLineWithDistance.getY() + height * (point2X-point1X) / distance;

        return new YPoint[]{new YPoint(firstIntersectionX,firstIntersectionY),new YPoint(secondIntersectionX,secondIntersectionY)};
    }

    public void moveNode(Node vertex, YPoint newLocation){
        this.graph2D.setCenter(vertex,newLocation);
    }

    public ArrayList<Edge> getEdgesFromPartition( int partition){
        return this.graphView.getRelationships().getEdgesFromPartition(partition);
    }

    public Edge getEdgeFromVertex( Node source, Node target){
        if( source.getEdgeTo(target) != null){
            return source.getEdgeTo(target);
        }
        else if(source.getEdgeFrom(target) !=null){
            return source.getEdgeFrom(target);
        }
        else{
            return null;
        }
    }

    public NodeList intersection( NodeList nodeList1, List nodeList2){
        NodeList result = new NodeList();
        for( NodeCursor v= nodeList1.nodes(); v.ok() ; v.next()){
            Node currentNode = v.node();
            if( nodeList2.contains(currentNode)){
                result.add(currentNode);
            }
        }
        return  result;
    }

    public void genericSmallNeighbourhoodPlacement(NodeList nodes, YPoint origin){


        double alpha = 360 / (double) (nodes.size());
        double currentAngle = 0;
        double currentAngleClockwise = 180-alpha;
        double finalAngleOffset = 0;


        for( int i= 0 ; i < nodes.size() ; i++){

            Node currentNode = (Node) nodes.get(i);

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

    public void genericSmallNeighbourhoodPlacement(NodeList nodes, YPoint origin, double distance){


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

    public boolean isDichotomos(){
        ArrayList<Edge> shortEdges = this.graphView.getRelationships().getEdgesFromPartition(1);
        ArrayList<Edge> longEdges = this.graphView.getRelationships().getEdgesFromPartition(2);
        for( Edge edge : edges(graph2D)){
            if( !shortEdges.contains(edge) && !longEdges.contains(edge)){
                return false;
            }
        }
        return true;
    }


    public boolean isInKPartitions(){
        Relationships relationships = graphView.getRelationships();
        int k = relationships.getK();

        for(  Edge edge : edges(graph2D)){
            int partitionIndex = relationships.getPartitionIndexEdge(edge);
            if( partitionIndex > k || partitionIndex == -1 ) {
                return false;
            }

        }
        return true;
    }

    public boolean pointsEqual( YPoint point1, YPoint point2){

        if( point1.distanceTo(point2) < 0.001){
            return true;
        }
        else{
            return false;
        }

    }


}
