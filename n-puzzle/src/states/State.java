package states;

import java.util.Objects;

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

    public int getF() {
        return g + h;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        State state = (State) o;
        return g == state.g &&
                h == state.h &&
                Objects.equals(parent, state.parent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(g, h, parent);
    }
}
