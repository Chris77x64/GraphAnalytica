package realizations;

import algo.TwoTreeLexBFS;
import model.MetricCollection;
import view.GraphView;
import view.error.RealizationErrorView;
import y.base.*;
import y.geom.YPoint;
import y.util.YRandom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chris on 09.01.2018.
 */
public class Realization2Tree extends Realization {

    private final double ADDITIONAL_LENGTH = 200;
    private final double INITIAL_HEIGHT_OFFSET = 20;
    private final double COMPARISON_THRESHOLD = 10;


    private ArrayList<Node> vertexOrder;

    private YPoint startLeft;
    private YPoint startRight;

    private YPoint startPoint= new YPoint(0,0);



    public Realization2Tree(GraphView graphView) {
        super(graphView);
        this.initializeVertexOrder();

        String res = " ";
        for( Node node: vertexOrder){
            res += node+" ";
        }
        System.out.println(res);


        if( isGraphType()) {
            this.initializeStartPoints();
            this.calculateRealization();

            this.basicGraph.updateViews();
            this.graphView.fitContent();
        }
        else{
            System.out.println("Nein");
        }

    }


    private void initializeVertexOrder(){

        //lexBFS = new LexicographicalBFS(graph2D,graph2D.firstNode());
        TwoTreeLexBFS twoTreeLexBFS = new TwoTreeLexBFS(graph2D,graph2D.firstNode());
        this.vertexOrder = twoTreeLexBFS.getLexicographicalOrder();

        /*
        NodeList core = new NodeList();
        for(NodeCursor v = this.graph2D.nodes(); v.ok() ; v.next()){
            Node currentNode = v.node();
            if( currentNode.degree() == 2){
                core.add(currentNode);
                break;
            }
        }


        NodeList[] BFS = Bfs.getLayers(this.graph2D,core);

        for( int i = 0; i < BFS.length ; i++){
            NodeList currentRecursionLayer = BFS[i];
            currentRecursionLayer.sort(new TwoTreeComparator(vertexOrder));

            for( NodeCursor v  = currentRecursionLayer.nodes(); v.ok() ; v.next()){
                Node currentNode = v.node();
                vertexOrder.add(currentNode);
            }
        }

        System.out.println(vertexOrder);
        */

    }


    private void initializeStartPoints(){

        Node firstNode = (Node) vertexOrder.get(0);
        Node secondNode = (Node) vertexOrder.get(1);

        Edge firstEdge = firstNode.getEdge(secondNode);

        int firstEdgePartition = this.graphView.getRelationships().getPartitionIndexEdge(firstEdge);


        double firstEdgeLength = getLengthEdge(firstEdgePartition);

        this.startLeft = startPoint;
        this.startRight = calculateIntersectionDownSide(startPoint,0,firstEdgeLength);

    }

    private void calculateRealization(){

        Node firstNode = (Node) vertexOrder.get(0);
        Node secondNode = (Node) vertexOrder.get(1);

        this.basicGraph.moveNode(firstNode,startLeft);
        this.basicGraph.moveNode(secondNode,startRight);

        ArrayList<YPoint> realizationSpots = new ArrayList<>();

        for( int i=2; i < vertexOrder.size(); i++){

            Node currentNode = (Node) vertexOrder.get(i);
            NodeList neighbours = new NodeList(currentNode.neighbors());

            List<Node> previousNodes =  vertexOrder.subList(0,i);
            NodeList common = intersection(neighbours,previousNodes);
            System.out.println("Parents for Node with index: "+currentNode+" Parents: "+common);

            Node left = (Node) common.get(0);
            Node right = (Node) common.get(1);

            YPoint leftPosition = this.graph2D.getCenter(left);
            YPoint rightPosition = this.graph2D.getCenter(right);

            Edge currentLeft = currentNode.getEdge(left);
            Edge currentRight = currentNode.getEdge(right);

            int partitionCurrentLeft = getPartitionEdge(currentLeft);
            int partitionCurrentRight = getPartitionEdge(currentRight);

            double lengthCurrentLeft = getLengthEdge(partitionCurrentLeft);
            double lengthCurrentRight = getLengthEdge(partitionCurrentRight);

            YPoint[] intersections = calculateCircleCircleIntersection(leftPosition,lengthCurrentLeft,rightPosition,lengthCurrentRight);

            YRandom random = new YRandom();
            int randomIndex = random.nextInt(2);


            YPoint destination = intersections[randomIndex];

            /*
            if( leftPosition.x < rightPosition.x && leftPosition.y > rightPosition.y ){
                destination = intersections[0];
                if( isOverlapping(realizationSpots,destination)){
                    destination = intersections[1];
                }
            }
            else if( leftPosition.x > rightPosition.x && leftPosition.y < rightPosition.y) {
                destination = intersections[1];
                if( isOverlapping(realizationSpots,destination)){
                    destination = intersections[0];
                }
            }
            else if( leftPosition.x > rightPosition.x && leftPosition.y > rightPosition.y){
                destination = intersections[1];
            }
            else{
                destination = intersections[0];
            }
            */

            realizationSpots.add(destination);

            this.basicGraph.moveNode(currentNode,destination);

        }

    }


    private boolean isOverlapping( ArrayList<YPoint> realizationSpots, YPoint desiredSpot){
        for( YPoint point: realizationSpots){
            double distance = MetricCollection.euklideanDistanceR2(point.x,point.y,desiredSpot.x,desiredSpot.y);
            if( distance < COMPARISON_THRESHOLD ){
                return true;
            }
        }
        return false;
    }


    private double getLengthEdge( int partition){
        int maxPartition = this.graphView.getRelationships().getMaxPartition();
        return partition+maxPartition+ADDITIONAL_LENGTH;
    }

    private int getPartitionEdge( Edge edge){
        return this.graphView.getRelationships().getPartitionIndexEdge(edge);
    }


    /*
    private NodeList maintainOrder(ArrayList<Node> previousNodes, NodeList currentLayer){
        NodeList result = new NodeList();
        for( NodeCursor v = currentLayer.nodes(); v.ok(); v.next()){
            Node currentNode = v.node();

        }
    }
    */

    private boolean is2Tree(){
        Node firstNode = vertexOrder.get(0);
        Node secondNode = vertexOrder.get(1);

        ArrayList<Node> finishedBevore = new ArrayList<>();
        finishedBevore.add(firstNode);
        finishedBevore.add(secondNode);
        for( int i= 2 ; i < vertexOrder.size() ; i++){
            Node currentNode = vertexOrder.get(i);

            NodeList currentNodeNeighbors = new NodeList(currentNode.neighbors());
            int count = 0;
            for( NodeCursor v = currentNodeNeighbors.nodes() ; v.ok() ; v.next()){
                Node neighbor = v.node();
                if( finishedBevore.contains(neighbor)){
                    count++;
                }
            }
            if( count != 2){
                return false;
            }
            finishedBevore.add(currentNode);

        }
        return true;
    }

    @Override
    public boolean isGraphType() {

        if( isInKPartitions()){
            /*
            if( is2Tree()){
                return true;
            }
            else{
                RealizationErrorView error = new RealizationErrorView(RealizationError.ERROR_NOT_2TREE);
                return false;
            }
            */
            return true;
        }
        else{
            RealizationErrorView error = new RealizationErrorView(RealizationError.ERROR_PARTITION);
            return false;
        }


    }


}
