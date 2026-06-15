import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class PlusOne {

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
     * Carry-propagation approach.
     *
     * Time: O(n)
     * Space: O(n)
     */
    static int[] plusOneCarryMethod(int[] nums) {

        if (nums == null) {
            return new int[0];
        }

        int[] result = nums.clone();
        int carry = 1;

        for (int i = result.length - 1; i >= 0; i--) {

            int sum = result[i] + carry;

            result[i] = sum % 10;
            carry = sum / 10;
        }

        if (carry > 0) {

            int[] newArr =
                new int[result.length + 1];

            newArr[0] = carry;

            System.arraycopy(
                result,
                0,
                newArr,
                1,
                result.length
            );

            return newArr;
        }

        return result;
    }

    /**
     * Optimal solution.
     *
     * Time: O(n)
     * Space: O(n)
     */
    static int[] plusOneAlternateMethodOne(
        int[] nums
    ) {

        if (nums == null) {
            return new int[0];
        }

        int[] result = nums.clone();

        int idx = result.length - 1;

        while (
            idx >= 0 &&
            result[idx] == 9
        ) {
            result[idx] = 0;
            idx--;
        }

        if (idx < 0) {

            int[] newArr =
                new int[result.length + 1];

            newArr[0] = 1;

            return newArr;
        }

        result[idx]++;

        return result;
    }

    /**
     * Reverse-array approach.
     *
     * Time: O(n)
     * Space: O(n)
     */
    static int[] plusOneAlternateMethodTwo(
        int[] nums
    ) {

        if (nums == null) {
            return new int[0];
        }

        int[] result = nums.clone();

        reverse(result);

        int idx = 0;

        while (
            idx < result.length &&
            result[idx] == 9
        ) {
            result[idx] = 0;
            idx++;
        }

        if (idx == result.length) {

            int[] newArr =
                new int[result.length + 1];

            System.arraycopy(
                result,
                0,
                newArr,
                0,
                result.length
            );

            newArr[result.length] = 1;

            result = newArr;

        } else {

            result[idx]++;
        }

        reverse(result);

        return result;
    }

    static void reverse(int[] arr) {

        for (
            int i = 0,
                j = arr.length - 1;
            i < j;
            i++, j--
        ) {

            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
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

            int[] actual =
                func.apply(test.input);

            boolean ok =
                Arrays.equals(
                    actual,
                    test.expected
                );

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

        // =================================================
        // Standard Cases
        // =================================================

        tests.add(new TestCase(
            "P1",
            new int[]{1,2,3},
            new int[]{1,2,4},
            "Simple increment"
        ));

        tests.add(new TestCase(
            "P2",
            new int[]{4,3,2,1},
            new int[]{4,3,2,2},
            "Increment last digit"
        ));

        tests.add(new TestCase(
            "P3",
            new int[]{1,2,9},
            new int[]{1,3,0},
            "Single carry"
        ));

        tests.add(new TestCase(
            "P4",
            new int[]{1,9,9},
            new int[]{2,0,0},
            "Multiple carries"
        ));

        // =================================================
        // All Nines
        // =================================================

        tests.add(new TestCase(
            "N1",
            new int[]{9},
            new int[]{1,0},
            "Single nine"
        ));

        tests.add(new TestCase(
            "N2",
            new int[]{9,9},
            new int[]{1,0,0},
            "Double nine"
        ));

        tests.add(new TestCase(
            "N3",
            new int[]{9,9,9},
            new int[]{1,0,0,0},
            "Triple nine"
        ));

        tests.add(new TestCase(
            "N4",
            new int[]{9,9,9,9,9},
            new int[]{1,0,0,0,0,0},
            "Five nines"
        ));

        // =================================================
        // Zeros
        // =================================================

        tests.add(new TestCase(
            "Z1",
            new int[]{0},
            new int[]{1},
            "Zero becomes one"
        ));

        tests.add(new TestCase(
            "Z2",
            new int[]{0,0,0},
            new int[]{0,0,1},
            "Leading zeros"
        ));

        // =================================================
        // Edge Cases
        // =================================================

        tests.add(new TestCase(
            "E1",
            new int[]{},
            new int[]{1},
            "Empty array treated as zero"
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
            new int[]{6},
            "Single digit"
        ));

        tests.add(new TestCase(
            "E4",
            new int[]{8},
            new int[]{9},
            "Single digit no carry"
        ));

        System.out.println(
            "#################################################"
        );
        System.out.println(
            "################ PLUS ONE TESTS #################"
        );
        System.out.println(
            "#################################################"
        );
        System.out.println();

        runTests(
            "Carry Method",
            PlusOne::plusOneCarryMethod,
            tests
        );

        runTests(
            "Alternate Method One",
            PlusOne::plusOneAlternateMethodOne,
            tests
        );

        runTests(
            "Alternate Method Two",
            PlusOne::plusOneAlternateMethodTwo,
            tests
        );
    }
}