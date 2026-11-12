import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.function.Function;


public class LoopLengthLinkedList {


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
        int length,
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
     * Returns the number of nodes in the loop.
     *
     * Returns -1 when the list is empty or contains no cycle.
     *
     * Time: O(n)
     * Extra space: O(n)
     */
    static Result loopLengthHashSet(Node head) {
        if (!validNode(head)) {
            return new Result(-1, false);
        }


        HashSet<Node> visited = new HashSet<>();
        Node current = head;


        while (current != null) {
            if (visited.contains(current)) {
                return new Result(
                    countNodes(current),
                    true
                );
            }


            visited.add(current);
            current = current.next;
        }


        return new Result(-1, false);
    }


    /**
     * Returns the number of nodes in the loop.
     *
     * Returns -1 when the list is empty or contains no cycle.
     *
     * Time: O(n)
     * Extra space: O(1)
     */
    static Result loopLengthFloydAlgo(Node head) {
        if (!validNode(head)) {
            return new Result(-1, false);
        }


        Node slow = head;
        Node fast = head;


        while (
            fast != null
            && fast.next != null
        ) {
            slow = slow.next;
            fast = fast.next.next;


            if (slow == fast) {
                return new Result(
                    countNodes(slow),
                    true
                );
            }
        }


        return new Result(-1, false);
    }


    /**
     * Counts the nodes in the cycle containing the given node.
     *
     * The given node must lie within a cycle.
     */
    static int countNodes(Node cycleNode) {
        Node current = cycleNode.next;
        int length = 1;


        while (current != cycleNode) {
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
        ArrayList<Integer> values = new ArrayList<>();


        Node current = head;


        while (current != null) {
            values.add(current.val);
            current = current.next;
        }


        return values;
    }


    static Node makeCycle(Node head, int cycleStartIndex) {
        if (!validNode(head)) {
            return null;
        }


        ArrayList<Node> nodes = new ArrayList<>();
        Node current = head;


        while (current != null) {
            nodes.add(current);
            current = current.next;
        }


        if (
            cycleStartIndex < 0
            || cycleStartIndex >= nodes.size()
        ) {
            return head;
        }


        Node tail = nodes.get(nodes.size() - 1);
        Node cycleStart = nodes.get(cycleStartIndex);


        tail.next = cycleStart;


        return head;
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
        final int cycleStartIndex;
        final int expectedLength;
        final boolean expectedValid;
        final String description;


        TestCase(
            String id,
            int[] values,
            int cycleStartIndex,
            int expectedLength,
            boolean expectedValid,
            String description
        ) {
            this.id = id;
            this.values = values;
            this.cycleStartIndex = cycleStartIndex;
            this.expectedLength = expectedLength;
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


            if (test.cycleStartIndex >= 0) {
                input = makeCycle(
                    input,
                    test.cycleStartIndex
                );
            }


            try {
                Result actual =
                    method.algorithm.solve(input);


                boolean success =
                    actual.valid() == test.expectedValid
                    && actual.length() == test.expectedLength;


                if (success) {
                    passed++;


                    System.out.printf(
                        "PASS %s (%s)%n",
                        test.id,
                        test.description
                    );


                    System.out.println(
                        "  loop length = "
                            + actual.length()
                    );
                } else {
                    failed++;


                    System.out.printf(
                        "FAIL %s (%s)%n",
                        test.id,
                        test.description
                    );


                    System.out.println(
                        "  input = "
                            + Arrays.toString(test.values)
                    );


                    System.out.println(
                        "  expected = "
                            + test.expectedLength
                            + ", valid = "
                            + test.expectedValid
                    );


                    System.out.println(
                        "  actual = "
                            + actual.length()
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
        List<TestCase> tests = new ArrayList<>();


        tests.add(
            new TestCase(
                "B1",
                new int[]{1, 2, 3, 4, 5},
                2,
                3,
                true,
                "cycle begins at index 2"
            )
        );


        tests.add(
            new TestCase(
                "B2",
                new int[]{1, 2, 3, 4, 5, 6},
                1,
                5,
                true,
                "cycle begins at index 1"
            )
        );


        tests.add(
            new TestCase(
                "B3",
                new int[]{10, 20, 30, 40},
                0,
                4,
                true,
                "whole list forms a cycle"
            )
        );


        tests.add(
            new TestCase(
                "S1",
                new int[]{42},
                0,
                1,
                true,
                "single-node cycle"
            )
        );


        tests.add(
            new TestCase(
                "S2",
                new int[]{1, 2},
                0,
                2,
                true,
                "two-node cycle"
            )
        );


        tests.add(
            new TestCase(
                "S3",
                new int[]{7, 7, 7, 7},
                2,
                2,
                true,
                "duplicate values with a cycle"
            )
        );


        tests.add(
            new TestCase(
                "S4",
                new int[]{-5, 0, 5},
                1,
                2,
                true,
                "negative and positive values"
            )
        );


        tests.add(
            new TestCase(
                "N1",
                new int[]{1, 2, 3},
                -1,
                -1,
                false,
                "acyclic list"
            )
        );


        tests.add(
            new TestCase(
                "N2",
                new int[]{42},
                -1,
                -1,
                false,
                "single-node acyclic list"
            )
        );


        tests.add(
            new TestCase(
                "E1",
                new int[]{},
                -1,
                -1,
                false,
                "empty list"
            )
        );


        tests.add(
            new TestCase(
                "E2",
                null,
                -1,
                -1,
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


        Random random = new Random(20261111L);


        for (
            int iteration = 1;
            iteration <= iterations;
            iteration++
        ) {
            int size = random.nextInt(50) + 1;
            int[] values = new int[size];


            for (int i = 0; i < size; i++) {
                values[i] =
                    random.nextInt(2001) - 1000;
            }


            boolean shouldHaveCycle =
                random.nextBoolean();


            int cycleStartIndex = -1;


            if (shouldHaveCycle) {
                cycleStartIndex =
                    random.nextInt(size);
            }


            Node input = buildList(values);


            if (shouldHaveCycle) {
                input = makeCycle(
                    input,
                    cycleStartIndex
                );
            }


            int expectedLength;


            if (shouldHaveCycle) {
                expectedLength =
                    size - cycleStartIndex;
            } else {
                expectedLength = -1;
            }


            Result result =
                method.algorithm.solve(input);


            boolean success =
                result.valid()
                && result.length() == expectedLength;


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
                    "cycleStartIndex = "
                        + cycleStartIndex
                );


                System.out.println(
                    "expected = "
                        + expectedLength
                );


                System.out.println(
                    "actual = "
                        + result.length()
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
            "################ LOOP LENGTH LINKED LIST ##################"
        );


        System.out.println(
            "############################################################"
        );


        System.out.println();


        List<TestCase> tests = buildTests();


        MethodCase hashSetMethod =
            new MethodCase(
                "HashSet",
                LoopLengthLinkedList
                    ::loopLengthHashSet
            );


        MethodCase floydMethod =
            new MethodCase(
                "Floyd's Algorithm",
                LoopLengthLinkedList
                    ::loopLengthFloydAlgo
            );


        runTests(hashSetMethod, tests);
        runRandomisedTests(hashSetMethod, 5000);


        runTests(floydMethod, tests);
        runRandomisedTests(floydMethod, 5000);
    }
}
