package main;

import javax.swing.*;
import java.awt.*;


public class WordPanel extends JPanel {

    private final GridData grid;
    private final JTextField across, down;

    private static final int FONT_SIZE = 18;
    private static final Font CHAR_FONT = new Font("Plain", Font.PLAIN, FONT_SIZE);
    private static final Color FIELD_COLOR = Color.WHITE;


    public WordPanel(GridData grid){
        this.grid = grid;

        across = new JTextField();
        across.setEditable(false);
        across.setFont(CHAR_FONT);
        across.setBackground(FIELD_COLOR);
        across.setForeground(Color.BLACK);

        down = new JTextField();
        down.setEditable(false);
        down.setFont(CHAR_FONT);
        down.setBackground(FIELD_COLOR);
        down.setForeground(Color.BLACK);

        this.setPreferredSize(new Dimension(300, 200));
        this.setBackground(Color.WHITE);
        this.setFocusable(false);
    }

    public void updateWordPanel(){

        int selectedRow = grid.getSelectedRow();
        int selectedCol = grid.getSelectedCol();
        String acrossText = grid.getAcrossWord(selectedRow, selectedCol);
        String downText = grid.getDownWord(selectedRow, selectedCol);
        System.out.println(acrossText);
        across.setText(acrossText);
        down.setText(downText);
        System.out.println("adding word segment to wordpanel");
        add(across);
        add(down);

        revalidate();
        repaint();
    }




}
