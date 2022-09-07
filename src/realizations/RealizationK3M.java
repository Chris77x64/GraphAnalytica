package realizations;

import view.GraphView;
import y.algo.Bipartitions;
import y.base.Edge;
import y.base.Node;
import y.base.NodeList;
import y.base.NodeMap;
import y.geom.YPoint;
import y.util.Maps;

import java.util.ArrayList;

import static y.algo.Bipartitions.RED;
import static y.util.Generics.nodes;

/**
 * Created by chris on 04.06.2018.
 */
public class RealizationK3M extends Realization {

    private final YPoint startPoint = new YPoint(0,0);

    private final double LENGTH_SHORT_EDGES = 200;
    private final double LENGTH_LONG_EDGES = 350;

    private NodeList uVertices;
    private NodeList vVertices;



    private ArrayList<Edge> longEdges;
    @Override
    public boolean isGraphType() {
        return false;
    }

    public RealizationK3M(GraphView graphView) {
        super(graphView);
        this.initializeAssets();
        this.initializePartitions();
        this.calculateRealization();
        graphView.fitContent();

    }

    private void calculateRealization(){

        Node u0 = (Node) uVertices.get(0);
        Node u1 = (Node) uVertices.get(1);
        Node u2 = (Node) uVertices.get(2);

        basicGraph.moveNode(u0,startPoint);

        YPoint posU1 = calculateIntersectionUpSide(startPoint,0,LENGTH_SHORT_EDGES);
        YPoint posU2 = calculateCircleCircleIntersection(startPoint,LENGTH_SHORT_EDGES,posU1,LENGTH_SHORT_EDGES)[0];

        basicGraph.moveNode(u1,posU1);
        basicGraph.moveNode(u2,posU2);

        for( int i=0; i < vVertices.size(); i++){
            Node currentNode = (Node) vVertices.get(i);

            double l1 = varphi(currentNode,u0);
            double l2 = varphi(currentNode,u1);

            YPoint[] intersections = calculateCircleCircleIntersection(startPoint,l1,posU1,l2);

            YPoint destination;
            if( isEdgeLong(currentNode,u2)){
                destination = intersections[1];
            }
            else{
                destination = intersections[0];
            }
            basicGraph.moveNode(currentNode,destination);

        }
    }

    private void initializePartitions(){
        this.longEdges = graphView.getRelationships().getEdgesFromPartition(2);
    }

    private boolean isEdgeLong(Node source,Node target){
        Edge currentEdge = source.getEdge(target);
        if( longEdges.contains(currentEdge)){
            return true;
        }
        else{
            return false;
        }
    }

    private double varphi( Node source, Node target){
        if( isEdgeLong(source,target)){
            return LENGTH_LONG_EDGES;
        }
        else{
            return LENGTH_SHORT_EDGES;
        }
    }


    private void initializeAssets() {

        uVertices = new NodeList();
        vVertices = new NodeList();


        NodeMap vertexMap = Maps.createHashedNodeMap();
        boolean twoColorable = Bipartitions.getBipartition(graph2D, vertexMap);
        if (twoColorable) {

            for (Node node : nodes(graph2D)) {

                Object color = vertexMap.get(node);
                if (color == RED) {
                    uVertices.add(node);
                } else {
                    vVertices.add(node);
                }

            }
        }

        if( vVertices.size() == 3){
            NodeList temp = uVertices;
            uVertices = vVertices;
            vVertices = temp;
        }
    }

}
