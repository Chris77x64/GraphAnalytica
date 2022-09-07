package realizations;

import view.GraphView;
import y.base.Edge;
import y.base.Node;
import y.base.NodeList;
import y.geom.YPoint;

import java.util.ArrayList;

import static y.util.Generics.nodes;

/**
 * Created by chris on 24.06.2018.
 */
public class RealizationSunflowerGraph extends Realization {

    private final double LENGTH_LONG_EDGES = 300;
    private final double LENGTH_SHORT_EDGES = 0.95 * LENGTH_LONG_EDGES;

    private final YPoint startPoint = new YPoint(0,0);

    private Node centralVertex;

    private NodeList outerCycle;
    private NodeList k3Nodes;

    private YPoint s1;
    private YPoint s2;
    private YPoint s3;
    private YPoint s4;
    private YPoint s5;
    private YPoint s6;
    private YPoint s7;
    private YPoint s8;

    private YPoint l1;
    private YPoint l2;
    private YPoint l3;
    private YPoint l4;
    private YPoint l5;
    private YPoint l6;
    private YPoint l7;
    private YPoint l8;

    private ArrayList<Edge> longEdges;

    public RealizationSunflowerGraph(GraphView graphView) {
        super(graphView);

        this.calculateVertexOrder();
        this.initializeRealizationSpots();
        this.initializeAssets();
        this.calculateRealization();

        this.graphView.fitContent();
        this.basicGraph.updateViews();
            }

    private void calculateRealization(){
        String currentCase = distinguishCase();

        switch (currentCase){
            case "CASE1":{
               this.realizationCase1();
               this.realizeK3Sequence();
                break;
            }
            case "CASE2":{
                this.realizationCase2();
                this.realizeK3Sequence();
                break;
            }
            case "CASE3":{
                this.shiftOuterCycle();
                this.realizationCase3();
                this.realizeK3Sequence();
                break;
            }
        }
    }


    private void shiftOuterCycle(){

        boolean flag = false;

        int index = Integer.MIN_VALUE;

        for( int i=0; i < outerCycle.size(); i++){
            Node currentNode = (Node) outerCycle.get(i);
            Node next;

            if( i+1== outerCycle.size()){
                next = (Node) outerCycle.get(0);
            }
            else{
                next = (Node) outerCycle.get(i+1);
            }

            Edge currentEdge = currentNode.getEdge(next);

            if( !longEdges.contains(currentEdge)){

                if( i+1 == outerCycle.size()){
                    Node second = (Node) outerCycle.get(1);
                    Edge firstSecond = next.getEdge(second);
                    if( !longEdges.contains(firstSecond)){
                        index = 1;
                        break;
                    }
                }

                if( flag){
                    index = i+1;
                    break;
                }
                else{
                    flag = true;
                }

            }
            else if( longEdges.contains(currentEdge) && flag){
                flag = false;
            }
        }


        NodeList newOuterCycle = new NodeList();

        for( int i = index; i < outerCycle.size(); i++){
            Node currentNode = (Node) outerCycle.get(i);
            newOuterCycle.add(currentNode);
        }

        for( int i= 0 ; i < index; i++){
            Node currentNode = (Node) outerCycle.get(i);
            newOuterCycle.add(currentNode);
        }
        outerCycle = newOuterCycle;

    }

    private Node getSecondLastLongEdgeNode(){

        boolean lastLongEdgeDiscovered = false;

        NodeList nodes = new NodeList();
        nodes.addAll(outerCycle);
        nodes.addLast(nodes.firstNode());

        for( int i=nodes.size()-1 ; i >= 0 ; i--){

            Node currentNode = (Node) nodes.get(i);
            Node previousNode = (Node) nodes.get(i-1);

            Edge currentCircleEdge = currentNode.getEdge(previousNode);

            if( longEdges.contains(currentCircleEdge)){
                if( lastLongEdgeDiscovered){
                    return previousNode;
                }
                else{
                    lastLongEdgeDiscovered = true;
                }
            }
        }
        return null;
    }

    private void realizeK3Sequence(){
        for( int i=0; i < k3Nodes.size(); i++){
            Node currentNode = (Node) k3Nodes.get(i);

            NodeList neighbours = new NodeList(currentNode.neighbors());

            Node neighbour1 = (Node) neighbours.get(0);
            Node neighbour2 = (Node) neighbours.get(1);


            YPoint positionN1 = graph2D.getCenter(neighbour1);
            YPoint positionN2 = graph2D.getCenter(neighbour2);

            Edge edge1 = currentNode.getEdge(neighbour1);
            Edge edge2 = currentNode.getEdge(neighbour2);

            YPoint destination;

            if( !longEdges.contains(edge1) && !longEdges.contains(edge2)){
                destination = farthestCircleCircleIntersectionPointToRelativePoint(positionN1,LENGTH_SHORT_EDGES,positionN2,LENGTH_SHORT_EDGES,startPoint);
            }
            else if( longEdges.contains(edge1) && !longEdges.contains(edge2)){
                destination = farthestCircleCircleIntersectionPointToRelativePoint(positionN1,LENGTH_LONG_EDGES,positionN2,LENGTH_SHORT_EDGES,startPoint);
            }
            else if( !longEdges.contains(edge1) && longEdges.contains(edge2)){
                destination = farthestCircleCircleIntersectionPointToRelativePoint(positionN1,LENGTH_SHORT_EDGES,positionN2,LENGTH_LONG_EDGES,startPoint);
            }
            else{
                destination = farthestCircleCircleIntersectionPointToRelativePoint(positionN1,LENGTH_LONG_EDGES,positionN2,LENGTH_LONG_EDGES,startPoint);
            }

            basicGraph.moveNode(currentNode,destination);
        }
    }

    private void realizationCase1(){

        Node firstCycleVertex = (Node) outerCycle.get(0);
        Edge centerFirst = centralVertex.getEdge(firstCycleVertex);

        basicGraph.moveNode(centralVertex,startPoint);

        if( longEdges.contains(centerFirst)){
            basicGraph.moveNode(firstCycleVertex,l1);
        }
        else{
            basicGraph.moveNode(firstCycleVertex,s1);
        }

        for( int i=1; i < outerCycle.size(); i++){

            Node currentVertex = (Node) outerCycle.get(i);

            Node previousVertex = (Node) outerCycle.get(i-1);
            YPoint positionPreviousVertex = graph2D.getCenter(previousVertex);

            Edge currentPrevious = currentVertex.getEdge(previousVertex);
            Edge currentCenter = currentVertex.getEdge(centralVertex);

            YPoint destination = null;

            if( !longEdges.contains(currentPrevious) && !longEdges.contains(currentCenter)){
                destination = case1Transition1(positionPreviousVertex);
            }
            else if( !longEdges.contains(currentPrevious) && longEdges.contains(currentCenter)){
                destination = case1Transition2(positionPreviousVertex);
            }
            else if( longEdges.contains(currentPrevious) && !longEdges.contains(currentCenter)){
                destination = case1Transition3(positionPreviousVertex);
            }
            else{
                destination = case1Transition4(positionPreviousVertex);
            }

            basicGraph.moveNode(currentVertex,destination);

        }

    }

    private void realizationCase2(){

        Node bound = getSecondLastLongEdgeNode();
        int boundIndex = outerCycle.indexOf(bound);

        Node firstCycleVertex = (Node) outerCycle.get(0);
        Edge centerFirst = centralVertex.getEdge(firstCycleVertex);

        basicGraph.moveNode(centralVertex,startPoint);

        if( longEdges.contains(centerFirst)){
            basicGraph.moveNode(firstCycleVertex,l1);
        }
        else{
            basicGraph.moveNode(firstCycleVertex,s1);
        }

        for( int i=1; i < boundIndex+1; i++){

            Node currentVertex = (Node) outerCycle.get(i);

            Node previousVertex = (Node) outerCycle.get(i-1);
            YPoint positionPreviousVertex = graph2D.getCenter(previousVertex);

            Edge currentPrevious = currentVertex.getEdge(previousVertex);
            Edge currentCenter = currentVertex.getEdge(centralVertex);

            YPoint destination = null;

            if( !longEdges.contains(currentPrevious) && !longEdges.contains(currentCenter)){
                destination = case1Transition1(positionPreviousVertex);
            }
            else if( !longEdges.contains(currentPrevious) && longEdges.contains(currentCenter)){
                destination = case1Transition2(positionPreviousVertex);
            }
            else if( longEdges.contains(currentPrevious) && !longEdges.contains(currentCenter)){
                destination = case1Transition3(positionPreviousVertex);
            }
            else{
                destination = case1Transition4(positionPreviousVertex);
            }

            basicGraph.moveNode(currentVertex,destination);
        }

        Node afterBound = (Node) outerCycle.get(boundIndex+1);
        Edge centerAfterBound = afterBound.getEdge(centralVertex);

        if( longEdges.contains(centerAfterBound)){
            basicGraph.moveNode(afterBound,l5);
        }
        else{
            basicGraph.moveNode(afterBound,s5);
        }

        for( int i=boundIndex+2; i < outerCycle.size(); i++){

            Node currentVertex = (Node) outerCycle.get(i);

            Node previousVertex = (Node) outerCycle.get(i-1);
            YPoint positionPreviousVertex = graph2D.getCenter(previousVertex);

            Edge currentPrevious = currentVertex.getEdge(previousVertex);
            Edge currentCenter = currentVertex.getEdge(centralVertex);

            YPoint destination = null;

            if( !longEdges.contains(currentPrevious) && !longEdges.contains(currentCenter)){
                destination = case2Transition1(positionPreviousVertex);
            }
            else if( !longEdges.contains(currentPrevious) && longEdges.contains(currentCenter)){
                destination = case2Transition2(positionPreviousVertex);
            }
            else if( longEdges.contains(currentPrevious) && !longEdges.contains(currentCenter)){
                destination = case2Transition3(positionPreviousVertex);
            }
            else{
                destination = case2Transition4(positionPreviousVertex);
            }

            basicGraph.moveNode(currentVertex,destination);
        }


    }


    private void realizationCase3(){

        Node firstCycleVertex = (Node) outerCycle.get(0);
        Edge centerFirst = centralVertex.getEdge(firstCycleVertex);

        basicGraph.moveNode(centralVertex,startPoint);

        if( longEdges.contains(centerFirst)){
            basicGraph.moveNode(firstCycleVertex,l1);
        }
        else{
            basicGraph.moveNode(firstCycleVertex,s1);
        }

        for( int i=1; i < outerCycle.size()-1; i++){

            Node currentVertex = (Node) outerCycle.get(i);

            Node previousVertex = (Node) outerCycle.get(i-1);
            YPoint positionPreviousVertex = graph2D.getCenter(previousVertex);

            Edge currentPrevious = currentVertex.getEdge(previousVertex);
            Edge currentCenter = currentVertex.getEdge(centralVertex);

            YPoint destination = null;

            if( !longEdges.contains(currentPrevious) && !longEdges.contains(currentCenter)){
                destination = case3Transition1(positionPreviousVertex);
            }
            else if( !longEdges.contains(currentPrevious) && longEdges.contains(currentCenter)){
                destination = case3Transition2(positionPreviousVertex);
            }
            else if( longEdges.contains(currentPrevious) && !longEdges.contains(currentCenter)){
                destination = case3Transition3(positionPreviousVertex);
            }
            else{
                destination = case3Transition4(positionPreviousVertex);
            }

            basicGraph.moveNode(currentVertex,destination);
        }

        Node lastCycleNode = (Node) outerCycle.get(outerCycle.size()-1);
        Edge lastCenter = lastCycleNode.getEdge(centralVertex);

        if( longEdges.contains(lastCenter)){
            basicGraph.moveNode(lastCycleNode,l7);
        }
        else{
            basicGraph.moveNode(lastCycleNode,s7);
        }
    }

    private YPoint case3Transition1( YPoint previous){
        if( pointsEqual(previous,s1) || pointsEqual(previous,l1)){
            return s2;
        }
        else if( pointsEqual(previous,s2) || pointsEqual(previous,l2)){
            return s1;
        }
        else if(pointsEqual(previous,s3) || pointsEqual(previous,l3)){
            return s8;
        }
        else if( pointsEqual(previous,s8) || pointsEqual(previous,l8)){
            return s3;
        }
        return null;
    }

    private YPoint case3Transition2( YPoint previous){
        if( pointsEqual(previous,s1) || pointsEqual(previous,l1)){
            return l2;
        }
        else if( pointsEqual(previous,s2) || pointsEqual(previous,l2)){
            return l1;
        }
        else if(pointsEqual(previous,s3) || pointsEqual(previous,l3)){
            return l8;
        }
        else if( pointsEqual(previous,s8) || pointsEqual(previous,l8)){
            return l3;
        }
        return null;
    }

    private YPoint case3Transition3( YPoint previous){
        if( pointsEqual(previous,s1) || pointsEqual(previous,l1)){
            return s3;
        }
        else if( pointsEqual(previous,s2) || pointsEqual(previous,l2)){
            return s3;
        }
        return null;
    }
    private YPoint case3Transition4( YPoint previous){
        if( pointsEqual(previous,s1) || pointsEqual(previous,l1)){
            return l3;
        }
        else if( pointsEqual(previous,s2) || pointsEqual(previous,l2)){
            return l3;
        }
        return null;
    }

    private YPoint case1Transition1( YPoint previous){
        if( pointsEqual(previous,s1) || pointsEqual(previous,l1)){
            return s2;
        }
        else if( pointsEqual(previous,s2) || pointsEqual(previous,l2)){
            return s1;
        }
        else if(pointsEqual(previous,s3) || pointsEqual(previous,l3)){
            return s4;
        }
        else if( pointsEqual(previous,s4) || pointsEqual(previous,l4)){
            return s3;
        }
        return null;
    }

    private YPoint case2Transition1( YPoint previous){
        if( pointsEqual(previous,s5) || pointsEqual(previous,l5)){
            return s6;
        }
        else if( pointsEqual(previous,s6) || pointsEqual(previous,l6)){
            return s5;
        }
        else if(pointsEqual(previous,s1) || pointsEqual(previous,l1)){
            return s2;
        }
        else if( pointsEqual(previous,s2) || pointsEqual(previous,l2)){
            return s1;
        }
        return null;
    }


    private YPoint case1Transition2( YPoint previous){
        if( pointsEqual(previous,s1) || pointsEqual(previous,l1)){
            return l2;
        }
        else if( pointsEqual(previous,s2) || pointsEqual(previous,l2)){
            return l1;
        }
        else if(pointsEqual(previous,s3) || pointsEqual(previous,l3)){
            return l4;
        }
        else if( pointsEqual(previous,s4) || pointsEqual(previous,l4)){
            return l3;
        }
        return null;
    }

    private YPoint case2Transition2( YPoint previous){
        if( pointsEqual(previous,s5) || pointsEqual(previous,l5)){
            return l6;
        }
        else if( pointsEqual(previous,s6) || pointsEqual(previous,l6)){
            return l5;
        }
        else if(pointsEqual(previous,s1) || pointsEqual(previous,l1)){
            return l2;
        }
        else if( pointsEqual(previous,s2) || pointsEqual(previous,l2)){
            return l1;
        }
        return null;
    }

    private YPoint case1Transition3( YPoint previous){
        if( pointsEqual(previous,s1) || pointsEqual(previous,l1)){
            return s4;
        }
        else if( pointsEqual(previous,s2) || pointsEqual(previous,l2)){
            return s4;
        }
        else if(pointsEqual(previous,s3) || pointsEqual(previous,l3)){
            return s1;
        }
        else if( pointsEqual(previous,s4) || pointsEqual(previous,l4)){
            return s1;
        }
        return null;
    }

    private YPoint case2Transition3( YPoint previous){
        if( pointsEqual(previous,s5) || pointsEqual(previous,l5)){
            return s1;
        }
        else if( pointsEqual(previous,s6) || pointsEqual(previous,l6)){
            return s1;
        }
        return null;
    }


    private YPoint case1Transition4( YPoint previous){
        if( pointsEqual(previous,s1) || pointsEqual(previous,l1)){
            return l4;
        }
        else if( pointsEqual(previous,s2) || pointsEqual(previous,l2)){
            return l4;
        }
        else if(pointsEqual(previous,s3) || pointsEqual(previous,l3)){
            return l1;
        }
        else if( pointsEqual(previous,s4) || pointsEqual(previous,l4)){
            return l1;
        }
        return null;
    }

    private YPoint case2Transition4( YPoint previous){
        if( pointsEqual(previous,s5) || pointsEqual(previous,l5)){
            return l1;
        }
        else if( pointsEqual(previous,s6) || pointsEqual(previous,l6)){
            return l1;
        }
        return null;
    }

    private void initializeRealizationSpots(){
        s1 = calculateIntersectionUpSide(startPoint,0,LENGTH_SHORT_EDGES);
        s2 = calculateIntersectionUpSide(startPoint,20,LENGTH_SHORT_EDGES);
        s3 = calculateIntersectionUpSide(startPoint,100,LENGTH_SHORT_EDGES);
        s4 = calculateIntersectionUpSide(startPoint,120,LENGTH_SHORT_EDGES);
        s5 = calculateIntersectionDownSide(startPoint,120,LENGTH_SHORT_EDGES);
        s6 = calculateIntersectionDownSide(startPoint,100,LENGTH_SHORT_EDGES);

        l1 = calculateIntersectionUpSide(startPoint,0,LENGTH_LONG_EDGES);
        l2 = calculateIntersectionUpSide(startPoint,20,LENGTH_LONG_EDGES);
        l3 = calculateIntersectionUpSide(startPoint,100,LENGTH_LONG_EDGES);
        l4 = calculateIntersectionUpSide(startPoint,120,LENGTH_LONG_EDGES);
        l5 = calculateIntersectionDownSide(startPoint,120,LENGTH_LONG_EDGES);
        l6 = calculateIntersectionDownSide(startPoint,100,LENGTH_LONG_EDGES);

        s7 = calculateIntersectionUpSide(startPoint,50,LENGTH_SHORT_EDGES);
        l7 = calculateIntersectionUpSide(startPoint,50,LENGTH_LONG_EDGES);

        s8 = calculateIntersectionUpSide(startPoint,80,LENGTH_SHORT_EDGES);
        l8 = calculateIntersectionUpSide(startPoint,80,LENGTH_LONG_EDGES);

    }



    private String distinguishCase(){

        int numLongEdges = 0;

        for( int i=0; i < outerCycle.size(); i++){
            Node current = (Node) outerCycle.get(i);
            Node next;

            if( i+1 == outerCycle.size()){
                next = (Node) outerCycle.get(0);
            }
            else{
                next = (Node) outerCycle.get(i+1);
            }

            Edge currentEdge = current.getEdge(next);

            if( longEdges.contains(currentEdge)){
                numLongEdges++;
            }
        }

        if( numLongEdges % 2 == 0){
            return "CASE1";
        }
        else if( numLongEdges % 2 == 1 && numLongEdges > 1){
            return "CASE2";
        }
        else{
            return "CASE3";
        }

    }





    private void calculateVertexOrder(){

        this.outerCycle = new NodeList();
        this.k3Nodes = new NodeList();

        int maxDegree = (graph2D.nodeCount()-1)/2;


        if( maxDegree !=5) {
            for (Node node : nodes(graph2D)) {
                if (node.degree() == maxDegree) {
                    centralVertex = node;
                } else if (node.degree() == 2) {
                    k3Nodes.add(node);
                } else {
                    outerCycle.add(node);
                }
            }
        }
        else{
            for (Node node : nodes(graph2D)) {
                if (node.degree() == 2) {
                    k3Nodes.add(node);
                }
            }
            for (Node node : nodes(graph2D)) {

                if( node.degree() !=2) {

                    NodeList neighbors = new NodeList(node.neighbors());

                    boolean flag = true;

                    for( int j=0; j < neighbors.size(); j++){
                         Node currentNeighbor = (Node) neighbors.get(j);
                        if( k3Nodes.contains(currentNeighbor)){
                            flag = false;
                            break;
                        }
                    }

                    if( flag){
                        centralVertex = node;
                    }
                    else{
                        outerCycle.add(node);
                    }
                }
            }
        }
        Node start = (Node) outerCycle.get(0);
        outerCycleWorker(start,new NodeList());
    }

    private void outerCycleWorker(Node current,NodeList visited){

        NodeList neighbors = new NodeList(current.neighbors());
        neighbors.remove(centralVertex);
        neighbors.removeAll(k3Nodes);
        neighbors.removeAll(visited);

        visited.add(current);


        if( !neighbors.isEmpty()){
            Node next = (Node) neighbors.get(0);
            outerCycleWorker(next,visited);
        }
        else{
            this.outerCycle = visited;
        }
    }


    @Override
    public boolean isGraphType() {
        return false;
    }

    private void initializeAssets(){
        this.longEdges = graphView.getRelationships().getEdgesFromPartition(2);
    }
}
