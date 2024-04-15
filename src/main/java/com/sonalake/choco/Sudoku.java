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

  private static final int SIZE = 49;
  private static final int SQUARE_SIZE = 7;
  private static final int MIN_VALUE = 1;
  private static final int MAX_VALUE = SIZE;

  static public void main(String... args) throws IOException {

    int[] stringGrind = { 34, 0, 0, 0, 13, 0, 4, 17, 0, 0, 30, 6, 45, 0, 9, 33, 21, 24, 0, 0, 0, 0, 0, 0, 0, 2, 0, 0,
        10, 36, 0, 48, 49, 0, 25, 11, 20, 44, 0, 32, 0, 27, 46, 0, 0, 41, 0, 22, 1, 46, 42, 0, 0, 0, 0, 0, 4, 34, 0, 8,
        0, 0, 39, 0, 6, 0, 18, 17, 40, 19, 21, 24, 14, 3, 15, 9, 0, 0, 0, 38, 0, 0, 16, 0, 0, 0, 0, 36, 43, 0, 0, 0, 35,
        0, 11, 20, 0, 5, 32, 0, 27, 0, 0, 44, 0, 1, 0, 42, 29, 41, 0, 0, 0, 23, 13, 39, 0, 0, 31, 0, 0, 17, 40, 0, 0, 0,
        21, 24, 14, 3, 15, 9, 33, 0, 7, 12, 0, 0, 37, 0, 43, 0, 49, 0, 25, 0, 0, 0, 48, 0, 26, 25, 0, 0, 5, 0, 35, 0, 0,
        20, 0, 29, 0, 28, 0, 1, 46, 42, 13, 0, 0, 34, 31, 8, 0, 0, 0, 0, 0, 0, 30, 6, 9, 0, 21, 0, 0, 3, 0, 0, 37, 0, 0,
        7, 0, 47, 0, 0, 2, 16, 7, 12, 47, 0, 43, 48, 0, 26, 0, 0, 27, 11, 0, 44, 0, 32, 35, 0, 22, 1, 0, 42, 29, 41, 0,
        39, 4, 34, 0, 8, 23, 30, 0, 0, 18, 17, 40, 0, 14, 0, 15, 9, 33, 0, 24, 3, 0, 0, 33, 0, 0, 0, 38, 37, 2, 16, 7,
        12, 0, 0, 0, 0, 36, 43, 0, 0, 0, 5, 32, 35, 0, 0, 20, 0, 0, 0, 0, 0, 41, 28, 0, 13, 39, 0, 0, 31, 0, 40, 19, 0,
        6, 45, 18, 17, 40, 19, 30, 6, 45, 18, 17, 0, 0, 0, 0, 33, 21, 24, 16, 0, 12, 0, 38, 0, 0, 10, 0, 43, 0, 49, 26,
        25, 0, 0, 0, 0, 27, 11, 0, 0, 28, 22, 0, 46, 42, 29, 0, 31, 0, 23, 13, 39, 4, 17, 40, 19, 0, 0, 45, 0, 24, 14,
        3, 0, 9, 0, 0, 0, 0, 7, 0, 47, 0, 37, 0, 10, 0, 0, 48, 49, 26, 0, 0, 5, 0, 35, 0, 11, 29, 41, 0, 0, 0, 0, 0, 0,
        0, 0, 0, 23, 13, 39, 4, 0, 31, 8, 23, 0, 0, 18, 17, 0, 19, 0, 0, 45, 15, 9, 0, 21, 24, 0, 3, 7, 12, 0, 0, 37, 2,
        16, 25, 10, 0, 43, 48, 49, 0, 0, 0, 0, 0, 5, 0, 0, 1, 46, 42, 0, 0, 0, 22, 0, 0, 0, 0, 41, 0, 0, 0, 0, 0, 31, 8,
        23, 13, 19, 0, 0, 0, 0, 17, 0, 0, 21, 24, 14, 0, 15, 0, 0, 0, 0, 0, 0, 0, 16, 49, 26, 0, 10, 36, 43, 0, 0, 32,
        35, 27, 0, 20, 44, 5, 0, 0, 27, 11, 0, 0, 0, 1, 46, 0, 0, 0, 28, 31, 0, 0, 13, 39, 4, 34, 0, 0, 0, 17, 40, 0, 0,
        33, 0, 24, 14, 0, 0, 0, 0, 16, 7, 12, 0, 38, 37, 36, 0, 48, 49, 26, 25, 0, 36, 43, 48, 49, 0, 0, 10, 0, 5, 32,
        35, 0, 0, 20, 42, 29, 41, 0, 22, 1, 46, 0, 13, 39, 0, 0, 0, 8, 6, 45, 0, 0, 0, 19, 30, 0, 9, 0, 0, 24, 14, 3, 0,
        38, 0, 2, 0, 0, 12, 0, 0, 37, 2, 16, 7, 0, 10, 36, 0, 0, 49, 26, 0, 35, 27, 11, 0, 44, 5, 0, 41, 28, 22, 1, 0,
        42, 29, 23, 0, 0, 0, 34, 31, 0, 19, 0, 6, 0, 0, 17, 40, 24, 14, 3, 15, 9, 0, 21, 14, 0, 15, 0, 33, 21, 0, 47,
        38, 0, 2, 16, 7, 12, 0, 26, 0, 10, 0, 0, 48, 20, 44, 5, 0, 0, 27, 11, 28, 0, 1, 46, 42, 0, 41, 8, 23, 13, 0, 0,
        0, 31, 0, 40, 19, 0, 0, 45, 18, 39, 0, 34, 31, 0, 0, 13, 45, 0, 0, 40, 0, 0, 6, 3, 15, 0, 33, 21, 24, 0, 16, 0,
        0, 47, 38, 0, 2, 26, 25, 0, 36, 0, 0, 49, 35, 0, 11, 20, 44, 5, 0, 22, 0, 46, 42, 29, 0, 0, 0, 1, 0, 42, 29, 0,
        0, 13, 0, 4, 34, 0, 8, 23, 40, 19, 0, 6, 0, 18, 0, 0, 0, 0, 0, 14, 0, 0, 16, 7, 12, 47, 0, 37, 2, 0, 0, 26, 25,
        10, 36, 0, 0, 0, 0, 35, 27, 11, 0, 0, 0, 0, 35, 0, 0, 20, 0, 0, 0, 46, 42, 29, 41, 34, 0, 8, 23, 13, 39, 0, 30,
        0, 45, 18, 17, 0, 0, 9, 0, 21, 0, 0, 3, 15, 0, 2, 0, 0, 0, 47, 0, 0, 36, 43, 0, 49, 0, 25, 10, 0, 43, 48, 0, 26,
        25, 0, 0, 5, 32, 35, 27, 0, 0, 42, 29, 0, 28, 0, 0, 8, 23, 0, 39, 0, 0, 31, 0, 0, 0, 0, 0, 0, 0, 0, 15, 0, 0, 0,
        0, 0, 12, 0, 0, 37, 0, 16, 0, 12, 0, 38, 37, 2, 0, 0, 0, 0, 0, 43, 0, 49, 0, 0, 0, 27, 11, 20, 0, 0, 0, 41, 0,
        22, 0, 0, 0, 0, 0, 13, 39, 0, 34, 31, 0, 19, 30, 6, 45, 0, 0, 21, 0, 0, 0, 0, 0, 0, 24, 0, 3, 15, 0, 0, 0, 0,
        47, 0, 0, 2, 0, 0, 0, 49, 26, 25, 0, 36, 0, 0, 20, 44, 5, 32, 0, 0, 0, 0, 22, 1, 46, 42, 0, 0, 8, 0, 0, 39, 4,
        0, 18, 0, 0, 0, 0, 0, 45, 18, 0, 0, 0, 0, 0, 45, 0, 0, 14, 3, 0, 0, 33, 0, 0, 16, 0, 12, 0, 38, 26, 0, 10, 36,
        43, 48, 0, 0, 20, 44, 0, 32, 35, 27, 0, 0, 41, 0, 22, 0, 0, 0, 0, 0, 31, 0, 0, 13, 0, 39, 4, 0, 31, 0, 0, 6, 0,
        0, 17, 40, 19, 30, 0, 3, 0, 0, 0, 21, 0, 2, 16, 7, 12, 0, 0, 37, 0, 0, 25, 10, 36, 43, 0, 32, 0, 27, 0, 0, 0, 5,
        28, 22, 1, 46, 42, 0, 41, 28, 0, 0, 46, 0, 0, 41, 0, 0, 39, 4, 34, 31, 0, 17, 40, 19, 30, 6, 0, 18, 15, 9, 0,
        21, 0, 0, 3, 2, 16, 7, 12, 47, 38, 0, 43, 48, 0, 0, 25, 10, 0, 20, 0, 5, 0, 35, 0, 0, 20, 44, 0, 32, 35, 0, 11,
        0, 0, 0, 0, 0, 0, 0, 4, 0, 0, 8, 23, 13, 0, 19, 0, 6, 45, 18, 0, 0, 0, 9, 33, 0, 0, 14, 0, 38, 0, 2, 16, 7, 0,
        47, 0, 0, 36, 0, 0, 0, 0, 0, 0, 0, 43, 48, 49, 26, 11, 20, 0, 5, 32, 0, 0, 0, 0, 0, 29, 0, 0, 22, 0, 8, 23, 0,
        39, 4, 34, 19, 30, 6, 45, 0, 17, 40, 14, 3, 15, 0, 0, 21, 24, 0, 12, 0, 0, 0, 0, 0, 7, 12, 47, 0, 37, 0, 0, 0,
        25, 10, 36, 43, 48, 0, 0, 32, 35, 0, 0, 0, 44, 42, 0, 0, 28, 0, 1, 46, 31, 0, 0, 13, 39, 4, 0, 17, 40, 0, 0, 0,
        45, 18, 0, 21, 0, 0, 0, 0, 9, 21, 24, 0, 3, 0, 9, 0, 0, 12, 0, 0, 37, 0, 16, 0, 0, 49, 26, 25, 10, 0, 27, 0, 20,
        44, 5, 0, 0, 0, 41, 28, 22, 1, 46, 42, 34, 0, 8, 23, 13, 0, 0, 0, 0, 0, 0, 19, 0, 6, 0, 0, 17, 40, 19, 0, 6, 33,
        21, 0, 0, 0, 0, 0, 38, 37, 2, 16, 7, 12, 47, 0, 0, 25, 0, 36, 0, 0, 27, 11, 20, 0, 5, 0, 35, 46, 42, 29, 41, 28,
        0, 1, 13, 39, 0, 34, 31, 8, 0, 16, 7, 12, 0, 38, 0, 0, 0, 0, 25, 10, 36, 0, 0, 44, 0, 32, 35, 27, 11, 20, 0, 0,
        29, 0, 0, 0, 0, 34, 31, 8, 23, 13, 0, 4, 18, 0, 0, 19, 30, 0, 45, 9, 33, 21, 24, 14, 0, 15, 0, 21, 24, 14, 0,
        15, 0, 16, 7, 12, 47, 0, 37, 0, 0, 43, 48, 0, 26, 25, 10, 35, 0, 11, 0, 44, 0, 32, 0, 29, 41, 28, 0, 1, 46, 4,
        34, 31, 8, 23, 13, 0, 6, 0, 0, 17, 40, 0, 30, 0, 45, 18, 17, 40, 19, 30, 9, 33, 0, 0, 0, 3, 15, 47, 0, 0, 2, 0,
        0, 12, 0, 49, 0, 25, 10, 36, 43, 0, 0, 0, 20, 44, 5, 0, 0, 46, 0, 29, 41, 28, 0, 0, 0, 39, 0, 34, 0, 8, 23, 13,
        0, 4, 0, 0, 8, 30, 0, 45, 0, 17, 0, 0, 0, 0, 0, 15, 0, 0, 0, 0, 0, 16, 7, 0, 0, 0, 48, 49, 0, 0, 0, 36, 0, 0,
        32, 0, 27, 11, 0, 44, 41, 28, 0, 1, 46, 42, 0, 0, 28, 22, 1, 46, 42, 0, 0, 23, 0, 0, 0, 0, 31, 18, 17, 0, 0, 30,
        6, 45, 0, 15, 9, 33, 21, 24, 0, 0, 0, 0, 7, 12, 47, 38, 36, 0, 48, 0, 26, 0, 10, 11, 20, 0, 0, 32, 0, 27, 0, 20,
        0, 0, 32, 0, 27, 29, 41, 0, 0, 1, 46, 0, 39, 0, 34, 31, 8, 23, 0, 40, 19, 30, 0, 0, 0, 0, 3, 15, 9, 33, 21, 0,
        14, 47, 38, 37, 2, 0, 0, 12, 26, 0, 10, 36, 0, 48, 0, 0, 0, 0, 0, 0, 0, 49, 27, 11, 20, 44, 5, 0, 35, 22, 0, 0,
        42, 0, 0, 28, 0, 31, 0, 0, 13, 39, 4, 40, 19, 0, 6, 45, 18, 17, 0, 14, 3, 0, 9, 33, 21, 16, 7, 0, 0, 38, 37, 2,
        0, 0, 0, 0, 36, 43, 0, 0, 0, 0, 0, 44, 0, 32, 0, 22, 1, 0, 0, 29, 41, 0, 0, 0, 8, 23, 13, 39, 17, 40, 19, 0, 6,
        45, 18, 21, 24, 0, 0, 0, 9, 33, 2, 0, 7, 12, 0, 0, 37, 2, 0, 7, 12, 0, 0, 37, 48, 0, 26, 25, 10, 0, 43, 0, 0, 5,
        0, 0, 27, 11, 1, 46, 42, 0, 0, 0, 22, 4, 34, 0, 0, 0, 0, 0, 0, 0, 0, 40, 0, 30, 6, 0, 0, 33, 21, 0, 14, 3, 9, 0,
        21, 0, 14, 3, 0, 0, 16, 7, 0, 0, 38, 0, 0, 36, 0, 0, 0, 26, 25, 0, 0, 27, 0, 0, 0, 5, 46, 0, 29, 0, 0, 22, 0, 0,
        4, 0, 31, 0, 23, 13, 0, 6, 0, 0, 0, 40, 19, 30, 0, 0, 18, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3, 0, 47, 38, 37, 0, 16, 7,
        0, 0, 0, 26, 25, 0, 0, 32, 35, 0, 0, 20, 0, 0, 22, 1, 46, 0, 0, 41, 28, 8, 0, 0, 0, 4, 34, 0, 8, 0, 0, 39, 0, 0,
        0, 0, 30, 6, 45, 18, 0, 0, 21, 24, 0, 3, 15, 9, 33, 38, 0, 2, 16, 0, 12, 47, 0, 48, 49, 0, 25, 0, 36, 44, 0, 32,
        35, 0, 11, 20, 0, 41, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 13, 0, 0, 34, 45, 0, 0, 40, 0, 30, 6, 14, 3,
        0, 9, 33, 21, 0, 38, 37, 0, 16, 0, 12, 47, 10, 36, 0, 0, 0, 26, 25, 0, 11, 0, 44, 5, 0, 35, 27, 11, 0, 44, 5,
        32, 0, 42, 29, 41, 28, 0, 0, 46, 0, 39, 4, 34, 31, 0, 0, 17, 40, 0, 30, 6, 0, 18, 14, 0, 15, 9, 0, 0, 24, 0, 0,
        0, 37, 0, 16, 0, 49, 0, 0, 10, 36, 43, 48, 19, 30, 6, 45, 18, 0, 40, 3, 15, 9, 0, 21, 24, 14, 0, 12, 47, 38, 37,
        2, 0, 0, 43, 48, 49, 0, 0, 0, 5, 0, 35, 27, 0, 20, 44, 28, 22, 1, 46, 0, 0, 0, 31, 8, 0, 0, 39, 4, 34, 31, 0, 0,
        0, 39, 4, 0, 0, 0, 30, 6, 0, 0, 0, 33, 21, 24, 0, 3, 0, 9, 47, 0, 37, 2, 0, 7, 12, 36, 43, 0, 49, 26, 25, 10,
        20, 44, 0, 32, 35, 27, 0, 0, 29, 0, 0, 0, 0, 0, 0, 29, 0, 28, 22, 1, 0, 0, 31, 8, 0, 0, 0, 0, 6, 45, 18, 17, 0,
        0, 30, 24, 0, 3, 15, 0, 0, 21, 0, 0, 37, 0, 0, 7, 0, 0, 0, 36, 43, 0, 49, 0, 0, 0, 0, 20, 0, 5, 32, 0, 0, 11,
        20, 44, 0, 0, 46, 42, 0, 41, 0, 0, 1, 23, 13, 0, 4, 34, 0, 8, 18, 17, 0, 19, 0, 6, 45, 24, 0, 0, 0, 9, 33, 0, 0,
        0, 47, 0, 37, 0, 16, 48, 0, 26, 25, 10, 0, 43, 0, 49, 26, 25, 0, 0, 43, 0, 0, 27, 11, 0, 0, 0, 0, 0, 0, 0, 0, 0,
        0, 39, 0, 0, 31, 8, 0, 13, 18, 17, 40, 0, 30, 6, 45, 0, 0, 24, 14, 3, 15, 9, 0, 0, 0, 7, 0, 47, 0, 0, 2, 16, 0,
        12, 47, 38, 0, 48, 0, 26, 0, 10, 36, 11, 0, 0, 0, 32, 0, 0, 22, 0, 0, 0, 29, 41, 28, 0, 4, 34, 31, 0, 0, 13, 0,
        45, 18, 0, 40, 0, 0, 0, 15, 0, 33, 21, 24, 0, 15, 0, 33, 0, 24, 14, 0, 37, 2, 16, 7, 12, 47, 38, 0, 0, 36, 43,
        0, 49, 26, 0, 0, 35, 27, 11, 0, 0, 0, 0, 42, 29, 0, 0, 22, 13, 39, 0, 34, 31, 8, 23, 19, 30, 6, 0, 0, 17, 40
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
    printGrid(grid, false, false);

    // solve it
    Solver solver = model.getSolver();
    solver.showShortStatistics();
    solver.setSearch(Search.minDomLBSearch(flatten(grid)));
    solver.solve();
    // System.out.print("[");
    // for (IntVar[] v : grid) {
    // for (IntVar i : v) {
    // System.out.print(i.getValue() + ",");
    // }
    // System.out.println();
    // }
    // System.out.print("]");

    // print out the solution
    printGrid(grid, true, true);
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
  private static void printGrid(IntVar[][] grid, boolean showSolution, boolean append) throws IOException {

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
    FileWriter writer = new FileWriter("result.txt", append);
    writer.write(at.render(200));
    writer.write("\n\n\n------------------------------------------------------------------------\n\n\n");
    writer.close();
  }

}
