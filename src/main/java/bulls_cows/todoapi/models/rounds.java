package bulls_cows.todoapi.models;

public class rounds {
	private int roundID;
	private int guess;
	private String ts;
	private int gameID;
	private String BullsandCows ;
	
	
	public String getBullsandCows () {
		return BullsandCows ;
	}

	public void setBullsandCows(String BullsandCows ) {
		this.BullsandCows  = BullsandCows ;
	}
	

	public int getroundId() {
		return roundID;
	}

	public void setroundId(int roundID) {
		this.roundID = roundID;
	}

	public int getguess() {
		return guess;
	}

	public void setguess(int guess) {
		this.guess = guess;
	}

	public String getts() {
		return ts;
	}

	public void setts(String ts) {
		this.ts = ts;
	}

	public int getgameID() {
		return gameID;
	}

	public void setgameID(int gameID) {
		this.gameID = gameID;
	}
}
