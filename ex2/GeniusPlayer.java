public class GeniusPlayer implements Player {

    /**
     * The genius player rum on the cols from the 1 cols,
     * until its find an empty cell.
     * @param board
     * @param mark
     */
    public void playTurn(Board board, Mark mark) {
        int size = board.getSize();
        int start = 1;
        for (int j = start; ; j ++ ) {
            for (int i = 0; i < size; i++) {
                if (board.getMark(i, j%size) == Mark.BLANK) {
                    board.putMark(mark, i, j%size);
                    return;
                }
            }
        }
    }
}
