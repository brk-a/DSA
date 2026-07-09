import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class MinimumMovesEqualiseArray {

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
     * Repeatedly increments every element until all values are equal.
     * Time: O(m * n log n), where m is the number of moves
     * Space: O(n)
     */
    static int minimumMovesEqualiseArrayBruteForce(int[] nums) {
        if (nums == null || nums.length == 0) {
            return -1;
        }

        if (nums.length == 1) {
            return 0;
        }

        int[] clone = nums.clone();
        Arrays.sort(clone);

        int moves = 0;
        while (clone[0] != clone[clone.length - 1]) {
            for (int i = 0; i < clone.length; i++) {
                clone[i]++;
            }
            moves++;
            Arrays.sort(clone);
        }

        return moves;
    }

    /**
     * Optimal mathematical solution.
     * Minimum moves = sum(nums) - n * min(nums)
     * Time: O(n)
     * Space: O(1)
     */
    static int minimumMovesEqualiseArrayMathsFormula(int[] nums) {
        if (nums == null || nums.length == 0) {
            return -1;
        }

        long sum = 0L;
        int minVal = Integer.MAX_VALUE;

        for (int num : nums) {
            sum += num;
            minVal = Math.min(minVal, num);
        }

        long result = sum - (long) nums.length * minVal;
        return (int) result;
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

    static int minimumMovesEqualiseArrayBruteForceExpected(int[] nums) {
        return minimumMovesEqualiseArrayBruteForce(nums);
    }

    public static void main(String[] args) {
        List<TestCase> tests = new ArrayList<>();

        /*
         * Standard examples
         */
        tests.add(new TestCase(
            "S1",
            new int[]{1, 2, 3},
            3,
            "Classic example with increasing sequence"
        ));

        tests.add(new TestCase(
            "S2",
            new int[]{5, 5, 5},
            0,
            "Already equal values"
        ));

        /*
         * Simple cases
         */
        tests.add(new TestCase(
            "M1",
            new int[]{1},
            0,
            "Single element"
        ));

        tests.add(new TestCase(
            "M2",
            new int[]{0},
            0,
            "Single zero"
        ));

        tests.add(new TestCase(
            "M3",
            new int[]{2, 2},
            0,
            "Two equal elements"
        ));

        /*
         * Mixed cases
         */
        tests.add(new TestCase(
            "C1",
            new int[]{1, 2, 3, 4},
            6,
            "Ascending values"
        ));

        tests.add(new TestCase(
            "C2",
            new int[]{1, 1, 2},
            1,
            "One move needed"
        ));

        tests.add(new TestCase(
            "C3",
            new int[]{3, 1, 2},
            3,
            "Unsorted values"
        ));

        tests.add(new TestCase(
            "C4",
            new int[]{-1, 0, 1},
            3,
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
            "All equal elements"
        ));

        /*
         * Cross-check cases
         */
        tests.add(new TestCase(
            "X1",
            new int[]{4, 1, 7, 2, 2},
            minimumMovesEqualiseArrayBruteForceExpected(new int[]{4, 1, 7, 2, 2}),
            "Brute-force computed expected value"
        ));

        tests.add(new TestCase(
            "X2",
            new int[]{10, 3, 10, 1},
            minimumMovesEqualiseArrayBruteForceExpected(new int[]{10, 3, 10, 1}),
            "Another brute-force cross-check"
        ));

        tests.add(new TestCase(
            "X3",
            new int[]{100, 101, 102},
            minimumMovesEqualiseArrayBruteForceExpected(new int[]{100, 101, 102}),
            "Small spread cross-check"
        ));

        System.out.println("############################################################");
        System.out.println("########### MINIMUM MOVES EQUALISE ARRAY ###################");
        System.out.println("############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(
            new MethodCase("Brute Force Reference", t -> minimumMovesEqualiseArrayBruteForce(t.nums)),
            new MethodCase("Math Formula O(n)", t -> minimumMovesEqualiseArrayMathsFormula(t.nums))
        );

        for (MethodCase method : methods) {
            runTests(method.name, method.method, tests);
        }
    }
}
