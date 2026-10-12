package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class MenuHandler extends JMenuBar implements ActionListener{

    private final GridData grid;

    private final JMenuItem symmetry;
    private final JMenuItem clear;


    private static final int FONT_SIZE = 18;
    private static final Font DEFAULT_FONT = new Font("Plain", Font.PLAIN, FONT_SIZE);
    private static final Color FIELD_COLOR = Color.GRAY;

    public MenuHandler(GridData grid){
        this.grid = grid;
        this.symmetry = new JMenuItem("Toggle Symmetry");
        this.clear = new JMenuItem("Clear white cells");

        setUp();

        this.setPreferredSize(new Dimension(500, 50));
        this.setFocusable(true);
    }


    public void setUp(){



        this.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        constraints.insets = new Insets(5, 10, 5, 10);
        constraints.fill = GridBagConstraints.CENTER;
        constraints.anchor = GridBagConstraints.WEST;

        symmetry.setActionCommand("togglesym");
        symmetry.addActionListener(this);
        constraints.gridx = 0; constraints.gridy = 0; constraints.weightx = 1.0;
        symmetry.setFont(DEFAULT_FONT);
        symmetry.setBackground(FIELD_COLOR);
        symmetry.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.DARK_GRAY));
        this.add(symmetry, constraints);

        clear.setActionCommand("clearwhites");
        clear.addActionListener(this);
        constraints.gridx = 1; constraints.gridy = 0; constraints.weightx = 1.0;
        clear.setFont(DEFAULT_FONT);
        clear.setBackground(FIELD_COLOR);
        clear.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.DARK_GRAY));
        this.add(clear, constraints);

    }

    public void actionPerformed(ActionEvent e){

        if("togglesym".equals(e.getActionCommand())){
            grid.toggleSymmetry();
        }
        else if("clearwhites".equals(e.getActionCommand())){
            grid.clearWhiteSquares();
        }


    }

}
