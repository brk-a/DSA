import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class SortLinkedList {


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


    static boolean isValidTernaryValue(int value) {
        return value == 0 || value == 1 || value == 2;
    }


    static Node node(int val) {
        return new Node(val);
    }


    static Node node(int val, Node next) {
        return new Node(val, next);
    }


    /**
     * Sorts a linked list containing only 0, 1 and 2 by overwriting the
     * node values in ascending order.
     *
     * Time: O(n)
     * Extra space: O(1)
     */
    static Result sortLinkedListFrequency(Node head) {
        if (!validNode(head)) {
            return new Result(null, false);
        }


        int[] count = new int[3];


        Node current = head;


        while (current != null) {
            if (!isValidTernaryValue(current.val)) {
                return new Result(null, false);
            }


            count[current.val]++;
            current = current.next;
        }


        current = head;


        for (int value = 0; value <= 2; value++) {
            for (
                int occurrence = 0;
                occurrence < count[value];
                occurrence++
            ) {
                current.val = value;
                current = current.next;
            }
        }


        return new Result(head, true);
    }


    /**
     * Sorts a linked list containing only 0, 1 and 2 by changing node
     * links rather than node values.
     *
     * Time: O(n)
     * Extra space: O(1)
     */
    static Result sortLinkedListChangeLinks(Node head) {
        if (!validNode(head)) {
            return new Result(null, false);
        }


        Node zeroDummy = node(0);
        Node oneDummy = node(0);
        Node twoDummy = node(0);


        Node zeroTail = zeroDummy;
        Node oneTail = oneDummy;
        Node twoTail = twoDummy;


        Node current = head;


        while (current != null) {
            if (!isValidTernaryValue(current.val)) {
                return new Result(null, false);
            }


            Node next = current.next;


            switch (current.val) {
                case 0 -> {
                    zeroTail.next = current;
                    zeroTail = current;
                }


                case 1 -> {
                    oneTail.next = current;
                    oneTail = current;
                }


                default -> {
                    twoTail.next = current;
                    twoTail = current;
                }
            }


            current = next;
        }


        zeroTail.next = oneDummy.next != null
            ? oneDummy.next
            : twoDummy.next;


        oneTail.next = twoDummy.next;
        twoTail.next = null;


        Node sortedHead = zeroDummy.next != null
            ? zeroDummy.next
            : oneDummy.next != null
                ? oneDummy.next
                : twoDummy.next;


        return new Result(sortedHead, true);
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


    static boolean containsSameNodes(
        Node original,
        Node sorted
    ) {
        ArrayList<Node> originalNodes =
            toNodes(original);


        ArrayList<Node> sortedNodes =
            toNodes(sorted);


        if (originalNodes.size() != sortedNodes.size()) {
            return false;
        }


        for (Node node : sortedNodes) {
            if (!originalNodes.contains(node)) {
                return false;
            }
        }


        return true;
    }


    static boolean containsSameValueMultiset(
        int[] original,
        Node sorted
    ) {
        int[] originalCounts = new int[3];
        int[] sortedCounts = new int[3];


        for (int value : original) {
            if (!isValidTernaryValue(value)) {
                return false;
            }


            originalCounts[value]++;
        }


        Node current = sorted;


        while (current != null) {
            if (!isValidTernaryValue(current.val)) {
                return false;
            }


            sortedCounts[current.val]++;
            current = current.next;
        }


        return Arrays.equals(originalCounts, sortedCounts);
    }


    static int[] sortedTernaryArray(int[] values) {
        int[] counts = new int[3];


        for (int value : values) {
            counts[value]++;
        }


        int[] sorted = new int[values.length];
        int index = 0;


        for (int value = 0; value <= 2; value++) {
            for (
                int occurrence = 0;
                occurrence < counts[value];
                occurrence++
            ) {
                sorted[index++] = value;
            }
        }


        return sorted;
    }


    /* **********************************************************************
     * Test Harness
     * **********************************************************************/


    static class TestCase {
        final String id;
        final int[] values;
        final int[] expected;
        final boolean expectedValid;
        final String description;


        TestCase(
            String id,
            int[] values,
            int[] expected,
            boolean expectedValid,
            String description
        ) {
            this.id = id;
            this.values = values;
            this.expected = expected;
            this.expectedValid = expectedValid;
            this.description = description;
        }
    }


    @FunctionalInterface
    interface Algorithm {
        Result solve(Node head);
    }


    @FunctionalInterface
    interface AdditionalCheck {
        boolean check(
            int[] originalValues,
            Node originalHead,
            Result result
        );
    }


    static class MethodCase {
        final String name;
        final Algorithm algorithm;
        final AdditionalCheck additionalCheck;


        MethodCase(
            String name,
            Algorithm algorithm,
            AdditionalCheck additionalCheck
        ) {
            this.name = name;
            this.algorithm = algorithm;
            this.additionalCheck = additionalCheck;
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
            Node input = buildList(test.values);


            try {
                Result actual =
                    method.algorithm.solve(input);


                boolean success =
                    actual.valid() == test.expectedValid
                    && toValues(actual.head()).equals(
                        toIntegerList(test.expected)
                    )
                    && !hasCycle(actual.head())
                    && method.additionalCheck.check(
                        test.values,
                        input,
                        actual
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
                        "  input    = "
                            + Arrays.toString(test.values)
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


        tests.add(
            new TestCase(
                "B1",
                new int[]{2, 1, 0, 1, 2, 0},
                new int[]{0, 0, 1, 1, 2, 2},
                true,
                "mixed ternary values"
            )
        );


        tests.add(
            new TestCase(
                "B2",
                new int[]{1, 1, 2, 0, 2, 0, 1},
                new int[]{0, 0, 1, 1, 1, 2, 2},
                true,
                "all three values present"
            )
        );


        tests.add(
            new TestCase(
                "B3",
                new int[]{0, 0, 0},
                new int[]{0, 0, 0},
                true,
                "only zeroes"
            )
        );


        tests.add(
            new TestCase(
                "B4",
                new int[]{1, 1, 1},
                new int[]{1, 1, 1},
                true,
                "only ones"
            )
        );


        tests.add(
            new TestCase(
                "B5",
                new int[]{2, 2, 2},
                new int[]{2, 2, 2},
                true,
                "only twos"
            )
        );


        tests.add(
            new TestCase(
                "S1",
                new int[]{0},
                new int[]{0},
                true,
                "single zero"
            )
        );


        tests.add(
            new TestCase(
                "S2",
                new int[]{1},
                new int[]{1},
                true,
                "single one"
            )
        );


        tests.add(
            new TestCase(
                "S3",
                new int[]{2},
                new int[]{2},
                true,
                "single two"
            )
        );


        tests.add(
            new TestCase(
                "S4",
                new int[]{2, 0},
                new int[]{0, 2},
                true,
                "two-node list"
            )
        );


        tests.add(
            new TestCase(
                "E1",
                new int[]{},
                new int[]{},
                false,
                "empty list"
            )
        );


        tests.add(
            new TestCase(
                "E2",
                null,
                new int[]{},
                false,
                "null list"
            )
        );


        tests.add(
            new TestCase(
                "E3",
                new int[]{0, 1, 3},
                new int[]{},
                false,
                "value outside 0, 1 and 2"
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


        Random random = new Random(20261115L);


        for (
            int iteration = 1;
            iteration <= iterations;
            iteration++
        ) {
            int size = random.nextInt(100) + 1;


            int[] values = new int[size];


            for (int index = 0; index < size; index++) {
                values[index] = random.nextInt(3);
            }


            int[] expected =
                sortedTernaryArray(values);


            Node input = buildList(values);


            Result result =
                method.algorithm.solve(input);


            boolean success =
                result.valid()
                && toValues(result.head()).equals(
                    toIntegerList(expected)
                )
                && !hasCycle(result.head())
                && method.additionalCheck.check(
                    values,
                    input,
                    result
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
                    "expected = "
                        + Arrays.toString(expected)
                );


                System.out.println(
                    "actual = "
                        + toValues(result.head())
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


    static ArrayList<Integer> toIntegerList(int[] values) {
        ArrayList<Integer> result = new ArrayList<>();


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
            "################ SORT LINKED LIST ##########################"
        );


        System.out.println(
            "############################################################"
        );


        System.out.println();


        List<TestCase> tests = buildTests();


        MethodCase frequencyMethod =
            new MethodCase(
                "Frequency Count Value Overwrite",
                SortLinkedList
                    ::sortLinkedListFrequency,
                (originalValues, originalHead, result) ->
                    containsSameValueMultiset(
                        originalValues,
                        result.head()
                    )
            );


        MethodCase changeLinksMethod =
            new MethodCase(
                "Three-Way Partition Link Change",
                SortLinkedList
                    ::sortLinkedListChangeLinks,
                (originalValues, originalHead, result) ->
                    containsSameNodes(
                        originalHead,
                        result.head()
                    )
            );


        runTests(frequencyMethod, tests);
        runRandomisedTests(frequencyMethod, 5000);


        runTests(changeLinksMethod, tests);
        runRandomisedTests(changeLinksMethod, 5000);
    }
}
