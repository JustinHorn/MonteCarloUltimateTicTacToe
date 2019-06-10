package monte_carlo;

import static org.junit.jupiter.api.Assertions.fail;

import monte_carlo.board.Board;

public class GenralsTest_Utils {
	public static void failure_if_sth_isDifferent(Board oldBoard, Board newBoard) {
		int[][] b_before = oldBoard.get_cloneOf_Board();
		int[][] b_after = newBoard.get_cloneOf_Board();
		
		for(int i = 0; i < 9;i++) {
			for(int j = 0; j < 9;j++) {
				if(b_before[i][j] != b_after[i][j]) {
					fail("Board has changed");
				} 
			}
		}
		
		boolean[][] avai_before = oldBoard.get_cloneOf_Available();
		boolean[][] avai_after = newBoard.get_cloneOf_Available();
		
		for(int i = 0; i < 3;i++) {
			for(int j = 0; j < 3;j++) {
				if(avai_before[i][j] != avai_after[i][j]) {
					fail("available has changed");
				} 
			}
		}
	}
}
