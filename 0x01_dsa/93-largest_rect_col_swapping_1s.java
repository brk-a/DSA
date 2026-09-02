import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * Largest Rectangle of 1s After Arbitrarily Swapping Columns.
 *
 * Problem:
 * Given a binary matrix, and permission to freely reorder its COLUMNS in
 * any order, find the area of the largest all-1s rectangle achievable.
 *
 * Key idea: for column j, let heights[i][j] be the number of consecutive
 * 1s ending at row i (a per-column running histogram, top to bottom).
 * Since columns can be freely rearranged, for each row i we can group any
 * k columns together - so the best rectangle "ending" at row i using k
 * columns has height equal to the k-th tallest of that row's heights
 * (the shortest among the k chosen), and area = height * k. Trying every
 * row and every k gives the answer.
 *
 * Notes:
 * - If the matrix is invalid (null / empty / zero-width first row),
 *   Result.valid() == false.
 *
 * Implementations:
 *
 * 1. Sort Row-Wise
 * 2. Sort Heights
 * 3. Threshold Counting (added for cross-checking)
 * All three are cross-checked against each other for both fixed and
 * randomised test matrices, following the same test-harness shape used
 * for SumOfDiagonalsMatrixTestHarness (TestCase / Algorithm / MethodCase /
 * runTests / randomised cross-checks).
 */
public class LargestRectangleColumsSwapping1s {

    static record Result(int result, boolean valid) {}

    /* **********************************************************************
     * Validation
     * **********************************************************************/

    static boolean validMatrix(int[][] matrix) {
        return matrix != null && matrix.length > 0 && matrix[0] != null && matrix[0].length > 0;
    }

    /* **********************************************************************
     * Shared Helper
     * **********************************************************************/

    /** heights[i][j] = number of consecutive 1s ending at row i, column j (top to bottom). */
    static int[][] buildHeights(int[][] matrix, int m, int n) {
        int[][] heights = new int[m][n];
        for (int j = 0; j < n; j++) {
            heights[0][j] = matrix[0][j];
            for (int i = 1; i < m; i++) {
                if (matrix[i][j] == 1) {
                    heights[i][j] = heights[i - 1][j] + 1;
                }
            }
        }
        return heights;
    }

    static void reverseInPlace(int[] arr) {
        int left = 0;
        int right = arr.length - 1;
        while (left < right) {
            int tmp = arr[left];
            arr[left] = arr[right];
            arr[right] = tmp;
            left++;
            right--;
        }
    }

    /* **********************************************************************
     * Algorithm Implementations
     * **********************************************************************/

    static Result largestRectangleColumsSwapping1sSortRowWise(int[][] matrix) {
        if (!validMatrix(matrix)) {
            return new Result(-1, false);
        }

        int m = matrix.length;
        int n = matrix[0].length;
        int[][] heights = buildHeights(matrix, m, n);

        int result = 0;
        for (int i = 0; i < m; i++) {
            int[] row = new int[n];
            for (int j = 0; j < n; j++) {
                row[j] = heights[i][j];
            }
            Arrays.sort(row);
            reverseInPlace(row);
            for (int j = 0; j < n; j++) {
                result = Math.max(result, row[j] * (j + 1));
            }
        }

        return new Result(result, true);
    }

    static Result largestRectangleColumsSwapping1sSortHeights(int[][] matrix) {
        if (!validMatrix(matrix)) {
            return new Result(-1, false);
        }

        int m = matrix.length;
        int n = matrix[0].length;
        int[][] heights = buildHeights(matrix, m, n);

        int result = 0;
        for (int i = 0; i < m; i++) {
            int[] row = new int[n];
            for (int j = 0; j < n; j++) {
                row[j] = heights[i][j];
            }
            Arrays.sort(row);
            reverseInPlace(row);
            for (int j = 0; j < n; j++) {
                result = Math.max(result, row[j] * (j + 1));
            }
        }

        return new Result(result, true);
    }

    static Result largestRectangleColumsSwapping1sThresholdCounting(int[][] matrix) {
        if (!validMatrix(matrix)) {
            return new Result(-1, false);
        }

        int m = matrix.length;
        int n = matrix[0].length;
        int[][] heights = buildHeights(matrix, m, n);

        int result = 0;
        for (int i = 0; i < m; i++) {
            for (int h = 1; h <= m; h++) {
                int count = 0;
                for (int j = 0; j < n; j++) {
                    if (heights[i][j] >= h) {
                        count++;
                    }
                }
                result = Math.max(result, h * count);
            }
        }

        return new Result(result, true);
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
                "Randomised Cross Checks (Sort Row-Wise vs Sort Heights vs Threshold Counting)");
        System.out.println(
                "======================================================");

        Random rng = new Random(20260902L);

        for (int i = 1; i <= iterations; i++) {

            int[][] matrix = randomMatrix(rng, 10, 10, 0.6);

            Result rowWise = largestRectangleColumsSwapping1sSortRowWise(matrix);
            Result heights = largestRectangleColumsSwapping1sSortHeights(matrix);
            Result threshold = largestRectangleColumsSwapping1sThresholdCounting(matrix);

            if (!resultsEqual(rowWise, heights) || !resultsEqual(rowWise, threshold)) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "matrix    = " + formatMatrix(matrix));

                System.out.println(
                        "rowWise   = " + rowWise);

                System.out.println(
                        "heights   = " + heights);

                System.out.println(
                        "threshold = " + threshold);

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
                        {0, 1, 0, 1, 0},
                        {0, 1, 0, 1, 1},
                        {1, 1, 1, 1, 1}
                },
                new Result(6, true),
                "rearranging columns beats any contiguous rectangle in the original layout"));

        tests.add(new TestCase(
                "B2",
                new int[][]{
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
                "all ones: the whole matrix, no rearrangement needed"));

        tests.add(new TestCase(
                "B4",
                new int[][]{{1, 0, 1, 1, 0}},
                new Result(3, true),
                "single row: answer is just the count of 1s"));

        tests.add(new TestCase(
                "B5",
                new int[][]{{1}, {1}, {0}, {1}},
                new Result(2, true),
                "single column: only one column, no rearranging possible"));

        /*
         * ============================================================
         * Non-Square Regression
         * (guards the original row-array sizing bug, "new int[m]"
         * instead of "new int[n]" - invisible on square matrices)
         * ============================================================
         */

        tests.add(new TestCase(
                "R1",
                new int[][]{
                        {1, 0, 1, 0, 1, 0, 1},
                        {1, 1, 1, 0, 1, 1, 1}
                },
                new Result(8, true),
                "2x7 non-square, more columns than rows"));

        tests.add(new TestCase(
                "R2",
                new int[][]{
                        {1, 1},
                        {1, 1},
                        {1, 1},
                        {1, 1},
                        {1, 1}
                },
                new Result(10, true),
                "5x2 non-square, more rows than columns"));

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
                "########  LARGEST RECTANGLE AFTER SWAPPING COLUMNS  ########");
        System.out.println(
                "############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "Sort Row-Wise",
                        LargestRectangleColumsSwapping1s::largestRectangleColumsSwapping1sSortRowWise),

                new MethodCase(
                        "Sort Heights",
                        LargestRectangleColumsSwapping1s::largestRectangleColumsSwapping1sSortHeights),

                new MethodCase(
                        "Threshold Counting",
                        LargestRectangleColumsSwapping1s::largestRectangleColumsSwapping1sThresholdCounting)
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
