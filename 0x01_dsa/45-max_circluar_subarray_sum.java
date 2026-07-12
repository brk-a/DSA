import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class MaximumCircularSubarraySum {

    static class TestCase {
        final String id;
        final int[] nums;
        final int expected;
        final String description;

        TestCase(String id, int[] nums, int expected, String description) {
            this.id = id;
            this.nums = nums;
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
     * Brute-force solution.
     * Enumerates all circular subarrays by choosing a start index and extending
     * up to n elements.
     * Time: O(n^2)
     * Space: O(1)
     */
    static int maximumCircularSubarraySumBruteForce(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int n = nums.length;
        int result = nums[0];

        for (int start = 0; start < n; start++) {
            int currSum = 0;
            for (int len = 0; len < n; len++) {
                int idx = (start + len) % n;
                currSum += nums[idx];
                result = Math.max(result, currSum);
            }
        }

        return result;
    }

    /**
     * Prefix/suffix solution.
     * Time: O(n)
     * Space: O(n)
     *
     * Circular subarray = prefix + suffix, where prefix is from start
     * and suffix is a max suffix starting after a cut point.
     */
    static int maximumCircularSubarraySumPrefixSuffix(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int n = nums.length;

        int[] maxSuffix = new int[n + 1];
        maxSuffix[n] = 0;

        int suffixSum = 0;
        for (int i = n - 1; i >= 0; i--) {
            suffixSum += nums[i];
            maxSuffix[i] = Math.max(maxSuffix[i + 1], suffixSum);
        }

        int normalSum = kadaneMax(nums);
        int prefixSum = 0;
        int circularSum = nums[0];

        for (int i = 0; i < n; i++) {
            prefixSum += nums[i];
            circularSum = Math.max(circularSum, prefixSum + maxSuffix[i + 1]);
        }

        return Math.max(normalSum, circularSum);
    }

    /**
     * Kadane-based solution.
     * Time: O(n)
     * Space: O(1)
     */
    static int maximumCircularSubarraySumKadaneAlgo(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int totalSum = 0;
        int maxSum = nums[0];
        int minSum = nums[0];

        int currMax = 0;
        int currMin = 0;

        for (int num : nums) {
            currMax = Math.max(num, currMax + num);
            maxSum = Math.max(maxSum, currMax);

            currMin = Math.min(num, currMin + num);
            minSum = Math.min(minSum, currMin);

            totalSum += num;
        }

        if (maxSum < 0) {
            return maxSum;
        }

        return Math.max(maxSum, totalSum - minSum);
    }

    private static int kadaneMax(int[] nums) {
        int best = nums[0];
        int curr = nums[0];

        for (int i = 1; i < nums.length; i++) {
            curr = Math.max(nums[i], curr + nums[i]);
            best = Math.max(best, curr);
        }

        return best;
    }

    static boolean arraysEqual(int a, int b) {
        return a == b;
    }

    static String formatArray(int[] nums) {
        return nums == null ? "null" : Arrays.toString(nums);
    }

    static void runTests(String algorithm, Function<TestCase, Integer> method, List<TestCase> tests) {
        System.out.println("==================================================");
        System.out.println(algorithm);
        System.out.println("==================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {
            try {
                int actual = method.apply(test);

                if (arraysEqual(actual, test.expected)) {
                    passed++;
                    System.out.printf("✓ %s (%s)%n", test.id, test.description);
                } else {
                    failed++;
                    System.out.printf("✗ %s (%s)%n", test.id, test.description);
                    System.out.printf("  nums      = %s%n", formatArray(test.nums));
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

    static int bruteForceExpected(int[] nums) {
        return maximumCircularSubarraySumBruteForce(nums);
    }

    public static void main(String[] args) {
        List<TestCase> tests = new ArrayList<>();

        /*
         * Standard examples
         */
        tests.add(new TestCase(
            "S1",
            new int[]{1, -2, 3, -2},
            3,
            "Normal case where non-circular is best"
        ));

        tests.add(new TestCase(
            "S2",
            new int[]{5, -3, 5},
            10,
            "Circular wrap gives the best result"
        ));

        tests.add(new TestCase(
            "S3",
            new int[]{3, -1, 2, -1},
            4,
            "Wrap-around ties or improves the answer"
        ));

        /*
         * Simple cases
         */
        tests.add(new TestCase(
            "M1",
            new int[]{1},
            1,
            "Single element positive"
        ));

        tests.add(new TestCase(
            "M2",
            new int[]{-1},
            -1,
            "Single element negative"
        ));

        tests.add(new TestCase(
            "M3",
            new int[]{0},
            0,
            "Single element zero"
        ));

        tests.add(new TestCase(
            "M4",
            new int[]{1, 2},
            3,
            "Two elements, no wrap needed"
        ));

        /*
         * Mixed cases
         */
        tests.add(new TestCase(
            "C1",
            new int[]{8, -1, 3, 4},
            15,
            "Circular answer excludes the minimum middle section"
        ));

        tests.add(new TestCase(
            "C2",
            new int[]{-4, 5, 1, 0},
            6,
            "Maximum subarray is linear"
        ));

        tests.add(new TestCase(
            "C3",
            new int[]{2, -2, 2, 7, 8, 0},
            19,
            "Circular and non-circular are both relevant"
        ));

        tests.add(new TestCase(
            "C4",
            new int[]{-2, -3, -1},
            -1,
            "All negative values should return the least negative element"
        ));

        tests.add(new TestCase(
            "C5",
            new int[]{4, -1, -2, 1, 5},
            9,
            "Wrap-around uses suffix plus prefix"
        ));

        /*
         * Edge cases
         */
        tests.add(new TestCase(
            "E1",
            null,
            0,
            "Null input"
        ));

        tests.add(new TestCase(
            "E2",
            new int[]{},
            0,
            "Empty input"
        ));

        tests.add(new TestCase(
            "E3",
            new int[]{0, 0, 0},
            0,
            "All zeros"
        ));

        tests.add(new TestCase(
            "E4",
            new int[]{-5, -2, -7, -3},
            -2,
            "All negative long input"
        ));

        /*
         * Cross-check cases
         */
        tests.add(new TestCase(
            "X1",
            new int[]{3, 0, 1, 0, 4, 0, 2},
            bruteForceExpected(new int[]{3, 0, 1, 0, 4, 0, 2}),
            "Brute-force computed expected value"
        ));

        tests.add(new TestCase(
            "X2",
            new int[]{2, -1, 2, -1, 2, -1},
            bruteForceExpected(new int[]{2, -1, 2, -1, 2, -1}),
            "Another brute-force cross-check"
        ));

        tests.add(new TestCase(
            "X3",
            new int[]{6, -6, 6},
            bruteForceExpected(new int[]{6, -6, 6}),
            "Wide wrap-around cross-check"
        ));

        System.out.println("############################################################");
        System.out.println("########### MAXIMUM CIRCULAR SUBARRAY SUM ##################");
        System.out.println("############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(
            new MethodCase("Brute Force O(n^2)", t -> maximumCircularSubarraySumBruteForce(t.nums)),
            new MethodCase("Prefix/Suffix O(n)", t -> maximumCircularSubarraySumPrefixSuffix(t.nums)),
            new MethodCase("Kadane O(n)", t -> maximumCircularSubarraySumKadaneAlgo(t.nums))
        );

        for (MethodCase method : methods) {
            runTests(method.name, method.method, tests);
        }
    }
}
