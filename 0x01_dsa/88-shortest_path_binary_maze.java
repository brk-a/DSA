import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Shortest Path in a Binary Maze.
 *
 * Problem:
 * Given a binary matrix (1 = open, 0 = blocked), find the length of the
 * shortest path from src to dst moving 4-directionally (up/down/left/right)
 * through open cells only.
 *
 * Notes:
 * - If the matrix is invalid, src/dst is null/wrong-length/out of bounds,
 *   or either the src or dst cell is blocked, every method returns
 *   Result(-1, false). The out-of-bounds check on src/dst is an added
 *   robustness check - the original code never validated these positions
 *   at all, so an out-of-range coordinate would previously have thrown
 *   ArrayIndexOutOfBoundsException instead of failing predictably.
 * - src == dst (on an open cell) is always reachable at distance 0.
 *
 * Implementations:
 *
 * 1. DFS (exhaustive, with backtracking)
 *      Explores every simple path from src, returning the length of the
 *      shortest one found. Intentionally exhaustive rather than "first
 *      path found" - used here purely as a cross-check against BFS - so
 *      it's exponential and only suitable for small mazes.
 *      Time: O(4^(m*n)) worst case   Space: O(m*n) recursion depth.
 *
 * 2. BFS
 *      Standard shortest-path BFS, using a cloned grid where visited
 *      cells are marked by setting them to 0.
 *      Time: O(m * n)   Space: O(m * n).
 *
 * Both are cross-checked against each other for both fixed and randomised
 * (small) test mazes, following the same test-harness shape used for
 * SumOfDiagonalsMatrixTestHarness (TestCase / Algorithm / MethodCase /
 * runTests / randomised cross-checks).
 */
public class ShortestPathBinaryMaze {

    static record Result(int result, boolean valid) {}

    private static final int[] DIR_X = {-1, 1, 0, 0};
    private static final int[] DIR_Y = {0, 0, -1, 1};

    /* **********************************************************************
     * Validation
     * **********************************************************************/

    static boolean validMatrix(int[][] matrix) {
        return matrix != null && matrix.length > 0 && matrix[0] != null && matrix[0].length > 0;
    }

    static boolean validPosition(int[] pos, int m, int n) {
        return pos != null && pos.length == 2
                && pos[0] >= 0 && pos[0] < m
                && pos[1] >= 0 && pos[1] < n;
    }

    /**
     * Consolidates every pre-flight check (matrix shape, src/dst in bounds,
     * src/dst cells open) into one place, used by both algorithms below.
     * The original code duplicated these checks in each method - and had
     * the SAME two bugs in both copies (an `&&` that should have been
     * `||`, and comma-indexing instead of double-bracket indexing) -
     * sharing one helper means there's only one place left to get it wrong.
     */
    static boolean validEndpoints(int[][] matrix, int[] src, int[] dst) {
        if (!validMatrix(matrix)) {
            return false;
        }
        int m = matrix.length;
        int n = matrix[0].length;
        if (!validPosition(src, m, n) || !validPosition(dst, m, n)) {
            return false;
        }
        return matrix[src[0]][src[1]] == 1 && matrix[dst[0]][dst[1]] == 1;
    }

    /* **********************************************************************
     * Algorithm Implementations
     * **********************************************************************/

    static Result shortestPathBinaryMazeDFS(int[][] matrix, int[] src, int[] dst) {
        if (!validEndpoints(matrix, src, dst)) {
            return new Result(-1, false);
        }

        int m = matrix.length;
        int n = matrix[0].length;
        boolean[][] visited = new boolean[m][n];

        int minDist = dfs(matrix, visited, src[0], src[1], dst[0], dst[1], 0);
        int result = (minDist == Integer.MAX_VALUE) ? -1 : minDist;

        return new Result(result, result != -1);
    }

    static Result shortestPathBinaryMazeBFS(int[][] matrix, int[] src, int[] dst) {
        if (!validEndpoints(matrix, src, dst)) {
            return new Result(-1, false);
        }

        int m = matrix.length;
        int n = matrix[0].length;
        int[][] clone = cloneMatrix(matrix);
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{src[0], src[1], 0});
        clone[src[0]][src[1]] = 0;

        while (!queue.isEmpty()) {
            int[] curr = queue.poll();
            int x = curr[0];
            int y = curr[1];
            int dist = curr[2];

            if (x == dst[0] && y == dst[1]) {
                return new Result(dist, true);
            }

            for (int i = 0; i < 4; i++) {
                int nx = x + DIR_X[i];
                int ny = y + DIR_Y[i];

                if (nx >= 0 && nx < m && ny >= 0 && ny < n && clone[nx][ny] == 1) {
                    clone[nx][ny] = 0;
                    queue.add(new int[]{nx, ny, dist + 1});
                }
            }
        }

        return new Result(-1, false);
    }

    /* **********************************************************************
     * Helpers
     * **********************************************************************/

    /**
     * Returns the length of the shortest path from (x, y) to (dx, dy)
     * found by exhaustive backtracking DFS, or Integer.MAX_VALUE if none
     * is found from this branch.
     *
     * (x, y, dx, dy) here are current-position / target-position pairs.
     * The original method declared its parameters as (x, dx, y, dy) -
     * transposing the target's x with the current position's y - which
     * left the base case comparing src's own coordinates to each other
     * instead of to the target, and scrambled position tracking through
     * every recursive call. It also relied on a shared mutable field to
     * carry the running minimum, which was simultaneously declared
     * `final` (so it could never be updated) and an instance field
     * (unusable from a static method), and would have carried stale state
     * between separate top-level calls even if those were fixed. Returning
     * the minimum directly, as done here, sidesteps all of that.
     */
    static int dfs(int[][] matrix, boolean[][] visited, int x, int y, int dx, int dy, int dist) {
        if (x == dx && y == dy) {
            return dist;
        }

        visited[x][y] = true;
        int best = Integer.MAX_VALUE;

        for (int i = 0; i < 4; i++) {
            int nx = x + DIR_X[i];
            int ny = y + DIR_Y[i];

            if (isSafe(matrix, visited, nx, ny)) {
                best = Math.min(best, dfs(matrix, visited, nx, ny, dx, dy, dist + 1));
            }
        }

        visited[x][y] = false;

        return best;
    }

    static boolean isSafe(int[][] matrix, boolean[][] visited, int x, int y) {
        int m = matrix.length;
        int n = matrix[0].length;

        return x >= 0 && x < m && y >= 0 && y < n && matrix[x][y] == 1 && !visited[x][y];
    }

    static int[][] cloneMatrix(int[][] matrix) {
        int[][] copy = new int[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            copy[i] = matrix[i].clone();
        }
        return copy;
    }

    /* **********************************************************************
     * Test Harness Types
     * **********************************************************************/

    static class TestCase {

        final String id;
        final int[][] matrix;
        final int[] src;
        final int[] dst;
        final Result expected;
        final String description;

        TestCase(
                String id,
                int[][] matrix,
                int[] src,
                int[] dst,
                Result expected,
                String description) {

            this.id = id;
            this.matrix = matrix;
            this.src = src;
            this.dst = dst;
            this.expected = expected;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {

        Result solve(int[][] matrix, int[] src, int[] dst);
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

        return a.result() == b.result() && a.valid() == b.valid();
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

                Result actual = method.solve(test.matrix, test.src, test.dst);

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
                            "  src = %s, dst = %s%n",
                            Arrays.toString(test.src),
                            Arrays.toString(test.dst));

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
     * Randomised Testing
     * **********************************************************************/

    static int[][] randomMaze(Random rng, int maxSize, double openProbability) {
        int m = rng.nextInt(maxSize) + 1;
        int n = rng.nextInt(maxSize) + 1;
        int[][] maze = new int[m][n];
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                maze[i][j] = rng.nextDouble() < openProbability ? 1 : 0;
            }
        }
        return maze;
    }

    static void runRandomisedTests(int iterations) {

        System.out.println(
                "======================================================");
        System.out.println(
                "Randomised Cross Checks (DFS vs BFS)");
        System.out.println(
                "======================================================");

        Random rng = new Random(20260828L);

        for (int i = 1; i <= iterations; i++) {

            // Kept small (up to 6x6): DFS here is exhaustive/exponential.
            int[][] maze = randomMaze(rng, 6, 0.6);
            int m = maze.length;
            int n = maze[0].length;

            int[] src = {rng.nextInt(m), rng.nextInt(n)};
            int[] dst = {rng.nextInt(m), rng.nextInt(n)};

            Result dfsResult = shortestPathBinaryMazeDFS(maze, src, dst);
            Result bfsResult = shortestPathBinaryMazeBFS(maze, src, dst);

            if (!resultsEqual(dfsResult, bfsResult)) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "maze = " + Arrays.deepToString(maze));

                System.out.println(
                        "src = " + Arrays.toString(src) + ", dst = " + Arrays.toString(dst));

                System.out.println(
                        "dfs = " + dfsResult);

                System.out.println(
                        "bfs = " + bfsResult);

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
         * (expected values verified with an independent Python BFS
         * before being hardcoded here)
         * ============================================================
         */

        tests.add(new TestCase(
                "B1",
                new int[][]{
                        {1, 0, 0, 0},
                        {1, 1, 0, 1},
                        {0, 1, 0, 1},
                        {0, 1, 1, 1},
                        {0, 0, 0, 1}
                },
                new int[]{0, 0}, new int[]{4, 3},
                new Result(7, true),
                "basic 5x4 maze with a winding path"));

        tests.add(new TestCase(
                "B2",
                new int[][]{
                        {1, 1, 1, 0, 1},
                        {0, 0, 1, 0, 1},
                        {1, 1, 1, 1, 1},
                        {1, 0, 0, 0, 0},
                        {1, 1, 1, 1, 1}
                },
                new int[]{0, 0}, new int[]{4, 4},
                new Result(12, true),
                "larger 5x5 maze"));

        tests.add(new TestCase(
                "B3",
                new int[][]{
                        {1, 1},
                        {1, 1}
                },
                new int[]{1, 1}, new int[]{1, 1},
                new Result(0, true),
                "src equals dst: distance 0"));

        /*
         * ============================================================
         * Boundary Regression
         * (guards the original "nx > 0 / ny > 0" bug in the BFS method,
         * which made row/column 0 permanently unreachable as an
         * expansion target - only usable as the literal starting cell)
         * ============================================================
         */

        tests.add(new TestCase(
                "R1",
                new int[][]{
                        {1, 1, 0},
                        {1, 0, 0},
                        {1, 1, 0}
                },
                new int[]{0, 1}, new int[]{2, 1},
                new Result(4, true),
                "the only path is forced through column 0"));

        /*
         * ============================================================
         * Unreachable / Blocked Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "N1",
                new int[][]{
                        {1, 0, 1},
                        {0, 0, 1},
                        {1, 1, 1}
                },
                new int[]{0, 0}, new int[]{2, 2},
                new Result(-1, false),
                "src is walled off from dst entirely"));

        tests.add(new TestCase(
                "N2",
                new int[][]{
                        {1, 1},
                        {1, 0}
                },
                new int[]{0, 0}, new int[]{1, 1},
                new Result(-1, false),
                "dst cell itself is blocked"));

        /*
         * ============================================================
         * Added Robustness: Out-of-Bounds Positions, and the &&/|| Fix
         * (the original guard used && instead of ||, so it only rejected
         * input when the matrix AND src AND dst were ALL invalid at
         * once - an invalid matrix with superficially fine-looking src/dst
         * arrays slipped through and crashed instead of failing gracefully)
         * ============================================================
         */

        tests.add(new TestCase(
                "V1",
                null, new int[]{0, 0}, new int[]{0, 0},
                new Result(-1, false),
                "null matrix with otherwise well-formed src/dst"));

        tests.add(new TestCase(
                "V2",
                new int[][]{{1, 1}, {1, 1}}, new int[]{5, 0}, new int[]{0, 0},
                new Result(-1, false),
                "src row is out of bounds"));

        tests.add(new TestCase(
                "V3",
                new int[][]{{1, 1}, {1, 1}}, new int[]{0, 0}, new int[]{0},
                new Result(-1, false),
                "dst has the wrong length"));

        System.out.println(
                "############################################################");
        System.out.println(
                "###############  SHORTEST PATH BINARY MAZE  ################");
        System.out.println(
                "############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "DFS (exhaustive)",
                        ShortestPathBinaryMaze::shortestPathBinaryMazeDFS),

                new MethodCase(
                        "BFS",
                        ShortestPathBinaryMaze::shortestPathBinaryMazeBFS)
        );

        for (MethodCase method : methods) {

            runTests(
                    method.name,
                    method.algorithm,
                    tests);
        }

        runRandomisedTests(3000);
    }
}
