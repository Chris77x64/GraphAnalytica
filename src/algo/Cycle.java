package algo;

import y.algo.Bfs;
import y.algo.Paths;
import y.base.*;
import y.util.Maps;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static y.util.Generics.nodes;

/**
 * Created by chris on 08.02.2018.
 */
public class Cycle {

    private Graph graph;

    private boolean cycleDetected;
    private NodeList cycle;

    private ArrayList<Node> candidates;
    private NodeMap candidateMap;

    public Cycle( Graph graph, int length){
        this.graph = graph;
        this.cycle = detectCycle(length);
    }


    private NodeList pathOfLength( int length, Node startNode ){

        this.candidates = new ArrayList<>();
        this.candidateMap = Maps.createHashedNodeMap();

        ArrayList<Node> parentList = new ArrayList<>();
        pathOfLengthWorker(startNode,length,parentList);

        NodeList resultList = new NodeList();
        Set<Node> removedDuplicates = new HashSet<>(candidates);
        for( Node node: removedDuplicates){
            resultList.add(node);
        }
        return resultList;
    }

    private ArrayList<Node> addVisitedNode(ArrayList<Node> parents,Node parent){


        ArrayList<Node> result = new ArrayList<>();
        result.addAll(parents);
        result.add(parent);
        return result;
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

                candidates.add(currentNode);
                if( candidateMap.get(currentNode) == null){
                    ArrayList<ArrayList<Node>> paths = new ArrayList<>();
                    paths.add(visited);
                    candidateMap.set(currentNode,paths);
                }
                else{
                    ArrayList<ArrayList<Node>> paths = (ArrayList<ArrayList<Node>>) candidateMap.get(currentNode);
                    paths.add(visited);
                    candidateMap.set(currentNode,paths);
                }

            return true;
        }

        NodeList neighbors = new NodeList(currentNode.neighbors());

        for(NodeCursor v = neighbors.nodes() ; v.ok() ; v.next()){

            ArrayList<Node> newVisitedList = addVisitedNode(visited,currentNode);

            Node currentNeighbor = v.node();

            if( (!visited.contains(currentNeighbor) ) ){

                    boolean result = pathOfLengthWorker(currentNeighbor,length,newVisitedList);

            }

        }

        return false;
    }

    public NodeList[] detectAllCycles( int length) {
        ArrayList<NodeList> result = new ArrayList<>();

        for (Node vertex : nodes(graph)) {
            NodeList candidates = pathOfLength(length - 1, vertex);

            for (NodeCursor v = candidates.nodes(); v.ok(); v.next()) {
                Node currentNode = v.node();

                NodeList neighbors = new NodeList(currentNode.neighbors());
                if (neighbors.contains(vertex)) {

                    ArrayList<ArrayList<Node>> paths = (ArrayList<ArrayList<Node>>) candidateMap.get(currentNode);
                    for( ArrayList<Node> path : paths){
                        NodeList cycle = new NodeList();
                        cycle.addAll(path);
                        result.add(cycle);
                    }
                    this.cycleDetected = true;
                }
            }
        }
        if( result.size() != 0) {
            NodeList[] allCycles = new NodeList[result.size()];
            for( int i=0; i < result.size() ; i++){
                NodeList currentList = result.get(i);
                allCycles[i] = currentList;
            }
            return allCycles;
        }
        else{
            return null;
        }
    }

    private NodeList detectCycle( int length){

        for( Node vertex: nodes(graph)){

            NodeList candidates = pathOfLength(length-1,vertex);

            for(NodeCursor v = candidates.nodes() ; v.ok() ; v.next()){
                Node currentNode = v.node();

                NodeList neighbors = new NodeList(currentNode.neighbors());
                if( neighbors.contains(vertex)){
                    EdgeList edgeList = Paths.findPath(graph,vertex,currentNode,false);
                    NodeList result = Paths.constructNodePath(edgeList);
                    this.cycleDetected = true;
                    return result;
                }
            }

        }
        this.cycleDetected = false;
        return null;
    }

    public boolean isCycleDetected() {
        return cycleDetected;
    }

    public void setCycleDetected(boolean cycleDetected) {
        this.cycleDetected = cycleDetected;
    }

    public NodeList getCycle() {
        return cycle;
    }

    public void setCycle(NodeList cycle) {
        this.cycle = cycle;
    }
}
