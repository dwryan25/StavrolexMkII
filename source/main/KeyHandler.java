package main;

import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;


public class KeyHandler extends KeyAdapter {
    private final GamePanel gp;
    private final WordPanel wp;
    private final GridData grid;



    public KeyHandler(GridData grid, GamePanel gp, WordPanel wp){
        this.gp = gp;
        this.grid = grid;
        this.wp = wp;

        attachGPListeners();
    }

    private void attachGPListeners() {
        gp.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                gp.requestFocusInWindow();

                int row = e.getY() / gp.getCellSize();
                int col = e.getX() / gp.getCellSize();

                grid.setSelectedCell(row, col);
                gp.repaint();
                try {
                    wp.updateWordPanel();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        gp.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed (KeyEvent e){
                int row = grid.getSelectedRow();
                int col = grid.getSelectedCol();

                switch (e.getKeyCode()) {
                    case KeyEvent.VK_PERIOD -> grid.toggleCellBlack(row,col);
                    case KeyEvent.VK_RIGHT -> grid.moveSelector(0, 1);
                    case KeyEvent.VK_LEFT -> grid.moveSelector(0, -1);
                    case KeyEvent.VK_UP -> grid.moveSelector(-1, 0);
                    case KeyEvent.VK_DOWN -> grid.moveSelector(1, 0);
                    case KeyEvent.VK_BACK_SPACE -> grid.backSelector();
                    case KeyEvent.VK_SPACE -> grid.switchDirection();


                    default -> {
                        System.out.println("Starting character input check");
                        char input = Character.toUpperCase(e.getKeyChar());
                        if(Character.isLetterOrDigit(input) && !grid.isCellBlack(row, col)){
                            System.out.println("setting char");
                            grid.setCellChar(row, col, input);
                            grid.advanceSelector();
                        }
                    }
                }

                gp.repaint();
                try {
                    wp.updateWordPanel();
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }//end keyPressed

        });
    }//end attachListener


}//end class KeyHandler
