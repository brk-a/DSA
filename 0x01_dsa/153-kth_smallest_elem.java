import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.BiFunction;

public class KthSmallestElement {

    static record Result(
        int result,
        boolean valid
    ) {}

    static boolean validArray(int[] array) {
        return array != null && array.length > 0;
    }

    static boolean validK(int[] array, int k) {
        return validArray(array)
            && k >= 1
            && k <= array.length;
    }

    /**
     * Sorting solution.
     *
     * Time: O(n log n)
     * Space: O(n), because the input is copied.
     */
    static Result kthSmallestElemSorting(
        int[] array,
        int k
    ) {
        if (!validK(array, k)) {
            return new Result(-1, false);
        }

        int[] sorted = array.clone();
        Arrays.sort(sorted);

        return new Result(sorted[k - 1], true);
    }

    /**
     * Max-heap containing the k smallest values seen so far.
     *
     * The largest among those k values stays at the heap root.
     *
     * Time: O(n log k)
     * Space: O(k)
     */
    static Result kthSmallestElemMaxHeap(
        int[] array,
        int k
    ) {
        if (!validK(array, k)) {
            return new Result(-1, false);
        }

        PriorityQueue<Integer> maxHeap =
            new PriorityQueue<>(
                Collections.reverseOrder()
            );

        for (int value : array) {
            maxHeap.offer(value);

            if (maxHeap.size() > k) {
                maxHeap.poll();
            }
        }

        return new Result(maxHeap.peek(), true);
    }

    /**
     * Iterative Quickselect solution.
     *
     * Expected time: O(n)
     * Worst-case time: O(n^2)
     * Extra space: O(n), because the input is copied.
     */
    static Result kthSmallestElemQuickSelect(
        int[] array,
        int k
    ) {
        if (!validK(array, k)) {
            return new Result(-1, false);
        }

        int[] working = array.clone();
        int targetIndex = k - 1;

        int left = 0;
        int right = working.length - 1;

        while (left <= right) {
            int pivotIndex = chooseMiddlePivot(left, right);

            int finalIndex = partition(
                working,
                left,
                right,
                pivotIndex
            );

            if (finalIndex == targetIndex) {
                return new Result(
                    working[finalIndex],
                    true
                );
            }

            if (targetIndex < finalIndex) {
                right = finalIndex - 1;
            } else {
                left = finalIndex + 1;
            }
        }

        return new Result(-1, false);
    }

    static int chooseMiddlePivot(
        int left,
        int right
    ) {
        return left + (right - left) / 2;
    }

    static int partition(
        int[] array,
        int left,
        int right,
        int pivotIndex
    ) {
        int pivotValue = array[pivotIndex];

        swap(array, pivotIndex, right);

        int storeIndex = left;

        for (int i = left; i < right; i++) {
            if (array[i] < pivotValue) {
                swap(array, i, storeIndex);
                storeIndex++;
            }
        }

        swap(array, storeIndex, right);

        return storeIndex;
    }

    /**
     * Counting-sort solution.
     *
     * This version is suitable when the numeric range is reasonably small.
     *
     * Time: O(n + range)
     * Space: O(range)
     */
    static Result kthSmallestElemCountingSort(
        int[] array,
        int k
    ) {
        if (!validK(array, k)) {
            return new Result(-1, false);
        }

        int minimum = array[0];
        int maximum = array[0];

        for (int value : array) {
            minimum = Math.min(minimum, value);
            maximum = Math.max(maximum, value);
        }

        long rangeLong =
            (long) maximum - minimum + 1L;

        /*
         * Avoid allocating an impractically large counting array.
         * In that case, this method reports invalid rather than
         * risking an OutOfMemoryError.
         */
        if (rangeLong > Integer.MAX_VALUE) {
            return new Result(-1, false);
        }

        int range = (int) rangeLong;
        int[] frequency = new int[range];

        for (int value : array) {
            frequency[value - minimum]++;
        }

        int remaining = k;

        for (int offset = 0; offset < frequency.length; offset++) {
            remaining -= frequency[offset];

            if (remaining <= 0) {
                return new Result(
                    offset + minimum,
                    true
                );
            }
        }

        return new Result(-1, false);
    }

    static void swap(
        int[] array,
        int first,
        int second
    ) {
        int temporary = array[first];
        array[first] = array[second];
        array[second] = temporary;
    }

    static class TestCase {
        final String id;
        final int[] input;
        final int k;
        final int expected;
        final boolean expectedValid;
        final String description;

        TestCase(
            String id,
            int[] input,
            int k,
            int expected,
            boolean expectedValid,
            String description
        ) {
            this.id = id;
            this.input = input;
            this.k = k;
            this.expected = expected;
            this.expectedValid = expectedValid;
            this.description = description;
        }
    }

    static void runTests(
        String algorithm,
        BiFunction<int[], Integer, Result> method,
        List<TestCase> tests
    ) {
        System.out.println("==================================================");
        System.out.println(algorithm);
        System.out.println("==================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {
            try {
                Result actual = method.apply(
                    test.input,
                    test.k
                );

                if (
                    actual.valid() == test.expectedValid
                    && actual.result() == test.expected
                ) {
                    passed++;
                    System.out.printf(
                        "✓ %s (%s)%n",
                        test.id,
                        test.description
                    );
                } else {
                    failed++;
                    System.out.printf(
                        "✗ %s (%s)%n",
                        test.id,
                        test.description
                    );
                    System.out.printf(
                        "  input    = %s, k=%d%n",
                        Arrays.toString(test.input),
                        test.k
                    );
                    System.out.printf(
                        "  expected = Result(%d, %s)%n",
                        test.expected,
                        test.expectedValid
                    );
                    System.out.printf(
                        "  actual   = Result(%d, %s)%n",
                        actual.result(),
                        actual.valid()
                    );
                }
            } catch (Exception ex) {
                failed++;
                System.out.printf(
                    "✗ %s (%s)%n",
                    test.id,
                    test.description
                );
                System.out.printf(
                    "  exception = %s%n",
                    ex.getMessage()
                );
            }
        }

        System.out.printf(
            "%nResults: %d passed, %d failed, %d total%n%n",
            passed,
            failed,
            tests.size()
        );
    }

    static List<TestCase> createTests() {
        List<TestCase> tests = new ArrayList<>();

        tests.add(new TestCase(
            "S1",
            new int[]{7, 10, 4, 3, 20, 15},
            3,
            7,
            true,
            "Ordinary unsorted array"
        ));

        tests.add(new TestCase(
            "S2",
            new int[]{7, 10, 4, 3, 20, 15},
            1,
            3,
            true,
            "Smallest element"
        ));

        tests.add(new TestCase(
            "S3",
            new int[]{7, 10, 4, 3, 20, 15},
            6,
            20,
            true,
            "Largest element"
        ));

        tests.add(new TestCase(
            "S4",
            new int[]{5, 5, 2, 2, 9, 1},
            4,
            5,
            true,
            "Duplicate values"
        ));

        tests.add(new TestCase(
            "S5",
            new int[]{-10, 4, -3, 2, -10},
            2,
            -10,
            true,
            "Negative values"
        ));

        tests.add(new TestCase(
            "S6",
            new int[]{8},
            1,
            8,
            true,
            "Single-element array"
        ));

        tests.add(new TestCase(
            "S7",
            new int[]{
                Integer.MIN_VALUE,
                0,
                Integer.MAX_VALUE
            },
            2,
            0,
            true,
            "Integer boundary values"
        ));

        tests.add(new TestCase(
            "E1",
            null,
            1,
            -1,
            false,
            "Null array"
        ));

        tests.add(new TestCase(
            "E2",
            new int[]{},
            1,
            -1,
            false,
            "Empty array"
        ));

        tests.add(new TestCase(
            "E3",
            new int[]{1, 2, 3},
            0,
            -1,
            false,
            "Zero is not a valid rank"
        ));

        tests.add(new TestCase(
            "E4",
            new int[]{1, 2, 3},
            4,
            -1,
            false,
            "k is larger than array length"
        ));

        return tests;
    }

    public static void main(String[] args) {
        List<TestCase> tests = createTests();

        System.out.println("############################################################");
        System.out.println("################ KTH SMALLEST ELEMENT ######################");
        System.out.println("############################################################");
        System.out.println();

        runTests(
            "Sorting O(n log n)",
            KthSmallestElement::kthSmallestElemSorting,
            tests
        );

        runTests(
            "Max Heap O(n log k)",
            KthSmallestElement::kthSmallestElemMaxHeap,
            tests
        );

        runTests(
            "Quickselect Expected O(n)",
            KthSmallestElement::kthSmallestElemQuickSelect,
            tests
        );

        runTests(
            "Counting Sort O(n + range)",
            KthSmallestElement::kthSmallestElemCountingSort,
            tests
        );
    }
}
