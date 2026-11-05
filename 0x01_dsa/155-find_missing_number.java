import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class FindMissingNumber {

    static record Result(
        int result,
        boolean valid
    ) {}

    static boolean validArray(int[] array) {
        return array != null && array.length > 0;
    }

    /**
     * Linear-search solution.
     *
     * For every candidate from 0 through n, search the array.
     *
     * Time: O(n^2)
     * Space: O(1)
     */
    static Result findMissingNumberLinearSearch(int[] array) {
        if (!validArray(array)) {
            return new Result(-1, false);
        }

        int n = array.length;

        for (int candidate = 0; candidate <= n; candidate++) {
            boolean found = false;

            for (int value : array) {
                if (value == candidate) {
                    found = true;
                    break;
                }
            }

            if (!found) {
                return new Result(candidate, true);
            }
        }

        return new Result(-1, false);
    }

    /**
     * Hash-set solution.
     *
     * Time: O(n)
     * Space: O(n)
     */
    static Result findMissingNumberHash(int[] array) {
        if (!validArray(array)) {
            return new Result(-1, false);
        }

        int n = array.length;
        Set<Integer> values = new HashSet<>();

        for (int value : array) {
            values.add(value);
        }

        for (int candidate = 0; candidate <= n; candidate++) {
            if (!values.contains(candidate)) {
                return new Result(candidate, true);
            }
        }

        return new Result(-1, false);
    }

    /**
     * Arithmetic-series solution.
     *
     * Expected sum of 0 through n is n(n + 1) / 2.
     *
     * Time: O(n)
     * Space: O(1)
     */
    static Result findMissingNumberMathsFormula(int[] array) {
        if (!validArray(array)) {
            return new Result(-1, false);
        }

        long n = array.length;
        long expectedSum = n * (n + 1) / 2;
        long actualSum = 0L;

        for (int value : array) {
            actualSum += value;
        }

        long missing = expectedSum - actualSum;

        if (
            missing < 0
            || missing > n
            || missing > Integer.MAX_VALUE
        ) {
            return new Result(-1, false);
        }

        return new Result((int) missing, true);
    }

    /**
     * XOR solution.
     *
     * Matching values cancel because:
     * x ^ x = 0
     * x ^ 0 = x
     *
     * Time: O(n)
     * Space: O(1)
     */
    static Result findMissingNumberXOR(int[] array) {
        if (!validArray(array)) {
            return new Result(-1, false);
        }

        int n = array.length;
        int missing = n;

        for (int i = 0; i < n; i++) {
            missing ^= i;
            missing ^= array[i];
        }

        return new Result(missing, true);
    }

    static boolean isValidInput(int[] array) {
        if (!validArray(array)) {
            return false;
        }

        int n = array.length;
        boolean[] seen = new boolean[n + 1];

        for (int value : array) {
            if (value < 0 || value > n || seen[value]) {
                return false;
            }

            seen[value] = true;
        }

        return true;
    }

    static class TestCase {
        final String id;
        final int[] input;
        final int expected;
        final boolean expectedValid;
        final String description;

        TestCase(
            String id,
            int[] input,
            int expected,
            boolean expectedValid,
            String description
        ) {
            this.id = id;
            this.input = input;
            this.expected = expected;
            this.expectedValid = expectedValid;
            this.description = description;
        }
    }

    static void runTests(
        String algorithm,
        Function<int[], Result> method,
        List<TestCase> tests
    ) {
        System.out.println("==================================================");
        System.out.println(algorithm);
        System.out.println("==================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {
            try {
                Result actual = method.apply(test.input);

                boolean correct =
                    actual.valid() == test.expectedValid
                    && actual.result() == test.expected;

                if (correct) {
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
                        "  expected = Result(%d, %s)%n",
                        test.expected,
                        test.expectedValid
                    );
                    System.out.printf(
                        "  actual   = Result(%d, %s)%n",
                        actual.result(),
                        actual.valid()
                    );
                }
            } catch (Exception ex) {
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

    static List<TestCase> createTests() {
        List<TestCase> tests = new ArrayList<>();

        tests.add(new TestCase(
            "S1",
            new int[]{3, 0, 1},
            2,
            true,
            "Missing value in the middle"
        ));

        tests.add(new TestCase(
            "S2",
            new int[]{0, 1},
            2,
            true,
            "Missing maximum value"
        ));

        tests.add(new TestCase(
            "S3",
            new int[]{1},
            0,
            true,
            "Missing zero"
        ));

        tests.add(new TestCase(
            "S4",
            new int[]{0, 1, 2, 3, 4},
            5,
            true,
            "Missing maximum value from range 0..5"
        ));

        tests.add(new TestCase(
            "S5",
            new int[]{4, 2, 1, 0},
            3,
            true,
            "Unsorted input"
        ));

        tests.add(new TestCase(
            "S6",
            new int[]{2, 0, 3, 1, 5},
            4,
            true,
            "Larger unsorted input"
        ));

        tests.add(new TestCase(
            "E1",
            null,
            -1,
            false,
            "Null input"
        ));

        tests.add(new TestCase(
            "E2",
            new int[]{},
            -1,
            false,
            "Empty input"
        ));

        System.out.println("############################################################");
        System.out.println("################## FIND MISSING NUMBER #####################");
        System.out.println("############################################################");
        System.out.println();

        return tests;
    }

    public static void main(String[] args) {
        List<TestCase> tests = createTests();

        runTests(
            "Linear Search O(n^2)",
            FindMissingNumber::findMissingNumberLinearSearch,
            tests
        );

        runTests(
            "Hash Set O(n)",
            FindMissingNumber::findMissingNumberHash,
            tests
        );

        runTests(
            "Math Formula O(n)",
            FindMissingNumber::findMissingNumberMathsFormula,
            tests
        );

        runTests(
            "XOR O(n)",
            FindMissingNumber::findMissingNumberXOR,
            tests
        );
    }
}
