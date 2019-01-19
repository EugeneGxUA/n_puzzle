package states;

/**
 * @author Eugene Garagulya on 1/19/19.
 */
public abstract class State {

    private int g;
    private int h;
    private State parent;


    public int getG() {
        return g;
    }

    public void setG(int g) {
        this.g = g;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public State getParent() {
        return parent;
    }

    public void setParent(State parent) {
        this.parent = parent;
    }

    @Override
    public boolean equals(Object obj) {

        try {
            State state = (State) obj;
            if (state.g == this.g && state.h == this.h) {
                return true;
            } else {
                return false;
            }
        } catch (ClassCastException e) {
            return false;
        }
    }
}
