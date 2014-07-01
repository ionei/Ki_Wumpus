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
	private int c = 1;
	private int x,y;
	private WumpusMapObject temp;
	private WumpusMapObject obj;
	List<WumpusMapObject> tempList = new ArrayList<WumpusMapObject>();
    private Ki_Thread b = new Ki_Thread();
    private Action tue = Action.NO_ACTION, tue2= Action.NO_ACTION;
    private int i = 0;
    private int j = 0;
	private AiWumpusMap zielmap = new AiWumpusMap();
	private Graph graph = new Graph();
	private LinkedList<Node> path = null;
	private Node nodeOld;
	long before;
	long after;
	long runningTimeNs;
	boolean sicherheit = true;
	boolean wumpusKnow = false;
	
	
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
		lastActionControll();
	}


	public void putRemainingTime(long time) {
		remainingTime = time;
	}


	public void putPlayerArrows(int arrows) {
		remainingArrows = arrows;
	}


	public Action getAction() {

    	System.out.println("getaction()");
		// Returns no action to do nothing
		//tue = null;
		long newbefore = System.nanoTime();
		b = new Ki_Thread();
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
	        synchronized(this){

        		before = System.nanoTime();
        		System.out.println("Bin an : " + getWumpusObjectPlayer().getRow() + " , " + getWumpusObjectPlayer().getColumn());
        		if(path != null){
        			if(path.size() < 1){
        				path = new LinkedList<Node>();
	        			zielerstellung();
	        			if(wumpusKnow){
	        				System.out.println("wumpus umrennen ");
	        				List<WumpusObjects> potentialobjects = new ArrayList<WumpusObjects>();	
	    					if(zielmap.getWumpusMapObject(nodeOld.getR()+1, nodeOld.getC()) != null){
	    						if(!zielmap.getWumpusMapObject(nodeOld.getR()+1, nodeOld.getC()).contains(WumpusObjects.WUMPUS)&&!zielmap.getWumpusMapObject(nodeOld.getR()+1, nodeOld.getC()).contains(WumpusObjects.TRAP))
	    							zielmap.setWumpusMapObject(AiWumpusMapObject.createWumpusMapObject(nodeOld.getR()+1, nodeOld.getC(), potentialobjects));
	    					}
	    					if(zielmap.getWumpusMapObject(nodeOld.getR()-1, nodeOld.getC()) != null){
	    						if(!zielmap.getWumpusMapObject(nodeOld.getR()-1, nodeOld.getC()).contains(WumpusObjects.WUMPUS)&&!zielmap.getWumpusMapObject(nodeOld.getR()-1, nodeOld.getC()).contains(WumpusObjects.TRAP))
	    							zielmap.setWumpusMapObject(AiWumpusMapObject.createWumpusMapObject(nodeOld.getR()-1, nodeOld.getC(), potentialobjects));
	    					}
	    					if(zielmap.getWumpusMapObject(nodeOld.getR(), nodeOld.getC()+1) != null){
	    						if(!zielmap.getWumpusMapObject(nodeOld.getR(), nodeOld.getC()+1).contains(WumpusObjects.WUMPUS)&&!zielmap.getWumpusMapObject(nodeOld.getR(), nodeOld.getC()+1).contains(WumpusObjects.TRAP))
	    							zielmap.setWumpusMapObject(AiWumpusMapObject.createWumpusMapObject(nodeOld.getR(), nodeOld.getC()+1, potentialobjects));
	    					}
	    					if(zielmap.getWumpusMapObject(nodeOld.getR(), nodeOld.getC()-1) != null){
	    						if(!zielmap.getWumpusMapObject(nodeOld.getR(), nodeOld.getC()-1).contains(WumpusObjects.WUMPUS)&&!zielmap.getWumpusMapObject(nodeOld.getR(), nodeOld.getC()-1).contains(WumpusObjects.TRAP))
	    							zielmap.setWumpusMapObject(AiWumpusMapObject.createWumpusMapObject(nodeOld.getR(), nodeOld.getC()-1, potentialobjects));
	    					}
	    					potentialobjects.add(WumpusObjects.WUMPUS);
	    					zielmap.setWumpusMapObject(AiWumpusMapObject.createWumpusMapObject(nodeOld.getR(), nodeOld.getC(), potentialobjects));
	    					wumpusKnow = false;
	        			}
	        			tempList = zielauswahl();
	        			if(sicherheit){
		        			temp=findShortZiel(tempList);
	        			}
	        			else{
		        			temp=logic(tempList);
	        			}
	        			pathFinding(getWumpusObjectPlayer(),temp);
	        			path.remove(0);
        			}
        		}
        		else{
        			zielerstellung();
        			tempList = zielauswahl();
        			if(sicherheit){
        				System.out.println("Sicher ");
        				j = 0;
	        			temp=findShortZiel(tempList);
        			}
        			else if (!wumpusKnow){
        				System.out.println("nicht Sicher ");
        				j = 0;
	        			temp=logic(tempList);
        			}
        			
        				
        			pathFinding(getWumpusObjectPlayer(),temp);
        			path.remove(0);
        		}
        			
        		if(path != null && path.size() > 0){
	        		//lastActionControll();
	        		
	        		if(path.get(0).getC() + 1 == getWumpusObjectPlayer().getColumn() && path.get(0).getR() == getWumpusObjectPlayer().getRow()){
	        			if(wumpusKnow && path.size() == 1){
		        			tue = Action.SHOOT_LEFT;
		        			System.out.println("shoot links");
	        			}
	        			else{
		        			tue = Action.MOVE_LEFT;
		        			System.out.println("links");
	        			}
	        		}
	        		else if(path.get(0).getC() - 1 == getWumpusObjectPlayer().getColumn() && path.get(0).getR() == getWumpusObjectPlayer().getRow()){
	        			if(wumpusKnow && path.size() == 1){
		        			tue = Action.SHOOT_RIGHT;
		        			System.out.println("shoot rechts");
	        			}
	        			else{
		        			tue = Action.MOVE_RIGHT;
		        			System.out.println("rechts");
	        			}
	        		}
	        		else if(path.get(0).getC() == getWumpusObjectPlayer().getColumn() && path.get(0).getR() + 1 == getWumpusObjectPlayer().getRow()){
	        			if(wumpusKnow && path.size() == 1){
		        			tue = Action.SHOOT_DOWN;
		        			System.out.println("shoot runter");
	        			}
	        			else{
		        			tue = Action.MOVE_DOWN;
		        			System.out.println("runter");
	        			}
	        		}
	        		else if(path.get(0).getC() == getWumpusObjectPlayer().getColumn() && path.get(0).getR() - 1 == getWumpusObjectPlayer().getRow()){
	        			if(wumpusKnow && path.size() == 1){
		        			tue = Action.SHOOT_UP;
		        			System.out.println("shoot hoch");
	        			}
	        			else{
		        			tue = Action.MOVE_UP;
		        			System.out.println("hoch");
	        			}
	        		}
	        		else{
	        			for(Node n: path){
	        				System.out.println("ERROR Node zuweit weg: " + n.getC() + " , " + n.getR());
	        			}
	        		}
	        		notify();
	        		nodeOld = path.get(0);
	        		path.remove(0);
        		}
        		else{
	        		System.out.println("Keine Route");
	        		tue = Action.NO_ACTION;
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
		else if(lastActionSuccess == ActionSuccess.TIMEOUT){
			System.out.println("TIME OUT -----------------------------------------------");
		}
		else {
			System.out.println("Letzte Aktion " + lastActionSuccess);
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
	private List<WumpusMapObject> zielauswahl(){
		List<WumpusMapObject> newZielList = new ArrayList<WumpusMapObject>();
		List<WumpusMapObject> risikoZielList = new ArrayList<WumpusMapObject>();

		System.out.println(zielmap.getMap().toString());
		for(WumpusMapObject openObject : zielmap.getMap()){
			if(openObject.getObjectsList().isEmpty()){
				newZielList.add(openObject);
			}
			else if(openObject.getObjectsList().contains(WumpusObjects.GOLD)){
				if(openObject.getObjectsList().size() == 1)
					newZielList.add(openObject);
			}
			else if(!openObject.getObjectsList().contains(WumpusObjects.PLAYER)){
					risikoZielList.add(openObject);
			}
			//rest ziele noch wählen
			
		}
		System.out.println(newZielList.toString());
		System.out.println(risikoZielList.toString());
		if(newZielList.size() > 0){
			sicherheit = true;
			return newZielList;
		}
		else {
			sicherheit = false;
			return risikoZielList;
		}
			
	}

	private WumpusMapObject logic(List<WumpusMapObject> risikoZielList) {  //Wände? logic überdenken
		List<objectContainer> risikoList = new ArrayList<objectContainer>();
		WumpusMapObject ziel = null;
		WumpusMapObject wumpusZiel = null;
		int wumpusRisiko;
		int trapRisiko;
		int row;
		int column;
		for(WumpusMapObject openObject : risikoZielList){
			wumpusRisiko = 0;
			trapRisiko = 0;
			row = openObject.getRow();
			column = openObject.getColumn();
				// oben kontrolle
			if(map.getWumpusMapObject(row + 1, column) != null){
				if(map.getWumpusMapObject(row + 1, column).contains(WumpusObjects.BREEZE)){
					trapRisiko+=10;	
				}
				if(map.getWumpusMapObject(row + 1, column).contains(WumpusObjects.STENCH)){
					wumpusRisiko+=10;
				}
			}
			else if(row + 1 == AiWumpusMapObject.upperWallPosition && AiWumpusMapObject.hitUpperWall){
				if(openObject.contains(WumpusObjects.TRAP))
					trapRisiko++;
				if(openObject.contains(WumpusObjects.WUMPUS))
					wumpusRisiko++;
			}
				//unten kontrolle
			if(map.getWumpusMapObject(row - 1, column) != null){
				if(map.getWumpusMapObject(row - 1, column).contains(WumpusObjects.BREEZE)){
					trapRisiko+=10;	
				}
				if(map.getWumpusMapObject(row - 1, column).contains(WumpusObjects.STENCH)){
					wumpusRisiko+=10;
				}
			}
			else if(row - 1 == AiWumpusMapObject.lowerWallPosition && AiWumpusMapObject.hitLowerWall){
				if(openObject.contains(WumpusObjects.TRAP))
					trapRisiko++;
				if(openObject.contains(WumpusObjects.WUMPUS))
					wumpusRisiko++;
			}
			// rechts kontrolle
			if(map.getWumpusMapObject(row , column + 1) != null){
				if(map.getWumpusMapObject(row , column + 1).contains(WumpusObjects.BREEZE)){
					trapRisiko+=10;	
				}
				if(map.getWumpusMapObject(row , column + 1).contains(WumpusObjects.STENCH)){
					wumpusRisiko+=10;
				}
			}
			else if(column + 1 == AiWumpusMapObject.rightWallPosition && AiWumpusMapObject.hitRightWall){
				if(openObject.contains(WumpusObjects.TRAP))
					trapRisiko++;
				if(openObject.contains(WumpusObjects.WUMPUS))
					wumpusRisiko++;
			}
			//links kontrolle
			if(map.getWumpusMapObject(row , column - 1) != null){
				if(map.getWumpusMapObject(row , column - 1).contains(WumpusObjects.BREEZE)){
					trapRisiko+=10;	
				}
				if(map.getWumpusMapObject(row , column - 1).contains(WumpusObjects.STENCH)){
					wumpusRisiko+=10;
				}
			}
			else if(column - 1 == AiWumpusMapObject.leftWallPosition && AiWumpusMapObject.hitLeftWall){
				if(openObject.contains(WumpusObjects.TRAP))
					trapRisiko++;
				if(openObject.contains(WumpusObjects.WUMPUS))
					wumpusRisiko++;
			}
			
			risikoList.add(new objectContainer(trapRisiko, wumpusRisiko, openObject));
			
		}
		int min = 100;
		int max = 0;
		for(objectContainer temp: risikoList){
			if(temp.getRisiko()<min){
				min = temp.getRisiko();
				ziel = temp.getObject();
				//System.out.println("mögliches Ziel : " + ziel.getRow() + " , " + ziel.getColumn() + "  Risiko : " + temp.getRisiko());
			}
			if(temp.getWumpusRisiko()>max){
				max = temp.getWumpusRisiko();
				wumpusZiel = temp.getObject();
				//System.out.println("mögliches Ziel : " + ziel.getRow() + " , " + ziel.getColumn() + "  Risiko : " + temp.getRisiko());
			}
		}
		if(max == 40 || max == 31 || max == 22 || max == 30 || max == 21 || max == 12 ){
			wumpusKnow = true;
			return wumpusZiel;
		}
		return ziel;
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
		
		//distance = (int) Math.sqrt(distanceColumn * distanceColumn) + (int) Math.sqrt(distanceRow * distanceRow);
		distance = (int) Math.sqrt(distanceColumn * distanceColumn*10 + distanceRow * distanceRow*10);
		
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
		System.out.println("ZIEL: " + ziel.getRow() + " , " + ziel.getColumn()  + " sicherheit "+ sicherheit);
		if(graph.getNode(ziel.getColumn(), ziel.getRow()) == null){
			graph.addNode(new Node(ziel));
			c++;
		//}
		//System.out.println("Matrix erstellung");
			a++;
			System.out.println("bin hier zum " + a + " ten mal und habe " + c + " Knoten erstellt und besitze "+ graph.getNodes().size() + " Konten");
			graph.feetMatrix();
			//graph.createMatrix();
		}
		else
			System.out.println("                         !!!!!!!!!!!!!!!!!!!!!! war am ziel schonmal ");
		//System.out.println("path erstellung");
		path = graph.aStar(graph.getNodeID(ziel.getColumn(), ziel.getRow()),graph.getNodeID(player.getColumn(), player.getRow()));
    	after = System.nanoTime();
    	runningTimeNs = (after - before);
    	System.out.println("path: " + runningTimeNs); 
	}
	

	
	
}