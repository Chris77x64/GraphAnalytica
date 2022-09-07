package algo;

import y.base.*;
import y.util.Maps;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import static y.util.Generics.nodes;

/**
 * Created by chris on 16.02.2018.
 */
public class Path {

    private Graph graph;

    private ArrayList<Node> pathEndpoints;
    private NodeMap paths;

    private Node startNode;
    private int length;

    private NodeList[] allPathsOfLength;

    private boolean abort;

    public Path( Graph graph , Node startNode, int length){
        this.graph = graph;
        this.startNode = startNode;
        this.length = length;
        this.abort = false;
        this.calculateAllPaths();
    }


    private void calculateAllPaths(){
        ArrayList<NodeList> result = new ArrayList<>();

        pathOfLength(length,startNode);

        for( Node currentEndpoint : pathEndpoints){
            ArrayList<ArrayList<Node>> p = (ArrayList<ArrayList<Node>>) paths.get(currentEndpoint);

            for( ArrayList<Node> list : p){
                NodeList res = new NodeList();
                res.addAll(list);
                result.add(res);
            }
        }

        this.allPathsOfLength = new NodeList[result.size()];
        for( int i= 0; i < result.size(); i++){
            allPathsOfLength[i] = result.get(i);
        }

    }

    private ArrayList<Node> addVisitedNode(ArrayList<Node> parents,Node parent){


        ArrayList<Node> result = new ArrayList<>();
        result.addAll(parents);
        result.add(parent);
        return result;
    }

    private NodeList pathOfLength(int length, Node startNode ){

        this.pathEndpoints = new ArrayList<>();
        this.paths = Maps.createHashedNodeMap();

        ArrayList<Node> parentList = new ArrayList<>();
        pathOfLengthWorker(startNode,length,parentList);

        NodeList resultList = new NodeList();
        Set<Node> removedDuplicates = new HashSet<>(pathEndpoints);
        for( Node node: removedDuplicates){
            resultList.add(node);
        }
        return resultList;
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

            pathEndpoints.add(currentNode);
            if( paths.get(currentNode) == null){

                ArrayList<ArrayList<Node>> path = new ArrayList<>();
                path.add(visited);

                paths.set(currentNode,path);
            }
            else{
                ArrayList<ArrayList<Node>> path = (ArrayList<ArrayList<Node>>) paths.get(currentNode);
                path.add(visited);

                paths.set(currentNode,path);
            }

            return true;
        }

        NodeList neighbors = new NodeList(currentNode.neighbors());

        for(NodeCursor v = neighbors.nodes(); v.ok() ; v.next()){

            ArrayList<Node> newVisitedList = addVisitedNode(visited,currentNode);

            Node currentNeighbor = v.node();

            if( (!visited.contains(currentNeighbor) ) ){

                boolean result = pathOfLengthWorker(currentNeighbor,length,newVisitedList);

            }

        }

        return false;
    }


    public NodeList[] getAllPathsOfLength() {
        return allPathsOfLength;
    }

}
