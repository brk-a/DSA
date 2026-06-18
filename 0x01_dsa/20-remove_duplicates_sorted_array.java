import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.function.Function;

public class RemoveDuplicatesSortedArray {

    record Result(int[] result, int length) {}

    static class TestCase {

        final String id;
        final int[] input;
        final int[] expected;
        final String description;

        TestCase(
            String id,
            int[] input,
            int[] expected,
            String description
        ) {
            this.id = id;
            this.input = input;
            this.expected = expected;
            this.description = description;
        }
    }

    /**
     * Approach 1
     *
     * True brute force.
     *
     * Compare each element against all
     * previously accepted unique elements.
     *
     * Time: O(n²)
     * Space: O(n)
     */
    static Result removeDuplicatesBruteForce(
        int[] nums
    ) {

        if (nums == null || nums.length == 0) {
            return new Result(new int[0], 0);
        }

        int[] result = new int[nums.length];

        int size = 0;

        for (int value : nums) {

            boolean seen = false;

            for (int j = 0; j < size; j++) {

                if (result[j] == value) {
                    seen = true;
                    break;
                }
            }

            if (!seen) {
                result[size++] = value;
            }
        }

        return new Result(result, size);
    }

    /**
     * Approach 2
     *
     * HashSet.
     *
     * Time: O(n)
     * Space: O(n)
     */
    static Result removeDuplicatesHashSet(
        int[] nums
    ) {

        if (nums == null || nums.length == 0) {
            return new Result(new int[0], 0);
        }

        HashSet<Integer> seen =
            new HashSet<>();

        int[] result =
            new int[nums.length];

        int size = 0;

        for (int value : nums) {

            if (seen.add(value)) {
                result[size++] = value;
            }
        }

        return new Result(result, size);
    }

    /**
     * Approach 3
     *
     * Two pointers.
     *
     * Uses sorted property.
     *
     * Time: O(n)
     * Space: O(1)
     */
    static Result removeDuplicatesTwoPointers(
        int[] nums
    ) {

        if (nums == null || nums.length == 0) {
            return new Result(new int[0], 0);
        }

        int[] result = nums.clone();

        int write = 1;

        for (
            int read = 1;
            read < result.length;
            read++
        ) {

            if (
                result[read]
                != result[write - 1]
            ) {

                result[write++] =
                    result[read];
            }
        }

        return new Result(result, write);
    }

    static boolean matchesExpected(
        Result actual,
        int[] expected
    ) {

        if (actual.length != expected.length) {
            return false;
        }

        for (
            int i = 0;
            i < expected.length;
            i++
        ) {

            if (
                actual.result[i]
                != expected[i]
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
                    Arrays.toString(test.input)
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
                        Arrays.copyOf(
                            actual.result,
                            actual.length
                        )
                    )
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

        // ====================================
        // Standard
        // ====================================

        tests.add(new TestCase(
            "S1",
            new int[]{
                1,1,2
            },
            new int[]{
                1,2
            },
            "Basic example"
        ));

        tests.add(new TestCase(
            "S2",
            new int[]{
                0,0,1,1,1,2,2,3,3,4
            },
            new int[]{
                0,1,2,3,4
            },
            "LeetCode example"
        ));

        // ====================================
        // Empty / Null
        // ====================================

        tests.add(new TestCase(
            "E1",
            new int[]{},
            new int[]{},
            "Empty array"
        ));

        tests.add(new TestCase(
            "E2",
            null,
            new int[]{},
            "Null input"
        ));

        // ====================================
        // Single Element
        // ====================================

        tests.add(new TestCase(
            "S3",
            new int[]{5},
            new int[]{5},
            "Single value"
        ));

        // ====================================
        // All Unique
        // ====================================

        tests.add(new TestCase(
            "U1",
            new int[]{
                1,2,3,4,5
            },
            new int[]{
                1,2,3,4,5
            },
            "Already unique"
        ));

        // ====================================
        // All Duplicates
        // ====================================

        tests.add(new TestCase(
            "D1",
            new int[]{
                7,7,7,7,7
            },
            new int[]{
                7
            },
            "All duplicates"
        ));

        // ====================================
        // Negative Numbers
        // ====================================

        tests.add(new TestCase(
            "N1",
            new int[]{
                -5,-5,-3,-3,-1
            },
            new int[]{
                -5,-3,-1
            },
            "Negative values"
        ));

        // ====================================
        // Zeroes
        // ====================================

        tests.add(new TestCase(
            "Z1",
            new int[]{
                0,0,0,0
            },
            new int[]{
                0
            },
            "All zeroes"
        ));

        // ====================================
        // Integer Limits
        // ====================================

        tests.add(new TestCase(
            "L1",
            new int[]{
                Integer.MIN_VALUE,
                Integer.MIN_VALUE,
                Integer.MAX_VALUE
            },
            new int[]{
                Integer.MIN_VALUE,
                Integer.MAX_VALUE
            },
            "Integer limits"
        ));

        // ====================================
        // Mixed
        // ====================================

        tests.add(new TestCase(
            "M1",
            new int[]{
                1,1,1,2,2,3,4,4,5
            },
            new int[]{
                1,2,3,4,5
            },
            "Mixed duplicates"
        ));

        System.out.println(
            "########################################################"
        );
        System.out.println(
            "######## REMOVE DUPLICATES SORTED ARRAY ########"
        );
        System.out.println(
            "########################################################"
        );
        System.out.println();

        runTests(
            "Brute Force O(n²)",
            RemoveDuplicatesSortedArray::
                removeDuplicatesBruteForce,
            tests
        );

        runTests(
            "HashSet O(n)",
            RemoveDuplicatesSortedArray::
                removeDuplicatesHashSet,
            tests
        );

        runTests(
            "Two Pointers O(n)",
            RemoveDuplicatesSortedArray::
                removeDuplicatesTwoPointers,
            tests
        );
    }
}