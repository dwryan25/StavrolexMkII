package main;

public class GridData {
    private final int rows;
    private final int cols;

    private char[][] cellValues;
    private boolean[][] cellFilled;

    private int selectedRow;
    private int selectedCol;


    public GridData(int rows, int cols){
        this.rows = rows;
        this.cols = cols;
        this.cellValues = new char[rows][cols];
        this.cellFilled = new boolean[rows][cols];
    }

    public setCellChar(){

    }

    public getCellChar(){


    }
}
