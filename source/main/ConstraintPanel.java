package main;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.util.function.Consumer;

public class ConstraintPanel extends JPanel{

    private final JCheckBox vowels;
    private final JCheckBox consonants;

    private final JTextField permitted;
    private final JTextField notPermitted;

    private final Consumer<CellConstraint> onChange;

    private boolean updating = false;

    private static final Font FONT = new Font("Clue", Font.ROMAN_BASELINE, 10);


    public ConstraintPanel(Consumer<CellConstraint> onChange){
        this.onChange = onChange;

        setLayout(new GridLayout(3, 1, 0, 5));
        setOpaque(false);
        setBorder(BorderFactory.createTitledBorder("Constraints on selected cell"));

        vowels = makeButton("Vowels Only");
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
            if(vowels.isSelected()) {
                consonants.setSelected(false);
                fireChanged();
            }
        });
        consonants.addActionListener(e -> {
            if (consonants.isSelected()) {
                vowels.setSelected(false);
                fireChanged();
            }
        });

        watch(permitted);
        watch(notPermitted);

    }

    public void setConstraint(CellConstraint c){
        System.out.println("setConstraint called while typing");
        updating = true;
        try {
            vowels.setSelected(c.type() == LetterType.VOWEL);
            consonants.setSelected(c.type() == LetterType.CONSONANT);
            permitted.setText(c.allowed());
            notPermitted.setText(c.banned());
        } finally{
            updating = false;
        }
    }

    public void setControlsEnabled(boolean enabled){
        vowels.setEnabled(enabled);
        consonants.setEnabled(enabled);
        permitted.setEnabled(enabled);
        notPermitted.setEnabled(enabled);
    }

    public CellConstraint buildConstraint(){
        LetterType type = (vowels.isSelected() ? LetterType.VOWEL
                : consonants.isSelected() ? LetterType.CONSONANT
                : LetterType.ANY);
        return new CellConstraint(type, normalize(permitted.getText()), normalize(notPermitted.getText()));
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
        System.out.println("Firing change\n");
        if(!updating) onChange.accept(buildConstraint());
    }

    private JPanel labeled(String text, JTextField field) {
        JPanel row = new JPanel(new BorderLayout(5, 0));
        row.setOpaque(false);
        row.add(new JLabel(text), BorderLayout.WEST);
        row.add(field, BorderLayout.CENTER);
        return row;
    }

    private static String normalize(String text){
        return text.toUpperCase().replaceAll("[^A-Z]}", "");
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
