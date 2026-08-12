// #!/usr/bin/env jshell

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.Function;

public class RowWiseMatrixMedian {

    // ==================== Test case record ====================
    static class TestCase {
        final String id;
        final int[][] matrix;
        final Integer expected; // null if we expect an exception

        TestCase(String id, int[][] matrix, Integer expected) {
            this.id = id;
            this.matrix = copyMatrix(matrix);
            this.expected = expected;
        }

        private static int[][] copyMatrix(int[][] matrix) {
            if (matrix == null) return null;
            int[][] copy = new int[matrix.length][];
            for (int i = 0; i < matrix.length; i++) {
                if (matrix[i] == null) {
                    copy[i] = null;
                } else {
                    copy[i] = matrix[i].clone();
                }
            }
            return copy;
        }
    }

    // ==================== Validation ====================
    private static boolean validMatrix(int[][] matrix) {
        return matrix != null
                && matrix.length > 0
                && matrix[0] != null
                && matrix[0].length > 0;
    }

    private static void requireValidMatrix(int[][] matrix) {
        if (!validMatrix(matrix)) {
            throw new IllegalArgumentException("Matrix must be non-null with at least one row and one column.");
        }
    }

    // ==================== Approach 1: Sorting ====================
    /**
     * Flatten matrix to list, sort, and pick middle element.
     * Time: O(mn log(mn)), Space: O(mn)
     */
    static int medianSorting(int[][] matrix) {
        requireValidMatrix(matrix);

        int m = matrix.length;
        int n = matrix[0].length;

        List<Integer> list = new ArrayList<>(m * n);
        for (int i = 0; i < m; i++) {
            for (int j = 0; j < n; j++) {
                list.add(matrix[i][j]);
            }
        }

        Collections.sort(list);
        int mid = list.size() / 2;
        return list.get(mid);
    }

    // ==================== Approach 2: Priority Queue (k-way merge) ====================
    /**
     * Use a min-heap to iterate through elements in sorted order until median index.
     * Time: O((mn/2) log m), Space: O(m)
     */
    static int medianPriorityQueue(int[][] matrix) {
        requireValidMatrix(matrix);

        int rows = matrix.length;
        int cols = matrix[0].length;

        PriorityQueue<int[]> minHeap = new PriorityQueue<>(
                Comparator.comparingInt(a -> a[0])
        );

        // Push first element of each row: {value, row, col}
        for (int i = 0; i < rows; i++) {
            minHeap.add(new int[]{matrix[i][0], i, 0});
        }

        int total = rows * cols;
        int medianIdx = total / 2; // 0-based index of median
        int count = 0;
        int result = -1;

        while (count <= medianIdx) {
            int[] top = minHeap.poll();
            int val = top[0];
            int row = top[1];
            int col = top[2];

            result = val;
            count++;

            if (col + 1 < cols) {
                minHeap.add(new int[]{matrix[row][col + 1], row, col + 1});
            }
        }

        return result;
    }

    // ==================== Approach 3: Binary Search on Value Range ====================
    /**
     * Binary search on value range [min, max] using upper_bound per row.
     * Time: O(m * log(n) * log(max - min)), Space: O(1)
     */
    static int medianBinarySearch(int[][] matrix) {
        requireValidMatrix(matrix);

        int m = matrix.length;      // rows
        int n = matrix[0].length;   // cols

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        // Since each row is sorted, min is min of first elements, max is max of last elements
        for (int i = 0; i < m; i++) {
            if (matrix[i][0] < min) {
                min = matrix[i][0];
            }
            if (matrix[i][n - 1] > max) {
                max = matrix[i][n - 1];
            }
        }

        int total = m * n;
        int desired = (total + 1) / 2; // 1-based count of elements <= median

        while (min < max) {
            int mid = min + (max - min) / 2;
            int count = 0;
            for (int i = 0; i < m; i++) {
                count += upperBound(matrix[i], mid);
            }

            if (count < desired) {
                min = mid + 1;
            } else {
                max = mid;
            }
        }

        return min;
    }

    /**
     * Returns number of elements in sorted array 'row' that are <= target.
     * Equivalent to upper_bound index.
     */
    private static int upperBound(int[] row, int target) {
        int low = 0;
        int high = row.length - 1;
        int pos = 0;

        while (low <= high) {
            int mid = low + (high - low) / 2;
            if (row[mid] <= target) {
                pos = mid + 1;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        return pos;
    }

    // ==================== Test runner ====================
    static void runTests(
            String name,
            Function<int[][], Integer> func,
            List<TestCase> tests
    ) {
        System.out.println("========================= method: " + name + " =========================");
        for (TestCase test : tests) {
            boolean passed;
            String resultStr;

            try {
                int got = func.apply(test.matrix);
                if (test.expected == null) {
                    passed = false;
                    resultStr = String.format(
                            "Test %s: got=%d, expected=exception, passed=%b",
                            test.id, got, passed
                    );
                } else {
                    passed = got == test.expected;
                    resultStr = String.format(
                            "Test %s: got=%d, expected=%d, passed=%b",
                            test.id, got, test.expected, passed
                    );
                }
            } catch (Exception e) {
                if (test.expected == null) {
                    passed = true;
                    resultStr = String.format(
                            "Test %s: got=exception(%s), expected=exception, passed=%b",
                            test.id, e.getClass().getSimpleName(), passed
                    );
                } else {
                    passed = false;
                    resultStr = String.format(
                            "Test %s: got=exception(%s), expected=%d, passed=%b",
                            test.id, e.getClass().getSimpleName(), test.expected, passed
                    );
                }
            }

            System.out.println(resultStr);
        }
    }

    // ==================== Main / Test suite ====================
    public static void main(String[] args) {
        List<TestCase> tests = new ArrayList<>();

        // Test 1: Simple 3x3 matrix
        tests.add(new TestCase(
                "1",
                new int[][]{
                        {1, 3, 5},
                        {2, 4, 6},
                        {7, 8, 9}
                },
                5 // sorted: [1,2,3,4,5,6,7,8,9] -> median index 4 -> 5
        ));

        // Test 2: 1x1 matrix
        tests.add(new TestCase(
                "2",
                new int[][]{
                        {42}
                },
                42
        ));

        // Test 3: 1 row, multiple columns
        tests.add(new TestCase(
                "3",
                new int[][]{
                        {1, 2, 3, 4, 5}
                },
                3 // median of [1,2,3,4,5]
        ));

        // Test 4: 1 column, multiple rows
        tests.add(new TestCase(
                "4",
                new int[][]{
                        {1},
                        {3},
                        {5},
                        {7},
                        {9}
                },
                5
        ));

        // Test 5: Even number of elements (definition: take upper middle)
        // For 4 elements: indices 0,1,2,3 -> median index = 4/2 = 2
        tests.add(new TestCase(
                "5",
                new int[][]{
                        {1, 2},
                        {3, 4}
                },
                3 // sorted: [1,2,3,4] -> index 2 -> 3
        ));

        // Test 6: Duplicates
        tests.add(new TestCase(
                "6",
                new int[][]{
                        {1, 2, 2},
                        {2, 2, 3},
                        {2, 4, 5}
                },
                2 // sorted: [1,2,2,2,2,2,3,4,5] -> median index 4 -> 2
        ));

        // Test 7: Larger matrix
        tests.add(new TestCase(
                "7",
                new int[][]{
                        {1, 3, 5, 7},
                        {2, 4, 6, 8},
                        {9, 10, 11, 12}
                },
                6 // total 12 -> median index 6 -> 0-based sorted[6]
                // sorted: [1,2,3,4,5,6,7,8,9,10,11,12] -> index 6 -> 7? WTF!? 12 elements, index 6 is 7.
                // Let's recalc: total=12, medianIdx = 12/2 = 6, sorted[6] = 7.
                // So correct expected is 7, not 6.
                7
        ));

        // Test 8: Invalid matrix (null) – expect exception
        tests.add(new TestCase(
                "8",
                null,
                null
        ));

        // Test 9: Invalid matrix (empty rows) – expect exception
        tests.add(new TestCase(
                "9",
                new int[][]{},
                null
        ));

        // Test 10: Invalid matrix (row with zero columns) – expect exception
        tests.add(new TestCase(
                "10",
                new int[][]{
                        {}
                },
                null
        ));

        System.out.println("========================= Row-Wise Matrix Median Problem =========================");

        runTests(
                "medianSorting",
                RowWiseMatrixMedian::medianSorting,
                tests
        );

        runTests(
                "medianPriorityQueue",
                RowWiseMatrixMedian::medianPriorityQueue,
                tests
        );

        runTests(
                "medianBinarySearch",
                RowWiseMatrixMedian::medianBinarySearch,
                tests
        );
    }
}
