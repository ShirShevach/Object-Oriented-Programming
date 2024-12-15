public class PlayerFactory {
    private static final String WHATEVER = "whatever";
    private static final String CLEVER = "clever";
    private static final String HUMAN = "human";
    private static final String GENIUS = "genius";

    /**
     * PlayerFactory's constructor
     */
    PlayerFactory() {}

    /**
     * The function return the fit Object like the name its received
     * @param playerName
     * @return
     */
    public Player buildPlayer(String playerName) {
        Player player;
        playerName = playerName.toLowerCase();
        switch (playerName) {
            case WHATEVER:
                player = new WhateverPlayer();
                break;
            case CLEVER:
                player = new CleverPlayer();
                break;
            case HUMAN:
                player = new HumanPlayer();
                break;
            case GENIUS:
                player = new GeniusPlayer();
                break;
            default:
                player = null;
        }
        return player;
    }
}
