package monte_carlo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;

import Board.Board;
import monte_carlo.*;
import tree.Node;
import tree.Root;


public class Test_MonteCarlo {

	@Test
	void test_setTree_hasChild() {
		Root root = new Root();
		Move m = new Move(1,2,3);
		Move[] moves = new Move[] {m};
		root.setChildren_and_getRandomChild(moves);
		
		MonteCarlo monte = new MonteCarlo();
		monte.setTree(root);
		
		assertEquals(true,monte.treeHasChild_withMove(m));
	}
	@Test
	void test_expandTree() {
		Move lvl1 = new Move(1,2,3);
		Move lvl2 = new Move(1,2,4);
		Root root = setUptree_with_hightOfTwo(lvl1,lvl2);
		
		MonteCarlo monte = new MonteCarlo();
		monte.setTree(root);
		assertEquals(true,monte.treeHasChild_withMove(lvl1));

		monte.expandTree(lvl1);
		assertEquals(true,monte.treeHasChild_withMove(lvl2));
	}
	
	private Root setUptree_with_hightOfTwo(Move m1,Move m2) {
		Root root = new Root();
		Node randomChild = root.setChildren_and_getRandomChild(new Move[]{m1});
		randomChild.setChildren_and_getRandomChild(new Move[] {m2});
		return root;
	}
	
	
	@Test
	void test_MonteCarlo() {
		Board b = new Board();
		Move move = new MonteCarlo().calcMove(b,1);
		System.out.println(move);
	}
	
	@Test
	void test_incorrect_TreeDoesNotMacthBoard() {
		Board b = new Board();
		Root root = new Root();
		root.setChildren_and_getRandomChild(Utils.getAvailableMoves(b));
		b.makeMove(new Move(0,0,17));
		
		MonteCarlo monte = new MonteCarlo();
		monte.setTree(root);
		try {
			Move move = monte.calcMove(b,1);
			fail("Made move without error");
		} catch (Exception e) {

		}
	}
	
}
