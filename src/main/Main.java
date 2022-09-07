package main;

import controller.GraphController;
import controller.ViewController;
import view.GraphView;
import view.ResolutionSelectionView;
import view.View;
import y.view.Graph2D;

public class Main {

	
	public static void main(String[] args) {

		ResolutionSelectionView resolutionSelectionView = new ResolutionSelectionView();


		GraphController gC = new GraphController();
		ViewController viewC = new ViewController(gC);
		Graph2D graph = new Graph2D();
		
		GraphView graphView = new GraphView(graph, viewC);

		// create a view
		@SuppressWarnings("unused")
		View v = new View(viewC, graphView);




		graphView.initializeAngleView();
		graphView.initializeEdgeView();
		graphView.initializeDistanceView();
		graphView.initializeIndependentSetView();
		graphView.initializeSpanningTreeView();
		graphView.initializeMinimumSpanningTreeView();
		graphView.initializeGrid();
		graphView.initializeLayoutView();
		graphView.initializeArticulationPointView();


		v.setResolution(resolutionSelectionView.getDimension());

	}






}
