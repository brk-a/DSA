import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;


public class SpiralMatrix {

    /* **********************************************************************
     * Validation
     * **********************************************************************/

    /**
     * Returns true only for a non-null, non-empty rectangular matrix.
     */
    private static boolean validMatrix(int[][] matrix) {
        if (matrix == null || matrix.length == 0) {
            return false;
        }

        if (matrix[0] == null || matrix[0].length == 0) {
            return false;
        }

        int columnCount = matrix[0].length;

        for (int row = 1; row < matrix.length; row++) {
            if (matrix[row] == null
                    || matrix[row].length != columnCount) {
                return false;
            }
        }

        return true;
    }

    /* **********************************************************************
     * Implementation 1: Visited Array
     * **********************************************************************/

    /**
     * Traverses the matrix in spiral order by tracking visited cells.
     *
     * Time: O(m * n)
     * Extra space: O(m * n), excluding the output list.
     */
    static ArrayList<Integer> spiralMatrixVisitedArray(
            int[][] matrix) {

        if (!validMatrix(matrix)) {
            return null;
        }

        int rows = matrix.length;
        int columns = matrix[0].length;

        ArrayList<Integer> result =
                new ArrayList<>(rows * columns);

        boolean[][] visited =
                new boolean[rows][columns];

        int[] directionRow =
                {0, 1, 0, -1};

        int[] directionColumn =
                {1, 0, -1, 0};

        int row = 0;
        int column = 0;
        int direction = 0;

        for (int count = 0;
             count < rows * columns;
             count++) {

            result.add(matrix[row][column]);
            visited[row][column] = true;

            int nextRow =
                    row + directionRow[direction];

            int nextColumn =
                    column + directionColumn[direction];

            boolean nextCellIsValid =
                    nextRow >= 0
                            && nextRow < rows
                            && nextColumn >= 0
                            && nextColumn < columns
                            && !visited[nextRow][nextColumn];

            if (nextCellIsValid) {
                row = nextRow;
                column = nextColumn;
            } else {
                direction = (direction + 1) % 4;

                row += directionRow[direction];
                column += directionColumn[direction];
            }
        }

        return result;
    }

    /* **********************************************************************
     * Implementation 2: Boundary Traversal
     * **********************************************************************/

    /**
     * Traverses the matrix in spiral order using four shrinking boundaries.
     *
     * Time: O(m * n)
     * Extra space: O(1), excluding the output list.
     */
    static ArrayList<Integer> spiralMatrixBoundaryTraversal(
            int[][] matrix) {

        if (!validMatrix(matrix)) {
            return null;
        }

        int rows = matrix.length;
        int columns = matrix[0].length;

        ArrayList<Integer> result =
                new ArrayList<>(rows * columns);

        int top = 0;
        int bottom = rows - 1;
        int left = 0;
        int right = columns - 1;

        while (top <= bottom && left <= right) {

            /*
             * Traverse the top row from left to right.
             */
            for (int column = left;
                 column <= right;
                 column++) {

                result.add(matrix[top][column]);
            }

            top++;

            /*
             * Traverse the right column from top to bottom.
             */
            for (int row = top;
                 row <= bottom;
                 row++) {

                result.add(matrix[row][right]);
            }

            right--;

            /*
             * Traverse the bottom row from right to left.
             *
             * The condition prevents duplicating a row when only one
             * row remains.
             */
            if (top <= bottom) {
                for (int column = right;
                     column >= left;
                     column--) {

                    result.add(matrix[bottom][column]);
                }

                bottom--;
            }

            /*
             * Traverse the left column from bottom to top.
             *
             * The condition prevents duplicating a column when only one
             * column remains.
             */
            if (left <= right) {
                for (int row = bottom;
                     row >= top;
                     row--) {

                    result.add(matrix[row][left]);
                }

                left++;
            }
        }

        return result;
    }

    /* **********************************************************************
     * Optional Recursive Variant
     * **********************************************************************/

    /**
     * Recursive boundary-based implementation.
     *
     * Time: O(m * n)
     * Extra space: O(min(m, n)) recursion depth, excluding the result list.
     */
    static ArrayList<Integer> spiralMatrixRecursive(
            int[][] matrix) {

        if (!validMatrix(matrix)) {
            return null;
        }

        int rows = matrix.length;
        int columns = matrix[0].length;

        ArrayList<Integer> result =
                new ArrayList<>(rows * columns);

        spiralRecursive(
                matrix,
                0,
                rows - 1,
                0,
                columns - 1,
                result);

        return result;
    }

    private static void spiralRecursive(
            int[][] matrix,
            int top,
            int bottom,
            int left,
            int right,
            ArrayList<Integer> result) {

        if (top > bottom || left > right) {
            return;
        }

        for (int column = left;
             column <= right;
             column++) {

            result.add(matrix[top][column]);
        }

        top++;

        for (int row = top;
             row <= bottom;
             row++) {

            result.add(matrix[row][right]);
        }

        right--;

        if (top <= bottom) {
            for (int column = right;
                 column >= left;
                 column--) {

                result.add(matrix[bottom][column]);
            }

            bottom--;
        }

        if (left <= right) {
            for (int row = bottom;
                 row >= top;
                 row--) {

                result.add(matrix[row][left]);
            }

            left++;
        }

        spiralRecursive(
                matrix,
                top,
                bottom,
                left,
                right,
                result);
    }

    /* **********************************************************************
     * Utility Methods
     * **********************************************************************/

    private static String matrixToString(int[][] matrix) {
        if (matrix == null) {
            return "null";
        }

        StringBuilder result =
                new StringBuilder("[");

        for (int row = 0;
             row < matrix.length;
             row++) {

            if (row > 0) {
                result.append(", ");
            }

            result.append(
                    matrix[row] == null
                            ? "null"
                            : Arrays.toString(matrix[row]));
        }

        result.append("]");
        return result.toString();
    }

    private static boolean listsEqual(
            List<Integer> first,
            List<Integer> second) {

        return first == null
                ? second == null
                : first.equals(second);
    }

    /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    @FunctionalInterface
    interface SpiralAlgorithm {
        ArrayList<Integer> solve(int[][] matrix);
    }

    static class TestCase {
        final String id;
        final int[][] matrix;
        final ArrayList<Integer> expected;
        final String description;

        TestCase(
                String id,
                int[][] matrix,
                ArrayList<Integer> expected,
                String description) {

            this.id = id;
            this.matrix = matrix;
            this.expected = expected;
            this.description = description;
        }
    }

    static void runTests(
            String algorithmName,
            SpiralAlgorithm algorithm,
            List<TestCase> tests) {

        System.out.println("======================================================");
        System.out.println(algorithmName);
        System.out.println("======================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {
            try {
                ArrayList<Integer> actual =
                        algorithm.solve(test.matrix);

                if (listsEqual(actual, test.expected)) {
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
                            "  matrix   = %s%n",
                            matrixToString(test.matrix));

                    System.out.printf(
                            "  expected = %s%n",
                            test.expected);

                    System.out.printf(
                            "  actual   = %s%n",
                            actual);
                }

            } catch (Exception exception) {
                failed++;

                System.out.printf(
                        "✗ %s (%s)%n",
                        test.id,
                        test.description);

                System.out.printf(
                        "  exception = %s%n",
                        exception);
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
     * Randomised Cross-Checking
     * **********************************************************************/

    private static int[][] randomMatrix(
            Random random,
            int maximumRows,
            int maximumColumns) {

        int rows =
                random.nextInt(maximumRows) + 1;

        int columns =
                random.nextInt(maximumColumns) + 1;

        int[][] matrix =
                new int[rows][columns];

        for (int row = 0; row < rows; row++) {
            for (int column = 0;
                 column < columns;
                 column++) {

                matrix[row][column] =
                        random.nextInt(201) - 100;
            }
        }

        return matrix;
    }

    private static void runRandomisedTests(
            int iterations) {

        System.out.println("======================================================");
        System.out.println("Randomised Cross-Checks");
        System.out.println("======================================================");

        Random random =
                new Random(123456789L);

        for (int iteration = 0;
             iteration < iterations;
             iteration++) {

            int[][] matrix =
                    randomMatrix(random, 10, 10);

            ArrayList<Integer> expected =
                    spiralMatrixBoundaryTraversal(matrix);

            ArrayList<Integer> visitedResult =
                    spiralMatrixVisitedArray(matrix);

            ArrayList<Integer> recursiveResult =
                    spiralMatrixRecursive(matrix);

            if (!expected.equals(visitedResult)) {
                System.out.println(
                        "Visited-array implementation failed.");

                System.out.println(
                        "matrix   = " + matrixToString(matrix));

                System.out.println(
                        "expected = " + expected);

                System.out.println(
                        "actual   = " + visitedResult);

                return;
            }

            if (!expected.equals(recursiveResult)) {
                System.out.println(
                        "Recursive implementation failed.");

                System.out.println(
                        "matrix   = " + matrixToString(matrix));

                System.out.println(
                        "expected = " + expected);

                System.out.println(
                        "actual   = " + recursiveResult);

                return;
            }
        }

        System.out.printf(
                "All %d randomised tests passed.%n%n",
                iterations);
    }

    /* **********************************************************************
     * Main
     * **********************************************************************/

    public static void main(String[] args) {
        List<TestCase> tests = List.of(

                new TestCase(
                        "S1",
                        new int[][]{
                                {1, 2, 3},
                                {4, 5, 6},
                                {7, 8, 9}
                        },
                        new ArrayList<>(
                                List.of(
                                        1, 2, 3,
                                        6, 9, 8,
                                        7, 4, 5)),
                        "Square matrix"),

                new TestCase(
                        "S2",
                        new int[][]{
                                {1, 2, 3, 4},
                                {5, 6, 7, 8},
                                {9, 10, 11, 12}
                        },
                        new ArrayList<>(
                                List.of(
                                        1, 2, 3, 4,
                                        8, 12, 11, 10,
                                        9, 5, 6, 7)),
                        "Rectangular matrix with more columns"),

                new TestCase(
                        "S3",
                        new int[][]{
                                {1, 2},
                                {3, 4},
                                {5, 6},
                                {7, 8}
                        },
                        new ArrayList<>(
                                List.of(
                                        1, 2, 4, 6,
                                        8, 7, 5, 3)),
                        "Rectangular matrix with more rows"),

                new TestCase(
                        "S4",
                        new int[][]{
                                {1, 2, 3, 4, 5}
                        },
                        new ArrayList<>(
                                List.of(1, 2, 3, 4, 5)),
                        "Single row"),

                new TestCase(
                        "S5",
                        new int[][]{
                                {1},
                                {2},
                                {3},
                                {4}
                        },
                        new ArrayList<>(
                                List.of(1, 2, 3, 4)),
                        "Single column"),

                new TestCase(
                        "S6",
                        new int[][]{
                                {42}
                        },
                        new ArrayList<>(
                                List.of(42)),
                        "Single element"),

                new TestCase(
                        "S7",
                        new int[][]{
                                {-1, -2, -3},
                                {-4, -5, -6}
                        },
                        new ArrayList<>(
                                List.of(
                                        -1, -2, -3,
                                        -6, -5, -4)),
                        "Negative values"),

                new TestCase(
                        "E1",
                        null,
                        null,
                        "Null matrix"),

                new TestCase(
                        "E2",
                        new int[][]{},
                        null,
                        "Empty matrix"),

                new TestCase(
                        "E3",
                        new int[][]{
                                {}
                        },
                        null,
                        "Empty row"),

                new TestCase(
                        "E4",
                        new int[][]{
                                {1, 2, 3},
                                {4, 5}
                        },
                        null,
                        "Jagged matrix"),

                new TestCase(
                        "E5",
                        new int[][]{
                                {1, 2},
                                null
                        },
                        null,
                        "Null row")
        );

        System.out.println("############################################################");
        System.out.println("################## SPIRAL MATRIX ############################");
        System.out.println("############################################################");
        System.out.println();

        runTests(
                "Visited Array",
                SpiralMatrix
                        ::spiralMatrixVisitedArray,
                tests);

        runTests(
                "Boundary Traversal",
                SpiralMatrix
                        ::spiralMatrixBoundaryTraversal,
                tests);

        runTests(
                "Recursive Boundary Traversal",
                SpiralMatrix
                        ::spiralMatrixRecursive,
                tests);

        runRandomisedTests(5000);
    }
}
