import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Detect whether a singly linked list contains a cycle.
 *
 * A cycle exists when following `next` references eventually returns to
 * a node that has already been visited.
 *
 * Two implementations are provided:
 *
 * 1. HashSet
 *
 *      Records every visited Node reference.
 *
 *      Time:  O(n)
 *      Space: O(n)
 *
 * 2. Floyd's Cycle Detection
 *
 *      Uses a slow pointer, which advances one node per step, and a
 *      fast pointer, which advances two nodes per step.
 *
 *      Time:  O(n)
 *      Space: O(1)
 *
 * Both algorithms compare Node references rather than node values.
 * Therefore, two separate nodes with the same value do not constitute
 * a cycle.
 */
public class DetectLoopLinkedList {


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


        Node(
                int val,
                Node next) {


            this.val = val;
            this.next = next;
        }
    }


    /* **********************************************************************
     * Result
     * **********************************************************************/


    /**
     * Represents the outcome of a cycle-detection request.
     *
     * `loopDetected` is true only when a cycle has been found.
     *
     * `valid` is false only when the supplied head reference is null.
     * An acyclic non-empty list is valid and has `loopDetected = false`.
     */
    static record Result(
            boolean loopDetected,
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


    /* **********************************************************************
     * 1. HashSet
     * **********************************************************************/


    /**
     * Detect a cycle by recording every visited Node reference.
     *
     * If a reference is encountered twice, the list contains a cycle.
     * If traversal reaches null, the list is acyclic.
     */
    static Result detectLoopHashSet(
            Node head) {


        if (!validNode(head)) {


            return new Result(
                    false,
                    false);
        }


        Set<Node> visited =
                new HashSet<>();


        Node current =
                head;


        while (current != null) {


            /*
             * `add` returns false when the exact Node reference has
             * already been stored in the set.
             */
            if (!visited.add(current)) {


                return new Result(
                        true,
                        true);
            }


            current =
                    current.next;
        }


        return new Result(
                false,
                true);
    }


    /* **********************************************************************
     * 2. Floyd's Cycle Detection
     * **********************************************************************/


    /**
     * Detect a cycle using Floyd's tortoise-and-hare algorithm.
     *
     * The slow pointer advances by one node and the fast pointer advances
     * by two nodes. If the list contains a cycle, the fast pointer will
     * eventually meet the slow pointer.
     *
     * If the fast pointer reaches the end of the list, no cycle exists.
     */
    static Result detectLoopFloydAlgo(
            Node head) {


        if (!validNode(head)) {


            return new Result(
                    false,
                    false);
        }


        Node slow =
                head;


        Node fast =
                head;


        /*
         * Both `fast` and `fast.next` must be checked before advancing
         * `fast` by two nodes.
         */
        while (fast != null
                && fast.next != null) {


            slow =
                    slow.next;


            fast =
                    fast.next.next;


            if (slow == fast) {


                return new Result(
                        true,
                        true);
            }
        }


        return new Result(
                false,
                true);
    }


    /* **********************************************************************
     * Test Harness
     * **********************************************************************/


    static class TestCase {


        final String id;
        final Node head;
        final boolean expectedLoopDetected;
        final boolean expectedValid;
        final String description;


        TestCase(
                String id,
                Node head,
                boolean expectedLoopDetected,
                boolean expectedValid,
                String description) {


            this.id = id;
            this.head = head;
            this.expectedLoopDetected = expectedLoopDetected;
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
                        actual.loopDetected()
                                == test.expectedLoopDetected
                                && actual.valid()
                                == test.expectedValid;


                if (success) {


                    passed++;


                    System.out.printf(
                            "PASS %s (%s)%n",
                            test.id,
                            test.description);


                    System.out.println(
                            "  loopDetected = "
                                    + actual.loopDetected());


                } else {


                    failed++;


                    System.out.printf(
                            "FAIL %s (%s)%n",
                            test.id,
                            test.description);


                    System.out.println(
                            "  expected = "
                                    + test.expectedLoopDetected
                                    + ", valid = "
                                    + test.expectedValid);


                    System.out.println(
                            "  actual   = "
                                    + actual.loopDetected()
                                    + ", valid = "
                                    + actual.valid());
                }


            } catch (Exception exception) {


                failed++;


                System.out.printf(
                        "FAIL %s (%s)%n",
                        test.id,
                        test.description);


                System.out.println(
                        "  exception = "
                                + exception);
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
                        first.loopDetected()
                                == test.expectedLoopDetected
                                && first.valid()
                                == test.expectedValid;


                for (int index = 1;
                        index < methods.size();
                        index++) {


                    Result actual =
                            methods.get(index)
                                    .algorithm
                                    .solve(
                                            test.head);


                    if (actual.loopDetected()
                            != first.loopDetected()
                            || actual.valid()
                            != first.valid()) {


                        success = false;


                        System.out.printf(
                                "  %s = (%s, %s)%n",
                                methods.get(index).name,
                                actual.loopDetected(),
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


            } catch (Exception exception) {


                failed++;


                System.out.printf(
                        "FAIL %s (%s)%n",
                        test.id,
                        test.description);


                System.out.println(
                        "  exception = "
                                + exception);
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
         * Null head.
         */
        for (MethodCase method :
                methods) {


            Result result =
                    method.algorithm.solve(
                            null);


            if (!result.valid()
                    && !result.loopDetected()) {


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
         * ============================================================
         * Empty List
         * ============================================================
         */
        tests.add(
                new TestCase(
                        "E1",
                        null,
                        false,
                        false,
                        "null head is invalid"));


        /*
         * ============================================================
         * Single Node
         * ============================================================
         */
        Node single =
                node(10);


        tests.add(
                new TestCase(
                        "S1",
                        single,
                        false,
                        true,
                        "single-node acyclic list"));


        /*
         * ============================================================
         * Simple Acyclic List
         * ============================================================
         *
         *      10 -> 20 -> 30 -> null
         */
        Node acyclicTail =
                node(30);


        Node acyclicMiddle =
                node(
                        20,
                        acyclicTail);


        Node acyclicHead =
                node(
                        10,
                        acyclicMiddle);


        tests.add(
                new TestCase(
                        "A1",
                        acyclicHead,
                        false,
                        true,
                        "three-node acyclic list"));


        /*
         * ============================================================
         * Self-Loop
         * ============================================================
         *
         *      10 -> 10 -> ...
         */
        Node selfLoop =
                node(10);


        selfLoop.next =
                selfLoop;


        tests.add(
                new TestCase(
                        "L1",
                        selfLoop,
                        true,
                        true,
                        "node points to itself"));


        /*
         * ============================================================
         * Cycle Back To Head
         * ============================================================
         *
         *      10 -> 20 -> 30
         *       ^           |
         *       |-----------+
         */
        Node cycleHead =
                node(10);


        Node cycleMiddle =
                node(
                        20,
                        cycleHead);


        Node cycleTail =
                node(
                        30,
                        cycleMiddle);


        cycleHead.next =
                cycleTail;


        tests.add(
                new TestCase(
                        "L2",
                        cycleHead,
                        true,
                        true,
                        "tail points back to head"));


        /*
         * ============================================================
         * Cycle Begins After A Prefix
         * ============================================================
         *
         *      1 -> 2 -> 3 -> 4
         *                 ^    |
         *                 |----+
         */
        Node prefixNode1 =
                node(1);


        Node cycleNode3 =
                node(3);


        Node cycleNode4 =
                node(
                        4,
                        cycleNode3);


        Node prefixNode2 =
                node(
                        2,
                        cycleNode3);


        prefixNode1.next =
                prefixNode2;


        tests.add(
                new TestCase(
                        "L3",
                        prefixNode1,
                        true,
                        true,
                        "cycle begins after a prefix"));


        /*
         * ============================================================
         * Duplicate Values Without A Cycle
         * ============================================================
         *
         *      7 -> 7 -> 7 -> null
         *
         * Equal values do not imply a cycle: the Node references differ.
         */
        Node duplicate3 =
                node(7);


        Node duplicate2 =
                node(
                        7,
                        duplicate3);


        Node duplicate1 =
                node(
                        7,
                        duplicate2);


        tests.add(
                new TestCase(
                        "D1",
                        duplicate1,
                        false,
                        true,
                        "duplicate values are not a cycle"));


        /*
         * ============================================================
         * Duplicate Values With A Cycle
         * ============================================================
         *
         *      7 -> 7 -> 7
         *       ^          |
         *       |----------+
         */
        Node duplicateCycle1 =
                node(7);


        Node duplicateCycle3 =
                node(
                        7,
                        duplicateCycle1);


        Node duplicateCycle2 =
                node(
                        7,
                        duplicateCycle3);


        duplicateCycle1.next =
                duplicateCycle2;


        tests.add(
                new TestCase(
                        "D2",
                        duplicateCycle1,
                        true,
                        true,
                        "duplicate values with a real cycle"));


        /*
         * ============================================================
         * Negative Values
         * ============================================================
         */
        Node negativeTail =
                node(-30);


        Node negativeMiddle =
                node(
                        -20,
                        negativeTail);


        Node negativeHead =
                node(
                        -10,
                        negativeMiddle);


        tests.add(
                new TestCase(
                        "N1",
                        negativeHead,
                        false,
                        true,
                        "negative values without a cycle"));


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
                new Random(20261110L);


        for (int iteration = 1;
                iteration <= iterations;
                iteration++) {


            int size =
                    1 + random.nextInt(30);


            boolean expectedLoopDetected =
                    random.nextBoolean();


            Node head =
                    buildRandomList(
                            random,
                            size,
                            expectedLoopDetected);


            for (MethodCase method :
                    methods) {


                Result result =
                        method.algorithm.solve(
                                head);


                if (!result.valid()
                        || result.loopDetected()
                        != expectedLoopDetected) {


                    System.out.println(
                            "Randomised test FAILED");


                    System.out.println(
                            "iteration = "
                                    + iteration);


                    System.out.println(
                            "algorithm = "
                                    + method.name);


                    System.out.println(
                            "expected loopDetected = "
                                    + expectedLoopDetected);


                    System.out.println(
                            "actual loopDetected = "
                                    + result.loopDetected());


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


    /**
     * Build either an acyclic list or a list with a cycle.
     *
     * When `cycle` is true, the final node points back to the head,
     * producing a cycle of length `size`.
     */
    static Node buildRandomList(
            Random random,
            int size,
            boolean cycle) {


        Node head =
                node(
                        random.nextInt(2001)
                                - 1000);


        Node current =
                head;


        for (int index = 1;
                index < size;
                index++) {


            Node next =
                    node(
                            random.nextInt(2001)
                                    - 1000);


            current.next =
                    next;


            current =
                    next;
        }


        if (cycle) {


            current.next =
                    head;
        }


        return head;
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
                "##############  LINKED LIST CYCLE DETECTION  ###############");


        System.out.println(
                "############################################################");


        System.out.println();


        List<MethodCase> methods =
                List.of(


                        new MethodCase(
                                "HashSet",
                                DetectLoopLinkedList
                                        ::detectLoopHashSet),


                        new MethodCase(
                                "Floyd's Algorithm",
                                DetectLoopLinkedList
                                        ::detectLoopFloydAlgo)
                );


        List<TestCase> tests =
                buildTests();


        /*
         * ============================================================
         * Individual Algorithm Tests
         * ============================================================
         */


        for (MethodCase method :
                methods) {


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
