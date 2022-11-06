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
	public static void play(){
		final GUIScreen guiScreen = TerminalFacade.createGUIScreen();
		Terminal terminal = TerminalFacade.createTerminal(Charset.forName("UTF8"));
		terminal.enterPrivateMode();
		
		final int terminalX = terminal.getTerminalSize().getColumns();
		final int terminalY = terminal.getTerminalSize().getRows();
		
		final int playerMinX = 0;
		final int playerMaxX = terminalX;
		final int playerMinY = terminalY - 3;
		final int playerMaxY = terminalY;
		
		final int maxMovementInterval = 300;
		final int botMovementInterval = 100;
		final int botSpawnInterval = 300;
		final int shotMoveInterval = 10;
		
		
		
		int X = playerMaxX / 2;
		int Y = playerMinY + 1;
		
		
		terminal.moveCursor(X, Y);
		terminal.putCharacter('^');
		
		
		Thread.currentThread();
		terminal.setCursorVisible(false);
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
				moveBots(bots);
			}
			
			if (movement % botSpawnInterval == 0){
				generateNewBots(bots, terminal, 0, terminalX);
			}
			
			if (movement % shotMoveInterval == 0){
				moveShots(shots, bots);
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
			if (key == Key.Kind.ArrowDown && Y < playerMaxY){
				Y += 1;
			}
			if (key == Key.Kind.ArrowLeft && X > playerMinX){
				X -= 1;
			}
			if (key == Key.Kind.ArrowRight && X < playerMaxX){
				X += 1;
			}
			if (key == Key.Kind.NormalKey) {
				shots.add(new Shot(terminal, X, Y-1));
			}
			terminal.moveCursor(X, Y);
			terminal.putCharacter('^');
		}
		
	}
	
	private static void moveBots(List<Bot> bots){
		for (Bot bot : bots) {
			bot.move();
		}
	}
	
	private static void moveShots(List<Shot> shots, List<Bot> bots) {
		Iterator<Shot> shotIterator = shots.listIterator();
		while (shotIterator.hasNext()){
			Shot shot = shotIterator.next();
			
			Iterator<Bot> botIterator = bots.listIterator();
			while (botIterator.hasNext()){
				Bot bot = botIterator.next();
				
				if (shot.getY() == bot.getY() && shot.getX() == bot.getX()){
					shot.remove();
					shot = null;
					shotIterator.remove();
					
					bot.remove();
					bot = null;
					botIterator.remove();
					break;
				}
			}
			if (shot != null){
				shot.move();
				if (shot.getY() < 0) {
					shot.remove();
					shot = null;
					shotIterator.remove();
				}
			}
		}
	}
	
	private static void generateNewBots(List<Bot> bots, Terminal terminal ,int minX, int maxX){
		final int between = 10;
		
		for (int i = minX; i <= maxX; i++) {
			if (i % between == 5)
				bots.add(new Bot(terminal, i));
		}
	}
}

