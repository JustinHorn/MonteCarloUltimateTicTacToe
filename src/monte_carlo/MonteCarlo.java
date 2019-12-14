package monte_carlo;

import monte_carlo.board.Board;
import monte_carlo.tree.*;

public class MonteCarlo {
	
	
	public static Root calcMove(Board board, long duration) {
		Root root = new Root();
		root.setChildren(Utils.getAvailableMoves(board));
		return calcMove(board,root, duration);
	}

	public static Root calcMove(Board board,Root outputArgument_root, long duration) {
		TreeUtils.doesTreeMatchBoard(outputArgument_root, board);
		
		monteCarloAlgorithm(board,outputArgument_root,duration);
	
		return outputArgument_root;
	}

	
	private static void monteCarloAlgorithm(Board board,Root root, long duration) { 
		long time = System.currentTimeMillis();
		while (System.currentTimeMillis() - time < duration) {			
			expand_advance_simulate(board,root);			
		}
		System.out.println("Simulations made: " + root.getSimulationCount());
	}
	
	
	private static void expand_advance_simulate(Board board,Root root) {
		Node youngestChild = TreeUtils.expandTree(root);
		Board board_atYoungestChild = Utils.advanceBoard(board, youngestChild);

		if (board_atYoungestChild.isOngoing()) {
			advance_simulate_update(youngestChild,board_atYoungestChild);
		} else {
			youngestChild.changeScore_and_simulationCount_of_MeAndParent(board_atYoungestChild.whoHasWon());
		}
	}
	
	private static void advance_simulate_update(Node youngestChild,Board board_atYoungestChild) {
		Node randomChild = advance_child(board_atYoungestChild,youngestChild);
		board_atYoungestChild.makeMove(randomChild.getMove());
		int outcome = Utils.simulateRandomGame(board_atYoungestChild);
		randomChild.changeScore_and_simulationCount_of_MeAndParent(outcome);
	}
	
	private static Node advance_child(Board board_atYoungestChild,Node youngestChild) {
		Move[] moves = Utils.getAvailableMoves(board_atYoungestChild);
		youngestChild.setChildren(moves);
		return youngestChild.getRandomChild();
	}
	
	
	
	
}
