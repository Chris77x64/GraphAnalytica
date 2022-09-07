package model;

import y.base.EdgeMap;
import y.base.NodeMap;
import y.view.Graph2D;

public class EdgeInfoGraph extends Graph2D{

	/**
	 * Store the edge infos
	 */
	EdgeMap edgeInformation;
	/**
	 * store all start nodes
	 */
	NodeMap startNodes; 
	/**
	 * store all end nodes
	 */
	NodeMap endNdoes;
	
	public EdgeInfoGraph() {
		super();
		this.edgeInformation = this.createEdgeMap();
		this.startNodes = this.createNodeMap();
		this.endNdoes = this.createNodeMap();
	}
	
	
}
