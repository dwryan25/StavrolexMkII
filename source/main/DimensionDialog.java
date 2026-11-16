package main;

import javax.swing.*;
import java.awt.*;
import java.text.ParseException;

public class DimensionDialog {

    private static final int MAX_SIZE = 30;
    private static final int MIN_SIZE = 3;

    private static final String[] PRESET_NAMES = {"5x5 Mini","10x10 Medium", "15x15 Standard", "21x21 Sunday"};
    private static final int[] PRESET_SIZES = {5, 10, 15, 21};

    private DimensionDialog(){}


    public static int[] ask(Component parent){
        JSpinner rowSelector = new JSpinner(new SpinnerNumberModel(15, MIN_SIZE, MAX_SIZE, 1));
        JSpinner colSelector = new JSpinner(new SpinnerNumberModel(15, MIN_SIZE, MAX_SIZE, 1));

        JComboBox<String> presets = new JComboBox<>(PRESET_NAMES);
        presets.setSelectedIndex(2);
        presets.addActionListener(e -> {
            int size = PRESET_SIZES[presets.getSelectedIndex()];
            if (size > 0){
                rowSelector.setValue(size);
                colSelector.setValue(size);
            }
        });

        JPanel form = new JPanel(new GridLayout(3,2, 8, 8));
        form.add(new JLabel("Presets"));
        form.add(presets);
        form.add(new JLabel("Rows:"));
        form.add(rowSelector);
        form.add(new JLabel("Columns"));
        form.add(colSelector);

        int choice = JOptionPane.showConfirmDialog(parent, form, "New Crossword", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (choice != JOptionPane.OK_OPTION) return null;

        try {
            rowSelector.commitEdit();
            colSelector.commitEdit();
        } catch (ParseException ignored) {

        }
        return new int[] {(Integer) rowSelector.getValue(), (Integer) colSelector.getValue()};
    }



}
