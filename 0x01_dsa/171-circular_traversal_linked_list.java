import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Traverse a circular singly linked list exactly once.
 *
 * A valid circular list begins at head and eventually returns to head.
 * Every reachable node is visited once, in link order.
 *
 * Two implementations are provided:
 *
 * 1. Iterative Traversal
 *
 *      Uses a do...while loop, because the head must be visited before
 *      the termination condition can be checked.
 *
 *      Time:  O(n)
 *      Space: O(n) for the result, plus O(1) auxiliary space
 *
 * 2. Recursive Traversal
 *
 *      Visits each node once and stops after returning to head.
 *
 *      Time:  O(n)
 *      Space: O(n) for the result, plus O(n) call-stack space
 *
 * The public API distinguishes between:
 *
 *      - an empty list, which is valid but produces no values;
 *      - a non-circular list, which is invalid;
 *      - a valid circular list.
 */
public class CircularTraversalLinkedList {


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
            List<Integer> values,
            boolean valid) {
    }


    /* **********************************************************************
     * Helpers
     * **********************************************************************/


    static Node node(int val) {
        return new Node(val);
    }


    static Node node(
            int val,
            Node next) {


        return new Node(
                val,
                next);
    }


    /**
     * Build a circular list from values.
     *
     * For example:
     *
     *      buildCircularList(1, 2, 3)
     *
     * produces:
     *
     *      1 -> 2 -> 3 -> 1
     */
    static Node buildCircularList(int... values) {


        if (values.length == 0) {
            return null;
        }


        Node head = node(values[0]);
        Node current = head;


        for (int i = 1; i < values.length; i++) {


            current.next = node(values[i]);
            current = current.next;
        }


        current.next = head;


        return head;
    }


    /**
     * Build a linear, non-circular list from values.
     */
    static Node buildLinearList(int... values) {


        Node head = null;
        Node current = null;


        for (int value : values) {


            Node newNode = node(value);


            if (head == null) {
                head = newNode;
            } else {
                current.next = newNode;
            }


            current = newNode;
        }


        return head;
    }


    /* **********************************************************************
     * 1. Iterative Traversal
     * **********************************************************************/


    /**
     * Traverse a circular linked list iteratively.
     *
     * The do...while loop is appropriate because the head itself must be
     * processed before checking whether traversal has returned to head.
     */
    static Result circularTraversalIterative(Node head) {


        if (head == null) {
            return new Result(
                    new ArrayList<>(),
                    true);
        }


        List<Integer> values = new ArrayList<>();
        Node current = head;


        do {


            values.add(current.val);
            current = current.next;


            if (current == null) {
                return new Result(
                        new ArrayList<>(),
                        false);
            }


        } while (current != head);


        return new Result(
                values,
                true);
    }


    /* **********************************************************************
     * 2. Recursive Traversal
     * **********************************************************************/


    /**
     * Traverse a circular linked list recursively.
     */
    static Result circularTraversalRecursive(Node head) {


        if (head == null) {
            return new Result(
                    new ArrayList<>(),
                    true);
        }


        List<Integer> values = new ArrayList<>();


        boolean circular =
                traverseFrom(
                        head,
                        head,
                        values);


        if (!circular) {
            return new Result(
                    new ArrayList<>(),
                    false);
        }


        return new Result(
                values,
                true);
    }


    /**
     * Visit nodes until either head is reached again or the list ends.
     *
     * The current node is added before moving to its successor. The
     * stopping condition is deliberately checked after moving, so the
     * head is included exactly once.
     */
    static boolean traverseFrom(
            Node current,
            Node head,
            List<Integer> values) {


        values.add(current.val);


        Node next = current.next;


        if (next == null) {
            return false;
        }


        if (next == head) {
            return true;
        }


        return traverseFrom(
                next,
                head,
                values);
    }


    /* **********************************************************************
     * Reference Traversal
     * **********************************************************************/


    /**
     * Independent reference implementation used by the test suite.
     *
     * It uses a visited-identity set to detect both circular and
     * malformed structures safely.
     */
    static Result circularTraversalReference(Node head) {


        List<Integer> values = new ArrayList<>();


        if (head == null) {
            return new Result(
                    values,
                    true);
        }


        java.util.IdentityHashMap<Node, Boolean> visited =
                new java.util.IdentityHashMap<>();


        Node current = head;


        while (current != null
                && !visited.containsKey(current)) {


            visited.put(current, Boolean.TRUE);
            values.add(current.val);
            current = current.next;
        }


        boolean circular = current == head;


        if (!circular) {
            return new Result(
                    new ArrayList<>(),
                    false);
        }


        return new Result(
                values,
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


        Result solve(Node head);
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
                        actual.valid() == test.expectedValid
                                && actual.values().equals(test.expected);


                if (success) {


                    passed++;


                    System.out.printf(
                            "PASS %s (%s)%n",
                            test.id,
                            test.description);


                    System.out.println(
                            "  values = "
                                    + actual.values());


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
                                    + actual.values()
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
                                .solve(test.head);


                boolean success =
                        first.valid() == test.expectedValid
                                && first.values().equals(test.expected);


                for (int i = 1; i < methods.size(); i++) {


                    Result actual =
                            methods.get(i)
                                    .algorithm
                                    .solve(test.head);


                    if (actual.valid() != first.valid()
                            || !actual.values().equals(first.values())) {


                        success = false;


                        System.out.printf(
                                "  %s = %s, valid = %s%n",
                                methods.get(i).name,
                                actual.values(),
                                actual.valid());
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


        /*
         * A linear list is not circular.
         */
        Node linear = buildLinearList(1, 2, 3);


        for (MethodCase method : methods) {


            Result result =
                    method.algorithm.solve(linear);


            if (!result.valid()) {


                passed++;


                System.out.printf(
                        "PASS I1 - %s rejects a linear list%n",
                        method.name);


            } else {


                failed++;


                System.out.printf(
                        "FAIL I1 - %s accepts a linear list%n",
                        method.name);
            }
        }


        /*
         * A single node whose next reference is null is not circular.
         */
        Node loneLinear = node(10);


        for (MethodCase method : methods) {


            Result result =
                    method.algorithm.solve(loneLinear);


            if (!result.valid()) {


                passed++;


                System.out.printf(
                        "PASS I2 - %s rejects a single non-circular node%n",
                        method.name);


            } else {


                failed++;


                System.out.printf(
                        "FAIL I2 - %s accepts a single non-circular node%n",
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


        List<TestCase> tests = new ArrayList<>();


        /*
         * ============================================================
         * Empty List
         * ============================================================
         */
        tests.add(
                new TestCase(
                        "B1",
                        null,
                        List.of(),
                        true,
                        "empty list"));


        /*
         * ============================================================
         * Single-Node Circular List
         * ============================================================
         *
         *      10 -> 10
         */
        Node single = node(10);
        single.next = single;


        tests.add(
                new TestCase(
                        "B2",
                        single,
                        List.of(10),
                        true,
                        "single-node circular list"));


        /*
         * ============================================================
         * Basic Circular List
         * ============================================================
         *
         *      1 -> 2 -> 3 -> 1
         */
        tests.add(
                new TestCase(
                        "B3",
                        buildCircularList(1, 2, 3),
                        List.of(1, 2, 3),
                        true,
                        "three-node circular list"));


        /*
         * ============================================================
         * Circular List With Negative Values
         * ============================================================
         *
         *      -5 -> 0 -> 5 -> -5
         */
        tests.add(
                new TestCase(
                        "B4",
                        buildCircularList(-5, 0, 5),
                        List.of(-5, 0, 5),
                        true,
                        "negative and zero values"));


        /*
         * ============================================================
         * Longer Circular List
         * ============================================================
         */
        tests.add(
                new TestCase(
                        "B5",
                        buildCircularList(2, 4, 6, 8, 10),
                        List.of(2, 4, 6, 8, 10),
                        true,
                        "five-node circular list"));


        /*
         * ============================================================
         * Linear Lists
         * ============================================================
         */
        tests.add(
                new TestCase(
                        "I1",
                        buildLinearList(1, 2, 3),
                        List.of(),
                        false,
                        "linear list does not return to head"));


        tests.add(
                new TestCase(
                        "I2",
                        node(7),
                        List.of(),
                        false,
                        "single node without a cycle"));


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


        Random random = new Random(20261105L);


        for (int iteration = 1; iteration <= iterations; iteration++) {


            int size = 1 + random.nextInt(30);


            int[] values = new int[size];


            for (int i = 0; i < size; i++) {
                values[i] = random.nextInt(201) - 100;
            }


            Node head = buildCircularList(values);
            List<Integer> expected =
                    List.of(java.util.Arrays.stream(values)
                            .boxed()
                            .toArray(Integer[]::new));


            for (MethodCase method : methods) {


                Result actual =
                        method.algorithm.solve(head);


                if (!actual.valid()
                        || !actual.values().equals(expected)) {


                    System.out.println(
                            "Randomised test FAILED");


                    System.out.println(
                            "iteration = "
                                    + iteration);


                    System.out.println(
                            "algorithm = "
                                    + method.name);


                    System.out.println(
                            "expected = "
                                    + expected);


                    System.out.println(
                            "actual = "
                                    + actual.values());


                    System.out.println(
                            "valid = "
                                    + actual.valid());


                    return;
                }
            }
        }


        System.out.printf(
                "All %d randomised tests passed.%n%n",
                iterations);
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


    public static void main(String[] args) {


        System.out.println(
                "############################################################");


        System.out.println(
                "############  CIRCULAR LINKED LIST TRAVERSAL  ##############");


        System.out.println(
                "############################################################");


        System.out.println();


        List<MethodCase> methods = List.of(


                new MethodCase(
                        "Iterative Traversal",
                        CircularTraversalLinkedList
                                ::circularTraversalIterative),


                new MethodCase(
                        "Recursive Traversal",
                        CircularTraversalLinkedList
                                ::circularTraversalRecursive),


                new MethodCase(
                        "Reference Traversal",
                        CircularTraversalLinkedList
                                ::circularTraversalReference)
        );


        List<TestCase> tests = buildTests();


        /*
         * ============================================================
         * Individual Algorithm Tests
         * ============================================================
         */
        for (MethodCase method : methods) {
            runTests(
                    method,
                    tests);
        }


        /*
         * ============================================================
         * Cross-Algorithm Tests
         * ============================================================
         */
        runCrossCheckTests(
                methods,
                tests);


        /*
         * ============================================================
         * Invalid Input Tests
         * ============================================================
         */
        runInvalidInputTests(
                methods);


        /*
         * ============================================================
         * Randomised Tests
         * ============================================================
         */
        runRandomisedTests(
                methods,
                5000);
    }
}
