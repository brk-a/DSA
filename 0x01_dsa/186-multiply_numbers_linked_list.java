import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

public class MultiplyNumbersLinkedList {

    /* **********************************************************************
     * Constants
     * **********************************************************************/

    private static final long MOD = 1_000_000_007L;

    private static final BigInteger BIG_MOD =
        BigInteger.valueOf(MOD);


    /* **********************************************************************
     * Node
     * **********************************************************************/

    static class Node {
        int val;
        Node next;

        Node(int val) {
            this.val = val;
            this.next = null;
        }

        Node(int val, Node next) {
            this.val = val;
            this.next = next;
        }
    }


    /* **********************************************************************
     * Result
     * **********************************************************************/

    static record Result(
        long value,
        boolean valid
    ) {}


    /* **********************************************************************
     * Helpers
     * **********************************************************************/

    static boolean validNode(Node node) {
        return node != null;
    }

    static boolean isValidDigit(Node head) {
        Node current = head;

        while (current != null) {
            if (current.val < 0 || current.val > 9) {
                return false;
            }

            current = current.next;
        }

        return true;
    }

    static Node node(int val) {
        return new Node(val);
    }

    static Node node(int val, Node next) {
        return new Node(val, next);
    }


    /* **********************************************************************
     * Algorithms
     * **********************************************************************/

    /**
     * Multiplies two numbers represented by linked lists.
     *
     * Each node represents one decimal digit, stored from the most
     * significant digit to the least significant digit.
     *
     * This method builds the exact numbers using BigInteger before
     * calculating the result modulo 1,000,000,007.
     *
     * Time: O(n + m)
     * Extra space: O(n + m)
     */
    static Result multiplyNumbersBruteForce(
        Node head1,
        Node head2
    ) {
        if (!validNode(head1) || !validNode(head2)) {
            return new Result(0L, false);
        }

        if (!isValidDigit(head1) || !isValidDigit(head2)) {
            return new Result(0L, false);
        }

        BigInteger number1 = toBigInteger(head1);
        BigInteger number2 = toBigInteger(head2);

        BigInteger product =
            number1.multiply(number2);

        long remainder =
            product.mod(BIG_MOD).longValueExact();

        return new Result(remainder, true);
    }

    /**
     * Multiplies two numbers represented by linked lists without
     * constructing their full decimal values.
     *
     * Each intermediate value is reduced modulo 1,000,000,007.
     *
     * Time: O(n + m)
     * Extra space: O(1)
     */
    static Result multiplyNumbersModularArithmetic(
        Node head1,
        Node head2
    ) {
        if (!validNode(head1) || !validNode(head2)) {
            return new Result(0L, false);
        }

        if (!isValidDigit(head1) || !isValidDigit(head2)) {
            return new Result(0L, false);
        }

        long number1 = getNumberModulo(head1);
        long number2 = getNumberModulo(head2);

        long product =
            (number1 * number2) % MOD;

        return new Result(product, true);
    }

    static BigInteger toBigInteger(Node head) {
        BigInteger number = BigInteger.ZERO;

        Node current = head;

        while (current != null) {
            number = number
                .multiply(BigInteger.TEN)
                .add(BigInteger.valueOf(current.val));

            current = current.next;
        }

        return number;
    }

    static long getNumberModulo(Node head) {
        long number = 0L;

        Node current = head;

        while (current != null) {
            number =
                (number * 10L + current.val) % MOD;

            current = current.next;
        }

        return number;
    }


    /* **********************************************************************
     * List Helpers
     * **********************************************************************/

    static Node buildList(int... values) {
        Node head = null;
        Node tail = null;

        for (int value : values) {
            Node current = node(value);

            if (head == null) {
                head = current;
                tail = current;
            } else {
                tail.next = current;
                tail = current;
            }
        }

        return head;
    }

    static ArrayList<Integer> toValues(Node head) {
        ArrayList<Integer> values =
            new ArrayList<>();

        Node current = head;

        while (current != null) {
            values.add(current.val);
            current = current.next;
        }

        return values;
    }

    static ArrayList<Integer> toIntegerList(int[] values) {
        ArrayList<Integer> result =
            new ArrayList<>();

        for (int value : values) {
            result.add(value);
        }

        return result;
    }


    /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    static class TestCase {
        final String id;
        final int[] firstNumber;
        final int[] secondNumber;
        final long expected;
        final boolean expectedValid;
        final String description;

        TestCase(
            String id,
            int[] firstNumber,
            int[] secondNumber,
            long expected,
            boolean expectedValid,
            String description
        ) {
            this.id = id;
            this.firstNumber = firstNumber;
            this.secondNumber = secondNumber;
            this.expected = expected;
            this.expectedValid = expectedValid;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {
        Result solve(Node first, Node second);
    }

    static class MethodCase {
        final String name;
        final Algorithm algorithm;

        MethodCase(
            String name,
            Algorithm algorithm
        ) {
            this.name = name;
            this.algorithm = algorithm;
        }
    }

    static void runTests(
        MethodCase method,
        List<TestCase> tests
    ) {
        printSeparator();
        System.out.println(method.name);
        printSeparator();

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {
            Node first =
                buildList(test.firstNumber);

            Node second =
                buildList(test.secondNumber);

            try {
                Result actual =
                    method.algorithm.solve(
                        first,
                        second
                    );

                boolean success =
                    actual.valid() == test.expectedValid
                    && actual.value() == test.expected;

                if (success) {
                    passed++;

                    System.out.printf(
                        "PASS %s (%s)%n",
                        test.id,
                        test.description
                    );

                    System.out.println(
                        "  result = "
                            + actual.value()
                    );
                } else {
                    failed++;

                    System.out.printf(
                        "FAIL %s (%s)%n",
                        test.id,
                        test.description
                    );

                    System.out.println(
                        "  first number  = "
                            + Arrays.toString(
                                test.firstNumber
                            )
                    );

                    System.out.println(
                        "  second number = "
                            + Arrays.toString(
                                test.secondNumber
                            )
                    );

                    System.out.println(
                        "  expected = "
                            + test.expected
                            + ", valid = "
                            + test.expectedValid
                    );

                    System.out.println(
                        "  actual   = "
                            + actual.value()
                            + ", valid = "
                            + actual.valid()
                    );
                }
            } catch (Exception exception) {
                failed++;

                System.out.printf(
                    "FAIL %s (%s)%n",
                    test.id,
                    test.description
                );

                System.out.println(
                    "  exception = "
                        + exception
                );
            }
        }

        printResults(
            passed,
            failed,
            tests.size()
        );
    }


    /* **********************************************************************
     * Test Data
     * **********************************************************************/

    static List<TestCase> buildTests() {
        List<TestCase> tests =
            new ArrayList<>();

        tests.add(
            new TestCase(
                "B1",
                new int[]{1, 2, 3},
                new int[]{4, 5},
                5_535L,
                true,
                "123 multiplied by 45"
            )
        );

        tests.add(
            new TestCase(
                "B2",
                new int[]{9, 9, 9},
                new int[]{9, 9, 9},
                998_001L,
                true,
                "999 multiplied by 999"
            )
        );

        tests.add(
            new TestCase(
                "B3",
                new int[]{1, 0, 0, 0},
                new int[]{1, 0, 0, 0},
                0L,
                true,
                "product exceeds the modulus"
            )
        );

        tests.add(
            new TestCase(
                "B4",
                new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9},
                new int[]{9, 8, 7, 6, 5, 4, 3, 2, 1},
                199_929_981L,
                true,
                "large product requiring modular reduction"
            )
        );

        tests.add(
            new TestCase(
                "S1",
                new int[]{0},
                new int[]{0},
                0L,
                true,
                "zero multiplied by zero"
            )
        );

        tests.add(
            new TestCase(
                "S2",
                new int[]{0},
                new int[]{7},
                0L,
                true,
                "zero multiplied by seven"
            )
        );

        tests.add(
            new TestCase(
                "S3",
                new int[]{1},
                new int[]{1},
                1L,
                true,
                "one multiplied by one"
            )
        );

        tests.add(
            new TestCase(
                "S4",
                new int[]{2},
                new int[]{3},
                6L,
                true,
                "single-digit multiplication"
            )
        );

        tests.add(
            new TestCase(
                "S5",
                new int[]{0, 0, 1, 2},
                new int[]{3},
                36L,
                true,
                "leading zeroes are ignored numerically"
            )
        );

        tests.add(
            new TestCase(
                "E1",
                new int[]{},
                new int[]{1},
                0L,
                false,
                "empty first list"
            )
        );

        tests.add(
            new TestCase(
                "E2",
                new int[]{1},
                new int[]{},
                0L,
                false,
                "empty second list"
            )
        );

        tests.add(
            new TestCase(
                "E3",
                new int[]{1, 2},
                new int[]{},
                0L,
                false,
                "both lists empty"
            )
        );

        tests.add(
            new TestCase(
                "E4",
                new int[]{1, 10},
                new int[]{2},
                0L,
                false,
                "invalid digit in first list"
            )
        );

        tests.add(
            new TestCase(
                "E5",
                new int[]{1},
                new int[]{-1},
                0L,
                false,
                "invalid digit in second list"
            )
        );

        return tests;
    }


    /* **********************************************************************
     * Randomised Tests
     * **********************************************************************/

    static void runRandomisedTests(
        MethodCase method,
        int iterations
    ) {
        printSeparator();
        System.out.println("Randomised Tests");
        printSeparator();

        Random random =
            new Random(20261121L);

        for (
            int iteration = 1;
            iteration <= iterations;
            iteration++
        ) {
            int firstLength =
                random.nextInt(20) + 1;

            int secondLength =
                random.nextInt(20) + 1;

            int[] firstValues =
                buildRandomDigits(
                    random,
                    firstLength
                );

            int[] secondValues =
                buildRandomDigits(
                    random,
                    secondLength
                );

            long expected =
                calculateExpectedRemainder(
                    firstValues,
                    secondValues
                );

            Node first =
                buildList(firstValues);

            Node second =
                buildList(secondValues);

            Result actual =
                method.algorithm.solve(
                    first,
                    second
                );

            boolean success =
                actual.valid()
                && actual.value() == expected;

            if (!success) {
                System.out.println(
                    "Randomised test FAILED"
                );

                System.out.println(
                    "iteration = "
                        + iteration
                );

                System.out.println(
                    "first number  = "
                        + Arrays.toString(firstValues)
                );

                System.out.println(
                    "second number = "
                        + Arrays.toString(secondValues)
                );

                System.out.println(
                    "expected = "
                        + expected
                );

                System.out.println(
                    "actual = "
                        + actual.value()
                );

                System.out.println(
                    "valid = "
                        + actual.valid()
                );

                return;
            }
        }

        System.out.printf(
            "All %d randomised tests passed.%n%n",
            iterations
        );
    }

    static int[] buildRandomDigits(
        Random random,
        int length
    ) {
        int[] values =
            new int[length];

        values[0] =
            random.nextInt(9) + 1;

        for (int index = 1; index < length; index++) {
            values[index] =
                random.nextInt(10);
        }

        return values;
    }

    static long calculateExpectedRemainder(
        int[] firstValues,
        int[] secondValues
    ) {
        BigInteger first =
            toBigInteger(
                buildList(firstValues)
            );

        BigInteger second =
            toBigInteger(
                buildList(secondValues)
            );

        return first
            .multiply(second)
            .mod(BIG_MOD)
            .longValueExact();
    }


    /* **********************************************************************
     * Output Helpers
     * **********************************************************************/

    static void printSeparator() {
        System.out.println(
            "============================================================"
        );
    }

    static void printResults(
        int passed,
        int failed,
        int total
    ) {
        System.out.println();

        System.out.printf(
            "Results: %d passed, %d failed, %d total%n",
            passed,
            failed,
            total
        );

        System.out.println();
    }


    /* **********************************************************************
     * Main Test Suite
     * **********************************************************************/

    public static void main(String[] args) {
        System.out.println(
            "############################################################"
        );

        System.out.println(
            "############ MULTIPLY NUMBERS LINKED LIST ##################"
        );

        System.out.println(
            "############################################################"
        );

        System.out.println();

        List<TestCase> tests =
            buildTests();

        MethodCase bruteForce =
            new MethodCase(
                "Brute Force Using BigInteger",
                MultiplyNumbersLinkedList
                    ::multiplyNumbersBruteForce
            );

        MethodCase modularArithmetic =
            new MethodCase(
                "Modular Arithmetic",
                MultiplyNumbersLinkedList
                    ::multiplyNumbersModularArithmetic
            );

        runTests(bruteForce, tests);
        runRandomisedTests(bruteForce, 5_000);

        runTests(modularArithmetic, tests);
        runRandomisedTests(modularArithmetic, 5_000);
    }
}
