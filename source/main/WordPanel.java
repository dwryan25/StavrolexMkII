package main;

import javax.swing.*;
import java.awt.*;


public class WordPanel extends JPanel {

    private final GridData grid;
    private final JTextField across, down;
    private final JLabel acrosslabel, downLabel;


    private static final int FONT_SIZE = 18;
    private static final Font CHAR_FONT = new Font("Plain", Font.PLAIN, FONT_SIZE);
    private static final Color FIELD_COLOR = Color.WHITE;


    public WordPanel(GridData grid){
        this.grid = grid;

        this.acrosslabel = createLabel("Across");
        this.downLabel = createLabel("Down");
        this.across = createTextField();
        this.down = createTextField();

        setUpPanel();

        this.setPreferredSize(new Dimension(300, 200));
        this.setBackground(Color.WHITE);
        this.setFocusable(false);
    }

    private void setUpPanel(){
        this.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.insets = new Insets(5, 10, 5, 10);
        constraints.fill = GridBagConstraints.CENTER;
        constraints.anchor = GridBagConstraints.WEST;

        constraints.gridx = 0; constraints.gridy = 0; constraints.weightx = 0;
        this.add(acrosslabel, constraints);

        constraints.gridx = 0; constraints.gridy = 1; constraints.weightx = 1.0;
        this.add(across, constraints);

        constraints.gridx = 0; constraints.gridy = 2; constraints.weightx = 0;
        this.add(downLabel, constraints);

        constraints.gridx = 0; constraints.gridy = 3; constraints.weightx = 1.0;
        this.add(down, constraints);
    }




    private JLabel createLabel(String labelText){
        JLabel label = new JLabel(labelText);
        label.setFont(CHAR_FONT);
        label.setForeground(Color.DARK_GRAY);
        return label;
    }

    private JTextField createTextField(){
        JTextField wordBox = new JTextField();
        wordBox.setEditable(false);
        wordBox.setFont(CHAR_FONT);
        wordBox.setBackground(FIELD_COLOR);
        wordBox.setForeground(Color.BLACK);

        return wordBox;
    }

    public void updateWordPanel(){

        int selectedRow = grid.getSelectedRow();
        int selectedCol = grid.getSelectedCol();
        String acrossText = grid.getAcrossWord(selectedRow, selectedCol);
        int acrossClueNum = grid.getHorizontalClueNumber(selectedRow, selectedCol);
        int downClueNum = grid.getVerticalClueNumber(selectedRow, selectedCol);
        String downText = grid.getDownWord(selectedRow, selectedCol);
        System.out.println(acrossText);

        acrosslabel.setText(acrossClueNum + " Across");
        across.setText(acrossText);

        downLabel.setText(downClueNum + " Down");
        down.setText(downText);

        System.out.println("adding word segment to wordpanel");


        revalidate();
        repaint();
    }





}
