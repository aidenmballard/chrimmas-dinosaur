package Main;

import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		// Creates frame
		JFrame window = new JFrame();

		// Sets information for window
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("2D Game");

		// Creates and adds the game panel
		GamePanel game = new GamePanel();
		window.add(game);
		game.startGameThread();

		// Packs the frame
		window.pack();

		// Sets the window visible
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}
}
