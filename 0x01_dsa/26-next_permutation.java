import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class NextPermutation {

    static class TestCase {

        final String id;
        final int[] nums;
        final int[] expected;
        final String description;

        TestCase(
            String id,
            int[] nums,
            int[] expected,
            String description
        ) {
            this.id = id;
            this.nums = nums;
            this.expected = expected;
            this.description = description;
        }
    }

    /**
     * Brute force:
     * Generate all permutations,
     * sort lexicographically,
     * locate current permutation,
     * return next.
     *
     * Time:
     * O(n! * n)
     *
     * Space:
     * O(n!)
     */
    static int[] nextPermutationBruteForce(
        int[] nums
    ) {

        if (
            nums == null ||
            nums.length == 0
        ) {
            return new int[]{};
        }

        int[] original =
            nums.clone();

        List<List<Integer>> perms =
            new ArrayList<>();

        generatePermutations(
            perms,
            original.clone(),
            0
        );

        Collections.sort(
            perms,
            (a, b) -> {

                for (
                    int i = 0;
                    i < a.size();
                    i++
                ) {

                    int cmp =
                        Integer.compare(
                            a.get(i),
                            b.get(i)
                        );

                    if (cmp != 0) {
                        return cmp;
                    }
                }

                return 0;
            }
        );

        for (
            int i = 0;
            i < perms.size();
            i++
        ) {

            boolean match = true;

            for (
                int j = 0;
                j < nums.length;
                j++
            ) {

                if (
                    nums[j]
                    != perms.get(i).get(j)
                ) {

                    match = false;
                    break;
                }
            }

            if (match) {

                int nextIndex =
                    (i + 1)
                    % perms.size();

                int[] result =
                    new int[nums.length];

                for (
                    int j = 0;
                    j < nums.length;
                    j++
                ) {

                    result[j] =
                        perms
                            .get(nextIndex)
                            .get(j);
                }

                return result;
            }
        }

        return original;
    }

    /**
     * Optimal next permutation.
     *
     * Time:
     * O(n)
     *
     * Space:
     * O(1)
     */
    static int[] nextPermutationOptimal(
        int[] nums
    ) {

        if (
            nums == null ||
            nums.length == 0
        ) {
            return new int[]{};
        }

        int[] result =
            nums.clone();

        int n =
            result.length;

        int pivot = -1;

        for (
            int i = n - 2;
            i >= 0;
            i--
        ) {

            if (
                result[i]
                < result[i + 1]
            ) {

                pivot = i;
                break;
            }
        }

        if (pivot == -1) {

            reverse(
                result,
                0,
                n - 1
            );

            return result;
        }

        for (
            int i = n - 1;
            i > pivot;
            i--
        ) {

            if (
                result[i]
                > result[pivot]
            ) {

                swap(
                    result,
                    i,
                    pivot
                );

                break;
            }
        }

        reverse(
            result,
            pivot + 1,
            n - 1
        );

        return result;
    }

    static void generatePermutations(
        List<List<Integer>> result,
        int[] nums,
        int idx
    ) {

        if (
            idx
            == nums.length - 1
        ) {

            List<Integer> tmp =
                new ArrayList<>();

            for (
                int x : nums
            ) {
                tmp.add(x);
            }

            result.add(tmp);
            return;
        }

        for (
            int i = idx;
            i < nums.length;
            i++
        ) {

            swap(
                nums,
                idx,
                i
            );

            generatePermutations(
                result,
                nums,
                idx + 1
            );

            swap(
                nums,
                idx,
                i
            );
        }
    }

    static void swap(
        int[] nums,
        int i,
        int j
    ) {

        int temp =
            nums[i];

        nums[i] =
            nums[j];

        nums[j] =
            temp;
    }

    static void reverse(
        int[] nums,
        int left,
        int right
    ) {

        while (
            left < right
        ) {

            swap(
                nums,
                left++,
                right--
            );
        }
    }

    static boolean matchesExpected(
        int[] actual,
        int[] expected
    ) {

        return Arrays.equals(
            actual,
            expected
        );
    }

    static void runTests(
        String algorithm,
        Function<TestCase, int[]> method,
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

                int[] actual =
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
                        "  expected = %s%n",
                        Arrays.toString(
                            test.expected
                        )
                    );

                    System.out.printf(
                        "  actual   = %s%n",
                        Arrays.toString(
                            actual
                        )
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

        tests.add(
            new TestCase(
                "N1",
                null,
                new int[]{},
                "Null input"
            )
        );

        tests.add(
            new TestCase(
                "N2",
                new int[]{},
                new int[]{},
                "Empty input"
            )
        );

        tests.add(
            new TestCase(
                "S1",
                new int[]{1},
                new int[]{1},
                "Single element"
            )
        );

        tests.add(
            new TestCase(
                "T1",
                new int[]{1, 2},
                new int[]{2, 1},
                "Two ascending"
            )
        );

        tests.add(
            new TestCase(
                "T2",
                new int[]{2, 1},
                new int[]{1, 2},
                "Two descending"
            )
        );

        tests.add(
            new TestCase(
                "E1",
                new int[]{1, 2, 3},
                new int[]{1, 3, 2},
                "Classic example"
            )
        );

        tests.add(
            new TestCase(
                "E2",
                new int[]{1, 3, 2},
                new int[]{2, 1, 3},
                "Middle permutation"
            )
        );

        tests.add(
            new TestCase(
                "E3",
                new int[]{3, 2, 1},
                new int[]{1, 2, 3},
                "Last permutation"
            )
        );

        tests.add(
            new TestCase(
                "D1",
                new int[]{1, 1, 5},
                new int[]{1, 5, 1},
                "Duplicates"
            )
        );

        tests.add(
            new TestCase(
                "D2",
                new int[]{1, 5, 1},
                new int[]{5, 1, 1},
                "Duplicates 2"
            )
        );

        tests.add(
            new TestCase(
                "L1",
                new int[]{1, 2, 3, 4},
                new int[]{1, 2, 4, 3},
                "Longer ascending"
            )
        );

        System.out.println(
            "############################################################"
        );

        System.out.println(
            "################ NEXT PERMUTATION PROBLEM ##########################"
        );

        System.out.println(
            "############################################################"
        );

        System.out.println();

        runTests(
            "Brute Force O(n! log(n!))",
            test ->
                nextPermutationBruteForce(
                    test.nums
                ),
            tests
        );

        runTests(
            "Optimal O(n)",
            test ->
                nextPermutationOptimal(
                    test.nums
                ),
            tests
        );
    }
}