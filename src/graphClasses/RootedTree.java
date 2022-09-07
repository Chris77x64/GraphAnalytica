package graphClasses;

import model.BasicGraph;
import view.GraphView;
import y.base.Node;
import y.base.NodeList;
import y.layout.tree.BalloonLayouter;
import y.layout.tree.TreeLayouter;
import y.util.YRandom;
import y.view.Graph2D;

/**
 * Created by chris on 16.01.2018.
 */
public class RootedTree {

    private final double MINIMAL_LAYER_DISTANCE = 200;
    private final double MINIMAL_NODE_DISTANCE = 200;


    private GraphView graphView;
    private Graph2D graph2D;
    private BasicGraph basicGraph;

    private int size;

    private YRandom random;

    private NodeList treeVertices;

    public RootedTree(GraphView graphView, int size) {
        this.graphView = graphView;
        this.graph2D = graphView.getGraph2D();
        this.basicGraph = graphView.getGraph();

        this.size = size;

        this.basicGraph.resetGraph();

        this.initializeAssets();
        this.initializeNodes();
        this.initializeLayout();

        this.graphView.fitContent();
    }


    private void initializeAssets(){
        this.random = new YRandom();
        this.treeVertices = new NodeList();
    }

    private void initializeNodes(){

        Node root = basicGraph.createNode();
        treeVertices.add(root);

        for( int i=1 ; i < size ; i++){
            int treeVertexIndex;
            if( i== 1){
                treeVertexIndex = 0;
            }
            else{
                treeVertexIndex = random.nextInt(0,treeVertices.size());
            }
            Node source = (Node) treeVertices.get(treeVertexIndex);
            Node target = basicGraph.createNode();
            basicGraph.createEdge(source,target);

            treeVertices.add(target);
        }
    }

    private void initializeLayout(){

        TreeLayouter treeLayouter = new TreeLayouter();
        treeLayouter.setMinimalLayerDistance(MINIMAL_LAYER_DISTANCE);
        treeLayouter.setMinimalNodeDistance(MINIMAL_NODE_DISTANCE);
        treeLayouter.doLayout(graph2D);

    }



}
