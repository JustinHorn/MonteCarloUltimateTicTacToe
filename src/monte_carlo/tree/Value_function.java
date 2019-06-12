package monte_carlo.tree;

public interface Value_function {
	double calcValue(long time,int score, int simulationCount);
	String getName();
}
