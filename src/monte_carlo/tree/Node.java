package monte_carlo.tree;
import monte_carlo.Move;
import monte_carlo.board.Board;


public class Node extends Root{
	private int score;
	private Move move;
	private Root parent;
	private double value;
	
	public Node(Root parent, Move move,Value_function f) {
		super(f);
		score = 0;
		this.parent = parent;
		this.move = move;
	}

	public void calcValue(long time) {
		value = f.calcValue(time,score, getSimulationCount());
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

}


