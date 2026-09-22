package main;

public class GridData {
    private final int rows;
    private final int cols;

    private final char[][] cellValues;
    private final boolean[][] cellFilled;

    private int selectedRow;
    private int selectedCol;

    private enum direction {HORIZONTAL,VERTICAL;
        private direction toggle(){
            return this == HORIZONTAL ? VERTICAL : HORIZONTAL;
        }
    };



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
        if (cellFilled[row][col]) {
            cellFilled[row][col] = false;
        }
        else{
            cellFilled[row][col] = true;
        }
    }

    public void moveSelector(int vert, int hor){
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
