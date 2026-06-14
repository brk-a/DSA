import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class SortedArrayInWaveForm {

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
     * Sorted-array wave transformation.
     *
     * Input:
     * [1,2,3,4,5,6]
     *
     * Output:
     * [2,1,4,3,6,5]
     *
     * Time: O(n)
     * Space: O(n)
     */
    static int[] sortedArrayInWaveForm(int[] nums) {

        if (nums == null) {
            return new int[0];
        }

        int[] result = nums.clone();

        for (int i = 0; i < result.length - 1; i += 2) {

            int temp = result[i];
            result[i] = result[i + 1];
            result[i + 1] = temp;
        }

        return result;
    }

    static void runTests(
        String methodName,
        Function<int[], int[]> func,
        List<TestCase> tests
    ) {

        System.out.println(
            "==================== "
            + methodName
            + " ===================="
        );

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            int[] actual = func.apply(test.input);

            boolean ok =
                Arrays.equals(actual, test.expected);

            if (ok) {

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
                    Arrays.toString(test.expected)
                );

                System.out.printf(
                    "  actual   = %s%n",
                    Arrays.toString(actual)
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

    public static void main(String[] args) {

        List<TestCase> tests =
            new ArrayList<>();

        // =====================================================
        // Positive Cases
        // =====================================================

        tests.add(new TestCase(
            "P1",
            new int[]{1,2,3,4,5,6},
            new int[]{2,1,4,3,6,5},
            "Even length sorted array"
        ));

        tests.add(new TestCase(
            "P2",
            new int[]{1,2,3,4,5},
            new int[]{2,1,4,3,5},
            "Odd length sorted array"
        ));

        tests.add(new TestCase(
            "P3",
            new int[]{10,20,30,40},
            new int[]{20,10,40,30},
            "Four elements"
        ));

        tests.add(new TestCase(
            "P4",
            new int[]{1,3,5,7,9,11,13},
            new int[]{3,1,7,5,11,9,13},
            "Larger odd-sized array"
        ));

        // =====================================================
        // Duplicates
        // =====================================================

        tests.add(new TestCase(
            "D1",
            new int[]{1,1,2,2,3,3},
            new int[]{1,1,2,2,3,3},
            "Duplicate pairs"
        ));

        tests.add(new TestCase(
            "D2",
            new int[]{1,1,1,1},
            new int[]{1,1,1,1},
            "All duplicates"
        ));

        tests.add(new TestCase(
            "D3",
            new int[]{1,2,2,3,3,4},
            new int[]{2,1,3,2,4,3},
            "Mixed duplicates"
        ));

        // =====================================================
        // Edge Cases
        // =====================================================

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
            new int[]{5},
            new int[]{5},
            "Single element"
        ));

        tests.add(new TestCase(
            "E4",
            new int[]{1,2},
            new int[]{2,1},
            "Two elements"
        ));

        // =====================================================
        // Negative Numbers
        // =====================================================

        tests.add(new TestCase(
            "N1",
            new int[]{-5,-4,-3,-2,-1},
            new int[]{-4,-5,-2,-3,-1},
            "All negative values"
        ));

        tests.add(new TestCase(
            "N2",
            new int[]{-10,-5,0,5,10},
            new int[]{-5,-10,5,0,10},
            "Negative to positive"
        ));

        // =====================================================
        // Zeroes
        // =====================================================

        tests.add(new TestCase(
            "Z1",
            new int[]{0,0,1,2,3},
            new int[]{0,0,2,1,3},
            "Leading zeroes"
        ));

        tests.add(new TestCase(
            "Z2",
            new int[]{0,0,0,0},
            new int[]{0,0,0,0},
            "All zeroes"
        ));

        // =====================================================
        // Integer Limits
        // =====================================================

        tests.add(new TestCase(
            "L1",
            new int[]{
                Integer.MIN_VALUE,
                -1,
                0,
                Integer.MAX_VALUE
            },
            new int[]{
                -1,
                Integer.MIN_VALUE,
                Integer.MAX_VALUE,
                0
            },
            "Extreme integer values"
        ));

        System.out.println(
            "####################################################"
        );
        System.out.println(
            "############ ARRAY IN WAVE FORM TESTS ##############"
        );
        System.out.println(
            "####################################################"
        );
        System.out.println();

        runTests(
            "Adjacent Pair Swap Wave Conversion",
            ArrayInWaveForm::arrayInWaveForm,
            tests
        );
    }
}