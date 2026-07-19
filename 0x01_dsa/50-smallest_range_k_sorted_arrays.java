import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Smallest Range Covering Elements from K Sorted Arrays.
 *
 * Given k sorted arrays, find the smallest inclusive range [a, b]
 * such that at least one element from every array lies within the range.
 *
 * Implementations:
 *
 * 1. Brute force (oracle)
 *      Exponential.
 *      Intended only for testing.
 *
 * 2. K-pointer scan
 *      O(totalElements * k)
 *
 * 3. Min-heap
 *      O(totalElements log k)
 */
public class SmallestRangeElementsKSortedArrays {

    /* **********************************************************************
     * Helper classes
     * **********************************************************************/

    /**
     * Heap node.
     */
    private static class Node implements Comparable<Node> {

        final int value;
        final int row;
        final int col;

        Node(int value, int row, int col) {
            this.value = value;
            this.row = row;
            this.col = col;
        }

        @Override
        public int compareTo(Node other) {
            return Integer.compare(this.value, other.value);
        }
    }

    /* **********************************************************************
     * Validation helpers
     * **********************************************************************/

    /**
     * Returns true if every row exists and contains at least one element.
     */
    static boolean validMatrix(int[][] matrix) {

        if (matrix == null || matrix.length == 0) {
            return false;
        }

        for (int[] row : matrix) {
            if (row == null || row.length == 0) {
                return false;
            }
        }

        return true;
    }

    static ArrayList<Integer> makeRange(int left, int right) {
        ArrayList<Integer> ans = new ArrayList<>(2);
        ans.add(left);
        ans.add(right);
        return ans;
    }

    /* **********************************************************************
     * Brute force oracle
     * **********************************************************************/

    /**
     * Enumerates every possible combination.
     *
     * Time:
     *      O(product(rowLengths))
     *
     * Intended only as a correctness oracle for testing.
     */
    static ArrayList<Integer> smallestRangeBruteForce(int[][] matrix) {

        if (!validMatrix(matrix)) {
            return new ArrayList<>();
        }

        Result best = new Result();

        dfs(matrix, 0, new int[matrix.length], best);

        return makeRange(best.left, best.right);
    }

    /**
     * Helper object for recursion.
     */
    private static class Result {

        int left;
        int right;
        int width = Integer.MAX_VALUE;
    }

    /**
     * Recursive enumeration.
     */
    private static void dfs(
            int[][] matrix,
            int row,
            int[] chosen,
            Result best) {

        if (row == matrix.length) {

            int min = Integer.MAX_VALUE;
            int max = Integer.MIN_VALUE;

            for (int value : chosen) {
                min = Math.min(min, value);
                max = Math.max(max, value);
            }

            int width = max - min;

            if (width < best.width ||
    (width == bestWidth && left < bestLeft)) {
                best.width = width;
                best.left = min;
                best.right = max;
            }

            return;
        }

        for (int value : matrix[row]) {
            chosen[row] = value;
            dfs(matrix, row + 1, chosen, best);
        }
    }

    /* **********************************************************************
     * K-pointer algorithm
     * **********************************************************************/

    /**
     * O(totalElements * k)
     */
    static ArrayList<Integer> smallestRangePointers(int[][] matrix) {

        if (!validMatrix(matrix)) {
            return new ArrayList<>();
        }

        int rows = matrix.length;

        int[] ptr = new int[rows];

        int bestLeft = 0;
        int bestRight = 0;
        int bestWidth = Integer.MAX_VALUE;

        while (true) {

            int minValue = Integer.MAX_VALUE;
            int maxValue = Integer.MIN_VALUE;
            int minRow = -1;

            for (int r = 0; r < rows; r++) {

                if (ptr[r] >= matrix[r].length) {
                    return makeRange(bestLeft, bestRight);
                }

                int value = matrix[r][ptr[r]];

                if (value < minValue) {
                    minValue = value;
                    minRow = r;
                }

                if (value > maxValue) {
                    maxValue = value;
                }
            }

            if (maxValue - minValue < bestWidth) {
                bestWidth = maxValue - minValue;
                bestLeft = minValue;
                bestRight = maxValue;
            }

            ptr[minRow]++;
        }
    }

    /* **********************************************************************
     * Min-heap algorithm
     * **********************************************************************/

    /**
     * O(totalElements log k)
     */
    static ArrayList<Integer> smallestRangeMinHeap(int[][] matrix) {

        if (!validMatrix(matrix)) {
            return new ArrayList<>();
        }

        PriorityQueue<Node> pq = new PriorityQueue<>();

        int currentMax = Integer.MIN_VALUE;

        for (int r = 0; r < matrix.length; r++) {

            pq.offer(new Node(matrix[r][0], r, 0));

            currentMax = Math.max(currentMax, matrix[r][0]);
        }

        int bestLeft = 0;
        int bestRight = 0;
        int bestWidth = Integer.MAX_VALUE;

        while (!pq.isEmpty()) {

            Node smallest = pq.poll();

            if (currentMax - smallest.value < bestWidth) {

                bestWidth = currentMax - smallest.value;
                bestLeft = smallest.value;
                bestRight = currentMax;
            }

            int nextCol = smallest.col + 1;

            if (nextCol >= matrix[smallest.row].length) {
                break;
            }

            int nextValue = matrix[smallest.row][nextCol];

            pq.offer(new Node(
                    nextValue,
                    smallest.row,
                    nextCol));

            currentMax = Math.max(currentMax, nextValue);
        }

        return makeRange(bestLeft, bestRight);
    }

        /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    /**
     * Single test case.
     */
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

    /**
     * Allows each algorithm to be executed uniformly.
     */
    @FunctionalInterface
    interface RangeAlgorithm {
        ArrayList<Integer> solve(int[][] matrix);
    }

    static class MethodCase {

        final String name;
        final RangeAlgorithm algorithm;

        MethodCase(String name, RangeAlgorithm algorithm) {
            this.name = name;
            this.algorithm = algorithm;
        }
    }

    /* **********************************************************************
     * Utility methods
     * **********************************************************************/

    /**
     * Deep copy because the algorithms should never share state.
     */
    static int[][] cloneMatrix(int[][] matrix) {

        if (matrix == null) {
            return null;
        }

        int[][] clone = new int[matrix.length][];

        for (int i = 0; i < matrix.length; i++) {
            clone[i] = matrix[i] == null
                    ? null
                    : matrix[i].clone();
        }

        return clone;
    }

    /**
     * Equality for returned ranges.
     */
    static boolean rangesEqual(
            List<Integer> a,
            List<Integer> b) {

        if (a == b) {
            return true;
        }

        if (a == null || b == null) {
            return false;
        }

        if (a.size() != b.size()) {
            return false;
        }

        return a.equals(b);
    }

    static String formatRange(List<Integer> range) {

        if (range == null) {
            return "null";
        }

        return range.toString();
    }

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

            if (matrix[i] == null) {
                sb.append("null");
            } else {
                sb.append(java.util.Arrays.toString(matrix[i]));
            }
        }

        sb.append("]");

        return sb.toString();
    }

    /**
     * Oracle used when expected values are not handwritten.
     */
    static ArrayList<Integer> bruteForceExpected(int[][] matrix) {

        return smallestRangeBruteForce(
                cloneMatrix(matrix)
        );
    }

    /* **********************************************************************
     * Test runner
     * **********************************************************************/

    static void runTests(
            String algorithm,
            RangeAlgorithm method,
            List<TestCase> tests) {

        System.out.println(
                "======================================================");
        System.out.println(algorithm);
        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            try {

                ArrayList<Integer> actual =
                        method.solve(
                                cloneMatrix(test.matrix));

                if (rangesEqual(actual, test.expected)) {

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
                            "  matrix    = %s%n",
                            formatMatrix(test.matrix));

                    System.out.printf(
                            "  expected  = %s%n",
                            formatRange(test.expected));

                    System.out.printf(
                            "  actual    = %s%n",
                            formatRange(actual));
                }

            } catch (Exception ex) {

                failed++;

                System.out.printf(
                        "✗ %s (%s)%n",
                        test.id,
                        test.description);

                System.out.printf(
                        "  matrix    = %s%n",
                        formatMatrix(test.matrix));

                System.out.printf(
                        "  exception = %s%n",
                        ex.toString());
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

    List<TestCase> tests = new ArrayList<>();
            /*
         * ============================================================
         * Standard textbook / LeetCode examples
         * ============================================================
         */

        tests.add(new TestCase(
                "S1",
                new int[][]{
                        {4, 7, 9, 12, 15},
                        {0, 8, 10, 14, 20},
                        {6, 12, 16, 30, 50}
                },
                makeRange(6, 8),
                "Classic GFG example"
        ));

        tests.add(new TestCase(
                "S2",
                new int[][]{
                        {4, 10, 15, 24, 26},
                        {0, 9, 12, 20},
                        {5, 18, 22, 30}
                },
                makeRange(20, 24),
                "Classic LeetCode example"
        ));

        tests.add(new TestCase(
                "S3",
                new int[][]{
                        {1},
                        {2},
                        {3}
                },
                makeRange(1, 3),
                "Single element per list"
        ));

        /*
         * ============================================================
         * Single-list cases
         * ============================================================
         */

        tests.add(new TestCase(
                "A1",
                new int[][]{
                        {5}
                },
                makeRange(5, 5),
                "Single list with one value"
        ));

        tests.add(new TestCase(
                "A2",
                new int[][]{
                        {1, 2, 3, 4, 5}
                },
                makeRange(1, 1),
                "Only one list"
        ));

        /*
         * ============================================================
         * Duplicate values
         * ============================================================
         */

        tests.add(new TestCase(
                "D1",
                new int[][]{
                        {1, 1, 1},
                        {1, 1},
                        {1}
                },
                makeRange(1, 1),
                "All values identical"
        ));

        tests.add(new TestCase(
                "D2",
                new int[][]{
                        {1, 2, 3},
                        {2, 2, 2},
                        {2, 5}
                },
                makeRange(2, 2),
                "Exact common value"
        ));

        tests.add(new TestCase(
                "D3",
                new int[][]{
                        {5, 6},
                        {5},
                        {5, 9}
                },
                makeRange(5, 5),
                "Common duplicate across lists"
        ));

        /*
         * ============================================================
         * Negative values
         * ============================================================
         */

        tests.add(new TestCase(
                "N1",
                new int[][]{
                        {-10, -5, 0},
                        {-6, -2},
                        {-7, -3}
                },
                bruteForceExpected(new int[][]{
                        {-10, -5, 0},
                        {-6, -2},
                        {-7, -3}
                }),
                "Negative values"
        ));

        tests.add(new TestCase(
                "N2",
                new int[][]{
                        {-5},
                        {-4},
                        {-3}
                },
                makeRange(-5, -3),
                "Negative singleton lists"
        ));

        /*
         * ============================================================
         * Mixed negatives and positives
         * ============================================================
         */

        tests.add(new TestCase(
                "M1",
                new int[][]{
                        {-5, -2, 3},
                        {-1, 4},
                        {2, 8}
                },
                bruteForceExpected(new int[][]{
                        {-5, -2, 3},
                        {-1, 4},
                        {2, 8}
                }),
                "Mixed values"
        ));

        /*
         * ============================================================
         * Ragged arrays
         * ============================================================
         */

        tests.add(new TestCase(
                "R1",
                new int[][]{
                        {1, 5, 9},
                        {4},
                        {6, 8}
                },
                bruteForceExpected(new int[][]{
                        {1, 5, 9},
                        {4},
                        {6, 8}
                }),
                "Different row lengths"
        ));

        tests.add(new TestCase(
                "R2",
                new int[][]{
                        {1},
                        {2, 3, 4, 5, 6},
                        {4, 8}
                },
                bruteForceExpected(new int[][]{
                        {1},
                        {2, 3, 4, 5, 6},
                        {4, 8}
                }),
                "Very uneven row lengths"
        ));

        /*
         * ============================================================
         * Large gaps
         * ============================================================
         */

        tests.add(new TestCase(
                "G1",
                new int[][]{
                        {1, 100},
                        {50},
                        {99}
                },
                bruteForceExpected(new int[][]{
                        {1, 100},
                        {50},
                        {99}
                }),
                "Large numeric gaps"
        ));

        tests.add(new TestCase(
                "G2",
                new int[][]{
                        {1, 1000},
                        {500},
                        {999}
                },
                bruteForceExpected(new int[][]{
                        {1, 1000},
                        {500},
                        {999}
                }),
                "Very large gaps"
        ));

        /*
         * ============================================================
         * Edge cases
         * ============================================================
         */

        tests.add(new TestCase(
                "E1",
                null,
                new ArrayList<>(),
                "Null matrix"
        ));

        tests.add(new TestCase(
                "E2",
                new int[][]{},
                new ArrayList<>(),
                "Empty matrix"
        ));

        tests.add(new TestCase(
                "E3",
                new int[][]{
                        {}
                },
                new ArrayList<>(),
                "Single empty row"
        ));

        tests.add(new TestCase(
                "E4",
                new int[][]{
                        {1, 2},
                        {}
                },
                new ArrayList<>(),
                "Mixed valid and empty rows"
        ));

        tests.add(new TestCase(
                "E5",
                new int[][]{
                        null,
                        {1}
                },
                new ArrayList<>(),
                "Null row"
        ));

        /*
         * ============================================================
         * Cross-checks against brute force
         * ============================================================
         */

        tests.add(new TestCase(
                "X1",
                new int[][]{
                        {1, 4},
                        {2, 5},
                        {3, 6}
                },
                bruteForceExpected(new int[][]{
                        {1, 4},
                        {2, 5},
                        {3, 6}
                }),
                "Simple cross-check"
        ));

        tests.add(new TestCase(
                "X2",
                new int[][]{
                        {2, 6, 9},
                        {1, 5},
                        {4, 10}
                },
                bruteForceExpected(new int[][]{
                        {2, 6, 9},
                        {1, 5},
                        {4, 10}
                }),
                "Cross-check"
        ));

        tests.add(new TestCase(
                "X3",
                new int[][]{
                        {3, 8},
                        {1, 5, 9},
                        {2, 6}
                },
                bruteForceExpected(new int[][]{
                        {3, 8},
                        {1, 5, 9},
                        {2, 6}
                }),
                "Cross-check"
        ));

        tests.add(new TestCase(
                "X4",
                new int[][]{
                        {0, 7},
                        {3, 5},
                        {2, 8}
                },
                bruteForceExpected(new int[][]{
                        {0, 7},
                        {3, 5},
                        {2, 8}
                }),
                "Cross-check"
        ));

        tests.add(new TestCase(
                "X5",
                new int[][]{
                        {1, 9},
                        {4, 8},
                        {6, 10}
                },
                bruteForceExpected(new int[][]{
                        {1, 9},
                        {4, 8},
                        {6, 10}
                }),
                "Cross-check"
        ));

                /*
         * ============================================================
         * Standard textbook / LeetCode examples
         * ============================================================
         */

        tests.add(new TestCase(
                "S1",
                new int[][]{
                        {4, 7, 9, 12, 15},
                        {0, 8, 10, 14, 20},
                        {6, 12, 16, 30, 50}
                },
                makeRange(6, 8),
                "Classic GFG example"
        ));

        tests.add(new TestCase(
                "S2",
                new int[][]{
                        {4, 10, 15, 24, 26},
                        {0, 9, 12, 20},
                        {5, 18, 22, 30}
                },
                makeRange(20, 24),
                "Classic LeetCode example"
        ));

        tests.add(new TestCase(
                "S3",
                new int[][]{
                        {1},
                        {2},
                        {3}
                },
                makeRange(1, 3),
                "Single element per list"
        ));

        /*
         * ============================================================
         * Single-list cases
         * ============================================================
         */

        tests.add(new TestCase(
                "A1",
                new int[][]{
                        {5}
                },
                makeRange(5, 5),
                "Single list with one value"
        ));

        tests.add(new TestCase(
                "A2",
                new int[][]{
                        {1, 2, 3, 4, 5}
                },
                makeRange(1, 1),
                "Only one list"
        ));

        /*
         * ============================================================
         * Duplicate values
         * ============================================================
         */

        tests.add(new TestCase(
                "D1",
                new int[][]{
                        {1, 1, 1},
                        {1, 1},
                        {1}
                },
                makeRange(1, 1),
                "All values identical"
        ));

        tests.add(new TestCase(
                "D2",
                new int[][]{
                        {1, 2, 3},
                        {2, 2, 2},
                        {2, 5}
                },
                makeRange(2, 2),
                "Exact common value"
        ));

        tests.add(new TestCase(
                "D3",
                new int[][]{
                        {5, 6},
                        {5},
                        {5, 9}
                },
                makeRange(5, 5),
                "Common duplicate across lists"
        ));

        /*
         * ============================================================
         * Negative values
         * ============================================================
         */

        tests.add(new TestCase(
                "N1",
                new int[][]{
                        {-10, -5, 0},
                        {-6, -2},
                        {-7, -3}
                },
                bruteForceExpected(new int[][]{
                        {-10, -5, 0},
                        {-6, -2},
                        {-7, -3}
                }),
                "Negative values"
        ));

        tests.add(new TestCase(
                "N2",
                new int[][]{
                        {-5},
                        {-4},
                        {-3}
                },
                makeRange(-5, -3),
                "Negative singleton lists"
        ));

        /*
         * ============================================================
         * Mixed negatives and positives
         * ============================================================
         */

        tests.add(new TestCase(
                "M1",
                new int[][]{
                        {-5, -2, 3},
                        {-1, 4},
                        {2, 8}
                },
                bruteForceExpected(new int[][]{
                        {-5, -2, 3},
                        {-1, 4},
                        {2, 8}
                }),
                "Mixed values"
        ));

        /*
         * ============================================================
         * Ragged arrays
         * ============================================================
         */

        tests.add(new TestCase(
                "R1",
                new int[][]{
                        {1, 5, 9},
                        {4},
                        {6, 8}
                },
                bruteForceExpected(new int[][]{
                        {1, 5, 9},
                        {4},
                        {6, 8}
                }),
                "Different row lengths"
        ));

        tests.add(new TestCase(
                "R2",
                new int[][]{
                        {1},
                        {2, 3, 4, 5, 6},
                        {4, 8}
                },
                bruteForceExpected(new int[][]{
                        {1},
                        {2, 3, 4, 5, 6},
                        {4, 8}
                }),
                "Very uneven row lengths"
        ));

        /*
         * ============================================================
         * Large gaps
         * ============================================================
         */

        tests.add(new TestCase(
                "G1",
                new int[][]{
                        {1, 100},
                        {50},
                        {99}
                },
                bruteForceExpected(new int[][]{
                        {1, 100},
                        {50},
                        {99}
                }),
                "Large numeric gaps"
        ));

        tests.add(new TestCase(
                "G2",
                new int[][]{
                        {1, 1000},
                        {500},
                        {999}
                },
                bruteForceExpected(new int[][]{
                        {1, 1000},
                        {500},
                        {999}
                }),
                "Very large gaps"
        ));

        /*
         * ============================================================
         * Edge cases
         * ============================================================
         */

        tests.add(new TestCase(
                "E1",
                null,
                new ArrayList<>(),
                "Null matrix"
        ));

        tests.add(new TestCase(
                "E2",
                new int[][]{},
                new ArrayList<>(),
                "Empty matrix"
        ));

        tests.add(new TestCase(
                "E3",
                new int[][]{
                        {}
                },
                new ArrayList<>(),
                "Single empty row"
        ));

        tests.add(new TestCase(
                "E4",
                new int[][]{
                        {1, 2},
                        {}
                },
                new ArrayList<>(),
                "Mixed valid and empty rows"
        ));

        tests.add(new TestCase(
                "E5",
                new int[][]{
                        null,
                        {1}
                },
                new ArrayList<>(),
                "Null row"
        ));

        /*
         * ============================================================
         * Cross-checks against brute force
         * ============================================================
         */

        tests.add(new TestCase(
                "X1",
                new int[][]{
                        {1, 4},
                        {2, 5},
                        {3, 6}
                },
                bruteForceExpected(new int[][]{
                        {1, 4},
                        {2, 5},
                        {3, 6}
                }),
                "Simple cross-check"
        ));

        tests.add(new TestCase(
                "X2",
                new int[][]{
                        {2, 6, 9},
                        {1, 5},
                        {4, 10}
                },
                bruteForceExpected(new int[][]{
                        {2, 6, 9},
                        {1, 5},
                        {4, 10}
                }),
                "Cross-check"
        ));

        tests.add(new TestCase(
                "X3",
                new int[][]{
                        {3, 8},
                        {1, 5, 9},
                        {2, 6}
                },
                bruteForceExpected(new int[][]{
                        {3, 8},
                        {1, 5, 9},
                        {2, 6}
                }),
                "Cross-check"
        ));

        tests.add(new TestCase(
                "X4",
                new int[][]{
                        {0, 7},
                        {3, 5},
                        {2, 8}
                },
                bruteForceExpected(new int[][]{
                        {0, 7},
                        {3, 5},
                        {2, 8}
                }),
                "Cross-check"
        ));

        tests.add(new TestCase(
                "X5",
                new int[][]{
                        {1, 9},
                        {4, 8},
                        {6, 10}
                },
                bruteForceExpected(new int[][]{
                        {1, 9},
                        {4, 8},
                        {6, 10}
                }),
                "Cross-check"
        ));

            /* **********************************************************************
     * Randomized Stress Testing
     * **********************************************************************/

    static int[][] randomSortedMatrix(
            java.util.Random rng,
            int minRows,
            int maxRows,
            int minCols,
            int maxCols,
            int minValue,
            int maxStep) {

        int rows = minRows + rng.nextInt(maxRows - minRows + 1);

        int[][] matrix = new int[rows][];

        for (int r = 0; r < rows; r++) {

            int cols = minCols + rng.nextInt(maxCols - minCols + 1);

            matrix[r] = new int[cols];

            int value = minValue + rng.nextInt(10);

            for (int c = 0; c < cols; c++) {

                value += rng.nextInt(maxStep + 1);

                matrix[r][c] = value;
            }
        }

        return matrix;
    }

    static void runRandomizedTests(int iterations) {

        System.out.println("======================================================");
        System.out.println("Randomized cross-checks");
        System.out.println("======================================================");

        java.util.Random rng = new java.util.Random(123456789L);

        int passed = 0;

        for (int test = 1; test <= iterations; test++) {

            int[][] matrix = randomSortedMatrix(
                    rng,
                    2,
                    5,
                    1,
                    5,
                    -20,
                    5);

            ArrayList<Integer> brute =
                    smallestRangeBruteForce(cloneMatrix(matrix));

            ArrayList<Integer> pointers =
                    smallestRangePointers(cloneMatrix(matrix));

            ArrayList<Integer> heap =
                    smallestRangeMinHeap(cloneMatrix(matrix));

            if (!rangesEqual(brute, pointers)
                    || !rangesEqual(brute, heap)) {

                System.out.println();
                System.out.println("Randomized test FAILED");
                System.out.println();

                System.out.println("Matrix:");
                System.out.println(formatMatrix(matrix));

                System.out.println();

                System.out.println("Brute Force : " + brute);
                System.out.println("Pointers    : " + pointers);
                System.out.println("Min Heap    : " + heap);

                return;
            }

            passed++;
        }

        System.out.printf(
                "All %d randomized tests passed.%n%n",
                passed);
    }

    /* **********************************************************************
     * Main
     * **********************************************************************/

    public static void main(String[] args) {

        List<TestCase> tests = new ArrayList<>();

        /*
         * ============================================================
         * INSERT THE DETERMINISTIC TESTS FROM PART 3 HERE
         * ============================================================
         */

        System.out.println("############################################################");
        System.out.println("########## SMALLEST RANGE FROM K SORTED ARRAYS #############");
        System.out.println("############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "Brute Force (Oracle)",
                        m -> smallestRangeBruteForce(
                                cloneMatrix(m))
                ),

                new MethodCase(
                        "K-Pointer O(totalElements * k)",
                        m -> smallestRangePointers(
                                cloneMatrix(m))
                ),

                new MethodCase(
                        "Min Heap O(totalElements log k)",
                        m -> smallestRangeMinHeap(
                                cloneMatrix(m))
                )
        );

        for (MethodCase method : methods) {
            runTests(
                    method.name,
                    method.algorithm,
                    tests);
        }

        /*
         * Stress test the optimized algorithms
         * against the brute-force oracle.
         */

        runRandomizedTests(1000);
    }
}
