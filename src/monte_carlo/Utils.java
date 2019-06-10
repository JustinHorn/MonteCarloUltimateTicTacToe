package monte_carlo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

import Board.Board;
import tree.Node;
import tree.Root;
import tree.TreeUtils;

public class Utils {

	
	public static Board advanceBoard(Board b, Node youngestChild) {
		b = b.clone();
		ArrayList<Move> moves_played = TreeUtils.getMoves_from_nodeParents_asList(youngestChild);
		Collections.reverse(moves_played);
		moves_played.add(youngestChild.getMove());
		b.makeMoves(moves_played.toArray(new Move[moves_played.size()]));
		return b;
	}
	
	public static int simulateRandomGame(Board b) {
		b = b.clone(); 	// will the global object become the clone two? no....
		while(b.isOngoing()) {
			ArrayList<Move> moves = b.getAvailableMoves();
			int index = (int)(Math.random()*moves.size());
			b.makeMove(moves.get(index));
		}
		return b.whoHasWon();
	}
	
	public static Move[] getAvailableMoves(Board b) {
		ArrayList<Move> moves = b.getAvailableMoves();
		return moves.toArray(new Move[moves.size()]);
	}

	
}
