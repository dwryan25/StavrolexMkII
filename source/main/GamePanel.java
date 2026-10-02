
package main;

import javax.swing.*;
import java.awt.*;


public class GamePanel extends JPanel {

    private final int cellSize;
    private final GridData grid;

    //Colors for grid and background
    private static final Color SELECTED_CELL = Color.RED;
    private static final Color BLACK_CELL = Color.BLACK;
    private static final Font CELL_FONT = new Font("Bold", Font.BOLD, 28);
    private static final Font CLUENUM_FONT = new Font("HyperText", Font.PLAIN, 12);
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
        grid.updateClueNumbers();

        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setFont(CELL_FONT);
        FontMetrics fmChar = g2.getFontMetrics(CELL_FONT);
        FontMetrics fmClueNum = g2.getFontMetrics(CLUENUM_FONT);

        int selectedRow = grid.getSelectedRow();
        int selectedCol = grid.getSelectedCol();
        boolean isHorizontal = grid.isDirectionHorizontal();
        BasicStroke borderStroke = new BasicStroke(3.0f);
        BasicStroke defStroke = new BasicStroke(1.0f);

        for (int r = 0; r <15; r++){
            for (int c = 0; c <15;c++){
                //Draw the cell
                int x = cellSize*c;
                int y = cellSize*r;
                int clueNum = grid.getCellClueNumber(r, c);
                g2.setStroke(defStroke);
                if (grid.isCellBlack(r,c)) {
                   g2.setColor(BLACK_CELL);
                   g2.fillRect(x, y, cellSize, cellSize);
                }
                else {
                    if(r == selectedRow && c == selectedCol){
                        g2.setColor(SELECTED_CELL);
                        g2.setStroke(borderStroke);
                    }
                    else{
                        g2.setColor(Color.BLACK);
                    }
                    g2.drawRect(x, y, cellSize - 1, cellSize - 1);
                    if(clueNum != 0) {
                        g2.setFont(CLUENUM_FONT);
                        int clueX = x + cellSize / 10;
                        int clueY = y + cellSize / 4;
                        g2.drawString(String.valueOf(clueNum), clueX, clueY);
                    }
                }
                //Draw the character inside the cell
                char ch = grid.getCellChar(r, c);
                if(ch != '\0' && !grid.isCellBlack(r,c)){
                    g2.setColor(TEXT_COLOR);
                    g2.setFont(CELL_FONT);
                    int textWidth = fmChar.charWidth(ch);
                    int textHeight = fmChar.getAscent();
                    int textX = x + (cellSize-textWidth) / 2;
                    int textY = y + (cellSize+textHeight)/ 2;
                    g2.drawString(String.valueOf(ch), textX, textY);

                }

            }//end column loop
        }//End row loop
        //Draw the shaded line on selected row or column
        int[] wordIndices;
        g2.setColor(Color.BLUE);
        wordIndices = isHorizontal ? grid.getHorizontalWordIndices(selectedRow, selectedCol) : grid.getVerticalWordIndices(selectedRow, selectedCol);
        int start = wordIndices[0];
        int end = wordIndices[1];
        int blockLength = end - start + 1;
        System.out.printf(" index %d to %d \n", wordIndices[0], wordIndices[1]);
        System.out.println("Block length is " + blockLength + "\n");
        if(isHorizontal){
            while(start <= end){
                if(start == selectedCol){start++; continue;}
                int x = start * cellSize;
                int y = selectedRow * cellSize;
                g2.drawRect(x, y, cellSize - 1, cellSize - 1);
                start++;
        }
        }
        else{
            while(start <= end){
                if (start == selectedRow){start++; continue;}
                int x = selectedCol * cellSize;
                int y = start * cellSize;
                g2.drawRect(x, y, cellSize - 1, cellSize - 1);
                start++;

            }
        }

        g2.dispose();
    }//end paintComponent

    public int getCellSize(){
        return cellSize;
    }
}//End class GamePanel



