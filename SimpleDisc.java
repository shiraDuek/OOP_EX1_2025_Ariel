/**
 * Represents a simple disc used in the game.
 * The SimpleDisc is a basic type of disc that can be placed on the game board.
 * It extends from the base Disc class and overrides the getType method to return a specific representation.
 */
public class SimpleDisc extends Disc{

    /**
     * Constructor for creating a SimpleDisc.
     * Initializes the SimpleDisc with the specified player.
     *
     * @param player The player who owns this disc.
     */
    public SimpleDisc(Player player){
        super(player);
    }
    /**
     * Returns the type of the disc as a string.
     * In this case, it returns the symbol "⬤" to represent the SimpleDisc.
     *
     * @return The string representation of the SimpleDisc type ("⬤").
     */
    @Override
    public String getType() {
        return "⬤";
    }
}
