package main.fteen;

import main.algorithm.Astar;
import main.algorithm.states.State;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Random;

public class MainFifteen {

    public static final byte SOLUTION_TYPE_SNAIL = 5;
    public static final byte SOLUTION_TYPE_ORDINARY = 7;

    private static boolean isShow = false;
    private static boolean isReadStream = false;

    private static int sideSize = 4;

    private static int stepCount = 10;

    private static byte[] startField;
    private static byte[] terminateField;

    public static void main(String[] args) {
        parseArgs(args);

        if (isReadStream) {
            try {
                startField = readStartStateFromSrc();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }

//            if (sideSize == 4 && !FifteenState.checkState(startField, sideSize)) {
//                System.out.println("I can't find a way, how to solve this fifteen");
//                System.exit(1);
//            }
        }

        int size = sideSize * sideSize;
        terminateField = getOrdinaryTerminalState(size, sideSize);

//        FifteenRules rules = new FifteenRulesWithPenalty(terminateField, sideSize);
        FifteenRules rules = new FifteenRules(terminateField, sideSize);
        FifteenState startState = new FifteenState(null, sideSize);

        if (startField == null) {
            startField = generateStartState(stepCount, rules);
        }

        if (sideSize == 4 && !FifteenState.checkState(startField, sideSize)) {
            System.out.println("This field is unsolvable");
            System.exit(1);
        }

        startState.setField(startField);
        System.out.println(sideSize);
        System.out.println(startState);

        try {
            Astar<FifteenState, FifteenRules> astar = new Astar<FifteenState, FifteenRules>(rules);

            long time = System.currentTimeMillis();
            Collection<State> res = astar.search(startState);
            time = System.currentTimeMillis() - time;

            if (res == null) {
                System.out.println("Solution not found");
                System.exit(0);
            } else {
                for (State state : res) {
                    System.out.println(state.toString());
                }
            }

            if (isShow) {
                System.out.println("Time " + time + " ms.");
                System.out.println("Solution length: " + (res.size() - 1));
                System.out.println("Opened states: " + astar.getClosedStatesCount());
            }

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

    }

    /**
     * Generate start state by swapCount switch
     *
     * @param swapCount - count if switch
     * @param rules
     * @return - generated start state
     */
    private static byte[] generateStartState(int swapCount, FifteenRules rules) {


        return new byte[]  {2, 1, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0};

//        int stepCount = swapCount;
//        byte[] startState = rules.getTerminateState();
//
//        int[] actions = rules.getActions();
//
//        Random random = new Random();
//
//        System.out.println("Start generate field with: " + sideSize + "x" + sideSize + " size;");
//        while (stepCount > 0) {
//            int j = random.nextInt(actions.length);
//            byte[] state = rules.doAction(startState, actions[j]);
//
//            if (state != null) {
//                startState = state;
//                stepCount--;
//            }
//        }
//        System.out.println("Field generate is finished;");
//        return startState;
    }

    /**
     * Read start state from input stream, defining dimension
     * of the field use quantity of strings in stream.
     *
     * @return byte[] which describe start state or null,
     *         if can't read start state.
     * @throws IOException
     */
    private static byte[] readStartStateFromSrc() throws IOException {


        System.out.println("Reading from stream - enter filename or field");

        InputStreamReader isrt = new InputStreamReader(System.in);

        BufferedReader reader = new BufferedReader(isrt);

        String line = null;
        sideSize = 0;
        StringBuffer buffer = new StringBuffer();

        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) {
                break;
            }
            if (line.endsWith(".txt")) {
                //read file
            }

            buffer.append(line);
            sideSize++;
        }

        String state = buffer.toString();
        if (state.isEmpty()) {
            return null;
        } else {
            return FifteenState.parseField(state);
        }
    }

    /**
     * Generate a terminal state as an ordered sequence
     *
     * @param size
     * @param sideSize
     * @return terminal state as an ordered sequence
     */
    private static byte[] getOrdinaryTerminalState(int size, int sideSize) {
        if (terminateField == null) {
            terminateField = new byte[size];
            byte k = 0;

            for (int i = 0; i < sideSize; i++) {

                for (int j = 0; j < sideSize; j++) {
                    terminateField[j + i * sideSize] = ++k;
                }

            }
            terminateField[size - 1] = 0;
        }

        return terminateField;
    }

    /**
     * Check start application params
     *
     * @param args
     */
    private static void parseArgs(String[] args) {

        if (args == null || args.length == 0) {
            return;
        }

        for (int i = 0; i < args.length; i++) {

            if (args[i].equals("-f") || args[i].equals("-i")) {
                isReadStream = true;
                continue;
            }

            if (args[i].equals("-h")) {
                try {
                    showHelpLog();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                continue;
            }

            if (args[i].equals("-v")) {
                isShow = true;
                continue;
            }

            if (args[i].equals("-s")) {
                isReadStream = false;
                sideSize = Integer.parseInt(args[++i]);
                continue;
            }

            if (args[i].equals("-c")) {
                isReadStream = false;
                stepCount = Integer.parseInt(args[++i]);
                continue;
            }

            throw new IllegalArgumentException("Unknown arg: " + args[i]);

        }
    }

    private static void showHelpLog() throws IOException {
        InputStreamReader strm = new InputStreamReader(
                //create file with help and read it
                MainFifteen.class.getResourceAsStream("/help.en"), StandardCharsets.UTF_8);

        BufferedReader reader = new BufferedReader(strm);

        PrintStream out = new PrintStream(System.out, true);

        String str = null;

        while ((str = reader.readLine()) != null) {
            out.println(str);
        }
        reader.close();
        System.exit(0);
    }

}
