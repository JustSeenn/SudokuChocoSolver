package com.sonalake.choco;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.search.strategy.Search;
import org.chocosolver.solver.variables.IntVar;


import static java.lang.String.format;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;


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

    public void printSudokuGrid(int[][] grid) {
        for (int[] row : grid) {
            for (int num : row) {
                System.out.print(num + " ");
            }
            System.out.println();
        }
    }

    public void printSudokuGrid() {
        for (int[] row : grid) {
            for (int num : row) {
                System.out.print(num + " ");
            }
            System.out.println();
        }
    }

    public void saveGridAsIntegerList(String gridName) {
        try {
            FileWriter writer = new FileWriter(gridName + ".txt");
            for (int[] row : grid) {
                for (int num : row) {
                    writer.write(num + ", ");
                }
                writer.write("\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeCountCells(int count) {
        //remove cells from the grid without checking for unique solution
        //check at the end if the grid has an unique solution if not retry
        int size = grid.length;
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                indices.add(i * size + j);
            }
        }
        Collections.shuffle(indices);
        int removed = 0;
        
        int[][] copyGrid = new int[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(grid[i], 0, copyGrid[i], 0, size);
        }
        for (int index : indices) {
            int row = index / size;
            int col = index % size;
            int temp = copyGrid[row][col];
            if (temp == 0) {
                removed++;
                continue;
            }
            copyGrid[row][col] = 0;
            removed++;
            if (removed >= count) {
                break;
            }
        }

        if (!hasUniqueSolution(copyGrid)) {
            System.out.println("Failed to remove " + count + " cells");
            removeCountCells(count);
        } else {
            grid = copyGrid;
            System.out.println("Removed " + count + " cells with brute force");
        }
    }

    public void removeCells(int min, int max) {
        int size = grid.length;
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j] != 0) {
                    indices.add(i * size + j); // Ajoute uniquement les indices des cellules non vides
                }
            }
        }
        Collections.shuffle(indices);
        int removed = 0;
        for (int index : indices) {
            int row = index / size;
            int col = index % size;
            int temp = grid[row][col];
            if(temp == 0) {
                System.out.println("Cell at (" + row + ", " + col + ") is already empty");
                removed++;
                continue;
            }
            grid[row][col] = 0;
            if (!hasUniqueSolution()) {
                System.out.println("Failed to remove cell at (" + row + ", " + col + ")");
                grid[row][col] = temp; // If removing the cell doesn't leave a unique solution, restore the cell
            } else {
                System.out.println("Removed cell at (" + row + ", " + col + ")");
                removed++;
                if (removed >= min && removed <= max) {
                    System.out.println("Removed " + removed + " cells in addition to the already removed cells");
                    if(!hasUniqueSolution(grid)){
                        System.out.println("Failed to remove " + removed + " cells");
                        System.out.println("The grid has no unique solution, try again the generation");
                    }
                    break; // Required number of cells removed
                }
            }
        }
        //System.out.println(Checker.isSudokuValid(grid));
    }

    private boolean hasUniqueSolution(int[][] gridToCheck) {
        int size = gridToCheck.length;
        int[][] copyGrid = new int[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(gridToCheck[i], 0, copyGrid[i], 0, size);
        }
        Model model = new Model("Sudoku");
        IntVar[][] vars = buildGrid(model, copyGrid);
    
        // Define constraints
        addConstraints(model, vars);
    
        // Search for a solution
        Solver solver = model.getSolver();
        solver.setSearch(Search.minDomLBSearch(flatten(vars)));

        boolean hasSolution = solver.solve();
        
        //System.out.println("Check done on first check : ");
        //System.out.println(hasSolution);

        if (!hasSolution) {
            return false; // Pas de solution possible
        }
        
        solver.limitTime("5s");

        // need to give a pessimistic strategy to avoid finding the same solution

        // Si on trouve une solution, on essaye d'en trouver une autre
        boolean hasSecondSolution = solver.solve();
        
        //System.out.println("Check done on second check, no second solution ?: ");
        //System.out.println(!hasSecondSolution);

        // Si on trouve une deuxième solution, cela signifie qu'il n'y a pas unicité
        return !hasSecondSolution;
    }

    private boolean hasUniqueSolution() {
        int size = grid.length;
        int[][] copyGrid = new int[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(grid[i], 0, copyGrid[i], 0, size);
        }
    
        Model model = new Model("Sudoku");
        IntVar[][] vars = buildGrid(model, copyGrid);
    
        // Define constraints
        addConstraints(model, vars);
    
        // Search for a solution
        Solver solver = model.getSolver();
        solver.setSearch(Search.minDomLBSearch(flatten(vars)));

        boolean hasSolution = solver.solve();
        
        //System.out.println("Check done on first check : ");
        //System.out.println(hasSolution);

        if (!hasSolution) {
            return false; // Pas de solution possible
        }
        
        solver.limitTime("5s");

        boolean hasSecondSolution = solver.solve();
        
        //System.out.println("Check done on second check, no second solution ?: ");
        //System.out.println(!hasSecondSolution);

        // If we find a second solution, it means that there is no uniqueness
        return !hasSecondSolution;
    }


    private static IntVar[][] buildGrid(Model model, int[][] predefinedRows) {
        // this grid will contain variables in the same shape as the input
        int size = predefinedRows.length;
        IntVar[][] grid = new IntVar[size][size];
    
        // check all the predefined values
        // if they're 0: create them as bounded variables across the colour range (1-9)
        // otherwise create them as a constance
        for (int row = 0; row != size; row++) {
          for (int col = 0; col != size; col++) {
            // print predefinedRows[row]
            int value = predefinedRows[row][col];
            // is this an unknown? if so then create it as a bounded variable
            if (value < 1) {
              grid[row][col] = model.intVar(format("[%s.%s]", row, col), 1, size);
            } else {
              // otherwise we have an actual value, so create it as a constant
              grid[row][col] = model.intVar(value);
            }
          }
        }
    
        return grid;
      }

    private static IntVar[] flatten(IntVar[][] board) {
        IntVar[] flat = new IntVar[board.length * board[0].length];
        for (int i = 0; i < board.length; i++) {
          for (int j = 0; j < board[0].length; j++) {
            flat[i * board[0].length + j] = board[i][j];
          }
        }
        return flat;
      }

    private void addConstraints(Model model, IntVar[][] vars) {
        for (int i = 0; i != vars.length; i++) {
            model.allDifferent(getCellsInRow(vars, i)).post();
            model.allDifferent(getCellsInColumn(vars, i)).post();
            model.allDifferent(getCellsInSquare(vars, i)).post();
        }
    }

    private static IntVar[] getCellsInRow(IntVar[][] grid, int row) {
        return grid[row];
      }

    private static IntVar[] getCellsInColumn(IntVar[][] grid, int column) {
        return Stream.of(grid).map(row -> row[column]).toArray(IntVar[]::new);
    }

    private static IntVar[] getCellsInSquare(IntVar[][] grid, int square) {
        List<IntVar> results = new ArrayList<>();
        // where does this square start in the grid
        int square_size = (int) Math.sqrt(grid.length);
        int size = grid.length;
        int startRow = square_size * (square / (size / square_size));

        // how to calculate square_size ,
        int startColumn = square_size * (square % (size / square_size));
    
        // get every cell in this square
        for (int row = startRow; row != startRow + square_size; row++) {
          for (int column = startColumn; column != startColumn + square_size; column++) {
            results.add(grid[row][column]);
          }
        }
    
        return results.toArray(new IntVar[0]);
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
