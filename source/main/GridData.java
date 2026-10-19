package main;


import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.nio.file.Path;
import java.io.File;
import java.io.IOException;

public class GridData {
    private final int rows;
    private final int cols;

    private final char[][] cellValues;
    private final boolean[][] cellFilled;
    private final int[][] clueNumbers;

    private final Map<String, String> clues;

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

        this.clues = new HashMap<>(100);

    }

    public void setCellChar(int row, int col, char input){
        cellValues[row][col] = input;
    }

    public void setClue(String key, String clue){
        clues.put(key, clue);
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
            clueNumbers[symRow][symCol] = 0;

        }
        else {
            cellFilled[row][col] = !cellFilled[row][col];
            clueNumbers[row][col] = 0;
        }
        cellValues[row][col] = '\0';
        advanceSelector();
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



    public void updateClueNumbers(){
        int clueCounter = 1;

        for (int i = 0; i < rows; i++){
            for (int j = 0; j < cols; j++){

                //Condition: If a cell's left adjacent cell is filled or null-> mark it as an across start
                boolean acrossStart = (j == 0 || cellFilled[i][j-1]) && (j+1 < cols && !cellFilled[i][j+1]);
                //Condition: If a cell's upper adjacent cell is filled or null -> mark it as an across start
                boolean downStart = (i == 0 || cellFilled[i-1][j]) && (i+1 < rows && !cellFilled[i+1][j]);
                //If either or both is true-> assign the cell a clue number and increment counter
                if(acrossStart || downStart){
                    clueNumbers[i][j] = clueCounter;
                    clueCounter++;
                }
                //If cell is black -> assign 0
                else if (cellFilled[i][j]){
                    clueNumbers[i][j] = 0;
                }
                //Middle of grid cases -> assign 0
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
        int end = cols;
        //Search backwards and forwards to find the first and last index. Stop at black squares or out of bounds.
        for (int i = col; i >= 0; i--){
            if(cellFilled[row][i]){
                start = i+1;
                break;
            }
        }
        for (int j = col; j < cols; j++){
            if(cellFilled[row][j]){
                end = j-1;
                break;
            }
        }
        start = (start==-1) ? 0 : start;
        end = (end==cols) ? cols-1 : end;
        return new int[]{start, end};
    }
    public int[] getVerticalWordIndices(int row, int col){
        if(cellFilled[row][col]){
            return new int[]{0,0};
        }
        int start = -1;
        int end = 15;
        //Search backwards and forwards to find the first and last index. Stop at black squares or out of bounds.
        for (int i = row; i >= 0; i--){
            if(cellFilled[i][col]){
                start = i+1;
                break;
            }
        }
        for (int j = row; j < rows; j++){
            if(cellFilled[j][col]){
                end = j-1;
                break;
            }
        }
        start = (start==-1) ? 0 : start;
        end = (end==rows) ? rows-1 : end;

        return new int[]{start, end};
    }

    public void clearPuzzle(){
        for(int i = 0; i < rows; i ++){
            for (int j = 0; j < cols; j++){
                clueNumbers[i][j] = 0;
                cellValues[i][j] = '\0';
                cellFilled[i][j] = false;
            }
        }
        clues.clear();
    }


    public int getCellClueNumber(int row, int col) {
        return clueNumbers[row][col];
    }

    public int getHorizontalClueNumber(int row, int col){
        if(cellFilled[row][col]){
            return 0;
        }
        return clueNumbers[row][getHorizontalWordIndices(row, col)[0]];
    }

    public int getVerticalClueNumber(int row, int col){
        if(cellFilled[row][col]){
            return 0;
        }
        return clueNumbers[getVerticalWordIndices(row, col)[0]][col];
    }

    public void saveTo(Path file) throws IOException {
        List<String> lines = new ArrayList<>();
        lines.add(rows + " " + cols);
        for(int r = 0; r < rows; r++){
            StringBuilder sb = new StringBuilder();
            for(int c = 0; c < cols; c++){
                sb.append(isCellBlack(r, c) ? "#" : isCellEmpty(r, c) ? "." : getCellChar(r, c));
            }
            lines.add(sb.toString());
        }
        clues.forEach((key, val) -> lines.add(key + ":" + val));

        Files.write(file, lines);
    }

    public void loadFrom(File file) throws IOException {
        List<String> lines = Files.readAllLines(file.toPath());
        if(lines.isEmpty()){throw new IOException("File is empty");}

        String[] size = lines.get(0).trim().split("\\s+");
        System.out.println(size[0] + " " + size[1]);
        try {
            if(size.length != 2 || Integer.parseInt(size[0]) != rows || Integer.parseInt(size[1]) != cols){
                System.out.println("File says grid size is " + Integer.parseInt(size[0]) + " " + Integer.parseInt(size[1]));
                throw new IOException("Wrong grid size");
            }
        } catch (NumberFormatException e){
            System.out.println("File says grid size is " + Integer.parseInt(size[0]) + " " + Integer.parseInt(size[1]));
            throw new IOException("Bad Header Line", e);
        }

        //Check grid lines
        if(lines.size() < rows + 1) {throw new IOException("Missing grid rows");}
        //Copy of textfile data in char array
        char[][] parsed = new char[rows][];
        for(int r = 0; r < rows; r++){
            String line = lines.get(r+1);
            if(line.length() != cols){throw new IOException("Column length does not match this grid");}
            parsed[r] = line.toCharArray();
        }

        Map<String, String> parsedClues = new HashMap<>();
        for(int i = 1+rows; i < lines.size(); i++){
            String line = lines.get(i);
            if(line.isBlank()) continue;
            int split = line.indexOf(':');
            if(split < 0) throw new IOException("Bad clue line");
            parsedClues.put(line.substring(0, split), line.substring(split+1));
        }
        clearPuzzle();

        for(int i = 0; i < rows; i++){
            for(int j = 0; j < cols; j++){
                char ch = parsed[i][j];
                if(ch == '#'){
                    toggleCellBlack(i, j);
                }
                else if (Character.isLetterOrDigit(ch)){
                    setCellChar(i, j, ch);
                }
            }
        }
        clues.putAll(parsedClues);
        setSelectedCell(0, 0);

        updateClueNumbers();
    }



    public String getClue(String key){
        return clues.getOrDefault(key, "(Your Clue)");
    }

    public char getCellChar(int row, int col){
        return cellValues[row][col];
    }

    public boolean isCellBlack(int row, int col){
        return cellFilled[row][col];
    }

    public boolean isCellEmpty(int row, int col){ return !cellFilled[row][col] && cellValues[row][col] == '\0';}

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
