import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.Function;


public class DeleteOccurrencesLinkedList {


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
     * Deletes every node whose value is equal to the given key.
     *
     * The list is modified in place.
     *
     * Time: O(n)
     * Extra space: O(1)
     */
    static Result deleteOccurrences(Node head, int key) {
        if (!validNode(head)) {
            return new Result(null, false);
        }


        Node current = head;
        Node previous = null;


        while (current != null) {
            if (current.val == key) {
                Node next = current.next;


                if (previous == null) {
                    head = next;
                } else {
                    previous.next = next;
                }


                current.next = null;
                current = next;
            } else {
                previous = current;
                current = current.next;
            }
        }


        return new Result(head, true);
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
        ArrayList<Integer> values =
            new ArrayList<>();


        Node current = head;


        while (current != null) {
            values.add(current.val);
            current = current.next;
        }


        return values;
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


    static boolean containsOnlyExpectedValues(
        Node head,
        int key
    ) {
        Node current = head;


        while (current != null) {
            if (current.val == key) {
                return false;
            }


            current = current.next;
        }


        return true;
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
            String description
        ) {
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
        Result solve(Node head, int key);
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


            try {
                Result actual =
                    method.algorithm.solve(
                        input,
                        test.key
                    );


                boolean success =
                    actual.valid()
                        == test.expectedValid
                    && toValues(actual.head()).equals(
                        toIntegerList(test.expected)
                    )
                    && !hasCycle(actual.head())
                    && containsOnlyExpectedValues(
                        actual.head(),
                        test.key
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
                        "  key      = "
                            + test.key
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
        List<TestCase> tests =
            new ArrayList<>();


        tests.add(
            new TestCase(
                "B1",
                new int[]{1, 2, 3, 4, 2},
                2,
                new int[]{1, 3, 4},
                true,
                "removes occurrences in the middle and end"
            )
        );


        tests.add(
            new TestCase(
                "B2",
                new int[]{2, 1, 2, 3, 2},
                2,
                new int[]{1, 3},
                true,
                "removes occurrences at the head, middle and end"
            )
        );


        tests.add(
            new TestCase(
                "B3",
                new int[]{7, 7, 7, 7},
                7,
                new int[]{},
                true,
                "removes every node"
            )
        );


        tests.add(
            new TestCase(
                "B4",
                new int[]{1, 2, 3},
                4,
                new int[]{1, 2, 3},
                true,
                "key is absent"
            )
        );


        tests.add(
            new TestCase(
                "S1",
                new int[]{42},
                42,
                new int[]{},
                true,
                "single-node list matching the key"
            )
        );


        tests.add(
            new TestCase(
                "S2",
                new int[]{42},
                7,
                new int[]{42},
                true,
                "single-node list not matching the key"
            )
        );


        tests.add(
            new TestCase(
                "S3",
                new int[]{-5, 0, -5, 5},
                -5,
                new int[]{0, 5},
                true,
                "negative key values"
            )
        );


        tests.add(
            new TestCase(
                "S4",
                new int[]{1, 1, 2, 1},
                1,
                new int[]{2},
                true,
                "consecutive and trailing duplicates"
            )
        );


        tests.add(
            new TestCase(
                "E1",
                new int[]{},
                3,
                new int[]{},
                false,
                "empty list"
            )
        );


        tests.add(
            new TestCase(
                "E2",
                null,
                3,
                new int[]{},
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


        Random random =
            new Random(20261117L);


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
                    random.nextInt(21) - 10;
            }


            int key =
                random.nextInt(21) - 10;


            int[] expected =
                deleteOccurrencesFromArray(
                    values,
                    key
                );


            Node input =
                buildList(values);


            Result result =
                method.algorithm.solve(
                    input,
                    key
                );


            boolean success =
                result.valid()
                && toValues(result.head()).equals(
                    toIntegerList(expected)
                )
                && !hasCycle(result.head())
                && containsOnlyExpectedValues(
                    result.head(),
                    key
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
                    "key = "
                        + key
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


    static int[] deleteOccurrencesFromArray(
        int[] values,
        int key
    ) {
        int retainedCount = 0;


        for (int value : values) {
            if (value != key) {
                retainedCount++;
            }
        }


        int[] retained =
            new int[retainedCount];


        int index = 0;


        for (int value : values) {
            if (value != key) {
                retained[index++] = value;
            }
        }


        return retained;
    }


    static ArrayList<Integer> toIntegerList(
        int[] values
    ) {
        ArrayList<Integer> result =
            new ArrayList<>();


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
            "############ DELETE OCCURRENCES LINKED LIST ################"
        );


        System.out.println(
            "############################################################"
        );


        System.out.println();


        MethodCase method =
            new MethodCase(
                "Previous Pointer Deletion",
                DeleteOccurrencesLinkedList
                    ::deleteOccurrences
            );


        List<TestCase> tests =
            buildTests();


        runTests(method, tests);
        runRandomisedTests(method, 5000);
    }
}
