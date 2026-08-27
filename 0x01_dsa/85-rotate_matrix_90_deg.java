import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Rotate Matrix 90 Degrees.
 *
 * Problem:
 * Given an m x n matrix, produce the matrix rotated 90 degrees
 * COUNTER-CLOCKWISE. (All three implementations below independently work
 * out to counter-clockwise once fixed - this is simply what the original
 * index arithmetic computes, not an arbitrary choice, and it is called out
 * explicitly here since "rotate 90 degrees" is ambiguous without it.)
 *
 * Notes:
 * - If the matrix is invalid (null / empty / zero-width first row), every
 *   method returns Result(matrix, false) - the original reference is
 *   handed back unchanged, and the valid flag signals not to trust it.
 * - rotateMatrix90DegreesExtraSpace supports ANY m x n matrix, including
 *   non-square, since it allocates a fresh n x m array for the (dimension
 *   swapping) result.
 * - rotateMatrix90DegreesFormingCycles and rotateMatrix90DegreesReverseRowsTranspose
 *   are genuinely IN-PLACE techniques (working within a same-shape copy of
 *   the input), which is only mathematically possible when the result has
 *   the same shape as the input - i.e. only for SQUARE (m == n) matrices.
 *   Both now validate for that explicitly and return Result(matrix, false)
 *   on a non-square matrix instead of throwing.
 *
 * Implementations:
 *
 * 1. Extra Space
 *      Direct index mapping into a freshly allocated n x m array.
 *      Time: O(m * n)   Space: O(m * n).
 *
 * 2. Forming Cycles
 *      Rotates layer by layer via 4-way corner swaps, entirely in place.
 *      Time: O(m * n)   Space: O(1) extra (besides the copy).
 *
 * 3. Reverse Rows + Transpose
 *      Reverses each row left-to-right, then transposes in place.
 *      Time: O(m * n)   Space: O(1) extra (besides the copy).
 *
 * All three are cross-checked against each other on square matrices, plus
 * an implementation-independent invariant check (rotating any matrix four
 * times must return the original), for both fixed and randomised test
 * matrices - following the same test-harness shape used for
 * SumOfDiagonalsMatrixTestHarness (TestCase / Algorithm / MethodCase /
 * runTests / randomised cross-checks).
 */
public class RotateMatrix90Degrees {

    static record Result(int[][] result, boolean valid) {}

    /* **********************************************************************
     * Validation
     * **********************************************************************/

    static boolean validMatrix(int[][] matrix) {
        return matrix != null && matrix.length > 0 && matrix[0] != null && matrix[0].length > 0;
    }

    static boolean validSquareMatrix(int[][] matrix) {
        return validMatrix(matrix) && matrix.length == matrix[0].length;
    }

    /* **********************************************************************
     * Algorithm Implementations
     * **********************************************************************/

    static Result rotateMatrix90DegreesExtraSpace(int[][] matrix) {
        if (!validMatrix(matrix)) {
            return new Result(matrix, false);
        }

        int m = matrix.length;
        int n = matrix[0].length;
        int[][] result = new int[n][m];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                result[n - j - 1][i] = matrix[i][j];
            }
        }

        return new Result(result, true);
    }

    static Result rotateMatrix90DegreesFormingCycles(int[][] matrix) {
        if (!validSquareMatrix(matrix)) {
            return new Result(matrix, false);
        }

        int n = matrix.length;
        int[][] result = cloneMatrix(matrix);

        for (int i = 0; i < n / 2; i++) {
            for (int j = i; j < n - i - 1; j++) {
                int tmp = result[i][j];
                result[i][j] = result[j][n - i - 1];
                result[j][n - i - 1] = result[n - i - 1][n - j - 1];
                result[n - i - 1][n - j - 1] = result[n - j - 1][i];
                result[n - j - 1][i] = tmp;
            }
        }

        return new Result(result, true);
    }

    static Result rotateMatrix90DegreesReverseRowsTranspose(int[][] matrix) {
        if (!validSquareMatrix(matrix)) {
            return new Result(matrix, false);
        }

        int n = matrix.length;
        int[][] result = cloneMatrix(matrix);

        for (int i = 0; i < n; i++) {
            int start = 0;
            int end = n - 1;
            while (start < end) {
                int tmp = result[i][start];
                result[i][start] = result[i][end];
                result[i][end] = tmp;
                start++;
                end--;
            }
        }

        // Transpose in place.
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int tmp = result[i][j];
                result[i][j] = result[j][i];
                result[j][i] = tmp;
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

    static boolean resultsEqual(Result a, Result b) {

        if (a == null && b == null) {
            return true;
        }

        if (a == null || b == null) {
            return false;
        }

        return matricesEqual(a.result(), b.result()) && a.valid() == b.valid();
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
                            "  expected  = valid=%s, result=%s%n",
                            test.expected.valid(),
                            formatMatrix(test.expected.result()));

                    System.out.printf(
                            "  actual    = valid=%s, result=%s%n",
                            actual.valid(),
                            formatMatrix(actual.result()));
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

    /** Rotating any matrix four times (90 degrees each) must return the original. */
    static boolean fourRotationsAreIdentity(Algorithm method, int[][] original) {
        int[][] current = cloneMatrix(original);

        for (int k = 0; k < 4; k++) {
            Result r = method.solve(cloneMatrix(current));
            if (!r.valid()) {
                return false;
            }
            current = r.result();
        }

        return matricesEqual(current, original);
    }

    /* **********************************************************************
     * Randomised Testing
     * **********************************************************************/

    static int[][] randomSquareMatrix(Random rng, int maxSize, int minValue, int maxValue) {
        int size = rng.nextInt(maxSize) + 1;
        int[][] matrix = new int[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                matrix[i][j] = minValue + rng.nextInt(maxValue - minValue + 1);
            }
        }

        return matrix;
    }

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
                "Randomised Cross Checks (square matrices, all 3 methods)");
        System.out.println(
                "======================================================");

        Random rng = new Random(20260825L);

        for (int i = 1; i <= iterations; i++) {

            int[][] matrix = randomSquareMatrix(rng, 8, -9, 9);

            Result extra = rotateMatrix90DegreesExtraSpace(cloneMatrix(matrix));
            Result cycles = rotateMatrix90DegreesFormingCycles(cloneMatrix(matrix));
            Result reverse = rotateMatrix90DegreesReverseRowsTranspose(cloneMatrix(matrix));

            if (!resultsEqual(extra, cycles) || !resultsEqual(extra, reverse)) {

                System.out.println(
                        "Randomised test FAILED (cross-check)");

                System.out.println(
                        "matrix  = " + formatMatrix(matrix));

                System.out.println(
                        "extra   = " + formatMatrix(extra.result()));

                System.out.println(
                        "cycles  = " + formatMatrix(cycles.result()));

                System.out.println(
                        "reverse = " + formatMatrix(reverse.result()));

                return;
            }

            if (!fourRotationsAreIdentity(RotateMatrix90Degrees::rotateMatrix90DegreesExtraSpace, matrix)) {
                System.out.println("Randomised test FAILED (four-rotations-identity, ExtraSpace)");
                System.out.println("matrix = " + formatMatrix(matrix));
                return;
            }
        }

        System.out.printf(
                "All %d Randomised tests passed (3-way cross-check + 4-rotation identity).%n%n",
                iterations);

        System.out.println(
                "======================================================");
        System.out.println(
                "Randomised Four-Rotation Identity Check (non-square, Extra Space only)");
        System.out.println(
                "======================================================");

        for (int i = 1; i <= iterations; i++) {
            int[][] matrix = randomMatrix(rng, 8, 8, -9, 9);

            if (!fourRotationsAreIdentity(RotateMatrix90Degrees::rotateMatrix90DegreesExtraSpace, matrix)) {
                System.out.println("Randomised test FAILED (non-square four-rotation identity)");
                System.out.println("matrix = " + formatMatrix(matrix));
                return;
            }
        }

        System.out.printf(
                "All %d non-square Randomised tests passed.%n%n",
                iterations);
    }

    /* **********************************************************************
     * Main
     * **********************************************************************/

    public static void main(String[] args) {

        List<TestCase> tests = new ArrayList<>();

        /*
         * ============================================================
         * Basic Square Cases (shared across all three methods)
         * ============================================================
         */

        tests.add(new TestCase(
                "B1",
                new int[][]{
                        {1, 2, 3},
                        {4, 5, 6},
                        {7, 8, 9}
                },
                new Result(new int[][]{
                        {3, 6, 9},
                        {2, 5, 8},
                        {1, 4, 7}
                }, true),
                "3x3, odd size"));

        tests.add(new TestCase(
                "B2",
                new int[][]{
                        {1, 2},
                        {3, 4}
                },
                new Result(new int[][]{
                        {2, 4},
                        {1, 3}
                }, true),
                "2x2, even size (guards the original redundant-loop bug, which cancelled out on even n)"));

        tests.add(new TestCase(
                "B3",
                new int[][]{{7}},
                new Result(new int[][]{{7}}, true),
                "1x1 matrix"));

        tests.add(new TestCase(
                "B4",
                new int[][]{
                        {1, 2, 3, 4},
                        {5, 6, 7, 8},
                        {9, 10, 11, 12},
                        {13, 14, 15, 16}
                },
                new Result(new int[][]{
                        {4, 8, 12, 16},
                        {3, 7, 11, 15},
                        {2, 6, 10, 14},
                        {1, 5, 9, 13}
                }, true),
                "4x4, even size"));

        /*
         * ============================================================
         * Edge Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "E1",
                null,
                new Result(null, false),
                "null matrix"));

        tests.add(new TestCase(
                "E2",
                new int[][]{},
                new Result(new int[][]{}, false),
                "empty matrix (zero rows)"));

        tests.add(new TestCase(
                "E3",
                new int[][]{
                        {},
                        {},
                        {}
                },
                new Result(new int[][]{{}, {}, {}}, false),
                "matrix with zero columns"));

        System.out.println(
                "############################################################");
        System.out.println(
                "#################  ROTATE MATRIX 90 DEGREES  ###############");
        System.out.println(
                "############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "Extra Space",
                        RotateMatrix90Degrees::rotateMatrix90DegreesExtraSpace),

                new MethodCase(
                        "Forming Cycles",
                        RotateMatrix90Degrees::rotateMatrix90DegreesFormingCycles),

                new MethodCase(
                        "Reverse Rows + Transpose",
                        RotateMatrix90Degrees::rotateMatrix90DegreesReverseRowsTranspose)
        );

        for (MethodCase method : methods) {

            runTests(
                    method.name,
                    method.algorithm,
                    tests);
        }

        /*
         * ============================================================
         * Method-Specific Tests: non-square matrices
         * (Extra Space supports them; the two in-place methods can't and
         * must reject them gracefully instead of throwing)
         * ============================================================
         */

        System.out.println(
                "======================================================");
        System.out.println(
                "Non-Square Matrices (method-specific expected behaviour)");
        System.out.println(
                "======================================================");

        int[][] nonSquare = {
                {1, 2, 3},
                {4, 5, 6}
        };
        int[][] expectedRotated = {
                {3, 6},
                {2, 5},
                {1, 4}
        };

        Result extraSpaceResult = rotateMatrix90DegreesExtraSpace(cloneMatrix(nonSquare));
        System.out.println(
                "✓? Extra Space on 2x3 rotates to 3x2: "
                        + (extraSpaceResult.valid() && matricesEqual(extraSpaceResult.result(), expectedRotated)
                                ? "PASS" : "FAIL (" + extraSpaceResult + ")"));

        Result cyclesResult = rotateMatrix90DegreesFormingCycles(cloneMatrix(nonSquare));
        System.out.println(
                "✓? Forming Cycles on 2x3 gracefully rejects (valid=false): "
                        + (!cyclesResult.valid() ? "PASS" : "FAIL (" + cyclesResult + ")"));

        Result reverseResult = rotateMatrix90DegreesReverseRowsTranspose(cloneMatrix(nonSquare));
        System.out.println(
                "✓? Reverse Rows + Transpose on 2x3 gracefully rejects (valid=false): "
                        + (!reverseResult.valid() ? "PASS" : "FAIL (" + reverseResult + ")"));

        System.out.println();

        runRandomisedTests(3000);
    }
}
