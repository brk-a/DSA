import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class MajorityElement {

    static class TestCase {

        final String id;
        final int[] nums;
        final int expected;
        final String description;

        TestCase(
            String id,
            int[] nums,
            int expected,
            String description
        ) {
            this.id = id;
            this.nums = nums;
            this.expected = expected;
            this.description = description;
        }
    }

    /**
     * Brute force solution.
     *
     * Time: O(n²)
     * Space: O(1)
     */
    static int majorityElementBruteForce(
        int[] nums
    ) {

        if (
            nums == null ||
            nums.length == 0
        ) {
            return -1;
        }

        int n = nums.length;

        for (
            int i = 0;
            i < n;
            i++
        ) {

            int count = 0;

            for (
                int j = 0;
                j < n;
                j++
            ) {

                if (
                    nums[i] ==
                    nums[j]
                ) {
                    count++;
                }
            }

            if (
                count > n / 2
            ) {
                return nums[i];
            }
        }

        return -1;
    }

    /**
     * Moore Voting Algorithm.
     *
     * Time: O(n)
     * Space: O(1)
     */
    static int majorityElementMooreVoting(
        int[] nums
    ) {

        if (
            nums == null ||
            nums.length == 0
        ) {
            return -1;
        }

        int candidate = 0;
        int count = 0;

        for (
            int num : nums
        ) {

            if (
                count == 0
            ) {

                candidate =
                    num;

                count = 1;

            } else if (
                num ==
                candidate
            ) {

                count++;

            } else {

                count--;
            }
        }

        count = 0;

        for (
            int num : nums
        ) {

            if (
                num ==
                candidate
            ) {
                count++;
            }
        }

        return
            count >
            nums.length / 2
                ? candidate
                : -1;
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

                boolean success =
                    actual ==
                    test.expected;

                if (
                    success
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
         * Standard majority cases
         */

        tests.add(
            new TestCase(
                "S1",
                new int[]{
                    3,2,3
                },
                3,
                "Simple majority"
            )
        );

        tests.add(
            new TestCase(
                "S2",
                new int[]{
                    2,2,1,1,1,2,2
                },
                2,
                "LeetCode example"
            )
        );

        /*
         * No majority
         */

        tests.add(
            new TestCase(
                "N1",
                new int[]{
                    1,2,3,4
                },
                -1,
                "No majority element"
            )
        );

        tests.add(
            new TestCase(
                "N2",
                new int[]{
                    1,1,2,2
                },
                -1,
                "Exactly half occurrences"
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
                7,
                "Single element array"
            )
        );

        /*
         * Null / empty
         */

        tests.add(
            new TestCase(
                "E1",
                null,
                -1,
                "Null array"
            )
        );

        tests.add(
            new TestCase(
                "E2",
                new int[]{},
                -1,
                "Empty array"
            )
        );

        /*
         * Negative values
         */

        tests.add(
            new TestCase(
                "NEG1",
                new int[]{
                    -1,-1,-1,2,3
                },
                -1,
                "Negative majority"
            )
        );

        tests.add(
            new TestCase(
                "NEG2",
                new int[]{
                    -5,-5,-5,-5,2
                },
                -5,
                "All negative majority"
            )
        );

        /*
         * Edge cases
         */

        tests.add(
            new TestCase(
                "EC1",
                new int[]{
                    Integer.MAX_VALUE,
                    Integer.MAX_VALUE,
                    1
                },
                Integer.MAX_VALUE,
                "MAX_VALUE majority"
            )
        );

        tests.add(
            new TestCase(
                "EC2",
                new int[]{
                    Integer.MIN_VALUE,
                    Integer.MIN_VALUE,
                    Integer.MIN_VALUE,
                    5
                },
                Integer.MIN_VALUE,
                "MIN_VALUE majority"
            )
        );

        System.out.println(
            "############################################################"
        );

        System.out.println(
            "############### MAJORITY ELEMENT PROBLEM ###################"
        );

        System.out.println(
            "############################################################"
        );

        System.out.println();

        runTests(
            "Brute Force O(n²)",
            test ->
                majorityElementBruteForce(
                    test.nums
                ),
            tests
        );

        runTests(
            "Moore Voting O(n)",
            test ->
                majorityElementMooreVoting(
                    test.nums
                ),
            tests
        );
    }
}
