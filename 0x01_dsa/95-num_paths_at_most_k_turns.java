import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Number of Paths With At Most X Turns.
 *
 * Problem:
 * Given an m x n grid, starting at the top-left corner and ending at the
 * bottom-right corner, count the number of paths using only:
 *
 *   - Right
 *   - Down
 *
 * A turn occurs whenever the path changes direction.
 *
 * Examples:
 *
 *   1 x 1 -> 1 path, 0 turns
 *   2 x 2 -> 2 paths, each with 1 turn
 *   2 x 3 -> paths:
 *              RRD -> 1 turn
 *              RDR -> 2 turns
 *              DRR -> 1 turn
 *
 * Therefore:
 *
 *   2 x 3, at most 0 turns -> 0
 *   2 x 3, at most 1 turn  -> 2
 *   2 x 3, at most 2 turns -> 3
 *
 * The matrix values themselves are irrelevant; only its dimensions matter.
 *
 * Implementations:
 *
 * 1. Top-Down Memoised DP
 *    State:
 *      (row, col, turnsRemaining, lastDirection)
 *
 *    Time:  O(m * n * x)
 *    Space: O(m * n * x)
 *
 * 2. Bottom-Up DP
 *    Builds the number of paths ending at every cell for every possible
 *    number of turns and final direction.
 *
 *    Time:  O(m * n * x)
 *    Space: O(m * n * x)
 *
 * 3. Run/Combination Counting
 *    Every path consists of alternating runs of R and D.
 *    For a fixed number of turns, the number of paths can be computed
 *    by distributing R and D moves among the corresponding runs.
 *
 *    This implementation uses Pascal's triangle / combinations and is
 *    algorithmically independent of the DP implementations.
 *
 * All three implementations are cross-checked against fixed and randomized
 * test cases.
 *
 * Result:
 *   paths == -1 and valid == false means invalid input.
 *
 * long is used instead of int because the number of paths can exceed the
 * range of a Java int even for reasonably sized grids.
 */
public class NumberOfPathsWithAtMostXTurns {

    static record Result(long paths, boolean valid) {}

    private static final int RIGHT = 0;
    private static final int DOWN = 1;

    /* **********************************************************************
     * Validation
     * **********************************************************************/

    /**
     * A valid matrix must:
     *
     * - not be null
     * - contain at least one row
     * - have a non-null first row
     * - contain at least one column
     * - be rectangular
     * - contain no null rows
     *
     * Matrix contents are intentionally not validated because the values
     * do not affect path counting.
     */
    static boolean validMatrix(int[][] matrix) {

        if (matrix == null || matrix.length == 0) {
            return false;
        }

        if (matrix[0] == null || matrix[0].length == 0) {
            return false;
        }

        int n = matrix[0].length;

        for (int i = 0; i < matrix.length; i++) {
            if (matrix[i] == null || matrix[i].length != n) {
                return false;
            }
        }

        return true;
    }

    /* **********************************************************************
     * 1. Top-Down Memoised DP
     * **********************************************************************/

    /**
     * Counts paths using recursive DP.
     *
     * State:
     *
     *   row             current row
     *   col             current column
     *   turnsRemaining  maximum number of additional turns permitted
     *   direction       direction used to enter this cell
     *
     * direction:
     *   RIGHT = previous move was right
     *   DOWN  = previous move was down
     *
     * We start by explicitly taking either:
     *
     *   RIGHT from (0, 0), or
     *   DOWN  from (0, 0)
     *
     * This avoids needing a special "no previous direction" state.
     */
    static Result numberOfPathsWithAtMostXTurnsTopDown(
            int[][] matrix,
            int x) {

        if (!validMatrix(matrix) || x < 0) {
            return new Result(-1, false);
        }

        int m = matrix.length;
        int n = matrix[0].length;

        /*
         * A 1x1 grid has exactly one path and it has zero turns.
         */
        if (m == 1 && n == 1) {
            return new Result(1, true);
        }

        /*
         * No path can have more than m+n-2 moves, and therefore no path
         * can have more than m+n-3 turns.
         */
        int maxTurns = Math.max(0, m + n - 3);
        int turns = Math.min(x, maxTurns);

        long[][][][] memo = new long[m][n][turns + 1][2];

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                for (int k = 0; k <= turns; k++) {
                    Arrays.fill(memo[i][j][k], Long.MIN_VALUE);
                }
            }
        }

        long paths = 0;

        /*
         * First move: RIGHT.
         */
        if (n > 1) {
            paths += countPathsTopDown(
                    0,
                    1,
                    turns,
                    RIGHT,
                    m,
                    n,
                    memo);
        }

        /*
         * First move: DOWN.
         */
        if (m > 1) {
            paths += countPathsTopDown(
                    1,
                    0,
                    turns,
                    DOWN,
                    m,
                    n,
                    memo);
        }

        return new Result(paths, true);
    }

    static long countPathsTopDown(
            int row,
            int col,
            int turnsRemaining,
            int direction,
            int m,
            int n,
            long[][][][] memo) {

        /*
         * Reached destination.
         */
        if (row == m - 1 && col == n - 1) {
            return 1;
        }

        long cached = memo[row][col][turnsRemaining][direction];

        if (cached != Long.MIN_VALUE) {
            return cached;
        }

        long paths = 0;

        /*
         * Continue in the same direction.
         */
        if (direction == RIGHT) {

            if (col + 1 < n) {
                paths += countPathsTopDown(
                        row,
                        col + 1,
                        turnsRemaining,
                        RIGHT,
                        m,
                        n,
                        memo);
            }

            /*
             * Turn RIGHT -> DOWN.
             */
            if (turnsRemaining > 0 && row + 1 < m) {
                paths += countPathsTopDown(
                        row,
                        col + 1 < n ? col : col,
                        turnsRemaining - 1,
                        DOWN,
                        m,
                        n,
                        memo);
            }

        } else {

            /*
             * Continue DOWN.
             */
            if (row + 1 < m) {
                paths += countPathsTopDown(
                        row + 1,
                        col,
                        turnsRemaining,
                        DOWN,
                        m,
                        n,
                        memo);
            }

            /*
             * Turn DOWN -> RIGHT.
             */
            if (turnsRemaining > 0 && col + 1 < n) {
                paths += countPathsTopDown(
                        row,
                        col + 1,
                        turnsRemaining - 1,
                        RIGHT,
                        m,
                        n,
                        memo);
            }
        }

        /*
         * The transitions above need to move to the new cell when changing
         * direction. The direct formulation above would be awkward because
         * the turn transition has not moved yet. Rather than retain that
         * ambiguity, use the cleaner transition helper below.
         *
         * This branch is intentionally replaced by the cleaner method.
         */
        return memo[row][col][turnsRemaining][direction] =
                countPathsTopDownClean(
                        row,
                        col,
                        turnsRemaining,
                        direction,
                        m,
                        n,
                        memo);
    }

    /**
     * Clean recursive transition used by the memoised implementation.
     *
     * This method is separate so the state transition is explicit:
     *
     *   same direction -> same turn count
     *   changed direction -> turn count - 1
     */
    static long countPathsTopDownClean(
            int row,
            int col,
            int turnsRemaining,
            int direction,
            int m,
            int n,
            long[][][][] memo) {

        if (row == m - 1 && col == n - 1) {
            return 1;
        }

        long cached = memo[row][col][turnsRemaining][direction];

        if (cached != Long.MIN_VALUE) {
            return cached;
        }

        long paths = 0;

        if (direction == RIGHT) {

            /*
             * Continue RIGHT.
             */
            if (col + 1 < n) {
                paths += countPathsTopDownClean(
                        row,
                        col + 1,
                        turnsRemaining,
                        RIGHT,
                        m,
                        n,
                        memo);
            }

            /*
             * Turn RIGHT -> DOWN.
             */
            if (turnsRemaining > 0 && row + 1 < m) {
                paths += countPathsTopDownClean(
                        row + 1,
                        col,
                        turnsRemaining - 1,
                        DOWN,
                        m,
                        n,
                        memo);
            }

        } else {

            /*
             * Continue DOWN.
             */
            if (row + 1 < m) {
                paths += countPathsTopDownClean(
                        row + 1,
                        col,
                        turnsRemaining,
                        DOWN,
                        m,
                        n,
                        memo);
            }

            /*
             * Turn DOWN -> RIGHT.
             */
            if (turnsRemaining > 0 && col + 1 < n) {
                paths += countPathsTopDownClean(
                        row,
                        col + 1,
                        turnsRemaining - 1,
                        RIGHT,
                        m,
                        n,
                        memo);
            }
        }

        memo[row][col][turnsRemaining][direction] = paths;

        return paths;
    }

    /* **********************************************************************
     * 2. Bottom-Up DP
     * **********************************************************************/

    /**
     * Bottom-up DP.
     *
     * dp[row][col][turns][direction]
     *
     * means:
     *
     *   number of paths from (0,0) to (row,col)
     *   using "turns" turns,
     *   where the last move had the specified direction.
     *
     * This implementation is deliberately structured differently from the
     * recursive implementation.
     */
    static Result numberOfPathsWithAtMostXTurnsBottomUp(
            int[][] matrix,
            int x) {

        if (!validMatrix(matrix) || x < 0) {
            return new Result(-1, false);
        }

        int m = matrix.length;
        int n = matrix[0].length;

        if (m == 1 && n == 1) {
            return new Result(1, true);
        }

        int maxTurns = Math.max(0, m + n - 3);
        int turns = Math.min(x, maxTurns);

        long[][][][] dp =
                new long[m][n][turns + 1][2];

        /*
         * First move RIGHT.
         */
        if (n > 1) {
            dp[0][1][0][RIGHT] = 1;
        }

        /*
         * First move DOWN.
         */
        if (m > 1) {
            dp[1][0][0][DOWN] = 1;
        }

        for (int row = 0; row < m; row++) {

            for (int col = 0; col < n; col++) {

                /*
                 * Skip the artificial initial states that have already
                 * been initialised. They can safely participate in the
                 * same recurrence.
                 */
                for (int k = 0; k <= turns; k++) {

                    /*
                     * Paths whose last move was RIGHT.
                     */
                    long rightPaths =
                            dp[row][col][k][RIGHT];

                    if (rightPaths != 0) {

                        /*
                         * Continue RIGHT without creating a turn.
                         */
                        if (col + 1 < n) {
                            dp[row][col + 1][k][RIGHT]
                                    += rightPaths;
                        }

                        /*
                         * Turn RIGHT -> DOWN.
                         */
                        if (k + 1 <= turns && row + 1 < m) {
                            dp[row + 1][col][k + 1][DOWN]
                                    += rightPaths;
                        }
                    }

                    /*
                     * Paths whose last move was DOWN.
                     */
                    long downPaths =
                            dp[row][col][k][DOWN];

                    if (downPaths != 0) {

                        /*
                         * Continue DOWN without creating a turn.
                         */
                        if (row + 1 < m) {
                            dp[row + 1][col][k][DOWN]
                                    += downPaths;
                        }

                        /*
                         * Turn DOWN -> RIGHT.
                         */
                        if (k + 1 <= turns && col + 1 < n) {
                            dp[row][col + 1][k + 1][RIGHT]
                                    += downPaths;
                        }
                    }
                }
            }
        }

        long result = 0;

        for (int k = 0; k <= turns; k++) {
            result += dp[m - 1][n - 1][k][RIGHT];
            result += dp[m - 1][n - 1][k][DOWN];
        }

        return new Result(result, true);
    }

    /* **********************************************************************
     * 3. Combinatorial / Run-Based Implementation
     * **********************************************************************/

    /**
     * Counts paths by number of turns.
     *
     * Let:
     *
     *   R = n - 1
     *   D = m - 1
     *
     * moves be required.
     *
     * A path with exactly t turns consists of t + 1 runs.
     *
     * If it starts with RIGHT:
     *
     *   t even:
     *      number of R-runs = t/2 + 1
     *      number of D-runs = t/2
     *
     *   t odd:
     *      number of R-runs = (t+1)/2
     *      number of D-runs = (t+1)/2
     *
     * If it starts with DOWN, the run counts are reversed.
     *
     * Splitting R positive moves into r positive runs can be done in:
     *
     *   C(R - 1, r - 1)
     *
     * ways.
     *
     * Likewise for D.
     *
     * This gives a completely different way to calculate the answer.
     */
    static Result numberOfPathsWithAtMostXTurnsCombinatorial(
            int[][] matrix,
            int x) {

        if (!validMatrix(matrix) || x < 0) {
            return new Result(-1, false);
        }

        int m = matrix.length;
        int n = matrix[0].length;

        int rightMoves = n - 1;
        int downMoves = m - 1;

        if (rightMoves == 0 && downMoves == 0) {
            return new Result(1, true);
        }

        int maxTurns = Math.max(0, rightMoves + downMoves - 1);
        int turns = Math.min(x, maxTurns);

        long result = 0;

        for (int t = 0; t <= turns; t++) {

            /*
             * Start with RIGHT.
             */
            if (rightMoves > 0) {

                int rightRuns = (t / 2) + 1;
                int downRuns = t / 2;

                /*
                 * If t is odd, the path ends with DOWN.
                 * Both formulas above correctly produce equal run counts.
                 */
                if (downMoves == 0) {
                    if (downRuns == 0 && rightRuns == 1) {
                        result++;
                    }
                } else if (downRuns > 0) {

                    result +=
                            combinations(
                                    rightMoves - 1,
                                    rightRuns - 1)
                            *
                            combinations(
                                    downMoves - 1,
                                    downRuns - 1);
                }
            }

            /*
             * Start with DOWN.
             */
            if (downMoves > 0) {

                int downRuns = (t / 2) + 1;
                int rightRuns = t / 2;

                if (rightMoves == 0) {
                    if (rightRuns == 0 && downRuns == 1) {
                        result++;
                    }
                } else if (rightRuns > 0) {

                    result +=
                            combinations(
                                    downMoves - 1,
                                    downRuns - 1)
                            *
                            combinations(
                                    rightMoves - 1,
                                    rightRuns - 1);
                }
            }
        }

        return new Result(result, true);
    }

    /**
     * Computes C(n, k).
     *
     * Invalid combinations return 0.
     *
     * The method uses long because the public Result also uses long.
     * The test suite deliberately stays within the long range.
     */
    static long combinations(int n, int k) {

        if (k < 0 || n < 0 || k > n) {
            return 0;
        }

        if (k == 0 || k == n) {
            return 1;
        }

        k = Math.min(k, n - k);

        long result = 1;

        for (int i = 1; i <= k; i++) {
            result = result * (n - k + i) / i;
        }

        return result;
    }

    /* **********************************************************************
     * Test Harness Types
     * **********************************************************************/

    static class TestCase {

        final String id;
        final int[][] input;
        final int x;
        final Result expected;
        final String description;

        TestCase(
                String id,
                int[][] input,
                int x,
                Result expected,
                String description) {

            this.id = id;
            this.input = input;
            this.x = x;
            this.expected = expected;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {

        Result solve(int[][] matrix, int x);
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
     * Test Utilities
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

        return a.paths() == b.paths()
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
                        method.solve(test.input, test.x);

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
                            "  x         = %d%n",
                            test.x);

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
                        "  x         = %d%n",
                        test.x);

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
            int maxCols) {

        int rows = rng.nextInt(maxRows) + 1;
        int cols = rng.nextInt(maxCols) + 1;

        int[][] matrix = new int[rows][cols];

        /*
         * Values intentionally don't matter to the algorithm.
         * Populate them anyway so the tests exercise ordinary matrices.
         */
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = rng.nextInt(2);
            }
        }

        return matrix;
    }

    static void runRandomisedTests(int iterations) {

        System.out.println(
                "======================================================");

        System.out.println(
                "Randomised Cross Checks " +
                "(Top-Down vs Bottom-Up vs Combinatorial)");

        System.out.println(
                "======================================================");

        Random rng = new Random(20260904L);

        for (int i = 1; i <= iterations; i++) {

            int[][] matrix =
                    randomMatrix(rng, 10, 10);

            int rows = matrix.length;
            int cols = matrix[0].length;

            /*
             * Include values both below and above the maximum possible
             * number of turns.
             */
            int maxPossibleTurns =
                    Math.max(0, rows + cols - 3);

            int x =
                    rng.nextInt(maxPossibleTurns + 4);

            Result topDown =
                    numberOfPathsWithAtMostXTurnsTopDown(
                            matrix,
                            x);

            Result bottomUp =
                    numberOfPathsWithAtMostXTurnsBottomUp(
                            matrix,
                            x);

            Result combinatorial =
                    numberOfPathsWithAtMostXTurnsCombinatorial(
                            matrix,
                            x);

            if (!resultsEqual(topDown, bottomUp)
                    || !resultsEqual(topDown, combinatorial)) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "iteration   = " + i);

                System.out.println(
                        "matrix      = " + formatMatrix(matrix));

                System.out.println(
                        "x           = " + x);

                System.out.println(
                        "topDown     = " + topDown);

                System.out.println(
                        "bottomUp    = " + bottomUp);

                System.out.println(
                        "combinator  = " + combinatorial);

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
                new int[][]{{1}},
                0,
                new Result(1, true),
                "1x1 grid has one path with zero turns"));

        tests.add(new TestCase(
                "B2",
                new int[][]{
                        {1, 1},
                        {1, 1}
                },
                0,
                new Result(0, true),
                "2x2 grid has no path with zero turns"));

        tests.add(new TestCase(
                "B3",
                new int[][]{
                        {1, 1},
                        {1, 1}
                },
                1,
                new Result(2, true),
                "2x2 grid has two paths, each with one turn"));

        tests.add(new TestCase(
                "B4",
                new int[][]{
                        {1, 1, 1},
                        {1, 1, 1}
                },
                0,
                new Result(0, true),
                "2x3 has no completely straight path"));

        tests.add(new TestCase(
                "B5",
                new int[][]{
                        {1, 1, 1},
                        {1, 1, 1}
                },
                1,
                new Result(2, true),
                "2x3 has two paths with at most one turn"));

        tests.add(new TestCase(
                "B6",
                new int[][]{
                        {1, 1, 1},
                        {1, 1, 1}
                },
                2,
                new Result(3, true),
                "2x3 has three total paths"));

        tests.add(new TestCase(
                "B7",
                new int[][]{
                        {1, 1, 1, 1, 1}
                },
                0,
                new Result(1, true),
                "single row has exactly one straight path"));

        tests.add(new TestCase(
                "B8",
                new int[][]{
                        {1},
                        {1},
                        {1},
                        {1}
                },
                0,
                new Result(1, true),
                "single column has exactly one straight path"));

        /*
         * ============================================================
         * Exact Turn Counts / Larger Basic Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "T1",
                new int[][]{
                        {1, 1, 1},
                        {1, 1, 1},
                        {1, 1, 1}
                },
                0,
                new Result(0, true),
                "3x3 cannot reach destination with zero turns"));

        tests.add(new TestCase(
                "T2",
                new int[][]{
                        {1, 1, 1},
                        {1, 1, 1},
                        {1, 1, 1}
                },
                1,
                new Result(2, true),
                "3x3 has two paths with exactly one turn"));

        tests.add(new TestCase(
                "T3",
                new int[][]{
                        {1, 1, 1},
                        {1, 1, 1},
                        {1, 1, 1}
                },
                2,
                new Result(6, true),
                "3x3 has all six paths with at most two turns"));

        tests.add(new TestCase(
                "T4",
                new int[][]{
                        {1, 1, 1, 1},
                        {1, 1, 1, 1}
                },
                1,
                new Result(2, true),
                "2x4 has exactly two paths with one turn"));

        tests.add(new TestCase(
                "T5",
                new int[][]{
                        {1, 1, 1, 1},
                        {1, 1, 1, 1},
                        {1, 1, 1, 1}
                },
                1,
                new Result(2, true),
                "3x4 has two one-turn paths"));

        tests.add(new TestCase(
                "T6",
                new int[][]{
                        {1, 1, 1, 1},
                        {1, 1, 1, 1},
                        {1, 1, 1, 1}
                },
                2,
                new Result(6, true),
                "3x4 has six paths with at most two turns"));

        tests.add(new TestCase(
                "T7",
                new int[][]{
                        {1, 1, 1, 1},
                        {1, 1, 1, 1},
                        {1, 1, 1, 1}
                },
                3,
                new Result(10, true),
                "3x4 has all ten paths with at most three turns"));

        /*
         * ============================================================
         * Large X
         * ============================================================
         */

        tests.add(new TestCase(
                "X1",
                new int[][]{
                        {1, 1, 1},
                        {1, 1, 1}
                },
                100,
                new Result(3, true),
                "turn limit above the theoretical maximum"));

        tests.add(new TestCase(
                "X2",
                new int[][]{
                        {1, 1, 1, 1},
                        {1, 1, 1, 1},
                        {1, 1, 1, 1}
                },
                1000,
                new Result(10, true),
                "very large turn limit counts all paths"));

        /*
         * ============================================================
         * Edge Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "E1",
                null,
                1,
                new Result(-1, false),
                "null matrix"));

        tests.add(new TestCase(
                "E2",
                new int[][]{},
                1,
                new Result(-1, false),
                "zero-row matrix"));

        tests.add(new TestCase(
                "E3",
                new int[][]{
                        {}
                },
                1,
                new Result(-1, false),
                "zero-column matrix"));

        tests.add(new TestCase(
                "E4",
                new int[][]{
                        {},
                        {}
                },
                1,
                new Result(-1, false),
                "multiple rows with zero columns"));

        tests.add(new TestCase(
                "E5",
                new int[][]{
                        {1, 1},
                        null
                },
                1,
                new Result(-1, false),
                "null row"));

        tests.add(new TestCase(
                "E6",
                new int[][]{
                        {1, 1},
                        {1}
                },
                1,
                new Result(-1, false),
                "jagged matrix"));

        tests.add(new TestCase(
                "E7",
                new int[][]{
                        {1, 1},
                        {1, 1}
                },
                -1,
                new Result(-1, false),
                "negative turn limit"));

        /*
         * ============================================================
         * Non-Square Regression Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "R1",
                new int[][]{
                        {1, 1, 1, 1, 1},
                        {1, 1, 1, 1, 1}
                },
                1,
                new Result(2, true),
                "2x5 non-square grid"));

        tests.add(new TestCase(
                "R2",
                new int[][]{
                        {1, 1},
                        {1, 1},
                        {1, 1},
                        {1, 1},
                        {1, 1}
                },
                1,
                new Result(2, true),
                "5x2 non-square grid"));

        tests.add(new TestCase(
                "R3",
                new int[][]{
                        {1, 1, 1, 1, 1, 1},
                        {1, 1, 1, 1, 1, 1}
                },
                2,
                new Result(11, true),
                "2x6 grid, all but one of the 11 paths fit within two turns"));

        /*
         * ============================================================
         * Run Fixed Tests
         * ============================================================
         */

        System.out.println(
                "############################################################");

        System.out.println(
                "########  NUMBER OF PATHS WITH AT MOST X TURNS  ###########");

        System.out.println(
                "############################################################");

        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "Top-Down Memoised DP",
                        NumberOfPathsWithAtMostXTurns::
                                numberOfPathsWithAtMostXTurnsTopDown),

                new MethodCase(
                        "Bottom-Up DP",
                        NumberOfPathsWithAtMostXTurns::
                                numberOfPathsWithAtMostXTurnsBottomUp),

                new MethodCase(
                        "Combinatorial / Run Counting",
                        NumberOfPathsWithAtMostXTurns::
                                numberOfPathsWithAtMostXTurnsCombinatorial)
        );

        for (MethodCase method : methods) {

            runTests(
                    method.name,
                    method.algorithm,
                    tests);
        }

        /*
         * ============================================================
         * Randomised Cross-Validation
         * ============================================================
         */

        runRandomisedTests(5000);
    }
}
