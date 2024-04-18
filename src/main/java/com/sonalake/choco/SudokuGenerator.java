package com.sonalake.choco;

import java.io.FileWriter;
import java.io.IOException;

public class SudokuGenerator {

    private static final int NUM_GRIDS_TO_GENERATE = 1; // Nombre de grilles à générer

    public static void main(String[] args) {
        int size = 16;
        try {
            FileWriter writer = new FileWriter("sudoku_grids_9.txt");
            for (int i = 0; i < NUM_GRIDS_TO_GENERATE; i++) {
                ConstructorInt sudoku = new ConstructorInt(size);
                sudoku.createSudokuGrid();
                // sudoku.removeCells((size * size) - 27, (size * size) - 17);
                // int[][] grid = sudoku.getGrid();
                // writeGridToFile(writer, grid);
                writer.write("\n"); // Ajoute une ligne vide entre les grilles
            }
            writer.close();
            System.out.println("Les grilles de Sudoku ont été enregistrées dans sudoku_grids.txt");
        } catch (IOException e) {
            System.err.println("Erreur lors de l'écriture dans le fichier.");
            e.printStackTrace();
        }
    }

    private static void writeGridToFile(FileWriter writer, int[][] grid) throws IOException {
        for (int[] row : grid) {
            for (int cell : row) {
                writer.write(cell + " ");
            }
            writer.write("\n");
        }
    }
}
