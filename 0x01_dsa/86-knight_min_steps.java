import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Knight's Minimum Moves to Target.
 *
 * Problem:
 * On a standard 8x8 chessboard, given a knight's starting square and a
 * target square, find the minimum number of knight moves needed to reach
 * the target.
 *
 * Notes:
 * - The board's CONTENTS are never read (only its shape is validated) -
 *   this is a pure shortest-path problem on the knight's-move graph, with
 *   no obstacles.
 * - If the board isn't a valid 8x8 grid, or either position is null,
 *   the wrong length, or outside the board, every method returns
 *   Result(-1, false). The out-of-bounds check on knightPos/targetPos is
 *   an added robustness check - the original code never validated these
 *   at all, so an out-of-range coordinate would previously have thrown
 *   ArrayIndexOutOfBoundsException instead of failing predictably.
 * - Only one implementation was given in the original code. A second,
 *   independently-written bidirectional BFS was added below so the plain
 *   BFS has something genuine to be cross-checked against, matching the
 *   multi-implementation pattern used throughout the rest of this series.
 *
 * Implementations:
 *
 * 1. BFS (single-direction, from the start only)
 *      Standard shortest-path BFS: expand outward from the knight's
 *      position, layer by layer, until the target is dequeued.
 *      Time: O(n^2)   Space: O(n^2).
 *
 * 2. Bidirectional BFS (added for cross-checking)
 *      Expands outward from BOTH the start and the target simultaneously,
 *      one full level at a time, stopping as soon as the two frontiers
 *      meet. Meaningfully different control flow from (1) - two queues,
 *      two distance grids, and a "have the frontiers touched" check
 *      instead of a single "is this the target" check.
 *      Time: O(n^2)   Space: O(n^2).
 *
 * Both are cross-checked against each other over EVERY ordered pair of
 * start/target squares on the board (64 x 64 = 4096 combinations, cheap
 * enough to test exhaustively rather than sample randomly), following the
 * same test-harness shape used for SumOfDiagonalsMatrixTestHarness
 * (TestCase / Algorithm / MethodCase / runTests / randomised cross-checks).
 */
public class KnightMinimumMovesToTarget {

    static record Result(int steps, boolean valid) {}

    static record Cell(int x, int y, int dist) {}

    private static final int[] DIR_X = {-2, -1, 1, 2, -2, -1, 1, 2};
    private static final int[] DIR_Y = {-1, -2, -2, -1, 1, 2, 2, 1};

    /* **********************************************************************
     * Validation
     * **********************************************************************/

    static boolean validBoard(int[][] board) {
        // A valid chess board is an 8 x 8 grid.
        return board != null && board.length == 8 && board[0] != null && board[0].length == board.length;
    }

    static boolean validPosition(int[] pos, int n) {
        return pos != null && pos.length == 2
                && pos[0] >= 0 && pos[0] < n
                && pos[1] >= 0 && pos[1] < n;
    }

    static boolean isInside(int x, int y, int n) {
        return x >= 0 && x < n && y >= 0 && y < n;
    }

    /* **********************************************************************
     * Algorithm Implementations
     * **********************************************************************/

    static Result knightMinimumMovesToTargetBFS(int[][] board, int[] knightPos, int[] targetPos) {
        int n = board == null ? 0 : board.length;
        if (!validBoard(board) || !validPosition(knightPos, n) || !validPosition(targetPos, n)) {
            return new Result(-1, false);
        }

        Queue<Cell> queue = new LinkedList<>();
        queue.add(new Cell(knightPos[0], knightPos[1], 0));

        boolean[][] visited = new boolean[n][n];
        visited[knightPos[0]][knightPos[1]] = true;

        while (!queue.isEmpty()) {
            Cell t = queue.poll();

            if (t.x() == targetPos[0] && t.y() == targetPos[1]) {
                return new Result(t.dist(), true);
            }

            for (int i = 0; i < 8; i++) {
                int x = t.x() + DIR_X[i];
                int y = t.y() + DIR_Y[i];

                if (isInside(x, y, n) && !visited[x][y]) {
                    visited[x][y] = true;
                    queue.add(new Cell(x, y, t.dist() + 1));
                }
            }
        }

        return new Result(-1, false);
    }

    static Result knightMinimumMovesToTargetBidirectionalBFS(int[][] board, int[] knightPos, int[] targetPos) {
        int n = board == null ? 0 : board.length;
        if (!validBoard(board) || !validPosition(knightPos, n) || !validPosition(targetPos, n)) {
            return new Result(-1, false);
        }

        if (knightPos[0] == targetPos[0] && knightPos[1] == targetPos[1]) {
            return new Result(0, true);
        }

        int[][] distFromStart = new int[n][n];
        int[][] distFromTarget = new int[n][n];
        for (int[] row : distFromStart) {
            Arrays.fill(row, -1);
        }
        for (int[] row : distFromTarget) {
            Arrays.fill(row, -1);
        }

        Queue<int[]> queueStart = new LinkedList<>();
        Queue<int[]> queueTarget = new LinkedList<>();

        distFromStart[knightPos[0]][knightPos[1]] = 0;
        distFromTarget[targetPos[0]][targetPos[1]] = 0;
        queueStart.add(new int[]{knightPos[0], knightPos[1]});
        queueTarget.add(new int[]{targetPos[0], targetPos[1]});

        while (!queueStart.isEmpty() && !queueTarget.isEmpty()) {
            Integer meetDistance = expandOneLevel(queueStart, distFromStart, distFromTarget, n);
            if (meetDistance != null) {
                return new Result(meetDistance, true);
            }

            meetDistance = expandOneLevel(queueTarget, distFromTarget, distFromStart, n);
            if (meetDistance != null) {
                return new Result(meetDistance, true);
            }
        }

        // Unreachable on a real 8x8 board (the knight's graph is fully
        // connected), kept only as a safety net.
        return new Result(-1, false);
    }

    /* **********************************************************************
     * Helper
     * **********************************************************************/

    /**
     * Expands every cell currently in {@code queue} by exactly one knight
     * move (one full BFS level from this side), returning the total path
     * length the instant a newly-reached cell turns out to already be
     * visited by the other side's search - or null if the two frontiers
     * haven't met yet.
     */
    static Integer expandOneLevel(Queue<int[]> queue, int[][] distThisSide, int[][] distOtherSide, int n) {
        int levelSize = queue.size();

        for (int k = 0; k < levelSize; k++) {
            int[] cell = queue.poll();
            int cx = cell[0];
            int cy = cell[1];

            for (int i = 0; i < 8; i++) {
                int nx = cx + DIR_X[i];
                int ny = cy + DIR_Y[i];

                if (isInside(nx, ny, n) && distThisSide[nx][ny] == -1) {
                    distThisSide[nx][ny] = distThisSide[cx][cy] + 1;

                    if (distOtherSide[nx][ny] != -1) {
                        return distThisSide[nx][ny] + distOtherSide[nx][ny];
                    }

                    queue.add(new int[]{nx, ny});
                }
            }
        }

        return null;
    }

    /* **********************************************************************
     * Test Harness Types
     * **********************************************************************/

    static class TestCase {

        final String id;
        final int[][] board;
        final int[] knightPos;
        final int[] targetPos;
        final Result expected;
        final String description;

        TestCase(
                String id,
                int[][] board,
                int[] knightPos,
                int[] targetPos,
                Result expected,
                String description) {

            this.id = id;
            this.board = board;
            this.knightPos = knightPos;
            this.targetPos = targetPos;
            this.expected = expected;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {

        Result solve(int[][] board, int[] knightPos, int[] targetPos);
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

    static boolean resultsEqual(Result a, Result b) {

        if (a == null && b == null) {
            return true;
        }

        if (a == null || b == null) {
            return false;
        }

        return a.steps() == b.steps() && a.valid() == b.valid();
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

                Result actual = method.solve(test.board, test.knightPos, test.targetPos);

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
                            "  knightPos = %s, targetPos = %s%n",
                            Arrays.toString(test.knightPos),
                            Arrays.toString(test.targetPos));

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
     * Exhaustive + Randomised Cross Checks
     * **********************************************************************/

    static void runExhaustiveCrossCheck(int[][] board) {

        System.out.println(
                "======================================================");
        System.out.println(
                "Exhaustive Cross Check (BFS vs Bidirectional BFS, all 64x64 start/target pairs)");
        System.out.println(
                "======================================================");

        int n = board.length;
        int mismatches = 0;
        int checked = 0;

        for (int sx = 0; sx < n; sx++) {
            for (int sy = 0; sy < n; sy++) {
                for (int tx = 0; tx < n; tx++) {
                    for (int ty = 0; ty < n; ty++) {

                        int[] start = {sx, sy};
                        int[] target = {tx, ty};

                        Result bfs = knightMinimumMovesToTargetBFS(board, start, target);
                        Result bidi = knightMinimumMovesToTargetBidirectionalBFS(board, start, target);

                        checked++;

                        if (!resultsEqual(bfs, bidi)) {
                            mismatches++;
                            System.out.printf(
                                    "MISMATCH: start=%s target=%s bfs=%s bidi=%s%n",
                                    Arrays.toString(start),
                                    Arrays.toString(target),
                                    bfs,
                                    bidi);
                        }
                    }
                }
            }
        }

        System.out.printf(
                "Checked %d pairs, %d mismatches.%n%n",
                checked,
                mismatches);
    }

    static void runRandomisedInvalidInputTests(int iterations) {

        System.out.println(
                "======================================================");
        System.out.println(
                "Randomised Cross Checks (malformed boards / positions)");
        System.out.println(
                "======================================================");

        Random rng = new Random(20260826L);
        int mismatches = 0;

        for (int i = 1; i <= iterations; i++) {
            int size = rng.nextInt(10) + 1; // occasionally not 8
            int[][] board = size == 0 ? null : new int[size][size];

            int[] start = {rng.nextInt(17) - 4, rng.nextInt(17) - 4}; // sometimes out of range
            int[] target = {rng.nextInt(17) - 4, rng.nextInt(17) - 4};

            Result bfs = knightMinimumMovesToTargetBFS(board, start, target);
            Result bidi = knightMinimumMovesToTargetBidirectionalBFS(board, start, target);

            if (!resultsEqual(bfs, bidi)) {
                mismatches++;
                System.out.printf(
                        "MISMATCH: size=%d start=%s target=%s bfs=%s bidi=%s%n",
                        size,
                        Arrays.toString(start),
                        Arrays.toString(target),
                        bfs,
                        bidi);
            }
        }

        System.out.printf(
                "Checked %d malformed-input cases, %d mismatches.%n%n",
                iterations,
                mismatches);
    }

    /* **********************************************************************
     * Main
     * **********************************************************************/

    public static void main(String[] args) {

        int[][] board = new int[8][8]; // content is irrelevant; only the 8x8 shape matters

        List<TestCase> tests = new ArrayList<>();

        /*
         * ============================================================
         * Basic Cases
         * (expected values verified with an independent Python BFS
         * before being hardcoded here)
         * ============================================================
         */

        tests.add(new TestCase(
                "B1",
                board, new int[]{0, 0}, new int[]{0, 0},
                new Result(0, true),
                "start equals target: 0 moves"));

        tests.add(new TestCase(
                "B2",
                board, new int[]{0, 0}, new int[]{1, 2},
                new Result(1, true),
                "a direct knight move away: 1 move"));

        tests.add(new TestCase(
                "B3",
                board, new int[]{0, 0}, new int[]{7, 7},
                new Result(6, true),
                "opposite corners of the board: well-known 6-move distance"));

        tests.add(new TestCase(
                "B4",
                board, new int[]{0, 0}, new int[]{0, 1},
                new Result(3, true),
                "adjacent square, not a knight move away: 3 moves"));

        tests.add(new TestCase(
                "B5",
                board, new int[]{0, 0}, new int[]{1, 1},
                new Result(4, true),
                "notorious corner-adjacent diagonal case: 4 moves, more than the general case"));

        tests.add(new TestCase(
                "B6",
                board, new int[]{3, 3}, new int[]{4, 5},
                new Result(1, true),
                "a direct knight move away from the centre of the board"));

        /*
         * ============================================================
         * Added Robustness: Out-of-Bounds / Malformed Positions
         * (the original code never validated knightPos/targetPos at all,
         * so these previously threw ArrayIndexOutOfBoundsException)
         * ============================================================
         */

        tests.add(new TestCase(
                "V1",
                board, new int[]{-1, 0}, new int[]{0, 0},
                new Result(-1, false),
                "knightPos row is negative"));

        tests.add(new TestCase(
                "V2",
                board, new int[]{0, 0}, new int[]{8, 0},
                new Result(-1, false),
                "targetPos row is out of range (board is 0..7)"));

        tests.add(new TestCase(
                "V3",
                board, new int[]{0}, new int[]{0, 0},
                new Result(-1, false),
                "knightPos has the wrong length"));

        /*
         * ============================================================
         * Edge Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "E1",
                null, new int[]{0, 0}, new int[]{0, 0},
                new Result(-1, false),
                "null board"));

        tests.add(new TestCase(
                "E2",
                new int[][]{{0, 0}, {0, 0}}, new int[]{0, 0}, new int[]{1, 1},
                new Result(-1, false),
                "board is not 8x8"));

        tests.add(new TestCase(
                "E3",
                board, null, new int[]{0, 0},
                new Result(-1, false),
                "null knightPos"));

        System.out.println(
                "############################################################");
        System.out.println(
                "###############  KNIGHT MINIMUM MOVES  #####################");
        System.out.println(
                "############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "BFS",
                        KnightMinimumMovesToTarget::knightMinimumMovesToTargetBFS),

                new MethodCase(
                        "Bidirectional BFS",
                        KnightMinimumMovesToTarget::knightMinimumMovesToTargetBidirectionalBFS)
        );

        for (MethodCase method : methods) {

            runTests(
                    method.name,
                    method.algorithm,
                    tests);
        }

        runExhaustiveCrossCheck(board);
        runRandomisedInvalidInputTests(2000);
    }
}
