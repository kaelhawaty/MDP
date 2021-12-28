/**
 * Interface that represents action taken from some space.
 */
interface Action {
    String getName();
}

/**
 *  Implementation for the Action interface containing the direction at which we move (North, East, West, South)
 */
class BoardAction implements Action {
    // String identifier for the action.
    private final String name;
    // Direction of the action.
    private final int dx, dy;

    public BoardAction(int dx, int dy, String name){
        this.dx = dx;
        this.dy = dy;
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }
}
