package main;


public class GridData {
    private final int rows;
    private final int cols;

    private final char[][] cellValues;
    private final boolean[][] cellFilled;

    private int selectedRow;
    private int selectedCol;

    private boolean isSymmetrical = true;
    private boolean isHorizontal = true;


    public GridData(int rows, int cols){
        this.rows = rows;
        this.cols = cols;
        this.cellValues = new char[rows][cols];
        this.cellFilled = new boolean[rows][cols];

    }

    public void setCellChar(int row, int col, char input){
        cellValues[row][col] = input;
    }

    public void toggleCellBlack(int row, int col){
        if(isSymmetrical){
            int symRow = rows - 1 - selectedRow;
            int symCol = cols - 1 - selectedCol;
            if(symRow == row && symCol == col){
                cellFilled[row][col] = !cellFilled[row][col];
            }
            cellFilled[row][col] = !cellFilled[row][col];
            cellFilled[symRow][symCol] = !cellFilled[symRow][symCol];
        }
        else {
            cellFilled[row][col] = !cellFilled[row][col];
        }
    }

    public void advanceSelector(){
        if(isHorizontal){
            selectedCol = Math.clamp(selectedCol+1, 0, cols-1);
        }
        else {
            selectedRow = Math.clamp(selectedRow+1, 0, rows-1);
        }
    }

    public void backSelector(){
        cellValues[selectedRow][selectedCol] = '\0';
        if(isHorizontal){
            selectedCol = Math.clamp(selectedCol-1, 0, cols-1);
        }
        else {
            selectedRow = Math.clamp(selectedRow-1, 0, rows-1);
        }
    }

    public void moveSelector(int vert, int hor){
        if (vert == -1 || vert == 1){
            isHorizontal = false;
        }
        else{isHorizontal = true;
        }
        selectedRow = Math.clamp(selectedRow + vert, 0, rows - 1);
        selectedCol = Math.clamp(selectedCol + hor, 0, cols - 1);
    }
    public void setSelectedCell(int row, int col){
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            selectedRow = row;
            selectedCol = col;

        }
    }

    public char getCellChar(int row, int col){
        return cellValues[row][col];
    }

    public boolean isCellBlack(int row, int col){
        return cellFilled[row][col];
    }
    public boolean isDirectionHorizontal(){
        return isHorizontal;
    }

    public void switchDirection(){
        isHorizontal = !isHorizontal;
    }

    public String getWord(int[] indices){

        return null;
    }

    public int[] getHorizontalWordIndices(int row, int col){
        int start = -1;
        int end = 15;
        //Search backwards and forwards to find the first and last index. Stop at black squares or out of bounds.
        for (int i = col; i >= 0; i--){
            if(cellFilled[row][i]){
                start = i+1;
                break;
            }
        }
        for (int j = col; j <= 14; j++){
            if(cellFilled[row][j]){
                end = j-1;
                break;
            }
        }
        start = (start==-1) ? 0 : start;
        end = (end==15) ? 14 : end;
        return new int[]{start, end};
    }
    public int[] getVerticalWordIndices(int row, int col){
        int start = -1;
        int end = 15;
        //Search backwards and forwards to find the first and last index. Stop at black squares or out of bounds.
        for (int i = row; i >= 0; i--){
            if(cellFilled[i][col]){
                start = i+1;
                break;
            }
        }
        for (int j = row; j <= 14; j++){
            if(cellFilled[j][col]){
                end = j-1;
                break;
            }
        }
        start = (start==-1) ? 0 : start;
        end = (end==15) ? 14 : end;

        return new int[]{start, end};
    }


    public int getRows(){
        return rows;
    }
    public int getCols(){
        return cols;
    }

    public int getSelectedRow(){
        return selectedRow;
    }

    public int getSelectedCol(){
        return selectedCol;
    }
}
