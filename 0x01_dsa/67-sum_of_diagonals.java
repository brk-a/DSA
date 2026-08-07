import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Sum of Diagonals in a Matrix.
 *
 * Problem:
 * Given an m x n integer matrix, compute:
 *
 * - Principal (main) diagonal sum:
 *     sum of matrix[i][i] for i from 0 to min(m, n) - 1.
 *
 * - Secondary (anti) diagonal sum:
 *     sum of matrix[i][n - 1 - i] for i from 0 to min(m, n) - 1,
 *     where the column index is valid.
 *
 * Notes:
 * - For non-square matrices, diagonals are defined over min(m, n) elements.
 * - For odd-sized square matrices, the center element belongs to both
 *   diagonals and is included in both sums.
 * - If the matrix is invalid (null / empty), Result.valid == false.
 *
 * Implementations:
 *
 * 1. Brute Force (Oracle)
 *      Double loop over all cells, checking i == j and i + j == n - 1.
 *
 * 2. Refactored Single Loop
 *      Single loop over i from 0 to min(m, n) - 1.
 *
 * The brute-force implementation is retained as a correctness oracle
 * for deterministic and randomised testing.
 */
public class SumOfDiagonalsMatrixTestHarness {

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
     * Result Record
     * **********************************************************************/

    static record Result(int principal, int secondary, boolean valid) {}

    /* **********************************************************************
     * Algorithm Implementations
     * **********************************************************************/

    /**
     * Brute Force Oracle.
     *
     * Double loop over all cells, checking:
     * - i == j for principal diagonal
     * - i + j == n - 1 for secondary diagonal
     *
     * Time: O(m * n)
     */
    static Result sumOfDiagonalsBruteForce(int[][] matrix) {

        if (!validMatrix(matrix)) {
            return new Result(0, 0, false);
        }

        int m = matrix.length;
        int n = matrix[0].length;

        int principal = 0;
        int secondary = 0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {

                if (i == j) {
                    principal += matrix[i][j];
                }

                if ((i + j) == (n - 1)) {
                    secondary += matrix[i][j];
                }
            }
        }

        return new Result(principal, secondary, true);
    }

    /**
     * Refactored Single Loop.
     *
     * Loops i from 0 to min(m, n) - 1 and directly accesses:
     * - (i, i) for principal
     * - (i, n - 1 - i) for secondary
     *
     * Time: O(min(m, n))
     */
    static Result sumOfDiagonalsMatrix(int[][] matrix) {

        if (!validMatrix(matrix)) {
            return new Result(0, 0, false);
        }

        int m = matrix.length;
        int n = matrix[0].length;
        int limit = Math.min(m, n);

        int principal = 0;
        int secondary = 0;

        for (int i = 0; i < limit; i++) {

            // Principal diagonal: (i, i)
            principal += matrix[i][i];

            // Secondary diagonal: (i, n - 1 - i)
            int j = n - 1 - i;
            if (j >= 0 && j < n) {
                secondary += matrix[i][j];
            }
        }

        return new Result(principal, secondary, true);
    }

    /* **********************************************************************
     * Test Harness Types
     * **********************************************************************/

    static class TestCase {

        final String id;
        final int[][] input;
        final Result expected;
        final String description;

        TestCase(
                String id,
                int[][] input,
                Result expected,
                String description) {

            this.id = id;
            this.input = input;
            this.expected = expected;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {

        Result solve(int[][] matrix);
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

    static boolean resultsEqual(Result a, Result b) {

        if (a == null && b == null) {
            return true;
        }

        if (a == null || b == null) {
            return false;
        }

        return a.principal() == b.principal()
                && a.secondary() == b.secondary()
                && a.valid() == b.valid();
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

                Result actual = method.solve(cloneMatrix(test.input));

                if (resultsEqual(actual, test.expected)) {

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

        int rows = rng.nextInt(maxRows) + 1;
        int cols = rng.nextInt(maxCols) + 1;

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

        Random rng = new Random(987654321L);

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

            Result brute =
                    sumOfDiagonalsBruteForce(
                            cloneMatrix(matrix));

            Result refactored =
                    sumOfDiagonalsMatrix(
                            cloneMatrix(matrix));

            if (!resultsEqual(brute, refactored)) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "matrix      = " + formatMatrix(matrix));

                System.out.println(
                        "brute       = " + brute);

                System.out.println(
                        "refactored  = " + refactored);

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
         * Square Matrices
         * ============================================================
         */

        tests.add(new TestCase(
                "S1",
                new int[][]{
                        {1, 2, 3},
                        {4, 5, 6},
                        {7, 8, 9}
                },
                new Result(1 + 5 + 9, 3 + 5 + 7, true),
                "3x3 square matrix"));

        tests.add(new TestCase(
                "S2",
                new int[][]{
                        {1, 2, 3, 4},
                        {5, 6, 7, 8},
                        {9, 10, 11, 12},
                        {13, 14, 15, 16}
                },
                new Result(1 + 6 + 11 + 16, 4 + 7 + 10 + 13, true),
                "4x4 square matrix"));

        tests.add(new TestCase(
                "S3",
                new int[][]{
                        {10}
                },
                new Result(10, 10, true),
                "1x1 matrix (both diagonals same element)"));

        /*
         * ============================================================
         * Rectangular Matrices (Wide)
         * ============================================================
         */

        tests.add(new TestCase(
                "W1",
                new int[][]{
                        {1, 2, 3, 4},
                        {5, 6, 7, 8}
                },
                new Result(1 + 6, 4 + 7, true),
                "2x4 wide matrix"));

        tests.add(new TestCase(
                "W2",
                new int[][]{
                        {1, 2, 3},
                        {4, 5, 6}
                },
                new Result(1 + 5, 3 + 5, true),
                "2x3 wide matrix"));

        /*
         * ============================================================
         * Rectangular Matrices (Tall)
         * ============================================================
         */

        tests.add(new TestCase(
                "T1",
                new int[][]{
                        {1, 2},
                        {3, 4},
                        {5, 6}
                },
                new Result(1 + 4, 2 + 3, true),
                "3x2 tall matrix"));

        tests.add(new TestCase(
                "T2",
                new int[][]{
                        {1, 2, 3},
                        {4, 5, 6},
                        {7, 8, 9},
                        {10, 11, 12}
                },
                new Result(1 + 5 + 9, 3 + 5 + 7, true),
                "4x3 tall matrix"));

        /*
         * ============================================================
         * Negative / Mixed Values
         * ============================================================
         */

        tests.add(new TestCase(
                "M1",
                new int[][]{
                        {-1, -2, -3},
                        {-4, -5, -6},
                        {-7, -8, -9}
                },
                new Result(-1 + -5 + -9, -3 + -5 + -7, true),
                "3x3 with negatives"));

        tests.add(new TestCase(
                "M2",
                new int[][]{
                        {0, 0, 0},
                        {0, 0, 0},
                        {0, 0, 0}
                },
                new Result(0, 0, true),
                "3x3 all zeros"));

        /*
         * ============================================================
         * Edge Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "E1",
                null,
                new Result(0, 0, false),
                "Null matrix"));

        tests.add(new TestCase(
                "E2",
                new int[][]{},
                new Result(0, 0, false),
                "Empty matrix (zero rows)"));

        tests.add(new TestCase(
                "E3",
                new int[][]{
                        {},
                        {},
                        {}
                },
                new Result(0, 0, false),
                "Matrix with zero columns"));

        System.out.println(
                "############################################################");
        System.out.println(
                "######## SUM OF DIAGONALS IN MATRIX (INT[][]) #############");
        System.out.println(
                "############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "Brute Force (Oracle)",
                        SumOfDiagonalsMatrixTestHarness::sumOfDiagonalsBruteForce),

                new MethodCase(
                        "Refactored Single Loop O(min(m, n))",
                        SumOfDiagonalsMatrixTestHarness::sumOfDiagonalsMatrix)
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
