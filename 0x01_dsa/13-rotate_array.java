import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;

public class RotateArrayLeft {

    /*
     * All methods clone the input and return the modified clone.
     * They do NOT mutate the caller's input array.
     *
     * Behaviour:
     * - null input -> returns null
     * - empty input -> returns new int[0]
     * - k < 0 -> throws IllegalArgumentException
     * - k == 0 -> returns cloned array
     *
     * Rotation direction:
     * LEFT rotation
     *
     * Example:
     * [1,2,3,4,5], k=2
     * => [3,4,5,1,2]
     */

    static int[] rotateBruteForce(int[] nums, int k) {

        if (nums == null) {
            return null;
        }

        if (nums.length == 0) {
            return new int[0];
        }

        if (k < 0) {
            throw new IllegalArgumentException("k must be >= 0");
        }

        if (k == 0) {
            return nums.clone();
        }

        int[] result = nums.clone();
        int n = result.length;

        k %= n;

        for (int i = 0; i < k; i++) {

            int first = result[0];

            for (int j = 0; j < n - 1; j++) {
                result[j] = result[j + 1];
            }

            result[n - 1] = first;
        }

        return result;
    }

    static int[] rotateTempArray(int[] nums, int k) {

        if (nums == null) {
            return null;
        }

        if (nums.length == 0) {
            return new int[0];
        }

        if (k < 0) {
            throw new IllegalArgumentException("k must be >= 0");
        }

        if (k == 0) {
            return nums.clone();
        }

        int n = nums.length;
        k %= n;

        int[] result = new int[n];

        for (int i = 0; i < n - k; i++) {
            result[i] = nums[k + i];
        }

        for (int i = 0; i < k; i++) {
            result[n - k + i] = nums[i];
        }

        return result;
    }

    static int[] rotateReversalAlgo(int[] nums, int k) {

        if (nums == null) {
            return null;
        }

        if (nums.length == 0) {
            return new int[0];
        }

        if (k < 0) {
            throw new IllegalArgumentException("k must be >= 0");
        }

        if (k == 0) {
            return nums.clone();
        }

        int n = nums.length;
        k %= n;

        int[] result = nums.clone();

        reverse(result, 0, k - 1);
        reverse(result, k, n - 1);
        reverse(result, 0, n - 1);

        return result;
    }

    static int[] rotateJugglingAlgo(int[] nums, int k) {

        if (nums == null) {
            return null;
        }

        if (nums.length == 0) {
            return new int[0];
        }

        if (k < 0) {
            throw new IllegalArgumentException("k must be >= 0");
        }

        if (k == 0) {
            return nums.clone();
        }

        int n = nums.length;
        k %= n;

        int[] result = nums.clone();

        int gcd = gcd(n, k);

        for (int i = 0; i < gcd; i++) {

            int temp = result[i];
            int j = i;

            while (true) {

                int d = j + k;

                if (d >= n) {
                    d -= n;
                }

                if (d == i) {
                    break;
                }

                result[j] = result[d];
                j = d;
            }

            result[j] = temp;
        }

        return result;
    }

    static void reverse(int[] nums, int left, int right) {

        while (left < right) {

            int temp = nums[left];
            nums[left] = nums[right];
            nums[right] = temp;

            left++;
            right--;
        }
    }

    static int gcd(int a, int b) {

        while (b != 0) {
            int temp = b;
            b = a % b;
            a = temp;
        }

        return a;
    }

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

                    continue;
                }

                boolean ok =
                        Arrays.equals(got, test.expected);

                if (ok) {

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
                            " got=%s expected=%s%n",
                            Arrays.toString(got),
                            Arrays.toString(test.expected));
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

                    System.out.println(ex);
                }
            }
        }

        System.out.printf(
                "Results: %d passed, %d failed%n%n",
                passed,
                failed);
    }

    public static void main(String[] args) {

        List<TestCase> tests = new ArrayList<>();

        tests.add(new TestCase(
                "P1",
                new int[]{1,2,3,4,5},
                2,
                new int[]{3,4,5,1,2},
                "Basic rotation",
                false));

        tests.add(new TestCase(
                "P2",
                new int[]{1,2,3,4,5,6},
                3,
                new int[]{4,5,6,1,2,3},
                "Half rotation",
                false));

        tests.add(new TestCase(
                "P3",
                new int[]{1,2,3,4,5},
                7,
                new int[]{3,4,5,1,2},
                "k greater than n",
                false));

        tests.add(new TestCase(
                "N1",
                null,
                2,
                null,
                "Null input",
                false));

        tests.add(new TestCase(
                "N2",
                new int[]{1,2,3},
                -1,
                null,
                "Negative k",
                true));

        tests.add(new TestCase(
                "E1",
                new int[]{},
                5,
                new int[]{},
                "Empty array",
                false));

        tests.add(new TestCase(
                "E2",
                new int[]{42},
                100,
                new int[]{42},
                "Single element",
                false));

        tests.add(new TestCase(
                "E3",
                new int[]{1,2,3},
                0,
                new int[]{1,2,3},
                "Zero rotation",
                false));

        runTests("Brute Force", RotateArrayLeft::rotateBruteForce, tests);
        runTests("Temp Array", RotateArrayLeft::rotateTempArray, tests);
        runTests("Reversal Algorithm", RotateArrayLeft::rotateReversalAlgo, tests);
        runTests("Juggling Algorithm", RotateArrayLeft::rotateJugglingAlgo, tests);

        System.out.println(
                "========================= clone safety test =========================");

        int[] original = {1,2,3,4,5};

        int[] result = rotateTempArray(original, 2);

        result[0] = 999;

        boolean passed =
                Arrays.equals(
                        original,
                        new int[]{1,2,3,4,5});

        System.out.println(
                passed
                        ? "Clone safety test: passed"
                        : "Clone safety test: FAILED");
    }
}