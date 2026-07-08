import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class MinimumSwapsAllOnes {

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
     * Brute-force solution.
     * Time: O(n * k), where k is the number of 1s
     * Space: O(1)
     */
    static int minimumSwapsAllOnesNestedLoops(int[] nums) {
        if (nums == null || nums.length == 0) {
            return -1;
        }

        int ones = countOnes(nums);
        if (ones == 0) {
            return -1;
        }

        if (ones == 1) {
            return 0;
        }

        int minSwaps = Integer.MAX_VALUE;

        for (int start = 0; start <= nums.length - ones; start++) {
            int onesInWindow = 0;
            for (int end = start; end < start + ones; end++) {
                if (nums[end] == 1) {
                    onesInWindow++;
                }
            }
            minSwaps = Math.min(minSwaps, ones - onesInWindow);
        }

        return minSwaps;
    }

    /**
     * Sliding window solution.
     * Time: O(n)
     * Space: O(1)
     */
    static int minimumSwapsAllOnesMaintainingWindowK(int[] nums) {
        if (nums == null || nums.length == 0) {
            return -1;
        }

        int ones = countOnes(nums);
        if (ones == 0) {
            return -1;
        }

        if (ones == 1) {
            return 0;
        }

        int currentOnes = 0;
        for (int i = 0; i < ones; i++) {
            if (nums[i] == 1) {
                currentOnes++;
            }
        }

        int maxOnes = currentOnes;

        for (int right = ones; right < nums.length; right++) {
            if (nums[right - ones] == 1) {
                currentOnes--;
            }
            if (nums[right] == 1) {
                currentOnes++;
            }
            maxOnes = Math.max(maxOnes, currentOnes);
        }

        return ones - maxOnes;
    }

    static int countOnes(int[] nums) {
        int count = 0;
        for (int num : nums) {
            if (num == 1) {
                count++;
            }
        }
        return count;
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

    public static void main(String[] args) {
        List<TestCase> tests = new ArrayList<>();

        /*
         * Standard examples
         */
        tests.add(new TestCase(
            "S1",
            new int[]{1, 0, 1, 0, 1},
            1,
            "Alternating bits with three ones"
        ));

        tests.add(new TestCase(
            "S2",
            new int[]{1, 1, 0, 0, 1},
            0,
            "Already grouped ones"
        ));

        /*
         * Simple cases
         */
        tests.add(new TestCase(
            "M1",
            new int[]{1},
            0,
            "Single element one"
        ));

        tests.add(new TestCase(
            "M2",
            new int[]{0},
            -1,
            "Single zero"
        ));

        tests.add(new TestCase(
            "M3",
            new int[]{0, 0, 0, 0},
            -1,
            "No ones present"
        ));

        /*
         * All ones / all zeros
         */
        tests.add(new TestCase(
            "N1",
            new int[]{1, 1, 1, 1},
            0,
            "All ones already grouped"
        ));

        tests.add(new TestCase(
            "N2",
            new int[]{0, 0, 0},
            -1,
            "All zeros"
        ));

        /*
         * Mixed cases
         */
        tests.add(new TestCase(
            "C1",
            new int[]{1, 0, 1, 1, 0, 1},
            1,
            "One swap needed"
        ));

        tests.add(new TestCase(
            "C2",
            new int[]{0, 1, 0, 1, 1, 0, 0},
            1,
            "Typical sliding window case"
        ));

        tests.add(new TestCase(
            "C3",
            new int[]{1, 0, 0, 1, 0, 1, 1, 0},
            1,
            "Multiple possible windows"
        ));

        tests.add(new TestCase(
            "C4",
            new int[]{1, 0, 1, 0, 1, 0, 1, 0},
            2,
            "Evenly spaced ones"
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
            new int[]{1, 0},
            0,
            "Two element array with one one"
        ));

        /*
         * Cross-check cases
         */
        tests.add(new TestCase(
            "X1",
            new int[]{1, 1, 0, 1, 0, 1, 0, 0, 1},
            minimumSwapsAllOnesBruteForceExpected(new int[]{1, 1, 0, 1, 0, 1, 0, 0, 1}),
            "Expected value computed with brute force helper"
        ));

        tests.add(new TestCase(
            "X2",
            new int[]{0, 1, 1, 0, 1, 0, 1, 1, 0, 0},
            minimumSwapsAllOnesBruteForceExpected(new int[]{0, 1, 1, 0, 1, 0, 1, 1, 0, 0}),
            "Another brute-force cross-check"
        ));

        System.out.println("############################################################");
        System.out.println("############ MINIMUM SWAPS ALL ONES ########################");
        System.out.println("############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(
            new MethodCase("Brute Force O(n * k)", t -> minimumSwapsAllOnesNestedLoops(t.nums)),
            new MethodCase("Sliding Window O(n)", t -> minimumSwapsAllOnesMaintainingWindowK(t.nums))
        );

        for (MethodCase method : methods) {
            runTests(method.name, method.method, tests);
        }
    }

    static int minimumSwapsAllOnesBruteForceExpected(int[] nums) {
        return minimumSwapsAllOnesNestedLoops(nums);
    }
}
