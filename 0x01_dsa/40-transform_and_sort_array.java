import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class SortTransformedArray {

    static class TestCase {
        final String id;
        final int[] nums;
        final int a;
        final int b;
        final int c;
        final int[] expected;
        final String description;

        TestCase(String id, int[] nums, int a, int b, int c, int[] expected, String description) {
            this.id = id;
            this.nums = nums;
            this.a = a;
            this.b = b;
            this.c = c;
            this.expected = expected;
            this.description = description;
        }
    }

    static class MethodCase {
        final String name;
        final Function<TestCase, int[]> method;

        MethodCase(String name, Function<TestCase, int[]> method) {
            this.name = name;
            this.method = method;
        }
    }

    /**
     * Applies f(x) = ax^2 + bx + c to each element and sorts using brute force.
     * Time: O(n log n)
     * Space: O(n)
     */
    static int[] sortTransformedArrayBruteForce(int[] nums, int a, int b, int c) {
        if (nums == null || nums.length == 0) {
            return new int[0];
        }

        int n = nums.length;
        int[] transformed = new int[n];

        for (int i = 0; i < n; i++) {
            transformed[i] = transform(nums[i], a, b, c);
        }

        Arrays.sort(transformed);
        return transformed;
    }

    /**
     * Two-pointer solution for the sorted input array.
     * Time: O(n)
     * Space: O(n)
     */
    static int[] sortTransformedArrayTwoPointers(int[] nums, int a, int b, int c) {
        if (nums == null || nums.length == 0) {
            return new int[0];
        }

        int n = nums.length;
        int[] result = new int[n];

        int left = 0;
        int right = n - 1;
        int idx = a >= 0 ? n - 1 : 0;

        while (left <= right) {
            int leftVal = transform(nums[left], a, b, c);
            int rightVal = transform(nums[right], a, b, c);

            if (a >= 0) {
                if (leftVal >= rightVal) {
                    result[idx--] = leftVal;
                    left++;
                } else {
                    result[idx--] = rightVal;
                    right--;
                }
            } else {
                if (leftVal <= rightVal) {
                    result[idx++] = leftVal;
                    left++;
                } else {
                    result[idx++] = rightVal;
                    right--;
                }
            }
        }

        return result;
    }

    static int transform(int x, int a, int b, int c) {
        return a * x * x + b * x + c;
    }

    static boolean arraysEqual(int[] a, int[] b) {
        return Arrays.equals(a, b);
    }

    static String formatArray(int[] nums) {
        return nums == null ? "null" : Arrays.toString(nums);
    }

    static void runTests(String algorithm, Function<TestCase, int[]> method, List<TestCase> tests) {
        System.out.println("==================================================");
        System.out.println(algorithm);
        System.out.println("==================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {
            try {
                int actual = 0; // placeholder to satisfy compiler? no
            } catch (Exception ex) {
                // no-op
            }
        }

        for (TestCase test : tests) {
            try {
                int[] actual = method.apply(test);

                if (arraysEqual(actual, test.expected)) {
                    passed++;
                    System.out.printf("✓ %s (%s)%n", test.id, test.description);
                } else {
                    failed++;
                    System.out.printf("✗ %s (%s)%n", test.id, test.description);
                    System.out.printf("  nums      = %s%n", formatArray(test.nums));
                    System.out.printf("  a,b,c     = %d,%d,%d%n", test.a, test.b, test.c);
                    System.out.printf("  expected  = %s%n", formatArray(test.expected));
                    System.out.printf("  actual    = %s%n", formatArray(actual));
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
            new int[]{-4, -2, 2, 4},
            1, 3, 5,
            new int[]{3, 9, 15, 33},
            "Classic example with a positive a"
        ));

        tests.add(new TestCase(
            "S2",
            new int[]{-4, -2, 2, 4},
            -1, 3, 5,
            new int[]{-23, -5, 1, 7},
            "Classic example with a negative a"
        ));

        /*
         * Simple cases
         */
        tests.add(new TestCase(
            "M1",
            new int[]{1},
            2, 3, 4,
            new int[]{9},
            "Single element array"
        ));

        tests.add(new TestCase(
            "M2",
            new int[]{-2, 0, 2},
            0, 2, 1,
            new int[]{-3, 1, 5},
            "Linear transformation with a = 0"
        ));

        tests.add(new TestCase(
            "M3",
            new int[]{-3, -1, 0, 1, 2},
            1, 0, 0,
            new int[]{0, 1, 1, 4, 9},
            "Pure quadratic with a positive coefficient"
        ));

        /*
         * No special shape needed
         */
        tests.add(new TestCase(
            "N1",
            new int[]{-3, -2, -1},
            1, 0, 0,
            new int[]{1, 4, 9},
            "All negative input values"
        ));

        tests.add(new TestCase(
            "N2",
            new int[]{1, 2, 3},
            -1, 0, 0,
            new int[]{-9, -4, -1},
            "All positive input values with negative a"
        ));

        tests.add(new TestCase(
            "N3",
            new int[]{0, 1, 2, 3},
            0, 0, 7,
            new int[]{7, 7, 7, 7},
            "Constant function"
        ));

        /*
         * Edge cases
         */
        tests.add(new TestCase(
            "E1",
            null,
            1, 2, 3,
            new int[0],
            "Null input"
        ));

        tests.add(new TestCase(
            "E2",
            new int[]{},
            1, 2, 3,
            new int[0],
            "Empty input"
        ));

        tests.add(new TestCase(
            "E3",
            new int[]{5, 6},
            0, -1, 0,
            new int[]{-6, -5},
            "Small array with linear decreasing transform"
        ));

        /*
         * Multiple values with duplicates
         */
        tests.add(new TestCase(
            "C1",
            new int[]{-2, -2, 0, 1, 1, 3},
            1, -2, 1,
            new int[]{-3, -3, 1, 1, 3, 4},
            "Handles duplicate input values"
        ));

        tests.add(new TestCase(
            "C2",
            new int[]{-5, -3, -1, 0, 2, 4},
            -2, 5, 1,
            sortTransformedArrayBruteForce(new int[]{-5, -3, -1, 0, 2, 4}, -2, 5, 1),
            "Cross-check expected output with brute force"
        ));

        System.out.println("############################################################");
        System.out.println("############ SORT TRANSFORMED ARRAY ########################");
        System.out.println("############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(
            new MethodCase("Brute Force O(n log n)", t ->
                sortTransformedArrayBruteForce(t.nums, t.a, t.b, t.c)
            ),
            new MethodCase("Two Pointers O(n)", t ->
                sortTransformedArrayTwoPointers(t.nums, t.a, t.b, t.c)
            )
        );

        for (MethodCase method : methods) {
            runTests(method.name, method.method, tests);
        }
    }
}
