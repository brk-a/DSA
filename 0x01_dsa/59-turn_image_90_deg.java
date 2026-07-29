import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Rotate Image 90 Degrees Clockwise.
 *
 * Implementations:
 *
 * 1. Temporary Matrix
 *      O(n * m)
 *
 * 2. Direct Index Mapping
 *      O(n * m)
 *
 * Both methods return a new matrix as ArrayList<ArrayList<Integer>>.
 */
public class RotateImage90DegreesTestHarness {


    /* **********************************************************************
     * Validation
     * **********************************************************************/


    static boolean validMatrix(int[][] matrix) {
        return matrix != null && matrix.length > 0 && matrix[0].length > 0;
    }


    static boolean isRectangular(int[][] matrix) {
        if (matrix == null || matrix.length == 0) {
            return false;
        }

        int m = matrix[0].length;
        for (int i = 1; i < matrix.length; i++) {
            if (matrix[i] == null || matrix[i].length != m) {
                return false;
            }
        }

        return true;
    }


    /* **********************************************************************
     * Algorithms
     * **********************************************************************/


    static ArrayList<ArrayList<Integer>> rotateImage90Deg1(int[][] matrix) {
        if (!validMatrix(matrix) || !isRectangular(matrix)) {
            return new ArrayList<>();
        }

        int n = matrix.length;
        int m = matrix[0].length;

        int[][] rotated = new int[m][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                rotated[j][n - 1 - i] = matrix[i][j];
            }
        }

        return toListMatrix(rotated);
    }


    static ArrayList<ArrayList<Integer>> rotateImage90Deg2(int[][] matrix) {
        if (!validMatrix(matrix) || !isRectangular(matrix)) {
            return new ArrayList<>();
        }

        int n = matrix.length;
        int m = matrix[0].length;

        ArrayList<ArrayList<Integer>> result = new ArrayList<>();

        for (int col = 0; col < m; col++) {
            ArrayList<Integer> row = new ArrayList<>();
            for (int rowIndex = n - 1; rowIndex >= 0; rowIndex--) {
                row.add(matrix[rowIndex][col]);
            }
            result.add(row);
        }

        return result;
    }


    /* **********************************************************************
     * Helpers
     * **********************************************************************/


    static ArrayList<ArrayList<Integer>> toListMatrix(int[][] matrix) {
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();

        for (int i = 0; i < matrix.length; i++) {
            ArrayList<Integer> row = new ArrayList<>();
            for (int j = 0; j < matrix[i].length; j++) {
                row.add(matrix[i][j]);
            }
            result.add(row);
        }

        return result;
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


    static String formatListMatrix(ArrayList<ArrayList<Integer>> matrix) {
        if (matrix == null) {
            return "null";
        }
        return matrix.toString();
    }


    static boolean listMatricesEqual(
            ArrayList<ArrayList<Integer>> a,
            ArrayList<ArrayList<Integer>> b) {

        if (a == b) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        if (a.size() != b.size()) {
            return false;
        }

        for (int i = 0; i < a.size(); i++) {
            if (a.get(i).size() != b.get(i).size()) {
                return false;
            }
            for (int j = 0; j < a.get(i).size(); j++) {
                if (!a.get(i).get(j).equals(b.get(i).get(j))) {
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
        final int[][] matrix;
        final ArrayList<ArrayList<Integer>> expected;
        final String description;

        TestCase(
                String id,
                int[][] matrix,
                ArrayList<ArrayList<Integer>> expected,
                String description) {
            this.id = id;
            this.matrix = matrix;
            this.expected = expected;
            this.description = description;
        }
    }


    @FunctionalInterface
    interface Algorithm {
        ArrayList<ArrayList<Integer>> solve(int[][] matrix);
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
                ArrayList<ArrayList<Integer>> actual =
                        method.solve(cloneMatrix(test.matrix));

                if (listMatricesEqual(actual, test.expected)) {
                    passed++;
                    System.out.printf("✓ %s (%s)%n", test.id, test.description);
                } else {
                    failed++;
                    System.out.printf("✗ %s (%s)%n", test.id, test.description);
                    System.out.printf("  matrix    = %s%n", formatMatrix(test.matrix));
                    System.out.printf("  expected  = %s%n", formatListMatrix(test.expected));
                    System.out.printf("  actual    = %s%n", formatListMatrix(actual));
                }
            } catch (Exception ex) {
                failed++;
                System.out.printf("✗ %s (%s)%n", test.id, test.description);
                System.out.printf("  matrix    = %s%n", formatMatrix(test.matrix));
                System.out.printf("  exception = %s%n", ex);
            }
        }

        System.out.println();
        System.out.printf("Results: %d passed, %d failed, %d total%n", passed, failed, tests.size());
        System.out.println();
    }


    static ArrayList<ArrayList<Integer>> expectedFrom(int[][] matrix) {
        return rotateImage90Deg1(matrix);
    }


    static void runRandomisedTests(int iterations) {
        System.out.println("======================================================");
        System.out.println("Randomised Cross Checks");
        System.out.println("======================================================");

        Random rng = new Random(123456789L);

        for (int i = 1; i <= iterations; i++) {
            int[][] matrix = randomMatrix(rng, 6, 6, -9, 9);

            ArrayList<ArrayList<Integer>> a = rotateImage90Deg1(cloneMatrix(matrix));
            ArrayList<ArrayList<Integer>> b = rotateImage90Deg2(cloneMatrix(matrix));

            if (!listMatricesEqual(a, b)) {
                System.out.println("Randomised test FAILED");
                System.out.println("matrix = " + formatMatrix(matrix));
                System.out.println("m1     = " + formatListMatrix(a));
                System.out.println("m2     = " + formatListMatrix(b));
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
                        {1, 2, 3},
                        {4, 5, 6},
                        {7, 8, 9}
                },
                expectedFrom(new int[][]{
                        {1, 2, 3},
                        {4, 5, 6},
                        {7, 8, 9}
                }),
                "Classic 3x3 example"));

        tests.add(new TestCase(
                "S2",
                new int[][]{
                        {1, 2},
                        {3, 4}
                },
                expectedFrom(new int[][]{
                        {1, 2},
                        {3, 4}
                }),
                "Simple 2x2 matrix"));

        /*
         * ============================================================
         * Rectangular Matrices
         * ============================================================
         */

        tests.add(new TestCase(
                "R1",
                new int[][]{
                        {1, 2, 3},
                        {4, 5, 6}
                },
                expectedFrom(new int[][]{
                        {1, 2, 3},
                        {4, 5, 6}
                }),
                "2x3 matrix"));

        tests.add(new TestCase(
                "R2",
                new int[][]{
                        {1, 2},
                        {3, 4},
                        {5, 6}
                },
                expectedFrom(new int[][]{
                        {1, 2},
                        {3, 4},
                        {5, 6}
                }),
                "3x2 matrix"));

        /*
         * ============================================================
         * Single Row / Single Column
         * ============================================================
         */

        tests.add(new TestCase(
                "A1",
                new int[][]{
                        {1, 2, 3, 4}
                },
                expectedFrom(new int[][]{
                        {1, 2, 3, 4}
                }),
                "Single row"));

        tests.add(new TestCase(
                "A2",
                new int[][]{
                        {1},
                        {2},
                        {3},
                        {4}
                },
                expectedFrom(new int[][]{
                        {1},
                        {2},
                        {3},
                        {4}
                }),
                "Single column"));

        /*
         * ============================================================
         * Negative / Zero Values
         * ============================================================
         */

        tests.add(new TestCase(
                "N1",
                new int[][]{
                        {0, -1},
                        {-2, 3}
                },
                expectedFrom(new int[][]{
                        {0, -1},
                        {-2, 3}
                }),
                "Mixed zero and negative values"));

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
                "Empty matrix"));

        tests.add(new TestCase(
                "E3",
                new int[][]{{}},
                new ArrayList<>(),
                "Empty row"));

        System.out.println("############################################################");
        System.out.println("######## ROTATE IMAGE 90 DEGREES CLOCKWISE #################");
        System.out.println("############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(
                new MethodCase(
                        "Temporary Matrix",
                        RotateImage90DegreesTestHarness::rotateImage90Deg1),
                new MethodCase(
                        "Direct Index Mapping",
                        RotateImage90DegreesTestHarness::rotateImage90Deg2)
        );

        for (MethodCase method : methods) {
            runTests(method.name, method.algorithm, tests);
        }

        runRandomisedTests(5000);
    }
}
