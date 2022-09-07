package sweep;

import java.util.ArrayList;
import java.util.PriorityQueue;

import model.HalfCircle;
import model.HalfCircle.CircleType;
import sweep.InterfaceXEvent.Type;
import y.base.Node;
import y.base.NodeCursor;
import y.base.NodeMap;
import y.geom.YCircle;
import y.geom.YPoint;


/**
 * Executes a Sweepline algorithm on the Graph
 * @author heinsohn
 *
 */
public class SweepLine {
	
	// need a Priority queue as x-structure
	private PriorityQueue<InterfaceXEvent> xEvents;
	
	// need updatable yStructure
	private YStructure yStructure;
	
	private ArrayList<YPoint> maxPlyRegions = new ArrayList<>();
	
	private int totalPostponed = 0; 
	private int postponedMax = 0;
	private int solvedIntersect = 0;
	private int solvedStart = 0;
	private int solvedEnd = 0;
	private int eventsTotal = 0;

	

	public SweepLine() {
		xEvents = new PriorityQueue<>(100);
		yStructure = new YStructure(this);
	}
	
	/**
	 * Execute Sweepline
	 */
	public void executeSweepLine(){
		totalPostponed = 0; 
		postponedMax = 0;
		solvedIntersect = 0;
		solvedStart = 0;
		solvedEnd = 0;
		eventsTotal = 0;
		boolean allowEndEvents = true;
		
		ArrayList<InterfaceXEvent> remember = new ArrayList<>();
		while(! xEvents.isEmpty()){
			InterfaceXEvent ev = xEvents.poll();
			
			if (ev == null)
				System.out.println("no event left? ");
			
			while (! executeXEvent(ev, ((remember.size()== 0) || allowEndEvents))){
				
				remember.add(ev);
				ev = xEvents.poll();
				if (ev == null){
					System.out.println("no event left? ");
					break;
				}
			}
			if (ev== null)
				allowEndEvents = true;
			
			if (remember.size() > 0 && ev != null){
				postponedMax = Math.max(postponedMax, remember.size());
				totalPostponed += remember.size();
				switch (ev.getType()) {
				case START:
					solvedStart++;
					break;
				case END:
					solvedEnd++;
					break;
				case INTERSECTION:
					solvedIntersect++;
				default:
					break;
				}
				
			}
			for (InterfaceXEvent e : remember){
				xEvents.add(e);
			}
//			System.out.println(remember.size() + " Events postponed");
			remember.clear();
			
			eventsTotal++;
			
		}
		
	}

	private boolean executeXEvent(InterfaceXEvent ev, boolean endeventallowed) {
		
		// update YStructure
		if (ev.getType() == Type.END && !endeventallowed){
			
			return false;
		}
		
		InterfaceXEvent[] newEvents = yStructure.handleXEvent(ev);
		// add newly found xEvents
		for (int i = 0; i < newEvents.length; i++){
			InterfaceXEvent event = newEvents[i];
			if (newEvents.length == 1 && event != null){
				//System.out.println("One event returning");
				return false;
			}
			if (event != null){
				xEvents.add(event);
				
			}
		}
		return true;
		
	}
	/**
	 * initialize the Sweepline with the necessary data
	 * @param circleMap
	 * @param cur
	 */
	public void initialize(NodeMap circleMap, NodeCursor cur) {
		
		for (NodeCursor v = cur; v.ok(); v.next()) {
			ArrayList<Node> list = new ArrayList<>();
			list.add(v.node());
			HalfCircle top = new HalfCircle(CircleType.TOP, v.node(), (YCircle) circleMap.get(v.node()),  yStructure);
			HalfCircle bot = new HalfCircle(CircleType.BOTTOM, v.node(), (YCircle) circleMap.get(v.node()), yStructure);
			
			xEvents.add(new CircleStartEvent(top, bot));
			xEvents.add(new CircleEndEvent(top, bot));
		}
		yStructure.initialize();
		
	}
	
	public void reportMaxPlyRegion(InterfaceXEvent e){
		
		double x = e.getXCoordinate();
		double y = e.getBottomHalfCircle().getActualY(x);
		
		YPoint maxRegion = new YPoint(x, y);
		maxPlyRegions.add(maxRegion);
		
	}
	
	public void clearMaxPlyRegion(){
		maxPlyRegions = new ArrayList<>();
	}

	public int getMaxPly() {
		return yStructure.getmaxPly();
	}

	public boolean getEmptyPly() {
		return yStructure.emptyPly();
	}

	/**
	 * new Maximal value for ply -> new list of max ply regions 
	 * @param max
	 */
	public void reportMaxPly(int max) {
		maxPlyRegions = new ArrayList<>();
	}
	
	/**
	 * Print the max Ply regions by node indices
	 
	public void printPlyRegions(){
		
		
		for (ArrayList<Node> l : maxPlyRegions){
			String s = "[ ";
			for (Node n : l){
				s += n + ", ";
			}
			s += "]";
			System.out.println(s);
		}
	}
	
	
	private void cleanUp(){
		ArrayList<ArrayList<Node>> cleanlist = new ArrayList<>();
		for (int i = 0; i < maxPlyRegions.size(); i++){
			boolean unique = true;
			ArrayList<Node> tocheck = maxPlyRegions.get(i);
			for (int j = i+1; j < maxPlyRegions.size(); j++){
				unique = true;
				ArrayList<Node> l = maxPlyRegions.get(j);
				for (Node n : tocheck){
					unique &= l.contains(n);
					if(!unique) break;
				}
				if (unique)
					break;
					
			}
			if (!unique || i == maxPlyRegions.size()-1)
				cleanlist.add(tocheck);
			
		}
		maxPlyRegions = cleanlist;
		
	}

	*/
	/**
	 * Get a List of List of nodes (which are involved in a Conflict
	 * @return
	 */
	public ArrayList<YPoint> getConflictNodes() {
		return maxPlyRegions;
	}
	
	/**
	 * Get the calculation properties for CSV file: 
	 * totalPostponed = 0; 
		postponedMax = 0;
		solvedIntersect = 0;
		solvedStart = 0;
		solvedEnd = 0;
		eventsTotal = 0;
	 * @return
	 */
	public String getCalculationProperties(){
		return totalPostponed + ", " + postponedMax + ", "+ solvedIntersect+ ", " + solvedStart + ", "+ solvedEnd+ ", " +eventsTotal;
	}
}
