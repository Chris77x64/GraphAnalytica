package sweep;

import model.HalfCircle;

public class CircleEndEvent implements InterfaceXEvent {

	private HalfCircle circleTop;
	private HalfCircle circleBottom;
	private double xCoordinate; 
	
	public CircleEndEvent(HalfCircle top, HalfCircle bot) {
		circleBottom = bot;
		circleTop = top; 
		xCoordinate = top.getENDXCoordiante();
	}
	@Override
	public int compareTo(InterfaceXEvent ev) {
		int res = ((Double) xCoordinate).compareTo(ev.getXCoordinate());

		if (res == 0) {
			// same coordinate -> end < intersection < start
			switch (ev.getType()) {
			case START:
				if (circleTop == ev.getTopHalfCircle()) // just for 0 ply
					return 1;
				return -1;
		
			case END:
				CircleEndEvent endEV = (CircleEndEvent) ev;
				return ((Double) circleTop.getRadius()).compareTo(endEV.circleTop.getRadius());

			case INTERSECTION:
				return -1;

			default:
				break;
			}
		}
		
		return res;
	}

	@Override
	public Type getType() {
		return Type.END;
	}

	@Override
	public double getXCoordinate() {
		return xCoordinate;
	}
	
	/**
	 * Get the top halfcircle of the Startevent
	 * @return
	 */
	public HalfCircle getTopHalfCircle(){
		return circleTop;
	}
	/**
	 * get the bottom Halfcircle of the Startevent
	 * @return
	 */
	public HalfCircle getBottomHalfCircle(){
		return circleBottom;
	}
	
	@Override
	public String print() {
		
		return "Endevent of circle: " + circleBottom.toString();
	}

}
