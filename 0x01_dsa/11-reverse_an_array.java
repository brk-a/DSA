// #!/usr/bin/env jshell

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class ReverseAnArray {

    /* 
     * All methods clone the input (or create a new array) and return the reversed clone.
     * They do NOT mutate the caller's input array/list.
     * 
     * Behaviour for special inputs:
     * - null input -> returns null
     * - empty input -> returns new int[0]
     */

    /**
     * Brute force approach: create a new array and fill it in reversed order.
     * Time: O(n), Space: O(n)
     */
    static int[] reverseBruteForce(int[] nums) {
        if (nums == null) {
            return null;
        }
        if (nums.length == 0) {
            return new int[0];
        }

        int n = nums.length;
        int[] result = new int[n];
        for (int i = 0; i < n; i++) {
            result[i] = nums[n - i - 1];
        }
        return result;
    }

    /**
     * Two-pointers approach: clone array, then swap elements from both ends.
     * Uses a temporary variable for swapping (clear and safe).
     * Time: O(n), Space: O(n) for the clone
     */
    static int[] reverseTwoPointers(int[] nums) {
        if (nums == null) {
            return null;
        }
        if (nums.length == 0) {
            return new int[0];
        }

        int n = nums.length;
        int[] result = nums.clone();
        int left = 0;
        int right = n - 1;
        while (left < right) {
            int tmp = result[left];
            result[left] = result[right];
            result[right] = tmp;
            left++;
            right--;
        }
        return result;
    }

    /**
     * One-pointer approach with XOR swap: clone array, then swap using XOR trick.
     * This demonstrates XOR swapping but is less readable than temp-variable swap.
     * Time: O(n), Space: O(n) for the clone
     */
    static int[] reverseOnePointerXor(int[] nums) {
        if (nums == null) {
            return null;
        }
        if (nums.length == 0) {
            return new int[0];
        }

        int n = nums.length;
        int[] result = nums.clone();
        for (int i = 0; i < n / 2; i++) {
            int j = n - i - 1;
            if (i != j) { // XOR trick requires distinct indices
                result[i] = result[i] ^ result[j];
                result[j] = result[i] ^ result[j];
                result[i] = result[i] ^ result[j];
            }
        }
        return result;
    }

    /**
     * Using Collections.reverse: accepts a List<Integer>, returns a new int[].
     * Does NOT modify the caller's list (clones it first).
     * Time: O(n), Space: O(n) for the list clone and result array
     */
    static int[] reverseUsingCollections(List<Integer> nums) {
        if (nums == null) {
            return null;
        }
        if (nums.isEmpty()) {
            return new int[0];
        }

        // Clone list to avoid mutating caller's list
        List<Integer> copy = new ArrayList<>(nums);
        Collections.reverse(copy);
        int[] result = new int[copy.size()];
        for (int i = 0; i < copy.size(); i++) {
            result[i] = copy.get(i);
        }
        return result;
    }

    /* 
     * TestCase expects: id, input array (or null), expected array (or null), description.
     * Comparison of arrays uses Arrays.equals; null compared to null passes.
     */
    static class TestCase {
        final String id;
        final int[] nums;
        final int[] expected;
        final String description;

        TestCase(String id, int[] nums, int[] expected, String description) {
            this.id = id;
            this.nums = nums;
            this.expected = expected;
            this.description = description;
        }
    }

    static void runTests(String name, Function<int[], int[]> func, List<TestCase> tests) {
        System.out.println("========================= method: " + name + " =========================");
        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {
            int[] got = func.apply(test.nums);
            boolean testPassed = Arrays.equals(got, test.expected);

            if (testPassed) {
                passed++;
                System.out.printf(" Test %s (%s): passed%n", test.id, test.description);
            } else {
                failed++;
                System.out.printf(" Test %s (%s): FAILED%n", test.id, test.description);
                System.out.printf("  input=%s%n", test.nums == null ? "null" : Arrays.toString(test.nums));
                System.out.printf("  got=%s%n", got == null ? "null" : Arrays.toString(got));
                System.out.printf("  expected=%s%n", test.expected == null ? "null" : Arrays.toString(test.expected));
            }
        }

        System.out.printf("Results: %d passed, %d failed out of %d tests%n%n", passed, failed, tests.size());
    }

    public static void main(String[] args) {
        List<TestCase> positiveTests = new ArrayList<>();
        List<TestCase> negativeTests = new ArrayList<>();
        List<TestCase> edgeTests = new ArrayList<>();

        // ============ POSITIVE TESTS ============
        positiveTests.add(new TestCase(
            "P1",
            new int[]{1, 2, 3},
            new int[]{3, 2, 1},
            "Simple 3-element array"
        ));

        positiveTests.add(new TestCase(
            "P2",
            new int[]{10, 20, 30, 40},
            new int[]{40, 30, 20, 10},
            "Even-length array"
        ));

        positiveTests.add(new TestCase(
            "P3",
            new int[]{1, 2, 3, 4, 5},
            new int[]{5, 4, 3, 2, 1},
            "Odd-length array"
        ));

        positiveTests.add(new TestCase(
            "P4",
            new int[]{5, 1, 9, 3, 7},
            new int[]{7, 3, 9, 1, 5},
            "Unsorted array"
        ));

        // ============ NEGATIVE TESTS ============
        negativeTests.add(new TestCase(
            "N1",
            null,
            null,
            "Null input"
        ));

        negativeTests.add(new TestCase(
            "N2",
            new int[]{},
            new int[]{},
            "Empty array"
        ));

        negativeTests.add(new TestCase(
            "N3",
            new int[]{42},
            new int[]{42},
            "Single element"
        ));

        // ============ EDGE CASE TESTS ============
        edgeTests.add(new TestCase(
            "E1",
            new int[]{5, 5, 5, 5},
            new int[]{5, 5, 5, 5},
            "All equal elements"
        ));

        edgeTests.add(new TestCase(
            "E2",
            new int[]{Integer.MAX_VALUE, Integer.MIN_VALUE, 0},
            new int[]{0, Integer.MIN_VALUE, Integer.MAX_VALUE},
            "Contains MIN_VALUE and MAX_VALUE"
        ));

        edgeTests.add(new TestCase(
            "E3",
            new int[]{-1, -2, -3, -4},
            new int[]{-4, -3, -2, -1},
            "All negative numbers"
        ));

        edgeTests.add(new TestCase(
            "E4",
            new int[]{1, 1, 2, 1, 1},
            new int[]{1, 1, 2, 1, 1},
            "Array with many duplicates"
        ));

        // Combine all tests
        List<TestCase> allTests = new ArrayList<>();
        allTests.addAll(positiveTests);
        allTests.addAll(negativeTests);
        allTests.addAll(edgeTests);

        System.out.println("##########################################################");
        System.out.println("########### REVERSE AN ARRAY PROBLEM #####################");
        System.out.println("##########################################################");
        System.out.println();

        // Run tests for int[] methods
        runTests("brute force", ReverseAnArray::reverseBruteForce, allTests);
        runTests("two pointers (temp swap)", ReverseAnArray::reverseTwoPointers, allTests);
        runTests("one pointer (XOR swap)", ReverseAnArray::reverseOnePointerXor, allTests);

        // Run tests for Collections-based method (converts int[] to List<Integer>)
        System.out.println("========================= method: reverseUsingCollections =========================");
        int passed = 0;
        int failed = 0;
        for (TestCase test : allTests) {
            List<Integer> inputList = test.nums == null ? null : toList(test.nums);
            int[] got = reverseUsingCollections(inputList);
            boolean testPassed = Arrays.equals(got, test.expected);
            if (testPassed) {
                passed++;
                System.out.printf(" Test %s: passed%n", test.id);
            } else {
                failed++;
                System.out.printf(" Test %s: FAILED%n", test.id);
                System.out.printf("  input=%s%n", inputList == null ? "null" : inputList.toString());
                System.out.printf("  got=%s%n", got == null ? "null" : Arrays.toString(got));
                System.out.printf("  expected=%s%n", test.expected == null ? "null" : Arrays.toString(test.expected));
            }
        }
        System.out.printf("Results: %d passed, %d failed out of %d tests%n%n", passed, failed, allTests.size());
    }

    // Helper to convert int[] to List<Integer>
    static List<Integer> toList(int[] arr) {
        List<Integer> list = new ArrayList<>();
        for (int v : arr) {
            list.add(v);
        }
        return list;
    }
}