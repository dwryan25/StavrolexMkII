package main;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class ConstraintPanel extends JPanel{

    private final JCheckBox vowels;
    private final JCheckBox consonants;

    private final JTextField permitted;
    private final JTextField notPermitted;

    private final Runnable onChange;

    private boolean updating = false;

    private static final Font FONT = new Font("Clue", Font.ROMAN_BASELINE, 10);


    public ConstraintPanel(Runnable onChange){
        this.onChange = onChange;

        setLayout(new GridLayout(3, 1, 0, 5));
        setOpaque(false);
        setBorder(BorderFactory.createTitledBorder("Constraints on selected cell"));

        vowels = makeButton("Vowels Onlu");
        consonants = makeButton("Consonants Only");
        permitted = makeTextField();
        notPermitted = makeTextField();

        setUpPanel();
    }

    private void setUpPanel(){

        JPanel toggles = new JPanel();

        toggles.add(vowels);
        toggles.add(consonants);

        add(toggles);
        add(labeled("Permitted", permitted));
        add(labeled("Not Permitted", notPermitted));


        vowels.addActionListener(e -> {
            if(vowels.isSelected()) {consonants.setSelected(false);}

        });
        consonants.addActionListener(e -> {
            if (consonants.isSelected()) { vowels.setSelected(false);}
        });

        watch(permitted);
        watch(notPermitted);

    }



    private JCheckBox makeButton(String title){
        JCheckBox button = new JCheckBox();
        button.setFont(FONT);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 20));
        button.setText(title);
        return button;
    }

    private JTextField makeTextField(){
        JTextField field = new JTextField();
        field.setEditable(true);
        field.setFont(FONT);
        field.setBackground(Color.WHITE);
        field.setForeground(Color.BLACK);

        return field;
    }

    private void fireChanged(){
        if(!updating) onChange.run();
    }

    private JPanel labeled(String text, JTextField field) {
        JPanel row = new JPanel(new BorderLayout(5, 0));
        row.setOpaque(false);
        row.add(new JLabel(text), BorderLayout.WEST);
        row.add(field, BorderLayout.CENTER);
        return row;
    }

    private void watch(JTextField field) {
        field.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                fireChanged();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                fireChanged();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                fireChanged();
            }
        });
    }
}//end ConstraintPanel
