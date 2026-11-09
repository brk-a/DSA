import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Pairwise swap of adjacent nodes in a singly linked list.
 *
 * Given:
 *
 *      1 -> 2 -> 3 -> 4 -> 5
 *
 * the expected result is:
 *
 *      2 -> 1 -> 4 -> 3 -> 5
 *
 * Two implementations are provided:
 *
 * 1. Swap Values
 *
 *      Swaps the integer values held by adjacent nodes.
 *
 *      Time:  O(n)
 *      Space: O(1)
 *
 * 2. Swap Links
 *
 *      Rewires the node links, leaving node values unchanged.
 *
 *      Time:  O(n)
 *      Space: O(1)
 *
 * A dummy head node is used by the link-swapping implementation so that
 * the first pair requires no special case.
 */
public class PairwiseLinkedList {


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


    static record Result(
            Node head,
            boolean valid) {
    }


    /* **********************************************************************
     * Helpers
     * **********************************************************************/


    static boolean validNode(
            Node head) {


        return head != null;
    }


    static Node node(
            int value) {


        return new Node(value);
    }


    static Node buildList(
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


    static List<Integer> toValues(
            Node head) {


        List<Integer> values =
                new ArrayList<>();


        Node current = head;


        while (current != null) {


            values.add(current.val);
            current = current.next;
        }


        return values;
    }


    static boolean sameValues(
            Node head,
            int... expected) {


        List<Integer> actual =
                toValues(head);


        if (actual.size() != expected.length) {
            return false;
        }


        for (int i = 0; i < expected.length; i++) {


            if (actual.get(i) != expected[i]) {
                return false;
            }
        }


        return true;
    }


    /* **********************************************************************
     * 1. Swap Values
     * **********************************************************************/


    /**
     * Swap the values of every adjacent pair of nodes.
     *
     * The list structure is unchanged; only the values are exchanged.
     *
     * For example:
     *
     *      1 -> 2 -> 3 -> 4 -> 5
     *
     * becomes:
     *
     *      2 -> 1 -> 4 -> 3 -> 5
     */
    static Result pairwiseLinkedListSwapValues(
            Node head) {


        if (!validNode(head)) {
            return new Result(
                    null,
                    false);
        }


        Node current = head;


        while (current != null
                && current.next != null) {


            int temporary = current.val;


            current.val = current.next.val;
            current.next.val = temporary;


            /*
             * Move two nodes forward because both nodes in the current
             * pair have already been processed.
             */
            current = current.next.next;
        }


        return new Result(
                head,
                true);
    }


    /* **********************************************************************
     * 2. Swap Links
     * **********************************************************************/


    /**
     * Swap every adjacent pair of nodes by changing links.
     *
     * A dummy node precedes the original head. For each pair:
     *
     *      previous -> first -> second -> remainder
     *
     * the links become:
     *
     *      previous -> second -> first -> remainder
     *
     * The dummy node removes the special case for the first pair.
     */
    static Result pairwiseLinkedListSwapLinks(
            Node head) {


        if (!validNode(head)) {
            return new Result(
                    null,
                    false);
        }


        Node dummy = new Node(0);
        dummy.next = head;


        Node previous = dummy;


        while (previous.next != null
                && previous.next.next != null) {


            Node first = previous.next;
            Node second = first.next;


            /*
             * Preserve the remainder of the list before rewiring.
             */
            Node remainder = second.next;


            /*
             * Reverse the current pair.
             */
            first.next = remainder;
            second.next = first;


            /*
             * Connect the preceding node to the new first node.
             */
            previous.next = second;


            /*
             * The original first node is now the final node of the
             * swapped pair, so it precedes the next pair.
             */
            previous = first;
        }


        return new Result(
                dummy.next,
                true);
    }


    /* **********************************************************************
     * Test Harness
     * **********************************************************************/


    static class TestCase {


        final String id;
        final Node input;
        final int[] expected;
        final String description;


        TestCase(
                String id,
                Node input,
                int[] expected,
                String description) {


            this.id = id;
            this.input = input;
            this.expected = expected;
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
                                test.input);


                boolean success =
                        actual.valid()
                                && sameValues(
                                        actual.head(),
                                        test.expected);


                if (success) {


                    passed++;


                    System.out.printf(
                            "PASS %s (%s)%n",
                            test.id,
                            test.description);


                    System.out.println(
                            "  result = "
                                    + toValues(actual.head()));


                } else {


                    failed++;


                    System.out.printf(
                            "FAIL %s (%s)%n",
                            test.id,
                            test.description);


                    System.out.println(
                            "  expected = "
                                    + java.util.Arrays.toString(test.expected));


                    System.out.println(
                            "  actual   = "
                                    + (actual.valid()
                                            ? toValues(actual.head())
                                            : "invalid result"));
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


        for (MethodCase method : methods) {


            Result result =
                    method.algorithm.solve(
                            null);


            if (!result.valid()) {


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
         * Even number of nodes.
         */
        tests.add(
                new TestCase(
                        "B1",
                        buildList(1, 2, 3, 4),
                        new int[]{2, 1, 4, 3},
                        "even-length list"));


        /*
         * Odd number of nodes.
         *
         * The final unpaired node remains in place.
         */
        tests.add(
                new TestCase(
                        "B2",
                        buildList(1, 2, 3, 4, 5),
                        new int[]{2, 1, 4, 3, 5},
                        "odd-length list"));


        /*
         * Two nodes.
         */
        tests.add(
                new TestCase(
                        "B3",
                        buildList(10, 20),
                        new int[]{20, 10},
                        "two-node list"));


        /*
         * Single node.
         */
        tests.add(
                new TestCase(
                        "B4",
                        buildList(7),
                        new int[]{7},
                        "single-node list"));


        /*
         * Negative and duplicate values.
         */
        tests.add(
                new TestCase(
                        "B5",
                        buildList(-1, -1, 0, 2),
                        new int[]{-1, -1, 2, 0},
                        "negative and duplicate values"));


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
                new Random(20260925L);


        for (int iteration = 1;
                iteration <= iterations;
                iteration++) {


            int size =
                    random.nextInt(20);


            int[] values =
                    new int[size];


            for (int i = 0; i < size; i++) {


                values[i] =
                        random.nextInt(201)
                                - 100;
            }


            Node input =
                    buildList(values);


            int[] expected =
                    values.clone();


            for (int i = 0; i + 1 < expected.length; i += 2) {


                int temporary = expected[i];
                expected[i] = expected[i + 1];
                expected[i + 1] = temporary;
            }


            for (MethodCase method : methods) {


                Result result =
                        method.algorithm.solve(
                                input);


                if (!result.valid()
                        || !sameValues(
                                result.head(),
                                expected)) {


                    System.out.println(
                            "Randomised test FAILED");


                    System.out.println(
                            "iteration = "
                                    + iteration);


                    System.out.println(
                            "algorithm = "
                                    + method.name);


                    System.out.println(
                            "input = "
                                    + java.util.Arrays.toString(values));


                    System.out.println(
                            "expected = "
                                    + java.util.Arrays.toString(expected));


                    System.out.println(
                            "actual = "
                                    + (result.valid()
                                            ? toValues(result.head())
                                            : "invalid result"));


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


    public static void main(
            String[] args) {


        System.out.println(
                "############################################################");


        System.out.println(
                "##############  PAIRWISE LINKED LIST SWAP  #################");


        System.out.println(
                "############################################################");


        System.out.println();


        List<MethodCase> methods =
                List.of(


                        new MethodCase(
                                "Swap Values",
                                PairwiseLinkedList
                                        ::pairwiseLinkedListSwapValues),


                        new MethodCase(
                                "Swap Links",
                                PairwiseLinkedList
                                        ::pairwiseLinkedListSwapLinks)
                );


        List<TestCase> tests =
                buildTests();


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
