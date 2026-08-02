import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Count Sorted Rows in a Matrix.
 *
 * Problem:
 * Given an m x n integer matrix, count the number of rows that are:
 *
 * - strictly increasing (each element < next element), or
 * - strictly decreasing (each element > next element).
 *
 * A row with a single element is considered sorted, but must be counted only once.
 *
 * Implementations:
 *
 * 1. Brute Force (Oracle)
 *      Explicitly checks each row for strictly increasing / decreasing.
 *
 * 2. Two-pass Linear Scan (O(m*n))
 *      - Left-to-right pass to count strictly increasing rows.
 *      - Right-to-left pass to count strictly decreasing rows (only if n > 1).
 *
 * The brute-force and two-pass implementations are logically equivalent;
 * the brute-force version is retained as an explicit oracle for testing.
 */
public class CountSortedRowsTestHarness {

    /* **********************************************************************
     * Validation Helpers
     * **********************************************************************/

    static boolean validMatrix(int[][] matrix) {
        return matrix != null
                && matrix.length > 0
                && matrix[0] != null
                && matrix[0].length > 0;
    }

    /* **********************************************************************
     * Algorithm Implementations
     * **********************************************************************/

    /**
     * Brute Force Oracle.
     *
     * For each row:
     * - Check if strictly increasing.
     * - Check if strictly decreasing.
     * Count rows where either is true.
     *
     * Time: O(m*n)
     */
    static int countSortedRowsBruteForce(int[][] matrix) {

        if (!validMatrix(matrix)) {
            return -1;
        }

        int rows = matrix.length;
        int cols = matrix[0].length;
        int result = 0;

        for (int i = 0; i < rows; i++) {

            boolean increasing = true;
            for (int j = 0; j < cols - 1; j++) {
                if (matrix[i][j + 1] <= matrix[i][j]) {
                    increasing = false;
                    break;
                }
            }

            boolean decreasing = true;
            for (int j = 0; j < cols - 1; j++) {
                if (matrix[i][j + 1] >= matrix[i][j]) {
                    decreasing = false;
                    break;
                }
            }

            if (increasing || decreasing) {
                result++;
            }
        }

        return result;
    }

    /**
     * Two-pass Linear Scan.
     *
     * 1. Left-to-right pass: count strictly increasing rows.
     * 2. Right-to-left pass: count strictly decreasing rows (only if cols > 1).
     *
     * Time: O(m*n)
     */
    static int countSortedRowsTwoPass(int[][] matrix) {

        if (!validMatrix(matrix)) {
            return -1;
        }

        int rows = matrix.length;
        int cols = matrix[0].length;
        int result = 0;

        // Strictly increasing rows
        for (int i = 0; i < rows; i++) {

            int j;
            for (j = 0; j < cols - 1; j++) {
                if (matrix[i][j + 1] <= matrix[i][j]) {
                    break;
                }
            }

            if (j == cols - 1) {
                result++;
            }
        }

        // Strictly decreasing rows (only if more than one column)
        if (cols > 1) {

            for (int i = 0; i < rows; i++) {

                int j;
                for (j = cols - 1; j > 0; j--) {
                    if (matrix[i][j - 1] <= matrix[i][j]) {
                        break;
                    }
                }

                if (j == 0) {
                    result++;
                }
            }
        }

        return result;
    }

    /* **********************************************************************
     * Test Harness Types
     * **********************************************************************/

    static class TestCase {

        final String id;
        final int[][] input;
        final Integer expected;
        final String description;

        TestCase(
                String id,
                int[][] input,
                Integer expected,
                String description) {

            this.id = id;
            this.input = input;
            this.expected = expected;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {

        int solve(int[][] matrix);
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

    /* **********************************************************************
     * Utilities
     * **********************************************************************/

    static int[][] cloneMatrix(int[][] matrix) {

        if (matrix == null) {
            return null;
        }

        int rows = matrix.length;
        int[][] copy = new int[rows][];

        for (int i = 0; i < rows; i++) {
            if (matrix[i] == null) {
                copy[i] = null;
            } else {
                copy[i] = matrix[i].clone();
            }
        }

        return copy;
    }

    static String formatMatrix(int[][] matrix) {

        if (matrix == null) {
            return "null";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < matrix.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(Arrays.toString(matrix[i]));
        }
        sb.append("]");
        return sb.toString();
    }

    static boolean intsEqual(Integer a, Integer b) {

        if (a == null && b == null) {
            return true;
        }

        if (a == null || b == null) {
            return false;
        }

        return a.intValue() == b.intValue();
    }

    static void runTests(
            String algorithmName,
            Algorithm method,
            List<TestCase> tests) {

        System.out.println(
                "======================================================");
        System.out.println(algorithmName);
        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            try {

                int actualValue = method.solve(
                        cloneMatrix(test.input));

                Integer actual = (test.input == null || !validMatrix(test.input))
                        ? (test.expected == null ? null : actualValue)
                        : actualValue;

                if (intsEqual(actual, test.expected)) {

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
                            "  input     = %s%n",
                            formatMatrix(test.input));

                    System.out.printf(
                            "  expected  = %s%n",
                            test.expected);

                    System.out.printf(
                            "  actual    = %s%n",
                            actual);
                }

            } catch (Exception ex) {

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
                        ex);
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
            Random rng,
            int maxRows,
            int maxCols,
            int minValue,
            int maxValue) {

        int rows = rng.nextInt(maxRows + 1);

        if (rows == 0) {
            return new int[0][];
        }

        int cols = rng.nextInt(maxCols + 1);

        if (cols == 0) {
            int[][] m = new int[rows][];
            for (int i = 0; i < rows; i++) {
                m[i] = new int[0];
            }
            return m;
        }

        int[][] matrix = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = minValue + rng.nextInt(maxValue - minValue + 1);
            }
        }

        return matrix;
    }

    static void runRandomisedTests(int iterations) {

        System.out.println(
                "======================================================");
        System.out.println(
                "Randomised Cross Checks (Brute vs Two-Pass)");
        System.out.println(
                "======================================================");

        Random rng = new Random(123456789L);

        for (int i = 1; i <= iterations; i++) {

            int[][] matrix = randomMatrix(
                    rng,
                    6,
                    6,
                    -10,
                    10);

            if (!validMatrix(matrix)) {
                continue;
            }

            int brute =
                    countSortedRowsBruteForce(
                            cloneMatrix(matrix));

            int twoPass =
                    countSortedRowsTwoPass(
                            cloneMatrix(matrix));

            if (brute != twoPass) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "matrix   = " + formatMatrix(matrix));

                System.out.println(
                        "brute    = " + brute);

                System.out.println(
                        "twoPass  = " + twoPass);

                return;
            }
        }

        System.out.printf(
                "All %d Randomised tests passed.%n%n",
                iterations);
    }

    /* **********************************************************************
     * Main
     * **********************************************************************/

    public static void main(String[] args) {

        List<TestCase> tests = new ArrayList<>();

        /*
         * ============================================================
         * Example from GFG
         * ============================================================
         */

        tests.add(new TestCase(
                "G1",
                new int[][]{
                        {1, 2, 3, 4, 5},
                        {4, 3, 1, 2, 6},
                        {8, 7, 6, 5, 4},
                        {5, 7, 8, 9, 10}
                },
                3,
                "GFG example: 3 sorted rows"));

        /*
         * ============================================================
         * Small / Simple Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "A1",
                new int[][]{
                        {1, 2, 3},
                        {3, 2, 1},
                        {1, 1, 1}
                },
                2,
                "Two sorted rows: increasing and decreasing"));

        tests.add(new TestCase(
                "A2",
                new int[][]{
                        {1},
                        {2},
                        {3}
                },
                3,
                "Single-column rows: all counted once"));

        tests.add(new TestCase(
                "A3",
                new int[][]{
                        {1, 2},
                        {2, 1},
                        {1, 1}
                },
                2,
                "Two-column rows: one increasing, one decreasing"));

        tests.add(new TestCase(
                "A4",
                new int[][]{
                        {1, 2, 3},
                        {1, 3, 2},
                        {3, 2, 1}
                },
                2,
                "Three rows: first increasing, last decreasing"));

        /*
         * ============================================================
         * All Increasing / All Decreasing
         * ============================================================
         */

        tests.add(new TestCase(
                "I1",
                new int[][]{
                        {1, 2, 3, 4},
                        {2, 3, 4, 5},
                        {0, 1, 2, 3}
                },
                3,
                "All rows strictly increasing"));

        tests.add(new TestCase(
                "D1",
                new int[][]{
                        {4, 3, 2, 1},
                        {5, 4, 3, 2},
                        {3, 2, 1, 0}
                },
                3,
                "All rows strictly decreasing"));

        /*
         * ============================================================
         * Mixed / Random Patterns
         * ============================================================
         */

        tests.add(new TestCase(
                "M1",
                new int[][]{
                        {1, 2, 2, 3},
                        {3, 2, 2, 1},
                        {1, 2, 3, 4}
                },
                1,
                "Only one strictly increasing row (others have equal neighbours)"));

        tests.add(new TestCase(
                "M2",
                new int[][]{
                        {5, 4, 3, 2},
                        {2, 3, 4, 5},
                        {1, 1, 1, 1},
                        {10, 9, 8, 7}
                },
                3,
                "Two decreasing, one increasing; flat row ignored"));

        /*
         * ============================================================
         * Edge Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "E1",
                null,
                -1,
                "Null matrix"));

        tests.add(new TestCase(
                "E2",
                new int[][]{},
                -1,
                "Empty matrix (zero rows)"));

        tests.add(new TestCase(
                "E3",
                new int[][]{
                        {},
                        {},
                        {}
                },
                -1,
                "Matrix with zero columns"));

        System.out.println(
                "############################################################");
        System.out.println(
                "############# COUNT SORTED ROWS IN MATRIX  #################");
        System.out.println(
                "############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "Brute Force (Oracle)",
                        CountSortedRowsTestHarness::countSortedRowsBruteForce),

                new MethodCase(
                        "Two-pass Linear Scan O(m*n)",
                        CountSortedRowsTestHarness::countSortedRowsTwoPass)
        );

        for (MethodCase method : methods) {

            runTests(
                    method.name,
                    method.algorithm,
                    tests);
        }

        runRandomisedTests(5000);
    }
}