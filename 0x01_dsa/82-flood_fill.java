import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Flood Fill.
 *
 * Problem:
 * Given an m x n grid of integer "colours", a starting pixel (sr, sc), and
 * a new colour, recolour every pixel connected to (sr, sc) (4-directionally:
 * up, down, left, right) that shares the starting pixel's original colour.
 *
 * Notes:
 * - Unlike the other files in this series, flood fill is defined as an
 *   IN-PLACE mutation: both implementations modify the grid they're given
 *   and return that same reference. This is intentional (it matches the
 *   standard "Flood Fill" problem statement, e.g. LeetCode 733) and is not
 *   something to "fix" - the test harness below clones each test's input
 *   before every call specifically so that running DFS doesn't consume the
 *   fixture BFS needs afterwards.
 * - If the grid is invalid (null / empty / zero-width first row), or if
 *   (sr, sc) falls outside the grid, both methods return null.
 * - If the starting pixel already has newColour, both methods return the
 *   grid unchanged (this also sidesteps the classic "new colour equals old
 *   colour" infinite-recursion trap, since dfs()/bfs() only ever get called
 *   once old != new is already guaranteed).
 *
 * Implementations:
 *
 * 1. DFS (recursive flood fill)
 * 2. BFS (iterative flood fill)
 *
 * Both are cross-checked against each other for both fixed and randomised
 * test grids, following the same test-harness shape used for
 * SumOfDiagonalsMatrixTestHarness (TestCase / Algorithm / MethodCase /
 * runTests / randomised cross-checks).
 */
public class FloodFill {

    private static final int[][] DIRECTIONS = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};

    /* **********************************************************************
     * Validation
     * **********************************************************************/

    static boolean validGrid(int[][] grid) {
        return grid != null && grid.length > 0 && grid[0] != null && grid[0].length > 0;
    }

    static boolean validStart(int[][] grid, int sr, int sc) {
        return sr >= 0 && sr < grid.length && sc >= 0 && sc < grid[0].length;
    }

    /* **********************************************************************
     * Algorithm Implementations
     * **********************************************************************/

    static int[][] floodFillDFS(int[][] grid, int sr, int sc, int newColour) {
        if (!validGrid(grid) || !validStart(grid, sr, sc)) {
            return null;
        }

        if (grid[sr][sc] == newColour) {
            return grid;
        }

        int oldColour = grid[sr][sc];
        dfs(grid, sr, sc, oldColour, newColour);

        return grid;
    }

    static int[][] floodFillBFS(int[][] grid, int sr, int sc, int newColour) {
        if (!validGrid(grid) || !validStart(grid, sr, sc)) {
            return null;
        }

        if (grid[sr][sc] == newColour) {
            return grid;
        }

        Queue<int[]> q = new LinkedList<>();
        int oldColour = grid[sr][sc];
        q.add(new int[]{sr, sc});

        // Change the colour of the starting pixel
        grid[sr][sc] = newColour;

        // Perform BFS
        while (!q.isEmpty()) {
            int[] front = q.poll();
            int x = front[0];
            int y = front[1];

            // Traverse all 4 directions
            for (int[] d : DIRECTIONS) {
                int nx = x + d[0];
                int ny = y + d[1];

                // Check boundary conditions and colour match
                if (nx >= 0 && nx < grid.length && ny >= 0 && ny < grid[0].length
                        && grid[nx][ny] == oldColour) {
                    grid[nx][ny] = newColour;
                    q.add(new int[]{nx, ny});
                }
            }
        }

        return grid;
    }

    /* **********************************************************************
     * Helper
     * **********************************************************************/

    static void dfs(int[][] grid, int x, int y, int oldColour, int newColour) {
        if (x < 0 || x >= grid.length
                || y < 0 || y >= grid[0].length
                || grid[x][y] != oldColour) {

            return;
        }

        // Update the colour of the current pixel
        grid[x][y] = newColour;

        // Recursively visit all 4 connected neighbours
        dfs(grid, x + 1, y, oldColour, newColour);
        dfs(grid, x - 1, y, oldColour, newColour);
        dfs(grid, x, y + 1, oldColour, newColour);
        dfs(grid, x, y - 1, oldColour, newColour);
    }

    /* **********************************************************************
     * Test Harness Types
     * **********************************************************************/

    static class TestCase {

        final String id;
        final int[][] input;
        final int sr;
        final int sc;
        final int newColour;
        final int[][] expected;
        final String description;

        TestCase(
                String id,
                int[][] input,
                int sr,
                int sc,
                int newColour,
                int[][] expected,
                String description) {

            this.id = id;
            this.input = input;
            this.sr = sr;
            this.sc = sc;
            this.newColour = newColour;
            this.expected = expected;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {

        int[][] solve(int[][] grid, int sr, int sc, int newColour);
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

    static int[][] cloneGrid(int[][] grid) {

        if (grid == null) {
            return null;
        }

        int rows = grid.length;
        int[][] copy = new int[rows][];

        for (int i = 0; i < rows; i++) {
            copy[i] = grid[i] == null ? null : grid[i].clone();
        }

        return copy;
    }

    static String formatGrid(int[][] grid) {

        if (grid == null) {
            return "null";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < grid.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(grid[i] == null ? "null" : Arrays.toString(grid[i]));
        }
        sb.append("]");
        return sb.toString();
    }

    static boolean gridsEqual(int[][] a, int[][] b) {

        if (a == null && b == null) {
            return true;
        }

        if (a == null || b == null) {
            return false;
        }

        if (a.length != b.length) {
            return false;
        }

        for (int i = 0; i < a.length; i++) {
            if (!Arrays.equals(a[i], b[i])) {
                return false;
            }
        }

        return true;
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

                int[][] actual = method.solve(
                        cloneGrid(test.input),
                        test.sr,
                        test.sc,
                        test.newColour);

                if (gridsEqual(actual, test.expected)) {

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
                            "  input     = %s, sr=%d, sc=%d, newColour=%d%n",
                            formatGrid(test.input),
                            test.sr,
                            test.sc,
                            test.newColour);

                    System.out.printf(
                            "  expected  = %s%n",
                            formatGrid(test.expected));

                    System.out.printf(
                            "  actual    = %s%n",
                            formatGrid(actual));
                }

            } catch (Exception ex) {

                failed++;

                System.out.printf(
                        "✗ %s (%s)%n",
                        test.id,
                        test.description);

                System.out.printf(
                        "  input     = %s, sr=%d, sc=%d, newColour=%d%n",
                        formatGrid(test.input),
                        test.sr,
                        test.sc,
                        test.newColour);

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

    static int[][] randomGrid(
            Random rng,
            int maxRows,
            int maxCols,
            int paletteSize) {

        int rows = rng.nextInt(maxRows) + 1;
        int cols = rng.nextInt(maxCols) + 1;

        int[][] grid = new int[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = rng.nextInt(paletteSize);
            }
        }

        return grid;
    }

    static void runRandomisedTests(int iterations) {

        System.out.println(
                "======================================================");
        System.out.println(
                "Randomised Cross Checks (DFS vs BFS)");
        System.out.println(
                "======================================================");

        Random rng = new Random(20260822L);

        for (int i = 1; i <= iterations; i++) {

            // A small palette (2-4 colours) keeps grids full of interesting
            // same-colour regions instead of mostly-unique cells.
            int paletteSize = 2 + rng.nextInt(3);
            int[][] grid = randomGrid(rng, 8, 8, paletteSize);

            int sr = rng.nextInt(grid.length);
            int sc = rng.nextInt(grid[0].length);
            int newColour = rng.nextInt(paletteSize + 1); // occasionally an unused colour

            int[][] dfsResult = floodFillDFS(cloneGrid(grid), sr, sc, newColour);
            int[][] bfsResult = floodFillBFS(cloneGrid(grid), sr, sc, newColour);

            if (!gridsEqual(dfsResult, bfsResult)) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "grid      = " + formatGrid(grid));

                System.out.println(
                        "sr,sc     = " + sr + "," + sc);

                System.out.println(
                        "newColour = " + newColour);

                System.out.println(
                        "dfs       = " + formatGrid(dfsResult));

                System.out.println(
                        "bfs       = " + formatGrid(bfsResult));

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
                new int[][]{
                        {1, 1, 1},
                        {1, 1, 0},
                        {1, 0, 1}
                },
                1, 1, 2,
                new int[][]{
                        {2, 2, 2},
                        {2, 2, 0},
                        {2, 0, 1}
                },
                "fills the connected region only; the isolated same-colour cell at (2,2) is untouched"));

        tests.add(new TestCase(
                "B2",
                new int[][]{{5}},
                0, 0, 7,
                new int[][]{{7}},
                "1x1 grid"));

        tests.add(new TestCase(
                "B3",
                new int[][]{
                        {1, 1},
                        {1, 1}
                },
                0, 0, 1,
                new int[][]{
                        {1, 1},
                        {1, 1}
                },
                "newColour equals starting colour: no-op"));

        tests.add(new TestCase(
                "B4",
                new int[][]{
                        {3, 3},
                        {3, 3}
                },
                1, 1, 9,
                new int[][]{
                        {9, 9},
                        {9, 9}
                },
                "solid grid, entire grid recoloured from a corner start"));

        /*
         * ============================================================
         * 4-Directional-Only Connectivity (no diagonals)
         * ============================================================
         */

        tests.add(new TestCase(
                "D1",
                new int[][]{
                        {1, 0, 1},
                        {0, 1, 0},
                        {1, 0, 1}
                },
                0, 0, 5,
                new int[][]{
                        {5, 0, 1},
                        {0, 1, 0},
                        {1, 0, 1}
                },
                "checkerboard: diagonal neighbours are NOT connected, only the start cell changes"));

        /*
         * ============================================================
         * Non-Square Grids
         * ============================================================
         */

        tests.add(new TestCase(
                "N1",
                new int[][]{
                        {0, 0, 0, 0, 0}
                },
                0, 2, 4,
                new int[][]{
                        {4, 4, 4, 4, 4}
                },
                "1x5 single row, fill from the middle spreads both ways"));

        tests.add(new TestCase(
                "N2",
                new int[][]{
                        {0},
                        {0},
                        {0}
                },
                1, 0, 6,
                new int[][]{
                        {6},
                        {6},
                        {6}
                },
                "3x1 single column"));

        /*
         * ============================================================
         * Boundary Validation
         * (added robustness: the original code never checked that
         * (sr, sc) actually falls inside the grid)
         * ============================================================
         */

        tests.add(new TestCase(
                "V1",
                new int[][]{
                        {1, 1},
                        {1, 1}
                },
                5, 0, 2,
                null,
                "sr out of bounds (too large)"));

        tests.add(new TestCase(
                "V2",
                new int[][]{
                        {1, 1},
                        {1, 1}
                },
                0, -1, 2,
                null,
                "sc out of bounds (negative)"));

        /*
         * ============================================================
         * Edge Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "E1",
                null,
                0, 0, 1,
                null,
                "null grid"));

        tests.add(new TestCase(
                "E2",
                new int[][]{},
                0, 0, 1,
                null,
                "empty grid (zero rows)"));

        tests.add(new TestCase(
                "E3",
                new int[][]{
                        {},
                        {},
                        {}
                },
                0, 0, 1,
                null,
                "grid with zero columns"));

        System.out.println(
                "############################################################");
        System.out.println(
                "##########################  FLOOD FILL  ###################");
        System.out.println(
                "############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "DFS (recursive flood fill)",
                        FloodFill::floodFillDFS),

                new MethodCase(
                        "BFS (iterative flood fill)",
                        FloodFill::floodFillBFS)
        );

        for (MethodCase method : methods) {

            runTests(
                    method.name,
                    method.algorithm,
                    tests);
        }

        // In-place mutation check: the returned grid must be the SAME
        // object the caller passed in (per the documented contract), and
        // must reflect the recolouring.
        int[][] original = {{1, 1}, {1, 0}};
        int[][] returned = floodFillDFS(original, 0, 0, 9);
        boolean sameReference = (returned == original);
        boolean mutatedCorrectly = gridsEqual(original, new int[][]{{9, 9}, {9, 0}});
        System.out.println(
                "In-place contract check: " + (sameReference && mutatedCorrectly
                        ? "PASS (same reference, correctly mutated)"
                        : "FAIL"));
        System.out.println();

        runRandomisedTests(5000);
    }
}
