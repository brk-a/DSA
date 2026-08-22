import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Maximum Sum Rectangle in a 2D Matrix.
 *
 * Problem:
 * Given an m x n matrix of integers (which may be negative), find the
 * maximum possible sum over all non-empty rectangular sub-matrices.
 *
 * Notes:
 * - If the matrix is invalid (null / empty / zero-width first row),
 *   Result.valid() == false.
 * - Every cell is itself a valid 1x1 rectangle, so on an all-negative
 *   matrix the answer is the single largest (least negative) cell.
 *
 * Implementations:
 *
 * 1. Brute Force
 *      Enumerate every (up, left, down, right) rectangle and sum its cells
 *      directly.
 *      Time: O(m^2 * n^2 * m * n)   Space: O(1) extra.
 *
 * 2. 2D Prefix Sum
 *      Build a prefix-sum matrix so any rectangle's sum is an O(1)
 *      inclusion-exclusion lookup; still enumerates every rectangle.
 *      Time: O(m^2 * n^2)   Space: O(m * n).
 *
 * 3. Kadane's Algorithm (column compression)
 *      Fix a pair of columns (left, right), collapse every row between
 *      them into a single running sum, then run 1D Kadane's algorithm on
 *      that column to find the best (up, down) range for this column pair.
 *      Time: O(n^2 * m)   Space: O(m).
 *
 * All three are cross-checked against each other for both fixed and
 * randomised test matrices, following the same test-harness shape used for
 * SumOfDiagonalsMatrixTestHarness (TestCase / Algorithm / MethodCase /
 * runTests / randomised cross-checks).
 */
public class MaxSumRectMatrix {

    static record Result(int sum, boolean valid) {}

    /* **********************************************************************
     * Validation
     * **********************************************************************/

    static boolean validMatrix(int[][] matrix) {
        return matrix != null && matrix.length > 0 && matrix[0] != null && matrix[0].length > 0;
    }

    /* **********************************************************************
     * Algorithm Implementations
     * **********************************************************************/

    static Result maxSumRectMatrixBruteForce(int[][] matrix) {
        if (!validMatrix(matrix)) {
            return new Result(-1, false);
        }

        int m = matrix.length;
        int n = matrix[0].length;
        int maxSum = Integer.MIN_VALUE;

        for (int up = 0; up < m; up++) {
            for (int left = 0; left < n; left++) {
                for (int down = up; down < m; down++) {
                    for (int right = left; right < n; right++) {
                        int sum = 0;
                        for (int i = up; i <= down; i++) {
                            for (int j = left; j <= right; j++) {
                                sum += matrix[i][j];
                            }
                        }

                        if (sum > maxSum) {
                            maxSum = sum;
                        }
                    }
                }
            }
        }

        return new Result(maxSum, true);
    }

    static Result maxSumRectMatrixPrefixSum(int[][] matrix) {
        if (!validMatrix(matrix)) {
            return new Result(-1, false);
        }

        int m = matrix.length;
        int n = matrix[0].length;
        int[][] prefix = new int[m][n];

        // Pass 1: row-wise running sums. prefix[i][j] = sum of matrix[i][0..j].
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                prefix[i][j] = matrix[i][j];
                if (j - 1 >= 0) {
                    prefix[i][j] += prefix[i][j - 1];
                }
            }
        }

        // Pass 2: accumulate down each column on top of the row sums, so
        // prefix[i][j] becomes the sum of the whole top-left block matrix[0..i][0..j].
        for (int j = 0; j < n; j++) {
            for (int i = 0; i < m; i++) {
                if (i - 1 >= 0) {
                    prefix[i][j] += prefix[i - 1][j];
                }
            }
        }

        int maxSum = Integer.MIN_VALUE;

        for (int up = 0; up < m; up++) {
            for (int left = 0; left < n; left++) {
                for (int down = up; down < m; down++) {
                    for (int right = left; right < n; right++) {
                        int sum = findSum(up, left, down, right, prefix);
                        if (sum > maxSum) {
                            maxSum = sum;
                        }
                    }
                }
            }
        }

        return new Result(maxSum, true);
    }

    static Result maxSumRectMatrixKadaneAlgo(int[][] matrix) {
        if (!validMatrix(matrix)) {
            return new Result(-1, false);
        }

        int m = matrix.length;
        int n = matrix[0].length;
        int maxSum = Integer.MIN_VALUE;
        int[] tmp = new int[m];

        for (int left = 0; left < n; left++) {
            for (int i = 0; i < m; i++) {
                tmp[i] = 0;
            }
            for (int right = left; right < n; right++) {
                for (int row = 0; row < m; row++) {
                    tmp[row] += matrix[row][right];
                }
                int sum = kadane(tmp);
                maxSum = Math.max(maxSum, sum);
            }
        }

        return new Result(maxSum, true);
    }

    /* **********************************************************************
     * Helpers
     * **********************************************************************/

    /** Inclusion-exclusion lookup: sum of matrix[up..down][left..right]. */
    static int findSum(int up, int left, int down, int right, int[][] prefix) {
        int sum = prefix[down][right];

        if (left - 1 >= 0) {
            sum -= prefix[down][left - 1];
        }
        if (up - 1 >= 0) {
            sum -= prefix[up - 1][right];
        }
        if (up - 1 >= 0 && left - 1 >= 0) {
            sum += prefix[up - 1][left - 1];
        }

        return sum;
    }

    /** Classic 1D Kadane's algorithm: max sum of any non-empty contiguous run. */
    static int kadane(int[] tmp) {
        int rows = tmp.length;
        int currSum = 0;
        int maxSum = Integer.MIN_VALUE;

        for (int i = 0; i < rows; i++) {
            currSum += tmp[i];
            maxSum = Math.max(maxSum, currSum);
            currSum = Math.max(currSum, 0);
        }

        return maxSum;
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

        return a.sum() == b.sum() && a.valid() == b.valid();
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
                "Randomised Cross Checks (Brute vs Prefix Sum vs Kadane)");
        System.out.println(
                "======================================================");

        Random rng = new Random(20260821L);

        for (int i = 1; i <= iterations; i++) {

            // Kept deliberately small (up to 6x6): brute force is O(m^3 n^3)
            // and this still exercises square and non-square shapes, all
            // signs of skew (mostly negative / mostly positive), and every
            // combination of up==0 / left==0 that the findSum fix guards.
            int[][] matrix = randomMatrix(rng, 6, 6, -9, 9);

            Result brute = maxSumRectMatrixBruteForce(cloneMatrix(matrix));
            Result prefixSum = maxSumRectMatrixPrefixSum(cloneMatrix(matrix));
            Result kadane = maxSumRectMatrixKadaneAlgo(cloneMatrix(matrix));

            if (!resultsEqual(brute, prefixSum) || !resultsEqual(brute, kadane)) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "matrix    = " + formatMatrix(matrix));

                System.out.println(
                        "brute     = " + brute);

                System.out.println(
                        "prefixSum = " + prefixSum);

                System.out.println(
                        "kadane    = " + kadane);

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
                        {1, 2},
                        {3, 4}
                },
                new Result(10, true),
                "2x2 all positive, best rectangle is the whole matrix"));

        tests.add(new TestCase(
                "B2",
                new int[][]{
                        {-1, -2},
                        {-3, -4}
                },
                new Result(-1, true),
                "2x2 all negative, best rectangle is the single least-negative cell"));

        tests.add(new TestCase(
                "B3",
                new int[][]{
                        {0, 0},
                        {0, 0}
                },
                new Result(0, true),
                "all zeros, best sum is 0"));

        /*
         * ============================================================
         * Classic Worked Example
         * (well-known "maximum sum rectangle" textbook matrix)
         * ============================================================
         */

        tests.add(new TestCase(
                "W1",
                new int[][]{
                        {1, 2, -1, -4, -20},
                        {-8, -3, 4, 2, 1},
                        {3, 8, 10, 1, 3},
                        {-4, -1, 1, 7, -6}
                },
                new Result(29, true),
                "classic 4x5 example, best rectangle sums to 29"));

        /*
         * ============================================================
         * Non-Square Matrices, Up==0 / Left==0 Boundary Coverage
         * (guards the findSum "left-1 twice" bug, which previously
         * crashed whenever up==0 with left>=1, and silently overcounted
         * whenever up>=1 with left==0)
         * ============================================================
         */

        tests.add(new TestCase(
                "N1",
                new int[][]{
                        {5, -1, 5, -1, 5}
                },
                new Result(13, true),
                "1x5 single row (up always 0), left ranges across all columns"));

        tests.add(new TestCase(
                "N2",
                new int[][]{
                        {5},
                        {-1},
                        {5},
                        {-1},
                        {5}
                },
                new Result(13, true),
                "5x1 single column (left always 0), up ranges across all rows"));

        tests.add(new TestCase(
                "N3",
                new int[][]{
                        {-5, 1, 1},
                        {1, -5, 1},
                        {1, 1, -5}
                },
                new Result(2, true),
                "3x3, best rectangles start at up=0 and separately at left=0"));

        tests.add(new TestCase(
                "N4",
                new int[][]{
                        {2, -1, 2, -1, 2, -1},
                        {-1, 2, -1, 2, -1, 2}
                },
                new Result(6, true),
                "2x6 rectangle, exercises every up==0/left==0 combination"));

        /*
         * ============================================================
         * Trivial Sizes
         * ============================================================
         */

        tests.add(new TestCase(
                "M1",
                new int[][]{{7}},
                new Result(7, true),
                "1x1 matrix, positive"));

        tests.add(new TestCase(
                "M2",
                new int[][]{{-7}},
                new Result(-7, true),
                "1x1 matrix, negative"));

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
                "###################  MAX SUM RECTANGLE  ###################");
        System.out.println(
                "############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "Brute Force",
                        MaxSumRectMatrix::maxSumRectMatrixBruteForce),

                new MethodCase(
                        "2D Prefix Sum",
                        MaxSumRectMatrix::maxSumRectMatrixPrefixSum),

                new MethodCase(
                        "Kadane's Algorithm (column compression)",
                        MaxSumRectMatrix::maxSumRectMatrixKadaneAlgo)
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
