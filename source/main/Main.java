package main;

import javax.swing.*;
import java.awt.*;

public class Main {

    public static void main(String[] args){
        SwingUtilities.invokeLater(() -> {
            JFrame window = new JFrame();
            window.setLayout(new BorderLayout());

            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setResizable(false);
            window.setTitle("Stavrolex");

            GridData crossword = new GridData(15, 15);
            GamePanel gamePanel = new GamePanel(crossword, 50);
            WordPanel wordPanel = new WordPanel(crossword, 50);
            new KeyHandler(crossword, gamePanel, wordPanel);


            window.add(gamePanel, BorderLayout.CENTER);
            window.add(wordPanel, BorderLayout.EAST);
            window.pack();


            window.setLocationRelativeTo(null);
            window.setVisible(true);
        });
    }
}
