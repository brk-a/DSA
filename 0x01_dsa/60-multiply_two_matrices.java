import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Multiply Two Matrices (Integer).
 *
 * Implementations:
 *
 * 1. Standard Triple Loop
 *      O(r1 * c1 * c2)
 *
 * 2. Alternative Implementation (Row × Column)
 *      O(r1 * c1 * c2)
 *
 * The primary method is multiplyTwoMatricesTripleLoop, and the
 * alternative is multiplyTwoMatricesRowColumn. Both require
 * rectangular matrices and compatible dimensions.
 */
public class MultiplyTwoMatricesTestHarness {


    /* **********************************************************************
     * Validation
     * **********************************************************************/


    static boolean validMatrix(int[][] matrix) {
        return matrix != null && matrix.length > 0 && matrix[0] != null && matrix[0].length > 0;
    }


    static boolean isRectangular(int[][] matrix) {
        if (matrix == null || matrix.length == 0) {
            return false;
        }

        int cols = matrix[0].length;

        for (int i = 1; i < matrix.length; i++) {
            if (matrix[i] == null || matrix[i].length != cols) {
                return false;
            }
        }

        return true;
    }


    /* **********************************************************************
     * Algorithms
     * **********************************************************************/


    /**
     * Original-style triple loop implementation.
     *
     * Assumes rectangular matrices and compatible dimensions.
     */
    static int[][] multiplyTwoMatricesTripleLoop(int[][] matrix1, int[][] matrix2) {

        if (!validMatrix(matrix1) || !validMatrix(matrix2)
                || !isRectangular(matrix1) || !isRectangular(matrix2)) {
            return null;
        }

        int r1 = matrix1.length;
        int r2 = matrix2.length;
        int c1 = matrix1[0].length;
        int c2 = matrix2[0].length;

        if (c1 != r2) {
            // dimension mismatch; returning null instead of exiting
            return null;
        }

        int[][] result = new int[r1][c2];

        for (int i = 0; i < r1; i++) {
            for (int j = 0; j < c2; j++) {

                int sum = 0;

                for (int k = 0; k < c1; k++) {
                    sum += matrix1[i][k] * matrix2[k][j];
                }

                result[i][j] = sum;
            }
        }

        return result;
    }


    /**
     * Alternative implementation using an explicit row × column helper.
     */
    static int[][] multiplyTwoMatricesRowColumn(int[][] matrix1, int[][] matrix2) {

        if (!validMatrix(matrix1) || !validMatrix(matrix2)
                || !isRectangular(matrix1) || !isRectangular(matrix2)) {
            return null;
        }

        int r1 = matrix1.length;
        int r2 = matrix2.length;
        int c1 = matrix1[0].length;
        int c2 = matrix2[0].length;

        if (c1 != r2) {
            return null;
        }

        int[][] result = new int[r1][c2];

        for (int i = 0; i < r1; i++) {
            for (int j = 0; j < c2; j++) {
                result[i][j] = dotProduct(matrix1[i], column(matrix2, j));
            }
        }

        return result;
    }


    /* **********************************************************************
     * Helpers
     * **********************************************************************/


    static int[] column(int[][] matrix, int colIndex) {
        int rows = matrix.length;
        int[] col = new int[rows];

        for (int i = 0; i < rows; i++) {
            col[i] = matrix[i][colIndex];
        }

        return col;
    }


    static int dotProduct(int[] a, int[] b) {
        int n = a.length;
        int sum = 0;

        for (int i = 0; i < n; i++) {
            sum += a[i] * b[i];
        }

        return sum;
    }


    static int[][] cloneMatrix(int[][] matrix) {
        if (matrix == null) {
            return null;
        }

        int[][] copy = new int[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            copy[i] = matrix[i] == null ? null : matrix[i].clone();
        }
        return copy;
    }


    static String formatMatrix(int[][] matrix) {
        if (matrix == null) {
            return "null";
        }
        return Arrays.deepToString(matrix);
    }


    static boolean matricesEqual(int[][] a, int[][] b) {

        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
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


    static int[][] randomMatrix(
            Random rng,
            int maxRows,
            int maxCols,
            int minValue,
            int maxValue) {

        int rows = 1 + rng.nextInt(maxRows);
        int cols = 1 + rng.nextInt(maxCols);

        int[][] matrix = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = minValue + rng.nextInt(maxValue - minValue + 1);
            }
        }

        return matrix;
    }


    /* **********************************************************************
     * Test Harness
     * **********************************************************************/


    static class TestCase {
        final String id;
        final int[][] matrix1;
        final int[][] matrix2;
        final int[][] expected;
        final String description;

        TestCase(
                String id,
                int[][] matrix1,
                int[][] matrix2,
                int[][] expected,
                String description) {

            this.id = id;
            this.matrix1 = matrix1;
            this.matrix2 = matrix2;
            this.expected = expected;
            this.description = description;
        }
    }


    @FunctionalInterface
    interface Algorithm {
        int[][] solve(int[][] a, int[][] b);
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


    static void runTests(
            String algorithm,
            Algorithm method,
            List<TestCase> tests) {

        System.out.println("======================================================");
        System.out.println(algorithm);
        System.out.println("======================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {
            try {
                int[][] actual =
                        method.solve(
                                cloneMatrix(test.matrix1),
                                cloneMatrix(test.matrix2));

                if (matricesEqual(actual, test.expected)) {

                    passed++;
                    System.out.printf("✓ %s (%s)%n", test.id, test.description);

                } else {

                    failed++;
                    System.out.printf("✗ %s (%s)%n", test.id, test.description);
                    System.out.printf("  matrix1   = %s%n", formatMatrix(test.matrix1));
                    System.out.printf("  matrix2   = %s%n", formatMatrix(test.matrix2));
                    System.out.printf("  expected  = %s%n", formatMatrix(test.expected));
                    System.out.printf("  actual    = %s%n", formatMatrix(actual));
                }

            } catch (Exception ex) {

                failed++;
                System.out.printf("✗ %s (%s)%n", test.id, test.description);
                System.out.printf("  matrix1   = %s%n", formatMatrix(test.matrix1));
                System.out.printf("  matrix2   = %s%n", formatMatrix(test.matrix2));
                System.out.printf("  exception = %s%n", ex);
            }
        }

        System.out.println();
        System.out.printf("Results: %d passed, %d failed, %d total%n", passed, failed, tests.size());
        System.out.println();
    }


    static int[][] expectedFrom(int[][] a, int[][] b) {
        return multiplyTwoMatricesTripleLoop(a, b);
    }


    /* **********************************************************************
     * Randomised Testing
     * **********************************************************************/


    static void runRandomisedTests(int iterations) {

        System.out.println("======================================================");
        System.out.println("Randomised Cross Checks");
        System.out.println("======================================================");

        Random rng = new Random(987654321L);

        for (int i = 1; i <= iterations; i++) {

            int rows1 = 1 + rng.nextInt(5);
            int cols1 = 1 + rng.nextInt(5);
            int rows2 = cols1;              // ensure compatibility: c1 == r2
            int cols2 = 1 + rng.nextInt(5);

            int[][] matrix1 = randomMatrix(rng, rows1, cols1, -5, 5);
            int[][] matrix2 = randomMatrix(rng, rows2, cols2, -5, 5);

            int[][] a =
                    multiplyTwoMatricesTripleLoop(
                            cloneMatrix(matrix1),
                            cloneMatrix(matrix2));

            int[][] b =
                    multiplyTwoMatricesRowColumn(
                            cloneMatrix(matrix1),
                            cloneMatrix(matrix2));

            if (!matricesEqual(a, b)) {

                System.out.println("Randomised test FAILED");
                System.out.println("matrix1 = " + formatMatrix(matrix1));
                System.out.println("matrix2 = " + formatMatrix(matrix2));
                System.out.println("m1      = " + formatMatrix(a));
                System.out.println("m2      = " + formatMatrix(b));
                return;
            }
        }

        System.out.printf("All %d Randomised tests passed.%n%n", iterations);
    }


    /* **********************************************************************
     * Main
     * **********************************************************************/


    public static void main(String[] args) {

        List<TestCase> tests = new ArrayList<>();

        /*
         * ============================================================
         * Standard Examples
         * ============================================================
         */

        tests.add(new TestCase(
                "S1",
                new int[][]{
                        {1, 2},
                        {3, 4}
                },
                new int[][]{
                        {5, 6},
                        {7, 8}
                },
                expectedFrom(
                        new int[][]{
                                {1, 2},
                                {3, 4}
                        },
                        new int[][]{
                                {5, 6},
                                {7, 8}
                        }),
                "Simple 2x2 × 2x2 example"));

        tests.add(new TestCase(
                "S2",
                new int[][]{
                        {1, 2, 3},
                        {4, 5, 6}
                },
                new int[][]{
                        {7, 8},
                        {9, 10},
                        {11, 12}
                },
                expectedFrom(
                        new int[][]{
                                {1, 2, 3},
                                {4, 5, 6}
                        },
                        new int[][]{
                                {7, 8},
                                {9, 10},
                                {11, 12}
                        }),
                "2x3 × 3x2 example"));

        /*
         * ============================================================
         * Identity / Zero Matrices
         * ============================================================
         */

        tests.add(new TestCase(
                "I1",
                new int[][]{
                        {1, 0},
                        {0, 1}
                },
                new int[][]{
                        {5, 6},
                        {7, 8}
                },
                expectedFrom(
                        new int[][]{
                                {1, 0},
                                {0, 1}
                        },
                        new int[][]{
                                {5, 6},
                                {7, 8}
                        }),
                "Identity matrix × arbitrary"));

        tests.add(new TestCase(
                "Z1",
                new int[][]{
                        {0, 0},
                        {0, 0}
                },
                new int[][]{
                        {5, 6},
                        {7, 8}
                },
                expectedFrom(
                        new int[][]{
                                {0, 0},
                                {0, 0}
                        },
                        new int[][]{
                                {5, 6},
                                {7, 8}
                        }),
                "Zero matrix × arbitrary"));

        /*
         * ============================================================
         * Negative Values
         * ============================================================
         */

        tests.add(new TestCase(
                "N1",
                new int[][]{
                        {-1, 2},
                        {3, -4}
                },
                new int[][]{
                        {5, -6},
                        {-7, 8}
                },
                expectedFrom(
                        new int[][]{
                                {-1, 2},
                                {3, -4}
                        },
                        new int[][]{
                                {5, -6},
                                {-7, 8}
                        }),
                "Mixed negative values"));

        /*
         * ============================================================
         * Edge Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "E1",
                null,
                new int[][]{{1}},
                null,
                "Null matrix1"));

        tests.add(new TestCase(
                "E2",
                new int[][]{{1}},
                null,
                null,
                "Null matrix2"));

        tests.add(new TestCase(
                "E3",
                new int[][]{},
                new int[][]{{1}},
                null,
                "Empty matrix1"));

        tests.add(new TestCase(
                "E4",
                new int[][]{{1}},
                new int[][]{},
                null,
                "Empty matrix2"));

        tests.add(new TestCase(
                "E5",
                new int[][]{{1, 2}},
                new int[][]{{3, 4}},
                null,
                "Dimension mismatch: 1x2 × 1x2"));

        System.out.println("############################################################");
        System.out.println("######## MULTIPLY TWO MATRICES (INTEGER) ###################");
        System.out.println("############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "Triple Loop",
                        MultiplyTwoMatricesTestHarness::multiplyTwoMatricesTripleLoop),

                new MethodCase(
                        "Row × Column",
                        MultiplyTwoMatricesTestHarness::multiplyTwoMatricesRowColumn)
        );

        for (MethodCase method : methods) {
            runTests(method.name, method.algorithm, tests);
        }

        runRandomisedTests(5000);
    }
}
