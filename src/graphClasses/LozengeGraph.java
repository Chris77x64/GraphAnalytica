package graphClasses;

import model.BasicGraph;
import view.GraphView;
import y.base.Node;
import y.base.NodeList;
import y.geom.YPoint;
import y.view.Graph2D;

/**
 * Created by chris on 04.04.2018.
 */
public class LozengeGraph {

    private final YPoint START_POINT = new YPoint(0,0);
    private final double LENGTH_PATH_EDGES = 200;
    private final double LENGTH_ST = 400;

    private GraphView graphView;
    private Graph2D graph2D;
    private BasicGraph basicGraph;

    private int size;

    private NodeList pathOrder;
    private Node s;
    private Node t;

    public LozengeGraph(GraphView graphView, int size){
        this.graphView = graphView;
        this.graph2D = graphView.getGraph2D();
        this.basicGraph = graphView.getGraph();
        this.size = size;

        basicGraph.resetGraph();
        this.initializeGraph();
        this.calculateLayout();

        this.graphView.fitContent();


    }


    private void initializeGraph(){

        s = basicGraph.createNode();
        t = basicGraph.createNode();

        this.pathOrder = new NodeList();

        Node previousNode = null;

        for( int i=0; i < size-2; i++){

            Node currentNode = basicGraph.createNode();
            basicGraph.createEdge(s,currentNode);
            basicGraph.createEdge(t,currentNode);

            if( previousNode != null){
                basicGraph.createEdge(previousNode,currentNode);
            }

            previousNode = currentNode;
            pathOrder.add(currentNode);

        }

    }

    private void calculateLayout(){

        Node firstPathNode = (Node) pathOrder.get(0);
        basicGraph.moveNode(firstPathNode,START_POINT);

        for( int i=1; i < pathOrder.size(); i++){

            Node currentNode = (Node) pathOrder.get(i);
            Node previousNode = (Node) pathOrder.get(i-1);

            YPoint previousPosition = graph2D.getCenter(previousNode);
            YPoint currentPosition = calculateIntersectionUpSide(previousPosition,-90,LENGTH_PATH_EDGES);
            basicGraph.moveNode(currentNode,currentPosition);
        }

        int middleIndex = getMiddleIndex(pathOrder.size());
        Node middlePathNode = (Node) pathOrder.get(middleIndex);

        YPoint positionMiddlePathNode = graph2D.getCenter(middlePathNode);
        if( pathOrder.size() % 2 == 0){
            positionMiddlePathNode = new YPoint(positionMiddlePathNode.getX(),positionMiddlePathNode.getY()-0.5* LENGTH_PATH_EDGES);
        }

        YPoint positionS = calculateIntersectionUpSide(positionMiddlePathNode,180,LENGTH_ST);
        YPoint positionT = calculateIntersectionUpSide(positionMiddlePathNode,0,LENGTH_ST);

        basicGraph.moveNode(s,positionS);
        basicGraph.moveNode(t,positionT);

    }


    private YPoint calculateIntersectionUpSide( YPoint center, double angle , double distance){
        double tempAngle = Math.toRadians(angle);

        double tempX =  center.x + ( distance * Math.cos(tempAngle) ) ;
        double tempY =  center.y - ( distance* Math.sin(tempAngle) )  ;
        return new YPoint(tempX,tempY);
    }

    private int getMiddleIndex( int listSize){

        if( listSize % 2 ==1){
            return (int) Math.floor(listSize/2d );
        }
        else{
            return listSize/2;
        }
    }


}
