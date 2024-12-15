import java.util.Scanner;

public class HumanPlayer implements Player{
    private static final String ERROR_COORDINATES = "Invalid coordinates, type again: ";
    private static final String TYPE_COORDINATES1 = "Player ";
    private static final String TYPE_COORDINATES2 = " type coordinates: ";

    /**
     * Requests coordinates from the user and places the
     * sign if the coordinates are "good" - normal and not occupied;
     * If not, it will ask for additional input
     * @param board
     * @param mark
     */
    public void playTurn(Board board, Mark mark) {
        Scanner scanner = new Scanner(System.in);
        System.out.printf(TYPE_COORDINATES1 + mark + TYPE_COORDINATES2);

        while (true) {
            String coordinates = scanner.nextLine();
            if (coordinates.length() == 2) {
                int row = Integer.parseInt(String.valueOf(coordinates.charAt(0)));
                int col = Integer.parseInt(String.valueOf(coordinates.charAt(1)));
                if (row >= 0 && row < board.getSize() && col >= 0 && col < board.getSize()
                && board.getMark(row, col) == Mark.BLANK) {
                    board.putMark(mark, row, col);
                    break;
                }
            }
            System.out.print(ERROR_COORDINATES);
        }
    }
}
