package algo;

import y.base.Graph;
import y.base.Node;
import y.base.NodeList;
import y.base.NodeMap;
import y.util.Maps;

import java.util.Stack;

import static y.util.Generics.nodes;

/**
 * Created by chris on 23.05.2018.
 */
public class ArticulationPoints {

    private Graph graph;

    private NodeMap visited;
    private NodeMap discovery;
    private NodeMap parent;
    private NodeMap lowNumber;


    private NodeList articulationPoints;

    public ArticulationPoints(Graph graph) {
        this.graph = graph;
        this.calculateArticulationPoints();
        System.out.println(articulationPoints);
    }



    private void calculateArticulationPoints(){

        this.articulationPoints = new NodeList();

        visited = Maps.createHashedNodeMap();
        discovery = Maps.createHashedNodeMap();
        lowNumber = Maps.createHashedNodeMap();
        parent = Maps.createHashedNodeMap();

        for( Node node: nodes(graph)){
            visited.setBool(node,false);
            discovery.setInt(node,0);
        }

        Node start = graph.firstNode();
        articulationPointWorker(start,0);

        for( int i=0; i < articulationPoints.size(); i++){
            Node currentArticulationPoint = (Node) articulationPoints.get(i);
            System.out.println(currentArticulationPoint);
        }

    }


    private void articulationPointWorker(Node current, int timestamp){

        timestamp = timestamp+1;
        visited.set(current,true);

        discovery.setInt(current,timestamp);
        lowNumber.setInt(current,timestamp);


        int numChildren = 0;

        Node currentParent = (Node) parent.get(current);
        NodeList neighbors = new NodeList(current.neighbors());


        for( int i=0; i < neighbors.size() ; i++){

            Node currentNeighbor = (Node) neighbors.get(i);

            boolean isVisited = visited.getBool(currentNeighbor);


            if( ! isVisited){

                numChildren = numChildren +1;

                this.parent.set(currentNeighbor,current);

                articulationPointWorker(currentNeighbor,timestamp);

                int newLow = Integer.min(lowNumber.getInt(current),lowNumber.getInt(currentNeighbor));
                this.lowNumber.setInt(current,newLow);

                if( currentParent == null && numChildren >= 2){
                    articulationPoints.add(current);
                }
                else if( currentParent != null && lowNumber.getInt(currentNeighbor) >= discovery.getInt(current)){
                    articulationPoints.add(current);
                }

            }
            else if (  currentParent != null) {
                if( currentNeighbor != currentParent){
                    int newLow = Integer.min(lowNumber.getInt(current),discovery.getInt(currentNeighbor));
                    this.lowNumber.setInt(current,newLow);
                }
            }
        }

    }


    public NodeList getArticulationPoints() {
        return articulationPoints;
    }
}
