package GameOfLive;

import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.gui.GUIScreen;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.terminal.Terminal;

import java.nio.charset.Charset;

import static GameOfLive.utilities.getRandomNumber;


public class GameOfLife {
	
	private static boolean losowy_start;
	
	public static void play() {
		final GUIScreen guiScreen = TerminalFacade.createGUIScreen();
		Terminal terminal = TerminalFacade.createTerminal(Charset.forName("UTF8"));
		terminal.enterPrivateMode();
		terminal.setCursorVisible(false);
		losowy_start = true;
		
		printStartScreen(terminal, 32, 3 , "Teraz możesz ustawić wielkość okna");
		printStartScreen(terminal, 25, 7 , "Sterowanie:");
		printStartScreen(terminal, 30, 9 , "Rozpocznij nową grę:                      Enter");
		printStartScreen(terminal, 30, 11, "Powrót do menu:                           Esc");
		printStartScreen(terminal, 30, 13, "przełączenie losowe/manualne rozpoczęcie: Tab (domyślnie losowe)");
		printStartScreen(terminal, 30, 15, "Poruszanie podczas ustawiania:            Strzałki");
		printStartScreen(terminal, 30, 17, "postawienie bloku:                        Spacja");
		printStartScreen(terminal, 25, 21, "Zasady gry:");
		printStartScreen(terminal, 30, 23, "1. Komórka przeżywa jeśli jest ma 2 lub 3 sąsiadów");
		printStartScreen(terminal, 30, 25, "2. Komórka odżywa jeśli ma 3 sąsiadów");
		printStartScreen(terminal, 30, 27, "3. Komórka umiera lub pozostaje martwa w przeciwnych przypadkach");
		
		Key input;
		boolean[][] board = new boolean[terminal.getTerminalSize().getColumns()/2]
				[terminal.getTerminalSize().getRows()];
		
		
		while(true){
			input = terminal.readInput();
			if (input != null){
				System.out.println(input.getKind());
				if (input.getKind() == Key.Kind.Tab){
					losowy_start = !losowy_start;
				} else if (input.getKind() == Key.Kind.Enter) {
					if (losowy_start){
						board = prepareRandomBoard(board);
					} else {
						board = prepareBoard(terminal, board);
					}
					break;
				} else if (input.getKind() == Key.Kind.Escape) {
					terminal.exitPrivateMode();
					menu.menu.menu();
				}
			}
			
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		
		Thread.currentThread();
		
		printBoard(board, terminal);
		
		while (true){
			try { // oddzielenie między iteracjami
				Thread.sleep(30);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			
			input = terminal.readInput();
			if (input != null){
				if (input.getKind() == Key.Kind.Escape){
					terminal.exitPrivateMode();
					menu.menu.menu();
				}
				
				if (input.getKind() == Key.Kind.Enter){
					if (losowy_start)
						board = prepareRandomBoard(board);
					else
						board = prepareBoard(terminal, board);
					
					printBoard(board, terminal);
					continue;
				}
				
				if (input.getKind() == Key.Kind.Tab) {
					losowy_start = !losowy_start;
				}
				
			}
			board = generateNewIteration(board);
			printBoard(board, terminal);
		}
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
	
	private static boolean[][] prepareRandomBoard(boolean [][] board){
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
	
	private static void printStartScreen(Terminal terminal, int x, int y, String text){
		terminal.moveCursor(x, y);
		for (char c : text.toCharArray()) {
			terminal.putCharacter(c);
		}
	}
	
	private static boolean[][] prepareBoard(Terminal terminal, boolean[][] board){
		terminal.clearScreen();
		board = new boolean[board.length][board[0].length];
		
		int x = board.length/2;
		int y = board[0].length/2;
		terminal.moveCursor(2*x, y);
		terminal.putCharacter('▓');
		terminal.putCharacter('▓');
		
		while (true){
			try { // oddzielenie między iteracjami
				Thread.sleep(100);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			Key input = terminal.readInput();
			
			if (input == null){
				continue;
			}
			
			if (input.getKind() == Key.Kind.Enter){
				break;
			}
			
			if (input.getKind() == Key.Kind.NormalKey){
				board[x][y] = !board[x][y];
			}
			
			if (input.getKind() == Key.Kind.ArrowLeft){
				if (x <= 0){
					continue;
				}
				x--;
				terminal.moveCursor(2*x, y);
				terminal.putCharacter('▓');
				terminal.putCharacter('▓');
				
				if (board[x+1][y]){
					terminal.putCharacter('█');
					terminal.putCharacter('▋');
				} else {
					terminal.putCharacter(' ');
					terminal.putCharacter(' ');
				}
			}
			
			if (input.getKind() == Key.Kind.ArrowRight){
				if (x >= board.length-1){
					continue;
				}
				
				terminal.moveCursor(2*x, y);
				
				if (board[x][y]){
					terminal.putCharacter('█');
					terminal.putCharacter('▋');
					
				} else {
					terminal.putCharacter(' ');
					terminal.putCharacter(' ');
				}
				
				terminal.putCharacter('▓');
				terminal.putCharacter('▓');
				
				x++;
			}
			
			if (input.getKind() == Key.Kind.ArrowUp){
				if (y <= 0){
					continue;
				}
				
				terminal.moveCursor(2*x, y);
				
				if (board[x][y]){
					terminal.putCharacter('█');
					terminal.putCharacter('▋');
				} else {
					terminal.putCharacter(' ');
					terminal.putCharacter(' ');
				}
				
				y--;
				terminal.moveCursor(2*x, y);
				terminal.putCharacter('▓');
				terminal.putCharacter('▓');
			}
			
			if (input.getKind() == Key.Kind.ArrowDown){
				if (y >= board[0].length){
					continue;
				}
				
				terminal.moveCursor(2*x, y);
				
				if (board[x][y]){
					terminal.putCharacter('█');
					terminal.putCharacter('▋');
				} else {
					terminal.putCharacter(' ');
					terminal.putCharacter(' ');
				}
				
				y++;
				terminal.moveCursor(2*x, y);
				terminal.putCharacter('▓');
				terminal.putCharacter('▓');
			}
			
			if (input.getKind() == Key.Kind.Escape){
				terminal.exitPrivateMode();
				menu.menu.menu();
			}
			
			if (input.getKind() == Key.Kind.Tab){
				losowy_start = !losowy_start;
			}
		}
		
		return board;
	}
}
