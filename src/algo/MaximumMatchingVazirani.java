package algo;

import view.GraphView;
import y.base.*;
import y.view.Graph2D;

/**
 * Created by chris on 10.05.2019.
 */
public class MaximumMatchingVazirani {


    private Graph2D graph;
    private GraphView graphView;


    // respective min and max levels
    private NodeMap minLevel;
    private NodeMap maxLevel;
    // predecessors
    private NodeMap predecessors;

    // boolean edge map that indicates if a edge is processed yet
    private EdgeMap processedEdges; //scanned

    // Boolean edge mappings indicating wheter given edge is a prob or a bridge
    private EdgeMap edgeProp;
    private EdgeMap edgeBridge;

    private NodeMap discovery;

    public MaximumMatchingVazirani(GraphView graphView){
        this.graphView = graphView;
        this.graph = graphView.getGraph2D();
    }

    private void initializeAssets(){

    }

    /*
    private void MIN(int i){

        //if i even
        if ( i % 2 == 0){

        }
        //if i odd
        else{

        }


        Node u;
        NodeList uNeighbours = new NodeList(u.neighbors());

        for ( int k= 0; k < uNeighbours.size(); k++){
            Node currentNode = (Node) uNeighbours.get(k);
            Edge currentEdge =  u.getEdge(currentNode);

            boolean alreadyProcessed = processedEdges.getBool(currentEdge);
            if ( ! alreadyProcessed){

                int currentNodeMinLevel = minLevel.getInt(currentNode);

                if (currentNodeMinLevel >= i+1){
                    minLevel.setInt(currentNode,i+1);

                    NodeList list = (NodeList) predecessors.get(currentNode);
                    list.add(u);

                    edgeProp.setBool(currentEdge,true);
                    edgeBridge.setBool(currentEdge,false);
                }
                else{
                    edgeBridge.setBool(currentEdge,true);
                    edgeProp.setBool(currentEdge,false);
                }

            }
        }
    }


*/
}
