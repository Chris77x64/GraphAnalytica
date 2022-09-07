package algo;

import y.base.*;
import y.util.Maps;


import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;

import static y.util.Generics.nodes;

/**
 * Created by chris on 22.05.2018.
 */
public class BCTree {

    private Graph graph;

    private Graph T;

    private Stack<Node> callStack;

    private NodeMap discovery;
    private NodeMap lowNumber;

    private NodeList articulationPoints;

    private int timestamp;

    private ArrayList<NodeList> blocks;

    private NodeMap cNodeMapping;
    private NodeMap bNodeMapping;

    private NodeList bNodes;
    private NodeList cNodes;

    public BCTree(Graph graph) {

        this.graph = graph;

        T = new Graph();
        calculateBCTree();
        System.out.println(T);
        //printT();

    }

    /*
    private void printT(){
        Node start = (Node) bNodes.get(0);
        System.out.println("BNODE with Block: "+bNodeMapping.get(start));
        printTWorker(start,null);
    }

    private void printTWorker( Node current,Node parent){

        System.out.println("currentcall "+current+" "+parent);
        NodeList neighbors = new NodeList(current.neighbors());

        for( int i=0; i < neighbors.size(); i++){

            Node currentNeighbor = (Node) neighbors.get(i);

            if( currentNeighbor != parent){

                if( cNodeMapping.get(currentNeighbor) != null){
                    System.out.println("CNODE with Articulation Point "+cNodeMapping.get(currentNeighbor));
                }
                else{
                    System.out.println("BNODE with Block: "+bNodeMapping.get(currentNeighbor));
                }

                printTWorker(currentNeighbor,current);
            }
        }
    }
    */

    private void calculateBCTree(){
        this.calculateBlocks();
        this.calculateArticulationPoints();

        cNodeMapping = Maps.createHashedNodeMap();
        bNodeMapping = Maps.createHashedNodeMap();

        bNodes = new NodeList();

        if( articulationPoints.isEmpty()){
            Node root = T.createNode();
            bNodeMapping.set(root,blocks.get(0));
            bNodes.add(root);
        }
        else{

            for( int i=0; i < blocks.size(); i++){
                NodeList currentBlock = blocks.get(i);

                Node currentBNode = T.createNode();
                bNodeMapping.set(currentBNode,currentBlock);
                bNodes.add(currentBNode);
            }

            for( int i=0; i < articulationPoints.size(); i++){
                Node ap = (Node) articulationPoints.get(i);

                Node currentCNode = T.createNode();
                cNodeMapping.set(currentCNode,ap);

                for( int j =0; j < bNodes.size(); j++){
                    Node currentBNode = (Node) bNodes.get(j);
                    NodeList correspondingBlock = (NodeList) bNodeMapping.get(currentBNode);

                    if( correspondingBlock.contains(ap)){
                        T.createEdge(currentCNode,currentBNode);
                    }
                }
            }
        }

    }

    private void calculateBlocks(){
        this.blocks = new ArrayList<>();
        this.callStack = new Stack<>();

        discovery = Maps.createHashedNodeMap();
        lowNumber = Maps.createHashedNodeMap();

        for( Node node: nodes(graph)){
            discovery.setInt(node,0);
        }

        this.timestamp = 0;

        Node firstVertex = graph.firstNode();

        calculateBCTreeWorker(firstVertex,null);
    }

    private void calculateArticulationPoints(){
        ArticulationPoints ap = new ArticulationPoints(graph);
        this.articulationPoints = ap.getArticulationPoints();
    }



    private void calculateBCTreeWorker2(Node current, Node parent){
        callStack.add(current);
        timestamp = timestamp + 1;

        discovery.setInt(current, timestamp);
        lowNumber.setInt(current, timestamp);


        NodeList neighbors = new NodeList(current.neighbors());

        for (int i = 0; i < neighbors.size(); i++) {

            Node currentNeighbor = (Node) neighbors.get(i);

            // tree edge
            if (discovery.getInt(currentNeighbor) == 0) {

                calculateBCTreeWorker(currentNeighbor, current);

                int nextLowNumber = Integer.min(lowNumber.getInt(current), lowNumber.getInt(currentNeighbor));
                lowNumber.setInt(current, nextLowNumber);
            }
            // back edge
            else if (discovery.getInt(currentNeighbor) < discovery.getInt(current)) {
                int nextLowNumber = Integer.min(lowNumber.getInt(current), discovery.getInt(currentNeighbor));
                lowNumber.setInt(current, nextLowNumber);
            }
        }

        if (parent != null && (lowNumber.getInt(current) >= discovery.getInt(parent))) {

            EdgeList currentComponent = new EdgeList();


            while (!callStack.isEmpty()) {
                Node w = callStack.pop();

                NodeList neighborsW = new NodeList(w.neighbors());

                for (int i = 0; i < neighborsW.size(); i++) {
                    Node u = (Node) neighborsW.get(i);
                    if (discovery.getInt(w) > discovery.getInt(u)) {
                        currentComponent.add(w.getEdge(u));
                    }
                }

                if (w == current) {
                    break;
                }

            }

            NodeList currentComponentNodes = extractNodes(currentComponent);
            this.blocks.add(currentComponentNodes);
        }
    }


    private void calculateBCTreeWorker(Node current, Node parent) {

        callStack.add(current);
        timestamp = timestamp + 1;

        discovery.setInt(current, timestamp);
        lowNumber.setInt(current, timestamp);


        NodeList neighbors = new NodeList(current.neighbors());

        for (int i = 0; i < neighbors.size(); i++) {

            Node currentNeighbor = (Node) neighbors.get(i);

            // tree edge
            if (discovery.getInt(currentNeighbor) == 0) {

                calculateBCTreeWorker(currentNeighbor, current);

                int nextLowNumber = Integer.min(lowNumber.getInt(current), lowNumber.getInt(currentNeighbor));
                lowNumber.setInt(current, nextLowNumber);
            }
            // back edge
            else if (discovery.getInt(currentNeighbor) < discovery.getInt(current)) {
                int nextLowNumber = Integer.min(lowNumber.getInt(current), discovery.getInt(currentNeighbor));
                lowNumber.setInt(current, nextLowNumber);
            }
        }


        if (parent != null && (lowNumber.getInt(current) >= discovery.getInt(parent))) {

            EdgeList currentComponent = new EdgeList();


            while( ! callStack.isEmpty()){
                Node w = callStack.pop();

                NodeList neighborsW = new NodeList(w.neighbors());

                for( int i=0; i < neighborsW.size(); i++){
                    Node u = (Node) neighborsW.get(i);
                    if( discovery.getInt(w) > discovery.getInt(u)){
                        currentComponent.add(w.getEdge(u));
                    }
                }

                if( w == current){
                    break;
                }

            }

            NodeList currentComponentNodes = extractNodes(currentComponent);
            System.out.println("ARTIKULATIONSPUNKT: "+parent);

            /*
            Node currentBlock = T.createNode();
            this.bNodeMapping.set(currentBlock,currentComponentNodes);


            Node cNode = T.createNode();
            this.cNodes.add(parent);
            this.cNodeMapping
            T.createEdge(currentBlock,cNode);
            */

            System.out.println(currentComponentNodes);
            this.blocks.add(currentComponentNodes);


            }
        }

        private NodeList extractNodes(EdgeList blockEdges){
            NodeList result = new NodeList();
            for( int i=0; i < blockEdges.size(); i++){
                Edge currentEdge = (Edge) blockEdges.get(i);

                Node source = currentEdge.source();
                Node target = currentEdge.target();

                if( !result.contains(source)){
                    result.add(source);
                }

                if( !result.contains(target)){
                    result.add(target);
                }
            }
            return result;
        }

    }