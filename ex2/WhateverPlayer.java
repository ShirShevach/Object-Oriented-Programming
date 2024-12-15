import java.util.Random;

public class WhateverPlayer implements Player{

    /**
     * The whatever player randomize row and col until
     * the board[row][col] is empty.
     * @param board
     * @param mark
     */
    public void playTurn(Board board, Mark mark) {
        Random random = new Random();
        int row = random.nextInt(board.getSize());
        int col = random.nextInt(board.getSize());

        while (board.getMark(row, col) != Mark.BLANK){
            row = random.nextInt(board.getSize());
            col = random.nextInt(board.getSize());
        }
        board.putMark(mark, row, col);
    }
}
