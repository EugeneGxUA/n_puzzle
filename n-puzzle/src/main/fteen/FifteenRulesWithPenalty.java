package main.fteen;

import main.algorithm.interfaces.Rules;

public class FifteenRulesWithPenalty extends FifteenRules {
    /**
     * @param terminateState - finish state
     * @param fieldSize      - field size (cell's count at one side of field)
     */
    public FifteenRulesWithPenalty(byte[] terminateState, int fieldSize) {
        super(terminateState, fieldSize);
    }

    @Override
    public int getH(FifteenState state) {
        int res = 0;
        int penalty = sideSize;
        for (int i = 0; i < size; i++) {
            if ((i + 1) % sideSize == 0) {
                penalty--;
            }
            if (state.getField()[i] != terminalState[i]) {
                res += penalty;
            }
        }
        return res;
    }
}
