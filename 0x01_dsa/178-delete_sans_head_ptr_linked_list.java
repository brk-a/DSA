import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;


public class DeleteSansHeadPointerLinkedList {


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
        Node node,
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
     * Deletes the given node from a singly linked list without access to
     * the head pointer.
     *
     * The given node adopts the value and successor of the following node.
     * The following node is then bypassed.
     *
     * For example:
     *
     * 4 -> 5 -> 1 -> 9
     *
     * Deleting the node containing 5 produces:
     *
     * 4 -> 1 -> 9
     *
     * Time: O(1)
     * Extra space: O(1)
     */
    static Result deleteSansHeadPointer(Node node) {
        if (!validNode(node)) {
            return new Result(null, false);
        }


        if (node.next == null) {
            return new Result(node, false);
        }


        Node successor = node.next;


        node.val = successor.val;
        node.next = successor.next;


        return new Result(node, true);
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


    static Node nodeAt(Node head, int index) {
        Node current = head;


        for (int i = 0; i < index; i++) {
            if (current == null) {
                return null;
            }


            current = current.next;
        }


        return current;
    }


    /* **********************************************************************
     * Test Harness
     * **********************************************************************/


    static class TestCase {
        final String id;
        final int[] values;
        final int targetIndex;
        final int[] expected;
        final boolean expectedValid;
        final String description;


        TestCase(
            String id,
            int[] values,
            int targetIndex,
            int[] expected,
            boolean expectedValid,
            String description
        ) {
            this.id = id;
            this.values = values;
            this.targetIndex = targetIndex;
            this.expected = expected;
            this.expectedValid = expectedValid;
            this.description = description;
        }
    }


    @FunctionalInterface
    interface Algorithm {
        Result solve(Node node);
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
            Node head =
                buildList(test.values);

            Node target =
                nodeAt(head, test.targetIndex);


            try {
                Result actual =
                    method.algorithm.solve(target);


                boolean success =
                    actual.valid()
                        == test.expectedValid
                    && toValues(head).equals(
                        toIntegerList(test.expected)
                    )
                    && !hasCycle(head)
                    && hasExpectedSize(
                        head,
                        test.expected.length
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
                            + toValues(head)
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
                        "  target index = "
                            + test.targetIndex
                    );


                    System.out.println(
                        "  expected = "
                            + Arrays.toString(test.expected)
                            + ", valid = "
                            + test.expectedValid
                    );


                    System.out.println(
                        "  actual   = "
                            + toValues(head)
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


    static boolean hasExpectedSize(
        Node head,
        int expectedSize
    ) {
        return toValues(head).size()
            == expectedSize;
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
                1,
                new int[]{1, 3, 4},
                true,
                "delete an interior node"
            )
        );


        tests.add(
            new TestCase(
                "B2",
                new int[]{1, 2, 3, 4, 5},
                2,
                new int[]{1, 2, 4, 5},
                true,
                "delete the middle node"
            )
        );


        tests.add(
            new TestCase(
                "B3",
                new int[]{1, 2, 3, 4, 5, 6},
                3,
                new int[]{1, 2, 3, 5, 6},
                true,
                "delete from a longer list"
            )
        );


        tests.add(
            new TestCase(
                "S1",
                new int[]{1, 2, 3},
                0,
                new int[]{2, 3},
                true,
                "delete the head node"
            )
        );


        tests.add(
            new TestCase(
                "S2",
                new int[]{1, 2},
                0,
                new int[]{2},
                true,
                "delete the first of two nodes"
            )
        );


        tests.add(
            new TestCase(
                "S3",
                new int[]{7, 7, 7, 7},
                1,
                new int[]{7, 7, 7},
                true,
                "duplicate values"
            )
        );


        tests.add(
            new TestCase(
                "S4",
                new int[]{-5, 0, 5, 10},
                2,
                new int[]{-5, 0, 10},
                true,
                "negative and positive values"
            )
        );


        tests.add(
            new TestCase(
                "E1",
                new int[]{},
                0,
                new int[]{},
                false,
                "null node"
            )
        );


        tests.add(
            new TestCase(
                "E2",
                new int[]{42},
                0,
                new int[]{42},
                false,
                "single-node list has no successor"
            )
        );


        tests.add(
            new TestCase(
                "E3",
                new int[]{1, 2, 3},
                2,
                new int[]{1, 2, 3},
                false,
                "tail node has no successor"
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
            new Random(20261112L);


        for (
            int iteration = 1;
            iteration <= iterations;
            iteration++
        ) {
            int size =
                random.nextInt(100) + 2;


            int[] values =
                new int[size];


            for (int i = 0; i < size; i++) {
                values[i] =
                    random.nextInt(2001) - 1000;
            }


            int targetIndex =
                random.nextInt(size - 1);


            int[] expected =
                removeIndex(values, targetIndex);


            Node head =
                buildList(values);

            Node target =
                nodeAt(head, targetIndex);


            Result result =
                method.algorithm.solve(target);


            boolean success =
                result.valid()
                && toValues(head).equals(
                    toIntegerList(expected)
                )
                && !hasCycle(head)
                && hasExpectedSize(
                    head,
                    expected.length
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
                    "target index = "
                        + targetIndex
                );


                System.out.println(
                    "expected = "
                        + Arrays.toString(expected)
                );


                System.out.println(
                    "actual = "
                        + toValues(head)
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


    static int[] removeIndex(
        int[] values,
        int targetIndex
    ) {
        int[] remaining =
            new int[values.length - 1];


        int outputIndex = 0;


        for (
            int index = 0;
            index < values.length;
            index++
        ) {
            if (index != targetIndex) {
                remaining[outputIndex++] =
                    values[index];
            }
        }


        return remaining;
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
            "############ DELETE SANS HEAD POINTER ######################"
        );


        System.out.println(
            "############################################################"
        );


        System.out.println();


        MethodCase method =
            new MethodCase(
                "Successor Copy and Bypass",
                DeleteSansHeadPointerLinkedList
                    ::deleteSansHeadPointer
            );


        List<TestCase> tests =
            buildTests();


        runTests(method, tests);
        runRandomisedTests(method, 5000);
    }
}
