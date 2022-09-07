package graphClasses;

import model.BasicGraph;
import view.GraphView;
import y.base.Node;
import y.base.NodeCursor;
import y.geom.AffineLine;
import y.geom.YPoint;
import y.layout.organic.OrganicLayouter;
import y.layout.transformer.GraphTransformer;
import y.layout.tree.BalloonLayouter;
import y.layout.tree.HVTreeLayouter;
import y.util.pq.ArrayIntNodePQ;
import y.view.Graph2D;

import java.awt.*;
import java.util.*;

/**
 * Created by chris on 03.01.2018.
 */
public class CaterpillarGraph {

    private final float START_POINT_HEIGHT = 25;

    private GraphView graphView;
    private Graph2D graph2D;
    private BasicGraph basicGraph;

    private YPoint startPoint;

    private ArrayList<Node> spineVertices;
    private ArrayList<Node> leafVertices;

    private int numLeafVertices;
    private int numSpineVertices;
    private double edgeLength;

    public CaterpillarGraph(  int numLeafVertices,int numSpineVertices, GraphView graphView){

        this.graphView = graphView;
        this.graph2D = this.graphView.getGraph2D();
        this.basicGraph = this.graphView.getGraph();

        this.numLeafVertices = numLeafVertices;
        this.numSpineVertices = numSpineVertices;
        this.edgeLength = calculateEdgeLength();

        this.calculateStartPoint();

        this.basicGraph.resetGraph();

        this.initializeSpineVertices(numSpineVertices);
        this.initializeLeaves(numLeafVertices,numSpineVertices);

        this.initializeLayout();

        this.basicGraph.updateViews();
        this.graphView.fitContent();

    }



    private void initializeSpineVertices( int numSpineVertices ){

        this.spineVertices = new ArrayList<>();
        Node previousNode = null;

        for( int i=0 ; i < numSpineVertices ; i++){

            Node currentNode = this.basicGraph.createNode();

           if( previousNode !=null){
               this.basicGraph.createEdge(previousNode,currentNode);
           }

           spineVertices.add(currentNode);
           previousNode = currentNode;
        }
    }

    private void calculateStartPoint(){

        this.startPoint = new YPoint(this.edgeLength,this.START_POINT_HEIGHT);
    }


    private double calculateEdgeLength(){

        double worldWidth = this.graphView.getWorldRect2D().getWidth();
        double worldHeight = this.graphView.getWorldRect2D().getHeight();

        double ratio = numLeafVertices/numSpineVertices;


         if( ratio > 3){
             return worldWidth / (double) (numSpineVertices+1);
         }
         else if( ratio > 2){

             double angle = 180/ (Math.floor(ratio)+1);
             double heightRatio = worldHeight / (double) (numSpineVertices+1);
             return heightRatio / Math.sin(Math.toRadians(angle));
         }
         else{
             return worldHeight / (numSpineVertices);
         }


    }




    /*
        Implementation of a randomized balls in bin algorithm with
        an unfair tiebreaker, benefiting those bins with smaller index.
        http://ls2-www.cs.tu-dortmund.de/grav/grav_files/lehre/winter200203/randalg/balls-and-bins.pdf
     */
    private void initializeLeaves(int numLeafVertices, int numSpineVertices){

        ArrayIntNodePQ priorityQueue = new ArrayIntNodePQ(this.graph2D,numLeafVertices);
        for( Node spineVertex : spineVertices){
            priorityQueue.add(spineVertex,0);
        }


        for( int i=0; i < numLeafVertices ; i++){

            Node minimumPriorityNode = priorityQueue.getMin();
            int currentPriority = priorityQueue.getPriority(minimumPriorityNode)+1;
            priorityQueue.increasePriority(minimumPriorityNode,currentPriority);

        }

        leafVertices = new ArrayList<>();

        while( ! priorityQueue.isEmpty() ){
            Node currentNode = priorityQueue.getMin();
            int currentLeaves = priorityQueue.getPriority(currentNode);

            for( int i= 0; i < currentLeaves ; i++){
                Node target = this.basicGraph.createNode();
                leafVertices.add(target);
                this.basicGraph.createEdge(currentNode,target);
            }

            priorityQueue.removeMin();

        }

    }


    private void initializeLayout(){
        double worldHeight = this.graphView.getWorldRect2D().getHeight();
        double worldWidth = this.graphView.getWorldRect2D().getWidth();

        Rectangle worldBoundingBox = new Rectangle(10,10,(int) worldWidth-20, (int) worldHeight-20);

        double ratio = leafVertices.size()/spineVertices.size();
        if( spineVertices.size() > 20 && ratio > 4 ){
            OrganicLayouter organicLayouter = new OrganicLayouter();
            organicLayouter.doLayout(this.graph2D);
        }
        else{
            BalloonLayouter balloonLayouter = new BalloonLayouter();
            balloonLayouter.setRootNodePolicy(BalloonLayouter.WEIGHTED_CENTER_ROOT);
            balloonLayouter.doLayout(this.graph2D);
        }



        /*
        OrganicLayouter organicLayouter = new OrganicLayouter();
        organicLayouter.doLayout(this.graph2D);
        */

        GraphTransformer graphTransformer = new GraphTransformer();
        graphTransformer.scaleToRect(this.graph2D,worldBoundingBox);

        this.basicGraph.updateViews();
    }

    private void initializeLayout2(){

        Node previousNode = null;


        for( int i=0; i < spineVertices.size(); i++){

            Node currentNode = spineVertices.get(i);
            basicGraph.moveNode(currentNode,startPoint);

            // number of neibours to process ( excluding the previous vertex )
            int numNeibours;
            if( i != 0){
                numNeibours = currentNode.neighbors().size()-1;
            }
            else{
                numNeibours = currentNode.neighbors().size();
            }
            YPoint[] positions =  this.calculatePositions(startPoint,numNeibours);
            int positionCount = 0;

            // process adjacent vertices
            for( NodeCursor adjacentVertices = currentNode.neighbors() ; adjacentVertices.ok(); adjacentVertices.next()){
                Node adjacentNode = adjacentVertices.node();
                if( previousNode !=null && adjacentNode.index() == previousNode.index()){

                    continue;
                }

                YPoint newPosition = positions[positionCount];
                this.basicGraph.moveNode(adjacentNode,newPosition);
                if( spineVertices.contains(adjacentNode)){
                    this.startPoint = this.graph2D.getCenter(adjacentNode);
                }

                positionCount++;

            }
            previousNode = currentNode;


        }

    }


    private YPoint[] calculatePositions( YPoint start, int number){

        YPoint[] result = new YPoint[number];

        double alpha = 180/ (number+1);
        double currentAngle = alpha;
        double edgeLength = calculateEdgeLength();


        for( int i= 0; i < number ; i++){
            double tempAngle = Math.toRadians(currentAngle);

            double tempX =  start.x + ( edgeLength * Math.cos(tempAngle) )  ;
            double tempY =  start.y + ( edgeLength * Math.sin(tempAngle) ) ;

            result[i] = new YPoint(tempX,tempY);
            currentAngle += alpha;
        }
        return result;

    }


}
