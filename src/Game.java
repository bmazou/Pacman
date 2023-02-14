// package pacman;

import javax.swing.JFrame;

public class Game extends JFrame {

	public Game() {
		add(new App());
	}

	public static void main(String[] args) {
		Game pac = new Game();
		pac.setVisible(true);
		pac.setTitle("Pacman");
		pac.setSize(App.SCREEN_WIDTH + 20, App.SCREEN_HEIGHT + 80);
		pac.setDefaultCloseOperation(EXIT_ON_CLOSE);
		pac.setLocationRelativeTo(null);
	}

}
