package monte_carlo.board;

import java.util.ArrayList;

import monte_carlo.Move;

public class Board {

	private int[][] board;
	private boolean[][] available;

	public static final int X = 17;
	public static final int O = 3;
	public static final int EMPTY = 0;
	public static final int	ONGOING = EMPTY;
	
	private static final int OVER = -1;

	public Board(int[][] board, boolean[][] available) {
		this.board = board;
		this.available = available;
	}
	
	public Board() {
		assign_startingValues_to_arrays();
	}
	
	public void assign_startingValues_to_arrays() {
		board = new int[9][9];
		available = new boolean[3][3];
		for (int i = 0; i < 81; i++) {
			board[i / 9][i % 9] = EMPTY;
			available[i / 27][i % 3] = true;
		}
	}
	
	public int getValueAt(int r, int c) {
		return board[r][c];
	}

	public boolean isAvailable(int r, int c) {
		return available[r / 3][c / 3];
	}
	
	public boolean isOngoing() {
		return whoHasWon() == ONGOING;
	}
	
	public boolean isMoveLegal(Move move) {
		return isMoveLegal(move, whosTurn());
	}
	
	public void makeMove(Move move) {
		makeMove(move, whosTurn());
	}

	public void makeMoves(Move[] moves) {
		boolean[][] savety_copy_of_available = get_cloneOf_Available();
		int[][] savety_copy_of_board = get_cloneOf_Board();

		try {
			do_the_moves(moves);
		} catch (Exception e) {
			board = savety_copy_of_board;
			available = savety_copy_of_available;
			throw new IllegalArgumentException(e.fillInStackTrace());
		}
	}
	
	private void do_the_moves(Move[] moves) {
		int whosTurn = whosTurn();
		for (int i = 0; i < moves.length; i++) {
			makeMove(moves[i], whosTurn);
			whosTurn = (whosTurn == X ? O : X);
		}
	}


	private void makeMove(Move move, int whosTurn) {
		if (isMoveLegal(move, whosTurn)) {
			board[move.getRow()][move.getCol()] = move.getPlayer();
			calcNextAvailable(move);
		} else {
			throw new IllegalArgumentException("Illegal Move: " + move);
		}
	}

	private boolean isMoveLegal(Move move, int whosTurn) {
		return board[move.getRow()][move.getCol()] == EMPTY && move.getPlayer() == whosTurn
				&& available[move.getRow() / 3][move.getCol() / 3];
	}

	private void calcNextAvailable(Move m) {
		makeAll_fields_unavailable();
		if (isOngoing()) {
			if (whoHasWon_3x3Field(m.getRow() % 3, m.getCol() % 3) == ONGOING) {
				available[m.getRow() % 3][m.getCol() % 3] = true;
			} else {
				makeAll_ongoingFields_available();
			}
		}
	}
	
	private void makeAll_fields_unavailable() {
		for (int r = 0; r < available.length; r++) {
			for (int c = 0; c < available[r].length; c++) {
				available[r][c] = false;
			}
		}
	}
	
	private void makeAll_ongoingFields_available() {
		for (int r = 0; r < available.length; r++) {
			for (int c = 0; c < available[r].length; c++) {
				if (whoHasWon_3x3Field(r, c) == ONGOING) {
					available[r][c] = true;
				}
			}
		}
	}

	public int whosTurn() {
		int countX = 0;
		int countO = 0;
		for (int r = 0; r < 9; r++) {
			for (int c = 0; c < 9; c++) {
				if (board[r][c] == O) {
					countO++;
				} else if (board[r][c] == X) {
					countX++;
				}
			}
		}
		return countX <= countO ? X : O;
	}

	public int whoHasWon() {
		int[][] stati_of_3x3Fields = new int[3][3];
		for (int r = 0; r < 3; r++) {
			for (int c = 0; c < 3; c++) {
				stati_of_3x3Fields[r][c] = whoHasWon_3x3Field(r, c);
			}
		}
		return whoHasWon_3x3Field(stati_of_3x3Fields);
	}

	public int whoHasWon_3x3Field(int r, int c) {
		return whoHasWon_3x3Field(get3x3Field(r, c));
	}
	
	/**<pre>
	 * Example:
	 * If you want the TicTacToe-Field at the top right:
	 * row = 0 col = 2
	 * @param row
	 * @param col
	 * @return
	 */
	public int[][] get3x3Field(int row, int col) {
		int[][] choosen_smallField = new int[3][3];
		for (int r = 0; r <  3; r++) {
			for (int c = 0; c <  3; c++) {
				choosen_smallField[r][c] = board[r+ 3 * row][c + 3* col];
			}
		}
		return choosen_smallField;
	}

	private int whoHasWon_3x3Field(int[][] field) {
		for (int r = 0; r < 3; r++) { // rows
			if (field[r][0] + field[r][1] + field[r][2] == X * 3 || field[r][0] + field[r][1] + field[r][2] == O * 3) {
				return (field[r][0] + field[r][1] + field[r][2]) / 3; //
			}
		}
		for (int r = 0; r < 3; r++) { // cols
			if (field[0][r] + field[1][r] + field[2][r] == X * 3 || field[0][r] + field[1][r] + field[2][r] == O * 3) {
				return (field[0][r] + field[1][r] + field[2][r]) / 3; //
			}
		}

		if (field[0][0] + field[1][1] + field[2][2] == X * 3 || field[0][0] + field[1][1] + field[2][2] == O * 3) { // diagonals
			return (field[0][0] + field[1][1] + field[2][2]) / 3;
		}
		if (field[2][0] + field[1][1] + field[0][2] == X * 3 || field[2][0] + field[1][1] + field[0][2] == O * 3) { // diagonals
			return (field[2][0] + field[1][1] + field[0][2]) / 3;
		}
		if (does_an_emptyField_exist(field)) {
			return ONGOING;
		}
		return OVER;
	}

	private boolean does_an_emptyField_exist(int[][] field) {
		for (int r = 0; r < field.length; r++) {
			for (int c = 0; c < field[r].length; c++) {
				if (field[r][c] == EMPTY) {
					return true;
				}
			}
		}
		return false;
	}

	public ArrayList<Move> getAvailableMoves() {
		ArrayList<Move> availAbleMoves = new ArrayList<Move>();
		int whosTurn = whosTurn();
		for (int r = 0; r < 9; r++) {
			for (int c = 0; c < 9; c++) {
				if (available[r / 3][c / 3] && board[r][c] == EMPTY) {
					availAbleMoves.add(new Move(r, c, whosTurn));
				}
			}
		}
		return availAbleMoves;
	}

	public Board clone() {
		available.clone();
		return new Board(get_cloneOf_Board(), get_cloneOf_Available());
	}

	public int[][] get_cloneOf_Board() {
		int[][] copy_board = new int[9][9];
		for (int i = 0; i < 81; i++) {
			copy_board[i / 9][i % 9] = board[i / 9][i % 9];
		}
		return copy_board;
	}

	public boolean[][] get_cloneOf_Available() {
		boolean[][] copy_available = new boolean[3][3];
		for (int i = 0; i < 81; i++) {
			copy_available[i / 27][i % 3] = available[i / 27][i % 3];
		}

		return copy_available;
	}

}
