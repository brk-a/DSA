import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class LeadersInAnArray {

    record Result(
        ArrayList<Integer> leaders,
        int count
    ) {}

    static class TestCase {

        final String id;
        final int[] input;
        final Integer[] expected;
        final String description;

        TestCase(
            String id,
            int[] input,
            Integer[] expected,
            String description
        ) {
            this.id = id;
            this.input = input;
            this.expected = expected;
            this.description = description;
        }
    }

    /**
     * Brute force.
     *
     * Time: O(n²)
     * Space: O(1) excluding output
     */
    static Result leadersNestedLoops(
        int[] nums
    ) {

        ArrayList<Integer> result =
            new ArrayList<>();

        if (
            nums == null ||
            nums.length == 0
        ) {
            return new Result(
                result,
                0
            );
        }

        int n = nums.length;

        for (
            int i = 0;
            i < n;
            i++
        ) {

            boolean leader = true;

            for (
                int j = i + 1;
                j < n;
                j++
            ) {

                if (
                    nums[j] > nums[i]
                ) {
                    leader = false;
                    break;
                }
            }

            if (leader) {
                result.add(nums[i]);
            }
        }

        return new Result(
            result,
            result.size()
        );
    }

    /**
     * Suffix maximum.
     *
     * Time: O(n)
     * Space: O(1) excluding output
     */
    static Result leadersSuffixMaximum(
        int[] nums
    ) {

        ArrayList<Integer> result =
            new ArrayList<>();

        if (
            nums == null ||
            nums.length == 0
        ) {
            return new Result(
                result,
                0
            );
        }

        int n = nums.length;

        int maxRight =
            nums[n - 1];

        result.add(maxRight);

        for (
            int i = n - 2;
            i >= 0;
            i--
        ) {

            if (
                nums[i] > maxRight
            ) {

                maxRight = nums[i];
                result.add(nums[i]);
            }
        }

        Collections.reverse(result);

        return new Result(
            result,
            result.size()
        );
    }

    static boolean matchesExpected(
        Result actual,
        Integer[] expected
    ) {

        if (
            actual.count !=
            expected.length
        ) {
            return false;
        }

        for (
            int i = 0;
            i < expected.length;
            i++
        ) {

            if (
                !actual.leaders()
                    .get(i)
                    .equals(expected[i])
            ) {
                return false;
            }
        }

        return true;
    }

    static void runTests(
        String name,
        Function<int[], Result> method,
        List<TestCase> tests
    ) {

        System.out.println(
            "=================================================="
        );

        System.out.println(name);

        System.out.println(
            "=================================================="
        );

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            Result actual =
                method.apply(
                    test.input
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
                    "  input    = %s%n",
                    test.input == null
                        ? "null"
                        : Arrays.toString(
                            test.input
                        )
                );

                System.out.printf(
                    "  expected = %s%n",
                    Arrays.toString(
                        test.expected
                    )
                );

                System.out.printf(
                    "  actual   = %s%n",
                    actual.leaders()
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

        // Standard example

        tests.add(
            new TestCase(
                "S1",
                new int[]{
                    16,17,4,3,5,2
                },
                new Integer[]{
                    17,5,2
                },
                "Classic example"
            )
        );

        // Increasing

        tests.add(
            new TestCase(
                "I1",
                new int[]{
                    1,2,3,4,5
                },
                new Integer[]{
                    5
                },
                "Strictly increasing"
            )
        );

        // Decreasing

        tests.add(
            new TestCase(
                "D1",
                new int[]{
                    5,4,3,2,1
                },
                new Integer[]{
                    5,4,3,2,1
                },
                "Strictly decreasing"
            )
        );

        // Single

        tests.add(
            new TestCase(
                "SG1",
                new int[]{
                    42
                },
                new Integer[]{
                    42
                },
                "Single element"
            )
        );

        // Empty

        tests.add(
            new TestCase(
                "E1",
                new int[]{},
                new Integer[]{},
                "Empty array"
            )
        );

        // Null

        tests.add(
            new TestCase(
                "E2",
                null,
                new Integer[]{},
                "Null array"
            )
        );

        // All equal

        tests.add(
            new TestCase(
                "EQ1",
                new int[]{
                    7,7,7,7
                },
                new Integer[]{
                    7
                },
                "All equal values"
            )
        );

        // Negatives

        tests.add(
            new TestCase(
                "N1",
                new int[]{
                    -1,-5,-2,-10
                },
                new Integer[]{
                    -1,-2,-10
                },
                "Negative values"
            )
        );

        // Integer limits

        tests.add(
            new TestCase(
                "L1",
                new int[]{
                    Integer.MAX_VALUE,
                    0,
                    Integer.MIN_VALUE
                },
                new Integer[]{
                    Integer.MAX_VALUE,
                    0,
                    Integer.MIN_VALUE
                },
                "Integer limits"
            )
        );

        System.out.println(
            "################################################"
        );

        System.out.println(
            "############ LEADERS IN AN ARRAY ############"
        );

        System.out.println(
            "################################################"
        );

        System.out.println();

        runTests(
            "Brute Force O(n²)",
            LeadersInAnArray::
                leadersNestedLoops,
            tests
        );

        runTests(
            "Suffix Maximum O(n)",
            LeadersInAnArray::
                leadersSuffixMaximum,
            tests
        );
    }
}
