package com.sonalake.choco;

import org.chocosolver.solver.Model;
import org.chocosolver.solver.Solver;
import org.chocosolver.solver.exception.ContradictionException;
import org.chocosolver.solver.variables.IntVar;
import org.chocosolver.util.objects.setDataStructures.iterable.IntIterableSet;
import org.chocosolver.solver.search.strategy.Search;


public class AdvancedSudokuGenerator {
    private static final int GRID_SIZE = 9;

    public static void main(String[] args) throws ContradictionException {
        int numberOfZeros = 20;
        int[][] grid = generateSudoku(numberOfZeros);
        if (grid != null) {
            printGrid(grid);
        } else {
            System.out.println("Failed to generate a valid grid.");
        }
    }

    private static void printGrid(int[][] grid) {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    private static void addAllDiffConstraints(Model model, IntVar[][] vars) {
        // Ajouter les contraintes allDiff pour les lignes et les colonnes
        for (int i = 0; i < GRID_SIZE; i++) {
            model.allDifferent(vars[i]).post();
            IntVar[] column = new IntVar[GRID_SIZE];
            for (int j = 0; j < GRID_SIZE; j++) {
                column[j] = vars[j][i];
            }
            model.allDifferent(column).post();
        }

        // Ajouter les contraintes allDiff pour les sous-grilles 3x3
        for (int i = 0; i < GRID_SIZE; i += 3) {
            for (int j = 0; j < GRID_SIZE; j += 3) {
                IntVar[] subgrid = new IntVar[GRID_SIZE];
                int idx = 0;
                for (int k = 0; k < 3; k++) {
                    for (int l = 0; l < 3; l++) {
                        subgrid[idx++] = vars[i + k][j + l];
                    }
                }
                model.allDifferent(subgrid).post();
            }
        }
    }
 
    private static int[][] generateSudoku(int numberOfZeros) throws ContradictionException {
        // Premièrement, générons une grille complète
        Model initialModel = new Model("Complete Sudoku");
        IntVar[][] initialVars = initialModel.intVarMatrix("cells", GRID_SIZE, GRID_SIZE, 1, GRID_SIZE);
        addAllDiffConstraints(initialModel, initialVars);
        
        Solver initialSolver = initialModel.getSolver();
        int[][] completeGrid = new int[GRID_SIZE][GRID_SIZE];
        
        if (initialSolver.solve()) {
            for (int i = 0; i < GRID_SIZE; i++) {
                for (int j = 0; j < GRID_SIZE; j++) {
                    completeGrid[i][j] = initialVars[i][j].getValue();
                }
            }
        }

        // Ensuite, nous retirons des valeurs pour créer des zéros tout en vérifiant l'unicité
        return removeValuesToCreateZeros(completeGrid, numberOfZeros);
    }

    private static int[][] removeValuesToCreateZeros(int[][] completeGrid, int numberOfZeros) throws ContradictionException {
        Model model = new Model("Sudoku With Zeros");
        IntVar[][] vars = model.intVarMatrix("cells", GRID_SIZE, GRID_SIZE, 0, GRID_SIZE);
        
        // Appliquer les valeurs initiales
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                vars[i][j] = model.intVar(completeGrid[i][j]);
            }
        }
        
        // Ajouter des contraintes
        addAllDiffConstraints(model, vars);
        // Ajouter une stratégie pour retirer des valeurs
        Solver solver = model.getSolver();
        solver.setSearch(Search.randomSearch(model.retrieveIntVars(true), 0));
        
        int removedCount = 0;
        while (removedCount < numberOfZeros && solver.solve()) {
            int removeRow = (int) (Math.random() * GRID_SIZE);
            int removeCol = (int) (Math.random() * GRID_SIZE);
            if (vars[removeRow][removeCol].getValue() != 0) {
                vars[removeRow][removeCol].removeAllValuesBut((IntIterableSet) model.intVar(0), null);
                removedCount++;
            }
        }

        // Conversion finale pour retour
        int[][] solutionGrid = new int[GRID_SIZE][GRID_SIZE];
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                solutionGrid[i][j] = vars[i][j].getValue();
            }
        }
        return solutionGrid;
    }

    // Implementer hasUniqueSolution, printGrid, et addAllDiffConstraints comme avant
}