import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

public class RotateByNNodesLinkedList {

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

        Node(int val, Node prev, Node next) {
            this.val = val;
            this.prev = prev;
            this.next = next;
        }
    }

    /* **********************************************************************
     * Result
     * **********************************************************************/

    record Result(
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

    /**
     * Rotates a non-circular doubly linked list to the left by k positions.
     *
     * For example:
     * 1 -> 2 -> 3 -> 4 -> 5, k = 2
     * becomes
     * 3 -> 4 -> 5 -> 1 -> 2
     *
     * Time: O(n)
     * Extra space: O(1)
     */
    static Result rotateByNCircularDll(Node head, int k) {
        if (!validNode(head)) {
            return new Result(null, false);
        }

        int length = listLength(head);
        k = Math.floorMod(k, length);

        if (k == 0) {
            return new Result(head, true);
        }

        Node tail = head;
        while (tail.next != null) {
            tail = tail.next;
        }

        // Temporarily make the list circular.
        tail.next = head;
        head.prev = tail;

        // Move the head forwards by k nodes.
        for (int count = 0; count < k; count++) {
            head = head.next;
            tail = tail.next;
        }

        // Break the circle at the new boundary.
        tail.next = null;
        head.prev = null;

        return new Result(head, true);
    }

    /**
     * Rotates a non-circular doubly linked list to the left by k positions
     * by splitting the list after the kth node.
     *
     * Time: O(n)
     * Extra space: O(1)
     */
    static Result rotateByNBreakAtP(Node head, int k) {
        if (!validNode(head)) {
            return new Result(null, false);
        }

        int length = listLength(head);
        k = Math.floorMod(k, length);

        if (k == 0) {
            return new Result(head, true);
        }

        Node splitPoint = head;
        for (int index = 1; index < k; index++) {
            splitPoint = splitPoint.next;
        }

        Node newHead = splitPoint.next;
        Node tail = newHead;

        // Detach the first k nodes.
        splitPoint.next = null;
        newHead.prev = null;

        // Find the final node of the remaining list.
        while (tail.next != null) {
            tail = tail.next;
        }

        // Attach the original prefix to the end.
        tail.next = head;
        head.prev = tail;

        return new Result(newHead, true);
    }

    static int listLength(Node head) {
        int length = 0;
        Node current = head;

        while (current != null) {
            length++;
            current = current.next;
        }

        return length;
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

    static boolean hasValidBackwardLinks(Node head) {
        if (head == null) {
            return true;
        }

        if (head.prev != null) {
            return false;
        }

        Node current = head;

        while (current.next != null) {
            if (current.next.prev != current) {
                return false;
            }

            current = current.next;
        }

        return true;
    }

    static boolean containsSameNodes(Node original, Node rotated) {
        ArrayList<Node> originalNodes = toNodes(original);
        ArrayList<Node> rotatedNodes = toNodes(rotated);

        if (originalNodes.size() != rotatedNodes.size()) {
            return false;
        }

        for (Node node : rotatedNodes) {
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
        final int k;
        final int[] expected;
        final boolean expectedValid;
        final String description;

        TestCase(
            String id,
            int[] values,
            int k,
            int[] expected,
            boolean expectedValid,
            String description
        ) {
            this.id = id;
            this.values = values;
            this.k = k;
            this.expected = expected;
            this.expectedValid = expectedValid;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {
        Result solve(Node head, int k);
    }

    record MethodCase(
        String name,
        Algorithm algorithm
    ) {}

    static void runTests(
        MethodCase method,
        List<TestCase> tests
    ) {
        printSeparator();
        System.out.println(method.name());
        printSeparator();

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {
            Node input = buildList(test.values);

            try {
                Result actual = method.algorithm().solve(input, test.k);

                boolean success =
                    actual.valid() == test.expectedValid
                    && toValues(actual.head()).equals(toIntegerList(test.expected))
                    && !hasCycle(actual.head())
                    && hasValidBackwardLinks(actual.head())
                    && containsSameNodes(input, actual.head());

                if (success) {
                    passed++;

                    System.out.printf(
                        "PASS %s (%s)%n",
                        test.id,
                        test.description
                    );

                    System.out.println(
                        "  result = " + toValues(actual.head())
                    );
                } else {
                    failed++;

                    System.out.printf(
                        "FAIL %s (%s)%n",
                        test.id,
                        test.description
                    );

                    System.out.println(
                        "  input    = " + Arrays.toString(test.values)
                    );

                    System.out.println(
                        "  k        = " + test.k
                    );

                    System.out.println(
                        "  expected = " + Arrays.toString(test.expected)
                            + ", valid = " + test.expectedValid
                    );

                    System.out.println(
                        "  actual   = " + toValues(actual.head())
                            + ", valid = " + actual.valid()
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
                    "  exception = " + exception
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

        tests.add(new TestCase(
            "B1",
            new int[]{1, 2, 3, 4, 5},
            2,
            new int[]{3, 4, 5, 1, 2},
            true,
            "rotate by two"
        ));

        tests.add(new TestCase(
            "B2",
            new int[]{1, 2, 3, 4, 5},
            4,
            new int[]{5, 1, 2, 3, 4},
            true,
            "rotate by length minus one"
        ));

        tests.add(new TestCase(
            "B3",
            new int[]{1, 2, 3, 4, 5},
            5,
            new int[]{1, 2, 3, 4, 5},
            true,
            "rotate by full length"
        ));

        tests.add(new TestCase(
            "B4",
            new int[]{1, 2, 3, 4, 5},
            7,
            new int[]{3, 4, 5, 1, 2},
            true,
            "rotate by more than length"
        ));

        tests.add(new TestCase(
            "B5",
            new int[]{1, 2, 3, 4, 5},
            -2,
            new int[]{4, 5, 1, 2, 3},
            true,
            "negative rotation"
        ));

        tests.add(new TestCase(
            "S1",
            new int[]{42},
            3,
            new int[]{42},
            true,
            "single-node list"
        ));

        tests.add(new TestCase(
            "S2",
            new int[]{1, 2},
            1,
            new int[]{2, 1},
            true,
            "two-node list"
        ));

        tests.add(new TestCase(
            "S3",
            new int[]{7, 7, 7, 7},
            2,
            new int[]{7, 7, 7, 7},
            true,
            "duplicate values"
        ));

        tests.add(new TestCase(
            "S4",
            new int[]{-5, 0, 5, 10},
            1,
            new int[]{0, 5, 10, -5},
            true,
            "negative and positive values"
        ));

        tests.add(new TestCase(
            "E1",
            new int[]{},
            2,
            new int[]{},
            false,
            "empty list"
        ));

        tests.add(new TestCase(
            "E2",
            null,
            2,
            new int[]{},
            false,
            "null list"
        ));

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

        Random random = new Random(20261123L);

        for (int iteration = 1; iteration <= iterations; iteration++) {
            int size = random.nextInt(100) + 1;
            int[] values = new int[size];

            for (int index = 0; index < size; index++) {
                values[index] = random.nextInt(2001) - 1000;
            }

            int k = random.nextInt(301) - 100;
            int[] expected = rotateArray(values, k);

            Node input = buildList(values);
            Result result = method.algorithm().solve(input, k);

            boolean success =
                result.valid()
                && toValues(result.head()).equals(toIntegerList(expected))
                && !hasCycle(result.head())
                && hasValidBackwardLinks(result.head())
                && containsSameNodes(input, result.head());

            if (!success) {
                System.out.println("Randomised test FAILED");
                System.out.println("iteration = " + iteration);
                System.out.println("input = " + Arrays.toString(values));
                System.out.println("k = " + k);
                System.out.println("expected = " + Arrays.toString(expected));
                System.out.println("actual = " + toValues(result.head()));
                System.out.println("valid = " + result.valid());

                return;
            }
        }

        System.out.printf(
            "All %d randomised tests passed.%n%n",
            iterations
        );
    }

    static int[] rotateArray(int[] values, int k) {
        int length = values.length;

        if (length == 0) {
            return new int[]{};
        }

        k = Math.floorMod(k, length);

        int[] rotated = new int[length];

        for (int index = 0; index < length; index++) {
            rotated[index] = values[(index + k) % length];
        }

        return rotated;
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
            "############### ROTATE BY N NODES LINKED LIST ##############"
        );

        System.out.println(
            "############################################################"
        );

        System.out.println();

        List<TestCase> tests = buildTests();

        MethodCase circularMethod = new MethodCase(
            "Circular Doubly Linked List Rotation",
            RotateByNNodesLinkedList::rotateByNCircularDll
        );

        MethodCase splitMethod = new MethodCase(
            "Break At Position Rotation",
            RotateByNNodesLinkedList::rotateByNBreakAtP
        );

        runTests(circularMethod, tests);
        runRandomisedTests(circularMethod, 5000);

        runTests(splitMethod, tests);
        runRandomisedTests(splitMethod, 5000);
    }
}
