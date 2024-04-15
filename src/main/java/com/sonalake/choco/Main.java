package com.sonalake.choco;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

public class Main {

    public static void main(String[] args) {
        int size = 25; // Taille de la grille de Sudoku (modifiable)
        ConstructorInt sudoku = new ConstructorInt(size);
        sudoku.createSudokuGrid();
        System.out.println("------------------------------------------------------------");
        sudoku.removeCells((size * size) - 27, (size * size) - 17);
        int[][] grid = sudoku.getGrid();
        // print all the grids
        for (int[] row : grid) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }

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
