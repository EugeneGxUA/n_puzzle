package main.puzzle;

import main.algorithm.Astar;
import main.algorithm.states.State;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainFifteen {

    public static final byte SOLUTION_TYPE_SNAIL = 5;
    public static final byte SOLUTION_TYPE_ORDINARY = 7;

    private static boolean isShow = false;
    private static boolean isReadInputStream = false;
    private static boolean isReadFileStream = false;
    private static boolean isOrdinarySolve = false;
    private static boolean isBigPenalty = false;
    private static boolean isEasyHeruistick = false;

    private static int sideSize = 4;

    private static int stepCount = 40;

    private static byte[] startField;
    private static byte[] terminateField;

    private static String fileName;

    public static void main(String[] args) {
        parseArgs(args);

        if (isReadInputStream) {
            try {
                startField = readStartStateFromInput();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        } else if (isReadFileStream) {
            try {
                startField = readStartStateFromFile();
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(1);
            }
        }

        int size = sideSize * sideSize;
        if (isOrdinarySolve) {
            terminateField = getOrdinaryTerminalState(size, sideSize);
        } else {
            terminateField = getSnailTerminalState(sideSize);
        }

        final FifteenRules rules; {
            if (isBigPenalty) {
                rules = new FifteenRulesWithPenalty(terminateField, sideSize);
            } else if (isEasyHeruistick){
                rules = new FifteenRules(terminateField, sideSize);
            } else {
                rules = new FifteenRulesManhattan(terminateField, sideSize);
            }
        }

        final FifteenState startState = new FifteenState(null, sideSize);

        if (startField == null) {
            startField = generateStartState(stepCount, rules);
        }

        if (!FifteenState.checkState(startField, sideSize, SOLUTION_TYPE_ORDINARY)) {
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

//            if (isShow) {
                System.out.println("Time " + time + " ms.");
                System.out.println("Solution length: " + (res.size() - 1));
                System.out.println("Opened states: " + astar.getClosedStatesCount());
                System.out.println("Total number of states ever selected in the \"opened\": " + astar.getOpenStates());
                System.out.println("Number of moves required to transition from the initial state to the final state: " + astar.getAllMoves());
//            }

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

//        return new byte[]  {1, 5, 3, 7, 10, 13, 6, 15, 9, 12, 4, 8, 2, 0, 14, 11};

        int stepCount = swapCount;
        byte[] startState = rules.getTerminateState();

        int[] actions = rules.getActions();

        Random random = new Random();

        System.out.println("Start generate field with: " + sideSize + "x" + sideSize + " size;");
        while (stepCount > 0) {
            int j = random.nextInt(actions.length);
            byte[] state = rules.doAction(startState, actions[j]);

            if (state != null) {
                startState = state;
                stepCount--;
            }
        }
        System.out.println("Field generate is finished;");
        return startState;
    }

    /**
     * Read start state from input stream, defining dimension
     * of the field use quantity of strings in stream.
     *
     * @return byte[] which describe start state or null,
     *         if can't read start state.
     * @throws IOException
     */
    private static byte[] readStartStateFromInput() throws IOException {


        System.out.println("Reading from stream...");

        InputStreamReader isrt = new InputStreamReader(System.in);

        BufferedReader reader = new BufferedReader(isrt);

        String line;
        sideSize = 0;
        StringBuffer buffer = new StringBuffer();

        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) {
                break;
            }

            buffer.append(line);
            sideSize++;
        }

        String state = buffer.toString();
        if (state.isEmpty()) {
            throw new IllegalArgumentException("InputStream is empty");
        } else {
            return FifteenState.parseField(state);
        }
    }

    /**
     * Read start state from File Stream, defining dimension
     * of the field use first string of file.
     *
     * @return byte[] which describe start state or throw IllegalArg,
     *         if can't read start state.
     * @throws IOException
     */
    private static byte[] readStartStateFromFile() throws IOException {
        InputStreamReader strm = new InputStreamReader(
                //create file with help and read it
                MainFifteen.class.getResourceAsStream("/" + fileName), StandardCharsets.UTF_8);

        BufferedReader reader = new BufferedReader(strm);

        String line;
        sideSize = 0;
        StringBuffer buffer = new StringBuffer();

        while ((line = reader.readLine()) != null) {
            if (line.isEmpty()) {
                break;
            }

            if (line.length() == 1) {
                try {
                    sideSize = Integer.parseInt(line);
                    continue;

                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Wrong file format");
                }
            }

            buffer.append(line);
            buffer.append("\n");
        }

        String state = buffer.toString();
        if (state.isEmpty()) {
            reader.close();
            throw new IllegalArgumentException("File is empty");
        } else {
            reader.close();
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

    private static byte[] getSnailTerminalState(int sideSize) {
        if (sideSize == 2) {
            return new byte[] {1, 2, 0, 3};
        } else if (sideSize == 3) {
            return new byte[] {1, 2, 3, 8, 0 ,4, 7, 6, 5};
        } else if (sideSize == 4) {
            return new byte[] {1, 2, 3, 4, 12, 13, 14, 5, 11, 0, 15, 6, 10, 9, 8, 7};
        } else if (sideSize == 5) {
            return new byte[] {1, 2, 3, 4, 5, 16, 17, 18, 19, 6, 15, 24, 0, 20, 7, 14, 23, 22, 21, 8, 13, 12, 11, 10, 9};
        } else {
            throw new IllegalArgumentException("I can't solve snail more then 5... Sorry");
        }
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

            if (args[i].equals("-i")) {
                isReadInputStream = true;
                continue;
            }
            
            if (args[i].equals("-f")) {
                isReadFileStream = true;
                fileName = args[++i];
                if (!fileName.endsWith(".txt")) {
                    throw new IllegalArgumentException("invalid file name");
                }
                continue;
            }

            if (args[i].equals("-ordinary")) {
                isOrdinarySolve = true;
                continue;
            }

            if (args[i].equals("-P")) {
                isBigPenalty = true;
                continue;
            }

            if (args[i].equals("-E")) {
                isBigPenalty = false;
                isEasyHeruistick = true;
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
                isReadInputStream = false;
                isReadFileStream = false;
                sideSize = Integer.parseInt(args[++i]);
                continue;
            }

            if (args[i].equals("-c")) {
                isReadInputStream = false;
                isReadFileStream = false;
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
