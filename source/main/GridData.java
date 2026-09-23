package main;

public class GridData {
    private final int rows;
    private final int cols;

    private final char[][] cellValues;
    private final boolean[][] cellFilled;

    private int selectedRow;
    private int selectedCol;

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
        if (cellFilled[row][col]) {
            cellFilled[row][col] = false;
        }
        else{
            cellFilled[row][col] = true;
        }
    }

    public void moveSelector(int vert, int hor){
        if (vert == -1 || vert == 1){
            isHorizontal = false;
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



    public int getBlockLength(int row, int col){
        System.out.println("Calling getBlockLength");
        int blockLength = 0;
        int backPointer,frontPointer;
        if(isHorizontal) {
            backPointer = col;
            frontPointer = col;
        }
        else{
            backPointer = row;
            frontPointer = row;
        }
        if(cellFilled[row][col]) {
            return 0;
        }
        else if(isHorizontal) {
            while (!cellFilled[row][backPointer] || !cellFilled[row][frontPointer]) {
                System.out.printf("backPointer: %d and frontPointer %d\n", backPointer, frontPointer);
                if (!cellFilled[row][frontPointer] && frontPointer < 15) {
                    System.out.println("Passed front null or black square check");
                    blockLength++;
                    frontPointer++;
                }
                if (!cellFilled[row][backPointer] && backPointer > 0) {
                    System.out.println("Passed back null or black square check");
                    blockLength++;
                    backPointer--;
                }
                if(frontPointer == 15 || backPointer == 0){
                    return cols;
                }
            }
        }
        else {
            while (!cellFilled[backPointer][col] || !cellFilled[frontPointer][col]){
                if(!cellFilled[frontPointer][col] && frontPointer < 15){
                    blockLength++;
                    frontPointer++;
                }
                if(!cellFilled[row][backPointer] && backPointer > 0){
                    blockLength++;
                    backPointer--;
                }
                if(backPointer == 0 || frontPointer == 15){
                    return cols;
                }
            }
        }
        return blockLength;
    }

    public int[] getBlock(int row, int col, int blockLength){
        int[] adjacentCells = new int[blockLength];
        int backPointer;

        if(cellFilled[row][col]) {
            return null;
        }
        else if(isHorizontal){
            //backtrack to find the first cell
            backPointer = col;
            while(!cellFilled[row][backPointer] && backPointer > 0){
                backPointer--;
                if(backPointer == 0){
                    break;
                }
            }
            //Loop through the whole block and capture each row or col val in an array
            while(!cellFilled[row][backPointer]){
                adjacentCells[backPointer] = backPointer;
                backPointer++;
                if(backPointer == 14){
                    break;
                }
            }
        }
        return adjacentCells;
    }//end getBlock



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
