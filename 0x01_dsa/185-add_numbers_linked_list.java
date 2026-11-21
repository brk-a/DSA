import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

public class AddNumbersLinkedList {

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

    static boolean isValidNumber(Node head) {
        Node current = head;

        while (current != null) {
            if (current.val < 0 || current.val > 9) {
                return false;
            }

            current = current.next;
        }

        return true;
    }

    /**
     * Adds two non-negative integers represented as linked lists.
     *
     * The digits are stored in reverse order:
     * 2 -> 4 -> 3 represents 342.
     *
     * Creates a completely new result linked list.
     *
     * Time: O(m + n)
     * Extra space: O(max(m, n)) for the result
     */
    static Result addNumbersNewLinkedList(Node head1, Node head2) {
        if (!validNode(head1) || !validNode(head2)) {
            return new Result(null, false);
        }

        if (!isValidNumber(head1) || !isValidNumber(head2)) {
            return new Result(null, false);
        }

        Node dummyHead = node(0);
        Node tail = dummyHead;

        Node current1 = head1;
        Node current2 = head2;
        int carry = 0;

        while (current1 != null || current2 != null || carry != 0) {
            int digit1 = current1 != null ? current1.val : 0;
            int digit2 = current2 != null ? current2.val : 0;

            int total = digit1 + digit2 + carry;
            int digit = total % 10;
            carry = total / 10;

            tail.next = node(digit);
            tail = tail.next;

            if (current1 != null) {
                current1 = current1.next;
            }

            if (current2 != null) {
                current2 = current2.next;
            }
        }

        return new Result(dummyHead.next, true);
    }

    /**
     * Adds two non-negative integers represented as linked lists.
     *
     * Uses recursion to process corresponding digits and carry values.
     *
     * Time: O(max(m, n))
     * Extra space: O(max(m, n)) for recursion and the result
     */
    static Result addNumbersRecursion(Node head1, Node head2) {
        if (!validNode(head1) || !validNode(head2)) {
            return new Result(null, false);
        }

        if (!isValidNumber(head1) || !isValidNumber(head2)) {
            return new Result(null, false);
        }

        Node head = addRecursive(head1, head2, 0);

        return new Result(head, true);
    }

    static Node addRecursive(Node current1, Node current2, int carry) {
        if (current1 == null && current2 == null && carry == 0) {
            return null;
        }

        int digit1 = current1 != null ? current1.val : 0;
        int digit2 = current2 != null ? current2.val : 0;

        int total = digit1 + digit2 + carry;
        int digit = total % 10;
        int nextCarry = total / 10;

        Node next1 = current1 != null ? current1.next : null;
        Node next2 = current2 != null ? current2.next : null;

        return node(
            digit,
            addRecursive(next1, next2, nextCarry)
        );
    }

    /**
     * Adds two non-negative integers represented as linked lists.
     *
     * Reuses the longer input linked list for the result, appending nodes
     * only when a final carry remains.
     *
     * Time: O(max(m, n))
     * Extra space: O(1), apart from at most one appended carry node
     */
    static Result addNumbersReuseLongerLinkedList(Node head1, Node head2) {
        if (!validNode(head1) || !validNode(head2)) {
            return new Result(null, false);
        }

        if (!isValidNumber(head1) || !isValidNumber(head2)) {
            return new Result(null, false);
        }

        int length1 = listLength(head1);
        int length2 = listLength(head2);

        Node longer = length1 >= length2 ? head1 : head2;
        Node shorter = length1 >= length2 ? head2 : head1;

        Node result = longer;
        Node currentLonger = longer;
        Node currentShorter = shorter;
        int carry = 0;

        while (currentShorter != null) {
            int total =
                currentLonger.val
                + currentShorter.val
                + carry;

            currentLonger.val = total % 10;
            carry = total / 10;

            currentLonger = currentLonger.next;
            currentShorter = currentShorter.next;
        }

        while (currentLonger != null && carry != 0) {
            int total = currentLonger.val + carry;

            currentLonger.val = total % 10;
            carry = total / 10;

            currentLonger = currentLonger.next;
        }

        if (carry != 0) {
            Node tail = findTail(result);
            tail.next = node(carry);
        }

        return new Result(result, true);
    }

    static int listLength(Node head) {
        int length = 0;
        Node current = head;

        while (current != null) {
            length++;
            current = current.next;
        }

        return length;
    }

    static Node findTail(Node head) {
        Node current = head;

        while (current.next != null) {
            current = current.next;
        }

        return current;
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
        final int[] values1;
        final int[] values2;
        final int[] expected;
        final boolean expectedValid;
        final String description;

        TestCase(
            String id,
            int[] values1,
            int[] values2,
            int[] expected,
            boolean expectedValid,
            String description
        ) {
            this.id = id;
            this.values1 = values1;
            this.values2 = values2;
            this.expected = expected;
            this.expectedValid = expectedValid;
            this.description = description;
        }
    }

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

    static void runTests(MethodCase method, List<TestCase> tests) {
        printSeparator();
        System.out.println(method.name);
        printSeparator();

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {
            Node input1 = buildList(test.values1);
            Node input2 = buildList(test.values2);

            try {
                Result actual = method.algorithm.solve(input1, input2);

                boolean success =
                    actual.valid() == test.expectedValid
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
                        "  input 1  = "
                            + Arrays.toString(test.values1)
                    );

                    System.out.println(
                        "  input 2  = "
                            + Arrays.toString(test.values2)
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
                new int[]{2, 4, 3},
                new int[]{5, 6, 4},
                new int[]{7, 0, 8},
                true,
                "342 + 465 = 807"
            )
        );

        tests.add(
            new TestCase(
                "B2",
                new int[]{0},
                new int[]{0},
                new int[]{0},
                true,
                "zero plus zero"
            )
        );

        tests.add(
            new TestCase(
                "B3",
                new int[]{9, 9, 9, 9, 9, 9, 9},
                new int[]{9, 9, 9, 9},
                new int[]{8, 9, 9, 9, 0, 0, 0, 1},
                true,
                "carry propagates through the result"
            )
        );

        tests.add(
            new TestCase(
                "B4",
                new int[]{1},
                new int[]{9, 9, 9},
                new int[]{0, 0, 0, 1},
                true,
                "different list lengths"
            )
        );

        tests.add(
            new TestCase(
                "B5",
                new int[]{5},
                new int[]{5},
                new int[]{0, 1},
                true,
                "single-digit carry"
            )
        );

        tests.add(
            new TestCase(
                "B6",
                new int[]{1, 2, 3},
                new int[]{4, 5, 6},
                new int[]{5, 7, 9},
                true,
                "no final carry"
            )
        );

        tests.add(
            new TestCase(
                "E1",
                new int[]{},
                new int[]{1},
                new int[]{},
                false,
                "empty first list"
            )
        );

        tests.add(
            new TestCase(
                "E2",
                new int[]{1},
                new int[]{},
                new int[]{},
                false,
                "empty second list"
            )
        );

        tests.add(
            new TestCase(
                "E3",
                new int[]{1, 10},
                new int[]{1},
                new int[]{},
                false,
                "invalid digit in first list"
            )
        );

        tests.add(
            new TestCase(
                "E4",
                new int[]{1},
                new int[]{-1},
                new int[]{},
                false,
                "invalid digit in second list"
            )
        );

        return tests;
    }

    /* **********************************************************************
     * Randomised Tests
     * **********************************************************************/

    static void runRandomisedTests(MethodCase method, int iterations) {
        printSeparator();
        System.out.println("Randomised Tests");
        printSeparator();

        Random random = new Random(20261120L);

        for (int iteration = 1; iteration <= iterations; iteration++) {
            int[] values1 = buildRandomDigits(random);
            int[] values2 = buildRandomDigits(random);

            int[] expected = addArrays(values1, values2);

            Node input1 = buildList(values1);
            Node input2 = buildList(values2);

            Result result = method.algorithm.solve(input1, input2);

            boolean success =
                result.valid()
                && toValues(result.head()).equals(
                    toIntegerList(expected)
                )
                && !hasCycle(result.head());

            if (!success) {
                System.out.println("Randomised test FAILED");

                System.out.println(
                    "iteration = "
                        + iteration
                );

                System.out.println(
                    "input 1 = "
                        + Arrays.toString(values1)
                );

                System.out.println(
                    "input 2 = "
                        + Arrays.toString(values2)
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

    static int[] buildRandomDigits(Random random) {
        int size = random.nextInt(100) + 1;
        int[] values = new int[size];

        for (int i = 0; i < size; i++) {
            values[i] = random.nextInt(10);
        }

        return values;
    }

    static int[] addArrays(int[] values1, int[] values2) {
        int maxLength = Math.max(values1.length, values2.length);

        int[] result = new int[maxLength + 1];
        int carry = 0;

        for (int i = 0; i < maxLength; i++) {
            int digit1 = i < values1.length ? values1[i] : 0;
            int digit2 = i < values2.length ? values2[i] : 0;

            int total = digit1 + digit2 + carry;

            result[i] = total % 10;
            carry = total / 10;
        }

        result[maxLength] = carry;

        if (carry == 0) {
            return Arrays.copyOf(result, maxLength);
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

    static void printResults(int passed, int failed, int total) {
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
            "################# ADD NUMBERS LINKED LIST ##################"
        );

        System.out.println(
            "############################################################"
        );

        System.out.println();

        List<MethodCase> methods = new ArrayList<>();

        methods.add(
            new MethodCase(
                "New Linked List",
                AddNumbersLinkedList::addNumbersNewLinkedList
            )
        );

        methods.add(
            new MethodCase(
                "Recursion",
                AddNumbersLinkedList::addNumbersRecursion
            )
        );

        methods.add(
            new MethodCase(
                "Reuse Longer Linked List",
                AddNumbersLinkedList::addNumbersReuseLongerLinkedList
            )
        );

        List<TestCase> tests = buildTests();

        for (MethodCase method : methods) {
            runTests(method, tests);
            runRandomisedTests(method, 5000);
        }
    }
}
