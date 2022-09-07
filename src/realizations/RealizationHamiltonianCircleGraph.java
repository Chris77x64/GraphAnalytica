package realizations;

import model.EdgeInducedSubgraph;
import view.GraphView;
import view.error.RealizationErrorView;
import y.algo.Cycles;
import y.base.Edge;
import y.base.EdgeList;
import y.base.Graph;
import y.base.NodeList;
import y.geom.YPoint;

import java.util.ArrayList;

/**
 * Created by chris on 16.01.2018.
 */
public class RealizationHamiltonianCircleGraph extends Realization {

    private final double SHORT_EDGE_SCALING_FACTOR = 30;

    private EdgeInducedSubgraph shortEdgesInducedSubgraph;
    private Graph inducedSubgraph;

    private YPoint centerPoint;

    public RealizationHamiltonianCircleGraph(GraphView graphView) {
        super(graphView);

        this.initializeInducedSubgraph();

        if( isGraphType()) {

            this.calculateRealization();

            this.graphView.fitContent();
            this.basicGraph.updateViews();
        }

    }

    private void calculateRealization(){
        double worldHeight = this.graphView.getWorldRect2D().getHeight();
        double worldWidth = this.graphView.getWorldRect2D().getWidth();
        this.centerPoint = new YPoint(0.5*worldWidth,0.5*worldHeight);

        double lengthShortEdges = SHORT_EDGE_SCALING_FACTOR * graph2D.nodeCount();

        NodeList nodes = new NodeList(graph2D.nodes());
        this.genericSmallNeighbourhoodPlacement(nodes,centerPoint,lengthShortEdges);
    }

    @Override
    public boolean isGraphType() {

        if( inducedSubgraph != null) {
            if (isDichotomos()) {

                EdgeList shortCycleEdges = Cycles.findCycle(inducedSubgraph, false);

                if (shortCycleEdges.size() == graph2D.nodeCount()) {
                    return true;
                } else {
                    RealizationErrorView error = new RealizationErrorView(RealizationError.ERROR_HAMILTONIAN);
                    return false;
                }
            } else {
                RealizationErrorView error = new RealizationErrorView(RealizationError.ERROR_PARTITION);
                return false;
            }
        }
        else{
            RealizationErrorView error = new RealizationErrorView(RealizationError.ERROR_PARTITION);
            return false;
        }

    }

    private void initializeInducedSubgraph(){
        ArrayList<Edge> shortEdges = this.graphView.getRelationships().getPartitions().get(1);
        this.shortEdgesInducedSubgraph = new EdgeInducedSubgraph(this.graph2D,shortEdges);
        this.inducedSubgraph = shortEdgesInducedSubgraph.getInducedSubgraph();
    }

}
