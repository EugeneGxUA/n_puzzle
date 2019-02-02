package main.fteen;

public class FifteenRulesManhattan extends FifteenRules {
    /**
     * @param terminateState - finish state
     * @param fieldSize      - field size (cell's count at one side of field)
     */
    public FifteenRulesManhattan(byte[] terminateState, int fieldSize) {
        super(terminateState, fieldSize);
    }

    @Override
    public int getH(FifteenState state) {
        int manhattanCounter = 0;

        int colMain = 0;
        int rowMain = 0;

        int colTerm = 0;
        int rowTerm = 0;
        for (int i = 0; i < state.getField().length; i++) {
            colMain++;
            rowMain = i / sideSize + 1;
            for (int j = 0; j < terminalState.length; j++) {
                rowTerm = j / sideSize + 1;
                colTerm++;
                if (state.getField()[i] == terminalState[j]) {
                    double diff = Math.sqrt(Math.pow(rowTerm - rowMain, 2) +  Math.pow(colTerm - colMain, 2));
                    manhattanCounter += diff;
                }

                if (colTerm == sideSize) {
                    colTerm = 1;
                }
            }

            if (colMain == sideSize) {
                colMain = 1;
            }
        }

        return manhattanCounter;
    }
}
