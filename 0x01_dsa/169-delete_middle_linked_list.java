import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Delete the middle node of a singly linked list.
 *
 * For a list containing n nodes, the middle node is the node at
 * zero-based index n / 2.
 *
 * Examples:
 *
 *      1 -> 2 -> 3 -> 4
 *
 * deletes 2, producing:
 *
 *      1 -> 3 -> 4
 *
 *      1 -> 2 -> 3 -> 4 -> 5
 *
 * deletes 3, producing:
 *
 *      1 -> 2 -> 4 -> 5
 *
 * Two implementations are provided:
 *
 * 1. Counting
 *
 *      Counts the nodes, then removes the node at index n / 2.
 *
 *      Time:  O(n)
 *      Space: O(1)
 *
 * 2. Two Pointers
 *
 *      Uses a fast pointer and a slow pointer to locate the node
 *      before the middle in one pass.
 *
 *      Time:  O(n)
 *      Space: O(1)
 *
 * Both methods return the head of the modified list. A separate field in
 * the result records whether the operation was valid.
 */
public class DeleteMiddleLinkedList {

    /* **********************************************************************
     * Node
     * **********************************************************************/

    static class Node {

        int val;
        Node next;

        Node(int val) {
            this.val = val;
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
            boolean valid) {
    }

    /* **********************************************************************
     * Helpers
     * **********************************************************************/

    static boolean validNode(
            Node node) {

        return node != null;
    }

    static Node node(
            int value) {

        return new Node(value);
    }

    static Node node(
            int value,
            Node next) {

        return new Node(
                value,
                next);
    }

    static Node buildList(
            int... values) {

        Node head = null;
        Node tail = null;

        for (int value : values) {

            Node current = node(value);

            if (head == null) {
                head = current;
            } else {
                tail.next = current;
            }

            tail = current;
        }

        return head;
    }

    static List<Integer> toValues(
            Node head) {

        ArrayList<Integer> values =
                new ArrayList<>();

        Node current = head;

        while (current != null) {

            values.add(current.val);
            current = current.next;
        }

        return values;
    }

    static int size(
            Node head) {

        int count = 0;
        Node current = head;

        while (current != null) {

            count++;
            current = current.next;
        }

        return count;
    }

    /* **********************************************************************
     * 1. Counting
     * **********************************************************************/

    /**
     * Delete the middle node by first counting the list length.
     *
     * The middle index is n / 2. To remove it safely, traversal stops at
     * the node immediately before that index.
     */
    static Result deleteMiddleCounting(
            Node head) {

        if (!validNode(head)) {
            return new Result(
                    null,
                    false);
        }

        /*
         * A one-node list becomes empty.
         */
        if (head.next == null) {
            return new Result(
                    null,
                    true);
        }

        int count = size(head);
        int middleIndex = count / 2;

        Node previous = head;

        for (int i = 0; i < middleIndex - 1; i++) {
            previous = previous.next;
        }

        Node middle = previous.next;

        previous.next = middle.next;

        return new Result(
                head,
                true);
    }

    /* **********************************************************************
     * 2. Two Pointers
     * **********************************************************************/

    /**
     * Delete the middle node using slow and fast pointers.
     *
     * The fast pointer advances two nodes for every one node advanced by
     * the slow pointer. By stopping before the usual midpoint, the slow
     * pointer finishes at the node immediately before the middle.
     */
    static Result deleteMiddleTwoPointers(
            Node head) {

        if (!validNode(head)) {
            return new Result(
                    null,
                    false);
        }

        /*
         * A one-node list becomes empty.
         */
        if (head.next == null) {
            return new Result(
                    null,
                    true);
        }

        Node previous = null;
        Node slow = head;
        Node fast = head;

        while (fast.next != null
                && fast.next.next != null) {

            fast = fast.next.next;
            previous = slow;
            slow = slow.next;
        }

        /*
         * For an even-length list, slow is initially the middle node.
         * For an odd-length list, it is the node before the middle.
         *
         * Moving previous forward when necessary gives the node before
         * the middle in both cases.
         */
        if (previous == null) {

            /*
             * Two-node list: delete the first node.
             */
            return new Result(
                    head.next,
                    true);
        }

        if (fast.next != null) {
            previous = slow;
            slow = slow.next;
        }

        previous.next = slow.next;

        return new Result(
                head,
                true);
    }

    /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    static class TestCase {

        final String id;
        final Node head;
        final List<Integer> expected;
        final boolean expectedValid;
        final String description;

        TestCase(
                String id,
                Node head,
                List<Integer> expected,
                boolean expectedValid,
                String description) {

            this.id = id;
            this.head = head;
            this.expected = expected;
            this.expectedValid = expectedValid;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {

        Result solve(
                Node head);
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
     * Test Runner
     * **********************************************************************/

    static void runTests(
            MethodCase method,
            List<TestCase> tests) {

        printSeparator();

        System.out.println(
                method.name);

        printSeparator();

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            try {

                Result actual =
                        method.algorithm.solve(
                                test.head);

                boolean success =
                        actual.valid()
                                == test.expectedValid
                                && toValues(actual.head())
                                .equals(test.expected);

                if (success) {

                    passed++;

                    System.out.printf(
                            "PASS %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  result = "
                                    + toValues(actual.head()));

                } else {

                    failed++;

                    System.out.printf(
                            "FAIL %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  expected = "
                                    + test.expected
                                    + ", valid = "
                                    + test.expectedValid);

                    System.out.println(
                            "  actual   = "
                                    + toValues(actual.head())
                                    + ", valid = "
                                    + actual.valid());
                }

            } catch (Exception ex) {

                failed++;

                System.out.printf(
                        "FAIL %s (%s)%n",
                        test.id,
                        test.description);

                System.out.println(
                        "  exception = "
                                + ex);
            }
        }

        printResults(
                passed,
                failed,
                tests.size());
    }

    /* **********************************************************************
     * Cross-Algorithm Tests
     * **********************************************************************/

    static void runCrossCheckTests(
            List<MethodCase> methods,
            List<TestCase> tests) {

        printSeparator();

        System.out.println(
                "Cross-Algorithm Tests");

        printSeparator();

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            try {

                Result first =
                        methods.get(0)
                                .algorithm
                                .solve(
                                        test.head);

                boolean success =
                        first.valid()
                                == test.expectedValid
                                && toValues(first.head())
                                .equals(test.expected);

                for (int i = 1;
                        i < methods.size();
                        i++) {

                    Result actual =
                            methods.get(i)
                                    .algorithm
                                    .solve(
                                            test.head);

                    if (actual.valid()
                            != first.valid()
                            || !toValues(actual.head())
                            .equals(toValues(first.head()))) {

                        success = false;

                        System.out.printf(
                                "  %s = %s%n",
                                methods.get(i).name,
                                toValues(actual.head()));
                    }
                }

                if (success) {

                    passed++;

                    System.out.printf(
                            "PASS %s (%s)%n",
                            test.id,
                            test.description);

                } else {

                    failed++;

                    System.out.printf(
                            "FAIL %s (%s)%n",
                            test.id,
                            test.description);
                }

            } catch (Exception ex) {

                failed++;

                System.out.printf(
                        "FAIL %s (%s)%n",
                        test.id,
                        test.description);

                System.out.println(
                        "  exception = "
                                + ex);
            }
        }

        printResults(
                passed,
                failed,
                tests.size());
    }

    /* **********************************************************************
     * Invalid Input Tests
     * **********************************************************************/

    static void runInvalidInputTests(
            List<MethodCase> methods) {

        printSeparator();

        System.out.println(
                "Invalid Input Tests");

        printSeparator();

        int passed = 0;
        int failed = 0;

        for (MethodCase method : methods) {

            Result result =
                    method.algorithm.solve(
                            null);

            if (!result.valid()) {

                passed++;

                System.out.printf(
                        "PASS I1 - %s rejects null head%n",
                        method.name);

            } else {

                failed++;

                System.out.printf(
                        "FAIL I1 - %s accepts null head%n",
                        method.name);
            }
        }

        printResults(
                passed,
                failed,
                passed + failed);
    }

    /* **********************************************************************
     * Test Data
     * **********************************************************************/

    static List<TestCase> buildTests() {

        List<TestCase> tests =
                new ArrayList<>();

        /*
         * Empty list.
         */
        tests.add(
                new TestCase(
                        "B1",
                        null,
                        List.of(),
                        false,
                        "empty list"));

        /*
         * One node.
         */
        tests.add(
                new TestCase(
                        "B2",
                        buildList(10),
                        List.of(),
                        true,
                        "single-node list"));

        /*
         * Two nodes: delete the first node.
         */
        tests.add(
                new TestCase(
                        "B3",
                        buildList(10, 20),
                        List.of(20),
                        true,
                        "two-node list"));

        /*
         * Three nodes: delete the second node.
         */
        tests.add(
                new TestCase(
                        "B4",
                        buildList(10, 20, 30),
                        List.of(10, 30),
                        true,
                        "three-node list"));

        /*
         * Four nodes: delete the second node.
         */
        tests.add(
                new TestCase(
                        "B5",
                        buildList(1, 2, 3, 4),
                        List.of(1, 3, 4),
                        true,
                        "even-length list"));

        /*
         * Five nodes: delete the third node.
         */
        tests.add(
                new TestCase(
                        "B6",
                        buildList(1, 2, 3, 4, 5),
                        List.of(1, 2, 4, 5),
                        true,
                        "odd-length list"));

        /*
         * Six nodes: delete the third node.
         */
        tests.add(
                new TestCase(
                        "B7",
                        buildList(1, 2, 3, 4, 5, 6),
                        List.of(1, 2, 4, 5, 6),
                        true,
                        "even-length list with six nodes"));

        /*
         * Negative values.
         */
        tests.add(
                new TestCase(
                        "N1",
                        buildList(-5, -4, -3, -2, -1),
                        List.of(-5, -4, -2, -1),
                        true,
                        "negative values"));

        /*
         * Duplicate values.
         */
        tests.add(
                new TestCase(
                        "D1",
                        buildList(7, 7, 7, 7),
                        List.of(7, 7, 7),
                        true,
                        "duplicate values"));

        return tests;
    }

    /* **********************************************************************
     * Randomised Tests
     * **********************************************************************/

    static void runRandomisedTests(
            List<MethodCase> methods,
            int iterations) {

        printSeparator();

        System.out.println(
                "Randomised Cross Checks");

        printSeparator();

        Random random =
                new Random(20261102L);

        for (int iteration = 1;
                iteration <= iterations;
                iteration++) {

            int size =
                    random.nextInt(20);

            int[] values =
                    new int[size];

            for (int i = 0; i < size; i++) {

                values[i] =
                        random.nextInt(41)
                                - 20;
            }

            List<Integer> expected =
                    expectedAfterDeletingMiddle(
                            values);

            for (MethodCase method : methods) {

                Result result =
                        method.algorithm.solve(
                                buildList(values));

                List<Integer> actual =
                        result.valid()
                                ? toValues(result.head())
                                : List.of();

                boolean success =
                        result.valid()
                                == !values.isEmpty()
                                && actual.equals(expected);

                if (!success) {

                    System.out.println(
                            "Randomised test FAILED");

                    System.out.println(
                            "iteration = "
                                    + iteration);

                    System.out.println(
                            "algorithm = "
                                    + method.name);

                    System.out.println(
                            "input = "
                                    + List.of(values));

                    System.out.println(
                            "expected = "
                                    + expected);

                    System.out.println(
                            "actual = "
                                    + actual);

                    System.out.println(
                            "valid = "
                                    + result.valid());

                    return;
                }
            }
        }

        System.out.printf(
                "All %d randomised tests passed.%n%n",
                iterations);
    }

    static List<Integer> expectedAfterDeletingMiddle(
            int[] values) {

        ArrayList<Integer> result =
                new ArrayList<>();

        if (values.length == 0) {
            return result;
        }

        int middleIndex =
                values.length / 2;

        for (int i = 0; i < values.length; i++) {

            if (i != middleIndex) {
                result.add(values[i]);
            }
        }

        return result;
    }

    /* **********************************************************************
     * Output Helpers
     * **********************************************************************/

    static void printSeparator() {

        System.out.println(
                "============================================================");
    }

    static void printResults(
            int passed,
            int failed,
            int total) {

        System.out.println();

        System.out.printf(
                "Results: %d passed, %d failed, %d total%n",
                passed,
                failed,
                total);

        System.out.println();
    }

    /* **********************************************************************
     * Main Test Suite
     * **********************************************************************/

    public static void main(
            String[] args) {

        System.out.println(
                "############################################################");

        System.out.println(
                "#############  DELETE MIDDLE LINKED LIST  ##################");

        System.out.println(
                "############################################################");

        System.out.println();

        List<MethodCase> methods =
                List.of(

                        new MethodCase(
                                "Counting",
                                DeleteMiddleLinkedList
                                        ::deleteMiddleCounting),

                        new MethodCase(
                                "Two Pointers",
                                DeleteMiddleLinkedList
                                        ::deleteMiddleTwoPointers)
                );

        List<TestCase> tests =
                buildTests();

        for (MethodCase method : methods) {

            runTests(
                    method,
                    tests);
        }

        runCrossCheckTests(
                methods,
                tests);

        runInvalidInputTests(
                methods);

        runRandomisedTests(
                methods,
                5000);
    }
}
