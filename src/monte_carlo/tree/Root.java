package monte_carlo.tree;
import java.util.Arrays;
import java.util.Comparator;

import monte_carlo.Move;

public class Root  {

	private int simulationCount;
	private long startingTime;
	protected Node[] child = null;  
	protected Value_function f;

	public Root(int sC,Value_function f) {
		errorhandling(f);
		simulationCount = sC;
		this.f = f;
		this.startingTime = System.currentTimeMillis();
	}
	
	public Root(int sC) {
		simulationCount = sC;
		init_value_function();
		this.startingTime = System.currentTimeMillis();
	}
	
	public Root(Value_function f) {
		errorhandling(f);
		this.f = f;
		startingTime = System.currentTimeMillis();
	}
	public Root() {
		init_value_function();
		startingTime = System.currentTimeMillis();
	}
	
	private void errorhandling(Value_function f) {
		if(f == null) {
			throw new IllegalArgumentException("Value_function is null");
		}
	}
	
	private void init_value_function() {
		f = new Value_function () {
				public double calcValue(long time,int score,int simulationCount) {
					if(simulationCount == 0) {
						return 0.5*Math.log10(Math.sqrt(System.currentTimeMillis()+1-time));
					} else {
						return ((double) score)/simulationCount+ 0.5*Math.log10(Math.sqrt(System.currentTimeMillis()+1-time)/simulationCount);
					}
				}
				public String getName() {
					return "Narrow";
				}
			};
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
	public void setValue_function(Value_function f) {
		this.f = f;
		if(childrenAssigned()) {
			for(int i = 0; i < child.length;i++) {
				child[i].setValue_function(f);
			}
		}
	}
	
	
	public boolean hasChild_withMove(Move m) {
		if (!childrenAssigned()) {
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
				child[i] = new Node(this, moves[i], f);
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

