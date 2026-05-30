// #!/usr/bin/env jshell

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class MaxSubarraySum {

    // Test case record
    static class TestCase {
        final String id;
        final int[] nums;
        final int expected;

        TestCase(String id, int[] nums, int expected) {
            this.id = id;
            this.nums = nums.clone();
            this.expected = expected;
        }
    }

    /**
     * Brute force approach: O(n²) time, O(1) space
     * Check all possible subarrays and track maximum sum
     */
    static int maxSubArraySumBruteForce(int[] nums) {
        int result = nums[0];
        int n = nums.length;

        for (int i = 0; i < n; i++) {
            int currentSum = 0;
            for (int j = i; j < n; j++) {
                currentSum += nums[j];
                result = Math.max(result, currentSum);
            }
        }

        return result;
    }

    /**
     * Kadane's Algorithm: O(n) time, O(1) space
     * Track maximum subarray ending at each position
     */
    static int maxSubArraySumKadaneAlgo(int[] nums) {
        int result = nums[0];
        int maxEnding = nums[0];
        int n = nums.length;

        for (int i = 1; i < n; i++) {
            maxEnding = Math.max(maxEnding + nums[i], nums[i]);
            result = Math.max(result, maxEnding);
        }

        return result;
    }

    /**
     * Runs a named approach against all test cases
     */
    static void runTests(String name, java.util.function.Function<int[], Integer> func,
                         List<TestCase> tests) {
        System.out.println("========================= method: " + name + " =========================");
        for (TestCase test : tests) {
            int got = func.apply(test.nums);
            boolean passed = got == test.expected;
            System.out.printf(
                "Test %s: nums=%s, got=%d, expected=%d, passed=%b%n",
                test.id,
                Arrays.toString(test.nums),
                got,
                test.expected,
                passed
            );
        }
    }

    public static void main(String[] args) {
        List<TestCase> tests = new ArrayList<>();

        // Test 1: Classic case (LeetCode 53 example)
        tests.add(new TestCase(
            "1",
            new int[]{-2, 1, -3, 4, -1, 2, 1, -5, 4},
            6  // subarray [4, -1, 2, 1]
        ));

        // Test 2: All positive numbers
        tests.add(new TestCase(
            "2",
            new int[]{1, 2, 3, 4, 5},
            15  // entire array
        ));

        // Test 3: All negative numbers
        tests.add(new TestCase(
            "3",
            new int[]{-1, -2, -3, -4},
            -1  // single largest element
        ));

        // Test 4: Single element
        tests.add(new TestCase(
            "4",
            new int[]{5},
            5
        ));

        // Test 5: Mixed with zeros
        tests.add(new TestCase(
            "5",
            new int[]{-2, 0, -1},
            0  // single element 0
        ));

        // Test 6: Two elements, positive then negative
        tests.add(new TestCase(
            "6",
            new int[]{5, -3},
            5  // just the first element
        ));

        // Test 7: Two elements, negative then positive
        tests.add(new TestCase(
            "7",
            new int[]{-3, 5},
            5  // just the second element
        ));

        // Test 8: Larger array with negative prefix
        tests.add(new TestCase(
            "8",
            new int[]{-5, -2, -8, -1, -4},
            -1  // single largest element
        ));

        System.out.println("========================= Maximum Subarray Sum Problem =========================");

        runTests(
            "brute force",
            MaxSubarraySum::maxSubArraySumBruteForce,
            tests
        );

        runTests(
            "Kadane's algorithm",
            MaxSubarraySum::maxSubArraySumKadaneAlgo,
            tests
        );
    }
}
