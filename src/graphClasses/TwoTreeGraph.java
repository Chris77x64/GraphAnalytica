package graphClasses;

import algo.KColoring;
import algo.TwoTreeLexBFS;
import model.BasicGraph;
import view.GraphView;
import y.base.Edge;
import y.base.Node;
import y.base.NodeCursor;
import y.base.NodeList;
import y.geom.YPoint;
import y.layout.circular.CircularLayouter;
import y.layout.hierarchic.IncrementalHierarchicLayouter;
import y.layout.hierarchic.incremental.EdgeLayoutDescriptor;
import y.layout.organic.OrganicLayouter;
import y.layout.orthogonal.OrthogonalLayouter;
import y.layout.partial.PartialLayouter;
import y.layout.radial.RadialLayouter;
import y.layout.random.RandomLayouter;
import y.layout.seriesparallel.SeriesParallelLayouter;
import y.layout.transformer.GraphTransformer;
import y.layout.tree.BalloonLayouter;
import y.module.BalloonLayoutModule;
import y.module.CompactOrthogonalLayoutModule;
import y.module.PartialLayoutModule;
import y.module.RadialLayoutModule;
import y.util.YRandom;
import y.view.Graph2D;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;

/**
 * Created by chris on 09.01.2018.
 */
public class TwoTreeGraph {


    private GraphView graphView;
    private Graph2D graph2D;
    private BasicGraph basicGraph;

    private int numVertices;
    private NodeList vertices;

    private ArrayList<Edge> edges;

    public TwoTreeGraph(int numVertices,GraphView graphView) {
        this.graphView = graphView;
        this.graph2D = this.graphView.getGraph2D();
        this.basicGraph = this.graphView.getGraph();
        this.basicGraph.resetGraph();

        this.numVertices = numVertices;
        this.initializeGraph();
        //this.initializeNodes();
        //this.initializeEdges();
        this.initializeLayout();


        this.graphView.fitContent();
        this.basicGraph.updateViews();

        TwoTreeLexBFS twoTreeLexBFS = new TwoTreeLexBFS(graph2D,graph2D.firstNode());

    }

    private void initializeGraph(){

        edges = new ArrayList<>();

        Node firstVertex = basicGraph.createNode();
        Node secondVertex = basicGraph.createNode();

        Edge firstEdge = basicGraph.createEdgeReturned(firstVertex,secondVertex);
        edges.add(firstEdge);

        for( int i=0; i < numVertices -2; i++){
            YRandom random = new YRandom();
            int edgeIndex = random.nextInt(0,edges.size());

            Edge selectedEdge = edges.get(edgeIndex);
            Node source = selectedEdge.source();
            Node target = selectedEdge.target();

            Node createdNode = basicGraph.createNode();
            Edge createdNodeSource = basicGraph.createEdgeReturned(createdNode,source);
            Edge createdNodeTarget = basicGraph.createEdgeReturned(createdNode,target);

            edges.add(createdNodeSource);
            edges.add(createdNodeTarget);

        }


    }

    private void initializeNodes(){
        this.vertices = new NodeList();

        for( int i= 0; i < numVertices ; i++ ){
            Node currentNode = basicGraph.createNode();
            vertices.add(currentNode);
        }

    }

    private void initializeEdges(){

        // generate K_2
        Node firstVertex = (Node) vertices.get(0);
        Node secondVertex = (Node) vertices.get(1);

        this.basicGraph.createEdge(firstVertex,secondVertex);

        for( int i = 2 ; i < vertices.size() ; i++){

            Node currentNode = (Node) vertices.get(i);
            Node previousNode = (Node) vertices.get(i-1);
            Node previousPreviousNode = (Node) vertices.get(i-2);

            this.basicGraph.createEdge(currentNode,previousNode);
            this.basicGraph.createEdge(currentNode,previousPreviousNode);
        }

    }

    private void initializeLayout(){

        double worldHeight = this.graphView.getWorldRect2D().getHeight();
        double worldWidth = this.graphView.getWorldRect2D().getWidth();

        Rectangle worldBoundingBox = new Rectangle(10,10,(int) worldWidth-20, (int) worldHeight-20);

       // RadialLayouter radialLayouter = new RadialLayouter();
       // radialLayouter.setEdgeRoutingStrategy(RadialLayouter.EDGE_ROUTING_STRATEGY_POLYLINE);
      //  radialLayouter.doLayout(graph2D);



        OrganicLayouter organicLayouter = new OrganicLayouter();
        organicLayouter.doLayout(graph2D);

        //GraphTransformer graphTransformer = new GraphTransformer();
       // graphTransformer.scaleToRect(this.graph2D,worldBoundingBox);

        this.basicGraph.updateViews();
    }



}
