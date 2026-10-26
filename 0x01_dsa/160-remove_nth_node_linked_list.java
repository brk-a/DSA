import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

public class RemoveNthNodeLinkedList {

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
     * Two-pass solution.
     *
     * 1. Find the list length.
     * 2. Convert the position from the end to a position from the start.
     * 3. Remove that node using a dummy head.
     *
     * Time: O(n)
     * Space: O(1)
     */
    static Result removeNthNodeEquivalentNode(
        Node head,
        int n
    ) {
        if (!validNode(head) || n <= 0) {
            return new Result(null, false);
        }

        int length = 0;
        Node current = head;

        while (current != null) {
            length++;
            current = current.next;
        }

        if (n > length) {
            return new Result(null, false);
        }

        /*
         * The nth node from the end is the
         * (length - n + 1)th node from the start.
         */
        int positionFromStart =
            length - n;

        Node dummy = node(0, head);
        Node previous = dummy;

        for (int i = 0; i < positionFromStart; i++) {
            previous = previous.next;
        }

        previous.next =
            previous.next.next;

        return new Result(dummy.next, true);
    }

    /**
     * Fast/slow pointer solution.
     *
     * Move fast n steps ahead, then move both pointers together.
     * When fast reaches the final node, slow points to the node
     * immediately before the node to remove.
     *
     * Time: O(n)
     * Space: O(1)
     */
    static Result removeNthNodeFastSlowPointer(
        Node head,
        int n
    ) {
        if (!validNode(head) || n <= 0) {
            return new Result(null, false);
        }

        Node dummy = node(0, head);
        Node fast = dummy;
        Node slow = dummy;

        for (int i = 0; i < n; i++) {
            fast = fast.next;

            if (fast == null) {
                return new Result(null, false);
            }
        }

        while (fast.next != null) {
            fast = fast.next;
            slow = slow.next;
        }

        slow.next = slow.next.next;

        return new Result(dummy.next, true);
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
        final int n;
        final int[] expected;
        final boolean expectedValid;
        final String description;

        TestCase(
            String id,
            int[] values,
            int n,
            int[] expected,
            boolean expectedValid,
            String description
        ) {
            this.id = id;
            this.values = values;
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
                    method.algorithm.solve(
                        input,
                        test.n
                    );

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
                method.algorithm.solve(
                    null,
                    1
                );

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
                    buildList(1, 2, 3),
                    0
                );

            if (
                !result.valid()
                && result.head() == null
            ) {
                passed++;

                System.out.printf(
                    "PASS I2 - %s rejects zero%n",
                    method.name
                );
            } else {
                failed++;

                System.out.printf(
                    "FAIL I2 - %s accepts zero%n",
                    method.name
                );
            }

            result =
                method.algorithm.solve(
                    buildList(1, 2, 3),
                    -1
                );

            if (
                !result.valid()
                && result.head() == null
            ) {
                passed++;

                System.out.printf(
                    "PASS I3 - %s rejects negative n%n",
                    method.name
                );
            } else {
                failed++;

                System.out.printf(
                    "FAIL I3 - %s accepts negative n%n",
                    method.name
                );
            }

            result =
                method.algorithm.solve(
                    buildList(1, 2, 3),
                    4
                );

            if (
                !result.valid()
                && result.head() == null
            ) {
                passed++;

                System.out.printf(
                    "PASS I4 - %s rejects n greater than length%n",
                    method.name
                );
            } else {
                failed++;

                System.out.printf(
                    "FAIL I4 - %s accepts n greater than length%n",
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
            new Random(20261025L);

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

            int n =
                random.nextInt(length) + 1;

            int[] expected =
                removeNthFromEnd(values, n);

            for (MethodCase method : methods) {
                Node input =
                    buildList(values);

                Result result =
                    method.algorithm.solve(
                        input,
                        n
                    );

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
        }

        System.out.printf(
            "All %d randomised tests passed.%n%n",
            iterations
        );
    }

    static int[] removeNthFromEnd(
        int[] values,
        int n
    ) {
        int[] result =
            new int[values.length - 1];

        int skipIndex =
            values.length - n;

        int resultIndex = 0;

        for (
            int i = 0;
            i < values.length;
            i++
        ) {
            if (i != skipIndex) {
                result[resultIndex++] =
                    values[i];
            }
        }

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
                new int[]{1, 2, 3, 4, 5},
                2,
                new int[]{1, 2, 3, 5},
                true,
                "remove second node from end"
            )
        );

        tests.add(
            new TestCase(
                "B2",
                new int[]{1, 2, 3, 4, 5},
                5,
                new int[]{2, 3, 4, 5},
                true,
                "remove head"
            )
        );

        tests.add(
            new TestCase(
                "B3",
                new int[]{1, 2, 3, 4, 5},
                1,
                new int[]{1, 2, 3, 4},
                true,
                "remove tail"
            )
        );

        tests.add(
            new TestCase(
                "S1",
                new int[]{10},
                1,
                new int[]{},
                true,
                "single-node list"
            )
        );

        tests.add(
            new TestCase(
                "S2",
                new int[]{1, 2},
                2,
                new int[]{2},
                true,
                "two-node list, remove head"
            )
        );

        tests.add(
            new TestCase(
                "S3",
                new int[]{7, 7, 7, 7},
                2,
                new int[]{7, 7, 7},
                true,
                "duplicate values"
            )
        );

        tests.add(
            new TestCase(
                "S4",
                new int[]{-5, 0, 5},
                2,
                new int[]{-5, 5},
                true,
                "negative and positive values"
            )
        );

        tests.add(
            new TestCase(
                "E1",
                new int[]{},
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
                new int[]{},
                false,
                "zero is invalid"
            )
        );

        tests.add(
            new TestCase(
                "E4",
                new int[]{1, 2, 3},
                -1,
                new int[]{},
                false,
                "negative n is invalid"
            )
        );

        tests.add(
            new TestCase(
                "E5",
                new int[]{1, 2, 3},
                4,
                new int[]{},
                false,
                "n is greater than list length"
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
            "############ REMOVE NTH NODE LINKED LIST ###################"
        );

        System.out.println(
            "############################################################"
        );

        System.out.println();

        List<MethodCase> methods =
            List.of(
                new MethodCase(
                    "Two-Pass Equivalent Node",
                    RemoveNthNodeLinkedList
                        ::removeNthNodeEquivalentNode
                ),
                new MethodCase(
                    "Fast/Slow Pointer",
                    RemoveNthNodeLinkedList
                        ::removeNthNodeFastSlowPointer
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