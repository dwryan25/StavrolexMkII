package main;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

public class CrossWord {

    private static final String MENU = "Main Menu";
    private static final String CONSTRUCTOR = "Constructor";

    private final JFrame window = new JFrame("Stavrolex");
    private final CardLayout cards = new CardLayout();
    private final JPanel root = new JPanel(cards);

    private final GridData crossword;
    private final GamePanel gamePanel;
    private final WordPanel wordPanel;

    public CrossWord(){
        window.setLayout(new BorderLayout());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Stavrolex");

        InputStream inputStream = getClass().getResourceAsStream("/xwordlist.dict");

        if (inputStream == null){
            throw new RuntimeException("Could not find 'xwordlist.dict' in the JAR resources folder.");
        }
        DictionaryHandler dict = new DictionaryHandler();
        try {
            dict.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        crossword = new GridData(15, 15);
        OptionsHandler menuHandler = new OptionsHandler(crossword, this::showMainMenu);
        gamePanel = new GamePanel(crossword, 50);
        wordPanel = new WordPanel(crossword, dict);

        new KeyHandler(crossword, gamePanel, wordPanel);

        //Cosntructor
        JPanel constructorPanel = new JPanel(new BorderLayout());
        constructorPanel.add(gamePanel, BorderLayout.CENTER);
        constructorPanel.add(wordPanel, BorderLayout.EAST);
        constructorPanel.add(menuHandler, BorderLayout.NORTH);

        //Start Menu
        StartMenu startMenu = new StartMenu(this::startNew, this::loadIn);
        startMenu.setPreferredSize(constructorPanel.getPreferredSize());
        root.add(constructorPanel, "Constructor");
        root.add(startMenu, "Main Menu");

        window.setContentPane(root);

        window.pack();

        window.setLocationRelativeTo(null);
        cards.show(root, "Main Menu");
        window.setVisible(true);
    }

    public void startNew(){
        crossword.clearPuzzle();
        openConstructor();

    }
    public void loadIn(File file){
        try {
            crossword.loadFrom(file);
        } catch(IOException e){
            JOptionPane.showMessageDialog(window, "Could not load file: \n" + e.getMessage(), "Load Failed", JOptionPane.ERROR_MESSAGE);
        }
        openConstructor();
    }

    private void openConstructor(){
        cards.show(root, CONSTRUCTOR);
        gamePanel.repaint();

        try {
            wordPanel.updateWordPanel();
        } catch (IOException e) {
            throw new RuntimeException();
        }
        gamePanel.requestFocusInWindow();
    }

    public void showMainMenu(){
        cards.show(root, MENU);
    }



}
