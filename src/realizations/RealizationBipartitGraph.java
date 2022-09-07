package realizations;

import view.GraphView;
import y.algo.Bipartitions;
import y.base.Edge;
import y.base.Node;
import y.base.NodeList;
import y.base.NodeMap;
import y.geom.YPoint;
import y.util.Maps;

import java.util.ArrayList;

import static y.algo.Bipartitions.RED;
import static y.util.Generics.nodes;

/**
 * Created by chris on 18.04.2018.
 */
public class RealizationBipartitGraph extends Realization {

    private final double DISTANCE_V1_VERTICES = 200;
    private final YPoint startPoint = new YPoint(0,0);

    private NodeList V1;
    private NodeList V2;

    private ArrayList<Edge> longEdges;

    public RealizationBipartitGraph(GraphView graphView) {
        super(graphView);
        this.initializePartitions();
        this.placeV1Vertices();
        this.initializeAssets();
        this.placeV2Vertices();
    }

    private void initializePartitions() {
        this.V1 = new NodeList();
        this.V2 = new NodeList();

        NodeMap vertexMap = Maps.createHashedNodeMap();
        boolean twoColorable = Bipartitions.getBipartition(graph2D, vertexMap);
        if (twoColorable) {

            for (Node node : nodes(graph2D)) {

                Object color = vertexMap.get(node);
                if (color == RED) {
                    V1.add(node);
                } else {
                    V2.add(node);
                }

            }
        }
    }

    private double calculateR(){
        return (DISTANCE_V1_VERTICES/ (2* Math.sin(Math.toRadians(180/V1.size()))));
    }

    private void placeV1Vertices(){
        double r = calculateR();

        double lengthShortEdges = 2*r+1;
        double lengthLongEdges = lengthShortEdges+DISTANCE_V1_VERTICES-2;

        this.genericSmallNeighbourhoodPlacement(V1,startPoint,r);
    }

    private boolean isInCircle( YPoint point, YPoint circleCenter , double radius){
        double pointX = point.getX();
        double pointY = point.getY();

        double centerX = circleCenter.getX();
        double centerY = circleCenter.getY();

        if( Math.abs(Math.pow(pointX-centerX,2)+Math.pow(pointY-centerY,2))-(Math.pow(radius,2)) < 10 ){
            return true;
        }
        else{
            return false;
        }
    }

    private YPoint commonIntersectionPoint( Node currentVertex, NodeList correspondingVertices){

        double r = calculateR();
        double lengthShortEdges = 2*r+1;
        double lengthLongEdges = lengthShortEdges+DISTANCE_V1_VERTICES-10;

        ArrayList<YPoint> intersectionPoints = new ArrayList<>();

        for( int i=0; i < correspondingVertices.size(); i++){

            for( int j=0 ; j < correspondingVertices.size(); j++){

                if( i==j){
                    continue;
                }
                else{
                    Node vertexI = (Node) correspondingVertices.get(i);
                    Node vertexJ = (Node) correspondingVertices.get(j);

                    YPoint posVertexI = graph2D.getCenter(vertexI);
                    YPoint posVertexJ = graph2D.getCenter(vertexJ);

                    double radiusCurrentI;

                    if( !isEdgeLong(currentVertex,vertexI)) {
                        radiusCurrentI = lengthShortEdges;
                    }
                    else{
                        radiusCurrentI = lengthLongEdges;
                    }

                    double radiusCurrentJ;

                    if( !isEdgeLong(currentVertex,vertexJ)) {
                        radiusCurrentJ = lengthShortEdges;
                    }
                    else{
                        radiusCurrentJ = lengthLongEdges;
                    }

                    YPoint[] intersections = calculateCircleCircleIntersection(posVertexI,radiusCurrentI,posVertexJ,radiusCurrentJ);
                    intersectionPoints.add(intersections[0]);
                    intersectionPoints.add(intersections[1]);

                }
            }

        }

        for( YPoint point: intersectionPoints){

            int count = 0;

            for( int i=0; i < correspondingVertices.size(); i++){
                Node circleCenterNode = (Node) correspondingVertices.get(i);
                YPoint circleCenter = graph2D.getCenter(circleCenterNode);

                double radius;

                if( !isEdgeLong(currentVertex,circleCenterNode)) {
                    radius = lengthShortEdges;
                }
                else{
                    radius = lengthLongEdges;
                }

                if( isInCircle(point,circleCenter,radius)){
                    count++;
                }
                else{
                    break;
                }
            }

            if( count == correspondingVertices.size()){
                return point;
            }


        }

        return null;
    }


    private void placeV2Vertices(){

        for( int i = 0; i < V2.size() ; i++){
            Node currentVertex = (Node) V2.get(i);
            NodeList correspondingV1Vertices = new NodeList(currentVertex.neighbors());
            YPoint destination = commonIntersectionPoint(currentVertex,correspondingV1Vertices);
            basicGraph.moveNode(currentVertex,destination);
        }

    }


    private void initializeAssets(){
        this.longEdges = graphView.getRelationships().getEdgesFromPartition(2);
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

    @Override
    public boolean isGraphType() {
        return false;
    }
}
