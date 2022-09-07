package model;

import y.base.Edge;
import y.geom.YPoint;

import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * Created by chris on 06.12.2017.
 */
public class MetricCollection {

    public static double euklideanDistanceR2(YPoint p1, YPoint p2){
        double point1X = p1.getX();
        double point1Y = p1.getY();
        double point2X = p2.getX();
        double point2Y = p2.getY();
        return  Math.sqrt(Math.pow(point1X-point2X,2) + Math.pow(point1Y-point2Y,2));
    }

    public static double euklideanDistanceR2( double point1X , double point1Y, double point2X , double point2Y){
        return  Math.sqrt(Math.pow(point1X-point2X,2) + Math.pow(point1Y-point2Y,2));
    }


    public static double angleBetween2Points( double point1X, double point1Y , double point2X, double point2Y){

        if(  point1X < point2X && point1Y > point2Y){
            return MetricCollection.angelSideSideSide(point1X,point2Y,point2X,point2Y,point1X,point1Y);
        }

        return 0;
    }

    /**
     * Returns the square of the distance from a point to a line segment.
     * The distance measured is the distance between the specified
     * point and the closest point between the specified end points.
     * If the specified point intersects the line segment in between the
     * end points, this method returns 0.0
     * @param pointX
     * @param pointY
     * @param lineX1
     * @param lineY1
     * @param lineX2
     * @param lineY2
     * @return
     */
    public static double distanceLinePoint( double pointX,double pointY, double lineX1, double lineY1, double lineX2, double lineY2){
        return Line2D.Double.ptSegDist(lineX1, lineY1, lineX2, lineY2,pointX, pointY);
    }

    /**
     * Line2D.Double.relativeCCW description:
     *
     * Returns an indicator of where the specified point
     * {@code (px,py)} lies with respect to the line segment from
     * {@code (x1,y1)} to {@code (x2,y2)}.
     * The return value can be either 1, -1, or 0 and indicates
     * in which direction the specified line must pivot around its
     * first end point, {@code (x1,y1)}, in order to point at the
     * specified point {@code (px,py)}.
     * <p>A return value of 1 indicates that the line segment must
     * turn in the direction that takes the positive X axis towards
     * the negative Y axis.  In the default coordinate system used by
     * Java 2D, this direction is counterclockwise.
     * <p>A return value of -1 indicates that the line segment must
     * turn in the direction that takes the positive X axis towards
     * the positive Y axis.  In the default coordinate system, this
     * direction is clockwise.
     * <p>A return value of 0 indicates that the point lies
     * exactly on the line segment.  Note that an indicator value
     * of 0 is rare and not useful for determining collinearity
     * because of floating point rounding issues.
     *
     * @param pointX
     * @param pointY
     * @param lineX1
     * @param lineY1
     * @param lineX2
     * @param lineY2
     * @return
     */
    public static String directionPointLine(double pointX,double pointY, double lineX1, double lineY1, double lineX2, double lineY2){
        int direction = Line2D.Double.relativeCCW( pointX,pointY,lineX1,lineY1,lineX2,lineY2);
        switch (direction){
            case 1: {
                return "POINT_COUNTERCLOCKWISE";
            }
            case -1: {
                return "POINT_CLOCKWISE";
            }
            case 0: {
                return "POINT_ON_LINE";
            }
            default: {
                return "ERROR";
            }
        }
    }


    /*
        Returns the angle between 2 adjacent arcs in a graph using law of consines
     * the length of those 2 arcs (Node1,Node2),(Node1,Node3) is denoted as  distNode1Node2,distNode1Node3
     * where as distNode2Node3 is the length of the arc connectiong (Node2,Node3)
     */
    public static double angelSideSideSide(  double node1X, double node1Y, double node2X, double node2Y, double node3X, double node3Y){

        double distNode1Node2 = MetricCollection.euklideanDistanceR2(node1X,node1Y,node2X,node2Y);
        double distNode1Node3 = MetricCollection.euklideanDistanceR2(node1X,node1Y,node3X,node3Y);
        double distNode2Node3 = MetricCollection.euklideanDistanceR2(node2X,node2Y,node3X,node3Y);

        double distNode1Node2Sq = Math.pow(distNode1Node2,2);
        double distNode1Node3Sq = Math.pow(distNode1Node3,2);
        double distNode2Node3Sq = Math.pow(distNode2Node3,2);

        double cosAngle = (distNode1Node2Sq+distNode1Node3Sq-distNode2Node3Sq) / ( 2* distNode1Node2* distNode1Node3);

        double angleRadians = Math.acos(cosAngle);
        double angleDegrees = Math.toDegrees(angleRadians);

        String locationNode3toNode1Node2 = directionPointLine(node3X,node3Y,node1X,node1Y,node2X,node2Y);

        switch (locationNode3toNode1Node2){
            case "POINT_COUNTERCLOCKWISE":{
                return angleDegrees;
            }
            case "POINT_CLOCKWISE":{
                return  360-angleDegrees;
            }
            case "POINT_ON_LINE":{
                return 180;
            }
            default: {
                return 0;
            }
        }

    }

    public static Point2D.Double pointOnLineWithDistance( double node1X, double node1Y, double node2X, double node2Y, double distance){

        double distanceNode1Node2 = euklideanDistanceR2(node1X,node1Y,node2X,node2Y);

        double resultX = node1X + ( ( (node2X-node1X) / distanceNode1Node2) * distance);
        double resultY = node1Y + ( ( (node2Y-node1Y) / distanceNode1Node2) * distance);

        return new Point2D.Double(resultX,resultY);
    }






}
