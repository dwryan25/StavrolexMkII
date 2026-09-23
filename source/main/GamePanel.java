
package main;

import javax.swing.*;
import java.awt.*;


public class GamePanel extends JPanel {

    private final int rows = 15;
    private final int cols = 15;

    private final int cellSize;
    private final GridData grid;

    //Colors for grid and background
    private static final Color SELECTED_CELL = Color.RED;
    private static final Color BLACK_CELL = Color.BLACK;
    private static final Font CELL_FONT = new Font("Bold", Font.BOLD, 28);
    private static final Color TEXT_COLOR = Color.BLACK;


    public GamePanel(GridData grid, int cellSize){
        this.cellSize = cellSize;
        this.grid = grid;

        this.setPreferredSize(new Dimension(grid.getRows()*cellSize + 1,grid.getCols()*cellSize + 1));
        this.setBackground(Color.WHITE);



        this.setFocusable(true);


    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D) g;

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(CELL_FONT);
        FontMetrics fm = g2.getFontMetrics();

        int selectedRow = grid.getSelectedRow();
        int selectedCol = grid.getSelectedCol();


        for (int r = 0; r <15; r++){
            for (int c = 0; c <15;c++){
                //Draw the cell
                int x = cellSize*c;
                int y = cellSize*r;
                if (grid.isCellBlack(r,c)) {
                   g2.setColor(BLACK_CELL);
                   g2.fillRect(x, y, cellSize, cellSize);
                }
                else if(r == selectedRow && c == selectedCol){
                    g2.setColor(SELECTED_CELL);
                    g2.drawRect(x, y, cellSize - 1, cellSize - 1);
                }
                else{
                    g2.setColor(Color.BLACK);
                    g2.drawRect(x, y, cellSize - 1, cellSize - 1);
                }
                //Draw the character inside the cell
                char ch = grid.getCellChar(r, c);
                if(ch != '\0' && !grid.isCellBlack(r,c)){
                    g2.setColor(TEXT_COLOR);
                    int textWidth = fm.charWidth(ch);
                    int textHeight = fm.getAscent();
                    int textX = x + (cellSize-textWidth) / 2;
                    int textY = y + (cellSize+textHeight)/ 2;
                    g2.drawString(String.valueOf(ch), textX, textY);
                }

            }//end column loop
        }//End row loop
        //Draw the shaded line on selected row or column
        g2.setColor(Color.GREEN);
        int blockLength = grid.getBlockLength(selectedRow, selectedCol);
        System.out.println("Block length is " + blockLength + "\n");
        int[] blockPositions = grid.getBlock(selectedRow, selectedCol, blockLength);



        g2.dispose();
    }//end paintComponent

    public int getCellSize(){
        return cellSize;
    }
}//End class GamePanel



