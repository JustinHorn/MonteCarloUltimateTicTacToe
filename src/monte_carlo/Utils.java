package monte_carlo;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

import Board.Board;
import tree.Node;
import tree.Root;

public class Utils {

	
	public static Board advanceBoard(Board b, Node youngestChild) {
		b = b.clone();
		ArrayList<Move> moves_played = getMoves_from_nodeParents_asList(youngestChild);
		Collections.reverse(moves_played);
		moves_played.add(youngestChild.getMove());
		b.makeMoves(moves_played.toArray(new Move[moves_played.size()]));
		return b;
	}
	
	public static ArrayList<Root> getParentsFromNode(Node n) {
		ArrayList<Root> a = new ArrayList<Root>();
		Root parent = n.getParent();
		while(parent instanceof Node) {
			a.add(parent);
			parent = ((Node) parent).getParent();
		}
		a .add(parent);
		return a;
	}
	
	public static ArrayList<Node> getParentsFromNode_exceptRoot(Node n) {
		ArrayList<Root> list = getParentsFromNode(n);
		list.remove(list.size()-1);
		return list.parallelStream().map(a -> (Node) a).collect(Collectors.toCollection(ArrayList<Node>::new));
	}
	
	public static ArrayList<Move> getMoves_from_nodeParents_asList(Node n) {
		ArrayList<Node> list =  getParentsFromNode_exceptRoot(n);
		ArrayList<Move> moves = list.parallelStream().map(m_n-> m_n.getMove()).collect(Collectors.toCollection(ArrayList<Move>::new));
		return moves;
	}
	
	public static Move[] getMoves_from_nodeParents_asArray(Node n) {
		ArrayList<Move> moves = getMoves_from_nodeParents_asList(n);
		return moves.toArray(new Move[moves.size()]);
	}
	
	public static int simulateRandomGame(Board b) {
		b = b.clone();
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
