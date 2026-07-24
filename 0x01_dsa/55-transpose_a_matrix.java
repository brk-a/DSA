import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Transpose Matrix Test Harness.
 *
 * Problem:
 * Given an m x n integer matrix, return its transpose:
 *   result[j][i] = matrix[i][j]
 *
 * Implementations:
 *
 * 1. transposeMatrixBruteForce (Oracle)
 *    Handles any well-formed rectangular matrix (m x n).
 *
 * 2. transposeMatrixSquareMatrixOnly
 *    Assumes the input is square (n x n). Returns empty if not.
 *
 * The brute-force version acts as the correctness oracle for
 * randomised cross-checking of the square-only implementation
 * on square matrices.
 */
public class TransposeMatrixTestHarness {

    /* **********************************************************************
     * Core Algorithm Implementations
     * **********************************************************************/

    /**
     * Brute-force transpose for any rectangular matrix.
     *
     * If matrix is null, empty, or has zero columns,
     * returns an empty list.
     *
     * Time:  O(m * n)
     * Space: O(m * n) for the result
     */
    static ArrayList<ArrayList<Integer>> transposeMatrixBruteForce(int[][] matrix) {

        if (!validMatrix(matrix)) {
            return new ArrayList<>();
        }

        int m = matrix.length;
        int n = matrix[0].length;

        ArrayList<ArrayList<Integer>> result = new ArrayList<>();

        // result has n rows, each of length m
        for (int col = 0; col < n; col++) {

            ArrayList<Integer> row = new ArrayList<>();

            for (int rowIdx = 0; rowIdx < m; rowIdx++) {
                row.add(matrix[rowIdx][col]);
            }

            result.add(row);
        }

        return result;
    }

    /**
     * Transpose for square matrices only.
     *
     * If matrix is null or not square (rows != cols),
     * returns an empty list.
     *
     * Time:  O(n^2)
     * Space: O(n^2) for the result
     */
    static ArrayList<ArrayList<Integer>> transposeMatrixSquareMatrixOnly(int[][] matrix) {

        if (matrix == null
                || matrix.length == 0
                || matrix[0] == null
                || matrix.length != matrix[0].length) {

            return new ArrayList<>();
        }

        int n = matrix.length;
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();

        for (int col = 0; col < n; col++) {

            ArrayList<Integer> row = new ArrayList<>();

            for (int rowIdx = 0; rowIdx < n; rowIdx++) {
                row.add(matrix[rowIdx][col]);
            }

            result.add(row);
        }

        return result;
    }

    /* **********************************************************************
     * Matrix Validation
     * **********************************************************************/

    static boolean validMatrix(int[][] matrix) {

        if (matrix == null || matrix.length == 0) {
            return false;
        }

        if (matrix[0] == null || matrix[0].length == 0) {
            return false;
        }

        return true;
    }

    /* **********************************************************************
     * Test Harness Types
     * **********************************************************************/

    static class TestCase {

        final String id;
        final int[][] input;
        final ArrayList<ArrayList<Integer>> expected;
        final String description;

        TestCase(
                String id,
                int[][] input,
                ArrayList<ArrayList<Integer>> expected,
                String description) {

            this.id = id;
            this.input = input;
            this.expected = expected;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface TransposeAlgorithm {

        ArrayList<ArrayList<Integer>> solve(int[][] matrix);
    }

    static class MethodCase {

        final String name;
        final TransposeAlgorithm algorithm;

        MethodCase(
                String name,
                TransposeAlgorithm algorithm) {

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

    static String formatListMatrix(ArrayList<ArrayList<Integer>> matrix) {

        if (matrix == null) {
            return "null";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < matrix.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            ArrayList<Integer> row = matrix.get(i);
            if (row == null) {
                sb.append("null");
            } else {
                sb.append(row.toString());
            }
        }
        sb.append("]");
        return sb.toString();
    }

    static boolean listMatrixEqual(
            ArrayList<ArrayList<Integer>> a,
            ArrayList<ArrayList<Integer>> b) {

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

            ArrayList<Integer> rowA = a.get(i);
            ArrayList<Integer> rowB = b.get(i);

            if (rowA == null && rowB == null) {
                continue;
            }

            if (rowA == null || rowB == null) {
                return false;
            }

            if (rowA.size() != rowB.size()) {
                return false;
            }

            for (int j = 0; j < rowA.size(); j++) {
                if (!rowA.get(j).equals(rowB.get(j))) {
                    return false;
                }
            }
        }

        return true;
    }

    static void runTests(
            String algorithmName,
            TransposeAlgorithm method,
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

                ArrayList<ArrayList<Integer>> actual =
                        method.solve(cloneMatrix(test.input));

                if (listMatrixEqual(actual, test.expected)) {

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
                            formatListMatrix(test.expected));

                    System.out.printf(
                            "  actual    = %s%n",
                            formatListMatrix(actual));
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

    static void runRandomisedTests(int iterations) {

        System.out.println(
                "======================================================");
        System.out.println(
                "Randomised Cross Checks (Square-only vs Brute Force)");
        System.out.println(
                "======================================================");

        Random rng = new Random(987654321L);

        for (int i = 1; i <= iterations; i++) {

            int[][] matrix = randomSquareMatrix(
                    rng,
                    8,
                    -10,
                    10);

            if (!validMatrix(matrix)) {
                continue;
            }

            ArrayList<ArrayList<Integer>> oracle =
                    transposeMatrixBruteForce(
                            cloneMatrix(matrix));

            ArrayList<ArrayList<Integer>> candidate =
                    transposeMatrixSquareMatrixOnly(
                            cloneMatrix(matrix));

            if (!listMatrixEqual(oracle, candidate)) {

                System.out.println(
                        "Randomised test FAILED (Square-only vs Brute)");

                System.out.println(
                        "matrix    = " + formatMatrix(matrix));

                System.out.println(
                        "oracle    = " + formatListMatrix(oracle));

                System.out.println(
                        "candidate = " + formatListMatrix(candidate));

                return;
            }
        }

        System.out.printf(
                "All %d Randomised tests passed (Square-only vs Brute).%n%n",
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
                makeMatrix(List.of(List.of(1))),
                "Single element"));

        tests.add(new TestCase(
                "S2",
                new int[][]{{1, 2}},
                makeMatrix(List.of(
                        List.of(1),
                        List.of(2))),
                "1x2 matrix -> 2x1"));

        tests.add(new TestCase(
                "S3",
                new int[][]{{1}, {2}},
                makeMatrix(List.of(
                        List.of(1, 2))),
                "2x1 matrix -> 1x2"));

        tests.add(new TestCase(
                "S4",
                new int[][]{
                        {1, 2, 3},
                        {4, 5, 6}
                },
                makeMatrix(List.of(
                        List.of(1, 4),
                        List.of(2, 5),
                        List.of(3, 6))),
                "2x3 rectangular"));

        tests.add(new TestCase(
                "S5",
                new int[][]{
                        {1, 2, 3},
                        {4, 5, 6},
                        {7, 8, 9}
                },
                makeMatrix(List.of(
                        List.of(1, 4, 7),
                        List.of(2, 5, 8),
                        List.of(3, 6, 9))),
                "3x3 square"));

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
                "############ TRANSPOSE MATRIX (INT MATRIX) ################");
        System.out.println(
                "############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "Brute Force (Oracle)",
                        TransposeMatrixTestHarness::transposeMatrixBruteForce),

                new MethodCase(
                        "Square Matrix Only",
                        TransposeMatrixTestHarness::transposeMatrixSquareMatrixOnly)
        );

        for (MethodCase method : methods) {

            runTests(
                    method.name,
                    method.algorithm,
                    tests);
        }

        // Randomised tests only for square matrices
        runRandomisedTests(5000);
    }

    /**
     * Helper to build expected results from a list-of-lists literal.
     */
    static ArrayList<ArrayList<Integer>> makeMatrix(
            List<List<Integer>> rows) {

        ArrayList<ArrayList<Integer>> result = new ArrayList<>();

        for (List<Integer> row : rows) {
            result.add(new ArrayList<>(row));
        }

        return result;
    }
}
