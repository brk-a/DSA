import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class ReverseLinkedList {

    /* **********************************************************************
     * Node
     * **********************************************************************/

    static class Node {
        int val;
        Node next;

        Node(int val) {
            this.val = val;
        }

        Node(Node next, int val) {
            this.next = next;
            this.val = val;
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

    static Node node(int value) {
        return new Node(value);
    }

    static Node node(int value, Node next) {
        return new Node(next, value);
    }

    /**
     * Reverse a linked list iteratively.
     *
     * Time: O(n)
     * Space: O(1)
     */
    static Result reverseLinkedListIteration(Node head) {
        if (!validNode(head)) {
            return new Result(null, false);
        }

        Node previous = null;
        Node current = head;

        while (current != null) {
            Node next = current.next;
            current.next = previous;
            previous = current;
            current = next;
        }

        return new Result(previous, true);
    }

    /**
     * Reverse a linked list recursively.
     *
     * Time: O(n)
     * Space: O(n) recursion stack
     */
    static Result reverseLinkedListRecursion(Node head) {
        if (!validNode(head)) {
            return new Result(null, false);
        }

        Node reversedHead =
            reverseRecursively(head);

        return new Result(reversedHead, true);
    }

    static Node reverseRecursively(Node head) {
        if (head == null || head.next == null) {
            return head;
        }

        Node reversedHead =
            reverseRecursively(head.next);

        head.next.next = head;
        head.next = null;

        return reversedHead;
    }

    /**
     * Reverse a linked list using an explicit array-backed stack.
     *
     * Time: O(n)
     * Space: O(n)
     */
    static Result reverseLinkedListStack(Node head) {
        if (!validNode(head)) {
            return new Result(null, false);
        }

        ArrayList<Node> stack = new ArrayList<>();
        Node current = head;

        while (current != null) {
            stack.add(current);
            current = current.next;
        }

        Node newHead =
            stack.get(stack.size() - 1);

        Node tail = newHead;

        for (int i = stack.size() - 2; i >= 0; i--) {
            tail.next = stack.get(i);
            tail = tail.next;
        }

        tail.next = null;

        return new Result(newHead, true);
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
        ArrayList<Integer> values = new ArrayList<>();
        Node current = head;

        while (current != null) {
            values.add(current.val);
            current = current.next;
        }

        return values;
    }

    static ArrayList<Node> toNodes(Node head) {
        ArrayList<Node> nodes = new ArrayList<>();
        Node current = head;

        while (current != null) {
            nodes.add(current);
            current = current.next;
        }

        return nodes;
    }

    static Node[] toNodeArray(Node head) {
        ArrayList<Node> nodes = toNodes(head);
        return nodes.toArray(new Node[0]);
    }

    static boolean hasCycle(Node head) {
        Node slow = head;
        Node fast = head;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;

            if (slow == fast) {
                return true;
            }
        }

        return false;
    }

    static boolean containsExactlyReversedNodes(
        Node original,
        Node reversed
    ) {
        Node[] originalNodes = toNodeArray(original);
        Node[] reversedNodes = toNodeArray(reversed);

        if (originalNodes.length != reversedNodes.length) {
            return false;
        }

        for (int i = 0; i < originalNodes.length; i++) {
            Node expected =
                originalNodes[
                    originalNodes.length - 1 - i
                ];

            if (reversedNodes[i] != expected) {
                return false;
            }
        }

        return true;
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
            Node input = buildList(test.values);

            try {
                Result actual =
                    method.algorithm.solve(input);

                boolean success =
                    actual.valid()
                        == test.expectedValid
                    && toValues(actual.head()).equals(
                        toIntegerList(test.expected)
                    )
                    && !hasCycle(actual.head())
                    && containsExactlyReversedNodes(
                        input,
                        actual.head()
                    );

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

        printResults(passed, failed, tests.size());
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
                Node input = buildList(test.values);

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
                    || !containsExactlyReversedNodes(
                        input,
                        result.head()
                    )
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

        printResults(passed, failed, tests.size());
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

            if (!result.valid() && result.head() == null) {
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
     * randomised Tests
     * **********************************************************************/

    static void runrandomisedTests(
        List<MethodCase> methods,
        int iterations
    ) {
        printSeparator();
        System.out.println("randomised Cross Checks");
        printSeparator();

        Random random =
            new Random(20261022L);

        for (int iteration = 1;
             iteration <= iterations;
             iteration++) {

            int size =
                random.nextInt(100);

            int[] values =
                new int[size];

            for (int i = 0; i < size; i++) {
                values[i] =
                    random.nextInt(2001) - 1000;
            }

            int[] expected =
                new int[size];

            for (int i = 0; i < size; i++) {
                expected[i] =
                    values[size - 1 - i];
            }

            for (MethodCase method : methods) {
                Node input =
                    buildList(values);

                Result result =
                    method.algorithm.solve(input);

                boolean success =
                    result.valid()
                        == (size > 0)
                    && toValues(result.head()).equals(
                        toIntegerList(expected)
                    )
                    && !hasCycle(result.head())
                    && containsExactlyReversedNodes(
                        input,
                        result.head()
                    );

                if (!success) {
                    System.out.println(
                        "randomised test FAILED"
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
                "B3",
                new int[]{1, 2},
                new int[]{2, 1},
                true,
                "two-node list"
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
                new int[]{7, 7, 7, 7},
                new int[]{7, 7, 7, 7},
                true,
                "duplicate values"
            )
        );

        tests.add(
            new TestCase(
                "S3",
                new int[]{-3, 0, 5, -10},
                new int[]{-10, 5, 0, -3},
                true,
                "negative and positive values"
            )
        );

        tests.add(
            new TestCase(
                "S4",
                new int[]{
                    Integer.MIN_VALUE,
                    0,
                    Integer.MAX_VALUE
                },
                new int[]{
                    Integer.MAX_VALUE,
                    0,
                    Integer.MIN_VALUE
                },
                true,
                "integer boundary values"
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

        return tests;
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
            "################ REVERSE LINKED LIST #######################"
        );

        System.out.println(
            "############################################################"
        );

        System.out.println();

        List<MethodCase> methods =
            List.of(
                new MethodCase(
                    "Iteration",
                    ReverseLinkedList
                        ::reverseLinkedListIteration
                ),
                new MethodCase(
                    "Recursion",
                    ReverseLinkedList
                        ::reverseLinkedListRecursion
                ),
                new MethodCase(
                    "Explicit Stack",
                    ReverseLinkedList
                        ::reverseLinkedListStack
                )
            );

        List<TestCase> tests =
            buildTests();

        /*
         * Individual algorithm tests.
         */
        for (MethodCase method : methods) {
            runTests(method, tests);
        }

        /*
         * Cross-algorithm tests.
         */
        runCrossCheckTests(methods, tests);

        /*
         * Invalid-input tests.
         */
        runInvalidInputTests(methods);

        /*
         * randomised tests.
         */
        runRandomisedTests(methods, 5000);
    }
}
