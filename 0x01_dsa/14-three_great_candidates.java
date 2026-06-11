// #!/usr/bin/env jshell

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class MaximumProductOfThreeNumbers {

    /*
     * - null input -> Integer.MIN_VALUE
     * - length < 3 -> Integer.MIN_VALUE
     *
     * Return:
     * Maximum product obtainable from any 3 elements.
     */

    static int maxProductBruteForce(int[] nums) {

        if (nums == null || nums.length < 3) {
            return Integer.MIN_VALUE;
        }

        int n = nums.length;
        long maxProduct = Long.MIN_VALUE;

        for (int i = 0; i < n - 2; i++) {

            for (int j = i + 1; j < n - 1; j++) {

                for (int k = j + 1; k < n; k++) {

                    long product =
                            (long) nums[i]
                                    * nums[j]
                                    * nums[k];

                    maxProduct =
                            Math.max(maxProduct, product);
                }
            }
        }

        return (int) maxProduct;
    }

    static int maxProductSorting(int[] nums) {

        if (nums == null || nums.length < 3) {
            return Integer.MIN_VALUE;
        }

        int[] sorted = nums.clone();

        Arrays.sort(sorted);

        int n = sorted.length;

        long candidate1 =
                (long) sorted[0]
                        * sorted[1]
                        * sorted[n - 1];

        long candidate2 =
                (long) sorted[n - 1]
                        * sorted[n - 2]
                        * sorted[n - 3];

        return (int) Math.max(candidate1, candidate2);
    }

    static int maxProductOnePass(int[] nums) {

        if (nums == null || nums.length < 3) {
            return Integer.MIN_VALUE;
        }

        int maxA = Integer.MIN_VALUE;
        int maxB = Integer.MIN_VALUE;
        int maxC = Integer.MIN_VALUE;

        int minA = Integer.MAX_VALUE;
        int minB = Integer.MAX_VALUE;

        for (int num : nums) {

            if (num > maxA) {

                maxC = maxB;
                maxB = maxA;
                maxA = num;

            } else if (num > maxB) {

                maxC = maxB;
                maxB = num;

            } else if (num > maxC) {

                maxC = num;
            }

            if (num < minA) {

                minB = minA;
                minA = num;

            } else if (num < minB) {

                minB = num;
            }
        }

        long candidate1 =
                (long) maxA * maxB * maxC;

        long candidate2 =
                (long) maxA * minA * minB;

        return (int) Math.max(candidate1, candidate2);
    }

    static class TestCase {

        final String id;
        final int[] nums;
        final int expected;
        final String description;

        TestCase(
                String id,
                int[] nums,
                int expected,
                String description) {

            this.id = id;
            this.nums = nums;
            this.expected = expected;
            this.description = description;
        }
    }

    static void runTests(
            String methodName,
            Function<int[], Integer> func,
            List<TestCase> tests) {

        System.out.println(
                "========================= method: "
                        + methodName
                        + " =========================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            int result = func.apply(test.nums);

            if (result == test.expected) {

                passed++;

                System.out.printf(
                        "Test %s (%s): passed%n",
                        test.id,
                        test.description);

            } else {

                failed++;

                System.out.printf(
                        "Test %s (%s): FAILED%n",
                        test.id,
                        test.description);

                System.out.printf(
                        "got=%d expected=%d%n",
                        result,
                        test.expected);
            }
        }

        System.out.printf(
                "Results: %d passed, %d failed%n%n",
                passed,
                failed);
    }

    public static void main(String[] args) {

        List<TestCase> tests = new ArrayList<>();

        tests.add(new TestCase(
                "P1",
                new int[]{1, 2, 3},
                6,
                "Exactly three numbers"));

        tests.add(new TestCase(
                "P2",
                new int[]{1, 2, 3, 4},
                24,
                "Simple positive values"));

        tests.add(new TestCase(
                "P3",
                new int[]{-10, -10, 5, 2},
                500,
                "Two negatives produce maximum"));

        tests.add(new TestCase(
                "P4",
                new int[]{-5, -4, -3, -2},
                -24,
                "All negatives"));

        tests.add(new TestCase(
                "P5",
                new int[]{0, 0, 0, 5},
                0,
                "Contains zeros"));

        tests.add(new TestCase(
                "P6",
                new int[]{-100, -98, -1, 2, 3, 4},
                39200,
                "Large negative pair"));

        tests.add(new TestCase(
                "N1",
                null,
                Integer.MIN_VALUE,
                "Null input"));

        tests.add(new TestCase(
                "N2",
                new int[]{1, 2},
                Integer.MIN_VALUE,
                "Less than three elements"));

        runTests(
                "Brute Force",
                MaximumProductOfThreeNumbers::maxProductBruteForce,
                tests);

        runTests(
                "Sorting",
                MaximumProductOfThreeNumbers::maxProductSorting,
                tests);

        runTests(
                "One Pass",
                MaximumProductOfThreeNumbers::maxProductOnePass,
                tests);
    }
}
