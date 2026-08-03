import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Common Elements in All Rows of a Matrix.
 *
 * Problem:
 * Given an m x n integer matrix, find all distinct elements that appear
 * in every row of the matrix.
 *
 * - Duplicates within a row do not count multiple times.
 * - If no element is common to all rows, return an empty list.
 * - If the matrix is invalid (null / empty), return null.
 *
 * Implementations:
 *
 * 1. Brute Force (Oracle)
 *      For each distinct element in the first row, check if it appears
 *      in every other row by scanning each row.
 *
 * 2. Hash Map Single Traversal (O(m*n))
 *      - Insert all elements of the first row into a map with value 1.
 *      - For each subsequent row i (1..m-1), for each element x:
 *          if map[x] == i, set map[x] = i + 1.
 *      - At the end, elements with map[x] == m are common to all rows.
 *
 * The brute-force implementation is retained as a correctness oracle
 * for deterministic and randomised testing.
 */
public class CommonElementRowsInMatrixTestHarness {

    /* **********************************************************************
     * Validation Helpers
     * **********************************************************************/

    static boolean validMatrix(int[][] matrix) {
        return matrix != null
                && matrix.length > 0
                && matrix[0] != null
                && matrix[0].length > 0;
    }

    /* **********************************************************************
     * Algorithm Implementations
     * **********************************************************************/

    /**
     * Brute Force Oracle.
     *
     * For each distinct element in the first row, check if it appears
     * in every other row.
     *
     * Time: O(m * n * distinct_in_first_row) ~ O(m * n^2) worst-case.
     */
    static ArrayList<Integer> commonElementRowsInMatrixBruteForce(int[][] matrix) {

        if (!validMatrix(matrix)) {
            return null;
        }

        int m = matrix.length;
        int n = matrix[0].length;

        // Use a map to track presence in all rows
        Map<Integer, Boolean> candidateMap = new HashMap<>();

        // Initialise candidates from first row (distinct)
        for (int j = 0; j < n; j++) {
            candidateMap.put(matrix[0][j], true);
        }

        // For each subsequent row, mark candidates false if not present
        for (int i = 1; i < m; i++) {

            // Track which candidates are present in this row
            Map<Integer, Boolean> presentInRow = new HashMap<>();
            for (int j = 0; j < n; j++) {
                int val = matrix[i][j];
                if (candidateMap.containsKey(val)) {
                    presentInRow.put(val, true);
                }
            }

            // Remove candidates not present in this row
            for (Map.Entry<Integer, Boolean> entry : candidateMap.entrySet()) {
                if (!presentInRow.containsKey(entry.getKey())) {
                    entry.setValue(false);
                }
            }
        }

        // Collect candidates still marked true
        ArrayList<Integer> result = new ArrayList<>();
        for (Map.Entry<Integer, Boolean> entry : candidateMap.entrySet()) {
            if (entry.getValue()) {
                result.add(entry.getKey());
            }
        }

        return result;
    }

    /**
     * Hash Map Single Traversal (Optimal).
     *
     * Time: O(m * n)
     * Space: O(n) extra for the map.
     */
    static ArrayList<Integer> commonElementRowsInMatrixHashMap(int[][] matrix) {

        if (!validMatrix(matrix)) {
            return null;
        }

        int m = matrix.length;
        int n = matrix[0].length;

        Map<Integer, Integer> mp = new HashMap<>();

        // Initialise map with first row elements -> count 1
        for (int j = 0; j < n; j++) {
            mp.put(matrix[0][j], 1);
        }

        // Process remaining rows
        for (int i = 1; i < m; i++) {

            for (int j = 0; j < n; j++) {

                int val = matrix[i][j];

                if (mp.containsKey(val) && mp.get(val) == i) {
                    mp.put(val, i + 1);
                }
            }
        }

        // Collect elements that appeared in all m rows
        ArrayList<Integer> result = new ArrayList<>();
        for (Map.Entry<Integer, Integer> entry : mp.entrySet()) {
            if (entry.getValue() == m) {
                result.add(entry.getKey());
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

    static boolean listsEqualUnordered(ArrayList<Integer> a, ArrayList<Integer> b) {

        if (a == null && b == null) {
            return true;
        }

        if (a == null || b == null) {
            return false;
        }

        if (a.size() != b.size()) {
            return false;
        }

        // Compare as multisets (order doesn't matter, but duplicates do)
        Map<Integer, Integer> countA = new HashMap<>();
        for (int x : a) {
            countA.put(x, countA.getOrDefault(x, 0) + 1);
        }

        Map<Integer, Integer> countB = new HashMap<>();
        for (int x : b) {
            countB.put(x, countB.getOrDefault(x, 0) + 1);
        }

        return countA.equals(countB);
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

                if (listsEqualUnordered(actual, test.expected)) {

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

        int rows = rng.nextInt(maxRows + 1);

        if (rows == 0) {
            return new int[0][];
        }

        int cols = rng.nextInt(maxCols + 1);

        if (cols == 0) {
            int[][] m = new int[rows][];
            for (int i = 0; i < rows; i++) {
                m[i] = new int[0];
            }
            return m;
        }

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
                "Randomised Cross Checks (Brute vs HashMap)");
        System.out.println(
                "======================================================");

        Random rng = new Random(987654321L);

        for (int i = 1; i <= iterations; i++) {

            int[][] matrix = randomMatrix(
                    rng,
                    5,
                    6,
                    -5,
                    10);

            if (!validMatrix(matrix)) {
                continue;
            }

            ArrayList<Integer> brute =
                    commonElementRowsInMatrixBruteForce(
                            cloneMatrix(matrix));

            ArrayList<Integer> hashMap =
                    commonElementRowsInMatrixHashMap(
                            cloneMatrix(matrix));

            if (!listsEqualUnordered(brute, hashMap)) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "matrix   = " + formatMatrix(matrix));

                System.out.println(
                        "brute    = " + formatList(brute));

                System.out.println(
                        "hashMap  = " + formatList(hashMap));

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
         * Example from GeeksforGeeks
         * ============================================================
         */

        tests.add(new TestCase(
                "G1",
                new int[][]{
                        {1, 2, 1, 4, 8},
                        {3, 7, 8, 5, 1},
                        {8, 7, 7, 3, 1},
                        {8, 1, 2, 7, 9}
                },
                new ArrayList<>(Arrays.asList(1, 8)),
                "GFG example: common elements 1 and 8"));

        /*
         * ============================================================
         * Small / Simple Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "A1",
                new int[][]{
                        {1, 2, 3},
                        {2, 3, 4},
                        {3, 4, 5}
                },
                new ArrayList<>(Arrays.asList(3)),
                "Single common element"));

        tests.add(new TestCase(
                "A2",
                new int[][]{
                        {1, 2, 3},
                        {4, 5, 6},
                        {7, 8, 9}
                },
                new ArrayList<>(),
                "No common elements"));

        tests.add(new TestCase(
                "A3",
                new int[][]{
                        {2, 2, 2},
                        {2, 2, 2},
                        {2, 2, 2}
                },
                new ArrayList<>(Arrays.asList(2)),
                "All same element"));

        tests.add(new TestCase(
                "A4",
                new int[][]{
                        {1, 2, 3},
                        {1, 2, 3},
                        {1, 2, 3}
                },
                new ArrayList<>(Arrays.asList(1, 2, 3)),
                "All rows identical"));

        tests.add(new TestCase(
                "A5",
                new int[][]{
                        {1, 1, 2},
                        {1, 2, 2},
                        {1, 2, 3}
                },
                new ArrayList<>(Arrays.asList(1, 2)),
                "Duplicates within rows"));

        /*
         * ============================================================
         * Single Row / Single Column
         * ============================================================
         */

        tests.add(new TestCase(
                "S1",
                new int[][]{
                        {5, 6, 7}
                },
                new ArrayList<>(Arrays.asList(5, 6, 7)),
                "Single row: all elements are common"));

        tests.add(new TestCase(
                "S2",
                new int[][]{
                        {1},
                        {1},
                        {1}
                },
                new ArrayList<>(Arrays.asList(1)),
                "Single column, all same"));

        tests.add(new TestCase(
                "S3",
                new int[][]{
                        {1},
                        {2},
                        {3}
                },
                new ArrayList<>(),
                "Single column, all different"));

        /*
         * ============================================================
         * Edge Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "E1",
                null,
                null,
                "Null matrix"));

        tests.add(new TestCase(
                "E2",
                new int[][]{},
                null,
                "Empty matrix (zero rows)"));

        tests.add(new TestCase(
                "E3",
                new int[][]{
                        {},
                        {},
                        {}
                },
                null,
                "Matrix with zero columns"));

        System.out.println(
                "############################################################");
        System.out.println(
                "######## COMMON ELEMENTS IN ALL ROWS OF MATRIX ############");
        System.out.println(
                "############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "Brute Force (Oracle)",
                        CommonElementRowsInMatrixTestHarness::commonElementRowsInMatrixBruteForce),

                new MethodCase(
                        "Hash Map Single Traversal O(m*n)",
                        CommonElementRowsInMatrixTestHarness::commonElementRowsInMatrixHashMap)
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
