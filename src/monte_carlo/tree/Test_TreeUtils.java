package monte_carlo.tree;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Collections;

import org.junit.jupiter.api.Test;

import monte_carlo.MonteCarlo;
import monte_carlo.Move;
import monte_carlo.Utils;
import monte_carlo.board.Board;

public class Test_TreeUtils {
	
	@Test
	void test_getParentsFromNode() {
		Root root = new Root();
		Board b = new Board();
		root.setChildren(Utils.getAvailableMoves(b));
		Node random_child = root.getRandomChild();
		ArrayList<Root> parents = TreeUtils.getParentsFromNode(random_child);
		
		assertEquals(root,parents.get(0));
		assertEquals(1,parents.size());
	}
	
	@Test
	void test_getParentsFromNode_exceptRoot() {
		Root root = new Root();
		Board b = new Board();
		
		root.setChildren(Utils.getAvailableMoves(b));
		Node random_child = root.getRandomChild();
		b.makeMove(random_child.getMove());
		random_child.setChildren(Utils.getAvailableMoves(b));
		Node childOf_randomChild = random_child.getRandomChild();
		
		
		ArrayList<Node> parent = TreeUtils.getParentsFromNode_exceptRoot(childOf_randomChild);
		
		assertEquals(random_child,parent.get(0));
		assertEquals(1,parent.size());
	}
	
	@Test
	void test_getMovesFromParents() {
		Root root = new Root();
		Board b = new Board();
		
		root.setChildren(Utils.getAvailableMoves(b));
		Node child1 = root.getRandomChild();
		b.makeMove(child1.getMove());
		child1.setChildren(Utils.getAvailableMoves(b));
		Node child2 = child1.getRandomChild();
		b.makeMove(child2.getMove());
		child2.setChildren(Utils.getAvailableMoves(b));
		Node child3 = child2.getRandomChild();
		
		Move[] moves = TreeUtils.getMoves_from_nodeParents_asArray(child3);
		
		assertEquals(child2.getMove(),moves[0]);
		assertEquals(child1.getMove(),moves[1]);
		
		ArrayList<Move> moves2 = TreeUtils.getMoves_from_nodeParents_asList(child3);
		Collections.reverse(moves2);
		
		assertEquals(child2.getMove(),moves2.get(1));
		assertEquals(child1.getMove(),moves2.get(0));
	}
	

	@Test
	void test_getBestChild_and_mostSimulations() {
		Board b = new Board();
		Root root = new Root();
		
		root.setChildren(Utils.getAvailableMoves(b));
		Node random_child = root.getRandomChild();
		random_child.changeScore_and_simulationCount_of_MeAndParent(b.X);

		assertEquals(random_child,TreeUtils.getChild_with_highestValue(root));
		assertEquals(random_child,TreeUtils.getChild_with_mostSimulations(root));
		
		random_child.changeScore_and_simulationCount_of_MeAndParent(b.O);
		assertTrue(random_child != TreeUtils.getChild_with_highestValue(root));
		assertEquals(random_child,TreeUtils.getChild_with_mostSimulations(root));
		
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

		root = TreeUtils.node_to_Root(child1);
		
		assertEquals(true,root.hasChild_withMove(lvl2));

	}
	
	private Root setUptree_with_hightOfTwo(Move m1,Move m2) {
		Root root = new Root();
		root.setChildren(new Move[]{m1});
		Node randomChild = root.getRandomChild();
		randomChild.setChildren(new Move[] {m2});
		return root;
	}
	
	@Test
	void test_expandTree() {
		Move lvl1 = new Move(1,2,3);
		Move lvl2 = new Move(1,2,4);
		Root root = setUptree_with_hightOfTwo(lvl1,lvl2);
		
		assertEquals(true,root.hasChild_withMove(lvl1));

		root = TreeUtils.expandTree(root,lvl1);
		assertEquals(true,root.hasChild_withMove(lvl2));
	}
	
	@Test
	void test_incorrect_TreeDoesNotMacthBoard() {
		Board b = new Board();
		Root root = new Root();
		root.setChildren(Utils.getAvailableMoves(b));
		b.makeMove(new Move(0,0,17));
		
		try {
			TreeUtils.doesTreeMatchBoard(root, b);
			fail("Made move without error");
		} catch (Exception e) {

		}
	}
}
