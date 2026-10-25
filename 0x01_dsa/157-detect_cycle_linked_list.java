import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class DetectCycleLinkedList {

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
        boolean cycleDetected,
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
     * Detect a cycle using a HashSet of node identities.
     *
     * Time: O(n)
     * Space: O(n)
     */
    static Result detectCycleLinkedListHashSet(Node head) {
        if (!validNode(head)) {
            return new Result(false, false);
        }

        Set<Node> visited = new HashSet<>();
        Node current = head;

        while (current != null) {
            if (!visited.add(current)) {
                return new Result(true, true);
            }

            current = current.next;
        }

        return new Result(false, true);
    }

    /**
     * Floyd's tortoise-and-hare algorithm.
     *
     * slow moves one node at a time.
     * fast moves two nodes at a time.
     *
     * Time: O(n)
     * Space: O(1)
     */
    static Result detectCycleLinkedListFloydAlgo(Node head) {
        if (!validNode(head)) {
            return new Result(false, false);
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
                return new Result(true, true);
            }
        }

        return new Result(false, true);
    }

    /* **********************************************************************
     * List Helpers
     * **********************************************************************/

    static Node buildList(int... values) {
        if (values == null || values.length == 0) {
            return null;
        }

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

    static ArrayList<Node> collectAcyclicNodes(Node head) {
        ArrayList<Node> nodes = new ArrayList<>();
        Set<Node> visited = new HashSet<>();

        Node current = head;

        while (
            current != null
            && visited.add(current)
        ) {
            nodes.add(current);
            current = current.next;
        }

        return nodes;
    }

    static Node createCycle(
        int[] values,
        int cycleEntryIndex
    ) {
        if (
            values == null
            || values.length == 0
            || cycleEntryIndex < 0
            || cycleEntryIndex >= values.length
        ) {
            return null;
        }

        Node head = buildList(values);
        ArrayList<Node> nodes =
            collectAcyclicNodes(head);

        Node tail =
            nodes.get(nodes.size() - 1);

        tail.next =
            nodes.get(cycleEntryIndex);

        return head;
    }

    static Node createSelfLoop(int value) {
        Node current = node(value);
        current.next = current;
        return current;
    }

    static boolean expectedCycle(
        Node head
    ) {
        Set<Node> visited = new HashSet<>();
        Node current = head;

        while (current != null) {
            if (!visited.add(current)) {
                return true;
            }

            current = current.next;
        }

        return false;
    }

    /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    static class TestCase {
        final String id;
        final Node head;
        final boolean expectedCycle;
        final boolean expectedValid;
        final String description;

        TestCase(
            String id,
            Node head,
            boolean expectedCycle,
            boolean expectedValid,
            String description
        ) {
            this.id = id;
            this.head = head;
            this.expectedCycle = expectedCycle;
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

    /* **********************************************************************
     * Test Runner
     * **********************************************************************/

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
            try {
                Result actual =
                    method.algorithm.solve(test.head);

                boolean success =
                    actual.cycleDetected()
                        == test.expectedCycle
                    && actual.valid()
                        == test.expectedValid;

                if (success) {
                    passed++;

                    System.out.printf(
                        "PASS %s (%s)%n",
                        test.id,
                        test.description
                    );

                    System.out.println(
                        "  cycleDetected = "
                            + actual.cycleDetected()
                    );
                } else {
                    failed++;

                    System.out.printf(
                        "FAIL %s (%s)%n",
                        test.id,
                        test.description
                    );

                    System.out.println(
                        "  expected = cycleDetected="
                            + test.expectedCycle
                            + ", valid="
                            + test.expectedValid
                    );

                    System.out.println(
                        "  actual   = cycleDetected="
                            + actual.cycleDetected()
                            + ", valid="
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
            ArrayList<Result> results =
                new ArrayList<>();

            for (MethodCase method : methods) {
                Result result =
                    method.algorithm.solve(test.head);

                results.add(result);

                if (
                    result.cycleDetected()
                        != test.expectedCycle
                    || result.valid()
                        != test.expectedValid
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
                && !result.cycleDetected()
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
     * Test Data
     * **********************************************************************/

    static List<TestCase> buildTests() {
        List<TestCase> tests =
            new ArrayList<>();

        tests.add(
            new TestCase(
                "A1",
                buildList(1, 2, 3, 4, 5),
                false,
                true,
                "ordinary acyclic list"
            )
        );

        tests.add(
            new TestCase(
                "A2",
                buildList(42),
                false,
                true,
                "single-node acyclic list"
            )
        );

        tests.add(
            new TestCase(
                "C1",
                createCycle(
                    new int[]{1, 2, 3, 4, 5},
                    0
                ),
                true,
                true,
                "tail points to head"
            )
        );

        tests.add(
            new TestCase(
                "C2",
                createCycle(
                    new int[]{1, 2, 3, 4, 5},
                    2
                ),
                true,
                true,
                "tail points to middle node"
            )
        );

        tests.add(
            new TestCase(
                "C3",
                createCycle(
                    new int[]{1, 2, 3, 4, 5},
                    4
                ),
                true,
                true,
                "tail points to itself"
            )
        );

        tests.add(
            new TestCase(
                "C4",
                createSelfLoop(99),
                true,
                true,
                "single-node self-loop"
            )
        );

        tests.add(
            new TestCase(
                "D1",
                buildList(7, 7, 7, 7),
                false,
                true,
                "duplicate values without a cycle"
            )
        );

        tests.add(
            new TestCase(
                "D2",
                createCycle(
                    new int[]{7, 7, 7, 7},
                    1
                ),
                true,
                true,
                "duplicate values with a cycle"
            )
        );

        tests.add(
            new TestCase(
                "N1",
                buildList(-3, 0, 5, -10),
                false,
                true,
                "negative and positive values"
            )
        );

        tests.add(
            new TestCase(
                "E1",
                null,
                false,
                false,
                "null head"
            )
        );

        return tests;
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
            new Random(20261023L);

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

            boolean shouldHaveCycle =
                random.nextBoolean();

            int cycleEntryIndex =
                random.nextInt(size);

            Node head;

            if (shouldHaveCycle) {
                head = createCycle(
                    values,
                    cycleEntryIndex
                );
            } else {
                head = buildList(values);
            }

            boolean expected =
                expectedCycle(head);

            for (MethodCase method : methods) {
                Result result =
                    method.algorithm.solve(head);

                if (
                    !result.valid()
                    || result.cycleDetected()
                        != expected
                ) {
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
                        "expected cycle = "
                            + expected
                    );

                    System.out.println(
                        "actual = "
                            + result.cycleDetected()
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
            "############### DETECT LINKED-LIST CYCLE ###################"
        );

        System.out.println(
            "############################################################"
        );

        System.out.println();

        List<MethodCase> methods =
            List.of(
                new MethodCase(
                    "HashSet",
                    DetectCycleLinkedList
                        ::detectCycleLinkedListHashSet
                ),
                new MethodCase(
                    "Floyd's Algorithm",
                    DetectCycleLinkedList
                        ::detectCycleLinkedListFloydAlgo
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
