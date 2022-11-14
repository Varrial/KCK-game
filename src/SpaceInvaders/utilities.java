package SpaceInvaders;

import java.util.Random;


public class utilities {
	public static int getRandomNumber(int min, int max) {
		return (int) ((Math.random() * (max - min)) + min);
	}
	
	public static boolean getRandomBool(){
		return new Random().nextBoolean();
	}
}
