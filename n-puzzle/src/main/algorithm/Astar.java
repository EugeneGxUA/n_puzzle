package main.algorithm;

/**
 * @author Eugene Garagulya on 1/19/19.
 */

import main.algorithm.states.State;
import main.algorithm.interfaces.Rules;

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

        //initialise close list - list of elements which we already used
        List<Integer> close = new LinkedList<Integer>();
        //initialise open q - list of elements which we need to check
        PriorityQueue<TState> open = new PriorityQueue<TState>(new Comparator<TState>() { @Override public int compare(TState o1, TState o2) {
            if (o1.getF() > o2.getF()) {
                return 1;
            } else {
                return -1;
            }
        } });

        //add to open list startState - start position
        open.add(startState);
        //At start pos G is 0
        startState.setG(0);
        //get cost of this vertex
        startState.setH(rules.getH(startState));

        // TODO: 2019-02-02 Write array into file JSON
        // TODO: 2019-02-02 Add one more heruistic method
        //While open list is not empty use algo
        while (!open.isEmpty()) {
            TState stateWithMinF = getStateWithMinF(open);
            if (rules.isTerminate(stateWithMinF)) {
                closedStates =  close.size();
                openStates = open.size();
                return completeSolution(stateWithMinF);
            }
            open.remove(stateWithMinF);

            close.add(stateWithMinF.hashCode());
            List<TState> neighbors = rules.getNeighbors(stateWithMinF);
            allMoves += neighbors.size();
            for (TState neighbor : neighbors) {

                if (close.contains(neighbor.hashCode())) {
                    continue;
                }

                int g = stateWithMinF.getG() + rules.getDistance(stateWithMinF, neighbor);

                if (!open.contains(neighbor)) {
                    neighbor.setH(rules.getH(neighbor));
                    neighbor.setG(g);
                    neighbor.setParent(stateWithMinF);

                    open.add(neighbor);
                } else if (g < neighbor.getG()){
                    open.remove(neighbor);
                    neighbor.setH(rules.getH(neighbor));
                    neighbor.setG(g);
                    neighbor.setParent(stateWithMinF);
                    open.add(neighbor);
                }

            }

        }

        throw new IllegalStateException("Illegal state");
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

    public int getOpenStates() {
        return openStates;
    }

    public int getAllMoves() {
        return allMoves;
    }

    private TRules rules;
    private int closedStates = 0;
    private int openStates = 0;
    private int allMoves = 0;
}
