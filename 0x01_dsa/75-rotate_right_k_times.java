import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


/**
 * Rotate every row of a matrix to the right by k positions.
 *
 * Example:
 *
 * Input:
 * {
 *     {1, 2, 3, 4},
 *     {5, 6, 7, 8}
 * }
 *
 * k = 2
 *
 * Output:
 * {
 *     {3, 4, 1, 2},
 *     {7, 8, 5, 6}
 * }
 *
 * The matrix is treated as m independent rows, each of length n.
 */
public class RotateMatrixRightKTimes {

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

    /**
     * Converts any k into the range [0, n - 1].
     *
     * This also handles negative k safely.
     *
     * For example, with n = 5:
     * - k = 7  becomes 2
     * - k = -1 becomes 4
     */
    private static int normaliseK(int k, int n) {
        int normalised = k % n;

        if (normalised < 0) {
            normalised += n;
        }

        return normalised;
    }

    /* **********************************************************************
     * Implementation 1: Auxiliary Matrix
     * **********************************************************************/

    /**
     * Creates and returns a new matrix.
     *
     * For each original element matrix[i][j], its destination is:
     *
     *     result[i][(j + k) % n]
     *
     * Time: O(m * n)
     * Extra space: O(m * n)
     */
    static int[][] rotateMatrixRightKTimesAuxMatrix(
            int[][] matrix,
            int k) {

        if (!validMatrix(matrix)) {
            return null;
        }

        int m = matrix.length;
        int n = matrix[0].length;

        int normalisedK = normaliseK(k, n);
        int[][] result = new int[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int destinationColumn =
                        (j + normalisedK) % n;

                result[i][destinationColumn] = matrix[i][j];
            }
        }

        return result;
    }

    /* **********************************************************************
     * Implementation 2: Temporary Row
     * **********************************************************************/

    /**
     * Creates and returns a new matrix by rotating each row.
     *
     * This is a direct correction of the approach in the original code.
     *
     * Time: O(m * n)
     * Extra space: O(m * n) for the result, plus O(n) temporary space.
     */
    static int[][] rotateMatrixRightKTimesTemporaryRow(
            int[][] matrix,
            int k) {

        if (!validMatrix(matrix)) {
            return null;
        }

        int m = matrix.length;
        int n = matrix[0].length;

        int normalisedK = normaliseK(k, n);
        int[][] result = cloneMatrix(matrix);

        if (normalisedK == 0) {
            return result;
        }

        for (int i = 0; i < m; i++) {
            int[] temporary = new int[normalisedK];

            /*
             * Save the final k elements of the row.
             */
            for (int j = 0; j < normalisedK; j++) {
                temporary[j] =
                        result[i][n - normalisedK + j];
            }

            /*
             * Shift the remaining elements right by k.
             */
            for (int j = n - normalisedK - 1; j >= 0; j--) {
                result[i][j + normalisedK] =
                        result[i][j];
            }

            /*
             * Put the saved final elements at the beginning.
             */
            for (int j = 0; j < normalisedK; j++) {
                result[i][j] = temporary[j];
            }
        }

        return result;
    }

    /* **********************************************************************
     * Implementation 3: In Place
     * **********************************************************************/

    /**
     * Rotates every row in place using the reversal algorithm.
     *
     * To rotate one row right by k:
     *
     * 1. Reverse the entire row.
     * 2. Reverse the first k elements.
     * 3. Reverse the remaining n - k elements.
     *
     * Time: O(m * n)
     * Extra space: O(1)
     *
     * This method returns the same matrix reference supplied by the caller.
     */
    static int[][] rotateMatrixRightKTimesInPlace(
            int[][] matrix,
            int k) {

        if (!validMatrix(matrix)) {
            return null;
        }

        int m = matrix.length;
        int n = matrix[0].length;

        int normalisedK = normaliseK(k, n);

        if (normalisedK == 0) {
            return matrix;
        }

        for (int i = 0; i < m; i++) {
            reverse(matrix[i], 0, n - 1);
            reverse(matrix[i], 0, normalisedK - 1);
            reverse(matrix[i], normalisedK, n - 1);
        }

        return matrix;
    }

    /**
     * Reverses values in array[left..right], inclusive.
     */
    private static void reverse(
            int[] array,
            int left,
            int right) {

        while (left < right) {
            int temporary = array[left];
            array[left] = array[right];
            array[right] = temporary;

            left++;
            right--;
        }
    }

    /* **********************************************************************
     * Optional Compatibility Method
     * **********************************************************************/

    /**
     * Keeps the same method name as your original code.
     *
     * This implementation returns a new matrix rather than modifying the
     * caller's matrix.
     */
    static int[][] rotateMatrixRightKTimes(
            int[][] matrix,
            int k) {

        return rotateMatrixRightKTimesAuxMatrix(matrix, k);
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
            copy[i] =
                    matrix[i] == null
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

    static boolean matricesEqual(
            int[][] first,
            int[][] second) {

        return Arrays.deepEquals(first, second);
    }

    /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    @FunctionalInterface
    interface Algorithm {
        int[][] solve(int[][] matrix, int k);
    }

    static class TestCase {
        final String id;
        final int[][] input;
        final int k;
        final int[][] expected;
        final String description;
        final boolean expectSameReference;

        TestCase(
                String id,
                int[][] input,
                int k,
                int[][] expected,
                String description,
                boolean expectSameReference) {

            this.id = id;
            this.input = input;
            this.k = k;
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
                int[][] actual =
                        method.algorithm.solve(input, test.k);

                boolean valuesMatch =
                        matricesEqual(actual, test.expected);

                boolean referenceMatches =
                        !test.expectSameReference
                                || actual == input;

                if (valuesMatch && referenceMatches) {
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
                            "  k          = %d%n",
                            test.k);

                    System.out.printf(
                            "  input      = %s%n",
                            formatMatrix(test.input));

                    System.out.printf(
                            "  expected   = %s%n",
                            formatMatrix(test.expected));

                    System.out.printf(
                            "  actual     = %s%n",
                            formatMatrix(actual));

                    if (test.expectSameReference) {
                        System.out.printf(
                                "  same ref   = %s%n",
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
                        "  exception  = %s%n",
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
     * Randomised Cross-Checking
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
                                + random.nextInt(
                                        maxValue - minValue + 1);
            }
        }

        return matrix;
    }

    static void runRandomisedTests(
            List<MethodCase> methods,
            int iterations) {

        System.out.println("======================================================");
        System.out.println("Randomised Cross-Checks");
        System.out.println("======================================================");

        Random random = new Random(987654321L);

        for (int iteration = 0;
             iteration < iterations;
             iteration++) {

            int[][] original =
                    randomMatrix(
                            random,
                            8,
                            8,
                            -100,
                            100);

            int k =
                    random.nextInt(101) - 50;

            int[][] expected =
                    rotateMatrixRightKTimesAuxMatrix(
                            cloneMatrix(original),
                            k);

            for (MethodCase method : methods) {
                int[][] input = cloneMatrix(original);

                int[][] actual =
                        method.algorithm.solve(input, k);

                if (!matricesEqual(actual, expected)) {
                    System.out.println(
                            "Randomised test FAILED");

                    System.out.println(
                            "method   = " + method.name);

                    System.out.println(
                            "k        = " + k);

                    System.out.println(
                            "input    = " + formatMatrix(original));

                    System.out.println(
                            "expected = " + formatMatrix(expected));

                    System.out.println(
                            "actual   = " + formatMatrix(actual));

                    return;
                }

                if (method.mutatesInput && actual != input) {
                    System.out.println(
                            "Identity test FAILED");

                    System.out.println(
                            "method did not return the supplied matrix: "
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
                        {1, 2, 3, 4},
                        {5, 6, 7, 8}
                },
                2,
                new int[][]{
                        {3, 4, 1, 2},
                        {7, 8, 5, 6}
                },
                "2x4 matrix rotated by 2",
                false));

        tests.add(new TestCase(
                "S2",
                new int[][]{
                        {1, 2, 3, 4, 5}
                },
                1,
                new int[][]{
                        {5, 1, 2, 3, 4}
                },
                "Single row rotated by 1",
                false));

        tests.add(new TestCase(
                "S3",
                new int[][]{
                        {1},
                        {2},
                        {3}
                },
                10,
                new int[][]{
                        {1},
                        {2},
                        {3}
                },
                "Single-column matrix",
                false));

        tests.add(new TestCase(
                "S4",
                new int[][]{
                        {1, 2, 3, 4, 5},
                        {6, 7, 8, 9, 10},
                        {11, 12, 13, 14, 15}
                },
                7,
                new int[][]{
                        {4, 5, 1, 2, 3},
                        {9, 10, 6, 7, 8},
                        {14, 15, 11, 12, 13}
                },
                "k larger than the column count",
                false));

        tests.add(new TestCase(
                "S5",
                new int[][]{
                        {1, 2, 3, 4},
                        {5, 6, 7, 8}
                },
                0,
                new int[][]{
                        {1, 2, 3, 4},
                        {5, 6, 7, 8}
                },
                "Zero rotations",
                false));

        tests.add(new TestCase(
                "S6",
                new int[][]{
                        {1, 2, 3, 4},
                        {5, 6, 7, 8}
                },
                4,
                new int[][]{
                        {1, 2, 3, 4},
                        {5, 6, 7, 8}
                },
                "Rotation by exactly the row length",
                false));

        tests.add(new TestCase(
                "S7",
                new int[][]{
                        {1, 2, 3, 4, 5}
                },
                -1,
                new int[][]{
                        {2, 3, 4, 5, 1}
                },
                "Negative k interpreted as a left rotation",
                false));

        tests.add(new TestCase(
                "E1",
                null,
                2,
                null,
                "Null matrix",
                false));

        tests.add(new TestCase(
                "E2",
                new int[][]{},
                2,
                null,
                "Empty matrix",
                false));

        tests.add(new TestCase(
                "E3",
                new int[][]{
                        {},
                        {}
                },
                2,
                null,
                "Matrix with empty rows",
                false));

        tests.add(new TestCase(
                "E4",
                new int[][]{
                        {1, 2, 3},
                        {4, 5}
                },
                2,
                null,
                "Jagged matrix",
                false));

        tests.add(new TestCase(
                "E5",
                new int[][]{
                        {1, 2},
                        null
                },
                2,
                null,
                "Matrix containing a null row",
                false));

        List<MethodCase> methods = List.of(
                new MethodCase(
                        "Auxiliary Matrix",
                        RotateMatrixRightKTimes
                                ::rotateMatrixRightKTimesAuxMatrix,
                        false),

                new MethodCase(
                        "Temporary Row",
                        RotateMatrixRightKTimes
                                ::rotateMatrixRightKTimesTemporaryRow,
                        false),

                new MethodCase(
                        "In Place",
                        RotateMatrixRightKTimes
                                ::rotateMatrixRightKTimesInPlace,
                        true)
        );

        System.out.println("############################################################");
        System.out.println("######## ROTATE MATRIX ROWS RIGHT BY K ######################");
        System.out.println("############################################################");
        System.out.println();

        for (MethodCase method : methods) {
            List<TestCase> methodTests = new ArrayList<>();

            for (TestCase test : tests) {
                methodTests.add(new TestCase(
                        test.id,
                        test.input,
                        test.k,
                        test.expected,
                        test.description,
                        method.mutatesInput));
            }

            runTests(method, methodTests);
        }

        runRandomisedTests(methods, 5000);
    }
}
