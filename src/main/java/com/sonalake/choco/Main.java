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
        int[] arr = {9,16,25,36,49,64,81,100,121,144,169,196,225,256,289,324,361,400,441,484,529,576,625,676,729,784,841,900,961,1024};

        for(int number: arr){
            long startTime = System.currentTimeMillis();
            int size = number; // Size of the sudoku grid (size x size)
            ConstructorInt sudoku = new ConstructorInt(size);
            sudoku.createSudokuGrid();
            sudoku.removeCountCells((int) Math.floor((size * size) * 0.01));
            sudoku.removeCells(1, 1);

            sudoku.saveGridAsIntegerList("./grids/sudoku_grids_" + size);
            FileWriter writer = new FileWriter("./grids/time_generation", true);
            long endTime = System.currentTimeMillis();
            writer.write(size + " " + (endTime - startTime)+ "\n");
            writer.close();
            System.out.println("That took " + (endTime - startTime) + " milliseconds");
        }
    }
}
