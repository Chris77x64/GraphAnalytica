package realizations;

import graphClasses.HanoiGraph;
import javafx.scene.transform.Affine;
import model.MetricCollection;
import view.GraphView;
import y.base.Edge;
import y.base.Node;
import y.base.NodeList;
import y.geom.AffineLine;
import y.geom.YPoint;
import y.util.YRandom;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by chris on 06.03.2018.
 */
public class RealizationHanoiGraph extends Realization{

    private final double LENGTH_SHORT_EDGES = 300;
    private final double LENGTH_LONG_EDGES = 400;

    private YPoint START_POSITION = new YPoint(0,0);
    private final double initialAngle = 75;

    private ArrayList<ArrayList<NodeList>> nodes;

    private ArrayList<Edge> shortEdges;
    private ArrayList<Edge> longEdges;

    public RealizationHanoiGraph(GraphView graphView) {
        super(graphView);

        double dist = Double.NEGATIVE_INFINITY;
        double minDist = Double.POSITIVE_INFINITY;

        basicGraph.resetGraph();

        /*
        for( int i=0; i < 10000; i++ ) {
            this.generateGraph();

            this.rightRealization();

            Node top3 = (Node) topK3.get(2);
            Node right3 = (Node) rightK3.get(2);
            YPoint posTop3 = graph2D.getCenter(top3);
            YPoint posRight3 = graph2D.getCenter(right3);
            double currDist = MetricCollection.euklideanDistanceR2(posTop3.x, posTop3.y, posRight3.x, posRight3.y);

            if (currDist > dist) {
                dist = currDist;
            }
            if( currDist < minDist){
                minDist = currDist;
            }
        }

        System.out.println(dist);
        System.out.println(minDist);


        graphView.fitContent();
        */


        /*
        for( int i=0; i < 15000; i++ ) {
            generateH3();
            this.graphView.getRelationships().generateRandomPartitions();
            this.initializeAssets();
            realizeH3();

            Node topTop3 = (Node) nodes.get(1).get(0).get(2);
            Node rightRight3 = (Node) nodes.get(2).get(2).get(2);

            YPoint posTop3 = graph2D.getCenter(topTop3);
            YPoint posRight3 = graph2D.getCenter(rightRight3);

            double currDist = MetricCollection.euklideanDistanceR2(posTop3.x, posTop3.y, posRight3.x, posRight3.y);

            if (currDist > dist) {
                dist = currDist;
            }
            if( currDist < minDist){
                minDist = currDist;
            }
            if( currDist < 1572){
                break;
            }
            basicGraph.resetGraph();
        }

        System.out.println(dist);
        System.out.println(minDist);
        */


        /*
        YRandom random = new YRandom();
        double firstRandom = 45;//random.nextInt(-180,181);
        double secondRandom = -135;//random.nextInt(-180,181);

        YPoint NEWPOSL = new YPoint(2.239214705425216E7,-1106.0042074208682);
        YPoint NEWPOSR = calculateIntersectionUpSide(NEWPOSL,firstRandom,863.4330385765176);
        //YPoint NEWPOSR = new YPoint(2.239296778001519E7,-1374.1941289386752);

        YPoint CURRPOSL = new YPoint(2.239371715725563E7,-282.84268058137167);
        YPoint CURRPOSR = calculateIntersectionUpSide(CURRPOSL,secondRandom,863.4330385765176);
        //YPoint CURRPOSR = new YPoint(2.239454615370199E7,-41.42684398497599);

        System.out.println(MetricCollection.euklideanDistanceR2(NEWPOSL.x,NEWPOSL.y,NEWPOSR.x,NEWPOSR.y));

       // Node asd = basicGraph.createNode(NEWPOSL);
      //  Node asd2 = basicGraph.createNode(NEWPOSR);

      //  graph2D.getRealizer(asd).setFillColor(Color.blue);
      //  graph2D.getRealizer(asd2).setFillColor(Color.blue);

       // basicGraph.createNode(CURRPOSL);
       // basicGraph.createNode(CURRPOSR);

        this.applyRotation(NEWPOSL,NEWPOSR,CURRPOSL,CURRPOSR);
        */


        /*
        generateH3();
        this.graphView.getRelationships().generateRandomPartitions();
        this.initializeAssets();
        realizeH3();
        graphView.fitContent();
        */

        /*
        HannoiGraph hannoiGraph = new HannoiGraph(graphView,5);
        */


        /*
        YPoint point1 = new YPoint(200,100);
        YPoint point2 = calculateIntersectionUpSide(point1,-120,200);
        basicGraph.createNode(point1);
        basicGraph.createNode(point2);

        YPoint p3 = extendLineSegment(point1,point2,500);


        basicGraph.createNode(p3);
        */

        graphView.fitContent();

        YPoint center = new YPoint(500,-100);

        YPoint point1 = new YPoint(-50,-400);
        YPoint point2 = calculateIntersectionUpSide(point1,-25,200);
        System.out.println(point2);
        circleLineIntersection(point1,point2,center,120);

        /*
        YPoint center = new YPoint(0,0);

        YPoint p1 = calculateIntersectionUpSide(center,50,200);
       // basicGraph.createNode(p1);
       // basicGraph.createNode(calculatePointOnCircleClockwise(p1,120,100));

        YPoint p2 = calculateIntersectionUpSide(p1,70,400);

        YPoint p3 = calculateIntersectionUpSide(center,80,200);
        YPoint p4 = calculateIntersectionUpSide(p3,40,500);

        basicGraph.createEdge(basicGraph.createNode(p1),basicGraph.createNode(p2));
        basicGraph.createEdge(basicGraph.createNode(p3),basicGraph.createNode(p4));
        intersectionTwoLineSegments(p1,p2,p3,p4);
        */
    }

    private void circleLineIntersection(YPoint p1,YPoint p2,YPoint centerLine,double radius){

        basicGraph.createNode(centerLine);
        basicGraph.createNode(p1);
        basicGraph.createNode(p2);
        this.graphView.setAddVertexDistanceCircle(centerLine,radius);

      //  p1= basisChange(p1);
      // p2= basisChange(p2);

        double centerX = centerLine.getX();
        double centerY = centerLine.getY();


        double x1=p1.getX()-centerX;
        double y1=p1.getY()-centerY;
        double x2= p2.getX()-centerX;
        double y2= p2.getY()-centerY;

        double A = y2-y1;
        double B = (x1-x2);
        double C = (x2*y1)-(x1*y2);

        System.out.println(A+" "+B+" "+C);

        double d0 = (Math.abs(C)/Math.sqrt(Math.pow(A,2)+Math.pow(B,2)));
        System.out.println(d0);

        if( d0 > radius){
            System.out.println("No intersection");
        }
        else if(d0 == radius){
            System.out.println("1 intersection");
        }
        else {

            System.out.println("2 Intersections");

            //double d= radius*radius - C*C / (A*A+B*B);
            //double m = Math.sqrt(d/ (A*A+B*B));
          //  double d = Math.sqrt(Math.pow(radius, 2) - ((Math.pow(C, 2)) / (Math.pow(A, 2) + Math.pow(B, 2))));
          //  double m = Math.sqrt((Math.pow(d, 2)) / ( (Math.pow(A, 2) + Math.pow(B, 2))));

            double x0 = - ( (A * C) / (Math.pow(A, 2) + Math.pow(B, 2)));
            double y0 = -( (B * C) / (Math.pow(A, 2) + Math.pow(B, 2)));

            double d= Math.sqrt(Math.pow(radius,2) - Math.pow(Math.abs(C)/Math.sqrt(A*A+B*B),2)  );
            double scale = d / (Math.sqrt( A*A+B*B));
            double m= scale;

            double r1X =  x0 + B * m+centerX;
            double r1Y = y0 - A * m+centerY;

            double r2X = x0 - B * m+centerX;

            double r2Y = y0 + A * m+centerY;

           basicGraph.createNode(new YPoint(r1X, r1Y));
           basicGraph.createNode(new YPoint(r2X, r2Y));

           // basicGraph.createNode(basisChange(new YPoint(r1X, r1Y)));
           // basicGraph.createNode(basisChange(new YPoint(r2X, r2Y)));

            System.out.println(r1X+" "+r1Y+" "+r2X+" "+r2Y);
        }
    }


    private void intersectionTwoLineSegments(YPoint p1,YPoint p2,YPoint p3,YPoint p4){
        double x1= p1.getX();
        double y1= p1.getY();
        double x2= p2.getX();
        double y2= p2.getY();
        double x3= p3.getX();
        double y3= p3.getY();
        double x4= p4.getX();
        double y4= p4.getY();

        double denominator = (x4-x3)*(y1-y2)-(x1-x2)*(y4-y3);
        if( denominator == 0){
            System.out.println("NO INTERSECTION");
        }
        else{
            double numerator1 = (y3-y4)*(x1-x3)+(x4-x3)*(y1-y3);
            double numerator2 = (y1-y2)*(x1-x3)+(x2-x1)*(y1-y3);

            double tA = numerator1/denominator;
            double tB = numerator2/denominator;

            if( (tA >= 0 && tA <=1) && (tB >= 0 && tB <=1)){
                double intersectionX = x1 + tA*(x2-x1);
                double intersectionY = y1 + tA*(y2-y1);
                YPoint intersection = new YPoint(intersectionX,intersectionY);
                basicGraph.createNode(intersection);
            }
            else{
                System.out.println("Infinite Intersection");
            }
        }
    }



    private void realizeH3(){
        double offsetX = 2000;
        for( int i=0; i < 3; i++){
            ArrayList<NodeList> currentH2 = nodes.get(i);
            NodeList topK3 = currentH2.get(0);
            NodeList leftK3 = currentH2.get(1);
            NodeList rightK3 = currentH2.get(2);
            realizeH2(topK3,leftK3,rightK3);
            this.START_POSITION = new YPoint(START_POSITION.x+offsetX,0);
           // System.out.println(topK3+" "+leftK3+" "+rightK3);
        }


        ArrayList<NodeList> leftH2 = nodes.get(0);
        ArrayList<NodeList> topH2 = nodes.get(1);
        ArrayList<NodeList> rightH2 = nodes.get(2);

        Node leftTop3 = (Node) leftH2.get(0).get(2);
        Node leftRight3 = (Node) leftH2.get(2).get(2);
        Node leftLeft3 = (Node) leftH2.get(1).get(2);

        Node topLeft3 = (Node) topH2.get(1).get(2);
        Node topRight3 = (Node) topH2.get(2).get(2);

        Node rightTop3 = (Node) rightH2.get(0).get(2);
        Node rightLeft3 = (Node) rightH2.get(1).get(2);

        YPoint positionLeftTop3 = graph2D.getCenter(leftTop3);
        YPoint positionLeftRight3 = graph2D.getCenter(leftRight3);

        YPoint positionTopLeft3 = graph2D.getCenter(topLeft3);
        YPoint positionTopRight3 = graph2D.getCenter(topRight3);

        YPoint positionRightTop3 = graph2D.getCenter(rightTop3);
        YPoint positionRightLeft3 = graph2D.getCenter(rightLeft3);

        YPoint positionTopH2 = calculateNextPosition(positionLeftTop3,positionLeftRight3,isEdgeLong(leftTop3,topLeft3));

        double lengthSecondConnectionEdge;

        if( isEdgeLong(topRight3,rightTop3)){
            lengthSecondConnectionEdge = LENGTH_LONG_EDGES;
        }
        else{
            lengthSecondConnectionEdge = LENGTH_SHORT_EDGES;
        }

        double lengthFirstIntersection = lengthSecondConnectionEdge + MetricCollection.euklideanDistanceR2(positionTopLeft3.x,positionTopLeft3.y,positionTopRight3.x,positionTopRight3.y);

        double lengthThirdConnectionEdge;

        if( isEdgeLong(leftRight3,rightLeft3)){
            lengthThirdConnectionEdge = LENGTH_LONG_EDGES;
        }
        else{
            lengthThirdConnectionEdge = LENGTH_SHORT_EDGES;
        }

        double lengthSecondIntersection = lengthThirdConnectionEdge + MetricCollection.euklideanDistanceR2(positionRightTop3.x,positionRightTop3.y,positionRightLeft3.x,positionRightLeft3.y);

        YPoint intersectionPoint = calculateCircleCircleIntersection(positionTopH2,lengthFirstIntersection,positionLeftRight3,lengthSecondIntersection)[0];

        YPoint newPositionTopRight3 = calculateAffineLineCirclePositionBackwards(positionTopH2,intersectionPoint,leftLeft3,topRight3,rightTop3);

        YPoint newPositionRightLeft3 = calculateAffineLineCirclePositionBackwards(intersectionPoint,positionLeftRight3,leftTop3,leftRight3,rightLeft3);

        moveTopH2(positionTopH2,newPositionTopRight3,topH2);
        moveRightH2(intersectionPoint,newPositionRightLeft3,rightH2);

    }



    private double calculateRotationAngle( YPoint newPosL, YPoint newPosR, YPoint currentPosL, YPoint currentPosR  ){


        /*
        1. Translate newPosR and curentPosR to the origin (0,0)
            dist(newPosL,newPosR)=dist(currentPosL,currentPosR)
         */
        double newPosOriginXOffset = newPosL.getX();
        double newPosOriginYOffset = newPosL.getY();
        double currentPosOriginXOffset = currentPosL.getX();
        double currentPosOriginYOffset = currentPosL.getY();

        YPoint translatedNewPosR = new YPoint(newPosR.getX()-newPosOriginXOffset,newPosR.getY()-newPosOriginYOffset);
        YPoint translatedCurrentPosR = new YPoint(currentPosR.getX()-currentPosOriginXOffset,currentPosR.getY()-currentPosOriginYOffset);


        /*
        2. Calculate Interior angle of K3 [Origin,translatedNewPos,translatedCurrentPos] using Law of Cosines
         */
        double translatedNewPosX = translatedNewPosR.getX();
        double translatedNewPosY = translatedNewPosR.getY();
        double translatedCurrentPosX = translatedCurrentPosR.getX();
        double translatedCurrentPosY = translatedCurrentPosR.getY();

        double lengthBasis = MetricCollection.euklideanDistanceR2(0,0,translatedNewPosX,translatedNewPosY);
        double lengthTranslatedNewPosTranslatedCurrentPos = MetricCollection.euklideanDistanceR2(translatedNewPosX,translatedNewPosY,translatedCurrentPosX,translatedCurrentPosY);

        double lawOfCosines = (Math.pow(lengthBasis,2)+Math.pow(lengthBasis,2)-Math.pow(lengthTranslatedNewPosTranslatedCurrentPos,2) ) / ( 2* lengthBasis *lengthBasis);

        double interiorAngle = Math.toDegrees(Math.acos(lawOfCosines));

        System.out.println("Interior Angle :"+ interiorAngle);


        /*
        3. Separate between 4 cases
         */

        double rotationAngle;

        double crossProduct = translatedNewPosX * translatedCurrentPosY - translatedCurrentPosX*translatedNewPosY;

        if( crossProduct > 0){
            rotationAngle = -interiorAngle;
        }
        else if( crossProduct < 0){
            rotationAngle = interiorAngle;
        }
        else{
            rotationAngle = 180;
        }
        return rotationAngle;
    }

    private YPoint performRotationWithVectorAddition( YPoint position, double rotationAngle , YPoint vector2Add){
        double rotatedX = (position.getX() * Math.cos(Math.toRadians(rotationAngle))) - (position.getY() * Math.sin(Math.toRadians(rotationAngle)))+vector2Add.getX();
        double rotatedY = (position.getX() * Math.sin(Math.toRadians(rotationAngle))) + (position.getY() * Math.cos(Math.toRadians(rotationAngle)))+vector2Add.getY();
        return new YPoint(rotatedX,rotatedY);
    }


    private void moveRightH2(YPoint newPosTop3, YPoint newPosLeft3, ArrayList<NodeList> H2 ){

        YPoint currentPosTop3 = graph2D.getCenter( (Node) H2.get(0).get(2));
        YPoint currentPosLeft3 = graph2D.getCenter( (Node) H2.get(1).get(2));

        double rotationAngle = calculateRotationAngle(newPosTop3,newPosLeft3,currentPosTop3,currentPosLeft3);

        double originXOffset = currentPosTop3.getX();
        double originYOffset = currentPosTop3.getY();

        for( NodeList list: H2){
            for( int i=0; i < list.size(); i++){
                Node currentNode = (Node) list.get(i);

                YPoint currentPos = graph2D.getCenter(currentNode);
                YPoint translatedPos = new YPoint(currentPos.getX()-originXOffset,currentPos.getY()-originYOffset);

                YPoint rotatedPos = performRotationWithVectorAddition(translatedPos,rotationAngle,newPosTop3);

                basicGraph.moveNode(currentNode,rotatedPos);
            }
        }
    }

    private void moveTopH2( YPoint newPosLeft3, YPoint newPosRight3, ArrayList<NodeList> H2){

        YPoint currentPosLeft3 = graph2D.getCenter( (Node) H2.get(1).get(2));
        YPoint currentPosRight3 = graph2D.getCenter( (Node) H2.get(2).get(2));

        double rotationAngle = calculateRotationAngle(newPosLeft3,newPosRight3,currentPosLeft3,currentPosRight3);

        double originXOffset = currentPosLeft3.getX();
        double originYOffset = currentPosLeft3.getY();

        for( NodeList list: H2){
            for( int i=0; i < list.size(); i++){

                Node currentNode = (Node) list.get(i);

                YPoint currentPos = graph2D.getCenter(currentNode);
                YPoint translatedPosition = new YPoint(currentPos.getX()-originXOffset,currentPos.getY()-originYOffset);

                YPoint rotatedPos = performRotationWithVectorAddition(translatedPosition,rotationAngle,newPosLeft3);

                basicGraph.moveNode(currentNode,rotatedPos);
            }
        }

    }

    private double angleBetween2Points( YPoint point1, YPoint point2) {

        double point1X = point1.getX();
        double point1Y = point1.getY();
        double point2X = point2.getX();
        double point2Y = point2.getY();

        if (point2X - point1X == 0) {
            return 90;
        } else {

            double slope = (point2Y - point1Y) / (point2X - point1X);

            if (slope == 0) {
                return 0;
            } else {
                  /*
            point1
                \
                 \
                   point2
         */
                if (point1X < point2X && point1Y < point2Y) {

                    double differenceX = point2X-point1X;
                    double differenceY = point2Y-point1Y;

                    double angle = Math.toDegrees( Math.atan(differenceY/differenceX));
                    return angle;
                }
        /*
                    point1
                   /
                  /
              point2
         */
                else if (point1X > point2X && point1Y < point2Y) {

                    double differenceX = point1X-point2X;
                    double differenceY = point2Y-point1Y;

                    double angle = Math.toDegrees( Math.atan(differenceY/differenceX));
                    return angle;

                }
                /*
                    point2
                   /
                  /
              point1
                */
                else if (point1X < point2X && point1Y > point2Y) {
                    double differenceY = point1Y - point2Y;
                    double differenceX = point2X - point1X;
                    double angle = Math.toDegrees(Math.atan(differenceY / differenceX));
                    return angle;
                }
                /*
                point2
                   \
                    \
                    point1
                */
                else {
                    double differenceY = point1Y - point2Y;
                    double differenceX = point1X - point2X;
                    double angle = Math.toDegrees(Math.atan(differenceY / differenceX));
                    return angle;
                }
            }

        }
    }


    private YPoint calculateAffineLineCirclePositionBackwards( YPoint affineLineP1, YPoint affineLineP2, Node minDistanceNode , Node source,Node target){

        double length;

        if( isEdgeLong(source,target)){
            length = LENGTH_LONG_EDGES;
        }
        else{
            length = LENGTH_SHORT_EDGES;
        }

        AffineLine line = new AffineLine(affineLineP1,affineLineP2);

        YPoint[] intersections = calculateIntersectionCircleLine(affineLineP2.x,affineLineP2.y,length,line);

        return getMinDistanceIntersection(intersections, minDistanceNode);

    }

    private YPoint calculateNextPosition( YPoint point1, YPoint point2, boolean isLong){
        double slopeAngle = calculateSlopeAngle(point1,point2);

        double angle;

        if( slopeAngle > 0){
            angle = 180- slopeAngle;
        }
        else{
            angle = -slopeAngle;
        }

        if( isLong) {
            return calculateIntersectionUpSide(point1,angle,LENGTH_LONG_EDGES);
        }
        else{
            return calculateIntersectionUpSide(point1,angle,LENGTH_SHORT_EDGES);
        }
    }

    private double calculateSlopeAngle( YPoint point1, YPoint point2){
        double point1X = point1.getX();
        double point1Y = point1.getY();
        double point2X = point2.getX();
        double point2Y = point2.getY();

        if( point2X - point1X == 0){
            return 90;
        }
        else{

            double slope = (point2Y-point1Y) / (point2X-point1X);

            if( slope == 0 ){
                return 0;
            }
            else{
                return Math.toDegrees(Math.atan(slope));
            }

        }
    }

    private void generateH3(){

        this.nodes = new ArrayList<>();

        for( int i=0; i < 3 ; i++){
            NodeList topK3 = new NodeList();
            NodeList leftK3 = new NodeList();
            NodeList rightK3 = new NodeList();
            this.generateH2(topK3,leftK3,rightK3);

            ArrayList<NodeList> currentH2 = new ArrayList<>();
            currentH2.add(topK3);
            currentH2.add(leftK3);
            currentH2.add(rightK3);

            nodes.add(currentH2);
        }

        ArrayList<NodeList> leftH2 = nodes.get(0);
        ArrayList<NodeList> topH2 = nodes.get(1);
        ArrayList<NodeList> rightH2 = nodes.get(2);

        Node leftTop3 = (Node) leftH2.get(0).get(2);
        Node leftRight3 = (Node) leftH2.get(2).get(2);

        Node topLeft3 = (Node) topH2.get(1).get(2);
        Node topRight3 = (Node) topH2.get(2).get(2);

        Node rightTop3 = (Node) rightH2.get(0).get(2);
        Node rightLeft3 = (Node) rightH2.get(1).get(2);

        basicGraph.createEdge(leftTop3,topLeft3);
        basicGraph.createEdge(topRight3,rightTop3);
        basicGraph.createEdge(rightLeft3,leftRight3);


    }


    private void generateH2( NodeList topK3, NodeList leftK3, NodeList rightK3){


        for( int i=0; i < 3; i++){
            Node topNode = basicGraph.createNode();
            Node leftNode = basicGraph.createNode();
            Node rightNode = basicGraph.createNode();

            topK3.add(topNode);
            leftK3.add(leftNode);
            rightK3.add(rightNode);
        }

        for( int i=0; i < 3; i++){
            Node currentTopNode = (Node) topK3.get(i);
            Node currentLeftNode = (Node) leftK3.get(i);
            Node currentRightNode = (Node) rightK3.get(i);


            Node nextTopNode;
            Node nextLeftNode;
            Node nextRightNode;

            if( i+1 < 3){
                nextTopNode = (Node) topK3.get(i+1);
                nextLeftNode = (Node) leftK3.get(i+1);
                nextRightNode = (Node) rightK3.get(i+1);
            }
            else{
                nextTopNode = (Node) topK3.get(0);
                nextLeftNode = (Node) leftK3.get(0);
                nextRightNode = (Node) rightK3.get(0);
            }

            basicGraph.createEdge(currentTopNode,nextTopNode);
            basicGraph.createEdge(currentLeftNode,nextLeftNode);
            basicGraph.createEdge(currentRightNode,nextRightNode);
        }

        basicGraph.createEdge( (Node) topK3.get(0),(Node) leftK3.get(0));
        basicGraph.createEdge( (Node) topK3.get(1),(Node) rightK3.get(0));
        basicGraph.createEdge( (Node) leftK3.get(1),(Node) rightK3.get(1));



    }


    private void realizeH2( NodeList topK3, NodeList leftK3, NodeList rightK3){

        Node top3 = (Node) topK3.get(2);
        Node top2 = (Node) topK3.get(1);
        Node top1 = (Node) topK3.get(0);

        Node right3 = (Node) rightK3.get(2);
        Node right2 = (Node) rightK3.get(1);
        Node right1 = (Node) rightK3.get(0);

        Node left3 = (Node) leftK3.get(2);
        Node left2 = (Node) leftK3.get(1);
        Node left1 = (Node) leftK3.get(0);

        NodeList left = new NodeList();
        left.add(top1);
        left.add(left1);
        left.add(left2);

        NodeList right = new NodeList();
        right.add(top1);
        right.add(top2);
        right.add(right1);

        NodeList bot = new NodeList();
        bot.add(right1);
        bot.add(right2);
        bot.add(left2);

        double distLeft = lengthStraightLine(left);
        double distRight = lengthStraightLine(right);
        double distBot = lengthStraightLine(bot);

        YPoint leftDestination = calculateIntersectionUpSide(START_POSITION,initialAngle,distLeft);
        basicGraph.moveNode(left2,START_POSITION);
        basicGraph.moveNode(top1,leftDestination);

        YPoint intersection = calculateCircleCircleIntersection(leftDestination,distRight,START_POSITION,distBot)[0];
        basicGraph.moveNode(right1,intersection);


        AffineLine lineLeft = new AffineLine(START_POSITION,leftDestination);
        AffineLine lineRight = new AffineLine(leftDestination,intersection);
        AffineLine lineBot = new AffineLine(START_POSITION,intersection);


        boolean left2Left1Long = isEdgeLong(left2,left1);
        boolean top1Top2Long = isEdgeLong(top1,top2);
        boolean right1Right2Long = isEdgeLong(right1,right2);

       // System.out.println( left2Left1Long+" "+top1Top2Long+" "+right1Right2Long);

        YPoint destinationLeft1;
        YPoint destinationTop2;
        YPoint destinationRight2;

        if( left2Left1Long){
         YPoint[] intersections = calculateIntersectionCircleLine(START_POSITION.x,START_POSITION.y,LENGTH_LONG_EDGES,lineLeft);
            destinationLeft1 = getMinDistanceIntersection(intersections,top1);
        }
        else{
            YPoint[] intersections = calculateIntersectionCircleLine(START_POSITION.x,START_POSITION.y,LENGTH_SHORT_EDGES,lineLeft);
            destinationLeft1 = getMinDistanceIntersection(intersections,top1);
        }

        if( top1Top2Long){
            YPoint[] intersections = calculateIntersectionCircleLine(leftDestination.x,leftDestination.y,LENGTH_LONG_EDGES,lineRight);
            destinationTop2 = getMinDistanceIntersection(intersections,right1);
        }
        else{
            YPoint[] intersections = calculateIntersectionCircleLine(leftDestination.x,leftDestination.y,LENGTH_SHORT_EDGES,lineRight);
            destinationTop2 = getMinDistanceIntersection(intersections,right1);
        }

        if( right1Right2Long){
            YPoint[] intersections = calculateIntersectionCircleLine(intersection.x,intersection.y,LENGTH_LONG_EDGES,lineBot);
            destinationRight2 = getMinDistanceIntersection(intersections,left2);
        }
        else{
            YPoint[] intersections = calculateIntersectionCircleLine(intersection.x,intersection.y,LENGTH_SHORT_EDGES,lineBot);
            destinationRight2 = getMinDistanceIntersection(intersections,left2);
        }

          basicGraph.moveNode(left1,destinationLeft1);
          basicGraph.moveNode(top2,destinationTop2);
          basicGraph.moveNode(right2,destinationRight2);

        realizeK3(top3,top1,top2,left2);
        realizeK3(right3,right1,right2,top1);
        realizeK3(left3,left1,left2,top2);




    }

    private void realizeK3( Node nodeToMove, Node adjacent1, Node adjacent2,Node maxDistanceNode){

        YPoint positionAdjacent1 = graph2D.getCenter( adjacent1);
        YPoint positionAdjacent2 = graph2D.getCenter( adjacent2);

        boolean nodeAdjacent1Long = isEdgeLong(nodeToMove,adjacent1);
        boolean nodeAdjacent2Long = isEdgeLong(nodeToMove,adjacent2);


        YPoint destination;
        YPoint[] intersections;

        if( nodeAdjacent1Long && nodeAdjacent2Long){
            intersections = calculateCircleCircleIntersection(positionAdjacent1,LENGTH_LONG_EDGES,positionAdjacent2,LENGTH_LONG_EDGES);
        }
        else if( !nodeAdjacent1Long && nodeAdjacent2Long){
            intersections =calculateCircleCircleIntersection(positionAdjacent1,LENGTH_SHORT_EDGES,positionAdjacent2,LENGTH_LONG_EDGES);
        }
        else if( nodeAdjacent1Long && !nodeAdjacent2Long){
            intersections = calculateCircleCircleIntersection(positionAdjacent1,LENGTH_LONG_EDGES,positionAdjacent2,LENGTH_SHORT_EDGES);
        }
        else{
            intersections = calculateCircleCircleIntersection(positionAdjacent1,LENGTH_SHORT_EDGES,positionAdjacent2,LENGTH_SHORT_EDGES);
        }

        destination = getMaxDistanceIntersection(intersections,maxDistanceNode);

        basicGraph.moveNode(nodeToMove,destination);

    }

    private YPoint getMaxDistanceIntersection( YPoint[] intersections, Node maxDistanceNode ){

        YPoint positionMaxDistanceNode = graph2D.getCenter(maxDistanceNode);

        double maxX = positionMaxDistanceNode.getX();
        double maxY = positionMaxDistanceNode.getY();

        YPoint int1 = intersections[0];
        YPoint int2 = intersections[1];

        double distInt1MaxNode = MetricCollection.euklideanDistanceR2(maxX,maxY,int1.x,int1.y);
        double distInt2MaxNode = MetricCollection.euklideanDistanceR2(maxX,maxY,int2.x,int2.y);

        if( distInt1MaxNode < distInt2MaxNode){
            return int2;
        }
        else{
            return int1;
        }
    }

    private YPoint getMinDistanceIntersection( YPoint[] intersections, Node minDistanceNode ){

        YPoint positionMinDistanceNode = graph2D.getCenter(minDistanceNode);

        double minX = positionMinDistanceNode.getX();
        double minY = positionMinDistanceNode.getY();

        YPoint int1 = intersections[0];
        YPoint int2 = intersections[1];

        double distInt1MaxNode = MetricCollection.euklideanDistanceR2(minX,minY,int1.x,int1.y);
        double distInt2MaxNode = MetricCollection.euklideanDistanceR2(minX,minY,int2.x,int2.y);

        if( distInt1MaxNode > distInt2MaxNode){
            return int2;
        }
        else{
            return int1;
        }
    }


    private double lengthStraightLine( NodeList list){
        double result = 0;
        for( int i=0; i < list.size(); i++){
            if( i+1 < list.size()){
                Node currentNode = (Node) list.get(i);
                Node nextNode = (Node) list.get(i+1);

                if( isEdgeLong(currentNode,nextNode)){
                    result += LENGTH_LONG_EDGES;
                }
                else{
                    result +=LENGTH_SHORT_EDGES;
                }
            }
        }
        return result;
    }

    @Override
    public boolean isGraphType() {
        return false;
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

    private void initializeAssets(){
        this.shortEdges = graphView.getRelationships().getEdgesFromPartition(1);
        this.longEdges = graphView.getRelationships().getEdgesFromPartition(2);
    }
}
