package graphClasses;

import model.BasicGraph;
import model.Relationships;
import view.GraphView;
import y.base.Edge;
import y.base.Node;
import y.base.NodeList;
import y.layout.organic.OrganicLayouter;
import y.layout.organic.SmartOrganicLayouter;
import y.layout.radial.RadialLayouter;
import y.util.YRandom;
import y.view.Graph2D;

import java.util.ArrayList;

/**
 * Created by chris on 02.02.2018.
 */
public class HamiltonianPathGraph {

    private GraphView graphView;
    private Graph2D graph2D;
    private BasicGraph basicGraph;

    private int numVertices;
    private int numInnerEdges;

    private NodeList pathNodes;

    private ArrayList<Edge> shortEdges;
    private ArrayList<Edge> longEdges;


    public HamiltonianPathGraph(GraphView graphView, int numVertices, int numInnerEdges){
        this.graphView = graphView;
        this.graph2D = graphView.getGraph2D();
        this.basicGraph = graphView.getGraph();

        this.numVertices = numVertices;
        this.numInnerEdges = numInnerEdges;

        this.basicGraph.resetGraph();

        this.shortEdges = new ArrayList<>();
        this.longEdges = new ArrayList<>();

        this.initializeVertices();
        this.initializeEdges();
        this.initializeLayout();
        this.initializePartitions();

        this.graphView.fitContent();
        this.basicGraph.updateViews();

    }

    private void initializeVertices(){
        this.pathNodes = new NodeList();

        for( int i=0; i < numVertices ; i++){
            Node currentNode = basicGraph.createNode();
            this.pathNodes.add(currentNode);
        }
    }

    private void initializeEdges(){
        YRandom random = new YRandom();

        for( int i=0; i < numVertices ; i++){
            if( i+1 < numVertices){
                Node currentNode = (Node) pathNodes.get(i);
                Node nextNode = (Node) pathNodes.get(i+1);

                Edge currentEdge = this.basicGraph.createEdgeReturned(currentNode,nextNode);
                this.shortEdges.add(currentEdge);
            }
        }


        for( int i=0; i < numInnerEdges ; i++){
            Node source;
            Node target;

            do{
                int sourceIndex = random.nextInt(0,pathNodes.size());
                int targetIndex = random.nextInt(0,pathNodes.size());
                source = (Node) pathNodes.get(sourceIndex);
                target = (Node) pathNodes.get(targetIndex);
            }
            while( (source.index() == target.index()) || (source.getEdge(target) != null) );

            Edge createdEdge = this.basicGraph.createEdgeReturned(source,target);
            this.longEdges.add(createdEdge);
        }

    }

    private void initializeLayout(){
        if( numVertices > numInnerEdges) {
            SmartOrganicLayouter smartOrganicLayouter = new SmartOrganicLayouter();
            smartOrganicLayouter.setPreferredEdgeLength(200);
            smartOrganicLayouter.doLayout(graph2D);
        }
        else{
            RadialLayouter radialLayouter = new RadialLayouter();
            radialLayouter.setEdgeRoutingStrategy(RadialLayouter.EDGE_ROUTING_STRATEGY_POLYLINE);
            radialLayouter.doLayout(graph2D);
        }
    }

    private void initializePartitions(){
        Relationships relationships = graphView.getRelationships();
        relationships.initializeDichotomousPartitions(shortEdges,longEdges);
    }

}
