import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Largest Square Submatrix of All 1s.
 *
 * Problem:
 * Given a binary matrix, find the SIDE LENGTH of the largest square
 * submatrix containing only 1s. (Not the area - none of the four
 * implementations below square their result, so "result" is side length
 * throughout, consistent with the classic textbook recurrence:
 * dp[i][j] = 1 + min(dp[i][j+1], dp[i+1][j], dp[i+1][j+1]) when
 * matrix[i][j] == 1.)
 *
 * Notes:
 * - If the matrix is invalid (null / empty / zero-width first row),
 *   Result.valid() == false.
 * - None of the four methods mutate matrix, so no defensive copy is
 *   needed anywhere (the original's matrix.clone() calls did nothing
 *   useful - shallow copies that were never written to in the first
 *   place - so they've been removed rather than "fixed").
 *
 * Implementations:
 *
 * 1. Recursion (naive, exponential)
 *      Explores the full grid from (0,0), following (i, j+1), (i+1, j)
 *      and (i+1, j+1), with heavy redundant re-exploration of shared
 *      subproblems.
 *      Time: O(3^(m+n)) worst case   Space: O(m+n) recursion depth.
 *
 * 2. Memoisation (top-down DP with a cache)
 *      Same recursion, but each (i, j) is solved once and cached.
 *      Time: O(m * n)   Space: O(m * n).
 *
 * 3. Tabulation (bottom-up DP, full 2D table)
 *      Fills the same recurrence from the bottom-right corner up.
 *      Time: O(m * n)   Space: O(m * n).
 *
 * 4. Optimised DP (bottom-up, single rolling row + diagonal)
 *      Same recurrence, but keeps only one row's worth of state plus a
 *      single "diagonal" value carried across columns.
 *      Time: O(m * n)   Space: O(n).
 *
 * All four are cross-checked against each other for both fixed and
 * randomised test matrices, following the same test-harness shape used
 * for SumOfDiagonalsMatrixTestHarness (TestCase / Algorithm / MethodCase /
 * runTests / randomised cross-checks).
 */
public class LargestSquareSubMatrixAll1s {

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

    static Result largestSquareSubMatrixAll1sRecursion(int[][] matrix) {
        if (!validMatrix(matrix)) {
            return new Result(-1, false);
        }

        int[] result = new int[1];
        maxSquareRecursion(0, 0, matrix, result);

        return new Result(result[0], true);
    }

    static Result largestSquareSubMatrixAll1sMemoisation(int[][] matrix) {
        if (!validMatrix(matrix)) {
            return new Result(-1, false);
        }

        int m = matrix.length;
        int n = matrix[0].length;
        int[] result = new int[]{0};
        int[][] memo = new int[m][n];
        for (int[] row : memo) {
            Arrays.fill(row, -1);
        }
        maxSquareMemoisation(0, 0, matrix, result, memo);

        return new Result(result[0], true);
    }

    static Result largestSquareSubMatrixAll1sTabulation(int[][] matrix) {
        if (!validMatrix(matrix)) {
            return new Result(-1, false);
        }

        int m = matrix.length;
        int n = matrix[0].length;
        int result = 0;
        int[][] tab = new int[m + 1][n + 1];

        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                if (matrix[i][j] == 0) {
                    tab[i][j] = 0;
                    continue;
                }

                tab[i][j] = 1 + Math.min(tab[i][j + 1], Math.min(tab[i + 1][j], tab[i + 1][j + 1]));
                result = Math.max(result, tab[i][j]);
            }
        }

        return new Result(result, true);
    }

    static Result largestSquareSubMatrixAll1sOptimisedDP(int[][] matrix) {
        if (!validMatrix(matrix)) {
            return new Result(-1, false);
        }

        int m = matrix.length;
        int n = matrix[0].length;
        int result = 0;
        // Indexed by COLUMN, one entry per column plus one padding slot -
        // this holds "row i+1's value at this column" until it gets
        // overwritten with "row i's value" as the sweep passes through.
        int[] tab = new int[n + 1];
        int diagonal;

        for (int i = m - 1; i >= 0; i--) {
            diagonal = 0;
            for (int j = n - 1; j >= 0; j--) {
                int down = tab[j]; // row i+1's value at this column, before it gets overwritten
                if (matrix[i][j] == 0) {
                    tab[j] = 0;
                } else {
                    tab[j] = 1 + Math.min(tab[j + 1], Math.min(diagonal, down));
                }
                diagonal = down; // becomes "row i+1, column j" for the next (j-1) iteration
                result = Math.max(result, tab[j]);
            }
        }

        return new Result(result, true);
    }

    /* **********************************************************************
     * Helpers
     * **********************************************************************/

    /** Returns the side length of the largest all-1s square with top-left corner (i, j). */
    static int maxSquareRecursion(int i, int j, int[][] matrix, int[] result) {
        if (i < 0 || i == matrix.length || j < 0 || j == matrix[0].length) {
            return 0;
        }

        int right = maxSquareRecursion(i, j + 1, matrix, result);
        int down = maxSquareRecursion(i + 1, j, matrix, result);
        int diagonal = maxSquareRecursion(i + 1, j + 1, matrix, result);

        if (matrix[i][j] == 0) {
            return 0;
        }

        int val = 1 + Math.min(right, Math.min(down, diagonal));
        result[0] = Math.max(result[0], val);

        return val;
    }

    /** Same as maxSquareRecursion, but each (i, j) is solved and cached only once. */
    static int maxSquareMemoisation(int i, int j, int[][] matrix, int[] result, int[][] memo) {
        if (i < 0 || i == matrix.length || j < 0 || j == matrix[0].length) {
            return 0;
        }
        if (memo[i][j] != -1) {
            return memo[i][j];
        }

        int right = maxSquareMemoisation(i, j + 1, matrix, result, memo);
        int down = maxSquareMemoisation(i + 1, j, matrix, result, memo);
        int diagonal = maxSquareMemoisation(i + 1, j + 1, matrix, result, memo);

        if (matrix[i][j] == 0) {
            memo[i][j] = 0;
            return 0;
        }

        int val = 1 + Math.min(right, Math.min(down, diagonal));
        result[0] = Math.max(result[0], val);
        memo[i][j] = val;

        return val;
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

                Result actual = method.solve(test.input);

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

    static int[][] randomMatrix(Random rng, int maxRows, int maxCols, double oneProbability) {
        int rows = rng.nextInt(maxRows) + 1;
        int cols = rng.nextInt(maxCols) + 1;
        int[][] matrix = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = rng.nextDouble() < oneProbability ? 1 : 0;
            }
        }
        return matrix;
    }

    static void runRandomisedTests(int iterations) {

        System.out.println(
                "======================================================");
        System.out.println(
                "Randomised Cross Checks (Recursion vs Memoisation vs Tabulation vs Optimised DP)");
        System.out.println(
                "======================================================");

        Random rng = new Random(20260901L);

        for (int i = 1; i <= iterations; i++) {

            // Kept small (up to 6x6): naive recursion is exponential.
            int[][] matrix = randomMatrix(rng, 6, 6, 0.65);

            Result recursion = largestSquareSubMatrixAll1sRecursion(matrix);
            Result memo = largestSquareSubMatrixAll1sMemoisation(matrix);
            Result tab = largestSquareSubMatrixAll1sTabulation(matrix);
            Result opt = largestSquareSubMatrixAll1sOptimisedDP(matrix);

            if (!resultsEqual(recursion, memo) || !resultsEqual(recursion, tab) || !resultsEqual(recursion, opt)) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "matrix    = " + formatMatrix(matrix));

                System.out.println(
                        "recursion = " + recursion);

                System.out.println(
                        "memo      = " + memo);

                System.out.println(
                        "tab       = " + tab);

                System.out.println(
                        "opt       = " + opt);

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
         * (expected values verified with an independent Python
         * brute force before being hardcoded here)
         * ============================================================
         */

        tests.add(new TestCase(
                "B1",
                new int[][]{
                        {1, 1, 1, 0},
                        {1, 1, 1, 1},
                        {1, 1, 1, 1},
                        {0, 1, 1, 1}
                },
                new Result(3, true),
                "classic worked example"));

        tests.add(new TestCase(
                "B2",
                new int[][]{
                        {0, 0, 0},
                        {0, 0, 0},
                        {0, 0, 0}
                },
                new Result(0, true),
                "all zeros"));

        tests.add(new TestCase(
                "B3",
                new int[][]{
                        {1, 1, 1, 1},
                        {1, 1, 1, 1},
                        {1, 1, 1, 1}
                },
                new Result(3, true),
                "all ones 3x4: bounded by the smaller dimension"));

        tests.add(new TestCase(
                "B4",
                new int[][]{{1, 1, 1, 1, 1}},
                new Result(1, true),
                "single row: a square can only ever be 1x1"));

        tests.add(new TestCase(
                "B5",
                new int[][]{{1}, {1}, {1}, {1}},
                new Result(1, true),
                "single column: a square can only ever be 1x1"));

        tests.add(new TestCase(
                "B6",
                new int[][]{{1}},
                new Result(1, true),
                "1x1 matrix, one"));

        tests.add(new TestCase(
                "B7",
                new int[][]{{0}},
                new Result(0, true),
                "1x1 matrix, zero"));

        tests.add(new TestCase(
                "B8",
                new int[][]{
                        {1, 1, 0, 1, 1},
                        {1, 1, 0, 1, 1},
                        {0, 0, 0, 0, 0},
                        {1, 1, 1, 1, 1},
                        {1, 1, 1, 1, 1}
                },
                new Result(2, true),
                "a blocking row of zeros splits two smaller squares"));

        /*
         * ============================================================
         * Non-Square Regression
         * (guards both the tabulation method's missing +1, which
         * previously made every result 0 regardless of matrix content,
         * and the optimised DP's row-vs-column indexing confusion,
         * which was invisible only on square matrices)
         * ============================================================
         */

        tests.add(new TestCase(
                "R1",
                new int[][]{
                        {1, 1, 1},
                        {1, 1, 1},
                        {1, 1, 1},
                        {1, 1, 1},
                        {1, 1, 1}
                },
                new Result(3, true),
                "5x3 all-ones (more rows than columns)"));

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
                "############  LARGEST SQUARE SUBMATRIX OF ALL 1s  ##########");
        System.out.println(
                "############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "Recursion",
                        LargestSquareSubMatrixAll1s::largestSquareSubMatrixAll1sRecursion),

                new MethodCase(
                        "Memoisation",
                        LargestSquareSubMatrixAll1s::largestSquareSubMatrixAll1sMemoisation),

                new MethodCase(
                        "Tabulation",
                        LargestSquareSubMatrixAll1s::largestSquareSubMatrixAll1sTabulation),

                new MethodCase(
                        "Optimised DP",
                        LargestSquareSubMatrixAll1s::largestSquareSubMatrixAll1sOptimisedDP)
        );

        for (MethodCase method : methods) {

            runTests(
                    method.name,
                    method.algorithm,
                    tests);
        }

        runRandomisedTests(3000);
    }
}
