import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class JumpGame {

    private static final int INT_MAX_VAL = Integer.MAX_VALUE;

    static class TestCase {
        final String id;
        final List<Integer> nums;
        final int expected;
        final String description;

        TestCase(String id, List<Integer> nums, int expected, String description) {
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
     * Brute-force recursion with memoisation.
     * Time: O(n^2) in the worst case.
     * Space: O(n).
     */
    static int jumpGameRecursion(List<Integer> nums) {
        if (nums == null || nums.isEmpty()) {
            return -1;
        }

        Integer[] memo = new Integer[nums.size()];
        int result = minimumJumps(0, nums, memo);
        return result == INT_MAX_VAL ? -1 : result;
    }

    /**
     * Bottom-up dynamic programming.
     * Time: O(n^2)
     * Space: O(n)
     */
    static int jumpGameTabulation(List<Integer> nums) {
        if (nums == null || nums.isEmpty()) {
            return -1;
        }

        int n = nums.size();
        int[] table = new int[n];
        Arrays.fill(table, INT_MAX_VAL);
        table[n - 1] = 0;

        for (int i = n - 2; i >= 0; i--) {
            int farthest = Math.min(n - 1, i + nums.get(i));
            for (int j = i + 1; j <= farthest; j++) {
                if (table[j] != INT_MAX_VAL) {
                    table[i] = Math.min(table[i], 1 + table[j]);
                }
            }
        }

        return table[0] == INT_MAX_VAL ? -1 : table[0];
    }

    /**
     * Greedy solution.
     * Time: O(n)
     * Space: O(1)
     */
    static int jumpGameGreedyAlgo(List<Integer> nums) {
        if (nums == null || nums.isEmpty()) {
            return -1;
        }

        int n = nums.size();
        if (n == 1) {
            return 0;
        }

        if (nums.get(0) == 0) {
            return -1;
        }

        int jumps = 0;
        int currEnd = 0;
        int farthest = 0;

        for (int i = 0; i < n - 1; i++) {
            farthest = Math.max(farthest, i + nums.get(i));

            if (i == currEnd) {
                jumps++;
                currEnd = farthest;

                if (currEnd >= n - 1) {
                    return jumps;
                }

                if (currEnd == i) {
                    return -1;
                }
            }
        }

        return -1;
    }

    // Helpers

    static int minimumJumps(int idx, List<Integer> nums, Integer[] memo) {
        int n = nums.size();

        if (idx >= n - 1) {
            return 0;
        }

        if (memo[idx] != null) {
            return memo[idx];
        }

        int best = INT_MAX_VAL;
        int farthest = Math.min(n - 1, idx + nums.get(idx));

        for (int next = idx + 1; next <= farthest; next++) {
            int sub = minimumJumps(next, nums, memo);
            if (sub != INT_MAX_VAL) {
                best = Math.min(best, 1 + sub);
            }
        }

        memo[idx] = best;
        return best;
    }

    static boolean arraysEqual(int a, int b) {
        return a == b;
    }

    static String formatList(List<Integer> nums) {
        return nums == null ? "null" : nums.toString();
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

                if (arraysEqual(actual, test.expected)) {
                    passed++;
                    System.out.printf("✓ %s (%s)%n", test.id, test.description);
                } else {
                    failed++;
                    System.out.printf("✗ %s (%s)%n", test.id, test.description);
                    System.out.printf("  nums      = %s%n", formatList(test.nums));
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

    static int bruteForceExpected(List<Integer> nums) {
        return jumpGameTabulation(nums);
    }

    public static void main(String[] args) {
        List<TestCase> tests = new ArrayList<>();

        /*
         * Standard examples
         */
        tests.add(new TestCase(
            "S1",
            List.of(2, 3, 1, 1, 4),
            2,
            "Classic reachable case"
        ));

        tests.add(new TestCase(
            "S2",
            List.of(2, 3, 0, 1, 4),
            2,
            "Classic reachable case with a zero"
        ));

        tests.add(new TestCase(
            "S3",
            List.of(1, 2, 3),
            2,
            "Small increasing example"
        ));

        /*
         * Simple cases
         */
        tests.add(new TestCase(
            "M1",
            List.of(0),
            0,
            "Single element"
        ));

        tests.add(new TestCase(
            "M2",
            List.of(1, 0),
            1,
            "One jump directly to the end"
        ));

        tests.add(new TestCase(
            "M3",
            List.of(1, 1, 1, 1),
            3,
            "Must jump one step at a time"
        ));

        /*
         * Unreachable cases
         */
        tests.add(new TestCase(
            "U1",
            List.of(0, 1),
            -1,
            "Cannot move from the start"
        ));

        tests.add(new TestCase(
            "U2",
            List.of(3, 2, 1, 0, 4),
            -1,
            "Classic unreachable case"
        ));

        tests.add(new TestCase(
            "U3",
            List.of(1, 0, 0, 0),
            -1,
            "Stuck after first move"
        ));

        /*
         * Mixed cases
         */
        tests.add(new TestCase(
            "C1",
            List.of(2, 1, 2, 3, 1, 1, 1),
            3,
            "Greedy should choose the best frontier"
        ));

        tests.add(new TestCase(
            "C2",
            List.of(4, 1, 1, 3, 1, 1, 1),
            2,
            "A long first jump reduces total jumps"
        ));

        tests.add(new TestCase(
            "C3",
            List.of(1, 4, 1, 1, 1, 1),
            2,
            "Jump from index 1 reaches the end"
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
            List.of(),
            -1,
            "Empty input"
        ));

        tests.add(new TestCase(
            "E3",
            List.of(5),
            0,
            "Single element with value greater than zero"
        ));

        /*
         * Cross-check cases
         */
        tests.add(new TestCase(
            "X1",
            List.of(1, 2, 0, 1),
            bruteForceExpected(List.of(1, 2, 0, 1)),
            "Cross-check against tabulation"
        ));

        tests.add(new TestCase(
            "X2",
            List.of(2, 1, 0, 3),
            bruteForceExpected(List.of(2, 1, 0, 3)),
            "Cross-check against tabulation"
        ));

        tests.add(new TestCase(
            "X3",
            List.of(2, 5, 0, 0),
            bruteForceExpected(List.of(2, 5, 0, 0)),
            "Cross-check against tabulation"
        ));

        System.out.println("############################################################");
        System.out.println("######################## JUMP GAME II ######################");
        System.out.println("############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(
            new MethodCase("Recursion / Memoization O(n^2)", t -> jumpGameRecursion(t.nums)),
            new MethodCase("Tabulation O(n^2)", t -> jumpGameTabulation(t.nums)),
            new MethodCase("Greedy O(n)", t -> jumpGameGreedyAlgo(t.nums))
        );

        for (MethodCase method : methods) {
            runTests(method.name, method.method, tests);
        }
    }
}
