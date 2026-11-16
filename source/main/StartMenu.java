package main;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class StartMenu extends JPanel{

    private final BiConsumer<Integer, Integer> newPuzzle;
    private final Consumer<File> loaded;

    public StartMenu(BiConsumer<Integer, Integer> newPuzzle, Consumer<File> loaded){
        this.newPuzzle = newPuzzle;
        this.loaded = loaded;

        setLayout(new GridBagLayout());
        setBackground(Color.WHITE);

        JPanel menuBox = new JPanel();
        menuBox.setLayout(new BoxLayout(menuBox, BoxLayout.Y_AXIS));
        menuBox.setOpaque(false);
        menuBox.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        JLabel title = new JLabel("Stavrolex");
        title.setFont(new Font(Font.SANS_SERIF, Font.BOLD, 48));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        menuBox.add(title);
        menuBox.add(Box.createRigidArea(new Dimension(0, 25)));
        menuBox.add(makeButton("New Crossword", this::handleNew));
        menuBox.add(Box.createRigidArea(new Dimension(0, 10)));
        menuBox.add(makeButton("Load Saved", this::handleSaved ));
        menuBox.add(Box.createRigidArea(new Dimension(0, 10)));
        menuBox.add(makeButton("Quit", () -> System.exit(0)));

        add(menuBox);
    }

    private JButton makeButton(String text, Runnable action){
        JButton button = new JButton(text);
        button.setFont(new Font(Font.SANS_SERIF, Font.PLAIN, 20));
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));

        button.addActionListener(e -> action.run());
        return button;
    }


    private void handleNew(){
        int[] size = DimensionDialog.ask(this);
        int rows = size[0];
        int cols = size[1];
        newPuzzle.accept(rows, cols);
    }

    private void handleSaved(){
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Select Crossword File");
        if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
            loaded.accept(chooser.getSelectedFile());
        }

    }

}
