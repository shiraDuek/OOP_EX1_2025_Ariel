/**
 * Represents a human player in the game.
 * This class extends the Player class and defines behavior specific to human players.
 */
public class HumanPlayer extends Player {

    /**
     * Constructs a HumanPlayer with a specified player type.
     *
     * @param isPlayerOne A boolean indicating whether this player is Player 1 (true) or Player 2 (false).
     */
    public HumanPlayer(boolean isPlayerOne) {
        super(isPlayerOne);  // Call the superclass constructor to initialize the player as Player 1 or Player 2
    }

    /**
     * Returns whether the player is a human.
     *
     * @return true, as this class represents a human player.
     */
    @Override
    boolean isHuman() {
        return true;  // Human players always return true for this method
    }
}
