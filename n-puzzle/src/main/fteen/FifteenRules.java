package main.fteen;

import main.algorithm.interfaces.Rules;
import main.algorithm.states.State;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FifteenRules implements Rules<FifteenState> {

    protected int sideSize;
    protected int size;

    protected byte[] terminalState;

    private int left = -1;
    private int top;
    private int right = 1;
    private int bottom;
    protected int[] actions;

    /**
     *
     * @param terminateState - finish state
     * @param fieldSize - field size (cell's count at one side of field)
     */
    public FifteenRules(byte[] terminateState, int fieldSize) {
        if (fieldSize < 2) {
            throw new IllegalArgumentException("Invalid field");
        }
        if (terminateState == null) {
            throw new IllegalArgumentException("Terminal state can't be null");
        }

        this.sideSize = fieldSize;
        size = sideSize * sideSize;

        if (terminateState.length != size) {
            throw new IllegalArgumentException("Terminate state size is incorrect");
        }

        this.terminalState = terminateState;

        top = -sideSize;
        bottom = sideSize;

        actions = new int[] { top, bottom, left, right };
    }

    @Override
    public List<FifteenState> getNeighbors(FifteenState currentState) {
        ArrayList<FifteenState> res = new ArrayList<FifteenState>();
        for (int i = 0; i < actions.length; i++) {
            byte[] field = doAction(currentState.getField(), actions[i]);
            if (field == null) {
                continue;
            }

            FifteenState state = new FifteenState(currentState, sideSize);
            state.setField(field);
            res.add(state);
        }
        return res;
    }

    @Override
    public int getDistance(FifteenState a, FifteenState b) {
        State c = b;
        int res = 0;
        while ((c != null) && (!c.equals(a))) {
            c = c.getParent();
            res++;
        }

        return res;
    }

    @Override
    public int getH(FifteenState state) {
        int res = 0;
        for (int i = 0; i < size; i++) {
            if (state.getField()[i] != terminalState[i]) {
                res++;
            }
        }

        return res;
    }

    @Override
    public boolean isTerminate(FifteenState state) {
        return Arrays.equals(state.getField(), terminalState);
    }

    public byte[] getTerminateState() {
        return terminalState;
    }

    public int[] getActions() {
        return actions;
    }

    /**
     * Applies rule to state
     *
     * @param field - start state
     * @param action - apply rule
     * @return new state which get as result of rule apply. Null if state is impossible
     */
    public byte[] doAction(byte[] field, int action) {
        //Search empty cell
        int zero = 0;
        for (; zero < field.length; zero++) {

            if (field[zero] == 0) {
                break;
            }
            if (zero >= field.length) {
                return null;
            }

        }
        //Calculates the index of the cell being moved
        int number = zero + action;
        if (number < 0 || number >= field.length) {
            return null;
        }
        if ((action == 1) && (zero + 1) % sideSize == 0) {
            return null;
        }
        if ((action == -1) && (zero + 1) % sideSize == 1) {
            return null;
        }

        /*
        Create new field after we changed empty cell with moved cell
         */
        byte[] newField = Arrays.copyOf(field, field.length);
        byte temp = newField[zero];
        newField[zero] = newField[number];
        newField[number] = temp;

        return newField;
    }
}
