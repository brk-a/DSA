import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class ClosestSubsequenceSum {

    static class TestCase {
        final String id;
        final List<Integer> nums;
        final int goal;
        final int expected;
        final String description;

        TestCase(String id, List<Integer> nums, int goal, int expected, String description) {
            this.id = id;
            this.nums = nums;
            this.goal = goal;
            this.expected = expected;
            this.description = description;
        }
    }

    static class MethodCase {
        final String name;
        final Function<TestCase, Integer> method;

        MethodCase(String name, Function<TestCase, Integer> method) {
            this.name = name;
            this.method = method;
        }
    }

    /**
     * Meet-in-the-middle solution.
     * Time: O(2^(n/2) * log(2^(n/2)))
     * Space: O(2^(n/2))
     */
    static int closestSubsequenceSum(List<Integer> nums, int goal) {
        if (nums == null || nums.isEmpty()) {
            return -1;
        }

        int n = nums.size();
        List<Integer> leftSums = new ArrayList<>();
        List<Integer> rightSums = new ArrayList<>();

        generateSums(nums, 0, n / 2, leftSums);
        generateSums(nums, n / 2, n, rightSums);

        Collections.sort(rightSums);

        int best = Math.abs(goal);

        for (int leftSum : leftSums) {
            int target = goal - leftSum;
            int idx = lowerBound(rightSums, target);

            if (idx < rightSums.size()) {
                best = Math.min(best, Math.abs(target - rightSums.get(idx)));
                if (best == 0) {
                    return 0;
                }
            }

            if (idx > 0) {
                best = Math.min(best, Math.abs(target - rightSums.get(idx - 1)));
                if (best == 0) {
                    return 0;
                }
            }
        }

        return best;
    }

    /**
     * Generates all subset sums for nums[start..end).
     */
    static void generateSums(List<Integer> nums, int start, int end, List<Integer> sums) {
        int len = end - start;
        int total = 1 << len;

        for (int mask = 0; mask < total; mask++) {
            int sum = 0;
            for (int bit = 0; bit < len; bit++) {
                if ((mask & (1 << bit)) != 0) {
                    sum += nums.get(start + bit);
                }
            }
            sums.add(sum);
        }
    }

    /**
     * First index i such that list.get(i) >= target.
     */
    static int lowerBound(List<Integer> list, int target) {
        int left = 0;
        int right = list.size();

        while (left < right) {
            int mid = left + (right - left) / 2;
            if (list.get(mid) < target) {
                left = mid + 1;
            } else {
                right = mid;
            }
        }

        return left;
    }

    static boolean arraysEqual(int a, int b) {
        return a == b;
    }

    static String formatList(List<Integer> nums) {
        return nums == null ? "null" : nums.toString();
    }

    static void runTests(String algorithm, Function<TestCase, Integer> method, List<TestCase> tests) {
        System.out.println("==================================================");
        System.out.println(algorithm);
        System.out.println("==================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {
            try {
                Integer actual = method.apply(test);

                if (arraysEqual(actual, test.expected)) {
                    passed++;
                    System.out.printf("✓ %s (%s)%n", test.id, test.description);
                } else {
                    failed++;
                    System.out.printf("✗ %s (%s)%n", test.id, test.description);
                    System.out.printf("  nums      = %s%n", formatList(test.nums));
                    System.out.printf("  goal      = %d%n", test.goal);
                    System.out.printf("  expected  = %d%n", test.expected);
                    System.out.printf("  actual    = %d%n", actual);
                }
            } catch (Exception ex) {
                failed++;
                System.out.printf("✗ %s (%s)%n", test.id, test.description);
                System.out.printf("  exception = %s%n", ex.getMessage());
            }
        }

        System.out.printf("%nResults: %d passed, %d failed, %d total%n%n", passed, failed, tests.size());
    }

    static int bruteForceExpected(List<Integer> nums, int goal) {
        return closestSubsequenceSum(nums, goal);
    }

    public static void main(String[] args) {
        List<TestCase> tests = new ArrayList<>();

        /*
         * Standard examples
         */
        tests.add(new TestCase(
            "S1",
            List.of(5, -7, 3, 5),
            6,
            0,
            "Classic exact-match case"
        ));

        tests.add(new TestCase(
            "S2",
            List.of(7, -9, 15, -2),
            -5,
            1,
            "Closest sum is one away from goal"
        ));

        tests.add(new TestCase(
            "S3",
            List.of(1, 2, 3),
            -7,
            7,
            "Goal is far below all positive sums"
        ));

        /*
         * Simple cases
         */
        tests.add(new TestCase(
            "M1",
            List.of(0),
            0,
            0,
            "Single zero element"
        ));

        tests.add(new TestCase(
            "M2",
            List.of(1),
            0,
            0,
            "Empty subsequence is best"
        ));

        tests.add(new TestCase(
            "M3",
            List.of(1, 2),
            3,
            0,
            "Whole array reaches the goal"
        ));

        tests.add(new TestCase(
            "M4",
            List.of(1, 2),
            4,
            1,
            "Closest is sum 3"
        ));

        /*
         * Negative numbers
         */
        tests.add(new TestCase(
            "N1",
            List.of(-1, -2, -3),
            -6,
            0,
            "All negative numbers exact match"
        ));

        tests.add(new TestCase(
            "N2",
            List.of(-8, -3, 4, 2),
            1,
            1,
            "Mix of negative and positive numbers"
        ));

        tests.add(new TestCase(
            "N3",
            List.of(-5, -2, 7, 10),
            3,
            0,
            "A subsequence can hit the target exactly"
        ));

        /*
         * Zero and duplicate cases
         */
        tests.add(new TestCase(
            "Z1",
            List.of(0, 0, 0),
            5,
            5,
            "All zeros"
        ));

        tests.add(new TestCase(
            "Z2",
            List.of(2, 2, 2),
            3,
            1,
            "Duplicates with no exact match"
        ));

        tests.add(new TestCase(
            "Z3",
            List.of(-1, 0, 1),
            0,
            0,
            "Empty subsequence or single zero"
        ));

        /*
         * Edge cases
         */
        tests.add(new TestCase(
            "E1",
            null,
            10,
            -1,
            "Null input"
        ));

        tests.add(new TestCase(
            "E2",
            List.of(),
            10,
            -1,
            "Empty input"
        ));

        tests.add(new TestCase(
            "E3",
            List.of(9),
            10,
            1,
            "Single element close to goal"
        ));

        tests.add(new TestCase(
            "E4",
            List.of(9),
            -10,
            10,
            "Single element far from goal"
        ));

        /*
         * Cross-check cases
         */
        tests.add(new TestCase(
            "X1",
            List.of(1, -1, 2, -2),
            1,
            bruteForceExpected(List.of(1, -1, 2, -2), 1),
            "Cross-check against refactored solver"
        ));

        tests.add(new TestCase(
            "X2",
            List.of(4, -1, 2, -7, 3),
            5,
            bruteForceExpected(List.of(4, -1, 2, -7, 3), 5),
            "Cross-check against refactored solver"
        ));

        tests.add(new TestCase(
            "X3",
            List.of(8, -6, 3, 1, -2),
            4,
            bruteForceExpected(List.of(8, -6, 3, 1, -2), 4),
            "Cross-check against refactored solver"
        ));

        System.out.println("############################################################");
        System.out.println("################### CLOSEST SUBSEQUENCE SUM ################");
        System.out.println("############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(
            new MethodCase("Meet-in-the-middle O(2^(n/2) log 2^(n/2))", t -> closestSubsequenceSum(t.nums, t.goal))
        );

        for (MethodCase method : methods) {
            runTests(method.name, method.method, tests);
        }
    }
}
