import java.util.Arrays;

public class Main {
    private final double EPS = 1e-6;
    private final double GAMMA = 0.99;

    public static void main(String[] args) {
        int[] rArr = new int[]{100, 3, 0, -3};
        for (int r : rArr) {
            Board board = new Board(r);
            Action[] actions = board.getActions();
            State[] states = board.getStates();
            Main solver = new Main();
            System.out.println("---------------------------------------");
            System.out.println("Current r: " + r);
            double[] U = solver.ValueIteration(states, actions, board);
            Action[] policies = solver.policyExtraction(states, actions, board, U);
            System.out.println("State values: ");
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    System.out.print(U[i * 3 + j] + "   ");
                }
                System.out.println();
            }

            System.out.println("---------------------------------------");
            System.out.println("State policies: ");
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    System.out.print(policies[i * 3 + j].getName() + " ");
                }
                System.out.println();
            }
            System.out.println("---------------------------------------");
            System.out.println("Policy iteration: ");
            Action[] policy = solver.policyIteration(states, actions, board);
            for (int i = 0; i < 3; i++) {
                for (int j = 0; j < 3; j++) {
                    System.out.print(policy[i * 3 + j].getName() + " ");
                }
                System.out.println();
            }
        }
    }

    /**
     * Returns optimal policies given the optimal values for each state.
     */
    private Action[] policyExtraction(State[] states, Action[] actions, TransitionModel model, double[] U) {
        Action[] policy = new Action[states.length];
        for (State state : states) {
            double maxActionValue = Integer.MIN_VALUE;
            for (Action action : actions) { // attempt all actions
                double actionValue = state.getReward();
                for (State next : states) { // attempt to reach all states using this action
                    actionValue += GAMMA * U[next.getID()] * model.getProbability(state, next, action);
                }
                if (maxActionValue < actionValue) { // find optimal action
                    policy[state.getID()] = action;
                    maxActionValue = actionValue;
                }
            }
        }
        return policy;
    }

    /**
     * Evaluates optimal values for each state by iteratively calculating the best action to take from each state
     * until the values converge.
     */
    private double[] ValueIteration(State[] states, Action[] actions, TransitionModel model) {
        double[] U = new double[states.length];
        int it = 0;
        double delta;
        do {
            delta = 0;
            double[] newU = U.clone();
            for (State state : states) {
                double maxActionValue = Integer.MIN_VALUE;
                for (Action action : actions) { // Attempt all actions
                    double actionValue = 0;
                    for (State next : states) { // Calculate expected action reward
                        actionValue += GAMMA * U[next.getID()] * model.getProbability(state, next, action);
                    }
                    maxActionValue = Math.max(maxActionValue, actionValue + state.getReward());
                }
                newU[state.getID()] = maxActionValue;
                delta = Math.max(delta, Math.abs(maxActionValue - U[state.getID()])); // check for convergence
            }
            U = newU;
            it++;
        } while (delta >= EPS);
        System.out.println("Number of iterations taken: " + it);
        return U;
    }

    /**
     * Evaluates a policy by returning the expected gain for each state.
     */
    private double[] policyEvaluation(State[] states, Action[] policies, TransitionModel model) {
        double[] U = new double[states.length];
        double delta;
        do {
            delta = 0;
            double[] newU = U.clone();
            for (State state : states) {
                double actionValue = 0;
                for (State next : states) {
                    actionValue += GAMMA * U[next.getID()] * model.getProbability(state, next, policies[state.getID()]);
                }
                actionValue += state.getReward();
                newU[state.getID()] = actionValue;
                delta = Math.max(delta, Math.abs(actionValue - U[state.getID()]));
            }
            U = newU;
        } while (delta >= EPS);
        return U;
    }

    /**
     * Evaluates the optimal policy iteratively by calculating the current expected gain and
     * generating a new policy from this expected gain.
     */
    private Action[] policyIteration(State[] states, Action[] actions, TransitionModel model) {
        // Initialize a policy with a random action, Here taken to be the first action.
        Action[] policy = new Action[states.length];
        Arrays.fill(policy, actions[0]);
        Action[] newPolicy = policy;
        int it = 0;
        do {
            policy = newPolicy;
            // Evaluate the current policy.
            double[] U = policyEvaluation(states, policy, model);
            // Extract a new policy from the current estimate.
            newPolicy = policyExtraction(states, actions, model, U);
            it++;
        } while (!Arrays.equals(policy, newPolicy));
        System.out.println("Number of iterations taken: " + it);
        return policy;
    }
}
