package com.sonalake.choco;

import de.vandermeer.asciitable.AsciiTable;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.solver.variables.impl.FixedIntVarImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.lang.String.format;

import java.io.FileWriter;
import java.io.IOException;

/**
 * Given a graph, how can we colour each node so that no other nodes connect to
 * each other, and what is the minimum
 * number of nodes
 */
public class Sudoku {

  private static final int SIZE = 36;
  private static final int SQUARE_SIZE = 6;
  private static final int MIN_VALUE = 1;
  private static final int MAX_VALUE = SIZE;

  static public void main(String... args) throws IOException {

    int[] stringGrind = { 5, 11, 0, 0, 0, 32, 24, 19, 8, 0, 28, 0, 0, 0, 4, 29, 0, 34, 14, 0, 3, 26, 22, 0, 0, 0, 9, 0,
        0, 0, 33, 35, 2, 25, 0, 27, 0, 24, 19, 0, 15, 0, 0, 0, 0, 18, 0, 4, 14, 0, 0, 0, 22, 16, 0, 0, 0, 12, 36, 0, 0,
        27, 33, 0, 0, 25, 21, 32, 5, 0, 31, 0, 4, 29, 0, 0, 0, 17, 0, 22, 0, 0, 0, 0, 9, 20, 0, 12, 36, 6, 33, 35, 0,
        25, 0, 27, 0, 7, 0, 0, 5, 0, 15, 0, 30, 0, 19, 8, 0, 26, 22, 0, 0, 13, 0, 0, 0, 0, 0, 1, 0, 0, 2, 25, 0, 27, 21,
        0, 5, 11, 0, 7, 0, 0, 15, 28, 0, 24, 0, 17, 0, 0, 23, 0, 0, 0, 36, 6, 9, 20, 25, 0, 27, 0, 35, 0, 0, 0, 5, 11,
        0, 0, 15, 0, 30, 24, 0, 0, 23, 34, 18, 17, 4, 0, 14, 13, 3, 0, 0, 0, 2, 0, 10, 0, 0, 0, 11, 0, 7, 0, 32, 0, 0,
        28, 30, 24, 0, 8, 18, 0, 0, 29, 23, 34, 0, 16, 14, 13, 0, 26, 0, 20, 0, 0, 0, 6, 25, 0, 0, 33, 32, 0, 0, 0, 21,
        0, 0, 0, 0, 0, 0, 19, 0, 15, 13, 4, 29, 0, 34, 0, 0, 0, 0, 0, 26, 22, 0, 1, 0, 36, 0, 0, 0, 0, 0, 0, 0, 5, 0, 0,
        0, 0, 0, 0, 0, 0, 0, 23, 0, 0, 20, 3, 0, 22, 0, 0, 0, 9, 35, 1, 12, 0, 0, 2, 25, 10, 0, 0, 24, 0, 0, 0, 17, 0,
        0, 0, 0, 0, 0, 29, 0, 0, 26, 22, 16, 0, 0, 1, 0, 0, 0, 0, 0, 0, 32, 2, 25, 10, 28, 5, 0, 0, 7, 21, 0, 23, 34,
        18, 0, 4, 22, 16, 0, 0, 3, 0, 35, 0, 0, 0, 6, 9, 32, 0, 25, 0, 27, 0, 0, 0, 28, 0, 0, 0, 17, 0, 24, 0, 0, 15,
        26, 22, 0, 14, 20, 0, 36, 6, 9, 35, 1, 0, 0, 2, 0, 0, 0, 33, 0, 0, 11, 0, 7, 0, 8, 15, 17, 30, 24, 19, 13, 0, 0,
        23, 34, 18, 0, 0, 0, 9, 0, 0, 10, 27, 33, 0, 2, 0, 0, 0, 11, 31, 0, 0, 0, 0, 0, 19, 8, 0, 34, 0, 0, 0, 29, 0, 0,
        0, 0, 0, 16, 14, 22, 0, 14, 0, 1, 0, 6, 0, 0, 2, 12, 0, 0, 25, 10, 27, 33, 0, 0, 11, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 3, 29, 23, 0, 18, 0, 36, 6, 0, 0, 2, 0, 0, 0, 32, 0, 25, 0, 30, 0, 31, 0, 21, 28, 0, 0, 0, 8, 15, 0, 18, 13,
        3, 29, 23, 0, 1, 0, 0, 16, 0, 0, 10, 27, 33, 32, 5, 0, 7, 0, 28, 30, 0, 31, 4, 0, 0, 0, 0, 17, 0, 29, 23, 0, 18,
        13, 0, 20, 1, 26, 0, 16, 0, 0, 36, 0, 9, 0, 31, 7, 0, 28, 30, 11, 0, 15, 17, 4, 24, 19, 0, 0, 0, 34, 0, 0, 0, 0,
        22, 0, 14, 0, 0, 0, 0, 12, 36, 6, 0, 0, 0, 27, 0, 32, 19, 0, 0, 0, 0, 24, 0, 18, 13, 0, 29, 0, 0, 0, 22, 16, 0,
        20, 2, 12, 0, 6, 0, 35, 0, 0, 5, 25, 10, 27, 30, 11, 0, 7, 0, 0, 23, 0, 0, 13, 0, 0, 16, 0, 20, 1, 0, 22, 0, 0,
        0, 6, 9, 0, 5, 25, 0, 0, 0, 0, 21, 28, 30, 0, 31, 7, 4, 24, 19, 8, 0, 0, 8, 15, 0, 4, 0, 0, 0, 0, 0, 26, 23, 0,
        12, 0, 16, 14, 20, 1, 25, 0, 6, 9, 0, 2, 32, 0, 0, 10, 27, 0, 0, 31, 0, 0, 28, 30, 0, 18, 0, 3, 26, 0, 14, 20,
        0, 0, 0, 0, 0, 0, 6, 0, 35, 2, 11, 0, 27, 0, 32, 0, 0, 30, 0, 31, 0, 0, 0, 19, 0, 0, 17, 0, 16, 0, 0, 1, 0, 0,
        9, 35, 0, 25, 0, 6, 11, 0, 27, 0, 0, 5, 24, 31, 0, 21, 0, 30, 17, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 9, 35, 0,
        0, 36, 33, 0, 5, 0, 10, 0, 0, 0, 0, 21, 28, 0, 0, 19, 8, 15, 17, 0, 13, 0, 0, 23, 34, 0, 12, 0, 0, 14, 20, 0,
        27, 0, 32, 0, 0, 10, 0, 28, 30, 0, 0, 7, 29, 19, 8, 15, 0, 4, 0, 0, 0, 0, 0, 0, 0, 1, 12, 22, 0, 0, 0, 0, 0, 0,
        35, 2, 0, 21, 28, 30, 0, 0, 0, 17, 4, 0, 0, 8, 26, 23, 0, 0, 0, 0, 12, 0, 0, 0, 20, 1, 35, 2, 25, 36, 6, 0, 11,
        10, 0, 33, 32, 0, 0, 0, 0, 12, 0, 16, 35, 2, 25, 10, 0, 0, 0, 27, 0, 32, 0, 0, 0, 0, 0, 28, 30, 24, 4, 29, 0, 8,
        0, 0, 0, 0, 0, 0, 0, 0, 9, 0, 2, 0, 10, 0, 32, 0, 11, 31, 0, 33, 0, 0, 0, 0, 0, 24, 23, 8, 0, 17, 4, 0, 3, 0,
        22, 0, 18, 0, 0, 0, 0, 0, 1, 12, 33, 0, 5, 11, 31, 0, 28, 0, 24, 19, 0, 0, 23, 8, 0, 17, 4, 29, 0, 0, 18, 0, 0,
        26, 1, 0, 0, 16, 14, 20, 0, 6, 9, 0, 0, 0, 0, 28, 0, 0, 19, 7, 0, 4, 0, 0, 8, 15, 0, 0, 18, 0, 0, 0, 36, 16, 14,
        0, 1, 0, 0, 25, 0, 6, 9, 35, 31, 27, 0, 0, 5, 11, 0, 17, 0, 29, 23, 0, 0, 3, 26, 22, 34, 18, 36, 16, 14, 20, 0,
        0, 0, 6, 0, 0, 0, 0, 5, 0, 0, 0, 33, 32, 19, 0, 0, 0, 0, 0, 0, 13, 3, 26, 0, 34, 20, 0, 12, 36, 0, 0, 0, 0, 0,
        35, 0, 0, 0, 27, 0, 0, 5, 0, 30, 24, 0, 0, 21, 0, 0, 8, 0, 17, 0, 0, 0, 0, 0, 23, 0, 15, 3, 26, 0, 0, 18, 13, 0,
        14, 0, 0, 12, 0, 27, 0, 35, 2, 0, 0, 11, 31, 7, 33, 0, 0, 8, 0, 0, 0, 0, 0, 13, 3, 26, 22, 16, 0, 1, 12, 36, 0,
        14, 20, 27, 9, 0, 0, 25, 0, 7, 0, 0, 0, 0, 31, 0, 0, 0, 0, 28, 0, 0, 15, 17, 4, 0, 23, 20, 0, 12, 36, 6, 14, 0,
        0, 10, 27, 0, 0, 7, 33, 32, 5, 11, 0, 0, 21, 28, 30, 0, 0, 29, 23, 34, 15, 0, 0, 0, 18, 13, 0, 0, 0, 0, 0, 25,
        0, 27, 0, 0, 0, 31, 0, 0, 0, 0, 21, 28, 30, 24, 19, 0, 0, 0, 0, 0, 0, 26, 0, 16, 0, 0, 3, 6, 0, 20, 0, 0, 0, 0,
        0, 0, 31, 7, 33, 0, 24, 19, 8, 0, 0, 34, 0, 0, 0, 29, 0, 0, 0, 0, 0, 26, 22, 12, 0, 6, 0, 20, 1, 27, 0, 0, 0, 0,
        0, 28, 0, 0, 19, 8, 0, 0, 0, 23, 0, 15, 17, 0, 18, 13, 0, 0, 22, 0, 0, 0, 1, 0, 36, 0, 0, 0, 9, 0, 2, 0, 0, 32,
        0, 11, 31
    };

    int[][] sudokuGrid = generateSudoku(stringGrind, SIZE);

    System.out.println("Size of the grid: " + sudokuGrid.length + "x" + sudokuGrid[0].length);

    // check that it's a square matrix
    for (int i = 0; i < sudokuGrid.length; i++) {
      if (sudokuGrid[i].length != sudokuGrid.length) {
        throw new IllegalArgumentException("The input matrix is not a square matrix " + i);
      }
    }
    // build the variables and constraint models
    Model model = new Model("sudoku");
    IntVar[][] grid = buildGrid(model, sudokuGrid);
    applyConnectionConstraints(model, grid);

    // print out the problem
    printGrid(grid, false);

    // solve it
    Solver solver = model.getSolver();
    solver.showShortStatistics();
    solver.setSearch(Search.minDomLBSearch(flatten(grid)));
    solver.solve();

    // print out the solution
    printGrid(grid, true);
  }

  public static IntVar[] flatten(IntVar[][] board) {
    IntVar[] flat = new IntVar[board.length * board[0].length];
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[0].length; j++) {
        flat[i * board[0].length + j] = board[i][j];
      }
    }
    return flat;
  }

  /**
   * Build a grid in the form of [row][column]. Where we have a fixed value we
   * just use a simple intvar.
   * Where we have a 0 (i.e. an unknown) we put it a bounded intvar (from 1->9)
   *
   * @param model          the model into which the variables will be created
   * @param predefinedRows the predefined values
   * @return the created grid of variables.
   */
  private static IntVar[][] buildGrid(Model model, int[][] predefinedRows) {
    // this grid will contain variables in the same shape as the input
    IntVar[][] grid = new IntVar[SIZE][SIZE];

    // check all the predefined values
    // if they're 0: create them as bounded variables across the colour range (1-9)
    // otherwise create them as a constance
    for (int row = 0; row != SIZE; row++) {
      for (int col = 0; col != SIZE; col++) {
        // print predefinedRows[row]
        int value = predefinedRows[row][col];
        // is this an unknown? if so then create it as a bounded variable
        if (value < MIN_VALUE) {
          grid[row][col] = model.intVar(format("[%s.%s]", row, col), MIN_VALUE, MAX_VALUE);
        } else {
          // otherwise we have an actual value, so create it as a constant
          grid[row][col] = model.intVar(value);
        }
      }
    }

    return grid;
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

  /**
   * Given the grid, apply the constraints that stop cells in the same row /
   * column / square having the same values
   *
   * @param model the model in which constraints will be stored
   * @param grid  the grid
   */
  private static void applyConnectionConstraints(Model model, IntVar[][] grid) {
    // all the rows are different
    for (int i = 0; i != SIZE; i++) {
      model.allDifferent(getCellsInRow(grid, i)).post();
      model.allDifferent(getCellsInColumn(grid, i)).post();
      model.allDifferent(getCellsInSquare(grid, i)).post();
    }
  }

  /**
   * Get the variables that are in a given row
   *
   * @param grid the grid
   * @param row  the row
   * @return all the variables in this row
   */
  private static IntVar[] getCellsInRow(IntVar[][] grid, int row) {
    return grid[row];
  }

  /**
   * Get the variables that are in a given column
   *
   * @param grid   the grid
   * @param column the column
   * @return all the variables in this column
   */
  private static IntVar[] getCellsInColumn(IntVar[][] grid, int column) {
    return Stream.of(grid).map(row -> row[column]).toArray(IntVar[]::new);
  }

  /**
   * Get the variables in the given square within the overall grid. There being 9
   * 3x3 squares, starting at 0,0
   *
   * @param grid   the grid
   * @param square the square, numbered 1->9, going in rows
   * @return the variables in the given square
   */
  private static IntVar[] getCellsInSquare(IntVar[][] grid, int square) {
    List<IntVar> results = new ArrayList<>();
    // where does this square start in the grid
    int startRow = SQUARE_SIZE * (square / (SIZE / SQUARE_SIZE));
    int startColumn = SQUARE_SIZE * (square % (SIZE / SQUARE_SIZE));

    // get every cell in this square
    for (int row = startRow; row != startRow + SQUARE_SIZE; row++) {
      for (int column = startColumn; column != startColumn + SQUARE_SIZE; column++) {
        results.add(grid[row][column]);
      }
    }

    return results.toArray(new IntVar[0]);
  }

  /**
   * Print out the solution to standard out
   *
   * @param grid         the grid of variables
   * @param showSolution if set to true then any discovered values will be show,
   *                     if not, then only the
   *                     original problem will be show. If true then the original
   *                     values will be wrapped
   *                     in stars (*)
   * @throws IOException
   */
  private static void printGrid(IntVar[][] grid, boolean showSolution) throws IOException {

    // We write the table out withthis
    AsciiTable at = new AsciiTable();
    at.addRule();

    // add each row to the table
    for (int row = 0; row != SIZE; row++) {
      List<String> labels = new ArrayList<>();
      for (int column = 0; column != SIZE; column++) {
        IntVar variable = grid[row][column];

        boolean isOriginalNumber = variable instanceof FixedIntVarImpl;

        // we show all numbers if we're showing the solution, but we always
        // show the original numbers
        boolean shouldShow = showSolution || isOriginalNumber;
        if (!shouldShow) {
          labels.add("");
        } else {
          // this is the number value for the cell, if we're showing the solution,
          // and this is an original value, we want to wrap it in stars
          String value = String.valueOf(variable.getValue());
          if (showSolution && isOriginalNumber) {
            value = "*" + value + "*";
          }
          labels.add(value);
        }
      }
      at.addRow(labels);
      at.addRule();
    }
    // put the at.reader in txt
    FileWriter writer = new FileWriter("result.txt");
    writer.write(at.render(150));
    writer.close();
  }

}
