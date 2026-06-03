// #!/usr/bin/env jshell

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class InsertAndMergeIntervals {

    /**
     * Approach 1: Insert then merge (O(n log n) time due to sorting)
     * Add new interval to list, then merge all overlapping intervals
     */
    static ArrayList<int[]> insertAndMergeIntervalsInsertionMerging(int[][] intervals, int[] newInterval) {
        if (intervals == null || intervals.length == 0 || newInterval == null || newInterval.length == 0) {
            ArrayList<int[]> result = new ArrayList<>();
            if (newInterval != null && newInterval.length == 2) {
                result.add(new int[]{newInterval[0], newInterval[1]});
            }
            return result;
        }

        ArrayList<int[]> intervalsList = new ArrayList<>(Arrays.asList(intervals));
        intervalsList.add(new int[]{newInterval[0], newInterval[1]});

        return mergeOverlap(intervalsList.toArray(new int[0][]));
    }

    /**
     * Approach 2: Insert while maintaining sorted order (O(n) time)
     * Add intervals before newInterval, merge overlapping then add rest
     */
    static ArrayList<int[]> insertAndMergeIntervalsContiguousIntervalsMerging(int[][] intervals, int[] newInterval) {
        if (intervals == null || intervals.length == 0 || newInterval == null || newInterval.length == 0) {
            ArrayList<int[]> result = new ArrayList<>();
            if (newInterval != null && newInterval.length == 2) {
                result.add(new int[]{newInterval[0], newInterval[1]});
            }
            return result;
        }

        ArrayList<int[]> result = new ArrayList<>();
        int i = 0;
        int n = intervals.length;

        // Add all intervals that end before newInterval starts
        while (i < n && intervals[i][1] < newInterval[0]) {
            result.add(new int[]{intervals[i][0], intervals[i][1]});
            i++;
        }

        // Merge all overlapping intervals with newInterval
        while (i < n && intervals[i][0] <= newInterval[1]) {
            newInterval[0] = Math.min(newInterval[0], intervals[i][0]);
            newInterval[1] = Math.max(newInterval[1], intervals[i][1]);
            i++;
        }

        result.add(new int[]{newInterval[0], newInterval[1]});

        // Add remaining intervals
        while (i < n) {
            result.add(new int[]{intervals[i][0], intervals[i][1]});
            i++;
        }

        return result;
    }

    /**
     * Helper: Merge overlapping intervals (O(n log n) time)
     * Sort by start time, then merge overlapping intervals
     */
    static ArrayList<int[]> mergeOverlap(int[][] intervals) {
        if (intervals == null || intervals.length == 0) {
            return new ArrayList<>();
        }

        Arrays.sort(intervals, Comparator.comparingInt(a -> a[0]));

        ArrayList<int[]> result = new ArrayList<>();
        result.add(new int[]{intervals[0][0], intervals[0][1]});

        int n = intervals.length;

        for (int i = 1; i < n; i++) {
            int[] last = result.get(result.size() - 1);
            int[] current = intervals[i];

            if (current[0] <= last[1]) {
                last[1] = Math.max(last[1], current[1]);
            } else {
                result.add(new int[]{current[0], current[1]});
            }
        }

        return result;
    }

    /**
     * Test case record
     */
    static class TestCase {
        final String id;
        final int[][] intervals;
        final int[] newInterval;
        final int[][] expected;

        TestCase(String id, int[][] intervals, int[] newInterval, int[][] expected) {
            this.id = id;
            this.intervals = intervals;
            this.newInterval = newInterval;
            this.expected = expected;
        }
    }

    /**
     * Convert ArrayList<int[]> to String for display
     */
    static String arrayListToString(ArrayList<int[]> list) {
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(Arrays.toString(list.get(i)));
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * Check if two ArrayList<int[]> are equal
     */
    static boolean equals(ArrayList<int[]> a, ArrayList<int[]> b) {
        if (a.size() != b.size()) return false;
        for (int i = 0; i < a.size(); i++) {
            if (!Arrays.equals(a.get(i), b.get(i))) return false;
        }
        return true;
    }

    /**
     * Runs a named approach against all test cases
     */
    static void runTests(String name, java.util.function.BiFunction<int[][], int[], ArrayList<int[]>> func,
                         List<TestCase> tests) {
        System.out.println("========================= method: " + name + " =========================");
        for (TestCase test : tests) {
            ArrayList<int[]> got = func.apply(test.intervals, test.newInterval);
            ArrayList<int[]> expectedList = new ArrayList<>();
            for (int[] interval : test.expected) {
                expectedList.add(new int[]{interval[0], interval[1]});
            }
            boolean passed = equals(got, expectedList);
            System.out.printf(
                "Test %s: intervals=%s, newInterval=%s, passed=%b%n",
                test.id,
                Arrays.deepToString(test.intervals),
                Arrays.toString(test.newInterval),
                passed
            );
            if (!passed) {
                System.out.printf("  got=%s, expected=%s%n",
                    arrayListToString(got),
                    arrayListToString(expectedList)
                );
            }
        }
    }

    public static void main(String[] args) {
        List<TestCase> tests = new ArrayList<>();

        // Test 1: Insert into middle with overlap
        tests.add(new TestCase(
            "1",
            new int[][]{{1, 3}, {6, 9}},
            new int[]{2, 5},
            new int[][]{{1, 5}, {6, 9}}
        ));

        // Test 2: Insert at beginning (LeetCode 57 example 1)
        tests.add(new TestCase(
            "2",
            new int[][]{{1, 2}, {3, 5}, {6, 7}, {8, 10}, {12, 16}},
            new int[]{4, 8},
            new int[][]{{1, 2}, {3, 10}, {12, 16}}
        ));

        // Test 3: Insert at end (no overlap)
        tests.add(new TestCase(
            "3",
            new int[][]{{1, 2}, {3, 5}},
            new int[]{6, 7},
            new int[][]{{1, 2}, {3, 5}, {6, 7}}
        ));

        // Test 4: Insert at beginning (no overlap)
        tests.add(new TestCase(
            "4",
            new int[][]{{3, 5}, {6, 7}},
            new int[]{1, 2},
            new int[][]{{1, 2}, {3, 5}, {6, 7}}
        ));

        // Test 5: Merge all intervals
        tests.add(new TestCase(
            "5",
            new int[][]{{1, 4}, {2, 3}},
            new int[]{0, 5},
            new int[][]{{0, 5}}
        ));

        // Test 6: Empty intervals array
        tests.add(new TestCase(
            "6",
            new int[][]{},
            new int[]{1, 3},
            new int[][]{{1, 3}}
        ));

        // Test 7: Single interval, no overlap
        tests.add(new TestCase(
            "7",
            new int[][]{{5, 7}},
            new int[]{1, 3},
            new int[][]{{1, 3}, {5, 7}}
        ));

        // Test 8: Single interval, full overlap
        tests.add(new TestCase(
            "8",
            new int[][]{{5, 7}},
            new int[]{4, 8},
            new int[][]{{4, 8}}
        ));

        System.out.println("========================= Insert and Merge Intervals Problem =========================");

        runTests(
            "insert then merge",
            (intervals, newI) -> insertAndMergeIntervalsInsertionMerging(intervals, newI),
            tests
        );

        runTests(
            "insert while merging",
            (intervals, newI) -> insertAndMergeIntervalsContiguousIntervalsMerging(intervals, newI),
            tests
        );
    }
}