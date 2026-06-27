import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class MinimiseHeights {

    static class TestCase {

        final String id;
        final int[] nums;
        final int k;
        final int expected;
        final String description;

        TestCase(
            String id,
            int[] nums,
            int k,
            int expected,
            String description
        ) {
            this.id = id;
            this.nums = nums;
            this.k = k;
            this.expected = expected;
            this.description = description;
        }
    }

    /**
     * Greedy Solution
     *
     * Time: O(n log n)
     * Space: O(n)
     */
    static int minimiseHeights(
        int[] nums,
        int k
    ) {

        if (
            nums == null ||
            nums.length == 0
        ) {
            return -1;
        }

        if (
            nums.length == 1
        ) {
            return 0;
        }

        int[] arr =
            Arrays.copyOf(
                nums,
                nums.length
            );

        Arrays.sort(arr);

        int n =
            arr.length;

        int answer =
            arr[n - 1] -
            arr[0];

        for (
            int i = 1;
            i < n;
            i++
        ) {

            if (
                arr[i] - k < 0
            ) {
                continue;
            }

            int smallest =
                Math.min(
                    arr[0] + k,
                    arr[i] - k
                );

            int largest =
                Math.max(
                    arr[i - 1] + k,
                    arr[n - 1] - k
                );

            answer =
                Math.min(
                    answer,
                    largest -
                    smallest
                );
        }

        return answer;
    }

    static void runTests(
        String algorithm,
        Function<TestCase, Integer> method,
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

                int actual =
                    method.apply(
                        test
                    );

                if (
                    actual ==
                    test.expected
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
                        "  nums      = %s%n",
                        test.nums == null
                            ? "null"
                            : Arrays.toString(
                                test.nums
                            )
                    );

                    System.out.printf(
                        "  k         = %d%n",
                        test.k
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
         * Standard examples
         */

        tests.add(
            new TestCase(
                "S1",
                new int[]{
                    1,5,8,10
                },
                2,
                5,
                "Classic example"
            )
        );

        tests.add(
            new TestCase(
                "S2",
                new int[]{
                    3,9,12,16,20
                },
                3,
                11,
                "Another standard example"
            )
        );

        /*
         * Single element
         */

        tests.add(
            new TestCase(
                "SE1",
                new int[]{
                    7
                },
                5,
                0,
                "Single element"
            )
        );

        /*
         * Null / Empty
         */

        tests.add(
            new TestCase(
                "E1",
                null,
                2,
                -1,
                "Null array"
            )
        );

        tests.add(
            new TestCase(
                "E2",
                new int[]{},
                2,
                -1,
                "Empty array"
            )
        );

        /*
         * Already equal
         */

        tests.add(
            new TestCase(
                "EQ1",
                new int[]{
                    5,5,5,5
                },
                3,
                0,
                "All equal"
            )
        );

        /*
         * Negative skip cases
         */

        tests.add(
            new TestCase(
                "NS1",
                new int[]{
                    1,2,3
                },
                5,
                2,
                "Decrease would become negative"
            )
        );

        /*
         * Large k
         */

        tests.add(
            new TestCase(
                "LK1",
                new int[]{
                    4,6
                },
                10,
                2,
                "Very large k"
            )
        );

        /*
         * Edge values
         */

        tests.add(
            new TestCase(
                "EC1",
                new int[]{
                    Integer.MAX_VALUE - 5,
                    Integer.MAX_VALUE
                },
                2,
                1,
                "Near MAX_VALUE"
            )
        );

        tests.add(
            new TestCase(
                "EC2",
                new int[]{
                    2,6,3,4,7,2,10,3,2,1
                },
                5,
                7,
                "GFG example"
            )
        );

        System.out.println(
            "############################################################"
        );

        System.out.println(
            "############ MINIMISE HEIGHTS PROBLEM ######################"
        );

        System.out.println(
            "############################################################"
        );

        System.out.println();

        runTests(
            "Greedy O(n log n)",
            test ->
                minimiseHeights(
                    test.nums,
                    test.k
                ),
            tests
        );
    }
}
