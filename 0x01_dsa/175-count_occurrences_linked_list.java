import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Count the occurrences of a value in a singly linked list.
 *
 * Two implementations are provided:
 *
 * 1. Recursive traversal
 *
 *      Traverses from the head towards the tail.
 *
 *      Time:  O(n)
 *      Space: O(n), owing to the call stack
 *
 * 2. Iterative traversal
 *
 *      Walks through the list using a single reference.
 *
 *      Time:  O(n)
 *      Space: O(1)
 *
 * A null head represents an empty list and is valid; it contains zero
 * occurrences of any key.
 */
public class CountOccurrencesLinkedList {


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
    }


    /* **********************************************************************
     * Result
     * **********************************************************************/


    static record Result(
            int count,
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


    static Node list(
            int... values) {


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


    /* **********************************************************************
     * 1. Recursive Traversal
     * **********************************************************************/


    /**
     * Count occurrences using recursion.
     *
     * The method first counts matches in the remainder of the list, then
     * adds one if the current node matches the key.
     *
     * Time:  O(n)
     * Space: O(n), because each node adds a stack frame.
     */
    static Result countOccurrencesRecursion(
            Node head,
            int key) {


        if (!validNode(head)) {
            return new Result(
                    0,
                    true);
        }


        Result remainder =
                countOccurrencesRecursion(
                        head.next,
                        key);


        if (!remainder.valid()) {
            return remainder;
        }


        int count = remainder.count();


        if (head.val == key) {
            count++;
        }


        return new Result(
                count,
                true);
    }


    /* **********************************************************************
     * 2. Iterative Traversal
     * **********************************************************************/


    /**
     * Count occurrences using iteration.
     *
     * This is the preferred implementation for long lists because it uses
     * constant extra space.
     *
     * Time:  O(n)
     * Space: O(1)
     */
    static Result countOccurrencesIteration(
            Node head,
            int key) {


        Node current = head;
        int count = 0;


        while (validNode(current)) {


            if (current.val == key) {
                count++;
            }


            current = current.next;
        }


        return new Result(
                count,
                true);
    }


    /* **********************************************************************
     * Test Harness
     * **********************************************************************/


    static class TestCase {


        final String id;
        final Node head;
        final int key;
        final int expected;
        final String description;


        TestCase(
                String id,
                Node head,
                int key,
                int expected,
                String description) {


            this.id = id;
            this.head = head;
            this.key = key;
            this.expected = expected;
            this.description = description;
        }
    }


    @FunctionalInterface
    interface Algorithm {


        Result solve(
                Node head,
                int key);
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
                                test.head,
                                test.key);


                boolean success =
                        actual.valid()
                                && actual.count()
                                == test.expected;


                if (success) {


                    passed++;


                    System.out.printf(
                            "PASS %s (%s)%n",
                            test.id,
                            test.description);


                    System.out.println(
                            "  count = "
                                    + actual.count());


                } else {


                    failed++;


                    System.out.printf(
                            "FAIL %s (%s)%n",
                            test.id,
                            test.description);


                    System.out.println(
                            "  expected = "
                                    + test.expected);


                    System.out.println(
                            "  actual   = "
                                    + actual.count()
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
                                        test.head,
                                        test.key);


                boolean success =
                        first.valid()
                                && first.count()
                                == test.expected;


                for (int index = 1;
                     index < methods.size();
                     index++) {


                    Result actual =
                            methods.get(index)
                                    .algorithm
                                    .solve(
                                            test.head,
                                            test.key);


                    if (!actual.valid()
                            || actual.count()
                            != first.count()) {


                        success = false;


                        System.out.printf(
                                "  %s = (%d, %s)%n",
                                methods.get(index).name,
                                actual.count(),
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
                new Random(20261109L);


        for (int iteration = 1;
             iteration <= iterations;
             iteration++) {


            int size =
                    random.nextInt(20);


            int[] values =
                    new int[size];


            for (int index = 0;
                 index < size;
                 index++) {


                values[index] =
                        random.nextInt(10);
            }


            Node head =
                    list(values);


            int key =
                    random.nextInt(10);


            int expected = 0;


            for (int value : values) {


                if (value == key) {
                    expected++;
                }
            }


            for (MethodCase method :
                    methods) {


                Result actual =
                        method.algorithm.solve(
                                head,
                                key);


                if (!actual.valid()
                        || actual.count()
                        != expected) {


                    System.out.println(
                            "Randomised test FAILED");


                    System.out.println(
                            "iteration = "
                                    + iteration);


                    System.out.println(
                            "algorithm = "
                                    + method.name);


                    System.out.println(
                            "key = "
                                    + key);


                    System.out.println(
                            "expected = "
                                    + expected);


                    System.out.println(
                            "actual = "
                                    + actual.count());


                    return;
                }
            }
        }


        System.out.printf(
                "All %d randomised tests passed.%n%n",
                iterations);
    }


    /* **********************************************************************
     * Test Data
     * **********************************************************************/


    static List<TestCase> buildTests() {


        List<TestCase> tests =
                new ArrayList<>();


        /*
         * Basic list:
         *
         *      1 -> 2 -> 3 -> 2 -> 4
         */
        Node basic =
                list(
                        1,
                        2,
                        3,
                        2,
                        4);


        tests.add(
                new TestCase(
                        "B1",
                        basic,
                        2,
                        2,
                        "value occurs twice")));


        tests.add(
                new TestCase(
                        "B2",
                        basic,
                        1,
                        1,
                        "value occurs once at the head")));


        tests.add(
                new TestCase(
                        "B3",
                        basic,
                        4,
                        1,
                        "value occurs once at the tail")));


        tests.add(
                new TestCase(
                        "B4",
                        basic,
                        9,
                        0,
                        "value does not occur")));


        /*
         * All matching values.
         */
        Node matching =
                list(
                        7,
                        7,
                        7);


        tests.add(
                new TestCase(
                        "M1",
                        matching,
                        7,
                        3,
                        "every value matches")));


        /*
         * Negative values.
         */
        Node negative =
                list(
                        -3,
                        0,
                        -3,
                        5);


        tests.add(
                new TestCase(
                        "N1",
                        negative,
                        -3,
                        2,
                        "negative values")));


        /*
         * Single-node list.
         */
        tests.add(
                new TestCase(
                        "S1",
                        node(42),
                        42,
                        1,
                        "single matching node")));


        tests.add(
                new TestCase(
                        "S2",
                        node(42),
                        41,
                        0,
                        "single non-matching node")));


        /*
         * Empty list.
         */
        tests.add(
                new TestCase(
                        "E1",
                        null,
                        5,
                        0,
                        "empty list")));


        return tests;
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
                "#############  COUNT OCCURRENCES LINKED LIST  ##############");


        System.out.println(
                "############################################################");


        System.out.println();


        List<MethodCase> methods =
                List.of(


                        new MethodCase(
                                "Recursive Traversal",
                                CountOccurrencesLinkedList
                                        ::countOccurrencesRecursion),


                        new MethodCase(
                                "Iterative Traversal",
                                CountOccurrencesLinkedList
                                        ::countOccurrencesIteration)
                );


        List<TestCase> tests =
                buildTests();


        /*
         * Individual algorithm tests.
         */
        for (MethodCase method :
                methods) {


            runTests(
                    method,
                    tests);
        }


        /*
         * Cross-algorithm tests.
         */
        runCrossCheckTests(
                methods,
                tests);


        /*
         * Randomised tests.
         */
        runRandomisedTests(
                methods,
                5000);
    }
}
