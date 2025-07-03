import java.util.Objects;

/**
 * The Position class represents a position on the game board.
 * It stores the row and column coordinates of a position and provides methods
 * to retrieve these values, compare positions, and generate a string representation.
 */
public class Position {

    // The row coordinate of the position
    private final int row;

    // The column coordinate of the position
    private final int col;

    /**
     * Constructs a new Position object with the specified row and column.
     *
     * @param row The row coordinate of the position.
     * @param col The column coordinate of the position.
     */
    public Position(int row, int col) {
        this.row = row;  // Set the row value
        this.col = col;  // Set the column value
    }

    /**
     * Gets the row coordinate of this position.
     *
     * @return The row coordinate of the position.
     */
    public int row() {
        return this.row;
    }

    /**
     * Gets the column coordinate of this position.
     *
     * @return The column coordinate of the position.
     */
    public int col() {
        return this.col;
    }

    /**
     * Returns a string representation of the position in the format (row, col).
     *
     * @return A string representation of the position.
     */
    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";  // Return the position in a readable format
    }

    /**
     * Checks if two Position objects are equal based on their row and column values.
     *
     * @param obj The object to compare this position with.
     * @return true if the positions have the same row and column, false otherwise.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;  // If the objects are the same instance, they are equal
        if (obj == null || getClass() != obj.getClass()) return false;  // Null check and class check
        Position position = (Position) obj;  // Cast the object to Position
        return row == position.row && col == position.col;  // Compare the row and column
    }
    /**
     * Computes the hash code for a Position object based on its row and column values.
     * This hash code is used to uniquely identify the position in hash-based collections like HashSet or HashMap.
     * The hash code is calculated by multiplying the row value by 31 and adding the column value.
     * This ensures that the combination of row and column uniquely identifies the position in the board.
     *
     * @return The computed hash code for this Position object.
     */
    @Override
    public int hashCode() {
        return 31 * row + col;
    }
}
