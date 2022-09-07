package model;



import java.util.ArrayList;

import sweep.YStructure;
import y.base.Node;
import y.geom.YCircle;
import y.geom.YPoint;
import y.geom.YVector;
import y.view.Graph2D;

/**
 * Represents a circle with CircleType top or bottom
 * 
 * @author Heinsohn
 * 
 */
public class HalfCircle {

	private Node node;
	private YCircle circle;
	private YStructure yStructure;

	/*
	 * One of TOP, BOTTOM
	 */
	public enum CircleType {
		TOP, BOTTOM
	};

	private CircleType type;
	
	/*
	 * Things to store during Sweepline
	 */
	private int lowerPly;
	private int upperPly;
	private int indexInYStructure;

	
	/**
	 * Store already checked HalfcircleIntersections
	 */
	private ArrayList<HalfCircle> checkedIntersections = new ArrayList<>();
	private ArrayList<HalfCircle> haveBeenSwaped = new ArrayList<>();

	/**
	 * Constructor for Half circles
	 * @param ty either TOP or BOTTOM
	 * @param n the Node
	 * @param circ the Circle
	 */
	public HalfCircle(CircleType ty, Node n, YCircle circ, YStructure ystruc) {
		node = n;
		circle = circ;
		type = ty;

		yStructure = ystruc;
		
		switch (type) {
		case TOP:
			lowerPly = 1;
			upperPly = 0;
			break;

		case BOTTOM:
			lowerPly = 0;
			upperPly = 1;
			break;
		default:
			break;
		}

	}

	/**
	 * Get the radius of the encapsulated circle
	 * 
	 * @return
	 */
	public double getRadius() {
		return circle.getRadius();
	}

	/**
	 * Get the y coordinate of the center
	 * 
	 * @return
	 */
	public double getStartYCoordiante() {
		return circle.getCenter().getY();
	}
	
	/**
	 * get the Node of this Half-circle
	 * @return Node n
	 */
	public Node getNode(){
		return node;
	}

	/**
	 * Get the actual Y value of the Halfcircle at xCoordinate
	 * 
	 * @param xCoordinate
	 * @return double the y-Value
	 */
	public double getActualY(double xCoordinate) {

		YPoint[] cut = circle.getYCut(xCoordinate);
		if (cut == null || cut.length <= 1) {
			return circle.getCenter().getY();
		}

		assert (cut.length == 2);
		switch (type) {
		case TOP:	
			return cut[1].getY();
		case BOTTOM:
			return cut[0].getY();

		default:
			break;
		}
		System.out.println("Check the getActualY() method in HalfCircle.java");
		return 0;
	}

	/**
	 * Set Index in YStructure to int index
	 * 
	 * @param int index
	 */
	public void setIndex(int index) {
		indexInYStructure = index;

	}

	/**
	 * Get the lower Ply of the Halfcircle
	 * 
	 * @return int lower Ply
	 */
	public int getLowerPly() {
		return lowerPly;
	}

	/**
	 * Set the lower Ply number to
	 * 
	 * @param int lowerPly
	 */
	public void setLowerPly(int lowerPly) {
		this.lowerPly = lowerPly;
	}

	/**
	 * Get the upper Ply number of this Halfcircle
	 * 
	 * @return int upper Ply number
	 */
	public int getUpperPly() {
		return upperPly;
	}

	/**
	 * Set the upper Ply to upperPly
	 * 
	 * @param int upperPly
	 */
	public void setUpperPly(int upperPly) {
		this.upperPly = upperPly;
	}

	/**
	 * Get Index in YStructure
	 * 
	 * @return int Index in YStructure
	 */
	public int getIndex() {
		return indexInYStructure;
	}

	/**
	 * Returns the leftmost coordinate
	 * 
	 * @return
	 */
	public double getStartXCoordiante() {
		return circle.getCenter().getX() - circle.getRadius();
	}

	/**
	 * returns the rightmost coordinate
	 * 
	 * @return
	 */
	public double getENDXCoordiante() {
		return circle.getCenter().getX() + circle.getRadius();
	}

	/**
	 * Is this {@link HalfCircle} TOP or BOTTOM
	 * 
	 * @return
	 */
	public CircleType getType() {
		return type;
	}

	/**
	 * Intersect this {@link HalfCircle} with {@link HalfCircle}other, there are either 1, 2 or no intersection (null)
	 * 
	 * @param HalfCircle
	 *            other
	 * @param double leftOf
	 * @return double.MAX_VALUE if non intersection exists
	 */
	public double[] intersect(HalfCircle other, double leftOf) {

		// whenever the Halfcircles are checked in forehand ignore this time
		if (other.checkedIntersections.contains(this) || checkedIntersections.contains(other))
			return null;
		
		
		if (other.circle.getCenter().getX() < circle.getCenter().getX()){
			// switch roles of this and other
			return other.intersect(this, leftOf);
		}
		
		// now they will be checked: 
		this.checkedIntersections.add(other);
		other.checkedIntersections.add(this);
		
		boolean haveSwaped = (other.haveBeenSwaped.contains(this) || haveBeenSwaped.contains(other));
		
		// if bot and top are next to each other again
		if (circle == other.circle)
			return null;

		// no intersection
		double dist = YPoint.distance(circle.getCenter(),
				other.circle.getCenter());
		double r = circle.getRadius();
		double rOther = other.circle.getRadius();

		// check for empty ply
		if (yStructure.emptyPly()){
			Graph2D gr = (Graph2D) node.getGraph();
			YPoint c1 = gr.getCenter(node);
			YPoint c2 = gr.getCenter(other.node);
			
			double distOfCenters = c1.distanceTo(c2);
			if (distOfCenters < r || distOfCenters < rOther)
				yStructure.setEmptyPlyFalse();
		}
		if (yStructure.getmaxPly() == 0){
			if (r + rOther > 0) {
				yStructure.setMaxPly(1);
			}
		}
		
		if (r + rOther <= dist)
			return null;

		if (dist + r <= rOther || dist + rOther <= r) // internally
			return null;

		//  caculate intersection coordinate
		double a = (r * r - rOther * rOther + dist * dist) / (2 * dist);
		YVector vec = new YVector(other.circle.getCenter(), circle.getCenter());

		if (a!= 0)
			vec.scale(a / dist);
		YPoint center;
		if (a == 0){
			
			center = circle.getCenter();
		} else {
			center = YVector.add(circle.getCenter(), vec);
		}
		double h = Math.sqrt(Math.abs(r * r - a * a));

		YVector orth = YVector.orthoNormal(vec);
		
		orth.scale(h);
		YPoint intersect1 = YVector.add(center	, orth);
		orth.norm();
		orth.scale(-h);
		YPoint intersect2 = YVector.add(center	, orth);



		
		double[] result = new double[2];
		result[0] = Double.MAX_VALUE;
		result[1] = Double.MAX_VALUE;
		if (checkValidIntersection(other, leftOf, intersect1.getY(), intersect1.getX(),haveSwaped)){
				result[0] = intersect1.getX();
			}
		if (checkValidIntersection(other, leftOf, intersect2.getY(), intersect2.getX(),haveSwaped)){
				result[1] =  intersect2.getX();
			}
				
		if (result[0] < Double.MAX_VALUE || result[1] < Double.MAX_VALUE)
			return result;
		// default if no intersection exists
		return null;
	}

	/**
	 * Check, if a possible Crossing of circles match the relative Positions
	 * @param other
	 * @param leftOf
	 * @param firstyCoordinate
	 * @param toCheck
	 * @param haveSwaped 
	 * @param haveBeenSwapedbefore 
	 * @return
	 */
	private boolean checkValidIntersection(HalfCircle other, double leftOf,
			double firstyCoordinate, double toCheck, boolean haveSwaped) {
		if (haveSwaped) {
			if (toCheck > leftOf) { // left of sweepline
				if (type == CircleType.TOP) { // this circle is TOP circle
					if (other.type == CircleType.TOP) { // other circle is TOP
						return (firstyCoordinate >= circle.getCenter().getY() && firstyCoordinate >= other.circle
								.getCenter().getY());
					} else { // other circle is botttom
						return (firstyCoordinate >= circle.getCenter().getY() && firstyCoordinate < other.circle
								.getCenter().getY());
					}
				} else { // this circle is bot circle
					if (other.type == CircleType.TOP) { // other circle is TOP
						return (firstyCoordinate < circle.getCenter().getY() && firstyCoordinate >= other.circle
								.getCenter().getY());
					} else { // other circle is botttom
						return (firstyCoordinate < circle.getCenter().getY() && firstyCoordinate < other.circle
								.getCenter().getY());
					}
				}

			}
		} else {
			if (toCheck >= leftOf-0.001) { // left of sweepline
				if (type == CircleType.TOP) { // this circle is TOP circle
					if (other.type == CircleType.TOP) { // other circle is TOP
						return (firstyCoordinate >= circle.getCenter().getY() && firstyCoordinate >= other.circle
								.getCenter().getY());
					} else { // other circle is botttom
						return (firstyCoordinate >= circle.getCenter().getY() && firstyCoordinate < other.circle
								.getCenter().getY());
					}
				} else { // this circle is bot circle
					if (other.type == CircleType.TOP) { // other circle is TOP
						return (firstyCoordinate < circle.getCenter().getY() && firstyCoordinate >= other.circle
								.getCenter().getY());
					} else { // other circle is botttom
						return (firstyCoordinate < circle.getCenter().getY() && firstyCoordinate < other.circle
								.getCenter().getY());
					}
				}
			}
		}
		return false;
	}

	/**
	 * A String representation for the HalfCircle
	 */
	public String toString() {
		if (type == CircleType.TOP)
			return "TOP " + node ;//+ " L = " + lowerPly + " U = " +upperPly;
		return "BOT " + node ;//+ " L = " + lowerPly + " U = " +upperPly;
	}
	
	/**
	 * Indicates that two {@link HalfCircle} are swapped 
	 * This Method should be called to ensure that they can be checked for further intersections again
	 * @param other
	 */
	public void swap(HalfCircle other){
		this.haveBeenSwaped.add(other);
		other.haveBeenSwaped.add(this);
	}

	/**
	 * update & return ply
	 * @return
	 */
	public void increasePly() {
		lowerPly++;
		upperPly++;
	}
	/**
	 * update & return ply
	 * @return
	 */
	public void decreasePly() {
		lowerPly--;
		upperPly--;
	}


	
	
	

}
