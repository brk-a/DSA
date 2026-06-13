import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Function;

public class MoveZeroesToEnd {

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
     * Auxiliary array approach
     * Time: O(n)
     * Space: O(n)
     */
    static int[] moveZeroesToEndBruteForce(int[] nums) {
        if (nums == null) {
            return new int[0];
        }

        int n = nums.length;

        int[] result = nums.clone();
        int[] temp = new int[n];

        int j = 0;

        for (int i = 0; i < n; i++) {
            if (result[i] != 0) {
                temp[j++] = result[i];
            }
        }

        while (j < n) {
            temp[j++] = 0;
        }

        System.arraycopy(temp, 0, result, 0, n);

        return result;
    }

    /**
     * Two-pass approach
     * Time: O(n)
     * Space: O(1)
     */
    static int[] moveZeroesToEndTwoTraversals(int[] nums) {
        if (nums == null) {
            return new int[0];
        }

        int[] result = nums.clone();

        int count = 0;

        for (int i = 0; i < result.length; i++) {
            if (result[i] != 0) {
                result[count++] = result[i];
            }
        }

        while (count < result.length) {
            result[count++] = 0;
        }

        return result;
    }

    /**
     * One-pass approach
     * Time: O(n)
     * Space: O(1)
     */
    static int[] moveZeroesToEndOneTraversal(int[] nums) {
        if (nums == null) {
            return new int[0];
        }

        int[] result = nums.clone();

        int count = 0;

        for (int i = 0; i < result.length; i++) {
            if (result[i] != 0) {

                if (i != count) {
                    int temp = result[i];
                    result[i] = result[count];
                    result[count] = temp;
                }

                count++;
            }
        }

        return result;
    }

    static void runTests(
        String name,
        Function<int[], int[]> func,
        List<TestCase> tests
    ) {
        System.out.println(
            "========================= method: "
            + name
            + " ========================="
        );

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            int[] got = func.apply(test.nums);

            boolean ok =
                Arrays.equals(got, test.expected);

            if (ok) {
                passed++;

                System.out.printf(
                    "✓ Test %s (%s): passed%n",
                    test.id,
                    test.description
                );
            } else {

                failed++;

                System.out.printf(
                    "✗ Test %s (%s): FAILED%n",
                    test.id,
                    test.description
                );

                System.out.printf(
                    "  input=%s%n",
                    Arrays.toString(test.nums)
                );

                System.out.printf(
                    "  got=%s%n",
                    Arrays.toString(got)
                );

                System.out.printf(
                    "  expected=%s%n",
                    Arrays.toString(test.expected)
                );
            }
        }

        System.out.printf(
            "Results: %d passed, %d failed out of %d tests%n%n",
            passed,
            failed,
            tests.size()
        );
    }

    public static void main(String[] args) {

        List<TestCase> tests =
            new ArrayList<>();

        // Positive cases

        tests.add(new TestCase(
            "P1",
            new int[]{1,0,2,3,0,4,0,1},
            new int[]{1,2,3,4,1,0,0,0},
            "Typical mixed array"
        ));

        tests.add(new TestCase(
            "P2",
            new int[]{0,0,1},
            new int[]{1,0,0},
            "Leading zeroes"
        ));

        tests.add(new TestCase(
            "P3",
            new int[]{1,2,3},
            new int[]{1,2,3},
            "No zeroes"
        ));

        tests.add(new TestCase(
            "P4",
            new int[]{0,1,0,3,12},
            new int[]{1,3,12,0,0},
            "LeetCode example"
        ));

        // Edge cases

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

        tests.add(new TestCase(
            "E3",
            new int[]{0,0,0},
            new int[]{0,0,0},
            "All zeroes"
        ));

        tests.add(new TestCase(
            "E4",
            new int[]{5},
            new int[]{5},
            "Single non-zero"
        ));

        tests.add(new TestCase(
            "E5",
            new int[]{0},
            new int[]{0},
            "Single zero"
        ));

        tests.add(new TestCase(
            "E6",
            new int[]{-1,0,-2,0,-3},
            new int[]{-1,-2,-3,0,0},
            "Negative values"
        ));

        System.out.println("########################################################");
        System.out.println("############ MOVE ZEROES TO END PROBLEM ################");
        System.out.println("########################################################");
        System.out.println();

        runTests(
            "auxiliary array",
            MoveZeroesToEnd::moveZeroesToEndBruteForce,
            tests
        );

        runTests(
            "two traversals",
            MoveZeroesToEnd::moveZeroesToEndTwoTraversals,
            tests
        );

        runTests(
            "one traversal (optimal)",
            MoveZeroesToEnd::moveZeroesToEndOneTraversal,
            tests
        );
    }
}