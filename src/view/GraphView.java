package view;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import model.Relationships;


import model.BasicGraph;
import model.State;

import controller.ViewController;

import util.Circle;
import util.Text;
import y.base.Edge;
import y.base.EdgeCursor;
import y.base.Node;
import y.geom.YCircle;
import y.geom.YPoint;
import y.view.*;

import static y.util.Generics.nodes;

/**
 * Manage the Graph2DView
 * @author heinsohn & hekeler
 *
 */
public class GraphView extends Graph2DView{

	/*
	* Grid Defaults
	 */
	private final double GRID_RESOLUTION = 25;
	private final Color GRID_LINE_COLOR = Color.black;
	private final int GRID_TYPE = GRID_LINES;

	/**
	 *  Vertex Defaults
	 */
	private final Color VERTEX_COLOR = Color.RED;
	private final Color SELECTED_VERTEX_COLOR = Color.ORANGE;

	/**
	 * Edge Defaults
	 */
	private final int EDGE_WIDTH = 3;
	public final LineType EDGE_LINE_STYLE = LineType.createLineType(EDGE_WIDTH,LineType.LINE_STYLE);
	public final Color EDGE_DEFAULT_COLOR = Color.BLACK;



	private double nodeWidth;
	private double nodeHeight;

	private boolean gridEnabled;

	private Circle addVertexDistanceCircle;
	private Circle addVertexDistance2Circle;
	private Text addVertexDistanceText;
	private double addVertexDistance;

	/**
	 * Check for properties of the Basic View.
	 */
	private ViewController viewControl;
	
	private BasicGraph graph;


	private State state;
	private State selectState;
	private State previousState;
	private Node vertex2Move;

	private AngleView angleView;
	private DistanceView distanceView;

	private Relationships relationships;
	private RelationshipsView relationshipsView;

	private IndependentSetView independentSetView;
	private SpanningTreeView spanningTreeView;
	private MinimumSpanningTreeView minimumSpanningTreeView;

	private ArticulationPointView articulationPointView;

	private LayoutView layoutView;

	public GraphView(Graph2D graph, ViewController viewController) {
		super(graph);
		viewControl = viewController;


		this.gridEnabled = true;
		this.initializeGrid();


		this.nodeHeight = GRID_RESOLUTION;
		this.nodeWidth = GRID_RESOLUTION;
		this.state = State.NOTHING;
		this.selectState = State.SELECT_FIRST_VERTEX;
		this.relationships = new Relationships(this);
		this.graph = new BasicGraph(graph,this);
		initView();
	}


	public void initializeGrid(){
		super.setGridColor(GRID_LINE_COLOR);
		super.setGridResolution(GRID_RESOLUTION);
		super.setGridType(GRID_TYPE);
		super.setGridVisible(true);
	}



	

	
	/**
	 * Zoom 
	 * @param
	 */
	public void setZoom(char c) {

		double currentZoom = super.getZoom();
		switch (c) {
		case '+' :
			super.setZoom(currentZoom * 1.2 );
			break;
		case '-' :

			 super.setZoom(currentZoom / 1.2 );


			break;

		default:
			break;
		}


		//this.fitContent();
		super.updateView();
		super.updateWorldRect();


		//System.out.println(super.getZoom());

	}
	
	public void updateView(){

		super.updateView();
	}


	public void resetVertexColoring(){
		for( Node node: nodes(getGraph2D())){
			NodeRealizer realizer =  getGraph2D().getRealizer(node);
			realizer.setFillColor(Color.red);
		}
	}


	//TODO: might not be here?
	public BasicGraph getModel(){
		return graph;
	}



	public void disableNodeLabels(){
		for( Node node: nodes(getGraph2D())){
			NodeRealizer realizer = getGraph2D().getRealizer(node);
			realizer.setLabelText("");
		}
		graph.updateViews();
	}

	public void enableNodeLabels(){
		for( Node node: nodes(getGraph2D())){
			NodeRealizer realizer = getGraph2D().getRealizer(node);
			realizer.setLabelText(""+node.index());
		}
		graph.updateViews();
	}



	public void setGraphViewSize( int x , int y){
		this.setPreferredSize(new Dimension(x,y));
	}

	public void setGraphViewSize( Dimension dimension){
		this.setPreferredSize(dimension);
	}

	
	private void initView() {

		setGridType(View2DConstants.GRID_POINTS);
		setGridResolution(10);
		setGridMode(false);
		setGridVisible(false);


		//setSize(new Dimension(2500, 800));
		//x und y

		//setPreferredSize(new Dimension(100, 900));

		//setPreferredSize(new Dimension(1850, 1000));

		System.out.println(super.getCanvasSize());

	}






	public State getState() {
		return state;
	}

	public void setState(State state) {
		this.state = state;
	}

	public State getSelectState() {
		return selectState;
	}

	public void setSelectState(State selectState) {
		this.selectState = selectState;
	}

	public BasicGraph getGraph() {
		return graph;
	}

	public void setGraph(BasicGraph graph) {
		this.graph = graph;
	}

	public Color getVERTEX_COLOR() {
		return VERTEX_COLOR;
	}

	public Color getSELECTED_VERTEX_COLOR() {
		return SELECTED_VERTEX_COLOR;
	}

	public double getNodeWidth() {
		return nodeWidth;
	}

	public void setNodeWidth(double nodeWidth) {
		this.nodeWidth = nodeWidth;
	}

	public double getNodeHeight() {
		return nodeHeight;
	}

	public void setNodeHeight(double nodeHeight) {
		this.nodeHeight = nodeHeight;
	}

	public State getPreviousState() {
		return previousState;
	}

	public void setPreviousState(State previousState) {
		this.previousState = previousState;
	}

	public Node getVertex2Move() {
		return vertex2Move;
	}

	public void setVertex2Move(Node vertex2Move) {
		this.vertex2Move = vertex2Move;
	}

	public AngleView getAngleView() {
		return angleView;
	}

	public void setAngleView(AngleView angleView) {
		this.angleView = angleView;
	}

	public void resetState(){
		this.state = State.NOTHING;
		this.setSelectState(State.SELECT_FIRST_VERTEX);
		if(this.graph.getSelectedNode() !=null){
			this.graph.getGraph2D().getRealizer(this.graph.getSelectedNode()).setFillColor(this.VERTEX_COLOR);
			this.graph.setSelectedNode(null);
		}
		if(this.graph.getSecondSelectedNode() !=null){
			this.graph.getGraph2D().getRealizer(this.graph.getSecondSelectedNode()).setFillColor(this.VERTEX_COLOR);
			this.graph.setSecondSelectedNode(null);
		}
		if( this.vertex2Move !=null){
			this.graph.getGraph2D().getRealizer(this.vertex2Move).setFillColor(this.VERTEX_COLOR);
			this.vertex2Move = null;
		}
		if( this.addVertexDistanceCircle !=null){
			addVertexDistanceCircle.dispose();
		}
		if( this.addVertexDistance2Circle !=null){
			addVertexDistance2Circle.dispose();
		}
		if( this.addVertexDistanceText != null){
			addVertexDistanceText.dispose();
		}
		this.updateView();
	}



	public void setAddVertexDistanceCircle(YPoint position, double distance){
		if( this.addVertexDistanceCircle !=null){
			addVertexDistanceCircle.dispose();
		}
		this.addVertexDistanceCircle = new Circle(this,position,distance);
	}

	public void setAddVertexDistanceCircle2(YPoint position, double distance){
		if( this.addVertexDistance2Circle !=null){
			addVertexDistance2Circle.dispose();
		}
		this.addVertexDistance2Circle = new Circle(this,position,distance);
	}

	public void setAddVertexDistanceText(YPoint position,String distance, int type){
		if( this.addVertexDistanceText !=null){
			addVertexDistanceText.dispose();
		}
		this.addVertexDistanceText = new Text(this,distance,position,type);
	}

	public void initializeArticulationPointView(){
		this.articulationPointView = new ArticulationPointView(this);
	}

	public Relationships getRelationships() {
		return relationships;
	}

	public void setRelationships(Relationships relationships) {
		this.relationships = relationships;
	}


	public RelationshipsView getRelationshipsView() {
		return relationshipsView;
	}

	public void setRelationshipsView(RelationshipsView relationshipsView) {
		this.relationshipsView = relationshipsView;
	}

	public DistanceView getDistanceView() {
		return distanceView;
	}

	public void setDistanceView(DistanceView distanceView) {
		this.distanceView = distanceView;
	}

	public IndependentSetView getIndependentSetView() {
		return independentSetView;
	}

	public void setIndependentSetView(IndependentSetView independentSetView) {
		this.independentSetView = independentSetView;
	}

	public SpanningTreeView getSpanningTreeView() {
		return spanningTreeView;
	}

	public void setSpanningTreeView(SpanningTreeView spanningTreeView) {
		this.spanningTreeView = spanningTreeView;
	}

	public MinimumSpanningTreeView getMinimumSpanningTreeView() {
		return minimumSpanningTreeView;
	}

	public void setMinimumSpanningTreeView(MinimumSpanningTreeView minimumSpanningTreeView) {
		this.minimumSpanningTreeView = minimumSpanningTreeView;
	}

	public Circle getAddVertexDistanceCircle() {
		return addVertexDistanceCircle;
	}

	public Text getAddVertexDistanceText() {
		return addVertexDistanceText;
	}

	public double getAddVertexDistance() {
		return addVertexDistance;
	}

	public void setAddVertexDistance(double addVertexDistance) {
		this.addVertexDistance = addVertexDistance;
	}

	public boolean isGridEnabled() {
		return gridEnabled;
	}

	public void setGridEnabled(boolean gridEnabled) {
		this.gridEnabled = gridEnabled;
	}

	public LayoutView getLayoutView() {
		return layoutView;
	}

	public void setLayoutView(LayoutView layoutView) {
		this.layoutView = layoutView;
	}

	public void disableGrid(){
		super.setGridVisible(false);
	}

	public void enableGrid(){
		super.setGridVisible(true);
	}

	public void initializeAngleView(){
		this.angleView = new AngleView(this);
	}

	public void initializeDistanceView(){
		this.distanceView = new DistanceView(this);
	}

	public void initializeEdgeView(){
		this.relationshipsView = new RelationshipsView(this);
	}

	public void initializeIndependentSetView() {
		this.independentSetView = new IndependentSetView(this);
	}

	public void initializeSpanningTreeView(){
		this.spanningTreeView = new SpanningTreeView(this);
	}

	public void initializeMinimumSpanningTreeView(){
		this.minimumSpanningTreeView = new MinimumSpanningTreeView(this);
	}

	public void initializeLayoutView(){
		this.layoutView = new LayoutView(this);
	}

	public ArticulationPointView getArticulationPointView() {
		return articulationPointView;
	}
}

