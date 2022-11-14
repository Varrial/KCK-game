package SpaceInvaders;

import com.googlecode.lanterna.terminal.Terminal;

import static SpaceInvaders.utilities.getRandomNumber;


public class Bot {
	private int X;
	private int Y;
	private boolean direction;
	private Terminal terminal;
	private int minX = 0;
	private int maxX = 99;
	private int minY = 2;
	private int maxY = 24;
	
	public Bot(Terminal terminal){
		X = getRandomNumber(minX, maxX);
		Y = minY;
		direction = true;
		this.terminal = terminal;
		place();
	}
	
	public Bot(Terminal terminal, int X){
		this.X = X;
		Y = minY;
		this.terminal = terminal;
		place();
	}
	
	private void place(){
		terminal.moveCursor(X, Y);
		terminal.putCharacter('V');
	}
	
	public boolean moveAndCheckGameOver(){
		terminal.moveCursor(X, Y);
		terminal.putCharacter(' ');
		
		if (direction){
			if (X < maxX){
				X += 1;
			}
			direction = false;
		} else {
			if (X > minX){
				X -= 1;
			}
			direction = true;
		}
		
		if (Y < maxY){
			Y += 1;
		}
		else {
			return true;
		}
		terminal.moveCursor(X, Y);
		terminal.putCharacter('V');
		return false;
	}
	
	public void remove(){
		terminal.moveCursor(X, Y);
		terminal.putCharacter(' ');
	}
	
	public int getX() {
		return X;
	}
	
	public int getY() {
		return Y;
	}
	
}