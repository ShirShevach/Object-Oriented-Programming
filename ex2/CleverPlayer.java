public class CleverPlayer implements Player{

    /**
     * The clever player rum on the rows in the board
     * until its find an empty cell
     * @param board
     * @param mark
     */
    public void playTurn(Board board, Mark mark) {
        int size = board.getSize();
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (board.getMark(i, j) == Mark.BLANK) {
                    board.putMark(mark, i, j);
                    return;
                }
            }
        }
    }
}
