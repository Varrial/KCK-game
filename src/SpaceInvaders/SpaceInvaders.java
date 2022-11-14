package SpaceInvaders;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class SpaceInvaders {
	
	private static int punkty = 1;
	
	public static void play(){
		final GUIScreen guiScreen = TerminalFacade.createGUIScreen();
		Terminal terminal = TerminalFacade.createTerminal(Charset.forName("UTF8"));
		terminal.enterPrivateMode();
		
		terminal.setCursorVisible(false);
		
		final int terminalX = terminal.getTerminalSize().getColumns();
		final int terminalY = terminal.getTerminalSize().getRows();
		punkty = -1;
		
		final int playerMinX = 0;
		final int playerMaxX = terminalX;
		final int playerMinY = terminalY - 5;
		final int playerMaxY = terminalY - 3;
		
		final int maxMovementInterval = 300;
		final int botMovementInterval = 100;
		final int botSpawnInterval = 300;
		final int shotMoveInterval = 10;
		
		prepareScreen(terminal);
		
		int X = playerMaxX / 2;
		int Y = playerMinY + 1;
		
		
		terminal.moveCursor(X, Y);
		terminal.putCharacter('^');
		
		
		Thread.currentThread();
		List<Bot> bots = new ArrayList<>();
		List<Shot> shots = new ArrayList<>();
		
		int movement = 0;
		
		while (true){
			try { // oddzielenie między ruchami
				Thread.sleep(10);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			
			if (movement % botMovementInterval == 0){
				if (moveBotsAndCheckGameOver(bots))
					gameOver(terminal);
			}
			
			if (movement % botSpawnInterval == 0){
				generateNewBots(bots, terminal, 0, terminalX);
			}
			
			if (movement % shotMoveInterval == 0){
				moveShots(shots, bots, terminal);
			}
			
			movement ++;
			if (movement >= maxMovementInterval){
				movement = 0;
			}
			
			Key tmp_key = terminal.readInput();
			if (tmp_key == null) {
				continue;
			}
			
			Key.Kind key = tmp_key.getKind();
			
			terminal.moveCursor(X, Y);
			terminal.putCharacter(' ');
			
			if (key == Key.Kind.ArrowUp && Y > playerMinY){
				Y -= 1;
			}
			else if (key == Key.Kind.ArrowDown && Y < playerMaxY){
				Y += 1;
			}
			else if (key == Key.Kind.ArrowLeft && X > playerMinX){
				X -= 1;
			}
			else if (key == Key.Kind.ArrowRight && X < playerMaxX){
				X += 1;
			}
			else if (key == Key.Kind.NormalKey) {
				shots.add(new Shot(terminal, X, Y-1));
			}
			else if (key == Key.Kind.Escape) {
				terminal.exitPrivateMode();
				menu.menu.menu();
			} else if (key == Key.Kind.Enter) {
				terminal.exitPrivateMode();
				SpaceInvaders.play();
			}
			terminal.moveCursor(X, Y);
			terminal.putCharacter('^');
		}
		
	}
	
	private static boolean moveBotsAndCheckGameOver(List<Bot> bots){
		for (Bot bot : bots) {
			if (bot.moveAndCheckGameOver())
				return true;
		}
		return false;
	}
	
	private static void moveShots(List<Shot> shots, List<Bot> bots, Terminal terminal) {
		Iterator<Shot> shotIterator = shots.listIterator();
		while (shotIterator.hasNext()){
			Shot shot = shotIterator.next();
			
			Iterator<Bot> botIterator = bots.listIterator();
			while (botIterator.hasNext()){
				Bot bot = botIterator.next();
				
//				punkty += 1
				if (shot.getY() == bot.getY() && shot.getX() == bot.getX()){
					shot.remove();
					shot = null;
					shotIterator.remove();
					
					bot.remove();
					bot = null;
					botIterator.remove();
					dodajPunkty(terminal);
					break;
				}
			}
			if (shot != null){
				shot.move();
				if (shot.getY() < 2) {
					shot.remove();
					shot = null;
					shotIterator.remove();
				}
			}
		}
	}
	
	private static void generateNewBots(List<Bot> bots, Terminal terminal, int minX, int maxX){
		final int between = utilities.getRandomNumber(0, 10);
		
		for (int i = minX+2; i < maxX; i+=between) {
			if (utilities.getRandomNumber(0, 5) == 0)
				bots.add(new Bot(terminal, i));
		}
	}
	
	private static void prepareScreen(Terminal terminal) {
		String text = "Punkty: ";
		
		terminal.moveCursor(5, 0);
		for (char c : text.toCharArray()) {
			terminal.putCharacter(c);
		}
		
		dodajPunkty(terminal);
		
		terminal.moveCursor(0,1);
		for (int i = 0; i < terminal.getTerminalSize().getColumns(); i++) {
			terminal.putCharacter('▔');
		}
		
		terminal.moveCursor(0,terminal.getTerminalSize().getRows()-6);
		for (int i = 0; i < terminal.getTerminalSize().getColumns(); i++) {
			terminal.putCharacter('▔');
		}
		
		terminal.moveCursor(0,terminal.getTerminalSize().getRows()-2);
		for (int i = 0; i < terminal.getTerminalSize().getColumns(); i++) {
			terminal.putCharacter('▔');
		}
		
		text = "poruszanie: ↑ ↓    strzał: spacja    wyjście: esc    nowa gra: enter";
		
		terminal.moveCursor(22, terminal.getTerminalSize().getColumns());
		for (char c : text.toCharArray()) {
			terminal.putCharacter(c);
		}
	}
	
	private static void dodajPunkty(Terminal terminal){
		punkty += 1;
		terminal.moveCursor(13, 0);
		
		for (char c : String.valueOf(punkty).toCharArray()) {
			terminal.putCharacter(c);
		}
	}
	
	private static void gameOver(Terminal terminal){
		terminal.setCursorVisible(false);
		terminal.moveCursor(
				(terminal.getTerminalSize().getColumns()/2)-5,
				(terminal.getTerminalSize().getRows()/2)-1
				);
		for (int i = 0; i < 11; i++) {
			terminal.putCharacter(' ');
		}
		
		terminal.moveCursor(
				(terminal.getTerminalSize().getColumns()/2)-5,
				(terminal.getTerminalSize().getRows()/2)
		);
		
		String text = " GAME OVER ";
		for (char c : text.toCharArray()) {
			terminal.putCharacter(c);
		}
		
		
		terminal.moveCursor(
				(terminal.getTerminalSize().getColumns()/2)-5,
				(terminal.getTerminalSize().getRows()/2)+1
		);
		for (int i = 0; i < 11; i++) {
			terminal.putCharacter(' ');
		}
		
		while (true){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			Key input = terminal.readInput();
			if (input != null){
				continue;
			}
			if (input.getKind() == Key.Kind.Enter){
				SpaceInvaders.play();
			} else if (input.getKind() == Key.Kind.Escape) {
				menu.menu.menu();
			}
		}
	}
}

