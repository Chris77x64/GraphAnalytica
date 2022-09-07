package algo;

import view.GraphView;
import y.base.Graph;
import y.base.Node;
import y.base.NodeList;

import java.util.ArrayList;
import java.util.Queue;

import static y.util.Generics.nodes;

/**
 * Created by chris on 29.05.2018.
 */
public class HanoiLabeling {


    private Graph graph;


    public HanoiLabeling(Graph graph) {
        this.graph = graph;

        NodeList vertices = new NodeList(graph.nodes());


        this.recursiveSplit(vertices);

    }


    private void recursiveSplit(NodeList vertices){

        ArrayList<NodeList> partitions = split(vertices);

        int size = partitions.get(0).size();

        if( size > 3){

            for( NodeList list: partitions){
                System.out.println(partitions);
                recursiveSplit(list);
            }

        }
        else{
            for(NodeList list: partitions){
                System.out.println(list);
            }
        }


    }



    private int subgraphDegree( Node vertex, NodeList subgraphVertices){

        int result = 0;

        NodeList neighbours = new NodeList(vertex.neighbors());

        for( int i=0; i < neighbours.size(); i++){
            Node currentNeighbour = (Node) neighbours.get(i);
            if( subgraphVertices.contains(currentNeighbour)){
                result = result + 1;
            }
        }

        return result;

    }


    private ArrayList<Node> subgraphNeighbours( Node vertex, NodeList subgraphVertices){

        ArrayList<Node> result = new ArrayList<>();

        NodeList neighbours = new NodeList(vertex.neighbors());

        for( int i=0; i < neighbours.size(); i++){
            Node currentNeighbour = (Node) neighbours.get(i);
            if( subgraphVertices.contains(currentNeighbour)){
                result.add(currentNeighbour);
            }
        }

        return result;
    }


    private int bfsDepth( int p){
        if( p == 2){
            return 1;
        }
        else{
            return (int) Math.pow(2,p-2)+bfsDepth(p-1);
        }
    }


    private NodeList depthLimitedBFS( Node start, NodeList vertices, int bound){
        NodeList visited = new NodeList();

        ArrayList<Node> queue = new ArrayList<>();
        queue.add(start);

        int current = 0;

        NodeList next = new NodeList();

        while( !queue.isEmpty()) {

            Node currentNode = queue.get(0);

            queue.remove(0);

            visited.add(currentNode);

            ArrayList<Node> admissibleNeighbours = subgraphNeighbours(currentNode,vertices);
            admissibleNeighbours.removeAll(visited);
            admissibleNeighbours.removeAll(queue);
            next.addAll(admissibleNeighbours);


            if( queue.isEmpty()){

                //System.out.println("CURRENT NODE:" +currentNode+" previous: "+previous+" NEXT: "+next);


                if( current == bound){
                    break;
                }
                else{
                    current = current +1;
                }

                queue.addAll(next);
                next = new NodeList();
            }
        }
        return visited;
    }

    private ArrayList<NodeList> bfsLayers( Node start, NodeList vertices, int bound){

        ArrayList<NodeList> layers = new ArrayList<>();

        ArrayList<Node> queue = new ArrayList<>();
        queue.add(start);

        NodeList next = new NodeList();
        NodeList previous = new NodeList();
        NodeList visited = new NodeList();

        int current = 0;

        while( !queue.isEmpty()){

            Node currentNode = queue.get(0);

            queue.remove(0);

            previous.add(currentNode);
            visited.add(currentNode);

            ArrayList<Node> admissibleNeighbours = subgraphNeighbours(currentNode,vertices);
            admissibleNeighbours.removeAll(visited);
            admissibleNeighbours.removeAll(queue);
            next.addAll(admissibleNeighbours);

            //System.out.println("CURRENT NODE:" +currentNode+ "ADMISSIBLE NEIGHBOURS:");
            //System.out.println(admissibleNeighbours);

            if( queue.isEmpty()){

                //System.out.println("CURRENT NODE:" +currentNode+" previous: "+previous+" NEXT: "+next);
                layers.add(previous);

                if( current == bound){
                    break;
                }
                else{
                    current = current +1;
                }

                queue.addAll(next);
                next = new NodeList();
                previous = new NodeList();
            }
        }

        return layers;

    }



    private ArrayList<NodeList> split( NodeList vertices){

        ArrayList<NodeList> result = new ArrayList<>();

        ArrayList<Node> criticalVertices = new ArrayList<>();

        for( int i=0; i < vertices.size(); i++){
            Node currentVertex = (Node) vertices.get(i);

            int subgraphDegree = subgraphDegree(currentVertex,vertices);

            if( subgraphDegree == 2){
                criticalVertices.add(currentVertex);
                System.out.println("VERTICES: "+vertices+ " CRITICAL VERTEX: "+currentVertex);
            }

        }
        int p = (int) (Math.log(vertices.size())/ Math.log(3));
        System.out.println(p);

        Node criticalVertex1 = criticalVertices.get(0);
        Node criticalVertex2 = criticalVertices.get(1);
        Node criticalVertex3 = criticalVertices.get(2);

        int depth = bfsDepth(p);
        System.out.println(depth+" p: "+p);



        NodeList set1 = depthLimitedBFS(criticalVertex1,vertices,depth);
        NodeList set2 = depthLimitedBFS(criticalVertex2,vertices,depth);
        NodeList set3 = depthLimitedBFS(criticalVertex3,vertices,depth);


        result.add(set1);
        result.add(set2);
        result.add(set3);

        return result;

    }





}
