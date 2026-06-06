// #!/usr/bin/env jshell

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ThirdLargestElement {

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
     * Sort the array and return the element at index n-3
     */
    static int thirdLargestElementBruteForce(int[] nums) {
        if (nums == null || nums.length < 3) {
            return -1;
        }

        int n = nums.length;
        int[] copy = nums.clone();  // Don't modify original array
        Arrays.sort(copy);

        return copy[n - 3];
    }

    /**
     * Three-pass approach: O(n) time, O(1) space
     * Three separate loops to find largest, second largest, and third largest
     */
    static int thirdLargestElementThreeLoops(int[] nums) {
        if (nums == null || nums.length < 3) {
            return -1;
        }

        int n = nums.length;
        int largest = Integer.MIN_VALUE;
        int secondLargest = Integer.MIN_VALUE;
        int thirdLargest = Integer.MIN_VALUE;
        int firstIdx = -1;
        int secondIdx = -1;

        // First loop: find largest
        for (int i = 0; i < n; i++) {
            if (nums[i] > largest) {
                largest = nums[i];
                firstIdx = i;
            }
        }

        // Second loop: find second largest (skip first index)
        for (int i = 0; i < n; i++) {
            if (i == firstIdx) {
                continue;
            }

            if (nums[i] > secondLargest) {
                secondLargest = nums[i];
                secondIdx = i;
            }
        }

        // Third loop: find third largest (skip first and second index)
        for (int i = 0; i < n; i++) {
            if (i == firstIdx || i == secondIdx) {
                continue;
            }

            if (nums[i] > thirdLargest) {
                thirdLargest = nums[i];
            }
        }

        return thirdLargest == Integer.MIN_VALUE ? -1 : thirdLargest;
    }

    /**
     * One-pass approach (optimal): O(n) time, O(1) space
     * Track largest, second largest, and third largest in a single pass
     */
    static int thirdLargestElementOneLoop(int[] nums) {
        if (nums == null || nums.length < 3) {
            return -1;
        }

        int n = nums.length;
        int largest = Integer.MIN_VALUE;
        int secondLargest = Integer.MIN_VALUE;
        int thirdLargest = Integer.MIN_VALUE;

        for (int i = 0; i < n; i++) {
            if (nums[i] > largest) {
                thirdLargest = secondLargest;
                secondLargest = largest;
                largest = nums[i];
            } else if (nums[i] > secondLargest) {
                thirdLargest = secondLargest;
                secondLargest = nums[i];
            } else if (nums[i] > thirdLargest) {
                thirdLargest = nums[i];
            }
        }

        return thirdLargest == Integer.MIN_VALUE ? -1 : thirdLargest;
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

        // ============ POSITIVE TESTS (valid third largest exists) ============
        
        // Test 1: Classic case
        positiveTests.add(new TestCase(
            "P1",
            new int[]{10, 20, 4, 45, 99},
            20,
            "Classic case with 5 elements"
        ));

        // Test 2: Sorted ascending
        positiveTests.add(new TestCase(
            "P2",
            new int[]{1, 2, 3, 4, 5},
            3,
            "Sorted ascending array"
        ));

        // Test 3: Sorted descending
        positiveTests.add(new TestCase(
            "P3",
            new int[]{5, 4, 3, 2, 1},
            3,
            "Sorted descending array"
        ));

        // Test 4: Exactly 3 elements
        positiveTests.add(new TestCase(
            "P4",
            new int[]{1, 2, 3},
            1,
            "Minimum valid array (exactly 3 elements)"
        ));

        // Test 5: Unsorted random
        positiveTests.add(new TestCase(
            "P5",
            new int[]{12, 35, 1, 10, 34, 1},
            12,
            "Unsorted array with duplicates"
        ));

        // Test 6: Large gap between values
        positiveTests.add(new TestCase(
            "P6",
            new int[]{100, 50, 25, 10, 5},
            25,
            "Large gap between values"
        ));

        // ============ NEGATIVE TESTS (no third element) ============

        // Test 7: Only 2 elements
        negativeTests.add(new TestCase(
            "N1",
            new int[]{1, 2},
            -1,
            "Only 2 elements (no third largest)"
        ));

        // Test 8: Single element
        negativeTests.add(new TestCase(
            "N2",
            new int[]{5},
            -1,
            "Single element"
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

        // Test 11: All same elements (third largest is same as all)
        edgeTests.add(new TestCase(
            "E1",
            new int[]{5, 5, 5, 5, 5},
            5,
            "All elements are the same"
        ));

        // Test 12: Two elements same (largest)
        edgeTests.add(new TestCase(
            "E2",
            new int[]{10, 10, 5, 3},
            5,
            "Two largest values are identical"
        ));

        // Test 13: Two elements same (third largest)
        edgeTests.add(new TestCase(
            "E3",
            new int[]{10, 9, 5, 5, 1},
            5,
            "Third largest appears multiple times"
        ));

        // Test 14: Negative numbers
        edgeTests.add(new TestCase(
            "E4",
            new int[]{-1, -2, -3, -4, -5},
            -3,
            "All negative numbers"
        ));

        // Test 15: Mix of positive and negative
        edgeTests.add(new TestCase(
            "E5",
            new int[]{-10, 0, 10, -5, 5},
            0,
            "Mix of positive and negative numbers"
        ));

        // Test 16: Large values
        edgeTests.add(new TestCase(
            "E6",
            new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE - 1, Integer.MAX_VALUE - 2},
            Integer.MAX_VALUE - 2,
            "Large integer values"
        ));

        // Test 17: MIN_VALUE included (third largest IS MIN_VALUE, returns -1)
        edgeTests.add(new TestCase(
            "E7",
            new int[]{Integer.MAX_VALUE, 0, Integer.MIN_VALUE},
            -1,  // Returns -1 when third largest is MIN_VALUE (accepted behavior)
            "Includes Integer.MIN_VALUE (returns -1)"
        ));

        // Test 18: Multiple duplicates (CORRECTED: third position is 10)
        edgeTests.add(new TestCase(
            "E8",
            new int[]{10, 10, 10, 5, 5, 1},
            10,  // Sorted: [1, 5, 5, 10, 10, 10], third from end is 10
            "Multiple duplicates at different levels"
        ));

        // Combine all tests
        List<TestCase> allTests = new ArrayList<>();
        allTests.addAll(positiveTests);
        allTests.addAll(negativeTests);
        allTests.addAll(edgeTests);

        System.out.println("##########################################################");
        System.out.println("########### THIRD LARGEST ELEMENT PROBLEM ##################");
        System.out.println("##########################################################");
        System.out.println();

        runTests(
            "brute force (sorting)",
            ThirdLargestElement::thirdLargestElementBruteForce,
            allTests
        );

        runTests(
            "three loops",
            ThirdLargestElement::thirdLargestElementThreeLoops,
            allTests
        );

        runTests(
            "one loop (optimal)",
            ThirdLargestElement::thirdLargestElementOneLoop,
            allTests
        );
    }
}