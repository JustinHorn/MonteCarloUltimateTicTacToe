package Arena;

public class Match {
	private Bot botX;
	private Bot botO;
	private char result;
	
	
	public Match(Bot botX,Bot botO, char result) {
		this.botX = botX;
		this.botO = botO;
		this.result = result;
	}
	
	@Override
	public String toString() {
		return "X: "+botX.getDuration()+", "+ botX.value_function_getName()+"   O: "+ 
				botO.getDuration()+", "+botO.value_function_getName()+" result: "+result;
	}
}
