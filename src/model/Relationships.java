package model;

import view.GraphView;
import y.base.Edge;
import y.base.EdgeCursor;
import y.base.EdgeList;
import y.base.Node;
import y.view.Graph2D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by chris on 08.12.2017.
 */
public class Relationships {

    private int k;
    private HashMap<Integer,ArrayList<Edge>> partitions;
    private GraphView graphView;
    private Graph2D graph2D;

    public Relationships(GraphView graphView){
        this.k = 2;
        this.partitions = new HashMap<>();
        this.initializePartitions(k);
        this.graphView = graphView;
        this.graph2D = this.graphView.getGraph2D();
    }

    private void initializePartitions( int k){
        for( int i=1; i < k+1 ; i++){
            partitions.put(i,new ArrayList<Edge>());
        }
    }

    public void initializeDichotomousPartitions( ArrayList<Edge> shortEdges, ArrayList<Edge> longEdges){
        this.k = 2;
        this.partitions.clear();
        partitions.put(1,shortEdges);
        partitions.put(2,longEdges);
    }

    public void insertEdgeInPartition( int index, Edge e){
        ArrayList<Edge> listToInsert = this.partitions.get(index);
        if( listToInsert != null) {
            listToInsert.add(e);
        }
    }


    public void deleteEdgeInPartition( int index, Edge e){
        this.partitions.get(index).remove(e);
    }

    public int getPartitionIndexEdge( Edge e){
        for( int key: partitions.keySet()){

            for( Edge currentEdge: partitions.get(key)){
                if (currentEdge.index() == e.index()){
                    return key;
                }
            }
        }
        return -1;
    }

    public void updateEdge(int newIndex, Edge e){
        int oldIndex = getPartitionIndexEdge(e);
        this.deleteEdgeInPartition(oldIndex,e);
        this.insertEdgeInPartition(newIndex,e);
    }


    public void updatePartitions(){
        ArrayList<Edge> graphEdges = this.getGraphEdges();
        for( int key: partitions.keySet()){

            ArrayList<Edge> edges2Delete = new ArrayList<>();
            for( Edge currentEdge: partitions.get(key)){
                if( !graphEdges.contains(currentEdge)){
                    edges2Delete.add(currentEdge);
                }
            }
            this.partitions.get(key).removeAll(edges2Delete);
        }
    }

    public ArrayList<Edge> getGraphEdges(){

        ArrayList<Edge> result = new ArrayList<>();

        for(EdgeCursor e = graph2D.edges(); e.ok(); e.next()) {

            Edge currentEdge = e.edge();
            result.add(currentEdge);
        }
        return result;
    }

    public int getK() {
        return k;
    }

    public void setK(int k) {
        this.k = k;
        this.partitions.clear();
        this.initializePartitions(k);
    }

    private double getLongestEdgePartition(int index){
        double maximumValue = Integer.MIN_VALUE;
        for( Edge currentEdge: this.partitions.get(index)){

            double distance = lengthEdge(currentEdge);

            if( distance > maximumValue){
                maximumValue = distance;
            }
        }
        return maximumValue;
    }

    private double lengthEdge( Edge currentEdge){
        Node source = currentEdge.source();
        Node target = currentEdge.target();

        double node1X = graph2D.getCenterX(source);
        double node1Y = graph2D.getCenterY(source);
        double node2X = graph2D.getCenterX(target);
        double node2Y = graph2D.getCenterY(target);

        return MetricCollection.euklideanDistanceR2(node1X,node1Y,node2X,node2Y);
    }

    public ArrayList<Edge> violatingEdges(){

        ArrayList<Double> maxima = new ArrayList<>();
        double longestEdgeE1 = getLongestEdgePartition(1);

        maxima.add(longestEdgeE1);
        ArrayList<Edge> violatingEdges = new ArrayList<>();

        for( int key=2 ; key < k+1 ; key++){

            for( Edge edge: partitions.get(key)){


                    double edgeLength = this.lengthEdge(edge);

                    for( Double max: maxima){
                        if( edgeLength < max){
                            violatingEdges.add(edge);
                            break;
                        }

                }

            }
            double longestEdgePreviousPartition = getLongestEdgePartition(key);
            maxima.add(longestEdgePreviousPartition);
        }

        return violatingEdges;
    }


    public HashMap<Integer, ArrayList<Edge>> getPartitions() {
        return partitions;
    }

    public void setPartitions(HashMap<Integer, ArrayList<Edge>> partitions) {
        this.partitions = partitions;
    }

    public ArrayList<Edge> getAllEdgesInPartitions(){
        ArrayList<Edge> result = new ArrayList<>();

        for( int key: this.partitions.keySet()){
            for( Edge e: this.partitions.get(key)){
                result.add(e);
            }
        }
        return result;
    }

    public void updateOnEdgeDeletion( Edge edgeToDelete){
        int edgePartition = this.getPartitionIndexEdge(edgeToDelete);
        if( edgePartition != -1){
            this.deleteEdgeInPartition(edgePartition,edgeToDelete);
        }
    }

    public void updateOnNodeDeletion( Node nodeToDelete){
        EdgeCursor edges = nodeToDelete.edges();
        ArrayList<Edge> edgesToDelete = new ArrayList<>();
        for(EdgeCursor e = edges; e.ok(); e.next()) {
            edgesToDelete.add(e.edge());
            System.out.println(edgesToDelete);
        }

        for( int key: this.partitions.keySet()){
            this.partitions.get(key).removeAll(edgesToDelete);
        }
    }


    public void generateRandomPartitions(){
        this.setK(this.k);
        Random random = new Random();

        for(EdgeCursor e = this.graph2D.edges(); e.ok(); e.next()) {
            Edge currentEdge = e.edge();
            int randomPartition = random.nextInt(k)+1;
            this.insertEdgeInPartition(randomPartition,currentEdge);
        }
        this.graphView.getRelationshipsView().updateEdgeColoring();
        this.graphView.getRelationshipsView().updatePartitionLabels();
        this.graphView.updateView();
    }

    public void setAllEdgesToPartition1(){
        this.setK(this.k);

        for(EdgeCursor e = this.graph2D.edges(); e.ok(); e.next()) {
            Edge currentEdge = e.edge();
            this.insertEdgeInPartition(1,currentEdge);
        }
        this.graphView.getRelationshipsView().updateEdgeColoring();
        this.graphView.getRelationshipsView().updatePartitionLabels();
        this.graphView.updateView();
    }


    public void setRemainingEdgesTo2(){
        ArrayList<Edge> partition1Edges = getEdgesFromPartition(1);

        for(EdgeCursor e = this.graph2D.edges(); e.ok(); e.next()) {
            Edge currentEdge = e.edge();
            if( !partition1Edges.contains(currentEdge)) {
                this.insertEdgeInPartition(2, currentEdge);
            }
        }
        this.graphView.getRelationshipsView().updateEdgeColoring();
        this.graphView.getRelationshipsView().updatePartitionLabels();
        this.graphView.updateView();

    }

    public int getMaxPartition(){
        return k;
    }

    public ArrayList<Edge> getEdgesFromPartition(int partition){
        return this.partitions.get(partition);
    }
}
