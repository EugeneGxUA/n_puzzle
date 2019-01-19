package main.algorithm;

/**
 * @author Eugene Garagulya on 1/19/19.
 */

import states.State;
import states.interfaces.Rules;

import java.util.*;

/**
 * Realization of A* search solution algorithm
 */
public class Astar <TState extends State, TRules extends Rules<TState>> {

    /**
     * @param startState - start state.
     * @return последовательность состояния от заданного до терминального.
     *
     * Применяеться алгоритм А* для поиска кратчайшего пути до терминального состояния от указанного.
     */
    public Collection<State> search(TState startState) {
        List<TState> close = new LinkedList<TState>();
        Queue<TState> open = new PriorityQueue<TState>();

        open.add(startState);
        startState.setG(0);
        startState.setH(rules.getH(startState));

        while (!open.isEmpty()) {
            TState x = getStateWithMinF(open);
            if (rules.isTerminate(x)) {
                closeStates =  close.size();
                return completeSolution(x);
            }
        }

        return null;
    }


    /**
     * This method create sequence of states from initial to final
     *
     * @param terminate last state
     * @return sequences of states passed from initial to final
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
     * This method search top with smaller weight in list
     *
     * @param open list with opened tops
     * @return top with smaller weight
     */
    private TState getStateWithMinF(Collection<TState> open) {
        TState top = null;
        int min = Integer.MAX_VALUE;
        for (TState state : open) {
            if (state.getF() < min) {
                min = state.getF();
                top = state;
            }
        }

        return top;
    }



    public Astar(TRules rules) throws IllegalAccessException {
        if (rules == null) {
            throw new IllegalAccessException("Rules can't be null");
        }

        this.rules = rules;
    }

    private TRules rules;
    private int closeStates = 0;
}
