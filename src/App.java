// package pacman;

import javax.swing.JFrame;

public class App extends JFrame {
	/**
	 * The main class of the game, adding the game to the window
	 */
	public App() {
		add(new Game(this));
	}

	public static void main(String[] args) {
		/**
		 * Main method setting up the window
		 */
		App app = new App();
		app.setVisible(true);
		app.setTitle("Pacman");
		app.setSize(Game.SCREEN_WIDTH + 20, Game.SCREEN_HEIGHT + 100);
		app.setDefaultCloseOperation(EXIT_ON_CLOSE);
		app.setLocationRelativeTo(null);
	}

}
