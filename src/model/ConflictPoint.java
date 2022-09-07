package model;

import y.base.Node;
import y.geom.YPoint;

import java.util.ArrayList;



/**
 * the class represents a conflict point 
 * @author heinsohn
 *
 */
public class ConflictPoint implements Comparable<ConflictPoint>{

	
	private ArrayList<Node> conflictingNodes;
	private int ply;
	private int sumOfDegree;
	private YPoint coordinate;
	private BasicGraph graph;
	
	public ConflictPoint(ArrayList<Node> nodes, BasicGraph g){
		ply = nodes.size();
		conflictingNodes = nodes;
		graph = g;
		calculateCoordinate();
		calculateSumOfEdges();
	}
	
	
	/**
	 * Calculate the median of all nodes
	 */
	private void calculateCoordinate(){
		//TODO: Inaccurate calculation maybe get an accurate one
		double x = 0; 
		double y = 0; 
		
		for (Node n : conflictingNodes){
			YPoint center = graph.getGraph2D().getCenter(n);
			x += center.x;
			y += center.y;
		}
		coordinate = new YPoint(x/ply, y/ply);
	}
	/**
	 * Calculate the sum of all edges
	 */
	private void calculateSumOfEdges(){
		sumOfDegree = 0; 
		for (Node n : conflictingNodes){
			sumOfDegree += n.degree();
		}
	}

	/**
	 * get a {@link YPoint} at the location of the conflict
	 * @return
	 */
	public YPoint getCenter() {
		return coordinate;
	}


	@Override
	public int compareTo(ConflictPoint arg0) {
		return new Integer(sumOfDegree).compareTo(arg0.sumOfDegree);
	}
	
	@Override
	public boolean equals(Object other){
		if (other instanceof ConflictPoint)
			return coordinate.equals(((ConflictPoint)other).coordinate);
		return false;
	}


	public ArrayList<Node> getNodes() {
		return conflictingNodes;
	}
	
	
}
