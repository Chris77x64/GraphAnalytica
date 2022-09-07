package algo;

import y.base.Graph;
import y.base.Node;
import y.base.NodeCursor;
import y.base.NodeList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static y.util.Generics.nodes;

/**
 * Created by chris on 16.02.2018.
 */
public class TwoTreeLexBFS {

    private Graph graph;
    private Node startNode;

    private ArrayList<Node> lexicographicalOrder;

    public TwoTreeLexBFS(Graph graph, Node startNode) {
        this.graph = graph;
        this.startNode = startNode;
        this.calculateLexicographicalOrder();
    }

    private void calculateLexicographicalOrder(){

        NodeList firstNodeNeighbors = new NodeList(startNode.neighbors());
        Node secondNode = (Node) firstNodeNeighbors.get(0);

        ArrayList<Node> finished = new ArrayList<>();
        finished.add(startNode);
        finished.add(secondNode);

        ArrayList<Node> processing = new ArrayList<>();
        for( Node vertex: nodes(graph)){
            if( !finished.contains(vertex)){
                processing.add(vertex);
            }
        }

        while( finished.size() != graph.nodeCount()){

            Collections.sort(processing, new Comparator<Node>() {
                @Override
                public int compare(Node o1, Node o2) {

                    NodeList o1Neighbors = new NodeList(o1.neighbors());
                    NodeList o2Neighbors = new NodeList(o2.neighbors());

                    return compareNeighbors(o1Neighbors, o2Neighbors,finished);
                }
            });

            Node nextNode = processing.get(processing.size()-1);
            finished.add(nextNode);

            processing.remove(nextNode);
        }

        this.lexicographicalOrder = finished;
    }

    private int compareNeighbors(NodeList o1Neighbors,NodeList o2Neighbors, ArrayList<Node> finished){

        int o1Count = 0;
        int o2Count = 0;

        for(NodeCursor v = o1Neighbors.nodes(); v.ok(); v.next()){
            Node currentNode = v.node();
            if( finished.contains(currentNode)){
                o1Count++;
            }
        }
        for(NodeCursor v = o2Neighbors.nodes(); v.ok(); v.next()){
            Node currentNode = v.node();
            if( finished.contains(currentNode)){
                o2Count++;
            }
        }
        if( o1Count > o2Count){
            return 1;
        }
        else if( o1Count < o2Count){
            return -1;
        }
        else{
            return 0;
        }

    }

    public ArrayList<Node> getLexicographicalOrder() {
        return lexicographicalOrder;
    }
}
