import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Print Matrix in Snake Pattern.
 *
 * Problem:
 * Given an m x n integer matrix, return a list of all elements visited
 * in snake (zigzag) pattern:
 *
 * - Row 0: left to right
 * - Row 1: right to left
 * - Row 2: left to right
 * - and so on, alternating direction for each row.
 *
 * Example:
 * Input:
 *   1 2 3
 *   4 5 6
 *   7 8 9
 *
 * Output:
 *   [1, 2, 3, 6, 5, 4, 7, 8, 9]
 *
 * Implementations:
 *
 * 1. Brute Force (Oracle)
 *      Explicitly traverses each row, choosing direction based on row index.
 *
 * 2. Refactored Snake Pattern
 *      Same logical approach, cleaner loop bounds and helpers.
 *
 * The brute-force implementation is retained as a correctness oracle
 * for deterministic and randomised testing.
 */
public class PrintMatrixSnakePatternTestHarness {

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
     * - If row index is even, traverse left to right.
     * - If row index is odd, traverse right to left.
     *
     * Time: O(m * n)
     */
    static ArrayList<Integer> printMatrixSnakePatternBruteForce(int[][] matrix) {

        if (!validMatrix(matrix)) {
            return null;
        }

        int m = matrix.length;
        int n = matrix[0].length;

        ArrayList<Integer> result = new ArrayList<>();

        for (int i = 0; i < m; i++) {

            if (i % 2 == 0) {
                // Even row: left to right
                for (int j = 0; j < n; j++) {
                    result.add(matrix[i][j]);
                }
            } else {
                // Odd row: right to left
                for (int j = n - 1; j >= 0; j--) {
                    result.add(matrix[i][j]);
                }
            }
        }

        return result;
    }

    /**
     * Refactored Snake Pattern.
     *
     * Same logic as brute force, but written in a cleaner, more maintainable way.
     *
     * Time: O(m * n)
     */
    static ArrayList<Integer> printMatrixSnakePattern(int[][] matrix) {

        if (!validMatrix(matrix)) {
            return null;
        }

        int m = matrix.length;
        int n = matrix[0].length;

        ArrayList<Integer> result = new ArrayList<>();

        for (int i = 0; i < m; i++) {

            if (i % 2 == 0) {
                // Even row: left to right
                for (int j = 0; j < n; j++) {
                    result.add(matrix[i][j]);
                }
            } else {
                // Odd row: right to left
                for (int j = n - 1; j >= 0; j--) {
                    result.add(matrix[i][j]);
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
        final ArrayList<Integer> expected;
        final String description;

        TestCase(
                String id,
                int[][] input,
                ArrayList<Integer> expected,
                String description) {

            this.id = id;
            this.input = input;
            this.expected = expected;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {

        ArrayList<Integer> solve(int[][] matrix);
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

    static String formatList(ArrayList<Integer> list) {

        if (list == null) {
            return "null";
        }

        return list.toString();
    }

    static boolean listsEqual(ArrayList<Integer> a, ArrayList<Integer> b) {

        if (a == null && b == null) {
            return true;
        }

        if (a == null || b == null) {
            return false;
        }

        return a.equals(b);
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

                ArrayList<Integer> actual =
                        method.solve(cloneMatrix(test.input));

                if (listsEqual(actual, test.expected)) {

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
                            formatList(test.expected));

                    System.out.printf(
                            "  actual    = %s%n",
                            formatList(actual));
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
                "Randomised Cross Checks (Brute vs Refactored)");
        System.out.println(
                "======================================================");

        Random rng = new Random(123456789L);

        for (int i = 1; i <= iterations; i++) {

            int[][] matrix = randomMatrix(
                    rng,
                    6,
                    6,
                    -10,
                    20);

            if (!validMatrix(matrix)) {
                continue;
            }

            ArrayList<Integer> brute =
                    printMatrixSnakePatternBruteForce(
                            cloneMatrix(matrix));

            ArrayList<Integer> refactored =
                    printMatrixSnakePattern(
                            cloneMatrix(matrix));

            if (!listsEqual(brute, refactored)) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "matrix      = " + formatMatrix(matrix));

                System.out.println(
                        "brute       = " + formatList(brute));

                System.out.println(
                        "refactored  = " + formatList(refactored));

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
         * Classic 3x3 Example
         * ============================================================
         */

        tests.add(new TestCase(
                "C1",
                new int[][]{
                        {1, 2, 3},
                        {4, 5, 6},
                        {7, 8, 9}
                },
                new ArrayList<>(Arrays.asList(1, 2, 3, 6, 5, 4, 7, 8, 9)),
                "Classic 3x3 snake pattern"));

        /*
         * ============================================================
         * Rectangular Matrices
         * ============================================================
         */

        tests.add(new TestCase(
                "R1",
                new int[][]{
                        {1, 2, 3, 4},
                        {5, 6, 7, 8},
                        {9, 10, 11, 12}
                },
                new ArrayList<>(Arrays.asList(
                        1, 2, 3, 4,
                        8, 7, 6, 5,
                        9, 10, 11, 12)),
                "3x4 rectangular matrix"));

        tests.add(new TestCase(
                "R2",
                new int[][]{
                        {1, 2},
                        {3, 4},
                        {5, 6},
                        {7, 8}
                },
                new ArrayList<>(Arrays.asList(
                        1, 2,
                        4, 3,
                        5, 6,
                        8, 7)),
                "4x2 rectangular matrix"));

        /*
         * ============================================================
         * Single Row / Single Column
         * ============================================================
         */

        tests.add(new TestCase(
                "S1",
                new int[][]{
                        {1, 2, 3, 4, 5}
                },
                new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5)),
                "Single row"));

        tests.add(new TestCase(
                "S2",
                new int[][]{
                        {1},
                        {2},
                        {3},
                        {4}
                },
                new ArrayList<>(Arrays.asList(1, 2, 3, 4)),
                "Single column"));

        /*
         * ============================================================
         * Small Matrices
         * ============================================================
         */

        tests.add(new TestCase(
                "A1",
                new int[][]{
                        {1, 2},
                        {3, 4}
                },
                new ArrayList<>(Arrays.asList(1, 2, 4, 3)),
                "2x2 matrix"));

        tests.add(new TestCase(
                "A2",
                new int[][]{
                        {1, 2, 3},
                        {4, 5, 6}
                },
                new ArrayList<>(Arrays.asList(1, 2, 3, 6, 5, 4)),
                "2x3 matrix"));

        tests.add(new TestCase(
                "A3",
                new int[][]{
                        {1, 2},
                        {3, 4},
                        {5, 6}
                },
                new ArrayList<>(Arrays.asList(1, 2, 4, 3, 5, 6)),
                "3x2 matrix"));

        /*
         * ============================================================
         * Negative / Mixed Values
         * ============================================================
         */

        tests.add(new TestCase(
                "M1",
                new int[][]{
                        {-1, -2, -3},
                        {4, 5, 6}
                },
                new ArrayList<>(Arrays.asList(-1, -2, -3, 6, 5, 4)),
                "Negative and positive values"));

        tests.add(new TestCase(
                "M2",
                new int[][]{
                        {0, 0, 0},
                        {0, 0, 0},
                        {0, 0, 0}
                },
                new ArrayList<>(Arrays.asList(
                        0, 0, 0,
                        0, 0, 0,
                        0, 0, 0)),
                "All zeros"));

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
                "Empty matrix (zero rows)"));

        tests.add(new TestCase(
                "E3",
                new int[][]{
                        {},
                        {},
                        {}
                },
                null,
                "Matrix with zero columns"));

        System.out.println(
                "############################################################");
        System.out.println(
                "############## PRINT MATRIX IN SNAKE PATTERN ###############");
        System.out.println(
                "############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "Brute Force (Oracle)",
                        PrintMatrixSnakePatternTestHarness::printMatrixSnakePatternBruteForce),

                new MethodCase(
                        "Refactored Snake Pattern O(m*n)",
                        PrintMatrixSnakePatternTestHarness::printMatrixSnakePattern)
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
