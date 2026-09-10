import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Maximum Sum k x k Submatrix.
 *
 * Problem:
 * Given an m x n matrix and a size k, find the maximum sum over every
 * k x k submatrix.
 *
 * Notes:
 * - If the matrix is invalid (null / empty / zero-width first row), or k
 *   is not a valid submatrix size (k < 1, or k bigger than either
 *   dimension), every method returns Result(-1, false). The k validation
 *   is an added robustness check - the original code never validated k at
 *   all, so a mismatched k would previously have produced either an
 *   ArrayIndexOutOfBoundsException or a silently meaningless answer
 *   rather than failing predictably.
 *
 * Implementations:
 *
 * 1. Brute Force
 *      Check every possible top-left corner and sum the k x k block
 *      directly.
 *      Time: O(m * n * k^2)   Space: O(1) extra.
 *
 * 2. Prefix Sum
 *      Build a 2D prefix-sum matrix so any k x k block's sum is an O(1)
 *      inclusion-exclusion lookup.
 *      Time: O(m * n)   Space: O(m * n).
 *
 * 3. Sliding Window
 *      Slide a k-row window down, maintaining a running per-column sum,
 *      then slide a k-column window across those sums.
 *      Time: O(m * n)   Space: O(n).
 *
 * All three are cross-checked against each other for both fixed and
 * randomised test matrices, following the same test-harness shape used
 * for SumOfDiagonalsMatrixTestHarness (TestCase / Algorithm / MethodCase /
 * runTests / randomised cross-checks).
 */
public class MaxSumSquareSubMatrix {

    static record Result(int maxSum, boolean valid) {}

    /* **********************************************************************
     * Validation
     * **********************************************************************/

    static boolean validMatrix(int[][] matrix) {
        return matrix != null && matrix.length > 0 && matrix[0] != null && matrix[0].length > 0;
    }

    static boolean validK(int m, int n, int k) {
        return k >= 1 && k <= m && k <= n;
    }

    /* **********************************************************************
     * Algorithm Implementations
     * **********************************************************************/

    static Result maxSumSquareSubMatrixBruteForce(int[][] matrix, int k) {
        if (!validMatrix(matrix)) {
            return new Result(-1, false);
        }

        int m = matrix.length;
        int n = matrix[0].length;
        if (!validK(m, n, k)) {
            return new Result(-1, false);
        }

        int maxSum = Integer.MIN_VALUE;

        for (int i = 0; i <= m - k; i++) {
            for (int j = 0; j <= n - k; j++) {
                int sum = 0;
                for (int r = i; r < i + k; r++) {
                    for (int c = j; c < j + k; c++) {
                        sum += matrix[r][c];
                    }
                }

                maxSum = Math.max(maxSum, sum);
            }
        }

        return new Result(maxSum, true);
    }

    static Result maxSumSquareSubMatrixPrefixSum(int[][] matrix, int k) {
        if (!validMatrix(matrix)) {
            return new Result(-1, false);
        }

        int m = matrix.length;
        int n = matrix[0].length;
        if (!validK(m, n, k)) {
            return new Result(-1, false);
        }

        int[][] pre = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                // Standard inclusion-exclusion: pre[i-1][j] and pre[i][j-1]
                // both already include the pre[i-1][j-1] corner, so it must
                // be SUBTRACTED once to avoid double-counting it.
                pre[i][j] = matrix[i - 1][j - 1] + pre[i - 1][j] + pre[i][j - 1] - pre[i - 1][j - 1];
            }
        }

        int maxSum = Integer.MIN_VALUE;
        for (int i = k; i <= m; i++) {
            for (int j = k; j <= n; j++) {
                int sum = pre[i][j] - pre[i - k][j] - pre[i][j - k] + pre[i - k][j - k];
                maxSum = Math.max(maxSum, sum);
            }
        }

        return new Result(maxSum, true);
    }

    static Result maxSumSquareSubMatrixSlidingWindow(int[][] matrix, int k) {
        if (!validMatrix(matrix)) {
            return new Result(-1, false);
        }

        int m = matrix.length;
        int n = matrix[0].length;
        if (!validK(m, n, k)) {
            return new Result(-1, false);
        }

        int[] colSum = new int[n];
        int maxSum = Integer.MIN_VALUE;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                colSum[j] += matrix[i][j];
                if (i >= k) {
                    colSum[j] -= matrix[i - k][j];
                }
            }
            if (i >= k - 1) {
                int windowSum = 0;
                for (int j = 0; j < n; j++) {
                    windowSum += colSum[j];
                    if (j >= k) {
                        windowSum -= colSum[j - k];
                    }
                    if (j >= k - 1) {
                        maxSum = Math.max(maxSum, windowSum);
                    }
                }
            }
        }

        return new Result(maxSum, true);
    }

    /* **********************************************************************
     * Test Harness Types
     * **********************************************************************/

    static class TestCase {

        final String id;
        final int[][] input;
        final int k;
        final Result expected;
        final String description;

        TestCase(
                String id,
                int[][] input,
                int k,
                Result expected,
                String description) {

            this.id = id;
            this.input = input;
            this.k = k;
            this.expected = expected;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {

        Result solve(int[][] matrix, int k);
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

        return a.maxSum() == b.maxSum() && a.valid() == b.valid();
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

                Result actual = method.solve(test.input, test.k);

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
                            "  input     = %s, k=%d%n",
                            formatMatrix(test.input),
                            test.k);

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
                        "  input     = %s, k=%d%n",
                        formatMatrix(test.input),
                        test.k);

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
                "Randomised Cross Checks (Brute Force vs Prefix Sum vs Sliding Window)");
        System.out.println(
                "======================================================");

        Random rng = new Random(20260908L);

        for (int i = 1; i <= iterations; i++) {

            // Kept modest (up to 10x10): brute force is O(m n k^2).
            // Non-square shapes deliberately included, and negative
            // values included to keep exercising the sign/init fixes.
            int[][] matrix = randomMatrix(rng, 10, 10, -15, 15);
            int m = matrix.length;
            int n = matrix[0].length;
            int k = rng.nextInt(Math.min(m, n)) + 1;

            Result brute = maxSumSquareSubMatrixBruteForce(matrix, k);
            Result prefix = maxSumSquareSubMatrixPrefixSum(matrix, k);
            Result sliding = maxSumSquareSubMatrixSlidingWindow(matrix, k);

            if (!resultsEqual(brute, prefix) || !resultsEqual(brute, sliding)) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "matrix  = " + formatMatrix(matrix) + ", k=" + k);

                System.out.println(
                        "brute   = " + brute);

                System.out.println(
                        "prefix  = " + prefix);

                System.out.println(
                        "sliding = " + sliding);

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
                2,
                new Result(54, true),
                "4x4 matrix, k=2: best block is the bottom-right 2x2"));

        tests.add(new TestCase(
                "B2",
                new int[][]{
                        {1, 2, 3, 4},
                        {5, 6, 7, 8},
                        {9, 10, 11, 12},
                        {13, 14, 15, 16}
                },
                1,
                new Result(16, true),
                "k=1: just the largest single cell"));

        /*
         * ============================================================
         * k Equals a Full Matrix Dimension
         * (guards the brute force's original "i < m - k" off-by-one,
         * which left the outer loop empty entirely whenever m == k)
         * ============================================================
         */

        tests.add(new TestCase(
                "R1",
                new int[][]{
                        {1, 2, 3, 4},
                        {5, 6, 7, 8},
                        {9, 10, 11, 12},
                        {13, 14, 15, 16}
                },
                4,
                new Result(136, true),
                "k equals m and n: the only valid block is the whole matrix"));

        /*
         * ============================================================
         * All-Negative Matrix
         * (guards the prefix-sum method's original "maxSum = 0" init,
         * which made 0 look like a valid answer even when every real
         * submatrix sum is negative)
         * ============================================================
         */

        tests.add(new TestCase(
                "R2",
                new int[][]{
                        {-1, -2, -3},
                        {-4, -5, -6},
                        {-7, -8, -9}
                },
                1,
                new Result(-1, true),
                "all-negative matrix, k=1: best is the least-negative single cell"));

        tests.add(new TestCase(
                "R3",
                new int[][]{
                        {-1, -2, -3},
                        {-4, -5, -6},
                        {-7, -8, -9}
                },
                2,
                new Result(-12, true),
                "all-negative matrix, k=2"));

        /*
         * ============================================================
         * Non-Square Regression
         * (guards the sliding-window method's original "new int[m]"
         * colSum sizing, invisible on square matrices)
         * ============================================================
         */

        tests.add(new TestCase(
                "R4",
                new int[][]{
                        {1, 2, 3, 4, 5},
                        {6, 7, 8, 9, 10},
                        {11, 12, 13, 14, 15}
                },
                2,
                new Result(48, true),
                "3x5 non-square, more columns than rows"));

        tests.add(new TestCase(
                "R5",
                new int[][]{
                        {1, 2, 3, 4, 5},
                        {6, 7, 8, 9, 10},
                        {11, 12, 13, 14, 15}
                },
                3,
                new Result(81, true),
                "3x5 non-square, k equals the smaller dimension"));

        /*
         * ============================================================
         * Added Robustness: Invalid k
         * ============================================================
         */

        tests.add(new TestCase(
                "V1",
                new int[][]{{1, 1}, {1, 1}},
                0,
                new Result(-1, false),
                "k = 0 is not a valid submatrix size"));

        tests.add(new TestCase(
                "V2",
                new int[][]{{1, 1}, {1, 1}},
                3,
                new Result(-1, false),
                "k larger than either matrix dimension"));

        tests.add(new TestCase(
                "V3",
                new int[][]{{1, 1}, {1, 1}},
                -1,
                new Result(-1, false),
                "negative k"));

        /*
         * ============================================================
         * Edge Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "E1",
                null,
                1,
                new Result(-1, false),
                "null matrix"));

        tests.add(new TestCase(
                "E2",
                new int[][]{},
                1,
                new Result(-1, false),
                "empty matrix (zero rows)"));

        tests.add(new TestCase(
                "E3",
                new int[][]{
                        {},
                        {},
                        {}
                },
                1,
                new Result(-1, false),
                "matrix with zero columns"));

        System.out.println(
                "############################################################");
        System.out.println(
                "###################  MAX SUM K x K SUBMATRIX  ##############");
        System.out.println(
                "############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "Brute Force",
                        MaxSumSquareSubMatrix::maxSumSquareSubMatrixBruteForce),

                new MethodCase(
                        "Prefix Sum",
                        MaxSumSquareSubMatrix::maxSumSquareSubMatrixPrefixSum),

                new MethodCase(
                        "Sliding Window",
                        MaxSumSquareSubMatrix::maxSumSquareSubMatrixSlidingWindow)
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
