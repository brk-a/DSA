import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;

/**
 * Minimum Points to Reach Destination.
 *
 * Problem:
 * Given a matrix of integer points, start at (0, 0) and reach
 * (m - 1, n - 1), moving only RIGHT or DOWN.
 *
 * A player's current points are increased/decreased by the value
 * of every cell visited.
 *
 * Find the minimum number of points required initially so that the
 * player can reach the destination while never having fewer than
 * 1 point.
 *
 * Example:
 *
 *     {-2, -3,  3}
 *     {-5, -10, 1}
 *     {10, 30, -5}
 *
 * Answer = 7
 *
 * Implementations:
 *
 * 1. Plain Recursion
 *    Direct top-down recurrence.
 *    Time:  O(2^(m+n)) in the worst case
 *    Space: O(m+n) recursion stack
 *
 * 2. Memoisation
 *    Same recurrence, but cache each cell.
 *    Time:  O(m*n)
 *    Space: O(m*n)
 *
 * 3. Tabulation
 *    Bottom-up dynamic programming.
 *    Time:  O(m*n)
 *    Space: O(m*n)
 *
 * 4. Dijkstra / Widest-Path
 *    Reformulates the problem around the best possible minimum
 *    running score along a path.
 *
 *    For a path, let prefixScore be the score after each visited cell.
 *    The initial score required is:
 *
 *        max(1, 1 - minimumPrefixScore)
 *
 *    Therefore we want to maximize the minimum prefix score.
 *
 *    This is a maximum-bottleneck / widest-path problem. A priority
 *    queue processes the currently strongest path first.
 *
 *    Time:  O(m*n log(m*n))
 *    Space: O(m*n)
 *
 * All implementations are cross-checked using fixed and randomized
 * test cases.
 */
public class MinimumPointsToReachDestination {

    /*
     * long is used instead of int for the answer so that values such
     * as Integer.MIN_VALUE cannot overflow when converted to the
     * required initial points.
     */
    static record Result(long steps, boolean valid) {}

    /* **********************************************************************
     * Validation
     * **********************************************************************/

    /**
     * A valid matrix must:
     * - not be null
     * - contain at least one row
     * - contain a non-null first row
     * - contain at least one column
     * - contain only non-null rows
     * - be rectangular
     */
    static boolean validMatrix(int[][] matrix) {

        if (matrix == null || matrix.length == 0) {
            return false;
        }

        if (matrix[0] == null || matrix[0].length == 0) {
            return false;
        }

        int columns = matrix[0].length;

        for (int[] row : matrix) {

            if (row == null || row.length != columns) {
                return false;
            }
        }

        return true;
    }

    /* **********************************************************************
     * Shared Helpers
     * **********************************************************************/

    /**
     * Converts the best possible minimum prefix score into the minimum
     * initial points required.
     */
    static long requiredInitialPoints(long minimumPrefixScore) {
        return Math.max(1L, 1L - minimumPrefixScore);
    }

    /**
     * Returns the required initial points for the destination cell alone.
     */
    static long destinationRequirement(int value) {
        return Math.max(1L, 1L - (long) value);
    }

    /* **********************************************************************
     * 1. Plain Recursion
     * **********************************************************************/

    static Result minimumPointsToReachDestinationRecursion(int[][] matrix) {

        if (!validMatrix(matrix)) {
            return new Result(-1, false);
        }

        return pointsRecursion(0, 0, matrix);
    }

    static Result pointsRecursion(int i, int j, int[][] matrix) {

        int m = matrix.length;
        int n = matrix[0].length;

        if (i == m - 1 && j == n - 1) {
            return new Result(
                    destinationRequirement(matrix[i][j]),
                    true);
        }

        long bestRequirement;

        if (i == m - 1) {

            // Last row: only RIGHT is possible.
            bestRequirement =
                    pointsRecursion(i, j + 1, matrix).steps();

        } else if (j == n - 1) {

            // Last column: only DOWN is possible.
            bestRequirement =
                    pointsRecursion(i + 1, j, matrix).steps();

        } else {

            long right =
                    pointsRecursion(i, j + 1, matrix).steps();

            long down =
                    pointsRecursion(i + 1, j, matrix).steps();

            bestRequirement = Math.min(right, down);
        }

        long requiredHere =
                Math.max(
                        1L,
                        bestRequirement - matrix[i][j]);

        return new Result(requiredHere, true);
    }

    /* **********************************************************************
     * 2. Memoisation
     * **********************************************************************/

    static Result minimumPointsToReachDestinationMemoisation(int[][] matrix) {

        if (!validMatrix(matrix)) {
            return new Result(-1, false);
        }

        int m = matrix.length;
        int n = matrix[0].length;

        long[][] memo = new long[m][n];
        boolean[][] computed = new boolean[m][n];

        return pointsMemoisation(
                0,
                0,
                matrix,
                memo,
                computed);
    }

    static Result pointsMemoisation(
            int i,
            int j,
            int[][] matrix,
            long[][] memo,
            boolean[][] computed) {

        int m = matrix.length;
        int n = matrix[0].length;

        if (i == m - 1 && j == n - 1) {

            long result =
                    destinationRequirement(matrix[i][j]);

            memo[i][j] = result;
            computed[i][j] = true;

            return new Result(result, true);
        }

        if (computed[i][j]) {
            return new Result(memo[i][j], true);
        }

        long bestRequirement;

        if (i == m - 1) {

            bestRequirement =
                    pointsMemoisation(
                            i,
                            j + 1,
                            matrix,
                            memo,
                            computed).steps();

        } else if (j == n - 1) {

            bestRequirement =
                    pointsMemoisation(
                            i + 1,
                            j,
                            matrix,
                            memo,
                            computed).steps();

        } else {

            long right =
                    pointsMemoisation(
                            i,
                            j + 1,
                            matrix,
                            memo,
                            computed).steps();

            long down =
                    pointsMemoisation(
                            i + 1,
                            j,
                            matrix,
                            memo,
                            computed).steps();

            bestRequirement = Math.min(right, down);
        }

        long result =
                Math.max(
                        1L,
                        bestRequirement - matrix[i][j]);

        memo[i][j] = result;
        computed[i][j] = true;

        return new Result(result, true);
    }

    /* **********************************************************************
     * 3. Tabulation
     * **********************************************************************/

    static Result minimumPointsToReachDestinationTabulation(int[][] matrix) {

        if (!validMatrix(matrix)) {
            return new Result(-1, false);
        }

        int m = matrix.length;
        int n = matrix[0].length;

        long[][] tab = new long[m][n];

        /*
         * Destination.
         */
        tab[m - 1][n - 1] =
                destinationRequirement(matrix[m - 1][n - 1]);

        /*
         * Last column.
         *
         * Only DOWN is possible.
         */
        for (int i = m - 2; i >= 0; i--) {

            tab[i][n - 1] =
                    Math.max(
                            1L,
                            tab[i + 1][n - 1] - matrix[i][n - 1]);
        }

        /*
         * Last row.
         *
         * Only RIGHT is possible.
         */
        for (int j = n - 2; j >= 0; j--) {

            tab[m - 1][j] =
                    Math.max(
                            1L,
                            tab[m - 1][j + 1] - matrix[m - 1][j]);
        }

        /*
         * Interior cells.
         */
        for (int i = m - 2; i >= 0; i--) {

            for (int j = n - 2; j >= 0; j--) {

                long minimumRequiredAfterLeaving =
                        Math.min(
                                tab[i + 1][j],
                                tab[i][j + 1]);

                tab[i][j] =
                        Math.max(
                                1L,
                                minimumRequiredAfterLeaving
                                        - matrix[i][j]);
            }
        }

        return new Result(tab[0][0], true);
    }

    /* **********************************************************************
     * 4. Dijkstra / Widest Path
     * **********************************************************************/

    /**
     * State used by the widest-path priority queue.
     *
     * minimumPrefixScore:
     *     The minimum running score encountered along this path,
     *     assuming an initial score of zero.
     */
    static class PathState {

        final long minimumPrefixScore;
        final int row;
        final int column;

        PathState(
                long minimumPrefixScore,
                int row,
                int column) {

            this.minimumPrefixScore = minimumPrefixScore;
            this.row = row;
            this.column = column;
        }
    }

    static Result minimumPointsToReachDestinationDijkstraAlgo(
            int[][] matrix) {

        if (!validMatrix(matrix)) {
            return new Result(-1, false);
        }

        int m = matrix.length;
        int n = matrix[0].length;

        /*
         * best[row][column] is the best (largest) minimum prefix score
         * discovered for this cell.
         */
        long[][] best = new long[m][n];

        for (long[] row : best) {
            Arrays.fill(row, Long.MIN_VALUE);
        }

        PriorityQueue<PathState> priorityQueue =
                new PriorityQueue<>(
                        (a, b) -> Long.compare(
                                b.minimumPrefixScore,
                                a.minimumPrefixScore));

        long startingScore = matrix[0][0];

        best[0][0] = startingScore;

        priorityQueue.add(
                new PathState(
                        startingScore,
                        0,
                        0));

        while (!priorityQueue.isEmpty()) {

            PathState current =
                    priorityQueue.poll();

            int row = current.row;
            int column = current.column;

            /*
             * Ignore an outdated priority-queue entry.
             */
            if (current.minimumPrefixScore != best[row][column]) {
                continue;
            }

            /*
             * The first time we remove the destination from this
             * max-priority queue, we have found the best possible
             * minimum prefix score.
             */
            if (row == m - 1 && column == n - 1) {

                return new Result(
                        requiredInitialPoints(
                                current.minimumPrefixScore),
                        true);
            }

            /*
             * DOWN.
             */
            if (row + 1 < m) {

                long nextScore =
                        current.minimumPrefixScore
                                + matrix[row + 1][column];

                long nextMinimum =
                        Math.min(
                                current.minimumPrefixScore,
                                nextScore);

                if (nextMinimum > best[row + 1][column]) {

                    best[row + 1][column] = nextMinimum;

                    priorityQueue.add(
                            new PathState(
                                    nextMinimum,
                                    row + 1,
                                    column));
                }
            }

            /*
             * RIGHT.
             */
            if (column + 1 < n) {

                long nextScore =
                        current.minimumPrefixScore
                                + matrix[row][column + 1];

                long nextMinimum =
                        Math.min(
                                current.minimumPrefixScore,
                                nextScore);

                if (nextMinimum > best[row][column + 1]) {

                    best[row][column + 1] = nextMinimum;

                    priorityQueue.add(
                            new PathState(
                                    nextMinimum,
                                    row,
                                    column + 1));
                }
            }
        }

        /*
         * A rectangular matrix always has at least one RIGHT/DOWN path,
         * so this should never be reached for a valid matrix.
         */
        return new Result(-1, false);
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

            sb.append(
                    matrix[i] == null
                            ? "null"
                            : Arrays.toString(matrix[i]));
        }

        sb.append("]");

        return sb.toString();
    }

    static boolean resultsEqual(Result a, Result b) {

        if (a == null && b == null) {
            return true;
        }

        if (a == null || b == null) {
            return false;
        }

        return a.steps() == b.steps()
                && a.valid() == b.valid();
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

                Result actual =
                        method.solve(test.input);

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
                            "  input    = %s%n",
                            formatMatrix(test.input));

                    System.out.printf(
                            "  expected = %s%n",
                            test.expected);

                    System.out.printf(
                            "  actual   = %s%n",
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
            int maxColumns,
            int minValue,
            int maxValue) {

        int rows =
                rng.nextInt(maxRows) + 1;

        int columns =
                rng.nextInt(maxColumns) + 1;

        int[][] matrix =
                new int[rows][columns];

        int range =
                maxValue - minValue + 1;

        for (int i = 0; i < rows; i++) {

            for (int j = 0; j < columns; j++) {

                matrix[i][j] =
                        rng.nextInt(range) + minValue;
            }
        }

        return matrix;
    }

    static void runRandomisedTests(int iterations) {

        System.out.println(
                "======================================================");

        System.out.println(
                "Randomised Cross Checks");

        System.out.println(
                "Recursion vs Memoisation vs Tabulation vs Dijkstra");

        System.out.println(
                "======================================================");

        Random rng =
                new Random(20260903L);

        for (int i = 1; i <= iterations; i++) {

            /*
             * Keep matrices small because plain recursion is intentionally
             * included in the cross-check.
             */
            int[][] matrix =
                    randomMatrix(
                            rng,
                            7,
                            7,
                            -10,
                            10);

            Result recursion =
                    minimumPointsToReachDestinationRecursion(matrix);

            Result memoisation =
                    minimumPointsToReachDestinationMemoisation(matrix);

            Result tabulation =
                    minimumPointsToReachDestinationTabulation(matrix);

            Result dijkstra =
                    minimumPointsToReachDestinationDijkstraAlgo(matrix);

            if (!resultsEqual(recursion, memoisation)
                    || !resultsEqual(recursion, tabulation)
                    || !resultsEqual(recursion, dijkstra)) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "iteration    = " + i);

                System.out.println(
                        "matrix       = "
                                + formatMatrix(matrix));

                System.out.println(
                        "recursion    = "
                                + recursion);

                System.out.println(
                        "memoisation  = "
                                + memoisation);

                System.out.println(
                        "tabulation   = "
                                + tabulation);

                System.out.println(
                        "dijkstra     = "
                                + dijkstra);

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

        List<TestCase> tests =
                new ArrayList<>();

        /*
         * ============================================================
         * Classic / Basic Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "B1",
                new int[][]{
                        {-2, -3, 3},
                        {-5, -10, 1},
                        {10, 30, -5}
                },
                new Result(7, true),
                "classic minimum initial points example"));

        tests.add(new TestCase(
                "B2",
                new int[][]{
                        {0}
                },
                new Result(1, true),
                "single zero cell"));

        tests.add(new TestCase(
                "B3",
                new int[][]{
                        {5}
                },
                new Result(1, true),
                "single positive cell"));

        tests.add(new TestCase(
                "B4",
                new int[][]{
                        {-5}
                },
                new Result(6, true),
                "single negative cell"));

        tests.add(new TestCase(
                "B5",
                new int[][]{
                        {1, 2, 3}
                },
                new Result(1, true),
                "single row with positive values"));

        tests.add(new TestCase(
                "B6",
                new int[][]{
                        {-1, -2, -3}
                },
                new Result(7, true),
                "single row with negative values"));

        tests.add(new TestCase(
                "B7",
                new int[][]{
                        {1},
                        {2},
                        {-5}
                },
                new Result(3, true),
                "single column"));

        tests.add(new TestCase(
                "B8",
                new int[][]{
                        {-2, -3},
                        {-5, -10}
                },
                new Result(8, true),
                "small all-negative matrix"));

        tests.add(new TestCase(
                "B9",
                new int[][]{
                        {10, -20},
                        {-20, 10}
                },
                new Result(11, true),
                "choosing between competing paths"));

        /*
         * ============================================================
         * Regression Cases
         * ============================================================
         *
         * These specifically guard against bugs in the original code.
         */

        tests.add(new TestCase(
                "R1",
                new int[][]{
                        {1, -2, 3},
                        {2, -5, 1},
                        {1, 1, -10}
                },
                new Result(8, true),
                "non-square 3x3 recurrence/table consistency"));

        tests.add(new TestCase(
                "R2",
                new int[][]{
                        {-2, -3, 3},
                        {-5, -10, 1}
                },
                new Result(6, true),
                "2x3 non-square matrix"));

        tests.add(new TestCase(
                "R3",
                new int[][]{
                        {-2, -3},
                        {-5, 10},
                        {10, -5}
                },
                new Result(6, true),
                "last-column and last-row transitions"));

        tests.add(new TestCase(
                "R4",
                new int[][]{
                        {0, 0, 0},
                        {0, 0, 0},
                        {0, 0, 0}
                },
                new Result(1, true),
                "all zeros"));

        tests.add(new TestCase(
                "R5",
                new int[][]{
                        {5, 5, 5},
                        {5, 5, 5},
                        {5, 5, 5}
                },
                new Result(1, true),
                "all positive values"));

        /*
         * ============================================================
         * Overflow Regression
         * ============================================================
         */

        tests.add(new TestCase(
                "O1",
                new int[][]{
                        {Integer.MIN_VALUE}
                },
                new Result(
                        2147483649L,
                        true),
                "Integer.MIN_VALUE must not overflow Math.abs"));

        tests.add(new TestCase(
                "O2",
                new int[][]{
                        {Integer.MAX_VALUE, Integer.MIN_VALUE}
                },
                new Result(
                        1,
                        true),
                "large positive followed by Integer.MIN_VALUE"));

        /*
         * ============================================================
         * Invalid Input Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "E1",
                null,
                new Result(-1, false),
                "null matrix"));

        tests.add(new TestCase(
                "E2",
                new int[][]{},
                new Result(-1, false),
                "empty matrix"));

        tests.add(new TestCase(
                "E3",
                new int[][]{
                        {}
                },
                new Result(-1, false),
                "one row with zero columns"));

        tests.add(new TestCase(
                "E4",
                new int[][]{
                        {},
                        {}
                },
                new Result(-1, false),
                "multiple zero-width rows"));

        tests.add(new TestCase(
                "E5",
                new int[][]{
                        {1, 2},
                        null
                },
                new Result(-1, false),
                "null row"));

        tests.add(new TestCase(
                "E6",
                new int[][]{
                        {1, 2},
                        {3}
                },
                new Result(-1, false),
                "jagged matrix"));

        tests.add(new TestCase(
                "E7",
                new int[][]{
                        {1},
                        {2, 3}
                },
                new Result(-1, false),
                "jagged matrix with longer second row"));

        /*
         * ============================================================
         * Header
         * ============================================================
         */

        System.out.println(
                "############################################################");

        System.out.println(
                "########  MINIMUM POINTS TO REACH DESTINATION  ########");

        System.out.println(
                "############################################################");

        System.out.println();

        /*
         * ============================================================
         * Run all deterministic tests against all implementations.
         * ============================================================
         */

        List<MethodCase> methods =
                List.of(

                        new MethodCase(
                                "Plain Recursion",
                                MinimumPointsToReachDestination
                                        ::minimumPointsToReachDestinationRecursion),

                        new MethodCase(
                                "Memoisation",
                                MinimumPointsToReachDestination
                                        ::minimumPointsToReachDestinationMemoisation),

                        new MethodCase(
                                "Tabulation",
                                MinimumPointsToReachDestination
                                        ::minimumPointsToReachDestinationTabulation),

                        new MethodCase(
                                "Dijkstra / Widest Path",
                                MinimumPointsToReachDestination
                                        ::minimumPointsToReachDestinationDijkstraAlgo)
                );

        for (MethodCase method : methods) {

            runTests(
                    method.name,
                    method.algorithm,
                    tests);
        }

        /*
         * ============================================================
         * Randomised cross-validation.
         * ============================================================
         */

        runRandomisedTests(5000);
    }
}
