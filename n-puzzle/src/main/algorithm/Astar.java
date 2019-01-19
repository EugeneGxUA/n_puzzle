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



        return null;
    }
}
