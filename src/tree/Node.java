package tree;
import Board.Board;
import monte_carlo.Move;

public class Node extends Root{
	private int score;
	private Move move;
	private Root parent;
	private double value;
	
	public Node(Root parent, Move move) {
		super();
		score = 0;
		this.parent = parent;
		this.move = move;
	}

	public void calcValue(long time) {
		/*Apperently it can happen that this function is executed in the same millisecond the object etc. got created. Therefor +1*/
		if (getSimulationCount() > 0) {
			value = ((double) score) / getSimulationCount()
					+2* Math.log10(Math.sqrt(time+1 - getStartTime()) / getSimulationCount());
		} else {
			value = +2* Math.log10( Math.sqrt(time+1 -  getStartTime()));
		}
	}
	
	public Root this_to_Root() {
		Root root = new Root(getSimulationCount());
		if(childrenAssigned()) {
			root.setChildren(getChildren());
		}
		return root;
		
	}
	
	public void setParent(Root parent) {
		this.parent = parent;
	}
	
	public double getValue() {
		return value;
	}
	

	public Root getParent() {
		return parent;
	}
	
	public int getScore() {
		return score;
	}
	

	@Override
	public void changeScore_and_simulationCount_of_MeAndParent(int winLooseOrDraw) {
		if (winLooseOrDraw == move.getPlayer() ) {
			score++;
		} else if (winLooseOrDraw == Board.EMPTY) {

		} else {
			score--;
		}
		super.changeScore_and_simulationCount_of_MeAndParent(winLooseOrDraw);
		parent.changeScore_and_simulationCount_of_MeAndParent(winLooseOrDraw);
	}

	public Move getMove() {
		return move;
	}

	@Override
	public Node getChild_with_mostSimulations() {
		throw new IllegalAccessError("You should not use this");

	}


}


