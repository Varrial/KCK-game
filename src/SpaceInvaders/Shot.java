package SpaceInvaders;

import com.googlecode.lanterna.terminal.Terminal;


public class Shot {
	private Terminal terminal;
	private int X;
	private int Y;
	private int special_row_1;
	private int special_row_2;
	
	public Shot(Terminal terminal, int X, int Y){
		this.terminal = terminal;
		this.X = X;
		this.Y = Y;
		special_row_1 = terminal.getTerminalSize().getRows()-6;
		special_row_2 = 1;
		place();
		
	}
	
	private void place() {
		terminal.moveCursor(X, Y);
		terminal.putCharacter('|');
	}
	
	public void move() {
		terminal.moveCursor(X, Y);
		if (Y != special_row_1)
			terminal.putCharacter(' ');
		else
			terminal.putCharacter('▔');
		
		if (Y >= 2) {
			Y--;
		}
		
		terminal.moveCursor(X, Y);
		if (Y != special_row_1 && Y != special_row_2)
			terminal.putCharacter('|');
		else
			terminal.putCharacter('T');
	
	}
	
	public void remove() {
		terminal.moveCursor(X, Y);
		if (Y != special_row_2)
			terminal.putCharacter(' ');
		else
			terminal.putCharacter('▔');
	}
	public int getX() {
		return X;
	}
	public int getY() {
		return Y;
	}
	
	
	
}
