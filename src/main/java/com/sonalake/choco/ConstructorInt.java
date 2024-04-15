package com.sonalake.choco;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.constraints.Constraint;
import org.chocosolver.solver.variables.IntVar;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ConstructorInt {

    private int[][] grid;

    public ConstructorInt(int size) {
        grid = new int[size][size];
    }

    public void createSudokuGrid() {
        fillGrid();
    }

    public int[][] getGrid() {
        return grid;
    }

    public void printSudokuGrid() {
        for (int[] row : grid) {
            for (int num : row) {
                System.out.print(num + " ");
            }
            System.out.println();
        }
    }

    public void removeCells(int min, int max) {
        int size = grid.length;
        int totalCells = size * size;
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < totalCells; i++) {
            indices.add(i);
        }
        Collections.shuffle(indices);
        int removed = 0;
        for (int index : indices) {
            int row = index / size;
            int col = index % size;
            int temp = grid[row][col];
            grid[row][col] = 0;
            if (!hasUniqueSolution()) {
                grid[row][col] = temp; // If removing the cell doesn't leave a unique solution, restore the cell
            } else {
                removed++;
                if (removed >= min && removed <= max) {
                    break; // Required number of cells removed
                }
            }
        }
    }

    private boolean hasUniqueSolution() {
        int size = grid.length;
        // Copy the current grid to avoid modifying the original
        int[][] copyGrid = new int[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(grid[i], 0, copyGrid[i], 0, size);
        }
        // Solve the Sudoku using Choco
        return solveSudoku(copyGrid);
    }

    private boolean solveSudoku(int[][] puzzle) {
        int size = puzzle.length;
        Model model = new Model("Sudoku");
        IntVar[][] vars = new IntVar[size][size];
        // Define variables
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (puzzle[i][j] == 0) {
                    vars[i][j] = model.intVar("Cell_" + i + "_" + j, 1, size);
                } else {
                    vars[i][j] = model.intVar("Cell_" + i + "_" + j, puzzle[i][j]);
                }
            }
        }
        // Define constraints
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (puzzle[i][j] == 0) {
                    // Row constraint
                    model.allDifferent(vars[i]).post();
                    // Column constraint
                    IntVar[] column = new IntVar[size];
                    for (int k = 0; k < size; k++) {
                        column[k] = vars[k][j];
                    }
                    model.allDifferent(column).post();
                    // Subgrid constraint
                    int subgridSize = (int) Math.sqrt(size);
                    int startRow = (i / subgridSize) * subgridSize;
                    int startCol = (j / subgridSize) * subgridSize;
                    IntVar[] subgrid = new IntVar[size];
                    int idx = 0;
                    for (int k = startRow; k < startRow + subgridSize; k++) {
                        for (int l = startCol; l < startCol + subgridSize; l++) {
                            subgrid[idx++] = vars[k][l];
                        }
                    }
                    model.allDifferent(subgrid).post();
                }
            }
        }
        // Search for a solution
        Solver solver = model.getSolver();
        return solver.solve();
    }

    private void fillGrid() {
        int size = grid.length;
        int subgridSize = (int) Math.sqrt(size);
        int[] numbers = new int[size];
        for (int i = 0; i < size; i++) {
            numbers[i] = i + 1;
        }
        shuffleArray(numbers);
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = numbers[(i * subgridSize + i / subgridSize + j) % size];
            }
        }
    }

    private void shuffleArray(int[] array) {
        List<Integer> list = new ArrayList<>();
        for (int value : array) {
            list.add(value);
        }
        Collections.shuffle(list);
        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i);
        }
    }
}
