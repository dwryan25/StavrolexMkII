package main;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class CrossWord {

    private static final String MENU = "Main Menu";
    private static final String CONSTRUCTOR = "Constructor";

    private final JFrame window = new JFrame("Stavrolex");
    private final CardLayout cards = new CardLayout();
    private final JPanel root = new JPanel(cards);

    private GridData crossword;
    private GamePanel gamePanel;
    private WordPanel wordPanel;
    private JPanel builderPanel;

    private final DictionaryHandler dict = new DictionaryHandler();

    public CrossWord(){
        window.setLayout(new BorderLayout());
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("Stavrolex");

        InputStream inputStream = getClass().getResourceAsStream("/xwordlist.dict");

        if (inputStream == null){
            throw new RuntimeException("Could not find 'xwordlist.dict' in the JAR resources folder.");
        }
        try {
            dict.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        //Start Menu
        StartMenu startMenu = new StartMenu(this::startNew, this::loadIn);
        startMenu.setPreferredSize(new Dimension(600, 400));
        root.add(startMenu, MENU);



        window.setContentPane(root);

        window.pack();

        window.setLocationRelativeTo(null);
        cards.show(root, MENU);
        window.setVisible(true);
    }

    private void buildConstructor(int rows, int cols){
        crossword = new GridData(rows, cols);
        OptionsHandler menuHandler = new OptionsHandler(crossword, this::showMainMenu);
        int cellSize = Math.min(80, 700 / Math.max(crossword.getRows(), crossword.getCols()));
        gamePanel = new GamePanel(crossword, cellSize);
        wordPanel = new WordPanel(crossword, dict);

        new KeyHandler(crossword, gamePanel, wordPanel);

        //Constructor
        JPanel constructorPanel = new JPanel(new BorderLayout());
        constructorPanel.add(gamePanel, BorderLayout.CENTER);
        constructorPanel.add(wordPanel, BorderLayout.EAST);
        constructorPanel.add(menuHandler, BorderLayout.NORTH);

        if (builderPanel != null) root.remove(builderPanel);
        builderPanel = constructorPanel;
        root.add(constructorPanel, CONSTRUCTOR);

        window.pack();
        window.setLocationRelativeTo(null);

    }

    public void startNew(int rows, int cols){
        buildConstructor(rows, cols);
        openConstructor();

    }
    public void loadIn(File file){
        try {
            int[] size = GridData.readSize(file);
            int rows = size[0];
            int cols = size[1];
            buildConstructor(rows, cols);
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
