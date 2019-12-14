package monte_carlo.board;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.ArrayList;

import org.junit.jupiter.api.Test;

import monte_carlo.*;

public class Test_Board {
	
	private final int X = Board.X;
	private final int O = Board.O;
	private final int EMPTY = Board.EMPTY;

	
	@Test
	void init() {
		Board b = new Board();
		int[][] board = b.get_cloneOf_Board();
		boolean[][] available = b.get_cloneOf_Available();
		for(int i = 0; i < 9;i++) {
			for(int j = 0; j < 9;j++) {
				if(board[i][j] != EMPTY) {
					fail(i+","+j);
				}
			}
		}
		for(int i = 0; i < 3;i++) {
			for(int j = 0; j < 3;j++) {
				if(available[i][j]) {
				
				} else {
					fail(i+","+j);
				}
			}
		}
	}
	
	@Test
	void test_whosTurn() {
		Board b = new Board();
		assertEquals(X,b.whosTurn());
		
		Move any_move_X = new Move(0,0,X);
		b.makeMove(any_move_X);
		assertEquals(O,b.whosTurn());
	}
	
	@Test
	void test_whoHasWon_X_everyWhere() {
		int[][] board = new int[9][9];
		for(int i = 0; i < 9;i++) {
			for(int j = 0; j < 9;j++) {
				board[i][j] = Board.X;
			}
		}
		boolean[][] available = new boolean[3][3];
		for(int i = 0; i < 3;i++) {
			for(int j = 0; j < 3;j++) {
				available[i][j] = true;
			}
		}
		
		Board b = new Board(board,available);
		assertEquals(X,b.whoHasWon());
		assertEquals(X,b.whoHasWon_3x3Field(0, 0));

	}
	
	@Test
	void test_whoHasWon_X_firstLine() {
		int[][] board = new int[9][9];
		for(int i = 0; i < 9;i++) {
			for(int j = 0; j < 9;j++) {
				if(i == 0) {
					board[i][j] = Board.X;
				} else {
					board[i][j] = Board.EMPTY;

				}
			}
		}
		boolean[][] available = new boolean[3][3];
		for(int i = 0; i < 3;i++) {
			for(int j = 0; j < 3;j++) {
				available[i][j] = true;
			}
		}
		
		Board b = new Board(board,available);
		assertEquals(X,b.whoHasWon());
		assertEquals(X,b.whoHasWon_3x3Field(0, 0));
		assertEquals(EMPTY,b.whoHasWon_3x3Field(1, 0));

	}
	@Test
	void test_whoHasWon_X_rightToLeft_Diagonals() {
		int[][] board = new int[9][9];
		for(int i = 0; i < 9;i++) {
			for(int j = 0; j < 9;j++) {
				if(i == j) {
					board[i][j] = Board.X;
				} else {
					board[i][j] = Board.EMPTY;

				}
			}
		}
		boolean[][] available = new boolean[3][3];
		for(int i = 0; i < 3;i++) {
			for(int j = 0; j < 3;j++) {
				available[i][j] = true;
			}
		}
		
		Board b = new Board(board,available);
		assertEquals(X,b.whoHasWon());
		assertEquals(X,b.whoHasWon_3x3Field(0, 0));
		assertEquals(EMPTY,b.whoHasWon_3x3Field(1, 0));

	}
	@Test
	void test_whoHasWon_X_leftToRight_Diagonals() {
		int[][] board = new int[9][9];
		for(int i = 0; i < 9;i++) {
			for(int j = 0; j < 9;j++) {
				if(i == 8-j) {
					board[i][j] = Board.X;
				} else {
					board[i][j] = Board.EMPTY;

				}
			}
		}
		boolean[][] available = new boolean[3][3];
		for(int i = 0; i < 3;i++) {
			for(int j = 0; j < 3;j++) {
				available[i][j] = true;
			}
		}
		
		Board b = new Board(board,available);
		assertEquals(X,b.whoHasWon());
		assertEquals(X,b.whoHasWon_3x3Field(0, 2));
		assertEquals(EMPTY,b.whoHasWon_3x3Field(1, 0));

	}
	
	
	
	@Test
	void makeMove() {
		Board b = new Board();
		Move center_topRight_X = new Move(3,5,X);
		b.makeMove(center_topRight_X);
		int[][] board = b.get_cloneOf_Board();
		boolean[][] available = b.get_cloneOf_Available();
		
		hasMove_beenSaved(board,center_topRight_X);
		
		all_unavailable_except_miniField_by_Move(available,center_topRight_X);
		Move topRight_centerLeft_O = new Move(1,6,O);
		b.makeMove(topRight_centerLeft_O);
		
		all_unavailable_except_miniField_by_Move(b.get_cloneOf_Available(),topRight_centerLeft_O);

		
	}
	
	@Test
	void make_correct_Moves() {
		Board b = new Board();
		Move center_topRight_X = new Move(3,5,X);
		Move topRight_centerLeft_O = new Move(1,6,O);
		Move centerLeft_bottomRight_X = new Move(5,2,X);

		Move[] moves = {center_topRight_X,topRight_centerLeft_O,centerLeft_bottomRight_X};
		b.makeMoves(moves);
		int[][] board = b.get_cloneOf_Board();
		boolean[][] available = b.get_cloneOf_Available();
		
		hasMove_beenSaved(board,center_topRight_X);
		hasMove_beenSaved(board,topRight_centerLeft_O);
		hasMove_beenSaved(board,centerLeft_bottomRight_X);
		
		all_unavailable_except_miniField_by_Move(available,centerLeft_bottomRight_X);
	}
	
	private void all_unavailable_except_miniField_by_Move(boolean[][] available,Move m) {
		for(int i = 0; i < 3;i++) {
			for(int j = 0; j < 3;j++) {
				if((i != m.getRow()%3 || j != m.getCol()%3) && available[i][j] ) {
					fail("available has not been updated properly: " + i+","+j);
				} 
			}
		}
	}
	
	private void hasMove_beenSaved(int[][] board,Move move) {
		if(board[move.getRow()][move.getCol()] != move.getPlayer()) {
			fail("Move "+move.getRow()+":"+ move.getCol()+":"+ codeToChar(move.getPlayer())+" has not been saved");
		}
	}
	
	@Test
	void make_incorrect_Move() {
		Board b = new Board();
		Move center_topRight_X = new Move(3,5,O);
		
		Board oldboard = b.clone();
		try {
			b.makeMove(center_topRight_X);
			fail("Wrong, move has been Made without error");
		} catch(Exception e) {
			
		}
		GenralsTest_Utils.failure_if_sth_isDifferent(oldboard,b);
	}
	
	@Test
	void make_incorrect_Moves() {
		Board b = new Board();
		Board oldBoard = b.clone();
		Move center_topRight_X = new Move(3,5,X);
		Move topRight_centerLeft_X = new Move(1,6,X);
		Move centerLeft_bottomRight_X = new Move(5,2,X);

		Move[] moves = {center_topRight_X,topRight_centerLeft_X,centerLeft_bottomRight_X};
		try {
			b.makeMoves(moves);
			fail("Wrong move has been made without error");
		} catch(Exception e) {
			
		}
		
		GenralsTest_Utils.failure_if_sth_isDifferent(oldBoard,b);
	}
	
	
	
	

	
	private char codeToChar(int number) {
		if(number == Board.O) {
			return 'O';
		} 
		if(number == Board.X){
			return 'X';
		}
		return '-';
	}
	
	
	
	@Test
	void getAvailableMoves( ) {
		Board b = new Board();
		Board oldBoard = b.clone();
		
		assertEquals(81,b.getAvailableMoves().size());
		GenralsTest_Utils.failure_if_sth_isDifferent(oldBoard, b);
		
		Move center_topRight_X = new Move(3,5,X);

		b.makeMove(center_topRight_X);
		ArrayList<Move> moves = b.getAvailableMoves();

		assertEquals(9,moves.size());
		//expected avaialable moves - all in topRight field
		Move m1 = new Move(0,6,O);
		Move m2 = new Move(0,7,O);
		Move m3 = new Move(0,8,O);
		Move m4 = new Move(1,6,O);
		Move m5 = new Move(1,7,O);
		Move m6 = new Move(1,8,O);
		Move m7 = new Move(2,6,O);
		Move m8 = new Move(2,7,O);
		Move m9 = new Move(2,8,O);
		
		assertTrue(m1.equals(moves.get(0)));
		assertTrue(m2.equals(moves.get(1)));
		assertTrue(m3.equals(moves.get(2)));
		assertTrue(m4.equals(moves.get(3)));
		assertTrue(m5.equals(moves.get(4)));
		assertTrue(m6.equals(moves.get(5)));
		assertTrue(m7.equals(moves.get(6)));
		assertTrue(m8.equals(moves.get(7)));
		assertTrue(m9.equals(moves.get(8)));

	}
	
}
