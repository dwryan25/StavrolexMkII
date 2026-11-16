package main;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
public class OptionsHandler extends JMenuBar implements ActionListener{

    private final GridData grid;

    private final JMenuItem symmetry;
    private final JMenuItem clear;
    private final JMenuItem startmenu;
    private final JMenuItem save;
    private final JMenuItem clearConstraints;

    private final Runnable onMainMenu;


    private static final int FONT_SIZE = 18;
    private static final Font DEFAULT_FONT = new Font("Plain", Font.PLAIN, FONT_SIZE);
    private static final Color FIELD_COLOR = Color.GRAY;

    public OptionsHandler(GridData grid, Runnable onMainMenu){
        this.grid = grid;
        this.onMainMenu = onMainMenu;
        this.symmetry = new JMenuItem("Toggle Symmetry");
        this.clear = new JMenuItem("Clear white cells");
        this.startmenu = new JMenuItem("Quit");
        this.save = new JMenuItem("Save");
        this.clearConstraints = new JMenuItem("Clear Constraints");

        setUp();

        this.setPreferredSize(new Dimension(500, 50));
        this.setFocusable(true);
    }


    public void setUp(){
        this.setLayout(new GridBagLayout());

        save.setActionCommand("save");
        save.addActionListener(this);
        save.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.BLUE));
        addButton(save, 0, 0, 1.0, GridBagConstraints.CENTER);

        symmetry.setActionCommand("togglesym");
        symmetry.addActionListener(this);
        symmetry.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.DARK_GRAY));
        addButton(symmetry, 1, 0, 1.0, GridBagConstraints.CENTER);

        clearConstraints.setActionCommand("clearconstraints");
        clearConstraints.addActionListener(this);
        clearConstraints.setBorder(BorderFactory.createMatteBorder(2,2,2,2,Color.DARK_GRAY));
        addButton(clearConstraints, 2, 0, 0.0, GridBagConstraints.CENTER);

        clear.setActionCommand("clearwhites");
        clear.addActionListener(this);
        clear.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.DARK_GRAY));
        addButton(clear, 3, 0, 0.0, GridBagConstraints.CENTER);

        startmenu.setActionCommand("quit");
        startmenu.addActionListener(this);
        startmenu.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.DARK_GRAY));
        addButton(startmenu, 4, 0, 1.0, GridBagConstraints.CENTER);

    }

    public void addButton(Component c, int gridX, int gridY, double weightX, int fill){
        GridBagConstraints gc = new GridBagConstraints();

        c.setFont(DEFAULT_FONT);
        c.setBackground(FIELD_COLOR);
        gc.insets = new Insets(5, 10, 5, 10);
        gc.fill = fill;
        gc.anchor = GridBagConstraints.WEST;
        gc.gridx = gridX;
        gc.gridy = gridY;
        gc.weightx = weightX;
        this.add(c, gc);
    }
    public void confirmAndReturn(){
        int choice = JOptionPane.showConfirmDialog(this, "Return to Main Menu? Unsaved Changed will be lost.", "Main Menu", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION){
            onMainMenu.run();
        }
    }

    private void saveCrossword(){
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new FileNameExtensionFilter("Crossword file: MyFile.xwd", "xwd"));
        if(chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) return;
        File chosen = chooser.getSelectedFile();
        if(chosen.getName().endsWith("xwd")) chosen = new File(chosen.getPath() + "xwd");
        try {
            grid.saveTo(chosen.toPath());
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Save Failed" + ex.getMessage());
        }
    }

    public void actionPerformed(ActionEvent e){
        if("togglesym".equals(e.getActionCommand())){
            grid.toggleSymmetry();
        }
        if("clearwhites".equals(e.getActionCommand())){
            grid.clearWhiteCells();
        }
        if("quit".equals(e.getActionCommand())){
            confirmAndReturn();
        }
        if("save".equals(e.getActionCommand())){
            saveCrossword();
        }
        if("clearconstraints".equals(e.getActionCommand())){
            grid.clearConstraints();
        }

    }

}
