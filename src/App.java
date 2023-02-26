// package pacman;

import javax.swing.JFrame;

public class App extends JFrame {
	public App() {
		add(new Game(this));
	}

	public static void main(String[] args) {
		App app = new App();
		app.setVisible(true);
		app.setTitle("Pacman");
		app.setSize(Game.SCREEN_WIDTH + 20, Game.SCREEN_HEIGHT + 100);
		app.setDefaultCloseOperation(EXIT_ON_CLOSE);
		app.setLocationRelativeTo(null);
	}

}
