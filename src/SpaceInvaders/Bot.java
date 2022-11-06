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
	private int minY = 0;
	private int maxY = 25;
	
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
	
	public void move(){
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
		
		}
		terminal.moveCursor(X, Y);
		terminal.putCharacter('V');
		
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