package Games.GameStarter;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

public class GamePanel extends JPanel {
	int originalTileSize = 16;
	int scale = 2;
	public int tileSize = originalTileSize * scale;

	int maxScreenCol = 16;
	int maxScreenRow = 16;

	public int screenWidth = maxScreenCol * tileSize;
	public int screenHeight = maxScreenRow * tileSize;

	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.BLACK);
		this.setDoubleBuffered(true);
		this.setFocusable(true);
	}
}
