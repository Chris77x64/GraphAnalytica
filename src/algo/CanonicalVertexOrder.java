package algo;

import y.base.*;
import y.layout.planar.Face;
import y.layout.planar.PlanarInformation;
import y.layout.planar.PlanarityTest;
import y.util.GraphHider;
import y.util.Maps;

import java.util.Map;

import static y.util.Generics.nodes;

/**
 * Created by chris on 24.05.2018.
 */
public class CanonicalVertexOrder {

    private Graph graph;
    private GraphHider graphHider;

    private NodeList canonicalOrdering;

    private NodeMap numChords;
    private NodeMap onOuterFace;
    private NodeMap visited;

    public CanonicalVertexOrder(Graph graph) {
        this.graph = graph;
        this.graphHider = new GraphHider(graph);


        this.initializeAssets();

        this.calculateCanonicalOrder();
    }

    private void initializeAssets(){
        this.numChords = Maps.createHashedNodeMap();
        this.onOuterFace = Maps.createHashedNodeMap();
        this.visited = Maps.createHashedNodeMap();

        for( Node node: nodes(graph)){
            numChords.setInt(node,0);
            onOuterFace.setBool(node,false);
            visited.setBool(node,false);
        }

    }


    private NodeList calculateOuterFace(){
        PlanarInformation planarInformation = new PlanarInformation(graph);
        PlanarityTest pt = new PlanarityTest();
        pt.embed(planarInformation);

        Face outerFace = planarInformation.getOuterFace();

        return calculateFaceOrder(outerFace);
    }


    private Node selectNextNode(NodeList outerFace){
        for( int i=0; i < outerFace.size(); i++){
            Node currentNode = (Node) outerFace.get(i);
          //  System.out.println(" currentnode: "+currentNode+" isvisited: "+visited.getBool(currentNode)+" CHORDS: "+numChords.getInt(currentNode));
            if( !visited.getBool(currentNode) && numChords.getInt(currentNode) == 0){
                return currentNode;
            }
        }
        //System.out.println(outerFace);
        return null;
    }


    private void updateChords(NodeList outerFace){

       // System.out.println("______________________________________________");

        for( int i=0; i < outerFace.size(); i++){

            Node currentNode = (Node) outerFace.get(i);

            int currentNodeIndex = outerFace.indexOf(currentNode);
            int chords = 0;

            NodeList neighbours = new NodeList(currentNode.neighbors());

            for( int k=0; k < neighbours.size(); k++){
                Node currentNeighbor = (Node) neighbours.get(k);

                if( outerFace.contains(currentNeighbor)){

                    int currentNeighborIndex = outerFace.indexOf(currentNeighbor);

                    if(     !(currentNeighborIndex == 0 && currentNodeIndex == (outerFace.size()-1) ) &&
                            !(currentNeighborIndex == (outerFace.size()-1) && currentNodeIndex == 0) &&
                            !(Math.abs(currentNeighborIndex-currentNodeIndex) == 1)){
                        chords = chords+1;
                    }

                }

            }
            //System.out.println("NODE "+currentNode+" has chords: "+chords);
            numChords.setInt(currentNode,chords);

        }
    }

    private void calculateCanonicalOrder(){
        this.canonicalOrdering = new NodeList();

        NodeList outerFaceNodes = calculateOuterFace();

        Node v1 = (Node) outerFaceNodes.get(0);
        Node v2 = (Node) outerFaceNodes.get(1);

        visited.setBool(v1,true);
        visited.setBool(v2,true);

        //System.out.println("initial outerface "+outerFaceNodes);

        for( int i= graph.nodeCount()-1; i != 1; i--){

            Node next = selectNextNode(outerFaceNodes);
            visited.setBool(next,true);
            canonicalOrdering.add(next);

            graphHider.hide(next);
            outerFaceNodes = calculateOuterFace();
            //System.out.println("currentouterface "+outerFaceNodes);
            updateChords(outerFaceNodes);

        }

        graphHider.unhideAll();
        canonicalOrdering.add(v2);
        canonicalOrdering.add(v1);
        canonicalOrdering.reverse();
        //System.out.println(canonicalOrdering);

    }

    private NodeList calculateFaceOrder(Face outerFace){

        EdgeList faceEdges = new EdgeList(outerFace.edges());

        NodeList result = new NodeList();
        for( int i=0; i < faceEdges.size(); i++){
            Edge currentEdge = (Edge) faceEdges.get(i);

            Node source = currentEdge.source();
            Node target = currentEdge.target();

            if( !result.contains(source)){
                result.add(source);
            }
            if( ! result.contains(target)){
                result.add(target);
            }
        }
        return result;
    }

    public NodeList getCanonicalOrdering() {
        return canonicalOrdering;
    }
}
