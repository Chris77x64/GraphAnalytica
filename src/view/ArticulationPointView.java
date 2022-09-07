package view;

import algo.ArticulationPoints;
import y.base.Graph;
import y.base.Node;
import y.base.NodeList;
import y.view.Graph2D;
import y.view.NodeRealizer;

import java.awt.*;

/**
 * Created by chris on 01.06.2018.
 */
public class ArticulationPointView {

    private final Color ARTICULATION_POINT_COLOR = Color.blue;

    private GraphView graphView;
    private Graph2D graph2D;

    NodeList articulationPoints;

    public ArticulationPointView(GraphView graphView) {
        this.graphView = graphView;
        this.graph2D = graphView.getGraph2D();
    }

    private void initializeArticulationPoints(){
        ArticulationPoints articulationPointsWorker = new ArticulationPoints(graph2D);
        this.articulationPoints = articulationPointsWorker.getArticulationPoints();
    }

    private void colorArticulationPoints(){
        for( int i=0; i< articulationPoints.size(); i++){
            Node currentNode = (Node) articulationPoints.get(i);
            NodeRealizer realizer = graph2D.getRealizer(currentNode);
            realizer.setFillColor(ARTICULATION_POINT_COLOR);
        }
        graphView.updateView();
    }

    public void visualizeArticulationPoints(){
        this.initializeArticulationPoints();
        this.colorArticulationPoints();
    }


}
