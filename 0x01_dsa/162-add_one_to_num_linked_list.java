import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class AddOneToNumLinkedList {

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
        Node head,
        boolean valid
    ) {}

    /* **********************************************************************
     * Helpers
     * **********************************************************************/

    static boolean validNode(Node node) {
        return node != null;
    }

    static Node node(int val) {
        return new Node(val);
    }

    static Node node(int val, Node next) {
        return new Node(val, next);
    }

    /**
     * Recursive solution.
     *
     * Recurses to the least significant digit, adds one, and returns
     * the carry while unwinding.
     *
     * Time: O(n)
     * Space: O(n) recursion stack
     */
    static Result addOneToNumRecursion(Node head) {
        if (!validNode(head) || !containsValidDigits(head)) {
            return new Result(null, false);
        }

        int carry = addCarryRecursion(head);

        if (carry > 0) {
            Node newHead = node(carry, head);
            return new Result(newHead, true);
        }

        return new Result(head, true);
    }

    static int addCarryRecursion(Node head) {
        if (head == null) {
            return 1;
        }

        int sum =
            head.val + addCarryRecursion(head.next);

        head.val = sum % 10;

        return sum / 10;
    }

    /**
     * Iterative solution.
     *
     * Reverses the list, adds one from the least significant digit,
     * propagates carry, then reverses the list back.
     *
     * Time: O(n)
     * Space: O(1)
     */
    static Result addOneToNumIteration(Node head) {
        if (!validNode(head) || !containsValidDigits(head)) {
            return new Result(null, false);
        }

        head = reverse(head);
        head = addOneIteration(head);

        return new Result(reverse(head), true);
    }

    static Node addOneIteration(Node head) {
        Node current = head;
        Node last = null;
        int carry = 1;

        while (current != null) {
            int sum = current.val + carry;

            current.val = sum % 10;
            carry = sum / 10;

            last = current;
            current = current.next;
        }

        if (carry > 0) {
            last.next = node(carry);
        }

        return head;
    }

    static Node reverse(Node head) {
        Node previous = null;
        Node current = head;

        while (current != null) {
            Node next = current.next;

            current.next = previous;
            previous = current;
            current = next;
        }

        return previous;
    }

    static boolean containsValidDigits(Node head) {
        Node current = head;

        while (current != null) {
            if (
                current.val < 0
                || current.val > 9
            ) {
                return false;
            }

            current = current.next;
        }

        return true;
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

    static boolean hasCycle(Node head) {
        Node slow = head;
        Node fast = head;

        while (
            fast != null
            && fast.next != null
        ) {
            slow = slow.next;
            fast = fast.next.next;

            if (slow == fast) {
                return true;
            }
        }

        return false;
    }

    /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    static class TestCase {
        final String id;
        final int[] values;
        final int[] expected;
        final boolean expectedValid;
        final String description;

        TestCase(
            String id,
            int[] values,
            int[] expected,
            boolean expectedValid,
            String description
        ) {
            this.id = id;
            this.values = values;
            this.expected = expected;
            this.expectedValid = expectedValid;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {
        Result solve(Node head);
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
            Node input =
                buildList(test.values);

            try {
                Result actual =
                    method.algorithm.solve(input);

                boolean success =
                    actual.valid()
                        == test.expectedValid
                    && toValues(actual.head()).equals(
                        toIntegerList(test.expected)
                    )
                    && !hasCycle(actual.head());

                if (success) {
                    passed++;

                    System.out.printf(
                        "PASS %s (%s)%n",
                        test.id,
                        test.description
                    );

                    System.out.println(
                        "  result = "
                            + toValues(actual.head())
                    );
                } else {
                    failed++;

                    System.out.printf(
                        "FAIL %s (%s)%n",
                        test.id,
                        test.description
                    );

                    System.out.println(
                        "  input    = "
                            + Arrays.toString(test.values)
                    );

                    System.out.println(
                        "  expected = "
                            + Arrays.toString(test.expected)
                            + ", valid = "
                            + test.expectedValid
                    );

                    System.out.println(
                        "  actual   = "
                            + toValues(actual.head())
                            + ", valid = "
                            + actual.valid()
                    );
                }
            } catch (Exception ex) {
                failed++;

                System.out.printf(
                    "FAIL %s (%s)%n",
                    test.id,
                    test.description
                );

                System.out.println(
                    "  exception = "
                        + ex
                );
            }
        }

        printResults(
            passed,
            failed,
            tests.size()
        );
    }

    static void runCrossCheckTests(
        List<MethodCase> methods,
        List<TestCase> tests
    ) {
        printSeparator();
        System.out.println("Cross-Algorithm Tests");
        printSeparator();

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {
            boolean success = true;
            ArrayList<ArrayList<Integer>> results =
                new ArrayList<>();

            for (MethodCase method : methods) {
                Node input =
                    buildList(test.values);

                Result result =
                    method.algorithm.solve(input);

                ArrayList<Integer> values =
                    toValues(result.head());

                results.add(values);

                if (
                    result.valid()
                        != test.expectedValid
                    || !values.equals(
                        toIntegerList(test.expected)
                    )
                    || hasCycle(result.head())
                ) {
                    success = false;
                }
            }

            if (success) {
                passed++;

                System.out.printf(
                    "PASS %s (%s)%n",
                    test.id,
                    test.description
                );
            } else {
                failed++;

                System.out.printf(
                    "FAIL %s (%s)%n",
                    test.id,
                    test.description
                );

                System.out.println(
                    "  results = "
                        + results
                );
            }
        }

        printResults(
            passed,
            failed,
            tests.size()
        );
    }

    static void runInvalidInputTests(
        List<MethodCase> methods
    ) {
        printSeparator();
        System.out.println("Invalid Input Tests");
        printSeparator();

        int passed = 0;
        int failed = 0;

        for (MethodCase method : methods) {
            Result result =
                method.algorithm.solve(null);

            if (
                !result.valid()
                && result.head() == null
            ) {
                passed++;

                System.out.printf(
                    "PASS I1 - %s rejects null head%n",
                    method.name
                );
            } else {
                failed++;

                System.out.printf(
                    "FAIL I1 - %s accepts null head%n",
                    method.name
                );
            }

            result =
                method.algorithm.solve(
                    buildList(1, 10, 2)
                );

            if (!result.valid()) {
                passed++;

                System.out.printf(
                    "PASS I2 - %s rejects invalid digit%n",
                    method.name
                );
            } else {
                failed++;

                System.out.printf(
                    "FAIL I2 - %s accepts invalid digit%n",
                    method.name
                );
            }

            result =
                method.algorithm.solve(
                    buildList(1, -2, 3)
                );

            if (!result.valid()) {
                passed++;

                System.out.printf(
                    "PASS I3 - %s rejects negative digit%n",
                    method.name
                );
            } else {
                failed++;

                System.out.printf(
                    "FAIL I3 - %s accepts negative digit%n",
                    method.name
                );
            }
        }

        printResults(
            passed,
            failed,
            passed + failed
        );
    }

    /* **********************************************************************
     * Randomised Tests
     * **********************************************************************/

    static void runRandomisedTests(
        List<MethodCase> methods,
        int iterations
    ) {
        printSeparator();
        System.out.println("Randomised Cross Checks");
        printSeparator();

        Random random =
            new Random(20261026L);

        for (
            int iteration = 1;
            iteration <= iterations;
            iteration++
        ) {
            int length =
                random.nextInt(50) + 1;

            int[] values =
                new int[length];

            for (int i = 0; i < length; i++) {
                values[i] =
                    random.nextInt(10);
            }

            int[] expected =
                addOneToArray(values);

            for (MethodCase method : methods) {
                Node input =
                    buildList(values);

                Result result =
                    method.algorithm.solve(input);

                boolean success =
                    result.valid()
                    && toValues(result.head()).equals(
                        toIntegerList(expected)
                    )
                    && !hasCycle(result.head());

                if (!success) {
                    System.out.println(
                        "Randomised test FAILED"
                    );

                    System.out.println(
                        "iteration = "
                            + iteration
                    );

                    System.out.println(
                        "algorithm = "
                            + method.name
                    );

                    System.out.println(
                        "input = "
                            + Arrays.toString(values)
                    );

                    System.out.println(
                        "expected = "
                            + Arrays.toString(expected)
                    );

                    System.out.println(
                        "actual = "
                            + toValues(result.head())
                    );

                    System.out.println(
                        "valid = "
                            + result.valid()
                    );

                    return;
                }
            }
        }

        System.out.printf(
            "All %d randomised tests passed.%n%n",
            iterations
        );
    }

    static int[] addOneToArray(int[] values) {
        int[] digits =
            values.clone();

        int carry = 1;

        for (
            int i = digits.length - 1;
            i >= 0 && carry > 0;
            i--
        ) {
            int sum = digits[i] + carry;

            digits[i] = sum % 10;
            carry = sum / 10;
        }

        if (carry == 0) {
            return digits;
        }

        int[] result =
            new int[digits.length + 1];

        result[0] = carry;
        System.arraycopy(
            digits,
            0,
            result,
            1,
            digits.length
        );

        return result;
    }

    static ArrayList<Integer> toIntegerList(
        int[] values
    ) {
        ArrayList<Integer> result =
            new ArrayList<>();

        for (int value : values) {
            result.add(value);
        }

        return result;
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
                new int[]{1, 2, 4},
                true,
                "simple increment"
            )
        );

        tests.add(
            new TestCase(
                "B2",
                new int[]{1, 2, 9},
                new int[]{1, 3, 0},
                true,
                "single trailing carry"
            )
        );

        tests.add(
            new TestCase(
                "B3",
                new int[]{9, 9, 9},
                new int[]{1, 0, 0, 0},
                true,
                "all nines"
            )
        );

        tests.add(
            new TestCase(
                "S1",
                new int[]{0},
                new int[]{1},
                true,
                "zero becomes one"
            )
        );

        tests.add(
            new TestCase(
                "S2",
                new int[]{9},
                new int[]{1, 0},
                true,
                "single nine"
            )
        );

        tests.add(
            new TestCase(
                "S3",
                new int[]{1, 9, 9, 9},
                new int[]{2, 0, 0, 0},
                true,
                "multiple trailing nines"
            )
        );

        tests.add(
            new TestCase(
                "S4",
                new int[]{1, 0, 0},
                new int[]{1, 0, 1},
                true,
                "zeros in the middle"
            )
        );

        tests.add(
            new TestCase(
                "E1",
                new int[]{},
                new int[]{},
                false,
                "empty list"
            )
        );

        tests.add(
            new TestCase(
                "E2",
                null,
                new int[]{},
                false,
                "null list"
            )
        );

        tests.add(
            new TestCase(
                "E3",
                new int[]{1, 10, 2},
                new int[]{},
                false,
                "invalid digit greater than nine"
            )
        );

        tests.add(
            new TestCase(
                "E4",
                new int[]{1, -2, 3},
                new int[]{},
                false,
                "negative digit"
            )
        );

        return tests;
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
            "############ ADD ONE TO NUM LINKED LIST ####################"
        );

        System.out.println(
            "############################################################"
        );

        System.out.println();

        List<MethodCase> methods =
            List.of(
                new MethodCase(
                    "Recursive Carry",
                    AddOneToNumLinkedList
                        ::addOneToNumRecursion
                ),
                new MethodCase(
                    "Reverse and Iterate",
                    AddOneToNumLinkedList
                        ::addOneToNumIteration
                )
            );

        List<TestCase> tests =
            buildTests();

        for (MethodCase method : methods) {
            runTests(method, tests);
        }

        runCrossCheckTests(methods, tests);
        runInvalidInputTests(methods);
        runRandomisedTests(methods, 5000);
    }
}