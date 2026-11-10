import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class ReorderLinkedList {

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
     * Reorders the list in place:
     *
     * L0 -> L1 -> ... -> Ln
     * becomes
     * L0 -> Ln -> L1 -> Ln-1 -> ...
     *
     * Time: O(n)
     * Extra space: O(1)
     */
    static Result reorderLinkedList(Node head) {
        if (!validNode(head)) {
            return new Result(null, false);
        }

        if (head.next == null) {
            return new Result(head, true);
        }

        Node middle = findMiddle(head);
        Node secondHalf = reverseList(middle.next);

        middle.next = null;

        Node first = head;
        Node second = secondHalf;

        while (second != null) {
            Node firstNext = first.next;
            Node secondNext = second.next;

            first.next = second;
            second.next = firstNext;

            first = firstNext;
            second = secondNext;
        }

        return new Result(head, true);
    }

    /**
     * Finds the last node of the first half.
     *
     * For an even-length list, this is the node before the midpoint.
     * For an odd-length list, this is the exact middle node.
     */
    static Node findMiddle(Node head) {
        Node slow = head;
        Node fast = head;

        while (
            fast.next != null
            && fast.next.next != null
        ) {
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow;
    }

    static Node reverseList(Node head) {
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

    static ArrayList<Node> toNodes(Node head) {
        ArrayList<Node> nodes =
            new ArrayList<>();

        Node current = head;

        while (current != null) {
            nodes.add(current);
            current = current.next;
        }

        return nodes;
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

    static boolean containsSameNodes(
        Node original,
        Node reordered
    ) {
        ArrayList<Node> originalNodes =
            toNodes(original);

        ArrayList<Node> reorderedNodes =
            toNodes(reordered);

        if (
            originalNodes.size()
            != reorderedNodes.size()
        ) {
            return false;
        }

        for (Node node : reorderedNodes) {
            if (!originalNodes.contains(node)) {
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
                    && !hasCycle(actual.head())
                    && containsSameNodes(
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

    /* **********************************************************************
     * Test Data
     * **********************************************************************/

    static List<TestCase> buildTests() {
        List<TestCase> tests =
            new ArrayList<>();

        tests.add(
            new TestCase(
                "B1",
                new int[]{1, 2, 3, 4},
                new int[]{1, 4, 2, 3},
                true,
                "even-length list"
            )
        );

        tests.add(
            new TestCase(
                "B2",
                new int[]{1, 2, 3, 4, 5},
                new int[]{1, 5, 2, 4, 3},
                true,
                "odd-length list"
            )
        );

        tests.add(
            new TestCase(
                "B3",
                new int[]{1, 2, 3, 4, 5, 6},
                new int[]{1, 6, 2, 5, 3, 4},
                true,
                "longer even-length list"
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
                new int[]{1, 2},
                true,
                "two-node list"
            )
        );

        tests.add(
            new TestCase(
                "S3",
                new int[]{1, 2, 3},
                new int[]{1, 3, 2},
                true,
                "three-node list"
            )
        );

        tests.add(
            new TestCase(
                "S4",
                new int[]{7, 7, 7, 7},
                new int[]{7, 7, 7, 7},
                true,
                "duplicate values"
            )
        );

        tests.add(
            new TestCase(
                "S5",
                new int[]{-5, 0, 5, 10},
                new int[]{-5, 10, 0, 5},
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
            new Random(20261025L);

        for (
            int iteration = 1;
            iteration <= iterations;
            iteration++
        ) {
            int size =
                random.nextInt(100) + 1;

            int[] values =
                new int[size];

            for (int i = 0; i < size; i++) {
                values[i] =
                    random.nextInt(2001) - 1000;
            }

            int[] expected =
                reorderArray(values);

            Node input =
                buildList(values);

            Result result =
                method.algorithm.solve(input);

            boolean success =
                result.valid()
                && toValues(result.head()).equals(
                    toIntegerList(expected)
                )
                && !hasCycle(result.head())
                && containsSameNodes(
                    input,
                    result.head()
                );

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

    static int[] reorderArray(int[] values) {
        int[] reordered =
            new int[values.length];

        int left = 0;
        int right = values.length - 1;
        int index = 0;

        while (left < right) {
            reordered[index++] = values[left++];
            reordered[index++] = values[right--];
        }

        if (left == right) {
            reordered[index] = values[left];
        }

        return reordered;
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
            "################ REORDER LINKED LIST #######################"
        );

        System.out.println(
            "############################################################"
        );

        System.out.println();

        MethodCase method =
            new MethodCase(
                "Middle Reverse Merge",
                ReorderLinkedList
                    ::reorderLinkedList
            );

        List<TestCase> tests =
            buildTests();

        runTests(method, tests);
        runRandomisedTests(method, 5000);
    }
}
