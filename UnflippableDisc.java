/**
 * Represents a disc that cannot be flipped in the game.
 * The UnflippableDisc is a special type of disc that is owned by a player but cannot
 * be flipped by the opponent. It extends the Disc class and overrides the getType and
 * setOwner methods to implement its unique behavior.
 */
public class UnflippableDisc extends Disc{

    /**
     * Constructor for creating an UnflippableDisc.
     * Initializes the UnflippableDisc with the specified player.
     *
     * @param player The player who owns this disc.
     */
    public UnflippableDisc(Player player){
        super(player);
    }
    /**
     * Returns the type of the disc as a string.
     * In this case, it returns the symbol "⭕" to represent the UnflippableDisc.
     *
     * @return The string representation of the UnflippableDisc type ("⭕").
     */
    @Override
    public String getType() {
        return "⭕";
    }
    /**
     * Prevents changing the owner of the UnflippableDisc.
     * This method is overridden to do nothing, as UnflippableDisc cannot have its owner changed.
     *
     * @param player The player to set as the owner (this method does nothing).
     */

    @Override
    public void setOwner(Player player) {
        return;
    }
}
