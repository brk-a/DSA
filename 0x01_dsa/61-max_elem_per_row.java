import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Max Element Per Row.
 *
 * Implementations:
 *
 * 1. Stream-Based
 *      Uses Arrays.stream(row).max().getAsInt()
 *
 * 2. Manual Loop (Oracle)
 *      Classic nested for-loop
 *
 * The manual-loop implementation is retained as a correctness oracle
 * for deterministic and randomised testing.
 */
public class MaxElementPerRowTestHarness {

    /* **********************************************************************
     * Validation
     * **********************************************************************/

    static boolean validMatrix(int[][] matrix) {
        return matrix != null && matrix.length > 0;
    }

    static boolean rectangularOrJagged(int[][] matrix) {
        if (matrix == null) {
            return false;
        }
        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i] == null || matrix[i].length == 0) {
                return false;
            }
        }
        return true;
    }

    /* **********************************************************************
     * Algorithms
     * **********************************************************************/

    /**
     * Stream-based implementation.
     *
     * Returns null if matrix is invalid or contains an empty row.
     */
    static int[] maxElementPerRowStream(int[][] matrix) {

        if (!validMatrix(matrix) || !rectangularOrJagged(matrix)) {
            return null;
        }

        int n = matrix.length;
        int[] result = new int[n];

        for (int i = 0; i < n; i++) {

            int maxVal = Arrays.stream(matrix[i]).max().getAsInt();
            result[i] = maxVal;
        }

        return result;
    }

    /**
     * Manual loop implementation (Oracle).
     *
     * Returns null if matrix is invalid or contains an empty row.
     */
    static int[] maxElementPerRowManual(int[][] matrix) {

        if (!validMatrix(matrix) || !rectangularOrJagged(matrix)) {
            return null;
        }

        int n = matrix.length;
        int[] result = new int[n];

        for (int i = 0; i < n; i++) {

            int maxVal = matrix[i][0];

            for (int j = 1; j < matrix[i].length; j++) {
                if (matrix[i][j] > maxVal) {
                    maxVal = matrix[i][j];
                }
            }

            result[i] = maxVal;
        }

        return result;
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

    static String formatArray(int[] arr) {
        if (arr == null) {
            return "null";
        }
        return Arrays.toString(arr);
    }

    static boolean arraysEqual(int[] a, int[] b) {
        return Arrays.equals(a, b);
    }

    static int[][] randomMatrix(
            Random rng,
            int maxRows,
            int maxCols,
            int minValue,
            int maxValue) {

        int rows = 1 + rng.nextInt(maxRows);
        int[][] matrix = new int[rows][];

        for (int i = 0; i < rows; i++) {

            int cols = 1 + rng.nextInt(maxCols);
            matrix[i] = new int[cols];

            for (int j = 0; j < cols; j++) {
                matrix[i][j] = minValue + rng.nextInt(maxValue - minValue + 1);
            }
        }

        return matrix;
    }

    /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    static class TestCase {

        final String id;
        final int[][] matrix;
        final int[] expected;
        final String description;

        TestCase(
                String id,
                int[][] matrix,
                int[] expected,
                String description) {

            this.id = id;
            this.matrix = matrix;
            this.expected = expected;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {
        int[] solve(int[][] matrix);
    }

    static class MethodCase {

        final String name;
        final Algorithm algorithm;

        MethodCase(
                String name,
                Algorithm algorithm) {

            this.name = name;
            this.algorithm = algorithm;
        }
    }

    static void runTests(
            String algorithm,
            Algorithm method,
            List<TestCase> tests) {

        System.out.println("======================================================");
        System.out.println(algorithm);
        System.out.println("======================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            try {

                int[] actual = method.solve(cloneMatrix(test.matrix));

                if (arraysEqual(actual, test.expected)) {

                    passed++;
                    System.out.printf("✓ %s (%s)%n", test.id, test.description);

                } else {

                    failed++;
                    System.out.printf("✗ %s (%s)%n", test.id, test.description);
                    System.out.printf("  matrix    = %s%n", formatMatrix(test.matrix));
                    System.out.printf("  expected  = %s%n", formatArray(test.expected));
                    System.out.printf("  actual    = %s%n", formatArray(actual));
                }

            } catch (Exception ex) {

                failed++;
                System.out.printf("✗ %s (%s)%n", test.id, test.description);
                System.out.printf("  matrix    = %s%n", formatMatrix(test.matrix));
                System.out.printf("  exception = %s%n", ex);
            }
        }

        System.out.println();
        System.out.printf("Results: %d passed, %d failed, %d total%n", passed, failed, tests.size());
        System.out.println();
    }

    /* **********************************************************************
     * Randomised Testing
     * **********************************************************************/

    static void runRandomisedTests(int iterations) {

        System.out.println("======================================================");
        System.out.println("Randomised Cross Checks");
        System.out.println("======================================================");

        Random rng = new Random(112233445L);

        for (int i = 1; i <= iterations; i++) {

            int[][] matrix = randomMatrix(rng, 6, 6, -10, 10);

            int[] a = maxElementPerRowManual(cloneMatrix(matrix));
            int[] b = maxElementPerRowStream(cloneMatrix(matrix));

            if (!arraysEqual(a, b)) {

                System.out.println("Randomised test FAILED");
                System.out.println("matrix = " + formatMatrix(matrix));
                System.out.println("manual = " + formatArray(a));
                System.out.println("stream = " + formatArray(b));
                return;
            }
        }

        System.out.printf("All %d randomised tests passed.%n%n", iterations);
    }

    /* **********************************************************************
     * Main
     * **********************************************************************/

    public static void main(String[] args) {

        List<TestCase> tests = new ArrayList<>();

        /*
         * ============================================================
         * Standard Examples
         * ============================================================
         */

        tests.add(new TestCase(
                "S1",
                new int[][]{
                        {1, 2, 3},
                        {4, 5, 6},
                        {7, 8, 9}
                },
                new int[]{3, 6, 9},
                "Simple 3x3 matrix"));

        tests.add(new TestCase(
                "S2",
                new int[][]{
                        {1, 2},
                        {3, 4},
                        {5, 6}
                },
                new int[]{2, 4, 6},
                "3x2 matrix"));

        /*
         * ============================================================
         * Single Row / Single Column
         * ============================================================
         */

        tests.add(new TestCase(
                "A1",
                new int[][]{
                        {5, -1, 3}
                },
                new int[]{5},
                "Single row"));

        tests.add(new TestCase(
                "A2",
                new int[][]{
                        {5},
                        {10},
                        {3}
                },
                new int[]{5, 10, 3},
                "Single column"));

        /*
         * ============================================================
         * Negative Values
         * ============================================================
         */

        tests.add(new TestCase(
                "N1",
                new int[][]{
                        {-1, -2, -3},
                        {-4, 0, -6}
                },
                new int[]{-1, 0},
                "Negative values with zero"));

        tests.add(new TestCase(
                "N2",
                new int[][]{
                        {-5, -1},
                        {-10, -3}
                },
                new int[]{-1, -3},
                "All negative values"));

        /*
         * ============================================================
         * Edge Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "E1",
                null,
                null,
                "Null matrix"));

        tests.add(new TestCase(
                "E2",
                new int[][]{},
                null,
                "Empty matrix"));

        tests.add(new TestCase(
                "E3",
                new int[][]{{}},
                null,
                "Empty row"));

        tests.add(new TestCase(
                "E4",
                new int[][]{
                        null,
                        {1, 2}
                },
                null,
                "Null row inside matrix"));

        System.out.println("############################################################");
        System.out.println("######## MAX ELEMENT PER ROW ################################");
        System.out.println("############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "Manual Loop (Oracle)",
                        MaxElementPerRowTestHarness::maxElementPerRowManual),

                new MethodCase(
                        "Stream-Based",
                        MaxElementPerRowTestHarness::maxElementPerRowStream)
        );

        for (MethodCase method : methods) {
            runTests(method.name, method.algorithm, tests);
        }

        runRandomisedTests(5000);
    }
}
