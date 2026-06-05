// #!/usr/bin/env jshell

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class SecondLargestElement {

    /**
     * Test case record
     */
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

    /**
     * Brute force approach: O(n log n) time, O(1) space
     * Sort the array and find the first element different from the largest
     */
    static int secondLargestElementBruteForce(int[] nums) {
        if (nums == null || nums.length == 0) {
            return -1;
        }

        int n = nums.length;
        int[] copy = nums.clone();  // Don't modify original array
        Arrays.sort(copy);
        
        for (int i = n - 2; i >= 0; i--) {
            if (copy[i] != copy[n - 1]) {
                return copy[i];
            }
        }

        return -1;
    }

    /**
     * Two-pass approach: O(n) time, O(1) space
     * First pass: find largest, second pass: find second largest
     */
    static int secondLargestElementTwoPass(int[] nums) {
        if (nums == null || nums.length == 0) {
            return -1;
        }

        int n = nums.length;
        int largest = Integer.MIN_VALUE;
        int secondLargest = Integer.MIN_VALUE;
        
        // First pass: find largest
        for (int i = 0; i < n; i++) {
            if (nums[i] > largest) {
                largest = nums[i];
            }
        }
        
        // Second pass: find second largest (distinct from largest)
        for (int i = 0; i < n; i++) {
            if (nums[i] > secondLargest && nums[i] != largest) {
                secondLargest = nums[i];
            }
        }

        return secondLargest == Integer.MIN_VALUE ? -1 : secondLargest;
    }

    /**
     * One-pass approach (optimal): O(n) time, O(1) space
     * Track largest and second largest in a single pass
     */
    static int secondLargestElementOnePass(int[] nums) {
        if (nums == null || nums.length == 0) {
            return -1;
        }

        int n = nums.length;
        int largest = Integer.MIN_VALUE;
        int secondLargest = Integer.MIN_VALUE;
        
        for (int i = 0; i < n; i++) {
            if (nums[i] > largest) {
                secondLargest = largest;
                largest = nums[i];
            } else if (nums[i] < largest && nums[i] > secondLargest) {
                secondLargest = nums[i];
            }
        }

        return secondLargest == Integer.MIN_VALUE ? -1 : secondLargest;
    }

    /**
     * Runs a named approach against all test cases
     */
    static void runTests(String name, java.util.function.Function<int[], Integer> func,
                         List<TestCase> tests) {
        System.out.println("========================= method: " + name + " =========================");
        int passed = 0;
        int failed = 0;
        
        for (TestCase test : tests) {
            int got = func.apply(test.nums);
            boolean testPassed = got == test.expected;
            
            if (testPassed) {
                passed++;
                System.out.printf("✓ Test %s (%s): passed%n", test.id, test.description);
            } else {
                failed++;
                System.out.printf("✗ Test %s (%s): FAILED%n", test.id, test.description);
                System.out.printf("  input=%s, got=%d, expected=%d%n",
                    Arrays.toString(test.nums),
                    got,
                    test.expected
                );
            }
        }
        
        System.out.printf("Results: %d passed, %d failed out of %d tests%n%n", passed, failed, tests.size());
    }

    public static void main(String[] args) {
        List<TestCase> positiveTests = new ArrayList<>();
        List<TestCase> negativeTests = new ArrayList<>();
        List<TestCase> edgeTests = new ArrayList<>();

        // ============ POSITIVE TESTS (valid second largest exists) ============
        
        // Test 1: Classic case
        positiveTests.add(new TestCase(
            "P1",
            new int[]{12, 35, 1, 10, 34, 1},
            34,
            "Classic case with duplicates"
        ));

        // Test 2: Simple array
        positiveTests.add(new TestCase(
            "P2",
            new int[]{1, 2, 3, 4, 5},
            4,
            "Sorted ascending array"
        ));

        // Test 3: Reverse sorted
        positiveTests.add(new TestCase(
            "P3",
            new int[]{5, 4, 3, 2, 1},
            4,
            "Sorted descending array"
        ));

        // Test 4: Unsorted with large gap
        positiveTests.add(new TestCase(
            "P4",
            new int[]{10, 5, 20, 15, 30},
            20,
            "Unsorted array with large gap"
        ));

        // Test 5: Two elements
        positiveTests.add(new TestCase(
            "P5",
            new int[]{1, 2},
            1,
            "Minimum valid array (2 elements)"
        ));

        // Test 6: Duplicates of largest
        positiveTests.add(new TestCase(
            "P6",
            new int[]{5, 5, 5, 4, 4},
            4,
            "Multiple duplicates of largest value"
        ));

        // ============ NEGATIVE TESTS (no distinct second largest) ============

        // Test 7: All same elements
        negativeTests.add(new TestCase(
            "N1",
            new int[]{5, 5, 5, 5},
            -1,
            "All elements are the same"
        ));

        // Test 8: Only one element
        negativeTests.add(new TestCase(
            "N2",
            new int[]{5},
            -1,
            "Single element (no second largest)"
        ));

        // Test 9: Empty array
        negativeTests.add(new TestCase(
            "N3",
            new int[]{},
            -1,
            "Empty array"
        ));

        // Test 10: Null input
        negativeTests.add(new TestCase(
            "N4",
            null,
            -1,
            "Null input"
        ));

        // ============ EDGE CASE TESTS ============

        // Test 11: Negative numbers
        edgeTests.add(new TestCase(
            "E1",
            new int[]{-1, -2, -3, -4},
            -2,
            "All negative numbers"
        ));

        // Test 12: Mix of positive and negative
        edgeTests.add(new TestCase(
            "E2",
            new int[]{-5, 0, 5, -10, 10},
            5,
            "Mix of positive and negative numbers"
        ));

        // Test 13: Large values
        edgeTests.add(new TestCase(
            "E3",
            new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE - 1, Integer.MAX_VALUE - 2},
            Integer.MAX_VALUE - 1,
            "Large integer values"
        ));

        // Test 14: Smallest and largest int
        edgeTests.add(new TestCase(
            "E4",
            new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE, 0},
            0,
            "Smallest and largest int values"
        ));

        // Test 15: Two largest are same
        edgeTests.add(new TestCase(
            "E5",
            new int[]{10, 10, 9, 8, 7},
            9,
            "Two largest values are identical"
        ));

        // Test 16: Second largest appears multiple times
        edgeTests.add(new TestCase(
            "E6",
            new int[]{10, 5, 5, 5, 1},
            5,
            "Second largest appears multiple times"
        ));

        // Combine all tests
        List<TestCase> allTests = new ArrayList<>();
        allTests.addAll(positiveTests);
        allTests.addAll(negativeTests);
        allTests.addAll(edgeTests);

        System.out.println("##########################################################");
        System.out.println("########## SECOND LARGEST ELEMENT PROBLEM ##################");
        System.out.println("##########################################################");
        System.out.println();

        runTests(
            "brute force (sorting)",
            SecondLargestElement::secondLargestElementBruteForce,
            allTests
        );

        runTests(
            "two pass",
            SecondLargestElement::secondLargestElementTwoPass,
            allTests
        );

        runTests(
            "one pass (optimal)",
            SecondLargestElement::secondLargestElementOnePass,
            allTests
        );
    }
}
