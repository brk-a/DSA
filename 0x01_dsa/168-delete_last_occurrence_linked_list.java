import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Delete the last occurrence of a given value in a singly linked list.
 *
 * Two implementations are provided:
 *
 * 1. Last Occurrence Tracking
 *
 *      Traverse the list and remember:
 *
 *          - The last node containing the target.
 *          - The node immediately preceding it.
 *
 *      Once traversal finishes, unlink the last occurrence.
 *
 *      Time:  O(n)
 *      Space: O(1)
 *
 * 2. Dummy Node
 *
 *      Introduce a sentinel node before the original head.
 *
 *      Traverse the list while remembering the node immediately
 *      preceding the last occurrence of the target.
 *
 *      The sentinel simplifies deletion because the head can be
 *      treated in exactly the same way as any other node.
 *
 *      Time:  O(n)
 *      Space: O(1)
 *
 * API contract:
 *
 *      - A null head is invalid.
 *      - A non-null list is valid, even if the target is absent.
 *      - If the target is absent, the list remains unchanged.
 *      - Only the last occurrence of the target is deleted.
 *      - The original list is modified in place.
 *
 * Both implementations return the updated head and a validity flag.
 */
public class DeleteLastOccurrenceLinkedList {

    /* **********************************************************************
     * Node
     * **********************************************************************/

    static class Node {

        int data;
        Node next;

        Node(int data) {
            this.data = data;
        }

        Node(
                int data,
                Node next) {

            this.data = data;
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

    /**
     * Validate the list head.
     */
    static boolean validNode(
            Node head) {

        return head != null;
    }

    /**
     * Create a node.
     */
    static Node node(
            int value) {

        return new Node(value);
    }

    /**
     * Create a node with a specified next node.
     */
    static Node node(
            int value,
            Node next) {

        return new Node(
                value,
                next);
    }

    /**
     * Build a linked list from an integer array.
     */
    static Node buildList(
            int[] values) {

        if (values == null
                || values.length == 0) {

            return null;
        }

        Node head = null;
        Node tail = null;

        for (int value : values) {

            Node current =
                    node(value);

            if (head == null) {

                head = current;

            } else {

                tail.next = current;
            }

            tail = current;
        }

        return head;
    }

    /**
     * Convert a linked list into an integer array.
     *
     * Used by the test suite to compare actual and expected results.
     */
    static int[] toArray(
            Node head) {

        ArrayList<Integer> values =
                new ArrayList<>();

        Node current = head;

        while (current != null) {

            values.add(
                    current.data);

            current = current.next;
        }

        int[] result =
                new int[values.size()];

        for (int i = 0;
             i < values.size();
             i++) {

            result[i] =
                    values.get(i);
        }

        return result;
    }

    /**
     * Return a readable representation of the linked list.
     */
    static String display(
            Node head) {

        StringBuilder result =
                new StringBuilder();

        Node current = head;

        while (current != null) {

            result.append(
                    current.data);

            if (current.next != null) {

                result.append(" -> ");
            }

            current = current.next;
        }

        result.append(" -> null");

        return result.toString();
    }

    /* **********************************************************************
     * 1. Last Occurrence Tracking
     * **********************************************************************/

    /**
     * Delete the last occurrence of key using last occurrence tracking.
     *
     * Maintain two references:
     *
     *      last:
     *          The most recently encountered node containing key.
     *
     *      lastPrev:
     *          The node immediately preceding last.
     *
     * During traversal, every matching node replaces the previous
     * candidate.
     *
     * After traversal:
     *
     *      Case 1: No matching node exists.
     *          Return the original list.
     *
     *      Case 2: The last occurrence is the head.
     *          Move head to head.next.
     *
     *      Case 3: The last occurrence is elsewhere.
     *          Bypass the matching node.
     */
    static Result deleteLastOccurrenceTracking(
            Node head,
            int key) {

        if (!validNode(head)) {

            return new Result(
                    null,
                    false);
        }

        Node last = null;
        Node lastPrev = null;

        Node current = head;
        Node previous = null;

        /*
         * Traverse the entire list.
         *
         * Remember the last occurrence of key and its predecessor.
         */
        while (current != null) {

            if (current.data == key) {

                last = current;
                lastPrev = previous;
            }

            previous = current;
            current = current.next;
        }

        /*
         * The target does not exist.
         *
         * The input list is valid, so return it unchanged.
         */
        if (last == null) {

            return new Result(
                    head,
                    true);
        }

        /*
         * The last occurrence is not the head.
         *
         * Bypass the node to be deleted.
         */
        if (lastPrev != null) {

            lastPrev.next =
                    last.next;

        } else {

            /*
             * The last occurrence is the head.
             *
             * Update the head reference.
             */
            head = head.next;
        }

        return new Result(
                head,
                true);
    }

    /* **********************************************************************
     * 2. Dummy Node
     * **********************************************************************/

    /**
     * Delete the last occurrence of key using a dummy node.
     *
     * The dummy node is positioned immediately before the original head.
     *
     * This eliminates the need for a special deletion case when the
     * head itself contains the target.
     *
     * Maintain:
     *
     *      lastPrev:
     *          The node immediately preceding the last occurrence.
     *
     *      current:
     *          The node currently being examined.
     *
     * Every time key is encountered, update lastPrev.
     *
     * After traversal, unlink the last occurrence if one was found.
     *
     * The updated head is dummy.next.
     */
    static Result deleteLastOccurrenceDummyNode(
            Node head,
            int key) {

        if (!validNode(head)) {

            return new Result(
                    null,
                    false);
        }

        /*
         * Introduce a sentinel before the original head.
         */
        Node dummy =
                node(0, head);

        Node lastPrev = null;

        Node current =
                dummy;

        /*
         * Traverse the entire list.
         *
         * Remember the predecessor of the last matching node.
         */
        while (current.next != null) {

            if (current.next.data == key) {

                lastPrev = current;
            }

            current = current.next;
        }

        /*
         * If a matching node was found, bypass it.
         */
        if (lastPrev != null) {

            lastPrev.next =
                    lastPrev.next.next;
        }

        /*
         * dummy.next is the updated head.
         *
         * This works whether or not the original head was deleted.
         */
        return new Result(
                dummy.next,
                true);
    }

    /* **********************************************************************
     * Reference Implementation
     * **********************************************************************/

    /**
     * Independent reference implementation for testing.
     *
     * Convert the list into an array, locate the final occurrence,
     * remove it and return the expected array.
     *
     * This implementation is deliberately less efficient than the
     * production algorithms.
     *
     * It is used only as a testing oracle.
     */
    static int[] referenceDelete(
            int[] values,
            int key) {

        if (values == null) {
            return null;
        }

        int lastIndex = -1;

        for (int i = 0;
             i < values.length;
             i++) {

            if (values[i] == key) {

                lastIndex = i;
            }
        }

        /*
         * No occurrence found.
         */
        if (lastIndex == -1) {

            return Arrays.copyOf(
                    values,
                    values.length);
        }

        int[] result =
                new int[values.length - 1];

        int resultIndex = 0;

        for (int i = 0;
             i < values.length;
             i++) {

            if (i != lastIndex) {

                result[resultIndex++] =
                        values[i];
            }
        }

        return result;
    }

    /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    static class TestCase {

        final String id;
        final int[] values;
        final int key;
        final int[] expected;
        final boolean expectedValid;
        final String description;

        TestCase(
                String id,
                int[] values,
                int key,
                int[] expected,
                boolean expectedValid,
                String description) {

            this.id = id;
            this.values = values;
            this.key = key;
            this.expected = expected;
            this.expectedValid = expectedValid;
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

                /*
                 * Create a fresh list for every test.
                 *
                 * This is important because the algorithms modify
                 * the input list in place.
                 */
                Node head =
                        buildList(
                                test.values);

                Result actual =
                        method.algorithm.solve(
                                head,
                                test.key);

                int[] actualValues =
                        toArray(
                                actual.head());

                boolean success =
                        actual.valid()
                                == test.expectedValid
                                && Arrays.equals(
                                        actualValues,
                                        test.expected);

                if (success) {

                    passed++;

                    System.out.printf(
                            "PASS %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  result = "
                                    + Arrays.toString(
                                            actualValues));

                } else {

                    failed++;

                    System.out.printf(
                            "FAIL %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  input    = "
                                    + Arrays.toString(
                                            test.values));

                    System.out.println(
                            "  key      = "
                                    + test.key);

                    System.out.println(
                            "  expected = "
                                    + Arrays.toString(
                                            test.expected));

                    System.out.println(
                            "  actual   = "
                                    + Arrays.toString(
                                            actualValues));

                    System.out.println(
                            "  valid    = "
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

                Result first = null;

                int[] firstValues = null;

                boolean success = true;

                for (MethodCase method : methods) {

                    /*
                     * Each algorithm receives its own independent list.
                     */
                    Node head =
                            buildList(
                                    test.values);

                    Result actual =
                            method.algorithm.solve(
                                    head,
                                    test.key);

                    int[] actualValues =
                            toArray(
                                    actual.head());

                    if (first == null) {

                        first = actual;
                        firstValues = actualValues;

                    } else {

                        if (actual.valid()
                                != first.valid()
                                || !Arrays.equals(
                                        actualValues,
                                        firstValues)) {

                            success = false;

                            System.out.printf(
                                    "  %s = %s, valid = %s%n",
                                    method.name,
                                    Arrays.toString(
                                            actualValues),
                                    actual.valid());
                        }
                    }

                    /*
                     * Also compare against the expected result.
                     */
                    if (actual.valid()
                            != test.expectedValid
                            || !Arrays.equals(
                                    actualValues,
                                    test.expected)) {

                        success = false;
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
         * Null head.
         *
         * According to the API contract, a null head is invalid.
         */
        for (MethodCase method : methods) {

            Result result =
                    method.algorithm.solve(
                            null,
                            10);

            if (!result.valid()
                    && result.head() == null) {

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
         * Basic List
         * ============================================================
         *
         *      10 -> 20 -> 30 -> 40 -> null
         *
         * Delete 20.
         *
         * Expected:
         *
         *      10 -> 30 -> 40 -> null
         */

        tests.add(
                new TestCase(
                        "B1",
                        new int[]{
                                10, 20, 30, 40
                        },
                        20,
                        new int[]{
                                10, 30, 40
                        },
                        true,
                        "delete middle node"));

        /*
         * ============================================================
         * Delete Head
         * ============================================================
         *
         *      10 -> 20 -> 30
         *
         * Delete 10.
         *
         * Expected:
         *
         *      20 -> 30
         */

        tests.add(
                new TestCase(
                        "B2",
                        new int[]{
                                10, 20, 30
                        },
                        10,
                        new int[]{
                                20, 30
                        },
                        true,
                        "delete head"));

        /*
         * ============================================================
         * Delete Tail
         * ============================================================
         *
         *      10 -> 20 -> 30
         *
         * Delete 30.
         *
         * Expected:
         *
         *      10 -> 20
         */

        tests.add(
                new TestCase(
                        "B3",
                        new int[]{
                                10, 20, 30
                        },
                        30,
                        new int[]{
                                10, 20
                        },
                        true,
                        "delete tail"));

        /*
         * ============================================================
         * Multiple Occurrences
         * ============================================================
         *
         *      5 -> 2 -> 5 -> 3 -> 5
         *
         * Delete the last occurrence of 5.
         *
         * Expected:
         *
         *      5 -> 2 -> 5 -> 3
         */

        tests.add(
                new TestCase(
                        "M1",
                        new int[]{
                                5, 2, 5, 3, 5
                        },
                        5,
                        new int[]{
                                5, 2, 5, 3
                        },
                        true,
                        "multiple occurrences"));

        /*
         * ============================================================
         * Consecutive Occurrences
         * ============================================================
         *
         *      1 -> 2 -> 2 -> 2 -> 3
         *
         * Delete the last occurrence of 2.
         *
         * Expected:
         *
         *      1 -> 2 -> 2 -> 3
         */

        tests.add(
                new TestCase(
                        "M2",
                        new int[]{
                                1, 2, 2, 2, 3
                        },
                        2,
                        new int[]{
                                1, 2, 2, 3
                        },
                        true,
                        "consecutive occurrences"));

        /*
         * ============================================================
         * Target Not Found
         * ============================================================
         *
         *      1 -> 2 -> 3 -> 4
         *
         * Delete 99.
         *
         * Expected: unchanged.
         */

        tests.add(
                new TestCase(
                        "N1",
                        new int[]{
                                1, 2, 3, 4
                        },
                        99,
                        new int[]{
                                1, 2, 3, 4
                        },
                        true,
                        "target not found"));

        /*
         * ============================================================
         * Single Node: Target Found
         * ============================================================
         *
         *      10
         *
         * Delete 10.
         *
         * Expected: empty list.
         */

        tests.add(
                new TestCase(
                        "S1",
                        new int[]{
                                10
                        },
                        10,
                        new int[]{},
                        true,
                        "single-node list"));

        /*
         * ============================================================
         * Single Node: Target Not Found
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "S2",
                        new int[]{
                                10
                        },
                        20,
                        new int[]{
                                10
                        },
                        true,
                        "single-node target absent"));

        /*
         * ============================================================
         * Two Nodes: Delete Head
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "T1",
                        new int[]{
                                10, 20
                        },
                        10,
                        new int[]{
                                20
                        },
                        true,
                        "two-node delete head"));

        /*
         * ============================================================
         * Two Nodes: Delete Tail
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "T2",
                        new int[]{
                                10, 20
                        },
                        20,
                        new int[]{
                                10
                        },
                        true,
                        "two-node delete tail"));

        /*
         * ============================================================
         * All Nodes Match
         * ============================================================
         *
         *      7 -> 7 -> 7 -> 7
         *
         * Delete the last 7.
         *
         * Expected:
         *
         *      7 -> 7 -> 7
         */

        tests.add(
                new TestCase(
                        "M3",
                        new int[]{
                                7, 7, 7, 7
                        },
                        7,
                        new int[]{
                                7, 7, 7
                        },
                        true,
                        "all nodes contain target"));

        /*
         * ============================================================
         * Negative Values
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "V1",
                        new int[]{
                                -10, -5, -10, 0, 10
                        },
                        -10,
                        new int[]{
                                -10, -5, 0, 10
                        },
                        true,
                        "negative target"));

        /*
         * ============================================================
         * Zero Values
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "V2",
                        new int[]{
                                0, 1, 0, 2, 0
                        },
                        0,
                        new int[]{
                                0, 1, 0, 2
                        },
                        true,
                        "zero as target"));

        /*
         * ============================================================
         * Integer Boundaries
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "V3",
                        new int[]{
                                Integer.MIN_VALUE,
                                0,
                                Integer.MAX_VALUE,
                                Integer.MIN_VALUE
                        },
                        Integer.MIN_VALUE,
                        new int[]{
                                Integer.MIN_VALUE,
                                0,
                                Integer.MAX_VALUE
                        },
                        true,
                        "integer boundary values"));

        /*
         * ============================================================
         * Delete Every Node
         * ============================================================
         *
         *      5 -> 5
         *
         * Delete the last occurrence.
         *
         * Expected:
         *
         *      5
         */

        tests.add(
                new TestCase(
                        "M4",
                        new int[]{
                                5, 5
                        },
                        5,
                        new int[]{
                                5
                        },
                        true,
                        "two matching nodes"));

        return tests;
    }

    /* **********************************************************************
     * Randomised Test Data
     * **********************************************************************/

    static int[] randomArray(
            Random random,
            int size) {

        int[] values =
                new int[size];

        for (int i = 0;
             i < size;
             i++) {

            /*
             * A small value range deliberately creates duplicates.
             */
            values[i] =
                    random.nextInt(21) - 10;
        }

        return values;
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

        int passed = 0;
        int failed = 0;

        for (int iteration = 1;
             iteration <= iterations;
             iteration++) {

            /*
             * Generate a non-empty linked list.
             */
            int size =
                    1 + random.nextInt(100);

            int[] values =
                    randomArray(
                            random,
                            size);

            /*
             * Generate a target.
             *
             * Sometimes choose an existing value.
             * Sometimes choose a value outside the generated range.
             */
            int key;

            if (random.nextBoolean()) {

                key = values[
                        random.nextInt(
                                values.length)];

            } else {

                key =
                        random.nextInt(41) + 100;
            }

            /*
             * Calculate the expected answer independently.
             */
            int[] expected =
                    referenceDelete(
                            values,
                            key);

            boolean success = true;

            for (MethodCase method : methods) {

                Node head =
                        buildList(
                                values);

                Result actual =
                        method.algorithm.solve(
                                head,
                                key);

                int[] actualValues =
                        toArray(
                                actual.head());

                if (!actual.valid()
                        || !Arrays.equals(
                                expected,
                                actualValues)) {

                    success = false;

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
                                    + Arrays.toString(
                                            values));

                    System.out.println(
                            "key = "
                                    + key);

                    System.out.println(
                            "expected = "
                                    + Arrays.toString(
                                            expected));

                    System.out.println(
                            "actual = "
                                    + Arrays.toString(
                                            actualValues));

                    System.out.println(
                            "valid = "
                                    + actual.valid());

                    break;
                }
            }

            if (success) {

                passed++;

            } else {

                failed++;

                /*
                 * Stop immediately after the first failure.
                 */
                break;
            }
        }

        printResults(
                passed,
                failed,
                passed + failed);
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
                "########  DELETE LAST OCCURRENCE LINKED LIST  ##############");

        System.out.println(
                "############################################################");

        System.out.println();

        /*
         * Register the implementations.
         */
        List<MethodCase> methods =
                List.of(

                        new MethodCase(
                                "Last Occurrence Tracking",
                                DeleteLastOccurrenceLinkedList
                                        ::deleteLastOccurrenceTracking),

                        new MethodCase(
                                "Dummy Node",
                                DeleteLastOccurrenceLinkedList
                                        ::deleteLastOccurrenceDummyNode)
                );

        /*
         * Build deterministic test cases.
         */
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

        /*
         * ============================================================
         * Completion
         * ============================================================
         */

        printSeparator();

        System.out.println(
                "TEST SUITE COMPLETED");

        printSeparator();
    }
}
