package sweep;


import java.util.ArrayList;
import java.util.Arrays;

import y.base.Node;
import model.HalfCircle;
import model.HalfCircle.CircleType;

public class YStructure {

	
	// based on Tree or linked list 
	// swap guys in a Tree?
	private ArrayList <HalfCircle> list;
	
	private int maxPly = 0;
	private boolean emptyPly = true;
	private int priotity = 1;
	private InterfaceXEvent currentEvent;

	private SweepLine sweepLine;
	private double xCoordinate;
	
	public YStructure(SweepLine s) {
		maxPly = 0;
		list = new ArrayList<>(); 
		xCoordinate = Integer.MIN_VALUE; 
		sweepLine = s;
	}
	
	
	/**
	 * insert a new Halfcircle
	 * @param top
	 * @param bot
	 * @return
	 */
	private InterfaceXEvent[] insert(HalfCircle top, HalfCircle bot){
		
		int index = getIndex(top.getStartYCoordiante());
		InterfaceXEvent[] events = new InterfaceXEvent[8];
		
		list.add(index, top);
		top.setIndex(index);
		
		int j = 1;
		// set the bottom around equal coordinates + increase their ply
		while (index+j < list.size() && bot.getActualY(top.getStartXCoordiante()) == list.get(index+j).getActualY(top.getStartXCoordiante())){
			j++;
		}
		
		// update indices
		list.add(index +j , bot);
		bot.setIndex(index +j);
		
		for (int i = index ; i < list.size(); i++){
			list.get(i).setIndex(i);
		}
		
		// update Ply
		for (int i = index +1; i < index+j ; i++){
			list.get(i).increasePly();
		}
		// update initial Ply
		if (index > 0){
			int ply = list.get(index-1).getLowerPly();
			top.setUpperPly(ply);
			top.setLowerPly(ply +1);
			
		} 
		
		if (index+j < list.size()-1){
			bot.setLowerPly(list.get(index+j+1).getUpperPly());
			bot.setUpperPly(bot.getLowerPly() +1);
			
		}
		
		top.swap(list.get(index+1));
		bot.swap(list.get(index+j-1));
		//check intersections with left and right and report them
		checkNeighbours(top, events, xCoordinate);
		checkNeighbours(bot, events, xCoordinate);
		if (top.getLowerPly() > bot.getUpperPly()){
			countIntersectionsDown(top.getIndex());
		} else {
			countIntersectionsUp(bot.getIndex());
		}
		
		if (top.getLowerPly() == maxPly || bot.getUpperPly() == maxPly){
			reportMaxPly();
		}
		
		return events;
	}
	
	/**
	 * Check for intersections
	 * @param top
	 * @param events
	 * @param xCoordinate
	 */
	private void checkNeighbours(HalfCircle top, InterfaceXEvent[] events,
			double xCoordinate2) {
		

		int index = top.getIndex();
		int indexInEvents = 0; 
		while (events[indexInEvents] != null)
			indexInEvents++;
		
		if (index > 0){
			double[] coord = top.intersect(list.get(index-1), xCoordinate2);
			if (coord != null){
				for (int i = 0; i < coord.length; i++){
					if (coord[i] < Double.MAX_VALUE){
						events[indexInEvents++] = new CircleIntersectionEvent(list.get(index-1), top, coord[i]);
					}
				}
			}
		} if (index < list.size()-1){
			double[] coord = top.intersect(list.get(index+1), xCoordinate2);
			if (coord != null){
				for (int i = 0; i < coord.length; i++){
					if (coord[i] < Double.MAX_VALUE){
						events[indexInEvents++] = new CircleIntersectionEvent(list.get(index+1), top, coord[i]);
					}
				}
			}
		}
		
	}

	/**
	 * Remove a pair of Half-circles
	 * @param top
	 * @param bot
	 * @return
	 */
	private InterfaceXEvent[] remove(HalfCircle top, HalfCircle bot){
		
		
		if (top.getLowerPly() == maxPly || bot.getUpperPly() == maxPly){
			reportMaxPly();
		}
		
		int topindex = top.getIndex();
		int botindex = bot.getIndex();
		InterfaceXEvent[] events = new InterfaceXEvent[3];
		
		assert(list.indexOf(top) == topindex);
		assert(list.indexOf(bot) == botindex);
		
		if (! (list.indexOf(top) == topindex && list.indexOf(bot) == botindex) ){
			System.out.println("Totally problematic!! ");
		}
		for (int i = topindex +1; i < botindex ; i++){
			// update lower ply
			list.get(i).decreasePly();
			// update index
			list.get(i).setIndex(list.get(i).getIndex() -1);
		}
		// update index
		for (int i = botindex +1; i < list.size(); i++){
			list.get(i).setIndex(list.get(i).getIndex()-2);
		}
		
		//TODO: here the wrong guys are removed?
		if (botindex == 0){
			System.out.println("Botindex is 0 ??");
		}
		
		// check. if we remove the correct one: 
		
		// remove halfcircles
		list.get(botindex).setIndex(-1);
		list.get(topindex).setIndex(-1);
		list.remove(botindex);
		list.remove(topindex);
		// check intersections
		if (topindex < list.size())
			checkNeighbours(list.get(topindex), events, xCoordinate);
		if (botindex -1 < 0)
			checkNeighbours(list.get(botindex-1), events, xCoordinate);
		
		
		return events;
		
	}


	/**
	 * Method for debugging pourposes
	 */
	@SuppressWarnings("unused")
	private void printStructure(){
		for (int i = 0; i < list.size(); i++){
			System.out.println("Element " + list.get(i).getIndex() + " : " + list.get(i)+ ", actual Y position: " + list.get(i).getActualY(xCoordinate));
		}
	}
	
	
	// Binary search to insert
	private int getIndex(double startYCoordiante) {
		if (list.size() == 0)
			return 0;
		
		int left = 0;
		int right = list.size()-1;
		
		
		// reverse ordering!!
		while(left < right){
			double rightvalue = list.get(right).getActualY(xCoordinate);
			if (startYCoordiante < rightvalue)
				return right +1;
			
			double leftvalue = list.get(left).getActualY(xCoordinate);
			if (startYCoordiante > leftvalue)
				return left;
			
			int mid = (left +right) /2;
			
			double midValue = list.get(mid).getActualY(xCoordinate);
			
			if(midValue <= startYCoordiante){
				right = mid -1;
				
			} else if (midValue > startYCoordiante){
				left = mid +1;
			} 
		}
		double act = list.get(left).getActualY(xCoordinate);
		if (act > startYCoordiante)
			return left+1;
		return left;
	}


	protected InterfaceXEvent[] handleXEvent(InterfaceXEvent ev) {
		currentEvent = ev;
		xCoordinate = ev.getXCoordinate();
//		String s = "Event: " + ev.print() + " at x= "+ xCoordinate;
//		System.out.println(s);
//		if (s.equals("Event: Startevent of circle: B4 L = 0 U = 1 at x= 56.323820740797565")){
//			System.out.println("look here!");
//		}
//		
//		printStructure();
//		System.out.println(" ");
		// ignore empty circles
		if (ev.getBottomHalfCircle().getRadius() == 0){
			return new InterfaceXEvent[1];
		}
		
		switch (ev.getType()) {
		case START:
			if (ev.getTopHalfCircle().toString().equals("B17 L = 0 U = 1)"))
					System.out.println("Look at B17");
			return insert(ev.getTopHalfCircle(), ev.getBottomHalfCircle());
			
		case END:
			return remove(ev.getTopHalfCircle()	, ev.getBottomHalfCircle());
			
		case INTERSECTION:
			InterfaceXEvent[] result =intersect(ev.getTopHalfCircle(), ev.getBottomHalfCircle());
			if (result == null){ 
				// could not handle event 
				
				InterfaceXEvent[] events = new InterfaceXEvent[1];
				CircleIntersectionEvent intersectEvent = (CircleIntersectionEvent) ev;
				events[0] = intersectEvent;
				return events;
			}
			return  result;
			
			
		default:
			break;
		}
		System.out.println("Check handleXEvent() method in YStructure");
		return null;
	}


	/**
	 * Insert the Top and Bot {@link HalfCircle} into the YStructure
	 * @param top
	 * @param bot
	 * @return Array of Intersectionevents
	 */
	private InterfaceXEvent[] intersect(HalfCircle top,
			HalfCircle bot) {
		
		int topindex = top.getIndex();
		int botindex = bot.getIndex();
		InterfaceXEvent[] events = new InterfaceXEvent[5];
		
		if (top.getUpperPly() == maxPly || bot.getLowerPly() == maxPly
				|| top.getLowerPly() == maxPly || bot.getUpperPly() == maxPly){
			reportMaxPly();
		}
		
		assert(list.indexOf(top) == topindex);
		assert(list.indexOf(bot) == botindex);
		
		if (topindex == -1) { // one of the guys has been removed!
			if (botindex == -1) {
				return new InterfaceXEvent[1];
			}
			if (botindex == -1) {
				// the guy has been removed (check neighbours again)
				return new InterfaceXEvent[1];
			}
			// check neighbours of bot
			events = new InterfaceXEvent[2];
			checkNeighbours(bot, events, xCoordinate);
			return events;
		} else {
			if (botindex == -1){
				// check neighbours of top
				events = new InterfaceXEvent[2];
				checkNeighbours(top, events, xCoordinate);
				return events;
			 } 
		}
			
			
			
		
		if (Math.abs(top.getIndex() - bot.getIndex()) >= 2){
			//System.out.println("Check top and bot index in intersect() in YStructure");
			
			// postpone this event
			return null;
		}
		
		// swap the guys
		HalfCircle tmp = list.get(topindex);
		list.set(topindex, list.get(botindex));
		list.set(botindex, tmp);
		
		list.get(topindex).setIndex(topindex);
		list.get(botindex).setIndex(botindex);
		
		// mark them as swaped
		top.swap(bot);
		
		checkNeighbours(top, events, xCoordinate);
		checkNeighbours(bot, events, xCoordinate);
		
		
		// update the Ply guys
		
		if (top.getType() == CircleType.TOP && bot.getType() == CircleType.TOP){
			// worked don't change
			if (top.getIndex() < bot.getIndex()){
				top.decreasePly();
				bot.increasePly();
			} else {
				top.increasePly();
				bot.decreasePly();
			}
		} else if (top.getType() == CircleType.BOTTOM && bot.getType() == CircleType.BOTTOM) {
			if (top.getIndex() < bot.getIndex()){
				bot.decreasePly();
				top.increasePly();
			} else {
				top.decreasePly();
				bot.increasePly();
			}
		} else if (top.getType() == CircleType.TOP && bot.getType() == CircleType.BOTTOM) {
			if (top.getIndex() > bot.getIndex()){
				top.decreasePly();
				bot.decreasePly();
			} else {
				top.increasePly();
				bot.increasePly();
			}
			
		} else if (top.getType() == CircleType.BOTTOM && bot.getType() == CircleType.TOP) {
			// worked, don't change!
			if (top.getIndex() < bot.getIndex()){
				bot.decreasePly();
				top.decreasePly();
			} else {
				bot.increasePly();
				top.increasePly();
			}
		}
		
		
		
		switch (top.getType()) {
		case BOTTOM:
			countIntersectionsUp(top.getIndex());
			break;
		default:
			countIntersectionsDown(top.getIndex());
			break;
		}
		
		switch (bot.getType()) {
		case BOTTOM:
			countIntersectionsUp(bot.getIndex());
			break;

		default:
			countIntersectionsDown(bot.getIndex());
			break;
		}
		
		if (top.getUpperPly() == maxPly || bot.getLowerPly() == maxPly
				|| top.getLowerPly() == maxPly || bot.getUpperPly() == maxPly){
			reportMaxPly();
		}
		
		return events;
	}

	/**
	 * reset values of maxPly, emptyPly and priority
	 */
	public void initialize() {
		maxPly = 0;
		emptyPly = true;
		priotity = 1;
		
	}

	/**
	 * return the ply value
	 * @return
	 */
	public int getmaxPly() {
		return maxPly;
	}
	
	/**
	 * return the maximal priority
	 * @return
	 */
	public int getPriority(){
		return priotity;
	}


	/**
	 * returns if the graph is empty ply
	 * @return
	 */
	public boolean emptyPly() {
		return emptyPly;
	}
	/**
	 * sets the empty Ply to false
	 */
	public void setEmptyPlyFalse(){
		emptyPly = false;
	}


	public void setMaxPly(int max) {
		maxPly = max;
		
		sweepLine.clearMaxPlyRegion();
		sweepLine.reportMaxPlyRegion(currentEvent);
		
		sweepLine.reportMaxPly(max);
	}


	/**
	 * Count the intersections in a max Ply region
	 * @param index
	 */
	private void countIntersectionsDown(int index) {
		// at index is a top half circle
		HalfCircle top = list.get(index);
		
		if (top.getLowerPly() > maxPly){
			setMaxPly(top.getLowerPly());
		}
		
		ArrayList<Node> nodeListInclude = new ArrayList<>();
		ArrayList<Node> nodeListExclude = new ArrayList<>();
		
		nodeListInclude.add(list.get(index).getNode());
		nodeListExclude.add(list.get(index).getNode());
		
		if (top.getLowerPly() == maxPly){
			int j = index;
			
			while (nodeListInclude.size() < maxPly) {
				switch (list.get(j).getType()) {
				case BOTTOM:
					if (!nodeListExclude.contains(list.get(j).getNode())){
						nodeListInclude.add(list.get(j).getNode());
						nodeListExclude.add(list.get(j).getNode());
					}
					break;

				case TOP:
					nodeListExclude.add(list.get(j).getNode());
					break;
				default:
					break;
				}
				j++;
			}
//			System.out.println("Ply-region with nodes: " + Arrays.deepToString(nodeListInclude.toArray()));
//			int i = 1;
//			while(index + i < list.size() && list.get(index+i).getLowerPly() < list.get(index+i-1).getLowerPly() && !(list.get(index + i).getNode() == top.getNode())){
//				nodeListInclude.add(list.get(index +i).getNode());
//				i++;
//			
//			}
		//TODO: slow here
//			if (nodeListInclude.size() == maxPly)
//				sweepLine.reportMaxPlyRegion(nodeListInclude);
//		
//			System.out.println("Found: ");
//			String s = "[ ";
//			for (Node n : nodeListInclude){
//				s += n + ", ";
//			}
//			s += "] , reported : " + (nodeListInclude.size() == maxPly);
//			System.out.println(s);
		}
		//return (nodeList.size() == maxPly);
	}


	private void reportMaxPly() {
		sweepLine.reportMaxPlyRegion(currentEvent);
	}


	private void countIntersectionsUp(int index) {
		// the index is a bottom half-circle
		
		HalfCircle bot = list.get(index);
		
		if (bot.getUpperPly() > maxPly){
			setMaxPly(bot.getUpperPly());
		}
		ArrayList<Node> nodeListInclude = new ArrayList<>();
		ArrayList<Node> nodeListExclude = new ArrayList<>();
		nodeListInclude.add(list.get(index).getNode());
		
		if (bot.getUpperPly() == maxPly) {
			int j = index;
			while (nodeListInclude.size() < maxPly && j >= 0) {
				switch (list.get(j).getType()) {
				case TOP:
					if (!nodeListExclude.contains(list.get(j).getNode())){
						nodeListInclude.add(list.get(j).getNode());
						nodeListExclude.add(list.get(j).getNode());
					}
					break;

				case BOTTOM:
					nodeListExclude.add(list.get(j).getNode());
					break;
				default:
					break;
				}
				j--;
				
			}
//			System.out.println("Ply-region with nodes: " + Arrays.deepToString(nodeListInclude.toArray()));
			//TODO: slow here
//			if (nodeListInclude.size() == maxPly)
//				sweepLine.reportMaxPlyRegion(nodeListInclude);

			
			//sysout
//			System.out.println("Searched UP at " + index);
//			printStructure();
//			System.out.println("Found: ");
//			String s = "[ ";
//			for (Node n : nodeListInclude) {
//				s += n + ", ";
//			}
//			s += "] , reported : " + (nodeListInclude.size() == maxPly);
//			System.out.println(s);
		}
		
	}
	
	
}
