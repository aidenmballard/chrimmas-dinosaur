package Main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JPanel;

import Button.Button;
import Cards.Card;
import Cards.Deck;
import Cards.Hand;

public class GamePanel extends JPanel implements Runnable {
	// Creates tiles and scales
	int originalTileSize = 16;
	int scale = 3;

	// Scales the tiles
	public int tileSize = originalTileSize * scale;

	// Creates columns and rows of tiles
	public int maxScreenCol = 16;
	public int maxScreenRow = 12;

	// Creates the width and height of the world
	public int screenWidth = tileSize * maxScreenCol;
	public int screenHeight = tileSize * maxScreenRow;

	// FPS
	int FPS = 60;

	// Creates the classes needed for the game
	Thread gameThread;
	public InputListener iLis = new InputListener();
	Deck mainDeck = new Deck(this);
	public Hand mainHand = new Hand(this, mainDeck);
	public Rectangle mouseHitbox = new Rectangle(iLis.x, iLis.y, 10, 10);
	public UI ui = new UI(this);
	Button discard = new Button(this, 50, 50, 100, 40, Color.red, "Discard", 1);
	Button play = new Button(this, 200, 200, 100, 40, Color.green, "Play Hand", 2);
	
	// Booleans for the states of the game
	public boolean titleState;
	public boolean mainState;
	public boolean blindState;
	public boolean shopState;

	// Constructor for gamePanel
	public GamePanel() {
		// Sets information about the panel
		this.setPreferredSize(new Dimension(screenWidth, screenHeight));
		this.setBackground(Color.green);
		this.setDoubleBuffered(true);
		this.addMouseListener(iLis);
		this.addMouseMotionListener(iLis);
		this.setFocusable(true);
		
		titleState = true;
	}

	// Method for starting the game thread
	public void startGameThread() {
		gameThread = new Thread(this);
		gameThread.start();
	}

	@Override
	public void run() {
		// Delta time code (it works, don't touch it)
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

	// Actions to be taken every frame
	public void update() {
		mouseHitbox.x = iLis.x - 3;
		mouseHitbox.y = iLis.y - 3;
		
		ui.update();
	}

	// Images to be drawn every frame
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		
		ui.draw(g2);
		
		g2.setColor(new Color(255, 0, 0, 100));
		g2.fill(mouseHitbox);
		
		g2.dispose();
	}
}
