import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

/**
 * Find the smallest missing positive integer in an array.
 *
 * Two implementations are provided:
 *  - firstMissingPositiveInPlace: O(n) time, O(1) extra space (modifies the array)
 *  - firstMissingPositiveSorting: O(n log n) time, O(1) extra space beyond sort
 *
 * For null or empty input, both return 1.
 */
public class SmallestMissingPositiveNumber {

    /* **********************************************************************
     * Algorithms
     * **********************************************************************/

    /**
     * In-place linear-time solution.
     *
     * Idea: place each positive value v (1 <= v <= n) at index v - 1.
     * After that, the first index i with nums[i] != i + 1 gives the answer i + 1.
     *
     * Time:  O(n)
     * Space: O(1) extra
     */
    static int firstMissingPositiveInPlace(int[] nums) {
        if (nums == null || nums.length == 0) {
            // Smallest positive integer is 1 by definition.
            return 1;
        }

        int n = nums.length;
        int i = 0;

        while (i < n) {
            int v = nums[i];
            // We only care about values in [1, n].
            // Place v at index v - 1 if it's not already there.
            if (v > 0 && v <= n && nums[v - 1] != v) {
                int tmp = nums[v - 1];
                nums[v - 1] = v;
                nums[i] = tmp;
            } else {
                i++;
            }
        }

        // Now the first index where nums[i] != i + 1 is our answer.
        for (i = 0; i < n; i++) {
            if (nums[i] != i + 1) {
                return i + 1;
            }
        }

        // All 1..n are present.
        return n + 1;
    }

    /**
     * Sorting-based solution.
     *
     * Idea: sort a copy, then scan positives starting from expected = 1.
     * When we see a positive number greater than expected, expected is missing.
     *
     * Time:  O(n log n)
     * Space: O(n) due to clone (or O(1) if in-place sort is allowed).
     */
    static int firstMissingPositiveSorting(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 1;
        }

        int[] clone = nums.clone();
        Arrays.sort(clone);

        int expected = 1;
        for (int v : clone) {
            if (v < expected) {
                // Ignore negatives, zeros, and duplicates less than expected.
                continue;
            }
            if (v == expected) {
                expected++;
            } else if (v > expected) {
                // We found a gap.
                return expected;
            }
        }

        // All positives from 1 up to expected - 1 are present.
        return expected;
    }

    /**
     * Simple set-based brute-force used only to generate expected values in tests.
     * Time:  O(n + range) where range is up to max positive value in the array.
     * Space: O(range)
     */
    static int firstMissingPositiveBruteForce(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 1;
        }

        int max = 0;
        for (int v : nums) {
            if (v > max) {
                max = v;
            }
        }
        if (max <= 0) {
            return 1;
        }

        boolean[] present = new boolean[max + 2]; // guard for max+1
        for (int v : nums) {
            if (v > 0 && v <= max + 1) {
                present[v] = true;
            }
        }

        for (int i = 1; i <= max + 1; i++) {
            if (!present[i]) {
                return i;
            }
        }
        // Should never reach here.
        return 1;
    }

    /* **********************************************************************
     * Test harness
     * **********************************************************************/

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

    static class MethodCase {
        final String name;
        final Function<TestCase, Integer> method;

        MethodCase(String name, Function<TestCase, Integer> method) {
            this.name = name;
            this.method = method;
        }
    }

    static boolean intsEqual(int a, int b) {
        return a == b;
    }

    static String formatArray(int[] nums) {
        return nums == null ? "null" : Arrays.toString(nums);
    }

    static void runTests(String algorithm, Function<TestCase, Integer> method, List<TestCase> tests) {
        System.out.println("==================================================");
        System.out.println(algorithm);
        System.out.println("==================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {
            try {
                Integer actual = method.apply(test);

                if (intsEqual(actual, test.expected)) {
                    passed++;
                    System.out.printf("✓ %s (%s)%n", test.id, test.description);
                } else {
                    failed++;
                    System.out.printf("✗ %s (%s)%n", test.id, test.description);
                    System.out.printf("  nums      = %s%n", formatArray(test.nums));
                    System.out.printf("  expected  = %d%n", test.expected);
                    System.out.printf("  actual    = %d%n", actual);
                }
            } catch (Exception ex) {
                failed++;
                System.out.printf("✗ %s (%s)%n", test.id, test.description);
                System.out.printf("  nums      = %s%n", formatArray(test.nums));
                System.out.printf("  exception = %s%n", ex.toString());
            }
        }

        System.out.printf("%nResults: %d passed, %d failed, %d total%n%n", passed, failed, tests.size());
    }

    static int bruteForceExpected(int[] nums) {
        return firstMissingPositiveBruteForce(nums);
    }

    public static void main(String[] args) {
        List<TestCase> tests = new ArrayList<>();

        /*
         * Standard examples from typical "first missing positive" statements.
         */
        tests.add(new TestCase(
            "S1",
            new int[]{1, 3, 6, 4, 1, 2},
            5,
            "Classic mixed array: smallest missing positive is 5"
        ));

        tests.add(new TestCase(
            "S2",
            new int[]{1, 2, 3},
            4,
            "All first positives present consecutively"
        ));

        tests.add(new TestCase(
            "S3",
            new int[]{2, 3, 4},
            1,
            "Missing 1 at the beginning"
        ));

        tests.add(new TestCase(
            "S4",
            new int[]{-1, -3},
            1,
            "All negatives"
        ));

        /*
         * Simple and small cases
         */
        tests.add(new TestCase(
            "M1",
            new int[]{0},
            1,
            "Single zero element"
        ));

        tests.add(new TestCase(
            "M2",
            new int[]{1},
            2,
            "Single one element"
        ));

        tests.add(new TestCase(
            "M3",
            new int[]{2},
            1,
            "Single positive not equal to 1"
        ));

        tests.add(new TestCase(
            "M4",
            new int[]{1, 2},
            3,
            "All small positives present"
        ));

        tests.add(new TestCase(
            "M5",
            new int[]{1, 1, 1},
            2,
            "Duplicates of 1"
        ));

        /*
         * Mixed negatives, zeros, and positives
         */
        tests.add(new TestCase(
            "N1",
            new int[]{0, 2, 2, 1, 1},
            3,
            "Mixed zeros, duplicates, and small positives"
        ));

        tests.add(new TestCase(
            "N2",
            new int[]{-8, -3, 4, 2},
            1,
            "Missing 1 with positives and negatives"
        ));

        tests.add(new TestCase(
            "N3",
            new int[]{-5, -2, 7, 10},
            1,
            "Positive numbers start above 1"
        ));

        tests.add(new TestCase(
            "N4",
            new int[]{3, 4, -1, 1},
            2,
            "LeetCode example: first missing positive is 2"
        ));

        /*
         * Zero and duplicate cases
         */
        tests.add(new TestCase(
            "Z1",
            new int[]{0, 0, 0},
            1,
            "All zeros"
        ));

        tests.add(new TestCase(
            "Z2",
            new int[]{2, 2, 2},
            1,
            "All same positive but missing 1"
        ));

        tests.add(new TestCase(
            "Z3",
            new int[]{-1, 0, 1},
            2,
            "Negative, zero and one present"
        ));

        /*
         * Edge cases
         */
        tests.add(new TestCase(
            "E1",
            null,
            1,
            "Null input"
        ));

        tests.add(new TestCase(
            "E2",
            new int[]{},
            1,
            "Empty input"
        ));

        tests.add(new TestCase(
            "E3",
            new int[]{1000000},
            1,
            "Single large positive not equal to 1"
        ));

        tests.add(new TestCase(
            "E4",
            new int[]{1, 2, 3, 4, 5, 6},
            7,
            "Consecutive positives up to length"
        ));

        tests.add(new TestCase(
            "E5",
            new int[]{2, 3, 7, 6, 8, -1, -10, 15},
            1,
            "Mixed with gaps and negatives"
        ));

        /*
         * Cross-check cases using brute-force expected values
         */
        tests.add(new TestCase(
            "X1",
            new int[]{1, -1, 2, -2},
            bruteForceExpected(new int[]{1, -1, 2, -2}),
            "Cross-check mixed small values"
        ));

        tests.add(new TestCase(
            "X2",
            new int[]{4, -1, 2, -7, 3},
            bruteForceExpected(new int[]{4, -1, 2, -7, 3}),
            "Cross-check random-like array"
        ));

        tests.add(new TestCase(
            "X3",
            new int[]{8, -6, 3, 1, -2},
            bruteForceExpected(new int[]{8, -6, 3, 1, -2}),
            "Cross-check another random-like array"
        ));

        System.out.println("############################################################");
        System.out.println("########### SMALLEST MISSING POSITIVE INTEGER ##############");
        System.out.println("############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(
            new MethodCase(
                "In-place O(n) time, O(1) space",
                t -> firstMissingPositiveInPlace(
                        t.nums == null ? null : t.nums.clone()
                ) // clone so each test starts from original input
            ),
            new MethodCase(
                "Sorting-based O(n log n) time",
                t -> firstMissingPositiveSorting(
                        t.nums == null ? null : t.nums.clone()
                )
            )
        );

        for (MethodCase method : methods) {
            runTests(method.name, method.method, tests);
        }
    }
}
