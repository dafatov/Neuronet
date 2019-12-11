package ru.demetrious;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class FuzzyLogic {
    final static String REGEX = "^~?[A-Z]([*@])~?[A-Z]$";
    private HashMap<Character, double[][]> fuzzySets;
    private ArrayList<Character> resultQueue;
    private int letterIterator = 0;

    /**
     * Constructor Fuzzy logic
     *
     * @param fuzzySetStructs The set of rows of the matrices
     */
    public FuzzyLogic(FuzzySetStruct... fuzzySetStructs) {
        init();
        for (FuzzySetStruct fuzzySetStruct : fuzzySetStructs) {
            fuzzySets.put(fuzzySetStruct.letter, new double[][]{fuzzySetStruct.fuzzySet});
        }
    }

    /**
     * Constructor Fuzzy logic
     *
     * @param fuzzyMatrixStructs The set of matrices
     */
    public FuzzyLogic(FuzzyMatrixStruct... fuzzyMatrixStructs) {
        init();
        for (FuzzyMatrixStruct fuzzyMatrixStruct : fuzzyMatrixStructs) {
            fuzzySets.put(fuzzyMatrixStruct.letter, fuzzyMatrixStruct.fuzzySetStructs);
        }
    }

    /**
     * Initialization of arrays
     */
    private void init() {
        fuzzySets = new HashMap<>();
        resultQueue = new ArrayList<>();
    }

    /**
     * Execution of the specified task for the current algorithm
     * The source and target data are specified here
     *
     * @return StringBuilder containing sources and results of the algorithm
     */
    public StringBuilder launch() {
        StringBuilder stringBuilder = new StringBuilder();

        solve("A*B", "~A*C", "D@E");

        for (Character character : resultQueue) {
            stringBuilder.append("Matrix ").append(character).append(":\n");
            stringBuilder.append(new FuzzyMatrixStruct(fuzzySets.get(character)).toString()).append("\n\n");
        }
        return stringBuilder;
    }

    /**
     * Finding a solution of rules set
     *
     * @param rules A set of rules
     */
    public void solve(String... rules) {
        for (String rule : rules) {
            parse(rule);
        }
    }

    /**
     * Parse each rule
     *
     * @param rule entered rule in "^~?[A-Z]([*@])~?[A-Z]$" regex format
     */
    private void parse(String rule) {
        if (!rule.matches(REGEX)) {
            try {
                throw new Exception("Error in rule syntax: pattern: " + REGEX);
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1000326);
            }
        }
        char[] symbols = rule.toCharArray();
        if (symbols.length == 3) {
            execute(symbols[1], fuzzySets.get(symbols[0]), fuzzySets.get(symbols[2]));
        } else {
            if (symbols.length == 4) {
                if (symbols[0] == '~') {
                    execute(symbols[2], reverse(fuzzySets.get(symbols[1])), fuzzySets.get(symbols[3]));
                } else {
                    execute(symbols[1], fuzzySets.get(symbols[0]), reverse(fuzzySets.get(symbols[3])));
                }
            } else {
                execute(symbols[2], reverse(fuzzySets.get(symbols[1])), reverse(fuzzySets.get(symbols[4])));
            }
        }
    }

    /**
     * Reverse matrix; each element x = 1 - x
     *
     * @param matrix Matrix
     * @return Reverse matrix
     */
    private double[][] reverse(double[][] matrix) {
        double[][] result = new double[matrix.length][matrix[0].length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                result[i][j] = 1 - matrix[i][j];
            }
        }
        return result;
    }

    /**
     * Execute operation on two operands
     *
     * @param operation Operations are:
     *                  '*' = multiply
     *                  '@' = composition
     * @param first     First operand
     * @param second    Second operand
     */
    private void execute(char operation, double[][] first, double[][] second) {
        if (first == null || second == null) {
            try {
                throw new Exception("Wrong variable in rules");
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(1000328);
            }
        }
        switch (operation) {
            case '*':
                multiply(first, second);
                break;
            case '@':
                composition(first, second);
                break;
            default:
                try {
                    throw new Exception("Unknown operation in rule");
                } catch (Exception e) {
                    e.printStackTrace();
                    System.exit(1000327);
                }
        }
    }

    /**
     * Multiply two matrices
     *
     * @param x First operand
     * @param y Second operand
     * @return Result
     */
    private double[][] multiply(double[][] x, double[][] y) {
        return composition(transpose(x), y);
    }

    /**
     * Transpose matrix
     *
     * @param matrix Operand
     * @return Result
     */
    private double[][] transpose(double[][] matrix) {
        double[][] result = new double[matrix[0].length][matrix.length];

        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                result[j][i] = matrix[i][j];
            }
        }
        return result;
    }

    /**
     * Composition two matrices
     *
     * @param x First operand
     * @param y Second operand
     * @return Result
     */
    private double[][] composition(double[][] x, double[][] y) {
        double[][] result = new double[x.length][y[0].length];

        for (int i = 0; i < x.length; i++) {
            for (int j = 0; j < y[0].length; j++) {
                for (int k = 0; k < y.length; k++) {
                    result[i][j] = Math.max(result[i][j], Math.min(x[i][k], y[k][j]));
                }
            }
        }
        char letter = getLetter();
        fuzzySets.put(letter, result);
        resultQueue.add(letter);
        return result;
    }

    /**
     * Looks for a free letter for names
     *
     * @return Returns the found letter
     */
    private char getLetter() {
        while (fuzzySets.containsKey(Main.ALPHABET[letterIterator])) {
            letterIterator++;
        }
        return Main.ALPHABET[letterIterator];
    }

    /**
     * Structure of named single row
     */
    public static class FuzzySetStruct {
        private char letter;
        private double[] fuzzySet;

        public FuzzySetStruct(char letter, double... fuzzySet) {
            this.letter = letter;
            this.fuzzySet = fuzzySet;
        }
    }

    /**
     * Structure of named matrix
     */
    public static class FuzzyMatrixStruct {
        private char letter;
        private double[][] fuzzySetStructs;

        public FuzzyMatrixStruct(char letter, double[][] fuzzyMatrix) {
            this.letter = letter;
            this.fuzzySetStructs = fuzzyMatrix;
        }

        public FuzzyMatrixStruct(double[][] fuzzySetStructs) {
            this.fuzzySetStructs = fuzzySetStructs;
        }

        /**
         * Convert object to string
         *
         * @return Matrix as a table
         */
        @Override
        public String toString() {
            StringBuilder stringBuilder = new StringBuilder();

            stringBuilder.append("[");
            for (int i = 0; i < fuzzySetStructs.length; i++) {
                stringBuilder.append(Arrays.toString(fuzzySetStructs[i]));
                stringBuilder.append(i == fuzzySetStructs.length - 1 ? "]" : "\n");
            }
            return stringBuilder.toString();
        }
    }
}
