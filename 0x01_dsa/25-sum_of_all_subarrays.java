import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class SumOfAllSubarrays {

    static class TestCase {

        final String id;
        final int[] nums;
        final long expected;
        final String description;

        TestCase(
            String id,
            int[] nums,
            long expected,
            String description
        ) {
            this.id = id;
            this.nums = nums;
            this.expected = expected;
            this.description = description;
        }
    }

    /**
     * Brute force / nested loops.
     *
     * Time: O(n²)
     * Space: O(1)
     */
    static long sumOfAllSubarraysNestedLoops(
        int[] nums
    ) {

        if (
            nums == null ||
            nums.length == 0
        ) {
            return 0;
        }

        long result = 0;

        for (
            int start = 0;
            start < nums.length;
            start++
        ) {

            long runningSum = 0;

            for (
                int end = start;
                end < nums.length;
                end++
            ) {

                runningSum +=
                    nums[end];

                result +=
                    runningSum;
            }
        }

        return result;
    }

    /**
     * Element contribution approach.
     *
     * Each element nums[i]
     * appears in:
     *
     * (i + 1) * (n - i)
     *
     * subarrays.
     *
     * Time: O(n)
     * Space: O(1)
     */
    static long sumOfAllSubarraysElementContribution(
        int[] nums
    ) {

        if (
            nums == null ||
            nums.length == 0
        ) {
            return 0;
        }

        long result = 0;
        int n = nums.length;

        for (
            int i = 0;
            i < n;
            i++
        ) {

            long occurrences =
                (long) (i + 1)
                * (n - i);

            result +=
                nums[i]
                * occurrences;
        }

        return result;
    }

    static boolean matchesExpected(
        long actual,
        long expected
    ) {

        return actual ==
            expected;
    }

    static void runTests(
        String algorithm,
        Function<TestCase, Long> method,
        List<TestCase> tests
    ) {

        System.out.println(
            "=================================================="
        );

        System.out.println(
            algorithm
        );

        System.out.println(
            "=================================================="
        );

        int passed = 0;
        int failed = 0;

        for (
            TestCase test
                : tests
        ) {

            try {

                long actual =
                    method.apply(
                        test
                    );

                boolean success =
                    matchesExpected(
                        actual,
                        test.expected
                    );

                if (success) {

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
                        "  nums      = %s%n",
                        test.nums == null
                            ? "null"
                            : Arrays.toString(
                                test.nums
                            )
                    );

                    System.out.printf(
                        "  expected  = %d%n",
                        test.expected
                    );

                    System.out.printf(
                        "  actual    = %d%n",
                        actual
                    );
                }

            } catch (
                Exception ex
            ) {

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

    public static void main(
        String[] args
    ) {

        List<TestCase> tests =
            new ArrayList<>();

        /*
         * Null / Empty
         */

        tests.add(
            new TestCase(
                "N1",
                null,
                0,
                "Null array"
            )
        );

        tests.add(
            new TestCase(
                "N2",
                new int[]{},
                0,
                "Empty array"
            )
        );

        /*
         * Single element
         */

        tests.add(
            new TestCase(
                "S1",
                new int[]{
                    5
                },
                5,
                "Single element"
            )
        );

        tests.add(
            new TestCase(
                "S2",
                new int[]{
                    -7
                },
                -7,
                "Single negative element"
            )
        );

        /*
         * Two elements
         */

        tests.add(
            new TestCase(
                "T1",
                new int[]{
                    1, 2
                },
                6,
                "Two positive numbers"
            )
        );

        tests.add(
            new TestCase(
                "T2",
                new int[]{
                    -1, -2
                },
                -6,
                "Two negative numbers"
            )
        );

        /*
         * Standard examples
         */

        tests.add(
            new TestCase(
                "E1",
                new int[]{
                    1, 2, 3
                },
                20,
                "Classic example"
            )
        );

        tests.add(
            new TestCase(
                "E2",
                new int[]{
                    1, 2, 3, 4
                },
                50,
                "Four increasing numbers"
            )
        );

        /*
         * Mixed values
         */

        tests.add(
            new TestCase(
                "M1",
                new int[]{
                    1, -2, 3
                },
                4,
                "Mixed positive and negative"
            )
        );

        tests.add(
            new TestCase(
                "M2",
                new int[]{
                    4, -1, 2
                },
                16,
                "Mixed values"
            )
        );

        /*
         * Zeros
         */

        tests.add(
            new TestCase(
                "Z1",
                new int[]{
                    0, 0, 0
                },
                0,
                "All zeros"
            )
        );

        tests.add(
            new TestCase(
                "Z2",
                new int[]{
                    0, 5, 0
                },
                20,
                "Zeros around value"
            )
        );

        /*
         * Larger values
         */

        tests.add(
            new TestCase(
                "L1",
                new int[]{
                    10, 20, 30
                },
                200,
                "Larger positive numbers"
            )
        );

        tests.add(
            new TestCase(
                "L2",
                new int[]{
                    1000, 2000
                },
                6000,
                "Large values"
            )
        );

        System.out.println(
            "############################################################"
        );

        System.out.println(
            "############ SUM OF ALL SUBARRAYS PROBLEM ##################"
        );

        System.out.println(
            "############################################################"
        );

        System.out.println();

        runTests(
            "Nested Loops O(n²)",
            test ->
                sumOfAllSubarraysNestedLoops(
                    test.nums
                ),
            tests
        );

        runTests(
            "Element Contribution O(n)",
            test ->
                sumOfAllSubarraysElementContribution(
                    test.nums
                ),
            tests
        );
    }
}