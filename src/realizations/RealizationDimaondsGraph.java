package realizations;

import view.GraphView;
import y.base.Edge;
import y.base.Node;
import y.base.NodeList;
import y.geom.YPoint;

import java.util.ArrayList;

import static y.util.Generics.nodes;

/**
 * Created by chris on 05.04.2018.
 */
public class RealizationDimaondsGraph  extends Realization{

    private final YPoint pStart = new YPoint(0,0);

    private final double L_S = 700;
    private final double L_L = 1000;

    private YPoint pS;
    private YPoint pT;

    private YPoint pTB;
    private YPoint pTT;
    private YPoint pTL;
    private YPoint pTR;

    private YPoint pBB;
    private YPoint pBT;
    private YPoint pBL;
    private YPoint pBR;

    private NodeList pathOrder;
    private Node s;
    private Node t;

    private ArrayList<Edge> longEdges;

    public RealizationDimaondsGraph(GraphView graphView) {
        super(graphView);
        this.initializeRealizationSpots();
        /*
        this.calculatePathOrder();
        this.initializeAssets();
        this.calculateRealization();
        this.graphView.fitContent();
        */
    }

    private void initializeAssets(){
        this.longEdges = graphView.getRelationships().getEdgesFromPartition(2);
    }

    private boolean isEdgeLong(Node source, Node target){
        Edge edge = source.getEdge(target);
        if( longEdges.contains(edge)){
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean isGraphType() {
        return false;
    }


    private boolean isOnTopSpots( YPoint point){
        if( pointsEqual(point,pTT) || pointsEqual(point,pTB) || pointsEqual(point,pTL) || pointsEqual(point,pTR)){
            return true;
        }
        else{
            return false;
        }
    }

    private YPoint topPlacement( boolean partition1 , boolean partition2){
        if( !partition1 && !partition2){
            return pTB;
        }
        else if( !partition1 && partition2){
            return pTL;
        }
        else if( partition1 && !partition2){
            return pTR;
        }
        else{
            return  pTT;
        }
    }

    private YPoint botPlacement( boolean partition1 , boolean partition2){
        if( !partition1 && !partition2){
            return pBT;
        }
        else if( !partition1 && partition2){
            return pBL;
        }
        else if( partition1 && !partition2){
            return pBR;
        }
        else{
            return  pBB;
        }
    }


    private void calculateRealization(){

        basicGraph.moveNode(s,pS);
        basicGraph.moveNode(t,pT);

        Node firstPathNode = (Node) pathOrder.get(0);

        YPoint destinationFirstPathNode = topPlacement(isEdgeLong(s,firstPathNode),isEdgeLong(t,firstPathNode));

        basicGraph.moveNode(firstPathNode,destinationFirstPathNode);

        for( int i= 1 ; i < pathOrder.size(); i++){
            Node previousNode = (Node) pathOrder.get(i-1);
            Node currentNode = (Node) pathOrder.get(i);

            YPoint previousPosition = graph2D.getCenter(previousNode);
            YPoint destination;

            if( !isEdgeLong(previousNode,currentNode)){

                if( isOnTopSpots(previousPosition)){
                    destination = topPlacement(isEdgeLong(s,currentNode),isEdgeLong(t,currentNode));
                }
                else{
                    destination = botPlacement(isEdgeLong(s,currentNode),isEdgeLong(t,currentNode));
                }
            }
            else{
                if( isOnTopSpots(previousPosition)){
                    destination = botPlacement(isEdgeLong(s,currentNode),isEdgeLong(t,currentNode));
                }
                else{
                    destination = topPlacement(isEdgeLong(s,currentNode),isEdgeLong(t,currentNode));
                }
            }
            basicGraph.moveNode(currentNode,destination);
        }
    }

    private void calculatePathOrder(){

        int n= graph2D.nodeCount();
        this.pathOrder = new NodeList();

        if( n == 4 ){

            NodeList st = new NodeList();

            for( Node node: nodes(graph2D)){
                if( node.degree() == 2){
                    st.add(node);
                }
                else{
                    pathOrder.add(node);
                }
            }
            this.s = (Node) st.get(0);
            this.t = (Node) st.get(1);
        }
        else if( n==5){

            Node deg3Node= null;

            for( Node node: nodes(graph2D)){
                if( node.degree() == 3){
                    deg3Node = node;
                    break;
                }

            }

            Node deg4Node = null;

            for( Node node: nodes(graph2D)){
                if( node.degree() == 4){
                    deg4Node = node;
                    break;
                }

            }

            NodeList deg4Neighbours = new NodeList(deg4Node.neighbors());
            NodeList deg3Neighbors = new NodeList(deg3Node.neighbors());

            NodeList st = new NodeList();

            for( int i=0; i < deg4Neighbours.size(); i++){
                Node currentNeighbour = (Node) deg4Neighbours.get(i);

                if( currentNeighbour.index() != deg3Node.index()){

                    if( !deg3Neighbors.contains(currentNeighbour)){
                        this.pathOrder.add(currentNeighbour);
                    }
                    else{
                        st.add(currentNeighbour);
                    }

                }
            }

            pathOrder.add(deg4Node);
            pathOrder.add(deg3Node);

            this.s = (Node) st.get(0);
            this.t = (Node) st.get(1);

        }
        else{
            NodeList deg3Nodes = new NodeList();

            for( Node node: nodes(graph2D)){
                if( node.degree() == 3){
                    deg3Nodes.add(node);
                }

            }

            Node topDeg3Node = (Node) deg3Nodes.get(0);
            NodeList topNeighbours = new NodeList(topDeg3Node.neighbors());
            Node botDeg3Node = (Node) deg3Nodes.get(1);
            NodeList botNeighbours = new NodeList(botDeg3Node.neighbors());

            NodeList st = new NodeList();

            for( int i=0; i <topNeighbours.size(); i++){
                Node currentNeighbour = (Node) topNeighbours.get(i);

                if( botNeighbours.contains(currentNeighbour)){
                    st.add(currentNeighbour);
                }
            }

            this.s = (Node) st.get(0);
            this.t = (Node) st.get(1);

            NodeList visited = new NodeList();
            visited.add(topDeg3Node);
            System.out.println(topDeg3Node);
            this.diamondAnalysisWorker(visited,topDeg3Node);
        }

        System.out.println(pathOrder);
        System.out.println(s);
        System.out.println(t);

    }

    private void diamondAnalysisWorker(NodeList visited, Node currentVertex){

        NodeList neighbors = new NodeList(currentVertex.neighbors());

        NodeList admissible = new NodeList();

        for( int i=0; i < neighbors.size(); i++){
            Node currentNeighbor = (Node) neighbors.get(i);

            if( currentNeighbor.index() != s.index() && currentNeighbor.index() != t.index() && !visited.contains(currentNeighbor)){
                admissible.add(currentNeighbor);
            }
        }

        if( admissible.size() == 0){
            this.pathOrder = visited;
        }
        else{
            Node newNode = (Node) admissible.get(0);
            visited.add(newNode);
            diamondAnalysisWorker(visited,newNode);
        }

    }

    private void initializeRealizationSpots(){

        this.pS = pStart;

        double distST = (L_S*Math.sin(Math.toRadians(116))) / Math.sin(Math.toRadians(32));

        this.pT = calculateIntersectionUpSide(pS,0,distST);

        this.pTB = calculateIntersectionUpSide(pS,32,L_S);
        this.pBT = calculateIntersectionDownSide(pS,32,L_S);

        double alpha = Math.toDegrees(Math.acos( Math.pow(distST,2)/ (2*distST*L_L) ) );

        this.pTT = calculateIntersectionUpSide(pS,alpha,L_L);
        this.pBB = calculateIntersectionDownSide(pS,alpha,L_L);

        double beta = alpha-32;

        double distTB = Math.sqrt( Math.pow(0.7*L_L,2)+Math.pow(L_L,2)-2*0.7*L_L*L_L*Math.cos(Math.toRadians(beta)));

        YPoint topMiddle = extendLineSegment(pTB,pTT,-0.5*distTB);

        this.pTR = calculateIntersectionUpSide(topMiddle,0,0.5*distTB);
        this.pTL = calculateIntersectionUpSide(topMiddle,180,0.5*distTB);

        YPoint botMiddle = extendLineSegment(pBB,pBT,-0.5*distTB);

        this.pBR = calculateIntersectionDownSide(botMiddle,0,0.5*distTB);
        this.pBL = calculateIntersectionDownSide(botMiddle,180,0.5*distTB);

        basicGraph.createNode(pS);
        basicGraph.createNode(pT);
        basicGraph.createNode(pTL);
        basicGraph.createNode(pTR);
        basicGraph.createNode(pTT);
        basicGraph.createNode(pTB);
        basicGraph.createNode(pBL);
        basicGraph.createNode(pBR);
        basicGraph.createNode(pBT);
        basicGraph.createNode(pBB);

    }


}
