import java.util.List;
import java.util.Random;
/**
 * A class representing an AI player that makes random moves in the game.
 * This AI randomly selects a valid move from the available options and
 * randomly chooses the type of disc to play (Simple, Bomb, or Unflippable)
 * based on the resources available to the current player.
 */
public class RandomAI extends AIPlayer {

    private static Random random=new Random();

    /**
     * Constructor for RandomAI.
     * Initializes the AI player with the specified turn (either player 1 or player 2).
     *
     * @param isPlayerOne A boolean indicating if this AI player is player 1.
     */
    public RandomAI(boolean isPlayerOne) {
        super(isPlayerOne);
      }
    /**
     * Makes a random move for the AI player based on the current game state.
     * The AI selects a random valid position from the available moves and
     * randomly chooses a type of disc (Simple, Bomb, or Unflippable)
     * based on the available resources.
     *
     * @param gameStatus The current game state, providing information on valid moves and the current player.
     * @return A Move object representing the random move made by the AI, or null if no valid moves are available.
     */
    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        if (gameStatus instanceof GameLogic) {
            GameLogic gameLogic = (GameLogic) gameStatus;
            List<Position> possibleOptions = gameLogic.ValidMoves();
            Player player = gameLogic.getCurrentPlayer();

            if (possibleOptions.isEmpty()) {
                return null;  // No valid moves available, return null or some default behavior.
            }

            boolean hasBombs = player.getNumber_of_bombs() > 0;
            boolean hasUnflip = player.getNumber_of_unflippedable() > 0;

            // Randomly select a position from the available options.
            int location = random.nextInt(possibleOptions.size());
            Position selectedPosition = possibleOptions.get(location);

            // Determine the type of disc to play based on available resources.
            Disc disc = createRandomDisc(gameLogic, hasBombs, hasUnflip);

            // Create and return the move.
            return new Move(disc, selectedPosition, null);
        }

        return null;
    }
    /**
     * Helper method to create a random disc based on the current player's resources.
     * Randomly selects between SimpleDisc, BombDisc, or UnflippableDisc based on the
     * player's available resources (bombs and unflippable discs).
     *
     * @param gameLogic The current game logic, providing the current player.
     * @param hasBombs A boolean indicating if the player has bombs available.
     * @param hasUnflip A boolean indicating if the player has unflippable discs available.
     * @return A randomly chosen disc (SimpleDisc, BombDisc, or UnflippableDisc).
     */
    // Helper method to create a random disc type based on player resources.
    private static Disc createRandomDisc(GameLogic gameLogic, boolean hasBombs, boolean hasUnflip) {
        Disc disc;

        if (!(hasBombs || hasUnflip)) {
            // Only SimpleDisc is available when no bombs or unflippable discs are available.
            disc = new SimpleDisc(gameLogic.getCurrentPlayer());
        } else {
            int kind;
            if (hasBombs && hasUnflip) {
                // Randomly choose between Simple, Bomb, or Unflippable disc.
                kind = random.nextInt(3);
            } else {
                // Choose between Simple and one of the available discs.
                kind = random.nextInt(2);
            }

            switch (kind) {
                case 0:
                    disc = new SimpleDisc(gameLogic.getCurrentPlayer());
                    break;
                case 1:
                    disc = hasBombs ? new BombDisc(gameLogic.getCurrentPlayer()) : new UnflippableDisc(gameLogic.getCurrentPlayer());
                    break;
                default:
                    disc = new BombDisc(gameLogic.getCurrentPlayer());
                    break;
            }
        }
        return disc;
    }
}

