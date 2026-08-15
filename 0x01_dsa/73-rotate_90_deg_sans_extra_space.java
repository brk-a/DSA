import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Rotate90DegSansExtraSpace {

    // Validation helper
    static boolean validMatrix(int[][] matrix) {
        return matrix != null
                && matrix.length > 0
                && matrix[0] != null
                && matrix[0].length > 0;
    }

    // Rotate A
    static int[][] rotate90DegSansExtraSpaceA(int[][] matrix) {
        if (!validMatrix(matrix)) {
            return null;
        }

        int m = matrix.length;
        int n = matrix[0].length;
        int[][] result = new int[n][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // clockwise rotation: (i, j) -> (j, m - 1 - i)
                result[j][m - 1 - i] = matrix[i][j];
            }
        }
        return result;
    }

    // Rotate B: first rotate then mirror rows by vertical flip to ensure clockwise result
    static int[][] rotate90DegSansExtraSpaceB(int[][] matrix) {
        if (!validMatrix(matrix)) {
            return null;
        }

        int m = matrix.length;
        int n = matrix[0].length;
        int[][] result = new int[n][m];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                // initial transpose-like placement
                result[j][i] = matrix[i][j];
            }
        }

        // vertical flip to achieve clockwise rotation for non-square could be adjusted;
        // here we perform a proper clockwise rotation by flipping each row after transpose
        for (int i = 0; i < n; i++) {
            for (int l = 0, r = m - 1; l < r; l++, r--) {
                int tmp = result[i][l];
                result[i][l] = result[i][r];
                result[i][r] = tmp;
            }
        }

        return result;
    }



    static class DiagonalResult {
        final int principal;
        final int secondary;
        final boolean valid;

        DiagonalResult(int p, int s, boolean v) {
            this.principal = p;
            this.secondary = s;
            this.valid = v;
        }

        @Override
        public String toString() {
            return "DiagonalResult{" +
                    "principal=" + principal +
                    ", secondary=" + secondary +
                    ", valid=" + valid +
                    '}';
        }
    }

    // Brute force oracle
    static DiagonalResult sumOfDiagonalsBruteForce(int[][] matrix) {
        if (!validMatrix(matrix)) {
            return new DiagonalResult(0, 0, false);
        }
        int m = matrix.length;
        int n = matrix[0].length;
        int principal = 0;
        int secondary = 0;
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (i == j) principal += matrix[i][j];
                if ((i + j) == (n - 1)) secondary += matrix[i][j];
            }
        }
        return new DiagonalResult(principal, secondary, true);
    }

    // Refactored single loop
    static DiagonalResult sumOfDiagonalsMatrix(int[][] matrix) {
        if (!validMatrix(matrix)) {
            return new DiagonalResult(0, 0, false);
        }
        int m = matrix.length;
        int n = matrix[0].length;
        int limit = Math.min(m, n);
        int principal = 0;
        int secondary = 0;
        for (int i = 0; i < limit; i++) {
            principal += matrix[i][i];
            int j = n - 1 - i;
            if (j >= 0 && j < n) {
                secondary += matrix[i][j];
            }
        }
        return new DiagonalResult(principal, secondary, true);
    }

    // Test harness types
    static class TestCase {
        final String id;
        final int[][] input;
        final DiagonalResult expected;
        final String description;

        TestCase(String id, int[][] input, DiagonalResult expected, String description) {
            this.id = id;
            this.input = input;
            this.expected = expected;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {
        DiagonalResult solve(int[][] matrix);
    }

    static class MethodCase {
        final String name;
        final Algorithm algorithm;

        MethodCase(String name, Algorithm algorithm) {
            this.name = name;
            this.algorithm = algorithm;
        }
    }

    // Utilities
    static int[][] cloneMatrix(int[][] matrix) {
        if (matrix == null) return null;
        int rows = matrix.length;
        int[][] copy = new int[rows][];
        for (int i = 0; i < rows; i++) {
            copy[i] = matrix[i] == null ? null : matrix[i].clone();
        }
        return copy;
    }

    static String formatMatrix(int[][] matrix) {
        if (matrix == null) return "null";
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < matrix.length; i++) {
            if (i > 0) sb.append(", ");
            sb.append(Arrays.toString(matrix[i]));
        }
        sb.append("]");
        return sb.toString();
    }

    static boolean resultsEqual(DiagonalResult a, DiagonalResult b) {
        if (a == null && b == null) return true;
        if (a == null || b == null) return false;
        return a.principal == b.principal
                && a.secondary == b.secondary
                && a.valid == b.valid;
    }

    static void runTests(String algorithmName, Algorithm method, List<TestCase> tests) {
        System.out.println("======================================================");
        System.out.println(algorithmName);
        System.out.println("======================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {
            try {
                DiagonalResult actual = method.solve(cloneMatrix(test.input));
                if (resultsEqual(actual, test.expected)) {
                    passed++;
                    System.out.printf("✓ %s (%s)%n", test.id, test.description);
                } else {
                    failed++;
                    System.out.printf("✗ %s (%s)%n", test.id, test.description);
                    System.out.printf("  input     = %s%n", formatMatrix(test.input));
                    System.out.printf("  expected  = %s%n", test.expected);
                    System.out.printf("  actual    = %s%n", actual);
                }
            } catch (Exception ex) {
                failed++;
                System.out.printf("✗ %s (%s)%n", test.id, test.description);
                System.out.printf("  input     = %s%n", formatMatrix(test.input));
                System.out.printf("  exception = %s%n", ex);
            }
        }

        System.out.println();
        System.out.printf("Results: %d passed, %d failed, %d total%n", passed, failed, tests.size());
        System.out.println();
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
        System.out.println("======================================================");
        System.out.println("Randomised Cross Checks (Brute vs Refactored)");
        System.out.println("======================================================");
        Random rng = new Random(987654321L);

        for (int i = 1; i <= iterations; i++) {
            int[][] matrix = randomMatrix(rng, 6, 6, -10, 20);
            if (!validMatrix(matrix)) continue;

            DiagonalResult brute = sumOfDiagonalsBruteForce(cloneMatrix(matrix));
            DiagonalResult refactored = sumOfDiagonalsMatrix(cloneMatrix(matrix));

            if (!resultsEqual(brute, refactored)) {
                System.out.println("Randomised test FAILED");
                System.out.println("matrix      = " + formatMatrix(matrix));
                System.out.println("brute       = " + brute);
                System.out.println("refactored  = " + refactored);
                return;
            }
        }

        System.out.printf("All %d Randomised tests passed.%n%n", iterations);
    }

    public static void main(String[] args) {
        List<TestCase> tests = new ArrayList<>();

        // S1: 3x3
        tests.add(new TestCase(
                "S1",
                new int[][]{
                        {1, 2, 3},
                        {4, 5, 6},
                        {7, 8, 9}
                },
                new DiagonalResult(1 + 5 + 9, 3 + 5 + 7, true),
                "3x3 square matrix"));

        // S2: 4x4
        tests.add(new TestCase(
                "S2",
                new int[][]{
                        {1, 2, 3, 4},
                        {5, 6, 7, 8},
                        {9, 10, 11, 12},
                        {13, 14, 15, 16}
                },
                new DiagonalResult(1 + 6 + 11 + 16, 4 + 7 + 10 + 13, true),
                "4x4 square matrix"));

        // S3: 1x1
        tests.add(new TestCase(
                "S3",
                new int[][]{{10}},
                new DiagonalResult(10, 10, true),
                "1x1 matrix (both diagonals same element)"));

        // W1: 2x4
        tests.add(new TestCase(
                "W1",
                new int[][]{
                        {1, 2, 3, 4},
                        {5, 6, 7, 8}
                },
                new DiagonalResult(1 + 6, 4 + 7, true),
                "2x4 wide matrix"));

        // W2: 2x3
        tests.add(new TestCase(
                "W2",
                new int[][]{
                        {1, 2, 3},
                        {4, 5, 6}
                },
                new DiagonalResult(1 + 5, 3 + 5, true),
                "2x3 wide matrix"));

        // T1: 3x2
        tests.add(new TestCase(
                "T1",
                new int[][]{
                        {1, 2},
                        {3, 4},
                        {5, 6}
                },
                new DiagonalResult(1 + 4, 2 + 3, true),
                "3x2 tall matrix"));

        // T2: 4x3
        tests.add(new TestCase(
                "T2",
                new int[][]{
                        {1, 2, 3},
                        {4, 5, 6},
                        {7, 8, 9},
                        {10, 11, 12}
                },
                new DiagonalResult(1 + 5 + 9, 3 + 5 + 7, true),
                "4x3 tall matrix"));

        // Negatives
        tests.add(new TestCase(
                "M1",
                new int[][]{
                        {-1, -2, -3},
                        {-4, -5, -6},
                        {-7, -8, -9}
                },
                new DiagonalResult(-1 + -5 + -9, -3 + -5 + -7, true),
                "3x3 with negatives"));

        // Zeros
        tests.add(new TestCase(
                "M2",
                new int[][]{
                        {0, 0, 0},
                        {0, 0, 0},
                        {0, 0, 0}
                },
                new DiagonalResult(0, 0, true),
                "3x3 all zeros"));

        // Edge null
        tests.add(new TestCase("E1", null, new DiagonalResult(0, 0, false), "Null matrix"));

        // Edge empty
        tests.add(new TestCase("E2", new int[][]{}, new DiagonalResult(0, 0, false), "Empty matrix (zero rows)"));

        // Edge zero columns
        tests.add(new TestCase("E3", new int[][]{{}, {}, {}}, new DiagonalResult(0, 0, false), "Matrix with zero columns"));

        System.out.println("############################################################");
        System.out.println("######## DIAGONALS IN MATRIX (INT[][]) #############");
        System.out.println("############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(
                new MethodCase("Brute Force (Oracle)", Rotate90DegSansExtraSpace::sumOfDiagonalsBruteForce),
                new MethodCase("Refactored Single Loop O(min(m, n))", Rotate90DegSansExtraSpace::sumOfDiagonalsMatrix)
        );

        for (MethodCase method : methods) {
            runTests(method.name, method.algorithm, tests);
        }

        runRandomisedTests(5000);
    }
}
