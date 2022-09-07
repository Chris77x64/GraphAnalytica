package realizations;

import algo.KColoring;
import algo.LongestPathTree;
import model.EdgeInducedSubgraph;
import model.Relationships;
import view.GraphView;
import view.error.RealizationErrorView;
import y.algo.GraphChecker;
import y.base.*;
import y.geom.YPoint;
import y.util.GraphCopier;

import java.util.ArrayList;

import static y.algo.Trees.directTree;

/**
 * Created by chris on 03.01.2018.
 */
public class RealizationCaterpillarGraph extends Realization{

    private final double OFFSET_CLOSED_NEIGHBOURHOOD = 10;

    private double LENGTH_SHORT_EDGES;

    private NodeList spineVertices;
    private NodeList leafVertices;

    private YPoint centerPoint;

    private double gamma;

    private EdgeInducedSubgraph edgeInducedSubgraph;
    private Graph inducedSubgraphShortEdges;

    public RealizationCaterpillarGraph(GraphView graphView) {
        super(graphView);
        this.initializeInducedSubgraph();

        if( isGraphType()) {
            this.initializeSpineVertices();
            this.initializeLeafVertices();

            this.centerPoint = initializeCenterPoint();

            this.placeSpineVertices();
            this.placeLeafVertices();


            /*
            double worldHeight = this.graphView.getWorldRect2D().getHeight();
            double worldWidth = this.graphView.getWorldRect2D().getWidth();

            Rectangle worldBoundingBox = new Rectangle(20,20,(int) worldWidth-20, (int) worldHeight-20);
            GraphTransformer graphTransformer = new GraphTransformer();
            graphTransformer.scaleToRect(this.graph2D,worldBoundingBox);
            */


            //this.graphView.fitContent();

            this.graphView.fitContent();
            this.basicGraph.updateViews();
        }
        else{
            System.out.println("UNGÜLTIGER TYP");
        }

    }

    private YPoint initializeCenterPoint(){
        double worldHeight = this.graphView.getWorldRect2D().getHeight();
        double worldWidth = this.graphView.getWorldRect2D().getWidth();
        return new YPoint(worldWidth/2,worldHeight/2);
    }


    private void initializeInducedSubgraph(){
        ArrayList<Edge> shortEdges = this.graphView.getRelationships().getPartitions().get(1);

        this.edgeInducedSubgraph = new EdgeInducedSubgraph(this.graph2D,shortEdges);
        this.inducedSubgraphShortEdges = edgeInducedSubgraph.getInducedSubgraph();

    }

    private void initializeSpineVertices(){


        LongestPathTree longestPathTree = new LongestPathTree(this.inducedSubgraphShortEdges);
        NodeList inducedPath = longestPathTree.getLongestPath();

        this.spineVertices = new NodeList();
        for( NodeCursor v = inducedPath.nodes(); v.ok() ; v.next()){
            Node inducedNode = v.node();
            Node originalNode = edgeInducedSubgraph.getGraphNodeByInducedNode(inducedNode);
            this.spineVertices.add(originalNode);
        }

        this.spineVertices.reverse();
        this.spineVertices.popNode();
        this.spineVertices.popLast();


    }

    private void initializeLeafVertices(){

        this.leafVertices = new NodeList(this.graph2D.nodes());
        leafVertices.removeAll(this.spineVertices);

    }


    private void placeSpineVertices(){


        double worldWidth = this.graphView.getWorldRect2D().getWidth();
        this.LENGTH_SHORT_EDGES = 200;

        double n = this.spineVertices.size();
        this.gamma = Math.min(3.3,(1/n)*60);


        double beta = 2*(1.5 * gamma);
        System.out.println("2 BETA: "+beta);

        int middleIndex;

        if( n % 2 == 0){
            middleIndex = (int) (n/2) -1 ;
        }
        else{
            middleIndex = (int) Math.floor( n/2d );
        }
        System.out.println("MEDIAN INDEX: "+middleIndex);

        Node startNode = (Node) spineVertices.get(middleIndex);
        this.basicGraph.moveNode(startNode,centerPoint);

        double startAngle = 1.5*gamma;


        for( int i = middleIndex+1 ; i < n ; i++){
            Node currentNode = (Node) spineVertices.get(i);
            YPoint destination = this.calculateIntersectionUpSide(this.centerPoint,startAngle,LENGTH_SHORT_EDGES);
            this.basicGraph.moveNode(currentNode,destination);
            startAngle += 3*gamma;
            this.centerPoint = destination;
        }

        startAngle = 180- (1.5*this.gamma);
        this.centerPoint = initializeCenterPoint();
        for( int i= middleIndex-1; i >= 0; i--){
            Node currentNode = (Node) spineVertices.get(i);
            YPoint destination = this.calculateIntersectionUpSide(this.centerPoint,startAngle,LENGTH_SHORT_EDGES);
            this.basicGraph.moveNode(currentNode,destination);
            startAngle -= 3*gamma;
            this.centerPoint = destination;
        }
    }


    private void placeLeafVertices() {

        int middleIndex;
        double n = this.spineVertices.size();
        if( n % 2 == 0){
            middleIndex = (int) (n/2) -1 ;
        }
        else{
            middleIndex = (int) Math.floor( n/2d );
        }

        System.out.println("SPINE VERTICES: "+spineVertices);
        for (int i = 0; i < spineVertices.size(); i++) {

            Node currentNode = (Node) spineVertices.get(i);

            YPoint currentPosition = this.graph2D.getCenter(currentNode);
            YPoint[] realizationSpots;

            if( i== middleIndex){
                realizationSpots = generateRealizationSpotsMiddle(currentPosition);
            }
            else if( i > middleIndex){
                int offset = i-middleIndex;
                realizationSpots = generateRealizationSpotsRight(currentPosition,offset);
            }
            else{
                int offset = middleIndex - i;
                realizationSpots = generateRealizationSpotsLeft(currentPosition,offset);
            }


            int offsetSpot1 = 0;
            int offsetSpot2 = 0;
            int offsetSpot3 = 0;

            Node inducedNode = this.edgeInducedSubgraph.getInducedNodeByGraphNode(currentNode);

            NodeList leavesToPlace = new NodeList();

            for( NodeCursor v = inducedNode.neighbors(); v.ok() ; v.next()){
                Node inducedCurrentNode = v.node();
                Node neighbour = this.edgeInducedSubgraph.getGraphNodeByInducedNode(inducedCurrentNode);
                leavesToPlace.add(neighbour);
            }
            leavesToPlace.removeAll(this.spineVertices);

            if (i == 0) {
                Node nextNode = (Node) spineVertices.get(i + 1);

                for (NodeCursor v = leavesToPlace.nodes(); v.ok(); v.next()) {

                    Node currentLeafNode = v.node();


                        boolean existsEdgeNextNodeLeaf = this.graph2D.containsEdge(nextNode, currentLeafNode) || this.graph2D.containsEdge(currentLeafNode,nextNode) ;

                        if (existsEdgeNextNodeLeaf) {

                            YPoint destination = calculateIntersectionDownSide(realizationSpots[0], 90, offsetSpot1);
                            this.basicGraph.moveNode(currentLeafNode, destination);
                            //offsetSpot1 += OFFSET_CLOSED_NEIGHBOURHOOD;
                        } else {
                            YPoint destination;
                            if( i % 2 == 0) {
                                destination = calculateIntersectionUpSide(realizationSpots[2], 90, offsetSpot3);
                            }
                            else{
                                destination = calculateIntersectionUpSide(realizationSpots[3], 90, offsetSpot3);
                            }
                            this.basicGraph.moveNode(currentLeafNode, destination);
                            //offsetSpot3 += OFFSET_CLOSED_NEIGHBOURHOOD;
                        }



                }
            } else if ( (i + 1) < spineVertices.size() && (i - 1) >= 0) {

                Node nextNode = (Node) spineVertices.get(i + 1);
                Node previousNode = (Node) spineVertices.get(i - 1);


                for (NodeCursor v = leavesToPlace.nodes(); v.ok(); v.next()) {

                    Node currentLeafNode = v.node();

                        boolean existsEdgePreviousNodeLeaf = this.graph2D.containsEdge(previousNode, currentLeafNode) || this.graph2D.containsEdge(currentLeafNode,previousNode);
                        boolean existsEdgeNextNodeLeaf = this.graph2D.containsEdge(nextNode, currentLeafNode) || this.graph2D.containsEdge(currentLeafNode,nextNode) ;

                        System.out.println("NEXTNODELEAVE: "+existsEdgeNextNodeLeaf+" at leafnode: "+currentLeafNode.index());
                        System.out.println("PREVIOUSNODELEAVE" + existsEdgePreviousNodeLeaf);

                        if (existsEdgeNextNodeLeaf && !existsEdgePreviousNodeLeaf) {

                            YPoint destination = calculateIntersectionDownSide(realizationSpots[0], 90, offsetSpot1);
                            this.basicGraph.moveNode(currentLeafNode, destination);
                            //offsetSpot1 += OFFSET_CLOSED_NEIGHBOURHOOD;

                        } else if (!existsEdgeNextNodeLeaf && existsEdgePreviousNodeLeaf) {

                            YPoint destination = calculateIntersectionDownSide(realizationSpots[1], 90, offsetSpot2);
                            this.basicGraph.moveNode(currentLeafNode, destination);
                            //offsetSpot2 += OFFSET_CLOSED_NEIGHBOURHOOD;
                        } else if (existsEdgeNextNodeLeaf && existsEdgePreviousNodeLeaf) {
                            System.out.println("INCASE");
                            YPoint destination;
                            if( i % 2 == 0) {
                                destination = calculateIntersectionUpSide(realizationSpots[2], 90, offsetSpot3);
                            }
                            else{
                                destination = calculateIntersectionUpSide(realizationSpots[3], 90, offsetSpot3);
                            }
                            System.out.println(realizationSpots);
                            System.out.println("LOCATION: "+destination);
                            this.basicGraph.moveNode(currentLeafNode, destination);
                            //offsetSpot3 += OFFSET_CLOSED_NEIGHBOURHOOD;
                        } else {
                            YPoint destination;
                            if( i % 2 == 0) {
                                destination = calculateIntersectionUpSide(realizationSpots[2], 90, offsetSpot3);
                            }
                            else{
                                destination = calculateIntersectionUpSide(realizationSpots[3], 90, offsetSpot3);
                            }
                            this.basicGraph.moveNode(currentLeafNode, destination);
                            //offsetSpot3 += OFFSET_CLOSED_NEIGHBOURHOOD;
                        }




                }

            } else {
                Node previousNode = (Node) spineVertices.get(i - 1);

                for (NodeCursor v = leavesToPlace.nodes(); v.ok(); v.next()) {

                    Node currentLeafNode = v.node();

                        boolean existsEdgePreviousNodeLeaf = this.graph2D.containsEdge(previousNode, currentLeafNode) || this.graph2D.containsEdge(currentLeafNode,previousNode);

                        if (existsEdgePreviousNodeLeaf) {

                            YPoint destination = calculateIntersectionDownSide(realizationSpots[1], 90, offsetSpot2);
                            this.basicGraph.moveNode(currentLeafNode, destination);
                            //offsetSpot2 += OFFSET_CLOSED_NEIGHBOURHOOD;
                        }
                        else{
                            YPoint destination;
                            if( i % 2 == 0) {
                                destination = calculateIntersectionUpSide(realizationSpots[2], 90, offsetSpot3);
                            }
                            else{
                                destination = calculateIntersectionUpSide(realizationSpots[3], 90, offsetSpot3);
                            }
                            this.basicGraph.moveNode(currentLeafNode, destination);
                            //offsetSpot3 += OFFSET_CLOSED_NEIGHBOURHOOD;
                        }



                }

            }

        }
    }

    private boolean isTree(){
        return GraphChecker.isTree(this.inducedSubgraphShortEdges);
    }


    private boolean isCaterpillar(){
        GraphCopier graphCopier = new GraphCopier(this.inducedSubgraphShortEdges.getGraphCopyFactory());
        Graph tempGraph = graphCopier.getCopyFactory().createGraph();

        NodeList pathVertices = new NodeList();

        for( NodeCursor v = inducedSubgraphShortEdges.nodes() ; v.ok() ; v.next() ){
            Node currentNode = v.node();
            if( currentNode.degree() > 1){
                pathVertices.add(currentNode);
            }
        }

        graphCopier.getCopyFactory().preCopyGraphData(inducedSubgraphShortEdges,tempGraph);
        graphCopier.copy(this.inducedSubgraphShortEdges,pathVertices.nodes(),tempGraph);

        LongestPathTree longestPathTree = new LongestPathTree(tempGraph);

        if(  longestPathTree.getLongestPath().size() == tempGraph.nodeCount()){
            return true;
        }
        else if( tempGraph.nodeCount() == 1){
            return true;
        }
        else{
            return false;
        }

    }



    private YPoint[] generateRealizationSpotsMiddle( YPoint start){

        YPoint[] result = new YPoint[4];
        result[0] = calculateIntersectionDownSide(start,60-(0.5*this.gamma),LENGTH_SHORT_EDGES);
        result[1] = calculateIntersectionDownSide(start,120+(0.5*this.gamma),LENGTH_SHORT_EDGES);
        result[2] = calculateIntersectionUpSide(start,90,LENGTH_SHORT_EDGES);
        result[3] = calculateIntersectionUpSide(start,90,0.5*LENGTH_SHORT_EDGES);

        return result;
    }

    private YPoint[] generateRealizationSpotsRight( YPoint start, int numRightVertex){

        YPoint[] result = new YPoint[4];
        double angle0 = 60-(0.5*this.gamma)-(3*numRightVertex*this.gamma);
        System.out.println("ANGLE0: "+angle0);
        result[0] = calculateIntersectionDownSide(start,angle0,LENGTH_SHORT_EDGES);
        result[1] = calculateIntersectionDownSide(start, angle0 + 60+this.gamma,LENGTH_SHORT_EDGES);
        double angle2 = 90 + (2*numRightVertex*(1.5*this.gamma));
        result[2] = calculateIntersectionUpSide(start,angle2,LENGTH_SHORT_EDGES);
        result[3] = calculateIntersectionUpSide(start,angle2,0.5*LENGTH_SHORT_EDGES);

        return result;
    }

    private YPoint[] generateRealizationSpotsLeft( YPoint start, int numLeftVertex){

        YPoint[] result = new YPoint[4];
        double angle0 = 60-(0.5*this.gamma)+(3*numLeftVertex*this.gamma);
        System.out.println("ANGLE0: "+angle0);
        result[0] = calculateIntersectionDownSide(start,angle0,LENGTH_SHORT_EDGES);
        result[1] = calculateIntersectionDownSide(start, angle0 + 60+this.gamma,LENGTH_SHORT_EDGES);
        double angle2 = 90 - (2*numLeftVertex*(1.5*this.gamma));
        result[2] = calculateIntersectionUpSide(start,angle2,LENGTH_SHORT_EDGES);
        result[3] = calculateIntersectionUpSide(start,angle2,0.5*LENGTH_SHORT_EDGES);

        return result;
    }


    private boolean isLong( Edge edge){
        Relationships relationships = this.graphView.getRelationships();
        int partition = relationships.getPartitionIndexEdge(edge);
        if( partition == 2){
            return true;
        }
        else{
            return false;
        }
    }

    private boolean isShort( Edge edge){
        Relationships relationships = this.graphView.getRelationships();
        int partition = relationships.getPartitionIndexEdge(edge);
        if( partition == 1){
            return true;
        }
        else{
            return false;
        }
    }



    @Override
    public boolean isGraphType() {
        if (isDichotomos()) {
            if (isTree()) {
                if (isCaterpillar()) {

                    KColoring coloring = new KColoring(graph2D,4,false);
                    boolean is4Colorable = coloring.isSuccesfull();
                    if( is4Colorable){
                        return true;
                    }
                    else{
                        RealizationErrorView error = new RealizationErrorView(RealizationError.ERROR_4_COLORING);
                        return false;
                    }

                } else {
                    RealizationErrorView error = new RealizationErrorView(RealizationError.ERROR_CATERPILLAR);
                    return false;
                }
            } else {
                RealizationErrorView error = new RealizationErrorView(RealizationError.ERROR_SUBGRAPH_INDUCED_SHORT_EDGES_IS_NOT_A_TREE);
                return false;
            }
        }
        else{
            RealizationErrorView error = new RealizationErrorView(RealizationError.ERROR_PARTITION);
            return false;
        }

    }

}
