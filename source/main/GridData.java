package main;


public class GridData {
    private final int rows;
    private final int cols;

    private final char[][] cellValues;
    private final boolean[][] cellFilled;
    private final int[][] clueNumbers;

    private int selectedRow;
    private int selectedCol;

    private boolean isSymmetrical = true;
    private boolean isHorizontal = true;


    public GridData(int rows, int cols){
        this.rows = rows;
        this.cols = cols;
        this.cellValues = new char[rows][cols];
        this.cellFilled = new boolean[rows][cols];
        this.clueNumbers = new int[rows][cols];

    }

    public void setCellChar(int row, int col, char input){
        cellValues[row][col] = input;
    }

    public void toggleSymmetry(){
        isSymmetrical = !isSymmetrical;
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
            clueNumbers[row][col] = 0;
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
        //Logical sequence checks if current direction matches the cursor movement. If it does not then the direction is simply switched.
        if((isHorizontal && (vert == 1 || vert ==-1)) || (!isHorizontal && (hor == 1 || hor ==-1))){
            isHorizontal = !isHorizontal;
        }
        else{
            selectedRow = Math.clamp(selectedRow + vert, 0, rows - 1);
            selectedCol = Math.clamp(selectedCol + hor, 0, cols - 1);
        }
    }


    public void setSelectedCell(int row, int col){
        if (row >= 0 && row < rows && col >= 0 && col < cols) {
            if (row == selectedRow && col == selectedCol){
                switchDirection();
                return;
            }
            selectedRow = row;
            selectedCol = col;
        }
    }

    /*public void updateClueNumbers(){
        int clueCounter = 1;
        for( int i = 0; i < 15; i++){
            for(int j = 0; j < 15; j++){
                if(i-1 < 0 || j-1 < 0){
                    if(cellFilled[i][j]){
                        clueNumbers[i][j] = 0;
                    }
                    else {
                        clueNumbers[i][j] = clueCounter;
                        clueCounter++;
                    }
               }//accounts for edge of grid cases
               else if ((cellFilled[i][j-1] || cellFilled[i-1][j]) && !cellFilled[i][j]){
                   clueNumbers[i][j] = clueCounter;
                   clueCounter++;
               }//adjacent to filled square cases
               else if (cellFilled[i][j]){
                   clueNumbers[i][j] = 0;
                }//filled cases
               else{
                   clueNumbers[i][j] = 0;
                }//middle of grid cases

            }
        }
    }*/


    public void updateClueNumbers(){
        int clueCounter = 1;

        for (int i = 0; i < 15; i++){
            for (int j = 0; j <15; j++){


                //Condition: If a cell's left adjacent cell is filled or null-> mark it as an across start
                boolean acrossStart = (j == 0 || cellFilled[i][j-1]) && (j+1 < cols && !cellFilled[i][j+1]);
                //Condition: If a cell's upper adjacent cell is filled or null -> mark it as an across start
                boolean downStart = (i == 0 || cellFilled[i-1][j]) && (i+1 < rows && !cellFilled[i+1][j]);
                //If either or both is true-> assign the cell a clue number and increment counter
                if(acrossStart || downStart){
                    clueNumbers[i][j] = clueCounter;
                    clueCounter++;
                }
                else if (cellFilled[i][j]){
                    clueNumbers[i][j] = 0;
                }
                else{
                    clueNumbers[i][j] = 0;
                }
            }
        }
    }

    public String getAcrossWord(int row, int col){
        int start;
        int end;
        StringBuilder st = new StringBuilder();
        //if string is horizontal find the word based on the columns
        int[] acrossIndices = getHorizontalWordIndices(row, col);
        start = acrossIndices[0];
        end = acrossIndices[1];

        while(start <= end){
            char cellVal = getCellChar(row, start);
            if(cellVal == '\0'){
                st.append('-');
            }
            else{
                st.append(cellVal);
            }
            start++;
        }
        return st.toString();
    }


    public String getDownWord(int row, int col){
        int start;
        int end;
        StringBuilder st = new StringBuilder();
        int[] acrossIndices = getVerticalWordIndices(row, col);
        start = acrossIndices[0];
        end = acrossIndices[1];
        while(start <= end){
            char cellVal = getCellChar(start, col);
            if(cellVal == '\0'){
                st.append('-');
            }
            else{
                st.append(cellVal);
            }
            start++;
        }
        return st.toString();
    }

    public int[] getHorizontalWordIndices(int row, int col){
        if(cellFilled[row][col]){
            return new int[]{0,0};
        }

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


    public int getCellClueNumber(int row, int col) {
        return clueNumbers[row][col];
    }

    public int getHorizontalClueNumber(int row, int col){
        return clueNumbers[row][getHorizontalWordIndices(row, col)[0]];
    }

    public int getVerticalClueNumber(int row, int col){
        return clueNumbers[getVerticalWordIndices(row, col)[0]][col];
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
