import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class MaxConsecutiveOnes {

    record Result(
        int[] result,
        int numberOfOnes
    ) {}

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
    static Result maxConsecutiveOnesBruteForce(int[] nums, int k) {
        if (nums == null || nums.length == 0) {
            return new Result(new int[0], 0);
        }

        int n = nums.length;
        int best = 0;

        for (int i = 0; i < n; i++) {
            int zeroCount = 0;
            for (int j = i; j < n; j++) {
                if (nums[j] == 0) {
                    zeroCount++;
                }

                if (zeroCount > k) {
                    break;
                }

                best = Math.max(best, j - i + 1);
            }
        }

        return new Result(nums.clone(), best);
    }

    /**
     * Sliding Window Solution
     *
     * Time: O(n)
     * Space: O(1)
     */
    static Result maxConsecutiveOnesSlidingWindow(int[] nums, int k) {
        if (nums == null || nums.length == 0) {
            return new Result(new int[0], 0);
        }

        int start = 0;
        int zeroCount = 0;
        int best = 0;

        for (int end = 0; end < nums.length; end++) {
            if (nums[end] == 0) {
                zeroCount++;
            }

            while (zeroCount > k) {
                if (nums[start] == 0) {
                    zeroCount--;
                }
                start++;
            }

            best = Math.max(best, end - start + 1);
        }

        return new Result(nums.clone(), best);
    }

    static void runTests(
        String algorithm,
        Function<TestCase, Result> method,
        List<TestCase> tests
    ) {
        System.out.println("==================================================");
        System.out.println(algorithm);
        System.out.println("==================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {
            try {
                Result actual = method.apply(test);

                if (actual.numberOfOnes() == test.expected) {
                    passed++;
                    System.out.printf("✓ %s (%s)%n", test.id, test.description);
                } else {
                    failed++;
                    System.out.printf("✗ %s (%s)%n", test.id, test.description);
                    System.out.printf("  nums     = %s%n", test.nums == null ? "null" : Arrays.toString(test.nums));
                    System.out.printf("  k        = %d%n", test.k);
                    System.out.printf("  expected = %d%n", test.expected);
                    System.out.printf("  actual   = %d%n", actual.numberOfOnes());
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
            new int[]{1, 1, 1, 0, 0, 0, 1, 1, 1, 1, 0},
            2,
            6,
            "Classic LeetCode example"
        ));

        tests.add(new TestCase(
            "S2",
            new int[]{0, 0, 1, 1, 1, 0, 0},
            0,
            3,
            "No flips allowed"
        ));

        tests.add(new TestCase(
            "S3",
            new int[]{1, 0, 1, 1, 0, 1},
            1,
            4,
            "One flip bridges the best window"
        ));

        /*
         * Single element
         */
        tests.add(new TestCase(
            "SE1",
            new int[]{1},
            0,
            1,
            "Single one"
        ));

        tests.add(new TestCase(
            "SE2",
            new int[]{0},
            0,
            0,
            "Single zero with no flips"
        ));

        tests.add(new TestCase(
            "SE3",
            new int[]{0},
            1,
            1,
            "Single zero with one flip"
        ));

        /*
         * Null / empty
         */
        tests.add(new TestCase(
            "E1",
            null,
            2,
            0,
            "Null array"
        ));

        tests.add(new TestCase(
            "E2",
            new int[]{},
            2,
            0,
            "Empty array"
        ));

        /*
         * All ones / all zeros
         */
        tests.add(new TestCase(
            "A1",
            new int[]{1, 1, 1, 1},
            0,
            4,
            "All ones"
        ));

        tests.add(new TestCase(
            "A2",
            new int[]{0, 0, 0, 0},
            2,
            2,
            "All zeros with limited flips"
        ));

        tests.add(new TestCase(
            "A3",
            new int[]{0, 0, 0, 0},
            4,
            4,
            "All zeros with enough flips"
        ));

        /*
         * More coverage
         */
        tests.add(new TestCase(
            "C1",
            new int[]{1, 0, 1, 0, 1, 0, 1},
            2,
            5,
            "Alternating values"
        ));

        tests.add(new TestCase(
            "C2",
            new int[]{1, 1, 0, 1, 1, 1, 0, 1, 1},
            1,
            6,
            "Best window in the middle"
        ));

        tests.add(new TestCase(
            "C3",
            new int[]{1, 0, 0, 1, 1, 0, 1, 1, 1, 0, 1},
            2,
            8,
            "Multiple valid windows"
        ));

        tests.add(new TestCase(
            "C4",
            new int[]{1, 0, 1, 1, 0, 1, 1, 0, 1},
            2,
            7,
            "Two flips across several blocks"
        ));

        /*
         * Edge cases
         */
        tests.add(new TestCase(
            "K1",
            new int[]{1, 0, 1, 1, 0, 1},
            10,
            6,
            "k larger than zero count"
        ));

        tests.add(new TestCase(
            "K2",
            new int[]{1, 0, 1, 1, 0, 1},
            100,
            6,
            "Very large k"
        ));

        System.out.println("############################################################");
        System.out.println("############ MAX CONSECUTIVE ONES PROBLEM ##################");
        System.out.println("############################################################");
        System.out.println();

        runTests(
            "Brute Force O(n^2)",
            test -> maxConsecutiveOnesBruteForce(test.nums, test.k),
            tests
        );

        runTests(
            "Sliding Window O(n)",
            test -> maxConsecutiveOnesSlidingWindow(test.nums, test.k),
            tests
        );
    }
}