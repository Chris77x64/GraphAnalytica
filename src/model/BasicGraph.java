package model;

import java.awt.event.MouseEvent;
import java.util.ArrayList;

import sweep.SweepLine;
import view.GraphView;
import y.base.Edge;
import y.base.Node;
import y.base.NodeCursor;
import y.base.NodeMap;
import y.geom.YCircle;
import y.geom.YPoint;
import y.view.EdgeLabel;
import y.view.EdgeRealizer;
import y.view.Graph2D;
import y.view.*;

import static y.util.Generics.nodes;

/**
 * Stores the Graph
 * @author heinsohn
 *
 */
public class BasicGraph {

	private final float EDGE_SELECT_TRESHHOLD = 150;
	private static double diskFactor = .5;

	/**
	 * Nodemap with Ply-circles
	 */
	private NodeMap circleMap;
	

	private GraphView graphView;
	private Graph2D graph;

	private Node selectedNode;
	private Node secondSelectedNode;
	
	private int maxPly = 0;
	private boolean emptyPly = true;
	private String statistics;
	private ArrayList<YPoint> maxPlyRegionpoints;
	
	private ArrayList<ConflictPoint> conflicts = new ArrayList<>();

	public BasicGraph(Graph2D g, GraphView graphView) {
		graph = g;
		this.graphView = graphView;
		circleMap = g.createNodeMap();
		updatePlyDisks();
		this.selectedNode = null;
		//calculatePlyNumber();
	}
	
	/**
	 * get all the PlyDisks 
	 * @return
	 */
	public YCircle[] getPlyDisks(){
		updatePlyDisks();
		int nodeCount = graph.nodeCount();
		YCircle[] result = new YCircle[nodeCount];
		int i = 0;
		for (NodeCursor v = graph.nodes(); v.ok(); v.next()) {
			result[i++] = (YCircle) circleMap.get(v.node());
		}
		return result;
	}
	
	public double getPlyRadius(Node n){
		return ((YCircle) circleMap.get(n)).radius;
	}
	
	public String getStatistics(){
		return statistics;
	}
	
	/**
	 * Get the Ply number of the graph
	 * @return
	 */
	public int getMaxPly(){return maxPly;}
	/**
	 * Is the Graph empty-ply?
	 * @return
	 */
	public boolean getEmptyPly(){return emptyPly;}
	
	/**
	 * Get the Ypoints of the max ply regions
	 */
	public ArrayList<YPoint> getMaxPlyRegionPoints(){
		return maxPlyRegionpoints;
	}
	/**
	 * Calculate all Ply-Disks
	 */
	private void updatePlyDisks(){
		
		for (NodeCursor v = graph.nodes(); v.ok(); v.next()) {
			double maxEdgeLength = 0;

			// find the longest edge length connected to the node v
			for (NodeCursor n = v.node().neighbors(); n.ok(); n.next()) {
				double distance = getDistance(v.node(), n.node());
				if (distance > maxEdgeLength) {
					maxEdgeLength = distance;
				}
			}
			// add disk to node map
			circleMap.set(v.node(), new YCircle(graph.getCenter(v.node()), maxEdgeLength* diskFactor));
		}
		
	}

	/**
	 * Start the calculation of the Ply number. 
	 */
	public void calculatePlyNumber(){
		// calculate Ply.Number
//		System.out.println("Calculate is called");
		SweepLine sweep = new SweepLine();
		sweep.initialize(circleMap, graph.nodes());
		sweep.executeSweepLine();
		maxPly = sweep.getMaxPly();
		emptyPly = sweep.getEmptyPly();
		
		maxPlyRegionpoints = sweep.getConflictNodes();
//		conflicts = new ArrayList<>();
//		for (ArrayList<Node> l : sweep.getConflictNodes()){
//			conflicts.add(new ConflictPoint(l, this));
//		}
		statistics = sweep.getCalculationProperties();
	}
	
	/**
	 * Calculate the distance between two Nodes
	 */
	private double getDistance(Node node, Node node2) {
		
		YPoint a = graph.getCenter(node);
		YPoint b = graph.getCenter(node2);
		return a.distanceTo(b);
	}
	
	public YPoint getCenter(Node node){
		return graph.getCenter(node);
	}
	
	/**
	 * set the center of a node to location of ypoint
	 * @param node
	 * @param p
	 */
	public void setCenter(Node node, YPoint p){
		graph.getRealizer(node).setCenter(p.x, p.y);
	}
	
	/**
	 * get the Graph2D
	 * @return
	 */
	public Graph2D getGraph2D(){
		return this.graph;
	}

	/**
	 * Get the List of Conflict points
	 * @return
	 */
	public ArrayList<ConflictPoint> getConflictPoints() {
		return conflicts;
	}

	public NodeMap getCircleMap() {
		return circleMap;
	}

	public void setCircleMap(NodeMap circleMap) {
		this.circleMap = circleMap;
	}

	public Node getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(Node selectedNode) {
		this.selectedNode = selectedNode;
	}

	public float getEDGE_SELECT_TRESHHOLD() {
		return EDGE_SELECT_TRESHHOLD;
	}

	public Node getSecondSelectedNode() {
		return secondSelectedNode;
	}

	public void setSecondSelectedNode(Node secondSelectedNode) {
		this.secondSelectedNode = secondSelectedNode;
	}

	public void createEdge(Node source, Node target){
		if( ! this.graph.containsEdge(source,target)) {
			this.graph.createEdge(source, target);
			Edge createdEdge = source.getEdgeTo(target);
			EdgeRealizer edgeRealizer = this.graph.getRealizer(createdEdge);

			// Partition
			edgeRealizer.addLabel(new EdgeLabel(""));
			// Distance
			edgeRealizer.addLabel(new EdgeLabel(""));
			// Angle Tail
			edgeRealizer.addLabel(new EdgeLabel(""));
			// Angle Head
			edgeRealizer.addLabel(new EdgeLabel(""));

			edgeRealizer.setLineType(this.graphView.EDGE_LINE_STYLE);
			edgeRealizer.setLineColor(this.graphView.EDGE_DEFAULT_COLOR);
			this.updateViews();

		}
		this.graphView.updateView();
	}



	public void resetNodeRealizers(){
		for( Node node : nodes(graph)){
			NodeRealizer nodeRealizer = graph.getRealizer(node);
			nodeRealizer.setFillColor(this.graphView.getVERTEX_COLOR());
		}
		this.graphView.updateView();
	}


	public Edge createEdgeReturned(Node source, Node target){
		if( ! this.graph.containsEdge(source,target)) {
			this.graph.createEdge(source, target);
			Edge createdEdge = source.getEdgeTo(target);
			EdgeRealizer edgeRealizer = this.graph.getRealizer(createdEdge);

			// Partition
			edgeRealizer.addLabel(new EdgeLabel(""));
			// Distance
			edgeRealizer.addLabel(new EdgeLabel(""));
			// Angle Tail
			edgeRealizer.addLabel(new EdgeLabel(""));
			// Angle Head
			edgeRealizer.addLabel(new EdgeLabel(""));

			edgeRealizer.setLineType(this.graphView.EDGE_LINE_STYLE);
			edgeRealizer.setLineColor(this.graphView.EDGE_DEFAULT_COLOR);
			return createdEdge;
		}
		return null;
	}

	public void createNode(MouseEvent click){
		double mouseX = click.getX();
		double mouseY = click.getY();

		Node createdNode = graph.createNode(mouseX,mouseY);
		NodeRealizer nodeRealizer = graph.getRealizer(createdNode);
		nodeRealizer.setSize(this.graphView.getNodeWidth(),this.graphView.getNodeHeight());
		nodeRealizer.setFillColor(this.graphView.getVERTEX_COLOR());
		nodeRealizer.setLabel(new NodeLabel(Integer.toString(createdNode.index())));
		this.updateViews();
	}

	public Node createNode(){

		Node createdNode = graph.createNode();
		NodeRealizer nodeRealizer = graph.getRealizer(createdNode);
		nodeRealizer.setSize(this.graphView.getNodeWidth(),this.graphView.getNodeHeight());
		nodeRealizer.setFillColor(this.graphView.getVERTEX_COLOR());
		nodeRealizer.setLabel(new NodeLabel(Integer.toString(createdNode.index())));
		this.updateViews();
		return createdNode;
	}

	public void moveNode(Node vertex, YPoint destination){
		NodeRealizer nodeRealizer = this.graph.getRealizer(vertex);
		nodeRealizer.setCenter(destination.getX(),destination.getY());
		this.updateViews();
	}

	public Node createNode(YPoint click){
		double mouseX = click.getX();
		double mouseY = click.getY();

		Node createdNode = graph.createNode(mouseX,mouseY);
		NodeRealizer nodeRealizer = graph.getRealizer(createdNode);
		nodeRealizer.setSize(this.graphView.getNodeWidth(),this.graphView.getNodeHeight());
		nodeRealizer.setFillColor(this.graphView.getVERTEX_COLOR());
		nodeRealizer.setLabel(new NodeLabel(Integer.toString(createdNode.index())));
		this.updateViews();
		return createdNode;
	}

	public void deleteNode(Node vertex){
		this.graphView.getRelationships().updateOnNodeDeletion(vertex);
		this.graph.removeNode(vertex);
		this.updateNodeLabels();
		this.updateViews();
	}

	public void deleteEdge(Edge edge){
		this.graphView.getRelationships().updateOnEdgeDeletion(edge);
		graph.removeEdge(edge);
		this.updateViews();
	}

	public void updateNodeLabels(){
		for (NodeCursor v = graph.nodes(); v.ok(); v.next()) {
			Node currentNode = v.node();
			NodeRealizer nodeRealizer = graph.getRealizer(currentNode);
			nodeRealizer.setLabel(new NodeLabel(Integer.toString(currentNode.index())));
		}
	}


	public void resetGraph(){

		for (NodeCursor v = graph.nodes(); v.ok(); v.next()) {
			Node currentNode = v.node();
			graph.removeNode(currentNode);
		}

		int k = this.graphView.getRelationships().getK();
		this.graphView.getRelationships().setK(k);
		this.updateViews();
	}

	public Edge getEdgeFromVertex( Node source, Node target){
		if( source.getEdgeTo(target) != null){
			return source.getEdgeTo(target);
		}
		else if(source.getEdgeFrom(target) !=null){
			return source.getEdgeFrom(target);
		}
		else{
			return null;
		}
	}

	public void updateViews(){
		this.graphView.getSpanningTreeView().update();
		this.graphView.getMinimumSpanningTreeView().update();
		this.graphView.getIndependentSetView().update();
		this.graphView.getRelationshipsView().updateEdgeColoring();
		this.graphView.getRelationshipsView().updatePartitionLabels();
		this.graphView.getAngleView().update();
		this.graphView.getDistanceView().update();
		this.graphView.updateView();
	}

}


