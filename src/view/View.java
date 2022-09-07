package view;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import controller.MouseClickListener;
import controller.ViewController;

/**
 * Initialize and manage the view
 * 
 * @author heinsohn & hekeler
 * 
 */
public class View extends JFrame {

	/*
	Graph View
	 */
	private GraphView graphView;

	/**
	 * the panel
	 */
	private JPanel panel;

	/**
	 * The Menu bar
	 */
	private JMenuBar menuBar;
	/**
	 * Menu bulletins
	 */
	private JMenu file, graph , graphClasses, layouts, relationships, view, analysis, realizations, demonstration;
	/**



	/*
	Menu Items for Analysis
	 */
	public JMenuItem generateIndependentSet, generateSpanningTree, generateMST, generateArticulationPoints;


	/*
	Menu Items for Graph Management
	 */
	public JMenuItem addVertex,addVertexWidthDistance,add2VertexDistance, addEdge, deleteVertex,
						deleteEdge, moveVertex, resetGraph,showCoordinates;


	/*
	Menu Items for Partition Management
	 */
	public JMenuItem turnOnOffPartitions, turnOnOffRelationsshipViolations, generateRandomPartitions, setAllEdges1,
						selectPartition, selectEdgePartition, setRemaingEdges2;

	/*
	Menu Items for Generation of Graphs
	 */
	public JMenuItem generateLozengeGraph,realizationHanoiGraph,generateKRectangulargraph,
			generateRectangularGraph, outerPlanarDoubleRectangle,doublePrismGraph,prismGraph,
			groetschGraph,generateRootedTree, generateWheelGraph, generateCircle, generateHamiltonianGraph,
			hamiltonianPathGraph, generateCaterpillar, generate2Tree, generatek3m,generateSunflower;


	/*
	Menu Items for Layout
	 */
	public JMenuItem organic, circular, random, organicLayout,smartOrganicLayout,
			radialLayout,balloonLayout,circularLayout,rootedTreeLayout;

	/*
	Menu Items for IO Operations

	 */
	public JMenuItem load, save;


	/*
	Menu Items for View manipulation
	 */
	public JMenuItem resetZoom,drawGrid, zoomMinus, fitContent, zoomPlus, enableLabels,disableLabels,
					turnOnOffAngels, turnOnOffDistances, resetVertexColor;


	/*
	Menu Items for Calculating a Realization
	 */
	public JMenuItem realizationHammiltonGraph,realizationpartial2tree,realizationRootedTree,
			realizationRectangularGraph,realizationDiamondGaph,realizationHamiltonPath,realizationTree, realizationSunflower,
			realizationPrismGraph,  realization2Tree, circleRealization,caterPillarRealization, wheelRealization, realizationk3m;


	/*
	Menu Items for Analysis
	 */
	public JMenuItem twoColoring,threeColoring,fourColoring;


	/*
	Misc Menu Items
	 */
	public JMenuItem demonstrationCircle,demonstrationBiggerCircle, demonstrationprism1,demonstrationprism2;


	/**
	 * The Label for infos
	 */
	public JLabel infoLabel;
	
	public View(ViewController controller, GraphView graphView) {
		super("Graph Analytica");

		controller.setView(this);
		controller.setGraphView(graphView);
		
		panel = new JPanel(new FlowLayout());
		panel.setBackground(Color.BLACK);



		initMenuBar(controller);

		this.mouseClickListenerGraphView(graphView);
		panel.add(graphView);
		panel.addMouseWheelListener((MouseWheelListener) controller);

		super.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		super.getContentPane().setLayout(new BorderLayout(20, 20));
		

		super.setJMenuBar(menuBar);
		
		super.getRootPane().setContentPane(panel);

		this.graphView = graphView;

		super.setLocationRelativeTo(null); // center current View
		super.setVisible(true);

		super.pack();


	}


	/**
	 * initialize Menu Bar
	 */
	private void initMenuBar(ActionListener buttonListener) {

		menuBar = new JMenuBar();
		menuBar.setLayout(new GridBagLayout());
		menuBar.setBackground(Color.BLACK);

		file = new JMenu("File ");
		file.setFont(new Font("Arial",Font.PLAIN,20));
		file.setOpaque(true);
		file.setBackground(Color.WHITE);
		menuBar.add(file);
		load = new JMenuItem("load graph");
		load.setFont(new Font("Arial",Font.PLAIN,20));
		load.addActionListener(buttonListener);
		file.add(load);
		save = new JMenuItem("save graph");
		save.setFont(new Font("Arial",Font.PLAIN,20));
		save.addActionListener(buttonListener);
		file.add(save);


		graph = new JMenu("Graph");
		graph.setFont(new Font("Arial",Font.PLAIN,20));
		graph.setOpaque(true);
		graph.setBackground(Color.WHITE);

		addVertex = new JMenuItem("Add Vertex");
		addVertex.addActionListener(buttonListener);
		addVertexWidthDistance = new JMenuItem("Add Vertex with Distance k relative to another Vertex");
		addVertexWidthDistance.addActionListener(buttonListener);
		add2VertexDistance = new JMenuItem("Add Vertex with Distance k relative to 2 Vertices");
		add2VertexDistance.addActionListener(buttonListener);
		addEdge = new JMenuItem("Add Edge");
		addEdge.addActionListener(buttonListener);
		deleteVertex = new JMenuItem("Delete Vertex");
		deleteVertex.addActionListener(buttonListener);
		deleteEdge = new JMenuItem("Delete Edge");
		deleteEdge.addActionListener(buttonListener);
		resetGraph = new JMenuItem("Reset Graph");
		resetGraph.addActionListener(buttonListener);
		moveVertex = new JMenuItem("Move Vertex");
		moveVertex.addActionListener(buttonListener);
		showCoordinates = new JMenuItem("Show Coordinates");
		showCoordinates.addActionListener(buttonListener);

		graph.add(addVertex);
		graph.add(addVertexWidthDistance);
		graph.add(add2VertexDistance);
		graph.add(addEdge);
		graph.add(deleteVertex);
		graph.add(deleteEdge);
		graph.add(resetGraph);
		graph.add(moveVertex);
		graph.add(showCoordinates);

		relationships = new JMenu("Relationships");
		relationships.setFont(new Font("Arial",Font.PLAIN,20));
		relationships.setOpaque(true);
		relationships.setBackground(Color.WHITE);

		selectPartition = new JMenuItem("Select Partitions");
		selectPartition.addActionListener(buttonListener);
		selectEdgePartition = new JMenuItem("Select Partition for Edge");
		selectEdgePartition.addActionListener(buttonListener);
		generateRandomPartitions = new JMenuItem("Generate Random Partitions");
		generateRandomPartitions.addActionListener(buttonListener);
		setAllEdges1 = new JMenuItem("Set All Partition to 1");
		setAllEdges1.addActionListener(buttonListener);
		setRemaingEdges2 = new JMenuItem("Set Remaining Partitions to 2");
		setRemaingEdges2.addActionListener(buttonListener);

		relationships.add(selectPartition);
		relationships.add(selectEdgePartition);
		relationships.add(generateRandomPartitions);
		relationships.add(setAllEdges1);
		relationships.add(setRemaingEdges2);


		view = new JMenu("View");
		view.setFont(new Font("Arial",Font.PLAIN,20));
		view.setOpaque(true);
		view.setBackground(Color.WHITE);


		turnOnOffDistances = new JMenuItem("Draw Edge Distances");
		turnOnOffDistances.addActionListener(buttonListener);

		this.turnOnOffPartitions = new JMenuItem("Draw Edge Partitions");
		this.turnOnOffPartitions.addActionListener(buttonListener);

		this.turnOnOffRelationsshipViolations = new JMenuItem("Draw Relationsship Violations");
		this.turnOnOffRelationsshipViolations.addActionListener(buttonListener);

		turnOnOffAngels = new JMenuItem("Draw Angles");
		turnOnOffAngels.addActionListener(buttonListener);

		enableLabels = new JMenuItem("Enable Vertex Labels");
		disableLabels = new JMenuItem("Disable Vertex Labels");
		enableLabels.addActionListener(buttonListener);
		disableLabels.addActionListener(buttonListener);

		drawGrid = new JMenuItem("Draw Grid");
		drawGrid.addActionListener(buttonListener);
		fitContent = new JMenuItem("Fit Content");
		fitContent.addActionListener(buttonListener);
		resetZoom = new JMenuItem("Reset Zoom");
		resetZoom.addActionListener(buttonListener);

		resetVertexColor = new JMenuItem("Reset Vertex Color");
		resetVertexColor.addActionListener(buttonListener);

		view.add(turnOnOffDistances);
		view.add(turnOnOffPartitions);
		view.add(turnOnOffRelationsshipViolations);
		view.add(turnOnOffAngels);
		view.add(enableLabels);
		view.add(disableLabels);
		view.add(drawGrid);
		view.add(fitContent);
		view.add(resetZoom);
		view.add(resetVertexColor);


		layouts = new JMenu("Layout");
		layouts.setFont(new Font("Arial",Font.PLAIN,20));
		layouts.setOpaque(true);
		layouts.setBackground(Color.WHITE);

		organicLayout = new JMenuItem("Apply Organic Layout");
		smartOrganicLayout = new JMenuItem("Apply Smart Organic Layout");
		radialLayout = new JMenuItem("Apply Radial Layout");
		balloonLayout = new JMenuItem("Apply Balloon Layout");
		circularLayout = new JMenuItem("Apply Circular Layout");
		rootedTreeLayout = new JMenuItem("Apply Rooted Tree Layout");

		organicLayout.addActionListener(buttonListener);
		smartOrganicLayout.addActionListener(buttonListener);
		radialLayout.addActionListener(buttonListener);
		balloonLayout.addActionListener(buttonListener);
		circularLayout.addActionListener(buttonListener);
		rootedTreeLayout.addActionListener(buttonListener);


		layouts.add(organicLayout);
		layouts.add(smartOrganicLayout);
		layouts.add(radialLayout);
		layouts.add(balloonLayout);
		layouts.add(circularLayout);
		layouts.add(rootedTreeLayout);


		analysis = new JMenu("Analysis");
		analysis.setFont(new Font("Arial",Font.PLAIN,20));
		analysis.setOpaque(true);
		analysis.setBackground(Color.WHITE);

		generateIndependentSet = new JMenuItem("Generate Independent Set");
		generateIndependentSet.addActionListener(buttonListener);
		generateSpanningTree = new JMenuItem("Generate Spanning Tree");
		generateSpanningTree.addActionListener(buttonListener);
		generateMST = new JMenuItem("Generate Minimum Spanning Tree");
		generateMST.addActionListener(buttonListener);
		fourColoring = new JMenuItem("Calculate 4-Coloring");
		fourColoring.addActionListener(buttonListener);
		threeColoring = new JMenuItem("Calculate 3-Coloring");
		threeColoring.addActionListener(buttonListener);
		twoColoring = new JMenuItem("Calculate 2-Coloring");
		twoColoring.addActionListener(buttonListener);
		generateArticulationPoints = new JMenuItem("Calculate Articulation Points");
		generateArticulationPoints.addActionListener(buttonListener);

		analysis.add(generateIndependentSet);
		analysis.add(generateSpanningTree);
		analysis.add(generateMST);
		analysis.add(twoColoring);
		analysis.add(threeColoring);
		analysis.add(fourColoring);
		analysis.add(generateArticulationPoints);



		graphClasses = new JMenu("Graph Class");
		graphClasses.setFont(new Font("Arial",Font.PLAIN,20));
		graphClasses.setOpaque(true);
		graphClasses.setBackground(Color.WHITE);

		generateCircle = new JMenuItem("Generate Circular Graph");
		generateCircle.addActionListener(buttonListener);
		generateLozengeGraph = new JMenuItem("Generate Diamond Graph");
		generateLozengeGraph.addActionListener(buttonListener);
		generateWheelGraph = new JMenuItem("Generate Wheel Graph");
		generateWheelGraph.addActionListener(buttonListener);
		generateHamiltonianGraph = new JMenuItem("Generate Hamiltonian Cycle Graph");
		generateHamiltonianGraph.addActionListener(buttonListener);
		generateCaterpillar = new JMenuItem("Generate Caterpillar");
		generateCaterpillar.addActionListener(buttonListener);
		generate2Tree = new JMenuItem("Generate 2-Tree");
		generate2Tree.addActionListener(buttonListener);
		generateRootedTree = new JMenuItem("Generate Rooted Tree");
		generateRootedTree.addActionListener(buttonListener);
		groetschGraph = new JMenuItem("Generate Groetsch Graph");
		groetschGraph.addActionListener(buttonListener);
		prismGraph = new JMenuItem("Generate Prism Graph");
		prismGraph.addActionListener(buttonListener);
		doublePrismGraph = new JMenuItem("Generate Double Prism Graph");
		doublePrismGraph.addActionListener(buttonListener);
		outerPlanarDoubleRectangle = new JMenuItem("Generate Double Rectangle Graph");
		outerPlanarDoubleRectangle.addActionListener(buttonListener);
		generateKRectangulargraph = new JMenuItem("Generate k-Rectangular Graph");
		generateKRectangulargraph.addActionListener(buttonListener);
		hamiltonianPathGraph = new JMenuItem("Generate Hamiltonian Path Graph");
		hamiltonianPathGraph.addActionListener(buttonListener);
		generateRectangularGraph = new JMenuItem("Generate Ladder Graph");
		generateRectangularGraph.addActionListener(buttonListener);
		generatek3m = new JMenuItem("Generate Complete Bipartite Graph K_3_m");
		generatek3m.addActionListener(buttonListener);
		generateSunflower = new JMenuItem("Generate Sunflower Graph");
		generateSunflower.addActionListener(buttonListener);


		graphClasses.add(generateCircle);
		graphClasses.add(generateLozengeGraph);
		graphClasses.add(generateRectangularGraph);
		graphClasses.add(generatek3m);
		graphClasses.add(outerPlanarDoubleRectangle);
		graphClasses.add(generateHamiltonianGraph);
		graphClasses.add(hamiltonianPathGraph);
		graphClasses.add(generateWheelGraph);
		graphClasses.add(generateSunflower);
		graphClasses.add(generate2Tree);
		graphClasses.add(generateCaterpillar);
		graphClasses.add(generateRootedTree);
		graphClasses.add(groetschGraph);
		graphClasses.add(prismGraph);
		graphClasses.add(doublePrismGraph);
		graphClasses.add(generateKRectangulargraph);


		realizations = new JMenu("Realizations");
		realizations.setFont(new Font("Arial",Font.PLAIN,20));
		realizations.setOpaque(true);
		realizations.setBackground(Color.WHITE);

		circleRealization = new JMenuItem("Calculate Circular Realization");
		circleRealization.addActionListener(buttonListener);
		realizationRectangularGraph = new JMenuItem("Calculate Ladder Graph Realization");
		realizationRectangularGraph.addActionListener(buttonListener);
		wheelRealization = new JMenuItem("Calculate Wheel Graph Realization");
		wheelRealization.addActionListener(buttonListener);
		caterPillarRealization = new JMenuItem("Calculate Caterpillar + 4 Colorability Realization");
		caterPillarRealization.addActionListener(buttonListener);
		realization2Tree = new JMenuItem("Calculate 2 Tree Realization");
		realization2Tree.addActionListener(buttonListener);
		realizationRootedTree = new JMenuItem("Calculate Rooted Tree Realization");
		realizationRootedTree.addActionListener(buttonListener);
		realizationHammiltonGraph = new JMenuItem("Calculate Hamiltonian Cycle Realization");
		realizationHammiltonGraph.addActionListener(buttonListener);
		//realizationPrismGraph = new JMenuItem("Calculate Prism Graph Realization");
		//realizationPrismGraph.addActionListener(buttonListener);
		realizationHamiltonPath = new JMenuItem("Calculate Hamiltonian Path Realization");
		realizationHamiltonPath.addActionListener(buttonListener);
		realizationHanoiGraph = new JMenuItem("Calculate Hanoi Graph Realization");
		realizationHanoiGraph.addActionListener(buttonListener);
		realizationDiamondGaph = new JMenuItem("Calculate Diamond Graph Realization");
		realizationDiamondGaph.addActionListener(buttonListener);
		realizationpartial2tree = new JMenuItem("Calculate Partial 2-Tree Realization");
		realizationpartial2tree.addActionListener(buttonListener);
		realizationk3m = new JMenuItem("Calculate K_3_m Realization");
		realizationk3m.addActionListener(buttonListener);
		realizationTree = new JMenuItem("Calculate Tree Realization");
		realizationTree.addActionListener(buttonListener);
		realizationSunflower = new JMenuItem("Calculate Sunflower Graph Realization");
		realizationSunflower.addActionListener(buttonListener);

		realizations.add(realizationTree);
		realizations.add(circleRealization);
		realizations.add(realizationRectangularGraph);
		realizations.add(realizationk3m);
		realizations.add(realizationDiamondGaph);
		realizations.add(realizationHammiltonGraph);
		realizations.add(realizationHamiltonPath);
		realizations.add(wheelRealization);
		realizations.add(realizationSunflower);
		realizations.add(realization2Tree);
		realizations.add(realizationpartial2tree);
		realizations.add(caterPillarRealization);
		realizations.add(realizationRootedTree);
		//realizations.add(realizationPrismGraph);
		realizations.add(realizationHanoiGraph);


		demonstration = new JMenu("Demonstrations");
		demonstration.setFont(new Font("Arial",Font.PLAIN,20));
		demonstration.setOpaque(true);
		demonstration.setBackground(Color.WHITE);

		demonstrationCircle = new JMenuItem("Fit Equal Circle Intersection Ellipse");
		demonstrationCircle.addActionListener(buttonListener);
		demonstrationBiggerCircle = new JMenuItem("Fit Unequal Circle Intersection Ellipse");
		demonstrationBiggerCircle.addActionListener(buttonListener);
		demonstrationprism1 = new JMenuItem("Prism Realization Case 3");
		demonstrationprism1.addActionListener(buttonListener);
		demonstrationprism2 = new JMenuItem("Prism Realization Case 4");
		demonstrationprism2.addActionListener(buttonListener);

		demonstration.add(demonstrationCircle);
		demonstration.add(demonstrationBiggerCircle);
		demonstration.add(demonstrationprism1);
		demonstration.add(demonstrationprism2);

		menuBar.add(graph);
		menuBar.add(graphClasses);
		menuBar.add(relationships);
		menuBar.add(realizations);
		menuBar.add(view);
		menuBar.add(layouts);
		menuBar.add(analysis);
		menuBar.add(demonstration);

		JPanel infoPanel = new JPanel();
		panel.add(infoPanel, BorderLayout.PAGE_END);


	}


	/*
	Apply Mouse Click Listener to current jPanel
	 */
	private void mouseClickListenerGraphView(GraphView graphView){

		MouseClickListener listener = new MouseClickListener(graphView);

		for( int i=0; i < graphView.getComponentCount(); i++){
			Component currentComponent = graphView.getComponent(i);
			if( currentComponent instanceof JPanel){
				graphView.getComponent(i).addMouseListener(listener);
				graphView.getComponent(i).addMouseMotionListener(listener);
				break;
			}
		}

	}

	public void setResolution(Dimension dimension){
		graphView.setGraphViewSize(dimension);
		this.setSize(dimension);
		this.pack();
		this.setLocationRelativeTo(null);
		graphView.updateUI();
		graphView.updateWorldRect();
		graphView.updateView();
	}

}
