package view;

import y.algo.Dfs;
import y.algo.IndependentSets;
import y.base.*;
import y.io.graphml.input.Directedness;
import y.util.GraphCopier;
import y.view.Graph2D;
import y.view.NodeRealizer;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by chris on 14.12.2017.
 */
public class IndependentSetView {

    private final Color VERTEX_COLOR = Color.RED;
    private final Color VERTEX_INDEPENDENT_COLOR = Color.BLUE;

    private Graph2D graph2D;
    private GraphView graphView;

    private boolean enableIndependentSetView;

    private ArrayList<Node> independentSet;

    public IndependentSetView(GraphView graphView) {
        this.graphView = graphView;
        this.graph2D = graphView.getGraph2D();
        this.enableIndependentSetView = false;
        this.independentSet = new ArrayList<>();
    }



    public void reset(){
        for( Node currentNode: independentSet){
            NodeRealizer nodeRealizer = this.graph2D.getRealizer(currentNode);
            nodeRealizer.setFillColor(VERTEX_COLOR);
        }
        independentSet.clear();
        this.graphView.updateView();
    }


    public void update(){
        if( enableIndependentSetView) {
            this.reset();
            this.calculateIndependentSet();
            for (Node currentNode : independentSet) {
                NodeRealizer nodeRealizer = this.graph2D.getRealizer(currentNode);
                nodeRealizer.setFillColor(VERTEX_INDEPENDENT_COLOR);
            }
            this.graphView.updateView();
        }
    }

    public void calculateIndependentSet(){
        if(enableIndependentSetView){

                NodeList independentSet = IndependentSets.getIndependentSet(this.graph2D);

                for (NodeCursor v = independentSet.nodes(); v.ok(); v.next()) {
                    this.independentSet.add(v.node());
                }
        }

    }

    public boolean isEnableIndependentSetView() {
        return enableIndependentSetView;
    }

    public void setEnableIndependentSetView(boolean enableIndependentSetView) {
        this.enableIndependentSetView = enableIndependentSetView;
    }
}
