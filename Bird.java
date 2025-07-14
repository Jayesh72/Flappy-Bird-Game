package flappybird;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;

public class Bird {
    int x, y;
    int velocity;

    boolean isJumping = false;
    int jumpTime = 0;
    final int maxJumpTime = 15;

    BufferedImage birdImage;
    Clip flapSound;

    public Bird() {
        x = GameConstants.BIRD_START_X;
        y = GameConstants.BIRD_START_Y;
        velocity = 0;

        try {
            birdImage = ImageIO.read(getClass().getResource("bird.png"));
        } catch (IOException e) {
            System.out.println("❌ Could not load bird image: " + e.getMessage());
        }

        try {
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(getClass().getResource("flap.wav"));
            flapSound = AudioSystem.getClip();
            flapSound.open(audioIn);
        } catch (Exception e) {
            System.out.println("❌ Could not load flap sound: " + e.getMessage());
        }
    }

    public void update() {
        if (isJumping) {
            double t = jumpTime / (double) maxJumpTime;
            double decay = 1 - t;
            velocity = (int) (GameConstants.JUMP_STRENGTH * decay);
            jumpTime++;
            if (jumpTime >= maxJumpTime) {
                isJumping = false;
            }
        } else {
            velocity += GameConstants.GRAVITY;
        }

        y += velocity;
    }

    public void jump() {
        isJumping = true;
        jumpTime = 0;

        // Play flap sound
        if (flapSound != null) {
            if (flapSound.isRunning()) {
                flapSound.stop();
            }
            flapSound.setFramePosition(0); // rewind
            flapSound.start();
        }
    }

    public void draw(Graphics g) {
        if (birdImage != null) {
            Graphics2D g2d = (Graphics2D) g;

            double angle = Math.toRadians(Math.max(-45, Math.min(velocity * 2, 45)));
            int cx = x + GameConstants.BIRD_WIDTH / 2;
            int cy = y + GameConstants.BIRD_HEIGHT / 2;

            AffineTransform old = g2d.getTransform();
            g2d.rotate(angle, cx, cy);
            g2d.drawImage(birdImage, x, y, GameConstants.BIRD_WIDTH, GameConstants.BIRD_HEIGHT, null);
            g2d.setTransform(old);
        } else {
            g.setColor(Color.red);
            g.fillOval(x, y, GameConstants.BIRD_WIDTH, GameConstants.BIRD_HEIGHT);
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, GameConstants.BIRD_WIDTH, GameConstants.BIRD_HEIGHT);
    }
}
