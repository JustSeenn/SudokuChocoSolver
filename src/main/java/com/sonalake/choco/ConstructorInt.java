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

    public void removeCountCells(int count) {
        // Imprime la grille initiale pour comparaison
        //printSudokuGrid();
    
        // Copie de la grille originale
        int[][] copyGrid = new int[grid.length][grid.length];
        for (int i = 0; i < grid.length; i++) {
            System.arraycopy(grid[i], 0, copyGrid[i], 0, grid.length);
        }
    
        // Liste de toutes les positions dans la grille
        List<Integer> positions = new ArrayList<>();
        for (int i = 0; i < grid.length * grid.length; i++) {
            positions.add(i);
        }
        Collections.shuffle(positions); // Mélange aléatoire des indices
    
        // Suppression aléatoire de 'count' cellules
        int removed = 0;
        for (int i = 0; i < positions.size() && removed < count; i++) {
            int pos = positions.get(i);
            int row = pos / grid.length;
            int col = pos % grid.length;
    
            if (copyGrid[row][col] != 0) {
                copyGrid[row][col] = 0;
                removed++;
            }
        }
    
        // Vérifie si la grille a une solution unique avec les cellules retirées

        int attempts = 0; // TODO: fix this

        //System.out.println("--------------------------------------------------------------------------------");
        //printSudokuGrid();
        //printSudokuGrid(copyGrid);
        //System.out.println(hasUniqueSolution(copyGrid));
        while (!hasUniqueSolution(copyGrid) && attempts < 100) { // Limite les tentatives pour éviter une boucle infinie
            attempts++;
            //System.out.println("Attempt " + attempts);
            removeCountCells(count); // Réessaye si pas unique
            return;
        }
    
        // Copie la grille modifiée dans la grille principale si elle a une solution unique
        if (hasUniqueSolution(copyGrid)) {
            for (int i = 0; i < grid.length; i++) {
                System.arraycopy(copyGrid[i], 0, grid[i], 0, grid.length);
            }
            System.out.println("Grid is valid");
        } else {
            System.out.println("Failed to create a unique solution grid after " + attempts + " attempts.");
        }
    
        // Imprime la nouvelle grille pour vérification
        //System.out.println("--------------------------------------------------------------------------------");
        //printSudokuGrid();
        //System.out.println("--------------------------------------------------------------------------------");
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
                removed++;
                continue;
            }
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
        //System.out.println(Checker.isSudokuValid(grid));
    }

    private boolean hasUniqueSolution(int[][] gridToCheck) {
        int size = gridToCheck.length;
        int[][] copyGrid = new int[size][size];
        for (int i = 0; i < size; i++) {
            System.arraycopy(gridToCheck[i], 0, copyGrid[i], 0, size);
        }
        Model model = new Model("Sudoku");
        IntVar[][] vars = new IntVar[size][size];
        // Define variables
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                vars[i][j] = model.intVar("Cell_" + i + "_" + j, copyGrid[i][j] == 0 ? 1 : copyGrid[i][j], size);
            }
        }
        // Define constraints
        addConstraints(model, vars);
        // Search for a solution
        Solver solver = model.getSolver();
        boolean hasSolution = solver.solve();
        if (!hasSolution) {
            return false; // Pas de solution possible
        }
        // Si on trouve une solution, on essaye d'en trouver une autre
        boolean hasSecondSolution = solver.solve();
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
        IntVar[][] vars = new IntVar[size][size];
    
        // Define variables
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                vars[i][j] = model.intVar("Cell_" + i + "_" + j, copyGrid[i][j] == 0 ? 1 : copyGrid[i][j], size);
            }
        }
    
        // Define constraints
        addConstraints(model, vars);
    
        // Search for a solution
        Solver solver = model.getSolver();
        boolean hasSolution = solver.solve();
    
        if (!hasSolution) {
            return false; // Pas de solution possible
        }
    
        // Si on trouve une solution, on essaye d'en trouver une autre
        boolean hasSecondSolution = solver.solve();
    
        // Si on trouve une deuxième solution, cela signifie qu'il n'y a pas unicité
        return !hasSecondSolution;
    }

    private void addConstraints(Model model, IntVar[][] vars) {
        int size = vars.length;
        for (int i = 0; i < size; i++) {
            model.allDifferent(vars[i]).post(); // Contrainte de ligne
            IntVar[] column = new IntVar[size];
            for (int j = 0; j < size; j++) {
                column[j] = vars[j][i];
            }
            model.allDifferent(column).post(); // Contrainte de colonne
        }
        int subgridSize = (int) Math.sqrt(size);
        for (int i = 0; i < size; i += subgridSize) {
            for (int j = 0; j < size; j += subgridSize) {
                IntVar[] subgrid = new IntVar[size];
                int idx = 0;
                for (int k = i; k < i + subgridSize; k++) {
                    for (int l = j; l < j + subgridSize; l++) {
                        subgrid[idx++] = vars[k][l];
                    }
                }
                model.allDifferent(subgrid).post(); // Contrainte de sous-grille
            }
        }
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
