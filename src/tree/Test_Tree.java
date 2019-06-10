package tree;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import Board.Board;
import monte_carlo.*;


public class Test_Tree {
	
	@Test
	void newTree() {
		Root root = new Root();
	}
	

	@Test
	void test_addChildren() {
		Board b = new Board();
		Root root = new Root();
		
		Node random_child = root.setChildren_and_getRandomChild(Utils.getAvailableMoves(b));
		assertEquals(root,random_child.getParent());
	}
	
	@Test
	void test_score() {
		Board b = new Board();
		Root root = new Root();
		
		Node random_child = root.setChildren_and_getRandomChild(Utils.getAvailableMoves(b));
		b.makeMove(random_child.getMove());
		
		Node childOf_randomChild = random_child.setChildren_and_getRandomChild(Utils.getAvailableMoves(b));
		
		childOf_randomChild.changeScore_and_simulationCount_of_MeAndParent(b.O);
		
		assertEquals(-1,random_child.getScore());
		assertEquals(1,childOf_randomChild.getScore());
	}
	
	@Test
	void test_getBestChild_and_mostSimulations() {
		Board b = new Board();
		Root root = new Root();
		
		Node random_child = root.setChildren_and_getRandomChild(Utils.getAvailableMoves(b));
		random_child.changeScore_and_simulationCount_of_MeAndParent(b.X);

		assertEquals(random_child,root.getChild_with_highestValue());
		assertEquals(random_child,root.getChild_with_mostSimulations());
		
		random_child.changeScore_and_simulationCount_of_MeAndParent(b.O);
		assertTrue(random_child != root.getChild_with_highestValue());
		assertEquals(random_child,root.getChild_with_mostSimulations());
		
	}
	

	@Test
	void test_Node_thistoTree() {
		Move lvl1 = new Move(1,2,3);
		Move lvl2 = new Move(1,2,4);
		Root root = setUptree_with_hightOfTwo(lvl1,lvl2);
		
		assertEquals(true,root.hasChild_withMove(lvl1));
		Node child1 = root.getChild_withMove(lvl1);
		
		
		assertEquals(lvl1,child1.getMove());
		assertEquals(true,child1.hasChild_withMove(lvl2));

		root = child1.this_to_Root();
		
		assertEquals(true,root.hasChild_withMove(lvl2));

	}
	
	private Root setUptree_with_hightOfTwo(Move m1,Move m2) {
		Root root = new Root();
		Node randomChild = root.setChildren_and_getRandomChild(new Move[]{m1});
		randomChild.setChildren_and_getRandomChild(new Move[] {m2});
		return root;
	}
	
	@Test
	void test_Node_setChildren() {
		Move lvl1 = new Move(1,2,3);
		Root root = new Root();
		Root root2 = new Root();
		Node m1 = new Node(root2,lvl1);

		root.setChildren(new Node[] {m1});
		assertEquals(root,m1.getParent());
		assertEquals(m1,root.getChild_withMove(m1.getMove()));
	}
	
	@Test
	void test_Node_setParent() {
		Move lvl1 = new Move(1,2,3);
		Root root = new Root();
		Root root2 = new Root();
		Node m1 = new Node(root2,lvl1);

		m1.setParent(root);
		assertEquals(root,m1.getParent());
	}
	
	
	
}
