package view;

import model.BasicGraph;
import view.error.LayoutErrorView;
import y.layout.circular.CircularLayouter;
import y.layout.organic.OrganicLayouter;
import y.layout.organic.SmartOrganicLayouter;
import y.layout.radial.RadialLayouter;
import y.layout.tree.BalloonLayouter;
import y.layout.tree.TreeLayouter;
import y.view.Graph2D;

/**
 * Created by chris on 05.02.2018.
 */
public class LayoutView {

    private final double PREFERRED_EDGE_LENGTH_ORGANIC = 200;

    private final double TREE_MINIMAL_LAYER_DISTANCE = 200;
    private final double TREE_MINIMAL_NODE_DISTANCE = 200;

    private GraphView graphView;
    private Graph2D graph2D;
    private BasicGraph basicGraph;

    public LayoutView( GraphView graphView){
        this.graphView = graphView;
        this.graph2D = graphView.getGraph2D();
        this.basicGraph = graphView.getGraph();
    }


    private void update(){
        this.basicGraph.updateViews();
        this.graphView.fitContent();
    }

    public void applyOrganicLayout(){
        OrganicLayouter organicLayouter = new OrganicLayouter();
        if( organicLayouter.canLayout(graph2D)) {
            organicLayouter.doLayout(this.graph2D);
            this.update();
        }
        else{
            LayoutErrorView layoutErrorView = new LayoutErrorView();
        }
    }

    public void applySmartOrganicLayout(){
        SmartOrganicLayouter smartOrganicLayouter = new SmartOrganicLayouter();
        smartOrganicLayouter.setPreferredEdgeLength(PREFERRED_EDGE_LENGTH_ORGANIC);
        if( smartOrganicLayouter.canLayout(graph2D)) {
            smartOrganicLayouter.doLayout(graph2D);
            this.update();
        }
        else{
            LayoutErrorView layoutErrorView = new LayoutErrorView();
        }
    }

    public void applyRadialLayout(){
        RadialLayouter radialLayouter = new RadialLayouter();
        radialLayouter.setEdgeRoutingStrategy(RadialLayouter.EDGE_ROUTING_STRATEGY_POLYLINE);
        if( radialLayouter.canLayout(graph2D)) {
            radialLayouter.doLayout(graph2D);
            this.update();
        }
        else{
            LayoutErrorView layoutErrorView = new LayoutErrorView();
        }
    }

    public void applyBalloonLayout(){
        BalloonLayouter balloonLayouter = new BalloonLayouter();
        balloonLayouter.setRootNodePolicy(BalloonLayouter.WEIGHTED_CENTER_ROOT);
        if( balloonLayouter.canLayout(graph2D)) {
            balloonLayouter.doLayout(this.graph2D);
            this.update();
        }
        else{
            LayoutErrorView layoutErrorView = new LayoutErrorView();
        }
    }

    public void applyCircularLayout(){
        CircularLayouter circularLayouter = new CircularLayouter();
        if( circularLayouter.canLayout(graph2D)) {
            circularLayouter.doLayout(this.graph2D);
            this.update();
        }
        else{
            LayoutErrorView layoutErrorView = new LayoutErrorView();
        }
    }

    public void applyRootedTreeLayout(){
        TreeLayouter treeLayouter = new TreeLayouter();
        treeLayouter.setMinimalLayerDistance(TREE_MINIMAL_LAYER_DISTANCE);
        treeLayouter.setMinimalNodeDistance(TREE_MINIMAL_NODE_DISTANCE);
        if( treeLayouter.canLayout(graph2D)) {
            treeLayouter.doLayout(graph2D);
            this.update();
        }
        else{
            LayoutErrorView layoutErrorView = new LayoutErrorView();
        }
    }


}
