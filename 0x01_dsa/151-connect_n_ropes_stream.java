import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.function.Function;

public class MinCostConnectRopes {

    static record Result(
        long totalCost,
        boolean valid
    ) {}

    static boolean validArray(int[] array) {
        return array != null && array.length > 0;
    }

    /**
     * Repeated sorting solution.
     *
     * Time: O(n^2 log n)
     * Space: O(n)
     */
    static Result minCostConnectRopesRepeatedSorting(int[] array) {
        if (!validArray(array)) {
            return new Result(-1L, false);
        }

        long[] ropes = new long[array.length];

        for (int i = 0; i < array.length; i++) {
            if (array[i] < 0) {
                return new Result(-1L, false);
            }

            ropes[i] = array[i];
        }

        int length = ropes.length;
        long totalCost = 0L;

        while (length > 1) {
            Arrays.sort(ropes, 0, length);

            long first = ropes[0];
            long second = ropes[1];
            long combined = first + second;

            totalCost += combined;

            for (int i = 2; i < length; i++) {
                ropes[i - 2] = ropes[i];
            }

            ropes[length - 2] = combined;
            length--;
        }

        return new Result(totalCost, true);
    }

    /**
     * Min-heap solution.
     *
     * Time: O(n log n)
     * Space: O(n)
     */
    static Result minCostConnectRopesHeap(int[] array) {
        if (!validArray(array)) {
            return new Result(-1L, false);
        }

        PriorityQueue<Long> minHeap =
            new PriorityQueue<>();

        for (int value : array) {
            if (value < 0) {
                return new Result(-1L, false);
            }

            minHeap.offer((long) value);
        }

        long totalCost = 0L;

        while (minHeap.size() > 1) {
            long first = minHeap.poll();
            long second = minHeap.poll();

            long combined = first + second;
            totalCost += combined;

            minHeap.offer(combined);
        }

        return new Result(totalCost, true);
    }

    static class TestCase {
        final String id;
        final int[] input;
        final long expected;
        final boolean expectedValid;
        final String description;

        TestCase(
            String id,
            int[] input,
            long expected,
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
                    && actual.totalCost() == test.expected
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
                        "  input          = %s%n",
                        Arrays.toString(test.input)
                    );
                    System.out.printf(
                        "  expected       = Result(%d, %s)%n",
                        test.expected,
                        test.expectedValid
                    );
                    System.out.printf(
                        "  actual         = Result(%d, %s)%n",
                        actual.totalCost(),
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
            new int[]{4, 3, 2, 6},
            29L,
            true,
            "Classic example"
        ));

        tests.add(new TestCase(
            "S2",
            new int[]{1, 2, 3, 4, 5},
            33L,
            true,
            "Increasing rope lengths"
        ));

        tests.add(new TestCase(
            "S3",
            new int[]{5, 5, 5},
            25L,
            true,
            "Equal rope lengths"
        ));

        tests.add(new TestCase(
            "S4",
            new int[]{10},
            0L,
            true,
            "Single rope needs no connection"
        ));

        tests.add(new TestCase(
            "S5",
            new int[]{0, 0, 1, 2},
            4L,
            true,
            "Zero-length ropes"
        ));

        tests.add(new TestCase(
            "S6",
            new int[]{1, 1, 1, 1},
            8L,
            true,
            "Repeated small ropes"
        ));

        tests.add(new TestCase(
            "B1",
            new int[]{
                Integer.MAX_VALUE,
                Integer.MAX_VALUE
            },
            4_294_967_294L,
            true,
            "Long arithmetic prevents overflow"
        ));

        tests.add(new TestCase(
            "B2",
            new int[]{
                Integer.MAX_VALUE,
                Integer.MAX_VALUE,
                Integer.MAX_VALUE
            },
            10_737_418_235L,
            true,
            "Large three-rope input"
        ));

        tests.add(new TestCase(
            "E1",
            null,
            -1L,
            false,
            "Null input"
        ));

        tests.add(new TestCase(
            "E2",
            new int[]{},
            -1L,
            false,
            "Empty input"
        ));

        tests.add(new TestCase(
            "E3",
            new int[]{2, -1, 3},
            -1L,
            false,
            "Negative rope length"
        ));

        System.out.println("############################################################");
        System.out.println("############### MIN COST CONNECT ROPES #####################");
        System.out.println("############################################################");
        System.out.println();

        runTests(
            "Repeated Sorting O(n^2 log n)",
            test ->
                minCostConnectRopesRepeatedSorting(
                    test.input
                ),
            tests
        );

        runTests(
            "Min Heap O(n log n)",
            test ->
                minCostConnectRopesHeap(test.input),
            tests
        );
    }
}
