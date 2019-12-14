package monte_carlo;

public class Move {
	private final int row;
	private final int col;
	private final int player;
	
	public Move(int row, int col,int player) {
		this.row = row;
		this.col = col;
		this.player = player;
	}
	
	public Move(int[] move) {
		this.row = move[0];
		this.col = move[1];
		this.player = move[2];
	}
	
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public int getPlayer() {
		return player;
	}
	
	@Override
	public String toString( ) {
		return "r: "+row+" c:"+col+" player: "+player;
	}
	
	@Override
	public boolean equals(Object move) {
		if(move instanceof Move) {
			Move m = (Move) move;
			if(m.getPlayer() == player && m.getRow() == row && m.getCol() == col) {
				return true;
			}
		}
		return false;	
	}
	
}
