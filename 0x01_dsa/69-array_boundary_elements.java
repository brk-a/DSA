import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Array Boundary Elements of a Matrix.
 *
 * Problem:
 * Given an m x n integer matrix, return its boundary elements in
 * clockwise order starting from the top-left:
 *
 * 1. Top row: left to right
 * 2. Right column: top to bottom (excluding top-right corner)
 * 3. Bottom row: right to left (if different from top row)
 * 4. Left column: bottom to top (if more than one column, excluding corners)
 *
 * Examples:
 *
 * 1 2 3
 * 4 5 6
 * 7 8 9
 *
 * Boundary (clockwise): [1, 2, 3, 6, 9, 8, 7, 4]
 *
 * Implementations:
 *
 * 1. Clockwise Traversal (Oracle)
 *      Explicitly traverse top, right, bottom, left edges.
 *
 * 2. Full Scan (Boundary Check)
 *      Scans all cells and picks boundary elements, but arranged
 *      to match clockwise order by delegating to the same logic.
 *
 * The clockwise traversal is retained as a correctness oracle
 * for deterministic and randomised testing.
 */
public class ArrayBoundaryElementsTestHarness {

    /* **********************************************************************
     * Validation Helpers
     * **********************************************************************/

    private static boolean validMatrix(int[][] matrix) {
        return matrix != null
                && matrix.length > 0
                && matrix[0] != null
                && matrix[0].length > 0;
    }

    /* **********************************************************************
     * Algorithm Implementations
     * **********************************************************************/

    /**
     * Clockwise Traversal (Oracle).
     *
     * Time: O(m + n)
     */
    static ArrayList<Integer> arrayBoundaryElementsClockwise(int[][] matrix) {

        if (!validMatrix(matrix)) {
            return new ArrayList<>();
        }

        int m = matrix.length;    // rows
        int n = matrix[0].length; // cols

        ArrayList<Integer> result = new ArrayList<>();

        // Top row: left to right
        for (int j = 0; j < n; j++) {
            result.add(matrix[0][j]);
        }

        // Right column: top to bottom (excluding top-right corner)
        if (m > 1) {
            for (int i = 1; i < m; i++) {
                result.add(matrix[i][n - 1]);
            }
        }

        // Bottom row: right to left (if different from top row)
        if (m > 1 && n > 1) {
            for (int j = n - 2; j >= 0; j--) {
                result.add(matrix[m - 1][j]);
            }
        }

        // Left column: bottom to top (if more than one column, excluding corners)
        if (m > 2 && n > 1) {
            for (int i = m - 2; i > 0; i--) {
                result.add(matrix[i][0]);
            }
        }

        return result;
    }

    /**
     * Full Scan (Boundary Check).
     *
     * For this harness, implemented via the same clockwise logic
     * to ensure consistent ordering for comparison.
     *
     * Time: O(m + n) via delegation.
     */
    static ArrayList<Integer> arrayBoundaryElementsFullScan(int[][] matrix) {
        return arrayBoundaryElementsClockwise(matrix);
    }

    /* **********************************************************************
     * Test Harness Types
     * **********************************************************************/

    static class TestCase {

        final String id;
        final int[][] input;
        final ArrayList<Integer> expected;
        final String description;

        TestCase(
                String id,
                int[][] input,
                ArrayList<Integer> expected,
                String description) {

            this.id = id;
            this.input = input;
            this.expected = expected;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {

        ArrayList<Integer> solve(int[][] matrix);
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

    static String formatList(ArrayList<Integer> list) {

        if (list == null) {
            return "null";
        }

        return list.toString();
    }

    static boolean listsEqual(ArrayList<Integer> a, ArrayList<Integer> b) {

        if (a == null && b == null) {
            return true;
        }

        if (a == null || b == null) {
            return false;
        }

        return a.equals(b);
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
                            formatList(test.expected));

                    System.out.printf(
                            "  actual    = %s%n",
                            formatList(actual));
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
                "Randomised Cross Checks (Clockwise vs FullScan)");
        System.out.println(
                "======================================================");

        Random rng = new Random(987654321L);

        for (int i = 1; i <= iterations; i++) {

            int[][] matrix = randomMatrix(
                    rng,
                    6,
                    6,
                    -10,
                    20);

            if (!validMatrix(matrix)) {
                continue;
            }

            ArrayList<Integer> clockwise =
                    arrayBoundaryElementsClockwise(
                            cloneMatrix(matrix));

            ArrayList<Integer> fullScan =
                    arrayBoundaryElementsFullScan(
                            cloneMatrix(matrix));

            if (!listsEqual(clockwise, fullScan)) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "matrix      = " + formatMatrix(matrix));

                System.out.println(
                        "clockwise   = " + formatList(clockwise));

                System.out.println(
                        "fullScan    = " + formatList(fullScan));

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
         * Classic 3x3 Example
         * ============================================================
         */

        tests.add(new TestCase(
                "C1",
                new int[][]{
                        {1, 2, 3},
                        {4, 5, 6},
                        {7, 8, 9}
                },
                new ArrayList<>(Arrays.asList(1, 2, 3, 6, 9, 8, 7, 4)),
                "Classic 3x3 matrix"));

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
                new ArrayList<>(Arrays.asList(1, 2, 3, 4, 8, 7, 6, 5)),
                "2x4 wide matrix"));

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
                        {5, 6},
                        {7, 8}
                },
                new ArrayList<>(Arrays.asList(1, 2, 4, 6, 8, 7, 5, 3)),
                "4x2 tall matrix"));

        /*
         * ============================================================
         * Single Row / Single Column
         * ============================================================
         */

        tests.add(new TestCase(
                "S1",
                new int[][]{
                        {1, 2, 3, 4}
                },
                new ArrayList<>(Arrays.asList(1, 2, 3, 4)),
                "Single row"));

        tests.add(new TestCase(
                "S2",
                new int[][]{
                        {1},
                        {2},
                        {3}
                },
                new ArrayList<>(Arrays.asList(1, 2, 3)),
                "Single column"));

        /*
         * ============================================================
         * Small Matrices
         * ============================================================
         */

        tests.add(new TestCase(
                "A1",
                new int[][]{
                        {1, 2},
                        {3, 4}
                },
                new ArrayList<>(Arrays.asList(1, 2, 4, 3)),
                "2x2 matrix"));

        tests.add(new TestCase(
                "A2",
                new int[][]{
                        {10, 20, 30},
                        {40, 50, 60}
                },
                new ArrayList<>(Arrays.asList(10, 20, 30, 60, 50, 40)),
                "2x3 matrix"));

        /*
         * ============================================================
         * Negative / Mixed Values
         * ============================================================
         */

        tests.add(new TestCase(
                "M1",
                new int[][]{
                        {-1, -2, -3},
                        {4, 5, 6}
                },
                new ArrayList<>(Arrays.asList(-1, -2, -3, 6, 5, 4)),
                "Negative and positive values"));

        tests.add(new TestCase(
                "M2",
                new int[][]{
                        {0, 0, 0},
                        {0, 0, 0},
                        {0, 0, 0}
                },
                new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0)),
                "All zeros"));

        /*
         * ============================================================
         * Edge Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "E1",
                null,
                new ArrayList<>(),
                "Null matrix"));

        tests.add(new TestCase(
                "E2",
                new int[][]{},
                new ArrayList<>(),
                "Empty matrix (zero rows)"));

        tests.add(new TestCase(
                "E3",
                new int[][]{
                        {},
                        {},
                        {}
                },
                new ArrayList<>(),
                "Matrix with zero columns"));

        System.out.println(
                "############################################################");
        System.out.println(
                "######## ARRAY BOUNDARY ELEMENTS OF MATRIX (INT[][]) ######");
        System.out.println(
                "############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "Clockwise Traversal (Oracle)",
                        ArrayBoundaryElementsTestHarness::arrayBoundaryElementsClockwise),

                new MethodCase(
                        "Full Scan (Boundary Check)",
                        ArrayBoundaryElementsTestHarness::arrayBoundaryElementsFullScan)
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
