// #!/usr/bin/env jshell

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

public class ReverseAnArrayInGroups {

    /*
     * All methods clone the input and return the modified clone.
     * They do NOT mutate the caller's input array.
     *
     * Behaviour:
     * - null input -> returns null
     * - empty input -> returns new int[0]
     * - k < 1 -> throws IllegalArgumentException
     * - k == 1 -> returns cloned array
     */

    /**
     * Reverse array in groups of size k using two pointers.
     *
     * Example:
     * [1,2,3,4,5,6,7,8], k=3
     * => [3,2,1,6,5,4,8,7]
     *
     * Time: O(n)
     * Space: O(n)
     */
    static int[] reverseGroupsTwoPointers(int[] nums, int k) {

        if (nums == null) {
            return null;
        }

        if (nums.length == 0) {
            return new int[0];
        }

        if (k < 1) {
            throw new IllegalArgumentException("k must be >= 1");
        }

        if (k == 1) {
            return nums.clone();
        }

        int[] result = nums.clone();
        int n = result.length;

        for (int start = 0; start < n; start += k) {

            int left = start;
            int right = Math.min(start + k - 1, n - 1);

            while (left < right) {

                int temp = result[left];
                result[left] = result[right];
                result[right] = temp;

                left++;
                right--;
            }
        }

        return result;
    }

    /**
     * Same algorithm as above; demonstrates XOR swapping
     *
     * Time: O(n)
     * Space: O(n)
     */
    static int[] reverseGroupsXorSwap(int[] nums, int k) {

        if (nums == null) {
            return null;
        }

        if (nums.length == 0) {
            return new int[0];
        }

        if (k < 1) {
            throw new IllegalArgumentException("k must be >= 1");
        }

        if (k == 1) {
            return nums.clone();
        }

        int[] result = nums.clone();
        int n = result.length;

        for (int start = 0; start < n; start += k) {

            int left = start;
            int right = Math.min(start + k - 1, n - 1);

            while (left < right) {

                result[left] ^= result[right];
                result[right] ^= result[left];
                result[left] ^= result[right];

                left++;
                right--;
            }
        }

        return result;
    }

    /*
     * TestCase structure:
     * id
     * input array
     * k
     * expected result
     * description
     * whether exception is expected
     */

    static class TestCase {

        final String id;
        final int[] nums;
        final int k;
        final int[] expected;
        final String description;
        final boolean expectException;

        TestCase(
                String id,
                int[] nums,
                int k,
                int[] expected,
                String description,
                boolean expectException) {

            this.id = id;
            this.nums = nums;
            this.k = k;
            this.expected = expected;
            this.description = description;
            this.expectException = expectException;
        }
    }

    static void runTests(
            String methodName,
            BiFunction<int[], Integer, int[]> func,
            List<TestCase> tests) {

        System.out.println(
                "========================= method: "
                        + methodName
                        + " =========================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            try {

                int[] got = func.apply(test.nums, test.k);

                if (test.expectException) {

                    failed++;
                    System.out.printf(
                            " Test %s (%s): FAILED%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  Expected exception but method returned normally.");

                    continue;
                }

                boolean testPassed =
                        Arrays.equals(got, test.expected);

                if (testPassed) {

                    passed++;

                    System.out.printf(
                            " Test %s (%s): passed%n",
                            test.id,
                            test.description);

                } else {

                    failed++;

                    System.out.printf(
                            " Test %s (%s): FAILED%n",
                            test.id,
                            test.description);

                    System.out.printf(
                            "  input=%s%n",
                            test.nums == null
                                    ? "null"
                                    : Arrays.toString(test.nums));

                    System.out.printf(
                            "  k=%d%n",
                            test.k);

                    System.out.printf(
                            "  got=%s%n",
                            got == null
                                    ? "null"
                                    : Arrays.toString(got));

                    System.out.printf(
                            "  expected=%s%n",
                            test.expected == null
                                    ? "null"
                                    : Arrays.toString(test.expected));
                }

            } catch (Exception ex) {

                if (test.expectException) {

                    passed++;

                    System.out.printf(
                            " Test %s (%s): passed (caught %s)%n",
                            test.id,
                            test.description,
                            ex.getClass().getSimpleName());

                } else {

                    failed++;

                    System.out.printf(
                            " Test %s (%s): FAILED%n",
                            test.id,
                            test.description);

                    System.out.printf(
                            "  Unexpected exception: %s%n",
                            ex);
                }
            }
        }

        System.out.printf(
                "Results: %d passed, %d failed out of %d tests%n%n",
                passed,
                failed,
                tests.size());
    }

    public static void main(String[] args) {

        List<TestCase> positiveTests = new ArrayList<>();
        List<TestCase> negativeTests = new ArrayList<>();
        List<TestCase> edgeTests = new ArrayList<>();

        // =====================================================
        // POSITIVE TESTS
        // =====================================================

        positiveTests.add(
                new TestCase(
                        "P1",
                        new int[]{1, 2, 3, 4, 5, 6},
                        2,
                        new int[]{2, 1, 4, 3, 6, 5},
                        "Reverse pairs",
                        false));

        positiveTests.add(
                new TestCase(
                        "P2",
                        new int[]{1, 2, 3, 4, 5, 6},
                        3,
                        new int[]{3, 2, 1, 6, 5, 4},
                        "Reverse groups of 3",
                        false));

        positiveTests.add(
                new TestCase(
                        "P3",
                        new int[]{1, 2, 3, 4, 5},
                        3,
                        new int[]{3, 2, 1, 5, 4},
                        "Last group smaller than k",
                        false));

        positiveTests.add(
                new TestCase(
                        "P4",
                        new int[]{10, 20, 30, 40},
                        4,
                        new int[]{40, 30, 20, 10},
                        "k equals array length",
                        false));

        positiveTests.add(
                new TestCase(
                        "P5",
                        new int[]{1, 2, 3, 4, 5, 6, 7, 8},
                        3,
                        new int[]{3, 2, 1, 6, 5, 4, 8, 7},
                        "Classic example",
                        false));

        // =====================================================
        // NEGATIVE TESTS
        // =====================================================

        negativeTests.add(
                new TestCase(
                        "N1",
                        null,
                        3,
                        null,
                        "Null input",
                        false));

        negativeTests.add(
                new TestCase(
                        "N2",
                        new int[]{},
                        3,
                        new int[]{},
                        "Empty array",
                        false));

        negativeTests.add(
                new TestCase(
                        "N3",
                        new int[]{1, 2, 3},
                        0,
                        null,
                        "k = 0 should throw exception",
                        true));

        negativeTests.add(
                new TestCase(
                        "N4",
                        new int[]{1, 2, 3},
                        -5,
                        null,
                        "Negative k should throw exception",
                        true));

        // =====================================================
        // EDGE TESTS
        // =====================================================

        edgeTests.add(
                new TestCase(
                        "E1",
                        new int[]{42},
                        5,
                        new int[]{42},
                        "Single element",
                        false));

        edgeTests.add(
                new TestCase(
                        "E2",
                        new int[]{1, 2, 3, 4, 5},
                        1,
                        new int[]{1, 2, 3, 4, 5},
                        "k = 1",
                        false));

        edgeTests.add(
                new TestCase(
                        "E3",
                        new int[]{1, 2, 3},
                        10,
                        new int[]{3, 2, 1},
                        "k greater than array length",
                        false));

        edgeTests.add(
                new TestCase(
                        "E4",
                        new int[]{5, 5, 5, 5},
                        2,
                        new int[]{5, 5, 5, 5},
                        "All equal values",
                        false));

        edgeTests.add(
                new TestCase(
                        "E5",
                        new int[]{
                                Integer.MAX_VALUE,
                                Integer.MIN_VALUE,
                                0
                        },
                        2,
                        new int[]{
                                Integer.MIN_VALUE,
                                Integer.MAX_VALUE,
                                0
                        },
                        "Contains MIN/MAX values",
                        false));

        edgeTests.add(
                new TestCase(
                        "E6",
                        new int[]{-1, -2, -3, -4, -5},
                        2,
                        new int[]{-2, -1, -4, -3, -5},
                        "Negative numbers",
                        false));

        edgeTests.add(
                new TestCase(
                        "E7",
                        new int[]{1, 1, 2, 1, 1},
                        2,
                        new int[]{1, 1, 1, 2, 1},
                        "Duplicates",
                        false));

        // =====================================================
        // COMBINE TESTS
        // =====================================================

        List<TestCase> allTests = new ArrayList<>();

        allTests.addAll(positiveTests);
        allTests.addAll(negativeTests);
        allTests.addAll(edgeTests);

        System.out.println("##########################################################");
        System.out.println("######## REVERSE AN ARRAY IN GROUPS PROBLEM ##############");
        System.out.println("##########################################################");
        System.out.println();

        runTests(
                "two pointers (temp swap)",
                ReverseAnArrayInGroups::reverseGroupsTwoPointers,
                allTests);

        runTests(
                "two pointers (XOR swap)",
                ReverseAnArrayInGroups::reverseGroupsXorSwap,
                allTests);

        // =====================================================
        // CLONE SAFETY TEST
        // =====================================================

        System.out.println(
                "========================= clone safety test =========================");

        int[] original = {1, 2, 3, 4, 5};

        int[] result =
                reverseGroupsTwoPointers(original, 1);

        result[0] = 999;

        boolean passed =
                Arrays.equals(
                        original,
                        new int[]{1, 2, 3, 4, 5});

        if (passed) {
            System.out.println(" Clone safety test: passed");
        } else {
            System.out.println(" Clone safety test: FAILED");
            System.out.println(
                    " Original array was modified: "
                            + Arrays.toString(original));
        }
    }
}