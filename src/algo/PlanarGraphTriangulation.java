package algo;

import y.base.*;
import y.layout.planar.Face;
import y.layout.planar.FaceCursor;
import y.layout.planar.PlanarInformation;
import y.layout.planar.PlanarityTest;
import y.util.Maps;

import static y.util.Generics.nodes;

/**
 * Created by chris on 24.05.2018.
 */
public class PlanarGraphTriangulation {

    private EdgeList artificialEdges;

    private Graph graph;

    private NodeMap discovery;
    private NodeMap lowNumber;

    public PlanarGraphTriangulation(Graph graph) {
        this.graph = graph;
        this.makeGraphBiconnected();
        this.triangulateGraph();
    }



    private void triangulateGraph(){
        PlanarInformation planarInformation = new PlanarInformation(graph);
        PlanarityTest pt = new PlanarityTest();
        pt.embed(planarInformation);

        FaceCursor fc = planarInformation.faces();

        for( FaceCursor f = fc; fc.ok(); fc.next()) {


            Face currentFace = f.face();


            EdgeList currentFaceEdges = new EdgeList(currentFace.edges());
            //System.out.println(currentFaceEdges);
            NodeList face = calculateFaceOrder(currentFaceEdges);
            //System.out.println("BEVORE REVERSE " + face);

            if (face.size() > 3) {

                Node minimumDegreeNodeInFace = minimumDegreeNode(face);
                //System.out.println("MIN DEG NODE: " + minimumDegreeNodeInFace);

                face = shiftFace(face, minimumDegreeNodeInFace);
                //System.out.println("AFTER SHIFT: " + face);

                Node v1 = (Node) face.get(0);
                NodeList adjV1 = adjacentNodeInFace(face, v1);


               // System.out.println(adjV1);

                if (adjV1.size() == 2) {

                    System.out.println("INCASE");
                    System.out.println(face);

                    for (int i = 2; i < face.size() - 1; i++) {
                        Node currentNode = (Node) face.get(i);
                        graph.createEdge(v1, currentNode);
                    }

                } else {
                    Node vj = null;
                    Node v2 = (Node) face.get(1);
                    Node vk = (Node) face.get(face.size() - 1);

                    for (int i = 0; i < adjV1.size(); i++) {
                        Node currentNode = (Node) adjV1.get(i);
                        if (currentNode != v2 && currentNode != vk) {
                            vj = currentNode;
                            break;
                        }
                    }

                    int indexJ = face.indexOf(vj);

                    for (int i = indexJ + 1; i < face.size(); i++) {
                        Node currentNode = (Node) face.get(i);
                        Edge createdEdge = graph.createEdge(v2, currentNode);
                        this.artificialEdges.add(createdEdge);
                    }


                    for (int i = 2; i < indexJ; i++) {

                        Node currentNode = (Node) face.get(i);
                        Node vj1 = (Node) face.get(indexJ + 1);

                        Edge createdEdge = graph.createEdge(vj1, currentNode);
                        this.artificialEdges.add(createdEdge);
                    }

                }

            }
        }

    }


    private NodeList adjacentNodeInFace( NodeList face, Node v1){

        NodeList result = new NodeList();

        NodeList neighbors = new NodeList(v1.neighbors());
        for( int i=0; i < neighbors.size(); i++){
            Node currentNeighbor = (Node) neighbors.get(i);
            if( face.contains(currentNeighbor) && !result.contains(currentNeighbor)){
                result.add(currentNeighbor);
            }
        }
        return result;
    }

    private NodeList shiftFace( NodeList face, Node minimumDegreeNode){

        NodeList result = new NodeList();

        int minimumDegreeIndex = face.indexOf(minimumDegreeNode);

        for( int i= minimumDegreeIndex; i < face.size(); i++){
            Node currentNode = (Node) face.get(i);
            result.add(currentNode);
        }

        for( int i=0; i < minimumDegreeIndex; i++){
            Node currentNode = (Node) face.get(i);
            result.add(currentNode);
        }

        return result;

    }


    private Node minimumDegreeNode( NodeList list){
        Node currentMinimumNode = null;
        int minDegree = Integer.MAX_VALUE;

        for( int i=0; i < list.size(); i++){
            Node currentNode = (Node) list.get(i);
            if( currentMinimumNode == null){
                currentMinimumNode = currentNode;
                minDegree = currentNode.degree();
            }
            else{
                if( currentNode.degree() < minDegree){
                    currentMinimumNode = currentNode;
                    minDegree = currentNode.degree();
                }
            }
        }
        return currentMinimumNode;
    }

    private NodeList calculateFaceOrder(EdgeList faceEdges){
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

    /*
    This implementation of a planar biconnected augmentation Algorithm is described in Tamassias
    Handbook of Graph Drawing. I modified an implementation of the function DFSMakeBiconnected
    from the Open Graph Drawing Framework ODGF. All credits belong to them
   http://www.ogdf.net/doc-ogdf/classogdf_1_1_dfs_make_biconnected.html
     */

    private void makeGraphBiconnected(){

        artificialEdges = new EdgeList();

        discovery = Maps.createHashedNodeMap();
        lowNumber = Maps.createHashedNodeMap();

        for( Node node: nodes(graph)){
            discovery.setInt(node,0);
        }

        Node start = graph.firstNode();
        biconnectWorker(start,null,0);

    }

    private void biconnectWorker(Node current, Node parent, int timestamp){
        timestamp = timestamp+1;

        discovery.setInt(current,timestamp);
        lowNumber.setInt(current,timestamp);


        NodeList neighbors = new NodeList(current.neighbors());

        Node previousUnvisitedNode = null;

        for( int i=0; i < neighbors.size() ; i++){

            Node currentNeighbor = (Node) neighbors.get(i);


            if( discovery.getInt(currentNeighbor) == 0){


                biconnectWorker(currentNeighbor,current,timestamp);

                int newLow = Integer.min(lowNumber.getInt(current),lowNumber.getInt(currentNeighbor));
                this.lowNumber.setInt(current,newLow);

                if( lowNumber.getInt(currentNeighbor) >= discovery.getInt(current)){

                    if( previousUnvisitedNode == null && parent !=null){
                        Edge artificialEdge = graph.createEdge(currentNeighbor,parent);
                        artificialEdges.add(artificialEdge);
                    }
                    else if( previousUnvisitedNode !=null){
                        Edge artificialEdge = graph.createEdge(currentNeighbor,previousUnvisitedNode);
                        artificialEdges.add(artificialEdge);
                    }

                }

                previousUnvisitedNode = currentNeighbor;

            }
            else if (  parent != null) {
                if( currentNeighbor != parent){
                    int newLow = Integer.min(lowNumber.getInt(current),discovery.getInt(currentNeighbor));
                    this.lowNumber.setInt(current,newLow);
                }
            }
        }
    }

    public EdgeList getArtificialEdges() {
        return artificialEdges;
    }
}
