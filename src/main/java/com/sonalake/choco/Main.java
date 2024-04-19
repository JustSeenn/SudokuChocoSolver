package com.sonalake.choco;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        int size = 9; // Size of the sudoku grid (size x size)
        ConstructorInt sudoku = new ConstructorInt(size);
        sudoku.createSudokuGrid();
        sudoku.removeCountCells(50);

        sudoku.removeCells(10, 11);
        sudoku.printSudokuGrid();
        sudoku.saveGridAsIntegerList("sudoku_grids_9");
        long endTime = System.currentTimeMillis();
        System.out.println("That took " + (endTime - startTime) + " milliseconds");
    }
}
