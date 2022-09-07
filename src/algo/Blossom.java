package algo;

import view.GraphView;
import y.algo.Bfs;
import y.base.*;
import y.layout.organic.OrganicLayouter;
import y.layout.organic.SmartOrganicLayouter;
import y.util.GraphCopier;
import y.view.Arrow;
import y.view.Graph2D;
import y.view.NodeLabel;

import java.awt.*;
import java.util.ArrayList;

import static y.util.Generics.edges;
import static y.util.Generics.nodes;

public class Blossom {

    private GraphView graphView;
    private Graph2D graph;

    private EdgeList matchedEdges;

    public Blossom(GraphView graphView){
        this.graphView = graphView;
        this.graph = graphView.getGraph2D();

        this.generateGraphI1();
        Graph2D auxGraph = this.generateAuxilliaryGraph();


       this.graphView.setGraph2D(auxGraph);

        this.calculateLayout();
    }


    private void generateGraphI1(){
        graph.clear();

        Node v1 = graph.createNode();
        Node v2 = graph.createNode();
        Node v3 = graph.createNode();
        Node v4 = graph.createNode();
        Node v5 = graph.createNode();
        Node v6 = graph.createNode();
        Node v7 = graph.createNode();
        Node v8 = graph.createNode();
        Node v9 = graph.createNode();
        Node v10 = graph.createNode();
        Node v11 = graph.createNode();
        Node v12 = graph.createNode();


        graph.getRealizer(v1).setLabelText("v1");
        graph.getRealizer(v2).setLabelText("v2");
        graph.getRealizer(v3).setLabelText("v3");
        graph.getRealizer(v4).setLabelText("v4");
        graph.getRealizer(v5).setLabelText("v5");
        graph.getRealizer(v6).setLabelText("v6");
        graph.getRealizer(v7).setLabelText("v7");
        graph.getRealizer(v8).setLabelText("v8");
        graph.getRealizer(v9).setLabelText("v9");
        graph.getRealizer(v10).setLabelText("v10");
        graph.getRealizer(v11).setLabelText("v11");
        graph.getRealizer(v12).setLabelText("v12");


        Edge v1v12 = graph.createEdge(v1,v12);
        Edge v12v11 = graph.createEdge(v12,v11);
        Edge v11v10 = graph.createEdge(v11,v10);
        Edge v10v9 = graph.createEdge(v10,v9);
        Edge v11v9 = graph.createEdge(v11,v9);
        Edge v9v8 = graph.createEdge(v9,v8);
        Edge v8v7 = graph.createEdge(v8,v7);
        Edge v8v6 = graph.createEdge(v8,v6);
        Edge v7v6 = graph.createEdge(v7,v6);
        Edge v7v5 = graph.createEdge(v7,v5);
        Edge v7v2 = graph.createEdge(v7,v2);

        Edge v5v4 = graph.createEdge(v5,v4);
        Edge v2v3 = graph.createEdge(v2,v3);
        Edge v3v4 = graph.createEdge(v3,v4);

        Edge v4v1 = graph.createEdge(v4,v1);

        matchedEdges = new EdgeList();
        matchedEdges.add(v11v10);
        matchedEdges.add(v9v8);
        matchedEdges.add(v7v6);
        matchedEdges.add(v5v4);
        matchedEdges.add(v2v3);
    }

    private void generateGraphI2(){
        graph.clear();

        Node v1 = graph.createNode();
        Node v2 = graph.createNode();
        Node v3 = graph.createNode();
        Node v4 = graph.createNode();
        Node v5 = graph.createNode();
        Node v6 = graph.createNode();
        Node v7 = graph.createNode();
        Node v8 = graph.createNode();
        Node v9 = graph.createNode();
        Node v10 = graph.createNode();
        Node v11 = graph.createNode();
        Node v12 = graph.createNode();


        graph.getRealizer(v1).setLabelText("v1");
        graph.getRealizer(v2).setLabelText("v2");
        graph.getRealizer(v3).setLabelText("v3");
        graph.getRealizer(v4).setLabelText("v4");
        graph.getRealizer(v5).setLabelText("v5");
        graph.getRealizer(v6).setLabelText("v6");
        graph.getRealizer(v7).setLabelText("v7");
        graph.getRealizer(v8).setLabelText("v8");
        graph.getRealizer(v9).setLabelText("v9");
        graph.getRealizer(v10).setLabelText("v10");
        graph.getRealizer(v11).setLabelText("v11");
        graph.getRealizer(v12).setLabelText("v12");


        Edge v1v12 = graph.createEdge(v1,v12);
        Edge v12v11 = graph.createEdge(v12,v11);
        Edge v11v10 = graph.createEdge(v11,v10);
        Edge v10v9 = graph.createEdge(v10,v9);
        Edge v11v9 = graph.createEdge(v11,v9);
        Edge v9v8 = graph.createEdge(v9,v8);
        Edge v8v7 = graph.createEdge(v8,v7);
        Edge v8v6 = graph.createEdge(v8,v6);
        Edge v7v6 = graph.createEdge(v7,v6);
        Edge v7v5 = graph.createEdge(v7,v5);
        Edge v7v2 = graph.createEdge(v7,v2);

        Edge v5v4 = graph.createEdge(v5,v4);
        Edge v2v3 = graph.createEdge(v2,v3);
        Edge v3v4 = graph.createEdge(v3,v4);

        Edge v4v1 = graph.createEdge(v4,v1);

        matchedEdges = new EdgeList();
        matchedEdges.add(v12v11);
        matchedEdges.add(v10v9);
        matchedEdges.add(v8v6);
        matchedEdges.add(v7v5);
        matchedEdges.add(v2v3);
        matchedEdges.add(v4v1);
    }

    private void generateGraph2(){
        graph.clear();

        Node b = graph.createNode();
        Node v2 = graph.createNode();
        Node v5 = graph.createNode();
        Node v6 = graph.createNode();
        Node v7 = graph.createNode();
        Node v8 = graph.createNode();

        graph.getRealizer(b).setLabelText("b");
        graph.getRealizer(v2).setLabelText("v2");
        graph.getRealizer(v5).setLabelText("v5");
        graph.getRealizer(v6).setLabelText("v6");
        graph.getRealizer(v7).setLabelText("v7");
        graph.getRealizer(v8).setLabelText("v8");


        Edge v1v2 = graph.createEdge(b,v2);

        Edge v2v6 = graph.createEdge(v2,v6);

        Edge v3v5 = graph.createEdge(b,v5);
        Edge v3v6 = graph.createEdge(b,v6);

        Edge v4v7 = graph.createEdge(b,v7);

        Edge v5v6 = graph.createEdge(v5,v6);
        Edge v5v7 = graph.createEdge(v5,v7);
        Edge v5v8 = graph.createEdge(v5,v8);

        Edge v6v8 = graph.createEdge(v6,v8);

        Edge v7v8 = graph.createEdge(v7,v8);

        matchedEdges = new EdgeList();
        matchedEdges.add(v3v5);
    }


    private void generateGraph(){
        graph.clear();

        Node v1 = graph.createNode();
        Node v2 = graph.createNode();
        Node v3 = graph.createNode();
        Node v4 = graph.createNode();

        Node v5 = graph.createNode();
        Node v6 = graph.createNode();
        Node v7 = graph.createNode();
        Node v8 = graph.createNode();

        graph.getRealizer(v1).setLabelText("v1");
        graph.getRealizer(v2).setLabelText("v2");
        graph.getRealizer(v3).setLabelText("v3");
        graph.getRealizer(v4).setLabelText("v4");
        graph.getRealizer(v5).setLabelText("v5");
        graph.getRealizer(v6).setLabelText("v6");
        graph.getRealizer(v7).setLabelText("v7");
        graph.getRealizer(v8).setLabelText("v8");

        Edge v1v4 = graph.createEdge(v1,v4);
        Edge v1v3 = graph.createEdge(v1,v3);
        Edge v1v2 = graph.createEdge(v1,v2);

        Edge v2v3 = graph.createEdge(v2,v3);
        Edge v2v6 = graph.createEdge(v2,v6);

        Edge v3v4 = graph.createEdge(v3,v4);
        Edge v3v5 = graph.createEdge(v3,v5);
        Edge v3v6 = graph.createEdge(v3,v6);

        Edge v4v7 = graph.createEdge(v4,v7);

        Edge v5v6 = graph.createEdge(v5,v6);
        Edge v5v7 = graph.createEdge(v5,v7);
        Edge v5v8 = graph.createEdge(v5,v8);

        Edge v6v8 = graph.createEdge(v6,v8);

        Edge v7v8 = graph.createEdge(v7,v8);

        matchedEdges = new EdgeList();
        matchedEdges.add(v1v4);
        matchedEdges.add(v3v5);
    }

    private void calculateLayout(){

        SmartOrganicLayouter smartOrganicLayouter = new SmartOrganicLayouter();
        smartOrganicLayouter.setMinimalNodeDistance(150);
        smartOrganicLayouter.doLayout(graph);

        this.graphView.updateView();
        this.graphView.fitContent();
    }

    private Graph2D generateAuxilliaryGraph() {
        GraphCopier gc = new GraphCopier(graph.getGraphCopyFactory());
        Graph2D auxGraph = (Graph2D) gc.copy(graph);
        auxGraph.clear();

        for (Node vertex : nodes(graph)) {
            Node n= auxGraph.createNode(graph.getRealizer(vertex));
            auxGraph.getRealizer(n).setFillColor(Color.green);
        }

        for (Edge edge : edges(graph)) {
            Node source = edge.source();
            Node target = edge.target();

            for (Node u : nodes(graph)) {
                for (Node w : nodes(graph)) {
                    if (u != w) {

                        for (Node v : nodes(graph)) {

                            Edge uv = u.getEdge(v);
                            Edge vw = w.getEdge(v);

                            if (uv != null && vw != null) {

                                if (matchedEdges.contains(vw) && !matchedEdges.contains(uv)) {
                                    Node auxU = getAuxGraphNode(u,auxGraph);
                                    Node auxW = getAuxGraphNode(w,auxGraph);
                                    Node auxV = getAuxGraphNode(v,auxGraph);

                                    Edge e = auxGraph.createEdge(auxU,auxW);

                                    auxGraph.getRealizer(auxV).setFillColor(Color.RED);
                                    auxGraph.getRealizer(auxW).setFillColor(Color.RED);


                                    break;
                                }
                            }
                        }

                    }
                }

            }
        }
        for( Edge e: edges(auxGraph)){
            auxGraph.getRealizer(e).setArrow(Arrow.STANDARD);
        }
        return auxGraph;
    }


    private NodeList freeVerticesNeighbours(NodeList freeVertices){
        NodeList result = new NodeList();

        for( int k=0; k < freeVertices.size(); k++){
            Node currentFreeVertex = (Node) freeVertices.get(k);

            NodeList currentNeighbours = new NodeList(currentFreeVertex.neighbors());

            for( int i=0; i < currentNeighbours.size(); i++){
                Node currentNeighbour = (Node) currentNeighbours.get(i);
                if( !freeVertices.contains(currentNeighbour)){
                    result.add(currentNeighbour);
                }
            }
        }

        return result;
    }

/*
    private void calculateShortestXtoNXPath(Graph2D originalGraph, Graph2D auxGraph,EdgeList matching){

        NodeList freeVertices = new NodeList(originalGraph.nodes());

        for( int i=0; i < matching.size(); i++){
            Edge currentEdge = (Edge) matching.get(i);
            freeVertices.remove(currentEdge.source());
            freeVertices.remove(currentEdge.target());
        }



        NodeList NX = freeVerticesNeighbours(freeVertices);


        boolean done = false;

        NodeMap visited = auxGraph.createNodeMap();
        NodeMap parent = auxGraph.createNodeMap();
        for( Node node: nodes(auxGraph)){
            visited.setBool(node,false);
        }

        NodeList nodes2Process= new NodeList();
        for( int i=0; i < freeVertices.size(); i++){
            Node currentNode = (Node) freeVertices.get(i);
            Node currentAuxNode = getAuxGraphNode(currentNode,auxGraph);
            nodes2Process.add(currentAuxNode);
        }

        ArrayList<NodeList> bfsLayers = new ArrayList<>();
        bfsLayers.add(nodes2Process); // first layer are free vertices

        while( !done){


            if( nodes2Process.isEmpty()){
                break;
            }
            else{
                NodeList nextLayer = new NodeList();

                for( int i=0; i < nodes2Process.size(); i++){
                    Node currentNode  = (Node) nodes2Process.get(i); // in original graph
                    NodeList neighbours = new NodeList(currentNode.neighbors());

                    for( int k=0 ; k < neighbours.size(); k++){
                        Node currentNeighbour = (Node) neighbours.get(k);
                        if( !visited.getBool(currentNeighbour)){
                            nextLayer.add(currentNeighbour);
                            parent.set(currentNeighbour,currentNode);
                        }
                    }


                }

            }

        }

        for ( int i=0; i < freeVertices.size(); i++){
            Node currentFreeVertex = (Node) freeVertices.get(i);
            Node auxNode = getAuxGraphNode(currentFreeVertex,auxGraph);

            if( auxNode.degree() > 0){
                NodeList[] bfsLayers = Bfs.getLayers(auxGraph,
                                                    new NodeList(auxNode),
                                                    true,
                                                    auxGraph.createNodeMap());
                System.out.println("Current Free Vertex"+currentFreeVertex+" with bfs layers:");
                for( int k=0; k < bfsLayers.length; k++){
                    NodeList currentLayer = bfsLayers[k];
                    System.out.println(currentLayer);
                }
            }
        }
    }

*/
    private Node getAuxGraphNode( Node originalNode, Graph2D auxGraph){
        for(Node vertex: nodes(auxGraph)){
            if ( graph.getRealizer(originalNode).getLabelText() == auxGraph.getRealizer(vertex).getLabelText()){
                return vertex;
            }
        }
        return null;
    }


}
