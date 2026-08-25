import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Count Unique Paths in a Matrix.
 *
 * Problem:
 * Given an m x n grid, count how many distinct paths lead from the
 * top-left cell to the bottom-right cell, moving only right or down at
 * each step (no obstacles - this is LeetCode 62, not the obstacle variant
 * "Unique Paths II"). Only the grid's DIMENSIONS matter; cell contents are
 * never read by any of the three approaches below.
 *
 * Notes:
 * - If the matrix is invalid (null / empty / zero-width first row),
 *   every method returns -1. A valid m x n grid (m, n >= 1) always has at
 *   least one path, so -1 is an unambiguous "invalid input" sentinel here.
 *
 * Implementations:
 *
 * 1. Recursion
 *      Naive branching recursion: from each cell, recurse right and down,
 *      counting the paths that reach the destination.
 *      Time: O(2^(m+n))   Space: O(m+n) recursion depth.
 *
 * 2. Tabulation (bottom-up DP, rolling 1D array)
 *      memo[j] holds the path count to column j "so far"; sweeping rows
 *      top to bottom and columns left to right in place turns
 *      paths[i][j] = paths[i-1][j] + paths[i][j-1] into a single rolling
 *      array update, memo[j] += memo[j-1].
 *      (Renamed from the original "Memoisation" - what's implemented here
 *      is bottom-up tabulation, not a top-down cache, so the old name was
 *      misleading rather than wrong code.)
 *      Time: O(m * n)   Space: O(n).
 *
 * 3. Combinatorics
 *      A path is a sequence of (m-1) down-moves and (n-1) right-moves in
 *      some order, so the answer is C(m+n-2, m-1), computed iteratively
 *      without needing factorials of the full size.
 *      Time: O(min(m, n))   Space: O(1).
 *
 * All three are cross-checked against each other for both fixed and
 * randomised test grids, following the same test-harness shape used for
 * SumOfDiagonalsMatrixTestHarness (TestCase / Algorithm / MethodCase /
 * runTests / randomised cross-checks).
 */
public class CountUniquePathsMatrix {

    /* **********************************************************************
     * Validation
     * **********************************************************************/

    static boolean validMatrix(int[][] matrix) {
        return matrix != null && matrix.length > 0 && matrix[0] != null && matrix[0].length > 0;
    }

    /* **********************************************************************
     * Algorithm Implementations
     * **********************************************************************/

    static int countUniquePathsRecursion(int[][] matrix) {
        if (!validMatrix(matrix)) {
            return -1;
        }

        int m = matrix.length;
        int n = matrix[0].length;

        return uniquePathsRecursion(0, 0, m, n);
    }

    static int countUniquePathsTabulation(int[][] matrix) {
        if (!validMatrix(matrix)) {
            return -1;
        }

        int m = matrix.length;
        int n = matrix[0].length;
        int[] table = new int[n];
        table[0] = 1;

        for (int i = 0; i < m; i++) {
            for (int j = 1; j < n; j++) {
                table[j] += table[j - 1];
            }
        }

        return table[n - 1];
    }

    static int countUniquePathsCombinatorics(int[][] matrix) {
        if (!validMatrix(matrix)) {
            return -1;
        }

        int m = matrix.length;
        int n = matrix[0].length;
        long paths = 1;
        int totalMoves = m + n - 2;
        int r = Math.min(m - 1, n - 1); // nCr

        for (int i = 1; i <= r; i++) {
            paths = paths * (totalMoves - r + i) / i;
        }

        return (int) paths;
    }

    /* **********************************************************************
     * Helper
     * **********************************************************************/

    static int uniquePathsRecursion(int currRow, int currCol, int totalRows, int totalCols) {
        if (currRow == totalRows - 1 && currCol == totalCols - 1) {
            return 1;
        }
        if (currRow >= totalRows || currCol >= totalCols) {
            return 0;
        }

        return uniquePathsRecursion(currRow + 1, currCol, totalRows, totalCols)
                + uniquePathsRecursion(currRow, currCol + 1, totalRows, totalCols);
    }

    /* **********************************************************************
     * Test Harness Types
     * **********************************************************************/

    static class TestCase {

        final String id;
        final int[][] input;
        final int expected;
        final String description;

        TestCase(
                String id,
                int[][] input,
                int expected,
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

    static String formatMatrixShape(int[][] matrix) {

        if (matrix == null) {
            return "null";
        }

        if (matrix.length == 0) {
            return "0x0";
        }

        int cols = matrix[0] == null ? 0 : matrix[0].length;
        return matrix.length + "x" + cols;
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

                int actual = method.solve(test.input);

                if (actual == test.expected) {

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
                            formatMatrixShape(test.input));

                    System.out.printf(
                            "  expected  = %d%n",
                            test.expected);

                    System.out.printf(
                            "  actual    = %d%n",
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
                        formatMatrixShape(test.input));

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

    static int[][] randomShapedMatrix(Random rng, int maxRows, int maxCols) {
        int rows = rng.nextInt(maxRows) + 1;
        int cols = rng.nextInt(maxCols) + 1;
        // Content is irrelevant to this problem; zero-fill is enough.
        return new int[rows][cols];
    }

    static void runRandomisedTests(int iterations) {

        System.out.println(
                "======================================================");
        System.out.println(
                "Randomised Cross Checks (Recursion vs Tabulation vs Combinatorics)");
        System.out.println(
                "======================================================");

        Random rng = new Random(20260823L);

        for (int i = 1; i <= iterations; i++) {

            // Kept small (up to 7x7): the recursion approach is O(2^(m+n))
            // and would blow up quickly on larger grids.
            int[][] matrix = randomShapedMatrix(rng, 7, 7);

            int recursion = countUniquePathsRecursion(matrix);
            int tabulation = countUniquePathsTabulation(matrix);
            int combinatorics = countUniquePathsCombinatorics(matrix);

            if (recursion != tabulation || recursion != combinatorics) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "shape         = " + formatMatrixShape(matrix));

                System.out.println(
                        "recursion     = " + recursion);

                System.out.println(
                        "tabulation    = " + tabulation);

                System.out.println(
                        "combinatorics = " + combinatorics);

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
                new int[1][1],
                1,
                "1x1 grid, already at the destination"));

        tests.add(new TestCase(
                "B2",
                new int[2][2],
                2,
                "2x2 grid"));

        tests.add(new TestCase(
                "B3",
                new int[3][3],
                6,
                "3x3 grid, C(4,2) = 6"));

        tests.add(new TestCase(
                "B4",
                new int[3][7],
                28,
                "classic 3x7 example (well-known unique-paths reference case)"));

        tests.add(new TestCase(
                "B5",
                new int[7][3],
                28,
                "7x3, transpose of B4, same answer by symmetry"));

        /*
         * ============================================================
         * Rows / Columns of 1 (straight-line, only one possible path)
         * ============================================================
         */

        tests.add(new TestCase(
                "L1",
                new int[1][10],
                1,
                "single row: only one path, straight across"));

        tests.add(new TestCase(
                "L2",
                new int[8][1],
                1,
                "single column: only one path, straight down"));

        /*
         * ============================================================
         * Targeted Regression: Combinatorics Truncation
         * (guards the original "(totalMoves - r + i) / i" bug, which
         * divided each term before multiplying into the running product
         * and silently undercounted whenever that division wasn't exact)
         * ============================================================
         */

        tests.add(new TestCase(
                "C1",
                new int[4][3],
                10,
                "4x3: buggy division order previously gave 8 instead of 10"));

        tests.add(new TestCase(
                "C2",
                new int[4][4],
                20,
                "4x4: buggy division order previously gave 16 instead of 20"));

        tests.add(new TestCase(
                "C3",
                new int[5][5],
                70,
                "5x5, C(8,4) = 70"));

        /*
         * ============================================================
         * Edge Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "E1",
                null,
                -1,
                "null matrix"));

        tests.add(new TestCase(
                "E2",
                new int[][]{},
                -1,
                "empty matrix (zero rows)"));

        tests.add(new TestCase(
                "E3",
                new int[][]{
                        {},
                        {},
                        {}
                },
                -1,
                "matrix with zero columns"));

        System.out.println(
                "############################################################");
        System.out.println(
                "####################  UNIQUE PATHS  ########################");
        System.out.println(
                "############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "Recursion",
                        CountUniquePathsMatrix::countUniquePathsRecursion),

                new MethodCase(
                        "Tabulation",
                        CountUniquePathsMatrix::countUniquePathsTabulation),

                new MethodCase(
                        "Combinatorics",
                        CountUniquePathsMatrix::countUniquePathsCombinatorics)
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
