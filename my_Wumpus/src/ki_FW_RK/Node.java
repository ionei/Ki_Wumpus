package ki_FW_RK;

import de.northernstars.jwumpus.core.WumpusMapObject;

public class Node {
	private int c;
	private int r;
	private int heuristic;
	private int range;
	private int comingFrom;
	private boolean visited;
	private WumpusMapObject object;
	
	public Node(WumpusMapObject mapObject){
		c = mapObject.getColumn();		
		r = mapObject.getRow();
		calcHeuristic();
	}
	public Node(int newColumn,int newRow){
		c = newColumn;		
		r = newRow;
		calcHeuristic();
	}
	
	public int getHeuristic() {
		return heuristic;
	}
	public void setHeuristic(int heuristic) {
		this.heuristic = heuristic;
	}
	public void calcHeuristic() {
		this.heuristic = (int) Math.sqrt(c * c + r * r);
	}
	public int getRange() {
		return range;
	}
	public void setRange(int range) {
		this.range = range;
	}
	public int getComingFrom() {
		return comingFrom;
	}
	public void setComingFrom(int newComingfrom) {
		this.comingFrom = newComingfrom;
	}
	public boolean isVisited() {
		return visited;
	}
	public void setVisited(boolean visited) {
		this.visited = visited;
	}
	public WumpusMapObject getObject() {
		return object;
	}
	public void setObject(WumpusMapObject object) {
		this.object = object;
	}
	public int getRangeAddHeuristic(){
		return range + heuristic;
	}
	public int getR() {
		return r;
	}
	public void setR(int r) {
		this.r = r;
	}
	public int getC() {
		return c;
	}
	public void setC(int c) {
		this.c = c;
	}

}
