import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class AlternatePositiveAndNegative {

    record Result(
        ArrayList<Integer> result,
        int length
    ) {}

    static class TestCase {

        final String id;
        final ArrayList<Integer> input;
        final Integer[] expected;
        final String description;

        TestCase(
            String id,
            ArrayList<Integer> input,
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
     * Stable partition approach.
     *
     * Preserves relative order of
     * positive and negative numbers.
     *
     * Time: O(n)
     * Space: O(n)
     */
    static Result alternatePositiveAndNegative(
        ArrayList<Integer> nums
    ) {

        if (
            nums == null ||
            nums.isEmpty()
        ) {
            return new Result(
                new ArrayList<>(),
                0
            );
        }

        ArrayList<Integer> positives =
            new ArrayList<>();

        ArrayList<Integer> negatives =
            new ArrayList<>();

        for (Integer value : nums) {

            if (value >= 0) {
                positives.add(value);
            } else {
                negatives.add(value);
            }
        }

        ArrayList<Integer> result =
            new ArrayList<>(nums);

        int pos = 0;
        int neg = 0;
        int write = 0;

        while (
            pos < positives.size() &&
            neg < negatives.size()
        ) {

            if (write % 2 == 0) {

                result.set(
                    write++,
                    positives.get(pos++)
                );

            } else {

                result.set(
                    write++,
                    negatives.get(neg++)
                );
            }
        }

        while (pos < positives.size()) {

            result.set(
                write++,
                positives.get(pos++)
            );
        }

        while (neg < negatives.size()) {

            result.set(
                write++,
                negatives.get(neg++)
            );
        }

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
            actual.length != expected.length
        ) {
            return false;
        }

        for (
            int i = 0;
            i < expected.length;
            i++
        ) {

            if (
                !actual.result.get(i)
                    .equals(expected[i])
            ) {
                return false;
            }
        }

        return true;
    }

    static void runTests(
        String name,
        Function<
            ArrayList<Integer>,
            Result
        > method,
        List<TestCase> tests
    ) {

        System.out.println(
            "===================================================="
        );

        System.out.println(name);

        System.out.println(
            "===================================================="
        );

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            Result actual =
                method.apply(test.input);

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
                    test.input
                );

                System.out.printf(
                    "  expected = %s%n",
                    Arrays.toString(
                        test.expected
                    )
                );

                System.out.printf(
                    "  actual   = %s%n",
                    actual.result
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

        // Standard

        tests.add(new TestCase(
            "S1",
            new ArrayList<>(
                Arrays.asList(
                    1,-1,2,-2,3,-3
                )
            ),
            new Integer[]{
                1,-1,2,-2,3,-3
            },
            "Already alternating"
        ));

        // Null / Empty

        tests.add(new TestCase(
            "E1",
            new ArrayList<>(),
            new Integer[]{},
            "Empty input"
        ));

        tests.add(new TestCase(
            "E2",
            null,
            new Integer[]{},
            "Null input"
        ));

        // Single

        tests.add(new TestCase(
            "S2",
            new ArrayList<>(
                Arrays.asList(5)
            ),
            new Integer[]{5},
            "Single positive"
        ));

        tests.add(new TestCase(
            "S3",
            new ArrayList<>(
                Arrays.asList(-5)
            ),
            new Integer[]{-5},
            "Single negative"
        ));

        // All positive

        tests.add(new TestCase(
            "P1",
            new ArrayList<>(
                Arrays.asList(
                    1,2,3,4
                )
            ),
            new Integer[]{
                1,2,3,4
            },
            "All positive"
        ));

        // All negative

        tests.add(new TestCase(
            "N1",
            new ArrayList<>(
                Arrays.asList(
                    -1,-2,-3,-4
                )
            ),
            new Integer[]{
                -1,-2,-3,-4
            },
            "All negative"
        ));

        // More positives

        tests.add(new TestCase(
            "M1",
            new ArrayList<>(
                Arrays.asList(
                    1,2,3,-1
                )
            ),
            new Integer[]{
                1,-1,2,3
            },
            "More positives"
        ));

        // More negatives

        tests.add(new TestCase(
            "M2",
            new ArrayList<>(
                Arrays.asList(
                    1,-1,-2,-3
                )
            ),
            new Integer[]{
                1,-1,-2,-3
            },
            "More negatives"
        ));

        // Zeroes

        tests.add(new TestCase(
            "Z1",
            new ArrayList<>(
                Arrays.asList(
                    0,-1,0,-2
                )
            ),
            new Integer[]{
                0,-1,0,-2
            },
            "Zero treated as positive"
        ));

        // Integer limits

        tests.add(new TestCase(
            "L1",
            new ArrayList<>(
                Arrays.asList(
                    Integer.MAX_VALUE,
                    Integer.MIN_VALUE
                )
            ),
            new Integer[]{
                Integer.MAX_VALUE,
                Integer.MIN_VALUE
            },
            "Integer limits"
        ));

        System.out.println(
            "########################################################"
        );

        System.out.println(
            "###### ALTERNATE POSITIVE AND NEGATIVE NUMBERS ######"
        );

        System.out.println(
            "########################################################"
        );

        System.out.println();

        runTests(
            "Stable Partition O(n)",
            AlternatePositiveAndNegative::
                alternatePositiveAndNegative,
            tests
        );
    }
}
