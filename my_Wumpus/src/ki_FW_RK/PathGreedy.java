package ki_FW_RK;

import java.util.LinkedList;

import de.northernstars.jwumpus.core.WumpusMapObject;



public class PathGreedy {
	LinkedList<NodeGreedy> nodes = new LinkedList<NodeGreedy>();
	
	boolean addNode(WumpusMapObject object){
		nodes.add(new NodeGreedy(object.getColumn(),object.getRow()));
		return false;
	}
	
}
