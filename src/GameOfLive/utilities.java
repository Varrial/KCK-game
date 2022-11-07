package GameOfLive;

public class utilities {
	public static int getRandomNumber(int min, int max) {
		return (int) ((Math.random() * (max - min)) + min);
	}
}
