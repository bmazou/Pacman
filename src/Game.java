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
		pac.setSize(App.SCREEN_SIZE + 20, App.SCREEN_SIZE + 60);
		pac.setDefaultCloseOperation(EXIT_ON_CLOSE);
		pac.setLocationRelativeTo(null);
	}

}
