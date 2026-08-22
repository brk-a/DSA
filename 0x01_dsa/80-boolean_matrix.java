import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Boolean Matrix.
 *
 * Problem:
 * Given an m x n matrix of 0s and 1s, modify it so that whenever a cell
 * matrix[i][j] is 1, the entire row i and the entire column j are set to 1.
 *
 * Notes:
 * - Invalid input (null / empty / zero-width first row) returns null.
 * - None of the three implementations mutate the caller's original matrix;
 *   each works on its own deep copy.
 *
 * Implementations:
 *
 * 1. Brute Force
 *      For every original 1, sweep its row and column, marking any 0 found
 *      as -1 (a "pending 1") so it isn't mistaken for a fresh trigger later
 *      in the same pass. A final pass converts every -1 back to 1.
 *      Time: O(m * n * (m + n))   Space: O(1) extra (besides the copy).
 *
 * 2. Temp Arrays
 *      Record which rows and columns contain a 1 in two boolean arrays,
 *      then fill any cell whose row or column was marked.
 *      Time: O(m * n)   Space: O(m + n).
 *
 * 3. No Temp Arrays (in-place marker reuse)
 *      Same idea as (2), but reuses row 0 and column 0 of the matrix itself
 *      as the marker arrays, plus a single boolean to remember column 0's
 *      own original state (since column 0 gets overwritten as a marker).
 *      Time: O(m * n)   Space: O(1) extra (besides the copy).
 *
 * All three are cross-checked against each other for both fixed and
 * randomised test matrices, following the same test-harness shape used for
 * SumOfDiagonalsMatrixTestHarness (TestCase / Algorithm / MethodCase /
 * runTests / randomised cross-checks).
 */
public class BooleanMatrix {

    /* **********************************************************************
     * Validation
     * **********************************************************************/

    static boolean validMatrix(int[][] matrix) {
        return matrix != null && matrix.length > 0 && matrix[0] != null && matrix[0].length > 0;
    }

    /* **********************************************************************
     * Algorithm Implementations
     * **********************************************************************/

    static int[][] booleanMatrixBruteForce(int[][] matrix) {
        if (!validMatrix(matrix)) {
            return null;
        }

        int m = matrix.length;
        int n = matrix[0].length;
        int[][] result = cloneMatrix(matrix);

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (result[i][j] == 1) {
                    for (int idx = 0; idx < m; idx++) {
                        if (result[idx][j] == 0) {
                            result[idx][j] = -1;
                        }
                    }
                    for (int idx = 0; idx < n; idx++) {
                        if (result[i][idx] == 0) {
                            result[i][idx] = -1;
                        }
                    }
                }
            }
        }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (result[i][j] == -1) {
                    result[i][j] = 1;
                }
            }
        }

        return result;
    }

    static int[][] booleanMatrixTempArrays(int[][] matrix) {
        if (!validMatrix(matrix)) {
            return null;
        }

        int m = matrix.length;
        int n = matrix[0].length;
        int[][] result = cloneMatrix(matrix);
        boolean[] rowMarker = new boolean[m];
        boolean[] colMarker = new boolean[n];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (result[i][j] == 1) {
                    rowMarker[i] = true;
                    colMarker[j] = true;
                }
            }
        }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (rowMarker[i] || colMarker[j]) {
                    result[i][j] = 1;
                }
            }
        }

        return result;
    }

    static int[][] booleanMatrixNoTempArrays(int[][] matrix) {
        if (!validMatrix(matrix)) {
            return null;
        }

        int m = matrix.length;
        int n = matrix[0].length;
        int[][] result = cloneMatrix(matrix);
        boolean col0 = false;

        for (int i = 0; i < m; i++) {
            if (result[i][0] == 1) {
                col0 = true;
            }
            for (int j = 1; j < n; j++) {
                if (result[i][j] == 1) {
                    result[i][0] = 1;
                    result[0][j] = 1;
                }
            }
        }

        for (int i = m - 1; i >= 0; i--) {
            for (int j = n - 1; j >= 1; j--) {
                if (result[i][0] == 1 || result[0][j] == 1) {
                    result[i][j] = 1;
                }
            }
            if (col0) {
                result[i][0] = 1;
            }
        }

        return result;
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
    interface Algorithm {

        int[][] solve(int[][] matrix);
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

                int[][] actual = method.solve(cloneMatrix(test.input));

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
     * Randomised Testing
     * **********************************************************************/

    static int[][] randomMatrix(
            Random rng,
            int maxRows,
            int maxCols,
            double oneProbability) {

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
                "Randomised Cross Checks (Brute vs Temp Arrays vs No Temp Arrays)");
        System.out.println(
                "======================================================");

        Random rng = new Random(20260820L);

        for (int i = 1; i <= iterations; i++) {

            // Vary 1-density across runs so both sparse and dense matrices,
            // including plenty with 1s on the very first row/column, get exercised.
            double density = 0.1 + 0.4 * rng.nextDouble();

            int[][] matrix = randomMatrix(rng, 8, 8, density);

            int[][] brute = booleanMatrixBruteForce(cloneMatrix(matrix));
            int[][] temp = booleanMatrixTempArrays(cloneMatrix(matrix));
            int[][] noTemp = booleanMatrixNoTempArrays(cloneMatrix(matrix));

            if (!matricesEqual(brute, temp) || !matricesEqual(brute, noTemp)) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "matrix  = " + formatMatrix(matrix));

                System.out.println(
                        "brute   = " + formatMatrix(brute));

                System.out.println(
                        "temp    = " + formatMatrix(temp));

                System.out.println(
                        "noTemp  = " + formatMatrix(noTemp));

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
                        {0, 0, 0},
                        {0, 1, 0},
                        {0, 0, 0}
                },
                new int[][]{
                        {0, 1, 0},
                        {1, 1, 1},
                        {0, 1, 0}
                },
                "single interior 1 fills its row and column"));

        tests.add(new TestCase(
                "B2",
                new int[][]{
                        {0, 0, 0},
                        {0, 0, 0},
                        {0, 0, 0}
                },
                new int[][]{
                        {0, 0, 0},
                        {0, 0, 0},
                        {0, 0, 0}
                },
                "all zeros stays all zeros"));

        tests.add(new TestCase(
                "B3",
                new int[][]{
                        {1, 1, 1},
                        {1, 1, 1},
                        {1, 1, 1}
                },
                new int[][]{
                        {1, 1, 1},
                        {1, 1, 1},
                        {1, 1, 1}
                },
                "all ones stays all ones"));

        tests.add(new TestCase(
                "B4",
                new int[][]{
                        {0, 0, 0, 0},
                        {0, 1, 0, 0},
                        {0, 0, 0, 0},
                        {0, 0, 0, 1}
                },
                new int[][]{
                        {0, 1, 0, 1},
                        {1, 1, 1, 1},
                        {0, 1, 0, 1},
                        {1, 1, 1, 1}
                },
                "two non-overlapping triggers, unioned rows/columns"));

        /*
         * ============================================================
         * Row 0 / Column 0 Edge Handling
         * (guards the original "j < 1" and "col0 = 1" bugs)
         * ============================================================
         */

        tests.add(new TestCase(
                "C1",
                new int[][]{
                        {1, 0, 0},
                        {0, 0, 0},
                        {0, 0, 0}
                },
                new int[][]{
                        {1, 1, 1},
                        {1, 0, 0},
                        {1, 0, 0}
                },
                "trigger at top-left corner (0,0)"));

        tests.add(new TestCase(
                "C2",
                new int[][]{
                        {0, 0, 0},
                        {0, 0, 0},
                        {0, 0, 1}
                },
                new int[][]{
                        {0, 0, 1},
                        {0, 0, 1},
                        {1, 1, 1}
                },
                "trigger at bottom-right corner"));

        tests.add(new TestCase(
                "C3",
                new int[][]{
                        {0, 0, 1},
                        {1, 0, 0},
                        {0, 0, 0}
                },
                new int[][]{
                        {1, 1, 1},
                        {1, 1, 1},
                        {1, 0, 1}
                },
                "trigger in column 0 plus a separate trigger in row 0"));

        /*
         * ============================================================
         * Non-Square Matrices
         * ============================================================
         */

        tests.add(new TestCase(
                "N1",
                new int[][]{
                        {0, 0, 1, 0, 0}
                },
                new int[][]{
                        {1, 1, 1, 1, 1}
                },
                "1x5 single row, one trigger fills the whole row"));

        tests.add(new TestCase(
                "N2",
                new int[][]{
                        {0},
                        {0},
                        {1},
                        {0},
                        {0}
                },
                new int[][]{
                        {1},
                        {1},
                        {1},
                        {1},
                        {1}
                },
                "5x1 single column, one trigger fills the whole column"));

        tests.add(new TestCase(
                "N3",
                new int[][]{
                        {0, 0, 0, 0, 0},
                        {0, 0, 1, 0, 0}
                },
                new int[][]{
                        {0, 0, 1, 0, 0},
                        {1, 1, 1, 1, 1}
                },
                "2x5 rectangle, single trigger"));

        /*
         * ============================================================
         * Trivial Sizes
         * ============================================================
         */

        tests.add(new TestCase(
                "M1",
                new int[][]{{0}},
                new int[][]{{0}},
                "1x1 matrix, zero"));

        tests.add(new TestCase(
                "M2",
                new int[][]{{1}},
                new int[][]{{1}},
                "1x1 matrix, one"));

        /*
         * ============================================================
         * Non-Mutation Check
         * ============================================================
         */

        tests.add(new TestCase(
                "P1",
                new int[][]{
                        {0, 1},
                        {0, 0}
                },
                new int[][]{
                        {1, 1},
                        {0, 1}
                },
                "result differs from input in-place, confirming a deep copy was used"));

        /*
         * ============================================================
         * Edge Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "E1",
                null,
                null,
                "null matrix"));

        tests.add(new TestCase(
                "E2",
                new int[][]{},
                null,
                "empty matrix (zero rows)"));

        tests.add(new TestCase(
                "E3",
                new int[][]{
                        {},
                        {},
                        {}
                },
                null,
                "matrix with zero columns"));

        System.out.println(
                "############################################################");
        System.out.println(
                "#########################  BOOLEAN MATRIX  ################");
        System.out.println(
                "############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "Brute Force",
                        BooleanMatrix::booleanMatrixBruteForce),

                new MethodCase(
                        "Temp Arrays",
                        BooleanMatrix::booleanMatrixTempArrays),

                new MethodCase(
                        "No Temp Arrays (O(1) space)",
                        BooleanMatrix::booleanMatrixNoTempArrays)
        );

        for (MethodCase method : methods) {

            runTests(
                    method.name,
                    method.algorithm,
                    tests);
        }

        // Non-mutation check: booleanMatrixNoTempArrays must not alter the
        // caller's original array (the original code's shallow .clone() bug
        // would have leaked writes back into it).
        int[][] original = {{0, 1}, {0, 0}};
        int[][] originalSnapshot = cloneMatrix(original);
        booleanMatrixNoTempArrays(original);
        System.out.println(
                "Input-mutation guard: " + (matricesEqual(original, originalSnapshot)
                        ? "PASS (input left untouched)"
                        : "FAIL (input was mutated!)"));
        System.out.println();

        runRandomisedTests(5000);
    }
}
