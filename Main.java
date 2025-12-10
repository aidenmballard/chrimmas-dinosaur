import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) {
		// Creates window
		JFrame window = new JFrame();

		// Sets window settings
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setResizable(false);
		window.setTitle("Christmas Dinosaur");

		// Adds the panel
		GamePanel game = new GamePanel();
		window.add(game);
		game.startGameThread();

		// Packs window
		window.pack();

		// Adds window
		window.setLocationRelativeTo(null);
		window.setVisible(true);
	}
}