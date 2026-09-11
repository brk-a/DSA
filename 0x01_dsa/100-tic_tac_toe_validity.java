import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Tic-Tac-Toe board validity checker.
 *
 * Rules assumed:
 * - X always moves first.
 * - Players alternate turns.
 * - X therefore has either the same number of marks as O,
 *   or exactly one more.
 * - Once either player wins, the game ends immediately.
 * - A board is invalid if both players have winning lines.
 * - A winning board must be reachable by making the final move
 *   of the winning player.
 *
 * Accepted cell values:
 * - 'X' / 'x'
 * - 'O' / 'o'
 * - any other character is invalid.
 */
public class TicTacToeValidity {

    private static final char EMPTY = '.';
    private static final char NO_WINNER = 'N';

    private static final int[][] WIN = {
            {0, 1, 2},
            {3, 4, 5},
            {6, 7, 8},
            {0, 3, 6},
            {1, 4, 7},
            {2, 5, 8},
            {0, 4, 8},
            {2, 4, 6}
    };

    static record Result(boolean valid, char winner) {}

    /* **********************************************************************
     * Validation
     * **********************************************************************/

    static boolean validBoard(char[][] board) {
        if (board == null || board.length != 3) {
            return false;
        }

        for (char[] row : board) {
            if (row == null || row.length != 3) {
                return false;
            }
        }

        return true;
    }

    private static char normalize(char c) {
        if (c == 'x') {
            return 'X';
        }
        if (c == 'o') {
            return 'O';
        }
        return c;
    }

    private static boolean validCell(char c) {
        return c == 'X'
                || c == 'x'
                || c == 'O'
                || c == 'o'
                || c == EMPTY;
    }

    /* **********************************************************************
     * Core Algorithm
     * **********************************************************************/

    static Result ticTacToeValidity(char[][] board) {

        if (!validBoard(board)) {
            return new Result(false, NO_WINNER);
        }

        int xCount = 0;
        int oCount = 0;

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                char cell = board[i][j];

                if (!validCell(cell)) {
                    return new Result(false, NO_WINNER);
                }

                char normalized = normalize(cell);

                if (normalized == 'X') {
                    xCount++;
                } else if (normalized == 'O') {
                    oCount++;
                }
            }
        }

        /*
         * X always starts, so:
         *
         *     xCount == oCount
         * or
         *     xCount == oCount + 1
         */
        if (xCount != oCount && xCount != oCount + 1) {
            return new Result(false, NO_WINNER);
        }

        boolean xWins = isCWin(board, 'X') > 0;
        boolean oWins = isCWin(board, 'O') > 0;

        // Both players cannot have won.
        if (xWins && oWins) {
            return new Result(false, NO_WINNER);
        }

        /*
         * If X wins, X must have just made the last move.
         * Therefore X must have exactly one more mark than O.
         *
         * Additionally, remove one X from the board. At least one
         * such removal must make the board non-winning, proving that
         * the winning board could have been produced by X's final move.
         */
        if (xWins) {

            if (xCount != oCount + 1) {
                return new Result(false, NO_WINNER);
            }

            if (!reachableAfterFinalMove(board, 'X')) {
                return new Result(false, NO_WINNER);
            }

            return new Result(true, 'X');
        }

        /*
         * If O wins, O must have just made the last move.
         * Therefore X and O must have equal numbers of marks.
         */
        if (oWins) {

            if (xCount != oCount) {
                return new Result(false, NO_WINNER);
            }

            if (!reachableAfterFinalMove(board, 'O')) {
                return new Result(false, NO_WINNER);
            }

            return new Result(true, 'O');
        }

        return new Result(true, NO_WINNER);
    }

    /**
     * Returns the number of winning lines for player c.
     */
    static int isCWin(char[][] board, char c) {

        if (!validBoard(board)) {
            return 0;
        }

        char target = normalize(c);
        int count = 0;

        for (int[] line : WIN) {

            int a = line[0];
            int b = line[1];
            int d = line[2];

            int r1 = a / 3;
            int c1 = a % 3;

            int r2 = b / 3;
            int c2 = b % 3;

            int r3 = d / 3;
            int c3 = d % 3;

            if (normalize(board[r1][c1]) == target
                    && normalize(board[r2][c2]) == target
                    && normalize(board[r3][c3]) == target) {
                count++;
            }
        }

        return count;
    }

    /**
     * Checks whether board could have been produced by player c's
     * final move.
     *
     * We temporarily remove each mark belonging to c. If doing so
     * leaves no winning line for either player, then that mark could
     * have been the final move.
     */
    private static boolean reachableAfterFinalMove(char[][] board, char c) {

        char target = normalize(c);

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                if (normalize(board[i][j]) != target) {
                    continue;
                }

                char original = board[i][j];
                board[i][j] = EMPTY;

                boolean xStillWins = isCWin(board, 'X') > 0;
                boolean oStillWins = isCWin(board, 'O') > 0;

                board[i][j] = original;

                if (!xStillWins && !oStillWins) {
                    return true;
                }
            }
        }

        return false;
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
        Result solve(char[][] board);
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

    static char[][] board(String... rows) {

        char[][] board = new char[rows.length][];

        for (int i = 0; i < rows.length; i++) {
            board[i] = rows[i].toCharArray();
        }

        return board;
    }

    static String formatBoard(char[][] board) {

        if (board == null) {
            return "null";
        }

        StringBuilder sb = new StringBuilder("[\n");

        for (int i = 0; i < board.length; i++) {

            sb.append("  ");

            if (board[i] == null) {
                sb.append("null");
            } else {
                sb.append(Arrays.toString(board[i]));
            }

            if (i < board.length - 1) {
                sb.append(",");
            }

            sb.append("\n");
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

        return a.valid() == b.valid()
                && a.winner() == b.winner();
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

                Result actual = method.solve(test.input);

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
                            formatBoard(test.input));

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
                        formatBoard(test.input));

                System.out.printf(
                        "  exception = %s%n",
                        ex);
            }
        }

        System.out.println();

        System.out.printf(
                "Results: %d passed, %d failed, %d total%n%n",
                passed,
                failed,
                tests.size());
    }

    /* **********************************************************************
     * Exhaustive Independent Reference Generator
     * **********************************************************************/

    /**
     * Generates every board reachable through a legal game.
     *
     * This is used as an independent reference for randomized testing.
     */
    static Set<String> generateReachableBoards() {

        Set<String> reachable = new HashSet<>();

        char[][] board = board(
                "...",
                "...",
                "...");

        generate(board, 'X', reachable);

        return reachable;
    }

    private static void generate(
            char[][] board,
            char player,
            Set<String> reachable) {

        String key = encode(board);

        if (!reachable.add(key)) {
            return;
        }

        // A game stops immediately after a win.
        if (isCWin(board, 'X') > 0
                || isCWin(board, 'O') > 0) {
            return;
        }

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {

                if (board[i][j] != EMPTY) {
                    continue;
                }

                board[i][j] = player;

                generate(
                        board,
                        player == 'X' ? 'O' : 'X',
                        reachable);

                board[i][j] = EMPTY;
            }
        }
    }

    static String encode(char[][] board) {

        StringBuilder sb = new StringBuilder(9);

        for (char[] row : board) {
            for (char c : row) {
                sb.append(normalize(c));
            }
        }

        return sb.toString();
    }

    static char[][] decode(String encoded) {

        char[][] board = new char[3][3];

        for (int i = 0; i < 9; i++) {
            board[i / 3][i % 3] = encoded.charAt(i);
        }

        return board;
    }

    /* **********************************************************************
     * Randomised Testing
     * **********************************************************************/

    static char[][] randomBoard(Random rng) {

        char[][] board = new char[3][3];

        char[] values = {
                EMPTY,
                'X',
                'O'
        };

        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] =
                        values[rng.nextInt(values.length)];
            }
        }

        return board;
    }

    static void runRandomisedTests(int iterations) {

        System.out.println(
                "======================================================");

        System.out.println(
                "Randomised Cross Checks Against Exhaustive Game States");

        System.out.println(
                "======================================================");

        Set<String> reachable = generateReachableBoards();

        Random rng = new Random(20260909L);

        for (int i = 1; i <= iterations; i++) {

            char[][] board = randomBoard(rng);

            Result actual =
                    ticTacToeValidity(board);

            boolean expectedValid =
                    reachable.contains(encode(board));

            boolean actualValid =
                    actual != null && actual.valid();

            if (expectedValid != actualValid) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "board = " + formatBoard(board));

                System.out.println(
                        "expected valid = " + expectedValid);

                System.out.println(
                        "actual = " + actual);

                return;
            }
        }

        System.out.printf(
                "All %d randomised tests passed.%n%n",
                iterations);

        System.out.printf(
                "Independent reachable states generated: %d%n%n",
                reachable.size());
    }

    /* **********************************************************************
     * Main Test Suite
     * **********************************************************************/

    public static void main(String[] args) {

        List<TestCase> tests = new ArrayList<>();

        /*
         * ============================================================
         * Valid Boards
         * ============================================================
         */

        tests.add(new TestCase(
                "V1",
                board(
                        "...",
                        "...",
                        "..."),
                new Result(true, 'N'),
                "empty board"));

        tests.add(new TestCase(
                "V2",
                board(
                        "X..",
                        "...",
                        "..."),
                new Result(true, 'N'),
                "X makes first move"));

        tests.add(new TestCase(
                "V3",
                board(
                        "XO.",
                        "...",
                        "..."),
                new Result(true, 'N'),
                "X and O have each made one move"));

        tests.add(new TestCase(
                "V4",
                board(
                        "XXX",
                        "OO.",
                        "..."),
                new Result(true, 'X'),
                "X wins on the final move"));

        tests.add(new TestCase(
                "V5",
                board(
                        "XXO",
                        "OOX",
                        "X.."),
                new Result(true, 'X'),
                "X wins vertically"));

        tests.add(new TestCase(
                "V6",
                board(
                        "XXO",
                        "XOO",
                        "X.."),
                new Result(true, 'X'),
                "X wins vertically with mixed board"));

        tests.add(new TestCase(
                "V7",
                board(
                        "X.O",
                        ".XO",
                        "..X"),
                new Result(true, 'X'),
                "X wins diagonally"));

        tests.add(new TestCase(
                "V8",
                board(
                        "XOX",
                        "OOX",
                        "XXO"),
                new Result(true, 'N'),
                "valid full draw"));

        tests.add(new TestCase(
                "V9",
                board(
                        "xxx",
                        "oo.",
                        "..."),
                new Result(true, 'X'),
                "lowercase X and O are accepted"));

        /*
         * ============================================================
         * Invalid Count Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "I1",
                board(
                        "OO.",
                        "...",
                        "..."),
                new Result(false, 'N'),
                "O cannot move before X"));

        tests.add(new TestCase(
                "I2",
                board(
                        "XXX",
                        "...",
                        "..."),
                new Result(false, 'N'),
                "X has too many moves"));

        tests.add(new TestCase(
                "I3",
                board(
                        "XXO",
                        "OOO",
                        "XX."),
                new Result(false, 'N'),
                "O wins but move counts are impossible"));

        /*
         * ============================================================
         * Invalid Winner Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "I4",
                board(
                        "XXX",
                        "OOO",
                        "..."),
                new Result(false, 'N'),
                "both players have winning lines"));

        tests.add(new TestCase(
                "I5",
                board(
                        "XXX",
                        "OO.",
                        "O.."),
                new Result(false, 'N'),
                "X wins but X/O counts do not permit X's final move"));

        /*
         * X has a winning line, but O also has a completed line
         * that could only have existed before X's final move.
         */
        tests.add(new TestCase(
                "I6",
                board(
                        "XXX",
                        "OO.",
                        "OO."),
                new Result(false, 'N'),
                "board cannot continue after O has already won"));

        /*
         * ============================================================
         * Board Validation
         * ============================================================
         */

        tests.add(new TestCase(
                "E1",
                null,
                new Result(false, 'N'),
                "null board"));

        tests.add(new TestCase(
                "E2",
                new char[][]{},
                new Result(false, 'N'),
                "zero rows"));

        tests.add(new TestCase(
                "E3",
                new char[][]{
                        {'X', 'O', '.'},
                        {'X', 'O', '.'}
                },
                new Result(false, 'N'),
                "not a 3x3 board"));

        tests.add(new TestCase(
                "E4",
                new char[][]{
                        {'X', 'O', '.'},
                        {'X', 'O', '.'},
                        null
                },
                new Result(false, 'N'),
                "null row"));

        tests.add(new TestCase(
                "E5",
                new char[][]{
                        {'X', 'O', '.'},
                        {'X', 'O'},
                        {'.', '.', '.'}
                },
                new Result(false, 'N'),
                "ragged board"));

        tests.add(new TestCase(
                "E6",
                board(
                        "XA.",
                        "...",
                        "..."),
                new Result(false, 'N'),
                "invalid cell character"));

        /*
         * ============================================================
         * Direct Winning-Line Tests
         * ============================================================
         */

        tests.add(new TestCase(
                "W1",
                board(
                        "XXX",
                        "...",
                        "..."),
                new Result(false, 'N'),
                "horizontal winning line"));

        /*
         * ============================================================
         * Test Execution
         * ============================================================
         */

        System.out.println(
                "############################################################");

        System.out.println(
                "################  TIC-TAC-TOE VALIDITY  ###################");

        System.out.println(
                "############################################################");

        System.out.println();

        List<MethodCase> methods = List.of(
                new MethodCase(
                        "Tic-Tac-Toe Validity",
                        TicTacToeValidity::ticTacToeValidity)
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
