package sweep;

import model.HalfCircle;

public class CircleStartEvent implements InterfaceXEvent {

	private double xCoordinate;

	private HalfCircle circleTop;
	private HalfCircle circleBottom;

	private Type type = Type.START;
	
	public CircleStartEvent(HalfCircle top, HalfCircle bot){
		circleBottom = bot;
		circleTop = top;
		xCoordinate = bot.getStartXCoordiante();
	}

	@Override
	public int compareTo(InterfaceXEvent ev) {
		int res = ((Double) xCoordinate).compareTo(ev.getXCoordinate());

		if (res == 0) {
			// same coordinate -> end < intersection < start 
			switch (ev.getType()) {
			case START:
				CircleStartEvent startEV = (CircleStartEvent) ev;
				return ((Double) circleTop.getRadius()).compareTo(startEV.circleTop.getRadius());
		
			case END:
				return 1;

			case INTERSECTION:
				return 1;

			default:
				break;
			}
		}
		
		return res;
	}

	public Type getType() {
		return type;
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
		
		return "Startevent of circle: " + circleBottom.toString();
	}
}
