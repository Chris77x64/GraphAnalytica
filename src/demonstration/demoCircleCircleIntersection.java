package demonstration;

import model.BasicGraph;
import model.MetricCollection;
import view.GraphView;
import y.geom.AffineLine;
import y.geom.YCircle;
import y.geom.YPoint;

import java.awt.geom.Point2D;

/**
 * Created by chris on 02.03.2018.
 */
public class demoCircleCircleIntersection {

    private final YPoint START_POINT = new YPoint(0,0);

    private BasicGraph basicGraph;
    private GraphView graphView;

    private double angle;
    private double dist;

    private double radius;

    private YPoint secondPoint;

    public demoCircleCircleIntersection( GraphView graphView, double angle,double dist,double radius ){

        this.graphView = graphView;
        this.basicGraph = graphView.getGraph();

        this.basicGraph.resetGraph();

        this.angle = angle;
        this.dist = dist;
        this.radius = radius;

        this.initializeAssets();
        this.initializeDemo();

        this.graphView.fitContent();
    }

    private void initializeDemo(){
        this.calculateClosestPointCircleCircleIntersectionWithoutRel(START_POINT,radius,secondPoint,radius);
    }

    private void initializeAssets(){
        this.secondPoint = calculateIntersectionUpSide(START_POINT,angle,dist);
        this.basicGraph.createNode(START_POINT);
        this.basicGraph.createNode(secondPoint);

        this.graphView.setAddVertexDistanceCircle( START_POINT,radius);
        this.graphView.setAddVertexDistanceCircle2( secondPoint,radius);
    }


    public void closestPointOnEllipseToPointWithoutRel(YPoint center, double rotationAngle, double a, double b){

        double angle = 0;
        double upperBound = 360;

        while( angle <= upperBound){
            angle += 0.5;

            double currentX = ((a * Math.cos(Math.toRadians(rotationAngle))) * Math.cos(Math.toRadians(angle))) + (-b * Math.sin(Math.toRadians(rotationAngle))*Math.sin(Math.toRadians(angle))) + center.getX();
            double currentY = ((a * Math.sin(Math.toRadians(rotationAngle))) * Math.cos(Math.toRadians(angle))) + (b * Math.cos(Math.toRadians(rotationAngle))*Math.sin(Math.toRadians(angle))) + center.getY();

            YPoint currentPosition = new YPoint(currentX,currentY);
            basicGraph.createNode(currentPosition);

        }
    }

    public void calculateClosestPointCircleCircleIntersectionWithoutRel( YPoint center1, double radius1, YPoint center2, double radius2){
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

        closestPointOnEllipseToPointWithoutRel(middleCenter1Center2,rotationAngle,a,b);

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
