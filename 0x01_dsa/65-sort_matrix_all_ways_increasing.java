import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Sort Matrix in All Ways Increasing Order.
 *
 * Problem:
 * Given an m x n integer matrix, rearrange its elements such that:
 *
 * - Every row is sorted in non-decreasing order (left to right).
 * - Every column is sorted in non-decreasing order (top to bottom).
 * - If the matrix is square, both main and anti-diagonals are also
 *   non-decreasing.
 *
 * Standard approach:
 *
 * 1. Flatten the matrix into a 1D array.
 * 2. Sort the 1D array.
 * 3. Refill the matrix row by row from the sorted array.
 *
 * This yields a matrix satisfying the above properties.
 *
 * Implementations:
 *
 * 1. Brute Force (Oracle)
 *      Flatten → sort → refill, written in a slightly different style.
 *
 * 2. Refactored Implementation
 *      Same algorithm, cleaner structure and a record-based return type.
 *
 * The brute-force implementation is retained as a correctness oracle
 * for deterministic and randomised testing.
 */
public class SortMatrixAllWaysIncreasingOrderTestHarness {

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

    static record Result(int[][] result, boolean sorted) {}

    /* **********************************************************************
     * Algorithm Implementations
     * **********************************************************************/

    /**
     * Brute Force Oracle.
     *
     * Flatten → sort → refill, written in a slightly different style
     * to act as an independent oracle.
     *
     * Time: O(m*n log(m*n))
     */
    static Result sortMatrixAllWaysBruteForce(int[][] matrix) {

        if (!validMatrix(matrix)) {
            return new Result(null, false);
        }

        int m = matrix.length;
        int n = matrix[0].length;

        int[] flat = new int[m * n];
        int idx = 0;

        for (int[] row : matrix) {
            for (int val : row) {
                flat[idx++] = val;
            }
        }

        Arrays.sort(flat);

        int[][] result = new int[m][n];
        idx = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = flat[idx++];
            }
        }

        return new Result(result, true);
    }

    /**
     * Refactored Implementation.
     *
     * Same algorithm: flatten → sort → refill.
     *
     * Time: O(m*n log(m*n))
     */
    static Result sortMatrixAllWaysIncreasingOrder(int[][] matrix) {

        if (!validMatrix(matrix)) {
            return new Result(null, false);
        }

        int m = matrix.length;
        int n = matrix[0].length;

        int[] flat = new int[m * n];
        int k = 0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                flat[k++] = matrix[i][j];
            }
        }

        Arrays.sort(flat);

        int[][] result = new int[m][n];
        k = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                result[i][j] = flat[k++];
            }
        }

        return new Result(result, true);
    }

    /* **********************************************************************
     * Verification Helpers
     * **********************************************************************/

    /**
     * Check that all rows are non-decreasing.
     */
    static boolean checkRowsSorted(int[][] matrix) {

        if (!validMatrix(matrix)) {
            return false;
        }

        int m = matrix.length;
        int n = matrix[0].length;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n - 1; j++) {
                if (matrix[i][j] > matrix[i][j + 1]) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Check that all columns are non-decreasing.
     */
    static boolean checkColsSorted(int[][] matrix) {

        if (!validMatrix(matrix)) {
            return false;
        }

        int m = matrix.length;
        int n = matrix[0].length;

        for (int j = 0; j < n; j++) {
            for (int i = 0; i < m - 1; i++) {
                if (matrix[i][j] > matrix[i + 1][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Check that main diagonal is non-decreasing (if square).
     */
    static boolean checkMainDiagonalSorted(int[][] matrix) {

        if (!validMatrix(matrix)) {
            return false;
        }

        int m = matrix.length;
        int n = matrix[0].length;

        if (m != n) {
            return true; // Not applicable for non-square matrices
        }

        for (int i = 0; i < n - 1; i++) {
            if (matrix[i][i] > matrix[i + 1][i + 1]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check that anti-diagonal is non-decreasing (if square).
     */
    static boolean checkAntiDiagonalSorted(int[][] matrix) {

        if (!validMatrix(matrix)) {
            return false;
        }

        int m = matrix.length;
        int n = matrix[0].length;

        if (m != n) {
            return true; // Not applicable for non-square matrices
        }

        for (int i = 0; i < n - 1; i++) {
            int j1 = n - 1 - i;
            int j2 = n - 1 - (i + 1);
            if (matrix[i][j1] > matrix[i + 1][j2]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check that the matrix satisfies all "all-ways increasing" properties.
     */
    static boolean checkAllWaysSorted(int[][] matrix) {

        return checkRowsSorted(matrix)
                && checkColsSorted(matrix)
                && checkMainDiagonalSorted(matrix)
                && checkAntiDiagonalSorted(matrix);
    }

    /* **********************************************************************
     * Test Harness Types
     * **********************************************************************/

    static class TestCase {

        final String id;
        final int[][] input;
        final boolean expectSorted;
        final String description;

        TestCase(
                String id,
                int[][] input,
                boolean expectSorted,
                String description) {

            this.id = id;
            this.input = input;
            this.expectSorted = expectSorted;
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

    static boolean matricesEqual(int[][] a, int[][] b) {

        if (a == null && b == null) {
            return true;
        }

        if (a == null || b == null) {
            return false;
        }

        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if (!Arrays.equals(a[i], b[i])) {
                return false;
            }
        }

        return true;
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

                Result res = method.solve(cloneMatrix(test.input));

                boolean ok = true;

                if (test.expectSorted) {

                    if (!res.sorted() || res.result() == null) {
                        ok = false;
                    } else if (!checkAllWaysSorted(res.result())) {
                        ok = false;
                    }

                } else {

                    // For invalid input, we expect sorted == false and result == null
                    if (res.sorted() || res.result() != null) {
                        ok = false;
                    }
                }

                if (ok) {

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
                            "  input          = %s%n",
                            formatMatrix(test.input));

                    System.out.printf(
                            "  result.sorted  = %s%n",
                            res.sorted());

                    System.out.printf(
                            "  result.matrix  = %s%n",
                            formatMatrix(res.result()));
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

        Random rng = new Random(987654321L);

        for (int i = 1; i <= iterations; i++) {

            int[][] matrix = randomMatrix(
                    rng,
                    5,
                    5,
                    -10,
                    20);

            if (!validMatrix(matrix)) {
                continue;
            }

            Result brute =
                    sortMatrixAllWaysBruteForce(
                            cloneMatrix(matrix));

            Result refactored =
                    sortMatrixAllWaysIncreasingOrder(
                            cloneMatrix(matrix));

            if (!matricesEqual(brute.result(), refactored.result())
                    || brute.sorted() != refactored.sorted()) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "matrix      = " + formatMatrix(matrix));

                System.out.println(
                        "brute       = " + formatMatrix(brute.result()));

                System.out.println(
                        "refactored  = " + formatMatrix(refactored.result()));

                return;
            }

            // Also verify that the result satisfies all-ways sorted property
            if (!checkAllWaysSorted(brute.result())) {

                System.out.println(
                        "Randomised test FAILED (result not all-ways sorted)");

                System.out.println(
                        "matrix      = " + formatMatrix(matrix));

                System.out.println(
                        "result      = " + formatMatrix(brute.result()));

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
         * Classic Square Matrices
         * ============================================================
         */

        tests.add(new TestCase(
                "C1",
                new int[][]{
                        {10, 20, 30},
                        {40, 50, 60},
                        {70, 80, 90}
                },
                true,
                "3x3 already sorted"));

        tests.add(new TestCase(
                "C2",
                new int[][]{
                        {9, 5, 1},
                        {8, 4, 2},
                        {7, 6, 3}
                },
                true,
                "3x3 reverse-sorted rows"));

        tests.add(new TestCase(
                "C3",
                new int[][]{
                        {1, 2, 3},
                        {4, 5, 6},
                        {7, 8, 9}
                },
                true,
                "3x3 natural order"));

        /*
         * ============================================================
         * Rectangular Matrices
         * ============================================================
         */

        tests.add(new TestCase(
                "R1",
                new int[][]{
                        {10, 1, 20},
                        {3, 30, 2}
                },
                true,
                "2x3 rectangular"));

        tests.add(new TestCase(
                "R2",
                new int[][]{
                        {5, 1},
                        {3, 2},
                        {4, 6}
                },
                true,
                "3x2 rectangular"));

        /*
         * ============================================================
         * Small Matrices
         * ============================================================
         */

        tests.add(new TestCase(
                "A1",
                new int[][]{
                        {2, 1},
                        {4, 3}
                },
                true,
                "2x2 unsorted"));

        tests.add(new TestCase(
                "A2",
                new int[][]{
                        {1, 2},
                        {3, 4}
                },
                true,
                "2x2 already sorted"));

        tests.add(new TestCase(
                "A3",
                new int[][]{
                        {5}
                },
                true,
                "1x1 matrix"));

        /*
         * ============================================================
         * Negative / Mixed Values
         * ============================================================
         */

        tests.add(new TestCase(
                "M1",
                new int[][]{
                        {-1, -3, 2},
                        {0, 4, -2}
                },
                true,
                "Mixed negative and positive"));

        tests.add(new TestCase(
                "M2",
                new int[][]{
                        {0, 0, 0},
                        {0, 0, 0}
                },
                true,
                "All zeros"));

        /*
         * ============================================================
         * Edge Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "E1",
                null,
                false,
                "Null matrix"));

        tests.add(new TestCase(
                "E2",
                new int[][]{},
                false,
                "Empty matrix (zero rows)"));

        tests.add(new TestCase(
                "E3",
                new int[][]{
                        {},
                        {},
                        {}
                },
                false,
                "Matrix with zero columns"));

        System.out.println(
                "############################################################");
        System.out.println(
                "######## SORT MATRIX ALL WAYS INCREASING ORDER ############");
        System.out.println(
                "############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "Brute Force (Oracle)",
                        SortMatrixAllWaysIncreasingOrderTestHarness::sortMatrixAllWaysBruteForce),

                new MethodCase(
                        "Refactored Implementation O(m*n log(m*n))",
                        SortMatrixAllWaysIncreasingOrderTestHarness::sortMatrixAllWaysIncreasingOrder)
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
