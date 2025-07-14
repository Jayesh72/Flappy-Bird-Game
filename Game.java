package flappybird;

import javax.swing.*;

public class Game {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Flappy Bird - Jayesh Parmar");
        GamePanel panel = new GamePanel();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panel);
        frame.pack();
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
