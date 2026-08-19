import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Search in a Row- and Column-Sorted Matrix.
 *
 * Problem:
 * Given an m x n integer matrix where:
 *  - Each row is sorted in non-decreasing order.
 *  - Each column is sorted in non-decreasing order.
 * Determine whether a target value exists in the matrix.
 *
 * Implementations:
 *
 * 1. Brute Force (Oracle)
 *      Double loop over all cells.
 *      Time: O(m * n)
 *
 * 2. Binary Search Per Row
 *      For each row, run binary search.
 *      Time: O(m * log n)
 *
 * 3. Row-Column Elimination (Staircase Search)
 *      Start at top-right corner; move down or left.
 *      Time: O(m + n)
 *
 * The brute-force implementation is retained as a correctness oracle
 * for deterministic and randomised testing.
 */
public class SearchRowColSortedMatrix {

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
     * Algorithm Implementations
     * **********************************************************************/

    /**
     * Brute Force Oracle.
     *
     * Double loop over all cells.
     *
     * Time: O(m * n)
     */
    static boolean searchBruteForce(int[][] matrix, int target) {
        if (!validMatrix(matrix)) {
            return false;
        }

        int m = matrix.length;
        int n = matrix[0].length;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == target) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Binary Search Per Row.
     *
     * For each row, run binary search.
     *
     * Time: O(m * log n)
     */
    static boolean searchBinarySearchPerRow(int[][] matrix, int target) {
        if (!validMatrix(matrix)) {
            return false;
        }

        int m = matrix.length;

        for (int i = 0; i < m; i++) {
            if (binarySearch(matrix[i], target)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Row-Column Elimination (Staircase Search).
     *
     * Start at top-right corner (0, n-1).
     * - If current == target: found.
     * - If target > current: move down (row++).
     * - If target < current: move left (col--).
     *
     * Time: O(m + n)
     */
    static boolean searchRowColElimination(int[][] matrix, int target) {
        if (!validMatrix(matrix)) {
            return false;
        }

        int m = matrix.length;
        int n = matrix[0].length;

        int row = 0;
        int col = n - 1; // top-right corner

        while (row < m && col >= 0) {
            int current = matrix[row][col];
            if (target == current) {
                return true;
            } else if (target > current) {
                row++; // eliminate this row
            } else {
                col--; // eliminate this column
            }
        }

        return false;
    }

    /**
     * Binary search helper for a sorted 1D array.
     *
     * Uses the safe midpoint formula and standard low <= high loop.
     */
    private static boolean binarySearch(int[] row, int target) {
        int low = 0;
        int high = row.length - 1;

        while (low <= high) {
            int mid = low + (high - low) / 2; // avoids overflow
            if (row[mid] == target) {
                return true;
            } else if (target > row[mid]) {
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }

        return false;
    }

    /* **********************************************************************
     * Test Harness Types
     * **********************************************************************/

    static class TestCase {

        final String id;
        final int[][] input;
        final int target;
        final boolean expected;
        final String description;

        TestCase(
                String id,
                int[][] input,
                int target,
                boolean expected,
                String description) {

            this.id = id;
            this.input = input;
            this.target = target;
            this.expected = expected;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {

        boolean solve(int[][] matrix, int target);
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

                boolean actual = method.solve(cloneMatrix(test.input), test.target);

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
                            formatMatrix(test.input));

                    System.out.printf(
                            "  target    = %d%n",
                            test.target);

                    System.out.printf(
                            "  expected  = %b%n",
                            test.expected);

                    System.out.printf(
                            "  actual    = %b%n",
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
                        "  target    = %d%n",
                        test.target);

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

    /**
     * Generate a random row-and-column sorted matrix.
     *
     * We generate values in non-decreasing order along rows and columns
     * by filling row by row and ensuring each new value is >= previous
     * in its row and column.
     */
    static int[][] randomRowColSortedMatrix(
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
                int minVal = minValue;

                if (i > 0) {
                    minVal = Math.max(minVal, matrix[i - 1][j]);
                }
                if (j > 0) {
                    minVal = Math.max(minVal, matrix[i][j - 1]);
                }

                int maxVal = Math.min(maxValue, Math.max(minVal, maxValue));
                if (maxVal < minVal) {
                    // Fallback if range collapses; just use minVal
                    matrix[i][j] = minVal;
                } else {
                    matrix[i][j] = minVal + rng.nextInt(maxVal - minVal + 1);
                }
            }
        }

        return matrix;
    }

    static void runRandomisedTests(int iterations) {

        System.out.println(
                "======================================================");
        System.out.println(
                "Randomised Cross Checks (Brute vs Optimised)");
        System.out.println(
                "======================================================");

        Random rng = new Random(987654321L);

        for (int i = 1; i <= iterations; i++) {

            int[][] matrix = randomRowColSortedMatrix(
                    rng,
                    6,
                    6,
                    -10,
                    20);

            if (!validMatrix(matrix)) {
                continue;
            }

            // Pick a random target from within the value range
            int minVal = matrix[0][0];
            int maxVal = matrix[matrix.length - 1][matrix[0].length - 1];
            int target = minVal + rng.nextInt(maxVal - minVal + 1);

            boolean brute = searchBruteForce(cloneMatrix(matrix), target);
            boolean binary = searchBinarySearchPerRow(cloneMatrix(matrix), target);
            boolean elim = searchRowColElimination(cloneMatrix(matrix), target);

            if (brute != binary || brute != elim) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "matrix      = " + formatMatrix(matrix));

                System.out.println(
                        "target      = " + target);

                System.out.println(
                        "brute       = " + brute);

                System.out.println(
                        "binary      = " + binary);

                System.out.println(
                        "elim        = " + elim);

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
                5,
                true,
                "3x3 square matrix, target present"));

        tests.add(new TestCase(
                "S2",
                new int[][]{
                        {1, 2, 3},
                        {4, 5, 6},
                        {7, 8, 9}
                },
                10,
                false,
                "3x3 square matrix, target absent"));

        tests.add(new TestCase(
                "S3",
                new int[][]{
                        {1, 2, 3, 4},
                        {5, 6, 7, 8},
                        {9, 10, 11, 12},
                        {13, 14, 15, 16}
                },
                13,
                true,
                "4x4 square matrix, target in first column"));

        tests.add(new TestCase(
                "S4",
                new int[][]{
                        {10}
                },
                10,
                true,
                "1x1 matrix, target present"));

        tests.add(new TestCase(
                "S5",
                new int[][]{
                        {10}
                },
                5,
                false,
                "1x1 matrix, target absent"));

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
                7,
                true,
                "2x4 wide matrix, target present"));

        tests.add(new TestCase(
                "W2",
                new int[][]{
                        {1, 2, 3, 4},
                        {5, 6, 7, 8}
                },
                9,
                false,
                "2x4 wide matrix, target absent"));

        tests.add(new TestCase(
                "W3",
                new int[][]{
                        {1, 2, 3},
                        {4, 5, 6}
                },
                1,
                true,
                "2x3 wide matrix, target at top-left"));

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
                4,
                true,
                "3x2 tall matrix, target present"));

        tests.add(new TestCase(
                "T2",
                new int[][]{
                        {1, 2},
                        {3, 4},
                        {5, 6}
                },
                7,
                false,
                "3x2 tall matrix, target absent"));

        tests.add(new TestCase(
                "T3",
                new int[][]{
                        {1, 2, 3},
                        {4, 5, 6},
                        {7, 8, 9},
                        {10, 11, 12}
                },
                10,
                true,
                "4x3 tall matrix, target in last row"));

        /*
         * ============================================================
         * Negative / Mixed Values
         * ============================================================
         */

        tests.add(new TestCase(
                "M1",
                new int[][]{
                        {-9, -8, -7},
                        {-6, -5, -4},
                        {-3, -2, -1}
                },
                -5,
                true,
                "3x3 with negatives, target present"));

        tests.add(new TestCase(
                "M2",
                new int[][]{
                        {-9, -8, -7},
                        {-6, -5, -4},
                        {-3, -2, -1}
                },
                0,
                false,
                "3x3 with negatives, target absent"));

        tests.add(new TestCase(
                "M3",
                new int[][]{
                        {0, 0, 0},
                        {0, 0, 0},
                        {0, 0, 0}
                },
                0,
                true,
                "3x3 all zeros, target present"));

        /*
         * ============================================================
         * Edge Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "E1",
                null,
                5,
                false,
                "Null matrix"));

        tests.add(new TestCase(
                "E2",
                new int[][]{},
                5,
                false,
                "Empty matrix (zero rows)"));

        tests.add(new TestCase(
                "E3",
                new int[][]{
                        {},
                        {},
                        {}
                },
                5,
                false,
                "Matrix with zero columns"));

        System.out.println(
                "############################################################");
        System.out.println(
                "#### SEARCH IN ROW- AND COLUMN-SORTED MATRIX (INT[][]) #####");
        System.out.println(
                "############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "Brute Force (Oracle) O(m * n)",
                        SearchRowColSortedMatrix::searchBruteForce),

                new MethodCase(
                        "Binary Search Per Row O(m * log n)",
                        SearchRowColSortedMatrix::searchBinarySearchPerRow),

                new MethodCase(
                        "Row-Column Elimination O(m + n)",
                        SearchRowColSortedMatrix::searchRowColElimination)
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
