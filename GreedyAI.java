import java.util.*;

/**
 * The GreedyAI class represents an AI player that uses a greedy strategy to make moves.
 * The AI chooses the move that maximizes the number of discs it can flip in a single move.
 */
public class GreedyAI extends AIPlayer {

    /**
     * Constructs a GreedyAI player with a specified player type.
     *
     * @param isPlayerOne A boolean indicating whether this AI is Player 1 (true) or Player 2 (false).
     */
    public GreedyAI(boolean isPlayerOne) {
        super(isPlayerOne);  // Call the superclass constructor with the player's information
    }

    /**
     * Makes a move based on a greedy strategy, choosing the move that maximizes the number of discs flipped.
     *
     * @param gameStatus The current game status which is used to determine valid moves and flips.
     * @return A Move object representing the best move according to the greedy strategy.
     */
    @Override
    public Move makeMove(PlayableLogic gameStatus) {
        // Cast the gameStatus to GameLogic to access game-specific methods
        GameLogic gameLogic = (GameLogic) gameStatus;

        // Get the current player making the move
        Player player = gameLogic.getCurrentPlayer();

        // Get the list of valid moves for the current player
        List<Position> valid = gameLogic.ValidMoves();

        // Disc object that will be used to make a move
        Disc disc;

        // List to store the number of flips for each valid move
        List<Integer> countValid = new ArrayList<>();

        // Variables to track the maximum flips and the corresponding move
        int max = 0;
        int indexMax = 0;

        // List to store positions that result in the same number of flips
        List<Position> sameCount = new ArrayList<>();

        // Iterate through all valid moves to count the number of flips for each
        for (int i = 0; i < valid.size(); i++) {
            Position p = valid.get(i);

            // Get the number of flips for the current position
            int currentCount = gameLogic.countFlips(p);

            // Store the count of flips for this move
            countValid.add(currentCount);

            // Check if the current move results in more flips than previous moves
            if (currentCount > max) {
                max = currentCount;  // Update the maximum number of flips
                indexMax = i;  // Update the index of the move with maximum flips
                sameCount.clear();  // Clear previous same count positions
                sameCount.add(valid.get(i));  // Add this position to same count
            } else if (currentCount == max) {
                // If this move has the same number of flips, add it to the sameCount list
                sameCount.add(valid.get(i));
            }
        }

        // Create a new SimpleDisc object for the current player
        disc = new SimpleDisc(player);

        // If there is only one best move, return it
        if (sameCount.size() == 1) {
            return new Move(disc, valid.get(indexMax), null);
        } else {
            // If there are multiple best moves, sort them based on relevance
            Collections.sort(sameCount, new MaxRelevantComparator());

            // Return the last position in the sorted list, which is the most relevant one
            return new Move(disc, sameCount.get(sameCount.size() - 1), null);
        }
    }

    /**
     * Comparator class to sort positions based on column first, then row.
     * This is used when multiple moves have the same number of flips.
     */
    class MaxRelevantComparator implements Comparator<Position> {

        /**
         * Compares two positions first by their column, then by their row.
         *
         * @param p1 The first position to compare.
         * @param p2 The second position to compare.
         * @return A negative integer, zero, or a positive integer as the first position is less than,
         *         equal to, or greater than the second position.
         */
        @Override
        public int compare(Position p1, Position p2) {
            // Compare by column first
            if (Integer.compare(p1.col(), p2.col()) != 0)
                return Integer.compare(p1.col(), p2.col());
            else
                // If columns are equal, compare by row
                return Integer.compare(p1.row(), p2.row());
        }
    }
}
