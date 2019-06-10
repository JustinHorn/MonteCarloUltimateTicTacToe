package monte_carlo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import Board.Board;
import Board.Test_Board;
import monte_carlo.*;
import tree.Node;
import tree.Root;


public class Test_Utils {
	
	@Test
	void test_getAvailableMoves() {
		Board b = new Board();
		Move[] moves = Utils.getAvailableMoves(b);
		assertEquals(81,moves.length);
	}
	
	
	@Test
	void test_getParentsFromNode() {
		Root root = new Root();
		Board b = new Board();
		
		Node random_child = root.setChildren_and_getRandomChild(Utils.getAvailableMoves(b));
		ArrayList<Root> parents = Utils.getParentsFromNode(random_child);
		
		assertEquals(root,parents.get(0));
		assertEquals(1,parents.size());
	}
	
	@Test
	void test_getParentsFromNode_exceptRoot() {
		Root root = new Root();
		Board b = new Board();
		
		Node random_child = root.setChildren_and_getRandomChild(Utils.getAvailableMoves(b));
		b.makeMove(random_child.getMove());
		Node childOf_randomChild = random_child.setChildren_and_getRandomChild(Utils.getAvailableMoves(b));
		
		
		ArrayList<Node> parent = Utils.getParentsFromNode_exceptRoot(childOf_randomChild);
		
		assertEquals(random_child,parent.get(0));
		assertEquals(1,parent.size());
	}
	
	@Test
	void test_getMovesFromParents() {
		Root root = new Root();
		Board b = new Board();
		
		
		Node child1 = root.setChildren_and_getRandomChild(Utils.getAvailableMoves(b));
		b.makeMove(child1.getMove());
		Node child2 = child1.setChildren_and_getRandomChild(Utils.getAvailableMoves(b));
		b.makeMove(child2.getMove());
		Node child3 = child2.setChildren_and_getRandomChild(Utils.getAvailableMoves(b));
		
		Move[] moves = Utils.getMoves_from_nodeParents_asArray(child3);
		
		assertEquals(child2.getMove(),moves[0]);
		assertEquals(child1.getMove(),moves[1]);
		
		ArrayList<Move> moves2 = Utils.getMoves_from_nodeParents_asList(child3);
		Collections.reverse(moves2);
		
		assertEquals(child2.getMove(),moves2.get(1));
		assertEquals(child1.getMove(),moves2.get(0));
	}
	
	@Test
	void test_simulateGame() {
		Board b = new Board();
		assertTrue(Utils.simulateRandomGame(b) != b.EMPTY);
	}
	
	@Test
	void test_advanceGame() {
		Board b = new Board();
		Board old_board = b.clone();
		Root root = new Root();		
		
		Node child1 = root.setChildren_and_getRandomChild(Utils.getAvailableMoves(b));
		b.makeMove(child1.getMove());
		Node child2 = child1.setChildren_and_getRandomChild(Utils.getAvailableMoves(b));
		b.makeMove(child2.getMove());
		Node child3 = child2.setChildren_and_getRandomChild(Utils.getAvailableMoves(b));
		b.makeMove(child3.getMove());
		
		old_board = Utils.advanceBoard(old_board,child3);
		
		GenralsTest_Utils.failure_if_sth_isDifferent(old_board, b);
		
	}
}
