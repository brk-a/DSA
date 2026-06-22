import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class MissingRanges {

    record Range(
        int start,
        int end
    ) {}

    static class TestCase {

        final String id;
        final int[] nums;
        final int lower;
        final int upper;
        final List<Range> expected;
        final String description;

        TestCase(
            String id,
            int[] nums,
            int lower,
            int upper,
            List<Range> expected,
            String description
        ) {
            this.id = id;
            this.nums = nums;
            this.lower = lower;
            this.upper = upper;
            this.expected = expected;
            this.description = description;
        }
    }

    /**
     * Missing ranges in sorted array.
     *
     * Time: O(n)
     * Space: O(k)
     *
     * Assumes nums is sorted ascending.
     */
    static List<Range> missingRanges(
        int[] nums,
        int lower,
        int upper
    ) {

        List<Range> result =
            new ArrayList<>();

        if (lower > upper) {
            return result;
        }

        if (
            nums == null ||
            nums.length == 0
        ) {

            result.add(
                new Range(
                    lower,
                    upper
                )
            );

            return result;
        }

        long nextExpected =
            lower;

        for (int num : nums) {

            if (
                num < lower
            ) {
                continue;
            }

            if (
                num > upper
            ) {
                break;
            }

            if (
                nextExpected <
                num
            ) {

                result.add(
                    new Range(
                        (int) nextExpected,
                        num - 1
                    )
                );
            }

            nextExpected =
                (long) num + 1;
        }

        if (
            nextExpected <= upper
        ) {

            result.add(
                new Range(
                    (int) nextExpected,
                    upper
                )
            );
        }

        return result;
    }

    static boolean matchesExpected(
        List<Range> actual,
        List<Range> expected
    ) {

        if (
            actual.size() !=
            expected.size()
        ) {
            return false;
        }

        for (
            int i = 0;
            i < actual.size();
            i++
        ) {

            if (
                !actual.get(i)
                    .equals(
                        expected.get(i)
                    )
            ) {
                return false;
            }
        }

        return true;
    }

    static void runTests(
        String algorithm,
        Function<TestCase,
            List<Range>> method,
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

                List<Range> actual =
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
                        "  lower     = %d%n",
                        test.lower
                    );

                    System.out.printf(
                        "  upper     = %d%n",
                        test.upper
                    );

                    System.out.printf(
                        "  expected  = %s%n",
                        test.expected
                    );

                    System.out.printf(
                        "  actual    = %s%n",
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
                    0,1,3,50,75
                },
                0,
                99,
                List.of(
                    new Range(
                        2,
                        2
                    ),
                    new Range(
                        4,
                        49
                    ),
                    new Range(
                        51,
                        74
                    ),
                    new Range(
                        76,
                        99
                    )
                ),
                "Classic missing ranges"
            )
        );

        tests.add(
            new TestCase(
                "S2",
                new int[]{
                    1,2,3,4,5
                },
                1,
                5,
                List.of(),
                "No missing values"
            )
        );

        /*
         * Missing at boundaries
         */

        tests.add(
            new TestCase(
                "B1",
                new int[]{
                    3,4,5
                },
                1,
                5,
                List.of(
                    new Range(
                        1,
                        2
                    )
                ),
                "Missing at start"
            )
        );

        tests.add(
            new TestCase(
                "B2",
                new int[]{
                    1,2,3
                },
                1,
                5,
                List.of(
                    new Range(
                        4,
                        5
                    )
                ),
                "Missing at end"
            )
        );

        tests.add(
            new TestCase(
                "B3",
                new int[]{
                    3
                },
                1,
                5,
                List.of(
                    new Range(
                        1,
                        2
                    ),
                    new Range(
                        4,
                        5
                    )
                ),
                "Missing both sides"
            )
        );

        /*
         * Empty / null
         */

        tests.add(
            new TestCase(
                "N1",
                null,
                1,
                5,
                List.of(
                    new Range(
                        1,
                        5
                    )
                ),
                "Null array"
            )
        );

        tests.add(
            new TestCase(
                "N2",
                new int[]{},
                1,
                5,
                List.of(
                    new Range(
                        1,
                        5
                    )
                ),
                "Empty array"
            )
        );

        /*
         * Outside range
         */

        tests.add(
            new TestCase(
                "O1",
                new int[]{
                    -10,-5,1,3,20
                },
                1,
                10,
                List.of(
                    new Range(
                        2,
                        2
                    ),
                    new Range(
                        4,
                        10
                    )
                ),
                "Ignore outside values"
            )
        );

        /*
         * Single value range
         */

        tests.add(
            new TestCase(
                "SV1",
                new int[]{
                    1
                },
                1,
                1,
                List.of(),
                "Single value present"
            )
        );

        tests.add(
            new TestCase(
                "SV2",
                new int[]{},
                7,
                7,
                List.of(
                    new Range(
                        7,
                        7
                    )
                ),
                "Single value missing"
            )
        );

        System.out.println(
            "############################################################"
        );

        System.out.println(
            "############### MISSING RANGES PROBLEM #####################"
        );

        System.out.println(
            "############################################################"
        );

        System.out.println();

        runTests(
            "Linear Scan O(n)",
            test ->
                missingRanges(
                    test.nums,
                    test.lower,
                    test.upper
                ),
            tests
        );
    }
}