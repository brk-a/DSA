import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Stack;

/**
 * Largest Binary Submatrix of All 1s (Maximal Rectangle).
 *
 * Problem:
 * Given a binary matrix, find the area of the largest rectangular
 * submatrix containing only 1s.
 *
 * Notes:
 * - If the matrix is invalid (null / empty / zero-width first row),
 *   Result.valid() == false.
 *
 * Implementations:
 *
 * 1. Brute Force
 *      Enumerate every (top, left, bottom, right) rectangle and check
 *      every cell inside it.
 *      Time: O(m^2 * n^2 * m * n)   Space: O(1) extra.
 *
 * 2. Dynamic Programming (expand upward per cell)
 *      memo[i][j] = length of the run of 1s ending at column j in row i.
 *      For each cell, scan upward tracking the narrowest run seen so far
 *      (the bottleneck width for that row span) and the resulting area.
 *      Time: O(m^2 * n)   Space: O(m * n).
 *
 * 3. Histogram (largest rectangle in histogram, row by row)
 *      Maintain a running per-column histogram of consecutive 1s, reset
 *      to 0 on a 0; for every row, solve "largest rectangle in histogram"
 *      with a monotonic stack.
 *      Time: O(m * n)   Space: O(n).
 *
 * All three are cross-checked against each other for both fixed and
 * randomised test matrices, following the same test-harness shape used for
 * SumOfDiagonalsMatrixTestHarness (TestCase / Algorithm / MethodCase /
 * runTests / randomised cross-checks).
 */
public class LargestBinarySubMatrixAllOnes {

    record Result(int area, boolean valid) {}

    /* **********************************************************************
     * Validation
     * **********************************************************************/

    static boolean validMatrix(int[][] matrix) {
        return matrix != null && matrix.length > 0 && matrix[0] != null && matrix[0].length > 0;
    }

    /* **********************************************************************
     * Algorithm Implementations
     * **********************************************************************/

    static Result largestBinarySubMatrixAllOnesBruteForce(int[][] matrix) {
        if (!validMatrix(matrix)) {
            return new Result(-1, false);
        }

        int m = matrix.length;
        int n = matrix[0].length;
        int result = 0;

        for (int top = 0; top < m; top++) {
            for (int left = 0; left < n; left++) {
                for (int bottom = top; bottom < m; bottom++) {
                    for (int right = left; right < n; right++) {
                        boolean valid = true;
                        for (int i = top; i <= bottom && valid; i++) {
                            for (int j = left; j <= right; j++) {
                                if (matrix[i][j] == 0) {
                                    valid = false;
                                    break;
                                }
                            }
                        }
                        if (valid) {
                            int area = (bottom - top + 1) * (right - left + 1);
                            result = Math.max(result, area);
                        }
                    }
                }
            }
        }

        return new Result(result, true);
    }

    static Result largestBinarySubMatrixAllOnesDynamicProgramming(int[][] matrix) {
        if (!validMatrix(matrix)) {
            return new Result(-1, false);
        }

        int m = matrix.length;
        int n = matrix[0].length;
        int[][] memo = new int[m][n];
        int result = 0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 0) {
                    continue;
                }
                memo[i][j] = (j == 0) ? 1 : memo[i][j - 1] + 1;
                int width = memo[i][j];
                for (int k = i; k >= 0; k--) {
                    // The achievable width across rows [k..i] is bounded by
                    // the NARROWEST row in that span - min, not max. Using
                    // max lets a wide upper row "lend" width to a narrower
                    // lower row that can't actually support it, reporting
                    // rectangles that don't exist.
                    width = Math.min(width, memo[k][j]);
                    int area = width * (i - k + 1);
                    result = Math.max(result, area);
                }
            }
        }

        return new Result(result, true);
    }

    static Result largestBinarySubMatrixAllOnesHistogram(int[][] matrix) {
        if (!validMatrix(matrix)) {
            return new Result(-1, false);
        }

        int m = matrix.length;
        int n = matrix[0].length;
        int[] hist = new int[n];
        int result = 0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 1) {
                    hist[j]++;
                } else {
                    hist[j] = 0;
                }
            }

            result = Math.max(result, getMaxArea(hist));
        }

        return new Result(result, true);
    }

    /* **********************************************************************
     * Helper
     * **********************************************************************/

    /** Classic "largest rectangle in histogram" via a monotonic stack. */
    static int getMaxArea(int[] hist) {
        int n = hist.length;
        Stack<Integer> stack = new Stack<>();
        int result = 0;
        int tp;

        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && hist[stack.peek()] >= hist[i]) {
                tp = stack.pop();
                int width = stack.isEmpty() ? i : i - stack.peek() - 1;
                result = Math.max(result, hist[tp] * width);
            }

            stack.push(i);
        }

        while (!stack.isEmpty()) {
            tp = stack.pop();
            int width = stack.isEmpty() ? n : n - stack.peek() - 1;
            result = Math.max(result, hist[tp] * width);
        }

        return result;
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

        return a.area() == b.area() && a.valid() == b.valid();
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
                "Randomised Cross Checks (Brute vs DP vs Histogram)");
        System.out.println(
                "======================================================");

        Random rng = new Random(20260829L);

        for (int i = 1; i <= iterations; i++) {

            // Kept small (up to 7x7): brute force is O(m^3 n^3). Includes
            // non-square shapes to exercise the brute force's m/n bounds.
            int[][] matrix = randomMatrix(rng, 7, 7, 0.6);

            Result brute = largestBinarySubMatrixAllOnesBruteForce(matrix);
            Result dp = largestBinarySubMatrixAllOnesDynamicProgramming(matrix);
            Result hist = largestBinarySubMatrixAllOnesHistogram(matrix);

            if (!resultsEqual(brute, dp) || !resultsEqual(brute, hist)) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "matrix = " + formatMatrix(matrix));

                System.out.println(
                        "brute  = " + brute);

                System.out.println(
                        "dp     = " + dp);

                System.out.println(
                        "hist   = " + hist);

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
                        {1, 0, 1, 0, 0},
                        {1, 0, 1, 1, 1},
                        {1, 1, 1, 1, 1},
                        {1, 0, 0, 1, 0}
                },
                new Result(6, true),
                "classic 4x5 worked example"));

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
                new Result(12, true),
                "all ones: the whole matrix"));

        tests.add(new TestCase(
                "B4",
                new int[][]{{1, 1, 0, 1, 1, 1}},
                new Result(3, true),
                "single row: longest run of 1s"));

        tests.add(new TestCase(
                "B5",
                new int[][]{{1}, {1}, {0}, {1}},
                new Result(2, true),
                "single column: longest run of 1s"));

        /*
         * ============================================================
         * Non-Square Regression
         * (guards the original brute-force loop bounds, which used
         * "bottom < n" and "right < m" - swapped with the matrix's
         * actual row/column counts, invisible only on square matrices)
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
                new Result(15, true),
                "5x3 all-ones (more rows than columns)"));

        /*
         * ============================================================
         * DP Min/Max Regression
         * (guards the original "Math.max" in the upward scan, which
         * let a wider row lend width to a narrower one beneath it and
         * reported rectangles that don't actually exist)
         * ============================================================
         */

        tests.add(new TestCase(
                "R2",
                new int[][]{
                        {1, 1},
                        {1, 1},
                        {0, 1}
                },
                new Result(4, true),
                "narrower bottom row must cap the achievable width above it"));

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
                "############  LARGEST BINARY SUBMATRIX OF ALL 1s  ##########");
        System.out.println(
                "############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "Brute Force",
                        LargestBinarySubMatrixAllOnes::largestBinarySubMatrixAllOnesBruteForce),

                new MethodCase(
                        "Dynamic Programming",
                        LargestBinarySubMatrixAllOnes::largestBinarySubMatrixAllOnesDynamicProgramming),

                new MethodCase(
                        "Histogram",
                        LargestBinarySubMatrixAllOnes::largestBinarySubMatrixAllOnesHistogram)
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
