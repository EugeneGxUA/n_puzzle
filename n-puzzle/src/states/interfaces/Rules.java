package states.interfaces;

import states.State;

import java.util.List;

/**
 * @author Eugene Garagulya on 1/19/19.
 */
public interface Rules <TState extends State> {
    public List<TState> getNeighbors(TState currentState);

    public int getDistnace(TState a, TState b);

    public int getH(TState state);

    public boolean isTerminate(TState state);
}
