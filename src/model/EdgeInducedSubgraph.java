package model;

import y.base.*;
import y.util.GraphCopier;
import y.util.Maps;
import y.view.Graph2D;

import java.util.*;

/**
 * Created by chris on 04.01.2018.
 */
public class EdgeInducedSubgraph {

    private GraphCopier factory;

    private Graph originalGraph;
    private Graph inducedSubgraph;

    // Mapping  Graph Node --> Induced Graph Index
    private NodeMap indexGraphToInducedMapping;

    // Mapping Induced Graph Node --> Graph Index
    private NodeMap indexInducedToGraphMapping;


    public EdgeInducedSubgraph(Graph2D currentGraph, ArrayList<Edge> inducedEdges){

        this.factory = new GraphCopier(currentGraph.getGraphCopyFactory());

        this.originalGraph = currentGraph;
        this.inducedSubgraph = factory.getCopyFactory().createGraph();

        factory.setNodeMapCopying(true);
        factory.getCopyFactory().preCopyGraphData(currentGraph,inducedSubgraph);


        this.initializeNodes(inducedEdges);
        this.initializeEdges(inducedEdges);


    }

    private void initializeNodes( ArrayList<Edge> inducedEdges){
        HashSet<Node> inducedNodes = new HashSet<Node>();

        for ( Edge edge: inducedEdges){
            Node source = edge.source();
            Node target = edge.target();
            inducedNodes.add(source);
            inducedNodes.add(target);
        }

        this.indexGraphToInducedMapping = Maps.createHashedNodeMap();
        this.indexInducedToGraphMapping = Maps.createHashedNodeMap();

        int i= 0;
        for( Node node: inducedNodes){
            Node inducedNode = this.inducedSubgraph.createNode();
            indexGraphToInducedMapping.setInt(node,i);
            indexInducedToGraphMapping.setInt(inducedNode,node.index());
            i++;
        }

    }

    private void initializeEdges( ArrayList<Edge> inducedEdges){

        for( Edge edge: inducedEdges){
            Node source = edge.source();
            Node target = edge.target();

            Node sourceInducedGraph = getInducedNodeByGraphNode(source);
            Node targetInducedGraph = getInducedNodeByGraphNode(target);

            this.inducedSubgraph.createEdge(sourceInducedGraph,targetInducedGraph);
        }
    }

    public Graph getInducedSubgraph() {
        return inducedSubgraph;
    }

    public void setInducedSubgraph(Graph inducedSubgraph) {
        this.inducedSubgraph = inducedSubgraph;
    }

    @Override
    public String toString() {
        String result = "Nodes: ";
        for(NodeCursor v = this.inducedSubgraph.nodes() ;v.ok(); v.next()){
            Node currentNode = v.node();
            result += currentNode.index()+" ";
        }
        result += " \n Edges: ";
        for(EdgeCursor e = this.inducedSubgraph.edges(); e.ok() ; e.next()){
            Edge currentEdge = e.edge();
            result+= currentEdge.toString()+" ";
        }
        return result;
    }


    public Node getGraphNodeByInducedNode( Node inducedNode){
        Node[] graphNodes = this.originalGraph.getNodeArray();
        int originalIndex = indexInducedToGraphMapping.getInt(inducedNode);
        return graphNodes[originalIndex];
    }

    public Node getInducedNodeByGraphNode( Node graphNode){
        Node[] inducedNodes = this.inducedSubgraph.getNodeArray();
        int inducedIndex = indexGraphToInducedMapping.getInt(graphNode);
        return inducedNodes[inducedIndex];
    }

    private boolean isSubgraphSpanning(){
        if( this.inducedSubgraph.nodeCount() == this.originalGraph.nodeCount() ){
            return true;
        }
        else{
            return false;
        }
    }

}
