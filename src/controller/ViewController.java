package controller;

import SelectionView.*;
import algo.Blossom;
import algo.KColoring;
import controller.GraphController.LayoutType;
import graphClasses.GroetschGraph;
import model.State;
import realizations.*;

import view.*;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;

/**
 * Manage changes in View Is registered as ActionListener to the View
 * 
 * @author heinsohn & hekeler
 * 
 */
public class ViewController implements ActionListener, MouseWheelListener,MouseListener {

	private static boolean autoUpdate = false;
	
	private static boolean drawConflicts = false;

	private View view;

	private Thread t;

	private GraphView gView;


	private GraphController graphControl;


	/**
	 * initialize relative local variables
	 */
	private String sep = File.separator;
	private String basePath = new File("").getAbsolutePath();

	public ViewController(GraphController gC) {
		super();
		graphControl = gC;
		basePath = basePath + sep + "GraphTest";
	}

	public void setView(View v) {
		view = v;
	}

	public void setGraphView(GraphView v) {
		gView = v;
	}

	public boolean isAutoUpdate() {
		return autoUpdate;
	}

	
	public void actionPerformed(ActionEvent e) {

		if (view == null)
			return;

		if (e.getSource() == view.addVertex){
			if(gView.getState() != State.ADD_VERTEX){
				gView.resetState();
			}
			gView.setState(State.ADD_VERTEX);
		}
		else if( e.getSource() == view.addVertexWidthDistance){
			if( gView.getState() != State.ADD_VERTEX_WITH_DISTANCE){
				gView.resetState();
			}
			gView.setState(State.ADD_VERTEX_WITH_DISTANCE);
		}
		else if(e.getSource() == view.add2VertexDistance){
			if( gView.getState() !=State.ADD_VERTEX_WITH_DISTANCE_2_VERTICES){
				gView.resetState();
			}
			gView.setState(State.ADD_VERTEX_WITH_DISTANCE_2_VERTICES);
		}
		else if (e.getSource() == view.addEdge){
			if(gView.getState() != State.ADD_EDGE){
				gView.resetState();
			}
			gView.setState(State.ADD_EDGE);
		}
		else if (e.getSource() == view.deleteVertex){
			if(gView.getState() != State.DELETE_VERTEX){
				gView.resetState();
			}
			gView.setState(State.DELETE_VERTEX);
		}
		else if( e.getSource() == view.resetZoom){
			gView.setZoom(1);
			gView.setViewPoint2D(0,0);
			gView.setWorldRect2D(0,0,1850,1000);

		}
		else if( e.getSource() == view.realizationTree){
			RealizationTree realizationTree = new RealizationTree(gView);
		}
		else if(e.getSource() == view.generateSunflower){
			NodeSelectionView nodeSelectionView = new NodeSelectionView(4,200,gView,11);
			nodeSelectionView.setVisible(true);
		}
		else if( e.getSource() == view.realizationSunflower){
			RealizationSunflowerGraph realizationSunflowerGraph = new RealizationSunflowerGraph(gView);
		}
		else if( e.getSource() == view.demonstrationCircle){
			demoCircleSelectionView demo = new demoCircleSelectionView(gView);
		}
		else if( e.getSource() == view.demonstrationBiggerCircle){
			Blossom blossom = new Blossom(gView);
			//demoCircleBiggerSelectionView demo = new demoCircleBiggerSelectionView(gView);
		}
		else if( e.getSource() == view.realizationHanoiGraph){
			RealizationHanoiGraph realizationHanoiGraph = new RealizationHanoiGraph(gView);
		}
		else if( e.getSource() == view.drawGrid){
			boolean gridEnabled = gView.isGridEnabled();
			if(gridEnabled){
				gView.disableGrid();
				gView.setGridEnabled(false);
			}
			else{
				gView.enableGrid();
				gView.setGridEnabled(true);
			}
			gView.updateView();
		}
		else if (e.getSource() == view.deleteEdge){
			if(gView.getState() != State.DELETE_EDGE){
				gView.resetState();
			}
			gView.setState(State.DELETE_EDGE);
		}
		else if( e.getSource() == view.generatek3m){
			NodeSelectionView view = new NodeSelectionView(0,200,gView,12);
			view.setVisible(true);
		}
		else if( e.getSource() == view.turnOnOffAngels){
			AngleView angleView = gView.getAngleView();
			if( !angleView.isShowAngels()) {
				angleView.setShowAngels(true);
				angleView.update();
			}
			else{
				angleView.setShowAngels(false);
				angleView.reset();
			}
		}
		else if( e.getSource() == view.realizationk3m){
			RealizationK3M realizationK3M = new RealizationK3M(gView);
		}
		else if( e.getSource() == view.turnOnOffDistances){
			if( !gView.getDistanceView().isDistanceViewEnabled()){
				gView.getDistanceView().setDistanceViewEnabled(true);
				gView.getDistanceView().update();
			}
			else{
				gView.getDistanceView().setDistanceViewEnabled(false);
				gView.getDistanceView().reset();
			}
		}
		else if( e.getSource() == view.generateArticulationPoints){
			gView.getArticulationPointView().visualizeArticulationPoints();
		}
		else if( e.getSource() == view.enableLabels){
			gView.enableNodeLabels();
		}
		else if( e.getSource() == view.disableLabels){
			gView.disableNodeLabels();
		}
		else if( e.getSource() == view.generateRectangularGraph){
			NodeSelectionView nodeSelectionView = new NodeSelectionView(1,201,gView,6);
			nodeSelectionView.setVisible(true);
		}
		else if( e.getSource() == view.generateLozengeGraph){
			NodeSelectionView nodeSelectionView = new NodeSelectionView(3,201,gView,9);
			nodeSelectionView.setVisible(true);
		}
		else if( e.getSource() == view.realizationRectangularGraph){
			RealizationRectangularGraph realizationRectangularGraph = new RealizationRectangularGraph(gView);
		}
		else if( e.getSource() == view.turnOnOffPartitions){
			RelationshipsView relationshipsView = gView.getRelationshipsView();
			if( !relationshipsView.isEnableLabels()){
				relationshipsView.setEnableLabels(true);
				relationshipsView.updatePartitionLabels();
			}
			else{
				gView.getRelationshipsView().setEnableLabels(false);
				gView.getRelationshipsView().resetPartitionLabels();
			}
		}
		else if( e.getSource() == view.realizationpartial2tree){
			RealizationPartial2Tree realizationPartial2Tree = new RealizationPartial2Tree(gView);
		}
		else if( e.getSource() == view.realizationDiamondGaph){
			RealizationDimaondsGraph realizationDimaondsGraph = new RealizationDimaondsGraph(gView);
		}
		else if( e.getSource() == view.demonstrationprism1){
			RealizationPrismGraph999999 realizationPrismGraph999999 = new RealizationPrismGraph999999(gView,false);
		}
		else if( e.getSource() == view.demonstrationprism2){
			RealizationPrismGraph999999 realizationPrismGraph999999 = new RealizationPrismGraph999999(gView,true);
		}
		else if( e.getSource() == view.turnOnOffRelationsshipViolations){
			RelationshipsView relationshipsView = gView.getRelationshipsView();
			if( !relationshipsView.isEnableColoring()){
				relationshipsView.setEnableColoring(true);
				relationshipsView.updateEdgeColoring();
			}
			else{
				relationshipsView.setEnableColoring(false);
				relationshipsView.resetEdgeColoring();
			}
		}
		else if( e.getSource() == view.moveVertex){
			if(gView.getState() != State.MOVE_VERTEX){
				gView.resetState();
			}
			gView.setState(State.MOVE_VERTEX);
		}
		else if( e.getSource() == view.showCoordinates){
			if(gView.getState() != State.NOTHING){
				gView.resetState();
			}
			gView.setState(State.NOTHING);
		}
		else if( e.getSource() == view.selectEdgePartition){
			gView.setState(State.SELECT_PARTITION_EDGE);
		}
		else if( e.getSource() == view.generateCaterpillar){
			CaterpillarSelectionView caterpillarSelectionView = new CaterpillarSelectionView(gView);
			caterpillarSelectionView.setVisible(true);
		}
		else if( e.getSource() == view.caterPillarRealization){
			RealizationCaterpillarGraph realizationCaterpillarGraph = new RealizationCaterpillarGraph(gView);
		}
		else if( e.getSource() == view.generateRandomPartitions){
			gView.getRelationships().generateRandomPartitions();
		}
		else if( e.getSource() == view.setAllEdges1){
			gView.getRelationships().setAllEdgesToPartition1();
		}
		else if(e.getSource() == view.generateIndependentSet){
			IndependentSetView independentSetView = gView.getIndependentSetView();
			if(!independentSetView.isEnableIndependentSetView()){
				independentSetView.setEnableIndependentSetView(true);
				independentSetView.update();
			}
			else{
				independentSetView.setEnableIndependentSetView(false);
				independentSetView.reset();
			}
		}
		else if( e.getSource() == view.generateHamiltonianGraph){
			HammiltonianCircleGraphView hammiltonianCircleGraphView = new HammiltonianCircleGraphView(gView);
			hammiltonianCircleGraphView.setVisible(true);
		}
		else if( e.getSource() == view.hamiltonianPathGraph){
			HammiltonianPathGraphView hamiltonianPathGraphView = new HammiltonianPathGraphView(gView);
			hamiltonianPathGraphView.setVisible(true);
		}
		else if( e.getSource() == view.realizationHammiltonGraph){
			RealizationHamiltonianCircleGraph realizationHamiltonianCircleGraph = new RealizationHamiltonianCircleGraph(gView);
		}
		else if( e.getSource() == view.realizationHamiltonPath){
			RealizationHamiltonianPathGraph realizationHamiltonianPathGraph = new RealizationHamiltonianPathGraph(gView);
		}
		else if( e.getSource() == view.groetschGraph){
			GroetschGraph groetschGraph = new GroetschGraph(gView);
		}
		else if( e.getSource() ==  view.organicLayout){
			gView.getLayoutView().applyOrganicLayout();
		}
		else if( e.getSource() == view.smartOrganicLayout){
			gView.getLayoutView().applySmartOrganicLayout();
		}
		else if( e.getSource() == view.radialLayout){
			gView.getLayoutView().applyRadialLayout();
		}
		else if( e.getSource() == view.balloonLayout){
			gView.getLayoutView().applyBalloonLayout();
		}
		else if( e.getSource() == view.circularLayout){
			gView.getLayoutView().applyCircularLayout();
		}
		else if( e.getSource() == view.rootedTreeLayout){
			gView.getLayoutView().applyRootedTreeLayout();
		}
		else if( e.getSource() == view.generateKRectangulargraph){
			kRectangularGraphSelectionView view = new kRectangularGraphSelectionView(gView);
		}
		else if( e.getSource() == view.generateWheelGraph){
			NodeSelectionView nodeSelectionView = new NodeSelectionView(4,51,gView,0);
			nodeSelectionView.setVisible(true);
			//WheelGraph wheelGraph = new WheelGraph(12,gView);
			//gView.getRelationships().generateRandomPartitions();
			//RealizationWheelGraph realizationWheelGraph = new RealizationWheelGraph(gView);
		}
		else if( e.getSource() == view.prismGraph){
			NodeSelectionView nodeSelectionView = new NodeSelectionView(3,500,gView,5);
			nodeSelectionView.setVisible(true);
		}
		else if( e.getSource() == view.doublePrismGraph){
			NodeSelectionView nodeSelectionView = new NodeSelectionView(3,500,gView,7);
			nodeSelectionView.setVisible(true);
		}
		else if( e.getSource() == view.outerPlanarDoubleRectangle){
			NodeSelectionView nodeSelectionView = new NodeSelectionView(3,500,gView,8);
			nodeSelectionView.setVisible(true);
		}
		else if( e.getSource() == view.realizationPrismGraph){
			RealizationPrismGraph realizationPrismGraph = new RealizationPrismGraph(gView);
		}
		else if( e.getSource() == view.generate2Tree){
			NodeSelectionView nodeSelectionView = new NodeSelectionView(4,501,gView,3);
			nodeSelectionView.setVisible(true);
		}
		else if( e.getSource() == view.generateRootedTree){
			NodeSelectionView nodeSelectionView = new NodeSelectionView(4,501,gView,4);
			nodeSelectionView.setVisible(true);
		}
		else if( e.getSource() == view.wheelRealization){
			RealizationWheelGraph realizationWheelGraph = new RealizationWheelGraph(gView);
			gView.getRelationshipsView().updateEdgeColoring();
			gView.updateView();
		}
		else if( e.getSource() == view.realizationRootedTree){
			RealizationRootedTree realizationRootedTree = new RealizationRootedTree(gView);
		}
		else if( e.getSource() == view.generateCircle){
			NodeSelectionView nodeSelectionView = new NodeSelectionView(3,501,gView,1);
			nodeSelectionView.setVisible(true);
		}
		else if( e.getSource() == view.fourColoring){
			gView.getGraph().resetNodeRealizers();
			KColoring kColoring = new KColoring(gView.getGraph2D(),4,true);

		}
		else if( e.getSource() == view.threeColoring){
			gView.getGraph().resetNodeRealizers();
			KColoring kColoring = new KColoring(gView.getGraph2D(),3,true);
		}
		else if( e.getSource() == view.twoColoring){
			gView.getGraph().resetNodeRealizers();
			KColoring kColoring = new KColoring(gView.getGraph2D(),2,true);
		}
		else if( e.getSource() == view.realization2Tree){
			Realization2Tree realization2Tree = new Realization2Tree(gView);
		}
		else if( e.getSource() == view.generateHamiltonianGraph){
			NodeSelectionView nodeSelectionView = new NodeSelectionView(4,51,gView,2);
			nodeSelectionView.setVisible(true);
		}
		else if( e.getSource() == view.setRemaingEdges2){
			gView.getRelationships().setRemainingEdgesTo2();
		}
		else if( e.getSource() == view.resetGraph){
			gView.getGraph().resetGraph();
		}
		else if( e.getSource() == view.circleRealization){
			RealizationCircleGraph realizationCircleGraph = new RealizationCircleGraph(gView);
		}
		else if( e.getSource() == view.generateSpanningTree){
			SpanningTreeView spanningTreeView = gView.getSpanningTreeView();
			if(!spanningTreeView.isEnableSpanningTreeView()){
				spanningTreeView.setEnableSpanningTreeView(true);
				spanningTreeView.update();
			}
			else{
				spanningTreeView.setEnableSpanningTreeView(false);
				spanningTreeView.reset();
			}
		}
		else if( e.getSource() == view.generateMST){
			MinimumSpanningTreeView minimumSpanningTreeView = gView.getMinimumSpanningTreeView();
			if(!minimumSpanningTreeView.isEnableSpanningTreeView()){
				minimumSpanningTreeView.setEnableSpanningTreeView(true);
				minimumSpanningTreeView.update();
			}
			else{
				minimumSpanningTreeView.setEnableSpanningTreeView(false);
				minimumSpanningTreeView.reset();
			}
		}
		else if (e.getSource() == view.organic) {
			graphControl.setLayout(LayoutType.ORGANIC, gView);
			gView.updateView();

			gView.fitContent();
		} else if (e.getSource() == view.random) {
			graphControl.setLayout(LayoutType.RANDOM, gView);
			gView.updateView();

			gView.fitContent();
		} else if (e.getSource() == view.circular) {
			graphControl.setLayout(LayoutType.CIRCULAR, gView);
			gView.updateView();

			gView.fitContent();
		} else if (e.getSource() == view.selectPartition){
			JDialog PartitionSelectionView = new PartitionSelectionView(gView);
			PartitionSelectionView.setAlwaysOnTop(true);
			PartitionSelectionView.setVisible(true);
		} else if (e.getSource() == view.save) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(basePath));
			int returnValue = fileChooser.showSaveDialog(view.getRootPane());
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				String filepath = fileChooser.getSelectedFile()
						.getAbsolutePath();
				graphControl.saveAsGML(gView.getGraph2D(), filepath);
			}
			basePath = fileChooser.getCurrentDirectory().toString();
		} else if (e.getSource() == view.load) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(new File(basePath));
			int returnValue = fileChooser.showOpenDialog(view.getRootPane());
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				gView.getGraph2D().clear();
				String filepath = fileChooser.getSelectedFile()
						.getAbsolutePath();
				if (filepath.endsWith(".gml")) {
					graphControl.loadGML(gView.getGraph2D(), filepath);
				}
				else if (filepath.endsWith(".graphml")) {
					graphControl.loadGraphML(gView.getGraph2D(), filepath);
//					graphControl.setLayout(LayoutType.RANDOM, gView);
				}
				else{
					graphControl.loadGML(gView.getGraph2D(),filepath);
				}
				
				gView.updateView();
				gView.updateView();
				gView.fitContent();
				
				basePath = fileChooser.getCurrentDirectory().toString();

			} else {
				System.out.println("Graph couldn't be loaded");
			}

		} else if (e.getSource() == view.zoomPlus) {
			gView.setZoom('+');
		} else if (e.getSource() == view.zoomMinus) {
			gView.setZoom('-');
		} else if (e.getSource() == view.fitContent) {
			gView.fitContent();
		} else if (e.getSource() == gView) {
			String s = e.getActionCommand();
			view.infoLabel.setText(s);
			return;
		}
		else if(e.getSource() == view.resetVertexColor){
			gView.resetVertexColoring();
			gView.updateView();
		}

		updateView();
	}

	public void updateView(){
		if (isAutoUpdate())
		gView.updateView();
	}
	/**
	 * Enable Mouse Wheel zooming
	 */
	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		if (arg0.getWheelRotation() == -1) {
			gView.setZoom('+');
		} else if (arg0.getWheelRotation() == 1) {
			gView.setZoom('-');
		}
		updateView();
	}

	/**
	 * Prints the String s to the infolabel
	 * 
	 * @param s
	 */
	public void showInfo(String s) {
		if (view == null)
			return;
		view.infoLabel.setText(s);
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		System.out.println(e.getSource());
	}

	@Override
	public void mousePressed(MouseEvent e) {
		System.out.println(e.getSource());
		System.out.println("Baum");
	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}
}
