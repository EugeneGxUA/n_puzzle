package main.algorithm;

/**
 * @author Eugene Garagulya on 1/19/19.
 */

import main.algorithm.states.State;
import main.algorithm.interfaces.Rules;
import main.fteen.FifteenState;

import java.util.*;

/**
 * Realization of A* search solution algorithm
 */
public class Astar <TState extends State, TRules extends Rules<TState>> {

    /**
     * @param startState - start state.
     * @return sequence of main.algorithm.states from initial to final
     *
     * Algorithm А* to search for a shorter path to the terminal state selected.
     */
    public Collection<State> search(TState startState) {
        LinkedList<Integer> close = new LinkedList<Integer>();
        PriorityQueue<TState> open = new PriorityQueue<>();
        open.add(startState);
        startState.setG(0);
        startState.setH(rules.getH(startState));

        while (!open.isEmpty()) {
            TState x = getStateWithMinF(open);
            if (rules.isTerminate(x)) {
                closedStates = close.size();
                return completeSolution(x);
            }
            open.remove(x);
            close.add(x.hashCode());
            List<TState> neighbors = rules.getNeighbors(x);
            for (TState neighbor : neighbors) {
                if (close.contains(neighbor.hashCode())) {
                    continue;
                }
                int g = x.getG() + rules.getDistance(x, neighbor);
                boolean isGBetter;
                if (!open.contains(neighbor)) {
                    neighbor.setH(rules.getH(neighbor));
                    if (open.size() == 8) {
                        PriorityQueue<TState> tempQ = new PriorityQueue<>(open);
                        open.clear();
                        for (int i = 0; i < 7; i++) {
                            open.add(tempQ.poll());
                        }
                    }
                    open.add(neighbor);
                    isGBetter = true;
                } else {
                    isGBetter = g < neighbor.getG();
                }
                if (isGBetter) {
                    neighbor.setParent(x);
                    neighbor.setG(g);
                }
            }
        }
        return null;
    }


    /**
     * This method creates sequence of main.algorithm.states from initial to final
     *
     * @param terminate last state
     * @return sequences of main.algorithm.states passed from initial to final
     */
    private Collection<State> completeSolution(TState terminate) {
        Deque<TState> path = new LinkedList<TState>();
        State c = terminate;
        while (c != null) {
            path.addFirst((TState) c);
            c = c.getParent();
        }
        return (Collection<State>) path;
    }

    /**
     * This method searches vertices with smallest weight in list
     *
     * @param open list with opened vertices
     * @return vertex with smallest weight
     */
    private TState getStateWithMinF(Collection<TState> open) {
        TState vertex = null;
        int min = Integer.MAX_VALUE;
        for (TState state : open) {

            if (state.getF() < min) {
                min = state.getF();
                vertex = state;
            }

        }

        return vertex;
    }

    public int getClosedStatesCount() {
        return closedStates;
    }



    public Astar(TRules rules) throws IllegalAccessException {
        if (rules == null) {
            throw new IllegalAccessException("Rules can't be null");
        }

        this.rules = rules;
    }

    private TRules rules;
    private int closedStates = 0;
}
