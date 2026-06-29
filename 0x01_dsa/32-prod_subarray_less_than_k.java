import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class ProductSubarrayLessThanK {

    static class TestCase {
        final String id;
        final int[] nums;
        final int k;
        final int expected;
        final String description;

        TestCase(String id, int[] nums, int k, int expected, String description) {
            this.id = id;
            this.nums = nums;
            this.k = k;
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
    static int productSubarrayLessThanKBruteForce(int[] nums, int k) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        if (k <= 1) {
            return 0;
        }

        int n = nums.length;
        int count = 0;

        for (int i = 0; i < n; i++) {
            long product = 1;
            for (int j = i; j < n; j++) {
                product *= nums[j];
                if (product < k) {
                    count++;
                } else {
                    break;
                }
            }
        }

        return count;
    }

    /**
     * Sliding Window Solution
     *
     * Assumes nums contains positive integers.
     *
     * Time: O(n)
     * Space: O(1)
     */
    static int productSubarrayLessThanKSlidingWindow(int[] nums, int k) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        if (k <= 1) {
            return 0;
        }

        int start = 0;
        long product = 1;
        int result = 0;

        for (int end = 0; end < nums.length; end++) {
            product *= nums[end];

            while (start <= end && product >= k) {
                product /= nums[start];
                start++;
            }

            result += end - start + 1;
        }

        return result;
    }

    /**
     * Convenience overload for ArrayList<Integer>.
     */
    static int productSubarrayLessThanKSlidingWindow(ArrayList<Integer> nums, int k) {
        if (nums == null || nums.isEmpty()) {
            return 0;
        }

        if (k <= 1) {
            return 0;
        }

        int start = 0;
        long product = 1;
        int result = 0;

        for (int end = 0; end < nums.size(); end++) {
            product *= nums.get(end);

            while (start <= end && product >= k) {
                product /= nums.get(start);
                start++;
            }

            result += end - start + 1;
        }

        return result;
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
                    System.out.printf("  nums      = %s%n", test.nums == null ? "null" : Arrays.toString(test.nums));
                    System.out.printf("  k         = %d%n", test.k);
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

    public static void main(String[] args) {
        List<TestCase> tests = new ArrayList<>();

        /*
         * Standard examples
         */
        tests.add(new TestCase(
            "S1",
            new int[]{10, 5, 2, 6},
            100,
            8,
            "Classic example"
        ));

        tests.add(new TestCase(
            "S2",
            new int[]{1, 2, 3},
            0,
            0,
            "k is zero"
        ));

        tests.add(new TestCase(
            "S3",
            new int[]{1, 1, 1},
            2,
            6,
            "All subarrays are valid"
        ));

        tests.add(new TestCase(
            "S4",
            new int[]{2, 5, 3, 10},
            30,
            6,
            "Mixed products"
        ));

        /*
         * Single element
         */
        tests.add(new TestCase(
            "SE1",
            new int[]{5},
            6,
            1,
            "Single element less than k"
        ));

        tests.add(new TestCase(
            "SE2",
            new int[]{5},
            5,
            0,
            "Single element equal to k"
        ));

        tests.add(new TestCase(
            "SE3",
            new int[]{1},
            1,
            0,
            "Single element with k = 1"
        ));

        /*
         * Null / Empty
         */
        tests.add(new TestCase(
            "E1",
            null,
            10,
            0,
            "Null array"
        ));

        tests.add(new TestCase(
            "E2",
            new int[]{},
            10,
            0,
            "Empty array"
        ));

        /*
         * k edge cases
         */
        tests.add(new TestCase(
            "K1",
            new int[]{1, 2, 3},
            1,
            0,
            "k = 1 with positive numbers"
        ));

        tests.add(new TestCase(
            "K2",
            new int[]{1, 2, 3},
            -1,
            0,
            "Negative k"
        ));

        /*
         * Zero-containing cases
         */
        tests.add(new TestCase(
            "Z1",
            new int[]{0, 1, 2},
            3,
            6,
            "Zero makes every subarray starting there valid"
        ));

        tests.add(new TestCase(
            "Z2",
            new int[]{1, 0, 2},
            3,
            6,
            "Zero in the middle"
        ));

        tests.add(new TestCase(
            "Z3",
            new int[]{0, 0, 0},
            1,
            0,
            "k = 1 with zeros"
        ));

        /*
         * More coverage
         */
        tests.add(new TestCase(
            "C1",
            new int[]{2, 2, 2},
            5,
            5,
            "Repeated values"
        ));

        tests.add(new TestCase(
            "C2",
            new int[]{3, 4, 7, 2, 1, 6},
            50,
            15,
            "Longer positive array"
        ));

        tests.add(new TestCase(
            "C3",
            new int[]{100, 1, 1},
            100,
            3,
            "Exact threshold exclusion"
        ));

        System.out.println("############################################################");
        System.out.println("###### PRODUCT SUBARRAY LESS THAN K PROBLEM ################");
        System.out.println("############################################################");
        System.out.println();

        runTests(
            "Brute Force O(n^2)",
            test -> productSubarrayLessThanKBruteForce(test.nums, test.k),
            tests
        );

        runTests(
            "Sliding Window O(n)",
            test -> productSubarrayLessThanKSlidingWindow(test.nums, test.k),
            tests
        );
    }
}