import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class EqualSumSegments {

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

    /**
     * Brute Force Solution
     *
     * Returns the first pair of cut indices [i, j] such that:
     * sum(0..i) == sum(i+1..j) == sum(j+1..n-1)
     *
     * Time: O(n^3)
     * Space: O(1)
     */
    static int[] equalSumSegmentsBruteForce(int[] nums) {
        if (nums == null || nums.length < 3) {
            return new int[]{-1, -1};
        }

        int n = nums.length;

        for (int i = 0; i < n - 2; i++) {
            for (int j = i + 1; j < n - 1; j++) {
                long sum1 = findSum(nums, 0, i);
                long sum2 = findSum(nums, i + 1, j);
                long sum3 = findSum(nums, j + 1, n - 1);

                if (sum1 == sum2 && sum2 == sum3) {
                    return new int[]{i, j};
                }
            }
        }

        return new int[]{-1, -1};
    }

    /**
     * Prefix-sum / greedy scan.
     *
     * Time: O(n)
     * Space: O(1)
     */
    static int[] equalSumSegmentsFirstTwo(int[] nums) {
        if (nums == null || nums.length < 3) {
            return new int[]{-1, -1};
        }

        long total = 0;
        for (int num : nums) {
            total += num;
        }

        if (total % 3 != 0) {
            return new int[]{-1, -1};
        }

        long target = total / 3;
        long prefix = 0;
        int firstCut = -1;
        int secondCut = -1;

        for (int i = 0; i < nums.length; i++) {
            prefix += nums[i];

            if (prefix == target && firstCut == -1) {
                firstCut = i;
            } else if (prefix == 2 * target && firstCut != -1) {
                secondCut = i;
                if (secondCut < nums.length - 1) {
                    return new int[]{firstCut, secondCut};
                }
            }
        }

        return new int[]{-1, -1};
    }

    static long findSum(int[] nums, int start, int endInclusive) {
        long sum = 0;
        for (int i = start; i <= endInclusive; i++) {
            sum += nums[i];
        }
        return sum;
    }

    static boolean samePair(int[] a, int[] b) {
        return Arrays.equals(a, b);
    }

    static String formatArray(int[] arr) {
        return arr == null ? "null" : Arrays.toString(arr);
    }

    static void runTests(
        String algorithm,
        Function<TestCase, int[]> method,
        List<TestCase> tests
    ) {
        System.out.println("==================================================");
        System.out.println(algorithm);
        System.out.println("==================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {
            try {
                int[] actual = method.apply(test);

                if (samePair(actual, test.expected)) {
                    passed++;
                    System.out.printf("✓ %s (%s)%n", test.id, test.description);
                } else {
                    failed++;
                    System.out.printf("✗ %s (%s)%n", test.id, test.description);
                    System.out.printf("  nums      = %s%n", formatArray(test.nums));
                    System.out.printf("  expected  = %s%n", formatArray(test.expected));
                    System.out.printf("  actual    = %s%n", formatArray(actual));
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
            new int[]{1, 2, 3, 0, 3},
            new int[]{2, 3},
            "Classic example with a valid split"
        ));

        tests.add(new TestCase(
            "S2",
            new int[]{0, 2, 1, -6, 6, -7, 9, 1, 2, 0, 1},
            new int[]{2, 7},
            "LeetCode-style example"
        ));

        tests.add(new TestCase(
            "S3",
            new int[]{0, 0, 0, 0},
            new int[]{0, 1},
            "All zeros, earliest valid split"
        ));

        /*
         * Null / small arrays
         */
        tests.add(new TestCase(
            "E1",
            null,
            new int[]{-1, -1},
            "Null array"
        ));

        tests.add(new TestCase(
            "E2",
            new int[]{},
            new int[]{-1, -1},
            "Empty array"
        ));

        tests.add(new TestCase(
            "E3",
            new int[]{1},
            new int[]{-1, -1},
            "Single element"
        ));

        tests.add(new TestCase(
            "E4",
            new int[]{1, 2},
            new int[]{-1, -1},
            "Two elements"
        ));

        /*
         * No valid partition
         */
        tests.add(new TestCase(
            "N1",
            new int[]{1, 2, 4},
            new int[]{-1, -1},
            "Total sum not divisible by 3"
        ));

        tests.add(new TestCase(
            "N2",
            new int[]{1, 1, 1, 1, 1},
            new int[]{-1, -1},
            "No equal-sum partition exists"
        ));

        tests.add(new TestCase(
            "N3",
            new int[]{3, 3, 6, 5, -2, 2, 5, 1, -9, 4},
            new int[]{-1, -1},
            "Close but invalid"
        ));

        /*
         * Multiple possible answers, verify one valid earliest pair
         */
        tests.add(new TestCase(
            "M1",
            new int[]{2, 2, 2, 2, 2, 2},
            new int[]{1, 3},
            "Repeated equal values"
        ));

        tests.add(new TestCase(
            "M2",
            new int[]{3, 3, 3},
            new int[]{0, 1},
            "Minimum valid length"
        ));

        tests.add(new TestCase(
            "M3",
            new int[]{1, -1, 1, -1, 1, -1},
            new int[]{1, 3},
            "Alternating values with zero total"
        ));

        /*
         * Edge values
         */
        tests.add(new TestCase(
            "L1",
            new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE},
            new int[]{-1, -1},
            "Large values without valid partition"
        ));

        tests.add(new TestCase(
            "L2",
            new int[]{Integer.MIN_VALUE, 0, Integer.MIN_VALUE, 0, Integer.MIN_VALUE, 0},
            new int[]{1, 3},
            "Large negative values with zeros"
        ));

        System.out.println("############################################################");
        System.out.println("############ EQUAL SUM SEGMENTS PROBLEM ####################");
        System.out.println("############################################################");
        System.out.println();

        runTests(
            "Brute Force O(n^3)",
            test -> equalSumSegmentsBruteForce(test.nums),
            tests
        );

        runTests(
            "Prefix Scan O(n)",
            test -> equalSumSegmentsFirstTwo(test.nums),
            tests
        );
    }
}
