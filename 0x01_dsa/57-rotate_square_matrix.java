import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Rotate Matrix Test Harness.
 *
 * Two different problems are demonstrated:
 *
 * 1. rotateMatrixRingByOne (Oracle)
 *    Rotates the elements of the matrix, ring (layer) by ring,
 *    by exactly one position clockwise (as in ring-rotation problems).[web:21]
 *
 * 2. rotateMatrix90ClockwiseInPlace
 *    Rotates an n x n matrix 90 degrees clockwise in-place using
 *    transpose + reverse rows, as in LeetCode 48.[web:22][web:23]
 *
 * The test harness is mainly focused on verifying the 90-degree rotation
 * using a separate "formula-based" implementation as oracle.
 */
public class RotateMatrixTestHarness {

    /* **********************************************************************
     * 1. Ring-by-ring rotation by ONE step (fixed version of your approach)
     * **********************************************************************/

    /**
     * Rotates each ring of the matrix by one position clockwise.
     *
     * If matrix is null or empty, returns the matrix as-is.
     *
     * Time:  O(m * n)
     * Space: O(1) extra (in-place)
     */
    static int[][] rotateMatrixRingByOne(int[][] matrix) {

        if (!validMatrix(matrix)) {
            return matrix;
        }

        int top = 0;
        int bottom = matrix.length - 1;
        int left = 0;
        int right = matrix[0].length - 1;

        while (top < bottom && left < right) {

            int prev = matrix[top + 1][left];

            // 1. Top row: left -> right
            for (int col = left; col <= right; col++) {
                int curr = matrix[top][col];
                matrix[top][col] = prev;
                prev = curr;
            }
            top++;

            // 2. Right column: top -> bottom
            for (int row = top; row <= bottom; row++) {
                int curr = matrix[row][right];
                matrix[row][right] = prev;
                prev = curr;
            }
            right--;

            // 3. Bottom row: right -> left
            if (top <= bottom) {
                for (int col = right; col >= left; col--) {
                    int curr = matrix[bottom][col];
                    matrix[bottom][col] = prev;
                    prev = curr;
                }
            }
            bottom--;

            // 4. Left column: bottom -> top
            if (left <= right) {
                for (int row = bottom; row >= top; row--) {
                    int curr = matrix[row][left];
                    matrix[row][left] = prev;
                    prev = curr;
                }
            }
            left++;
        }

        return matrix;
    }

    /* **********************************************************************
     * 2. 90-degree rotation (in-place) implementations
     * **********************************************************************/

    /**
     * Formula-based 90° clockwise rotation.
     *
     * Creates a new matrix result and copies values:
     *   result[j][n-1-i] = matrix[i][j]
     *
     * Used as oracle for randomised tests.[web:23]
     *
     * Requires square matrix (n x n). Returns null for invalid.
     */
    static int[][] rotateMatrix90ClockwiseFormula(int[][] matrix) {

        if (!validSquareMatrix(matrix)) {
            return null;
        }

        int n = matrix.length;
        int[][] result = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {

                int newRow = j;
                int newCol = n - 1 - i;

                result[newRow][newCol] = matrix[i][j];
            }
        }

        return result;
    }

    /**
     * In-place 90° clockwise rotation: transpose + reverse rows.
     *
     * Steps:[web:22][web:23]
     *   1. Transpose: swap matrix[i][j] with matrix[j][i] for i < j.
     *   2. Reverse each row.
     *
     * Requires square matrix (n x n). Returns the same matrix reference.
     */
    static int[][] rotateMatrix90ClockwiseInPlace(int[][] matrix) {

        if (!validSquareMatrix(matrix)) {
            return matrix;
        }

        int n = matrix.length;

        // 1. Transpose
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int tmp = matrix[i][j];
                matrix[i][j] = matrix[j][i];
                matrix[j][i] = tmp;
            }
        }

        // 2. Reverse each row
        for (int i = 0; i < n; i++) {
            int left = 0;
            int right = n - 1;
            while (left < right) {
                int tmp = matrix[i][left];
                matrix[i][left] = matrix[i][right];
                matrix[i][right] = tmp;
                left++;
                right--;
            }
        }

        return matrix;
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

    static boolean validSquareMatrix(int[][] matrix) {
        if (!validMatrix(matrix)) {
            return false;
        }
        int n = matrix.length;
        return matrix[0].length == n;
    }

    /* **********************************************************************
     * Test Harness Types
     * **********************************************************************/

    static class TestCase {

        final String id;
        final int[][] input;
        final int[][] expected;
        final String description;

        TestCase(
                String id,
                int[][] input,
                int[][] expected,
                String description) {

            this.id = id;
            this.input = input;
            this.expected = expected;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface RotateAlgorithm {
        int[][] solve(int[][] matrix);
    }

    static class MethodCase {

        final String name;
        final RotateAlgorithm algorithm;

        MethodCase(
                String name,
                RotateAlgorithm algorithm) {

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

            if (a[i] == null && b[i] == null) {
                continue;
            }

            if (a[i] == null || b[i] == null) {
                return false;
            }

            if (a[i].length != b[i].length) {
                return false;
            }

            for (int j = 0; j < a[i].length; j++) {
                if (a[i][j] != b[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    static void runTests(
            String algorithmName,
            RotateAlgorithm method,
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

                int[][] actual =
                        method.solve(
                                cloneMatrix(test.input));

                if (matricesEqual(actual, test.expected)) {

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
                            formatMatrix(test.expected));

                    System.out.printf(
                            "  actual    = %s%n",
                            formatMatrix(actual));
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
     * Randomised Testing for 90° Rotation
     * **********************************************************************/

    static int[][] randomSquareMatrix(
            Random rng,
            int maxSize,
            int minValue,
            int maxValue) {

        int n = rng.nextInt(maxSize + 1);

        if (n == 0) {
            return new int[0][0];
        }

        int[][] matrix = new int[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = minValue
                        + rng.nextInt(maxValue - minValue + 1);
            }
        }

        return matrix;
    }

    static void runRandomisedTests90Rotation(int iterations) {

        System.out.println(
                "======================================================");
        System.out.println(
                "Randomised Cross Checks (90° In-place vs Formula)");
        System.out.println(
                "======================================================");

        Random rng = new Random(246813579L);

        for (int i = 1; i <= iterations; i++) {

            int[][] matrix = randomSquareMatrix(
                    rng,
                    8,
                    -10,
                    10);

            if (!validSquareMatrix(matrix)) {
                continue;
            }

            int[][] oracle =
                    rotateMatrix90ClockwiseFormula(
                            cloneMatrix(matrix));

            int[][] candidateInput =
                    cloneMatrix(matrix);

            int[][] candidate =
                    rotateMatrix90ClockwiseInPlace(
                            candidateInput);

            if (!matricesEqual(oracle, candidate)) {

                System.out.println(
                        "Randomised test FAILED (In-place vs Formula)");

                System.out.println(
                        "original  = " + formatMatrix(matrix));

                System.out.println(
                        "oracle    = " + formatMatrix(oracle));

                System.out.println(
                        "candidate = " + formatMatrix(candidate));

                return;
            }
        }

        System.out.printf(
                "All %d Randomised tests passed (In-place vs Formula).%n%n",
                iterations);
    }

    /* **********************************************************************
     * Main
     * **********************************************************************/

    public static void main(String[] args) {

        List<TestCase> ringTests = new ArrayList<>();
        List<TestCase> rotate90Tests = new ArrayList<>();

        /*
         * ============================================================
         * Ring-by-one rotation tests
         * ============================================================
         */

        ringTests.add(new TestCase(
                "R1",
                new int[][]{{1}},
                new int[][]{{1}},
                "1x1 ring rotation (no change)"));

        ringTests.add(new TestCase(
                "R2",
                new int[][]{
                        {1, 2},
                        {3, 4}
                },
                new int[][]{
                        {3, 1},
                        {4, 2}
                },
                "2x2 ring rotated by one clockwise"));

        ringTests.add(new TestCase(
                "R3",
                new int[][]{
                        {1, 2, 3},
                        {4, 5, 6},
                        {7, 8, 9}
                },
                // After one ring rotation for outer ring:
                // outer: 1,2,3,6,9,8,7,4 -> 4,1,2,3,6,9,8,7
                // Result:
                // 4,1,2
                // 7,5,3
                // 8,9,6
                new int[][]{
                        {4, 1, 2},
                        {7, 5, 3},
                        {8, 9, 6}
                },
                "3x3 ring rotated by one clockwise"));

        /*
         * ============================================================
         * 90-degree rotation tests
         * ============================================================
         */

        rotate90Tests.add(new TestCase(
                "C1",
                new int[][]{
                        {1, 2},
                        {3, 4}
                },
                new int[][]{
                        {3, 1},
                        {4, 2}
                },
                "2x2 rotate 90° clockwise"));

        rotate90Tests.add(new TestCase(
                "C2",
                new int[][]{
                        {1, 2, 3},
                        {4, 5, 6},
                        {7, 8, 9}
                },
                new int[][]{
                        {7, 4, 1},
                        {8, 5, 2},
                        {9, 6, 3}
                },
                "3x3 rotate 90° clockwise"));

        rotate90Tests.add(new TestCase(
                "C3",
                new int[][]{
                        {1, 2, 3, 4},
                        {5, 6, 7, 8},
                        {9, 10, 11, 12},
                        {13, 14, 15, 16}
                },
                new int[][]{
                        {13, 9, 5, 1},
                        {14, 10, 6, 2},
                        {15, 11, 7, 3},
                        {16, 12, 8, 4}
                },
                "4x4 rotate 90° clockwise"));

        System.out.println(
                "############################################################");
        System.out.println(
                "#################### ROTATE MATRIX #########################");
        System.out.println(
                "############################################################");
        System.out.println();

        // Ring-by-one rotation tests (using ring rotation as algorithm)
        runTests(
                "Ring-by-one Rotation (In-place)",
                RotateMatrixTestHarness::rotateMatrixRingByOne,
                ringTests);

        // 90° rotation tests (using in-place rotation)
        runTests(
                "90° Clockwise Rotation (In-place)",
                RotateMatrixTestHarness::rotateMatrix90ClockwiseInPlace,
                rotate90Tests);

        // Randomised cross-checks for 90° rotation
        runRandomisedTests90Rotation(5000);
    }
}
