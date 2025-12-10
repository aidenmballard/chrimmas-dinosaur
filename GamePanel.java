
// Import statements
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
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

	public double xPos = 30;
	public double yPos = 50;

	public double xVelocity = 10;
	public double yVelocity = 10;

	public int triangleX = 400;
	public int triangleXOffset = 6;

	public int birdX = 800;
	public int birdXOffset = 6;

	int snowflakeX = 10;
	double snowflakeYConstant = 40;
	double snowflakeY = 40;

	int[][] snowflakeOffsets = new int[15][15];
	int row = 14;
	int count = 1;

	Thread gameThread;

	public GamePanel() {
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.WHITE);
		this.setDoubleBuffered(true);
		this.setFocusable(true);

		for (int i = 0; i < 15; i++)
			for (int j = 0; j < 15; j++)
				snowflakeOffsets[i][j] = (int) (Math.random() * 40);
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
		yPos += yVelocity;
		if (yPos + 40 >= screenHeight - 80) {
			yPos = screenHeight - 120;
			yVelocity = 0;
		} else if (yVelocity < 8)
			yVelocity += 0.4;

		triangleX -= triangleXOffset;
		birdX -= birdXOffset;
		if ((triangleX > xPos && triangleX - ((int) xPos + 160) <= 10 && yVelocity == 0)
				|| (birdX > xPos && birdX - ((int) xPos + 160) <= 10 && yVelocity == 0))
			yVelocity += -10;

		if (triangleX < -30) {
			triangleX = screenWidth + (int) (Math.random() * 400 + 70);
			if (birdX < -40 && Math.random() < 0.2)
				birdX = triangleX + 380;
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		// Draws snoflakes
		g2.setColor(Color.LIGHT_GRAY);
		g2.setFont(new Font("Arial", Font.PLAIN, 40));
		for (int i = 0; i < 15; i++) {
			for (int j = 0; j < 15; j++) {
				g2.drawString("*", (snowflakeX + snowflakeOffsets[i][j]) % screenWidth,
						(int) (snowflakeY % screenHeight));
				snowflakeY += 40;
			}
			snowflakeX += 40;
		}

		snowflakeYConstant += 0.3;

		snowflakeX = 10;
		snowflakeY = snowflakeYConstant;

		// Draws the ground
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(10));
		g2.drawRect(-10, screenHeight - 80, screenWidth + 40, 90);
		g2.setColor(Color.WHITE);
		g2.fillRect(-10, screenHeight - 75, screenWidth + 40, 90);

		// Draws the player
		g2.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(10));
		g2.fillRect((int) xPos, (int) yPos, 40, 40);

		// Draws the trees and birds
		g2.fillPolygon(new int[] { triangleX - 30, triangleX, triangleX + 30 },
				new int[] { screenHeight - 80, screenHeight - 140, screenHeight - 80 }, 3);
		g2.fillOval(birdX, screenHeight - 160, 40, 30);

		g2.dispose();
	}
}
