import static java.lang.Math.abs;

public class Tournament {
    private static final String ERROR_NAME = "problem with the name of one or more players";
    private static final String END_GAME_MASSAGE = "######### Results #########%nPlayer 1, %s won: %d rounds%nPlayer 2, " +
            "%s won: %d rounds%nTies: %d%n";

    private int rounds;
    private Renderer renderer;
    private Player[] players;
    private int [] wonList = new int [] {0, 0};
    private int tie = 0;

    /**
     * The function receives the arguments from the command line,
     * initializes the variables and starts the tournament.
     * @param args
     */
    public static void main(String[] args) {
        int rounds = Integer.parseInt(args[0]);
        int size = Integer.parseInt(args[1]);
        int winStreak = Integer.parseInt(args[2]);

        RendererFactory rendererFactory = new RendererFactory();
        Renderer renderer = rendererFactory.buildRenderer(args[3], size);
        PlayerFactory playerFactory = new PlayerFactory();

        String [] playerNames = new String[] {args[4], args[5]};
        Player player1 = playerFactory.buildPlayer(playerNames[0]);
        Player player2 = playerFactory.buildPlayer(playerNames[1]);
        Tournament d = new Tournament(rounds, renderer, new Player[] {player1, player2});

        d.playTournament(size, winStreak, playerNames);
    }

    /**
     * The Tournament's constructor
     * @param rounds
     * @param renderer
     * @param players
     */
    Tournament (int rounds, Renderer renderer, Player[] players) {
        this.rounds = rounds;
        this.renderer = renderer;
        this.players = new Player[]{players[0], players[1]};
    }

    /**
     * The function runs the game as the number of rounds
     * and finally prints the tournament result
     * @param size
     * @param winStreak
     * @param playerNames
     */
    private void playTournament(int size, int winStreak, String[] playerNames) {
        PlayerFactory playerFactory = new PlayerFactory();
        this.players[0] = playerFactory.buildPlayer(playerNames[0]);
        this.players[1] = playerFactory.buildPlayer(playerNames[1]);
        if (this.players[0] == null || this.players[1] == null) {
            System.out.println(ERROR_NAME);
            return;
        }
        int index = 1;
        Mark currWinner;
        Game game;
        for (int i = 0; i < this.rounds; i++) {
            index = abs(1- index);
            game = new Game(this.players[index], this.players[abs(1- index)], size, winStreak, this.renderer);
            currWinner = game.run();
            if (currWinner == Mark.X) {
                wonList[index] += 1;
            }
            else if (currWinner == Mark.O) {
                wonList[abs(1- index)] += 1;
            }
            else {
                tie += 1;
            }
        }
        System.out.printf(END_GAME_MASSAGE,
                playerNames[0], wonList[0], playerNames[1], wonList[1], tie);
    }

}
