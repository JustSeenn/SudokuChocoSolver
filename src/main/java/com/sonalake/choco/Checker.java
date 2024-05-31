package com.sonalake.choco;

import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Set;

public class Checker {

    public static void main(String[] args) {
        int size = 1024;
        int[] grind = Sudoku.readSudokuFromFile("./grids/sudoku_grids_"+size+".txt_result.txt", size);

        int[][] board = generateSudoku(grind, size);
        System.out.println(isSudokuValid(board));
    }

    public static int[][] generateSudoku(int[] input, int size) {
        int[][] grid = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = input[i * size + j];
            }
        }

        return grid;
    }

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
    // this function checks if the sudoku is valid or not by doing the following:
    // 1. check if the row has the same number
    // 2. check if the column has the same number
    // 3. check if the box has the same number
    // if any of the above conditions are true, then the sudoku is not valid

}
