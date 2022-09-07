package demonstration;

import model.BasicGraph;
import model.MetricCollection;
import view.GraphView;
import y.base.Graph;
import y.geom.AffineLine;
import y.geom.YCircle;
import y.geom.YPoint;

import java.awt.geom.Point2D;

/**
 * Created by chris on 02.03.2018.
 */
public class demoBiggerCircleCircleIntersection {

    private final YPoint START_POINT = new YPoint(0,0);

    private BasicGraph basicGraph;
    private GraphView graphView;

    private double angle;
    private double dist;

    private double radiusSmall;
    private double radiusBig;

    private YPoint secondPoint;

    public demoBiggerCircleCircleIntersection(GraphView graphView, double angle,double dist,double radiusSmall, double radiusBig  ){

        this.graphView = graphView;
        this.basicGraph = graphView.getGraph();

        this.basicGraph.resetGraph();

        this.angle = angle;
        this.dist = dist;
        this.radiusSmall = radiusSmall;
        this.radiusBig = radiusBig;

        this.initializeAssets();
        this.initializeDemo();


        this.graphView.fitContent();


    }

    private void initializeDemo(){
        this.calculateClosestPointBiggerCircleCircleIntersection(START_POINT,radiusBig,secondPoint,radiusSmall);
    }

    private void initializeAssets(){
        this.secondPoint = calculateIntersectionUpSide(START_POINT,angle,dist);
        this.basicGraph.createNode(START_POINT);
        this.basicGraph.createNode(secondPoint);

        this.graphView.setAddVertexDistanceCircle( START_POINT,radiusBig);
        this.graphView.setAddVertexDistanceCircle2( secondPoint,radiusSmall);
    }


    public void closestPointOnHalfEllipseToPoint(YPoint center, double rotationAngle, double a, double b, boolean intervalUpper){

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

            double currentX = ( (a * Math.cos(Math.toRadians(rotationAngle))) * Math.cos(Math.toRadians(angle))) + (-b * Math.sin(Math.toRadians(rotationAngle))*Math.sin(Math.toRadians(angle))) + center.getX();
            double currentY = ((a * Math.sin(Math.toRadians(rotationAngle))) * Math.cos(Math.toRadians(angle))) + (b * Math.cos(Math.toRadians(rotationAngle))*Math.sin(Math.toRadians(angle))) + center.getY();

            YPoint currentPosition = new YPoint(currentX,currentY);
            basicGraph.createNode(currentPosition);
        }

    }


    public void calculateClosestPointBiggerCircleCircleIntersection( YPoint centerBigger, double radiusBigger, YPoint center2, double radius2){
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
            closestPointOnHalfEllipseToPoint(middleCenter1Center2,rotationAngle,a,b,false);
        }
        else if(center2X >= centerBigger.getX() && center2Y <= centerBigger.getY()){
            closestPointOnHalfEllipseToPoint(middleCenter1Center2,rotationAngle,a,b,false);
        }
        else if(center2X <= centerBigger.getX() && center2Y >= centerBigger.getY()){
           closestPointOnHalfEllipseToPoint(middleCenter1Center2,rotationAngle,a,b,true);
        }
        else{
            closestPointOnHalfEllipseToPoint(middleCenter1Center2,rotationAngle,a,b,true);
        }


    }


    private YPoint[] calculateIntersectionCircleLine( double centerX, double centerY, double radius, AffineLine line){
        YPoint centerPoint = new YPoint(centerX,centerY);
        YCircle distanceCircle = new YCircle(centerPoint,radius);
        return  distanceCircle.getCut(line);
    }

    private YPoint[] calculateCircleCircleIntersection(YPoint center1,double radius1,YPoint center2,double radius2){
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

    private YPoint calculateIntersectionUpSide( YPoint center, double angle , double distance){
        double tempAngle = Math.toRadians(angle);

        double tempX =  center.x + ( distance * Math.cos(tempAngle) ) ;
        double tempY =  center.y - ( distance* Math.sin(tempAngle) )  ;
        return new YPoint(tempX,tempY);
    }


}
