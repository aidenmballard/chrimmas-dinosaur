
// Import statements
import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.geom.AffineTransform;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import java.awt.image.BufferedImage;

public class GamePanel extends JPanel implements Runnable {
    int originalTileSize = 16;
    int scale = 2;
    public int tileSize = originalTileSize * scale;

    int maxScreenCol = 20;
    int maxScreenRow = 12;

    public int screenWidth = maxScreenCol * tileSize;
    public int screenHeight = maxScreenRow * tileSize;

    int screenWidth2 = screenWidth;
    int screenHeight2 = screenHeight;
    BufferedImage tempScreen;
    Graphics2D g2;

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

    BufferedImage dinoLeft = null;
    BufferedImage dinoRight = null;
    BufferedImage dinoUp = null;
    BufferedImage currentDino = null;
    int count = 0;

    Thread gameThread;

    public GamePanel() {
        this.setPreferredSize(new Dimension(screenHeight, screenWidth));
        this.setBackground(new Color(233, 233, 255));
        this.setDoubleBuffered(true);
        this.setFocusable(true);

        for (int i = 0; i < 15; i++)
            for (int j = 0; j < 15; j++)
                snowflakeOffsets[i][j] = (int) (Math.random() * 40);

        try {
            dinoLeft = ImageIO.read(getClass().getResourceAsStream("/Images/BeardDinoL.png"));
            dinoRight = ImageIO.read(getClass().getResourceAsStream("/Images/BeardDinoR.png"));
            dinoUp = ImageIO.read(getClass().getResourceAsStream("/Images/BeardDino.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        currentDino = dinoLeft;
        tempScreen = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_ARGB);
        g2 = (Graphics2D) tempScreen.getGraphics();
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
                drawToTempScreen();
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
        count++;
        if (yVelocity != 0)
            currentDino = dinoUp;
        else {
            if (count % 15 == 0) {
                if (currentDino == dinoLeft)
                    currentDino = dinoRight;
                else
                    currentDino = dinoLeft;
                count = 0;
            }
        }

        yPos += yVelocity;
        if (yPos + 80 >= screenHeight - 80) {
            yPos = screenHeight - 160;
            yVelocity = 0;
        } else if (yVelocity < 8)
            yVelocity += 0.4;

        triangleX -= triangleXOffset;
        birdX -= birdXOffset;
        if ((triangleX > xPos && triangleX - ((int) xPos + 150) <= 10 && yVelocity == 0)
                || (birdX > xPos && birdX - ((int) xPos + 150) <= 10 && yVelocity == 0))
            yVelocity += -11;

        if (triangleX < -90) {
            triangleX = screenWidth + (int) (Math.random() * 400 + 70);
            if (birdX < -40 && Math.random() < 0.4)
                birdX = triangleX + 380;
        }
    }

    public void drawToTempScreen() {
        // Clear with transparency
        g2.setComposite(AlphaComposite.Clear);
        g2.fillRect(0, 0, screenWidth, screenHeight);
        g2.setComposite(AlphaComposite.SrcOver);

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
        g2.setStroke(new BasicStroke(4));
        g2.drawRect(-10, screenHeight - 80, screenWidth + 40, 90);
        g2.setColor(Color.WHITE);
        g2.fillRect(-10, screenHeight - 79, screenWidth + 40, 90);

        // Draws the player
        g2.drawImage(currentDino, (int) xPos, (int) yPos, 80, 80, this);

        g2.setStroke(new BasicStroke(10));
        g2.setFont(new Font("Arial", Font.BOLD, 17));
        g2.setColor(Color.BLACK);
        g2.drawString("          *", triangleX, screenHeight - 190);
        g2.drawString("         /.\\", triangleX, screenHeight - 175);
        g2.drawString("        /..'\\", triangleX, screenHeight - 160);
        g2.drawString("        /'.'\\", triangleX, screenHeight - 145);
        g2.drawString("       /.''.'\\", triangleX, screenHeight - 130);
        g2.drawString("       /.'.'.\\", triangleX, screenHeight - 115);
        g2.drawString("      /'.''.'.\\", triangleX, screenHeight - 100);
        g2.drawString("        [_]", triangleX, screenHeight - 85);

        g2.setFont(new Font("Arial", Font.BOLD, 11));
        String[] asciiArt = {
                "  {  }",
                "   }{",
                " .-'\\",
                "  \"'\\\"\"\"\"\"\"'),",
                "      )/----,)",
                "      / \\   / | "
        };
        int y = screenHeight - 180;
        for (String line : asciiArt) {
            g2.drawString(line, birdX, y);
            y += 13;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // Enable quality
        g2d.setRenderingHint(
                RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);

        int w = screenWidth;
        int h = screenHeight;

        // Save transform
        AffineTransform old = g2d.getTransform();

        // Move origin to center of panel
        g2d.translate(h / 2.0, w / 2.0);

        // Rotate 90 degrees clockwise
        g2d.rotate(Math.toRadians(90));

        // Move origin back to top-left of image
        g2d.translate(-w / 2.0, -h / 2.0);

        // Draw the scene
        g2d.drawImage(tempScreen, 0, 0, null);

        // Restore transform
        g2d.setTransform(old);
    }

}
