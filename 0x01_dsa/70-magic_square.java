import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Magic Square Test Harness.
 *
 * A magic square is an n x n grid containing all integers from 1 to n^2 exactly once,
 * such that every row, every column, and both main diagonals have the same sum.[web:32][web:39]
 *
 * Implementations:
 *
 * 1. magicSquareBruteForce
 *    - Uses the first row's sum as the target.
 *    - Checks uniqueness with an int[] visited of size n^2 + 1.
 *    - Validates rows, columns, and diagonals explicitly.
 *
 * 2. magicSquareOnePass
 *    - Uses the closed-form magic sum:
 *          target = n * (n^2 + 1) / 2.[web:32]
 *    - Single pass that tracks row/column sums and diagonals,
 *      plus uniqueness via boolean[] visited.
 *
 * Both require:
 *  - Non-null, square matrix
 *  - All rows non-null and same length
 */
public class MagicSquareTestHarness {

    /* **********************************************************************
     * Core Validation
     * **********************************************************************/

    private static boolean validMatrix(int[][] matrix) {
        if (matrix == null || matrix.length == 0) {
            return false;
        }
        int n = matrix.length;
        if (matrix[0] == null || matrix[0].length == 0) {
            return false;
        }
        // must be square and all rows same length
        if (matrix[0].length != n) {
            return false;
        }
        for (int i = 1; i < n; i++) {
            if (matrix[i] == null || matrix[i].length != n) {
                return false;
            }
        }
        return true;
    }

    /* **********************************************************************
     * Implementation 1: Brute Force
     * **********************************************************************/

    static boolean magicSquareBruteForce(int[][] matrix) {

        if (!validMatrix(matrix)) {
            return false;
        }

        int n = matrix.length;

        // target sum from first row
        int target = 0;
        for (int j = 0; j < n; j++) {
            target += matrix[0][j];
        }

        int maxVal = n * n;
        int[] visited = new int[maxVal + 1];

        // check rows, columns, and uniqueness
        for (int i = 0; i < n; i++) {

            int rowSum = 0;
            int colSum = 0;

            for (int j = 0; j < n; j++) {

                rowSum += matrix[i][j];
                colSum += matrix[j][i];

                int val = matrix[i][j];

                // value must be in [1, n^2] and unique
                if (val < 1 || val > maxVal || visited[val] == 1) {
                    return false;
                }
                visited[val] = 1;
            }

            if (rowSum != target || colSum != target) {
                return false;
            }
        }

        // diagonal sums
        int d1 = 0;
        int d2 = 0;

        for (int i = 0; i < n; i++) {
            d1 += matrix[i][i];
            d2 += matrix[i][n - 1 - i];
        }

        return d1 == target && d2 == target;
    }

    /* **********************************************************************
     * Implementation 2: One Pass with Formula Target
     * **********************************************************************/

    static boolean magicSquareOnePass(int[][] matrix) {

        if (!validMatrix(matrix)) {
            return false;
        }

        int n = matrix.length;

        // Magic constant formula: n * (n^2 + 1) / 2.[web:32]
        int target = n * (n * n + 1) / 2;

        boolean[] visited = new boolean[n * n + 1];

        int d1 = 0;
        int d2 = 0;

        for (int i = 0; i < n; i++) {

            int rowSum = 0;
            int colSum = 0;

            for (int j = 0; j < n; j++) {

                int valRow = matrix[i][j];
                int valCol = matrix[j][i];

                // Range + duplicate check on row value
                if (valRow < 1 || valRow > n * n || visited[valRow]) {
                    return false;
                }
                visited[valRow] = true;

                rowSum += valRow;
                colSum += valCol;

                // main diagonal
                if (i == j) {
                    d1 += valRow;
                }
                // secondary diagonal
                if (i + j == n - 1) {
                    d2 += valRow;
                }
            }

            if (rowSum != target || colSum != target) {
                return false;
            }
        }

        return d1 == target && d2 == target;
    }

    /* **********************************************************************
     * Test Harness Types
     * **********************************************************************/

    static class TestCase {

        final String id;
        final int[][] input;
        final boolean expected;
        final String description;

        TestCase(
                String id,
                int[][] input,
                boolean expected,
                String description) {

            this.id = id;
            this.input = input;
            this.expected = expected;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface MagicAlgorithm {
        boolean solve(int[][] matrix);
    }

    static class MethodCase {

        final String name;
        final MagicAlgorithm algorithm;

        MethodCase(
                String name,
                MagicAlgorithm algorithm) {

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

    static void runTests(
            String algorithmName,
            MagicAlgorithm method,
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

                boolean actual =
                        method.solve(
                                cloneMatrix(test.input));

                if (actual == test.expected) {

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
                        "  expected  = %s%n",
                        test.expected);

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

    static int[][] randomPermutationMatrix(
            Random rng,
            int n) {

        if (n <= 0) {
            return new int[0][0];
        }

        int total = n * n;
        int[] vals = new int[total];
        for (int i = 0; i < total; i++) {
            vals[i] = i + 1;
        }

        // Fisher-Yates shuffle
        for (int i = total - 1; i > 0; i--) {
            int j = rng.nextInt(i + 1);
            int tmp = vals[i];
            vals[i] = vals[j];
            vals[j] = tmp;
        }

        int[][] matrix = new int[n][n];
        int idx = 0;
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                matrix[i][j] = vals[idx++];
            }
        }

        return matrix;
    }

    static void runRandomisedTests(int iterations, int n) {

        System.out.println(
                "======================================================");
        System.out.println(
                "Randomised Cross Checks (OnePass vs BruteForce)");
        System.out.println(
                "======================================================");

        Random rng = new Random(135792468L);

        for (int i = 1; i <= iterations; i++) {

            int[][] matrix = randomPermutationMatrix(
                    rng,
                    n);

            boolean brute =
                    magicSquareBruteForce(
                            cloneMatrix(matrix));

            boolean onePass =
                    magicSquareOnePass(
                            cloneMatrix(matrix));

            if (brute != onePass) {

                System.out.println(
                        "Randomised test FAILED (OnePass vs BruteForce)");

                System.out.println(
                        "matrix    = " + formatMatrix(matrix));

                System.out.println(
                        "brute     = " + brute);

                System.out.println(
                        "onePass   = " + onePass);

                return;
            }
        }

        System.out.printf(
                "All %d Randomised tests passed for n = %d.%n%n",
                iterations,
                n);
    }

    /* **********************************************************************
     * Main
     * **********************************************************************/

    public static void main(String[] args) {

        List<TestCase> tests = new ArrayList<>();

        /*
         * ============================================================
         * Classic Magic Squares and Non-magic Examples
         * ============================================================
         */

        // Lo Shu 3x3 magic square (target = 15)
        int[][] loShu = {
                {8, 1, 6},
                {3, 5, 7},
                {4, 9, 2}
        };

        tests.add(new TestCase(
                "M1",
                loShu,
                true,
                "Lo Shu 3x3 magic square"));

        // Same numbers but rearranged (not magic)
        int[][] nonMagic = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        tests.add(new TestCase(
                "M2",
                nonMagic,
                false,
                "3x3 non-magic square"));

        // Duplicate values (invalid)
        int[][] dupValues = {
                {2, 2, 2},
                {2, 2, 2},
                {2, 2, 2}
        };

        tests.add(new TestCase(
                "M3",
                dupValues,
                false,
                "Duplicates, not a magic square"));

        // Out-of-range values (e.g. 0 and > n^2)
        int[][] outOfRange = {
                {0, 10, 5},
                {3, 5, 7},
                {4, 9, 2}
        };

        tests.add(new TestCase(
                "M4",
                outOfRange,
                false,
                "Values outside 1..n^2"));

        // Non-square matrix should be rejected
        int[][] nonSquare = {
                {1, 2, 3},
                {4, 5, 6}
        };

        tests.add(new TestCase(
                "E1",
                nonSquare,
                false,
                "Non-square matrix"));

        // Null matrix
        tests.add(new TestCase(
                "E2",
                null,
                false,
                "Null matrix"));

        System.out.println(
                "############################################################");
        System.out.println(
                "################### MAGIC SQUARE CHECK #####################");
        System.out.println(
                "############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "Brute Force",
                        MagicSquareTestHarness::magicSquareBruteForce),

                new MethodCase(
                        "One Pass (Formula Target)",
                        MagicSquareTestHarness::magicSquareOnePass)
        );

        for (MethodCase method : methods) {

            runTests(
                    method.name,
                    method.algorithm,
                    tests);
        }

        // Randomised cross-checks on 3x3
        runRandomisedTests(5000, 3);
    }
}
