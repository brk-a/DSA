import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class MinimumMovesEqualiseArrayOddEven {

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

    /**
     * Brute-force reference solution.
     * Try removing each index and recompute odd/even sums after the shift.
     * Time: O(n^2)
     * Space: O(1)
     */
    static int minimumMovesEqualiseArrayOddEvenBruteForce(int[] nums) {
        if (nums == null || nums.length == 0) {
            return -1;
        }

        int count = 0;
        for (int remove = 0; remove < nums.length; remove++) {
            int evenSum = 0;
            int oddSum = 0;
            int newIndex = 0;

            for (int i = 0; i < nums.length; i++) {
                if (i == remove) {
                    continue;
                }

                if (newIndex % 2 == 0) {
                    evenSum += nums[i];
                } else {
                    oddSum += nums[i];
                }

                newIndex++;
            }

            if (evenSum == oddSum) {
                count++;
            }
        }

        return count;
    }

    /**
     * Optimal solution using prefix sums.
     * For each removal index i:
     * - left side keeps parity
     * - right side flips parity
     * Time: O(n)
     * Space: O(n)
     */
    static int minimumMovesEqualiseArrayOddEvenPrefixSuffixSum(int[] nums) {
        if (nums == null || nums.length == 0) {
            return -1;
        }

        int n = nums.length;
        int[] prefixEven = new int[n + 1];
        int[] prefixOdd = new int[n + 1];

        for (int i = 0; i < n; i++) {
            prefixEven[i + 1] = prefixEven[i];
            prefixOdd[i + 1] = prefixOdd[i];

            if (i % 2 == 0) {
                prefixEven[i + 1] += nums[i];
            } else {
                prefixOdd[i + 1] += nums[i];
            }
        }

        int totalEven = prefixEven[n];
        int totalOdd = prefixOdd[n];
        int result = 0;

        for (int i = 0; i < n; i++) {
            int leftEven = prefixEven[i];
            int leftOdd = prefixOdd[i];

            int rightEven = totalEven - prefixEven[i + 1];
            int rightOdd = totalOdd - prefixOdd[i + 1];

            int newEvenSum = leftEven + rightOdd;
            int newOddSum = leftOdd + rightEven;

            if (newEvenSum == newOddSum) {
                result++;
            }
        }

        return result;
    }

    static boolean arraysEqual(int a, int b) {
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
                int actual = method.apply(test);

                if (arraysEqual(actual, test.expected)) {
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
                System.out.printf("  exception = %s%n", ex.getMessage());
            }
        }

        System.out.printf("%nResults: %d passed, %d failed, %d total%n%n", passed, failed, tests.size());
    }

    static int bruteForceExpected(int[] nums) {
        return minimumMovesEqualiseArrayOddEvenBruteForce(nums);
    }

    public static void main(String[] args) {
        List<TestCase> tests = new ArrayList<>();

        /*
         * Standard examples
         */
        tests.add(new TestCase(
            "S1",
            new int[]{1, 2, 3},
            0,
            "No removal balances even and odd sums"
        ));

        tests.add(new TestCase(
            "S2",
            new int[]{2, 1, 2},
            1,
            "Removing the middle element balances the array"
        ));

        /*
         * Simple cases
         */
        tests.add(new TestCase(
            "M1",
            new int[]{1},
            1,
            "Single element; removing it leaves empty even and odd sums equal"
        ));

        tests.add(new TestCase(
            "M2",
            new int[]{1, 1},
            2,
            "Both removals produce equal sums"
        ));

        tests.add(new TestCase(
            "M3",
            new int[]{0, 0, 0},
            3,
            "All zero values"
        ));

        /*
         * Mixed cases
         */
        tests.add(new TestCase(
            "C1",
            new int[]{1, 2, 3, 4},
            0,
            "Ascending values"
        ));

        tests.add(new TestCase(
            "C2",
            new int[]{5, 5, 5, 5},
            0,
            "All equal values with even length"
        ));

        tests.add(new TestCase(
            "C3",
            new int[]{4, 1, 2, 1, 4},
            1,
            "One balanced removal in the middle"
        ));

        tests.add(new TestCase(
            "C4",
            new int[]{-1, 2, -3, 4},
            0,
            "Includes negative numbers"
        ));

        /*
         * Edge cases
         */
        tests.add(new TestCase(
            "E1",
            null,
            -1,
            "Null input"
        ));

        tests.add(new TestCase(
            "E2",
            new int[]{},
            -1,
            "Empty input"
        ));

        tests.add(new TestCase(
            "E3",
            new int[]{7, 7, 7, 7, 7},
            0,
            "Odd/even parity never balances after removal"
        ));

        /*
         * Cross-check cases
         */
        tests.add(new TestCase(
            "X1",
            new int[]{3, 1, 2, 3, 4},
            bruteForceExpected(new int[]{3, 1, 2, 3, 4}),
            "Brute-force computed expected value"
        ));

        tests.add(new TestCase(
            "X2",
            new int[]{10, 3, 10, 1, 2, 8},
            bruteForceExpected(new int[]{10, 3, 10, 1, 2, 8}),
            "Another brute-force cross-check"
        ));

        tests.add(new TestCase(
            "X3",
            new int[]{100, 101, 102, 103},
            bruteForceExpected(new int[]{100, 101, 102, 103}),
            "Small spread cross-check"
        ));

        System.out.println("############################################################");
        System.out.println("###### MINIMUM MOVES EQUALISE ARRAY ODD/EVEN ###############");
        System.out.println("############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(
            new MethodCase("Brute Force Reference", t -> minimumMovesEqualiseArrayOddEvenBruteForce(t.nums)),
            new MethodCase("Prefix/Suffix O(n)", t -> minimumMovesEqualiseArrayOddEvenPrefixSuffixSum(t.nums))
        );

        for (MethodCase method : methods) {
            runTests(method.name, method.method, tests);
        }
    }
}
