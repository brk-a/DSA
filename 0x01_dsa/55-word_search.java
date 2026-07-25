import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Word Search in 2D Grid Test Harness.
 *
 * Problem:
 * Given a 2D grid of characters (matrix) and a target string, find all starting
 * coordinates (row, col) where the word appears in straight line in any of
 * the eight directions:
 *
 *  (-1,-1), (-1,0), (-1,1),
 *  ( 0,-1),         ( 0,1),
 *  ( 1,-1), ( 1,0), ( 1,1)
 *
 * Implementations:
 *
 * 1. wordSearchRecursion (Oracle)
 *    Uses a recursive helper to check a given direction.
 *
 * 2. wordSearchIteration
 *    Uses the iterative search2D helper to check all 8 directions.
 *
 * Both return int[][] where each row is {row, col} for a match.
 */
public class WordSearchTestHarness {

    /* **********************************************************************
     * Core Algorithm Implementations
     * **********************************************************************/

    /**
     * Recursive implementation (oracle).
     *
     * For every cell and every direction, recursively checks the word.
     * Returns all starting coordinates where the full word is matched
     * in that direction.
     */
    static int[][] wordSearchRecursion(char[][] matrix, String target) {

        if (!validMatrix(matrix) || target == null) {
            return new int[][]{};
        }

        if (target.length() == 0) {
            // Convention: empty word "found" at (0,0) if matrix valid
            return new int[][]{{0, 0}};
        }

        int m = matrix.length;
        int n = matrix[0].length;

        List<int[]> result = new ArrayList<>();

        // Directions: 8 directions (dx, dy)
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int row = 0; row < m; row++) {
            for (int col = 0; col < n; col++) {

                for (int dir = 0; dir < 8; dir++) {

                    if (findTargetRecursion(
                            0,
                            target,
                            matrix,
                            row,
                            col,
                            dx[dir],
                            dy[dir])) {

                        result.add(new int[]{row, col});
                        break; // avoid duplicate entries for same start
                    }
                }
            }
        }

        return result.toArray(new int[0][]);
    }

    /**
     * Iterative implementation using search2D helper.
     *
     * For every cell, calls search2D which scans all 8 directions
     * iteratively. Collects all starting coordinates.
     */
    static int[][] wordSearchIteration(char[][] matrix, String target) {

        if (!validMatrix(matrix) || target == null) {
            return new int[][]{};
        }

        if (target.length() == 0) {
            // Same convention as recursion
            return new int[][]{{0, 0}};
        }

        int m = matrix.length;
        int n = matrix[0].length;

        // Max possible occurrences
        int[][] tmp = new int[m * n][2];
        int count = 0;

        for (int row = 0; row < m; row++) {
            for (int col = 0; col < n; col++) {

                if (search2D(matrix, row, col, target)) {
                    tmp[count][0] = row;
                    tmp[count][1] = col;
                    count++;
                }
            }
        }

        // Resize to actual number of found coordinates
        int[][] result = new int[count][2];
        for (int i = 0; i < count; i++) {
            result[i][0] = tmp[i][0];
            result[i][1] = tmp[i][1];
        }

        return result;
    }


    /* **********************************************************************
     * Recursive Helper and Utilities
     * **********************************************************************/

    /**
     * Recursive helper: does the word starting at index 'idx'
     * match along direction (dirX, dirY) starting from (x, y)?
     */
    static boolean findTargetRecursion(
            int idx,
            String target,
            char[][] matrix,
            int x,
            int y,
            int dirX,
            int dirY) {

        int m = matrix.length;
        int n = matrix[0].length;

        // If we matched all characters, success
        if (idx == target.length()) {
            return true;
        }

        // Out of bounds or mismatch => fail
        if (!validCoordinates(x, y, m, n)) {
            return false;
        }

        if (matrix[x][y] != target.charAt(idx)) {
            return false;
        }

        // Move to next character along direction
        return findTargetRecursion(
                idx + 1,
                target,
                matrix,
                x + dirX,
                y + dirY,
                dirX,
                dirY);
    }

    static boolean validCoordinates(int x, int y, int m, int n) {
        return x >= 0 && x < m && y >= 0 && y < n;
    }

    /**
     * Iterative search in all 8 directions from (row, col).
     * Mirrors the standard 8-direction word search used in
     * common grid word search problems.[web:11]
     */
    static boolean search2D(char[][] matrix, int row, int col, String target) {

        int m = matrix.length;
        int n = matrix[0].length;

        // First char must match
        if (matrix[row][col] != target.charAt(0)) {
            return false;
        }

        int len = target.length();

        // Eight directions
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

        for (int dir = 0; dir < 8; dir++) {

            int currX = row + dx[dir];
            int currY = col + dy[dir];

            int k;
            // First char already checked; match remaining
            for (k = 1; k < len; k++) {

                if (!validCoordinates(currX, currY, m, n)) {
                    break;
                }

                if (matrix[currX][currY] != target.charAt(k)) {
                    break;
                }

                currX += dx[dir];
                currY += dy[dir];
            }

            if (k == len) {
                return true;
            }
        }

        return false;
    }

    static boolean validMatrix(char[][] matrix) {
        return matrix != null
                && matrix.length > 0
                && matrix[0] != null
                && matrix[0].length > 0;
    }

    /* **********************************************************************
     * Test Harness Types
     * **********************************************************************/

    static class TestCase {

        final String id;
        final char[][] input;
        final String target;
        final int[][] expected;
        final String description;

        TestCase(
                String id,
                char[][] input,
                String target,
                int[][] expected,
                String description) {

            this.id = id;
            this.input = input;
            this.target = target;
            this.expected = expected;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface WordSearchAlgorithm {
        int[][] solve(char[][] matrix, String target);
    }

    static class MethodCase {

        final String name;
        final WordSearchAlgorithm algorithm;

        MethodCase(
                String name,
                WordSearchAlgorithm algorithm) {

            this.name = name;
            this.algorithm = algorithm;
        }
    }

    /* **********************************************************************
     * Utilities
     * **********************************************************************/

    static char[][] cloneMatrix(char[][] matrix) {

        if (matrix == null) {
            return null;
        }

        char[][] clone = new char[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            clone[i] = matrix[i] == null ? null : matrix[i].clone();
        }
        return clone;
    }

    static String formatMatrix(char[][] matrix) {

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

    static String formatCoords(int[][] coords) {

        if (coords == null) {
            return "null";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < coords.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(Arrays.toString(coords[i]));
        }
        sb.append("]");
        return sb.toString();
    }

    static boolean coordsEqual(int[][] a, int[][] b) {

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

            if (a[i] == null && b[i] == null) {
                continue;
            }

            if (a[i] == null || b[i] == null) {
                return false;
            }

            if (a[i].length != b[i].length) {
                return false;
            }

            for (int j = 0; j < a[i].length; j++) {
                if (a[i][j] != b[i][j]) {
                    return false;
                }
            }
        }

        return true;
    }

    static void runTests(
            String algorithmName,
            WordSearchAlgorithm method,
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

                int[][] actual =
                        method.solve(
                                cloneMatrix(test.input),
                                test.target);

                if (coordsEqual(actual, test.expected)) {

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
                            "  target    = %s%n",
                            test.target);

                    System.out.printf(
                            "  expected  = %s%n",
                            formatCoords(test.expected));

                    System.out.printf(
                            "  actual    = %s%n",
                            formatCoords(actual));
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
                        "  target    = %s%n",
                        test.target);

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

    static char[][] randomCharMatrix(
            Random rng,
            int maxRows,
            int maxCols) {

        int rows = rng.nextInt(maxRows + 1);
        int cols = rng.nextInt(maxCols + 1);

        if (rows == 0 || cols == 0) {
            return new char[0][0];
        }

        char[][] matrix = new char[rows][cols];

        // Just use uppercase letters A-Z
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                matrix[r][c] = (char) ('A' + rng.nextInt(26));
            }
        }

        return matrix;
    }

    static String randomWordFromMatrix(
            Random rng,
            char[][] matrix,
            int maxLen) {

        if (!validMatrix(matrix)) {
            return "";
        }

        int m = matrix.length;
        int n = matrix[0].length;

        // Pick a random starting cell
        int row = rng.nextInt(m);
        int col = rng.nextInt(n);

        // Eight directions
        int[] dx = {-1, -1, -1, 0, 0, 1, 1, 1};
        int[] dy = {-1, 0, 1, -1, 1, -1, 0, 1};

        int dir = rng.nextInt(8);

        StringBuilder sb = new StringBuilder();
        sb.append(matrix[row][col]);

        int len = 1 + rng.nextInt(maxLen); // length between 1 and maxLen

        int currX = row;
        int currY = col;

        for (int k = 1; k < len; k++) {

            int nextX = currX + dx[dir];
            int nextY = currY + dy[dir];

            if (!validCoordinates(nextX, nextY, m, n)) {
                break;
            }

            sb.append(matrix[nextX][nextY]);

            currX = nextX;
            currY = nextY;
        }

        return sb.toString();
    }

    static void runRandomisedTests(int iterations) {

        System.out.println(
                "======================================================");
        System.out.println(
                "Randomised Cross Checks (Iteration vs Recursion)");
        System.out.println(
                "======================================================");

        Random rng = new Random(192837465L);

        for (int i = 1; i <= iterations; i++) {

            char[][] matrix = randomCharMatrix(
                    rng,
                    6,
                    6);

            if (!validMatrix(matrix)) {
                continue;
            }

            String target = randomWordFromMatrix(
                    rng,
                    matrix,
                    5);

            int[][] oracle =
                    wordSearchRecursion(
                            cloneMatrix(matrix),
                            target);

            int[][] candidate =
                    wordSearchIteration(
                            cloneMatrix(matrix),
                            target);

            if (!coordsEqual(oracle, candidate)) {

                System.out.println(
                        "Randomised test FAILED (Iteration vs Recursion)");

                System.out.println(
                        "matrix    = " + formatMatrix(matrix));

                System.out.println(
                        "target    = " + target);

                System.out.println(
                        "oracle    = " + formatCoords(oracle));

                System.out.println(
                        "candidate = " + formatCoords(candidate));

                return;
            }
        }

        System.out.printf(
                "All %d Randomised tests passed (Iteration vs Recursion).%n%n",
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

        char[][] grid1 = {
                {'C', 'A', 'T'},
                {'X', 'Z', 'T'},
                {'Y', 'O', 'G'}
        };

        // "CAT" horizontally at (0,0)
        tests.add(new TestCase(
                "S1",
                grid1,
                "CAT",
                new int[][]{{0, 0}},
                "Simple horizontal match"));

        // "TOG" diagonal from (1,2) down-left to (2,1)? Actually grid1 doesn't
        // have that exactly, so use "CZTG" etc. We'll keep it simple:
        // "T" occurs at (0,2) and (1,2); single-letter matches should be both.
        tests.add(new TestCase(
                "S2",
                grid1,
                "T",
                new int[][]{{0, 2}, {1, 2}},
                "Single letter matches"));

        char[][] grid2 = {
                {'A', 'B', 'C', 'D'},
                {'E', 'F', 'G', 'H'},
                {'I', 'J', 'K', 'L'},
                {'M', 'N', 'O', 'P'}
        };

        // "ABCD" from (0,0) to the right
        tests.add(new TestCase(
                "S3",
                grid2,
                "ABCD",
                new int[][]{{0, 0}},
                "Top row left-to-right"));

        // "AEIM" from (0,0) downwards
        tests.add(new TestCase(
                "S4",
                grid2,
                "AEIM",
                new int[][]{{0, 0}},
                "First column top-to-bottom"));

        // "DGJM" diagonal down-left starting at (0,3)
        tests.add(new TestCase(
                "S5",
                grid2,
                "DGJM",
                new int[][]{{0, 3}},
                "Diagonal down-left from (0,3)"));

        /*
         * ============================================================
         * Edge Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "E1",
                null,
                "ANY",
                new int[][]{},
                "Null matrix"));

        tests.add(new TestCase(
                "E2",
                new char[][]{},
                "ANY",
                new int[][]{},
                "Empty outer array"));

        tests.add(new TestCase(
                "E3",
                grid1,
                "",
                new int[][]{{0, 0}},
                "Empty target word"));

        System.out.println(
                "############################################################");
        System.out.println(
                "################## WORD SEARCH (CHAR GRID) #################");
        System.out.println(
                "############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "Recursive (Oracle)",
                        WordSearchTestHarness::wordSearchRecursion),

                new MethodCase(
                        "Iterative search2D",
                        WordSearchTestHarness::wordSearchIteration)
        );

        for (MethodCase method : methods) {

            runTests(
                    method.name,
                    method.algorithm,
                    tests);
        }

        runRandomisedTests(2000);
    }
}