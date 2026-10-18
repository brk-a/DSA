import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.Function;

public class MedianOfStream {

    static record Result(
        ArrayList<Double> result,
        boolean valid
    ) {}

    static boolean validArray(int[] array) {
        return array != null && array.length > 0;
    }

    /**
     * Insertion-sort solution.
     *
     * Maintains a sorted prefix after every insertion.
     *
     * Time: O(n^2)
     * Space: O(n)
     */
    static Result medianOfStreamInsertionSort(int[] array) {
        if (!validArray(array)) {
            return new Result(new ArrayList<>(), false);
        }

        int[] sorted = Arrays.copyOf(array, array.length);
        ArrayList<Double> result = new ArrayList<>();

        for (int i = 0; i < sorted.length; i++) {
            int current = sorted[i];
            int j = i - 1;

            while (
                j >= 0
                && sorted[j] > current
            ) {
                sorted[j + 1] = sorted[j];
                j--;
            }

            sorted[j + 1] = current;

            result.add(
                medianFromSortedPrefix(sorted, i + 1)
            );
        }

        return new Result(result, true);
    }

    static double medianFromSortedPrefix(
        int[] sorted,
        int length
    ) {
        if (length % 2 == 1) {
            return sorted[length / 2];
        }

        long left = sorted[(length / 2) - 1];
        long right = sorted[length / 2];

        return (left + right) / 2.0;
    }

    /**
     * Two-heap solution.
     *
     * smallerHalf: max-heap containing the lower half.
     * largerHalf: min-heap containing the upper half.
     *
     * Time: O(n log n)
     * Space: O(n)
     */
    static Result medianOfStreamHeap(int[] array) {
        if (!validArray(array)) {
            return new Result(new ArrayList<>(), false);
        }

        PriorityQueue<Integer> smallerHalf =
            new PriorityQueue<>(
                (a, b) -> Integer.compare(b, a)
            );

        PriorityQueue<Integer> largerHalf =
            new PriorityQueue<>();

        ArrayList<Double> result = new ArrayList<>();

        for (int value : array) {
            addToHeaps(
                value,
                smallerHalf,
                largerHalf
            );

            result.add(
                currentMedian(
                    smallerHalf,
                    largerHalf
                )
            );
        }

        return new Result(result, true);
    }

    static void addToHeaps(
        int value,
        PriorityQueue<Integer> smallerHalf,
        PriorityQueue<Integer> largerHalf
    ) {
        if (
            smallerHalf.isEmpty()
            || value <= smallerHalf.peek()
        ) {
            smallerHalf.offer(value);
        } else {
            largerHalf.offer(value);
        }

        rebalanceHeaps(smallerHalf, largerHalf);
    }

    static void rebalanceHeaps(
        PriorityQueue<Integer> smallerHalf,
        PriorityQueue<Integer> largerHalf
    ) {
        if (smallerHalf.size() > largerHalf.size() + 1) {
            largerHalf.offer(smallerHalf.poll());
        } else if (largerHalf.size() > smallerHalf.size()) {
            smallerHalf.offer(largerHalf.poll());
        }
    }

    static double currentMedian(
        PriorityQueue<Integer> smallerHalf,
        PriorityQueue<Integer> largerHalf
    ) {
        if (
            smallerHalf.size() == largerHalf.size()
        ) {
            long left = smallerHalf.peek();
            long right = largerHalf.peek();

            return (left + right) / 2.0;
        }

        return smallerHalf.peek();
    }

    static class TestCase {
        final String id;
        final int[] input;
        final ArrayList<Double> expected;
        final String description;

        TestCase(
            String id,
            int[] input,
            ArrayList<Double> expected,
            String description
        ) {
            this.id = id;
            this.input = input;
            this.expected = expected;
            this.description = description;
        }
    }

    static ArrayList<Double> doubles(double... values) {
        ArrayList<Double> result = new ArrayList<>();

        for (double value : values) {
            result.add(value);
        }

        return result;
    }

    static boolean resultsEqual(
        ArrayList<Double> actual,
        ArrayList<Double> expected
    ) {
        if (actual.size() != expected.size()) {
            return false;
        }

        for (int i = 0; i < actual.size(); i++) {
            if (
                Math.abs(actual.get(i) - expected.get(i))
                > 1e-9
            ) {
                return false;
            }
        }

        return true;
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
                    actual.valid()
                    && resultsEqual(
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
                        "  expected = %s%n",
                        test.expected
                    );
                    System.out.printf(
                        "  actual   = %s%n",
                        actual.result()
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
            new int[]{5, 15, 1, 3},
            doubles(5, 10, 5, 4),
            "Classic running median example"
        ));

        tests.add(new TestCase(
            "S2",
            new int[]{1, 2, 3, 4, 5},
            doubles(1, 1.5, 2, 2.5, 3),
            "Increasing values"
        ));

        tests.add(new TestCase(
            "S3",
            new int[]{5, 4, 3, 2, 1},
            doubles(5, 4.5, 4, 3.5, 3),
            "Decreasing values"
        ));

        tests.add(new TestCase(
            "S4",
            new int[]{2, 2, 2, 2},
            doubles(2, 2, 2, 2),
            "Duplicate values"
        ));

        tests.add(new TestCase(
            "S5",
            new int[]{-5, -1, -10, 0},
            doubles(-5, -3, -5, -3),
            "Negative values"
        ));

        tests.add(new TestCase(
            "S6",
            new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE},
            doubles(
                Integer.MAX_VALUE,
                Integer.MAX_VALUE
            ),
            "Large positive values"
        ));

        tests.add(new TestCase(
            "S7",
            new int[]{Integer.MIN_VALUE, Integer.MAX_VALUE},
            doubles(
                Integer.MIN_VALUE,
                -0.5
            ),
            "Integer boundary values"
        ));

        tests.add(new TestCase(
            "E1",
            null,
            new ArrayList<>(),
            "Null input"
        ));

        tests.add(new TestCase(
            "E2",
            new int[]{},
            new ArrayList<>(),
            "Empty input"
        ));

        System.out.println("############################################################");
        System.out.println("################## MEDIAN OF STREAM ########################");
        System.out.println("############################################################");
        System.out.println();

        runTests(
            "Insertion Sort O(n^2)",
            test ->
                medianOfStreamInsertionSort(test.input),
            tests
        );

        runTests(
            "Two Heaps O(n log n)",
            test ->
                medianOfStreamHeap(test.input),
            tests
        );
    }
}
