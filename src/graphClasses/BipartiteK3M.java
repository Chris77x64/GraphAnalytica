package graphClasses;

import model.BasicGraph;
import view.GraphView;
import y.base.Node;
import y.view.Graph2D;

/**
 * Created by chris on 04.06.2018.
 */
public class BipartiteK3M {

    private GraphView graphView;
    private Graph2D graph2D;
    private BasicGraph basicGraph;

    private int m;

    public BipartiteK3M(GraphView graphView, int m) {
        this.graphView = graphView;
        this.graph2D = graphView.getGraph2D();
        this.basicGraph = graphView.getGraph();
        this.m = m;

        basicGraph.resetGraph();

        this.generateK3m();
        this.graphView.getLayoutView().applyRadialLayout();
    }

    private void generateK3m(){
        basicGraph.resetGraph();
        Node u0 = basicGraph.createNode();
        Node u1 = basicGraph.createNode();
        Node u2 = basicGraph.createNode();


        for( int i=0; i < m; i++){
            Node currentNode = basicGraph.createNode();
            basicGraph.createEdge(currentNode,u0);
            basicGraph.createEdge(currentNode,u1);
            basicGraph.createEdge(currentNode,u2);
        }
    }


}
