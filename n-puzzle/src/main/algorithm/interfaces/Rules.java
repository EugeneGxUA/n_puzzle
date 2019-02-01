package main.algorithm.interfaces;

import main.algorithm.states.State;

import java.util.List;

/**
 * @author Eugene Garagulya on 1/19/19.
 */
public interface Rules <TState extends State> {
    /**
     * Возвращает список состояний, в которые может быть осуществлен переход из
     * указанного состояния.
     *
     * @param currentState - текущее состояние, для которого раскрываются соседние.
     * @return список состояний, в которые может быть осуществлен переход из
     *         указанного состояния.
     */
    List<TState> getNeighbors(TState currentState);

    /**
     * Возвращает растояние между указанными состояниями.
     *
     * @param a - первое состояние.
     * @param b - второе состояние.
     * @return растояние между указанными состояниями.
     */
    int getDistance(TState a, TState b);

    /**
     * Вычисляет эвристическую оценку расстояния от указанного состояния до
     * конечного.
     *
     * @param state - текущее состояние.
     * @return значение оценки расстояния от указанного состояния до конечного.
     */
    int getH(TState state);

    /**
     * Check is this state terminal
     *
     * @param state
     * @return true, if state is terminal.
     */
    boolean isTerminate(TState state);
}
