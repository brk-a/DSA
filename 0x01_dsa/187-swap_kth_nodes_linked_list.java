import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

public class SwapKthNodeLinkedList {

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


    /* **********************************************************************
     * Algorithm
     * **********************************************************************/

    /**
     * Swaps the kth node from the beginning with the kth node from
     * the end.
     *
     * For example, in the list:
     *
     *     1 -> 2 -> 3 -> 4 -> 5
     *
     * swapping k = 2 gives:
     *
     *     1 -> 4 -> 3 -> 2 -> 5
     *
     * The nodes themselves are relinked; their values are not copied.
     *
     * Time: O(n)
     * Extra space: O(1)
     */
    static Result swapKthNodeLinkedList(
        Node head,
        int k
    ) {
        if (!validNode(head) || k <= 0) {
            return new Result(null, false);
        }

        int length = getLength(head);

        // Keep the original behaviour for an out-of-range position:
        // the list remains unchanged and is still considered valid.
        if (k > length) {
            return new Result(head, true);
        }

        // The kth node from the beginning and the kth node from the
        // end are the same node.
        if (2 * k - 1 == length) {
            return new Result(head, true);
        }

        // A dummy node removes special handling for swaps involving
        // the original head.
        Node dummy = node(0, head);

        Node previousX = dummy;
        for (int index = 1; index < k; index++) {
            previousX = previousX.next;
        }

        Node previousY = dummy;
        for (int index = 1; index < length - k + 1; index++) {
            previousY = previousY.next;
        }

        Node x = previousX.next;
        Node y = previousY.next;

        // Handle adjacent nodes carefully.
        //
        // If x and y are adjacent, then previousY is x. Relinking
        // previousY.next before changing x.next would otherwise create
        // a cycle.
        if (x.next == y) {
            x.next = y.next;
            y.next = x;
            previousX.next = y;
        } else if (y.next == x) {
            y.next = x.next;
            x.next = y;
            previousY.next = x;
        } else {
            Node nodeAfterX = x.next;
            Node nodeAfterY = y.next;

            previousX.next = y;
            previousY.next = x;

            x.next = nodeAfterY;
            y.next = nodeAfterX;
        }

        return new Result(dummy.next, true);
    }

    static int getLength(Node head) {
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
        Node swapped
    ) {
        ArrayList<Node> originalNodes =
            toNodes(original);

        ArrayList<Node> swappedNodes =
            toNodes(swapped);

        if (
            originalNodes.size()
            != swappedNodes.size()
        ) {
            return false;
        }

        for (Node node : swappedNodes) {
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
                        test.k
                    );

                boolean success =
                    actual.valid() == test.expectedValid
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
                        "  k        = "
                            + test.k
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
                new int[]{1, 2, 3, 4, 5},
                2,
                new int[]{1, 4, 3, 2, 5},
                true,
                "swap second node from each end"
            )
        );

        tests.add(
            new TestCase(
                "B2",
                new int[]{1, 2, 3, 4, 5, 6},
                2,
                new int[]{1, 5, 4, 3, 2, 6},
                true,
                "swap second node from each end in an even-length list"
            )
        );

        tests.add(
            new TestCase(
                "B3",
                new int[]{1, 2, 3, 4, 5, 6, 7},
                3,
                new int[]{1, 2, 5, 4, 3, 6, 7},
                true,
                "swap third node from each end"
            )
        );

        tests.add(
            new TestCase(
                "B4",
                new int[]{1, 2, 3, 4},
                2,
                new int[]{1, 3, 2, 4},
                true,
                "adjacent nodes in the middle"
            )
        );

        tests.add(
            new TestCase(
                "B5",
                new int[]{1, 2, 3, 4, 5},
                1,
                new int[]{5, 2, 3, 4, 1},
                true,
                "swap first and last nodes"
            )
        );

        tests.add(
            new TestCase(
                "B6",
                new int[]{1, 2, 3, 4, 5},
                5,
                new int[]{5, 2, 3, 4, 1},
                true,
                "swap last and first nodes using k equal to length"
            )
        );

        tests.add(
            new TestCase(
                "B7",
                new int[]{1, 2, 3, 4},
                1,
                new int[]{4, 2, 3, 1},
                true,
                "swap first and last nodes in an even-length list"
            )
        );

        tests.add(
            new TestCase(
                "S1",
                new int[]{42},
                1,
                new int[]{42},
                true,
                "single-node list"
            )
        );

        tests.add(
            new TestCase(
                "S2",
                new int[]{1, 2},
                1,
                new int[]{2, 1},
                true,
                "two-node list"
            )
        );

        tests.add(
            new TestCase(
                "S3",
                new int[]{1, 2, 3},
                2,
                new int[]{1, 2, 3},
                true,
                "middle node swaps with itself"
            )
        );

        tests.add(
            new TestCase(
                "S4",
                new int[]{7, 7, 7, 7},
                2,
                new int[]{7, 7, 7, 7},
                true,
                "duplicate values"
            )
        );

        tests.add(
            new TestCase(
                "S5",
                new int[]{-5, 0, 5, 10},
                2,
                new int[]{-5, 5, 0, 10},
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
                "zero is not a valid position"
            )
        );

        tests.add(
            new TestCase(
                "E4",
                new int[]{1, 2, 3},
                -2,
                new int[]{},
                false,
                "negative position"
            )
        );

        tests.add(
            new TestCase(
                "E5",
                new int[]{1, 2, 3},
                4,
                new int[]{1, 2, 3},
                true,
                "position beyond the list leaves it unchanged"
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
            new Random(20261122L);

        for (
            int iteration = 1;
            iteration <= iterations;
            iteration++
        ) {
            int size =
                random.nextInt(50) + 1;

            int[] values =
                new int[size];

            for (int index = 0; index < size; index++) {
                values[index] =
                    random.nextInt(2001) - 1000;
            }

            int k =
                random.nextInt(size + 3) - 1;

            int[] expected =
                swapKthNodeArray(
                    values,
                    k
                );

            boolean expectedValid =
                k >= 1 && size > 0;

            Node input =
                buildList(values);

            Result result =
                method.algorithm.solve(
                    input,
                    k
                );

            boolean success =
                result.valid() == expectedValid
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
                    "k = "
                        + k
                );

                System.out.println(
                    "expected = "
                        + Arrays.toString(expected)
                        + ", valid = "
                        + expectedValid
                );

                System.out.println(
                    "actual = "
                        + toValues(result.head())
                        + ", valid = "
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

    static int[] swapKthNodeArray(
        int[] values,
        int k
    ) {
        if (values.length == 0 || k <= 0) {
            return new int[]{};
        }

        if (k > values.length) {
            return values.clone();
        }

        int[] swapped =
            values.clone();

        int left = k - 1;
        int right =
            values.length - k;

        int temporary = swapped[left];
        swapped[left] = swapped[right];
        swapped[right] = temporary;

        return swapped;
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
            "############### SWAP KTH NODE LINKED LIST ##################"
        );

        System.out.println(
            "############################################################"
        );

        System.out.println();

        MethodCase method =
            new MethodCase(
                "Dummy Head Relinking",
                SwapKthNodeLinkedList
                    ::swapKthNodeLinkedList
            );

        List<TestCase> tests =
            buildTests();

        runTests(method, tests);
        runRandomisedTests(method, 5_000);
    }
}
