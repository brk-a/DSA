import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;

public class TrappingRainWater {

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
     * For each index, compute the highest bar on the left and on the right.
     * Time: O(n^2)
     * Space: O(1)
     */
    static int trappingRainWaterBruteForce(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int n = nums.length;
        int result = 0;

        for (int i = 1; i < n - 1; i++) {
            int leftMax = nums[i];
            for (int j = 0; j <= i; j++) {
                leftMax = Math.max(leftMax, nums[j]);
            }

            int rightMax = nums[i];
            for (int j = i; j < n; j++) {
                rightMax = Math.max(rightMax, nums[j]);
            }

            result += Math.min(leftMax, rightMax) - nums[i];
        }

        return result;
    }

    /**
     * Prefix/suffix maximum solution.
     * Time: O(n)
     * Space: O(n)
     */
    static int trappingRainWaterPrefixSuffix(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int n = nums.length;
        int result = 0;
        int[] leftMax = new int[n];
        int[] rightMax = new int[n];

        leftMax[0] = nums[0];
        for (int i = 1; i < n; i++) {
            leftMax[i] = Math.max(leftMax[i - 1], nums[i]);
        }

        rightMax[n - 1] = nums[n - 1];
        for (int i = n - 2; i >= 0; i--) {
            rightMax[i] = Math.max(rightMax[i + 1], nums[i]);
        }

        for (int i = 0; i < n; i++) {
            result += Math.min(leftMax[i], rightMax[i]) - nums[i];
        }

        return result;
    }

    /**
     * Two-pointer solution.
     * Time: O(n)
     * Space: O(1)
     */
    static int trappingRainWaterTwoPointers(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int left = 0;
        int right = nums.length - 1;
        int leftMax = 0;
        int rightMax = 0;
        int result = 0;

        while (left < right) {
            if (nums[left] <= nums[right]) {
                leftMax = Math.max(leftMax, nums[left]);
                result += leftMax - nums[left];
                left++;
            } else {
                rightMax = Math.max(rightMax, nums[right]);
                result += rightMax - nums[right];
                right--;
            }
        }

        return result;
    }

    /**
     * Monotonic stack solution.
     * Time: O(n)
     * Space: O(n)
     */
    static int trappingRainWaterStack(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int n = nums.length;
        Stack<Integer> stack = new Stack<>();
        int result = 0;

        for (int i = 0; i < n; i++) {
            while (!stack.isEmpty() && nums[stack.peek()] < nums[i]) {
                int bottomIndex = stack.pop();

                if (stack.isEmpty()) {
                    break;
                }

                int leftIndex = stack.peek();
                int distance = i - leftIndex - 1;
                int boundedHeight = Math.min(nums[leftIndex], nums[i]) - nums[bottomIndex];

                if (boundedHeight > 0) {
                    result += distance * boundedHeight;
                }
            }

            stack.push(i);
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
        return trappingRainWaterBruteForce(nums);
    }

    public static void main(String[] args) {
        List<TestCase> tests = new ArrayList<>();

        /*
         * Standard examples
         */
        tests.add(new TestCase(
            "S1",
            new int[]{0, 2, 0, 3, 1, 0, 1, 3, 2, 1},
            9,
            "Classic example"
        ));

        tests.add(new TestCase(
            "S2",
            new int[]{3, 0, 2, 0, 4},
            7,
            "Another standard example"
        ));

        /*
         * Simple cases
         */
        tests.add(new TestCase(
            "M1",
            new int[]{1},
            0,
            "Single bar cannot trap water"
        ));

        tests.add(new TestCase(
            "M2",
            new int[]{1, 1},
            0,
            "Two bars cannot trap water"
        ));

        tests.add(new TestCase(
            "M3",
            new int[]{0, 0, 0},
            0,
            "All zero values"
        ));

        /*
         * Mixed cases
         */
        tests.add(new TestCase(
            "C1",
            new int[]{1, 2, 3, 4},
            0,
            "Strictly increasing heights"
        ));

        tests.add(new TestCase(
            "C2",
            new int[]{4, 3, 2, 1},
            0,
            "Strictly decreasing heights"
        ));

        tests.add(new TestCase(
            "C3",
            new int[]{4, 1, 3, 1, 5},
            7,
            "Multiple basins"
        ));

        tests.add(new TestCase(
            "C4",
            new int[]{5, 2, 1, 2, 1, 5},
            14,
            "Deep container"
        ));

        tests.add(new TestCase(
            "C5",
            new int[]{2, 0, 2},
            2,
            "Single bounded pit"
        ));

        tests.add(new TestCase(
            "C6",
            new int[]{3, 1, 2, 1, 2, 1, 5},
            10,
            "Irregular terrain"
        ));

        /*
         * Edge cases
         */
        tests.add(new TestCase(
            "E1",
            null,
            0,
            "Null input"
        ));

        tests.add(new TestCase(
            "E2",
            new int[]{},
            0,
            "Empty input"
        ));

        tests.add(new TestCase(
            "E3",
            new int[]{10, 10, 10, 10},
            0,
            "Flat surface"
        ));

        /*
         * Cross-check cases
         */
        tests.add(new TestCase(
            "X1",
            new int[]{3, 0, 1, 0, 4, 0, 2},
            bruteForceExpected(new int[]{3, 0, 1, 0, 4, 0, 2}),
            "Brute-force computed expected value"
        ));

        tests.add(new TestCase(
            "X2",
            new int[]{2, 1, 0, 1, 3},
            bruteForceExpected(new int[]{2, 1, 0, 1, 3}),
            "Another brute-force cross-check"
        ));

        tests.add(new TestCase(
            "X3",
            new int[]{6, 0, 0, 0, 6},
            bruteForceExpected(new int[]{6, 0, 0, 0, 6}),
            "Wide basin cross-check"
        ));

        System.out.println("############################################################");
        System.out.println("############### TRAPPING RAIN WATER ########################");
        System.out.println("############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(
            new MethodCase("Brute Force O(n^2)", t -> trappingRainWaterBruteForce(t.nums)),
            new MethodCase("Prefix/Suffix O(n)", t -> trappingRainWaterPrefixSuffix(t.nums)),
            new MethodCase("Two Pointers O(n)", t -> trappingRainWaterTwoPointers(t.nums)),
            new MethodCase("Stack O(n)", t -> trappingRainWaterStack(t.nums))
        );

        for (MethodCase method : methods) {
            runTests(method.name, method.method, tests);
        }
    }
}
