import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class MaxProductSubArray {

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
     * Brute Force Solution
     *
     * Time: O(n^2)
     * Space: O(1)
     */
    static int maxProductSubArrayBruteForce(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        long maxProd = nums[0];
        for (int i = 0; i < nums.length; i++) {
            long product = 1;
            for (int j = i; j < nums.length; j++) {
                product *= nums[j];
                maxProd = Math.max(maxProd, product);
            }
        }

        return (int) maxProd;
    }

    /**
     * DP Solution: track both current max and current min.
     *
     * Time: O(n)
     * Space: O(1)
     */
    static int maxProductSubArrayTrackMinMax(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        long currMax = nums[0];
        long currMin = nums[0];
        long maxProd = nums[0];

        for (int i = 1; i < nums.length; i++) {
            long num = nums[i];
            long prevMax = currMax;
            long prevMin = currMin;

            currMax = Math.max(num, Math.max(num * prevMax, num * prevMin));
            currMin = Math.min(num, Math.min(num * prevMax, num * prevMin));
            maxProd = Math.max(maxProd, currMax);
        }

        return (int) maxProd;
    }

    /**
     * Bidirectional traversal solution.
     *
     * Time: O(n)
     * Space: O(1)
     */
    static int maxProductSubArrayBidirectionalTraversal(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        long maxProd = nums[0];
        long leftToRight = 1;
        long rightToLeft = 1;
        int n = nums.length;

        for (int i = 0; i < n; i++) {
            leftToRight *= nums[i];
            rightToLeft *= nums[n - 1 - i];

            maxProd = Math.max(maxProd, Math.max(leftToRight, rightToLeft));

            if (leftToRight == 0) {
                leftToRight = 1;
            }
            if (rightToLeft == 0) {
                rightToLeft = 1;
            }
        }

        return (int) maxProd;
    }

    static void runTests(
        String algorithm,
        Function<TestCase, Integer> method,
        List<TestCase> tests
    ) {
        System.out.println("==================================================");
        System.out.println(algorithm);
        System.out.println("==================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {
            try {
                int actual = method.apply(test);

                if (actual == test.expected) {
                    passed++;
                    System.out.printf("✓ %s (%s)%n", test.id, test.description);
                } else {
                    failed++;
                    System.out.printf("✗ %s (%s)%n", test.id, test.description);
                    System.out.printf("  nums      = %s%n",
                        test.nums == null ? "null" : Arrays.toString(test.nums));
                    System.out.printf("  expected  = %d%n", test.expected);
                    System.out.printf("  actual    = %d%n", actual);
                }
            } catch (Exception ex) {
                failed++;
                System.out.printf("✗ %s (%s)%n", test.id, test.description);
                System.out.printf("  exception = %s%n", ex.getMessage());
            }
        }

        System.out.printf(
            "%nResults: %d passed, %d failed, %d total%n%n",
            passed,
            failed,
            tests.size()
        );
    }

    public static void main(String[] args) {
        List<TestCase> tests = new ArrayList<>();

        /*
         * Standard examples
         */
        tests.add(new TestCase(
            "S1",
            new int[]{2, 3, -2, 4},
            6,
            "Classic example"
        ));

        tests.add(new TestCase(
            "S2",
            new int[]{-2, 0, -1},
            0,
            "Zero resets the product"
        ));

        tests.add(new TestCase(
            "S3",
            new int[]{-2, 3, -4},
            24,
            "Two negatives flip the sign"
        ));

        tests.add(new TestCase(
            "S4",
            new int[]{2, -5, -2, -4, 3},
            24,
            "Best product comes from a middle subarray"
        ));

        tests.add(new TestCase(
            "S5",
            new int[]{1, 2, 3, 4},
            24,
            "All positive numbers"
        ));

        /*
         * Single element
         */
        tests.add(new TestCase(
            "SE1",
            new int[]{7},
            7,
            "Single positive element"
        ));

        tests.add(new TestCase(
            "SE2",
            new int[]{-7},
            -7,
            "Single negative element"
        ));

        tests.add(new TestCase(
            "SE3",
            new int[]{0},
            0,
            "Single zero element"
        ));

        /*
         * Null / Empty
         */
        tests.add(new TestCase(
            "E1",
            null,
            0,
            "Null array"
        ));

        tests.add(new TestCase(
            "E2",
            new int[]{},
            0,
            "Empty array"
        ));

        /*
         * Zero-heavy cases
         */
        tests.add(new TestCase(
            "Z1",
            new int[]{0, 2},
            2,
            "Leading zero"
        ));

        tests.add(new TestCase(
            "Z2",
            new int[]{2, 0},
            2,
            "Trailing zero"
        ));

        tests.add(new TestCase(
            "Z3",
            new int[]{0, -2, -3, 0, -2, -40},
            80,
            "Multiple zero-separated segments"
        ));

        tests.add(new TestCase(
            "Z4",
            new int[]{0, 0, 0},
            0,
            "All zeros"
        ));

        /*
         * Negative parity cases
         */
        tests.add(new TestCase(
            "N1",
            new int[]{-1, -2, -9, -6},
            108,
            "Even count of negatives"
        ));

        tests.add(new TestCase(
            "N2",
            new int[]{-1, -2, -3},
            6,
            "Odd count of negatives"
        ));

        tests.add(new TestCase(
            "N3",
            new int[]{-1, -3, -10, 0, 60},
            60,
            "Maximum product at the end"
        ));

        tests.add(new TestCase(
            "N4",
            new int[]{1, -2, -3, 0, 7, -8, -2},
            112,
            "Multiple sign changes"
        ));

        /*
         * Edge values
         */
        tests.add(new TestCase(
            "L1",
            new int[]{Integer.MAX_VALUE},
            Integer.MAX_VALUE,
            "Maximum int single element"
        ));

        tests.add(new TestCase(
            "L2",
            new int[]{Integer.MIN_VALUE},
            Integer.MIN_VALUE,
            "Minimum int single element"
        ));

        tests.add(new TestCase(
            "L3",
            new int[]{46340, 46340},
            2147395600,
            "Large but safe multiplication"
        ));

        tests.add(new TestCase(
            "L4",
            new int[]{Integer.MAX_VALUE - 1, 1},
            Integer.MAX_VALUE - 1,
            "Near max int without overflow"
        ));

        System.out.println("############################################################");
        System.out.println("############ MAX PRODUCT SUBARRAY PROBLEM ##################");
        System.out.println("############################################################");
        System.out.println();

        runTests(
            "Brute Force O(n^2)",
            test -> maxProductSubArrayBruteForce(test.nums),
            tests
        );

        runTests(
            "DP Track Min/Max O(n)",
            test -> maxProductSubArrayTrackMinMax(test.nums),
            tests
        );

        runTests(
            "Bidirectional Traversal O(n)",
            test -> maxProductSubArrayBidirectionalTraversal(test.nums),
            tests
        );
    }
}