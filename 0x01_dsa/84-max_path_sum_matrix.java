import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Maximum Path Sum in a Matrix.
 *
 * Problem:
 * Given an m x n matrix of integers (which may be negative), a path starts
 * at any cell in row 0 and, at each step, may move to the cell directly
 * below, or diagonally below-left, or diagonally below-right. A path may
 * stop after any number of steps (it does not have to reach the last row).
 * Find the maximum possible sum over all such paths.
 *
 * Notes:
 * - If the matrix is invalid (null / empty / zero-width first row),
 *   Result.valid() == false.
 * - Only one implementation was given in the original code. A second,
 *   independently-written brute-force oracle (maxPathSumMatrixBruteForce)
 *   was added below so the DP version has something genuine to be
 *   cross-checked against, matching the multi-implementation pattern used
 *   throughout the rest of this series.
 *
 * Implementations:
 *
 * 1. DP (row-by-row, in-place on a cloned grid)
 *      dp[i][j] = matrix[i][j] + max(dp[i-1][j-1], dp[i-1][j], dp[i-1][j+1]),
 *      skipping any of the three parents that falls outside the grid.
 *      Time: O(m * n)   Space: O(m * n) for the working copy.
 *
 * 2. Brute Force (reference oracle, added for cross-checking)
 *      Explicitly enumerates every path from every starting cell in row 0,
 *      branching -1/0/+1 in column at each step, treating every visited
 *      cell as a possible stopping point.
 *      Time: O(n * 3^m)   Space: O(m) recursion depth.
 *
 * Both are cross-checked against each other for both fixed and randomised
 * test matrices, following the same test-harness shape used for
 * SumOfDiagonalsMatrixTestHarness (TestCase / Algorithm / MethodCase /
 * runTests / randomised cross-checks).
 */
public class MaxPathSumMatrix {

    static record Result(int result, boolean valid) {}

    /* **********************************************************************
     * Validation
     * **********************************************************************/

    static boolean validMatrix(int[][] matrix) {
        return matrix != null && matrix.length > 0 && matrix[0] != null && matrix[0].length > 0;
    }

    /* **********************************************************************
     * Algorithm Implementations
     * **********************************************************************/

    static Result maxPathSumMatrixDP(int[][] matrix) {
        if (!validMatrix(matrix)) {
            return new Result(-1, false);
        }

        int m = matrix.length;
        int n = matrix[0].length;
        int[][] clone = cloneMatrix(matrix);
        int result = Arrays.stream(clone[0]).max().getAsInt();

        // Row 0 is the base case (a path of length 1, already accounted for
        // above) - the recurrence only applies from row 1 downward, since
        // it looks at "the row above".
        for (int i = 1; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int up = clone[i - 1][j];
                // A missing diagonal must never be treated as if it
                // contributed 0: on an all-negative row that would let a
                // non-existent neighbour beat every real one. MIN_VALUE
                // guarantees a missing direction can only "win" if it's
                // literally the only option, which never happens here
                // since `up` always exists once i >= 1.
                int left = (j > 0) ? clone[i - 1][j - 1] : Integer.MIN_VALUE;
                int right = (j < n - 1) ? clone[i - 1][j + 1] : Integer.MIN_VALUE;
                clone[i][j] += Math.max(up, Math.max(left, right));
                result = Math.max(result, clone[i][j]);
            }
        }

        return new Result(result, true);
    }

    static Result maxPathSumMatrixBruteForce(int[][] matrix) {
        if (!validMatrix(matrix)) {
            return new Result(-1, false);
        }

        int m = matrix.length;
        int n = matrix[0].length;
        int best = Integer.MIN_VALUE;

        for (int j = 0; j < n; j++) {
            best = Math.max(best, explore(matrix, 0, j, matrix[0][j], m, n));
        }

        return new Result(best, true);
    }

    /* **********************************************************************
     * Helper
     * **********************************************************************/

    /** Explores every downward path from (row, col); every cell visited is a valid stop. */
    static int explore(int[][] matrix, int row, int col, int sumSoFar, int m, int n) {
        int best = sumSoFar;

        if (row + 1 < m) {
            for (int dc = -1; dc <= 1; dc++) {
                int nextCol = col + dc;
                if (nextCol >= 0 && nextCol < n) {
                    best = Math.max(best,
                            explore(matrix, row + 1, nextCol, sumSoFar + matrix[row + 1][nextCol], m, n));
                }
            }
        }

        return best;
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
            copy[i] = matrix[i] == null ? null : matrix[i].clone();
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
            sb.append(matrix[i] == null ? "null" : Arrays.toString(matrix[i]));
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

        return a.result() == b.result() && a.valid() == b.valid();
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
                "Randomised Cross Checks (DP vs Brute Force)");
        System.out.println(
                "======================================================");

        Random rng = new Random(20260824L);

        for (int i = 1; i <= iterations; i++) {

            // Kept small (up to 8x8): brute force is O(n * 3^m).
            // Values include negatives specifically to exercise the
            // MIN_VALUE boundary fix on both edge columns.
            int[][] matrix = randomMatrix(rng, 8, 8, -9, 9);

            Result dp = maxPathSumMatrixDP(cloneMatrix(matrix));
            Result brute = maxPathSumMatrixBruteForce(cloneMatrix(matrix));

            if (!resultsEqual(dp, brute)) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "matrix = " + formatMatrix(matrix));

                System.out.println(
                        "dp     = " + dp);

                System.out.println(
                        "brute  = " + brute);

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
         * Basic Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "B1",
                new int[][]{
                        {1, 2, 3},
                        {4, 5, 6},
                        {7, 8, 9}
                },
                new Result(18, true),
                "3x3 ascending columns, best path runs straight down column 2 (3+6+9)"));

        tests.add(new TestCase(
                "B2",
                new int[][]{{42}},
                new Result(42, true),
                "1x1 matrix"));

        tests.add(new TestCase(
                "B3",
                new int[][]{
                        {3, -1, 7, 2}
                },
                new Result(7, true),
                "single row: best path is just the largest single cell"));

        tests.add(new TestCase(
                "B4",
                new int[][]{
                        {1},
                        {2},
                        {3}
                },
                new Result(6, true),
                "single column: only one possible path, straight down"));

        /*
         * ============================================================
         * Boundary Regression
         * (guards the original "0 as a missing-neighbour placeholder"
         * bug: on an all-negative row, a nonexistent diagonal must not
         * be able to outscore the real, negative neighbours)
         * ============================================================
         */

        tests.add(new TestCase(
                "R1",
                new int[][]{
                        {-5, -3, -5},
                        {10, 10, 10}
                },
                new Result(7, true),
                "left/right edge columns both have only negative real neighbours"));

        /*
         * ============================================================
         * Early-Stopping Semantics
         * (a path may end after any step - it is not required to reach
         * the last row)
         * ============================================================
         */

        tests.add(new TestCase(
                "R2",
                new int[][]{
                        {-1, -2},
                        {-3, -4},
                        {-5, -6}
                },
                new Result(-1, true),
                "paths may stop after any step: best is to stop immediately, never forced further down"));

        /*
         * ============================================================
         * Larger / Mixed-Sign Matrices
         * ============================================================
         */

        tests.add(new TestCase(
                "M1",
                new int[][]{
                        {5, -4, 2, 3},
                        {-3, 4, 2, -1},
                        {6, -2, 1, -3},
                        {-1, 5, 1, 0}
                },
                new Result(20, true), // verified via the independent brute-force oracle
                "4x4 mixed positive/negative"));

        tests.add(new TestCase(
                "M2",
                new int[][]{
                        {2, -1, 3, -2, 4},
                        {-3, 2, -1, 3, -2}
                },
                new Result(7, true), // verified via the independent brute-force oracle
                "2x5 non-square, mixed signs"));

        /*
         * ============================================================
         * Edge Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "E1",
                null,
                new Result(-1, false),
                "null matrix"));

        tests.add(new TestCase(
                "E2",
                new int[][]{},
                new Result(-1, false),
                "empty matrix (zero rows)"));

        tests.add(new TestCase(
                "E3",
                new int[][]{
                        {},
                        {},
                        {}
                },
                new Result(-1, false),
                "matrix with zero columns"));

        System.out.println(
                "############################################################");
        System.out.println(
                "###################  MAX PATH SUM MATRIX  ##################");
        System.out.println(
                "############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "DP",
                        MaxPathSumMatrix::maxPathSumMatrixDP),

                new MethodCase(
                        "Brute Force (reference oracle)",
                        MaxPathSumMatrix::maxPathSumMatrixBruteForce)
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
