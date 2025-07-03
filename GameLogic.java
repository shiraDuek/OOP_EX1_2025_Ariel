import java.util.*;


public class GameLogic implements PlayableLogic {
    // The size of the game board (8x8)
    private final int boardSize = 8;
    // The game board represented by a 2D array of Disc objects
    private Disc[][] gameBoard = new Disc[boardSize][boardSize];
    // Player 1 and Player 2 objects
    private Player player1;
    private Player player2;
    // A boolean indicating the turn of the player. If true, it's player 1's turn, otherwise player 2's turn.
    private boolean turn = true;
    // A stack to keep track of the move history (used for undo functionality)
    private Stack<Move> historyMoves = new Stack<>();
    // A boolean indicating if the game mode is only human players (true) or AI can also play (false)
    private boolean onlyHumen;
    // Array that stores all possible directions a move can go in (8 directions)
    private Position[] arrDirections = new Position[8];
    // Initialize the direction array with the possible 8 directions
    {
        arrDirections[0] = new Position(1, 1);
        arrDirections[1] = new Position(1, 0);
        arrDirections[2] = new Position(-1, 0);
        arrDirections[3] = new Position(-1, 1);
        arrDirections[4] = new Position(1, -1);
        arrDirections[5] = new Position(-1, -1);
        arrDirections[6] = new Position(0, 1);
        arrDirections[7] = new Position(0, -1);
    }

    /**
     * Executes a move for the current player, placing a disc at the specified position on the board.
     * If the move is valid, the disc is placed, and any opponent's discs between the placed disc
     * and the current player's discs are flipped. Handles special disc types like BombDisc and UnflippableDisc.
     *
     * @param a The position on the board where the disc will be placed.
     * @param disc The disc that will be placed at the given position.
     * @return Returns true if the move is valid and executed, false otherwise.
     */
    @Override
    public boolean locate_disc(Position a, Disc disc) {
        String str;
        Player player = getCurrentPlayer();
        // Determine which player is making the move
        if (player.equals(player1))
            str = "Player 1";
        else
            str = "Player 2";
        // Check if the move is valid
        if (ValidMoves().contains(a)) {
            // Handle special discs (Bomb or Unflippable)
            if (disc instanceof BombDisc) {
                if (player.getNumber_of_bombs() == 0)
                    return false;  // No bombs left
                else
                    player.reduce_bomb();  // Deduct bomb usage
            }
            if (disc instanceof UnflippableDisc) {
                if (player.getNumber_of_unflippedable() == 0)
                    return false;  // No unflippable discs left
                else
                    player.reduce_unflippedable();  // Deduct unflippable disc usage
            }
            gameBoard[a.row()][a.col()] = disc;  // Place the disc on the board
            System.out.println(str + " placed a " + disc.getType() + " in " + a.toString());
            HashSet<Position> positionsToFlip = new HashSet<>();

            flipsForLocation(a,positionsToFlip);

            Stack<Position> history=new Stack<>();
            for (Position pos : positionsToFlip) {
                history.add(pos);
                Disc dTemp=getDiscAtPosition(pos);
                dTemp.setOwner(player);
                System.out.println(str + " flipped the " + dTemp.getType() + " in " + pos.toString());
            }
            Move move = new Move(disc, a, history);
            historyMoves.push(move);
            turn = !turn;  // Toggle player turn
            System.out.println();
            return true;
        }
        return false;
    }
/**
 * Checks all 8 directions from the given position to find and record discs that need to be flipped.
            * It iterates through all possible directions and flips any opponent's discs between the given position
            * and the next owned disc.
 *
         * @param pos The position on the board where the disc is placed.
 * @param setFlip A set to keep track of the discs that need to be flipped.
            */
    private void flipsForLocation(Position pos, HashSet<Position> setFlip) {
        Player player = getCurrentPlayer();

        for (Position arrDirection : arrDirections) {
            int xDirection = arrDirection.col();
            int yDirection = arrDirection.row();
            processDirection(pos, xDirection, yDirection, player, setFlip);
        }
    }

    /**
     * Processes a single direction from the specified position to check for opponent's discs to flip.
     * It adds discs to the flip set if they are between the placed disc and the player's own discs.
     *
     * @param pos The position on the board where the disc is placed.
     * @param xDirection The direction in the x-axis to check.
     * @param yDirection The direction in the y-axis to check.
     * @param player The current player who is making the move.
     * @param setFlip A set to keep track of the discs that need to be flipped.
     */
    private void processDirection(Position pos, int xDirection, int yDirection, Player player, HashSet<Position> setFlip) {
        Stack<Disc> temp = new Stack<>();
        Stack<Position> tempHistory = new Stack<>();
        for (int x = pos.row() + xDirection, y = pos.col() + yDirection;
             x >= 0 && x < boardSize && y >= 0 && y < boardSize;
             x += xDirection, y += yDirection) {

            if (gameBoard[x][y] == null) {
                break;
            }

            Disc disc = gameBoard[x][y];
            if (disc.getOwner().equals(player)) {
                recordFlips( tempHistory, setFlip);
                break;
            } else {
                tempHistory.push(new Position(x, y));
                temp.push(disc);
            }
        }
        temp.clear();

    }
    /**
     * Records the positions of the discs that should be flipped.
     * Adds discs to the flip set only if they are not already in the set, and ensures they are not unflippable discs.
     *
     * @param tempHistory A stack of positions that have been identified for flipping.
     * @param setFlip A set to store the positions of discs to be flipped.
     */
    private void recordFlips(Stack<Position> tempHistory, HashSet<Position> setFlip) {
        while (!tempHistory.empty()) {
            Position positionTemp = tempHistory.pop();
            Disc discTemp=getDiscAtPosition(positionTemp);
            if (!setFlip.contains(positionTemp)) {
                if (!(discTemp instanceof UnflippableDisc)) {
                    setFlip.add(positionTemp);
                    if (discTemp instanceof BombDisc) {
                        addFlipsForBomb(positionTemp, setFlip);
                    }
                }
            }
        }
    }
/**
 * Handles the flipping of discs around a bomb disc. It checks all 8 directions from the bomb's position
 * and flips any opponent's discs adjacent to it. This function also recursively handles bombs
 * that may be present in adjacent positions.
 *
 * @param bomb The position of the bomb disc on the board.
 * @param setFlip A set to track the discs that need to be flipped.
 */

    private void addFlipsForBomb(Position bomb, Set<Position> setFlip) {
        Disc disc1 = gameBoard[bomb.row()][bomb.col()];
        Stack<Position> bombs= new Stack<>();
        if (disc1 instanceof BombDisc) {
            Player player = getCurrentPlayer();
            for (Position arrDirection : arrDirections) {
                int xDirection = arrDirection.col();
                int yDirection = arrDirection.row();
                int x = bomb.row() + xDirection, y = bomb.col() + yDirection;
                // Check bounds and if there's a disc to flip
                if (x >= 0 && x < boardSize && y >= 0 && y < boardSize) {
                    if (gameBoard[x][y] != null) {
                        Disc disc = gameBoard[x][y];
                        Position current = new Position(x, y);
                        // Avoid duplicate positions in the flip list
                        if (setFlip.contains(current)) {
                            continue;
                        }
                        // Check if the disc is owned by the opponent and add it to flip list
                        if ((!disc.getOwner().equals(player))&&!(disc instanceof UnflippableDisc)) {
                                setFlip.add(current);
                        }
                        // If it's a bomb, recurse to check surrounding discs
                        if (disc instanceof BombDisc&&setFlip.contains(current)) {
                            addFlipsForBomb(new Position(x, y), setFlip);
                        }
                    }
                }
            }
        }
    }

    // Return the disc at a specific position on the board
    @Override
    public Disc getDiscAtPosition(Position position) {
        return gameBoard[position.row()][position.col()];
    }

    // Return the size of the game board
    @Override
    public int getBoardSize() {
        return boardSize;
    }
    /**
     * Finds and returns all valid moves for the current player.
     * This function checks each position on the board to determine if placing a disc there would result
     * in a valid move for the current player. A move is valid if it can capture at least one opponent's disc
     * when placed at that position.
     *
     * @return A list of valid positions where the current player can place a disc.
     */
    @Override
    public List<Position> ValidMoves() {
        Player player = getCurrentPlayer();
        List<Position> validMoves = new ArrayList<>();

        // Check each board position for valid moves
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (gameBoard[i][j] == null) {
                    if (possibleMove(i, j, player)) {
                        validMoves.add(new Position(i, j));  // Add valid position
                    }
                }
            }
        }
        return validMoves;
    }

    /**
     * Checks if a move at position (i, j) is possible for the current player.
     * This function checks if placing a disc at the given position would result in a valid move by
     * looking at all 8 possible directions (up, down, left, right, and diagonals).
     *
     * @param i The row index of the position.
     * @param j The column index of the position.
     * @param player The current player who is making the move.
     * @return true if the move at position (i, j) is valid for the current player, otherwise false.
     */    private boolean possibleMove(int i, int j, Player player) {
        for (int k = 0; k < 8; k++) {
            if (isGoodDirection(new Position(i, j), arrDirections[k], player)) {
                return true;  // If any direction is valid, return true
            }
        }
        return false;  // No valid directions found
    }

    /**
     * Checks if a direction from a given position is a valid move for the player.
     * This function examines a specific direction from the given position to determine if placing a disc there
     * would result in flipping at least one opponent's disc. The direction is valid if the path contains
     * one or more opponent's discs followed by one of the player's own discs.
     *
     * @param position The starting position on the board from where the direction is being checked.
     * @param direction The direction in which to check for valid moves (specified by a Position object representing a direction).
     * @param player The current player who is trying to make the move.
     * @return true if the direction contains a valid sequence of opponent's discs followed by the player's disc, otherwise false.
     */
    private boolean isGoodDirection(Position position, Position direction, Player player) {
        boolean wasOtherPlayer = false;
        int xDirection = direction.col();
        int yDirection = direction.row();

        for (int x = position.row() + xDirection, y = position.col() + yDirection;
             x >= 0 && x < boardSize && y >= 0 && y < boardSize;
             x += xDirection, y += yDirection) {
            Disc disc = gameBoard[x][y];

            if (disc == null) {
                return false;  // Hit an empty space, invalid direction
            } else if (disc.getOwner().equals(player)) {
                return wasOtherPlayer;  // Found a player's disc, if there was an opponent's disc before, return true
            } else {
                if (!(disc instanceof UnflippableDisc)) {
                    wasOtherPlayer = true;  // Found an opponent's disc
                }
            }
        }
        return false;
    }

    /**
     * Returns the current player whose turn it is.
     * This function checks the current game state to determine which player's turn it is, and returns the corresponding player.
     *
     * @return The player whose turn it is (either player1 or player2).
     */
    public Player getCurrentPlayer() {
        return turn ? player1 : player2;
    }
    /**
     * Counts the number of discs that would be flipped if a disc were placed at the given position.
     * This function checks all directions from the specified position and counts how many opponent's discs
     * would be flipped by placing a disc there.
     *
     * @param a The position on the board where the disc is being placed.
     * @return The number of discs that would be flipped if the current player places a disc at the given position.
     */
    public int countFlips(Position a) {
        HashSet<Position> flips= new HashSet<>();
        flipsForLocation(a,flips);
        return flips.size();
    }

    // Return the first player
    @Override
    public Player getFirstPlayer() {
        return player1;
    }

    // Return the second player
    @Override
    public Player getSecondPlayer() {
        return player2;
    }

    // Set the two players for the game
    @Override
    public void setPlayers(Player player1, Player player2) {
        this.player1 = player1;
        this.player2 = player2;
    }

    // Check if it's the first player's turn
    @Override
    public boolean isFirstPlayerTurn() {
        return turn;
    }

    // Check if the game has finished (no valid moves left)
    @Override
    public boolean isGameFinished() {
        if (ValidMoves().isEmpty()) {
            int winner = getWinner();
            if (winner == 1)
                player1.addWin();  // Player 1 wins
            else if (winner == 2)
                player2.addWin();  // Player 2 wins
            return true;
        }
        return false;
    }

    // Determine the winner based on the number of discs each player has
    private int getWinner() {
        int count1 = 0;
        int count2 = 0;

        // Count the discs owned by each player
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Disc disc = gameBoard[i][j];
                if (disc != null) {
                    if (disc.getOwner().equals(player1)) {
                        count1++;
                    } else if (disc.getOwner().equals(player2)) {
                        count2++;
                    }
                }
            }
        }

        // Return the winner (1 or 2), or 0 for a tie
        if (count1 > count2) {
            System.out.println("Player 1 wins with " + count1 + " discs! Player 2 had " + count2 + " discs.");
            return 1;
        }
        else if (count2 > count1) {
            System.out.println("Player 2 wins with " + count2 + " discs! Player 1 had " + count1 + " discs.");
            return 2;
        }
        return 0;
    }

    // Reset the game board to its initial state
    @Override
    public void reset() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                gameBoard[i][j] = null;
            }
        }
        // Place the initial discs in the center
        gameBoard[3][3] = new SimpleDisc(player1);
        gameBoard[3][4] = new SimpleDisc(player2);
        gameBoard[4][3] = new SimpleDisc(player2);
        gameBoard[4][4] = new SimpleDisc(player1);
        turn = true;  // Player 1 starts
        player1.reset_bombs_and_unflippedable();
        player2.reset_bombs_and_unflippedable();
        onlyHumen=player1.isHuman()&&player2.isHuman();
        historyMoves.clear();
    }

    /**
     * Undoes the last move made by the current player.
     * This function reverts the last move by removing the placed disc from the board, restoring any resources (bombs or unflippable discs)
     * used during the move, and flipping back any discs that were flipped as part of the move.
     * If there is no previous move to undo, the function will print a message indicating this.
     *
     * @note This operation is only available when the game is in human-player mode (not AI).
     */
    @Override
    public void undoLastMove() {
        if (!onlyHumen) return;  // Only allow undo in human-player mode
        System.out.println("Undoing last move:");

        // Check if there is a previous move to undo
        if (historyMoves.empty()) {
            System.out.println("\tNo previous move available to undo.");
            return;
        }

        // Retrieve the last move and the associated data
        Move lastMove = historyMoves.pop();
        Disc disc = lastMove.disc();
        Player player = disc.getOwner();

        // Restore the resources (bombs or unflippable discs)
        if (disc instanceof BombDisc) player.increase_bomb();
        else if (disc instanceof UnflippableDisc) player.increase_unflippedable();

        Position position = lastMove.position();
        System.out.println("\tUndo: removing " + disc.getType() + " from " + position.toString());

        // Remove the disc from the board
        gameBoard[lastMove.position().row()][lastMove.position().col()] = null;

        // Flip back the discs that were flipped during the move
        Stack<Position> historyFlips = lastMove.getFlips();
        while (!historyFlips.isEmpty()) {
            Position p = historyFlips.pop();
            gameBoard[p.row()][p.col()].setOwner(getCurrentPlayer());
            System.out.println("\tUndo: flipping back " + gameBoard[p.row()][p.col()].getType() + " in " + p);
        }

        // Switch the player's turn
        turn = !turn;
        System.out.println();
    }
}

