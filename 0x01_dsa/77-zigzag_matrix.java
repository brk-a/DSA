import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ZigzagMatrix {

    private ZigzagMatrix() {
        // Utility class; do not instantiate.
    }

    /**
     * Returns true only for a non-null, non-empty rectangular matrix.
     */
    static boolean isValidMatrix(int[][] matrix) {
        if (matrix == null || matrix.length == 0) {
            return false;
        }

        if (matrix[0] == null || matrix[0].length == 0) {
            return false;
        }

        int columns = matrix[0].length;

        for (int[] row : matrix) {
            if (row == null || row.length != columns) {
                return false;
            }
        }

        return true;
    }

    /**
     * Traverses the matrix diagonally in zigzag order.
     *
     * Example:
     *
     * 1 2 3
     * 4 5 6
     * 7 8 9
     *
     * Result:
     * 1, 2, 4, 7, 5, 3, 6, 8, 9
     */
    static List<Integer> zigzagByDiagonal(int[][] matrix) {
        List<Integer> result = new ArrayList<>();

        if (!isValidMatrix(matrix)) {
            return result;
        }

        int rows = matrix.length;
        int columns = matrix[0].length;

        for (int diagonal = 0; diagonal < rows + columns - 1; diagonal++) {
            int rowStart = Math.max(0, diagonal - columns + 1);
            int rowEnd = Math.min(rows - 1, diagonal);

            if (diagonal % 2 == 0) {
                // Traverse upward-right.
                for (int row = rowEnd; row >= rowStart; row--) {
                    int column = diagonal - row;
                    result.add(matrix[row][column]);
                }
            } else {
                // Traverse downward-left.
                for (int row = rowStart; row <= rowEnd; row++) {
                    int column = diagonal - row;
                    result.add(matrix[row][column]);
                }
            }
        }

        return result;
    }

    /**
     * Traverses the matrix by identifying the starting point of each diagonal.
     *
     * Diagonals start on:
     * 1. The top row, from left to right.
     * 2. The left column, from the second row onward.
     */
    static List<Integer> zigzagByStartingPoint(int[][] matrix) {
        List<Integer> result = new ArrayList<>();

        if (!isValidMatrix(matrix)) {
            return result;
        }

        int rows = matrix.length;
        int columns = matrix[0].length;
        int diagonalNumber = 0;

        // Diagonals beginning on the top row.
        for (int startColumn = 0; startColumn < columns; startColumn++) {
            appendDiagonal(
                    matrix,
                    0,
                    startColumn,
                    diagonalNumber % 2 == 0,
                    result
            );
            diagonalNumber++;
        }

        // Diagonals beginning on the left column.
        // Start at row 1 because the top-left diagonal was already processed.
        for (int startRow = 1; startRow < rows; startRow++) {
            appendDiagonal(
                    matrix,
                    startRow,
                    0,
                    diagonalNumber % 2 == 0,
                    result
            );
            diagonalNumber++;
        }

        return result;
    }

    /**
     * Appends one diagonal to result.
     *
     * If upwardRight is true, the diagonal is traversed upward-right.
     * Otherwise, it is traversed downward-left.
     */
    private static void appendDiagonal(
            int[][] matrix,
            int startRow,
            int startColumn,
            boolean upwardRight,
            List<Integer> result
    ) {
        int rows = matrix.length;
        int columns = matrix[0].length;

        if (upwardRight) {
            int row = startRow;
            int column = startColumn;

            while (row >= 0 && column < columns) {
                result.add(matrix[row][column]);
                row--;
                column++;
            }
        } else {
            int row = startRow;
            int column = startColumn;

            while (row < rows && column >= 0) {
                result.add(matrix[row][column]);
                row++;
                column--;
            }
        }
    }

    /**
     * Independent oracle that computes each cell's diagonal number and then
     * reads the diagonals in the required direction.
     */
    static List<Integer> zigzagBruteForce(int[][] matrix) {
        List<Integer> result = new ArrayList<>();

        if (!isValidMatrix(matrix)) {
            return result;
        }

        int rows = matrix.length;
        int columns = matrix[0].length;

        for (int diagonal = 0; diagonal < rows + columns - 1; diagonal++) {
            List<Integer> diagonalValues = new ArrayList<>();

            for (int row = 0; row < rows; row++) {
                int column = diagonal - row;

                if (column >= 0 && column < columns) {
                    diagonalValues.add(matrix[row][column]);
                }
            }

            if (diagonal % 2 == 0) {
                for (int i = diagonalValues.size() - 1; i >= 0; i--) {
                    result.add(diagonalValues.get(i));
                }
            } else {
                result.addAll(diagonalValues);
            }
        }

        return result;
    }

    @FunctionalInterface
    interface Algorithm {
        List<Integer> solve(int[][] matrix);
    }

    static class TestCase {
        final String id;
        final int[][] input;
        final List<Integer> expected;
        final String description;

        TestCase(
                String id,
                int[][] input,
                List<Integer> expected,
                String description
        ) {
            this.id = id;
            this.input = input;
            this.expected = expected;
            this.description = description;
        }
    }

    static class MethodCase {
        final String name;
        final Algorithm algorithm;

        MethodCase(String name, Algorithm algorithm) {
            this.name = name;
            this.algorithm = algorithm;
        }
    }

    static int[][] cloneMatrix(int[][] matrix) {
        if (matrix == null) {
            return null;
        }

        int[][] copy = new int[matrix.length][];

        for (int i = 0; i < matrix.length; i++) {
            copy[i] = matrix[i] == null ? null : matrix[i].clone();
        }

        return copy;
    }

    static String formatMatrix(int[][] matrix) {
        if (matrix == null) {
            return "null";
        }

        return Arrays.deepToString(matrix);
    }

    static void runTests(
            String algorithmName,
            Algorithm algorithm,
            List<TestCase> tests
    ) {
        System.out.println("======================================================");
        System.out.println(algorithmName);
        System.out.println("======================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {
            try {
                List<Integer> actual =
                        algorithm.solve(cloneMatrix(test.input));

                if (test.expected.equals(actual)) {
                    passed++;
                    System.out.printf(
                            "PASS %s (%s)%n",
                            test.id,
                            test.description
                    );
                } else {
                    failed++;
                    System.out.printf(
                            "FAIL %s (%s)%n",
                            test.id,
                            test.description
                    );
                    System.out.println("  input    = " + formatMatrix(test.input));
                    System.out.println("  expected = " + test.expected);
                    System.out.println("  actual   = " + actual);
                }
            } catch (Exception exception) {
                failed++;
                System.out.printf(
                        "FAIL %s (%s)%n",
                        test.id,
                        test.description
                );
                System.out.println("  input     = " + formatMatrix(test.input));
                System.out.println("  exception = " + exception);
            }
        }

        System.out.printf(
                "Results: %d passed, %d failed, %d total%n%n",
                passed,
                failed,
                tests.size()
        );

        if (failed > 0) {
            throw new AssertionError(
                    algorithmName + " had " + failed + " failed test(s)"
            );
        }
    }

    static int[][] randomMatrix(
            Random random,
            int maxRows,
            int maxColumns,
            int minimumValue,
            int maximumValue
    ) {
        int rows = random.nextInt(maxRows) + 1;
        int columns = random.nextInt(maxColumns) + 1;

        int[][] matrix = new int[rows][columns];

        for (int row = 0; row < rows; row++) {
            for (int column = 0; column < columns; column++) {
                matrix[row][column] =
                        minimumValue
                                + random.nextInt(
                                        maximumValue - minimumValue + 1
                                );
            }
        }

        return matrix;
    }

    static void runRandomisedTests(int iterations) {
        System.out.println("======================================================");
        System.out.println("Randomised Cross-Checks");
        System.out.println("======================================================");

        Random random = new Random(987654321L);

        for (int testNumber = 1; testNumber <= iterations; testNumber++) {
            int[][] matrix = randomMatrix(random, 10, 10, -100, 100);

            List<Integer> expected =
                    zigzagBruteForce(cloneMatrix(matrix));

            List<Integer> diagonalResult =
                    zigzagByDiagonal(cloneMatrix(matrix));

            List<Integer> startingPointResult =
                    zigzagByStartingPoint(cloneMatrix(matrix));

            if (!expected.equals(diagonalResult)
                    || !expected.equals(startingPointResult)) {
                System.out.println("Randomised test FAILED");
                System.out.println("matrix          = " + formatMatrix(matrix));
                System.out.println("expected        = " + expected);
                System.out.println("by diagonal     = " + diagonalResult);
                System.out.println("by start point  = " + startingPointResult);

                throw new AssertionError(
                        "Randomised cross-check failed at test "
                                + testNumber
                );
            }
        }

        System.out.printf(
                "All %d Randomised tests passed.%n%n",
                iterations
        );
    }

    static List<TestCase> createTests() {
        List<TestCase> tests = new ArrayList<>();

        int[][] threeByThree = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        tests.add(new TestCase(
                "S1",
                threeByThree,
                Arrays.asList(1, 2, 4, 7, 5, 3, 6, 8, 9),
                "3x3 square matrix"
        ));

        tests.add(new TestCase(
                "S2",
                new int[][]{
                        {1, 2, 3, 4},
                        {5, 6, 7, 8},
                        {9, 10, 11, 12},
                        {13, 14, 15, 16}
                },
                Arrays.asList(
                        1, 2, 5, 9, 6, 3,
                        4, 7, 10, 13, 14, 11,
                        8, 12, 15, 16
                ),
                "4x4 square matrix"
        ));

        tests.add(new TestCase(
                "S3",
                new int[][]{{10}},
                Arrays.asList(10),
                "1x1 matrix"
        ));

        tests.add(new TestCase(
                "W1",
                new int[][]{
                        {1, 2, 3, 4},
                        {5, 6, 7, 8}
                },
                Arrays.asList(1, 2, 5, 6, 3, 4, 7, 8),
                "2x4 wide matrix"
        ));

        tests.add(new TestCase(
                "W2",
                new int[][]{
                        {1, 2, 3},
                        {4, 5, 6}
                },
                Arrays.asList(1, 2, 4, 5, 3, 6),
                "2x3 wide matrix"
        ));

        tests.add(new TestCase(
                "T1",
                new int[][]{
                        {1, 2},
                        {3, 4},
                        {5, 6}
                },
                Arrays.asList(1, 2, 3, 5, 4, 6),
                "3x2 tall matrix"
        ));

        tests.add(new TestCase(
                "T2",
                new int[][]{
                        {1, 2, 3},
                        {4, 5, 6},
                        {7, 8, 9},
                        {10, 11, 12}
                },
                Arrays.asList(
                        1, 2, 4, 7, 5, 3,
                        6, 8, 10, 11, 9, 12
                ),
                "4x3 tall matrix"
        ));

        tests.add(new TestCase(
                "M1",
                new int[][]{
                        {-1, -2, -3},
                        {-4, -5, -6},
                        {-7, -8, -9}
                },
                Arrays.asList(
                        -1, -2, -4, -7, -5,
                        -3, -6, -8, -9
                ),
                "Matrix containing negative values"
        ));

        tests.add(new TestCase(
                "M2",
                new int[][]{
                        {0, 0, 0},
                        {0, 0, 0},
                        {0, 0, 0}
                },
                Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0, 0),
                "Matrix containing zeros"
        ));

        tests.add(new TestCase(
                "E1",
                null,
                List.of(),
                "Null matrix"
        ));

        tests.add(new TestCase(
                "E2",
                new int[][]{},
                List.of(),
                "Matrix with zero rows"
        ));

        tests.add(new TestCase(
                "E3",
                new int[][]{{}, {}, {}},
                List.of(),
                "Matrix with zero columns"
        ));

        tests.add(new TestCase(
                "E4",
                new int[][]{
                        {1, 2},
                        null
                },
                List.of(),
                "Matrix containing a null row"
        ));

        tests.add(new TestCase(
                "E5",
                new int[][]{
                        {1, 2},
                        {3}
                },
                List.of(),
                "Jagged matrix"
        ));

        return tests;
    }

    public static void main(String[] args) {
        System.out.println("############################################################");
        System.out.println("################ ZIGZAG MATRIX TRAVERSAL ###################");
        System.out.println("############################################################");
        System.out.println();

        List<TestCase> tests = createTests();

        List<MethodCase> methods = List.of(
                new MethodCase(
                        "Brute Force Oracle",
                        ZigzagMatrix::zigzagBruteForce
                ),
                new MethodCase(
                        "Diagonal-Based Implementation",
                        ZigzagMatrix::zigzagByDiagonal
                ),
                new MethodCase(
                        "Starting-Point Implementation",
                        ZigzagMatrix::zigzagByStartingPoint
                )
        );

        for (MethodCase method : methods) {
            runTests(method.name, method.algorithm, tests);
        }

        runRandomisedTests(5_000);

        System.out.println("All tests passed.");
    }
}
