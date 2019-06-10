package monte_carlo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.jupiter.api.Test;

import Board.Board;
import monte_carlo.*;
import tree.Node;
import tree.Root;
import tree.TreeUtils;


public class Test_MonteCarlo {
	
	@Test
	void test_MonteCarlo() {
		Board b = new Board();
		Root root = MonteCarlo.calcMove(b,1);
		Move move = TreeUtils.getChild_with_mostSimulations(root).getMove();
		System.out.println(move);
	}
	
	
}
