package main;

import javax.swing.*;
import java.awt.*;

public class WordSnapshot extends JPanel {


    private static final int MAX_BOX_SIZE = 32;
    private static final int MIN_BOX_SIZE = 12;
    private String word = "";
    private int selectedIndex;

    private final JPanel gridContainer;

    public WordSnapshot(){
        setOpaque(false);
        setLayout(new GridBagLayout());

        gridContainer = new JPanel();
        gridContainer.setOpaque(false);
        gridContainer.setLayout(new GridLayout(1,0,0,0));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;
        add(gridContainer, gbc);
    }

    public void setWord(String word, int selected){
        this.word = (word == null) ? " " : word;
        this.selectedIndex = selected;

        updateSnapshot();

    }

    private void updateSnapshot(){
        gridContainer.removeAll();

        int n = word.length();
        if(n == 0) {
            revalidate();
            repaint();
            return;
        }

        int availableWidth = getWidth();
        int size = availableWidth / n;
        int targetSize = Math.clamp(size, MIN_BOX_SIZE, MAX_BOX_SIZE);

        int fontsize = Math.max(10, targetSize * 3/5);
        Font cellFont = new Font("Bold", Font.BOLD, fontsize);

        Dimension cellSize = new Dimension(targetSize, targetSize);
        for(int i = 0; i < word.length(); i++){
            char ch = word.charAt(i);

            String letter = ch == '-' || Character.isWhitespace(ch) ? " " : String.valueOf(Character.toUpperCase(ch));
            JLabel cellLabel = new JLabel(letter, SwingConstants.CENTER);

            cellLabel.setFont(cellFont);
            cellLabel.setBackground(Color.WHITE);

            cellLabel.setPreferredSize(cellSize);
            cellLabel.setMaximumSize(cellSize);
            cellLabel.setMinimumSize(cellSize);

            if(i == selectedIndex){
                cellLabel.setBorder(BorderFactory.createLineBorder(Color.RED, 1));
            }
            else{
                cellLabel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
            }
            gridContainer.add(cellLabel);
        }

        revalidate();
        repaint();
    }

    @Override
    public Dimension getPreferredSize(){
        return new Dimension(super.getPreferredSize().width, MAX_BOX_SIZE);
    }

}
