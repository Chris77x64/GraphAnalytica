package algo;

import y.base.*;
import y.util.Maps;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by chris on 16.02.2018.
 */
public class PrismGraphCycles {

    private NodeMap cycleMapping;

    private NodeList vertexOrder;
    private NodeList startNodeNeighbors;

    private boolean abort;

    private Graph graph;

    public PrismGraphCycles(Graph graph) {
        this.graph = graph;
        this.abort = false;
        this.vertexOrder = new NodeList();

        int cycleLength = graph.nodeCount() / 2;
        Node startNode = graph.firstNode();
        this.startNodeNeighbors = new NodeList(startNode.neighbors());

        this.pathOfLength(cycleLength-1,startNode);
    }

    private ArrayList<Node> addVisitedNode(ArrayList<Node> parents, Node parent){


        ArrayList<Node> result = new ArrayList<>();
        result.addAll(parents);
        result.add(parent);
        return result;
    }

    private void pathOfLength(int length, Node startNode ){

        ArrayList<Node> parentList = new ArrayList<>();
        pathOfLengthWorker(startNode,length,parentList);

    }

    private boolean pathOfLengthWorker(Node currentNode, int length, ArrayList<Node> visited){

        String res= "";
        for( Node node: visited){
            res+= node;
        }
        // System.out.println(res);
        //CURRCYCLE: [0,8,6,4,2]

        if( visited.size() == length) {

            visited.add(currentNode);

            if( startNodeNeighbors.contains(currentNode)){
                NodeList cycle = new NodeList();
                cycle.addAll(visited);
                if( isOuterCycle(cycle)){
                    this.vertexOrder = prepareVertexOrder(cycle);
                    this.abort = true;
                }
            }

            return true;
        }

        NodeList neighbors = new NodeList(currentNode.neighbors());

        for(NodeCursor v = neighbors.nodes(); v.ok() ; v.next()){

            if( abort){
                break;
            }
            ArrayList<Node> newVisitedList = addVisitedNode(visited,currentNode);

            Node currentNeighbor = v.node();

            if( (!visited.contains(currentNeighbor) ) ){

                boolean result = pathOfLengthWorker(currentNeighbor,length,newVisitedList);

            }

        }

        return false;
    }

    private boolean isOuterCycle( NodeList innerCycle){

        this.cycleMapping = Maps.createHashedNodeMap();

        for(NodeCursor v = innerCycle.nodes() ; v.ok() ; v.next() ){
            Node currentNode = v.node();
            NodeList currentNeighbors = new NodeList(currentNode.neighbors());

            ArrayList<Node> distinctNeighbors = getDistinctNeighbors(innerCycle,currentNeighbors);
            if( distinctNeighbors.size() == 1){
                Node distinctNeighbor = distinctNeighbors.get(0);
                //  System.out.println("DISTINCT NEIGHBOR: "+distinctNeighbor+" INNERNODE: "+currentNode);
                cycleMapping.set(currentNode,distinctNeighbor);
            }
            else{
                return false;
            }

        }
        return true;

    }

    private ArrayList<Node> getDistinctNeighbors( NodeList comparison, NodeList neighbors){
        ArrayList<Node> result = new ArrayList<>();
        for(NodeCursor v = neighbors.nodes() ; v.ok() ; v.next() ){
            Node currentNeighbor = v.node();
            if( ! comparison.contains(currentNeighbor)){
                result.add(currentNeighbor);
            }
        }
        return result;
    }

    private NodeList prepareVertexOrder(NodeList innerCycle){
        NodeList result = new NodeList();
        for( NodeCursor v = innerCycle.nodes() ; v.ok() ; v.next() ){
            Node currentInnerNode = v.node();
            Node currentOuterNode = (Node) this.cycleMapping.get(currentInnerNode);
            //System.out.println(currentInnerNode);
            result.add(currentOuterNode);
            result.add(currentInnerNode);
        }
        return result;
    }


    public NodeList getVertexOrder() {
        return vertexOrder;
    }
}
