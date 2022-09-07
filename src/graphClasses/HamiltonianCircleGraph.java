package graphClasses;

import model.BasicGraph;
import model.Relationships;
import view.GraphView;
import y.base.Edge;
import y.base.Node;
import y.base.NodeList;
import y.layout.organic.SmartOrganicLayouter;
import y.layout.radial.RadialLayouter;
import y.util.YRandom;
import y.view.Graph2D;

import java.util.ArrayList;

/**
 * Created by chris on 16.01.2018.
 */
public class HamiltonianCircleGraph {

    private final double PREFERRED_EDGE_LENGTH_ORGANIC = 200;

    private GraphView graphView;
    private Graph2D graph2D;
    private BasicGraph basicGraph;

    private int numVertices;
    private int numInnerEdges;

    private NodeList circleNodes;

    ArrayList<Edge> shortEdges;
    ArrayList<Edge> longEdges;

    public HamiltonianCircleGraph(GraphView graphView, int numVertices, int numInnerEdges) {
        this.graphView = graphView;
        this.graph2D = graphView.getGraph2D();
        this.basicGraph = graphView.getGraph();

        this.numVertices = numVertices;
        this.numInnerEdges = numInnerEdges;

        this.basicGraph.resetGraph();

        this.initializeAssets();
        this.initializeVertices();
        this.initializeCircleEdges();
        this.initializeInnerCircleEdges();
        this.initializePartitions();
        this.initializeLayout();

        this.graphView.fitContent();
        this.basicGraph.updateViews();

    }


    private void initializeVertices(){
        this.circleNodes = new NodeList();

        for( int i=0; i < numVertices ; i++){
            Node currentNode = basicGraph.createNode();
            circleNodes.add(currentNode);
        }
    }

    private void initializeCircleEdges(){

        for( int i=0; i < circleNodes.size(); i++){
            Node currentNode = (Node) circleNodes.get(i);
            Node nextNode;
            if( i+1 == circleNodes.size()){
                nextNode = (Node) circleNodes.get(0);
            }
            else{
                nextNode = (Node) circleNodes.get(i+1);
            }
            Edge createdEdge = this.basicGraph.createEdgeReturned(currentNode,nextNode);
            this.shortEdges.add(createdEdge);
        }

    }


    private void initializeInnerCircleEdges(){

        YRandom random = new YRandom();

        for( int i=0; i < numInnerEdges; i++){

            Node source;
            Node target;

            do{
                int sourceIndex = random.nextInt(0,circleNodes.size());
                int targetIndex = random.nextInt(0,circleNodes.size());
                source = (Node) circleNodes.get(sourceIndex);
                target = (Node) circleNodes.get(targetIndex);
            }
            while( (source.index() == target.index()) || (source.getEdge(target) != null) );

            Edge createdEdge = this.basicGraph.createEdgeReturned(source,target);
            this.longEdges.add(createdEdge);
        }

    }


    private void initializeLayout(){

        if( numVertices > numInnerEdges) {
            SmartOrganicLayouter smartOrganicLayouter = new SmartOrganicLayouter();
            smartOrganicLayouter.setPreferredEdgeLength(PREFERRED_EDGE_LENGTH_ORGANIC);
            smartOrganicLayouter.doLayout(graph2D);
        }
        else{
            RadialLayouter radialLayouter = new RadialLayouter();
            radialLayouter.setEdgeRoutingStrategy(RadialLayouter.EDGE_ROUTING_STRATEGY_POLYLINE);
            radialLayouter.doLayout(graph2D);
        }
    }

    private void initializeAssets(){
        this.shortEdges = new ArrayList<>();
        this.longEdges = new ArrayList<>();
    }


    private void initializePartitions(){
        Relationships relationships = graphView.getRelationships();
        relationships.initializeDichotomousPartitions(shortEdges,longEdges);
    }
}
