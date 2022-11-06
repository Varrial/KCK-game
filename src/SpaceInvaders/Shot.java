package SpaceInvaders;

import com.googlecode.lanterna.terminal.Terminal;


public class Shot {
	private Terminal terminal;
	private int X;
	private int Y;
	
	public Shot(Terminal terminal, int X, int Y){
		this.terminal = terminal;
		this.X = X;
		this.Y = Y;
		place();
	}
	
	private void place() {
		terminal.moveCursor(X, Y);
		terminal.putCharacter('|');
	}
	
	public void move() {
		terminal.moveCursor(X, Y);
		terminal.putCharacter(' ');
		
		if (Y >= 0) {
			Y--;
		}
		
		terminal.moveCursor(X, Y);
		terminal.putCharacter('|');
	}
	
	public void remove() {
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
