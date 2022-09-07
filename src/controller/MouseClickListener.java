package controller;

import SelectionView.DistanceSelectionView;
import SelectionView.PartitionEdgeSelectionView;
import model.*;
import view.*;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.DecimalFormat;

import model.State;
import y.base.*;

import y.geom.YPoint;
import y.view.*;

import javax.swing.*;


public class MouseClickListener extends MouseAdapter{

	private GraphView graphView;


	public MouseClickListener(GraphView graphView){

		this.graphView = graphView;

	}


	private void addVertex( YPoint click){
		this.graphView.getGraph().createNode(click);
	}

	private void addEdge(Node start, Node end){

		this.graphView.getGraph().createEdge(start,end);
			/*
			Edge createdEdge = start.getEdgeTo(end);
			this.graphView.getGraph2D().getRealizer(createdEdge).addLabel(new EdgeLabel(Integer.toString(createdEdge.index())));
			EdgeLabel asd = new EdgeLabel("ASD");
			asd.setDistance(-100);
			this.graphView.getGraph2D().getRealizer(createdEdge).addLabel(asd);
			this.graphView.getGraph2D().getRealizer(createdEdge).setLineColor(Color.red);
			LineType highlightedEdge = LineType.createLineType(3,
					LineType.DASHED_STYLE);
			this.graphView.getGraph2D().getRealizer(createdEdge).setLineType(highlightedEdge);
			*/



	}

	private void removeVertex ( YPoint click){

		Node noteToDelete = null;


		Graph2D graph2D = this.graphView.getGraph2D();

		for (NodeCursor v = graph2D.nodes(); v.ok(); v.next()) {
			Node currentVertex = v.node();
			if( vertexSelected(click,currentVertex)){
				noteToDelete = currentVertex;
				this.graphView.setState(State.DELETE_VERTEX);
			}
		}

		if( noteToDelete != null){
			this.graphView.getGraph().deleteNode(noteToDelete);
		}

	}

	private void moveVertex( YPoint click){

		double mouseX = click.getX();
		double mouseY = click.getY();

		BasicGraph graph = this.graphView.getGraph();

		Node vertex2Move = this.graphView.getVertex2Move();

		synchronized (graph) {
			NodeRealizer real = graph.getGraph2D().getRealizer(vertex2Move);
			real.setCenter(mouseX, mouseY);
			real.setFillColor(this.graphView.getVERTEX_COLOR());
			this.graphView.getAngleView().update();
			this.graphView.updateView();
			this.graphView.setState(State.MOVE_VERTEX);
		}
	}


	private void selectVertex ( YPoint click){

		Graph2D graph2D = this.graphView.getGraph2D();

		for (NodeCursor v = graph2D.nodes(); v.ok(); v.next()) {
			Node currentVertex = v.node();
			if (vertexSelected(click, currentVertex)) {
				this.graphView.setState(State.MOVE_VERTEX_DESTINATION);
				this.graphView.setVertex2Move(currentVertex);
				graph2D.getRealizer(currentVertex).setFillColor(this.graphView.getSELECTED_VERTEX_COLOR());
				this.graphView.updateView();
				break;
			}
		}
	}


	private void removeEdge ( Node source, Node target){

		Edge edgeToDelete = null;

		Graph2D graph2D = this.graphView.getGraph2D();

		for(EdgeCursor e = graph2D.edges(); e.ok(); e.next()){

			Edge currentEdge = e.edge();
			Node startVertex = currentEdge.source();
			Node endVertex = currentEdge.target();

			if( (startVertex.index() == source.index() && endVertex.index() == target.index()) || (startVertex.index() == target.index() && endVertex.index() == source.index())){
				edgeToDelete = currentEdge;
			}

		}

		if( edgeToDelete != null){
			this.graphView.getGraph().deleteEdge(edgeToDelete);
		}
		this.graphView.setState(State.DELETE_EDGE);

	}

	private void selectEdgePartition(){
		JDialog dialog = new PartitionEdgeSelectionView(this.graphView);
		dialog.setVisible(true);

	}


	private void addVertexWithDistance2Vertices(YPoint click){
		Graph2D graph2D = this.graphView.getGraph2D();
		if(graphView.getSelectState() == State.SELECT_FIRST_VERTEX){
			for (NodeCursor v = graph2D.nodes(); v.ok(); v.next()) {
				Node currentVertex = v.node();
				if (vertexSelected(click, currentVertex)) {
					graphView.getGraph().setSelectedNode(currentVertex);
					graph2D.getRealizer(currentVertex).setFillColor(this.graphView.getSELECTED_VERTEX_COLOR());
					this.graphView.updateView();
					this.graphView.setSelectState(State.SELECT_SECOND_VERTEX);
				}
			}
		}
		else if( graphView.getSelectState() == State.SELECT_SECOND_VERTEX){
			for (NodeCursor v = graph2D.nodes(); v.ok(); v.next()) {
				Node currentVertex = v.node();
				if (vertexSelected(click, currentVertex) && currentVertex.index() != this.graphView.getGraph().getSelectedNode().index()) {
					graphView.getGraph().setSecondSelectedNode(currentVertex);
					graph2D.getRealizer(currentVertex).setFillColor(this.graphView.getSELECTED_VERTEX_COLOR());
					this.graphView.updateView();
					DistanceSelectionView distanceSelectionView = new DistanceSelectionView(this.graphView,1);
					distanceSelectionView.setVisible(true);
				}
			}
		}
		else if(graphView.getSelectState() == State.SELECT_THIRD_VERTEX){

			Node firstSelectedNode = this.graphView.getGraph().getSelectedNode();
			Node secondSelectedNode = this.graphView.getGraph().getSecondSelectedNode();

			YPoint firstSelectedNodePosition = this.graphView.getGraph2D().getCenter(firstSelectedNode);
			YPoint secondSelectedNodePosition = this.graphView.getGraph2D().getCenter(secondSelectedNode);

			double distanceMouseVertex1 = MetricCollection.euklideanDistanceR2(firstSelectedNodePosition.x,firstSelectedNodePosition.y,click.x,click.y);
			double distanceMouseVertex2 = MetricCollection.euklideanDistanceR2(secondSelectedNodePosition.x,secondSelectedNodePosition.y,click.x,click.y);

			double distance = this.graphView.getAddVertexDistance();

			if( distanceMouseVertex1 < distance && distanceMouseVertex2 < distance ){
				this.graphView.getGraph().createNode(click);
				this.graphView.resetState();
			}
		}
	}

	private void addVertexWithDistance( YPoint click){

		Graph2D graph2D = this.graphView.getGraph2D();
		if(graphView.getSelectState() == State.SELECT_FIRST_VERTEX){
			for (NodeCursor v = graph2D.nodes(); v.ok(); v.next()) {
				Node currentVertex = v.node();
				if (vertexSelected(click, currentVertex)) {
					graphView.getGraph().setSelectedNode(currentVertex);

					DistanceSelectionView distanceSelectionView = new DistanceSelectionView(this.graphView,0);
					distanceSelectionView.setVisible(true);
				}
			}
		}
		else if( graphView.getSelectState() == State.SELECT_SECOND_VERTEX){
			Node selectedNode = this.graphView.getGraph().getSelectedNode();
			YPoint selectedNodePosition = this.graphView.getGraph2D().getCenter(selectedNode);
			double distanceMouseVertex = MetricCollection.euklideanDistanceR2(selectedNodePosition.x,selectedNodePosition.y,click.x,click.y);
			if( distanceMouseVertex < this.graphView.getAddVertexDistance()){
				this.graphView.getGraph().createNode(click);
				this.graphView.resetState();
			}

		}
	}


	@Override
	public void mouseClicked(MouseEvent mouse) {

		int mouseX = mouse.getX();
		int mouseY = mouse.getY();

		double viewPositionX = this.graphView.toWorldCoordX(mouseX);
		double viewPositionY = this.graphView.toWorldCoordY(mouseY);

		YPoint mouseClick = new YPoint(viewPositionX,viewPositionY);


		switch (this.graphView.getState()){
			case DELETE_VERTEX: {
				this.removeVertex(mouseClick);
				break;
			}
			case ADD_VERTEX:{
				System.out.println("CURRENTZOOM: "+this.graphView.getZoom());
				this.addVertex(mouseClick);
				graphView.setPreviousState(State.ADD_VERTEX);
				this.graphView.setState(State.ADD_VERTEX);
				break;
			}
			case ADD_VERTEX_WITH_DISTANCE:{
				addVertexWithDistance(mouseClick);
				break;
			}
			case ADD_VERTEX_WITH_DISTANCE_2_VERTICES:{
				addVertexWithDistance2Vertices(mouseClick);
				break;
			}
			case ADD_EDGE:{

					Graph2D graph2D = this.graphView.getGraph2D();
					for(NodeCursor v = graph2D.nodes(); v.ok(); v.next() ){
						Node currentVertex = v.node();

						if( this.vertexSelected(mouseClick,currentVertex)) {
							if(graphView.getSelectState() == State.SELECT_FIRST_VERTEX){
								graph2D.getRealizer(currentVertex).setFillColor(this.graphView.getSELECTED_VERTEX_COLOR());
								this.graphView.updateView();
								this.graphView.getGraph().setSelectedNode(currentVertex);
								graphView.setSelectState(State.SELECT_SECOND_VERTEX);
								break;
							}
							else if ( graphView.getSelectState() == State.SELECT_SECOND_VERTEX){
								Node previouslySelectedNode = this.graphView.getGraph().getSelectedNode();
								graph2D.getRealizer(previouslySelectedNode).setFillColor(this.graphView.getVERTEX_COLOR());
								this.addEdge(previouslySelectedNode,currentVertex);
								graphView.setSelectState(State.SELECT_FIRST_VERTEX);
							}

						}

					}
					break;
			}
			case NOTHING:{
				System.out.println( " X:"+mouseClick.getX()+" Y: "+mouseClick.getY());
				break;
			}
			case DELETE_EDGE: {
				Graph2D graph2D = this.graphView.getGraph2D();
				if( graphView.getSelectState() == State.SELECT_FIRST_VERTEX) {

					for (NodeCursor v = graph2D.nodes(); v.ok(); v.next()) {
						Node currentVertex = v.node();

						if (this.vertexSelected(mouseClick, currentVertex)) {
							this.graphView.getGraph().setSelectedNode(currentVertex);
							graph2D.getRealizer(currentVertex).setFillColor(this.graphView.getSELECTED_VERTEX_COLOR());
							this.graphView.updateView();
							this.graphView.setSelectState(State.SELECT_SECOND_VERTEX);
						}
					}
					this.graphView.setState(State.DELETE_EDGE);
				}
				else if( graphView.getSelectState() == State.SELECT_SECOND_VERTEX) {
					for (NodeCursor v = graph2D.nodes(); v.ok(); v.next()) {
						Node currentVertex = v.node();

						if (this.vertexSelected(mouseClick, currentVertex)) {
							removeEdge(this.graphView.getGraph().getSelectedNode(), currentVertex);
							graph2D.getRealizer(this.graphView.getGraph().getSelectedNode()).setFillColor(this.graphView.getVERTEX_COLOR());
							this.graphView.updateView();
							graphView.setSelectState(State.SELECT_FIRST_VERTEX);
						}
					}
				}


				break;
			}
			case MOVE_VERTEX: {
				this.selectVertex(mouseClick);
				break;
			}
			case MOVE_VERTEX_DESTINATION:{
				this.moveVertex(mouseClick);
				break;
			}
			case SELECT_PARTITION_EDGE:{
				Graph2D graph2D = this.graphView.getGraph2D();
				if( graphView.getSelectState() == State.SELECT_FIRST_VERTEX) {

					for (NodeCursor v = graph2D.nodes(); v.ok(); v.next()) {
						Node currentVertex = v.node();

						if (this.vertexSelected(mouseClick, currentVertex)) {
							this.graphView.getGraph().setSelectedNode(currentVertex);
							graph2D.getRealizer(currentVertex).setFillColor(this.graphView.getSELECTED_VERTEX_COLOR());
							this.graphView.updateView();
							this.graphView.setSelectState(State.SELECT_SECOND_VERTEX);
						}
					}
				}
				else if( graphView.getSelectState() == State.SELECT_SECOND_VERTEX){
					for (NodeCursor v = graph2D.nodes(); v.ok(); v.next()) {
						Node currentVertex = v.node();

						if (this.vertexSelected(mouseClick, currentVertex)) {
							this.graphView.getGraph().setSecondSelectedNode(currentVertex);
							graph2D.getRealizer(this.graphView.getGraph().getSelectedNode()).setFillColor(this.graphView.getVERTEX_COLOR());
							this.selectEdgePartition();
							this.graphView.updateView();
							graphView.setSelectState(State.SELECT_FIRST_VERTEX);
						}
					}
				}
				break;
			}
		}
		this.graphView.getDistanceView().update();
		this.graphView.getAngleView().update();
		this.graphView.getRelationshipsView().updateEdgeColoring();
		this.graphView.getRelationshipsView().updatePartitionLabels();
		this.graphView.getIndependentSetView().update();
		this.graphView.getSpanningTreeView().update();
		this.graphView.getMinimumSpanningTreeView().update();
	}

	@Override
	public void mouseEntered(MouseEvent click) {
		// ignore the guy
		
	}

	@Override
	public void mouseExited(MouseEvent click) {
		// ignore the guy
		//System.out.println(click.getSource());
	}

	@Override
	public void mousePressed(MouseEvent click) {
		// ignore the guy
		//System.out.println("TRIGGER");
	}

	@Override
	public void mouseReleased(MouseEvent click) {
		// ignore the guy
		
	}

	private void getVertexOnCoords(MouseEvent click){
		double mouseX = click.getX();
		double mouseY = click.getY();

		for(NodeCursor v = this.graphView.getGraph2D().nodes(); v.ok(); v.next() ){
			//this.graphView.getGraph2D().removeNode(v.node);
			v.node().toString();
		}
	}
	
	private boolean vertexSelected( YPoint click , Node vertex) {

		Graph2D graph2D = this.graphView.getGraph2D();
		double mouseX = click.getX();
		double mouseY = click.getY();
		double centerPositionX = graph2D.getCenterX(vertex);
		double centerPositionY = graph2D.getCenterY(vertex);
		double embeddingWidth = graph2D.getWidth(vertex);

		double distance = MetricCollection.euklideanDistanceR2(centerPositionX, centerPositionY, mouseX, mouseY);
		if ( distance < embeddingWidth) {
			return true;
		}
		else return false;
	}


	@Override
	public void mouseMoved(MouseEvent e) {


		int mouseX = e.getX();
		int mouseY = e.getY();

		double viewPositionX = this.graphView.toWorldCoordX(mouseX);
		double viewPositionY = this.graphView.toWorldCoordY(mouseY);

		YPoint mouseClick = new YPoint(viewPositionX,viewPositionY);

		switch (this.graphView.getState()){

			case ADD_VERTEX_WITH_DISTANCE:{
				if(graphView.getSelectState() == State.SELECT_SECOND_VERTEX){
					Node selectedNode = this.graphView.getGraph().getSelectedNode();
					YPoint selectedNodePosition = this.graphView.getGraph2D().getCenter(selectedNode);
					double newDistance = MetricCollection.euklideanDistanceR2(selectedNodePosition.x,selectedNodePosition.y,mouseClick.x,mouseClick.y);
					DecimalFormat decimalFormat = new DecimalFormat("#.#");

					String newString = decimalFormat.format(newDistance);
					graphView.getAddVertexDistanceText().move(mouseClick,newString);
				}
				break;
			}
			case ADD_VERTEX_WITH_DISTANCE_2_VERTICES:{
				if(graphView.getSelectState() == State.SELECT_THIRD_VERTEX) {
					Node firstSelectedNode = this.graphView.getGraph().getSelectedNode();
					Node secondSelectedNode = this.graphView.getGraph().getSecondSelectedNode();

					YPoint firstSelectedNodePosition = this.graphView.getGraph2D().getCenter(firstSelectedNode);
					YPoint secondSelectedNodePosition = this.graphView.getGraph2D().getCenter(secondSelectedNode);

					double distanceMouseVertex1 = MetricCollection.euklideanDistanceR2(firstSelectedNodePosition.x, firstSelectedNodePosition.y, mouseClick.x, mouseClick.y);
					double distanceMouseVertex2 = MetricCollection.euklideanDistanceR2(secondSelectedNodePosition.x, secondSelectedNodePosition.y, mouseClick.x, mouseClick.y);

					DecimalFormat decimalFormat = new DecimalFormat("#.#");

					String newString = decimalFormat.format(distanceMouseVertex1) + " | " + decimalFormat.format(distanceMouseVertex2);
					graphView.getAddVertexDistanceText().move(mouseClick, newString);
				}
			}


		}

	}
}
