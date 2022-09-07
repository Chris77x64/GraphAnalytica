package realizations;

import algo.PrismGraphCycles;
import model.MetricCollection;
import model.Relationships;
import view.GraphView;
import y.base.Edge;
import y.base.Node;
import y.base.NodeList;
import y.geom.YPoint;
import y.util.YRandom;
import y.view.GenericNodeRealizer;
import y.view.NodeRealizer;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by chris on 26.03.2018.
 */
public class RealizationPrismGraph999999 extends Realization{

    private final double L_MAX = 1000;
    private final double L_L = 0.75 * L_MAX;
    private final double L_S = 0.7 * L_MAX;

    private final double R = (Math.sqrt(3) / 3 ) * L_MAX;

    private final YPoint ps = new YPoint(0,0);

    private YPoint case3P1;
    private YPoint case3P2;
    private YPoint case3P3;

    private YPoint case3L11;
    private YPoint case3S11;

    private YPoint case3L12;
    private YPoint case3S12;


    private YPoint case3L22;
    private YPoint case3S22;

    private YPoint case3L21;
    private YPoint case3S21;

    private YPoint case3L32;
    private YPoint case3S32;

    private YPoint case3L31;
    private YPoint case3S31;

    private YPoint case3LStart;
    private YPoint case3SStart;

    private NodeList vertexOrder;

    private NodeList innerCycle;
    private NodeList outerCycle;

    private ArrayList<Edge> shortEdges;
    private ArrayList<Edge> longEdges;


    private YPoint p1;
    private YPoint p2;
    private YPoint p3;

    private YPoint l11;
    private YPoint s11;

    private YPoint l12;
    private YPoint s12;

    private YPoint l21;
    private YPoint s21;

    private YPoint l22;
    private YPoint s22;

    private YPoint l31;
    private YPoint s31;

    private YPoint l32;
    private YPoint s32;

    private YPoint sStart;
    private YPoint lStart;


    public RealizationPrismGraph999999(GraphView graphView,boolean type) {

        super(graphView);


        this.initializeVertexOrder1();


        if( type) {

            this.initializeAssets();



            //this.initializeInnerCycle();
            //this.initializeOuterCycle();
            this.modifyPartitions2();
            this.shiftOuterCycle();


            basicGraph.updateViews();


            this.initializRealizationSpotsCase4();

            this.realizeInnerCycleCase4();
            this.realizeOuterCycleCase4();
            basicGraph.updateViews();

        }
        else{

        this.initializeAssets();
        //this.initializeVertexOrder();

        //this.initializeInnerCycle();
        //this.initializeOuterCycle();
            this.modifyPartitions();
        this.shiftOuterCycle();


        basicGraph.updateViews();

        this.initializeRealizationSpotsCase3();
        this.realizeInnerCycle();
        this.realizeOuterCycle();


        }


        graphView.fitContent();

    }

    private void vertexOrderDfs(Node currentNode, Node parent){

        if(  (innerCycle.size() == (graph2D.nodeCount()*0.5)) ) {

            NodeList neighbors = setDifference(new NodeList(currentNode.neighbors()),parent);
            for( int i=0; i < neighbors.size(); i++){
                Node currentNeighbor = (Node) neighbors.get(i);
                if( !innerCycle.contains(currentNeighbor) && !outerCycle.contains(currentNeighbor)){
                    outerCycle.add(currentNeighbor);
                    break;
                }
            }

        }
        else{
            NodeList neighbors = setDifference(new NodeList(currentNode.neighbors()),parent);

            Node firstNeighbor = (Node) neighbors.get(0);

            NodeList correspondingNeighbors = setDifference(new NodeList(firstNeighbor.neighbors()),currentNode);

            boolean flag = true;

            for( int i=0; i < correspondingNeighbors.size(); i++){
                Node comparison = (Node) correspondingNeighbors.get(i);
                if( outerCycle.contains(comparison)){
                    outerCycle.add(firstNeighbor);
                    Node next = (Node) neighbors.get(1);
                    innerCycle.add(next);
                    flag = false;
                    vertexOrderDfs(next,currentNode);
                    break;
                }
            }

            if( flag){
                Node other = (Node) neighbors.get(1);
                outerCycle.add(other);
                innerCycle.add(firstNeighbor);
                vertexOrderDfs(firstNeighbor,currentNode);
            }
        }

    }


    private NodeList setDifference( NodeList list, Node difference){
        list.remove(difference);
        return list;
    }

    private void initializeVertexOrder1(){
        this.innerCycle = new NodeList();
        this.outerCycle = new NodeList();

        Node startNode = graph2D.firstNode();
        innerCycle.add(startNode);

        NodeList startNeighbors = new NodeList(startNode.neighbors());

        Node neighbor1 = (Node) startNeighbors.get(0);
        Node neighbor2 = (Node) startNeighbors.get(1);
        Node neighbor3 = (Node) startNeighbors.get(2);

        NodeList neighborhood1 = setDifference(new NodeList(neighbor1.neighbors()),startNode);
        NodeList neighborhood2 = setDifference(new NodeList(neighbor2.neighbors()),startNode);
        NodeList neighborhood3 = setDifference(new NodeList(neighbor3.neighbors()),startNode);

        ArrayList<NodeList> neighbourhoods = new ArrayList<>();
        neighbourhoods.add(neighborhood1);
        neighbourhoods.add(neighborhood2);
        neighbourhoods.add(neighborhood3);

        Node fraudNode = null;

        for( int i=0; i < startNeighbors.size(); i++){
            Node currentNeighbor = (Node) startNeighbors.get(i);

            NodeList currentNeighbourhood = neighbourhoods.get(i);

            NodeList comparison = new NodeList();
            for( int t=0; t < neighbourhoods.size(); t++){
                    if( t == i){
                        continue;
                    }
                    else{
                        comparison.addAll(neighbourhoods.get(t));
                }
            }

            if( comparison.containsAll(currentNeighbourhood)){
                fraudNode = currentNeighbor;
                break;
            }
        }

        outerCycle.add(fraudNode);

        NodeList difference = setDifference(startNeighbors,fraudNode);
        Node next = (Node) difference.get(0);
        innerCycle.add(next);
        vertexOrderDfs(next,startNode);
        System.out.println(innerCycle);
        System.out.println(outerCycle);
    }

    private void test(){

        YPoint s = new YPoint(0,0);
        YPoint t = calculateIntersectionUpSide(s,0,1.696*L_S);

        YPoint tb = calculateIntersectionUpSide(t,148,L_S);
        YPoint bt = calculateIntersectionDownSide(t,32,L_S);

        basicGraph.createNode(s);
        basicGraph.createNode(t);
        basicGraph.createNode(tb);
        basicGraph.createNode(bt);


    }
    @Override
    public boolean isGraphType() {
        return false;
    }


    private void modifyPartitions2(){

        this.initializeAssets();
        for( int i=0; i < innerCycle.size(); i++){
            Node currentInnerNode = (Node) innerCycle.get(i);
            Node nextInnerNode = (Node) innerCycle.get( (i+1) % (innerCycle.size()) );

            Node currentOuterNode = (Node) outerCycle.get(i);
            Node nextOuterNode = (Node) outerCycle.get( (i+1) % (innerCycle.size()) );

            if( isEdgeLong(currentInnerNode,nextInnerNode) && isEdgeLong(currentOuterNode,nextOuterNode) ) {
                Edge currentEdge = currentOuterNode.getEdge(nextOuterNode);
                graphView.getRelationships().updateEdge(1, currentEdge);
            }
        }

        /*
        YRandom random = new YRandom();

        int edgeIndex = random.nextInt(0,innerCycle.size()-1);

        Node currentSelectedInner = (Node) innerCycle.get(edgeIndex);
        Node nextSelectedInner = (Node) innerCycle.get(edgeIndex+1);

        Node currentSelectedOuter = (Node) outerCycle.get(edgeIndex);
        Node nextSelectedOuter = (Node) outerCycle.get(edgeIndex+1);

        Edge selectedEdge = currentSelectedInner.getEdge(nextSelectedInner);

        graphView.getRelationships().updateEdge(2,selectedEdge);

        if(isEdgeLong(currentSelectedOuter,nextSelectedOuter)){
            Edge selectedOuterEdge = currentSelectedOuter.getEdge(nextSelectedOuter);
            graphView.getRelationships().updateEdge(1,selectedOuterEdge);
        }
        */
        this.initializeAssets();
    }

    private void modifyPartitions(){

        this.initializeAssets();

        for( int i=0; i < innerCycle.size(); i++){
            Node currentNode = (Node) innerCycle.get(i);
            Node nextNode = (Node) innerCycle.get( (i+1) % (innerCycle.size()) );
            Edge currentEdge = currentNode.getEdge(nextNode);
            graphView.getRelationships().updateEdge(1,currentEdge);
        }

        YRandom random = new YRandom();

        int edgeIndex = random.nextInt(0,innerCycle.size()-1);

        Node currentSelectedInner = (Node) innerCycle.get(edgeIndex);
        Node nextSelectedInner = (Node) innerCycle.get(edgeIndex+1);

        Node currentSelectedOuter = (Node) outerCycle.get(edgeIndex);
        Node nextSelectedOuter = (Node) outerCycle.get(edgeIndex+1);

        Edge selectedEdge = currentSelectedInner.getEdge(nextSelectedInner);

        graphView.getRelationships().updateEdge(2,selectedEdge);

        if(isEdgeLong(currentSelectedOuter,nextSelectedOuter)){
            Edge selectedOuterEdge = currentSelectedOuter.getEdge(nextSelectedOuter);
            graphView.getRelationships().updateEdge(1,selectedOuterEdge);
        }
        this.initializeAssets();
    }

    private void initializeAssets(){
        this.shortEdges = graphView.getRelationships().getEdgesFromPartition(1);
        this.longEdges = graphView.getRelationships().getEdgesFromPartition(2);
    }


    private void initializeOuterCycle(){
        this.outerCycle = new NodeList();
        for( int i=0 ; i < vertexOrder.size(); i +=2 ) {

            Node outerNode = (Node) vertexOrder.get(i);
            outerCycle.add(outerNode);
        }
    }
    private void initializeInnerCycle(){
        this.innerCycle = new NodeList();

        for( int i=1 ; i < vertexOrder.size(); i +=2 ) {

            Node innerNode = (Node) vertexOrder.get(i);
            innerCycle.add(innerNode);
        }
    }

    private void initializeVertexOrder() {

        PrismGraphCycles prismGraphCycles = new PrismGraphCycles(graph2D);
        this.vertexOrder = prismGraphCycles.getVertexOrder();
    }

    private void shiftOuterCycle(){

        int shiftIndex = -1;

        for( int i=0; i < outerCycle.size(); i++){

            Node currentNode = (Node) outerCycle.get(i);
            Node nextNode;

            if( i+1 < outerCycle.size()){
                nextNode = (Node) outerCycle.get(i+1);
            }
            else{
                nextNode = (Node) outerCycle.get(0);
            }


            boolean edgeLong = isEdgeLong(currentNode,nextNode);

            if( edgeLong){
                shiftIndex = i;
                break;
            }

        }

        NodeList newOuterCycle = new NodeList();
        NodeList newInnerCycle = new NodeList();

        for( int i= shiftIndex+1 ; i < outerCycle.size(); i++){
            Node currentInnerNode = (Node) innerCycle.get(i);
            Node currentOuterNode = (Node) outerCycle.get(i);

            newInnerCycle.add(currentInnerNode);
            newOuterCycle.add(currentOuterNode);
        }

        for( int i=0; i < shiftIndex+1; i++){
            Node currentInnerNode = (Node) innerCycle.get(i);
            Node currentOuterNode = (Node) outerCycle.get(i);

            newInnerCycle.add(currentInnerNode);
            newOuterCycle.add(currentOuterNode);
        }

        this.innerCycle = newInnerCycle;
        this.outerCycle = newOuterCycle;


    }

    private void initializeRealizationSpotsCase3(){
        case3P1 = calculateIntersectionUpSide(ps,0,L_MAX);
        case3P2 = ps;
        case3P3 = calculateIntersectionUpSide(ps,0,0.5*L_MAX);

        case3L11 = calculateIntersectionUpSide(case3P1,148,L_L);
        case3S11 = calculateIntersectionUpSide(case3P1,148,L_S);
        case3L12 = calculateIntersectionDownSide(case3P1,148,L_L);
        case3S12 = calculateIntersectionDownSide(case3P1,148,L_S);

        case3L21 = calculateIntersectionUpSide(case3P2,32,L_L);
        case3S21 = calculateIntersectionUpSide(case3P2,32,L_S);

        case3L22 = calculateIntersectionDownSide(case3P2,32,L_L);
        case3S22 = calculateIntersectionDownSide(case3P2,32,L_S);

        case3L31 = calculateIntersectionUpSide( case3P3,60,L_L);
        case3S31 = calculateIntersectionUpSide( case3P3,60,L_S);

        case3L32 = calculateIntersectionDownSide( case3P3,60,L_L);
        case3S32 = calculateIntersectionDownSide( case3P3,60,L_S);

        case3SStart = calculateIntersectionUpSide(case3P1,133,L_S);
        case3LStart = calculateIntersectionUpSide(case3P1,133,L_L);

    }

    private YPoint differencePosition( YPoint position){
        if( position.equals(case3P1)){
            return case3P2;
        }
        else{
            return case3P1;
        }
    }

    private int customMod( int number, int mod){
        if( number < 0){
            return number+mod;
        }
        else{
            return number % mod;
        }
    }

    private int getW(){
        int n = innerCycle.size();
        for( int i=0; i < n; i++){
            int currentIndex = i;
            int previousIndex = customMod(i-1,n);
            int nextIndex = customMod(i+1,n);

            Node currentNode = (Node) innerCycle.get(currentIndex);
            Node previousNode = (Node) innerCycle.get(previousIndex);
            Node nextNode = (Node) innerCycle.get(nextIndex);

            if( !isEdgeLong(previousNode,currentNode) && ! isEdgeLong(currentNode,nextNode)){
                return i;
            }

        }
        return -1;
    }


    private void realizeInnerCycle(){
        int w = getW();

        Node firstCycleVertex = (Node) innerCycle.get(0);
        Node secondCycleVertex = (Node) innerCycle.get(1);

        if( w==0){
            basicGraph.moveNode( firstCycleVertex,case3P3);
            basicGraph.moveNode( secondCycleVertex,case3P2);

            for( int i=2 ; i < innerCycle.size() ; i++){
                Node previousNode  = (Node) innerCycle.get(i-1);
                Node currentNode = (Node) innerCycle.get(i);

                YPoint previousPosition = graph2D.getCenter(previousNode);

                if(!isEdgeLong(previousNode,currentNode)){

                    basicGraph.moveNode(currentNode,previousPosition);
                }
                else{
                    YPoint newPosition = differencePosition(previousPosition);
                    basicGraph.moveNode(currentNode,newPosition);
                }
            }
        }
        else{
            basicGraph.moveNode( firstCycleVertex,case3P1);

            for( int i=1 ; i < w ; i++){
                Node previousNode  = (Node) innerCycle.get(i-1);
                Node currentNode = (Node) innerCycle.get(i);

                YPoint previousPosition = graph2D.getCenter(previousNode);

                if(!isEdgeLong(previousNode,currentNode)){

                    basicGraph.moveNode(currentNode,previousPosition);
                }
                else{
                    YPoint newPosition = differencePosition(previousPosition);
                    basicGraph.moveNode(currentNode,newPosition);
                }
            }

            Node vertexW = (Node) innerCycle.get(w);
            basicGraph.moveNode(vertexW,case3P3);

            if( w+1 <= innerCycle.size()-1){
                Node nextVertexW = (Node) innerCycle.get(w+1);
                Node previousVertexW = (Node) innerCycle.get(w-1);
                YPoint positionWMinus1 = graph2D.getCenter(previousVertexW);
                YPoint newPositionW = differencePosition(positionWMinus1);
                basicGraph.moveNode(nextVertexW,newPositionW);

                for( int i=w+2 ; i < innerCycle.size() ; i++){
                    Node previousNode  = (Node) innerCycle.get(i-1);
                    Node currentNode = (Node) innerCycle.get(i);

                    YPoint previousPosition = graph2D.getCenter(previousNode);

                    if(!isEdgeLong(previousNode,currentNode)){

                        basicGraph.moveNode(currentNode,previousPosition);
                    }
                    else{
                        YPoint newPosition = differencePosition(previousPosition);
                        basicGraph.moveNode(currentNode,newPosition);
                    }
                }
            }
        }
    }


    private void realizeOuterCycle(){
        int w = getW();
        Node v0 = (Node) innerCycle.get(0);
        Node u0 = (Node) outerCycle.get(0);
        Node v1 = (Node) innerCycle.get(1);
        Node u1 = (Node) outerCycle.get(1);

        if( w == 0) {
            if (!isEdgeLong(v0, u0)) {
                basicGraph.moveNode(u0, case3S31);
            } else {
                basicGraph.moveNode(u0, case3L31);
            }

            if( !isEdgeLong(u0,u1) && !isEdgeLong(v1,u1)){
                basicGraph.moveNode(u1,case3S21);
            }
            else if( !isEdgeLong(u0,u1) && isEdgeLong(v1,u1) ){
                basicGraph.moveNode(u1,case3L21);
            }
            else if( isEdgeLong(u0,u1) && !isEdgeLong(v1,u1) ){
                basicGraph.moveNode(u1,case3S22);
            }
            else{
                basicGraph.moveNode(u1,case3L22);
            }


            for( int i=2 ; i < outerCycle.size()-1; i++){
                Node currentInner = (Node) innerCycle.get(i);
                Node currentOuter = (Node) outerCycle.get(i);

                Node previousInner = (Node) innerCycle.get(i-1);
                Node previousOuter = (Node) outerCycle.get(i-1);

                YPoint positionPreviousOuter = graph2D.getCenter(previousOuter);
                YPoint destination;

                if( !isEdgeLong(previousInner,currentInner) && !isEdgeLong(previousOuter,currentOuter)){

                    if( !isEdgeLong(currentInner,currentOuter)){
                        destination =aS(positionPreviousOuter);
                    }
                    else{
                        destination = aL(positionPreviousOuter);
                    }

                }
                else if(!isEdgeLong(previousInner,currentInner) && isEdgeLong(previousOuter,currentOuter)){
                    if( !isEdgeLong(currentInner,currentOuter)){
                        destination =bS(positionPreviousOuter);
                    }
                    else{
                        destination = bL(positionPreviousOuter);
                    }
                }
                else {
                    if( !isEdgeLong(currentInner,currentOuter)){
                        destination =cS(positionPreviousOuter);
                    }
                    else{
                        destination = cL(positionPreviousOuter);
                    }
                }

                basicGraph.moveNode(currentOuter,destination);

            }

        }
        else{
            if (!isEdgeLong(v0, u0)) {
                basicGraph.moveNode(u0, case3SStart);
            } else {
                basicGraph.moveNode(u0, case3LStart);
            }

            for( int i=1 ; i < w; i++){
                Node currentInner = (Node) innerCycle.get(i);
                Node currentOuter = (Node) outerCycle.get(i);

                Node previousInner = (Node) innerCycle.get(i-1);
                Node previousOuter = (Node) outerCycle.get(i-1);

                YPoint positionPreviousOuter = graph2D.getCenter(previousOuter);
                YPoint destination;

                if( !isEdgeLong(previousInner,currentInner) && !isEdgeLong(previousOuter,currentOuter)){

                    if( !isEdgeLong(currentInner,currentOuter)){
                        destination =aS(positionPreviousOuter);
                    }
                    else{
                        destination = aL(positionPreviousOuter);
                    }

                }
                else if(!isEdgeLong(previousInner,currentInner) && isEdgeLong(previousOuter,currentOuter)){
                    if( !isEdgeLong(currentInner,currentOuter)){
                        destination =bS(positionPreviousOuter);
                    }
                    else{
                        destination = bL(positionPreviousOuter);
                    }
                }
                else {
                    if( !isEdgeLong(currentInner,currentOuter)){
                        destination =cS(positionPreviousOuter);
                    }
                    else{
                        destination = cL(positionPreviousOuter);
                    }
                }
                if( destination == null){

                    System.out.println("CRASH");
                    System.out.println("Index "+i+" "+previousOuter);
                    System.out.println(case3LStart+" "+case3SStart);
                    System.out.println(positionVertex(positionPreviousOuter));
                }
                basicGraph.moveNode(currentOuter,destination);

            }

            performTransitionMiddleVertex(w);

            for( int i=w+2 ; i < outerCycle.size()-1; i++){
                Node currentInner = (Node) innerCycle.get(i);
                Node currentOuter = (Node) outerCycle.get(i);

                Node previousInner = (Node) innerCycle.get(i-1);
                Node previousOuter = (Node) outerCycle.get(i-1);

                YPoint positionPreviousOuter = graph2D.getCenter(previousOuter);
                YPoint destination;

                if( !isEdgeLong(previousInner,currentInner) && !isEdgeLong(previousOuter,currentOuter)){

                    if( !isEdgeLong(currentInner,currentOuter)){
                        destination =aS(positionPreviousOuter);
                    }
                    else{
                        destination = aL(positionPreviousOuter);
                    }

                }
                else if(!isEdgeLong(previousInner,currentInner) && isEdgeLong(previousOuter,currentOuter)){
                    if( !isEdgeLong(currentInner,currentOuter)){
                        destination =bS(positionPreviousOuter);
                    }
                    else{
                        destination = bL(positionPreviousOuter);
                    }
                }
                else {
                    if( !isEdgeLong(currentInner,currentOuter)){
                        destination =cS(positionPreviousOuter);
                    }
                    else{
                        destination = cL(positionPreviousOuter);
                    }
                }

                basicGraph.moveNode(currentOuter,destination);

            }

        }

        placeLastVertex(w);
    }


    private String positionVertex(YPoint point){
        if( point.equals(case3P1)){
            return "p1";
        }
        else if( point.equals(case3P2)){
            return "p2";
        }
        else if( point.equals(case3P3)){
            return "p3";
        }
        else if( point.equals(case3L11)){
            return "l11";
        }
        else if( point.equals(case3S11)){
            return "s11";
        }
        else if( point.equals(case3L12)){
            return "l12";
        }
        else if( point.equals(case3S12)){
            return "s12";
        }
        else if( point.equals(case3L21)){
            return "l21";
        }
        else if( point.equals(case3S21)){
            return "s21";
        }
        else if( point.equals(case3L22)){
            return "l22";
        }
        else if( point.equals(case3S22)){
            return "s22";
        }
        else if( point.equals(case3SStart)){
            return "sstart";
        }
        else if( point.equals(case3LStart)){
            return "lstart";
        }
        else if( point.equals(case3L31)){
            return "l31";
        }
        else if( point.equals(case3S31)){
            return "s31";
        }
        else if( point.equals(case3L32)){
            return "l32";
        }
        else if( point.equals(case3S32)){
            return "s32";
        }
        else{
            return point.toString();
        }
    }


    private void placeLastVertex( int w){

        Node lastInner = (Node) innerCycle.get(innerCycle.size()-1);
        Node lastOuter = (Node) outerCycle.get(innerCycle.size()-1);

        Node secondLastInner =  (Node) innerCycle.get(innerCycle.size()-2);
        Node secondLastOuter =  (Node) outerCycle.get(outerCycle.size()-2);

        YPoint positionLastInner = graph2D.getCenter(lastInner);
        YPoint positionSecondLastOuter = graph2D.getCenter(secondLastOuter);

        YPoint destination;
        if(w == 0){

            if( positionSecondLastOuter.equals(case3L11) || positionSecondLastOuter.equals(case3S11) ||
                    positionSecondLastOuter.equals(case3L21) || positionSecondLastOuter.equals(case3S21)  ){

               if( !isEdgeLong(secondLastOuter,lastOuter) && !isEdgeLong(lastInner,lastOuter)){
                   destination = extendLineSegment(case3P2,case3P1,-L_S);
               }
               else if(!isEdgeLong(secondLastOuter,lastOuter) && isEdgeLong(lastInner,lastOuter) ){
                   destination = extendLineSegment(case3P2,case3P1,-L_L);
               }
               else if(isEdgeLong(secondLastOuter,lastOuter) && !isEdgeLong(lastInner,lastOuter) ){
                   destination = extendLineSegment(case3P2,case3P1,L_S);
               }
               else{
                   destination = extendLineSegment(case3P2,case3P1,L_L);
               }
            }
            else{



                    if (!isEdgeLong(secondLastOuter, lastOuter) && !isEdgeLong(lastInner, lastOuter)) {
                        destination = case3S12;
                    } else if (!isEdgeLong(secondLastOuter, lastOuter) && isEdgeLong(lastInner, lastOuter)) {
                        destination = case3L12;
                    } else if (isEdgeLong(secondLastOuter, lastOuter) && !isEdgeLong(lastInner, lastOuter)) {
                        destination = extendLineSegment(case3P2, case3P1, L_S);
                    } else {
                        destination = extendLineSegment(case3P2, case3P1, L_L);
                    }

                }





        }
        else{

            Node firstOuter = (Node) outerCycle.get(0);
            YPoint positionFirstOuter = graph2D.getCenter(firstOuter);

            if( positionLastInner.equals(case3P1)){

                if( positionSecondLastOuter.equals(case3L11) || positionSecondLastOuter.equals(case3S11)){

                    if (!isEdgeLong(secondLastOuter, lastOuter) && !isEdgeLong(lastInner, lastOuter)) {
                        destination = MinCircleCircleIntersection(positionSecondLastOuter,L_S,positionFirstOuter,L_L,case3P1);
                    } else if (!isEdgeLong(secondLastOuter, lastOuter) && isEdgeLong(lastInner, lastOuter)) {
                        destination = MaxCircleCircleIntersection(positionSecondLastOuter,L_S,positionFirstOuter,L_L,case3P1);
                    } else if (isEdgeLong(secondLastOuter, lastOuter) && !isEdgeLong(lastInner, lastOuter)) {
                        destination = case3S12;
                    } else {
                        destination = case3L12;
                    }

                }
                else if(positionSecondLastOuter.equals(case3L12) || positionSecondLastOuter.equals(case3S12) ){

                    if (!isEdgeLong(secondLastOuter, lastOuter) && !isEdgeLong(lastInner, lastOuter)) {
                        destination = case3S12;
                    } else if (!isEdgeLong(secondLastOuter, lastOuter) && isEdgeLong(lastInner, lastOuter)) {
                        destination = case3L12;
                    } else if (isEdgeLong(secondLastOuter, lastOuter) && !isEdgeLong(lastInner, lastOuter)) {
                        destination = extendLineSegment(case3P2,case3P1,L_S);
                    } else {
                        destination = extendLineSegment(case3P2,case3P1,L_L);
                    }

                }
                else if(positionSecondLastOuter.equals(case3L21) || positionSecondLastOuter.equals(case3S21) ){

                    if (!isEdgeLong(lastInner, lastOuter)) {
                        destination = extendLineSegment(positionSecondLastOuter,case3P1,L_S);
                    } else{
                        destination = MaxCircleCircleIntersection(positionSecondLastOuter,L_S,positionFirstOuter,L_L,case3P1);
                    }


                }
                else if(positionSecondLastOuter.equals(case3L22) || positionSecondLastOuter.equals(case3S22) ){

                    if (!isEdgeLong(lastInner, lastOuter)) {
                        destination = case3S12;
                    } else{
                        destination = case3L12;
                    }


                }
                else if(positionSecondLastOuter.equals(case3L31) || positionSecondLastOuter.equals(case3S31) ){

                    if (!isEdgeLong(secondLastOuter, lastOuter) && !isEdgeLong(lastInner, lastOuter)) {
                        destination = MinCircleCircleIntersection(positionSecondLastOuter,L_S,positionFirstOuter,L_L,case3P1);
                    } else if (!isEdgeLong(secondLastOuter, lastOuter) && isEdgeLong(lastInner, lastOuter)) {
                        destination = MaxCircleCircleIntersection(positionSecondLastOuter,L_S,positionFirstOuter,L_L,case3P1);
                    } else if (isEdgeLong(secondLastOuter, lastOuter) && !isEdgeLong(lastInner, lastOuter)) {
                        destination = case3S12;
                    } else {
                        destination = case3L12;
                    }

                }
                else{

                    if (!isEdgeLong(secondLastOuter, lastOuter) && !isEdgeLong(lastInner, lastOuter)) {
                        destination = case3S12;
                    } else if (!isEdgeLong(secondLastOuter, lastOuter) && isEdgeLong(lastInner, lastOuter)) {
                        destination = case3L12;
                    } else if (isEdgeLong(secondLastOuter, lastOuter) && !isEdgeLong(lastInner, lastOuter)) {
                        destination = extendLineSegment(case3P2,case3P1,L_S);
                    } else {
                        destination = extendLineSegment(case3P2,case3P1,L_L);
                    }
                }



            }
            else{


                if(positionSecondLastOuter.equals(case3L21) || positionSecondLastOuter.equals(case3S21) ) {

                    if (!isEdgeLong(secondLastOuter, lastOuter) && !isEdgeLong(lastInner, lastOuter)) {
                        destination = MinCircleCircleIntersection(positionSecondLastOuter,L_S,positionFirstOuter,L_L,case3P3);
                    } else if (!isEdgeLong(secondLastOuter, lastOuter) && isEdgeLong(lastInner, lastOuter)) {
                        destination = MaxCircleCircleIntersection(positionSecondLastOuter,L_S,positionFirstOuter,L_L,case3P3);
                    } else if (isEdgeLong(secondLastOuter, lastOuter) && !isEdgeLong(lastInner, lastOuter)) {
                        destination = case3S32;
                    } else {
                        destination = case3L32;
                    }

                }
                else{

                    if (!isEdgeLong(secondLastOuter, lastOuter) && !isEdgeLong(lastInner, lastOuter)) {
                        destination = case3S32;
                    } else if (!isEdgeLong(secondLastOuter, lastOuter) && isEdgeLong(lastInner, lastOuter)) {
                        destination = case3L32;
                    } else if (isEdgeLong(secondLastOuter, lastOuter) && !isEdgeLong(lastInner, lastOuter)) {
                        destination = extendLineSegment(case3P2,case3P3,-L_S);
                    } else {
                        destination = extendLineSegment(case3P2,case3P3,-L_L);
                    }

                }
            }

        }

        basicGraph.moveNode(lastOuter,destination);

    }

    private void performTransitionMiddleVertex(int w){

        Node currentInnerW = (Node) innerCycle.get(w);
        Node currentOuterW = (Node) outerCycle.get(w);

        Node previousInnerW = (Node) innerCycle.get(w-1);
        Node previousOuterW = (Node) outerCycle.get(w-1);

        YPoint positionPreviousOuterW = graph2D.getCenter(previousOuterW);

        YPoint destination;

        if( positionPreviousOuterW.equals(case3L11) || positionPreviousOuterW.equals(case3S11) ||
                positionPreviousOuterW.equals(case3L21) || positionPreviousOuterW.equals(case3S21) ){

            if( !isEdgeLong(previousOuterW,currentOuterW) && !isEdgeLong(currentInnerW,currentOuterW)){
                destination = case3S31;
            }
            else if( !isEdgeLong(previousOuterW,currentOuterW) && isEdgeLong(currentInnerW,currentOuterW)){
                destination = case3L31;
            }
            else if( isEdgeLong(previousOuterW,currentOuterW) && !isEdgeLong(currentInnerW,currentOuterW)){
                destination = case3S32;
            }
            else{
                destination = case3L32;
            }
        }
        else{

            if( !isEdgeLong(previousOuterW,currentOuterW) && !isEdgeLong(currentInnerW,currentOuterW)){
                destination = case3S32;
            }
            else if( !isEdgeLong(previousOuterW,currentOuterW) && isEdgeLong(currentInnerW,currentOuterW)){
                destination = case3L32;
            }
            else if( isEdgeLong(previousOuterW,currentOuterW) && !isEdgeLong(currentInnerW,currentOuterW)){
                destination = case3S31;
            }
            else{
                destination = case3L31;
            }

        }

        basicGraph.moveNode(currentOuterW,destination);

        if( w+1 <= outerCycle.size()-2){

            Node nextOuterNodeW = (Node) outerCycle.get(w+1);
            Node nextInnerNodeW = (Node) innerCycle.get(w+1);

            YPoint positionNextInnerNodeW = graph2D.getCenter(nextInnerNodeW);

            YPoint destination2;

            if( positionNextInnerNodeW.equals(case3P1)){

                if(  destination.equals(case3L31) || destination.equals(case3S31)){

                    if( !isEdgeLong(currentOuterW,nextOuterNodeW) && !isEdgeLong(nextInnerNodeW,nextOuterNodeW)){
                        destination2 = case3S11;
                    }
                    else if( !isEdgeLong(currentOuterW,nextOuterNodeW) && isEdgeLong(nextInnerNodeW,nextOuterNodeW) ){
                        destination2 = case3L11;
                    }
                    else if( isEdgeLong(currentOuterW,nextOuterNodeW) && !isEdgeLong(nextInnerNodeW,nextOuterNodeW) ){
                        destination2 = case3S12;
                    }
                    else{
                        destination2 = case3L12;
                    }
                }
                else{

                    if( !isEdgeLong(currentOuterW,nextOuterNodeW) && !isEdgeLong(nextInnerNodeW,nextOuterNodeW)){
                        destination2 = case3S12;
                    }
                    else if( !isEdgeLong(currentOuterW,nextOuterNodeW) && isEdgeLong(nextInnerNodeW,nextOuterNodeW) ){
                        destination2 = case3L12;
                    }
                    else if( isEdgeLong(currentOuterW,nextOuterNodeW) && !isEdgeLong(nextInnerNodeW,nextOuterNodeW) ){
                        destination2 = case3S11;
                    }
                    else{
                        destination2 = case3L11;
                    }


                }

            }
            else{

                if(  destination.equals(case3L31) || destination.equals(case3S31)){

                    if( !isEdgeLong(currentOuterW,nextOuterNodeW) && !isEdgeLong(nextInnerNodeW,nextOuterNodeW)){
                        destination2 = case3S21;
                    }
                    else if( !isEdgeLong(currentOuterW,nextOuterNodeW) && isEdgeLong(nextInnerNodeW,nextOuterNodeW) ){
                        destination2 = case3L21;
                    }
                    else if( isEdgeLong(currentOuterW,nextOuterNodeW) && !isEdgeLong(nextInnerNodeW,nextOuterNodeW) ){
                        destination2 = case3S22;
                    }
                    else{
                        destination2 = case3L22;
                    }
                }
                else{

                    if( !isEdgeLong(currentOuterW,nextOuterNodeW) && !isEdgeLong(nextInnerNodeW,nextOuterNodeW)){
                        destination2 = case3S22;
                    }
                    else if( !isEdgeLong(currentOuterW,nextOuterNodeW) && isEdgeLong(nextInnerNodeW,nextOuterNodeW) ){
                        destination2 = case3L22;
                    }
                    else if( isEdgeLong(currentOuterW,nextOuterNodeW) && !isEdgeLong(nextInnerNodeW,nextOuterNodeW) ){
                        destination2 = case3S21;
                    }
                    else{
                        destination2 = case3L21;
                    }


                }


            }
            basicGraph.moveNode(nextOuterNodeW,destination2);

        }

    }


    /*
    public void testCase3(){
        YPoint p1 = calculateIntersectionUpSide(ps,0,L_MAX);
        YPoint p2 = ps;

       Node p1Node = basicGraph.createNode(p1);
       Node p2Node = basicGraph.createNode(p2);

       graph2D.getRealizer(p1Node).setLabelText("");
        graph2D.getRealizer(p2Node).setLabelText("");
        graph2D.getRealizer(p1Node).setSize(40,40);
        graph2D.getRealizer(p2Node).setSize(40,40);





        YPoint p1TL = calculateIntersectionUpSide(p1,148,L_L);
        YPoint p1TS = calculateIntersectionUpSide(p1,148,L_S);

        YPoint p2TL = calculateIntersectionUpSide(p2,32,L_L);
        YPoint p2TS = calculateIntersectionUpSide(p2,32,L_S);


        Node p1TLNode = basicGraph.createNode(p1TL);
        Node p1TSNode = basicGraph.createNode(p1TS);

        Node p2TLNode = basicGraph.createNode(p2TL);
        Node p2TSNode = basicGraph.createNode(p2TS);

        graph2D.getRealizer(p1TLNode).setLabelText("");
        graph2D.getRealizer(p1TLNode).setFillColor(Color.blue);
        graph2D.getRealizer(p1TLNode).setSize(40,40);
        graph2D.getRealizer(p1TSNode).setLabelText("");
        graph2D.getRealizer(p1TSNode).setFillColor(Color.blue);
        graph2D.getRealizer(p1TSNode).setSize(40,40);

        graph2D.getRealizer(p2TLNode).setLabelText("");
        graph2D.getRealizer(p2TLNode).setFillColor(Color.blue);
        graph2D.getRealizer(p2TLNode).setSize(40,40);
        graph2D.getRealizer(p2TSNode).setLabelText("");
        graph2D.getRealizer(p2TSNode).setFillColor(Color.blue);
        graph2D.getRealizer(p2TSNode).setSize(40,40);

    //    basicGraph.createNode(p2T);

        YPoint p1BL = calculateIntersectionDownSide(p1,148,L_L);
        YPoint p1BS = calculateIntersectionDownSide(p1,148,L_S);

        YPoint p2BL = calculateIntersectionDownSide(p2,32,L_L);
        YPoint p2BS = calculateIntersectionDownSide(p2,32,L_S);

        Node p1BLNode = basicGraph.createNode(p1BL);
        Node p1BSNode = basicGraph.createNode(p1BS);
        Node p2BLNode = basicGraph.createNode(p2BL);
        Node p2BSNode = basicGraph.createNode(p2BS);

        graph2D.getRealizer(p1BLNode).setLabelText("");
        graph2D.getRealizer(p1BLNode).setFillColor(Color.blue);
        graph2D.getRealizer(p1BSNode).setLabelText("");
        graph2D.getRealizer(p1BSNode).setFillColor(Color.blue);
        graph2D.getRealizer(p1BLNode).setSize(40,40);
        graph2D.getRealizer(p1BSNode).setSize(40,40);

        graph2D.getRealizer(p2BLNode).setLabelText("");
        graph2D.getRealizer(p2BLNode).setFillColor(Color.blue);
        graph2D.getRealizer(p2BSNode).setLabelText("");
        graph2D.getRealizer(p2BSNode).setFillColor(Color.blue);
        graph2D.getRealizer(p2BLNode).setSize(40,40);
        graph2D.getRealizer(p2BSNode).setSize(40,40);
    //    basicGraph.createNode(p2B);


        YPoint p3 = extendLineSegment(p2,p1,-0.5*L_MAX);

        Node p3Node = basicGraph.createNode(p3);
        graph2D.getRealizer(p3Node).setLabelText("");


        YPoint test = calculateIntersectionDownSide(p1,90,L_S);
        basicGraph.createNode(test);
        YPoint test2 = calculateIntersectionDownSide(p1,90,L_L);
        basicGraph.createNode(test2);

        YPoint p3T = calculateIntersectionUpSide(p3,60,L_L);
        YPoint p3B = calculateIntersectionDownSide(p3,60,L_L);

        basicGraph.createNode(p3T);
        basicGraph.createNode(p3B);

        YPoint sStart = calculateIntersectionUpSide(p1,133,L_L);
        YPoint lStart = calculateIntersectionUpSide(p1,133,L_S);

        basicGraph.createNode(sStart);
        basicGraph.createNode(lStart);

       // this.graphView.setAddVertexDistanceCircle( p2T,L_S);
      //  this.graphView.setAddVertexDistanceCircle2( lStart,L_S);

    }
    */


    private void initializRealizationSpotsCase4(){


        p1 = calculateIntersectionUpSide(ps,0,R);
        p3 = calculateIntersectionUpSide(ps,120,R);
        p2 = calculateIntersectionDownSide(ps,120,R);


        l11 = calculateIntersectionDownSide(p1,180-32,L_L);
        s11 = calculateIntersectionDownSide(p1,180-32,L_S);

        l12 = calculateIntersectionUpSide(p1,180-32,L_L);
        s12 = calculateIntersectionUpSide(p1,180-32,L_S);

        l22 = calculateIntersectionUpSide(p2,28,L_L);
        s22 = calculateIntersectionUpSide(p2,28,L_S);


        l21 = calculateIntersectionUpSide(p2,92,L_L);
        s21 = calculateIntersectionUpSide(p2,92,L_S);


        l32 = calculateIntersectionDownSide(p3,92,L_L);
        s32 = calculateIntersectionDownSide(p3,92,L_S);


        l31 = calculateIntersectionDownSide(p3,28,L_L);
        s31 = calculateIntersectionDownSide(p3,28,L_S);

        lStart = calculateIntersectionDownSide(p1,180-45,L_L);
        sStart = calculateIntersectionDownSide(p1,180-45,L_S);

    }


    private YPoint case1SpotDifference( YPoint point){

        if( pointsEqual(point,p1)){
            return p2;
        }
        else{
            return p1;
        }
    }

    private YPoint case2SpotDifference( YPoint point){

        if( pointsEqual(point,p1)){
            return p3;
        }
        else{
            return p1;
        }
    }

    private void realizeOuterCycleCase4(){

        int ml = numLongEdgesInnerCycle();

        Node firstInner = (Node) innerCycle.get(0);
        Node firstOuter = (Node) outerCycle.get(0);


        if( ml % 2 == 0){
            if( !isEdgeLong(firstInner,firstOuter) ){
                basicGraph.moveNode(firstOuter,sStart);
            }
            else{
                basicGraph.moveNode(firstOuter,lStart);
            }

            for( int i=1; i < innerCycle.size()-1 ; i++){
                Node currentOuter = (Node) outerCycle.get(i);
                Node previousOuter  = (Node) outerCycle.get(i-1);

                Node currentInner = (Node) innerCycle.get(i);
                Node previousInner = (Node) innerCycle.get(i-1);

                YPoint positionPreviousOuter = graph2D.getCenter(previousOuter);

                YPoint destination;

                if( !isEdgeLong(previousInner,currentInner) && !isEdgeLong(previousOuter,currentOuter)){

                    if( !isEdgeLong(currentInner,currentOuter)){
                        destination = case1VTS(positionPreviousOuter);
                    }
                    else{
                        destination = case1VTL(positionPreviousOuter);
                    }

                }
                else if( !isEdgeLong(previousInner,currentInner) && isEdgeLong(previousOuter,currentOuter)){
                    if( !isEdgeLong(currentInner,currentOuter)){
                        destination = case2VTS(positionPreviousOuter);
                    }
                    else{
                        destination = case2VTL(positionPreviousOuter);
                    }
                }
                else{
                    if( !isEdgeLong(currentInner,currentOuter)){
                        destination = case3VTS(positionPreviousOuter);
                    }
                    else{
                        destination = case3VTL(positionPreviousOuter);
                    }
                }

                basicGraph.moveNode(currentOuter,destination);

            }

        }
        else{
            if( !isEdgeLong(firstInner,firstOuter) ){
                basicGraph.moveNode(firstOuter,sStart);
            }
            else{
                basicGraph.moveNode(firstOuter,lStart);
            }


            int t = getT();

            for( int i=1; i < t ; i++){
                Node currentOuter = (Node) outerCycle.get(i);
                Node previousOuter  = (Node) outerCycle.get(i-1);

                Node currentInner = (Node) innerCycle.get(i);
                Node previousInner = (Node) innerCycle.get(i-1);

                YPoint positionPreviousOuter = graph2D.getCenter(previousOuter);

                YPoint destination;

                if( !isEdgeLong(previousInner,currentInner) && !isEdgeLong(previousOuter,currentOuter)){

                    if( !isEdgeLong(currentInner,currentOuter)){
                        destination = case1VTS(positionPreviousOuter);
                    }
                    else{
                        destination = case1VTL(positionPreviousOuter);
                    }

                }
                else if( !isEdgeLong(previousInner,currentInner) && isEdgeLong(previousOuter,currentOuter)){
                    if( !isEdgeLong(currentInner,currentOuter)){
                        destination = case2VTS(positionPreviousOuter);
                    }
                    else{
                        destination = case2VTL(positionPreviousOuter);
                    }
                }
                else{
                    if( !isEdgeLong(currentInner,currentOuter)){
                        destination = case3VTS(positionPreviousOuter);
                    }
                    else{
                        destination = case3VTL(positionPreviousOuter);
                    }
                }

                basicGraph.moveNode(currentOuter,destination);

            }

            Node outerT = (Node) outerCycle.get(t);
            Node outerTMinus1 = (Node) outerCycle.get(t-1);
            Node innerT = (Node) innerCycle.get(t);

            YPoint positionOuterTMinus1 = graph2D.getCenter(outerTMinus1);

            YPoint destinationT;

            if( !isEdgeLong(innerT,outerT)){
                destinationT = transitionTS(positionOuterTMinus1);
            }
            else{
                destinationT = transitionTL(positionOuterTMinus1);
            }

            basicGraph.moveNode(outerT,destinationT);


            for( int i=t+1; i < outerCycle.size()-1 ; i++){
                Node currentOuter = (Node) outerCycle.get(i);
                Node previousOuter  = (Node) outerCycle.get(i-1);

                Node currentInner = (Node) innerCycle.get(i);
                Node previousInner = (Node) innerCycle.get(i-1);

                YPoint positionPreviousOuter = graph2D.getCenter(previousOuter);

                YPoint destination;

                if( !isEdgeLong(previousInner,currentInner) && !isEdgeLong(previousOuter,currentOuter)){

                    if( !isEdgeLong(currentInner,currentOuter)){
                        destination = case1NTS(positionPreviousOuter);
                    }
                    else{
                        destination = case1NTL(positionPreviousOuter);
                    }

                }
                else if( !isEdgeLong(previousInner,currentInner) && isEdgeLong(previousOuter,currentOuter)){
                    if( !isEdgeLong(currentInner,currentOuter)){
                        destination = case2NTS(positionPreviousOuter);
                    }
                    else{
                        destination = case2NTL(positionPreviousOuter);
                    }
                }
                else{
                    if( !isEdgeLong(currentInner,currentOuter)){
                        destination = case3NTS(positionPreviousOuter);
                    }
                    else{
                        destination = case3NTL(positionPreviousOuter);
                    }
                }

                basicGraph.moveNode(currentOuter,destination);

            }


        }

        Node lastOuter = (Node) outerCycle.get(outerCycle.size()-1);
        YPoint positionLastOuter = positionLastOuterVertex();

        basicGraph.moveNode(lastOuter,positionLastOuter);

    }



    private void realizeInnerCycleCase4(){

        int ml = numLongEdgesInnerCycle();

        if( ml % 2 == 0){
            System.out.println("even");

            Node firstInnerCycleNode  = (Node) innerCycle.get(0);
            basicGraph.moveNode(firstInnerCycleNode,p1);

            for( int i=1 ; i < innerCycle.size() ; i++){

                Node currentNode = (Node) innerCycle.get(i);
                Node previousNode = (Node) innerCycle.get( (i-1));

                YPoint previousPosition = graph2D.getCenter(previousNode);

                if( !isEdgeLong(previousNode,currentNode)){
                    basicGraph.moveNode(currentNode,previousPosition);
                }
                else{
                    YPoint newPosition = case1SpotDifference(previousPosition);
                    basicGraph.moveNode(currentNode,newPosition);
                }

            }

        }
        else{

            int t = getT();
            System.out.println("T:"+ t);
            System.out.println(innerCycle);
            System.out.println(outerCycle);


            Node firstInnerCycleNode  = (Node) innerCycle.get(0);
            basicGraph.moveNode(firstInnerCycleNode,p1);

            for( int i=1 ; i < t ; i++){

                Node currentNode = (Node) innerCycle.get(i);
                Node previousNode = (Node) innerCycle.get( (i-1));

                YPoint previousPosition = graph2D.getCenter(previousNode);

                if( !isEdgeLong(previousNode,currentNode)){
                    basicGraph.moveNode(currentNode,previousPosition);
                }
                else{
                    YPoint newPosition = case1SpotDifference(previousPosition);
                    basicGraph.moveNode(currentNode,newPosition);
                }

            }

            Node innerCycleTNode = (Node) innerCycle.get(t);
            basicGraph.moveNode(innerCycleTNode,p3);


            for( int i=t+1 ; i < innerCycle.size() ; i++){

                Node currentNode = (Node) innerCycle.get(i);
                Node previousNode = (Node) innerCycle.get( (i-1));

                YPoint previousPosition = graph2D.getCenter(previousNode);

                if( !isEdgeLong(previousNode,currentNode)){
                    basicGraph.moveNode(currentNode,previousPosition);
                }
                else{
                    YPoint newPosition = case2SpotDifference(previousPosition);
                    basicGraph.moveNode(currentNode,newPosition);
                }

            }



        }

    }

    private int numLongEdgesInnerCycle(){
        int edgeCount = 0;
        for( int i=0; i < innerCycle.size() ; i++){
            Node currentNode = (Node) innerCycle.get(i);
            Node nextNode = (Node) innerCycle.get( (i+1) % innerCycle.size());

            if(isEdgeLong(currentNode,nextNode)){
                edgeCount++;
            }
        }
        return edgeCount;
    }


    private int getT(){

        boolean lastEdgeDetected = false;

        for( int i= innerCycle.size()-1; i >=0 ; i--){

            Node currentNode = (Node) innerCycle.get(i);
            Node nextNode = (Node) innerCycle.get( (i+1) % innerCycle.size());

            if(isEdgeLong(currentNode,nextNode)){

                if( !lastEdgeDetected){
                    lastEdgeDetected = true;
                }
                else{
                    return i+1;
                }
            }

        }
        return -1;
    }

    private boolean isEdgeLong(Node source, Node target){
        Edge edge = source.getEdge(target);
        if( longEdges.contains(edge)){
            return true;
        }
        else{
            return false;
        }
    }


    private YPoint aS(YPoint point){

        if( pointsEqual(point,case3SStart) || pointsEqual(point,case3LStart)){
            return case3S11;
        }
        else if ( pointsEqual(point,case3L11) || pointsEqual(point,case3S11)) {
            return case3S11;
        }
        else if (pointsEqual(point,case3L12) || pointsEqual(point,case3S12)) {
            return case3S12;
        }
        else if (pointsEqual(point,case3L21) || pointsEqual(point,case3S21)) {
            return case3S21;
        }
        else if (pointsEqual(point,case3L22) || pointsEqual(point,case3S22)) {
            return case3S22;
        }
        else{
            return null;
        }
    }

    private YPoint aL(YPoint point){

        if( pointsEqual(point,case3SStart) || pointsEqual(point,case3LStart)){
            return case3L11;
        }
        else if ( pointsEqual(point,case3L11) || pointsEqual(point,case3S11)) {
            return case3L11;
        }
        else if (pointsEqual(point,case3L12) || pointsEqual(point,case3S12)) {
            return case3L12;
        }
        else if (pointsEqual(point,case3L21) || pointsEqual(point,case3S21)) {
            return case3L21;
        }
        else if (pointsEqual(point,case3L22) || pointsEqual(point,case3S22)) {
            return case3L22;
        }
        else{
            return null;
        }
    }


    private YPoint bS(YPoint point){

        if( pointsEqual(point,case3SStart) || pointsEqual(point,case3LStart)){
            return case3S12;
        }
        else if ( pointsEqual(point,case3L11) || pointsEqual(point,case3S11)) {
            return case3S12;
        }
        else if (pointsEqual(point,case3L12) || pointsEqual(point,case3S12)) {
            return case3S11;
        }
        else if (pointsEqual(point,case3L21) || pointsEqual(point,case3S21)) {
            return case3S22;
        }
        else if (pointsEqual(point,case3L22) || pointsEqual(point,case3S22)) {
            return case3S21;
        }
        else{
            return null;
        }
    }

    private YPoint bL(YPoint point){

        if( pointsEqual(point,case3SStart) || pointsEqual(point,case3LStart)){
            return case3L12;
        }
        else if ( pointsEqual(point,case3L11) || pointsEqual(point,case3S11)) {
            return case3L12;
        }
        else if (pointsEqual(point,case3L12) || pointsEqual(point,case3S12)) {
            return case3L11;
        }
        else if (pointsEqual(point,case3L21) || pointsEqual(point,case3S21)) {
            return case3L22;
        }
        else if (pointsEqual(point,case3L22) || pointsEqual(point,case3S22)) {
            return case3L21;
        }
        else{
            return null;
        }
    }

    private YPoint cS(YPoint point){

        if( pointsEqual(point,case3SStart) || pointsEqual(point,case3LStart)){
            return case3S21;
        }
        else if ( pointsEqual(point,case3L11) || pointsEqual(point,case3S11)) {
            return case3S21;
        }
        else if (pointsEqual(point,case3L12) || pointsEqual(point,case3S12)) {
            return case3S22;
        }
        else if (pointsEqual(point,case3L21) || pointsEqual(point,case3S21)) {
            return case3S11;
        }
        else if (pointsEqual(point,case3L22) || pointsEqual(point,case3S22)) {
            return case3S12;
        }
        else{
            return null;
        }
    }

    private YPoint cL(YPoint point){

        if( pointsEqual(point,case3SStart) || pointsEqual(point,case3LStart)){
            return case3L21;
        }
        else if ( pointsEqual(point,case3L11) || pointsEqual(point,case3S11)) {
            return case3L21;
        }
        else if (pointsEqual(point,case3L12) || pointsEqual(point,case3S12)) {
            return case3L22;
        }
        else if (pointsEqual(point,case3L21) || pointsEqual(point,case3S21)) {
            return case3L11;
        }
        else if (pointsEqual(point,case3L22) || pointsEqual(point,case3S22)) {
            return case3L12;
        }
        else{
            return null;
        }
    }



    private YPoint transitionTS( YPoint point){
        if( pointsEqual(point,l21) || pointsEqual(point,s21)){
            return s32;
        }
        else if ( pointsEqual(point,l22) || pointsEqual(point,s22)) {
            return s31;
        }
        else{
            return null;
        }
    }

    private YPoint transitionTL( YPoint point){
        if( pointsEqual(point,l21) || pointsEqual(point,s21)){
            return l32;
        }
        else if ( pointsEqual(point,l22) || pointsEqual(point,s22)) {
            return l31;
        }
        else{
            return null;
        }
    }




    private YPoint case3VTS( YPoint point){

        if( pointsEqual(point,sStart) || pointsEqual(point,lStart)){
            return s22;
        }
        else if ( pointsEqual(point,l11) || pointsEqual(point,s11)) {
            return s22;
        }
        else if (pointsEqual(point,l12) || pointsEqual(point,s12)) {
            return s21;
        }
        else if (pointsEqual(point,l21) || pointsEqual(point,s21)) {
            return s12;
        }
        else if (pointsEqual(point,l22) || pointsEqual(point,s22)) {
            return s11;
        }
        else{
            return null;
        }

    }


    private YPoint case3VTL( YPoint point){

        if( pointsEqual(point,sStart) || pointsEqual(point,lStart)){
            return l22;
        }
        else if ( pointsEqual(point,l11) || pointsEqual(point,s11)) {
            return l22;
        }
        else if (pointsEqual(point,l12) || pointsEqual(point,s12)) {
            return l21;
        }
        else if (pointsEqual(point,l21) || pointsEqual(point,s21)) {
            return l12;
        }
        else if (pointsEqual(point,l22) || pointsEqual(point,s22)) {
            return l11;
        }
        else{
            return null;
        }

    }

    private YPoint case2VTS( YPoint point){

        if( pointsEqual(point,sStart) || pointsEqual(point,lStart)){
            return s12;
        }
        else if ( pointsEqual(point,l11) || pointsEqual(point,s11)) {
            return s12;
        }
        else if (pointsEqual(point,l12) || pointsEqual(point,s12)) {
            return s11;
        }
        else if (pointsEqual(point,l21) || pointsEqual(point,s21)) {
            return s22;
        }
        else if (pointsEqual(point,l22) || pointsEqual(point,s22)) {
            return s21;
        }
        else{
            return null;
        }

    }


    private YPoint case2VTL( YPoint point){

        if( pointsEqual(point,sStart) || pointsEqual(point,lStart)){
            return l12;
        }
        else if ( pointsEqual(point,l11) || pointsEqual(point,s11)) {
            return l12;
        }
        else if (pointsEqual(point,l12) || pointsEqual(point,s12)) {
            return l11;
        }
        else if (pointsEqual(point,l21) || pointsEqual(point,s21)) {
            return l22;
        }
        else if (pointsEqual(point,l22) || pointsEqual(point,s22)) {
            return l21;
        }
        else{
            return null;
        }

    }

    private YPoint case1VTS( YPoint point){

        if( pointsEqual(point,sStart) || pointsEqual(point,lStart)){
            return s11;
        }
        else if ( pointsEqual(point,l11) || pointsEqual(point,s11)) {
            return s11;
        }
        else if (pointsEqual(point,l12) || pointsEqual(point,s12)) {
            return s12;
        }
        else if (pointsEqual(point,l21) || pointsEqual(point,s21)) {
            return s21;
        }
        else if (pointsEqual(point,l22) || pointsEqual(point,s22)) {
            return s22;
        }
        else{
            return null;
        }

    }


    private YPoint case1VTL( YPoint point){

        if( pointsEqual(point,sStart) || pointsEqual(point,lStart)){
            return l11;
        }
        else if ( pointsEqual(point,l11) || pointsEqual(point,s11)) {
            return l11;
        }
        else if (pointsEqual(point,l12) || pointsEqual(point,s12)) {
            return l12;
        }
        else if (pointsEqual(point,l21) || pointsEqual(point,s21)) {
            return l21;
        }
        else if (pointsEqual(point,l22) || pointsEqual(point,s22)) {
            return l22;
        }
        else{
            return null;
        }

    }

    private YPoint case1NTS( YPoint point){


        if ( pointsEqual(point,l11) || pointsEqual(point,s11)) {
            return s11;
        }
        else if (pointsEqual(point,l12) || pointsEqual(point,s12)) {
            return s12;
        }
        else if (pointsEqual(point,l31) || pointsEqual(point,s31)) {
            return s31;
        }
        else if (pointsEqual(point,l32) || pointsEqual(point,s32)) {
            return s32;
        }
        else{
            return null;
        }

    }

    private YPoint case1NTL( YPoint point){

        if ( pointsEqual(point,l11) || pointsEqual(point,s11)) {
            return l11;
        }
        else if (pointsEqual(point,l12) || pointsEqual(point,s12)) {
            return l12;
        }
        else if (pointsEqual(point,l31) || pointsEqual(point,s31)) {
            return l31;
        }
        else if (pointsEqual(point,l32) || pointsEqual(point,s32)) {
            return l32;
        }
        else{
            return null;
        }

    }

    private YPoint case2NTS( YPoint point){


        if ( pointsEqual(point,l11) || pointsEqual(point,s11)) {
            return s12;
        }
        else if (pointsEqual(point,l12) || pointsEqual(point,s12)) {
            return s11;
        }
        else if (pointsEqual(point,l31) || pointsEqual(point,s31)) {
            return s32;
        }
        else if (pointsEqual(point,l32) || pointsEqual(point,s32)) {
            return s31;
        }
        else{
            return null;
        }

    }

    private YPoint case2NTL( YPoint point){

        if ( pointsEqual(point,l11) || pointsEqual(point,s11)) {
            return l12;
        }
        else if (pointsEqual(point,l12) || pointsEqual(point,s12)) {
            return l11;
        }
        else if (pointsEqual(point,l31) || pointsEqual(point,s31)) {
            return l32;
        }
        else if (pointsEqual(point,l32) || pointsEqual(point,s32)) {
            return l31;
        }
        else{
            return null;
        }

    }

    private YPoint case3NTS( YPoint point){


        if (pointsEqual(point,l31) || pointsEqual(point,s31)) {
            return s12;
        }
        else if (pointsEqual(point,l32) || pointsEqual(point,s32)) {
            return s11;
        }
        else{
            return null;
        }

    }

    private YPoint case3NTL( YPoint point){

        if (pointsEqual(point,l31) || pointsEqual(point,s31)) {
            return l12;
        }
        else if (pointsEqual(point,l32) || pointsEqual(point,s32)) {
            return l11;
        }
        else{
            return null;
        }

    }


    private YPoint positionLastOuterVertex() {

        YPoint destination = null;

        Node lastOuterNode = (Node) outerCycle.get(outerCycle.size() - 1);
        Node secondLastOuterNode = (Node) outerCycle.get(outerCycle.size() - 2);

        Node lastInnerNode = (Node) innerCycle.get(innerCycle.size() - 1);

        YPoint positionSecondLastOuterNode = graph2D.getCenter(secondLastOuterNode);

        Node firstOuterVertex = (Node) outerCycle.get(0);

        YPoint positionFirstOuter = graph2D.getCenter(firstOuterVertex);

        if ((pointsEqual(positionSecondLastOuterNode, l11) || pointsEqual(positionSecondLastOuterNode, s11))) {

            if( !isEdgeLong(secondLastOuterNode,lastOuterNode) && !isEdgeLong(lastInnerNode,lastOuterNode)){
                destination = MinCircleCircleIntersection(positionFirstOuter,L_L,positionSecondLastOuterNode,L_S,p1);
            }
            else if( !isEdgeLong(secondLastOuterNode,lastOuterNode) && isEdgeLong(lastInnerNode,lastOuterNode)){
                destination = MaxCircleCircleIntersection(positionFirstOuter,L_L,positionSecondLastOuterNode,L_S,p1);
            }
            else if( isEdgeLong(secondLastOuterNode,lastOuterNode) && !isEdgeLong(lastInnerNode,lastOuterNode)){
                destination = s12;
            }
            else{
                destination = l12;
            }

        } else if ((pointsEqual(positionSecondLastOuterNode, l12) || pointsEqual(positionSecondLastOuterNode, s12))){

            if( !isEdgeLong(secondLastOuterNode,lastOuterNode) && !isEdgeLong(lastInnerNode,lastOuterNode)){
                destination = s12;
            }
            else if( !isEdgeLong(secondLastOuterNode,lastOuterNode) && isEdgeLong(lastInnerNode,lastOuterNode)){
                destination = l12;
            }
            else if( isEdgeLong(secondLastOuterNode,lastOuterNode) && !isEdgeLong(lastInnerNode,lastOuterNode)){
                destination = extendLineSegment(ps,p1,L_S);
            }
            else{
                destination = extendLineSegment(ps,p1,L_L);
            }



        }
        else if ((pointsEqual(positionSecondLastOuterNode, l21) || pointsEqual(positionSecondLastOuterNode, s21))){

            if( !isEdgeLong(lastInnerNode,lastOuterNode)){
                destination = s12;
            }
            else{
                destination = l12;
            }

        }
        else if ((pointsEqual(positionSecondLastOuterNode, l22) )){
            if( !isEdgeLong(lastInnerNode,lastOuterNode)){
                destination = extendLineSegment(p2,l22,L_S);
            }
            else{
                destination = MaxCircleCircleIntersection(p1,L_L,l22,L_S,positionFirstOuter);
            }
        }
        else if ((pointsEqual(positionSecondLastOuterNode, s22) )){
            if( !isEdgeLong(lastInnerNode,lastOuterNode)){
                destination = extendLineSegment(p2,s22,L_S);
            }
            else{
                destination = MaxCircleCircleIntersection(p1,L_L,s22,L_S,positionFirstOuter);
            }
        }
        else if ((pointsEqual(positionSecondLastOuterNode, l32) || pointsEqual(positionSecondLastOuterNode, s32))){
            if( !isEdgeLong(lastInnerNode,lastOuterNode)){
                destination = MaxCircleCircleIntersection(p1,L_S,positionSecondLastOuterNode,L_S,positionFirstOuter);
            }
            else{
                destination = l21;
            }
        }
        else{
            if( !isEdgeLong(lastInnerNode,lastOuterNode)){
                destination = s12;
            }
            else{
                destination = l12;
            }
        }


        return destination;


    }



}
