package realizations;

import view.GraphView;
import y.base.Node;
import y.base.NodeList;
import y.geom.YPoint;

import java.util.ArrayList;

/**
 * Created by chris on 25.04.2018.
 */
public class RealizationBinaryTree extends Realization{


    private final double LENGTH_SHORT_EDGES = 200;
    private final double LENGTH_LONG_EDGES = 230;

    private final double LENGTH_UPPER_LEAF = 100;
    private final double LENGTH_LOWER_LEAF = 150;

    private final YPoint startPoint = new YPoint(0,0);

    private NodeList spineVertices;

    private NodeList leafVerticesTop;
    private NodeList leafVerticesBot;

    public RealizationBinaryTree(GraphView graphView) {
        super(graphView);
        generateGraph(11);

        this.realizeSpineVertices();
        this.realizeLeafVertices();
    }


    @Override
    public boolean isGraphType() {
        return false;
    }



    private YPoint[] calculateRealizationSpots(int index){

        int spineSize = spineVertices.size();
        int middleIndex = getMiddleIndex(spineSize);

        YPoint[] result = new YPoint[3];

        double lengthTopLeaf;

        /*
        if( index % 2 == 0){
            lengthTopLeaf = LENGTH_UPPER_LEAF;
        }
        else {
            lengthTopLeaf = LENGTH_SHORT_EDGES;
        }
        */

        lengthTopLeaf = LENGTH_UPPER_LEAF;

        if( middleIndex == index ){

            Node middleVertex = (Node) spineVertices.get(middleIndex);
            YPoint positionMiddleVertex = graph2D.getCenter(middleVertex);

            YPoint top = calculateIntersectionUpSide(positionMiddleVertex,90,lengthTopLeaf);
            YPoint top2 = calculateIntersectionUpSide(positionMiddleVertex,90,LENGTH_SHORT_EDGES);
            YPoint bot = calculateIntersectionDownSide(positionMiddleVertex,90,LENGTH_LOWER_LEAF);

            result[0] = top;
            result[1] = bot;
            result[2] = top2;
            return result;

        }
        else if( index < middleIndex){

            int leafIndex= middleIndex-index;
            int n = this.spineVertices.size();

            double gamma = Math.min(3.3,((double) 1/n)*60);

            double offset = leafIndex*(1.5* gamma);

            double angleTop = 90-offset;
            double angleBot = 90+offset;

            Node correspondingSpineVertex = (Node) spineVertices.get(index);
            YPoint posCorrespondingSpineVertex = graph2D.getCenter(correspondingSpineVertex);

            YPoint top = calculateIntersectionUpSide(posCorrespondingSpineVertex,angleTop,lengthTopLeaf);
            YPoint top2 = calculateIntersectionUpSide(posCorrespondingSpineVertex,angleTop,LENGTH_SHORT_EDGES);
            YPoint bot = calculateIntersectionDownSide(posCorrespondingSpineVertex,angleBot,LENGTH_LOWER_LEAF);

            result[0] = top;
            result[1] = bot;
            result[2] = top2;
            return result;


        }
        else if( index > middleIndex){

            int leafIndex= index - middleIndex;
            int n = this.spineVertices.size();

            double gamma = Math.min(3.3,((double) 1/n)*60);
            double offset = leafIndex*(1.5* gamma);

            double angleTop = 90+offset;
            double angleBot = 90-offset;

            Node correspondingSpineVertex = (Node) spineVertices.get(index);
            YPoint posCorrespondingSpineVertex = graph2D.getCenter(correspondingSpineVertex);

            YPoint top = calculateIntersectionUpSide(posCorrespondingSpineVertex,angleTop,lengthTopLeaf);
            YPoint top2 = calculateIntersectionUpSide(posCorrespondingSpineVertex,angleTop,LENGTH_SHORT_EDGES);
            YPoint bot = calculateIntersectionDownSide(posCorrespondingSpineVertex,angleBot,LENGTH_LOWER_LEAF);

            result[0] = top;
            result[1] = bot;
            result[2] = top2;
            return result;
        }

        return null;

    }


    private ArrayList<Node> leaves(Node currentNode){

        ArrayList<Node> result = new ArrayList<>();

        NodeList neighbors = new NodeList(currentNode.neighbors());

        for( int i=0; i < neighbors.size(); i++){

            Node currentNeighbor = (Node) neighbors.get(i);

            if( !spineVertices.contains(currentNeighbor)){
                result.add(currentNeighbor);
            }

        }
        return result;
    }


    private void genericLeafPlacement( ArrayList<Node> leaves, YPoint[] spots){

        int size = leaves.size();

        YPoint top = spots[0];
        YPoint bot = spots[1];
        YPoint asd2 = spots[2];

        switch (size){

            case 1:{

                Node currentLeaf = leaves.get(0);
                basicGraph.moveNode(currentLeaf,top);
                break;

            }

            case 2:{

                Node topLeaf = leaves.get(0);
                Node botLeaf = leaves.get(1);

                basicGraph.moveNode(topLeaf,top);
                basicGraph.moveNode(botLeaf,bot);

                Node asd = basicGraph.createNode(asd2);
                basicGraph.createEdge(topLeaf,asd);
                break;

            }
        }
    }





    private void realizeLeafVertices(){

        int sizeSpines = this.spineVertices.size();

        for( int i = 0 ; i < sizeSpines ; i++){
            Node currentSpineVertex = (Node) spineVertices.get(i);
            ArrayList<Node> currentLeaves = leaves(currentSpineVertex);
            YPoint[] currentSpots = calculateRealizationSpots(i);
            genericLeafPlacement(currentLeaves,currentSpots);
        }

        /*
        Node root = (Node) spineVertices.get(0);
        YPoint positionRoot = graph2D.getCenter(root);

        Node secondNode = (Node) spineVertices.get(1);
        YPoint positionSecond = graph2D.getCenter(secondNode);

        Node firstTop = (Node) leafVerticesTop.get(0);
        Node firstBot = (Node) leafVerticesBot.get(0);

        YPoint[] intersections = calculateCircleCircleIntersection(positionRoot,LENGTH_LONG_EDGES,positionSecond,LENGTH_SHORT_EDGES);
        YPoint positionTop = intersections[0];
        YPoint positionBot = intersections[1];

        basicGraph.moveNode(firstTop,positionTop);
        basicGraph.moveNode(firstBot,positionBot);


        for( int i=2; i < spineVertices.size(); i++){

            Node previousSpine = (Node) spineVertices.get(i-1);
            Node currentSpine = (Node) spineVertices.get(i);


            Node previousTop = (Node) leafVerticesTop.get(i-2);
            Node previousBot = (Node) leafVerticesBot.get(i-2);

            Node currentTop = (Node) leafVerticesTop.get(i-1);
            Node currentBot = (Node) leafVerticesBot.get(i-1);


            YPoint positionPreviousSpine = graph2D.getCenter(previousSpine);
            YPoint positionCurrentSpine = graph2D.getCenter(currentSpine);

            YPoint positionPreviousTop = graph2D.getCenter(previousTop);
            YPoint positionPreviousBot = graph2D.getCenter(previousBot);

            YPoint destinationTop = MaxCircleCircleIntersection(positionCurrentSpine,LENGTH_SHORT_EDGES,positionPreviousTop,LENGTH_LONG_EDGES,positionPreviousSpine);
            YPoint destinationBot = MaxCircleCircleIntersection(positionCurrentSpine,LENGTH_SHORT_EDGES,positionPreviousBot,LENGTH_LONG_EDGES,positionPreviousSpine);

            basicGraph.moveNode(currentTop,destinationTop);
            basicGraph.moveNode(currentBot,destinationBot);

        }
        */
    }


    private int getMiddleIndex( int n){
        int result;
        if( n % 2 == 0){
            result = (int) (n/2) -1 ;
        }
        else{
            result = (int) Math.floor( n/2d );
        }
        return result;
    }


    private void s2(){

        int n = this.spineVertices.size();
        double gamma = Math.min(3.3,((double) 1/n)*60);


        System.out.println("GAMMA: "+gamma);
        int middleIndex = getMiddleIndex(n);

        Node startNode = (Node) spineVertices.get(middleIndex);
        this.basicGraph.moveNode(startNode,startPoint);

        double offset = (1.5* gamma);
        Node previousNode = startNode;

        for( int i= middleIndex-1; i >= 0; i--){

            Node currentNode = (Node) spineVertices.get(i);
            YPoint positionPreviousNode = basicGraph.getCenter(previousNode);


            YPoint destination = this.calculateIntersectionUpSide(positionPreviousNode,offset,LENGTH_SHORT_EDGES);
            this.basicGraph.moveNode(currentNode,destination);

            // adjust spine angle
            previousNode = currentNode;
        }

        previousNode = startNode;
        for( int i = middleIndex+1 ; i < n ; i++){

            Node currentNode = (Node) spineVertices.get(i);
            YPoint positionPreviousNode = basicGraph.getCenter(previousNode);

            YPoint destination = this.calculateIntersectionUpSide(positionPreviousNode,offset,LENGTH_SHORT_EDGES);
            this.basicGraph.moveNode(currentNode,destination);

            // adjust spine angle
            previousNode = currentNode;
        }


    }
    private void realizeSpineVertices(){


        int n = this.spineVertices.size();
        double gamma = Math.min(3.3,((double) 1/n)*60);


        System.out.println("GAMMA: "+gamma);
        int middleIndex = getMiddleIndex(n);

        Node startNode = (Node) spineVertices.get(middleIndex);
        this.basicGraph.moveNode(startNode,startPoint);



        double offset = (1.5* gamma);


        double currentAngle = 180-offset;
        Node previousNode = startNode;
        System.out.println(offset);

        for( int i= middleIndex-1; i >= 0; i--){

            Node currentNode = (Node) spineVertices.get(i);
            YPoint positionPreviousNode = basicGraph.getCenter(previousNode);


            YPoint destination = this.calculateIntersectionUpSide(positionPreviousNode,currentAngle,LENGTH_SHORT_EDGES);
            this.basicGraph.moveNode(currentNode,destination);

            // adjust spine angle
            currentAngle = currentAngle - offset;
            previousNode = currentNode;
        }


        currentAngle = offset;
        previousNode = startNode;

        for( int i = middleIndex+1 ; i < n ; i++){

            Node currentNode = (Node) spineVertices.get(i);
            YPoint positionPreviousNode = basicGraph.getCenter(previousNode);

            YPoint destination = this.calculateIntersectionUpSide(positionPreviousNode,currentAngle,LENGTH_SHORT_EDGES);
            this.basicGraph.moveNode(currentNode,destination);

            // adjust spine angle
            currentAngle = currentAngle+offset;
            previousNode = currentNode;
        }




    }


    private void generateGraph( int n){

        this.spineVertices = new NodeList();
        this.leafVerticesTop = new NodeList();
        this.leafVerticesBot = new NodeList();

        Node root = basicGraph.createNode();
        this.spineVertices.add(root);

        Node previousSpine = root;

        for( int i=1 ; i < n ; i++){
            Node currentSpine = basicGraph.createNode();
            basicGraph.createEdge(currentSpine,previousSpine);
            spineVertices.add(currentSpine);

            Node leaf1 = basicGraph.createNode();
            Node leaf2 = basicGraph.createNode();

           basicGraph.createEdge(currentSpine,leaf1);
            basicGraph.createEdge(currentSpine,leaf2);

            leafVerticesTop.add(leaf1);
            leafVerticesBot.add(leaf2);

            previousSpine = currentSpine;
        }

    }

}
