public class Board {
    private Mark[][] board;
    private int size = 4;

    /**
     * default constructor
     */
    Board() {
        board = new Mark[size][size];
        markAllBlank();
    }

    /**
     * non default constructor
     * @param size
     */
    Board(int size) {
        this.size = size;
        board = new Mark[size][size];
        markAllBlank();
    }
    /*
    the function clear the board (put Mark.BLANK in all the cells)
     */
    private void markAllBlank() {
        for (int i = 0; i < size; i ++ ) {
            for (int j = 0; j < size; j++) {
                this.board[i][j] = Mark.BLANK;
            }
        }
    }

    /**
     * the function return the size of the board
     * @return
     */
    public int getSize() {
        return this.size;
    }

    /**
     * The function return the board
     * @return
     */
    public Mark[][] getBoard() {
        return this.board;
    }

    /**
     * The function put mark in the received indexes
     * @param mark
     * @param row
     * @param col
     * @return
     */
    boolean putMark(Mark mark, int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            return false;
        }
        if (this.board[row][col] == Mark.BLANK) {
            this.board[row][col] = mark;
            return true;
        }
        return false;
    }

    /**
     * The function return the mark in the received indexes
     * @param row
     * @param col
     * @return
     */
    Mark getMark(int row, int col) {
        if (row < 0 || row >= size || col < 0 || col >= size) {
            return Mark.BLANK;
        }
        return this.board[row][col];
    }
}
