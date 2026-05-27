// #!/usr/bin/env jshell

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class TwoSum {

    // Test case record
    static class TestCase {
        final String id;
        final int[] nums;
        final int target;
        final boolean expected;

        TestCase(String id, int[] nums, int target, boolean expected) {
            this.id = id;
            this.nums = nums.clone();
            this.target = target;
            this.expected = expected;
        }
    }

    // 1. Brute force
    static boolean twoSumBruteForce(int[] nums, int target) {
        int n = nums.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (nums[i] + nums[j] == target) {
                    return true;
                }
            }
        }
        return false;
    }

    // 2. Sort + binary search style
    static boolean twoSumBinarySearch(int[] nums, int target) {
        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        int n = sorted.length;
        for (int i = 0; i < n; i++) {
            int complement = target - sorted[i];
            if (binarySearch(sorted, i + 1, n - 1, complement)) {
                return true;
            }
        }
        return false;
    }

    private static boolean binarySearch(int[] nums, int left, int right, int target) {
        while (left <= right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] == target) {
                return true;
            } else if (nums[mid] < target) {
                left = mid + 1;
            } else {
                right = mid - 1;
            }
        }
        return false;
    }

    // 3. Two pointers after sort
    static boolean twoSumTwoPointers(int[] nums, int target) {
        int[] sorted = nums.clone();
        Arrays.sort(sorted);
        int left = 0;
        int right = sorted.length - 1;

        while (left < right) {
            int sum = sorted[left] + sorted[right];
            if (sum == target) {
                return true;
            } else if (sum < target) {
                left++;
            } else {
                right--;
            }
        }
        return false;
    }

    // 4. Hash set
    static boolean twoSumHashSet(int[] nums, int target) {
        HashSet<Integer> seen = new HashSet<>();
        for (int num : nums) {
            int complement = target - num;
            if (seen.contains(complement)) {
                return true;
            }
            seen.add(num);
        }
        return false;
    }

    // Runs a named approach against all tests
    static void runTests(String name, java.util.function.BiFunction<int[], Integer, Boolean> func,
                         List<TestCase> tests) {
        System.out.println("========================= method: " + name + " =========================");
        for (TestCase test : tests) {
            boolean got = func.apply(test.nums, test.target);
            boolean passed = got == test.expected;
            System.out.printf(
                "Test %s: nums=%s, target=%d, got=%b, expected=%b, passed=%b%n",
                test.id,
                Arrays.toString(test.nums),
                test.target,
                got,
                test.expected,
                passed
            );
        }
    }

    public static void main(String[] args) {
        List<TestCase> tests = new ArrayList<>();

        // Test 1: has a pair
        tests.add(new TestCase(
            "1",
            new int[]{2, 7, 11, 15},
            9,
            true
        ));

        // Test 2: no pair
        tests.add(new TestCase(
            "2",
            new int[]{2, 3, 4},
            10,
            false
        ));

        // Test 3: one element, same value twice allowed?
        tests.add(new TestCase(
            "3",
            new int[]{3, 3},
            6,
            true
        ));

        // Test 4: negative numbers
        tests.add(new TestCase(
            "4",
            new int[]{-1, 0, 1, 2},
            -1,
            true
        ));

        System.out.println("========================= Two-sum problem =========================");

        runTests(
            "brute force",
            TwoSum::twoSumBruteForce,
            tests
        );

        runTests(
            "sort + binary search",
            (nums, target) -> twoSumBinarySearch(nums, target),
            tests
        );

        runTests(
            "two pointers",
            (nums, target) -> twoSumTwoPointers(nums, target),
            tests
        );

        runTests(
            "hash set",
            (nums, target) -> twoSumHashSet(nums, target),
            tests
        );
    }
}
