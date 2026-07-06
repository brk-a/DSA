import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class LongestMountainSubArray {

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

    /**
     * Returns the length of the longest mountain subarray.
     * A mountain must have:
     * - length >= 3
     * - strictly increasing then strictly decreasing
     */
    static int longestMountain(int[] nums) {
        if (nums == null || nums.length < 3) {
            return 0;
        }

        int n = nums.length;
        int longest = 0;
        int i = 1;

        while (i < n - 1) {
            boolean isPeak = nums[i - 1] < nums[i] && nums[i] > nums[i + 1];

            if (!isPeak) {
                i++;
                continue;
            }

            int left = i - 1;
            while (left > 0 && nums[left - 1] < nums[left]) {
                left--;
            }

            int right = i + 1;
            while (right < n - 1 && nums[right] > nums[right + 1]) {
                right++;
            }

            longest = Math.max(longest, right - left + 1);
            i = right;
        }

        return longest;
    }

    static int longestMountainBruteForce(int[] nums) {
        return longestMountain(nums);
    }

    static int longestMountainPeakExpansion(int[] nums) {
        return longestMountain(nums);
    }

    static int longestMountainTwoPointers(int[] nums) {
        return longestMountain(nums);
    }

    static class MethodCase {
        final String name;
        final Function<int[], Integer> method;

        MethodCase(String name, Function<int[], Integer> method) {
            this.name = name;
            this.method = method;
        }
    }

    static boolean arraysEqual(int a, int b) {
        return a == b;
    }

    static String formatArray(int[] nums) {
        return nums == null ? "null" : Arrays.toString(nums);
    }

    static void runTests(String algorithm, Function<int[], Integer> method, List<TestCase> tests) {
        System.out.println("==================================================");
        System.out.println(algorithm);
        System.out.println("==================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {
            try {
                int actual = method.apply(test.nums);

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
            new int[]{2, 1, 4, 7, 3, 2, 5},
            5,
            "Classic example with mountain [1,4,7,3,2]"
        ));

        tests.add(new TestCase(
            "S2",
            new int[]{2, 2, 2},
            0,
            "All equal values have no mountain"
        ));

        /*
         * Simple mountains
         */
        tests.add(new TestCase(
            "M1",
            new int[]{1, 3, 2},
            3,
            "Smallest valid mountain"
        ));

        tests.add(new TestCase(
            "M2",
            new int[]{1, 2, 3, 4, 3, 2, 1},
            7,
            "Whole array is a mountain"
        ));

        tests.add(new TestCase(
            "M3",
            new int[]{0, 1, 2, 1, 0},
            5,
            "Mountain centered in the array"
        ));

        /*
         * No mountain
         */
        tests.add(new TestCase(
            "N1",
            new int[]{1, 2, 3, 4, 5},
            0,
            "Strictly increasing only"
        ));

        tests.add(new TestCase(
            "N2",
            new int[]{5, 4, 3, 2, 1},
            0,
            "Strictly decreasing only"
        ));

        tests.add(new TestCase(
            "N3",
            new int[]{1, 2, 2, 3, 4},
            0,
            "Plateau prevents a mountain"
        ));

        tests.add(new TestCase(
            "N4",
            new int[]{1, 1, 1, 1},
            0,
            "Flat array"
        ));

        /*
         * Multiple mountains
         */
        tests.add(new TestCase(
            "C1",
            new int[]{2, 1, 4, 7, 3, 2, 5, 6, 4, 2},
            5,
            "Multiple mountains, longest is length 5"
        ));

        tests.add(new TestCase(
            "C2",
            new int[]{0, 2, 3, 4, 5, 2, 1, 0, 1, 2, 1},
            8,
            "Longest mountain appears first"
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
            "Empty array"
        ));

        tests.add(new TestCase(
            "E3",
            new int[]{1, 2},
            0,
            "Too short to form a mountain"
        ));

        tests.add(new TestCase(
            "E4",
            new int[]{1, 2, 1},
            3,
            "Exactly one peak with length 3"
        ));

        System.out.println("############################################################");
        System.out.println("############ LONGEST MOUNTAIN SUBARRAY #####################");
        System.out.println("############################################################");
        System.out.println();

        runTests(
            "Peak Expansion O(n)",
            LongestMountainSubArray::longestMountain,
            tests
        );
    }
}
