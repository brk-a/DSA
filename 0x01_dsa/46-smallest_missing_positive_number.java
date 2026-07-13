import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class SmallestMissingPositiveNumber {

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
     * Sorting-based solution.
     * Time: O(n log n)
     * Space: O(n) because we clone the input to avoid mutating it.
     */
    static int smallestMissingPositiveNumberSorting(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 1;
        }

        int[] clone = nums.clone();
        Arrays.sort(clone);

        int result = 1;
        for (int value : clone) {
            if (value == result) {
                result++;
            } else if (value > result) {
                break;
            }
        }

        return result;
    }

    /**
     * Visited-array solution.
     * Time: O(n)
     * Space: O(n)
     */
    static int smallestMissingPositiveNumberVisitedArray(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 1;
        }

        int n = nums.length;
        boolean[] visited = new boolean[n];

        for (int num : nums) {
            if (num > 0 && num <= n) {
                visited[num - 1] = true;
            }
        }

        for (int i = 0; i < n; i++) {
            if (!visited[i]) {
                return i + 1;
            }
        }

        return n + 1;
    }

    /**
     * Cyclic-sort solution.
     * Time: O(n)
     * Space: O(1)
     */
    static int smallestMissingPositiveNumberCycleSort(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 1;
        }

        int n = nums.length;

        for (int i = 0; i < n; i++) {
            while (nums[i] > 0 && nums[i] <= n && nums[i] != nums[nums[i] - 1]) {
                swap(nums, i, nums[i] - 1);
            }
        }

        for (int i = 0; i < n; i++) {
            if (nums[i] != i + 1) {
                return i + 1;
            }
        }

        return n + 1;
    }

    /**
     * Negation-based solution.
     * First partitions positives to the front, then marks presence by negating.
     * Time: O(n)
     * Space: O(1)
     */
    static int smallestMissingPositiveNumberNegatingElements(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 1;
        }

        int[] arr = nums.clone();
        int k = partitionArray(arr);

        for (int i = 0; i < k; i++) {
            int value = Math.abs(arr[i]);
            if (value >= 1 && value <= k && arr[value - 1] > 0) {
                arr[value - 1] = -arr[value - 1];
            }
        }

        for (int i = 0; i < k; i++) {
            if (arr[i] > 0) {
                return i + 1;
            }
        }

        return k + 1;
    }

    /**
     * Marking-indices solution.
     * Time: O(n)
     * Space: O(1)
     */
    static int smallestMissingPositiveNumberMarkingIndices(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 1;
        }

        int[] arr = nums.clone();
        int n = arr.length;

        boolean hasOne = false;
        for (int num : arr) {
            if (num == 1) {
                hasOne = true;
                break;
            }
        }

        if (!hasOne) {
            return 1;
        }

        for (int i = 0; i < n; i++) {
            if (arr[i] <= 0 || arr[i] > n) {
                arr[i] = 1;
            }
        }

        for (int i = 0; i < n; i++) {
            int idx = Math.abs(arr[i]) - 1;
            if (arr[idx] > 0) {
                arr[idx] = -arr[idx];
            }
        }

        for (int i = 0; i < n; i++) {
            if (arr[i] > 0) {
                return i + 1;
            }
        }

        return n + 1;
    }

    // Helpers

    static void swap(int[] arr, int i, int j) {
        if (i == j) {
            return;
        }
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    static int partitionArray(int[] arr) {
        int pivotIdx = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > 0) {
                swap(arr, i, pivotIdx);
                pivotIdx++;
            }
        }
        return pivotIdx;
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
                int[] input = test.nums == null ? null : test.nums.clone();
                int actual = method.apply(new TestCase(test.id, input, test.expected, test.description));

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
        if (nums == null || nums.length == 0) {
            return 1;
        }

        int[] clone = nums.clone();
        Arrays.sort(clone);

        int result = 1;
        for (int value : clone) {
            if (value == result) {
                result++;
            } else if (value > result) {
                break;
            }
        }

        return result;
    }

    public static void main(String[] args) {
        List<TestCase> tests = new ArrayList<>();

        /*
         * Standard examples
         */
        tests.add(new TestCase(
            "S1",
            new int[]{1, 2, 0},
            3,
            "Simple case with 1, 2, and 0"
        ));

        tests.add(new TestCase(
            "S2",
            new int[]{3, 4, -1, 1},
            2,
            "Classic missing-positive example"
        ));

        tests.add(new TestCase(
            "S3",
            new int[]{7, 8, 9, 11, 12},
            1,
            "No 1 present"
        ));

        /*
         * Simple cases
         */
        tests.add(new TestCase(
            "M1",
            new int[]{1},
            2,
            "Single element one"
        ));

        tests.add(new TestCase(
            "M2",
            new int[]{2},
            1,
            "Single element greater than one"
        ));

        tests.add(new TestCase(
            "M3",
            new int[]{-1},
            1,
            "Single negative element"
        ));

        tests.add(new TestCase(
            "M4",
            new int[]{1, 2},
            3,
            "Two elements already in order"
        ));

        /*
         * Mixed cases
         */
        tests.add(new TestCase(
            "C1",
            new int[]{2, -3, 4, 1, 1, 7},
            3,
            "Mixed positives, duplicates, and negatives"
        ));

        tests.add(new TestCase(
            "C2",
            new int[]{5, 3, 2, 5, 1},
            4,
            "Missing value in the middle"
        ));

        tests.add(new TestCase(
            "C3",
            new int[]{2, 2, 2, 2},
            1,
            "Duplicates only"
        ));

        tests.add(new TestCase(
            "C4",
            new int[]{1, 1, 0, -1, -2},
            2,
            "Has 1, missing 2"
        ));

        tests.add(new TestCase(
            "C5",
            new int[]{4, 3, 2, 1},
            5,
            "All numbers 1 to n present"
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
            new int[]{0, 0, 0},
            1,
            "All zeros"
        ));

        tests.add(new TestCase(
            "E4",
            new int[]{-5, -2, -7, -3},
            1,
            "All negative values"
        ));

        tests.add(new TestCase(
            "E5",
            new int[]{Integer.MAX_VALUE, 1, 2},
            3,
            "Large values outside range"
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
            new int[]{2, -1, 2, -1, 2, -1},
            bruteForceExpected(new int[]{2, -1, 2, -1, 2, -1}),
            "Another brute-force cross-check"
        ));

        tests.add(new TestCase(
            "X3",
            new int[]{6, 5, 4, 3, 2, 1},
            bruteForceExpected(new int[]{6, 5, 4, 3, 2, 1}),
            "Reverse ordered positive sequence"
        ));

        System.out.println("############################################################");
        System.out.println("########### SMALLEST MISSING POSITIVE NUMBER ###############");
        System.out.println("############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(
            new MethodCase("Sorting O(n log n)", t -> smallestMissingPositiveNumberSorting(t.nums)),
            new MethodCase("Visited Array O(n)", t -> smallestMissingPositiveNumberVisitedArray(t.nums)),
            new MethodCase("Cycle Sort O(n)", t -> smallestMissingPositiveNumberCycleSort(t.nums)),
            new MethodCase("Negating Elements O(n)", t -> smallestMissingPositiveNumberNegatingElements(t.nums)),
            new MethodCase("Marking Indices O(n)", t -> smallestMissingPositiveNumberMarkingIndices(t.nums))
        );

        for (MethodCase method : methods) {
            runTests(method.name, method.method, tests);
        }
    }
}
