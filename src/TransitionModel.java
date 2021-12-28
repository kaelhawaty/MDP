/**
 *  Interface that returns the probability to transition to State S’ given State S and Action a -> P(S’ | S, a).
 */
interface TransitionModel{
    double getProbability(State start, State end, Action a);
}

/**
 * Implementation for TransitionModel Interface where it returns all the allowable actions, all the allowable states,
 * and returns the probabilities needed by the TransitionModel.
 */
class Board implements TransitionModel {
    private int[][] board;
    final int N = 3;
    public Board(int r) {
        this.board = new int[][]{{r, -1, 10}, {-1, -1, -1}, {-1, -1, -1}};
    }

    /**
     * Returns all the allowable state given the current Board.
     */
    public State[] getStates() {
        State[] arr = new State[N*N];
        for(int i = 0; i < N; i++){
            for(int j = 0; j < N; j++){
                arr[i * 3 + j] = new BoardState(i * 3 + j, i, j, board[i][j], (i == 0 && (j == 0 || j == 2)));
            }
        }
        return arr;
    }

    /**
     * Returns all the allowable actions for this board.
     */
    public Action[] getActions() {
        return new Action[]{new BoardAction(0, 1, "East"),
                new BoardAction(0, -1, "West"),
                new BoardAction(1, 0, "South"),
                new BoardAction(-1, 0, "North")};
    }

    /**
     *  Returns the probability to transition to State S’ given State S and Action a -> P(S’ | S, a).
     */
    public double getProbability(State start, State end, Action a) {
        BoardState start_state = (BoardState) start;
        if (start_state.isTerminal()) {
            return 0.0;
        }
        BoardState end_state = (BoardState) end;
        BoardAction action = (BoardAction) a;
        int dx = (action.getDy() != 0) ? 1 : 0, dy = (action.getDx() != 0) ? 1 : 0;
        int [] next_xs = new int[]{start_state.getX() + action.getDx(), start_state.getX() + dx, start_state.getX() - dx};
        int [] next_ys = new int[]{start_state.getY() + action.getDy(), start_state.getY() + dy, start_state.getY() - dy};
        // Check if the end state is in the same direction as the given action.
        if (next_xs[0] == end_state.getX() && next_ys[0] == end_state.getY()) {
            return 0.8;
        }
        if (start == end) {
            // Check if left or right movements perpendicular to the given action
            // results in out of board movement.
            double prob = 0;
            for (int i = 1; i < N; ++i) {
                if (next_xs[i] < 0 || next_xs[i] == 3) {
                    prob += 0.1;
                } else if (next_ys[i] < 0 || next_ys[i] == 3) {
                    prob += 0.1;
                }
            }
            return prob;
        }
        // Check if the end state is the left or right state perpendicular to the given action.
        for (int i = 1; i < N; ++i) {
            if (next_xs[i] == end_state.getX() && next_ys[i] == end_state.getY()) {
                return 0.1;
            }
        }
        return 0.0;
    }
}
