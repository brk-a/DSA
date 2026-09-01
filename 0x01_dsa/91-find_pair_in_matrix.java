import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Find a Pair with Maximum Difference (larger element strictly below-right).
 *
 * Problem:
 * Given an m x n matrix, find the maximum value of matrix[x][y] - matrix[i][j]
 * over every pair of cells where x > i AND y > j (the larger value's cell is
 * strictly below AND to the right of the smaller value's cell).
 *
 * Notes:
 * - If the matrix is invalid (null / empty / zero-width first row),
 *   Result.valid() == false, with diff == -1.
 * - If the matrix IS well-formed but no cell has anything strictly
 *   below-right of it (a single row, a single column, or a 1x1 matrix),
 *   there is no valid pair at all. Both methods report this as
 *   Result(Integer.MIN_VALUE, true) - the matrix itself was fine
 *   (valid == true), there just isn't an answer to find. This is
 *   deliberately a different sentinel from the "-1, invalid input" case.
 *
 * Implementations:
 *
 * 1. Brute Force
 *      Check every (i, j) against every (x, y) strictly below-right of it.
 *      Time: O(m^2 * n^2)   Space: O(1) extra.
 *
 * 2. Suffix Max (rolling row)
 *      For each row, build a "max value anywhere in the bottom-right
 *      suffix rectangle starting at this cell" array, computed from the
 *      row below. Looking up the suffix max at (i+1, j+1) gives the best
 *      possible partner for (i, j) in O(1).
 *      Time: O(m * n)   Space: O(n).
 *
 * Both are cross-checked against each other for both fixed and randomised
 * test matrices, following the same test-harness shape used for
 * SumOfDiagonalsMatrixTestHarness (TestCase / Algorithm / MethodCase /
 * runTests / randomised cross-checks).
 */
public class FindPairInMatrix {

    static record Result(int diff, boolean valid) {}

    /* **********************************************************************
     * Validation
     * **********************************************************************/

    static boolean validMatrix(int[][] matrix) {
        return matrix != null && matrix.length > 0 && matrix[0] != null && matrix[0].length > 0;
    }

    /* **********************************************************************
     * Algorithm Implementations
     * **********************************************************************/

    static Result findPairInMatrixBruteForce(int[][] matrix) {
        if (!validMatrix(matrix)) {
            return new Result(-1, false);
        }

        int m = matrix.length;
        int n = matrix[0].length;
        int diff = Integer.MIN_VALUE;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int x = i + 1; x < m; x++) {
                    for (int y = j + 1; y < n; y++) {
                        diff = Math.max(diff, matrix[x][y] - matrix[i][j]);
                    }
                }
            }
        }

        return new Result(diff, true);
    }

    static Result findPairInMatrixSuffixMax(int[][] matrix) {
        if (!validMatrix(matrix)) {
            return new Result(-1, false);
        }

        int m = matrix.length;
        int n = matrix[0].length;

        // nextRow[j] = max value anywhere in the suffix rectangle starting
        // at (row below current, column j) - initialised here for the last
        // row, where that rectangle is just the row itself from j onward.
        int[] nextRow = new int[n];
        nextRow[n - 1] = matrix[m - 1][n - 1];
        for (int j = n - 2; j >= 0; j--) {
            nextRow[j] = Math.max(matrix[m - 1][j], nextRow[j + 1]);
        }

        int diff = Integer.MIN_VALUE;

        for (int i = m - 2; i >= 0; i--) {
            int[] currRow = new int[n];
            currRow[n - 1] = Math.max(matrix[i][n - 1], nextRow[n - 1]);

            for (int j = n - 2; j >= 0; j--) {
                // nextRow[j + 1] is the max value strictly below-right of
                // (i, j) - exactly the best possible partner for it.
                diff = Math.max(diff, nextRow[j + 1] - matrix[i][j]);
                currRow[j] = Math.max(matrix[i][j], Math.max(currRow[j + 1], nextRow[j]));
            }

            nextRow = currRow;
        }

        return new Result(diff, true);
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

        return a.diff() == b.diff() && a.valid() == b.valid();
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

    static int[][] randomMatrix(Random rng, int maxRows, int maxCols, int minValue, int maxValue) {
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
                "Randomised Cross Checks (Brute Force vs Suffix Max)");
        System.out.println(
                "======================================================");

        Random rng = new Random(20260831L);

        for (int i = 1; i <= iterations; i++) {

            // Kept modest (up to 8x8): brute force is O(m^2 n^2). Includes
            // non-square shapes deliberately to exercise the suffix-max
            // sizing bug that was invisible on square matrices.
            int[][] matrix = randomMatrix(rng, 8, 8, -20, 20);

            Result brute = findPairInMatrixBruteForce(matrix);
            Result suffix = findPairInMatrixSuffixMax(matrix);

            if (!resultsEqual(brute, suffix)) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "matrix = " + formatMatrix(matrix));

                System.out.println(
                        "brute  = " + brute);

                System.out.println(
                        "suffix = " + suffix);

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
                        {1, 2, 3, 4},
                        {5, 6, 7, 8},
                        {9, 10, 11, 12},
                        {13, 14, 15, 16}
                },
                new Result(15, true),
                "ascending 4x4: best pair is top-left and bottom-right"));

        tests.add(new TestCase(
                "B2",
                new int[][]{
                        {-1, -2, -3},
                        {-4, -5, -6},
                        {10, 20, 30}
                },
                new Result(35, true),
                "mixed sign values"));

        /*
         * ============================================================
         * Non-Square Regression
         * (guards the original array-sizing and m/n-confusion bugs in
         * the suffix-max method, invisible on square matrices - the
         * original crashed outright on a shape like this one)
         * ============================================================
         */

        tests.add(new TestCase(
                "R1",
                new int[][]{
                        {3, 1, 4, 1, 5},
                        {9, 2, 6, 5, 3},
                        {5, 8, 9, 7, 9}
                },
                new Result(8, true),
                "3x5 non-square matrix, more columns than rows"));

        tests.add(new TestCase(
                "R2",
                new int[][]{
                        {3, 1},
                        {4, 1},
                        {5, 9},
                        {2, 6},
                        {5, 3}
                },
                new Result(6, true),
                "5x2 non-square matrix, more rows than columns"));

        /*
         * ============================================================
         * No-Pair Cases
         * (matrix is well-formed, but shape makes x > i AND y > j
         * unsatisfiable for any cell)
         * ============================================================
         */

        tests.add(new TestCase(
                "N1",
                new int[][]{{5}},
                new Result(Integer.MIN_VALUE, true),
                "1x1 matrix: no cell below-right of the only cell"));

        tests.add(new TestCase(
                "N2",
                new int[][]{{1, 2, 3, 4, 5}},
                new Result(Integer.MIN_VALUE, true),
                "single row: no row below to pick x from"));

        tests.add(new TestCase(
                "N3",
                new int[][]{{1}, {2}, {3}},
                new Result(Integer.MIN_VALUE, true),
                "single column: no column right to pick y from"));

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
                "######################  FIND PAIR IN MATRIX  ###############");
        System.out.println(
                "############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "Brute Force",
                        FindPairInMatrix::findPairInMatrixBruteForce),

                new MethodCase(
                        "Suffix Max",
                        FindPairInMatrix::findPairInMatrixSuffixMax)
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
