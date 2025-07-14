package flappybird;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Random;
import javax.imageio.ImageIO;

public class Pipe {
    int x, topHeight, bottomY, bottomHeight;
    int width = GameConstants.PIPE_WIDTH;

    // Static images (loaded once for all pipes)
    static BufferedImage topPipeImage;
    static BufferedImage bottomPipeImage;

    // Load the pipe images once when the class is first used
    static {
        try {
            topPipeImage = ImageIO.read(Pipe.class.getResource("toppipe.png"));
            bottomPipeImage = ImageIO.read(Pipe.class.getResource("bottompipe.png"));
        } catch (IOException e) {
            System.out.println("❌ Could not load pipe images: " + e.getMessage());
        }
    }

    // Constructor to create a pipe at a given X position
    public Pipe(int startX) {
        x = startX;
        Random rand = new Random();
        topHeight = rand.nextInt(200) + 50;

        bottomY = topHeight + GameConstants.PIPE_GAP;
        bottomHeight = GameConstants.SCREEN_HEIGHT - bottomY - GameConstants.GROUND_HEIGHT;
    }

    // Move the pipe to the left
    public void update() {
        x -= GameConstants.PIPE_SPEED;
    }

    // Draw the pipe using images or fallback to green rectangles
    public void draw(Graphics g) {
        if (topPipeImage != null && bottomPipeImage != null) {
            // Draw top pipe from y=0 down to topHeight
            g.drawImage(topPipeImage, x, 0, width, topHeight, null);

            // Draw bottom pipe from bottomY down to the ground
            g.drawImage(bottomPipeImage, x, bottomY, width, bottomHeight, null);
        } else {
            // Fallback in case images don't load
            g.setColor(Color.green);
            g.fillRect(x, 0, width, topHeight);
            g.fillRect(x, bottomY, width, bottomHeight);
        }
    }

    // Check for collision with the bird
    public boolean collidesWith(Bird bird) {
        Rectangle birdBox = bird.getBounds();
        Rectangle topPipe = new Rectangle(x, 0, width, topHeight);
        Rectangle bottomPipe = new Rectangle(x, bottomY, width, bottomHeight);
        return birdBox.intersects(topPipe) || birdBox.intersects(bottomPipe);
    }
}
