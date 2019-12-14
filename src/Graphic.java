

import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;

import javax.swing.*;

import monte_carlo.MonteCarlo;
import monte_carlo.Move;
import monte_carlo.Utils;
import monte_carlo.board.Board;
import monte_carlo.tree.Root;
import monte_carlo.tree.TreeUtils;



public class Graphic extends JFrame {

	
	private static final long serialVersionUID = 1L;
	private PaintPanel[][] paintPanel;
	private Board field;
	private boolean doesComputerPlay = true;
	private boolean PcFightsPc = false;
	private long ai_time_millis = 1000;
	private ArrayList<Move> remember_moves;
	private Root root;
	
	public static void main(String[] args) {
		new Graphic();
	}

	public Graphic() {
		remember_moves = new ArrayList<Move>();
		field = new Board();
		setUpJFrame();
		root = new Root();
		root.setChildren(Utils.getAvailableMoves(field));
		doPCMove();
		while(PcFightsPc) { 
			doPCMove();
		}
		
	}

	private void setUpJFrame() {
		setTitle("ultimate TicTacToe");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setEnabled(true);
		add(create_fieldPanel());
		setBounds(0, 0, 500, 500);
		revalidate();
		repaint();
	}

	private JPanel create_fieldPanel() {
		JPanel panel = new JPanel();

		panel.setLayout(new GridLayout(3, 3));

		JPanel[][] smallerFields = create_smallerFields();
		paintPanel = create_paintPanels();
		
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				smallerFields[i/3][j/3].add(paintPanel[i][j]);
			}
		}

		for (int i = 0; i < 3; i++) {
			for (int j = 0; j < 3; j++) {
				panel.add(smallerFields[i][j]);
			}
		}
		return panel;
	}
	
	
	private JPanel[][] create_smallerFields() {
		JPanel[][] smallerFields = new JPanel[3][3];
		for (int i = 0; i < 3; i++) {
				for (int j = 0; j < 3; j++) {
					smallerFields[i][j] = new JPanel();
					smallerFields[i][j].setLayout(new GridLayout(3, 3));
					smallerFields[i][j].setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
				}
			}
		
		return smallerFields;
	}
	
	private PaintPanel[][] create_paintPanels() {
		PaintPanel[][] paintPanel = new PaintPanel[9][9];
		for (int i = 0; i < 9; i++) {
			for (int j = 0; j < 9; j++) {
				paintPanel[i][j] = new PaintPanel(i,j);
				final int r = i;
				final int c = j;
				paintPanel[i][j].addMouseListener(create_MouseListener_for_PaintPanel(r, c));
			}
		}
		
		return paintPanel;
	}

	private MouseListener create_MouseListener_for_PaintPanel(final int ROW, final int COL) {
		return new MouseListener() {
			final int row = ROW;
			final int col = COL;

			@Override
			public void mouseClicked(MouseEvent e) {
				doMoves_maybe();
			}

			@Override
			public void mouseEntered(MouseEvent e) {}

			@Override
			public void mouseExited(MouseEvent e) {}

			@Override
			public void mousePressed(MouseEvent e) {
				doMoves_maybe();
			}

			@Override
			public void mouseReleased(MouseEvent e) {
				doMoves_maybe();
			}

			private void doMoves_maybe() {
				if (is_notOver_activePanel_and_not_yetTaken()) {
					make_move(new Move(row,col,field.whosTurn()));
					if(computerIsPlayer_and_gameIsNotOver()) {
						doPCMove();
					}
				}
			}

			private boolean is_notOver_activePanel_and_not_yetTaken() {
				return field.isAvailable(row, col) && 
						field.getValueAt(row, col) == Board.EMPTY 
						&& field.isOngoing();
			}
			private boolean computerIsPlayer_and_gameIsNotOver() {
				return doesComputerPlay && field.isOngoing();
			}
			

		};

	}
	
	private void make_move(Move m) {
		field.makeMove(m);
		remember_moves.add(m);
		adaptPanels_to_mBoard(); 
	}
	
	
	private void adaptPanels_to_mBoard() {
		for(int r = 0; r < 9; r++) {
			for(int c = 0;c <9;c++) {
				paintPanel[r][c].setState(field.getValueAt(r, c));
				paintPanel[r][c].setAvailable(field.isAvailable(r, c));
				paintPanel[r][c].revalidate();
				paintPanel[r][c].repaint();
			}
		}
	}
	

	private void doPCMove() {
		/**if(remember_moves.size() > 0) {
			for(int i = 0; i <remember_moves.size();i++) { 
				if(root.hasChild_withMove(remember_moves.get(i))) {
					root = TreeUtils.expandTree(root,remember_moves.get(i));
				}
			}
			remember_moves.removeAll(remember_moves);
		}*/
		root = MonteCarlo.calcMove(field,ai_time_millis); 
		Move move = TreeUtils.getChild_with_mostSimulations(root).getMove();
		make_move(move);
		
		System.out.println(move);
	}
}

