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
import tree.TreeUtils;


public class Test_Utils {
	
	@Test
	void test_getAvailableMoves() {
		Board b = new Board();
		Move[] moves = Utils.getAvailableMoves(b);
		assertEquals(81,moves.length);
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
		root.setChildren(Utils.getAvailableMoves(b));
		Node child1 = root.getRandomChild();
		b.makeMove(child1.getMove());
		child1.setChildren(Utils.getAvailableMoves(b));
		Node child2 = child1.getRandomChild();
		b.makeMove(child2.getMove());
		child2.setChildren(Utils.getAvailableMoves(b));
		Node child3 = child2.getRandomChild();
		
		b.makeMove(child3.getMove());
		
		old_board = Utils.advanceBoard(old_board,child3);
		
		GenralsTest_Utils.failure_if_sth_isDifferent(old_board, b);
		
	}
}
