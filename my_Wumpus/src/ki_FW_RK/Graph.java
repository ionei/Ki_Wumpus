package ki_FW_RK;

import java.util.LinkedList;

public class Graph {
	private LinkedList<Node> nodes;
	private int[] [] matrix;
	private int[] [] matrixNew;
	public Graph(){
		nodes = new LinkedList<Node>();
	}
	public void addBorder(int from, int to){
		matrix[from] [to] = 1;
		matrix[to] [from] = 1;
	}
	public void newAddBorder(int from, int to){
		matrixNew[from] [to] = 1;
		matrixNew[to] [from] = 1;
	}
	public void addNode(Node node){
		nodes.add(node);
	}
	public int getNodeID(int c, int r){
		for(int i = 0;i < nodes.size();i++){
			if(r == nodes.get(i).getR() && c == nodes.get(i).getC()) 
				return i;
		}
		return -1;
	}
	public Node getNode(int c, int r){
		for(int i = 0;i < nodes.size();i++){
			if(r == nodes.get(i).getR() && c == nodes.get(i).getC()) 
				return nodes.get(i);
		}
		return null;
	}
	
	public void createMatrix(){
		matrix = new int[nodes.size()] [nodes.size()];
		for(int i = 0;i < nodes.size();i++){
			for(int j = ((int)(nodes.size()+1)/2);j < nodes.size();j++){
				int absc = nodes.get(i).getC() - nodes.get(j).getC();
				int absr = nodes.get(i).getR() - nodes.get(j).getR();
				int abs = absc * absc + absr * absr;
				if (abs == 1) addBorder(i, j);
			}
		}
	}
	
	public void feetMatrix(){
		if(matrix[0].length != nodes.size()){
			matrixNew = new int[nodes.size()] [nodes.size()];
			for (int i = 0; i < matrix[0].length; i++) {
				System.arraycopy(matrix[i], 0, matrixNew[i], 0, matrix[0].length);
				int absc = nodes.get(i).getC() - nodes.get(matrix[0].length).getC();//nodes größe?
				int absr = nodes.get(i).getR() - nodes.get(matrix[0].length).getR();
				int abs = absc * absc + absr * absr;
				if (abs == 1) newAddBorder(i, matrix[0].length);
			}
			matrix = matrixNew;
		}
		else{
			System.out.println("ERROR feetMatrix() Matrixgröße: " + matrix[0].length + "  Kontenanzahl: " + nodes.size());
		}
	}
	
	private int getNextNode(){
		int id = -1;
		int value = Integer.MAX_VALUE;
		for(int i = 0;i < nodes.size();i++){
			if(!nodes.get(i).isVisited() && nodes.get(i).getRangeAddHeuristic() < value){
				id = i;
				value = nodes.get(i).getRangeAddHeuristic();
			}
		}
		return id;
	}
	
	public LinkedList<Node> aStar(int startnode, int endnode){
		int id, newDistance;
		int maxValue = Integer.MAX_VALUE - 100000;
		for(int i = 0;i < nodes.size();i++){
			int absc = nodes.get(i).getC() - nodes.get(endnode).getC();
			int absr = nodes.get(i).getR() - nodes.get(endnode).getR();
			nodes.get(i).setHeuristic((int) Math.sqrt(absc * absc + absr * absr));
			nodes.get(i).setVisited(false);
			nodes.get(i).setRange(maxValue);
		}
		nodes.get(startnode).setRange(0);
		outer : for(int i = 0;i < nodes.size();i++){
			id = getNextNode();
			if(id == -1) break;
			nodes.get(id).setVisited(true);
			for(int ab = 0;ab < nodes.size();ab++){
				if(!nodes.get(ab).isVisited() && matrix[ab] [id] > 0){
					newDistance = nodes.get(id).getRange() + matrix[ab] [id];
					if(newDistance < nodes.get(ab).getRange()){
						nodes.get(ab).setRange(newDistance);
						nodes.get(ab).setComingFrom(id);
						
						if(ab == endnode) {
							break outer;
						}
					}
				}
			}
		}


		LinkedList<Node> way = new LinkedList<Node>();
		id = endnode;
		way.add(nodes.get(id));
		while(id != startnode){
			id = nodes.get(id).getComingFrom();
			way.add(nodes.get(id));
		}
		
		return way;
	}
	public LinkedList<Node> myaStar(int startnode, int endnode){
		int id, newDistance;
		int maxValue = Integer.MAX_VALUE - 100000;
		for(int i = 0;i < nodes.size();i++){
			int absc = nodes.get(i).getC() - nodes.get(endnode).getC();
			int absr = nodes.get(i).getR() - nodes.get(endnode).getR();
			nodes.get(i).setHeuristic((int) Math.sqrt(absc * absc + absr * absr));
			nodes.get(i).setVisited(false);
			nodes.get(i).setRange(maxValue);
		}
		nodes.get(startnode).setRange(0);
		outer : for(int i = 0;i < nodes.size();i++){
			id = getNextNode();
			if(id == -1) break;
			nodes.get(id).setVisited(true);
			for(int ab = 0;ab < nodes.size();ab++){
				if(!nodes.get(ab).isVisited() && matrix[ab] [id] > 0){
					newDistance = nodes.get(id).getRange() + matrix[ab] [id];
					if(newDistance < nodes.get(ab).getRange()){
						nodes.get(ab).setRange(newDistance);
						nodes.get(ab).setComingFrom(id);
						
						if(ab == endnode) break outer;
					}
				}
			}
		}


		LinkedList<Node> way = new LinkedList<Node>();
		id = endnode;
		way.add(nodes.get(id));
		while(id != startnode){
			id = nodes.get(id).getComingFrom();
			way.add(nodes.get(id));
		}
		
		return way;
	}
	public int[][] getMatrix() {
		return matrix;
	}
	public void setMatrix(int[][] matrix) {
		this.matrix = matrix;
	}
	public LinkedList<Node> getNodes() {
		return nodes;
	}
}
