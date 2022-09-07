package realizations;

import view.GraphView;
import y.algo.Trees;
import y.base.Edge;
import y.base.Node;
import y.base.NodeList;
import y.geom.YPoint;

/**
 * Created by chris on 24.06.2018.
 */
public class RealizationTree extends Realization {

    private final YPoint START_POINT = new YPoint(0,0);

    private final double EDGE_LENGTH_MULTIPLIER = 100;

    private Node root;

    public RealizationTree(GraphView graphView) {
        super(graphView);

        if( isGraphType()) {
            this.root = Trees.getRoot(graph2D);
            calculateRealization(root,null);

            graphView.fitContent();
            basicGraph.updateViews();
        }
        else{

        }
    }


    private void calculateRealization( Node current, Node parent){
        if( parent == null){
            basicGraph.moveNode(current,START_POINT);
        }

        NodeList currentChildren = new NodeList(current.neighbors());

        if( parent != null){
            currentChildren.remove(parent);
        }

        YPoint positionCurrent = graph2D.getCenter(current);

        if( currentChildren.size() > 0){

            int numChildren = currentChildren.size();
            double alpha = 180/ (double) (numChildren +1);
            double beta = alpha;

            for( int i=0; i < currentChildren.size(); i++){

                Node currentChild = (Node) currentChildren.get(i);
                Edge currentEdge = currentChild.getEdge(current);

                double lengthEdge = getEdgeLength(currentEdge);
                YPoint destination = calculateIntersectionDownSide(positionCurrent,beta,lengthEdge);

                basicGraph.moveNode(currentChild,destination);
                calculateRealization(currentChild,current);

                beta += alpha;
            }

        }

    }


    private double getEdgeLength(Edge edge){
        int partition = this.graphView.getRelationships().getPartitionIndexEdge(edge);
        return partition * EDGE_LENGTH_MULTIPLIER;
    }

    @Override
    public boolean isGraphType() {
        if( Trees.isTree(graph2D)){
            return true;
        }
        else{
            return false;
        }
    }
}
