package Graphics;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import Board.Board;


/**
 * 
 * @author Justin Horn
 * Paints the shit
 *
 */
public class PaintPanel extends JPanel {

	private int state;
	private boolean available = true;
	private final int r;
	private final int c;
	
	public PaintPanel(int r, int c) {
		super();
		this.r = r;
		this.c = c;
		state = Board.EMPTY;
		this.setBackground(Color.white);
		setBorder(BorderFactory.createLineBorder(Color.black));
	}

	/**
	 * Sets the state of the TicTacToe field:<br>
	 * X<br>
	 * O<br>
	 * - 
	 * @param x
	 * @return
	 */
	public int setState(int x) {
		if(state != Board.X && state != Board.O) {
			state = x;
		}
		return x;
	}
	
	public void setAvailable(boolean a) {
		available = a;
	}
	
	public void reset() {
		state = Board.EMPTY;
	}

	public int getState() {
		return state;
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
	    g2.setStroke(new BasicStroke(3));
		int w = this.getWidth();
		int h = this.getHeight();
		if (state == Board.O) {
			g2.setColor(Color.red);
			g2.drawOval(w/20, h/20, 9*w/10, 9*h/10);
		} else if (state == Board.X) {
			g2.setColor(Color.blue);
			g2.drawLine(w/20, h/20, 9*w/10, 9*h/10);
			g2.drawLine(9*w/10, h/20, w/20, 9*h/10);
		} else if (available) {
			g2.setColor(Color.red);
			g2.drawRect(w/20, h/20, 9*w/10, 9*h/10);
			g2.drawString(r+" , "+c, w/5, h/5);
		}
	}

}
