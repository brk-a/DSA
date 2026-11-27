import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class ReverseSublistLinkedLIst {

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
     * Reverses the sublist from position m through n inclusive.
     *
     * Positions are 1-based.
     *
     * Time: O(n)
     * Space: O(1)
     */
    static Result reverseSublistLinkedList(
        Node head,
        int m,
        int n
    ) {
        if (!validNode(head)) {
            return new Result(null, false);
        }

        if (m < 1 || n < m) {
            return new Result(null, false);
        }

        int length = listLength(head);

        if (n > length) {
            return new Result(null, false);
        }

        if (m == n) {
            return new Result(head, true);
        }

        Node dummy = node(0, head);

        /*
         * beforeSublist points to the node immediately
         * before position m.
         */
        Node beforeSublist = dummy;

        for (int i = 1; i < m; i++) {
            beforeSublist = beforeSublist.next;
        }

        Node sublistHead =
            beforeSublist.next;

        Node sublistTail = sublistHead;

        for (int i = m; i < n; i++) {
            sublistTail = sublistTail.next;
        }

        Node afterSublist =
            sublistTail.next;

        /*
         * Detach the sublist before reversing it.
         */
        sublistTail.next = null;

        Node reversedHead =
            reverse(sublistHead);

        beforeSublist.next = reversedHead;

        sublistHead.next = afterSublist;

        return new Result(dummy.next, true);
    }

    /**
     * Reverses a complete singly linked list in place.
     *
     * Time: O(n)
     * Space: O(1)
     */
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

    /* **********************************************************************
     * List Helpers
     * **********************************************************************/

    static int listLength(Node head) {
        int length = 0;
        Node current = head;

        while (current != null) {
            length++;
            current = current.next;
        }

        return length;
    }

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

    static int[] reverseSubarray(
        int[] values,
        int m,
        int n
    ) {
        int[] result =
            values.clone();

        int left = m - 1;
        int right = n - 1;

        while (left < right) {
            int temporary =
                result[left];

            result[left] =
                result[right];

            result[right] =
                temporary;

            left++;
            right--;
        }

        return result;
    }

    /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    static class TestCase {
        final String id;
        final int[] values;
        final int m;
        final int n;
        final int[] expected;
        final boolean expectedValid;
        final String description;

        TestCase(
            String id,
            int[] values,
            int m,
            int n,
            int[] expected,
            boolean expectedValid,
            String description
        ) {
            this.id = id;
            this.values = values;
            this.m = m;
            this.n = n;
            this.expected = expected;
            this.expectedValid = expectedValid;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {
        Result solve(
            Node head,
            int m,
            int n
        );
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
                    method.algorithm.solve(
                        input,
                        test.m,
                        test.n
                    );

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
                            + ", m = "
                            + test.m
                            + ", n = "
                            + test.n
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

    static void runInvalidInputTests(
        MethodCase method
    ) {
        printSeparator();
        System.out.println("Invalid Input Tests");
        printSeparator();

        int passed = 0;
        int failed = 0;

        Result result =
            method.solve(null, 1, 2);

        if (
            !result.valid()
            && result.head() == null
        ) {
            passed++;

            System.out.println(
                "PASS I1 - rejects null head"
            );
        } else {
            failed++;

            System.out.println(
                "FAIL I1 - accepts null head"
            );
        }

        result =
            method.solve(
                buildList(1, 2, 3),
                0,
                2
            );

        if (!result.valid()) {
            passed++;

            System.out.println(
                "PASS I2 - rejects m below one"
            );
        } else {
            failed++;

            System.out.println(
                "FAIL I2 - accepts m below one"
            );
        }

        result =
            method.solve(
                buildList(1, 2, 3),
                3,
                2
            );

        if (!result.valid()) {
            passed++;

            System.out.println(
                "PASS I3 - rejects m greater than n"
            );
        } else {
            failed++;

            System.out.println(
                "FAIL I3 - accepts m greater than n"
            );
        }

        result =
            method.solve(
                buildList(1, 2, 3),
                1,
                4
            );

        if (!result.valid()) {
            passed++;

            System.out.println(
                "PASS I4 - rejects n beyond list length"
            );
        } else {
            failed++;

            System.out.println(
                "FAIL I4 - accepts n beyond list length"
            );
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
        MethodCase method,
        int iterations
    ) {
        printSeparator();
        System.out.println("Randomised Tests");
        printSeparator();

        Random random =
            new Random(20261126L);

        for (
            int iteration = 1;
            iteration <= iterations;
            iteration++
        ) {
            int length =
                random.nextInt(100) + 1;

            int[] values =
                new int[length];

            for (int i = 0; i < length; i++) {
                values[i] =
                    random.nextInt(2001) - 1000;
            }

            int m =
                random.nextInt(length) + 1;

            int n =
                m + random.nextInt(length - m + 1) - 1;

            int[] expected =
                reverseSubarray(values, m, n);

            Node input =
                buildList(values);

            Result result =
                method.solve(input, m, n);

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
                    "input = "
                        + Arrays.toString(values)
                );

                System.out.println(
                    "m = "
                        + m
                );

                System.out.println(
                    "n = "
                        + n
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

        System.out.printf(
            "All %d randomised tests passed.%n%n",
            iterations
        );
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
                new int[]{1, 2, 3, 4, 5},
                2,
                4,
                new int[]{1, 4, 3, 2, 5},
                true,
                "reverse middle sublist"
            )
        );

        tests.add(
            new TestCase(
                "B2",
                new int[]{1, 2, 3, 4, 5},
                1,
                5,
                new int[]{5, 4, 3, 2, 1},
                true,
                "reverse entire list"
            )
        );

        tests.add(
            new TestCase(
                "B3",
                new int[]{1, 2, 3, 4, 5},
                1,
                3,
                new int[]{3, 2, 1, 4, 5},
                true,
                "reverse sublist beginning at head"
            )
        );

        tests.add(
            new TestCase(
                "B4",
                new int[]{1, 2, 3, 4, 5},
                3,
                5,
                new int[]{1, 2, 5, 4, 3},
                true,
                "reverse sublist ending at tail"
            )
        );

        tests.add(
            new TestCase(
                "S1",
                new int[]{1, 2},
                1,
                2,
                new int[]{2, 1},
                true,
                "two-node list"
            )
        );

        tests.add(
            new TestCase(
                "S2",
                new int[]{1, 2, 3},
                2,
                2,
                new int[]{1, 2, 3},
                true,
                "single-node sublist"
            )
        );

        tests.add(
            new TestCase(
                "S3",
                new int[]{7, 7, 7, 7},
                2,
                3,
                new int[]{7, 7, 7, 7},
                true,
                "duplicate values"
            )
        );

        tests.add(
            new TestCase(
                "S4",
                new int[]{-5, 0, 5, 10},
                1,
                4,
                new int[]{10, 5, 0, -5},
                true,
                "negative and positive values"
            )
        );

        tests.add(
            new TestCase(
                "E1",
                new int[]{},
                1,
                1,
                new int[]{},
                false,
                "empty list"
            )
        );

        tests.add(
            new TestCase(
                "E2",
                null,
                1,
                1,
                new int[]{},
                false,
                "null list"
            )
        );

        tests.add(
            new TestCase(
                "E3",
                new int[]{1, 2, 3},
                0,
                2,
                new int[]{},
                false,
                "m below one"
            )
        );

        tests.add(
            new TestCase(
                "E4",
                new int[]{1, 2, 3},
                3,
                2,
                new int[]{},
                false,
                "m greater than n"
            )
        );

        tests.add(
            new TestCase(
                "E5",
                new int[]{1, 2, 3},
                1,
                4,
                new int[]{},
                false,
                "n beyond list length"
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
            "############# REVERSE SUBLIST LINKED LIST ##################"
        );

        System.out.println(
            "############################################################"
        );

        System.out.println();

        MethodCase method =
            new MethodCase(
                "Dummy Head Reversal",
                ReverseSublistLinkedLIst
                    ::reverseSublistLinkedList
            );

        List<TestCase> tests =
            buildTests();

        runTests(method, tests);
        runInvalidInputTests(method);
        runRandomisedTests(method, 5000);
    }
}
