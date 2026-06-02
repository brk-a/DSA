// #!/usr/bin/env jshell

import java.util.Stack;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class TrappingRainWater {

    // Test case record
    static class TestCase {
        final String id;
        final int[] height;
        final int expected;

        TestCase(String id, int[] height, int expected) {
            this.id = id;
            this.height = height.clone();
            this.expected = expected;
        }
    }

    /**
     * Brute force approach: O(n²) time, O(1) space
     * For each bar, find max height to left and right
     */
    static int trappingRainWaterBruteForce(int[] height) {
        int n = height.length;
        if (n == 0) {
            return 0;
        }

        int result = 0;

        for (int i = 1; i < n - 1; i++) {
            int left = height[i];
            for (int j = 0; j < i; j++) {
                left = Math.max(left, height[j]);
            }

            int right = height[i];
            for (int j = i + 1; j < n; j++) {
                right = Math.max(right, height[j]);
            }

            result += Math.min(left, right) - height[i];
        }

        return result;
    }

    /**
     * Prefix-Suffix max approach: O(n) time, O(n) space
     * Precompute left and right max arrays
     */
    static int trappingRainWaterPrefixSuffixMax(int[] height) {
        int n = height.length;
        if (n == 0) return 0;

        int[] left = new int[n];
        int[] right = new int[n];
        int result = 0;

        left[0] = height[0];
        for (int i = 1; i < n; i++) {
            left[i] = Math.max(left[i - 1], height[i]);
        }

        right[n - 1] = height[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            right[i] = Math.max(right[i + 1], height[i]);
        }

        for (int i = 0; i < n; i++) {
            int minOfTwo = Math.min(left[i], right[i]);
            result += minOfTwo - height[i];
        }

        return result;
    }

    /**
     * Two pointers approach: O(n) time, O(1) space
     * Use two pointers from both ends, track max from each side
     */
    static int trappingRainWaterTwoPointers(int[] height) {
        int n = height.length;
        if (n == 0) return 0;

        int left = 0;
        int right = n - 1;
        int leftMax = 0;
        int rightMax = 0;
        int result = 0;

        while (left < right) {
            if (height[left] < height[right]) {
                if (height[left] >= leftMax) {
                    leftMax = height[left];
                } else {
                    result += leftMax - height[left];
                }
                left++;
            } else {
                if (height[right] >= rightMax) {
                    rightMax = height[right];
                } else {
                    result += rightMax - height[right];
                }
                right--;
            }
        }

        return result;
    }

    /**
     * Stack-based approach: O(n) time, O(n) space
     * Use monotonic stack to find trapped water between bars
     */
    static int trappingRainWaterStack(int[] height) {
        int n = height.length;
        if (n == 0) {
            return 0;
        }

        Stack<Integer> stack = new Stack<>();
        int result = 0;

        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && height[stack.peek()] < height[i]) {
                int popHeight = height[stack.pop()];
                if (stack.isEmpty()) {
                    break;
                }

                int distance = i - stack.peek() - 1;
                int boundedHeight = Math.min(height[stack.peek()], height[i]) - popHeight;
                result += distance * boundedHeight;
            }

            stack.push(i);
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
            int got = func.apply(test.height);
            boolean passed = got == test.expected;
            System.out.printf(
                "Test %s: height=%s, got=%d, expected=%d, passed=%b%n",
                test.id,
                Arrays.toString(test.height),
                got,
                test.expected,
                passed
            );
        }
    }

    public static void main(String[] args) {
        List<TestCase> tests = new ArrayList<>();

        // Test 1: Classic case (LeetCode 42 example 1)
        tests.add(new TestCase(
            "1",
            new int[]{0, 1, 0, 2, 1, 0, 1, 3, 2, 1, 2, 1},
            6
        ));

        // Test 2: LeetCode 42 example 2
        tests.add(new TestCase(
            "2",
            new int[]{4, 2, 0, 3, 2, 5},
            9
        ));

        // Test 3: All same height
        tests.add(new TestCase(
            "3",
            new int[]{2, 2, 2, 2},
            0
        ));

        // Test 4: Increasing then decreasing
        tests.add(new TestCase(
            "4",
            new int[]{1, 2, 3, 4, 3, 2, 1},
            0
        ));

        // Test 5: V-shape
        tests.add(new TestCase(
            "5",
            new int[]{3, 2, 1, 2, 3},
            4
        ));

        // Test 6: Single element
        tests.add(new TestCase(
            "6",
            new int[]{5},
            0
        ));

        // Test 7: Two elements
        tests.add(new TestCase(
            "7",
            new int[]{3, 5},
            0
        ));

        // Test 8: Empty array
        tests.add(new TestCase(
            "8",
            new int[]{},
            0
        ));

        // Test 9: Large gap
        tests.add(new TestCase(
            "9",
            new int[]{5, 0, 0, 0, 5},
            15
        ));

        System.out.println("========================= Trapping Rain Water Problem =========================");

        runTests(
            "brute force",
            TrappingRainWater::trappingRainWaterBruteForce,
            tests
        );

        runTests(
            "prefix-suffix max",
            TrappingRainWater::trappingRainWaterPrefixSuffixMax,
            tests
        );

        runTests(
            "two pointers",
            TrappingRainWater::trappingRainWaterTwoPointers,
            tests
        );

        runTests(
            "stack",
            TrappingRainWater::trappingRainWaterStack,
            tests
        );
    }
}