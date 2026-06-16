import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class ArrayInWaveForm {

    static class TestCase {
        final String id;
        final int[] input;
        final String description;

        TestCase(
            String id,
            int[] input,
            String description
        ) {
            this.id = id;
            this.input = input;
            this.description = description;
        }
    }

    /**
     * Approach 1: Sort then swap adjacent pairs
     *
     * Example:
     * [10,90,49,2,1,5,23]
     *
     * Sort:
     * [1,2,5,10,23,49,90]
     *
     * Swap pairs:
     * [2,1,10,5,49,23,90]
     *
     * Time: O(n log n)
     * Space: O(n)
     */
    static int[] waveSortUsingSorting(int[] nums) {

        if (nums == null) {
            return new int[0];
        }

        int[] result = nums.clone();

        Arrays.sort(result);

        for (int i = 0; i < result.length - 1; i += 2) {
            swap(result, i, i + 1);
        }

        return result;
    }

    /**
     * Approach 2: One-pass optimal solution
     *
     * For every even index i:
     *
     * arr[i] >= arr[i - 1]
     * arr[i] >= arr[i + 1]
     *
     * Time: O(n)
     * Space: O(n) because we clone input.
     *
     * O(1) extra space if performed directly on input.
     */
    static int[] waveSortOptimal(int[] nums) {

        if (nums == null) {
            return new int[0];
        }

        int[] result = nums.clone();

        for (int i = 0; i < result.length; i += 2) {

            if (i > 0 && result[i] < result[i - 1]) {
                swap(result, i, i - 1);
            }

            if (i < result.length - 1
                && result[i] < result[i + 1]) {
                swap(result, i, i + 1);
            }
        }

        return result;
    }

    /**
     * Brute-force style approach.
     *
     * Repeatedly fixes local violations.
     *
     * Educational approach only.
     *
     * Time: O(n)
     * Space: O(n)
     */
    static int[] waveSortBruteForce(int[] nums) {

        if (nums == null) {
            return new int[0];
        }

        int[] result = nums.clone();

        for (int i = 0; i < result.length - 1; i++) {

            if (i % 2 == 0) {

                if (result[i] < result[i + 1]) {
                    swap(result, i, i + 1);
                }

            } else {

                if (result[i] > result[i + 1]) {
                    swap(result, i, i + 1);
                }
            }
        }

        return result;
    }

    static void swap(
        int[] arr,
        int i,
        int j
    ) {
        int temp = arr[i];
        arr[i] = arr[j];
        arr[j] = temp;
    }

    /**
     * Verifies:
     *
     * arr[0] >= arr[1]
     * arr[1] <= arr[2]
     * arr[2] >= arr[3]
     * ...
     */
    static boolean isWaveArray(int[] arr) {

        if (arr == null) {
            return false;
        }

        for (int i = 0; i < arr.length - 1; i++) {

            if (i % 2 == 0) {

                if (arr[i] < arr[i + 1]) {
                    return false;
                }

            } else {

                if (arr[i] > arr[i + 1]) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Ensures output contains exactly
     * the same multiset of values.
     */
    static boolean containsSameElements(
        int[] original,
        int[] transformed
    ) {

        if (original == null) {
            return transformed.length == 0;
        }

        int[] a = original.clone();
        int[] b = transformed.clone();

        Arrays.sort(a);
        Arrays.sort(b);

        return Arrays.equals(a, b);
    }

    static void runTests(
        String methodName,
        Function<int[], int[]> func,
        List<TestCase> tests
    ) {

        System.out.println(
            "===================================================="
        );
        System.out.println(methodName);
        System.out.println(
            "===================================================="
        );

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            int[] actual = func.apply(test.input);

            boolean waveValid =
                isWaveArray(actual);

            boolean elementsValid =
                containsSameElements(
                    test.input,
                    actual
                );

            boolean success =
                waveValid && elementsValid;

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
                    "  input     = %s%n",
                    Arrays.toString(test.input)
                );

                System.out.printf(
                    "  output    = %s%n",
                    Arrays.toString(actual)
                );

                System.out.printf(
                    "  waveValid = %s%n",
                    waveValid
                );

                System.out.printf(
                    "  sameElems = %s%n",
                    elementsValid
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
        // Typical Cases
        // =================================================

        tests.add(new TestCase(
            "P1",
            new int[]{10, 90, 49, 2, 1, 5, 23},
            "GeeksForGeeks example"
        ));

        tests.add(new TestCase(
            "P2",
            new int[]{3, 6, 5, 10, 7, 20},
            "Typical unsorted array"
        ));

        tests.add(new TestCase(
            "P3",
            new int[]{1, 2, 3, 4, 5, 6},
            "Already sorted ascending"
        ));

        tests.add(new TestCase(
            "P4",
            new int[]{9, 8, 7, 6, 5, 4},
            "Descending array"
        ));

        tests.add(new TestCase(
            "P5",
            new int[]{8, 1, 7, 2, 6, 3, 5, 4},
            "Alternating high-low values"
        ));

        // =================================================
        // Duplicates
        // =================================================

        tests.add(new TestCase(
            "D1",
            new int[]{1, 1, 1, 1},
            "All duplicates"
        ));

        tests.add(new TestCase(
            "D2",
            new int[]{4, 4, 2, 2, 8, 8},
            "Duplicate groups"
        ));

        tests.add(new TestCase(
            "D3",
            new int[]{5, 5, 5, 2, 2, 1},
            "Many duplicates"
        ));

        // =================================================
        // Edge Cases
        // =================================================

        tests.add(new TestCase(
            "E1",
            new int[]{},
            "Empty array"
        ));

        tests.add(new TestCase(
            "E2",
            null,
            "Null input"
        ));

        tests.add(new TestCase(
            "E3",
            new int[]{42},
            "Single element"
        ));

        tests.add(new TestCase(
            "E4",
            new int[]{1, 2},
            "Two elements"
        ));

        tests.add(new TestCase(
            "E5",
            new int[]{2, 1},
            "Two elements reversed"
        ));

        // =================================================
        // Negative Values
        // =================================================

        tests.add(new TestCase(
            "N1",
            new int[]{-5, -1, -3, -2, -4},
            "All negative values"
        ));

        tests.add(new TestCase(
            "N2",
            new int[]{-10, 5, -3, 8, 0, -1},
            "Mixed negatives and positives"
        ));

        // =================================================
        // Zeroes
        // =================================================

        tests.add(new TestCase(
            "Z1",
            new int[]{0, 0, 0, 0},
            "All zeroes"
        ));

        tests.add(new TestCase(
            "Z2",
            new int[]{0, 5, 0, 3, 0, 2},
            "Interspersed zeroes"
        ));

        // =================================================
        // Extreme Integer Values
        // =================================================

        tests.add(new TestCase(
            "L1",
            new int[]{
                Integer.MAX_VALUE,
                Integer.MIN_VALUE,
                0,
                -1,
                1
            },
            "Integer limits"
        ));

        System.out.println(
            "########################################################"
        );
        System.out.println(
            "############### ARRAY IN WAVE FORM #####################"
        );
        System.out.println(
            "########################################################"
        );
        System.out.println();

        runTests(
            "Brute Force Local Fixes",
            ArrayInWaveForm::waveSortBruteForce,
            tests
        );

        runTests(
            "Sort Then Swap Adjacent Pairs",
            ArrayInWaveForm::waveSortUsingSorting,
            tests
        );

        runTests(
            "One-Pass Optimal Solution",
            ArrayInWaveForm::waveSortOptimal,
            tests
        );
    }
}
