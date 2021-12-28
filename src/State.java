/**
 * Interface a given State in the MDP problem
 */
interface State {
    /**
     * Returns ynique Identifier for a State.
      */
    int getID();

    /**
     * Returns True if this is an absorbing/end state.
     */
    boolean isTerminal();

    /**
     * Returns the reward of this state.
     */
    double getReward();
}

/**
 *  Implementation for the State interface containing a unique ID, position on the board, reward for the given state,
 *  and whether it is a terminal state or not.
 */
class BoardState implements State {
    // Unique identifier for each state.
    private int id;
    // Position in the board.
    private int x, y;
    private double reward;
    private boolean isTerminal;

    public BoardState(int id, int x, int y, double reward, boolean isTerminal) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.reward = reward;
        this.isTerminal = isTerminal;
    }

    @Override
    public int getID() {
        return id;
    }

    @Override
    public double getReward() {
        return reward;
    }

    @Override
    public boolean isTerminal() {
        return isTerminal;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }
}
