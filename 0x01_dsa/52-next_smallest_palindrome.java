import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Next Smallest Palindrome (Digit Array).
 *
 * Implementations:
 *
 * 1. Brute Force (Oracle)
 *      Increment until palindrome
 *
 * 2. Mirror & Carry Propagation
 *      O(n)
 *
 * The brute-force implementation is retained as a correctness oracle
 * for deterministic and randomised testing.
 */
public class NextSmallestPalindromeTestHarness {

    /* **********************************************************************
     * Palindrome Logic
     * **********************************************************************/

    static boolean validDigits(int[] digits) {

        return digits != null && digits.length > 0;
    }

    /**
     * Brute Force Oracle.
     *
     * Repeatedly increments the digit array by 1
     * until a palindrome is reached.
     */
    static int[] nextSmallestPalindromeBruteForce(int[] digits) {

        if (!validDigits(digits)) {
            return null;
        }

        int n = digits.length;
        int[] result = digits.clone();

        while (true) {

            int carry = 1;

            // Increment like big integer
            for (int i = n - 1; i >= 0; i--) {

                result[i] += carry;

                if (result[i] == 10) {
                    result[i] = 0;
                    carry = 1;
                } else {
                    carry = 0;
                    break;
                }
            }

            // Overflow: allocate new digit at front
            if (carry == 1) {

                int[] newDigits = new int[n + 1];
                newDigits[0] = 1;

                for (int i = 0; i < n; i++) {
                    newDigits[i + 1] = result[i];
                }

                result = newDigits;
                n++;
            }

            if (checkPalindrome(result) == 1) {
                break;
            }
        }

        return result;
    }

    /**
     * Optimised approach using mirroring and carry propagation.
     */
    static int[] nextSmallestPalindromeMirrorAndCarryPropagation(int[] digits) {

        if (!validDigits(digits)) {
            return null;
        }

        int n = digits.length;
        int[] result;

        if (areAllNines(digits) == 1) {

            // 9 -> 11, 99 -> 101, 999 -> 1001, etc.
            result = new int[n + 1];
            result[0] = 1;

            for (int i = 1; i < n; i++) {
                result[i] = 0;
            }

            result[n] = 1;

        } else {

            result = digits.clone();
            nextPalindrome(result);
        }

        return result;
    }

    /* **********************************************************************
     * Helpers
     * **********************************************************************/

    static int checkPalindrome(int[] digits) {

        int n = digits.length;

        for (int i = 0; i < n / 2; i++) {
            if (digits[i] != digits[n - 1 - i]) {
                return 0;
            }
        }

        return 1;
    }

    static int areAllNines(int[] digits) {

        for (int digit : digits) {
            if (digit != 9) {
                return 0;
            }
        }

        return 1;
    }

    /**
     * In-place transform: given a digit array, modifies it to the
     * next palindrome that is >= the current value.
     *
     * Combined with the all-9s special case, this yields the next
     * strictly larger palindrome.
     */
    static void nextPalindrome(int[] digits) {

        int n = digits.length;
        int mid = n / 2;

        boolean leftSmaller = false;

        int i = mid - 1;
        int j = (n % 2 == 1) ? mid + 1 : mid;

        // Find the first mismatch from the middle outwards
        while (i >= 0 && digits[i] == digits[j]) {
            i--;
            j++;
        }

        // Determine if left side is smaller than right side
        if (i < 0 || digits[i] < digits[j]) {
            leftSmaller = true;
        }

        // Mirror left side to right side
        while (i >= 0) {
            digits[j] = digits[i];
            j++;
            i--;
        }

        // If left side was smaller, need to increment the middle
        // then propagate carry back and mirror again
        if (leftSmaller) {

            int carry = 1;
            i = mid - 1;

            if (n % 2 == 1) {

                digits[mid] += carry;
                carry = digits[mid] / 10;
                digits[mid] %= 10;

                j = mid + 1;

            } else {

                j = mid;
            }

            while (i >= 0) {

                digits[i] += carry;
                carry = digits[i] / 10;
                digits[i] %= 10;

                digits[j] = digits[i];
                j++;
                i--;
            }
        }
    }

    /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    static class TestCase {

        final String id;
        final int[] input;
        final int[] expected;
        final String description;

        TestCase(
                String id,
                int[] input,
                int[] expected,
                String description) {

            this.id = id;
            this.input = input;
            this.expected = expected;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {

        int[] solve(int[] digits);
    }

    static class MethodCase {

        final String name;
        final Algorithm algorithm;

        MethodCase(
                String name,
                Algorithm algorithm) {

            this.name = name;
            this.algorithm = algorithm;
        }
    }

    /* **********************************************************************
     * Utilities
     * **********************************************************************/

    static int[] cloneArray(int[] nums) {

        if (nums == null) {
            return null;
        }

        return nums.clone();
    }

    static String formatArray(int[] nums) {

        if (nums == null) {
            return "null";
        }

        return Arrays.toString(nums);
    }

    static boolean arraysEqual(int[] a, int[] b) {

        return Arrays.equals(a, b);
    }

    static void runTests(
            String algorithm,
            Algorithm method,
            List<TestCase> tests) {

        System.out.println(
                "======================================================");
        System.out.println(algorithm);
        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            try {

                int[] actual =
                        method.solve(
                                cloneArray(test.input));

                if (arraysEqual(actual, test.expected)) {

                    passed++;

                    System.out.printf(
                            "✓ %s (%s)%n",
                            test.id,
                            test.description);

                } else {

                    failed++;

                    System.out.printf(
                            "✗ %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.printf(
                            "  input     = %s%n",
                            formatArray(test.input));

                    System.out.printf(
                            "  expected  = %s%n",
                            formatArray(test.expected));

                    System.out.printf(
                            "  actual    = %s%n",
                            formatArray(actual));
                }

            } catch (Exception ex) {

                failed++;

                System.out.printf(
                        "✗ %s (%s)%n",
                        test.id,
                        test.description);

                System.out.printf(
                        "  input     = %s%n",
                        formatArray(test.input));

                System.out.printf(
                        "  exception = %s%n",
                        ex);
            }
        }

        System.out.println();

        System.out.printf(
                "Results: %d passed, %d failed, %d total%n",
                passed,
                failed,
                tests.size());

        System.out.println();
    }

    /* **********************************************************************
     * Randomised Testing
     * **********************************************************************/

    static int[] randomDigits(
            Random rng,
            int maxLength) {

        int n = rng.nextInt(maxLength + 1);

        if (n == 0) {
            return new int[0];
        }

        int[] digits = new int[n];

        // Avoid leading zero for non-empty arrays
        digits[0] = 1 + rng.nextInt(9);

        for (int i = 1; i < n; i++) {
            digits[i] = rng.nextInt(10);
        }

        return digits;
    }

    static void runRandomisedTests(int iterations) {

        System.out.println(
                "======================================================");
        System.out.println(
                "Randomised Cross Checks");
        System.out.println(
                "======================================================");

        Random rng = new Random(987654321L);

        for (int i = 1; i <= iterations; i++) {

            int[] digits =
                    randomDigits(
                            rng,
                            8);

            if (!validDigits(digits)) {
                continue;
            }

            int[] brute =
                    nextSmallestPalindromeBruteForce(
                            cloneArray(digits));

            int[] optimized =
                    nextSmallestPalindromeMirrorAndCarryPropagation(
                            cloneArray(digits));

            if (!arraysEqual(brute, optimized)) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "digits    = " + formatArray(digits));

                System.out.println(
                        "brute     = " + formatArray(brute));

                System.out.println(
                        "optimized = " + formatArray(optimized));

                return;
            }
        }

        System.out.printf(
                "All %d Randomised tests passed.%n%n",
                iterations);
    }

    /* **********************************************************************
     * Main
     * **********************************************************************/

    public static void main(String[] args) {

        List<TestCase> tests = new ArrayList<>();

        /*
         * ============================================================
         * Single Digit / Small Arrays
         * ============================================================
         */

        tests.add(new TestCase(
                "A1",
                new int[]{1},
                new int[]{2},
                "Single digit: simple increment"));

        tests.add(new TestCase(
                "A2",
                new int[]{9},
                new int[]{1, 0, 1},
                "Single digit nine"));

        tests.add(new TestCase(
                "A3",
                new int[]{1, 2, 3},
                new int[]{1, 3, 1},
                "Simple three-digit"));

        tests.add(new TestCase(
                "A4",
                new int[]{1, 2, 9},
                new int[]{1, 3, 1},
                "Carry from right side"));

        /*
         * ============================================================
         * All Nines / Overflow Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "N1",
                new int[]{9, 9},
                new int[]{1, 0, 1},
                "Two digits all nines"));

        tests.add(new TestCase(
                "N2",
                new int[]{9, 9, 9},
                new int[]{1, 0, 0, 1},
                "Three digits all nines"));

        /*
         * ============================================================
         * Already Palindrome (Next Larger)
         * ============================================================
         */

        tests.add(new TestCase(
                "P1",
                new int[]{1, 2, 1},
                new int[]{1, 3, 1},
                "Already palindrome: odd length"));

        tests.add(new TestCase(
                "P2",
                new int[]{1, 3, 3, 1},
                new int[]{1, 3, 4, 3, 1},
                "Already palindrome: even length"));

        /*
         * ============================================================
         * Mixed Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "M1",
                new int[]{2, 3, 5, 8},
                nextSmallestPalindromeBruteForce(
                        new int[]{2, 3, 5, 8}),
                "Mixed digits cross-check"));

        tests.add(new TestCase(
                "M2",
                new int[]{4, 5, 4, 9},
                nextSmallestPalindromeBruteForce(
                        new int[]{4, 5, 4, 9}),
                "Non-palindrome cross-check"));

        /*
         * ============================================================
         * Edge Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "E1",
                null,
                null,
                "Null array"));

        tests.add(new TestCase(
                "E2",
                new int[]{},
                null,
                "Empty array"));

        System.out.println(
                "############################################################");
        System.out.println(
                "######## NEXT SMALLEST PALINDROME (DIGIT ARRAY) ###########");
        System.out.println(
                "############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "Brute Force (Oracle)",
                        NextSmallestPalindromeTestHarness::nextSmallestPalindromeBruteForce),

                new MethodCase(
                        "Mirror & Carry Propagation O(n)",
                        NextSmallestPalindromeTestHarness::nextSmallestPalindromeMirrorAndCarryPropagation)
        );

        for (MethodCase method : methods) {

            runTests(
                    method.name,
                    method.algorithm,
                    tests);
        }

        runRandomisedTests(5000);
    }
}
