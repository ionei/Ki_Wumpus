package ki_FW_RK;

import java.util.ArrayList;
import java.util.List;

import de.northernstars.jwumpus.core.WumpusMapObject;
import de.northernstars.jwumpus.core.WumpusObjects;

public class AiWumpusMapObject {

	public static boolean hitUpperWall = false;
	public static int upperWallPosition = 0;
	public static boolean hitLowerWall = false;
	public static int lowerWallPosition = 0;
	public static boolean hitLeftWall = false;
	public static int leftWallPosition = 0;
	public static boolean hitRightWall = false;
	public static int rightWallPosition = 0;
	
	public static WumpusMapObject createWumpusMapObject(int row, int column,
			List<WumpusObjects> objectsList) {
		WumpusMapObject newobjekt = new WumpusMapObject(row, column, objectsList);
		newobjekt.setVisited(false);
		return newobjekt;
	}

	public static WumpusMapObject setObjectsList(WumpusMapObject aobject,List<WumpusObjects> objectsList) {
		if(aobject.getObjectsList()==null || aobject.getObjectsList().isEmpty() || aobject.isVisited()){
			return aobject;
		}
		List<WumpusObjects> newlist = new ArrayList<WumpusObjects>();
		for(WumpusObjects newstate : objectsList){
			if(aobject.getObjectsList().contains(newstate)){
				newlist.add(newstate);
			}
		}
		aobject.setObjectsList(newlist);
		return aobject;
	}
}
