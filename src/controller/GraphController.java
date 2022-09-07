package controller;

import java.io.IOException;

import view.GraphView;
import y.base.EdgeCursor;
import y.io.GMLIOHandler;
import y.io.GraphMLIOHandler;
import y.io.IOHandler;
import y.layout.circular.CircularLayouter;
import y.layout.organic.OrganicLayouter;
import y.layout.random.RandomLayouter;
import y.view.Arrow;
import y.view.Graph2D;

/**
 * Manage changes in the Graph
 * 
 * @author heinsohn
 * 
 */
public class GraphController {

	private IOHandler ioh;
	private GMLIOHandler iohGML;
	
	public enum LayoutType {ORGANIC, RANDOM, CIRCULAR};

	public GraphController() {
		ioh = new GraphMLIOHandler();
		iohGML = new GMLIOHandler();
	}
	
	/**
	 * Set the layout for the Graph & update view
	 * @param type one of ORGANIC, RANDOM, CIRCULAR
	 * @param gView
	 */
	public void setLayout(LayoutType type, GraphView gView){
		switch (type) {
		case ORGANIC:
			OrganicLayouter organicLayouter = new OrganicLayouter();
//			organicLayouter.setPreferredEdgeLength(100);
			organicLayouter.setIterationFactor(.9);
			organicLayouter.doLayoutCore(gView.getGraph2D());
			
//			gView.fitContent();
			break;
		case RANDOM:
			RandomLayouter randomlayouter = new RandomLayouter();
			randomlayouter.doLayoutCore(gView.getGraph2D());
//			gView.fitContent();
			break;
			
		case CIRCULAR:
			CircularLayouter circularLayouter = new CircularLayouter();
			circularLayouter.doLayout(gView.getGraph2D());
//			gView.fitContent();
			break;
		default:
			
			break;
		}
	}

	/**
	 * Save a Graph to file
	 * 
	 * @param graph
	 * @param filename
	 * @return
	 */
	public boolean saveAsGML(Graph2D graph, String filename) {
		try {
			iohGML.write(graph, filename);
			return true;
		} catch (IOException ioEx) {
			return false;
		}
	}

	/**
	 * load a graph from file
	 * 
	 * @param graph
	 * @param filename
	 * @return
	 */
	public boolean loadGML(Graph2D graph, String filename) {
		try {
			iohGML.read(graph, filename);
		} catch (IOException ioEx) {
			return false;
		}
		for (EdgeCursor e = graph.edges(); e.ok(); e.next()) {
			graph.getRealizer(e.edge()).setArrow(Arrow.NONE);
		}
		return true;

	}

	/**
	 * Save a Graph
	 * 
	 * @param graph
	 * @param filename
	 * @return
	 */
	public boolean saveAsGraphML(Graph2D graph, String filename) {
		try {
			ioh.write(graph, filename);
			return true;
		} catch (IOException ioEx) {
			return false;
		}
	}

	/**
	 * Load a Graph
	 * @param graph
	 * @param filename
	 * @return
	 */
	public boolean loadGraphML(Graph2D graph, String filename) {
		try {
			ioh.read(graph, filename);
		} catch (IOException ioEx) {
			return false;
		}
		for (EdgeCursor e = graph.edges(); e.ok(); e.next()) {
			graph.getRealizer(e.edge()).setArrow(Arrow.NONE);
		}
		return true;

	}
}
