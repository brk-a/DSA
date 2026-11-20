import java.util.ArrayList;
import java.util.List;

/**
 * Merge two singly linked lists by inserting nodes from the second list
 * into alternate positions of the first list.
 *
 * Given:
 *
 *      first list:  1 -> 2 -> 3
 *      second list: 4 -> 5 -> 6 -> 7
 *
 * the merged list becomes:
 *
 *      1 -> 4 -> 2 -> 5 -> 3 -> 6
 *
 * and the remaining second-list nodes are:
 *
 *      7
 *
 * Two implementations are provided:
 *
 * 1. Auxiliary List
 *
 *      Copies references to the second list into a temporary list,
 *      then inserts them into the first list.
 *
 *      Time:  O(m + n)
 *      Space: O(m)
 *
 * 2. Two Pointers
 *
 *      Merges the lists directly by rewiring their next pointers.
 *
 *      Time:  O(min(m, n))
 *      Space: O(1)
 *
 * Both methods mutate their input lists. The returned first list begins
 * at head1; the returned second list contains any unused nodes from the
 * original second list.
 */
public class MergeAlternatePositionsLinkedList {


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
            List<Node> result,
            boolean valid) {
    }


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


    static Node buildList(int... values) {


        Node head = null;
        Node tail = null;


        for (int value : values) {


            Node newNode = node(value);


            if (head == null) {
                head = newNode;
                tail = newNode;
            } else {
                tail.next = newNode;
                tail = newNode;
            }
        }


        return head;
    }


    static List<Integer> toValues(Node head) {


        List<Integer> values = new ArrayList<>();


        while (head != null) {
            values.add(head.val);
            head = head.next;
        }


        return values;
    }


    /* **********************************************************************
     * 1. Auxiliary List
     * **********************************************************************/


    /**
     * Merge using an auxiliary list of references to the second list.
     *
     * This approach first records every node in the second list. It then
     * inserts those nodes, one at a time, after alternate nodes in the
     * first list.
     *
     * The input lists are mutated.
     */
    static Result mergeAlternatePositionsUsingList(
            Node head1,
            Node head2) {


        if (!validNode(head1) || !validNode(head2)) {
            return new Result(new ArrayList<>(), false);
        }


        List<Node> secondListNodes = new ArrayList<>();


        Node current2 = head2;


        while (current2 != null) {
            secondListNodes.add(current2);
            current2 = current2.next;
        }


        Node current1 = head1;
        int index = 0;


        while (current1 != null && index < secondListNodes.size()) {


            Node next1 = current1.next;
            Node next2 = secondListNodes.get(index).next;


            /*
             * Insert the current node from the second list immediately
             * after the current node from the first list.
             */
            current1.next = secondListNodes.get(index);
            secondListNodes.get(index).next = next1;


            current1 = next1;


            /*
             * The next unused node from the second list is the node that
             * followed the node just inserted.
             */
            secondListNodes.set(index, next2);


            index++;
        }


        Node remainingSecondList = null;


        if (index < secondListNodes.size()) {
            remainingSecondList = secondListNodes.get(index);
        }


        List<Node> result = new ArrayList<>();
        result.add(head1);
        result.add(remainingSecondList);


        return new Result(result, true);
    }


    /* **********************************************************************
     * 2. Two Pointers
     * **********************************************************************/


    /**
     * Merge using two pointers and constant extra space.
     *
     * At each step, the current node from the second list is inserted
     * immediately after the current node from the first list.
     *
     * The input lists are mutated.
     */
    static Result mergeAlternatePositionsTwoPointers(
            Node head1,
            Node head2) {


        if (!validNode(head1) || !validNode(head2)) {
            return new Result(new ArrayList<>(), false);
        }


        Node current1 = head1;
        Node current2 = head2;


        while (current1 != null && current2 != null) {


            Node next1 = current1.next;
            Node next2 = current2.next;


            /*
             * Insert the current second-list node after the current
             * first-list node.
             */
            current2.next = next1;
            current1.next = current2;


            current1 = next1;
            current2 = next2;
        }


        List<Node> result = new ArrayList<>();
        result.add(head1);
        result.add(current2);


        return new Result(result, true);
    }


    /* **********************************************************************
     * Test Harness
     * **********************************************************************/


    @FunctionalInterface
    interface Algorithm {


        Result solve(Node head1, Node head2);
    }


    static class MethodCase {


        final String name;
        final Algorithm algorithm;


        MethodCase(String name, Algorithm algorithm) {
            this.name = name;
            this.algorithm = algorithm;
        }
    }


    static class TestCase {


        final String id;
        final int[] firstList;
        final int[] secondList;
        final List<Integer> expectedMerged;
        final List<Integer> expectedRemaining;
        final boolean expectedValid;
        final String description;


        TestCase(
                String id,
                int[] firstList,
                int[] secondList,
                List<Integer> expectedMerged,
                List<Integer> expectedRemaining,
                boolean expectedValid,
                String description) {


            this.id = id;
            this.firstList = firstList;
            this.secondList = secondList;
            this.expectedMerged = expectedMerged;
            this.expectedRemaining = expectedRemaining;
            this.expectedValid = expectedValid;
            this.description = description;
        }
    }


    /* **********************************************************************
     * Test Runner
     * **********************************************************************/


    static void runTests(
            MethodCase method,
            List<TestCase> tests) {


        printSeparator();
        System.out.println(method.name);
        printSeparator();


        int passed = 0;
        int failed = 0;


        for (TestCase test : tests) {


            try {


                Node first = buildList(test.firstList);
                Node second = buildList(test.secondList);


                Result actual = method.algorithm.solve(first, second);


                boolean success =
                        actual.valid() == test.expectedValid;


                if (success && actual.valid()) {


                    List<Integer> merged =
                            toValues(actual.result().get(0));


                    List<Integer> remaining =
                            toValues(actual.result().get(1));


                    success =
                            merged.equals(test.expectedMerged)
                                    && remaining.equals(test.expectedRemaining);
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


                    if (actual.valid()) {


                        System.out.println(
                                "  expected merged    = "
                                        + test.expectedMerged);


                        System.out.println(
                                "  actual merged      = "
                                        + toValues(actual.result().get(0)));


                        System.out.println(
                                "  expected remaining = "
                                        + test.expectedRemaining);


                        System.out.println(
                                "  actual remaining   = "
                                        + toValues(actual.result().get(1)));


                    } else {


                        System.out.println(
                                "  expected valid = "
                                        + test.expectedValid);


                        System.out.println(
                                "  actual valid   = "
                                        + actual.valid());
                    }
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


        printResults(passed, failed, tests.size());
    }


    /* **********************************************************************
     * Invalid Input Tests
     * **********************************************************************/


    static void runInvalidInputTests(List<MethodCase> methods) {


        printSeparator();
        System.out.println("Invalid Input Tests");
        printSeparator();


        int passed = 0;
        int failed = 0;


        Node validList = buildList(1, 2, 3);


        for (MethodCase method : methods) {


            Result nullFirst =
                    method.algorithm.solve(null, validList);


            if (!nullFirst.valid()) {
                passed++;


                System.out.printf(
                        "PASS I1 - %s rejects null first list%n",
                        method.name);


            } else {
                failed++;


                System.out.printf(
                        "FAIL I1 - %s accepts null first list%n",
                        method.name);
            }


            Result nullSecond =
                    method.algorithm.solve(validList, null);


            if (!nullSecond.valid()) {
                passed++;


                System.out.printf(
                        "PASS I2 - %s rejects null second list%n",
                        method.name);


            } else {
                failed++;


                System.out.printf(
                        "FAIL I2 - %s accepts null second list%n",
                        method.name);
            }
        }


        printResults(passed, failed, passed + failed);
    }


    /* **********************************************************************
     * Test Data
     * **********************************************************************/


    static List<TestCase> buildTests() {


        List<TestCase> tests = new ArrayList<>();


        /*
         * Equal-length lists.
         *
         * First:  1 -> 2 -> 3
         * Second: 4 -> 5 -> 6
         *
         * Merged:   1 -> 4 -> 2 -> 5 -> 3 -> 6
         * Remaining: none
         */
        tests.add(new TestCase(
                "B1",
                new int[] {1, 2, 3},
                new int[] {4, 5, 6},
                List.of(1, 4, 2, 5, 3, 6),
                List.of(),
                true,
                "equal-length lists"));


        /*
         * Second list is longer.
         *
         * First:  1 -> 2 -> 3
         * Second: 4 -> 5 -> 6 -> 7 -> 8
         *
         * Merged:   1 -> 4 -> 2 -> 5 -> 3 -> 6
         * Remaining: 7 -> 8
         */
        tests.add(new TestCase(
                "B2",
                new int[] {1, 2, 3},
                new int[] {4, 5, 6, 7, 8},
                List.of(1, 4, 2, 5, 3, 6),
                List.of(7, 8),
                true,
                "second list is longer"));


        /*
         * First list is longer.
         *
         * First:  1 -> 2 -> 3 -> 4 -> 5
         * Second: 10 -> 20
         *
         * Merged:   1 -> 10 -> 2 -> 20 -> 3 -> 4 -> 5
         * Remaining: none
         */
        tests.add(new TestCase(
                "B3",
                new int[] {1, 2, 3, 4, 5},
                new int[] {10, 20},
                List.of(1, 10, 2, 20, 3, 4, 5),
                List.of(),
                true,
                "first list is longer"));


        /*
         * Single-node lists.
         */
        tests.add(new TestCase(
                "B4",
                new int[] {1},
                new int[] {2},
                List.of(1, 2),
                List.of(),
                true,
                "single-node lists"));


        /*
         * Negative values.
         */
        tests.add(new TestCase(
                "N1",
                new int[] {-3, -1},
                new int[] {-4, -2, 0},
                List.of(-3, -4, -1, -2),
                List.of(0),
                true,
                "negative and zero values"));


        return tests;
    }


    /* **********************************************************************
     * Randomised Tests
     * **********************************************************************/


    static void runRandomisedTests(
            List<MethodCase> methods,
            int iterations) {


        printSeparator();
        System.out.println("Randomised Cross Checks");
        printSeparator();


        java.util.Random random = new java.util.Random(20261103L);


        for (int iteration = 1; iteration <= iterations; iteration++) {


            int firstSize = 1 + random.nextInt(10);
            int secondSize = 1 + random.nextInt(15);


            int[] firstValues = new int[firstSize];
            int[] secondValues = new int[secondSize];


            for (int i = 0; i < firstSize; i++) {
                firstValues[i] = random.nextInt(100);
            }


            for (int i = 0; i < secondSize; i++) {
                secondValues[i] = 100 + random.nextInt(100);
            }


            List<Integer> expectedMerged = new ArrayList<>();
            List<Integer> expectedRemaining = new ArrayList<>();


            int firstIndex = 0;
            int secondIndex = 0;


            while (firstIndex < firstSize
                    && secondIndex < secondSize) {


                expectedMerged.add(firstValues[firstIndex++]);
                expectedMerged.add(secondValues[secondIndex++]);
            }


            while (firstIndex < firstSize) {
                expectedMerged.add(firstValues[firstIndex++]);
            }


            while (secondIndex < secondSize) {
                expectedRemaining.add(secondValues[secondIndex++]);
            }


            TestCase test = new TestCase(
                    "R" + iteration,
                    firstValues,
                    secondValues,
                    expectedMerged,
                    expectedRemaining,
                    true,
                    "randomised cross-check");


            for (MethodCase method : methods) {


                Node first = buildList(test.firstList);
                Node second = buildList(test.secondList);


                Result actual = method.algorithm.solve(first, second);


                List<Integer> merged =
                        toValues(actual.result().get(0));


                List<Integer> remaining =
                        toValues(actual.result().get(1));


                if (!actual.valid()
                        || !merged.equals(expectedMerged)
                        || !remaining.equals(expectedRemaining)) {


                    System.out.println("Randomised test FAILED");
                    System.out.println("iteration = " + iteration);
                    System.out.println("algorithm = " + method.name);
                    System.out.println("expected merged = " + expectedMerged);
                    System.out.println("actual merged   = " + merged);
                    System.out.println("expected remaining = " + expectedRemaining);
                    System.out.println("actual remaining   = " + remaining);


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
                "########  MERGE ALTERNATE POSITIONS LINKED LIST  ##########");


        System.out.println(
                "############################################################");
        System.out.println();


        List<MethodCase> methods = List.of(
                new MethodCase(
                        "Auxiliary List",
                        MergeAlternatePositionsLinkedList
                                ::mergeAlternatePositionsUsingList),


                new MethodCase(
                        "Two Pointers",
                        MergeAlternatePositionsLinkedList
                                ::mergeAlternatePositionsTwoPointers));


        List<TestCase> tests = buildTests();


        for (MethodCase method : methods) {
            runTests(method, tests);
        }


        runInvalidInputTests(methods);
        runRandomisedTests(methods, 5000);
    }
}
