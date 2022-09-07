package realizations;

import model.EdgeInducedSubgraph;
import view.GraphView;
import view.error.RealizationErrorView;
import y.algo.Bfs;
import y.algo.Trees;
import y.base.*;
import y.geom.YPoint;
import y.util.Maps;
import java.util.ArrayList;

/**
 * Created by chris on 16.01.2018.
 */
public class RealizationRootedTree extends Realization {

    private final double LENGTH_SHORT_EDGES = 200;

    private EdgeInducedSubgraph shortEdgesInducedSubgraph;
    private Graph inducedSubgraph;

    private NodeList[] bfsLayers;
    private NodeMap nodeHeight;

    private ArrayList<YPoint> realizationSpots;

    public RealizationRootedTree(GraphView graphView) {
        super(graphView);

        this.initializeInducedSubgraph();




            if (isGraphType()) {
                this.initializeRealizationSpots();
                this.calculateRealization();
                this.graphView.fitContent();
                this.basicGraph.updateViews();
            }

    }


    private void calculateRealization() {

        for (int i = 0; i < bfsLayers.length; i++) {
            YPoint realizationSpot = realizationSpots.get(i);

            NodeList currentNodeList = bfsLayers[i];

            if (i == 0) {
                Node currentNode = (Node) currentNodeList.get(0);
                this.basicGraph.moveNode(currentNode, realizationSpot);
            } else {

                this.genericSmallNeighbourhoodPlacement(currentNodeList, realizationSpot);
            }
        }
    }

    private void initializeRealizationSpots() {
        double worldHeight = this.graphView.getWorldRect2D().getHeight();
        double worldWidth = this.graphView.getWorldRect2D().getWidth();

        this.realizationSpots = new ArrayList<>();

        YPoint startPoint = new YPoint((1 / 3) * worldWidth, (1 / 2) * worldHeight);
        realizationSpots.add(startPoint);

        for (int i = 1; i < bfsLayers.length; i++) {
            YPoint previousRealizationSpot = realizationSpots.get(i - 1);
            YPoint nextRealizationSpot = calculateIntersectionUpSide(previousRealizationSpot, 0, LENGTH_SHORT_EDGES);
            realizationSpots.add(nextRealizationSpot);
        }

    }

    private void initializeInducedSubgraph() {
        ArrayList<Edge> shortEdges = this.graphView.getRelationships().getPartitions().get(1);
        this.shortEdgesInducedSubgraph = new EdgeInducedSubgraph(this.graph2D, shortEdges);
        this.inducedSubgraph = shortEdgesInducedSubgraph.getInducedSubgraph();
    }


    private void initializeNodeMap() {

        Node root = Trees.getRoot(inducedSubgraph);

        NodeList coreNodes = new NodeList();
        coreNodes.add(root);

        NodeList[] inducedBFSLayers = Bfs.getLayers(inducedSubgraph, coreNodes);
        this.bfsLayers = new NodeList[inducedBFSLayers.length];

        this.nodeHeight = Maps.createHashedNodeMap();

        for (int i = 0; i < inducedBFSLayers.length; i++) {

            NodeList currentNodeList = inducedBFSLayers[i];
            NodeList currentLayer = new NodeList();

            for (NodeCursor v = currentNodeList.nodes(); v.ok(); v.next()) {
                Node currentInducedNode = v.node();
                Node currentNode = this.shortEdgesInducedSubgraph.getGraphNodeByInducedNode(currentInducedNode);

                nodeHeight.setInt(currentNode, i);
                currentLayer.add(currentNode);
            }
            bfsLayers[i] = currentLayer;
        }

    }

    private boolean isDepthRequirementFulfilled() {
        ArrayList<Edge> longEdges = this.graphView.getRelationships().getPartitions().get(2);
        for (Edge edge : longEdges) {

            Node source = edge.source();
            Node target = edge.target();

            int heightSource = nodeHeight.getInt(source);
            int heightTarget = nodeHeight.getInt(target);

            int difference = Math.abs(heightSource - heightTarget);

            if (difference <= 1) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean isGraphType() {

        if (isDichotomos()) {

            if (Trees.isTree(inducedSubgraph)) {

                ArrayList<Edge> shortEdges = graphView.getRelationships().getEdgesFromPartition(1);
                if( shortEdges.size() != inducedSubgraph.edgeCount() || shortEdges.isEmpty() ){
                    RealizationErrorView error = new RealizationErrorView(RealizationError.ERROR_PARTITION);
                    return false;
                }
                else{
                    this.initializeNodeMap();
                    if (isDepthRequirementFulfilled()) {
                        return true;
                    } else {
                        RealizationErrorView error = new RealizationErrorView(RealizationError.ERROR_HEIGHT_LONG_EDGES);
                        System.out.println("HEIGHT VIOLATION !");
                        return false;
                    }
                }
            } else {
                RealizationErrorView error = new RealizationErrorView(RealizationError.ERROR_SUBGRAPH_INDUCED_SHORT_EDGES_IS_NOT_A_TREE);
                return false;
            }
        } else {
            RealizationErrorView error = new RealizationErrorView(RealizationError.ERROR_PARTITION);
            return false;
        }
    }
}
