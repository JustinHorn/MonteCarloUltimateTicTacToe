package Arena;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

import monte_carlo.board.Board;
import monte_carlo.tree.Value_function;

public class Arena {

	Bot[] bots = new Bot[4];
	ArrayList<Match> matches = new ArrayList<Match>();

	public static void main(String[] args) {
		new Arena();

	}

	public Arena() {
		Value_function narrow = new Value_function() {
			public double calcValue(long time,int score,int simulationCount) {
				if(simulationCount == 0) {
					return 0.5*Math.log10(Math.sqrt(System.currentTimeMillis()+1-time));
				} else {
					return ((double) score)/simulationCount+ 0.5*Math.log10(Math.sqrt(System.currentTimeMillis()+1-time)/simulationCount);
				}
			}
			public String getName() {
				return "Narrow";
			}
		};
		
		Value_function wide = new Value_function() { // sucks
			public double calcValue(long time,int score,int simulationCount) {
				if(simulationCount == 0) {
					return 8*Math.log10(Math.sqrt(System.currentTimeMillis()+1-time));
				} else {
					return ((double) score)/simulationCount+ 8*Math.log10(Math.sqrt(System.currentTimeMillis()+1-time)/simulationCount);
				}
			}
			public String getName() {
				return "Wide";
			}
		};
		
		bots[0] = new Bot(1000,narrow);
		bots[1] = new Bot(5000,narrow);

		
		bots[2] = new Bot(1000);
		bots[3] = new Bot(5000);


		makeTheMatches();
		Arrays.asList(bots).sort(new CompareBots());
		writeBotsAndMatches_to_file();
	}

	private void makeTheMatches() {
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 4; j++) {
				if (i != j) {
					matches.add(fight(bots[i], bots[j]));
					matches.add(fight(bots[i], bots[j]));
					matches.add(fight(bots[i], bots[j]));
				}
			}
		}
	}

	private Match fight(Bot botX, Bot botO) {
		Board b = new Board();

		while (b.isOngoing()) {
			b.makeMove(botX.makeMove(b));
			if (b.isOngoing()) {
				b.makeMove(botO.makeMove(b));
			}
		}
		int outcome = b.whoHasWon();
		botX.incrementGames_played();
		botO.incrementGames_played();

		if (outcome == b.X) {
			botX.incrementScore();
			botX.incrementScoreX();
		} else if (outcome == b.O) {
			botO.incrementScore();
			botO.incrementScoreO();
		}
		Match m = new Match(botX, botO, toResult(b));
		return m;

	}

	public char toResult(Board b) {
		if (b.isOngoing()) {
			throw new IllegalArgumentException("Board is still ongoing");
		}
		int outcome = b.whoHasWon();
		if (outcome == b.X) {
			return 'X';
		} else if (outcome == b.O) {
			return 'O';
		} else {
			return '-';
		}
	}

	private void writeBotsAndMatches_to_file() {
		File f = new File("Arenafight2.txt");
		try {
			if (!f.exists()) {
				f.createNewFile();
			}
			BufferedWriter out = new BufferedWriter(new FileWriter(f));
			for(int i = bots.length-1;i  >= 0;i--) {
				out.write(bots[i].toString()+System.lineSeparator());
				out.flush();
			}
			for(int i = 0;i  < matches.size();i++) {
				out.write(matches.get(i).toString()+System.lineSeparator());
				out.flush();
			}
			out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

final class CompareBots implements Comparator<Bot> {

	@Override
	public int compare(Bot bot1, Bot bot2) {
		if(bot1.getScore() < bot2.getScore() ) {
			return -1;
		}
		if(bot1.getScore() > bot2.getScore() ) {
			return 1;
		}
		return 0;
	}
	
}
