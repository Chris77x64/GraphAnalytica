package sweep;

import model.HalfCircle;

public interface InterfaceXEvent extends Comparable<InterfaceXEvent> {

	
	public int compareTo(InterfaceXEvent ev);
	enum Type {START, END, INTERSECTION};
	
	public Type getType();
	
	double getXCoordinate();
	
	public HalfCircle getTopHalfCircle();
	
	public HalfCircle getBottomHalfCircle();
	
	public String print();
	
}
