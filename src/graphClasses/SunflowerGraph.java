package graphClasses;

import model.BasicGraph;
import model.MetricCollection;
import view.GraphView;
import y.base.Node;
import y.base.NodeList;
import y.geom.YPoint;
import y.view.Graph2D;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * Created by chris on 24.06.2018.
 */
public class SunflowerGraph {

    private final double DISTANCE = 300;
    private int size;


    private GraphView graphView;
    private Graph2D graph2D;
    private BasicGraph basicGraph;

    private final YPoint centerPoint = new YPoint(0,0);

    public SunflowerGraph(int size , GraphView graphView){
        this.size = size;
        this.graphView = graphView;
        this.graph2D = graphView.getGraph2D();
        this.basicGraph = graphView.getGraph();
        this.initializeWheelGraph();

        this.basicGraph.updateViews();
        this.graphView.fitContent();
    }

    public SunflowerGraph(int size , YPoint centerPoint, GraphView graphView){
        this.size = size;
        this.graphView = graphView;
        this.graph2D = graphView.getGraph2D();
        this.basicGraph = graphView.getGraph();
        this.initializeWheelGraph();

        this.basicGraph.updateViews();
        this.graphView.fitContent();

    }


    private void initializeWheelGraph(){
        this.initializeConstructionLines();
        this.update();
    }


    private void update(){
        this.graphView.updateView();
    }

    private void initializeConstructionLines(){

        this.basicGraph.resetGraph();
        Node centerNode = this.basicGraph.createNode(centerPoint);

        ArrayList<Node> circleNodes = new ArrayList<>();

        double alpha = 360 / (double) (size-1);
        double currentAngle = 0;
        double currentAngleClockwise = 180-alpha;
        double finalAngleOffset = 0;

        double circleX = this.centerPoint.getX();
        double circleY = this.centerPoint.getY();

        for( int i= 1 ; i < size ; i++){

            if( currentAngle > 180) {

                //System.out.println("AKTUELLER WINKEL" +currentAngleClockwise);

                double tempAngle = Math.toRadians(currentAngleClockwise);
                double tempX =  ( DISTANCE * Math.cos(tempAngle) ) + circleX ;
                double tempY =  circleY - ( DISTANCE * Math.sin(tempAngle) )  ;
                YPoint tempPoint = new YPoint(tempX,tempY);

                Node circleNode = this.basicGraph.createNode(tempPoint);
                this.basicGraph.createEdge(centerNode,circleNode);
                circleNodes.add(circleNode);
                currentAngleClockwise = currentAngleClockwise-alpha;


            }
            else{
                double tempAngle = Math.toRadians(currentAngle);
                double tempX = circleX + ( DISTANCE * Math.cos(tempAngle) );
                double tempY = circleY + ( DISTANCE * Math.sin(tempAngle) );
                YPoint tempPoint = new YPoint(tempX,tempY);

                Node circleNode = this.basicGraph.createNode(tempPoint);
                this.basicGraph.createEdge(centerNode,circleNode);
                circleNodes.add(circleNode);
                currentAngle = currentAngle + alpha;

                //System.out.println("LOWER ANGLE:" +currentAngle);

                if( currentAngle > 180 ) {


                    finalAngleOffset = 180 - (currentAngle - alpha);
                    //                      System.out.println("DIFFERENZ: "+finalAngleOffset);
                    currentAngleClockwise -= (alpha-finalAngleOffset);
                    currentAngleClockwise +=alpha;
//                        System.out.println("Angle: "+currentAngleClockwise);

                    // if (size % 2 == 1) {
                    //finalAngleOffset = 180 - (currentAngle - alpha);
                    //System.out.println("DIFFERENZ: "+finalAngleOffset);
                    // currentAngleClockwise += finalAngleOffset;
                    // System.out.println("Angle: "+currentAngleClockwise);
                    // }
                    // else{
                    //   finalAngleOffset = 180 - (currentAngle - alpha);
                    //  System.out.println("DIFFERENZ: "+finalAngleOffset);
                    //  currentAngleClockwise += finalAngleOffset;
                    //  System.out.println("Angle: "+currentAngleClockwise);
                    // }
                }


            }


        }


        int size = circleNodes.size();
        for(int i=0; i < size ; i++){
            if( i <  ( size -1) ){
                Node source = circleNodes.get(i);
                Node target = circleNodes.get(i+1);
                basicGraph.createEdge(source,target);

            }
            else{
                Node source = circleNodes.get(i);
                Node target = circleNodes.get(0);
                basicGraph.createEdge(source,target);
            }
        }


        for( int i=0; i < circleNodes.size(); i++){

            Node currentNode = circleNodes.get(i);
            Node nextNode;

            if( i+1 == circleNodes.size()){
                nextNode = circleNodes.get(0);
            }
            else{
                nextNode = circleNodes.get(i+1);
            }


            YPoint positionCurrent = basicGraph.getCenter(currentNode);
            YPoint positionNext = basicGraph.getCenter(nextNode);


            double length = Math.max(0.7*DISTANCE,0.7*MetricCollection.euklideanDistanceR2(positionCurrent,positionNext));
            //double length = 0.7*MetricCollection.euklideanDistanceR2(positionCurrent,positionNext);

            YPoint destination = farthestCircleCircleIntersectionPointToRelativePoint(positionCurrent,length,positionNext,length,centerPoint);

            Node outerNode = basicGraph.createNode(destination);

            basicGraph.createEdge(currentNode,outerNode);
            basicGraph.createEdge(nextNode,outerNode);

        }

    }


    private YPoint calculateCenterGraphView(){
        double worldHeight = this.graphView.getWorldRect2D().getHeight();
        double worldWidth = this.graphView.getWorldRect2D().getWidth();
        return new YPoint(worldWidth/2,worldHeight/2);
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
}
