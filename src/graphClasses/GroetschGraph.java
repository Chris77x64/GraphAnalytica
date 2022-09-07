package graphClasses;

import model.BasicGraph;
import view.GraphView;
import y.base.Node;
import y.layout.circular.CircularLayouter;
import y.layout.organic.OrganicLayouter;
import y.view.Graph2D;

/**
 * Created by chris on 25.01.2018.
 */
public class GroetschGraph {

    private GraphView graphView;
    private Graph2D graph2D;
    private BasicGraph basicGraph;


    private Node centerNode;

    private Node starTop;
    private Node starMiddleLeft;
    private Node starMiddleRight;
    private Node starBottomLeft;
    private Node starBottomRight;


    private Node outerTop;
    private Node outerMiddleLeft;
    private Node outerMiddleRight;
    private Node outerBottomLeft;
    private Node outerBottomRight;


    public GroetschGraph(GraphView graphView) {
        this.graphView = graphView;
        this.graph2D = graphView.getGraph2D();
        this.basicGraph = graphView.getGraph();

        this.basicGraph.resetGraph();
        this.initializeStarGraph();
        this.initializeOuterNodes();
        this.calculateLayout();

        this.graphView.fitContent();
        this.basicGraph.updateViews();
    }


    private void initializeStarGraph(){
        this.centerNode = basicGraph.createNode();
        this.starTop = basicGraph.createNode();
        this.starMiddleLeft = basicGraph.createNode();
        this.starMiddleRight = basicGraph.createNode();
        this.starBottomLeft = basicGraph.createNode();
        this.starBottomRight = basicGraph.createNode();

        this.basicGraph.createEdge(centerNode,starTop);
        this.basicGraph.createEdge(centerNode,starMiddleLeft);
        this.basicGraph.createEdge(centerNode,starMiddleRight);
        this.basicGraph.createEdge(centerNode,starBottomLeft);
        this.basicGraph.createEdge(centerNode,starBottomRight);
    }

    private void initializeOuterNodes(){
        outerTop = basicGraph.createNode();
        outerMiddleLeft = basicGraph.createNode();
        outerMiddleRight = basicGraph.createNode();
        outerBottomLeft = basicGraph.createNode();
        outerBottomRight = basicGraph.createNode();


        /*
        Edges from Star Graph to outer Vertices
         */
        this.basicGraph.createEdge(starTop,outerMiddleLeft);
        this.basicGraph.createEdge(starTop,outerMiddleRight);

        this.basicGraph.createEdge(starMiddleLeft,outerTop);
        this.basicGraph.createEdge(starMiddleLeft,outerBottomLeft);

        this.basicGraph.createEdge(starMiddleRight,outerTop);
        this.basicGraph.createEdge(starMiddleRight,outerBottomRight);

        this.basicGraph.createEdge(starBottomLeft,outerMiddleLeft);
        this.basicGraph.createEdge(starBottomLeft,outerBottomRight);

        this.basicGraph.createEdge(starBottomRight,outerMiddleRight);
        this.basicGraph.createEdge(starBottomRight,outerBottomLeft);

        /*
        Edges between outer vertices
         */

        this.basicGraph.createEdge(outerTop,outerMiddleLeft);
        this.basicGraph.createEdge(outerTop,outerMiddleRight);

        this.basicGraph.createEdge(outerMiddleLeft,outerBottomLeft);
        this.basicGraph.createEdge(outerMiddleRight,outerBottomRight);

        this.basicGraph.createEdge(outerBottomLeft,outerBottomRight);

    }

    private void calculateLayout(){
        /*
        OrganicLayouter organicLayouter = new OrganicLayouter();
        organicLayouter.setPreferredEdgeLength(400);
        organicLayouter.doLayout(graph2D);
        */
        CircularLayouter circularLayouter = new CircularLayouter();
        circularLayouter.doLayout(this.graph2D);
    }


}
