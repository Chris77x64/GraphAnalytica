package algo;

import view.error.ColoringErrorView;
import y.algo.Bipartitions;
import y.base.*;
import y.util.Maps;
import y.util.Tuple;
import y.view.Graph2D;
import y.view.NodeRealizer;

import java.awt.*;
import java.util.*;

import static y.algo.Bipartitions.RED;
import static y.util.Generics.nodes;

/**
 * Created by chris on 17.01.2018.
 */
public class KColoring  {


    private final Color K1 = Color.RED;
    private final Color K2 = Color.GREEN;
    private final Color K3 = Color.ORANGE;
    private final Color K4 = Color.BLUE;


    private Graph2D graph;
    private int numColors;

    private NodeMap result;

    private boolean isSuccesfull;

    public KColoring(Graph2D graph, int k, boolean errorActivated) {
        this.graph = graph;
        this.numColors = k;
        this.initializeDomain();

        if( k== 2){
            boolean successful = twoColoring();
            if (successful) {
                this.colorResult();
                this.isSuccesfull = true;
            }
            else{
                if(errorActivated){
                    ColoringErrorView coloringErrorView = new ColoringErrorView(2);
                }
                this.isSuccesfull = false;
            }
        }
        else {
            boolean successful = this.recursiveBacktracking();
            if (successful) {
                this.colorResult();
                this.isSuccesfull = true;
            }
            else{
                if(errorActivated){
                    ColoringErrorView coloringErrorView = new ColoringErrorView(k);
                }
                this.isSuccesfull = false;
            }
        }
    }


    private boolean twoColoring(){
        NodeMap vertexMap = Maps.createHashedNodeMap();
        boolean twoColorable = Bipartitions.getBipartition(graph,vertexMap);
        if( twoColorable){
            NodeMap domain = Maps.createHashedNodeMap();
            for( Node node: nodes(graph)){

                Object color = vertexMap.get(node);
                if( color == RED){
                    domain = setDomainToValue(node,1,domain);
                }
                else{
                    domain = setDomainToValue(node,2,domain);
                }

            }
            this.result = domain;
            return true;
        }
        else{
            return false;
        }

    }

    private boolean recursiveBacktracking(){
        NodeMap initialDomain = initializeDomain();
        NodeMap initialAssignmentMap = initializeAssignmentMap();
        return recursiveBacktracking(initialDomain,initialAssignmentMap);
    }


    private boolean recursiveBacktracking( NodeMap domain, NodeMap assignment){
        if( isAssignmentComplete(domain,assignment)){
            this.result = domain;
            return true;
        }
        else{
            //Node consideredNode = selectUnassignedVariable(assignment);

            // Select a Node that hast been assigned a color yet, with maximum degree
            Node consideredNode = degreeHeuristic(assignment);

            // Terminate recursion : last node was colored and there was a violation
            if( consideredNode == null){
                return false;
            }


           // System.out.println("CONSIDERED NODE: "+consideredNode.index());
           // System.out.println("DOMAIN: ");
           // printDomain(domain);

            // Sort the given domain, with a minimum conflict heuristic ( ascending order )
            ArrayList<Integer> domainConsideredNode = getDomainFromNode(consideredNode,domain);
            ArrayList<Integer> sortedDomainConsideredNode = leastContainingValuesHeuristic(domainConsideredNode,consideredNode,domain);
            NodeMap newAssignmentMap = assignNode(consideredNode,assignment);
          //  System.out.println("ASSIGNMENTMAP: ");
          //  printAssignment(assignment);

            for( int value: sortedDomainConsideredNode){
                //System.out.println("CONSIDERED NODE "+consideredNode.index()+" Value "+value);
                // Assign the color "value" as new Color and change the domain
                NodeMap changedDomain = setDomainToValue(consideredNode,value,domain);
               // printDomain(changedDomain);
               // System.out.println("--------");

                NodeMap newDomain = maintainVertexConsistency(consideredNode,changedDomain,value);
//                printDomain(newDomain);


                if( newDomain == null){
                    return false;
                }
                else {

                    if (isOneDomainEmpty(newDomain)) {
                        System.out.println(" False at CONSIDERED NODE " + consideredNode.index() + " Value " + value);
                        return false;
                    } else {
                        boolean result = recursiveBacktracking(newDomain, newAssignmentMap);

                        if (result) {
                            return true;
                        }
                    }
                }


                //printDomain(newDomain);

                // Terminate Recursion a violation occurred, one vertex cant be assigned with any value because his domain is empty


            }
            return false;
        }
    }


    private Tuple revised(Node vertex, NodeMap domain, int lastRemainingValue){

        ArrayList<Tuple> result = new ArrayList<>();
        boolean oneDomainEmpty = false;

        for( NodeCursor v = vertex.neighbors(); v.ok() ; v.next()){
            Node neighbor = v.node();
            ArrayList<Integer> neighborDomain =  getDomainFromNode(neighbor,domain);

            //int lastRemainingValueIndex = neighborDomain.indexOf(lastRemainingValue);

            boolean removalSuccesfull = neighborDomain.remove(new Integer(lastRemainingValue));
            if( removalSuccesfull){

                if( neighborDomain.size() == 0){
                    oneDomainEmpty = true;
                    break;
                }
                else if( neighborDomain.size() == 1){
                    int remainingValue = neighborDomain.get(0);
                    result.add(new Tuple(neighbor,remainingValue));
                }
            }
            domain.set(neighbor,neighborDomain);
        }
        return new Tuple(oneDomainEmpty,result);
    }

    private NodeMap maintainVertexConsistency(Node vertex , NodeMap domain, int lastRemainingValue){

        NodeMap result = domain;

        ArrayList<Tuple> queue = new ArrayList<>();
        Tuple firstVertex2Process = new Tuple(vertex,lastRemainingValue);
        queue.add(firstVertex2Process);

        while( ! queue.isEmpty() ){

            Tuple currentElement = queue.remove(0);

            Node currentNode = (Node) currentElement.o1;
            int currentValueToCheck = (int) currentElement.o2;

            Tuple currentRevise = revised(currentNode,result,currentValueToCheck);
            boolean oneVertexEmpty = (boolean) currentRevise.o1;

            if(oneVertexEmpty){
                return null;
            }
            else{
                ArrayList<Tuple> affectedVertices = (ArrayList<Tuple>) currentRevise.o2;
                queue.addAll(affectedVertices);
            }


        }
        return result;

    }


    private ArrayList<Integer> leastContainingValuesHeuristic(ArrayList<Integer> domainSelectedVertex , Node selectedVertex , NodeMap domain ){

        ArrayList<Integer> result = domainSelectedVertex;

        Collections.sort(result, new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                int excludedValueso1 = 0;
                int excludedValueso2 = 0;

                for( NodeCursor v = selectedVertex.neighbors(); v.ok() ; v.next()){
                    Node neighbor = v.node();
                    ArrayList<Integer> domainNeighbor = getDomainFromNode(neighbor,domain);
                    if( domainNeighbor.contains(o1)){
                        excludedValueso1 += 1;
                    }
                    if( domainNeighbor.contains(o2)){
                        excludedValueso2 += 1;
                    }
                }

                if( excludedValueso1 > excludedValueso2){
                    return 1;
                }
                else if( excludedValueso1 < excludedValueso2){
                    return -1;
                }
                else{
                    return 0;
                }
            }
        });

        return result;
    }

    private Node degreeHeuristic(NodeMap assignment ){
        ArrayList<Node> candidates = new ArrayList<>();

        for( NodeCursor v = graph.nodes(); v.ok(); v.next()){
            Node currentNode = v.node();
            if( !assignment.getBool(currentNode)){
                candidates.add(currentNode);
            }
        }

        Collections.sort(candidates, new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                if( o1.degree() > o2.degree()){
                    return 1;
                }
                else if( o1.degree() < o2.degree()){
                    return -1;
                }
                else{
                    return 0;
                }
            }
        });

        if( candidates.isEmpty()){
            return null;
        }
        else{
            return candidates.get(candidates.size()-1);
        }
    }


    private Node minimumRemainingValuesHeuristic( NodeMap assignment, NodeMap domain){

        ArrayList<Node> candidates = new ArrayList<>();

        for( NodeCursor v = graph.nodes(); v.ok(); v.next()){
            Node currentNode = v.node();
            if( !assignment.getBool(currentNode)){
                candidates.add(currentNode);
            }
        }

        Collections.sort(candidates, new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                ArrayList<Integer> domainO1 = getDomainFromNode(o1,domain);
                ArrayList<Integer> domainO2 = getDomainFromNode(o2,domain);
                if( domainO1.size() < domainO2.size() ){
                    return 1;
                }
                else if( domainO1.size() > domainO2.size()){
                    return -1;
                }
                else{
                    return 1;
                }
            }
        });
        if( candidates.isEmpty()){
            return null;
        }
        else {
            return candidates.get(0);
        }
    }


    private boolean isOneDomainEmpty( NodeMap domain){
        for( Node currentNode : nodes(graph)){
            if( isDomainEmpty(currentNode,domain)){
                return true;
            }
        }
        return false;
    }

    private Node selectFirstUnassignedVariable(NodeMap assignment){
        for( NodeCursor v = graph.nodes(); v.ok(); v.next()){
            Node currentNode = v.node();
            if( !assignment.getBool(currentNode)){
                return currentNode;
            }
        }
        return null;
    }

    private boolean isAssignmentComplete(NodeMap domain,NodeMap assignment){
        for( Node node : nodes(graph)){
            if( !assignment.getBool(node)){
                return false;
            }
        }
        for( Node node : nodes(graph)){
            for( NodeCursor v = node.neighbors(); v.ok(); v.next()){
                Node neighbor = v.node();

                ArrayList<Integer> domainNode = getDomainFromNode(node,domain);
                ArrayList<Integer> domainNeighbor = getDomainFromNode(neighbor,domain);

                if( domainNode.get(0) == domainNeighbor.get(0)){
                    return false;
                }

            }
        }
        return true;
    }

    private NodeMap initializeAssignmentMap(){
        NodeMap result = Maps.createHashedNodeMap();
        for(NodeCursor v = graph.nodes() ; v.ok() ; v.next() ){
            Node currentNode = v.node();
            result.setBool(currentNode,false);
        }
        return result;
    }


    private NodeMap initializeDomain(){
        NodeMap result = Maps.createHashedNodeMap();

        for(NodeCursor v = graph.nodes() ; v.ok() ; v.next() ){
            Node currentNode = v.node();
            ArrayList<Integer> domainNode = new ArrayList<>();
            for( int i=0; i < numColors ; i++){
                domainNode.add(i+1);
            }
            result.set(currentNode,domainNode);
        }
        return result;
    }

    private void printDomain(NodeMap domain){

        for( NodeCursor v = graph.nodes() ; v.ok() ; v.next()){
            Node currentNode = v.node();
            ArrayList<Integer> domainNode = (ArrayList<Integer>) domain.get(currentNode);
            String printString = "Node: "+currentNode.index()+" With Domain: ";
            for( int element: domainNode){
                printString += " "+element;
            }
            System.out.println(printString);
        }
    }

    private void printAssignment(NodeMap assignment){
        for( NodeCursor v = graph.nodes() ; v.ok() ; v.next()){
            Node currentNode = v.node();
            System.out.println("Node "+currentNode.index()+" is assigned: "+assignment.getBool(currentNode));
        }
    }


    private ArrayList<Integer> getDomainFromNode(Node vertex, NodeMap nodeMap){
        return (ArrayList<Integer>) nodeMap.get(vertex);
    }


    private boolean isDomainEmpty(Node vertex,NodeMap nodeMap){
        return  getDomainFromNode(vertex,nodeMap).isEmpty();
    }

    private void deleteValueFromDomain(Node vertex,int value, NodeMap nodeMap){
        getDomainFromNode(vertex,nodeMap).remove(value);
    }

    private NodeMap setDomainToValue( Node vertex, int value, NodeMap domain){
        NodeMap newDomain = Maps.createHashedNodeMap();

        for( Node node : nodes(graph)){
            if( node.index() == vertex.index()){
                ArrayList<Integer> newValueAssignment = new ArrayList<>();
                newValueAssignment.add(value);
                newDomain.set(vertex,newValueAssignment);
            }
            else{
                newDomain.set(node,domain.get(node));
            }
        }
        return newDomain;
    }


    private NodeMap assignNode( Node vertex, NodeMap assignmentMap){
        NodeMap newAssignmentMap = Maps.createHashedNodeMap();

        for( Node node : nodes(graph)) {
            if (node.index() == vertex.index()) {
                newAssignmentMap.setBool(node,true);
            }
            else{
                newAssignmentMap.setBool(node,assignmentMap.getBool(node));
            }
        }
        return newAssignmentMap;
    }

    private void colorResult(){
        for( Node node: nodes(graph)){

            NodeRealizer nodeRealizer = graph.getRealizer(node);

            int color = getDomainFromNode(node,result).get(0);

            switch (color){
                case 1:{
                    nodeRealizer.setFillColor(K1);
                    break;
                }
                case 2:{
                    nodeRealizer.setFillColor(K2);
                    break;
                }
                case 3:{
                    nodeRealizer.setFillColor(K3);
                    break;
                }
                case 4:{
                    nodeRealizer.setFillColor(K4);
                    break;
                }
            }


        }
    }


    public boolean isSuccesfull() {
        return isSuccesfull;
    }
}
