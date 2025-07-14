package flappybird;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    Timer timer;
    Bird bird;
    ArrayList<Pipe> pipes;
    int score = 0;
    int highScore = 0;
    int pipeSpawnTimer = 0;
    boolean isGameRunning = false;
    boolean isGameOver = false;

    JButton startButton, restartButton;
    BufferedImage backgroundImage;

    public GamePanel() {
        setPreferredSize(new Dimension(GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT));
        setLayout(null);
        setFocusable(true);
        addKeyListener(this);

        try {
            backgroundImage = ImageIO.read(getClass().getResource("background.png"));
        } catch (IOException e) {
            System.out.println("❌ Could not load background image: " + e.getMessage());
        }

        initGame();

        startButton = new JButton("Start");
        startButton.setBounds(350, 250, 100, 40);
        startButton.addActionListener(e -> startGame());
        this.add(startButton);

        restartButton = new JButton("Play Again");
        restartButton.setBounds(340, 300, 120, 40);
        restartButton.setVisible(false);
        restartButton.addActionListener(e -> {
            isGameOver = false;
            startButton.setVisible(false);
            restartButton.setVisible(false);
            initGame();
            startGame();
        });
        this.add(restartButton);

        timer = new Timer(20, this);
    }

    private void initGame() {
        bird = new Bird();
        pipes = new ArrayList<>();
        pipes.add(new Pipe(GameConstants.SCREEN_WIDTH));
        score = 0;
        pipeSpawnTimer = 0;
    }

    private void startGame() {
        isGameRunning = true;
        isGameOver = false;
        startButton.setVisible(false);
        restartButton.setVisible(false);
        requestFocusInWindow();
        timer.start();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, GameConstants.SCREEN_WIDTH, GameConstants.SCREEN_HEIGHT, null);
        }

        bird.draw(g);
        for (Pipe pipe : pipes) pipe.draw(g);

        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString("Score: " + score, 20, 40);
        g.drawString("High Score: " + highScore, 20, 70);

        if (!isGameRunning && !isGameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.drawString("Flappy Bird", 300, 200);
        }

        if (isGameOver) {
            g.setFont(new Font("Arial", Font.BOLD, 36));
            g.setColor(Color.RED);
            g.drawString("Game Over", 300, 200);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!isGameRunning) return;

        bird.update();
        pipeSpawnTimer++;

        if (pipeSpawnTimer > 90) {
            pipes.add(new Pipe(GameConstants.SCREEN_WIDTH));
            pipeSpawnTimer = 0;
        }

        for (Pipe pipe : pipes) {
            pipe.update();
            if (pipe.x + pipe.width == bird.x) score++;

            if (pipe.collidesWith(bird)) {
                endGame();
                return;
            }
        }

        pipes.removeIf(pipe -> pipe.x + pipe.width < 0);

        if (bird.y >= GameConstants.SCREEN_HEIGHT - GameConstants.GROUND_HEIGHT || bird.y <= 0) {
            endGame();
        }

        repaint();
    }

    private void endGame() {
        isGameRunning = false;
        isGameOver = true;
        timer.stop();
        highScore = Math.max(score, highScore);
        repaint();
        restartButton.setVisible(true);
    }

    @Override public void keyPressed(KeyEvent e) { 
        if (isGameRunning && e.getKeyCode() == KeyEvent.VK_SPACE) bird.jump();
    }
    @Override public void keyReleased(KeyEvent e) {}
    @Override public void keyTyped(KeyEvent e) {}
}
 