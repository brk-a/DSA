import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class ReverseDoublyLinkedList {

    /* **********************************************************************
     * Node
     * **********************************************************************/

    static class Node {
        int val;
        Node prev;
        Node next;

        Node(int val) {
            this.val = val;
            this.prev = null;
            this.next = null;
        }

        Node(
            int val,
            Node prev,
            Node next
        ) {
            this.val = val;
            this.prev = prev;
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

    static Node node(
        int val,
        Node prev,
        Node next
    ) {
        return new Node(val, prev, next);
    }

    /**
     * Recursive in-place reversal.
     *
     * At each node:
     * 1. Swap prev and next.
     * 2. Continue into the original next node, which is now head.prev.
     *
     * Time: O(n)
     * Space: O(n) recursion stack
     */
    static Result reverseDoublyLinkedListRecursion(
        Node head
    ) {
        if (!validNode(head)) {
            return new Result(null, false);
        }

        Node reversedHead =
            reverseRecursively(head);

        return new Result(reversedHead, true);
    }

    static Node reverseRecursively(Node head) {
        if (head == null) {
            return null;
        }

        Node originalNext = head.next;

        head.next = head.prev;
        head.prev = originalNext;

        if (head.prev == null) {
            return head;
        }

        return reverseRecursively(head.prev);
    }

    /**
     * Iterative two-pointer reversal.
     *
     * Swaps prev and next for every node, then returns the final node,
     * which becomes the new head.
     *
     * Time: O(n)
     * Space: O(1)
     */
    static Result reverseDoublyLinkedListTwoPointers(
        Node head
    ) {
        if (!validNode(head)) {
            return new Result(null, false);
        }

        Node current = head;
        Node previous = null;

        while (current != null) {
            previous = current.prev;

            current.prev = current.next;
            current.next = previous;

            current = current.prev;
        }

        return new Result(previous.prev, true);
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
                current.prev = tail;
                tail = current;
            }
        }

        return head;
    }

    static ArrayList<Integer> forwardValues(Node head) {
        ArrayList<Integer> values =
            new ArrayList<>();

        Node current = head;

        while (current != null) {
            values.add(current.val);
            current = current.next;
        }

        return values;
    }

    static ArrayList<Integer> backwardValues(Node head) {
        ArrayList<Integer> values =
            new ArrayList<>();

        Node current = head;

        while (current != null && current.next != null) {
            current = current.next;
        }

        while (current != null) {
            values.add(current.val);
            current = current.prev;
        }

        return values;
    }

    static boolean isConsistentDoublyLinkedList(Node head) {
        if (!validNode(head)) {
            return true;
        }

        if (head.prev != null) {
            return false;
        }

        Node current = head;

        while (current != null) {
            if (current.next != null) {
                if (current.next.prev != current) {
                    return false;
                }
            }

            if (
                current.prev != null
                && current.prev.next != current
            ) {
                return false;
            }

            current = current.next;
        }

        return true;
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
                    && forwardValues(actual.head()).equals(
                        toIntegerList(test.expected)
                    )
                    && backwardValues(actual.head()).equals(
                        toIntegerList(test.values)
                    )
                    && isConsistentDoublyLinkedList(
                        actual.head()
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
                            + forwardValues(actual.head())
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
                            + forwardValues(actual.head())
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
                    forwardValues(result.head());

                results.add(values);

                if (
                    result.valid()
                        != test.expectedValid
                    || !values.equals(
                        toIntegerList(test.expected)
                    )
                    || !isConsistentDoublyLinkedList(
                        result.head()
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
            new Random(20261029L);

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

            int[] expected =
                reverseArray(values);

            for (MethodCase method : methods) {
                Node input =
                    buildList(values);

                Result result =
                    method.algorithm.solve(input);

                boolean success =
                    result.valid()
                    && forwardValues(result.head()).equals(
                        toIntegerList(expected)
                    )
                    && backwardValues(result.head()).equals(
                        toIntegerList(values)
                    )
                    && isConsistentDoublyLinkedList(
                        result.head()
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
                            + forwardValues(result.head())
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

    static int[] reverseArray(int[] values) {
        int[] reversed =
            new int[values.length];

        for (int i = 0; i < values.length; i++) {
            reversed[i] =
                values[values.length - 1 - i];
        }

        return reversed;
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
                new int[]{5, 4, 3, 2, 1},
                true,
                "ordinary list"
            )
        );

        tests.add(
            new TestCase(
                "B2",
                new int[]{5, 4, 3, 2, 1},
                new int[]{1, 2, 3, 4, 5},
                true,
                "already descending list"
            )
        );

        tests.add(
            new TestCase(
                "S1",
                new int[]{42},
                new int[]{42},
                true,
                "single-node list"
            )
        );

        tests.add(
            new TestCase(
                "S2",
                new int[]{1, 2},
                new int[]{2, 1},
                true,
                "two-node list"
            )
        );

        tests.add(
            new TestCase(
                "S3",
                new int[]{7, 7, 7},
                new int[]{7, 7, 7},
                true,
                "duplicate values"
            )
        );

        tests.add(
            new TestCase(
                "S4",
                new int[]{-5, 0, 5},
                new int[]{5, 0, -5},
                true,
                "negative and positive values"
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
            "############ REVERSE DOUBLY LINKED LIST ####################"
        );

        System.out.println(
            "############################################################"
        );

        System.out.println();

        List<MethodCase> methods =
            List.of(
                new MethodCase(
                    "Recursion",
                    ReverseDoublyLinkedList
                        ::reverseDoublyLinkedListRecursion
                ),
                new MethodCase(
                    "Two Pointers",
                    ReverseDoublyLinkedList
                        ::reverseDoublyLinkedListTwoPointers
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
