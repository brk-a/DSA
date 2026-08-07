import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Matrix Row With Maximum Number of 1s.
 *
 * Problem:
 * Given a binary matrix (elements are 0 or 1), find the row index
 * that contains the maximum number of 1s.
 *
 * Rules:
 * - If the matrix is invalid (null / empty), return -1.
 * - If no 1s exist in the entire matrix, return -1.
 * - If multiple rows tie for maximum 1s, return the smallest row index.
 *
 * Implementations:
 *
 * 1. Brute Force (Oracle)
 *      Count 1s in each row by scanning all elements.
 *      Works for any binary matrix (sorted or unsorted rows).
 *
 * 2. Binary Search per Row
 *      Assumes each row is sorted non-decreasingly (0s then 1s).
 *      Uses lowerBound to find first 1, then count = n - idx.
 *
 * 3. Top-Right Traversal (O(m + n))
 *      Assumes each row is sorted non-decreasingly.
 *      Starts at top-right corner and moves left/down.
 *
 * The brute-force implementation is retained as a correctness oracle
 * for deterministic and randomised testing.
 */
public class MatrixRowWithMaxOnesTestHarness {

    /* **********************************************************************
     * Validation Helpers
     * **********************************************************************/

    static boolean validateMatrix(int[][] matrix) {
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
     * Count 1s in each row by full scan.
     *
     * Time: O(m * n)
     * Works for any binary matrix (sorted or unsorted).
     */
    static int matrixRowWithMaxOnesBruteForce(int[][] matrix) {

        if (!validateMatrix(matrix)) {
            return -1;
        }

        int m = matrix.length;
        int n = matrix[0].length;

        int rowIdx = -1;
        int maxCount = 0;

        for (int i = 0; i < m; i++) {

            int count = 0;
            for (int j = 0; j < n; j++) {
                if (matrix[i][j] == 1) {
                    count++;
                }
            }

            if (count > maxCount) {
                maxCount = count;
                rowIdx = i;
            }
        }

        return rowIdx;
    }

    /**
     * Binary Search per Row.
     *
     * Assumes each row is sorted non-decreasingly (0s then 1s).
     *
     * Time: O(m * log n)
     */
    static int matrixRowWithMaxOnesBinarySearch(int[][] matrix) {

        if (!validateMatrix(matrix)) {
            return -1;
        }

        int m = matrix.length;
        int n = matrix[0].length;

        int rowIdx = -1;
        int maxCount = 0;

        for (int i = 0; i < m; i++) {

            int idx = lowerBound(matrix[i], 1);

            if (idx == -1) {
                // No 1s in this row
                continue;
            }

            int count = n - idx;

            if (count > maxCount) {
                maxCount = count;
                rowIdx = i;
            }
        }

        return rowIdx;
    }

    /**
     * Top-Right Traversal.
     *
     * Assumes each row is sorted non-decreasingly (0s then 1s).
     *
     * Time: O(m + n)
     */
    static int matrixRowWithMaxOnesTopRightToOutside(int[][] matrix) {

        if (!validateMatrix(matrix)) {
            return -1;
        }

        int m = matrix.length;
        int n = matrix[0].length;

        int row = 0;
        int col = n - 1;
        int maxRow = -1;

        while (row < m && col >= 0) {

            if (matrix[row][col] == 0) {
                row++;
            } else {
                maxRow = row;
                col--;
            }
        }

        return maxRow;
    }

    /* **********************************************************************
     * Helpers for Binary Search
     * **********************************************************************/

    /**
     * Returns index of first element >= target in a sorted array,
     * or -1 if no such element exists.
     */
    static int lowerBound(int[] row, int target) {

        int left = 0;
        int right = row.length;

        while (left < right) {

            int mid = left + (right - left) / 2;

            if (row[mid] >= target) {
                right = mid;
            } else {
                left = mid + 1;
            }
        }

        return left == row.length ? -1 : left;
    }

    /* **********************************************************************
     * Test Harness Types
     * **********************************************************************/

    static class TestCase {

        final String id;
        final int[][] input;
        final Integer expected;
        final String description;

        TestCase(
                String id,
                int[][] input,
                Integer expected,
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

    static boolean intsEqual(Integer a, Integer b) {

        if (a == null && b == null) {
            return true;
        }

        if (a == null || b == null) {
            return false;
        }

        return a.intValue() == b.intValue();
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

                int actual = method.solve(cloneMatrix(test.input));

                if (intsEqual(actual, test.expected)) {

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

    static int[][] randomSortedBinaryMatrix(
            Random rng,
            int maxRows,
            int maxCols) {

        int rows = rng.nextInt(maxRows) + 1;
        int cols = rng.nextInt(maxCols) + 1;

        int[][] matrix = new int[rows][cols];

        for (int i = 0; i < rows; i++) {

            // Generate sorted row: k zeros then (cols - k) ones
            int k = rng.nextInt(cols + 1); // number of zeros

            for (int j = 0; j < cols; j++) {
                matrix[i][j] = (j >= k) ? 1 : 0;
            }
        }

        return matrix;
    }

    static void runRandomisedTests(int iterations) {

        System.out.println(
                "======================================================");
        System.out.println(
                "Randomised Cross Checks (Sorted Rows)");
        System.out.println(
                "======================================================");

        Random rng = new Random(123456789L);

        for (int i = 1; i <= iterations; i++) {

            int[][] matrix = randomSortedBinaryMatrix(
                    rng,
                    6,
                    8);

            if (!validateMatrix(matrix)) {
                continue;
            }

            int brute =
                    matrixRowWithMaxOnesBruteForce(
                            cloneMatrix(matrix));

            int binary =
                    matrixRowWithMaxOnesBinarySearch(
                            cloneMatrix(matrix));

            int topRight =
                    matrixRowWithMaxOnesTopRightToOutside(
                            cloneMatrix(matrix));

            if (brute != binary || brute != topRight) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "matrix   = " + formatMatrix(matrix));

                System.out.println(
                        "brute    = " + brute);

                System.out.println(
                        "binary   = " + binary);

                System.out.println(
                        "topRight = " + topRight);

                return;
            }
        }

        System.out.printf(
                "All %d Randomised tests passed (sorted rows).%n%n",
                iterations);
    }

    /* **********************************************************************
     * Main
     * **********************************************************************/

    public static void main(String[] args) {

        List<TestCase> tests = new ArrayList<>();

        /*
         * ============================================================
         * Classic Examples
         * ============================================================
         */

        tests.add(new TestCase(
                "C1",
                new int[][]{
                        {0, 0, 0, 1},
                        {0, 0, 1, 1},
                        {0, 1, 1, 1},
                        {0, 0, 0, 0}
                },
                2,
                "Classic example: row 2 has most 1s"));

        tests.add(new TestCase(
                "C2",
                new int[][]{
                        {0, 1, 1},
                        {0, 0, 1},
                        {0, 0, 0}
                },
                0,
                "Row 0 has most 1s"));

        /*
         * ============================================================
         * All Zeros / All Ones
         * ============================================================
         */

        tests.add(new TestCase(
                "Z1",
                new int[][]{
                        {0, 0, 0},
                        {0, 0, 0}
                },
                -1,
                "All zeros: no 1s"));

        tests.add(new TestCase(
                "O1",
                new int[][]{
                        {1, 1, 1},
                        {1, 1, 1},
                        {1, 1, 1}
                },
                0,
                "All ones: first row wins tie"));

        /*
         * ============================================================
         * Single Row / Single Column
         * ============================================================
         */

        tests.add(new TestCase(
                "S1",
                new int[][]{
                        {0, 1, 1, 1}
                },
                0,
                "Single row with 1s"));

        tests.add(new TestCase(
                "S2",
                new int[][]{
                        {0},
                        {1},
                        {1},
                        {0}
                },
                1,
                "Single column: first row with 1 wins tie"));

        /*
         * ============================================================
         * Ties (First Row Wins)
         * ============================================================
         */

        tests.add(new TestCase(
                "T1",
                new int[][]{
                        {0, 1, 1},
                        {0, 1, 1},
                        {0, 0, 1}
                },
                0,
                "Tie between row 0 and 1: first wins"));

        tests.add(new TestCase(
                "T2",
                new int[][]{
                        {1, 1},
                        {1, 1},
                        {1, 1}
                },
                0,
                "All rows tied: first wins"));

        /*
         * ============================================================
         * Edge Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "E1",
                null,
                -1,
                "Null matrix"));

        tests.add(new TestCase(
                "E2",
                new int[][]{},
                -1,
                "Empty matrix (zero rows)"));

        tests.add(new TestCase(
                "E3",
                new int[][]{
                        {},
                        {},
                        {}
                },
                -1,
                "Matrix with zero columns"));

        System.out.println(
                "############################################################");
        System.out.println(
                "######## MATRIX ROW WITH MAX ONES (BINARY MATRIX) #########");
        System.out.println(
                "############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "Brute Force (Oracle)",
                        MatrixRowWithMaxOnesTestHarness::matrixRowWithMaxOnesBruteForce),

                new MethodCase(
                        "Binary Search per Row (Sorted Rows)",
                        MatrixRowWithMaxOnesTestHarness::matrixRowWithMaxOnesBinarySearch),

                new MethodCase(
                        "Top-Right Traversal O(m + n) (Sorted Rows)",
                        MatrixRowWithMaxOnesTestHarness::matrixRowWithMaxOnesTopRightToOutside)
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
