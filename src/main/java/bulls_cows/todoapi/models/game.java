package bulls_cows.todoapi.models;

public class game {

	private int gameID;
	private int answer;
	private boolean finished;

	public int getgameId() {
		return gameID;
	}

	public void setgameId(int id) {
		this.gameID = gameID;
	}

	public int getAnswer() {
		return answer;
	}

	public void setanswer(int answer) {
		this.answer = answer;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public int random(int x) {
		int random = (int) (Math.random() * 9000) + 1000;
		return random;
	}

}