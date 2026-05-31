// #!/usr/bin/env jshell

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class ContainerWithMostWater {

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
     * Check all possible pairs of lines and calculate area
     */
    static int containerWithMostWaterBruteForce(int[] height) {
        int n = height.length;
        int result = 0;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                // Area = min(height[i], height[j]) * width (j - i)
                int amount = Math.min(height[i], height[j]) * (j - i);
                result = Math.max(amount, result);
            }
        }

        return result;
    }

    /**
     * Two-pointer approach: O(n) time, O(1) space
     * Start with widest container, move shorter pointer inward
     */
    static int containerWithMostWaterTwoPointers(int[] height) {
        int left = 0;
        int right = height.length - 1;
        int result = 0;
        while (left < right) {
            // Calculate current area
            int water = Math.min(height[left], height[right]) * (right - left);
            result = Math.max(result, water);

            // Move the pointer pointing to the shorter line
            if (height[left] < height[right]) {
                left++;
            } else {
                right--;
            }
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

        // Test 1: Classic case (LeetCode 11 example 1)
        tests.add(new TestCase(
            "1",
            new int[]{1, 8, 6, 2, 5, 4, 8, 3, 7},
            49  // lines at index 1 and 8: min(8,7) * 7 = 49
        ));

        // Test 2: LeetCode 11 example 2
        tests.add(new TestCase(
            "2",
            new int[]{1, 1},
            1  // min(1,1) * 1 = 1
        ));

        // Test 3: All same height
        tests.add(new TestCase(
            "3",
            new int[]{5, 5, 5, 5},
            15  // min(5,5) * 3 = 15 (first and last)
        ));

        // Test 4: Increasing heights
        tests.add(new TestCase(
            "4",
            new int[]{1, 2, 3, 4, 5},
            6
        ));

        // Test 5: Decreasing heights
        tests.add(new TestCase(
            "5",
            new int[]{5, 4, 3, 2, 1},
            6 
        ));

        // Test 6: Two tall lines with short ones in between
        tests.add(new TestCase(
            "6",
            new int[]{1, 1, 1, 1, 10, 1, 1, 1, 1, 10},
            50  // min(10,10) * 5 = 50 (indices 4 and 9)
        ));

        // Test 7: Single pair
        tests.add(new TestCase(
            "7",
            new int[]{3, 7},
            3  // min(3,7) * 1 = 3
        ));

        // Test 8: Three elements
        tests.add(new TestCase(
            "8",
            new int[]{1, 5, 1},
            2  // min(1,1)*2 = 2
        ));

        System.out.println("========================= Container With Most Water Problem =========================");

        runTests(
            "brute force",
            ContainerWithMostWater::containerWithMostWaterBruteForce,
            tests
        );

        runTests(
            "two pointers",
            ContainerWithMostWater::containerWithMostWaterTwoPointers,
            tests
        );
    }
}