import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SplitCircularLinkedList {

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

    /**
     * Contains the heads of the two resulting circular lists.
     *
     * When the input is invalid, both heads are null.
     * When the input contains one node, the second head is null.
     */
    static record Result(
        List<Node> heads,
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

    static Result invalidResult() {
        return new Result(
            Arrays.asList((Node) null, (Node) null),
            false
        );
    }

    /**
     * Splits a circular linked list into two circular linked lists.
     *
     * The first list contains ceil(n / 2) nodes.
     * The second list contains the remaining nodes.
     *
     * Time: O(n)
     * Extra space: O(1)
     */
    static Result splitCircularLinkedList(Node head) {
        if (!validNode(head) || !validNode(head.next)) {
            return invalidResult();
        }

        /*
         * A one-node circular list cannot be divided into two
         * non-empty circular lists, so the node remains in the
         * first list and the second list is empty.
         */
        if (head.next == head) {
            return new Result(
                Arrays.asList(head, (Node) null),
                true
            );
        }

        Node slow = head;
        Node fast = head;

        while (true) {
            if (
                !validNode(fast.next)
                || !validNode(fast.next.next)
            ) {
                return invalidResult();
            }

            if (
                fast.next == head
                || fast.next.next == head
            ) {
                break;
            }

            slow = slow.next;
            fast = fast.next.next;
        }

        /*
         * For an even-length list, fast stops immediately before
         * the final node. Move it forward so that it points to
         * the final node of the original list.
         */
        if (fast.next.next == head) {
            fast = fast.next;
        }

        Node firstHead = head;
        Node secondHead = slow.next;

        fast.next = secondHead;
        slow.next = firstHead;

        return new Result(
            Arrays.asList(firstHead, secondHead),
            true
        );
    }

    /* **********************************************************************
     * Circular List Helpers
     * **********************************************************************/

    static Node buildCircularList(int... values) {
        if (values.length == 0) {
            return null;
        }

        Node head = node(values[0]);
        Node tail = head;

        for (int index = 1; index < values.length; index++) {
            Node current = node(values[index]);
            tail.next = current;
            tail = current;
        }

        tail.next = head;
        return head;
    }

    static ArrayList<Integer> toValues(Node head) {
        ArrayList<Integer> values = new ArrayList<>();

        if (!validNode(head)) {
            return values;
        }

        Node current = head;

        do {
            values.add(current.val);
            current = current.next;
        } while (current != head);

        return values;
    }

    static boolean isCircular(Node head) {
        if (!validNode(head) || !validNode(head.next)) {
            return false;
        }

        Node current = head.next;

        while (validNode(current)) {
            if (current == head) {
                return true;
            }

            current = current.next;
        }

        return false;
    }

    static boolean containsSameNodes(
        Node original,
        Node firstHalf,
        Node secondHalf
    ) {
        ArrayList<Node> originalNodes = toNodes(original);
        ArrayList<Node> resultNodes = new ArrayList<>();

        resultNodes.addAll(toNodes(firstHalf));
        resultNodes.addAll(toNodes(secondHalf));

        if (originalNodes.size() != resultNodes.size()) {
            return false;
        }

        for (Node node : resultNodes) {
            if (!originalNodes.contains(node)) {
                return false;
            }
        }

        return true;
    }

    static ArrayList<Node> toNodes(Node head) {
        ArrayList<Node> nodes = new ArrayList<>();

        if (!validNode(head)) {
            return nodes;
        }

        Node current = head;

        do {
            nodes.add(current);
            current = current.next;
        } while (current != head);

        return nodes;
    }

    /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    static class TestCase {
        final String id;
        final int[] values;
        final int[] expectedFirst;
        final int[] expectedSecond;
        final boolean expectedValid;
        final String description;

        TestCase(
            String id,
            int[] values,
            int[] expectedFirst,
            int[] expectedSecond,
            boolean expectedValid,
            String description
        ) {
            this.id = id;
            this.values = values;
            this.expectedFirst = expectedFirst;
            this.expectedSecond = expectedSecond;
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

        MethodCase(String name, Algorithm algorithm) {
            this.name = name;
            this.algorithm = algorithm;
        }
    }

    static void runTests(MethodCase method, List<TestCase> tests) {
        printSeparator();
        System.out.println(method.name);
        printSeparator();

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {
            Node input = buildCircularList(test.values);

            try {
                Result actual = method.algorithm.solve(input);

                boolean success =
                    actual.valid() == test.expectedValid
                    && toValues(actual.heads().get(0)).equals(
                        toIntegerList(test.expectedFirst)
                    )
                    && toValues(actual.heads().get(1)).equals(
                        toIntegerList(test.expectedSecond)
                    )
                    && isCircular(actual.heads().get(0))
                    && containsSameNodes(
                        input,
                        actual.heads().get(0),
                        actual.heads().get(1)
                    );

                if (success) {
                    passed++;

                    System.out.printf(
                        "PASS %s (%s)%n",
                        test.id,
                        test.description
                    );

                    System.out.println(
                        "  first half  = "
                            + toValues(actual.heads().get(0))
                    );

                    System.out.println(
                        "  second half = "
                            + toValues(actual.heads().get(1))
                    );
                } else {
                    failed++;

                    System.out.printf(
                        "FAIL %s (%s)%n",
                        test.id,
                        test.description
                    );

                    System.out.println(
                        "  input         = "
                            + Arrays.toString(test.values)
                    );

                    System.out.println(
                        "  expected first  = "
                            + Arrays.toString(test.expectedFirst)
                    );

                    System.out.println(
                        "  expected second = "
                            + Arrays.toString(test.expectedSecond)
                    );

                    System.out.println(
                        "  actual first   = "
                            + toValues(actual.heads().get(0))
                    );

                    System.out.println(
                        "  actual second  = "
                            + toValues(actual.heads().get(1))
                    );

                    System.out.println(
                        "  valid = "
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

        printResults(passed, failed, tests.size());
    }

    /* **********************************************************************
     * Test Data
     * **********************************************************************/

    static List<TestCase> buildTests() {
        List<TestCase> tests = new ArrayList<>();

        tests.add(
            new TestCase(
                "B1",
                new int[]{1, 2, 3, 4},
                new int[]{1, 2},
                new int[]{3, 4},
                true,
                "even-length circular list"
            )
        );

        tests.add(
            new TestCase(
                "B2",
                new int[]{1, 2, 3, 4, 5},
                new int[]{1, 2, 3},
                new int[]{4, 5},
                true,
                "odd-length circular list"
            )
        );

        tests.add(
            new TestCase(
                "B3",
                new int[]{1, 2, 3, 4, 5, 6},
                new int[]{1, 2, 3},
                new int[]{4, 5, 6},
                true,
                "longer even-length circular list"
            )
        );

        tests.add(
            new TestCase(
                "S1",
                new int[]{42},
                new int[]{42},
                new int[]{},
                true,
                "single-node circular list"
            )
        );

        tests.add(
            new TestCase(
                "S2",
                new int[]{1, 2},
                new int[]{1},
                new int[]{2},
                true,
                "two-node circular list"
            )
        );

        tests.add(
            new TestCase(
                "S3",
                new int[]{1, 2, 3},
                new int[]{1, 2},
                new int[]{3},
                true,
                "three-node circular list"
            )
        );

        tests.add(
            new TestCase(
                "S4",
                new int[]{7, 7, 7, 7},
                new int[]{7, 7},
                new int[]{7, 7},
                true,
                "duplicate values"
            )
        );

        tests.add(
            new TestCase(
                "S5",
                new int[]{-5, 0, 5, 10},
                new int[]{-5, 0},
                new int[]{5, 10},
                true,
                "negative and positive values"
            )
        );

        tests.add(
            new TestCase(
                "E1",
                new int[]{},
                new int[]{},
                new int[]{},
                false,
                "empty circular list"
            )
        );

        tests.add(
            new TestCase(
                "E2",
                new int[]{1},
                new int[]{},
                new int[]{},
                false,
                "non-circular single-node list"
            )
        );

        tests.add(
            new TestCase(
                "E3",
                new int[]{1, 2},
                new int[]{},
                new int[]{},
                false,
                "non-circular two-node list"
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

        Random random = new Random(20261118L);

        for (
            int iteration = 1;
            iteration <= iterations;
            iteration++
        ) {
            int size = random.nextInt(100) + 1;
            int[] values = new int[size];

            for (int index = 0; index < size; index++) {
                values[index] = random.nextInt(2001) - 1000;
            }

            int[] expectedFirst = firstHalf(values);
            int[] expectedSecond = secondHalf(values);

            Node input = buildCircularList(values);
            Result result = method.algorithm.solve(input);

            boolean success =
                result.valid()
                && toValues(result.heads().get(0)).equals(
                    toIntegerList(expectedFirst)
                )
                && toValues(result.heads().get(1)).equals(
                    toIntegerList(expectedSecond)
                )
                && isCircular(result.heads().get(0))
                && containsSameNodes(
                    input,
                    result.heads().get(0),
                    result.heads().get(1)
                );

            if (!success) {
                System.out.println("Randomised test FAILED");
                System.out.println("iteration = " + iteration);
                System.out.println("input = " + Arrays.toString(values));
                System.out.println(
                    "expected first = "
                        + Arrays.toString(expectedFirst)
                );
                System.out.println(
                    "expected second = "
                        + Arrays.toString(expectedSecond)
                );
                System.out.println(
                    "actual first = "
                        + toValues(result.heads().get(0))
                );
                System.out.println(
                    "actual second = "
                        + toValues(result.heads().get(1))
                );
                System.out.println("valid = " + result.valid());

                return;
            }
        }

        System.out.printf(
            "All %d randomised tests passed.%n%n",
            iterations
        );
    }

    static int[] firstHalf(int[] values) {
        int firstSize = (values.length + 1) / 2;
        return Arrays.copyOfRange(values, 0, firstSize);
    }

    static int[] secondHalf(int[] values) {
        int firstSize = (values.length + 1) / 2;
        return Arrays.copyOfRange(values, firstSize, values.length);
    }

    static ArrayList<Integer> toIntegerList(int[] values) {
        ArrayList<Integer> result = new ArrayList<>();

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

    static void printResults(int passed, int failed, int total) {
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
            "############# SPLIT CIRCULAR LINKED LIST ###################"
        );

        System.out.println(
            "############################################################"
        );
        System.out.println();

        MethodCase method = new MethodCase(
            "Slow and Fast Pointer Split",
            SplitCircularLinkedList::splitCircularLinkedList
        );

        List<TestCase> tests = buildTests();

        runTests(method, tests);
        runRandomisedTests(method, 5000);
    }
}
