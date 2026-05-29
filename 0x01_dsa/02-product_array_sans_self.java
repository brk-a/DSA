// #!/usr/bin/env jschool

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.Objects;

public class ProductArraySansSelf {

    // Test case record
    static class TestCase {
        final String id;
        final int[] nums;
        final int[] expected;

        TestCase(String id, int[] nums, int[] expected) {
            this.id = id;
            this.nums = nums.clone();
            this.expected = expected.clone();
        }
    }

    /**
     * Brute force approach: O(n²) time, O(1) extra space (excluding result)
     * For each index i, multiply all elements except nums[i]
     */
    static int[] productArraySansSelfBruteForce(int[] nums) {
        int n = nums.length;
        int[] result = new int[n];

        // Initialise result array to 1 (multiplicative identity)
        Arrays.fill(result, 1);

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                if (i != j) {
                    result[i] *= nums[j];
                }
            }
        }

        return result;
    }

    /**
     * Prefix-Suffix approach: O(n) time, O(n) space
     * Compute prefix products and suffix products, then multiply them
     */
    static int[] productArraySansSelfPrefixSuffix(int[] nums) {
        int n = nums.length;
        int[] prefixProduct = new int[n];
        int[] suffixProduct = new int[n];
        int[] result = new int[n];

        prefixProduct[0] = 1;
        for (int i = 1; i < n; i++) {
            prefixProduct[i] = nums[i - 1] * prefixProduct[i - 1];
        }

        suffixProduct[n - 1] = 1;
        for (int j = n - 2; j >= 0; j--) {
            suffixProduct[j] = nums[j + 1] * suffixProduct[j + 1];
        }

        for (int i = 0; i < n; i++) {
            result[i] = prefixProduct[i] * suffixProduct[i];
        }

        return result;
    }

    /**
     * Product-with-Zeroes handling approach: O(n) time, O(1) extra space (excluding result)
     * Handles arrays with zeroes correctly
     */
    static int[] productArraySansSelfProductArray(int[] nums) {
        int zeroes = 0;
        int idx = -1;
        int product = 1;
        int n = nums.length;

        // First pass: count zeroes and compute product of non-zero elements
        for (int i = 0; i < n; i++) {
            if (nums[i] == 0) {
                zeroes++;
                idx = i;
            } else {
                product *= nums[i];
            }
        }

        int[] result = new int[n];
        Arrays.fill(result, 0);

        if (zeroes == 0) {
            // No zeros: divide total product by each element
            for (int i = 0; i < n; i++) {
                result[i] = product / nums[i];
            }
        } else if (zeroes == 1) {
            // Exactly one zero: only the zero position gets the product
            result[idx] = product;
        }
        // If zeroes > 1, all results remain 0 (already filled)

        return result;
    }

    /**
     * Runs a named approach against all test cases
     */
    static void runTests(String name, java.util.function.Function<int[], int[]> func,
                         List<TestCase> tests) {
        System.out.println("========================= method: " + name + " =========================");
        for (TestCase test : tests) {
            int[] got = func.apply(test.nums);
            boolean passed = Arrays.equals(got, test.expected);
            System.out.printf(
                "Test %s: nums=%s, got=%s, expected=%s, passed=%b%n",
                test.id,
                Arrays.toString(test.nums),
                Arrays.toString(got),
                Arrays.toString(test.expected),
                passed
            );
        }
    }

    public static void main(String[] args) {
        List<TestCase> tests = new ArrayList<>();

        // Test 1: Classic case with no zeroes
        tests.add(new TestCase(
            "1",
            new int[]{1, 2, 3, 4},
            new int[]{24, 12, 8, 6}
        ));

        // Test 2: Array with one zero
        tests.add(new TestCase(
            "2",
            new int[]{1, 2, 0, 4},
            new int[]{0, 0, 8, 0}
        ));

        // Test 3: Array with two zeroes
        tests.add(new TestCase(
            "3",
            new int[]{0, 2, 0, 4},
            new int[]{0, 0, 0, 0}
        ));

        // Test 4: Single element
        tests.add(new TestCase(
            "4",
            new int[]{5},
            new int[]{1}  // product of empty set = 1
        ));

        // Test 5: Two elements
        tests.add(new TestCase(
            "5",
            new int[]{1, 2},
            new int[]{2, 1}
        ));

        // Test 6: Negative numbers
        tests.add(new TestCase(
            "6",
            new int[]{-1, 2, -3, 4},
            new int[]{-24, 12, -8, 6}
        ));

        // Test 7: All same values
        tests.add(new TestCase(
            "7",
            new int[]{2, 2, 2, 2},
            new int[]{8, 8, 8, 8}
        ));

        // Test 8: Empty array
        tests.add(new TestCase(
            "8",
            new int[]{},
            new int[]{}
        ));

        System.out.println("========================= Product of Array Except Self Problem =========================");

        runTests(
            "brute force",
            ProductArraySansSelf::productArraySansSelfBruteForce,
            tests
        );

        runTests(
            "prefix-suffix",
            ProductArraySansSelf::productArraySansSelfPrefixSuffix,
            tests
        );

        runTests(
            "product with zeroes handling",
            ProductArraySansSelf::productArraySansSelfProductArray,
            tests
        );
    }
}