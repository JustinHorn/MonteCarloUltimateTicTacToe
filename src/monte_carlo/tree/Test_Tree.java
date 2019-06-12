package monte_carlo.tree;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.jupiter.api.Test;

import monte_carlo.*;
import monte_carlo.board.Board;


public class Test_Tree {
	
	@Test
	void newTree() {
		Root root = new Root();
	}
	

	@Test
	void test_addChildren() {
		Board b = new Board();
		Root root = new Root();
		
		root.setChildren(Utils.getAvailableMoves(b));
		Node random_child = root.getRandomChild();
		assertEquals(root,random_child.getParent());
	}
	
	@Test
	void test_score() {
		Board b = new Board();
		Root root = new Root();
		
		root.setChildren(Utils.getAvailableMoves(b));
		Node random_child = root.getRandomChild();
		b.makeMove(random_child.getMove());
		
		random_child.setChildren(Utils.getAvailableMoves(b));
		Node childOf_randomChild = random_child.getRandomChild();
		
		childOf_randomChild.changeScore_and_simulationCount_of_MeAndParent(b.O);
		
		assertEquals(-1,random_child.getScore());
		assertEquals(1,childOf_randomChild.getScore());
	}
	
	
	@Test
	void test_Node_setChildren() {
		Move lvl1 = new Move(1,2,3);
		Root root = new Root();
		Root root2 = new Root();
		Node m1 = new Node(root2,lvl1, (x,y,z)->(1));

		root.setChildren(new Node[] {m1});
		assertEquals(root,m1.getParent());
		assertEquals(m1,root.getChild_withMove(m1.getMove()));
	}
	
	@Test
	void test_Node_setParent() {
		Move lvl1 = new Move(1,2,3);
		Root root = new Root();
		Root root2 = new Root();
		Node m1 = new Node(root2,lvl1,(x,y,z)->x);

		m1.setParent(root);
		assertEquals(root,m1.getParent());
	}
	
	
	
}
