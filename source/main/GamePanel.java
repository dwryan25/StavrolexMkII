
package main;

import javax.swing.*;
import java.awt.*;


public class GamePanel extends JPanel {

    final int origTileSize = 20;
    final int scale = 3;

    public final int tileSize = origTileSize * scale;
    final int maxScreenCol = 30;
    final int maxScreenRow = 18;

    final int screenWidth = tileSize*maxScreenCol;
    final int screenHeight = tileSize*maxScreenRow;

    private final int rows = 15;
    private final int cols = 15;


    KeyHandler keyH = new KeyHandler(this);

    public GamePanel(GridData grid, int cellSize){
        this.setPreferredSize(new Dimension(screenWidth,screenHeight));
        this.setBackground(Color.WHITE);
        this.setDoubleBuffered();
        this.addKeyListener(keyH);

        this.setFocusable(true);

    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;



    }
}//End class GamePanel



