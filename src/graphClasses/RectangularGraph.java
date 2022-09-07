package graphClasses;

import model.BasicGraph;
import view.GraphView;
import y.base.Node;
import y.base.NodeList;
import y.view.Graph2D;

/**
 * Created by chris on 05.02.2018.
 */
public class RectangularGraph {

    private GraphView graphView;
    private Graph2D graph2D;
    private BasicGraph basicGraph;

    private int numRectangles;

    public RectangularGraph(GraphView graphView, int numRectangles) {
        this.graphView = graphView;
        this.graph2D = graphView.getGraph2D();
        this.basicGraph = graphView.getGraph();

        this.numRectangles = numRectangles;

        this.basicGraph.resetGraph();

        this.initializeGraph();
        this.initializeLayout();
    }


    private void initializeGraph(){
        NodeList vertices = new NodeList();

        for( int i=0; i < numRectangles; i++){
            Node leftTop;
            Node leftBot;
            if( i == 0) {
                leftTop = basicGraph.createNode();
                leftBot = basicGraph.createNode();
                vertices.add(leftTop);
                vertices.add(leftBot);
                basicGraph.createEdge(leftTop,leftBot);
            }
            else{
                int leftTopIndex = i*2;
                int leftBotIndex = (i*2)+1;
                leftTop = (Node) vertices.get(leftTopIndex);
                leftBot = (Node) vertices.get(leftBotIndex);
            }
            Node rightTop = basicGraph.createNode();
            Node rightBot = basicGraph.createNode();

            basicGraph.createEdge(leftTop,rightTop);
            basicGraph.createEdge(leftBot,rightBot);
            basicGraph.createEdge(rightTop,rightBot);

            vertices.add(rightTop);
            vertices.add(rightBot);
        }
    }

    private void initializeLayout(){
        this.graphView.getLayoutView().applySmartOrganicLayout();
    }

}
