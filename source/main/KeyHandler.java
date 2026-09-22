package main;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;


public class KeyHandler extends KeyAdapter {
    private final GamePanel gp;
    private final GridData grid;


    public KeyHandler(GridData grid, GamePanel gp){
        this.gp = gp;
        this.grid = grid;

        attachListeners();
    }


    private void attachListeners() {
        gp.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mouseClicked(e);
                gp.requestFocusInWindow();

                int row = e.getY() / gp.getCellSize();
                int col = e.getX() / gp.getCellSize();

                grid.setSelectedCell(row, col);

                gp.repaint();
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


                    default -> {
                        System.out.println("Starting character input check");
                        char input = Character.toUpperCase(e.getKeyChar());
                        if(Character.isLetterOrDigit(input) && !grid.isCellBlack(row, col)){
                            System.out.println("setting char");
                            grid.setCellChar(row, col, input);
                        }
                    }


                }
                gp.repaint();
            }//end keyPressed

        });
    }//end attachListeners
}//end class KeyHandler
