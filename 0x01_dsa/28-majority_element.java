import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class MajorityElementTwo {

    static class TestCase {

        final String id;
        final int[] nums;
        final List<Integer> expected;
        final String description;

        TestCase(
            String id,
            int[] nums,
            List<Integer> expected,
            String description
        ) {
            this.id = id;
            this.nums = nums;
            this.expected = expected;
            this.description = description;
        }
    }

    /**
     * Brute Force
     *
     * Time: O(n²)
     * Space: O(1)
     */
    static List<Integer> majorityElementBruteForce(
        int[] nums
    ) {

        List<Integer> result =
            new ArrayList<>();

        if (
            nums == null ||
            nums.length == 0
        ) {
            return result;
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
                count >
                n / 3
            ) {

                if (
                    !result.contains(
                        nums[i]
                    )
                ) {
                    result.add(
                        nums[i]
                    );
                }
            }

            if (
                result.size() == 2
            ) {
                break;
            }
        }

        Collections.sort(
            result
        );

        return result;
    }

    /**
     * Boyer-Moore Voting Algorithm
     * (Majority Element II)
     *
     * Time: O(n)
     * Space: O(1)
     */
    static List<Integer> majorityElementBoyerMoore(
        int[] nums
    ) {

        List<Integer> result =
            new ArrayList<>();

        if (
            nums == null ||
            nums.length == 0
        ) {
            return result;
        }

        int candidate1 = 0;
        int candidate2 = 0;

        int count1 = 0;
        int count2 = 0;

        for (
            int num : nums
        ) {

            if (
                num ==
                candidate1
            ) {

                count1++;

            } else if (
                num ==
                candidate2
            ) {

                count2++;

            } else if (
                count1 == 0
            ) {

                candidate1 = num;
                count1 = 1;

            } else if (
                count2 == 0
            ) {

                candidate2 = num;
                count2 = 1;

            } else {

                count1--;
                count2--;
            }
        }

        count1 = 0;
        count2 = 0;

        for (
            int num : nums
        ) {

            if (
                num ==
                candidate1
            ) {
                count1++;
            }

            if (
                num ==
                candidate2
            ) {
                count2++;
            }
        }

        int threshold =
            nums.length / 3;

        if (
            count1 >
            threshold
        ) {
            result.add(
                candidate1
            );
        }

        if (
            candidate1 !=
            candidate2 &&
            count2 >
            threshold
        ) {
            result.add(
                candidate2
            );
        }

        Collections.sort(
            result
        );

        return result;
    }

    static void runTests(
        String algorithm,
        Function<TestCase, List<Integer>> method,
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

                List<Integer> actual =
                    method.apply(
                        test
                    );

                boolean success =
                    actual.equals(
                        test.expected
                    );

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
                    3,2,3
                },
                Arrays.asList(
                    3
                ),
                "Single majority > n/3"
            )
        );

        tests.add(
            new TestCase(
                "S2",
                new int[]{
                    1,1,1,3,3,2,2,2
                },
                Arrays.asList(
                    1,
                    2
                ),
                "Two majorities"
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
                Collections.emptyList(),
                "No majority"
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
                Arrays.asList(
                    7
                ),
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
                Collections.emptyList(),
                "Null array"
            )
        );

        tests.add(
            new TestCase(
                "E2",
                new int[]{},
                Collections.emptyList(),
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
                Arrays.asList(
                    -1
                ),
                "Negative majority"
            )
        );

        tests.add(
            new TestCase(
                "NEG2",
                new int[]{
                    -5,-5,-5,-5,2
                },
                Arrays.asList(
                    -5
                ),
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
                Arrays.asList(
                    Integer.MAX_VALUE
                ),
                "MAX_VALUE"
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
                Arrays.asList(
                    Integer.MIN_VALUE
                ),
                "MIN_VALUE"
            )
        );

        tests.add(
            new TestCase(
                "EC3",
                new int[]{
                    1,1,2,2,3,3
                },
                Collections.emptyList(),
                "Exactly n/3 occurrences"
            )
        );

        System.out.println(
            "############################################################"
        );

        System.out.println(
            "############ MAJORITY ELEMENT II PROBLEM ###################"
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
            "Boyer-Moore O(n)",
            test ->
                majorityElementBoyerMoore(
                    test.nums
                ),
            tests
        );
    }
}