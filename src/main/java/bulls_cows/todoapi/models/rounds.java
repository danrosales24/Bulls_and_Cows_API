package bulls_cows.todoapi.models;

public class rounds {
	private int roundID;
	private int guess;
	private String ts;

	public int getroundId() {
		return roundID;
	}

	public void setroundId(int id) {
		this.roundID = roundID;
	}

	public int getguess() {
		return guess;
	}

	public void setguess(int id) {
		this.guess = guess;
	}

	public String getts() {
		return ts;
	}

	public void setts(int id) {
		this.ts = ts;
	}
}
