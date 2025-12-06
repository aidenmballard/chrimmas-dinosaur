// Import statements
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {
	int originalTileSize = 16;
	int scale = 2;
	public int tileSize = originalTileSize * scale;

	int maxScreenCol = 16;
	int maxScreenRow = 16;

	public int screenWidth = maxScreenCol * tileSize;
	public int screenHeight = maxScreenRow * tileSize;

	int FPS = 60;

	public int xPos = 30;
	public int yPos = 50;
	
	public int xVelocity = 10;
	public int yVelocity = 10;

	Thread gameThread;

	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.WHITE);
		this.setDoubleBuffered(true);
		this.setFocusable(true);
	}

	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	@Override
	public void run() {
		double drawInterval = 1000000000 / FPS; // Draws 60 times a second
		double delta = 0;
		long lastTime = System.nanoTime();
		long currentTime;
		long timer = 0;
		int drawCount = 0;

		// While the game thread exists, loop runs
		while (gameThread != null) {
			currentTime = System.nanoTime();

			delta += (currentTime - lastTime) / drawInterval;
			timer += (currentTime - lastTime);
			lastTime = currentTime;

			if (delta >= 1) {
				update();
				repaint();
				delta--;
				drawCount++;
			}

			if (timer >= 1000000000) {
				drawCount = 0;
				timer = 0;
			}
		}
	}

	public void update() {
		xPos += xVelocity;
		yPos += yVelocity;

		if (xPos >= screenWidth || xPos <= 0) xVelocity *= -1;
		if (yPos >= screenHeight || yPos <= 0) yVelocity *= -1;
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		g2.setColor(Color.BLACK);
		g2.fillRect(xPos, yPos, 40, 40);
		
		g2.dispose();
	}
}
