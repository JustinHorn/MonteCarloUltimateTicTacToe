package monte_carlo;

import java.util.ArrayList;

import Board.Board;
import tree.Node;
import tree.Root;

public class MonteCarlo {
	
	private Root root;

	public MonteCarlo() {
		root = new Root();
	}
	
	public void setTree(Root root) {
		this.root = root;
	}
	
	public void expandTree(Move m) {
		if(root.hasChild_withMove(m)) {
			Node child = root.getChild_withMove(m);
			root = child.this_to_Root();
		} else {
			throw new IllegalArgumentException("No such child: "+m);
		}
	}
	
	public boolean treeHasChildren() {
		return root.childrenAssigned();
	}
	
	public boolean treeHasChild_withMove(Move m) {
		return root.hasChild_withMove(m);
	}
	
	public Move calcMove(Board board,long duration) {
		set_and_checkTree(System.currentTimeMillis(),board);

		monteCarloAlgorithm(board,duration);
	
		return root.getChild_with_mostSimulations().getMove();
	}

	
	private void set_and_checkTree(long time, Board board) {
		setUpTree(time,board);
		doesTreeMatchBoard(board);
	}
	
	private void setUpTree(long time,Board board) {
		root.setTime(time);
		if(!treeHasChildren()) {
			root.setChildren_and_getRandomChild(Utils.getAvailableMoves(board));
		}
	}
	
	private void doesTreeMatchBoard(Board b) {
		ArrayList<Move> moves = b.getAvailableMoves();
		if(moves.parallelStream().anyMatch(move -> !treeHasChild_withMove(move))) {
			throw new IllegalArgumentException("Tree does not match Board");
		}
	}
	
	private void monteCarloAlgorithm(Board board, long duration) {
		long time = System.currentTimeMillis();
		while (System.currentTimeMillis() - time < duration) {			
			expand_advance_simulate(board);			
		}
		System.out.println("Simulations made: " + root.getSimulationCount());
	}
	
	
	private void expand_advance_simulate(Board board) {
		Node youngestChild = expand(root);
		Board board_atYoungestChild = Utils.advanceBoard(board, youngestChild);
		Move[] moves = Utils.getAvailableMoves(board_atYoungestChild);

		Node randomChild = youngestChild.setChildren_and_getRandomChild(moves);

		if (youngestChild.hasChildren()) {
			board_atYoungestChild.makeMove(randomChild.getMove());
		}
		
		int outcome = Utils.simulateRandomGame(board_atYoungestChild);
		randomChild.changeScore_and_simulationCount_of_MeAndParent(outcome);
	}

	protected Node expand(Root node) {
		Node bestChild = node.getChild_with_highestValue();
		while (bestChild.hasChildren()) {
			bestChild = bestChild.getChild_with_highestValue();
		}
		return bestChild;
	}
	
	
}
