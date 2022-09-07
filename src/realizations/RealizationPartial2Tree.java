package realizations;

import view.GraphView;
import y.base.Edge;
import y.base.Node;
import y.base.NodeList;
import y.util.GraphHider;
import y.util.YRandom;
import y.view.Graph2D;

import java.util.ArrayList;
import java.util.Collections;

import static y.util.Generics.nodes;

/**
 * Created by chris on 12.04.2018.
 */
public class RealizationPartial2Tree extends Realization{

    private ArrayList<Edge> artificialEdges;

    public RealizationPartial2Tree(GraphView graphView) {
        super(graphView);
        //fetchGraph();
        this.triangulate();

        for( Edge e : artificialEdges){
            YRandom random = new YRandom();
            int part = random.nextInt(1,3);
            this.graphView.getRelationships().insertEdgeInPartition(part,e);
        }

        Realization2Tree realization2Tree = new Realization2Tree(graphView);

        for( Edge e : artificialEdges){
            basicGraph.deleteEdge(e);
        }

    }

    private ArrayList<Node> eliminationOrder;


    private ArrayList<Node> calculateDeg2Nodes(){
        ArrayList<Node> deg2Nodes = new ArrayList<>();

        for( Node node: nodes(graph2D)){
            if( node.degree() == 2){
                deg2Nodes.add(node);
            }
        }
        return deg2Nodes;
    }


    private void fetchGraph(){
        for( Node node: nodes(graph2D)){
            if( node.degree() == 1){
                Node neighbour = (Node) new NodeList(node.neighbors()).get(0);

                NodeList neighbouradj = new NodeList(neighbour.neighbors());
                for( int i=0; i < neighbouradj.size() ; i++){
                    Node currentComparison = (Node) neighbouradj.get(i);
                    if( node.index() != currentComparison.index()){
                        basicGraph.createEdge(node,currentComparison);
                        break;
                    }
                }
            }
        }

    }


    private void triangulate(){

        fetchGraph();


        ArrayList<Node> deg2Nodes = calculateDeg2Nodes();
        this.eliminationOrder = new ArrayList<>();

        artificialEdges = new ArrayList<Edge>();


        GraphHider hider = new GraphHider(graph2D);

        while ( !deg2Nodes.isEmpty() && graph2D.nodeCount() > 2){

            Node currentNode = deg2Nodes.get(0);
            System.out.println("NODE AT CRASH"+currentNode+" "+currentNode.degree());

            NodeList currentNeighbors = new NodeList(currentNode.neighbors());
            System.out.println("NEIGHBOURS': "+currentNeighbors);

            Node y = (Node) currentNeighbors.get(0);
            Node z = (Node) currentNeighbors.get(1);

            Edge currentY = y.getEdge(currentNode);
            Edge currentZ = z.getEdge(currentNode);

            if( y.equals(z) ){
                System.out.println(y +" INDEX: "+y.index());
                System.out.println(z +" INDEX: "+z.index());
                break;
            }

            if( y.getEdge(z) == null) {
                Edge e = basicGraph.createEdgeReturned(y, z);
                artificialEdges.add(e);
            }





            hider.hide(currentY);
            hider.hide(currentZ);
            hider.hide(currentNode);

         //   deg2Nodes.remove(0);
            deg2Nodes = calculateDeg2Nodes();

            eliminationOrder.add(currentNode);


        }

         hider.unhideAll();

        Collections.reverse(eliminationOrder);
        String order = " ";
        for( int i=0; i < eliminationOrder.size(); i++){
            order += eliminationOrder.get(i).index();
        }
        System.out.println(order);






    }




    @Override
    public boolean isGraphType() {
        return false;
    }

}
