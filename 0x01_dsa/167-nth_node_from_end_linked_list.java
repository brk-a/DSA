import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

public class NthNodeFromEndLinkedList {

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
     * Brute-force solution.
     *
     * Pass 1: count the nodes.
     * Pass 2: move to index length - k.
     *
     * Time: O(n)
     * Space: O(1)
     */
    static Result nthNodeFromEndBruteForce(
        Node head,
        int k
    ) {
        if (!validNode(head) || k <= 0) {
            return new Result(null, false);
        }

        int length = 0;
        Node current = head;

        while (current != null) {
            length++;
            current = current.next;
        }

        if (k > length) {
            return new Result(null, false);
        }

        int stepsFromStart =
            length - k;

        current = head;

        for (int i = 0; i < stepsFromStart; i++) {
            current = current.next;
        }

        return new Result(current, true);
    }

    /**
     * Two-pointer solution.
     *
     * Move fast k nodes ahead, maintaining a gap of k nodes.
     * Then move both pointers together until fast reaches null.
     * Slow is then the k-th node from the end.
     *
     * Time: O(n)
     * Space: O(1)
     */
    static Result nthNodeFromEndTwoPointers(
        Node head,
        int k
    ) {
        if (!validNode(head) || k <= 0) {
            return new Result(null, false);
        }

        Node fast = head;
        Node slow = head;

        for (int i = 0; i < k; i++) {
            if (fast == null) {
                return new Result(null, false);
            }

            fast = fast.next;
        }

        while (fast != null) {
            slow = slow.next;
            fast = fast.next;
        }

        return new Result(slow, true);
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

    /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    static class TestCase {
        final String id;
        final int[] values;
        final int k;
        final Integer expectedValue;
        final boolean expectedValid;
        final String description;

        TestCase(
            String id,
            int[] values,
            int k,
            Integer expectedValue,
            boolean expectedValid,
            String description
        ) {
            this.id = id;
            this.values = values;
            this.k = k;
            this.expectedValue = expectedValue;
            this.expectedValid = expectedValid;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {
        Result solve(
            Node head,
            int k
        );
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
                        test.k
                    );

                Integer actualValue =
                    actual.valid() && actual.head() != null
                        ? actual.head().val
                        : null;

                boolean success =
                    actual.valid()
                        == test.expectedValid
                    && actualValue
                        == test.expectedValue
                    && !hasCycle(actual.head());

                if (success) {
                    passed++;

                    System.out.printf(
                        "PASS %s (%s)%n",
                        test.id,
                        test.description
                    );

                    System.out.println(
                        "  result = "
                            + actualValue
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
                            + ", k = "
                            + test.k
                    );

                    System.out.println(
                        "  expected = "
                            + test.expectedValue
                            + ", valid = "
                            + test.expectedValid
                    );

                    System.out.println(
                        "  actual   = "
                            + actualValue
                            + ", valid = "
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
            ArrayList<Integer> results =
                new ArrayList<>();

            for (MethodCase method : methods) {
                Node input =
                    buildList(test.values);

                Result result =
                    method.algorithm.solve(
                        input,
                        test.k
                    );

                Integer actualValue =
                    result.valid() && result.head() != null
                        ? result.head().val
                        : null;

                results.add(actualValue);

                if (
                    result.valid()
                        != test.expectedValid
                    || actualValue
                        != test.expectedValue
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
                method.algorithm.solve(
                    null,
                    1
                );

            if (
                !result.valid()
                && result.head() == null
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

            result =
                method.algorithm.solve(
                    buildList(1, 2, 3),
                    0
                );

            if (
                !result.valid()
                && result.head() == null
            ) {
                passed++;

                System.out.printf(
                    "PASS I2 - %s rejects zero%n",
                    method.name
                );
            } else {
                failed++;

                System.out.printf(
                    "FAIL I2 - %s accepts zero%n",
                    method.name
                );
            }

            result =
                method.algorithm.solve(
                    buildList(1, 2, 3),
                    -1
                );

            if (
                !result.valid()
                && result.head() == null
            ) {
                passed++;

                System.out.printf(
                    "PASS I3 - %s rejects negative k%n",
                    method.name
                );
            } else {
                failed++;

                System.out.printf(
                    "FAIL I3 - %s accepts negative k%n",
                    method.name
                );
            }

            result =
                method.algorithm.solve(
                    buildList(1, 2, 3),
                    4
                );

            if (
                !result.valid()
                && result.head() == null
            ) {
                passed++;

                System.out.printf(
                    "PASS I4 - %s rejects k greater than length%n",
                    method.name
                );
            } else {
                failed++;

                System.out.printf(
                    "FAIL I4 - %s accepts k greater than length%n",
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
            new Random(20261031L);

        for (
            int iteration = 1;
            iteration <= iterations;
            iteration++
        ) {
            int length =
                random.nextInt(100) + 1;

            int[] values =
                new int[length];

            for (int i = 0; i < length; i++) {
                values[i] =
                    random.nextInt(2001) - 1000;
            }

            int k =
                random.nextInt(length) + 1;

            int expectedValue =
                values[length - k];

            for (MethodCase method : methods) {
                Node input =
                    buildList(values);

                Result result =
                    method.algorithm.solve(
                        input,
                        k
                    );

                boolean success =
                    result.valid()
                    && result.head() != null
                    && result.head().val
                        == expectedValue;

                if (!success) {
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
                        "input = "
                            + Arrays.toString(values)
                    );

                    System.out.println(
                        "k = "
                            + k
                    );

                    System.out.println(
                        "expected = "
                            + expectedValue
                    );

                    System.out.println(
                        "actual = "
                            + (
                                result.head() == null
                                    ? "null"
                                    : result.head().val
                            )
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
     * Test Data
     * **********************************************************************/

    static List<TestCase> buildTests() {
        List<TestCase> tests =
            new ArrayList<>();

        tests.add(
            new TestCase(
                "B1",
                new int[]{1, 2, 3, 4, 5},
                2,
                4,
                true,
                "second node from the end"
            )
        );

        tests.add(
            new TestCase(
                "B2",
                new int[]{1, 2, 3, 4, 5},
                5,
                1,
                true,
                "first node from the end is the head"
            )
        );

        tests.add(
            new TestCase(
                "B3",
                new int[]{1, 2, 3, 4, 5},
                1,
                5,
                true,
                "last node"
            )
        );

        tests.add(
            new TestCase(
                "S1",
                new int[]{42},
                1,
                42,
                true,
                "single-node list"
            )
        );

        tests.add(
            new TestCase(
                "S2",
                new int[]{7, 7, 7},
                2,
                7,
                true,
                "duplicate values"
            )
        );

        tests.add(
            new TestCase(
                "S3",
                new int[]{-10, 0, 10},
                3,
                -10,
                true,
                "negative and positive values"
            )
        );

        tests.add(
            new TestCase(
                "E1",
                new int[]{},
                1,
                null,
                false,
                "empty list"
            )
        );

        tests.add(
            new TestCase(
                "E2",
                null,
                1,
                null,
                false,
                "null list"
            )
        );

        tests.add(
            new TestCase(
                "E3",
                new int[]{1, 2, 3},
                0,
                null,
                false,
                "zero is not a valid position"
            )
        );

        tests.add(
            new TestCase(
                "E4",
                new int[]{1, 2, 3},
                -1,
                null,
                false,
                "negative k"
            )
        );

        tests.add(
            new TestCase(
                "E5",
                new int[]{1, 2, 3},
                4,
                null,
                false,
                "k greater than list length"
            )
        );

        return tests;
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
            "############ NTH NODE FROM END LINKED LIST #################"
        );

        System.out.println(
            "############################################################"
        );

        System.out.println();

        List<MethodCase> methods =
            List.of(
                new MethodCase(
                    "Brute Force",
                    NthNodeFromEndLinkedList
                        ::nthNodeFromEndBruteForce
                ),
                new MethodCase(
                    "Two Pointers",
                    NthNodeFromEndLinkedList
                        ::nthNodeFromEndTwoPointers
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
