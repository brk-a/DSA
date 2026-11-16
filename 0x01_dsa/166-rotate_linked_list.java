import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

public class RotateLinkedList {

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
     * Rotates the list right by one position, k times.
     *
     * Time: O(nk)
     * Space: O(1)
     */
    static Result rotateLinkedListShiftHead(
        Node head,
        int k
    ) {
        if (!validNode(head) || k < 0) {
            return new Result(null, false);
        }

        int length = listLength(head);

        if (length <= 1) {
            return new Result(head, true);
        }

        k %= length;

        if (k == 0) {
            return new Result(head, true);
        }

        for (int i = 0; i < k; i++) {
            head = rotateRightByOne(head);
        }

        return new Result(head, true);
    }

    static Node rotateRightByOne(Node head) {
        Node current = head;

        while (current.next.next != null) {
            current = current.next;
        }

        Node newHead = current.next;

        current.next = null;
        newHead.next = head;

        return newHead;
    }

    /**
     * Circular-list pointer solution.
     *
     * 1. Find the length and original tail.
     * 2. Reduce k modulo length.
     * 3. Make the list circular.
     * 4. Move to the new tail.
     * 5. Break the circle after the new tail.
     *
     * Time: O(n)
     * Space: O(1)
     */
    static Result rotateLinkedListChangePointer(
        Node head,
        int k
    ) {
        if (!validNode(head) || k < 0) {
            return new Result(null, false);
        }

        int length = 1;
        Node tail = head;

        while (tail.next != null) {
            tail = tail.next;
            length++;
        }

        k %= length;

        if (k == 0) {
            return new Result(head, true);
        }

        tail.next = head;

        int stepsToNewTail =
            length - k - 1;

        Node newTail = head;

        for (int i = 0; i < stepsToNewTail; i++) {
            newTail = newTail.next;
        }

        Node newHead = newTail.next;

        newTail.next = null;

        return new Result(newHead, true);
    }

    /* **********************************************************************
     * List Helpers
     * **********************************************************************/

    static int listLength(Node head) {
        int length = 0;
        Node current = head;

        while (current != null) {
            length++;
            current = current.next;
        }

        return length;
    }

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
        final int[] expected;
        final boolean expectedValid;
        final String description;

        TestCase(
            String id,
            int[] values,
            int k,
            int[] expected,
            boolean expectedValid,
            String description
        ) {
            this.id = id;
            this.values = values;
            this.k = k;
            this.expected = expected;
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

                boolean success =
                    actual.valid()
                        == test.expectedValid
                    && toValues(actual.head()).equals(
                        toIntegerList(test.expected)
                    )
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
                            + ", k = "
                            + test.k
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
            ArrayList<ArrayList<Integer>> results =
                new ArrayList<>();

            for (MethodCase method : methods) {
                Node input =
                    buildList(test.values);

                Result result =
                    method.algorithm.solve(
                        input,
                        test.k
                    );

                ArrayList<Integer> values =
                    toValues(result.head());

                results.add(values);

                if (
                    result.valid()
                        != test.expectedValid
                    || !values.equals(
                        toIntegerList(test.expected)
                    )
                    || hasCycle(result.head())
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
                    -1
                );

            if (
                !result.valid()
                && result.head() == null
            ) {
                passed++;

                System.out.printf(
                    "PASS I2 - %s rejects negative k%n",
                    method.name
                );
            } else {
                failed++;

                System.out.printf(
                    "FAIL I2 - %s accepts negative k%n",
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
            new Random(20261030L);

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
                random.nextInt(length * 2);

            int[] expected =
                rotateArrayRight(values, k);

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
                    && toValues(result.head()).equals(
                        toIntegerList(expected)
                    )
                    && !hasCycle(result.head());

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
        }

        System.out.printf(
            "All %d randomised tests passed.%n%n",
            iterations
        );
    }

    static int[] rotateArrayRight(
        int[] values,
        int k
    ) {
        int length = values.length;
        int[] rotated =
            new int[length];

        k %= length;

        for (int i = 0; i < length; i++) {
            rotated[(i + k) % length] =
                values[i];
        }

        return rotated;
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
                new int[]{4, 5, 1, 2, 3},
                true,
                "classic right rotation"
            )
        );

        tests.add(
            new TestCase(
                "B2",
                new int[]{1, 2, 3, 4, 5},
                1,
                new int[]{5, 1, 2, 3, 4},
                true,
                "rotate right by one"
            )
        );

        tests.add(
            new TestCase(
                "B3",
                new int[]{1, 2, 3, 4, 5},
                5,
                new int[]{1, 2, 3, 4, 5},
                true,
                "full rotation returns original list"
            )
        );

        tests.add(
            new TestCase(
                "B4",
                new int[]{1, 2, 3, 4, 5},
                7,
                new int[]{4, 5, 1, 2, 3},
                true,
                "k greater than length"
            )
        );

        tests.add(
            new TestCase(
                "S1",
                new int[]{42},
                10,
                new int[]{42},
                true,
                "single-node list"
            )
        );

        tests.add(
            new TestCase(
                "S2",
                new int[]{1, 2},
                1,
                new int[]{2, 1},
                true,
                "two-node list"
            )
        );

        tests.add(
            new TestCase(
                "S3",
                new int[]{7, 7, 7},
                2,
                new int[]{7, 7, 7},
                true,
                "duplicate values"
            )
        );

        tests.add(
            new TestCase(
                "S4",
                new int[]{-5, 0, 5},
                1,
                new int[]{5, -5, 0},
                true,
                "negative and positive values"
            )
        );

        tests.add(
            new TestCase(
                "E1",
                new int[]{},
                1,
                new int[]{},
                false,
                "empty list"
            )
        );

        tests.add(
            new TestCase(
                "E2",
                null,
                1,
                new int[]{},
                false,
                "null list"
            )
        );

        tests.add(
            new TestCase(
                "E3",
                new int[]{1, 2, 3},
                -1,
                new int[]{},
                false,
                "negative k"
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
            "################## ROTATE LINKED LIST ######################"
        );

        System.out.println(
            "############################################################"
        );

        System.out.println();

        List<MethodCase> methods =
            List.of(
                new MethodCase(
                    "Shift Head Repeatedly",
                    RotateLinkedList
                        ::rotateLinkedListShiftHead
                ),
                new MethodCase(
                    "Circular Pointer Rewiring",
                    RotateLinkedList
                        ::rotateLinkedListChangePointer
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
