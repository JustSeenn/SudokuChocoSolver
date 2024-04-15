import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SudokuReader {

    public int[][] readSudokuGridFromFile(String filename, int size) throws FileNotFoundException {
        File file = new File(filename);
        Scanner scanner = new Scanner(file);
        int[][] grid = new int[size][size];
        int i = 0;
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (!line.isEmpty()) {
                grid[i] = parseGrid(line);
                i++;
            }
        }
        scanner.close();
        return grid;
    }

    private static int[] parseGrid(String line) {
        String[] cells = line.split("\\s+");
        int[] row = new int[cells.length];
        for (int i = 0; i < cells.length; i++) {
            row[i] = Integer.parseInt(cells[i]);
        }
        return row;
    }

    private static void printGrid(int[][] grid) {
        for (int[] row : grid) {
            for (int cell : row) {
                System.out.print(cell + " ");
            }
            System.out.println();
        }
    }
}
