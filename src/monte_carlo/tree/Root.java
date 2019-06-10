package monte_carlo.tree;
import java.util.Arrays;
import java.util.Comparator;

import monte_carlo.Move;

public class Root  {

	private int simulationCount;
	private long startingTime;
	protected Node[] child = null;  
	

	public Root(int sC) {
		simulationCount = sC;
		this.startingTime = System.currentTimeMillis();
	}
	
	public Root() {
		startingTime = System.currentTimeMillis();
	}
	
	public boolean childrenAssigned() {
		return child != null;
	}
	
	public boolean hasChildren() {
		if(child == null || child.length == 0) {
			return false;
		}
		return true;
	}


	public long getStartTime() {
		return startingTime;
	}
	
	public int getSimulationCount() {
		return simulationCount;
	}
	
	protected Node[] getChildren() {
		return child;
	}
	
	public void setTime(long time) {
		startingTime = time;
		if(child != null) {
			for(int i = 0; i < child.length; i++) {
				child[i].setTime(time);
			}
		}
	}
	
	
	public boolean hasChild_withMove(Move m) {
		if (child == null) {
			return false;
		}
		for(int i = 0; i < child.length;i++) {
			if(m.equals(child[i].getMove())) {
				return true;
			}
		}
		return false;
	}
	
	public Node getChild_withMove(Move m) {
		for(int i = 0; i < child.length;i++) {
			if(m.equals(child[i].getMove())) {
				return child[i];
			}
		}
		throw new IllegalArgumentException("No child with such move: "+ m);
	}
	
	public void setChildren(Node[] children) {
		child = children;
		for(int i = 0; i < children.length;i++) {
			child[i].setParent(this);
		}
	}
	
	public void setChildren(Move[] moves) {
		if(moves.length > 0) {
			child = new Node[moves.length];
			for (int i = 0; i < child.length; i++) {
				child[i] = new Node(this, moves[i]);
			}
		} else {
			throw new IllegalArgumentException("No moves given!");
		}
	}
	
	public Node getRandomChild() {
		if(hasChildren()) {
			int index = (int)(child.length*Math.random());
			return child[index];
		} 
		throw new IllegalArgumentException("Thsi Node has no children!");
	}
	
	public void changeScore_and_simulationCount_of_MeAndParent(int winLooseOrDraw) {
		simulationCount++;
	}
}

final class CompareSimulationCount implements Comparator<Node>{

	@Override
	public int compare(Node node1, Node node2) {
		if(node1.getSimulationCount() < node2.getSimulationCount()) {
			return -1;
		} else if(node1.getSimulationCount() > node2.getSimulationCount()) {
			return 1;
		} else {
			return 0;
		}
	}
	
}


final class CompareValue implements Comparator<Node>{

	@Override
	public int compare(Node node1, Node node2) {
		if(node1.getValue() < node2.getValue()) {
			return -1;
		} else if(node1.getValue() > node2.getValue()) {
			return 1;
		} else {
			return 0;
		}
	}
	
}
