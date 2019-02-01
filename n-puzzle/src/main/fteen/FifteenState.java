package main.fteen;

import main.algorithm.states.State;

import java.util.Arrays;

public class FifteenState extends State implements Comparable<State>{

    private byte[] field;
    private int sideSize;
    private int hash;

    /**
     * Create description of field state
     *
     * @param parent - previous state
     * @param sideSize - size of the field side
     */
    public FifteenState(State parent, int sideSize) {
        super(parent);
        this.sideSize = sideSize;
    }

    public static byte[] parseField(String string) {
        int i = 0;

        String[] lines = string.split("\n");

        byte[] res = new byte[lines.length * lines.length];

        for (String line : lines) {
            String[] values = line.trim().replaceAll("\\s+", ":").split(":");
            for (String value : values) {
                res[i] = Byte.parseByte(value.trim());
                i++;
            }
        }

        return res;
    }

    /**
     * This method check - do we can terminate this game
     *
     * @param field - state of field
     * @return true - if we can terminate this game
     *
     */
    public static boolean checkState(byte[] field, int sideSize) {
        int N = 0;
        int e = 0;

        for (int i = 0; i < field.length; i++) {
            // Check empty field number
            if (field[i] == 0) {
                e = i / sideSize + 1;
            }
            if (i == 0) {
                continue;
            }

            // Check cell quantity which smaller then current
            for (int j = i + 1; j < field.length; j++) {
                if (field[j] < field[i]) {
                    N++;
                }
            }
        }
        //If N is even, there is no solution
        N += e;

        return (N & 1) == 0; //First bit of even number is equal 0
    }


    public static boolean checkState(byte[] field, int sideSize, byte type) {

        int inversionCount = 0;

        for (int i = 0; i < field.length; i++) {

            if (field[i] == 0) {
                continue;
            }

            for (int j = 0; j < i; j++) {
                if (field[j] > field[i]) {
                    inversionCount++;
                }
            }
        }

        int e = 0;
        int counter = 0;
        for (int j = field.length - 1; j >= 0; j--) {
            if (field[j] == 0) {
                e = counter / sideSize + 1;
                break;
            }
            counter++;
        }

        boolean isInversionEven = inversionCount % 2 == 0;

        //Solved first state
        if (sideSize % 2 != 0 && isInversionEven) {
            return true;
        } else {
            if (e % 2 == 0 && !isInversionEven) {
                return true;
            } else if (e % 2 == 1 && isInversionEven) {
                return true;
            }
        }

        if (sideSize % 2 != 0) {
            if (type == MainFifteen.SOLUTION_TYPE_SNAIL) {
                isInversionEven = !isInversionEven;
            }
            return isInversionEven;
        }


        return false;
    }

    // Return field state as array of bytes
    public byte[] getField() {
        return field;
    }

    //Set field state
    public void setField(byte[] field) {
        this.field = field;
        hash = Arrays.hashCode(field);
    }

    public int getSideSize() {
        return sideSize;
    }


    @Override
    public String toString() {
        if (field == null) {
            return "null";
        }
        StringBuilder stringBuilder = new StringBuilder(field.length);

        for (int i = 0; i < sideSize; i++) {
            for (int j = 0; j < sideSize; j++) {
                stringBuilder.append(field[j + i * sideSize]);
                stringBuilder.append("\t");
            }
            stringBuilder.append("\n");
        }

        return stringBuilder.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof FifteenState)) {
            return false;
        }

        return hash == o.hashCode();
    }

    @Override
    public int hashCode() {
        return hash;
    }

    @Override
    public int compareTo(State state) {
        return state.getF();
    }
}
