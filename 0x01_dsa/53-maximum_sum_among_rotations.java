import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Max Sum Among Rotations.
 *
 * Problem:
 * Given an integer array nums, consider all cyclic rotations of nums.
 * For each rotation, define:
 *
 *     F = sum_{i=0..n-1} (i * a[i])
 *
 * where a[i] is the element at index i in that rotated array.
 *
 * Return the maximum F across all rotations.
 *
 * Implementations:
 *
 * 1. Brute Force (Oracle)
 *      Enumerate all rotations explicitly and compute F.
 *
 * 2. Maths Formula (O(n))
 *      Uses recurrence:
 *          F(k+1) = F(k) + sum(nums) - n * nums[n - 1 - k]
 *
 * 3. Pivot-based Heuristic
 *      Attempts to compute F for a rotation derived from a pivot.
 *      This is NOT guaranteed correct for all inputs and is kept
 *      as an experimental candidate, cross-checked against the oracle.
 */
public class MaxSumAmongRotationsTestHarness {

    /* **********************************************************************
     * Core Algorithm Implementations
     * **********************************************************************/

    static boolean validArray(int[] nums) {
        return nums != null && nums.length > 0;
    }

    /**
     * Brute Force (Oracle).
     *
     * Time: O(n^2)
     * Enumerates each rotation and computes F directly.
     */
    static int maxSumAmongRotationsBruteForce(int[] nums) {

        if (!validArray(nums)) {
            return -1;
        }

        int n = nums.length;
        int result = Integer.MIN_VALUE;

        for (int rotation = 0; rotation < n; rotation++) {

            int sum = 0;

            for (int i = 0; i < n; i++) {
                int idx = (rotation + i) % n;
                sum += i * nums[idx];
            }

            result = Math.max(result, sum);
        }

        return result;
    }

    /**
     * Maths Formula O(n).
     *
     * Uses recurrence:
     *
     * Let:
     *      arrSum = sum(nums[i]) for i in [0..n-1]
     *      F(0)   = sum(i * nums[i]) for i in [0..n-1]
     *
     * Then for k >= 0:
     *      F(k+1) = F(k) + arrSum - n * nums[n - 1 - k]
     *
     * We iterate through all rotations starting from F(0).
     */
    static int maxSumAmongRotationsMathsFormula(int[] nums) {

        if (!validArray(nums)) {
            return -1;
        }

        int n = nums.length;

        int arrSum = 0;
        for (int num : nums) {
            arrSum += num;
        }

        int currVal = 0;
        for (int i = 0; i < n; i++) {
            currVal += i * nums[i];
        }

        int result = currVal;

        for (int k = 0; k < n - 1; k++) {

            currVal = currVal + arrSum - n * nums[n - 1 - k];
            result = Math.max(result, currVal);
        }

        return result;
    }

    /**
     * Pivot-based Heuristic.
     *
     * Finds a pivot index where nums[i] > nums[(i + 1) % n],
     * then computes F for the rotation that would place pivot
     * at index n - 1.
     *
     * NOTE: This is not a proven correct algorithm for this
     * problem; it is kept to demonstrate cross-checking with
     * the brute-force oracle.
     */
    static int maxSumAmongRotationsPivot(int[] nums) {

        if (!validArray(nums)) {
            return -1;
        }

        int n = nums.length;
        int pivot = findPivot(nums);
        int diff = n - 1 - pivot;

        long result = 0L;

        for (int i = 0; i < n; i++) {
            int rotatedIndex = (i + diff) % n;
            result += (long) rotatedIndex * nums[i];
        }

        return (int) result;
    }

    /* **********************************************************************
     * Helpers for Pivot Heuristic
     * **********************************************************************/

    static int findPivot(int[] nums) {

        int n = nums.length;

        for (int i = 0; i < n; i++) {
            if (nums[i] > nums[(i + 1) % n]) {
                return i;
            }
        }

        return 0;
    }

    /* **********************************************************************
     * Test Harness Types
     * **********************************************************************/

    static class TestCase {

        final String id;
        final int[] input;
        final Integer expected;
        final String description;

        TestCase(
                String id,
                int[] input,
                Integer expected,
                String description) {

            this.id = id;
            this.input = input;
            this.expected = expected;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {

        int solve(int[] nums);
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

    static boolean intsEqual(Integer a, Integer b) {

        if (a == null && b == null) {
            return true;
        }

        if (a == null || b == null) {
            return false;
        }

        return a.intValue() == b.intValue();
    }

    static void runTests(
            String algorithmName,
            Algorithm method,
            List<TestCase> tests) {

        System.out.println(
                "======================================================");
        System.out.println(algorithmName);
        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            try {

                int actualValue = method.solve(
                        cloneArray(test.input));

                Integer actual = (test.input == null || test.input.length == 0)
                        ? (test.expected == null ? null : actualValue)
                        : actualValue;

                if (intsEqual(actual, test.expected)) {

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
                            "  input     = %s%n",
                            formatArray(test.input));

                    System.out.printf(
                            "  expected  = %s%n",
                            test.expected);

                    System.out.printf(
                            "  actual    = %s%n",
                            actual);
                }

            } catch (Exception ex) {

                failed++;

                System.out.printf(
                        "✗ %s (%s)%n",
                        test.id,
                        test.description);

                System.out.printf(
                        "  input     = %s%n",
                        formatArray(test.input));

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

        if (n == 0) {
            return new int[0];
        }

        int[] nums = new int[n];

        for (int i = 0; i < n; i++) {
            nums[i] = minValue + rng.nextInt(maxValue - minValue + 1);
        }

        return nums;
    }

    static void runRandomisedTests(
            int iterations,
            boolean includePivot) {

        System.out.println(
                "======================================================");
        System.out.println(
                "Randomised Cross Checks");
        System.out.println(
                "======================================================");

        Random rng = new Random(987654321L);

        for (int i = 1; i <= iterations; i++) {

            int[] nums = randomArray(
                    rng,
                    8,
                    -10,
                    10);

            if (!validArray(nums)) {
                continue;
            }

            int brute =
                    maxSumAmongRotationsBruteForce(
                            cloneArray(nums));

            int formula =
                    maxSumAmongRotationsMathsFormula(
                            cloneArray(nums));

            if (brute != formula) {

                System.out.println(
                        "Randomised test FAILED (Formula vs Brute)");

                System.out.println(
                        "nums     = " + formatArray(nums));

                System.out.println(
                        "brute    = " + brute);

                System.out.println(
                        "formula  = " + formula);

                return;
            }

            if (includePivot) {

                int pivotResult =
                        maxSumAmongRotationsPivot(
                                cloneArray(nums));

                if (pivotResult != brute) {

                    System.out.println(
                            "Randomised test FAILED (Pivot vs Brute)");

                    System.out.println(
                            "nums      = " + formatArray(nums));

                    System.out.println(
                            "brute     = " + brute);

                    System.out.println(
                            "pivot     = " + pivotResult);

                    // Do not return; treat pivot as experimental.
                    // You can decide whether to abort here or just log.
                }
            }
        }

        System.out.printf(
                "All %d Randomised tests passed (Brute vs Formula).%n%n",
                iterations);
    }

    /* **********************************************************************
     * Main
     * **********************************************************************/

    public static void main(String[] args) {

        List<TestCase> tests = new ArrayList<>();

        /*
         * ============================================================
         * Small / Simple Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "A1",
                new int[]{1},
                0,
                "Single element: only rotation is [1], F = 0 * 1 = 0"));

        tests.add(new TestCase(
                "A2",
                new int[]{1, 2},
                2,
                "Two elements: rotations [1,2] -> 2, [2,1] -> 1"));

        tests.add(new TestCase(
                "A3",
                new int[]{1, 2, 3},
                8,
                "Three elements: best rotation gives F = 8"));

        tests.add(new TestCase(
                "A4",
                new int[]{8, 3, 1, 2},
                29,
                "Classic example: [8,3,1,2] has max F = 29"));

        tests.add(new TestCase(
                "A5",
                new int[]{4, 3, 2, 6},
                26,
                "Another classic example: [4,3,2,6] has max F = 26"));

        /*
         * ============================================================
         * Negative / Mixed Values
         * ============================================================
         */

        tests.add(new TestCase(
                "M1",
                new int[]{-1, -2, -3},
                -4,
                "All negatives"));

        tests.add(new TestCase(
                "M2",
                new int[]{-1, 2, -3, 4},
                13,
                "Mixed signs"));

        /*
         * ============================================================
         * Edge Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "E1",
                null,
                -1,
                "Null array"));

        tests.add(new TestCase(
                "E2",
                new int[]{},
                -1,
                "Empty array"));

        System.out.println(
                "############################################################");
        System.out.println(
                "######## MAX SUM AMONG ROTATIONS (INT ARRAY) ##############");
        System.out.println(
                "############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "Brute Force (Oracle)",
                        MaxSumAmongRotationsTestHarness::maxSumAmongRotationsBruteForce),

                new MethodCase(
                        "Maths Formula O(n)",
                        MaxSumAmongRotationsTestHarness::maxSumAmongRotationsMathsFormula),

                new MethodCase(
                        "Pivot-based Heuristic (Experimental)",
                        MaxSumAmongRotationsTestHarness::maxSumAmongRotationsPivot)
        );

        for (MethodCase method : methods) {

            runTests(
                    method.name,
                    method.algorithm,
                    tests);
        }

        runRandomisedTests(
                5000,
                true);
    }
}
