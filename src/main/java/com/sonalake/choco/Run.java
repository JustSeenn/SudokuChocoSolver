package com.sonalake.choco;

import java.io.FileWriter;
import java.io.IOException;

public class Run {

    public static void main(String[] args) {

        int sudokuSize = 25;
        int i;
        int maxBlankCells = 0;
        long timeTook = 0;
        for(i=0; i<100; i++){
            long startTime = System.currentTimeMillis();
            ConstructorInt sudoku = searchMaxCellsToRemove(sudokuSize,300,20,150);
            long endTime = System.currentTimeMillis();
            if(sudoku.getBlankCellsCount() > maxBlankCells){
                maxBlankCells = sudoku.getBlankCellsCount();
                timeTook = endTime - startTime;
            } else if(sudoku.getBlankCellsCount() == maxBlankCells){
                if(endTime - startTime < timeTook){
                    timeTook = endTime - startTime;
                }
            }
        }

        //write in file the max number of blank cells and the time took to find it for the grid size 9
        try {
            FileWriter myWriter = new FileWriter("maxBlankCells_" + sudokuSize + ".txt");
            myWriter.write("Max number of blank cells: " + maxBlankCells + "\n");
            myWriter.write("Time took to find it: " + timeTook + " milliseconds\n");
            //myWriter.write("Grid has a unique solution " + sudoku.hasUniqueSolution() + "\n");
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    
    private static ConstructorInt searchMaxCellsToRemove(int size, int count, int min, int max){
        long startTime = System.currentTimeMillis();
        ConstructorInt sudoku = new ConstructorInt(size);
        sudoku.createSudokuGrid();
        sudoku.removeCountCells(count); // number of cells to remove bruteforce
        sudoku.removeCountCells(40);
        //sudoku.removeCells(min, max);
        sudoku.removeCellsbis();
        //System.out.println(sudoku.hasUniqueSolution());
        //sudoku.printSudokuGrid();
        //sudoku.saveGridAsIntegerList("sudoku_grids_9"); // save the grid as a list of integers for sudoku solver
        long endTime = System.currentTimeMillis();
        System.out.println("That took " + (endTime - startTime) + " milliseconds");
        System.out.println("Number of blank cells: " + sudoku.getBlankCellsCount());

        return sudoku;
    }
}
