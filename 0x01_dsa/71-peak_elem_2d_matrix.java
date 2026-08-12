import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;


/**
 * Finds a peak element in a rectangular two-dimensional matrix.
 *
 * A peak is an item that is greater than or equal to all of its existing
 * neighbours: above, below, left and right.
 *
 * For an invalid matrix:
 * - null matrix -> returns null
 * - empty matrix -> returns null
 * - matrix containing a null row -> returns null
 * - jagged matrix -> returns null
 * - matrix containing an empty row -> returns null
 *
 * For a valid matrix, each method returns:
 * - [row, column] when a peak is found
 * - [-1, -1] only as a defensive fallback
 */
public class PeakElement2dMatrix {


    /**
     * Validates that the matrix is non-null, non-empty and rectangular.
     */
    private static boolean validMatrix(int[][] matrix) {


        if (matrix == null || matrix.length == 0) {
            return false;
        }


        if (matrix[0] == null || matrix[0].length == 0) {
            return false;
        }


        int columnCount = matrix[0].length;


        for (int[] row : matrix) {


            if (row == null || row.length != columnCount) {
                return false;
            }
        }


        return true;
    }


    /**
     * Exhaustive search.
     *
     * Time complexity: O(m * n)
     * Space complexity: O(1), excluding the returned list
     *
     * The original method name is retained for compatibility. This is not
     * technically a two-pointer algorithm; it is a complete matrix scan.
     */
    public static ArrayList<Integer> peakElement2dMatrixTwoPointers(
            int[][] matrix) {


        if (!validMatrix(matrix)) {
            return null;
        }


        int rows = matrix.length;
        int columns = matrix[0].length;


        for (int row = 0; row < rows; row++) {


            for (int column = 0; column < columns; column++) {


                if (isPeak(matrix, row, column)) {
                    return coordinate(row, column);
                }
            }
        }


        return coordinate(-1, -1);
    }


    /**
     * Binary search on columns.
     *
     * Time complexity: O(m * log n)
     * Space complexity: O(1), excluding the returned list
     */
    public static ArrayList<Integer> peakElement2dMatrixColumnSearch(
            int[][] matrix) {


        if (!validMatrix(matrix)) {
            return null;
        }


        int rows = matrix.length;
        int columns = matrix[0].length;


        int low = 0;
        int high = columns - 1;


        while (low <= high) {


            int middle = low + (high - low) / 2;
            int maximumRow = findMaximumRow(matrix, middle);
            int current = matrix[maximumRow][middle];


            boolean hasGreaterLeftNeighbour =
                    middle > 0
                            && matrix[maximumRow][middle - 1] > current;


            boolean hasGreaterRightNeighbour =
                    middle + 1 < columns
                            && matrix[maximumRow][middle + 1] > current;


            if (!hasGreaterLeftNeighbour && !hasGreaterRightNeighbour) {


                /*
                 * The item is already the maximum in its column, so it is
                 * also greater than or equal to its vertical neighbours.
                 */
                return coordinate(maximumRow, middle);
            }


            if (hasGreaterRightNeighbour) {
                low = middle + 1;
            } else {
                high = middle - 1;
            }
        }


        /*
         * A finite, non-empty matrix always contains a non-strict peak.
         * This return is retained as a defensive fallback.
         */
        return coordinate(-1, -1);
    }


    /**
     * Returns the row containing the largest item in the specified column.
     */
    private static int findMaximumRow(int[][] matrix, int column) {


        int maximumRow = 0;


        for (int row = 1; row < matrix.length; row++) {


            if (matrix[row][column] > matrix[maximumRow][column]) {
                maximumRow = row;
            }
        }


        return maximumRow;
    }


    /**
     * Checks whether matrix[row][column] is a peak.
     */
    private static boolean isPeak(int[][] matrix, int row, int column) {


        int rows = matrix.length;
        int columns = matrix[0].length;
        int current = matrix[row][column];


        if (row > 0 && matrix[row - 1][column] > current) {
            return false;
        }


        if (row + 1 < rows && matrix[row + 1][column] > current) {
            return false;
        }


        if (column > 0 && matrix[row][column - 1] > current) {
            return false;
        }


        if (column + 1 < columns && matrix[row][column + 1] > current) {
            return false;
        }


        return true;
    }


    /**
     * Creates a coordinate result in the format [row, column].
     */
    private static ArrayList<Integer> coordinate(int row, int column) {


        ArrayList<Integer> result = new ArrayList<>();
        result.add(row);
        result.add(column);
        return result;
    }


    /**
     * Test case used by the test harness.
     */
    static class TestCase {


        final String id;
        final int[][] matrix;
        final ArrayList<Integer> expected;
        final boolean expectNull;
        final String description;


        TestCase(
                String id,
                int[][] matrix,
                ArrayList<Integer> expected,
                boolean expectNull,
                String description) {


            this.id = id;
            this.matrix = matrix;
            this.expected = expected;
            this.expectNull = expectNull;
            this.description = description;
        }
    }


    /**
     * Runs the supplied method against all test cases.
     */
    static void runTests(
            String methodName,
            Function<int[][], ArrayList<Integer>> function,
            List<TestCase> tests) {


        System.out.println(
                "========================= method: "
                        + methodName
                        + " =========================");


        int passed = 0;
        int failed = 0;


        for (TestCase test : tests) {


            int[][] original = deepCopy(test.matrix);


            try {


                ArrayList<Integer> actual = function.apply(test.matrix);


                boolean correct;


                if (test.expectNull) {
                    correct = actual == null;
                } else if (actual == null) {
                    correct = false;
                } else if (test.expected != null) {
                    correct = actual.equals(test.expected);
                } else {
                    correct = isValidPeakResult(test.matrix, actual);
                }


                boolean inputUnchanged =
                        Arrays.deepEquals(test.matrix, original);


                correct = correct && inputUnchanged;


                if (correct) {


                    passed++;


                    System.out.printf(
                            " Test %s (%s): passed%n",
                            test.id,
                            test.description);


                } else {


                    failed++;


                    System.out.printf(
                            " Test %s (%s): FAILED%n",
                            test.id,
                            test.description);


                    System.out.printf(
                            " got=%s%n",
                            actual);


                    if (!inputUnchanged) {
                        System.out.println(" input matrix was modified");
                    }
                }


            } catch (Exception exception) {


                failed++;


                System.out.printf(
                        " Test %s (%s): FAILED%n",
                        test.id,
                        test.description);


                System.out.println(exception);
            }
        }


        System.out.printf(
                "Results: %d passed, %d failed%n%n",
                passed,
                failed);
    }


    /**
     * Checks that a result contains valid coordinates for a peak.
     */
    private static boolean isValidPeakResult(
            int[][] matrix,
            ArrayList<Integer> result) {


        if (result.size() != 2) {
            return false;
        }


        int row = result.get(0);
        int column = result.get(1);


        if (row < 0 || column < 0) {
            return false;
        }


        if (row >= matrix.length || column >= matrix[0].length) {
            return false;
        }


        return isPeak(matrix, row, column);
    }


    /**
     * Makes a deep copy of a two-dimensional array.
     */
    private static int[][] deepCopy(int[][] matrix) {


        if (matrix == null) {
            return null;
        }


        int[][] copy = new int[matrix.length][];


        for (int row = 0; row < matrix.length; row++) {


            if (matrix[row] != null) {
                copy[row] = matrix[row].clone();
            }
        }


        return copy;
    }


    /**
     * Creates a coordinate list for test expectations.
     */
    private static ArrayList<Integer> expected(int row, int column) {
        return coordinate(row, column);
    }


    public static void main(String[] args) {


        List<TestCase> tests = new ArrayList<>();


        tests.add(new TestCase(
                "P1",
                new int[][]{
                        {10, 8, 10, 10},
                        {14, 13, 12, 11},
                        {15, 9, 8, 7}
                },
                expected(2, 0),
                false,
                "Known peak at the bottom-left"));


        tests.add(new TestCase(
                "P2",
                new int[][]{
                        {1, 2, 3},
                        {4, 5, 6},
                        {7, 8, 9}
                },
                expected(2, 2),
                false,
                "Peak at the bottom-right"));


        tests.add(new TestCase(
                "P3",
                new int[][]{
                        {9}
                },
                expected(0, 0),
                false,
                "Single-item matrix"));


        tests.add(new TestCase(
                "P4",
                new int[][]{
                        {1, 2, 1, 0, 1}
                },
                null,
                false,
                "Single-row matrix"));


        tests.add(new TestCase(
                "P5",
                new int[][]{
                        {1},
                        {3},
                        {2},
                        {1}
                },
                null,
                false,
                "Single-column matrix"));


        tests.add(new TestCase(
                "P6",
                new int[][]{
                        {5, 5},
                        {5, 5}
                },
                null,
                false,
                "Matrix containing equal values"));


        tests.add(new TestCase(
                "N1",
                null,
                null,
                true,
                "Null matrix"));


        tests.add(new TestCase(
                "N2",
                new int[0][0],
                null,
                true,
                "Empty matrix"));


        tests.add(new TestCase(
                "N3",
                new int[][]{
                        {}
                },
                null,
                true,
                "Matrix containing an empty row"));


        tests.add(new TestCase(
                "N4",
                new int[][]{
                        {1, 2},
                        null
                },
                null,
                true,
                "Matrix containing a null row"));


        tests.add(new TestCase(
                "N5",
                new int[][]{
                        {1, 2},
                        {3}
                },
                null,
                true,
                "Jagged matrix"));


        runTests(
                "Exhaustive Matrix Scan",
                PeakElement2dMatrix::peakElement2dMatrixTwoPointers,
                tests);


        runTests(
                "Column Binary Search",
                PeakElement2dMatrix::peakElement2dMatrixColumnSearch,
                tests);
    }
}
