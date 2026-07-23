import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Spiral Matrix Traversal Test Harness.
 *
 * Problem:
 * Given an m x n matrix, return all elements in clockwise spiral order.
 *
 * Implementations:
 *
 * 1. spiralMatrixVisitedMatrix (Oracle)
 *    Uses a visited[][] matrix and direction deltas to traverse
 *    the matrix in spiral order. Time: O(m*n), Space: O(m*n) for visited.
 *
 * 2. spiralMatrixBoundaryTraversal
 *    Uses four boundaries (top, bottom, left, right) to peel layers
 *    in spiral order without extra visited storage.
 *    Time: O(m*n), Space: O(1) extra (excluding result).
 *
 * The visited-matrix version acts as a correctness oracle for
 * randomised cross-checking of the boundary-based implementation.
 */
public class SpiralMatrixTestHarness {

    /* **********************************************************************
     * Core Algorithm Implementations
     * **********************************************************************/

    /**
     * Oracle implementation using a visited matrix and direction deltas.
     *
     * Traverses in spiral order:
     *   Right -> Down -> Left -> Up, turning whenever the next cell
     *   would be out of bounds or already visited.
     *
     * Time:  O(m * n)
     * Space: O(m * n) for visited
     */
    static ArrayList<Integer> spiralMatrixVisitedMatrix(int[][] matrix) {

        if (!validMatrix(matrix)) {
            return new ArrayList<>();
        }

        int m = matrix.length;
        int n = matrix[0].length;

        ArrayList<Integer> result = new ArrayList<>();
        boolean[][] visited = new boolean[m][n];

        // Directions: right, down, left, up
        int[] deltaRow = {0, 1, 0, -1};
        int[] deltaCol = {1, 0, -1, 0};

        int row = 0;
        int col = 0;
        int dirIdx = 0;

        for (int i = 0; i < m * n; i++) {

            result.add(matrix[row][col]);
            visited[row][col] = true;

            int newRow = row + deltaRow[dirIdx];
            int newCol = col + deltaCol[dirIdx];

            // Check bounds and visited
            if (newRow >= 0 && newRow < m
                    && newCol >= 0 && newCol < n
                    && !visited[newRow][newCol]) {

                row = newRow;
                col = newCol;

            } else {
                // Rotate direction clockwise
                dirIdx = (dirIdx + 1) % 4;
                row += deltaRow[dirIdx];
                col += deltaCol[dirIdx];
            }
        }

        return result;
    }

    /**
     * Boundary-based spiral traversal.
     *
     * Uses four pointers:
     *   top, bottom, left, right
     *
     * Steps in each loop:
     *   1. Traverse top row: left -> right, then top++
     *   2. Traverse right column: top -> bottom, then right--
     *   3. If top <= bottom, traverse bottom row: right -> left, then bottom--
     *   4. If left <= right, traverse left column: bottom -> top, then left++
     *
     * Time:  O(m * n)
     * Space: O(1) extra (excluding result)
     */
    static ArrayList<Integer> spiralMatrixBoundaryTraversal(int[][] matrix) {

        if (!validMatrix(matrix)) {
            return new ArrayList<>();
        }

        int m = matrix.length;
        int n = matrix[0].length;

        ArrayList<Integer> result = new ArrayList<>();

        int top = 0;
        int bottom = m - 1;
        int left = 0;
        int right = n - 1;

        while (top <= bottom && left <= right) {

            // 1. Top row: left -> right
            for (int col = left; col <= right; col++) {
                result.add(matrix[top][col]);
            }
            top++;

            // 2. Right column: top -> bottom
            for (int row = top; row <= bottom; row++) {
                result.add(matrix[row][right]);
            }
            right--;

            // 3. Bottom row: right -> left (if still valid)
            if (top <= bottom) {
                for (int col = right; col >= left; col--) {
                    result.add(matrix[bottom][col]);
                }
                bottom--;
            }

            // 4. Left column: bottom -> top (if still valid)
            if (left <= right) {
                for (int row = bottom; row >= top; row--) {
                    result.add(matrix[row][left]);
                }
                left++;
            }
        }

        return result;
    }

    /* **********************************************************************
     * Matrix Validation
     * **********************************************************************/

    static boolean validMatrix(int[][] matrix) {
        return matrix != null
                && matrix.length > 0
                && matrix[0] != null
                && matrix[0].length > 0;
    }

    /* **********************************************************************
     * Test Harness Types
     * **********************************************************************/

    static class TestCase {

        final String id;
        final int[][] input;
        final List<Integer> expected;
        final String description;

        TestCase(
                String id,
                int[][] input,
                List<Integer> expected,
                String description) {

            this.id = id;
            this.input = input;
            this.expected = expected;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface SpiralAlgorithm {
        ArrayList<Integer> solve(int[][] matrix);
    }

    static class MethodCase {

        final String name;
        final SpiralAlgorithm algorithm;

        MethodCase(
                String name,
                SpiralAlgorithm algorithm) {

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

        int[][] clone = new int[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            clone[i] = matrix[i] == null ? null : matrix[i].clone();
        }
        return clone;
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
            if (matrix[i] == null) {
                sb.append("null");
            } else {
                sb.append(Arrays.toString(matrix[i]));
            }
        }
        sb.append("]");
        return sb.toString();
    }

    static boolean listsEqual(List<Integer> a, List<Integer> b) {

        if (a == null && b == null) {
            return true;
        }

        if (a == null || b == null) {
            return false;
        }

        if (a.size() != b.size()) {
            return false;
        }

        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i))) {
                return false;
            }
        }

        return true;
    }

    static void runTests(
            String algorithmName,
            SpiralAlgorithm method,
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

                ArrayList<Integer> actual =
                        method.solve(cloneMatrix(test.input));

                if (listsEqual(actual, test.expected)) {

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

        int rows = rng.nextInt(maxRows + 1);
        int cols = rng.nextInt(maxCols + 1);

        if (rows == 0 || cols == 0) {
            return new int[0][0];
        }

        int[][] matrix = new int[rows][cols];

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                matrix[r][c] = minValue
                        + rng.nextInt(maxValue - minValue + 1);
            }
        }

        return matrix;
    }

    static void runRandomisedTests(int iterations) {

        System.out.println(
                "======================================================");
        System.out.println(
                "Randomised Cross Checks (Visited vs Boundary)");
        System.out.println(
                "======================================================");

        Random rng = new Random(123456789L);

        for (int i = 1; i <= iterations; i++) {

            int[][] matrix = randomMatrix(
                    rng,
                    8,
                    8,
                    -10,
                    10);

            if (!validMatrix(matrix)) {
                continue;
            }

            ArrayList<Integer> oracle =
                    spiralMatrixVisitedMatrix(
                            cloneMatrix(matrix));

            ArrayList<Integer> candidate =
                    spiralMatrixBoundaryTraversal(
                            cloneMatrix(matrix));

            if (!listsEqual(oracle, candidate)) {

                System.out.println(
                        "Randomised test FAILED (Boundary vs Visited)");

                System.out.println(
                        "matrix    = " + formatMatrix(matrix));

                System.out.println(
                        "oracle    = " + oracle);

                System.out.println(
                        "candidate = " + candidate);

                return;
            }
        }

        System.out.printf(
                "All %d Randomised tests passed (Boundary vs Visited).%n%n",
                iterations);
    }

    /* **********************************************************************
     * Main
     * **********************************************************************/

    public static void main(String[] args) {

        List<TestCase> tests = new ArrayList<>();

        /*
         * ============================================================
         * Small / Simple Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "S1",
                new int[][]{{1}},
                List.of(1),
                "Single element"));

        tests.add(new TestCase(
                "S2",
                new int[][]{{1, 2}},
                List.of(1, 2),
                "1x2 matrix"));

        tests.add(new TestCase(
                "S3",
                new int[][]{{1}, {2}},
                List.of(1, 2),
                "2x1 matrix"));

        tests.add(new TestCase(
                "S4",
                new int[][]{
                        {1, 2, 3},
                        {4, 5, 6},
                        {7, 8, 9}
                },
                List.of(1, 2, 3, 6, 9, 8, 7, 4, 5),
                "3x3 classic spiral"));

        tests.add(new TestCase(
                "S5",
                new int[][]{
                        {1, 2, 3, 4},
                        {5, 6, 7, 8},
                        {9, 10, 11, 12}
                },
                List.of(1, 2, 3, 4, 8, 12, 11, 10, 9, 5, 6, 7),
                "3x4 rectangular"));

        tests.add(new TestCase(
                "S6",
                new int[][]{
                        {1, 2, 3, 4},
                        {5, 6, 7, 8},
                        {9, 10, 11, 12},
                        {13, 14, 15, 16}
                },
                List.of(
                        1, 2, 3, 4,
                        8, 12, 16,
                        15, 14, 13,
                        9, 5,
                        6, 7, 11, 10),
                "4x4 full spiral"));

        /*
         * ============================================================
         * Edge Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "E1",
                new int[][]{},
                new ArrayList<>(),
                "Empty outer array"));

        tests.add(new TestCase(
                "E2",
                null,
                new ArrayList<>(),
                "Null matrix"));

        System.out.println(
                "############################################################");
        System.out.println(
                "########### SPIRAL MATRIX (INT MATRIX) #####################");
        System.out.println(
                "############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "Visited Matrix (Oracle)",
                        SpiralMatrixTestHarness::spiralMatrixVisitedMatrix),

                new MethodCase(
                        "Boundary Traversal",
                        SpiralMatrixTestHarness::spiralMatrixBoundaryTraversal)
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
