package ki_FW_RK;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.sun.org.apache.xpath.internal.FoundIndex;

import de.northernstars.jwumpus.core.*;
import de.northernstars.jwumpus.examples.*;


public class MyAi implements WumpusAI{
	private WumpusMap map;
	private long remainingTime = 0;
	private int remainingArrows = 0;
	private ActionSuccess lastActionSuccess = ActionSuccess.SUCCESSFULL;
	private PlayerState playerState = PlayerState.UNKNOWN;
	private int a = 0;
	private int x,y;
	private WumpusMapObject obj;
    private Ki_Thread b = new Ki_Thread();
    private Action tue = Action.NO_ACTION, tue2= Action.NO_ACTION;
    private int i = 0;
	private int xvor,xre,yvor,yre;
	private AiWumpusMap zielmap = new AiWumpusMap();
	private Graph graph = new Graph();
	private LinkedList<Node> path = null;
	long before;
	long after;
	long runningTimeNs;
	
	
	MyAi(){
		zielmap.setCheckDimension(false);
		graph.addNode(new Node(0,0));
		graph.createMatrix();
		
	}

	public void putWumpusWorldMap(WumpusMap map) {
		this.map = new WumpusMap(map);
	}


	public void putLastActionSuccess(ActionSuccess actionSuccess) {
		lastActionSuccess = actionSuccess;
	}


	public void putRemainingTime(long time) {
		remainingTime = time;
	}


	public void putPlayerArrows(int arrows) {
		remainingArrows = arrows;
	}


	public Action getAction() {
		// Returns no action to do nothing
		//tue = null;
		long newbefore = System.nanoTime();
		b = new Ki_Thread();
		//System.out.println("Thread start player: " + playerState);
        b.start();
		synchronized (b) {
			try {
				b.wait();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//System.out.println("Thread ende " + playerState);
	    	/*long newafter = System.nanoTime();
	    	long newrunningTimeNs = (newafter - newbefore);
	    	System.out.println("thread: " + newrunningTimeNs);*/
	    	System.out.println();

			return tue;
		}

	}


	public void putPlayerState(PlayerState playerState) {
		this.playerState = playerState;
	}


	public void resetAi() {
		map = null;
		playerState = PlayerState.UNKNOWN;
	}
	//***************************************
	public WumpusMapObject getWumpusObjectPlayer(){
		
		for(WumpusMapObject mapobj : map.getMap()){
			if(mapobj.contains(WumpusObjects.PLAYER)){
				obj = mapobj;
			}
		}
		
		return obj;
	}
	
	class Ki_Thread extends Thread{
	    int total;
	    @Override
	    public void run(){
	        synchronized(this){//aufräumen

        		before = System.nanoTime();
        		lastActionControll();
	        		if(path != null){
	        			if(path.size() < 2){
	        				path = new LinkedList<Node>();
		        			zielerstellung();
		        			pathFinding(getWumpusObjectPlayer(),zielauswahl());
	        			}
	        		}
	        		else{
	        			zielerstellung();
	        			pathFinding(getWumpusObjectPlayer(),zielauswahl());
	        		}

	            	after = System.nanoTime();
	            	runningTimeNs = (after - before);
	            	System.out.println("1. IF: " + runningTimeNs); 
	        			
	        		if(path != null && path.size() > 1){
		        		//lastActionControll();
		        		path.remove(path.size() - 1);
		        		if(path.get(path.size() - 1).getC() + 1 == getWumpusObjectPlayer().getColumn() && path.get(path.size() - 1).getR() == getWumpusObjectPlayer().getRow()){
		        			tue = Action.MOVE_LEFT;
		        			System.out.println("links");
		        		}
		        		else if(path.get(path.size() - 1).getC() - 1 == getWumpusObjectPlayer().getColumn() && path.get(path.size() - 1).getR() == getWumpusObjectPlayer().getRow()){
		        			tue = Action.MOVE_RIGHT;
		        			System.out.println("rechts");
		        		}
		        		else if(path.get(path.size() - 1).getC() == getWumpusObjectPlayer().getColumn() && path.get(path.size() - 1).getR() + 1 == getWumpusObjectPlayer().getRow()){
		        			tue = Action.MOVE_DOWN;
		        			System.out.println("runter");
		        		}
		        		else if(path.get(path.size() - 1).getC() == getWumpusObjectPlayer().getColumn() && path.get(path.size() - 1).getR() - 1 == getWumpusObjectPlayer().getRow()){
		        			tue = Action.MOVE_UP;
		        			System.out.println("hoch");
		        		}
		        		else{
		        			for(Node n: path){
		        				System.out.println("ERROR Node zuweit weg: " + n.getC() + " , " + n.getR());
		        			}
		        		}
		            	System.out.println("if"); 
		        		
	        		}
	        		else{
		        		//System.out.println("Keine Route");
		        		//lastActionControll();
		        		zielerstellung();
		        		pathFinding(getWumpusObjectPlayer(),zielauswahl());
		        		tue = Action.NO_ACTION;
		            	System.out.println("else"); 
	        		}

	            	after = System.nanoTime();
	            	runningTimeNs = (after - before);
	            	System.out.println("Thread: " + runningTimeNs); 
	        		notify();
	        	
	        	
	        }
	    }
	}

	private void pathdarstellen(){
		for(int i = 0;i < path.size();i++){
			System.out.println(i + "ter Knoten : " + path.get(i).getC() + " , " + path.get(i).getR());
		}
	}
	
	private void checkMovement(){
		if(tue == Action.MOVE_DOWN){
			System.out.println(Action.MOVE_DOWN);
		}
		else if(tue == Action.MOVE_LEFT){
			System.out.println(Action.MOVE_LEFT);
		}
		else if(tue == Action.MOVE_UP){
			System.out.println(Action.MOVE_UP);
		}
		else if(tue == Action.MOVE_RIGHT){
			System.out.println(Action.MOVE_RIGHT);
		}
		else {
			System.out.println("Action unkown");
		}
	}
	private void testRunFunction(){

	if(lastActionSuccess==ActionSuccess.HIT_WALL)
		i++;
	if(i>=4)
		i=0;
	if(i==0)
		tue = Action.MOVE_UP;
	else if(i==1)
		tue = Action.MOVE_RIGHT;
	else if(i==2)
		tue = Action.MOVE_DOWN;
	else if(i==3)
		tue = Action.MOVE_LEFT;

	}
	private void lastActionControll(){
		
		if(lastActionSuccess==ActionSuccess.HIT_WALL){
			switch(tue){
			case MOVE_DOWN:
				AiWumpusMapObject.hitLowerWall = true;
				AiWumpusMapObject.lowerWallPosition = getWumpusObjectPlayer().getRow() - 1;
				break;
			case MOVE_LEFT:
				AiWumpusMapObject.hitLeftWall = true;
				AiWumpusMapObject.leftWallPosition = getWumpusObjectPlayer().getColumn() - 1;
				break;
			case MOVE_RIGHT:
				AiWumpusMapObject.hitRightWall = true;
				AiWumpusMapObject.rightWallPosition = getWumpusObjectPlayer().getColumn() + 1;
				break;
			case MOVE_UP:
				AiWumpusMapObject.hitUpperWall = true;
				AiWumpusMapObject.upperWallPosition = getWumpusObjectPlayer().getRow() + 1;
				break;
			}
			cleanMap();
		}
		if(lastActionSuccess==ActionSuccess.TIMEOUT){
			System.out.println("TIME OUT -----------------------------------------------");
		}
	}
	private void zielerstellung(){
		int r,c;
		r=getWumpusObjectPlayer().getRow();
		c=getWumpusObjectPlayer().getColumn();
		zielmap.removeWumpusMapObject(r, c);
		zielmap.setWumpusMapObject(getWumpusObjectPlayer());
		
		List<WumpusObjects> potentialobjects = new ArrayList<WumpusObjects>();		
		if(getWumpusObjectPlayer().contains(WumpusObjects.STENCH)){
			potentialobjects.add(WumpusObjects.WUMPUS);
		}
		if(getWumpusObjectPlayer().contains(WumpusObjects.BREEZE)){
			potentialobjects.add(WumpusObjects.TRAP);
		}
		if(getWumpusObjectPlayer().contains(WumpusObjects.TWINKLE)){
			potentialobjects.add(WumpusObjects.GOLD);
		}

		if( !AiWumpusMapObject.hitUpperWall || AiWumpusMapObject.hitUpperWall && AiWumpusMapObject.upperWallPosition > r+1){
			if(zielmap.getWumpusMapObject(r+1, c) != null){
				AiWumpusMapObject.setObjectsList( zielmap.getWumpusMapObject(r+1, c), potentialobjects );
			}
			else{
				zielmap.setWumpusMapObject(AiWumpusMapObject.createWumpusMapObject(r+1, c, potentialobjects));
			}
		}
		
		if( !AiWumpusMapObject.hitRightWall || AiWumpusMapObject.hitRightWall && AiWumpusMapObject.rightWallPosition > c+1){
			if(zielmap.getWumpusMapObject(r, c+1) != null){
				AiWumpusMapObject.setObjectsList( zielmap.getWumpusMapObject(r, c+1), potentialobjects );
			}
			else{
				zielmap.setWumpusMapObject(AiWumpusMapObject.createWumpusMapObject(r, c+1, potentialobjects));
			}
		}
		
		if( !AiWumpusMapObject.hitLowerWall || AiWumpusMapObject.hitLowerWall && AiWumpusMapObject.lowerWallPosition < r-1){
			if(zielmap.getWumpusMapObject(r-1, c) != null){
				AiWumpusMapObject.setObjectsList( zielmap.getWumpusMapObject(r-1, c), potentialobjects );
			}
			else{
				zielmap.setWumpusMapObject(AiWumpusMapObject.createWumpusMapObject(r-1, c, potentialobjects));
			}
		}
		
		if( !AiWumpusMapObject.hitLeftWall || AiWumpusMapObject.hitLeftWall && AiWumpusMapObject.leftWallPosition < c-1){
			if(zielmap.getWumpusMapObject(r, c-1) != null){
				AiWumpusMapObject.setObjectsList( zielmap.getWumpusMapObject(r, c-1), potentialobjects );
			}
			else{
				zielmap.setWumpusMapObject(AiWumpusMapObject.createWumpusMapObject(r, c-1, potentialobjects));
			}
		}
	}
	private WumpusMapObject zielauswahl(){
		List<WumpusMapObject> newZielList = new ArrayList<WumpusMapObject>();
		
		for(WumpusMapObject openObject : zielmap.getMap()){
			if(openObject.getObjectsList().isEmpty()){
				newZielList.add(openObject);
			}
			//rest ziele noch wählen
			
		}
		return findShortZiel(newZielList);
	}
	private WumpusMapObject findShortZiel(List<WumpusMapObject> ziele){
		WumpusMapObject zielObject = null;
		int prio=4;
		
		for(WumpusMapObject openObject : ziele){
			if(zielObject == null || distanceToPlayer(openObject) < distanceToPlayer(zielObject)) {
				zielObject = openObject;
			}
			if(distanceToPlayer(zielObject) == distanceToPlayer(openObject)){
				//hoch
				if (prio > 1 && openObject.getColumn() == getWumpusObjectPlayer().getColumn() && openObject.getRow() == getWumpusObjectPlayer().getRow()+1){
					zielObject = openObject;
					prio=2;
				}//rechts
				else if (prio > 2 && openObject.getColumn() == getWumpusObjectPlayer().getColumn()+1 && openObject.getRow() == getWumpusObjectPlayer().getRow()){
					zielObject = openObject;
					prio=3;
				}//runter
				else if (prio > 3 && openObject.getColumn() == getWumpusObjectPlayer().getColumn() && openObject.getRow() == getWumpusObjectPlayer().getRow()-1){
					zielObject = openObject;
					prio=4;
				}//links
				else if (prio > 0 && openObject.getColumn() == getWumpusObjectPlayer().getColumn()-1 && openObject.getRow() == getWumpusObjectPlayer().getRow()){
					zielObject = openObject;
					prio=1;
				}
			}
		}
		return zielObject;
	}
	private int distanceToPlayer(WumpusMapObject zielObject){
		int distance;
		int distanceColumn,distanceRow;

		distanceColumn = (zielObject.getColumn() - getWumpusObjectPlayer().getColumn());
		distanceRow = (zielObject.getRow() - getWumpusObjectPlayer().getRow());
		
		distance = (int) Math.sqrt(distanceColumn * distanceColumn) + (int) Math.sqrt(distanceRow * distanceRow);
		
		return distance;
	}
	void cleanMap(){
		
		List<WumpusMapObject> vPositionsToRemove = new ArrayList<WumpusMapObject>();
		for( WumpusMapObject vMapObject : zielmap.getMap()){
			
			if( AiWumpusMapObject.hitLeftWall && vMapObject.getColumn() <= AiWumpusMapObject.leftWallPosition
					|| AiWumpusMapObject.hitRightWall && vMapObject.getColumn() >= AiWumpusMapObject.rightWallPosition
					|| AiWumpusMapObject.hitUpperWall && vMapObject.getRow() >= AiWumpusMapObject.upperWallPosition
					|| AiWumpusMapObject.hitLowerWall && vMapObject.getRow() <= AiWumpusMapObject.lowerWallPosition
					){
				vPositionsToRemove.add(vMapObject);
			}
			
		}
		
		for(WumpusMapObject vMapObject : vPositionsToRemove ){
			zielmap.removeWumpusMapObject(vMapObject.getRow(), vMapObject.getColumn());
		}
		
	}
	private void pathFinding(WumpusMapObject player, WumpusMapObject ziel){
    	before = System.nanoTime();
		//System.out.println("graph" + map.toString());
		if(graph.getNode(ziel.getColumn(), ziel.getRow())==null){
			graph.addNode(new Node(ziel));
		}
		//System.out.println("Matrix erstellung");
		graph.feetMatrix();
		//graph.createMatrix();
		//System.out.println("path erstellung");
		path = graph.aStar(graph.getNodeID(player.getColumn(), player.getRow()), graph.getNodeID(ziel.getColumn(), ziel.getRow()));
    	after = System.nanoTime();
    	runningTimeNs = (after - before);
    	System.out.println("path: " + runningTimeNs); 
	}
	

	
	
}