package com.sonalake.choco;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        int size = 9; // Size of the sudoku grid (size x size)
        ConstructorInt sudoku = new ConstructorInt(size);
        sudoku.createSudokuGrid();
        sudoku.removeCountCells((int) Math.floor((size * size) * 0.1));
        sudoku.removeCells(1, 1);

        sudoku.saveGridAsIntegerList("sudoku_grids_" + size);
        long endTime = System.currentTimeMillis();
        System.out.println("That took " + (endTime - startTime) + " milliseconds");
    }
}
