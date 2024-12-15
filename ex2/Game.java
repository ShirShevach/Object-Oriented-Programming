import static java.lang.Math.abs;

public class Game {
    private int size = 4;
    private int winStreak = 3;
    private Player playerX;
    private Player playerO;
    private Renderer renderer;
    private Board board;
    private int numSigns;

    /**
     * The Game's constructor
     * @param playerX
     * @param playerO
     * @param renderer
     */
    Game(Player playerX, Player playerO, Renderer renderer) {
        constructor(playerX, playerO, renderer);
    }

    /**
     * The Game's constructor
     * @param playerX
     * @param playerO
     * @param size
     * @param winStreak
     * @param renderer
     */
    Game(Player playerX, Player playerO,int size, int winStreak, Renderer renderer) {
        this.size = size;
        this.winStreak = winStreak;
        if (this.winStreak < 0 || this.winStreak >= size) {
            this.winStreak = size;
        }
        this.playerX = playerX;
        this.playerO = playerO;
        this.renderer = renderer;
        constructor(playerX, playerO, renderer);
    }

    /*
    help the constructor
     */
    private void constructor(Player playerX, Player playerO, Renderer renderer) {
        this.playerX = playerX;
        this.playerO = playerO;
        this.renderer = renderer;
        board = new Board(this.size);
        this.numSigns = 0;
    }

    /**
     * return the winStreak
     * @return
     */
    public int getWinStreak() {
        return this.winStreak;
    }

    /**
     * return True if the board is full, else otherwise
     * @return
     */
    private boolean boardIsFull() {
        return this.numSigns == this.size*this.size;
    }

    /**
     * The function resets the numSigns
     * and clears the board.
     */
    private void finishGame() {
        this.numSigns = 0;
        for (int i = 0; i < size; i ++ ) {
            for (int j = 0; j < size; j++) {
                board.putMark(Mark.BLANK, i, j);
            }
        }
    }

    /*
    The function check if there is a winning in the game
     */
    private boolean playerWin(Mark mark) {
        for (int i = 0; i < this.size; i++) {
            if (chackRow(i, mark)) {
                return true;
            }
            if (chackCol(i, mark)) {
                return true;
            }
            if (chackDiag1(0, i, mark)) {
                return true;
            }
            if (i > 0 && chackDiag1(i, 0, mark)) {
                return true;
            }
            if (chackDiag2(i, 0, mark)) {
                return true;
            }
            if (i > 0 && chackDiag2(size-1, i, mark)) {
                return true;
            }
        }
        return false;
    }

    /*
    check if there is a winning in single row
     */
    private boolean chackRow(int row, Mark mark) {
        int sequence = 0;
        for (int i = 0; i < size; i++) {
            if (board.getMark(row, i) == mark) {
                sequence += 1;
                if (sequence == this.winStreak) {
                    return true;
                }
            }
            else {
                sequence = 0;
            }
        }
        return false;
    }

    /*
    check if there is a winning in single col
     */
    private boolean chackCol(int col, Mark mark) {
        int sequence = 0;
        for (int i = 0; i < size; i++) {
            if (board.getMark(i, col) == mark) {
                sequence += 1;
                if (sequence == this.winStreak) {
                    return true;
                }
            }
            else {
                sequence = 0;
            }
        }
        return false;
    }

    /*
    check if there is a winning in single right diag
     */
    private boolean chackDiag1(int x, int y, Mark mark) {
        int sequence = 0;
        while (x >= 0 && x < this.size && y >=0 && y < this.size) {
            if (board.getMark(x, y) == mark) {
                sequence += 1;
                if (sequence == this.winStreak) {
                    return true;
                }
            }
            else {
                sequence = 0;
            }
            x += 1;
            y += 1;
        }
        return false;
    }

    /*
    check if there is a winning in single left diag
     */
    private boolean chackDiag2(int x, int y, Mark mark) {
        int sequence = 0;
        while (x >= 0 && x < this.size && y >=0 && y < this.size) {
            if (board.getMark(x, y) == mark) {
                sequence += 1;
                if (sequence == this.winStreak) {
                    return true;
                }
            }
            else {
                sequence = 0;
            }
            x -= 1;
            y += 1;
        }
        return false;
    }

    /**
     * The function runs a game from start to finish,
     * and returns the token of the winning player
     * @return
     */
    public Mark run() {
        renderer.renderBoard(board);
        int index = 1;
        Player[] players = {playerX, playerO};
        Mark[] marks = {Mark.X, Mark.O};
        Player player;
        Mark mark;
        while (! boardIsFull()) {
            index = abs(index - 1);
            player = players[index];
            mark = marks[index];

            player.playTurn(board, mark);
            renderer.renderBoard(board);
            this.numSigns += 1;
            if (playerWin(mark)) {
                return mark;
            }
        }
        finishGame();
        return Mark.BLANK;
    }

}
