package GameOfLive;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;

import java.nio.charset.Charset;

import static GameOfLive.utilities.getRandomNumber;


public class GameOfLive {
	public static void play() {
		final GUIScreen guiScreen = TerminalFacade.createGUIScreen();
		Terminal terminal = TerminalFacade.createTerminal(Charset.forName("UTF8"));
		terminal.enterPrivateMode();
		
		while(terminal.readInput() == null){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		
		Thread.currentThread();
		terminal.setCursorVisible(false);
		
		boolean[][] board = new boolean[terminal.getTerminalSize().getColumns()/2]
										[terminal.getTerminalSize().getRows()];
		
		board = prepareNewBoard(board);
		printBoard(board, terminal);
		
		while (true){
			try { // oddzielenie między iteracjami
				Thread.sleep(30);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			
			Key input = terminal.readInput();
			if (input != null){
				if (input.getKind() == Key.Kind.Escape){
					break;
				}
				
				if (input.getKind() == Key.Kind.Enter){
					board = prepareNewBoard(board);
					printBoard(board, terminal);
					continue;
				}
				
			}
			board = generateNewIteration(board);
			printBoard(board, terminal);
		}
		
		terminal.exitPrivateMode();
		
	}
	
	private static void printBoard(boolean [][] board, Terminal terminal){
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				terminal.moveCursor(2*i, j);
				if (board[i][j]){
//					terminal.putCharacter('■');
					terminal.putCharacter('█');
					terminal.putCharacter('▋');
				} else {
					terminal.putCharacter(' ');
					terminal.putCharacter(' ');
				}
			}
		}
	}
	
	private static boolean[][] prepareNewBoard(boolean [][] board){
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				
				if (getRandomNumber(0, 2) == 0){
					board[i][j] = true;
				}
			}
		}
		
		return board;
	}
	
	private static boolean[][] generateNewIteration(boolean [][] oldBoard){
		boolean[][] newBoard = new boolean[oldBoard.length][oldBoard[0].length];
		
		for (int i = 0; i < oldBoard.length; i++) {
			for (int j = 0; j < oldBoard[i].length; j++) {
				
				int neighbor = getCountNeighbors(i, j, oldBoard);
				
				if (oldBoard[i][j]){ // żywa komórka
					if (neighbor == 2 || neighbor == 3){
						newBoard[i][j] = true; //każda komórka z 2 lub 3 sąsiadami przeżywa
					}
				} else { // martwa komórka
					if (neighbor == 3) { // każda martwa komórka z 3 sąsiadami odżywa
						newBoard[i][j] = true;
					}
				}
			}
		}
		
		return newBoard;
	}
	
	private static int getCountNeighbors(int x, int y, boolean[][] board){
		int count = 0;
		
		int x_max = board.length-1;
		int y_max = board[0].length-1;
		
		if (x > 0 && board[x-1][y]){
			count++;
		} // gora
		
		if (x < x_max && board[x+1][y]){
			count++;
		} // doł
		
		if (y > 0 && board[x][y-1]) {
			count++;
		} // lewo
		
		if (y < y_max && board[x][y+1]) {
			count++;
		} // prawo
		
		if (x > 0 && y > 0 && board[x-1][y-1]) {
			count++;
		} // lewo góra
		
		if (x > 0 && y < y_max && board[x-1][y+1]) {
			count++;
		} // prawo góra
		
		if (x < x_max && y > 0 && board[x+1][y-1]) {
			count++;
		} // lewo dół
		
		if (x < x_max && y < y_max && board[x+1][y+1]) {
			count++;
		} // prawo dół
		
		return count;
	}
}
