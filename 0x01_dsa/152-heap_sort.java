import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class HeapSort {

    static record Result(
        int[] result,
        boolean valid
    ) {}

    static boolean validArray(int[] array) {
        return array != null && array.length > 0;
    }

    /**
     * Heap sort using a max-heap.
     *
     * Time: O(n log n)
     * Extra space: O(1), excluding the defensive input copy
     */
    static Result heapSort(int[] array) {
        if (!validArray(array)) {
            return new Result(new int[0], false);
        }

        int[] result = array.clone();
        int n = result.length;

        /*
         * Build a max-heap.
         * Leaves already satisfy the heap property.
         */
        for (int i = n / 2 - 1; i >= 0; i--) {
            heapify(result, n, i);
        }

        /*
         * Move the largest item to the end,
         * shrink the active heap, and restore heap order.
         */
        for (int end = n - 1; end > 0; end--) {
            swap(result, 0, end);
            heapify(result, end, 0);
        }

        return new Result(result, true);
    }

    /**
     * Restores the max-heap property for the subtree rooted at i.
     *
     * The heap is considered to occupy indices [0, n).
     */
    static void heapify(int[] array, int n, int i) {
        int current = i;

        while (true) {
            int left = 2 * current + 1;
            int right = 2 * current + 2;
            int largest = current;

            if (
                left < n
                && array[left] > array[largest]
            ) {
                largest = left;
            }

            if (
                right < n
                && array[right] > array[largest]
            ) {
                largest = right;
            }

            if (largest == current) {
                return;
            }

            swap(array, current, largest);
            current = largest;
        }
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
        final int[] expected;
        final boolean expectedValid;
        final String description;

        TestCase(
            String id,
            int[] input,
            int[] expected,
            boolean expectedValid,
            String description
        ) {
            this.id = id;
            this.input = input;
            this.expected = expected;
            this.expectedValid = expectedValid;
            this.description = description;
        }
    }

    static void runTests(
        String algorithm,
        Function<TestCase, Result> method,
        List<TestCase> tests
    ) {
        System.out.println("==================================================");
        System.out.println(algorithm);
        System.out.println("==================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {
            try {
                Result actual = method.apply(test);

                if (
                    actual.valid() == test.expectedValid
                    && Arrays.equals(
                        actual.result(),
                        test.expected
                    )
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
                        "  input    = %s%n",
                        Arrays.toString(test.input)
                    );
                    System.out.printf(
                        "  expected = %s, valid=%s%n",
                        Arrays.toString(test.expected),
                        test.expectedValid
                    );
                    System.out.printf(
                        "  actual   = %s, valid=%s%n",
                        Arrays.toString(actual.result()),
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

    public static void main(String[] args) {
        List<TestCase> tests = new ArrayList<>();

        tests.add(new TestCase(
            "S1",
            new int[]{4, 10, 3, 5, 1},
            new int[]{1, 3, 4, 5, 10},
            true,
            "Ordinary unsorted array"
        ));

        tests.add(new TestCase(
            "S2",
            new int[]{5, 4, 3, 2, 1},
            new int[]{1, 2, 3, 4, 5},
            true,
            "Reverse-sorted array"
        ));

        tests.add(new TestCase(
            "S3",
            new int[]{1, 2, 3, 4, 5},
            new int[]{1, 2, 3, 4, 5},
            true,
            "Already sorted array"
        ));

        tests.add(new TestCase(
            "S4",
            new int[]{2, 2, 1, 3, 2},
            new int[]{1, 2, 2, 2, 3},
            true,
            "Duplicate values"
        ));

        tests.add(new TestCase(
            "S5",
            new int[]{-5, 0, 10, -2, 3},
            new int[]{-5, -2, 0, 3, 10},
            true,
            "Negative and positive values"
        ));

        tests.add(new TestCase(
            "S6",
            new int[]{7},
            new int[]{7},
            true,
            "Single-element array"
        ));

        tests.add(new TestCase(
            "S7",
            new int[]{
                Integer.MAX_VALUE,
                Integer.MIN_VALUE,
                0
            },
            new int[]{
                Integer.MIN_VALUE,
                0,
                Integer.MAX_VALUE
            },
            true,
            "Integer boundary values"
        ));

        tests.add(new TestCase(
            "E1",
            null,
            new int[0],
            false,
            "Null array"
        ));

        tests.add(new TestCase(
            "E2",
            new int[]{},
            new int[0],
            false,
            "Empty array"
        ));

        System.out.println("############################################################");
        System.out.println("######################## HEAP SORT ##########################");
        System.out.println("############################################################");
        System.out.println();

        runTests(
            "Heap Sort O(n log n)",
            test -> heapSort(test.input),
            tests
        );
    }
}
