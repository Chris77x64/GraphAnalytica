package realizations;


import algo.TwoTreeLexBFS;
import model.MetricCollection;
import view.GraphView;
import y.base.Edge;
import y.base.Node;
import y.base.NodeList;
import y.geom.YPoint;

import java.util.ArrayList;
import java.util.Collections;

import static y.util.Generics.nodes;

/**
 * Created by chris on 05.02.2018.
 */
public class RealizationRectangularGraph extends Realization {

    private final double LENGTH_SHORT_EDGE = 200;
    private final double LENGTH_LONG_EDGE = 400;

    private ArrayList<Node> vertexOrder;

    private ArrayList<Edge> shortEdges;
    private ArrayList<Edge> longEdges;


    public RealizationRectangularGraph(GraphView graphView) {
        super(graphView);
        this.calculateVertexOrder();
        this.initializeAssets();
        this.calculateRealization();

        this.basicGraph.updateViews();
        this.graphView.fitContent();
    }

    private void initializeAssets(){
        this.shortEdges = graphView.getRelationships().getEdgesFromPartition(1);
        this.longEdges = graphView.getRelationships().getEdgesFromPartition(2);
    }


    private void calculateRealization(){

        this.initializeFirstTwoVertices();

        for( int i=0; i < vertexOrder.size(); i+=2){
            if( i+3 < vertexOrder.size()) {

                Node topLeft = vertexOrder.get(i);
                Node botLeft = vertexOrder.get(i + 1);
                Node topRight = vertexOrder.get(i + 2);
                Node botRight = vertexOrder.get(i + 3);

                boolean topLeftBotLeftLong = isEdgeLong(topLeft,botLeft);
                boolean topLeftTopRightLong = isEdgeLong(topLeft,topRight);
                boolean botLeftBotRightLong = isEdgeLong(botLeft,botRight);
                boolean topRightBotRightLong = isEdgeLong(topRight,botRight);

                if( !topLeftBotLeftLong && !topLeftTopRightLong && !botLeftBotRightLong && topRightBotRightLong ){
                    this.caseKKKL(topLeft,botLeft,topRight,botRight);
                }
                else if( topLeftBotLeftLong && !topLeftTopRightLong && !botLeftBotRightLong && !topRightBotRightLong ){
                    this.caseLKKK(topLeft,botLeft,topRight,botRight);
                }
                else if( !topLeftBotLeftLong && topLeftTopRightLong && botLeftBotRightLong && topRightBotRightLong ){
                    this.caseKLLL(topLeft,botLeft,topRight,botRight);
                }
                else if( topLeftBotLeftLong && topLeftTopRightLong && botLeftBotRightLong && !topRightBotRightLong){
                    this.caseLLLK(topLeft,botLeft,topRight,botRight);
                }
                /*
                    Rectangular Cases
                 */
                else if( !topLeftBotLeftLong && !topLeftTopRightLong && !botLeftBotRightLong && !topRightBotRightLong){
                    this.caseKKKK(topLeft,botLeft,topRight,botRight);
                }
                else if( !topLeftBotLeftLong && topLeftTopRightLong && botLeftBotRightLong && !topRightBotRightLong){
                    this.caseKLLK(topLeft,botLeft,topRight,botRight);
                }
                else if( topLeftBotLeftLong && topLeftTopRightLong && botLeftBotRightLong && topRightBotRightLong){
                    this.caseLLLL(topLeft,botLeft,topRight,botRight);
                }
                else if( topLeftBotLeftLong && !topLeftTopRightLong && !botLeftBotRightLong && topRightBotRightLong){
                    this.caseLKKL(topLeft,botLeft,topRight,botRight);
                }
                /*
                Kite Cases
                 */
                else if( !topLeftBotLeftLong && !topLeftTopRightLong && botLeftBotRightLong && topRightBotRightLong){
                    this.caseKKLL(topLeft,botLeft,topRight,botRight);
                }
                else if( !topLeftBotLeftLong && topLeftTopRightLong && !botLeftBotRightLong && topRightBotRightLong){
                    this.caseKLKL(topLeft,botLeft,topRight,botRight);
                }
                else if( topLeftBotLeftLong && topLeftTopRightLong && !botLeftBotRightLong && !topRightBotRightLong){
                    this.caseLLKK(topLeft,botLeft,topRight,botRight);
                }
                else if(topLeftBotLeftLong && !topLeftTopRightLong && botLeftBotRightLong && !topRightBotRightLong ){
                    this.caseLKLK(topLeft,botLeft,topRight,botRight);
                }
                /*
                Irregular Cases
                 */
                else if( !topLeftBotLeftLong && !topLeftTopRightLong && botLeftBotRightLong && !topRightBotRightLong ){
                    this.caseKKLK(topLeft,botLeft,topRight,botRight);
                }
                else if( !topLeftBotLeftLong && topLeftTopRightLong && !botLeftBotRightLong && !topRightBotRightLong ){
                    this.caseKLKK(topLeft,botLeft,topRight,botRight);
                }
                else if( topLeftBotLeftLong && !topLeftTopRightLong && botLeftBotRightLong && topRightBotRightLong){
                    this.caseLKLL(topLeft,botLeft,topRight,botRight);
                }
                else if( topLeftBotLeftLong && topLeftTopRightLong && !botLeftBotRightLong && topRightBotRightLong){
                    this.caseLLKL(topLeft,botLeft,topRight,botRight);
                }


            }
        }

    }


    private void initializeFirstTwoVertices(){

        Node firstVertex = vertexOrder.get(0);
        Node secondVertex = vertexOrder.get(1);

        YPoint center = calculateCenterGraphView();


        YPoint firstLocation = null;
        YPoint secondLocation = null;

        Edge firstEdge = firstVertex.getEdge(secondVertex);

        double skewness = -30;
        if( shortEdges.contains(firstEdge)) {
            firstLocation = new YPoint(341.0, 612.0);
            secondLocation = calculateIntersectionUpSide(firstLocation,skewness,200);
           //secondLocation = new YPoint(1642.05, 703.92);
            //firstLocation = new YPoint(10, center.y - ( 0.5 * LENGTH_SHORT_EDGE));
            //secondLocation = new YPoint(10, center.y + ( 0.5 * LENGTH_SHORT_EDGE));
        }
        else{
            firstLocation = new YPoint(341.0, 612.0);
            secondLocation = calculateIntersectionUpSide(firstLocation,skewness,400);
            //firstLocation = new YPoint(10, center.y - ( 0.5 * LENGTH_LONG_EDGE));
            //secondLocation = new YPoint(10, center.y + ( 0.5* LENGTH_LONG_EDGE));
        }


        this.basicGraph.moveNode(firstVertex,firstLocation);
        this.basicGraph.moveNode(secondVertex,secondLocation);


    }

    private void calculateVertexOrder(){
        Node startNode = getDegreeTwoNode();
        TwoTreeLexBFS twoTreeLexBFS = new TwoTreeLexBFS(graph2D,startNode);
        this.vertexOrder = twoTreeLexBFS.getLexicographicalOrder();
    }


    private ArrayList<Node> modifyLexicographicalBFSOrder( ArrayList<Node> lexBFSOrder){
        ArrayList<Node> result = lexBFSOrder;

        for( int i=2; i < result.size(); i +=2){
            if( i+1 < result.size()){
                int currentNodeIndex = i;
                int nextNodeIndex = i+1;

                Node currentNode = lexBFSOrder.get(i);
                Node previousNode = lexBFSOrder.get(i-2);

                if( currentNode.getEdge(previousNode) == null){
                    Collections.swap(result,currentNodeIndex,nextNodeIndex);
                }
            }
        }
        String res = " ";
        for( Node node : result){
            res += node+" ";
        }
        System.out.println(res);
        return result;
    }


    private YPoint calculateCenterGraphView(){
        double worldHeight = this.graphView.getWorldRect2D().getHeight();
        double worldWidth = this.graphView.getWorldRect2D().getWidth();
        return new YPoint(worldWidth/2,worldHeight/2);
    }


    private Node getDegreeTwoNode(){
        for( Node vertex: nodes(graph2D)){
            if( vertex.degree() == 2){
                System.out.println("degree 2 vertex: "+vertex.index());
                return vertex;
            }
        }
        return null;
    }

    private void caseKKKL(Node topLeft,Node botLeft,Node topRight,Node botRight) {
        YPoint positionTopLeft = this.graph2D.getCenter(topLeft);
        YPoint positionBotLeft = this.graph2D.getCenter(botLeft);

        double topLeftX = this.graph2D.getCenterX(topLeft);
        double topLeftY = this.graph2D.getCenterY(topLeft);
        double botLeftX = this.graph2D.getCenterX(botLeft);
        double botLeftY = this.graph2D.getCenterY(botLeft);

        double angleTop;
        double angleBot;
        YPoint topRightDestination = null;
        YPoint botRightDestination = null;

        if (botLeftY < topLeftY) {

            double[] skewnessOffsetYCase = skewnessOffsetYCase(topLeft, botLeft);

            double topOffset = skewnessOffsetYCase[0];
            double botOffset = skewnessOffsetYCase[1];

            if (topLeftX < botLeftX) {
                angleTop = 120 + topOffset;
                angleBot = botOffset - 120; ;
                topRightDestination = calculateIntersectionUpSide(positionTopLeft,angleTop,LENGTH_SHORT_EDGE);
                botRightDestination = calculateIntersectionUpSide(positionBotLeft,angleBot,LENGTH_SHORT_EDGE);

            } else if (topLeftX > botLeftX) {
                angleTop = topOffset - 120;
                angleBot = botOffset + 120;
                topRightDestination = calculateIntersectionUpSide(positionTopLeft,angleTop,LENGTH_SHORT_EDGE);
                botRightDestination = calculateIntersectionUpSide(positionBotLeft,angleBot,LENGTH_SHORT_EDGE);
            }
        }
        else{
            double skewnessOffset = this.skewnessOffset(topLeft,botLeft);
            angleTop = 30 + skewnessOffset;
            angleBot = 30 - skewnessOffset;
            topRightDestination = calculateIntersectionUpSide(positionTopLeft,angleTop,LENGTH_SHORT_EDGE);
            botRightDestination = calculateIntersectionDownSide(positionBotLeft,angleBot,LENGTH_SHORT_EDGE);
        }




        this.basicGraph.moveNode(topRight,topRightDestination);
        this.basicGraph.moveNode(botRight,botRightDestination);
    }

    private void caseLKKK(Node topLeft,Node botLeft,Node topRight,Node botRight){
        YPoint positionTopLeft = this.graph2D.getCenter(topLeft);
        YPoint positionBotLeft = this.graph2D.getCenter(botLeft);

        double topLeftX = this.graph2D.getCenterX(topLeft);
        double topLeftY = this.graph2D.getCenterY(topLeft);
        double botLeftX = this.graph2D.getCenterX(botLeft);
        double botLeftY = this.graph2D.getCenterY(botLeft);

        double angleTop;
        double angleBot;
        YPoint topRightDestination = null;
        YPoint botRightDestination = null;

        if( botLeftY < topLeftY) {

            double[] skewnessOffsetYCase = skewnessOffsetYCase(topLeft, botLeft);

            double topOffset = skewnessOffsetYCase[0];
            double botOffset = skewnessOffsetYCase[1];

            if (topLeftX < botLeftX) {

                angleTop = topOffset + Math.toDegrees(Math.acos(0.5));
                angleBot =  botOffset - Math.toDegrees(Math.acos(0.5));

                topRightDestination = calculateIntersectionUpSide(positionTopLeft,angleTop,LENGTH_SHORT_EDGE);
                botRightDestination = calculateIntersectionUpSide(positionBotLeft,angleBot,LENGTH_SHORT_EDGE);
            } else if (topLeftX > botLeftX) {

                angleTop = topOffset - Math.toDegrees(Math.acos(0.5));
                angleBot =  botOffset + Math.toDegrees(Math.acos(0.5));

                topRightDestination = calculateIntersectionUpSide(positionTopLeft,angleTop,LENGTH_SHORT_EDGE);
                botRightDestination = calculateIntersectionUpSide(positionBotLeft,angleBot,LENGTH_SHORT_EDGE);
            }
        }
        else{
            double skewnessOffset = this.skewnessOffset(topLeft,botLeft);
            angleTop = 30 - skewnessOffset;
            angleBot = 30 + skewnessOffset;

            topRightDestination = calculateIntersectionDownSide(positionTopLeft,angleTop,LENGTH_SHORT_EDGE);
            botRightDestination = calculateIntersectionUpSide(positionBotLeft,angleBot,LENGTH_SHORT_EDGE);
        }


        this.basicGraph.moveNode(topRight,topRightDestination);
        this.basicGraph.moveNode(botRight,botRightDestination);
    }

    private void caseKLLL(Node topLeft,Node botLeft,Node topRight,Node botRight ){
        YPoint positionTopLeft = this.graph2D.getCenter(topLeft);
        YPoint positionBotLeft = this.graph2D.getCenter(botLeft);


        double topLeftX = this.graph2D.getCenterX(topLeft);
        double topLeftY = this.graph2D.getCenterY(topLeft);
        double botLeftX = this.graph2D.getCenterX(botLeft);
        double botLeftY = this.graph2D.getCenterY(botLeft);

        double angleTop;
        double angleBot;
        YPoint topRightDestination = null;
        YPoint botRightDestination = null;

        if( botLeftY < topLeftY) {

            double[] skewnessOffsetYCase = skewnessOffsetYCase(topLeft, botLeft);

            double topOffset = skewnessOffsetYCase[0];
            double botOffset = skewnessOffsetYCase[1];


            if( topLeftX < botLeftX){

                angleTop = topOffset + 90 + Math.toDegrees(Math.asin(0.25));
                angleBot = botOffset - 90 - Math.toDegrees(Math.asin(0.25));

                topRightDestination = calculateIntersectionUpSide(positionTopLeft,angleTop,LENGTH_LONG_EDGE);
                botRightDestination = calculateIntersectionUpSide(positionBotLeft,angleBot,LENGTH_LONG_EDGE);


            } else if ( topLeftX > botLeftX){
                angleTop = topOffset - 90 - Math.toDegrees(Math.asin(0.25));
                angleBot = botOffset + 90 + Math.toDegrees(Math.asin(0.25));

                topRightDestination = calculateIntersectionUpSide(positionTopLeft,angleTop,LENGTH_LONG_EDGE);
                botRightDestination = calculateIntersectionUpSide(positionBotLeft,angleBot,LENGTH_LONG_EDGE);
            }
        }
        else{
            double skewnessOffset = this.skewnessOffset(topLeft,botLeft);

            angleTop = Math.toDegrees(Math.asin(0.25))+skewnessOffset;
            angleBot = Math.toDegrees(Math.asin(0.25))-skewnessOffset;

            topRightDestination = calculateIntersectionUpSide(positionTopLeft,angleTop,LENGTH_LONG_EDGE);
            botRightDestination = calculateIntersectionDownSide(positionBotLeft,angleBot,LENGTH_LONG_EDGE);
        }





        this.basicGraph.moveNode(topRight,topRightDestination);
        this.basicGraph.moveNode(botRight,botRightDestination);
    }

    private void caseLLLK(Node topLeft,Node botLeft,Node topRight,Node botRight){
        YPoint positionTopLeft = this.graph2D.getCenter(topLeft);
        YPoint positionBotLeft = this.graph2D.getCenter(botLeft);

        double topLeftX = this.graph2D.getCenterX(topLeft);
        double topLeftY = this.graph2D.getCenterY(topLeft);
        double botLeftX = this.graph2D.getCenterX(botLeft);
        double botLeftY = this.graph2D.getCenterY(botLeft);

        double angleTop;
        double angleBot;
        YPoint topRightDestination = null;
        YPoint botRightDestination = null;

        if( botLeftY < topLeftY) {

            double[] skewnessOffsetYCase = skewnessOffsetYCase(topLeft, botLeft);

            double topOffset = skewnessOffsetYCase[0];
            double botOffset = skewnessOffsetYCase[1];

            if( topLeftX < botLeftX){

                angleTop = topOffset + Math.toDegrees(Math.acos(0.25)) ;
                angleBot =  botOffset - Math.toDegrees(Math.acos(0.25));

                topRightDestination = calculateIntersectionUpSide(positionTopLeft,angleTop,LENGTH_LONG_EDGE);
                botRightDestination = calculateIntersectionUpSide(positionBotLeft,angleBot,LENGTH_LONG_EDGE);
            }
            else if ( topLeftX > botLeftX){

                angleTop = topOffset - Math.toDegrees(Math.acos(0.25)) ;
                angleBot =  botOffset + Math.toDegrees(Math.acos(0.25));

                topRightDestination = calculateIntersectionUpSide(positionTopLeft,angleTop,LENGTH_LONG_EDGE);
                botRightDestination = calculateIntersectionUpSide(positionBotLeft,angleBot,LENGTH_LONG_EDGE);
            }
        }
        else{
            double skewnessOffset = this.skewnessOffset(topLeft,botLeft);

            angleTop = Math.toDegrees(Math.asin(0.25)) - skewnessOffset;
            angleBot = Math.toDegrees(Math.asin(0.25)) + skewnessOffset;

            topRightDestination = calculateIntersectionDownSide(positionTopLeft,angleTop,LENGTH_LONG_EDGE);
            botRightDestination = calculateIntersectionUpSide(positionBotLeft,angleBot,LENGTH_LONG_EDGE);
        }




        this.basicGraph.moveNode(topRight,topRightDestination);
        this.basicGraph.moveNode(botRight,botRightDestination);
    }

    private void caseKKKK(Node topLeft,Node botLeft,Node topRight,Node botRight){
        YPoint positionTopLeft = this.graph2D.getCenter(topLeft);
        YPoint positionBotLeft = this.graph2D.getCenter(botLeft);

        double angle = rectangleSkewnessOffset(topLeft,botLeft);

        YPoint topRightDestination = calculateIntersectionUpSide(positionTopLeft,angle,LENGTH_SHORT_EDGE);
        YPoint botRightDestination = calculateIntersectionUpSide(positionBotLeft,angle,LENGTH_SHORT_EDGE);


        this.basicGraph.moveNode(topRight,topRightDestination);
        this.basicGraph.moveNode(botRight,botRightDestination);
    }

    private void caseLLLL(Node topLeft,Node botLeft,Node topRight,Node botRight){
        YPoint positionTopLeft = this.graph2D.getCenter(topLeft);
        YPoint positionBotLeft = this.graph2D.getCenter(botLeft);

        double angle = rectangleSkewnessOffset(topLeft,botLeft);

        YPoint topRightDestination = calculateIntersectionUpSide(positionTopLeft,angle,LENGTH_LONG_EDGE);
        YPoint botRightDestination = calculateIntersectionUpSide(positionBotLeft,angle,LENGTH_LONG_EDGE);


        this.basicGraph.moveNode(topRight,topRightDestination);
        this.basicGraph.moveNode(botRight,botRightDestination);
    }

    private void  caseKLLK(Node topLeft,Node botLeft,Node topRight,Node botRight){
        this.caseLLLL(topLeft,botLeft,topRight,botRight);
    }

    private void caseLKKL(Node topLeft,Node botLeft,Node topRight,Node botRight){
        this.caseKKKK(topLeft,botLeft,topRight,botRight);
    }

    private void caseKKLL(Node topLeft,Node botLeft,Node topRight,Node botRight){
        YPoint positionTopLeft = this.graph2D.getCenter(topLeft);
        YPoint positionBotLeft = this.graph2D.getCenter(botLeft);

        double lawOfCosines = (Math.pow(LENGTH_SHORT_EDGE*Math.sqrt(5),2)+Math.pow(LENGTH_SHORT_EDGE,2)-Math.pow(LENGTH_SHORT_EDGE*2,2))
                                / (2* LENGTH_SHORT_EDGE * Math.sqrt(5)*LENGTH_SHORT_EDGE);
        double lawOfCosinesAngle = Math.toDegrees(Math.acos(lawOfCosines));

        double pythagorasAngle = 180-90-Math.toDegrees(Math.atan(0.5));

        double skewnessOffset = this.skewnessOffset(topLeft,botLeft);

        double topLeftX = this.graph2D.getCenterX(topLeft);
        double topLeftY = this.graph2D.getCenterY(topLeft);
        double botLeftX = this.graph2D.getCenterX(botLeft);
        double botLeftY = this.graph2D.getCenterY(botLeft);

        double angleTop;
        double angleBot;
        YPoint topRightDestination = null;
        YPoint botRightDestination = null;

        if( botLeftY < topLeftY) {

            double[] skewnessOffsetYCase = skewnessOffsetYCase(topLeft, botLeft);

            double topOffset = skewnessOffsetYCase[0];
            double botOffset = skewnessOffsetYCase[1];

            if( topLeftX < botLeftX){

                angleTop = topOffset  + (lawOfCosinesAngle+pythagorasAngle-90)+90;
                angleBot =  botOffset - 90 ;

                topRightDestination = calculateIntersectionUpSide(positionTopLeft, angleTop, LENGTH_SHORT_EDGE);
                botRightDestination = calculateIntersectionUpSide(positionBotLeft, angleBot, LENGTH_LONG_EDGE);

            } else if ( topLeftX > botLeftX){

                angleTop = topOffset - (lawOfCosinesAngle+pythagorasAngle-90) - 90;
                angleBot = botOffset + 90;

                topRightDestination = calculateIntersectionUpSide(positionTopLeft, angleTop, LENGTH_SHORT_EDGE);
                botRightDestination = calculateIntersectionUpSide(positionBotLeft, angleBot, LENGTH_LONG_EDGE);
            }

        }
        else{
            angleTop = lawOfCosinesAngle+pythagorasAngle-90 + skewnessOffset;
            angleBot = 0 + skewnessOffset;
            topRightDestination = calculateIntersectionUpSide(positionTopLeft,angleTop,LENGTH_SHORT_EDGE);
            botRightDestination = calculateIntersectionUpSide(positionBotLeft,angleBot,LENGTH_LONG_EDGE);
        }


        this.basicGraph.moveNode(topRight,topRightDestination);
        this.basicGraph.moveNode(botRight,botRightDestination);

    }

    private void caseKLKL(Node topLeft,Node botLeft,Node topRight,Node botRight){
        YPoint positionTopLeft = this.graph2D.getCenter(topLeft);
        YPoint positionBotLeft = this.graph2D.getCenter(botLeft);

        double lawOfCosines = (Math.pow(LENGTH_SHORT_EDGE*Math.sqrt(5),2)+Math.pow(LENGTH_SHORT_EDGE,2)-Math.pow(LENGTH_SHORT_EDGE*2,2))
                / (2* LENGTH_SHORT_EDGE * Math.sqrt(5)*LENGTH_SHORT_EDGE);
        double lawOfCosinesAngle = Math.toDegrees(Math.acos(lawOfCosines));

        double pythagorasAngle = 180-90-Math.toDegrees(Math.atan(0.5));


        double topLeftX = this.graph2D.getCenterX(topLeft);
        double topLeftY = this.graph2D.getCenterY(topLeft);
        double botLeftX = this.graph2D.getCenterX(botLeft);
        double botLeftY = this.graph2D.getCenterY(botLeft);

        double angleTop;
        double angleBot;
        YPoint topRightDestination = null;
        YPoint botRightDestination = null;

        if( botLeftY < topLeftY) {

            double[] skewnessOffsetYCase = skewnessOffsetYCase(topLeft, botLeft);

            double topOffset = skewnessOffsetYCase[0];
            double botOffset = skewnessOffsetYCase[1];

            if( topLeftX < botLeftX){

                angleTop = topOffset + 90;

                angleBot = botOffset - 90 - (lawOfCosinesAngle+pythagorasAngle-90) ;

                topRightDestination = calculateIntersectionUpSide(positionTopLeft,angleTop,LENGTH_LONG_EDGE);
                botRightDestination = calculateIntersectionUpSide(positionBotLeft,angleBot,LENGTH_SHORT_EDGE);
            } else if ( topLeftX > botLeftX){

                angleTop = topOffset - 90;
                angleBot = botOffset + 90 + (lawOfCosinesAngle+pythagorasAngle-90);
                topRightDestination = calculateIntersectionUpSide(positionTopLeft,angleTop,LENGTH_LONG_EDGE);
                botRightDestination = calculateIntersectionUpSide(positionBotLeft,angleBot,LENGTH_SHORT_EDGE);
            }

        }
        else{
            double skewnessOffset = this.skewnessOffset(topLeft,botLeft);

            angleTop = 0+skewnessOffset;
            angleBot = lawOfCosinesAngle+pythagorasAngle-90-skewnessOffset;

            topRightDestination = calculateIntersectionUpSide(positionTopLeft,angleTop,LENGTH_LONG_EDGE);
            botRightDestination = calculateIntersectionDownSide(positionBotLeft,angleBot,LENGTH_SHORT_EDGE);
        }



        this.basicGraph.moveNode(topRight,topRightDestination);
        this.basicGraph.moveNode(botRight,botRightDestination);
    }


    private void caseLLKK(Node topLeft,Node botLeft,Node topRight,Node botRight){
        YPoint positionTopLeft = this.graph2D.getCenter(topLeft);
        YPoint positionBotLeft = this.graph2D.getCenter(botLeft);

        double lawOfCosinesAngle = Math.toDegrees(Math.acos( (2*Math.sqrt(5))/5 ));
        double pythagorasAngle = 180-90-Math.toDegrees(Math.atan(2));


        double topLeftX = this.graph2D.getCenterX(topLeft);
        double topLeftY = this.graph2D.getCenterY(topLeft);
        double botLeftX = this.graph2D.getCenterX(botLeft);
        double botLeftY = this.graph2D.getCenterY(botLeft);

        double angleTop;
        double angleBot;
        YPoint topRightDestination = null;
        YPoint botRightDestination = null;

        if( botLeftY < topLeftY) {

            double[] skewnessOffsetYCase = skewnessOffsetYCase(topLeft, botLeft);

            double topOffset = skewnessOffsetYCase[0];
            double botOffset = skewnessOffsetYCase[1];

            if( topLeftX < botLeftX){

                angleTop = topOffset + (lawOfCosinesAngle+pythagorasAngle) ;
                angleBot =  botOffset - 90;

                topRightDestination = calculateIntersectionUpSide(positionTopLeft,angleTop,LENGTH_LONG_EDGE);
                botRightDestination = calculateIntersectionUpSide(positionBotLeft,angleBot,LENGTH_SHORT_EDGE);

            }
            else if ( topLeftX > botLeftX){

                angleTop = topOffset - (lawOfCosinesAngle+pythagorasAngle) ;
                angleBot =  botOffset + 90;

                topRightDestination = calculateIntersectionUpSide(positionTopLeft,angleTop,LENGTH_LONG_EDGE);
                botRightDestination = calculateIntersectionUpSide(positionBotLeft,angleBot,LENGTH_SHORT_EDGE);
            }
        }
        else{
            double skewnessOffset = this.skewnessOffset(topLeft,botLeft);

            angleTop = 90-lawOfCosinesAngle-pythagorasAngle - skewnessOffset;
            angleBot = skewnessOffset;

            topRightDestination = calculateIntersectionDownSide(positionTopLeft,angleTop,LENGTH_LONG_EDGE);
            botRightDestination = calculateIntersectionUpSide(positionBotLeft,angleBot,LENGTH_SHORT_EDGE);
        }



        this.basicGraph.moveNode(topRight,topRightDestination);
        this.basicGraph.moveNode(botRight,botRightDestination);
    }

    private void caseLKLK(Node topLeft,Node botLeft,Node topRight,Node botRight ){

        YPoint positionTopLeft = this.graph2D.getCenter(topLeft);
        YPoint positionBotLeft = this.graph2D.getCenter(botLeft);

        double lawOfCosinesAngle = Math.toDegrees(Math.acos( (2*Math.sqrt(5))/5 ));
        double pythagorasAngle = 180-90-Math.toDegrees(Math.atan(2));

        double topLeftX = this.graph2D.getCenterX(topLeft);
        double topLeftY = this.graph2D.getCenterY(topLeft);
        double botLeftX = this.graph2D.getCenterX(botLeft);
        double botLeftY = this.graph2D.getCenterY(botLeft);

        double angleTop;
        double angleBot;
        YPoint topRightDestination = null;
        YPoint botRightDestination = null;

        if( botLeftY < topLeftY) {

            double[] skewnessOffsetYCase = skewnessOffsetYCase(topLeft, botLeft);

            double topOffset = skewnessOffsetYCase[0];
            double botOffset = skewnessOffsetYCase[1];

            if( topLeftX < botLeftX){

                angleTop = topOffset + 90;
                angleBot = botOffset - (lawOfCosinesAngle+pythagorasAngle);

                topRightDestination = calculateIntersectionUpSide(positionTopLeft,angleTop,LENGTH_SHORT_EDGE);
                botRightDestination = calculateIntersectionUpSide(positionBotLeft,angleBot,LENGTH_LONG_EDGE);
            }
            else if ( topLeftX > botLeftX){
                angleTop = topOffset - 90;
                angleBot = botOffset + (lawOfCosinesAngle+pythagorasAngle);

                topRightDestination = calculateIntersectionUpSide(positionTopLeft,angleTop,LENGTH_SHORT_EDGE);
                botRightDestination = calculateIntersectionUpSide(positionBotLeft,angleBot,LENGTH_LONG_EDGE);
            }
        }
        else{
            double[] skewnessOffset = this.offsetSkewnessRightAngeledUp(topLeft,botLeft);

            angleTop = 0 + skewnessOffset[0];
            angleBot = 90-lawOfCosinesAngle-pythagorasAngle + skewnessOffset[1];

            topRightDestination = calculateIntersectionUpSide(positionTopLeft,angleTop,LENGTH_SHORT_EDGE);
            botRightDestination = calculateIntersectionUpSide(positionBotLeft,angleBot,LENGTH_LONG_EDGE);
        }




        this.basicGraph.moveNode(topRight,topRightDestination);
        this.basicGraph.moveNode(botRight,botRightDestination);

    }

    private void caseKKLK(Node topLeft,Node botLeft,Node topRight,Node botRight){
        YPoint positionTopLeft = this.graph2D.getCenter(topLeft);
        YPoint positionBotLeft = this.graph2D.getCenter(botLeft);


        double lawOfCosinesAngle = Math.toDegrees(Math.acos( (5*Math.sqrt(2)) / 8));
        double pythagorasAngle = Math.toDegrees(Math.atan(1));

        double skewnessOffset = this.skewnessOffset(topLeft,botLeft);

        double topLeftX = this.graph2D.getCenterX(topLeft);
        double topLeftY = this.graph2D.getCenterY(topLeft);
        double botLeftX = this.graph2D.getCenterX(botLeft);
        double botLeftY = this.graph2D.getCenterY(botLeft);

        double angleTop;
        double angleBot;
        YPoint topRightDestination = null;
        YPoint botRightDestination = null;


        if( botLeftY < topLeftY){

            double[] skewnessOffsetYCase = skewnessOffsetYCase(topLeft,botLeft);

            double topOffset = skewnessOffsetYCase[0];
            double botOffset = skewnessOffsetYCase[1];

            if( topLeftX < botLeftX){

                angleTop = topOffset + 90;
                angleBot =  botOffset - (90-(pythagorasAngle - lawOfCosinesAngle)) ;

                topRightDestination = calculateIntersectionUpSide(positionTopLeft, angleTop, LENGTH_SHORT_EDGE);
                botRightDestination = calculateIntersectionUpSide(positionBotLeft, angleBot, LENGTH_LONG_EDGE);

            } else if ( topLeftX > botLeftX){

                angleTop = topOffset - 90;
                angleBot = botOffset + (90-(pythagorasAngle - lawOfCosinesAngle));

                topRightDestination = calculateIntersectionUpSide(positionTopLeft, angleTop, LENGTH_SHORT_EDGE);
                botRightDestination = calculateIntersectionUpSide(positionBotLeft, angleBot, LENGTH_LONG_EDGE);
            } else{

                angleTop = 0 + skewnessOffset;
                angleBot = 90 - pythagorasAngle - lawOfCosinesAngle + skewnessOffset;
                topRightDestination = calculateIntersectionUpSide(positionTopLeft, angleTop, LENGTH_SHORT_EDGE);
                botRightDestination = calculateIntersectionUpSide(positionBotLeft, angleBot, LENGTH_LONG_EDGE);
            }

        }
        else {

            angleTop = 0 + skewnessOffset;
            angleBot = 90 - pythagorasAngle - lawOfCosinesAngle + skewnessOffset;
            topRightDestination = calculateIntersectionUpSide(positionTopLeft, angleTop, LENGTH_SHORT_EDGE);
            botRightDestination = calculateIntersectionUpSide(positionBotLeft, angleBot, LENGTH_LONG_EDGE);

        }

        this.basicGraph.moveNode(topRight, topRightDestination);
        this.basicGraph.moveNode(botRight, botRightDestination);

    }

    private void caseKLKK(Node topLeft,Node botLeft,Node topRight,Node botRight){
        YPoint positionTopLeft = this.graph2D.getCenter(topLeft);
        YPoint positionBotLeft = this.graph2D.getCenter(botLeft);

        double lawOfCosinesAngle = Math.toDegrees(Math.acos( (5*Math.sqrt(2)) / 8));
        double pythagorasAngle = Math.toDegrees(Math.atan(1));

        double skewnessOffset = this.skewnessOffset(topLeft,botLeft);

        double topLeftX = this.graph2D.getCenterX(topLeft);
        double topLeftY = this.graph2D.getCenterY(topLeft);
        double botLeftX = this.graph2D.getCenterX(botLeft);
        double botLeftY = this.graph2D.getCenterY(botLeft);

        double angleTop;
        double angleBot;
        YPoint topRightDestination = null;
        YPoint botRightDestination = null;

        if( botLeftY < topLeftY) {

            double[] skewnessOffsetYCase = skewnessOffsetYCase(topLeft, botLeft);

            double topOffset = skewnessOffsetYCase[0];
            double botOffset = skewnessOffsetYCase[1];

            if (topLeftX < botLeftX) {
                angleTop =   (90-pythagorasAngle-lawOfCosinesAngle) + topOffset;
                angleBot = botOffset - 90;
                topRightDestination = calculateIntersectionUpSide(positionTopLeft,angleTop,LENGTH_LONG_EDGE);
                botRightDestination = calculateIntersectionUpSide(positionBotLeft,angleBot,LENGTH_SHORT_EDGE);

            } else if (topLeftX > botLeftX) {
                angleTop = topOffset - (90-pythagorasAngle-lawOfCosinesAngle);
                angleBot = botOffset + 90;
                topRightDestination = calculateIntersectionUpSide(positionTopLeft,angleTop,LENGTH_LONG_EDGE);
                botRightDestination = calculateIntersectionUpSide(positionBotLeft,angleBot,LENGTH_SHORT_EDGE);
            }
        }
        else{
            angleTop = 90-pythagorasAngle-lawOfCosinesAngle - skewnessOffset;
            angleBot = skewnessOffset ;

            topRightDestination = calculateIntersectionDownSide(positionTopLeft,angleTop,LENGTH_LONG_EDGE);
            botRightDestination = calculateIntersectionUpSide(positionBotLeft,angleBot,LENGTH_SHORT_EDGE);
        }



        this.basicGraph.moveNode(topRight,topRightDestination);
        this.basicGraph.moveNode(botRight,botRightDestination);
    }

    private void caseLKLL(Node topLeft,Node botLeft,Node topRight,Node botRight){
        YPoint positionTopLeft = this.graph2D.getCenter(topLeft);
        YPoint positionBotLeft = this.graph2D.getCenter(botLeft);

        double lawOfCosinesAngle = Math.toDegrees(Math.acos( (5*Math.sqrt(2)) / 8));
        double pythagorasAngle = Math.toDegrees(Math.atan(1));

        double topLeftX = this.graph2D.getCenterX(topLeft);
        double topLeftY = this.graph2D.getCenterY(topLeft);
        double botLeftX = this.graph2D.getCenterX(botLeft);
        double botLeftY = this.graph2D.getCenterY(botLeft);

        double angleTop;
        double angleBot;
        YPoint topRightDestination = null;
        YPoint botRightDestination = null;

        if( botLeftY < topLeftY) {

            double[] skewnessOffsetYCase = skewnessOffsetYCase(topLeft, botLeft);

            double topOffset = skewnessOffsetYCase[0];
            double botOffset = skewnessOffsetYCase[1];

            if( topLeftX < botLeftX){

                angleTop =   (90-pythagorasAngle-lawOfCosinesAngle) + topOffset;
                angleBot = botOffset - 90;

                topRightDestination = calculateIntersectionUpSide(positionTopLeft,angleTop,LENGTH_SHORT_EDGE);
                botRightDestination = calculateIntersectionUpSide(positionBotLeft,angleBot,LENGTH_LONG_EDGE);

            }
            else if ( topLeftX > botLeftX){
                angleTop = topOffset - (90-pythagorasAngle-lawOfCosinesAngle);
                angleBot = botOffset + 90;
                topRightDestination = calculateIntersectionUpSide(positionTopLeft,angleTop,LENGTH_SHORT_EDGE);
                botRightDestination = calculateIntersectionUpSide(positionBotLeft,angleBot,LENGTH_LONG_EDGE);
            }

        }else{
            double skewnessOffset = this.skewnessOffset(topLeft,botLeft);

            angleTop = 90-pythagorasAngle-lawOfCosinesAngle - skewnessOffset;
            angleBot = skewnessOffset;

            topRightDestination = calculateIntersectionDownSide(positionTopLeft,angleTop,LENGTH_SHORT_EDGE);
            botRightDestination = calculateIntersectionUpSide(positionBotLeft,angleBot,LENGTH_LONG_EDGE);
        }




        this.basicGraph.moveNode(topRight,topRightDestination);
        this.basicGraph.moveNode(botRight,botRightDestination);
    }

    private void caseLLKL( Node topLeft,Node botLeft,Node topRight,Node botRight){
        YPoint positionTopLeft = this.graph2D.getCenter(topLeft);
        YPoint positionBotLeft = this.graph2D.getCenter(botLeft);

        double lawOfCosinesAngle = Math.toDegrees(Math.acos( (5*Math.sqrt(2)) / 8));
        double pythagorasAngle = Math.toDegrees(Math.atan(1));

        double topLeftX = this.graph2D.getCenterX(topLeft);
        double topLeftY = this.graph2D.getCenterY(topLeft);
        double botLeftX = this.graph2D.getCenterX(botLeft);
        double botLeftY = this.graph2D.getCenterY(botLeft);

        double angleTop;
        double angleBot;
        YPoint topRightDestination = null;
        YPoint botRightDestination = null;

        if( botLeftY < topLeftY) {

            double[] skewnessOffsetYCase = skewnessOffsetYCase(topLeft, botLeft);

            double topOffset = skewnessOffsetYCase[0];
            double botOffset = skewnessOffsetYCase[1];

            if( topLeftX < botLeftX){
                angleTop = topOffset + 90;
                angleBot = botOffset - (pythagorasAngle+lawOfCosinesAngle);

                topRightDestination = calculateIntersectionUpSide(positionTopLeft,angleTop,LENGTH_LONG_EDGE);
                botRightDestination = calculateIntersectionUpSide(positionBotLeft,angleBot,LENGTH_SHORT_EDGE);
            }
            else if ( topLeftX > botLeftX){

                angleTop = topOffset - 90;
                angleBot = botOffset + (pythagorasAngle+lawOfCosinesAngle);

                topRightDestination = calculateIntersectionUpSide(positionTopLeft,angleTop,LENGTH_LONG_EDGE);
                botRightDestination = calculateIntersectionUpSide(positionBotLeft,angleBot,LENGTH_SHORT_EDGE);
            }
        }
        else{
            double skewnessOffset = this.skewnessOffset(topLeft,botLeft);

            angleTop = 0 - skewnessOffset;
            angleBot = 90-pythagorasAngle-lawOfCosinesAngle + skewnessOffset;

            topRightDestination = calculateIntersectionDownSide(positionTopLeft,angleTop,LENGTH_LONG_EDGE);
            botRightDestination = calculateIntersectionUpSide(positionBotLeft,angleBot,LENGTH_SHORT_EDGE);
        }




        this.basicGraph.moveNode(topRight,topRightDestination);
        this.basicGraph.moveNode(botRight,botRightDestination);
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


    private final double rectangleSkewnessOffset(Node topLeft, Node botLeft ){

        double topLeftY = this.graph2D.getCenterY(topLeft);
        double botLeftY = this.graph2D.getCenterY(botLeft);

        if( topLeftY > botLeftY){
            return 90;
        }
        else{
            return 0;
        }


    }

    private double[] skewnessOffsetYCase( Node topLeft, Node botLeft  ){

        double topLeftX = this.graph2D.getCenterX(topLeft);
        double topLeftY = this.graph2D.getCenterY(topLeft);
        double botLeftX = this.graph2D.getCenterX(botLeft);
        double botLeftY = this.graph2D.getCenterY(botLeft);

        double distanceTopLeftYBotLeftY = topLeftY- botLeftY;
        double lengthTopLeftBotLeft = MetricCollection.euklideanDistanceR2(topLeftX,topLeftY,botLeftX,botLeftY);
        double offset = Math.toDegrees(Math.asin(distanceTopLeftYBotLeftY/lengthTopLeftBotLeft));

        double[] result = new double[2];

        if (topLeftX < botLeftX) {
            result[0] = offset;
            result[1] = 180+offset;
            return result;
        }
        else if(topLeftX > botLeftX) {
            result[0] = 180-offset;
            result[1] = -offset;
            return result;
        }
        else{
            return new double[]{0,0};
        }
    }

    private double skewnessOffset(Node topLeft, Node botLeft ){
        double topLeftX = this.graph2D.getCenterX(topLeft);
        double topLeftY = this.graph2D.getCenterY(topLeft);
        double botLeftX = this.graph2D.getCenterX(botLeft);
        double botLeftY = this.graph2D.getCenterY(botLeft);


        /*
            topLeft
                \
                 \
                   botLeft
         */
            if (topLeftX < botLeftX) {
                double distTopLeftBotLeft = MetricCollection.euklideanDistanceR2(topLeftX, topLeftY, botLeftX, botLeftY);
                double differenceXTopLeftBotLeft = botLeftX - topLeftX;

                double offset = 90 - Math.toDegrees(Math.acos(differenceXTopLeftBotLeft / distTopLeftBotLeft));

                return offset;
            }
        /*
                    topLeft
                   /
                  /
              botLeft
         */
            else if (topLeftX > botLeftX) {
                double distTopLeftBotLeft = MetricCollection.euklideanDistanceR2(topLeftX, topLeftY, botLeftX, botLeftY);
                double differenceXTopLeftBotLeft = topLeftX - botLeftX;

                double offset = -(90 - Math.toDegrees(Math.acos(differenceXTopLeftBotLeft / distTopLeftBotLeft)));

                return offset;
            } else {
                return 0;
            }


    }


    private double[] offsetSkewnessRightAngeledUp(Node topLeft, Node botLeft){
        double topLeftX = this.graph2D.getCenterX(topLeft);
        double topLeftY = this.graph2D.getCenterY(topLeft);
        double botLeftX = this.graph2D.getCenterX(botLeft);
        double botLeftY = this.graph2D.getCenterY(botLeft);
        double[] result = new double[2];

        /*
            topLeft
                \
                 \
                   botLeft
         */
        if( topLeftX < botLeftX){
            double distTopLeftBotLeft = MetricCollection.euklideanDistanceR2(topLeftX,topLeftY,botLeftX,botLeftY);
            double differenceXTopLeftBotLeft = botLeftX-topLeftX;

            double angleTopOffset = 90 - Math.toDegrees(Math.acos( differenceXTopLeftBotLeft/distTopLeftBotLeft ));
            double angleBotOffset = angleTopOffset;

            result[0] = angleTopOffset;
            result[1] = angleBotOffset;

            System.out.println("INCASE TOPLEFT X < BOTLEFT X");
            return result;
        }
        /*
                    topLeft
                   /
                  /
              botLeft
         */
        else if(topLeftX > botLeftX){
            double distTopLeftBotLeft = MetricCollection.euklideanDistanceR2(topLeftX,topLeftY,botLeftX,botLeftY);
            double differenceXTopLeftBotLeft = topLeftX-botLeftX;

            double angleTopOffset = -(90 - Math.toDegrees(Math.acos( differenceXTopLeftBotLeft/distTopLeftBotLeft )));
            double angleBotOffset = angleTopOffset;

            result[0] = angleTopOffset;
            result[1] = angleBotOffset;
            return result;
        }
        else{
            return new double[]{0,0};
        }
    }




    @Override
    public boolean isGraphType() {
        return false;
    }
}
