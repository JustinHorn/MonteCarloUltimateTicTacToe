package monte_carlo.tree;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.stream.Collectors;

import monte_carlo.Move;
import monte_carlo.board.Board;

public class TreeUtils {

	public static Root expandTree(Root root, Move m) {
		if(root.hasChild_withMove(m)) {
			Node child = root.getChild_withMove(m);
			return node_to_Root(child);
		} else {
			throw new IllegalArgumentException("No such child: "+m);
		}
	}
	
	public static Root node_to_Root(Node n) {
		Root root = new Root(n.getSimulationCount());
		if(n.childrenAssigned()) {
			root.setChildren(n.getChildren());
		}
		return root;
		
	}
	
	public static void doesTreeMatchBoard(Root root, Board board) {
		ArrayList<Move> moves = board.getAvailableMoves();
		if(moves.parallelStream().anyMatch(move -> !root.hasChild_withMove(move))) {
			throw new IllegalArgumentException("First generation of tree Moves does not match avaialble moves of board");
		}
	}
	
	public static Node expandTree(Root root) {
		Node bestChild = TreeUtils.getChild_with_highestValue(root);
		while (bestChild.hasChildren()) {
			bestChild = TreeUtils.getChild_with_highestValue(bestChild);
		}
		return bestChild;
	}
	
	public static Node getChild_with_highestValue(Root root) {
		Node[] child = root.getChildren();
		if(child == null) {
			throw new NullPointerException("Children have not been assinged!");
		}
		if(child.length > 0) {
			long time = System.currentTimeMillis();
			for(int i = 0; i < child.length;i++) {
				child[i].calcValue(time);
			}
			Arrays.sort(child, new CompareValue());
			return child[child.length-1];
		} else {
			return (Node) root;
		}
	}
	
	public static Node getChild_with_mostSimulations(Root root) {
		if (root.childrenAssigned()) {
				Node[] child = root.getChildren();
				Arrays.sort(child, new CompareSimulationCount());
				return child[child.length-1];
		} 
		throw new IllegalAccessError("Children have not been assinged!");
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


