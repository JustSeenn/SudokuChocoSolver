package com.sonalake.choco;

import java.io.FileWriter;
import java.io.IOException;

public class Main {

    public static void main2(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        int size = 900; // Size of the sudoku grid (size x size)
        ConstructorInt sudoku = new ConstructorInt(size);
        sudoku.createSudokuGrid();
        sudoku.removeCountCells((int) Math.floor((size * size) * 0.1));
        sudoku.removeCells(1, 1);

        sudoku.saveGridAsIntegerList("sudoku_grids_" + size);
        long endTime = System.currentTimeMillis();
        System.out.println("That took " + (endTime - startTime) + " milliseconds");
    }

    public static void main(String[] args) throws IOException {
        int[] arr = {1024};
        for(int number: arr){
            long startTime = System.currentTimeMillis();
            int size = number; // Size of the sudoku grid (size x size)
            ConstructorInt sudoku = new ConstructorInt(size);
            sudoku.createSudokuGrid();
            // sudoku.removeCountCells((int) Math.floor((size * size) * 0.01));
            sudoku.removeCountCells(2);


            sudoku.saveGridAsIntegerList("./grids/sudoku_grids_" + size);
            FileWriter writer = new FileWriter("./grids/time_generation", true);
            long endTime = System.currentTimeMillis();
            writer.write(size + " " + (endTime - startTime)+ "\n");
            writer.close();
            System.out.println("That took " + (endTime - startTime) + " milliseconds");
        }
    }
}
