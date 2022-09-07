package algo;

import model.BasicGraph;
import view.GraphView;
import y.algo.GraphChecker;
import y.algo.GraphConnectivity;
import y.base.*;
import y.geom.YPoint;
import y.util.Maps;
import y.view.Graph2D;

import java.util.Collections;
import java.util.Comparator;

import static y.util.Generics.nodes;

/**
 * Created by chris on 24.05.2018.
 */
public class PlanarShiftGridDrawing {

    private Graph2D graph;
    private BasicGraph basicGraph;
    private GraphView graphView;

    private NodeList canonicalOrdering;

    private EdgeList artificialEdges;

    private NodeMap xOffset;

    private NodeMap yPosition;
    private NodeMap xPosition;

    private NodeMap left;
    private NodeMap right;

    public PlanarShiftGridDrawing(GraphView graphView) {

        this.graphView = graphView;
        this.basicGraph = graphView.getGraph();
        this.graph = graphView.getGraph2D();




        if( isPlanar() && isSimplyConnected()) {

           this.applyPreprocessing();
           this.shiftMethod();

            this.basicGraph.updateViews();
            this.graphView.fitContent();



        }


    }


    private YPoint perpendicularSlope(YPoint p1, YPoint p2){

        double point1X = p1.getX();
        double point1Y = p1.getY();
        double point2X = p2.getX();
        double point2Y = p2.getY();

        double resultX = (point2X+point1X+point2Y-point1Y)/2;
        double resultY = (point2X-point1X+point1Y+point2Y)/2;

        return new YPoint(resultX,resultY);

    }


    private void acumulateOffset(Node current,double xValue){
        if( current != null){
            double newX = this.xOffset.getDouble(current)+xValue;
            this.xPosition.setDouble(current,newX);

            Node left = (Node) this.left.get(current);
            Node right = (Node) this.right.get(current);

            System.out.println("CURENT" +current+" LEFT "+ left+ " RIGHT: "+right);

            acumulateOffset(left,newX);
            acumulateOffset(right,newX);
        }
    }

    private NodeList orderedNeighbors(NodeList outerCycle, Node node2Add){

        NodeList result = new NodeList();

        NodeList neighbors = new NodeList(node2Add.neighbors());

        for( int i=0; i < neighbors.size(); i++ ){

            Node currentNeighbor = (Node) neighbors.get(i);

            if( outerCycle.contains(currentNeighbor) && !result.contains(currentNeighbor)){
                result.add(currentNeighbor);
            }
        }

        result.sort(new Comparator() {
            @Override
            public int compare(Object o1, Object o2) {
                Node n1 = (Node) o1;
                Node n2 = (Node) o2;

                if(outerCycle.indexOf(n1) < outerCycle.indexOf(n2)){
                    return -1;
                }
                else{
                    return 1;
                }
            }
        });

        return result;
    }


    private NodeList updateOuterFace( NodeList oldFace, NodeList adj, Node newVertex, Node v1,Node v2){

        NodeList result = new NodeList();

        Node firstADJ = (Node) adj.get(0);

        if( firstADJ == v1){

            adj.remove(v1);
            adj.remove(v2);

            oldFace.removeAll(adj);

            result.add(v1);
            result.add(newVertex);

            for( int i=1 ; i < oldFace.size(); i++){
                Node currentNode = (Node) oldFace.get(i);
                result.add(currentNode);
            }

            return result;
        }
        else{

            int firstAdjIndex = oldFace.indexOf(firstADJ);

            adj.remove(v1);
            adj.remove(v2);
            oldFace.removeAll(adj);

            for( int i=0; i < firstAdjIndex; i++){
                Node currentNode = (Node) oldFace.get(i);
                result.add(currentNode);
            }
            result.add(newVertex);

            for( int i= firstAdjIndex ; i < oldFace.size(); i++){
                Node currentNode = (Node) oldFace.get(i);
                result.add(currentNode);
            }
            return result;

        }




    }

    private void shiftMethod(){

        Node v1 = (Node) canonicalOrdering.get(0);
        Node v2 = (Node) canonicalOrdering.get(1);
        Node v3 = (Node) canonicalOrdering.get(2);

        this.xOffset.setDouble(v1,0);
        this.xOffset.setDouble(v2,100);
        this.xOffset.setDouble(v3,100);

        this.yPosition.setDouble(v1,0);
        this.yPosition.setDouble(v2,0);
        this.yPosition.setDouble(v3,100);

        this.right.set(v1,v3);
        this.right.set(v2,null);
        this.right.set(v3,v2);

        this.left.set(v1,null);
        this.left.set(v2,null);
        this.left.set(v3,null);

        NodeList outerFace = new NodeList();
        outerFace.add(v1);
        outerFace.add(v3);
        outerFace.add(v2);



        for( int i=3; i < canonicalOrdering.size(); i++){

            Node currentVertex = (Node) canonicalOrdering.get(i);

            NodeList neighboursFace = orderedNeighbors(outerFace,currentVertex);

        //    System.out.println("-----------------------------");
         //   System.out.println("NEIGHBOURSFACE: "+neighboursFace);

            Node vp = (Node) neighboursFace.get(0);
            Node vpNext = (Node) neighboursFace.get(1);

            Node vq = (Node) neighboursFace.get(neighboursFace.size()-1);


//            System.out.println("CURRENT: "+currentVertex+" "+" outerFace: "+outerFace+" neighboursface: "+neighboursFace+ "VP: "+vp+" vpNext: "+vpNext+" VQ:"+vq);

            this.xOffset.setDouble(vpNext,this.xOffset.getDouble(vpNext)+100);
            this.xOffset.setDouble(vq,this.xOffset.getDouble(vq)+100);

            double offset = 0;

            int p = outerFace.indexOf(vp);
            int q = outerFace.indexOf(vq);

            for( int j=p; j < neighboursFace.size(); j++){
                Node currentNeighbor = (Node) neighboursFace.get(j);
                offset += this.xOffset.getDouble(currentNeighbor);
            }

            double currentXOffset = ( offset+ this.yPosition.getDouble(vq)-this.yPosition.getDouble(vp))/2;
            this.xOffset.setDouble(currentVertex,currentXOffset );
            double currentYOffset = ( offset+ this.yPosition.getDouble(vq)+this.yPosition.getDouble(vp))/2;
            this.yPosition.setDouble(currentVertex,currentYOffset);


            this.right.set(vp,currentVertex);
            if( p+1 != q){
                this.left.set(currentVertex,vpNext);
            }
            else{
                this.left.set(currentVertex,null);
            }

            this.right.set(currentVertex,vq);

            if( q-1 != p){
                Node qPrevious = (Node) neighboursFace.get(neighboursFace.size()-2);
                this.right.set(qPrevious,null);
            }

            this.xOffset.setDouble(vq,offset-currentXOffset);

            if( p+1 != q){
                this.xOffset.setDouble(vpNext,this.xOffset.getDouble(vpNext)-currentXOffset);
            }

            outerFace = updateOuterFace(outerFace,neighboursFace,currentVertex,v1,v2);
        }

        this.xPosition.setDouble(v1,0);
        acumulateOffset(v1,0);

        for( Node node: nodes(graph)){
            double nodeX = this.xPosition.getDouble(node);
            double nodeY = this.yPosition.getDouble(node);

            YPoint destination = new YPoint(nodeX,nodeY);
            basicGraph.moveNode(node,destination);
        }

        for( int i=0; i < artificialEdges.size(); i++){
            Edge currentEdge = (Edge) artificialEdges.get(i);
            graph.removeEdge(currentEdge);
        }

    }


    private void applyPreprocessing(){

        PlanarGraphTriangulation triangulation = new PlanarGraphTriangulation(graph);

        this.artificialEdges = triangulation.getArtificialEdges();


        CanonicalVertexOrder canonicalVertexOrder = new CanonicalVertexOrder(graph);

        this.canonicalOrdering = canonicalVertexOrder.getCanonicalOrdering();
        System.out.println("______________________________");
        System.out.println(canonicalOrdering);
        System.out.println("______________________________");


        this.xOffset = Maps.createHashedNodeMap();
        this.xPosition = Maps.createHashedNodeMap();
        this.yPosition = Maps.createHashedNodeMap();
        this.left = Maps.createHashedNodeMap();
        this.right = Maps.createHashedNodeMap();


    }

    private boolean isPlanar(){
        return GraphChecker.isPlanar(graph);
    }

    private boolean isSimplyConnected(){
        return GraphConnectivity.isConnected(graph);
    }

}
