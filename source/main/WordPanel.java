package main;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.List;
import java.io.IOException;


public class WordPanel extends JPanel {

    private final GridData grid;
    private final DictionaryHandler dict;



    private final WordSnapshot across, down;
    private final JLabel acrosslabel, downLabel;

    private final ConstraintPanel acrossConstraints, downConstraints;

    private final JTextField acrossClue;
    private final JTextField downClue;

    private final JTextArea acrossArea = createListArea();
    private final JTextArea downArea = createListArea();

    private final JScrollPane acrossList = createScrollPane(acrossArea);
    private final JScrollPane downList = createScrollPane(downArea);


    private static final int FONT_SIZE = 18;
    private static final Font PLAIN = new Font("Plain", Font.PLAIN, FONT_SIZE);
    private static final Font CLUE_FONT = new Font("Clue", Font.ROMAN_BASELINE, FONT_SIZE);
    private static final Font LIST_FONT = new Font("Italicized", Font.ITALIC, FONT_SIZE);
    private static final Color FIELD_COLOR = Color.WHITE;

    private static final int MAX_RESULT = 500;


    public WordPanel(GridData grid, DictionaryHandler dict){
        this.grid = grid;
        this.dict = dict;



        this.acrosslabel = createLabel("Across");
        this.downLabel = createLabel("Down");

        this.across = createWordSnapShot();
        this.down = createWordSnapShot();

        this.acrossConstraints = createConstraintsPanel();
        this.downConstraints = createConstraintsPanel();

        this.acrossClue = createClueField();
        this.downClue = createClueField();

        setUpClueListener();
        setUpPanel();

        this.setPreferredSize(new Dimension(300, 300));
        this.setBackground(Color.WHITE);
        this.setFocusable(true);
    }

    private void setUpClueListener(){
        acrossClue.getDocument().addDocumentListener(new DocumentListener() {
            private void updateClue(){
                String key = acrosslabel.getText();
                grid.setClue(key, acrossClue.getText());
            }
            @Override
            public void insertUpdate(DocumentEvent e) {updateClue();}

            @Override
            public void removeUpdate(DocumentEvent e) {updateClue();}

            @Override
            public void changedUpdate(DocumentEvent e) {updateClue();}
        });
        downClue.getDocument().addDocumentListener(new DocumentListener() {
            private void updateClue(){
                String key = downLabel.getText();
                grid.setClue(key, downClue.getText());
            }
            @Override
            public void insertUpdate(DocumentEvent e) {updateClue();}

            @Override
            public void removeUpdate(DocumentEvent e) {updateClue();}

            @Override
            public void changedUpdate(DocumentEvent e) {updateClue();}
        });
    }

    private void setUpPanel(){
        this.setLayout(new GridBagLayout());

        addField(acrosslabel, 0, 0.0, GridBagConstraints.HORIZONTAL);
        addField(across, 1, 0.0, GridBagConstraints.HORIZONTAL);
        addField(acrossConstraints, 2, 0.0, GridBagConstraints.HORIZONTAL);
        addField(acrossClue, 3, 0.0, GridBagConstraints.HORIZONTAL);
        addField(acrossList, 4,1.0, GridBagConstraints.BOTH);

        addField(downLabel, 5, 0.0, GridBagConstraints.HORIZONTAL);
        addField(down, 6, 0.0, GridBagConstraints.HORIZONTAL);
        addField(downConstraints, 7, 0.0, GridBagConstraints.HORIZONTAL);
        addField(downClue, 8, 0.0, GridBagConstraints.HORIZONTAL);
        addField(downList, 9, 1.0, GridBagConstraints.BOTH);

    }


    private void addField(Component c, int row, double weightY, int fill){
        GridBagConstraints gc = new GridBagConstraints();

        gc.insets = new Insets(5, 10, 5, 10);
        gc.anchor = GridBagConstraints.WEST;

        gc.gridx = 0;
        gc.gridy = row;
        gc.weighty = weightY;
        gc.weightx = 1.0;
        gc.fill = fill;

        this.add(c, gc);

    }



    private JLabel createLabel(String labelText){
        JLabel label = new JLabel(labelText);
        label.setFont(PLAIN);
        label.setForeground(Color.DARK_GRAY);
        return label;
    }

    private WordSnapshot createWordSnapShot(){
        WordSnapshot wordBox = new WordSnapshot();
        wordBox.setFont(PLAIN);
        wordBox.setBackground(FIELD_COLOR);
        wordBox.setForeground(Color.BLACK);

        return wordBox;
    }

    private ConstraintPanel createConstraintsPanel(){
        ConstraintPanel constraintPanel = new ConstraintPanel(this::updateMatches);
        constraintPanel.setFont(PLAIN);
        constraintPanel.setBackground(FIELD_COLOR);
        constraintPanel.setForeground(Color.BLACK);

        return constraintPanel;
    }

    private JTextField createClueField(){
        JTextField clueBox = new JTextField();
        clueBox.setEditable(true);
        clueBox.setFont(CLUE_FONT);
        clueBox.setBackground(FIELD_COLOR);
        clueBox.setForeground(Color.BLACK);

        return clueBox;
    }

    private JTextArea createListArea() {
        JTextArea area = new JTextArea();
        area.setLineWrap(true);
        area.setWrapStyleWord(false);
        area.setEditable(false);
        area.setFont(LIST_FONT);
        area.setBackground(FIELD_COLOR);
        area.setForeground(Color.BLACK);
        return area;
    }

    private JScrollPane createScrollPane(JTextArea list){

        JScrollPane pane = new JScrollPane(list);
        pane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        pane.setPreferredSize(new Dimension(200, 150));
        return pane;
    }

    public void updateWordPanel() throws IOException {

        int selectedRow = grid.getSelectedRow();
        int selectedCol = grid.getSelectedCol();

        String acrossText = grid.getAcrossWord(selectedRow, selectedCol);
        String downText = grid.getDownWord(selectedRow, selectedCol);

        int downIndex = selectedRow - grid.getVerticalWordIndices(selectedRow, selectedCol)[0];
        int acrossIndex = selectedCol - grid.getHorizontalWordIndices(selectedRow, selectedCol)[0];

        int acrossClueNum = grid.getHorizontalClueNumber(selectedRow, selectedCol);
        int downClueNum = grid.getVerticalClueNumber(selectedRow, selectedCol);

        System.out.println(acrossText);

        acrosslabel.setText(acrossClueNum + " Across");
        across.setWord(acrossText, acrossIndex);

        acrossClue.setText(grid.getClue(acrosslabel.getText()));

        downLabel.setText(downClueNum + " Down");
        down.setWord(downText, downIndex);

        downClue.setText(grid.getClue(downLabel.getText()));

        System.out.println("adding word segment to wordpanel");

        showMatches(acrossText, acrossArea);
        showMatches(downText, downArea);

    }

    public void updateMatches(){

    }


    public void showMatches(String text, JTextArea textArea){
        if(text == null || text.isEmpty()){
            textArea.setText("");
        }

        String pattern = text.replaceAll("[^A-Za-z]", ".");

        List<DictionaryHandler.Entry> hits = dict.match(pattern);

        StringBuilder sb = new StringBuilder();
        int shown = Math.min(hits.size(), MAX_RESULT);

        for (int i = 0; i < shown; i++) {
            sb.append(hits.get(i).word()).append('\n');
        }
        if (hits.size() > shown) {
            sb.append("... and ").append(hits.size() - shown).append(" more");
        }
        if (hits.isEmpty()) {
            sb.append("(no matches)");
        }
        System.out.println("pattern=[" + pattern + "] hits=" + dict.match(pattern).size());
        textArea.setText(sb.toString());
        textArea.setCaretPosition(0); // scroll back to the top for each new selection
    }



}
