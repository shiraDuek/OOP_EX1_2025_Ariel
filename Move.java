import javax.swing.*;
import java.util.List;
import java.util.Stack;

/**
 * The Move class represents a single move made by a player in the game.
 * It stores information about the position where the move was made,
 * the disc used for the move, and the stack of positions that were flipped as a result of the move.
 */
public class Move {

    // Stack that holds the positions of the discs that were flipped during the move
    private Stack<Position> flips = new Stack<>();

    // The position where the disc was placed
    private final Position position;

    // The disc that was placed on the board
    private final Disc disc;

    /**
     * Constructs a new Move object with the given disc, position, and flipped positions.
     *
     * @param d The disc that was placed during the move.
     * @param p The position on the board where the disc was placed.
     * @param f A stack of positions that were flipped as a result of the move.
     */
    public Move(Disc d, Position p, Stack<Position> f) {
        this.position = p;  // Set the position where the disc was placed
        this.disc = d;  // Set the disc used for the move
        this.flips = f;  // Set the stack of flipped positions
    }

    /**
     * Gets the disc used for this move.
     *
     * @return The disc that was placed during the move.
     */
    public Disc disc() {
        return disc;
    }

    /**
     * Gets the stack of positions that were flipped as a result of this move.
     *
     * @return The stack of flipped positions.
     */
    public Stack<Position> getFlips() {
        return flips;
    }


    /**
     * Gets the position where the disc was placed during this move.
     *
     * @return The position where the disc was placed.
     */
    public Position position() {
        return this.position;
    }
}
