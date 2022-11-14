package menu;

import GameOfLive.GameOfLife;
import SpaceInvaders.SpaceInvaders;
import com.googlecode.lanterna.TerminalFacade;
import com.googlecode.lanterna.input.Key;
import com.googlecode.lanterna.screen.Screen;
import com.googlecode.lanterna.screen.ScreenCharacterStyle;
import com.googlecode.lanterna.terminal.Terminal;

public class menu {
	public static void menu() {
		Screen screen = TerminalFacade.createScreen();
		screen.startScreen();
		
		String title = "Wybierz opcje";
		
		String[] option = {"Space Invaders",
				"Game of Life",
				"Skalowanie",
				"Wyjście"
		};
		
		String info = "poruszanie: ↑ ↓    wybór: enter";
		
		int selected = 0;
		
		int columns = screen.getTerminalSize().getColumns();
		int[] position = preparePositions(screen.getTerminalSize().getRows());
		printMenuOptions(screen, title, option, info, columns, position, true);
		screen.setCursorPosition(null);
		screen.refresh();
		
		Thread.currentThread();
		
		while(true){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			Key input = screen.readInput();
			if(input == null){
				continue;
			}
			
//			usunięcie tła z wybranego
			if(input.getKind() == Key.Kind.ArrowDown || input.getKind() == Key.Kind.ArrowUp){
				if (selected == 0){
					screen.putString((columns/2) - (option[0].length()/2), position[1], option[0], Terminal.Color.CYAN, Terminal.Color.BLACK, ScreenCharacterStyle.Bold);
				} else if (selected == 1) {
					screen.putString((columns/2) - (option[1].length()/2), position[2], option[1], Terminal.Color.CYAN, Terminal.Color.BLACK, ScreenCharacterStyle.Bold);
				} else if (selected == 2) {
					screen.putString((columns/2) - (option[2].length()/2), position[3], option[2], Terminal.Color.CYAN, Terminal.Color.BLACK, ScreenCharacterStyle.Bold);
				} else if (selected == 3) {
					screen.putString((columns/2) - (option[3].length()/2), position[4], option[3], Terminal.Color.CYAN, Terminal.Color.BLACK, ScreenCharacterStyle.Bold);
				}
			}
			
//			wybranie opcji z przeskakiwaniem
			if(input.getKind() == Key.Kind.ArrowDown){
				if (selected == option.length-1){
					selected = 0;
				} else {
					selected += 1;
				}
			}
			
			
			if(input.getKind() == Key.Kind.ArrowUp){
				if (selected == 0){
					selected = option.length-1;
				} else {
					selected -= 1;
				}
			}
			
			
//			dodanie tła
			if(input.getKind() == Key.Kind.ArrowDown || input.getKind() == Key.Kind.ArrowUp) {
				if (selected == 0) {
					screen.putString(
							(columns / 2) - (option[0].length() / 2),
							position[1],
							option[0],
							Terminal.Color.CYAN,
							Terminal.Color.BLUE,
							ScreenCharacterStyle.Bold);
				} else if (selected == 1) {
					screen.putString(
							(columns / 2) - (option[1].length() / 2),
							position[2],
							option[1],
							Terminal.Color.CYAN,
							Terminal.Color.BLUE,
							ScreenCharacterStyle.Bold);
				} else if (selected == 2) {
					screen.putString((columns/2) - (option[2].length()/2), position[3], option[2], Terminal.Color.CYAN, Terminal.Color.BLUE, ScreenCharacterStyle.Bold);
				} else if (selected == 3) {
					screen.putString((columns/2) - (option[3].length()/2), position[4], option[3], Terminal.Color.CYAN, Terminal.Color.BLUE, ScreenCharacterStyle.Bold);
				}
			}
			
			
			if(input.getKind() == Key.Kind.Enter){
				if (selected != 2){
					break;
				}
				
				screen.updateScreenSize();
				columns = screen.getTerminalSize().getColumns();
				position = preparePositions(screen.getTerminalSize().getRows());
				printMenuOptions(screen, title, option, info, columns, position, false);
				
			}
			screen.refresh();
		}
		
		screen.stopScreen();
		if (selected == 0){
			SpaceInvaders.play();
		}
		if (selected == 1){
			GameOfLife.play();
		}
		
	}
	
	private static int[] preparePositions (int rows){
		int[] position = new int[6];
		int tmp = 0;
		
		
		
		if (rows > 30){
			tmp = (rows/2) - (10/2);
			position[1] = tmp;
			position[2] = tmp+3;
			position[3] = tmp+6;
			position[4] = tmp+9;
		} else if (rows >= 12){
			tmp = (rows/2) - (7/2);
			position[1] = tmp;
			position[2] = tmp+2;
			position[3] = tmp+4;
			position[4] = tmp+6;
		} else {
			tmp = (rows/2) - (4/2);
			position[1] = tmp;
			position[2] = tmp+1;
			position[3] = tmp+2;
			position[4] = tmp+3;
		}
		
		if (tmp > 4){
			position[0] = 2;
		} else if (tmp > 3){
			position[0] = 1;
		} else {
			position[0] = 0;
		}
		
		position[5] = rows-1;
		
		return position;
		
	}
	
	private static void printMenuOptions(Screen screen,
										 String start,
										 String[] middle,
										 String end,
										 int columns,
										 int position[],
										 boolean first_time){
		screen.clear();

		screen.putString((columns/2) - (start.length()/2), position[0], start, Terminal.Color.MAGENTA, Terminal.Color.BLACK, ScreenCharacterStyle.Bold);
		
		if (first_time) {
			screen.putString((columns/2) - (middle[0].length()/2), position[1], middle[0], Terminal.Color.CYAN, Terminal.Color.BLUE, ScreenCharacterStyle.Bold);
		} else {
			screen.putString((columns/2) - (middle[0].length()/2), position[1], middle[0], Terminal.Color.CYAN, Terminal.Color.BLACK, ScreenCharacterStyle.Bold);
		}
		
		screen.putString((columns/2) - (middle[1].length()/2), position[2], middle[1], Terminal.Color.CYAN, Terminal.Color.BLACK, ScreenCharacterStyle.Bold);
		
		if (first_time) {
			screen.putString((columns/2) - (middle[2].length()/2), position[3], middle[2], Terminal.Color.CYAN, Terminal.Color.BLACK, ScreenCharacterStyle.Bold);
		} else {
			screen.putString((columns/2) - (middle[2].length()/2), position[3], middle[2], Terminal.Color.CYAN, Terminal.Color.BLUE, ScreenCharacterStyle.Bold);
		}
		
		screen.putString((columns/2) - (middle[3].length()/2), position[4], middle[3], Terminal.Color.CYAN, Terminal.Color.BLACK, ScreenCharacterStyle.Bold);
		
		screen.putString((columns/2) - (end.length()/2), position[5], end, Terminal.Color.DEFAULT, Terminal.Color.BLACK, ScreenCharacterStyle.Bold);
	}
	
	public static void main(String[] args) {
		menu();
	}
	
}