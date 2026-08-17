import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


/**
 * Rotate a rectangular integer matrix by 180 degrees.
 *
 * Implementations:
 *
 * 1. rotateMatrix180DegreesNinetyTwice
 *      Performs two clockwise 90-degree rotations.
 *      Time: O(m * n)
 *      Extra space: O(m * n)
 *
 * 2. rotateMatrix180DegreesAuxMatrix
 *      Directly writes each value into its rotated position.
 *      Time: O(m * n)
 *      Extra space: O(m * n)
 *
 * 3. rotateMatrix180DegreesInPlace
 *      Rotates the supplied matrix in place.
 *      Time: O(m * n)
 *      Extra space: O(1)
 *
 * A matrix is considered valid only when:
 * - It is not null.
 * - It contains at least one row.
 * - Every row is non-null.
 * - Every row has the same positive length.
 */
public class RotateMatrix180DegreesTestHarness {

    /* **********************************************************************
     * Validation
     * **********************************************************************/

    static boolean validMatrix(int[][] matrix) {
        if (matrix == null || matrix.length == 0) {
            return false;
        }

        if (matrix[0] == null || matrix[0].length == 0) {
            return false;
        }

        int columnCount = matrix[0].length;

        for (int i = 1; i < matrix.length; i++) {
            if (matrix[i] == null || matrix[i].length != columnCount) {
                return false;
            }
        }

        return true;
    }

    /* **********************************************************************
     * Rotation Implementations
     * **********************************************************************/

    /**
     * Rotates the matrix by applying two clockwise 90-degree rotations.
     *
     * For an m x n matrix:
     * - First rotation produces an n x m matrix.
     * - Second rotation produces an m x n matrix.
     */
    static int[][] rotateMatrix180DegreesNinetyTwice(int[][] matrix) {
        if (!validMatrix(matrix)) {
            return null;
        }

        return rotate90Clockwise(rotate90Clockwise(matrix));
    }

    /**
     * Helper that creates a new matrix rotated 90 degrees clockwise.
     *
     * Element mapping:
     *
     *     source[i][j] -> result[j][m - 1 - i]
     */
    private static int[][] rotate90Clockwise(int[][] matrix) {
        int m = matrix.length;
        int n = matrix[0].length;

        int[][] result = new int[n][m];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                result[j][m - 1 - i] = matrix[i][j];
            }
        }

        return result;
    }

    /**
     * Rotates the matrix by writing every value directly into its
     * 180-degree destination.
     *
     * Element mapping:
     *
     *     source[i][j] -> result[m - 1 - i][n - 1 - j]
     */
    static int[][] rotateMatrix180DegreesAuxMatrix(int[][] matrix) {
        if (!validMatrix(matrix)) {
            return null;
        }

        int m = matrix.length;
        int n = matrix[0].length;

        int[][] result = new int[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                result[m - 1 - i][n - 1 - j] = matrix[i][j];
            }
        }

        return result;
    }

    /**
     * Rotates the supplied matrix in place.
     *
     * Each element is swapped with its 180-degree counterpart.
     *
     * For an odd number of rows, the middle row is reversed separately.
     *
     * This method returns the same matrix reference that it receives.
     */
    static int[][] rotateMatrix180DegreesInPlace(int[][] matrix) {
        if (!validMatrix(matrix)) {
            return null;
        }

        int m = matrix.length;
        int n = matrix[0].length;

        /*
         * Swap corresponding elements in the top and bottom rows.
         *
         * Every pair is processed exactly once because only the top half
         * of the rows is visited.
         */
        for (int i = 0; i < m / 2; i++) {
            int oppositeRow = m - 1 - i;

            for (int j = 0; j < n; j++) {
                int oppositeColumn = n - 1 - j;

                int temporary = matrix[i][j];
                matrix[i][j] = matrix[oppositeRow][oppositeColumn];
                matrix[oppositeRow][oppositeColumn] = temporary;
            }
        }

        /*
         * If the number of rows is odd, the middle row has no opposite row.
         * Reverse that row to complete the 180-degree rotation.
         */
        if (m % 2 != 0) {
            int middleRow = m / 2;

            for (int j = 0; j < n / 2; j++) {
                int oppositeColumn = n - 1 - j;

                int temporary = matrix[middleRow][j];
                matrix[middleRow][j] = matrix[middleRow][oppositeColumn];
                matrix[middleRow][oppositeColumn] = temporary;
            }
        }

        return matrix;
    }

    /* **********************************************************************
     * Test Harness Types
     * **********************************************************************/

    @FunctionalInterface
    interface Algorithm {
        int[][] solve(int[][] matrix);
    }

    static class TestCase {
        final String id;
        final int[][] input;
        final int[][] expected;
        final String description;
        final boolean expectSameReference;

        TestCase(
                String id,
                int[][] input,
                int[][] expected,
                String description,
                boolean expectSameReference) {

            this.id = id;
            this.input = input;
            this.expected = expected;
            this.description = description;
            this.expectSameReference = expectSameReference;
        }
    }

    static class MethodCase {
        final String name;
        final Algorithm algorithm;
        final boolean mutatesInput;

        MethodCase(
                String name,
                Algorithm algorithm,
                boolean mutatesInput) {

            this.name = name;
            this.algorithm = algorithm;
            this.mutatesInput = mutatesInput;
        }
    }

    /* **********************************************************************
     * Utilities
     * **********************************************************************/

    static int[][] cloneMatrix(int[][] matrix) {
        if (matrix == null) {
            return null;
        }

        int[][] copy = new int[matrix.length][];

        for (int i = 0; i < matrix.length; i++) {
            copy[i] = matrix[i] == null
                    ? null
                    : matrix[i].clone();
        }

        return copy;
    }

    static String formatMatrix(int[][] matrix) {
        if (matrix == null) {
            return "null";
        }

        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < matrix.length; i++) {
            if (i > 0) {
                result.append(", ");
            }

            result.append(
                    matrix[i] == null
                            ? "null"
                            : Arrays.toString(matrix[i]));
        }

        result.append("]");
        return result.toString();
    }

    static boolean matricesEqual(int[][] first, int[][] second) {
        return Arrays.deepEquals(first, second);
    }

    static void assertCondition(
            boolean condition,
            String message) {

        if (!condition) {
            throw new AssertionError(message);
        }
    }

    /* **********************************************************************
     * Deterministic Tests
     * **********************************************************************/

    static void runTests(
            MethodCase method,
            List<TestCase> tests) {

        System.out.println("======================================================");
        System.out.println(method.name);
        System.out.println("======================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {
            int[][] input = cloneMatrix(test.input);

            try {
                int[][] actual = method.algorithm.solve(input);

                boolean valuesMatch = matricesEqual(actual, test.expected);

                boolean referenceMatches =
                        !test.expectSameReference
                                || actual == input;

                boolean passedTest =
                        valuesMatch && referenceMatches;

                if (passedTest) {
                    passed++;
                    System.out.printf(
                            "✓ %s (%s)%n",
                            test.id,
                            test.description);
                } else {
                    failed++;

                    System.out.printf(
                            "✗ %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.printf(
                            "  input before = %s%n",
                            formatMatrix(test.input));

                    System.out.printf(
                            "  expected     = %s%n",
                            formatMatrix(test.expected));

                    System.out.printf(
                            "  actual       = %s%n",
                            formatMatrix(actual));

                    if (test.expectSameReference) {
                        System.out.printf(
                                "  same ref     = %s%n",
                                actual == input);
                    }
                }

            } catch (Exception exception) {
                failed++;

                System.out.printf(
                        "✗ %s (%s)%n",
                        test.id,
                        test.description);

                System.out.printf(
                        "  input     = %s%n",
                        formatMatrix(test.input));

                System.out.printf(
                        "  exception = %s%n",
                        exception);
            }
        }

        System.out.println();
        System.out.printf(
                "Results: %d passed, %d failed, %d total%n",
                passed,
                failed,
                tests.size());

        System.out.println();
    }

    /* **********************************************************************
     * Randomised Testing
     * **********************************************************************/

    static int[][] randomMatrix(
            Random random,
            int maxRows,
            int maxColumns,
            int minValue,
            int maxValue) {

        int rows = random.nextInt(maxRows) + 1;
        int columns = random.nextInt(maxColumns) + 1;

        int[][] matrix = new int[rows][columns];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                matrix[i][j] =
                        minValue
                                + random.nextInt(maxValue - minValue + 1);
            }
        }

        return matrix;
    }

    static void runRandomisedTests(
            List<MethodCase> methods,
            int iterations) {

        System.out.println("======================================================");
        System.out.println("Randomised Cross Checks");
        System.out.println("======================================================");

        Random random = new Random(987654321L);

        for (int iteration = 1; iteration <= iterations; iteration++) {
            int[][] original = randomMatrix(
                    random,
                    8,
                    8,
                    -100,
                    100);

            int[][] expected =
                    rotateMatrix180DegreesAuxMatrix(
                            cloneMatrix(original));

            for (MethodCase method : methods) {
                int[][] input = cloneMatrix(original);
                int[][] actual = method.algorithm.solve(input);

                if (!matricesEqual(actual, expected)) {
                    System.out.println("Randomised test FAILED");
                    System.out.println("method   = " + method.name);
                    System.out.println("input    = " + formatMatrix(original));
                    System.out.println("expected = " + formatMatrix(expected));
                    System.out.println("actual   = " + formatMatrix(actual));
                    return;
                }

                if (method.mutatesInput && actual != input) {
                    System.out.println("Randomised identity test FAILED");
                    System.out.println("method did not return the supplied matrix: "
                            + method.name);
                    return;
                }
            }
        }

        System.out.printf(
                "All %d randomised tests passed.%n%n",
                iterations);
    }

    /* **********************************************************************
     * Main
     * **********************************************************************/

    public static void main(String[] args) {
        List<TestCase> tests = new ArrayList<>();

        tests.add(new TestCase(
                "S1",
                new int[][]{
                        {1, 2, 3},
                        {4, 5, 6},
                        {7, 8, 9}
                },
                new int[][]{
                        {9, 8, 7},
                        {6, 5, 4},
                        {3, 2, 1}
                },
                "3x3 square matrix",
                false));

        tests.add(new TestCase(
                "S2",
                new int[][]{
                        {1, 2, 3, 4},
                        {5, 6, 7, 8},
                        {9, 10, 11, 12},
                        {13, 14, 15, 16}
                },
                new int[][]{
                        {16, 15, 14, 13},
                        {12, 11, 10, 9},
                        {8, 7, 6, 5},
                        {4, 3, 2, 1}
                },
                "4x4 square matrix",
                false));

        tests.add(new TestCase(
                "R1",
                new int[][]{
                        {1, 2, 3, 4},
                        {5, 6, 7, 8}
                },
                new int[][]{
                        {8, 7, 6, 5},
                        {4, 3, 2, 1}
                },
                "2x4 rectangular matrix",
                false));

        tests.add(new TestCase(
                "R2",
                new int[][]{
                        {1, 2},
                        {3, 4},
                        {5, 6}
                },
                new int[][]{
                        {6, 5},
                        {4, 3},
                        {2, 1}
                },
                "3x2 rectangular matrix",
                false));

        tests.add(new TestCase(
                "R3",
                new int[][]{
                        {1, 2, 3, 4, 5}
                },
                new int[][]{
                        {5, 4, 3, 2, 1}
                },
                "1x5 matrix",
                false));

        tests.add(new TestCase(
                "R4",
                new int[][]{
                        {1},
                        {2},
                        {3},
                        {4}
                },
                new int[][]{
                        {4},
                        {3},
                        {2},
                        {1}
                },
                "4x1 matrix",
                false));

        tests.add(new TestCase(
                "E1",
                null,
                null,
                "Null matrix",
                false));

        tests.add(new TestCase(
                "E2",
                new int[][]{},
                null,
                "Empty matrix",
                false));

        tests.add(new TestCase(
                "E3",
                new int[][]{
                        {},
                        {}
                },
                null,
                "Matrix with zero columns",
                false));

        tests.add(new TestCase(
                "E4",
                new int[][]{
                        {1, 2, 3},
                        {4, 5}
                },
                null,
                "Jagged matrix",
                false));

        tests.add(new TestCase(
                "E5",
                new int[][]{
                        {1, 2},
                        null
                },
                null,
                "Matrix containing a null row",
                false));

        System.out.println("############################################################");
        System.out.println("######## ROTATE MATRIX 180 DEGREES #########################");
        System.out.println("############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(
                new MethodCase(
                        "Two 90-Degree Rotations",
                        RotateMatrix180DegreesTestHarness
                                ::rotateMatrix180DegreesNinetyTwice,
                        false),

                new MethodCase(
                        "Auxiliary Matrix",
                        RotateMatrix180DegreesTestHarness
                                ::rotateMatrix180DegreesAuxMatrix,
                        false),

                new MethodCase(
                        "In Place",
                        RotateMatrix180DegreesTestHarness
                                ::rotateMatrix180DegreesInPlace,
                        true)
        );

        /*
         * The in-place method must return the same input reference.
         * Update only the in-place test cases accordingly.
         */
        List<TestCase> inPlaceTests = new ArrayList<>();

        for (TestCase test : tests) {
            inPlaceTests.add(new TestCase(
                    test.id,
                    test.input,
                    test.expected,
                    test.description,
                    true));
        }

        for (MethodCase method : methods) {
            runTests(
                    method,
                    method.mutatesInput
                            ? inPlaceTests
                            : tests);
        }

        runRandomisedTests(methods, 5000);
    }
}
