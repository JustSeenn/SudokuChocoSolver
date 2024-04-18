package com.sonalake.choco;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Main {

    public static void main(String[] args) throws IOException {
        long startTime = System.currentTimeMillis();
        int size = 1000; // Size of the sudoku grid (size x size)
        ConstructorInt sudoku = new ConstructorInt(size);
        sudoku.createSudokuGrid();
        // sudoku.removeCountCells(1000); // number of cells to remove bruteforce
        sudoku.removeCountCellsSymmetry(500);
        // number of cells to remove with backtracking after bruteforce
        // ex: 2000 bruteforce + 1000 = 3000 here
        sudoku.removeCells(2000, 2100);
        sudoku.printSudokuGrid();
        sudoku.saveGridAsIntegerList("sudoku_grids_9"); // save the grid as a list of integers for sudoku solver
        long endTime = System.currentTimeMillis();
        System.out.println("That took " + (endTime - startTime) + " milliseconds");
    }

    // public static void main(String[] args) {
    // int size = 16;
    // // read from file
    // SudokuReader reader = new SudokuReader();
    // try {
    // int[][] grids = reader.readSudokuGridFromFile("sudoku_grids_16.txt", size);

    // if (grids != null) {
    // SolutionInt solver = new SolutionInt(size);
    // solver.printSudokuGrid(grids);

    // solver.solveSudoku(grids);
    // System.out.println("------------------------------------------------------------");
    // solver.printSudokuGrid(grids);
    // System.out.println("------------------------------------------------------------");
    // System.out.println(isSudokuValid(grids) + " is the sudoku valid");

    // } else {
    // System.out.println("No sudoku grids found in the file.");
    // }
    // } catch (FileNotFoundException e) {
    // System.err.println("File not found.");
    // e.printStackTrace();
    // }
    // }

    public static boolean isSudokuValid(int[][] board) {
        int size = board.length;
        int root = (int) Math.sqrt(size);
        Set<String> seen = new HashSet<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int num = board[i][j];
                if (num != 0) {
                    if (!seen.add(num + "row" + i) || !seen.add(num + "col" + j)
                            || !seen.add(num + "box" + i / root + "-" + j / root)) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

}
