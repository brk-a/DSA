// #!/usr/bin/env jshell

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MergeIntervals {

    /**
     * Test case record
     */
    static class TestCase {
        final String id;
        final int[][] intervals;
        final int[][] expected;
        final String description;

        TestCase(String id, int[][] intervals, int[][] expected, String description) {
            this.id = id;
            this.intervals = intervals;
            this.expected = expected;
            this.description = description;
        }
    }

    /**
     * Brute force approach: O(n²) time, O(n) space
     * Sort by start time, then for each interval, merge with all overlapping future intervals
     */
    static ArrayList<int[]> mergeIntervalsBruteForce(int[][] intervals) {
        if (intervals == null || intervals.length == 0) {
            return new ArrayList<>();
        }

        int n = intervals.length;
        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
        ArrayList<int[]> result = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            int start = intervals[i][0];
            int end = intervals[i][1];

            // Skip if already merged
            if (!result.isEmpty() && result.get(result.size() - 1)[1] >= end) {
                continue;
            }

            // Merge with all overlapping future intervals
            for (int j = i + 1; j < n; j++) {
                if (intervals[j][0] <= end) {
                    end = Math.max(end, intervals[j][1]);
                }
            }

            result.add(new int[]{start, end});
        }

        return result;
    }

    /**
     * Optimal approach (last merged interval): O(n log n) time, O(n) space
     * Sort by start time, merge on-the-fly with last interval in result
     */
    static ArrayList<int[]> mergeIntervalsLastMergedInterval(int[][] intervals) {
        if (intervals == null || intervals.length == 0) {
            return new ArrayList<>();
        }

        Arrays.sort(intervals, (a, b) -> Integer.compare(a[0], b[0]));
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
    static void runTests(String name, java.util.function.Function<int[][], ArrayList<int[]>> func,
                         List<TestCase> tests) {
        System.out.println("========================= method: " + name + " =========================");
        int passed = 0;
        int failed = 0;
        
        for (TestCase test : tests) {
            ArrayList<int[]> got = func.apply(test.intervals);
            ArrayList<int[]> expectedList = new ArrayList<>();
            for (int[] interval : test.expected) {
                expectedList.add(new int[]{interval[0], interval[1]});
            }
            boolean testPassed = equals(got, expectedList);
            
            if (testPassed) {
                passed++;
                System.out.printf("✓ Test %s (%s): passed%n", test.id, test.description);
            } else {
                failed++;
                System.out.printf("✗ Test %s (%s): FAILED%n", test.id, test.description);
                System.out.printf("  input=%s, got=%s, expected=%s%n",
                    Arrays.deepToString(test.intervals),
                    arrayListToString(got),
                    arrayListToString(expectedList)
                );
            }
        }
        
        System.out.printf("Results: %d passed, %d failed out of %d tests%n", passed, failed, tests.size());
        System.out.println();
    }

    public static void main(String[] args) {
        List<TestCase> positiveTests = new ArrayList<>();
        List<TestCase> negativeTests = new ArrayList<>();
        List<TestCase> edgeTests = new ArrayList<>();

        // ============ POSITIVE TESTS (valid inputs with expected merging) ============
        
        // Test 1: Classic case (LeetCode 56 example 1)
        positiveTests.add(new TestCase(
            "P1",
            new int[][]{{1, 3}, {2, 6}, {8, 10}, {15, 18}},
            new int[][]{{1, 6}, {8, 10}, {15, 18}},
            "Classic overlapping intervals"
        ));

        // Test 2: All intervals merge into one
        positiveTests.add(new TestCase(
            "P2",
            new int[][]{{1, 4}, {2, 3}, {3, 5}},
            new int[][]{{1, 5}},
            "All intervals merge into one"
        ));

        // Test 3: Adjacent intervals (touching at boundary)
        positiveTests.add(new TestCase(
            "P3",
            new int[][]{{1, 2}, {2, 3}, {3, 4}},
            new int[][]{{1, 4}},
            "Adjacent intervals touching at boundary"
        ));

        // Test 4: One interval contains another
        positiveTests.add(new TestCase(
            "P4",
            new int[][]{{1, 10}, {2, 5}, {3, 7}},
            new int[][]{{1, 10}},
            "Nested intervals (one contains others)"
        ));

        // Test 5: Partial overlap
        positiveTests.add(new TestCase(
            "P5",
            new int[][]{{1, 4}, {4, 5}},
            new int[][]{{1, 5}},
            "Partial overlap at boundary"
        ));

        // Test 6: Multiple separate groups
        positiveTests.add(new TestCase(
            "P6",
            new int[][]{{1, 3}, {2, 4}, {6, 8}, {7, 9}},
            new int[][]{{1, 4}, {6, 9}},
            "Multiple separate groups of overlapping intervals"
        ));

        // ============ NEGATIVE TESTS (no merging needed) ============

        // Test 7: No overlaps
        negativeTests.add(new TestCase(
            "N1",
            new int[][]{{1, 2}, {3, 4}, {5, 6}},
            new int[][]{{1, 2}, {3, 4}, {5, 6}},
            "No overlapping intervals"
        ));

        // Test 8: Single interval
        negativeTests.add(new TestCase(
            "N2",
            new int[][]{{5, 7}},
            new int[][]{{5, 7}},
            "Single interval (no merging possible)"
        ));

        // Test 9: Already merged and sorted
        negativeTests.add(new TestCase(
            "N3",
            new int[][]{{1, 2}, {4, 5}, {7, 8}},
            new int[][]{{1, 2}, {4, 5}, {7, 8}},
            "Already non-overlapping and sorted"
        ));

        // ============ EDGE CASE TESTS ============

        // Test 10: Empty array
        edgeTests.add(new TestCase(
            "E1",
            new int[][]{},
            new int[][]{},
            "Empty array"
        ));

        // Test 11: Null input
        edgeTests.add(new TestCase(
            "E2",
            null,
            new int[][]{},
            "Null input"
        ));

        // Test 12: Single element with same start and end
        edgeTests.add(new TestCase(
            "E3",
            new int[][]{{1, 1}},
            new int[][]{{1, 1}},
            "Single point interval (start == end)"
        ));

        // Test 13: Negative numbers
        edgeTests.add(new TestCase(
            "E4",
            new int[][]{{-5, -3}, {-4, -2}, {1, 3}, {2, 4}},
            new int[][]{{-5, -2}, {1, 4}},
            "Intervals with negative numbers"
        ));

        // Test 14: Large values
        edgeTests.add(new TestCase(
            "E5",
            new int[][]{{Integer.MAX_VALUE - 1, Integer.MAX_VALUE}},
            new int[][]{{Integer.MAX_VALUE - 1, Integer.MAX_VALUE}},
            "Large integer values"
        ));

        // Test 15: Reversed order (needs sorting)
        edgeTests.add(new TestCase(
            "E6",
            new int[][]{{8, 10}, {1, 3}, {2, 6}, {15, 18}},
            new int[][]{{1, 6}, {8, 10}, {15, 18}},
            "Unsorted input (needs sorting first)"
        ));

        // Combine all tests
        List<TestCase> allTests = new ArrayList<>();
        allTests.addAll(positiveTests);
        allTests.addAll(negativeTests);
        allTests.addAll(edgeTests);

        System.out.println("##########################################################");
        System.out.println("################# MERGE INTERVALS PROBLEM ##################");
        System.out.println("##########################################################");
        System.out.println();

        runTests(
            "brute force",
            MergeIntervals::mergeIntervalsBruteForce,
            allTests
        );

        runTests(
            "last merged interval",
            MergeIntervals::mergeIntervalsLastMergedInterval,
            allTests
        );
    }
}