import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;


public class MergeSortedLinkedLists {


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


    /**
     * Merges two sorted linked lists by copying their values into an array,
     * sorting the array and rebuilding a new list.
     *
     * Time: O((m + n) log(m + n))
     * Extra space: O(m + n)
     */
    static Result mergeSortedLinkedListsArray(Node head1, Node head2) {
        if (!validNode(head1) && !validNode(head2)) {
            return new Result(null, false);
        }


        if (!validNode(head1)) {
            return new Result(head2, true);
        }


        if (!validNode(head2)) {
            return new Result(head1, true);
        }


        ArrayList<Integer> values = new ArrayList<>();


        Node current1 = head1;
        while (current1 != null) {
            values.add(current1.val);
            current1 = current1.next;
        }


        Node current2 = head2;
        while (current2 != null) {
            values.add(current2.val);
            current2 = current2.next;
        }


        values.sort(Integer::compareTo);


        Node dummy = new Node(-1);
        Node current = dummy;


        for (int value : values) {
            current.next = new Node(value);
            current = current.next;
        }


        return new Result(dummy.next, true);
    }


    /**
     * Merges two sorted linked lists recursively.
     *
     * Time: O(m + n)
     * Extra space: O(m + n) because of recursion
     */
    static Result mergeSortedLinkedListsRecursion(Node head1, Node head2) {
        if (!validNode(head1) && !validNode(head2)) {
            return new Result(null, false);
        }


        if (!validNode(head1)) {
            return new Result(head2, true);
        }


        if (!validNode(head2)) {
            return new Result(head1, true);
        }


        if (head1.val <= head2.val) {
            head1.next = mergeSortedLinkedListsRecursion(
                head1.next,
                head2
            ).head();


            return new Result(head1, true);
        }


        head2.next = mergeSortedLinkedListsRecursion(
            head1,
            head2.next
        ).head();


        return new Result(head2, true);
    }


    /**
     * Merges two sorted linked lists iteratively.
     *
     * Time: O(m + n)
     * Extra space: O(1)
     */
    static Result mergeSortedLinkedListsIteration(Node head1, Node head2) {
        if (!validNode(head1) && !validNode(head2)) {
            return new Result(null, false);
        }


        if (!validNode(head1)) {
            return new Result(head2, true);
        }


        if (!validNode(head2)) {
            return new Result(head1, true);
        }


        Node dummy = new Node(-1);
        Node current = dummy;


        while (head1 != null && head2 != null) {
            if (head1.val <= head2.val) {
                current.next = head1;
                head1 = head1.next;
            } else {
                current.next = head2;
                head2 = head2.next;
            }


            current = current.next;
        }


        if (head1 != null) {
            current.next = head1;
        } else {
            current.next = head2;
        }


        return new Result(dummy.next, true);
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


    static ArrayList<Node> toNodes(Node head) {
        ArrayList<Node> nodes = new ArrayList<>();


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


        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;


            if (slow == fast) {
                return true;
            }
        }


        return false;
    }


    static boolean containsSameNodes(Node first, Node second) {
        ArrayList<Node> firstNodes = toNodes(first);
        ArrayList<Node> secondNodes = toNodes(second);


        if (firstNodes.size() != secondNodes.size()) {
            return false;
        }


        for (Node node : secondNodes) {
            if (!firstNodes.contains(node)) {
                return false;
            }
        }


        return true;
    }


    static int[] mergeSortedArrays(int[] first, int[] second) {
        int[] merged = new int[first.length + second.length];


        int firstIndex = 0;
        int secondIndex = 0;
        int mergedIndex = 0;


        while (
            firstIndex < first.length
            && secondIndex < second.length
        ) {
            if (first[firstIndex] <= second[secondIndex]) {
                merged[mergedIndex++] = first[firstIndex++];
            } else {
                merged[mergedIndex++] = second[secondIndex++];
            }
        }


        while (firstIndex < first.length) {
            merged[mergedIndex++] = first[firstIndex++];
        }


        while (secondIndex < second.length) {
            merged[mergedIndex++] = second[secondIndex++];
        }


        return merged;
    }


    static ArrayList<Integer> toIntegerList(int[] values) {
        ArrayList<Integer> result = new ArrayList<>();


        for (int value : values) {
            result.add(value);
        }


        return result;
    }


    /* **********************************************************************
     * Test Harness
     * **********************************************************************/


    static class TestCase {
        final String id;
        final int[] firstValues;
        final int[] secondValues;
        final int[] expected;
        final boolean expectedValid;
        final String description;


        TestCase(
            String id,
            int[] firstValues,
            int[] secondValues,
            int[] expected,
            boolean expectedValid,
            String description
        ) {
            this.id = id;
            this.firstValues = firstValues;
            this.secondValues = secondValues;
            this.expected = expected;
            this.expectedValid = expectedValid;
            this.description = description;
        }
    }


    @FunctionalInterface
    interface Algorithm {
        Result solve(Node first, Node second);
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
            Node first = buildList(test.firstValues);
            Node second = buildList(test.secondValues);


            try {
                Result actual = method.algorithm.solve(first, second);


                boolean success =
                    actual.valid() == test.expectedValid
                    && toValues(actual.head()).equals(
                        toIntegerList(test.expected)
                    )
                    && !hasCycle(actual.head());


                if (test.expectedValid) {
                    success = success
                        && containsSameNodes(first, actual.head())
                        && containsSameNodes(second, actual.head());
                }


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
                        "  first    = "
                            + Arrays.toString(test.firstValues)
                    );


                    System.out.println(
                        "  second   = "
                            + Arrays.toString(test.secondValues)
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


        printResults(passed, failed, tests.size());
    }


    /* **********************************************************************
     * Test Data
     * **********************************************************************/


    static List<TestCase> buildTests() {
        List<TestCase> tests = new ArrayList<>();


        tests.add(new TestCase(
            "B1",
            new int[]{1, 3, 5},
            new int[]{2, 4, 6},
            new int[]{1, 2, 3, 4, 5, 6},
            true,
            "alternating values"
        ));


        tests.add(new TestCase(
            "B2",
            new int[]{1, 2, 3},
            new int[]{4, 5, 6},
            new int[]{1, 2, 3, 4, 5, 6},
            true,
            "first list entirely smaller"
        ));


        tests.add(new TestCase(
            "B3",
            new int[]{4, 5, 6},
            new int[]{1, 2, 3},
            new int[]{1, 2, 3, 4, 5, 6},
            true,
            "second list entirely smaller"
        ));


        tests.add(new TestCase(
            "B4",
            new int[]{1, 1, 2},
            new int[]{1, 3},
            new int[]{1, 1, 1, 2, 3},
            true,
            "duplicate values"
        ));


        tests.add(new TestCase(
            "B5",
            new int[]{-5, 0, 10},
            new int[]{-10, 5, 20},
            new int[]{-10, -5, 0, 5, 10, 20},
            true,
            "negative and positive values"
        ));


        tests.add(new TestCase(
            "S1",
            new int[]{1},
            new int[]{2},
            new int[]{1, 2},
            true,
            "single-node lists"
        ));


        tests.add(new TestCase(
            "S2",
            new int[]{1, 2},
            new int[]{},
            new int[]{1, 2},
            true,
            "second list empty"
        ));


        tests.add(new TestCase(
            "S3",
            new int[]{},
            new int[]{1, 2},
            new int[]{1, 2},
            true,
            "first list empty"
        ));


        tests.add(new TestCase(
            "E1",
            new int[]{},
            new int[]{},
            new int[]{},
            false,
            "both lists empty"
        ));


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


        Random random = new Random(20261114L);


        for (
            int iteration = 1;
            iteration <= iterations;
            iteration++
        ) {
            int firstSize = random.nextInt(50);
            int secondSize = random.nextInt(50);


            int[] firstValues =
                buildSortedRandomValues(random, firstSize);
            int[] secondValues =
                buildSortedRandomValues(random, secondSize);


            int[] expected =
                mergeSortedArrays(firstValues, secondValues);


            Node first = buildList(firstValues);
            Node second = buildList(secondValues);


            Result result = method.algorithm.solve(first, second);


            boolean success =
                result.valid()
                && toValues(result.head()).equals(
                    toIntegerList(expected)
                )
                && !hasCycle(result.head());


            if (firstSize > 0) {
                success = success
                    && containsSameNodes(first, result.head());
            }


            if (secondSize > 0) {
                success = success
                    && containsSameNodes(second, result.head());
            }


            if (!success) {
                System.out.println("Randomised test FAILED");


                System.out.println(
                    "iteration = " + iteration
                );


                System.out.println(
                    "first = " + Arrays.toString(firstValues)
                );


                System.out.println(
                    "second = " + Arrays.toString(secondValues)
                );


                System.out.println(
                    "expected = " + Arrays.toString(expected)
                );


                System.out.println(
                    "actual = " + toValues(result.head())
                );


                System.out.println(
                    "valid = " + result.valid()
                );


                return;
            }
        }


        System.out.printf(
            "All %d randomised tests passed.%n%n",
            iterations
        );
    }


    static int[] buildSortedRandomValues(
        Random random,
        int size
    ) {
        int[] values = new int[size];


        for (int index = 0; index < size; index++) {
            values[index] = random.nextInt(2001) - 1000;
        }


        Arrays.sort(values);


        return values;
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
            "############# MERGE SORTED LINKED LISTS ####################"
        );


        System.out.println(
            "############################################################"
        );


        System.out.println();


        List<MethodCase> methods = List.of(
            new MethodCase(
                "Array Merge",
                MergeSortedLinkedLists
                    ::mergeSortedLinkedListsArray
            ),
            new MethodCase(
                "Recursive Merge",
                MergeSortedLinkedLists
                    ::mergeSortedLinkedListsRecursion
            ),
            new MethodCase(
                "Iterative Merge",
                MergeSortedLinkedLists
                    ::mergeSortedLinkedListsIteration
            )
        );


        List<TestCase> tests = buildTests();


        for (MethodCase method : methods) {
            runTests(method, tests);
            runRandomisedTests(method, 5000);
        }
    }
}
