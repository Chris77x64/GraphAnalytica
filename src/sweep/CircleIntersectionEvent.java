package sweep;

import model.HalfCircle;

public class CircleIntersectionEvent implements InterfaceXEvent{

	
	private HalfCircle circleTop;
	private HalfCircle circleBottom;
	private double xCoordinate; 
	int priority = 1;
	
	CircleIntersectionEvent(HalfCircle top, HalfCircle bot, double xCoordi){
		circleTop = top; 
		circleBottom = bot; 
		xCoordinate = xCoordi;
	}
	
	@Override
	public int compareTo(InterfaceXEvent ev) {
		int res = ((Double) xCoordinate).compareTo(ev.getXCoordinate());

		if (res == 0) {
			// same coordinate -> end < intersection < start
			switch (ev.getType()) {
			case START:
				if (priority > 1)
					return 1;
				return -1;
		
			case END:
				return 1;

			case INTERSECTION:
				//TODO: compare intersectionevents?
				CircleIntersectionEvent intEV = (CircleIntersectionEvent) ev;
				int comp = ((Integer) priority).compareTo(intEV.priority);
				if (comp == 0) 
					return ((Double) circleTop.getRadius()).compareTo(intEV.circleTop.getRadius());
				
				return comp;
				

			default:
				break;
			}
		}
		
		return res;
	}

	@Override
	public Type getType() {
		return Type.INTERSECTION;
	}

	@Override
	public double getXCoordinate() {
		return xCoordinate;
	}

	@Override
	public HalfCircle getTopHalfCircle() {
		return circleTop;
	}

	@Override
	public HalfCircle getBottomHalfCircle() {
		return circleBottom;
	}

	@Override
	public String print() {
		
		return "Intersection event of circle: " + circleBottom.toString() +" and " + circleTop.toString();
	}

	public int decreasePriority() {
		priority++;
		xCoordinate += 0.000001;
		return priority;
	}
}
