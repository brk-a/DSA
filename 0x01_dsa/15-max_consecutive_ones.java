import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class MaxConsecutiveOnes {

    /*
     * Problem:
     * Find the length of the longest consecutive run of identical numbers.
     *
     * Example:
     * [1,1,1,2,2,3,3,3,3]
     * Answer = 4
     *
     * Behaviour:
     * - null input -> 0
     * - empty input -> 0
     */

    /**
     * Simple traversal approach.
     *
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    static int maxConsecutiveOnesSimpleTraversal(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }

        int maxCount = 1;
        int count = 1;

        for (int i = 1; i < nums.length; i++) {

            if (nums[i] == nums[i - 1]) {
                count++;
            } else {
                maxCount = Math.max(maxCount, count);
                count = 1;
            }
        }

        return Math.max(maxCount, count);
    }

    /**
     * Bit manipulation approach.
     *
     * Uses XOR:
     * a ^ b == 0 when a == b
     *
     * Time Complexity: O(n)
     * Space Complexity: O(1)
     */
    static int maxConsecutiveOnesBitManipulation(int[] nums) {

        if (nums == null || nums.length == 0) {
            return 0;
        }

        int maxCount = 1;
        int count = 1;

        int prev = nums[0];

        for (int i = 1; i < nums.length; i++) {

            int num = nums[i];

            if ((prev ^ num) == 0) {
                count++;
            } else {
                maxCount = Math.max(maxCount, count);
                count = 1;
            }

            prev = num;
        }

        return Math.max(maxCount, count);
    }

    /* =====================================================
       TEST INFRASTRUCTURE
       ===================================================== */

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

    static void runTests(
            String methodName,
            Function<int[], Integer> method,
            List<TestCase> tests
    ) {

        System.out.println(
                "==================== METHOD: "
                        + methodName
                        + " ===================="
        );

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            int result = method.apply(test.nums);

            boolean success = result == test.expected;

            if (success) {
                passed++;
                System.out.printf(
                        "Test %s (%s): PASSED%n",
                        test.id,
                        test.description
                );
            } else {
                failed++;

                System.out.printf(
                        "Test %s (%s): FAILED%n",
                        test.id,
                        test.description
                );

                System.out.printf(
                        " Input    : %s%n",
                        test.nums == null
                                ? "null"
                                : Arrays.toString(test.nums)
                );

                System.out.printf(
                        " Expected : %d%n",
                        test.expected
                );

                System.out.printf(
                        " Got      : %d%n",
                        result
                );
            }
        }

        System.out.printf(
                "Results: %d passed, %d failed, Total=%d%n%n",
                passed,
                failed,
                tests.size()
        );
    }

    public static void main(String[] args) {

        List<TestCase> positiveTests = new ArrayList<>();
        List<TestCase> negativeTests = new ArrayList<>();
        List<TestCase> edgeTests = new ArrayList<>();

        /* =====================================================
           POSITIVE TESTS
           ===================================================== */

        positiveTests.add(
                new TestCase(
                        "P1",
                        new int[]{1, 1, 1, 2, 2},
                        3,
                        "Three consecutive ones"
                )
        );

        positiveTests.add(
                new TestCase(
                        "P2",
                        new int[]{1, 2, 2, 2, 3},
                        3,
                        "Middle run is longest"
                )
        );

        positiveTests.add(
                new TestCase(
                        "P3",
                        new int[]{4, 4, 4, 4, 4},
                        5,
                        "Entire array identical"
                )
        );

        positiveTests.add(
                new TestCase(
                        "P4",
                        new int[]{1, 2, 3, 3, 3, 3, 4},
                        4,
                        "Longest run near end"
                )
        );

        positiveTests.add(
                new TestCase(
                        "P5",
                        new int[]{5, 5, 1, 1, 1, 1, 7},
                        4,
                        "Longest run in middle"
                )
        );

        positiveTests.add(
                new TestCase(
                        "P6",
                        new int[]{9, 9, 9, 9, 2},
                        4,
                        "Longest run at start"
                )
        );

        /* =====================================================
           NEGATIVE / VALIDATION TESTS
           ===================================================== */

        negativeTests.add(
                new TestCase(
                        "N1",
                        null,
                        0,
                        "Null input"
                )
        );

        negativeTests.add(
                new TestCase(
                        "N2",
                        new int[]{},
                        0,
                        "Empty array"
                )
        );

        negativeTests.add(
                new TestCase(
                        "N3",
                        new int[]{42},
                        1,
                        "Single element"
                )
        );

        /* =====================================================
           EDGE TESTS
           ===================================================== */

        edgeTests.add(
                new TestCase(
                        "E1",
                        new int[]{1, 2, 3, 4, 5},
                        1,
                        "No duplicates"
                )
        );

        edgeTests.add(
                new TestCase(
                        "E2",
                        new int[]{0, 0, 0, 0, 0, 0},
                        6,
                        "All zeros"
                )
        );

        edgeTests.add(
                new TestCase(
                        "E3",
                        new int[]{-1, -1, -1, -2, -2},
                        3,
                        "Negative numbers"
                )
        );

        edgeTests.add(
                new TestCase(
                        "E4",
                        new int[]{
                                Integer.MAX_VALUE,
                                Integer.MAX_VALUE,
                                Integer.MIN_VALUE
                        },
                        2,
                        "Integer boundary values"
                )
        );

        edgeTests.add(
                new TestCase(
                        "E5",
                        new int[]{1, 1, 2, 2, 3, 3},
                        2,
                        "Multiple equal sized runs"
                )
        );

        edgeTests.add(
                new TestCase(
                        "E6",
                        new int[]{7, 7, 7, 1, 1, 1, 1},
                        4,
                        "Longest run at end"
                )
        );

        edgeTests.add(
                new TestCase(
                        "E7",
                        new int[]{1, 2, 2, 3, 4, 4, 4, 5},
                        3,
                        "Several duplicate groups"
                )
        );

        edgeTests.add(
                new TestCase(
                        "E8",
                        new int[]{
                                8, 8,
                                1, 1, 1,
                                2, 2,
                                3, 3, 3, 3,
                                4
                        },
                        4,
                        "Many groups of varying lengths"
                )
        );

        edgeTests.add(
                new TestCase(
                        "E9",
                        new int[]{
                                -5, -5,
                                -5, -5,
                                -5, -5,
                                -5
                        },
                        7,
                        "Large negative run"
                )
        );

        edgeTests.add(
                new TestCase(
                        "E10",
                        new int[]{1, 2, 3, 4, 4},
                        2,
                        "Run appears at very end"
                )
        );

        /* =====================================================
           COMBINE TESTS
           ===================================================== */

        List<TestCase> allTests = new ArrayList<>();

        allTests.addAll(positiveTests);
        allTests.addAll(negativeTests);
        allTests.addAll(edgeTests);

        System.out.println("#####################################################");
        System.out.println("############ MAX CONSECUTIVE IDENTICAL VALUES #######");
        System.out.println("#####################################################");
        System.out.println();

        runTests(
                "Simple Traversal",
                MaxConsecutiveOnes::maxConsecutiveOnesSimpleTraversal,
                allTests
        );

        runTests(
                "Bit Manipulation (XOR)",
                MaxConsecutiveOnes::maxConsecutiveOnesBitManipulation,
                allTests
        );
    }
}