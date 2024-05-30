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
    public int backtrack;
    private int blankCellsCount = 0;

    public ConstructorInt(int size) {
        grid = new int[size][size];
    }
    public void setBacktrack(int num){
        this.backtrack = 0;
    }

    public void createSudokuGrid() {
        fillGrid();
    }

    public int[][] getGrid() {
        return grid;
    }

    public int getBlankCellsCount() {
        return blankCellsCount;
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
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeCellsbis() {
        int size = grid.length;
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j] != 0) {
                    indices.add(i * size + j);
                }
            }
        }
        Collections.shuffle(indices);
        //int i=0;
        for (int index : indices) {
            int row = index / size;
            int col = index % size;
            int temp = grid[row][col];
            grid[row][col] = 0;
            if (!hasUniqueSolution()) {
                grid[row][col] = temp;
                break;
            } else {
                blankCellsCount++;
            }
            //i++;
            //if(i%(indices.size()/10)==0){
            //    System.out.println("Removed " + (blankCellsCount) + " cells (i check)");
            //}
        }
        System.out.println("Removed " + (blankCellsCount) + " cells");
    }


    public void removeCountCellsSymmetry(int count) {
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

        if (count % 2 != 0) {
            count--;
        }

        for (int i = 0; i < indices.size() && removed < count; i++) {
            int pos = indices.get(i);
            int row = pos / size;
            int col = pos % size;

            // Calculez la position symétrique
            int symRow = size - 1 - row;
            int symCol = size - 1 - col;

            // Assurez-vous que les deux cellules contiennent des valeurs avant de les
            // effacer
            if (copyGrid[row][col] != 0 && copyGrid[symRow][symCol] != 0) {
                copyGrid[row][col] = 0;
                copyGrid[symRow][symCol] = 0;
                removed += 2; // Incremente par deux puisque deux cellules sont retirées à chaque fois
            }
        }

        if (!hasUniqueSolution(copyGrid)) {
            // System.out.println("Failed to remove " + count + " cells symmetrically.");
            removeCountCellsSymmetry(count);
        } else {
            System.out.println("Removed " + removed + " cells symmetrically.");
            grid = copyGrid;
            blankCellsCount = removed;
        }
    }

    public int removeCountCells(int count) {
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
            this.backtrack++;
            removeCountCells(count);
        } else { 
            grid = copyGrid;
            blankCellsCount += removed;
            System.out.println("Removed " + removed + " cells with brute force");
        }

        return this.backtrack;
    }

    public void removeCells(int min, int max) {
        int size = grid.length;
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (grid[i][j] != 0) {
                    indices.add(i * size + j);
                }
            }
        }
        Collections.shuffle(indices);
        int removed = 0;
        for (int index : indices) {
            if (removed >= max)
                break;
            int row = index / size;
            int col = index % size;
            int temp = grid[row][col];
            grid[row][col] = 0;
            if (!hasUniqueSolution()) {
                grid[row][col] = temp;
            } else {
                removed++;
                blankCellsCount++;
                if (removed >= min) {
                    if (!hasUniqueSolution()) {
                        blankCellsCount--;
                        grid[row][col] = temp;
                    }
                    break;
                }
            }
        }
        System.out.println("Removed " + removed + " cells");
    }

    private boolean hasUniqueSolution(int[][] gridToCheck) {
        int size = gridToCheck.length;
        int[][] copyGrid = new int[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(gridToCheck[i], 0, copyGrid[i], 0, size);
        }
        Model model = new Model("Sudoku");
        IntVar[][] vars = buildGrid(model, copyGrid);

        addConstraints(model, vars);

        Solver solver = model.getSolver();
        solver.setSearch(Search.minDomLBSearch(flatten(vars)));

        boolean hasSolution = solver.solve();

        if (!hasSolution) {
            return false;
        }

        ConstraintImpactStrategy strategy = new ConstraintImpactStrategy(model, flatten(vars));
        model.getSolver().setSearch(strategy);
        boolean hasSecondSolution = solver.solve();

        return !hasSecondSolution;
    }

    public boolean hasUniqueSolution() {
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

        long startTime = System.currentTimeMillis();
        boolean hasSolution = solver.solve();
        long endTime = System.currentTimeMillis();

        long time = endTime - startTime;

        if (!hasSolution) {
            return false; // Pas de solution possible
        }

        //solver.limitTime(time * 5);
        ConstraintImpactStrategy strategy = new ConstraintImpactStrategy(model, flatten(vars));

        //model.getSolver().setSearch(strategy);
        boolean hasSecondSolution = solver.solve();
        return !hasSecondSolution;
    }

    public static IntVar[][] buildGrid(Model model, int[][] predefinedRows) {
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

    public static IntVar[] flatten(IntVar[][] board) {
        IntVar[] flat = new IntVar[board.length * board[0].length];
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                flat[i * board[0].length + j] = board[i][j];
            }
        }
        return flat;
    }

    public void addConstraints(Model model, IntVar[][] vars) {
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
