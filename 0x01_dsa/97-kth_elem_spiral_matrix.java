import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Kth Element From Spiral Matrix.
 *
 * Problem:
 * Given an m x n matrix and a 1-based position k, return the kth element
 * when the matrix is traversed in clockwise spiral order.
 *
 * Example:
 *
 * Matrix:
 *
 *   1  2  3
 *   4  5  6
 *   7  8  9
 *
 * Spiral order:
 *
 *   1, 2, 3, 6, 9, 8, 7, 4, 5
 *
 * Therefore:
 *
 *   k = 1 -> 1
 *   k = 5 -> 9
 *   k = 9 -> 5
 *
 * k is 1-based.
 *
 * Result:
 *
 *   valid == false
 *       means the input matrix or k is invalid.
 *
 *   valid == true
 *       means element is the requested kth element.
 *
 * A valid matrix is rectangular, non-empty, and contains no null rows.
 */
public class KthElementFromSpiralMatrix {

    static record Result(int element, boolean valid) {}

    /* **********************************************************************
     * Validation
     * **********************************************************************/

    /**
     * Returns true only for a non-null, non-empty rectangular matrix.
     */
    static boolean validMatrix(int[][] matrix) {

        if (matrix == null || matrix.length == 0) {
            return false;
        }

        if (matrix[0] == null || matrix[0].length == 0) {
            return false;
        }

        int columns = matrix[0].length;

        for (int row = 0; row < matrix.length; row++) {

            if (matrix[row] == null
                    || matrix[row].length != columns) {
                return false;
            }
        }

        return true;
    }

    /**
     * Validates both matrix and 1-based k.
     */
    static boolean validInput(int[][] matrix, int k) {

        if (!validMatrix(matrix)) {
            return false;
        }

        long size = (long) matrix.length * matrix[0].length;

        return k >= 1 && k <= size;
    }

    /* **********************************************************************
     * 1. Spiral Simulation
     * **********************************************************************/

    /**
     * Finds the kth element by directly simulating the spiral traversal.
     *
     * Time:
     *   O(k), worst case O(m * n)
     *
     * Space:
     *   O(1)
     *
     * k is 1-based.
     */
    static Result kthElementFromSpiralMatrixSimulation(
            int[][] matrix,
            int k) {

        if (!validInput(matrix, k)) {
            return new Result(-1, false);
        }

        int top = 0;
        int bottom = matrix.length - 1;
        int left = 0;
        int right = matrix[0].length - 1;

        int count = 0;

        while (top <= bottom && left <= right) {

            /*
             * Top row: left -> right
             */
            for (int col = left; col <= right; col++) {

                count++;

                if (count == k) {
                    return new Result(matrix[top][col], true);
                }
            }

            top++;

            /*
             * Right column: top -> bottom
             *
             * Only traverse it if there is still a row remaining.
             */
            for (int row = top; row <= bottom; row++) {

                count++;

                if (count == k) {
                    return new Result(matrix[row][right], true);
                }
            }

            right--;

            /*
             * Bottom row: right -> left
             *
             * Only traverse if top and bottom still describe a row.
             */
            if (top <= bottom) {

                for (int col = right; col >= left; col--) {

                    count++;

                    if (count == k) {
                        return new Result(matrix[bottom][col], true);
                    }
                }

                bottom--;
            }

            /*
             * Left column: bottom -> top
             *
             * Only traverse if left and right still describe a column.
             */
            if (left <= right) {

                for (int row = bottom; row >= top; row--) {

                    count++;

                    if (count == k) {
                        return new Result(matrix[row][left], true);
                    }
                }

                left++;
            }
        }

        /*
         * validInput guarantees that this should never be reached.
         */
        return new Result(-1, false);
    }

    /* **********************************************************************
     * 2. Layerwise Traversal
     * **********************************************************************/

    /**
     * Finds the kth element by determining which spiral layer contains k
     * and then calculating its exact coordinates.
     *
     * Time:
     *   O(min(m, n))
     *
     * Space:
     *   O(1)
     *
     * k is 1-based.
     */
    static Result kthElementFromSpiralMatrixLayerwiseTraversal(
            int[][] matrix,
            int k) {

        if (!validInput(matrix, k)) {
            return new Result(-1, false);
        }

        int top = 0;
        int bottom = matrix.length - 1;
        int left = 0;
        int right = matrix[0].length - 1;

        int remaining = k;

        while (top <= bottom && left <= right) {

            int rows = bottom - top + 1;
            int columns = right - left + 1;

            int perimeter = perimeterSize(rows, columns);

            if (remaining <= perimeter) {

                int[] position = positionWithinLayer(
                        top,
                        bottom,
                        left,
                        right,
                        remaining);

                return new Result(
                        matrix[position[0]][position[1]],
                        true);
            }

            remaining -= perimeter;

            top++;
            bottom--;
            left++;
            right--;
        }

        /*
         * validInput guarantees that this should never be reached.
         */
        return new Result(-1, false);
    }

    /**
     * Returns the number of cells in the clockwise perimeter of a
     * rows x columns submatrix.
     *
     * Examples:
     *
     *   1 x n -> n
     *   m x 1 -> m
     *   m x n -> 2m + 2n - 4
     */
    static int perimeterSize(int rows, int columns) {

        if (rows == 1) {
            return columns;
        }

        if (columns == 1) {
            return rows;
        }

        return 2 * rows + 2 * columns - 4;
    }

    /**
     * Returns {row, column} of the position represented by a 1-based
     * offset within the current spiral layer.
     *
     * The layer is traversed:
     *
     *   top-left -> top-right
     *   top-right -> bottom-right
     *   bottom-right -> bottom-left
     *   bottom-left -> top-left
     *
     * The caller guarantees:
     *
     *   1 <= offset <= perimeterSize(...)
     */
    static int[] positionWithinLayer(
            int top,
            int bottom,
            int left,
            int right,
            int offset) {

        int rows = bottom - top + 1;
        int columns = right - left + 1;

        /*
         * Single row.
         */
        if (rows == 1) {
            return new int[]{
                    top,
                    left + offset - 1
            };
        }

        /*
         * Single column.
         */
        if (columns == 1) {
            return new int[]{
                    top + offset - 1,
                    left
            };
        }

        /*
         * Top edge.
         */
        if (offset <= columns) {

            return new int[]{
                    top,
                    left + offset - 1
            };
        }

        offset -= columns;

        /*
         * Right edge.
         *
         * The top-right corner was already consumed by the top edge,
         * so this edge contains rows - 1 cells.
         */
        if (offset <= rows - 1) {

            return new int[]{
                    top + offset,
                    right
            };
        }

        offset -= rows - 1;

        /*
         * Bottom edge.
         *
         * The bottom-right corner was already consumed by the right edge,
         * so this edge contains columns - 1 cells.
         */
        if (offset <= columns - 1) {

            return new int[]{
                    bottom,
                    right - offset
            };
        }

        offset -= columns - 1;

        /*
         * Left edge.
         *
         * Both corners were already consumed.
         */
        return new int[]{
                bottom - offset,
                left
        };
    }

    /* **********************************************************************
     * Independent Reference Implementation
     * **********************************************************************/

    /**
     * Independent oracle used only by the test suite.
     *
     * Unlike the production implementations, this method first constructs
     * the complete spiral traversal.
     *
     * Time:
     *   O(m * n)
     *
     * Space:
     *   O(m * n)
     */
    static Result referenceSpiral(
            int[][] matrix,
            int k) {

        if (!validInput(matrix, k)) {
            return new Result(-1, false);
        }

        int total = matrix.length * matrix[0].length;

        int[] spiral = new int[total];

        int top = 0;
        int bottom = matrix.length - 1;
        int left = 0;
        int right = matrix[0].length - 1;

        int index = 0;

        while (top <= bottom && left <= right) {

            for (int col = left; col <= right; col++) {
                spiral[index++] = matrix[top][col];
            }

            top++;

            for (int row = top; row <= bottom; row++) {
                spiral[index++] = matrix[row][right];
            }

            right--;

            if (top <= bottom) {

                for (int col = right; col >= left; col--) {
                    spiral[index++] = matrix[bottom][col];
                }

                bottom--;
            }

            if (left <= right) {

                for (int row = bottom; row >= top; row--) {
                    spiral[index++] = matrix[row][left];
                }

                left++;
            }
        }

        return new Result(spiral[k - 1], true);
    }

    /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    static class TestCase {

        final String id;
        final int[][] input;
        final int k;
        final Result expected;
        final String description;

        TestCase(
                String id,
                int[][] input,
                int k,
                Result expected,
                String description) {

            this.id = id;
            this.input = input;
            this.k = k;
            this.expected = expected;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {

        Result solve(int[][] matrix, int k);
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

        for (int row = 0; row < matrix.length; row++) {

            if (row > 0) {
                sb.append(", ");
            }

            if (matrix[row] == null) {
                sb.append("null");
            } else {
                sb.append(Arrays.toString(matrix[row]));
            }
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

        return a.element() == b.element()
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
                        method.solve(test.input, test.k);

                if (resultsEqual(actual, test.expected)) {

                    passed++;

                    System.out.printf(
                            "✓ %s (%s)%n",
                            test.id,
                            test.description);

                } else {

                    failed++;

                    printFailure(
                            test,
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
                        "  k         = %d%n",
                        test.k);

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

    static void printFailure(
            TestCase test,
            Result actual) {

        System.out.printf(
                "✗ %s (%s)%n",
                test.id,
                test.description);

        System.out.printf(
                "  input     = %s%n",
                formatMatrix(test.input));

        System.out.printf(
                "  k         = %d%n",
                test.k);

        System.out.printf(
                "  expected  = %s%n",
                test.expected);

        System.out.printf(
                "  actual    = %s%n",
                actual);
    }

    /* **********************************************************************
     * Fixed Tests
     * **********************************************************************/

    static List<TestCase> buildTests() {

        List<TestCase> tests = new ArrayList<>();

        /*
         * ------------------------------------------------------------
         * 1x1
         * ------------------------------------------------------------
         */

        tests.add(new TestCase(
                "B1",
                new int[][]{{42}},
                1,
                new Result(42, true),
                "1x1 matrix, only element"));

        tests.add(new TestCase(
                "B2",
                new int[][]{{-1}},
                1,
                new Result(-1, true),
                "valid result may itself be -1"));

        /*
         * ------------------------------------------------------------
         * Single Row
         * ------------------------------------------------------------
         */

        tests.add(new TestCase(
                "R1",
                new int[][]{{1, 2, 3, 4, 5}},
                1,
                new Result(1, true),
                "single row, first element"));

        tests.add(new TestCase(
                "R2",
                new int[][]{{1, 2, 3, 4, 5}},
                3,
                new Result(3, true),
                "single row, middle element"));

        tests.add(new TestCase(
                "R3",
                new int[][]{{1, 2, 3, 4, 5}},
                5,
                new Result(5, true),
                "single row, last element"));

        /*
         * ------------------------------------------------------------
         * Single Column
         * ------------------------------------------------------------
         */

        tests.add(new TestCase(
                "C1",
                new int[][]{
                        {1},
                        {2},
                        {3},
                        {4},
                        {5}
                },
                1,
                new Result(1, true),
                "single column, first element"));

        tests.add(new TestCase(
                "C2",
                new int[][]{
                        {1},
                        {2},
                        {3},
                        {4},
                        {5}
                },
                3,
                new Result(3, true),
                "single column, middle element"));

        tests.add(new TestCase(
                "C3",
                new int[][]{
                        {1},
                        {2},
                        {3},
                        {4},
                        {5}
                },
                5,
                new Result(5, true),
                "single column, last element"));

        /*
         * ------------------------------------------------------------
         * 2x2
         * ------------------------------------------------------------
         */

        tests.add(new TestCase(
                "S1",
                new int[][]{
                        {1, 2},
                        {3, 4}
                },
                1,
                new Result(1, true),
                "2x2 first"));

        tests.add(new TestCase(
                "S2",
                new int[][]{
                        {1, 2},
                        {3, 4}
                },
                2,
                new Result(2, true),
                "2x2 top-right"));

        tests.add(new TestCase(
                "S3",
                new int[][]{
                        {1, 2},
                        {3, 4}
                },
                3,
                new Result(4, true),
                "2x2 bottom-right"));

        tests.add(new TestCase(
                "S4",
                new int[][]{
                        {1, 2},
                        {3, 4}
                },
                4,
                new Result(3, true),
                "2x2 bottom-left"));

        /*
         * ------------------------------------------------------------
         * 3x3
         * ------------------------------------------------------------
         */

        tests.add(new TestCase(
                "M1",
                new int[][]{
                        {1, 2, 3},
                        {4, 5, 6},
                        {7, 8, 9}
                },
                1,
                new Result(1, true),
                "3x3 first"));

        tests.add(new TestCase(
                "M2",
                new int[][]{
                        {1, 2, 3},
                        {4, 5, 6},
                        {7, 8, 9}
                },
                3,
                new Result(3, true),
                "3x3 top-right"));

        tests.add(new TestCase(
                "M3",
                new int[][]{
                        {1, 2, 3},
                        {4, 5, 6},
                        {7, 8, 9}
                },
                5,
                new Result(9, true),
                "3x3 fifth element"));

        tests.add(new TestCase(
                "M4",
                new int[][]{
                        {1, 2, 3},
                        {4, 5, 6},
                        {7, 8, 9}
                },
                7,
                new Result(7, true),
                "3x3 seventh element"));

        tests.add(new TestCase(
                "M5",
                new int[][]{
                        {1, 2, 3},
                        {4, 5, 6},
                        {7, 8, 9}
                },
                8,
                new Result(8, true),
                "3x3 eighth element"));

        tests.add(new TestCase(
                "M6",
                new int[][]{
                        {1, 2, 3},
                        {4, 5, 6},
                        {7, 8, 9}
                },
                9,
                new Result(5, true),
                "3x3 center is final element"));

        /*
         * ------------------------------------------------------------
         * 3x4
         *
         * Spiral:
         * 1  2  3  4
         * 5  6  7  8
         * 9 10 11 12
         *
         * -> 1,2,3,4,8,12,11,10,9,5,6,7
         * ------------------------------------------------------------
         */

        int[][] threeByFour = {
                {1, 2, 3, 4},
                {5, 6, 7, 8},
                {9, 10, 11, 12}
        };

        tests.add(new TestCase(
                "M7",
                threeByFour,
                4,
                new Result(4, true),
                "3x4 end of top edge"));

        tests.add(new TestCase(
                "M8",
                threeByFour,
                5,
                new Result(8, true),
                "3x4 right edge"));

        tests.add(new TestCase(
                "M9",
                threeByFour,
                8,
                new Result(10, true),
                "3x4 bottom edge"));

        tests.add(new TestCase(
                "M10",
                threeByFour,
                9,
                new Result(9, true),
                "3x4 bottom-left"));

        tests.add(new TestCase(
                "M11",
                threeByFour,
                10,
                new Result(5, true),
                "3x4 left edge"));

        tests.add(new TestCase(
                "M12",
                threeByFour,
                12,
                new Result(7, true),
                "3x4 final inner element"));

        /*
         * ------------------------------------------------------------
         * 4x3
         * ------------------------------------------------------------
         */

        int[][] fourByThree = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9},
                {10, 11, 12}
        };

        tests.add(new TestCase(
                "N1",
                fourByThree,
                1,
                new Result(1, true),
                "4x3 first"));

        tests.add(new TestCase(
                "N2",
                fourByThree,
                3,
                new Result(3, true),
                "4x3 top-right"));

        tests.add(new TestCase(
                "N3",
                fourByThree,
                5,
                new Result(9, true),
                "4x3 right edge"));

        tests.add(new TestCase(
                "N4",
                fourByThree,
                7,
                new Result(11, true),
                "4x3 bottom edge"));

        tests.add(new TestCase(
                "N5",
                fourByThree,
                9,
                new Result(4, true),
                "4x3 left edge"));

        tests.add(new TestCase(
                "N6",
                fourByThree,
                12,
                new Result(8, true),
                "4x3 final inner element"));

        /*
         * ------------------------------------------------------------
         * Values Including Negatives and Zero
         * ------------------------------------------------------------
         */

        tests.add(new TestCase(
                "V1",
                new int[][]{
                        {-1, 0, -2},
                        {-3, -4, -5},
                        {-6, -7, -8}
                },
                1,
                new Result(-1, true),
                "negative first value"));

        tests.add(new TestCase(
                "V2",
                new int[][]{
                        {-1, 0, -2},
                        {-3, -4, -5},
                        {-6, -7, -8}
                },
                5,
                new Result(-5, true),
                "negative interior traversal value"));

        tests.add(new TestCase(
                "V3",
                new int[][]{
                        {-1, 0, -2},
                        {-3, -4, -5},
                        {-6, -7, -8}
                },
                9,
                new Result(-4, true),
                "negative center value"));

        /*
         * ------------------------------------------------------------
         * Invalid Matrix
         * ------------------------------------------------------------
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
                "zero rows"));

        tests.add(new TestCase(
                "E3",
                new int[][]{{}},
                1,
                new Result(-1, false),
                "zero columns"));

        tests.add(new TestCase(
                "E4",
                new int[][]{
                        {},
                        {}
                },
                1,
                new Result(-1, false),
                "multiple zero-length rows"));

        tests.add(new TestCase(
                "E5",
                new int[][]{
                        {1, 2},
                        null
                },
                1,
                new Result(-1, false),
                "null row"));

        tests.add(new TestCase(
                "E6",
                new int[][]{
                        {1, 2},
                        {3}
                },
                1,
                new Result(-1, false),
                "jagged matrix"));

        tests.add(new TestCase(
                "E7",
                new int[][]{
                        {1},
                        {2, 3}
                },
                1,
                new Result(-1, false),
                "another jagged matrix"));

        /*
         * ------------------------------------------------------------
         * Invalid k
         * ------------------------------------------------------------
         */

        int[][] twoByTwo = {
                {1, 2},
                {3, 4}
        };

        tests.add(new TestCase(
                "K1",
                twoByTwo,
                0,
                new Result(-1, false),
                "k is zero"));

        tests.add(new TestCase(
                "K2",
                twoByTwo,
                -1,
                new Result(-1, false),
                "k is negative"));

        tests.add(new TestCase(
                "K3",
                twoByTwo,
                5,
                new Result(-1, false),
                "k exceeds matrix size"));

        tests.add(new TestCase(
                "K4",
                twoByTwo,
                Integer.MAX_VALUE,
                new Result(-1, false),
                "very large invalid k"));

        /*
         * ------------------------------------------------------------
         * Large Rectangular Matrices
         * ------------------------------------------------------------
         */

        int[][] oneByTen = {
                {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}
        };

        tests.add(new TestCase(
                "L1",
                oneByTen,
                10,
                new Result(10, true),
                "1x10 last element"));

        int[][] tenByOne = {
                {1},
                {2},
                {3},
                {4},
                {5},
                {6},
                {7},
                {8},
                {9},
                {10}
        };

        tests.add(new TestCase(
                "L2",
                tenByOne,
                10,
                new Result(10, true),
                "10x1 last element"));

        int[][] twoByFive = {
                {1, 2, 3, 4, 5},
                {6, 7, 8, 9, 10}
        };

        tests.add(new TestCase(
                "L3",
                twoByFive,
                6,
                new Result(10, true),
                "2x5 right-bottom corner"));

        tests.add(new TestCase(
                "L4",
                twoByFive,
                10,
                new Result(7, true),
                "2x5 final inner traversal"));

        return tests;
    }

    /* **********************************************************************
     * Randomised Testing
     * **********************************************************************/

    static int[][] randomMatrix(
            Random rng,
            int maxRows,
            int maxColumns) {

        int rows = rng.nextInt(maxRows) + 1;
        int columns = rng.nextInt(maxColumns) + 1;

        int[][] matrix = new int[rows][columns];

        for (int row = 0; row < rows; row++) {

            for (int col = 0; col < columns; col++) {

                /*
                 * Use a broad value range, including negative numbers and
                 * zero. This catches implementations that incorrectly use
                 * the element value to determine validity.
                 */
                matrix[row][col] =
                        rng.nextInt(2001) - 1000;
            }
        }

        return matrix;
    }

    static void runRandomisedTests(int iterations) {

        System.out.println(
                "======================================================");

        System.out.println(
                "Randomised Cross Checks " +
                "(Simulation vs Layerwise vs Reference)");

        System.out.println(
                "======================================================");

        Random rng = new Random(20260906L);

        for (int iteration = 1;
             iteration <= iterations;
             iteration++) {

            int[][] matrix =
                    randomMatrix(rng, 15, 15);

            int total =
                    matrix.length * matrix[0].length;

            int k =
                    rng.nextInt(total) + 1;

            Result simulation =
                    kthElementFromSpiralMatrixSimulation(
                            matrix,
                            k);

            Result layerwise =
                    kthElementFromSpiralMatrixLayerwiseTraversal(
                            matrix,
                            k);

            Result reference =
                    referenceSpiral(matrix, k);

            if (!resultsEqual(simulation, reference)
                    || !resultsEqual(layerwise, reference)) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "iteration  = " + iteration);

                System.out.println(
                        "matrix     = " + formatMatrix(matrix));

                System.out.println(
                        "k          = " + k);

                System.out.println(
                        "simulation = " + simulation);

                System.out.println(
                        "layerwise  = " + layerwise);

                System.out.println(
                        "reference  = " + reference);

                return;
            }
        }

        System.out.printf(
                "All %d randomised tests passed.%n%n",
                iterations);
    }

    /* **********************************************************************
     * Validation Tests
     * **********************************************************************/

    static void runValidationTests() {

        System.out.println(
                "======================================================");

        System.out.println(
                "Validation Tests");

        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        Object[][] cases = {

                {null, false},

                {new int[][]{}, false},

                {new int[][]{{}}, false},

                {new int[][]{{1}, null}, false},

                {new int[][]{{1, 2}, {3}}, false},

                {new int[][]{{1, 2}, {3, 4}}, true},

                {new int[][]{{1}}, true},

                {new int[][]{{1, 2, 3}}, true}
        };

        for (int i = 0; i < cases.length; i++) {

            int[][] matrix = (int[][]) cases[i][0];
            boolean expected = (boolean) cases[i][1];

            boolean actual = validMatrix(matrix);

            if (actual == expected) {

                passed++;

                System.out.printf(
                        "✓ V%d%n",
                        i + 1);

            } else {

                failed++;

                System.out.printf(
                        "✗ V%d%n",
                        i + 1);

                System.out.printf(
                        "  matrix   = %s%n",
                        formatMatrix(matrix));

                System.out.printf(
                        "  expected = %s%n",
                        expected);

                System.out.printf(
                        "  actual   = %s%n",
                        actual);
            }
        }

        System.out.println();

        System.out.printf(
                "Validation results: %d passed, %d failed, %d total%n",
                passed,
                failed,
                cases.length);

        System.out.println();
    }

    /* **********************************************************************
     * Main
     * **********************************************************************/

    public static void main(String[] args) {

        List<TestCase> tests = buildTests();

        System.out.println(
                "############################################################");

        System.out.println(
                "##########  KTH ELEMENT FROM SPIRAL MATRIX  ###############");

        System.out.println(
                "############################################################");

        System.out.println();

        runValidationTests();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "Spiral Simulation",
                        KthElementFromSpiralMatrix::
                                kthElementFromSpiralMatrixSimulation),

                new MethodCase(
                        "Layerwise Traversal",
                        KthElementFromSpiralMatrix::
                                kthElementFromSpiralMatrixLayerwiseTraversal),

                new MethodCase(
                        "Independent Reference",
                        KthElementFromSpiralMatrix::
                                referenceSpiral)
        );

        for (MethodCase method : methods) {

            runTests(
                    method.name,
                    method.algorithm,
                    tests);
        }

        /*
         * Cross-check the two production implementations against an
         * independent oracle on many randomly generated matrices.
         */
        runRandomisedTests(5000);

        System.out.println(
                "All testing complete.");
    }
}
