import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Number of Islands.
 *
 * Problem:
 * Given an m x n grid of 'L' (land) and 'W' (water) cells, count the number
 * of islands. An island is a maximal group of 'L' cells connected
 * 8-directionally (horizontally, vertically, and diagonally) - this matches
 * the direction tables in the original implementation. (Classic "Number of
 * Islands" / LeetCode 200 uses 4-directional adjacency instead; if that is
 * actually what you want, drop the four diagonal offsets from DIR_ROW/DIR_COL.)
 *
 * Notes:
 * - If the grid is invalid (null / empty / zero-width first row),
 *   Result.valid() == false.
 * - Only the first row's length is used to validate/size the grid, matching
 *   the original code's behaviour; ragged rows are not checked.
 *
 * Implementations:
 *
 * 1. DFS (recursive flood fill)
 * 2. BFS (iterative flood fill)
 * 3. Disjoint Set / Union-Find
 *
 * All three are cross-checked against each other for both fixed and
 * randomised test grids, following the same test-harness shape used for
 * SumOfDiagonalsMatrixTestHarness (TestCase / Algorithm / MethodCase /
 * runTests / randomised cross-checks).
 */
public class NumberOfIslands {

    /** 8-connectivity: N, S, E, W and the four diagonals. */
    private static final int[] DIR_ROW = {-1, -1, -1, 0, 0, 1, 1, 1};
    private static final int[] DIR_COL = {-1, 0, 1, -1, 1, -1, 0, 1};

    /* **********************************************************************
     * Validation
     * **********************************************************************/

    static boolean validGrid(char[][] grid) {
        return grid != null && grid.length > 0 && grid[0] != null && grid[0].length > 0;
    }

    /* **********************************************************************
     * Result Record
     * **********************************************************************/

    static record Result(int count, boolean valid) {}

    /* **********************************************************************
     * Algorithm Implementations
     * **********************************************************************/

    /**
     * DFS (recursive flood fill).
     * Time: O(m * n)   Space: O(m * n) for visited + recursion stack.
     */
    static Result numberOfIslandsDFS(char[][] grid) {
        if (!validGrid(grid)) {
            return new Result(0, false);
        }

        int m = grid.length;
        int n = grid[0].length;
        boolean[][] visited = new boolean[m][n];
        int result = 0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 'L' && !visited[i][j]) {
                    dfs(grid, i, j, visited);
                    result++;
                }
            }
        }

        return new Result(result, true);
    }

    /**
     * BFS (iterative flood fill).
     * Time: O(m * n)   Space: O(m * n) for visited + queue.
     */
    static Result numberOfIslandsBFS(char[][] grid) {
        if (!validGrid(grid)) {
            return new Result(0, false);
        }

        int m = grid.length;
        int n = grid[0].length;
        boolean[][] visited = new boolean[m][n];
        int result = 0;

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 'L' && !visited[i][j]) {
                    bfs(grid, i, j, visited);
                    result++;
                }
            }
        }

        return new Result(result, true);
    }

    /**
     * Disjoint Set / Union-Find.
     * Time: O(m * n * alpha(m * n))   Space: O(m * n).
     */
    static Result numberOfIslandsDisjointSet(char[][] grid) {
        if (!validGrid(grid)) {
            return new Result(0, false);
        }

        int m = grid.length;
        int n = grid[0].length;
        int[] parent = new int[m * n];
        int[] rank = new int[m * n];
        for (int i = 0; i < m * n; i++) {
            parent[i] = i;
        }

        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] != 'L') {
                    continue;
                }

                int curr = i * n + j;
                for (int k = 0; k < DIR_ROW.length; k++) {
                    int newRow = i + DIR_ROW[k];
                    int newCol = j + DIR_COL[k];
                    if (newRow >= 0 && newRow < m && newCol >= 0 && newCol < n
                            && grid[newRow][newCol] == 'L') {
                        int next = newRow * n + newCol;
                        unite(curr, next, parent, rank);
                    }
                }
            }
        }

        HashSet<Integer> roots = new HashSet<>();
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                if (grid[i][j] == 'L') {
                    roots.add(find(i * n + j, parent));
                }
            }
        }

        return new Result(roots.size(), true);
    }

    /* **********************************************************************
     * Traversal Helpers
     * **********************************************************************/

    static void dfs(char[][] grid, int row, int col, boolean[][] visited) {
        visited[row][col] = true;
        for (int k = 0; k < DIR_ROW.length; k++) {
            int newRow = row + DIR_ROW[k];
            int newCol = col + DIR_COL[k];
            if (isSafe(grid, newRow, newCol, visited)) {
                dfs(grid, newRow, newCol, visited);
            }
        }
    }

    static void bfs(char[][] grid, int row, int col, boolean[][] visited) {
        Queue<int[]> queue = new LinkedList<>();
        visited[row][col] = true;
        queue.add(new int[] {row, col});

        while (!queue.isEmpty()) {
            int[] cell = queue.poll();
            int currRow = cell[0];
            int currCol = cell[1];

            for (int k = 0; k < DIR_ROW.length; k++) {
                int newRow = currRow + DIR_ROW[k];
                int newCol = currCol + DIR_COL[k];
                if (isSafe(grid, newRow, newCol, visited)) {
                    visited[newRow][newCol] = true;
                    queue.add(new int[] {newRow, newCol});
                }
            }
        }
    }

    static boolean isSafe(char[][] grid, int row, int col, boolean[][] visited) {
        int m = grid.length;
        int n = grid[0].length;

        return row >= 0 && row < m && col >= 0 && col < n
                && grid[row][col] == 'L' && !visited[row][col];
    }

    /* **********************************************************************
     * Union-Find Helpers
     * **********************************************************************/

    static void unite(int a, int b, int[] parent, int[] rank) {
        int rootA = find(a, parent);
        int rootB = find(b, parent);
        if (rootA == rootB) {
            return;
        }
        if (rank[rootA] < rank[rootB]) {
            parent[rootA] = rootB;
        } else if (rank[rootA] > rank[rootB]) {
            parent[rootB] = rootA;
        } else {
            parent[rootB] = rootA;
            rank[rootA]++;
        }
    }

    static int find(int cell, int[] parent) {
        if (parent[cell] != cell) {
            parent[cell] = find(parent[cell], parent);
        }
        return parent[cell];
    }

    /* **********************************************************************
     * Test Harness Types
     * **********************************************************************/

    static class TestCase {

        final String id;
        final char[][] input;
        final Result expected;
        final String description;

        TestCase(
                String id,
                char[][] input,
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

        Result solve(char[][] grid);
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

    static char[][] cloneGrid(char[][] grid) {

        if (grid == null) {
            return null;
        }

        int rows = grid.length;
        char[][] copy = new char[rows][];

        for (int i = 0; i < rows; i++) {
            copy[i] = grid[i] == null ? null : grid[i].clone();
        }

        return copy;
    }

    static String formatGrid(char[][] grid) {

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

    static boolean resultsEqual(Result a, Result b) {

        if (a == null && b == null) {
            return true;
        }

        if (a == null || b == null) {
            return false;
        }

        return a.count() == b.count() && a.valid() == b.valid();
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

                Result actual = method.solve(cloneGrid(test.input));

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
                            formatGrid(test.input));

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
                        formatGrid(test.input));

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

    static char[][] randomGrid(
            Random rng,
            int maxRows,
            int maxCols,
            double landProbability) {

        int rows = rng.nextInt(maxRows) + 1;
        int cols = rng.nextInt(maxCols) + 1;

        char[][] grid = new char[rows][cols];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = rng.nextDouble() < landProbability ? 'L' : 'W';
            }
        }

        return grid;
    }

    static void runRandomisedTests(int iterations) {

        System.out.println(
                "======================================================");
        System.out.println(
                "Randomised Cross Checks (DFS vs BFS vs Disjoint Set)");
        System.out.println(
                "======================================================");

        Random rng = new Random(20260819L);

        for (int i = 1; i <= iterations; i++) {

            // Vary land density across runs so both sparse and dense grids
            // (and both diagonal-only and orthogonal connections) get exercised.
            double density = 0.2 + 0.6 * rng.nextDouble();

            char[][] grid = randomGrid(rng, 8, 8, density);

            Result dfs = numberOfIslandsDFS(cloneGrid(grid));
            Result bfs = numberOfIslandsBFS(cloneGrid(grid));
            Result ds = numberOfIslandsDisjointSet(cloneGrid(grid));

            if (!resultsEqual(dfs, bfs) || !resultsEqual(dfs, ds)) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "grid = " + formatGrid(grid));

                System.out.println(
                        "dfs  = " + dfs);

                System.out.println(
                        "bfs  = " + bfs);

                System.out.println(
                        "ds   = " + ds);

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
         * Basic Connectivity
         * ============================================================
         */

        tests.add(new TestCase(
                "B1",
                new char[][]{
                        {'L', 'L'},
                        {'L', 'L'}
                },
                new Result(1, true),
                "2x2 solid land block, one island"));

        tests.add(new TestCase(
                "B2",
                new char[][]{
                        {'W', 'W'},
                        {'W', 'W'}
                },
                new Result(0, true),
                "2x2 all water, no islands"));

        tests.add(new TestCase(
                "B3",
                new char[][]{
                        {'L', 'L', 'W', 'W'},
                        {'L', 'L', 'W', 'W'},
                        {'W', 'W', 'W', 'W'},
                        {'W', 'W', 'W', 'W'}
                },
                new Result(1, true),
                "single 2x2 island in an otherwise empty grid"));

        tests.add(new TestCase(
                "B4",
                new char[][]{
                        {'L', 'L', 'W', 'W', 'W'},
                        {'L', 'L', 'W', 'W', 'W'},
                        {'W', 'W', 'W', 'W', 'W'},
                        {'W', 'W', 'W', 'L', 'L'},
                        {'W', 'W', 'W', 'L', 'L'}
                },
                new Result(2, true),
                "two orthogonally-separated 2x2 islands"));

        /*
         * ============================================================
         * Diagonal (8-directional) Connectivity
         * ============================================================
         */

        tests.add(new TestCase(
                "D1",
                new char[][]{
                        {'L', 'W'},
                        {'W', 'L'}
                },
                new Result(1, true),
                "diagonal-only connection, \"\\\" direction"));

        tests.add(new TestCase(
                "D2",
                new char[][]{
                        {'W', 'L'},
                        {'L', 'W'}
                },
                new Result(1, true),
                "diagonal-only connection, \"/\" direction"));

        tests.add(new TestCase(
                "D3",
                new char[][]{
                        {'L', 'W', 'L'},
                        {'W', 'L', 'W'},
                        {'L', 'W', 'L'}
                },
                new Result(1, true),
                "checkerboard corners all linked through the center via diagonals"));

        tests.add(new TestCase(
                "D4",
                new char[][]{
                        {'L', 'W', 'W', 'W', 'W', 'W'},
                        {'W', 'L', 'W', 'W', 'W', 'W'},
                        {'W', 'W', 'L', 'W', 'W', 'W'},
                        {'W', 'W', 'W', 'W', 'W', 'W'},
                        {'W', 'W', 'W', 'W', 'L', 'W'},
                        {'W', 'W', 'W', 'W', 'W', 'L'}
                },
                new Result(2, true),
                "two diagonal chains separated by a water row"));

        /*
         * ============================================================
         * Boundary Regression (guards the original col > 0 bug, which
         * made column 0 unreachable as a *neighbour* and could split a
         * single island into two whenever it touched the left edge)
         * ============================================================
         */

        tests.add(new TestCase(
                "R1",
                new char[][]{
                        {'L', 'L'},
                        {'L', 'W'}
                },
                new Result(1, true),
                "island touching column 0, reached only via a neighbour scan"));

        tests.add(new TestCase(
                "R2",
                new char[][]{
                        {'W', 'L', 'W'},
                        {'L', 'W', 'L'},
                        {'W', 'L', 'W'}
                },
                new Result(1, true),
                "diamond touching column 0, linked only by diagonals"));

        /*
         * ============================================================
         * Mixed Layout
         * ============================================================
         */

        tests.add(new TestCase(
                "M1",
                new char[][]{
                        {'L', 'W', 'L'},
                        {'L', 'W', 'L'},
                        {'W', 'W', 'W'},
                        {'L', 'L', 'W'}
                },
                new Result(3, true),
                "three islands: two vertical pairs plus one horizontal pair"));

        tests.add(new TestCase(
                "M2",
                new char[][]{
                        {'L'}
                },
                new Result(1, true),
                "1x1 grid, single land cell"));

        tests.add(new TestCase(
                "M3",
                new char[][]{
                        {'W'}
                },
                new Result(0, true),
                "1x1 grid, single water cell"));

        /*
         * ============================================================
         * Edge Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "E1",
                null,
                new Result(0, false),
                "null grid"));

        tests.add(new TestCase(
                "E2",
                new char[][]{},
                new Result(0, false),
                "empty grid (zero rows)"));

        tests.add(new TestCase(
                "E3",
                new char[][]{
                        {},
                        {},
                        {}
                },
                new Result(0, false),
                "grid with zero columns"));

        System.out.println(
                "############################################################");
        System.out.println(
                "######################  NUMBER OF ISLANDS  ################");
        System.out.println(
                "############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "DFS (recursive flood fill)",
                        NumberOfIslands::numberOfIslandsDFS),

                new MethodCase(
                        "BFS (iterative flood fill)",
                        NumberOfIslands::numberOfIslandsBFS),

                new MethodCase(
                        "Disjoint Set / Union-Find",
                        NumberOfIslands::numberOfIslandsDisjointSet)
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
