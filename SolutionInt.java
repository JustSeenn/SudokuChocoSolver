public class SolutionInt {
    private int size;
    private int root;

    public SolutionInt(int size) {
        this.size = size;
        this.root = (int) Math.sqrt(size);
    }

    public void solveSudoku(int[][] board) {
        solveSudoku(board, 0, 0);
    }

    public void printSudokuGrid(int[][] board) {
        for (int i = 0; i < this.size; i++) {
            for (int j = 0; j < this.size; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    private boolean solveSudoku(int[][] board, int row, int col) {
        if (col == this.size) {
            col = 0;
            row++;
            if (row == this.size) {
                return true;
            }
        }

        if (board[row][col] != 0) {
            return solveSudoku(board, row, col + 1);
        }
        for (char c = 1; c <= this.size; c++) {
            if (isValid(board, row, col, c)) {
                board[row][col] = c;
                if (solveSudoku(board, row, col + 1)) {
                    return true;
                }
                board[row][col] = 0;
            }
        }
        return false;
    }

    private boolean isValid(int[][] board, int row, int col, int c) {
        for (int i = 0; i < this.size; i++) {
            if (board[row][i] == c) {
                return false;
            }
        }
        for (int i = 0; i < this.size; i++) {
            if (board[i][col] == c) {
                return false;
            }
        }
        int startRow = (row / this.root) * this.root;
        int startCol = (col / this.root) * this.root;
        for (int i = startRow; i < startRow + this.root; i++) {
            for (int j = startCol; j < startCol + this.root; j++) {
                if (board[i][j] == c) {
                    return false;
                }
            }
        }
        return true;
    }
}
