package Arena;

import monte_carlo.MonteCarlo;
import monte_carlo.Move;
import monte_carlo.Utils;
import monte_carlo.board.Board;
import monte_carlo.tree.*;

public class Bot {

	private final long duration;
	private int score = 0;
	private int scoreX = 0;
	private int scoreO = 0;
	private int games_played = 0;
	private Value_function value_function;
	
	public Bot(long duration) {
		this.duration = duration;
	}
	
	public Bot(long duration,Value_function f) {
		this.duration = duration;
		this.value_function=f;
	}
	
	public long getDuration() {
		return duration;
	}
	
	public int getScore( ) {
		return score;
	}

	public void incrementScore() {
		score++;
	}

	public void incrementScoreX() {
		scoreX++;
	}

	public void incrementScoreO() {
		scoreO++;
	}

	public void incrementGames_played() {
		games_played++;
	}
	
	public String value_function_getName( ) {
		if(value_function == null) {
			return "Standard";
		} else {
			return value_function.getName();
		}
	}
	
	public Move makeMove(Board b) {
		Root calculatedRoot;
		if(value_function == null) {
			calculatedRoot = MonteCarlo.calcMove(b, duration);
		} else {
			Root root = new Root(value_function);
			root.setChildren(Utils.getAvailableMoves(b));
			calculatedRoot = MonteCarlo.calcMove(b,root, duration);
		}
		return TreeUtils.getChild_with_mostSimulations(calculatedRoot).getMove();
	}

	@Override
	public String toString() {
		return duration +", " + value_function_getName()+" score: "+score+" scoreX: "+scoreX+ " scoreO: "+scoreO+" games: "+ games_played;
	}
}
