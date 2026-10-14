package main;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Path;

public class Main {

    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(() -> {
            JFrame window = new JFrame();
            window.setLayout(new BorderLayout());

            window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            window.setResizable(false);
            window.setTitle("Stavrolex");

            DictionaryHandler dict = new DictionaryHandler();
            try {
                dict.load(Path.of("resources/xwordlist.dict"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            GridData crossword = new GridData(15, 15);
            MenuHandler menuHandler = new MenuHandler(crossword);
            GamePanel gamePanel = new GamePanel(crossword, 50);
            WordPanel wordPanel = new WordPanel(crossword, dict);

            new KeyHandler(crossword, gamePanel, wordPanel);


            window.add(gamePanel, BorderLayout.CENTER);
            window.setJMenuBar(menuHandler);
            window.add(wordPanel, BorderLayout.EAST);

            window.pack();


            window.setLocationRelativeTo(null);
            window.setVisible(true);
        });
    }
}
