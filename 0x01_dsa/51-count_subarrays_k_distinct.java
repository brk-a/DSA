import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Count Subarrays With Exactly K Distinct Integers.
 *
 * Implementations:
 *
 * 1. Brute Force (Oracle)
 *      O(n²)
 *
 * 2. Sliding Window
 *      O(n)
 *
 * The optimized solution uses
 *
 *      exactlyK = atMostK(k) - atMostK(k - 1)
 *
 * The brute-force implementation is retained as a correctness oracle
 * for deterministic and Randomised testing.
 */
public class CountSubarraysWithKDistinct {

    /* **********************************************************************
     * Validation
     * **********************************************************************/

    static boolean validArray(int[] nums) {
        return nums != null;
    }

    /* **********************************************************************
     * Brute Force Oracle
     * **********************************************************************/

    /**
     * O(n²)
     *
     * Enumerates every subarray.
     */
    static int countSubarraysBruteForce(
            int[] nums,
            int k) {

        if (!validArray(nums) || nums.length == 0 || k <= 0) {
            return 0;
        }

        int answer = 0;

        for (int left = 0; left < nums.length; left++) {

            Map<Integer, Integer> freq = new HashMap<>();

            int distinct = 0;

            for (int right = left; right < nums.length; right++) {

                freq.put(
                        nums[right],
                        freq.getOrDefault(nums[right], 0) + 1);

                if (freq.get(nums[right]) == 1) {
                    distinct++;
                }

                if (distinct == k) {
                    answer++;
                } else if (distinct > k) {
                    break;
                }
            }
        }

        return answer;
    }

    /* **********************************************************************
     * Sliding Window
     * **********************************************************************/

    /**
     * Counts subarrays having at most K distinct values.
     *
     * O(n)
     */
    static int atMostK(
            int[] nums,
            int k) {

        if (!validArray(nums) || nums.length == 0 || k < 0) {
            return 0;
        }

        int left = 0;
        int answer = 0;

        Map<Integer, Integer> freq = new HashMap<>();

        int remaining = k;

        for (int right = 0; right < nums.length; right++) {

            freq.put(
                    nums[right],
                    freq.getOrDefault(nums[right], 0) + 1);

            if (freq.get(nums[right]) == 1) {
                remaining--;
            }

            while (remaining < 0) {

                freq.put(
                        nums[left],
                        freq.get(nums[left]) - 1);

                if (freq.get(nums[left]) == 0) {
                    remaining++;
                }

                left++;
            }

            answer += right - left + 1;
        }

        return answer;
    }

    /**
     * O(n)
     */
    static int countSubarraysSlidingWindow(
            int[] nums,
            int k) {

        if (!validArray(nums) || nums.length == 0 || k <= 0) {
            return 0;
        }

        return atMostK(nums, k)
                - atMostK(nums, k - 1);
    }

    /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    static class TestCase {

        final String id;
        final int[] nums;
        final int k;
        final int expected;
        final String description;

        TestCase(
                String id,
                int[] nums,
                int k,
                int expected,
                String description) {

            this.id = id;
            this.nums = nums;
            this.k = k;
            this.expected = expected;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {

        int solve(int[] nums, int k);
    }

    static class MethodCase {

        final String name;
        final Algorithm algorithm;

        MethodCase(
                String name,
                Algorithm algorithm) {

            this.name = name;
            this.algorithm = algorithm;
        }
    }

    /* **********************************************************************
     * Utilities
     * **********************************************************************/

    static int[] cloneArray(int[] nums) {

        if (nums == null) {
            return null;
        }

        return nums.clone();
    }

    static String formatArray(int[] nums) {

        if (nums == null) {
            return "null";
        }

        return Arrays.toString(nums);
    }

    static void runTests(
            String algorithm,
            Algorithm method,
            List<TestCase> tests) {

        System.out.println(
                "======================================================");
        System.out.println(algorithm);
        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            try {

                int actual =
                        method.solve(
                                cloneArray(test.nums),
                                test.k);

                if (actual == test.expected) {

                    passed++;

                    System.out.printf(
                            "✓ %s (%s)%n",
                            test.id,
                            test.description);

                } else {

                    failed++;

                    System.out.printf(
                            "✗ %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.printf(
                            "  nums      = %s%n",
                            formatArray(test.nums));

                    System.out.printf(
                            "  k         = %d%n",
                            test.k);

                    System.out.printf(
                            "  expected  = %d%n",
                            test.expected);

                    System.out.printf(
                            "  actual    = %d%n",
                            actual);
                }

            } catch (Exception ex) {

                failed++;

                System.out.printf(
                        "✗ %s (%s)%n",
                        test.id,
                        test.description);

                System.out.printf(
                        "  nums      = %s%n",
                        formatArray(test.nums));

                System.out.printf(
                        "  k         = %d%n",
                        test.k);

                System.out.printf(
                        "  exception = %s%n",
                        ex);
            }
        }

        System.out.println();

        System.out.printf(
                "Results: %d passed, %d failed, %d total%n",
                passed,
                failed,
                tests.size());

        System.out.println();
    }

    /* **********************************************************************
     * Randomised Testing
     * **********************************************************************/

    static int[] randomArray(
            Random rng,
            int maxLength,
            int minValue,
            int maxValue) {

        int n = rng.nextInt(maxLength + 1);

        int[] nums = new int[n];

        for (int i = 0; i < n; i++) {
            nums[i] =
                    minValue
                    + rng.nextInt(maxValue - minValue + 1);
        }

        return nums;
    }

    static void runRandomisedTests(
            int iterations) {

        System.out.println(
                "======================================================");
        System.out.println(
                "Randomised Cross Checks");
        System.out.println(
                "======================================================");

        Random rng = new Random(123456789L);

        for (int i = 1; i <= iterations; i++) {

            int[] nums =
                    randomArray(
                            rng,
                            8,
                            -2,
                            3);

            int k = rng.nextInt(6);

            int brute =
                    countSubarraysBruteForce(
                            cloneArray(nums),
                            k);

            int sliding =
                    countSubarraysSlidingWindow(
                            cloneArray(nums),
                            k);

            if (brute != sliding) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "nums    = " + formatArray(nums));

                System.out.println(
                        "k       = " + k);

                System.out.println(
                        "brute   = " + brute);

                System.out.println(
                        "sliding = " + sliding);

                return;
            }
        }

        System.out.printf(
                "All %d Randomised tests passed.%n%n",
                iterations);
    }

    /* **********************************************************************
     * Main
     * **********************************************************************/

    public static void main(String[] args) {

        List<TestCase> tests = new ArrayList<>();

        /*
         * ============================================================
         * INSERT DETERMINISTIC TESTS HERE
         * ============================================================
         */

        /*
         * ============================================================
         * Standard Examples
         * ============================================================
         */

        tests.add(new TestCase(
                "S1",
                new int[]{1, 2, 1, 2, 3},
                2,
                7,
                "Classic LeetCode example"));

        tests.add(new TestCase(
                "S2",
                new int[]{1, 2, 1, 3, 4},
                3,
                3,
                "Second LeetCode example"));

        /*
         * ============================================================
         * Single Element / Small Arrays
         * ============================================================
         */

        tests.add(new TestCase(
                "A1",
                new int[]{5},
                1,
                1,
                "Single element"));

        tests.add(new TestCase(
                "A2",
                new int[]{5},
                2,
                0,
                "K larger than distinct count"));

        /*
         * ============================================================
         * All Equal
         * ============================================================
         */

        tests.add(new TestCase(
                "D1",
                new int[]{5, 5, 5, 5},
                1,
                10,
                "Every subarray qualifies"));

        tests.add(new TestCase(
                "D2",
                new int[]{5, 5, 5, 5},
                2,
                0,
                "Impossible K"));

        /*
         * ============================================================
         * All Distinct
         * ============================================================
         */

        tests.add(new TestCase(
                "U1",
                new int[]{1, 2, 3, 4},
                2,
                3,
                "Adjacent pairs"));

        tests.add(new TestCase(
                "U2",
                new int[]{1, 2, 3, 4},
                4,
                1,
                "Whole array"));

        /*
         * ============================================================
         * Negative Values
         * ============================================================
         */

        tests.add(new TestCase(
                "N1",
                new int[]{-1, -2, -1},
                2,
                3,
                "Negative numbers"));

        tests.add(new TestCase(
                "N2",
                new int[]{-5, -5, -4},
                2,
                2,
                "Negative duplicates"));

        /*
         * ============================================================
         * Mixed Duplicates
         * ============================================================
         */

        tests.add(new TestCase(
                "M1",
                new int[]{1, 2, 1, 3, 2},
                3,
                countSubarraysBruteForce(
                        new int[]{1, 2, 1, 3, 2},
                        3),
                "Mixed duplicates"));

        tests.add(new TestCase(
                "M2",
                new int[]{2, 1, 2, 1, 2},
                2,
                countSubarraysBruteForce(
                        new int[]{2, 1, 2, 1, 2},
                        2),
                "Alternating duplicates"));

        /*
         * ============================================================
         * Edge Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "E1",
                null,
                2,
                0,
                "Null array"));

        tests.add(new TestCase(
                "E2",
                new int[]{},
                2,
                0,
                "Empty array"));

        tests.add(new TestCase(
                "E3",
                new int[]{1, 2, 3},
                0,
                0,
                "K equals zero"));

        /*
         * ============================================================
         * Large K
         * ============================================================
         */

        tests.add(new TestCase(
                "L1",
                new int[]{1, 2, 1},
                5,
                0,
                "K larger than distinct values"));

        tests.add(new TestCase(
                "L2",
                new int[]{1, 2},
                3,
                0,
                "K larger than array length"));

        /*
         * ============================================================
         * Cross Checks
         * ============================================================
         */

        tests.add(new TestCase(
                "X1",
                new int[]{1, 2, 3, 1},
                2,
                countSubarraysBruteForce(
                        new int[]{1, 2, 3, 1},
                        2),
                "Oracle cross-check"));

        tests.add(new TestCase(
                "X2",
                new int[]{3, 3, 1, 2},
                2,
                countSubarraysBruteForce(
                        new int[]{3, 3, 1, 2},
                        2),
                "Oracle cross-check"));

        System.out.println(
                "############################################################");
        System.out.println(
                "######## COUNT SUBARRAYS WITH EXACTLY K DISTINCT ###########");
        System.out.println(
                "############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "Brute Force (Oracle)",
                        (nums, k) ->
                                countSubarraysBruteForce(
                                        cloneArray(nums),
                                        k)),

                new MethodCase(
                        "Sliding Window O(n)",
                        (nums, k) ->
                                countSubarraysSlidingWindow(
                                        cloneArray(nums),
                                        k))
        );

        for (MethodCase method : methods) {

            runTests(
                    method.name,
                    method.algorithm,
                    tests);
        }

        runRandomisedTests(5000);
    }
}
