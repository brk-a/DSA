import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Size of the Largest Plus/Cross of 1s in a Binary Matrix.
 *
 * Problem:
 * Given a binary matrix, find the size (total cell count) of the largest
 * "plus" shape of 1s: a centre cell plus an equal-length arm of 1s
 * extending up, down, left, and right from it.
 *
 * Notes:
 * - If the matrix is invalid (null / empty / zero-width first row),
 *   Result.valid() == false.
 * - For a centre with arm length L in every direction, the total cell
 *   count is 4L + 1 (the centre, plus L cells in each of 4 directions).
 *   Both methods express this as 4 * arm - 3, where "arm" is (L + 1) -
 *   i.e. an INCLUSIVE run length that counts the centre cell itself, not
 *   the exclusive extension length L. The two are algebraically the same
 *   formula; see the arm/L relationship documented on each method.
 *
 * Implementations:
 *
 * 1. Brute Force
 *      For each 1-cell, grow "arm" outward step by step in all 4
 *      directions simultaneously until any direction runs out of bounds
 *      or hits a 0.
 *      Time: O(m * n * min(m, n))   Space: O(1) extra.
 *
 * 2. Dynamic Programming
 *      Precompute, for every cell, the inclusive run of consecutive 1s
 *      extending left, right, up, and down. The largest cross centred at
 *      a cell is bounded by the SHORTEST of those four runs.
 *      Time: O(m * n)   Space: O(m * n).
 *
 * Both are cross-checked against each other for both fixed and randomised
 * test matrices, following the same test-harness shape used for
 * SumOfDiagonalsMatrixTestHarness (TestCase / Algorithm / MethodCase /
 * runTests / randomised cross-checks).
 */
public class SizeOfLargestCrossBinaryMatrix {

    static record Result(int size, boolean valid) {}

    /* **********************************************************************
     * Validation
     * **********************************************************************/

    static boolean validMatrix(int[][] matrix) {
        return matrix != null && matrix.length > 0 && matrix[0] != null && matrix[0].length > 0;
    }

    /* **********************************************************************
     * Algorithm Implementations
     * **********************************************************************/

    static Result sizeOfLargestCrossBinaryMatrixBruteForce(int[][] matrix) {
        if (!validMatrix(matrix)) {
            return new Result(-1, false);
        }

        int m = matrix.length;
        int n = matrix[0].length;
        int size = 0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 0) {
                    continue;
                }

                // arm starts at 1 and grows to (max valid extension L) + 1,
                // since the loop keeps succeeding through L and fails at L+1.
                int arm = 1;
                while (i - arm >= 0 && i + arm < m && j - arm >= 0 && j + arm < n
                        && matrix[i - arm][j] == 1 && matrix[i + arm][j] == 1
                        && matrix[i][j - arm] == 1 && matrix[i][j + arm] == 1) {
                    arm++;
                }

                size = Math.max(size, 4 * arm - 3);
            }
        }

        return new Result(size, true);
    }

    static Result sizeOfLargestCrossBinaryMatrixDynamicProgramming(int[][] matrix) {
        if (!validMatrix(matrix)) {
            return new Result(-1, false);
        }

        int m = matrix.length;
        int n = matrix[0].length;
        int[][] left = new int[m][n];
        int[][] right = new int[m][n];
        int[][] top = new int[m][n];
        int[][] bottom = new int[m][n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 1) {
                    left[i][j] = 1 + (j > 0 ? left[i][j - 1] : 0);
                    top[i][j] = 1 + (i > 0 ? top[i - 1][j] : 0);
                }
            }
        }
        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 0; j--) {
                if (matrix[i][j] == 1) {
                    right[i][j] = 1 + (j + 1 < n ? right[i][j + 1] : 0);
                    bottom[i][j] = 1 + (i + 1 < m ? bottom[i + 1][j] : 0);
                }
            }
        }

        int size = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                int arm = Math.min(Math.min(left[i][j], right[i][j]), Math.min(top[i][j], bottom[i][j]));
                size = Math.max(size, 4 * arm - 3);
            }
        }

        return new Result(size, true);
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

        return a.size() == b.size() && a.valid() == b.valid();
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
                "Randomised Cross Checks (Brute Force vs Dynamic Programming)");
        System.out.println(
                "======================================================");

        Random rng = new Random(20260907L);

        for (int i = 1; i <= iterations; i++) {

            // Includes non-square shapes deliberately to exercise the DP
            // method's sizing and boundary-condition bugs.
            int[][] matrix = randomMatrix(rng, 12, 12, 0.7);

            Result brute = sizeOfLargestCrossBinaryMatrixBruteForce(matrix);
            Result dp = sizeOfLargestCrossBinaryMatrixDynamicProgramming(matrix);

            if (!resultsEqual(brute, dp)) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "matrix = " + formatMatrix(matrix));

                System.out.println(
                        "brute  = " + brute);

                System.out.println(
                        "dp     = " + dp);

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
                        {1, 1, 1},
                        {1, 1, 1},
                        {1, 1, 1}
                },
                new Result(5, true),
                "3x3 all ones: the centre cross, arm length 1"));

        tests.add(new TestCase(
                "B2",
                new int[][]{
                        {0, 0, 1, 0, 0},
                        {0, 0, 1, 0, 0},
                        {1, 1, 1, 1, 1},
                        {0, 0, 1, 0, 0},
                        {0, 0, 1, 0, 0}
                },
                new Result(9, true),
                "explicit 5x5 cross shape, arm length 2"));

        tests.add(new TestCase(
                "B3",
                new int[][]{
                        {0, 0, 0},
                        {0, 0, 0},
                        {0, 0, 0}
                },
                new Result(0, true),
                "all zeros"));

        tests.add(new TestCase(
                "B4",
                new int[][]{{1}},
                new Result(1, true),
                "1x1 matrix, a lone 1 is a valid (trivial) cross"));

        tests.add(new TestCase(
                "B5",
                new int[][]{{0}},
                new Result(0, true),
                "1x1 matrix, a lone 0"));

        /*
         * ============================================================
         * Non-Square Regression
         * (guards the DP method's array-sizing bug, "new int[n][m]"
         * instead of "new int[m][n]", and its two boundary-condition
         * bugs in the right/bottom computation - all invisible on
         * square matrices)
         * ============================================================
         */

        tests.add(new TestCase(
                "R1",
                new int[][]{
                        {1, 1, 1, 1, 1},
                        {1, 1, 1, 1, 1},
                        {1, 1, 1, 1, 1}
                },
                new Result(5, true),
                "3x5 all ones, more columns than rows"));

        tests.add(new TestCase(
                "R2",
                new int[][]{
                        {1, 1},
                        {1, 1},
                        {1, 1},
                        {1, 1},
                        {1, 1}
                },
                new Result(1, true),
                "5x2 all ones: too narrow for any arm extension"));

        tests.add(new TestCase(
                "R3",
                new int[][]{
                        {1, 1, 0, 1, 1, 1, 1},
                        {1, 1, 1, 1, 1, 1, 1},
                        {0, 1, 1, 1, 1, 1, 0}
                },
                new Result(5, true),
                "3x7 non-square with mixed 0s and 1s"));

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
                "############  LARGEST CROSS IN A BINARY MATRIX  ############");
        System.out.println(
                "############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "Brute Force",
                        SizeOfLargestCrossBinaryMatrix::sizeOfLargestCrossBinaryMatrixBruteForce),

                new MethodCase(
                        "Dynamic Programming",
                        SizeOfLargestCrossBinaryMatrix::sizeOfLargestCrossBinaryMatrixDynamicProgramming)
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
