public class GameException extends Throwable{
	String error;

	public GameException(String error) {
		this.error = error;
	}

	@Override
	public String toString() {
		return error;
	}
}
