import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Random;
import java.util.function.Function;

public class PalindromeLinkedList {

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
        boolean isPalindrome,
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
     * Determines whether a singly linked list is a palindrome using a stack.
     *
     * Time: O(n)
     * Extra space: O(n)
     */
    static Result palindromeLinkedListStack(Node head) {
        if (!validNode(head)) {
            return new Result(false, false);
        }

        Deque<Node> stack = new ArrayDeque<>();
        Node current = head;

        while (current != null) {
            stack.push(current);
            current = current.next;
        }

        current = head;

        while (current != null) {
            Node reversedNode = stack.pop();

            if (current.val != reversedNode.val) {
                return new Result(false, true);
            }

            current = current.next;
        }

        return new Result(true, true);
    }

    /**
     * Determines whether a singly linked list is a palindrome by reversing
     * only the second half of the list.
     *
     * The input list is restored before returning.
     *
     * Time: O(n)
     * Extra space: O(1)
     */
    static Result palindromeLinkedListReverseSecondHalf(Node head) {
        if (!validNode(head)) {
            return new Result(false, false);
        }

        if (head.next == null) {
            return new Result(true, true);
        }

        Node firstHalfEnd = findFirstHalfEnd(head);
        Node secondHalfStart = reverseList(firstHalfEnd.next);

        Node first = head;
        Node second = secondHalfStart;
        boolean isPalindrome = true;

        while (second != null) {
            if (first.val != second.val) {
                isPalindrome = false;
                break;
            }

            first = first.next;
            second = second.next;
        }

        firstHalfEnd.next = reverseList(secondHalfStart);

        return new Result(isPalindrome, true);
    }

    /**
     * Finds the final node of the first half.
     *
     * For an even-length list, this is the node before the midpoint.
     * For an odd-length list, this is the exact middle node.
     */
    static Node findFirstHalfEnd(Node head) {
        Node slow = head;
        Node fast = head;

        while (
            fast.next != null
            && fast.next.next != null
        ) {
            slow = slow.next;
            fast = fast.next.next;
        }

        return slow;
    }

    static Node reverseList(Node head) {
        Node previous = null;
        Node current = head;

        while (current != null) {
            Node next = current.next;
            current.next = previous;
            previous = current;
            current = next;
        }

        return previous;
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
        final boolean expectedPalindrome;
        final boolean expectedValid;
        final String description;

        TestCase(
            String id,
            int[] values,
            boolean expectedPalindrome,
            boolean expectedValid,
            String description
        ) {
            this.id = id;
            this.values = values;
            this.expectedPalindrome = expectedPalindrome;
            this.expectedValid = expectedValid;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {
        Result solve(Node head);
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
            Node input = buildList(test.values);

            try {
                Result actual = method.algorithm.solve(input);

                boolean success =
                    actual.valid() == test.expectedValid
                    && actual.isPalindrome() == test.expectedPalindrome
                    && !hasCycle(input);

                if (success) {
                    passed++;

                    System.out.printf(
                        "PASS %s (%s)%n",
                        test.id,
                        test.description
                    );

                    System.out.println(
                        "  values = "
                            + toValues(input)
                    );

                    System.out.println(
                        "  result = "
                            + actual.isPalindrome()
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
                            + test.expectedPalindrome
                            + ", valid = "
                            + test.expectedValid
                    );

                    System.out.println(
                        "  actual   = "
                            + actual.isPalindrome()
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
        List<TestCase> tests = new ArrayList<>();

        tests.add(
            new TestCase(
                "B1",
                new int[]{1, 2, 2, 1},
                true,
                true,
                "even-length palindrome"
            )
        );

        tests.add(
            new TestCase(
                "B2",
                new int[]{1, 2, 3, 2, 1},
                true,
                true,
                "odd-length palindrome"
            )
        );

        tests.add(
            new TestCase(
                "B3",
                new int[]{1, 2, 3, 4, 3, 2, 1},
                true,
                true,
                "longer odd-length palindrome"
            )
        );

        tests.add(
            new TestCase(
                "B4",
                new int[]{1, 2, 3, 4},
                false,
                true,
                "even-length non-palindrome"
            )
        );

        tests.add(
            new TestCase(
                "B5",
                new int[]{1, 2, 3, 4, 5},
                false,
                true,
                "odd-length non-palindrome"
            )
        );

        tests.add(
            new TestCase(
                "S1",
                new int[]{42},
                true,
                true,
                "single-node list"
            )
        );

        tests.add(
            new TestCase(
                "S2",
                new int[]{7, 7},
                true,
                true,
                "two-node palindrome"
            )
        );

        tests.add(
            new TestCase(
                "S3",
                new int[]{7, 8},
                false,
                true,
                "two-node non-palindrome"
            )
        );

        tests.add(
            new TestCase(
                "S4",
                new int[]{9, 9, 9, 9, 9},
                true,
                true,
                "duplicate values"
            )
        );

        tests.add(
            new TestCase(
                "S5",
                new int[]{-5, 0, -5},
                true,
                true,
                "negative and zero values"
            )
        );

        tests.add(
            new TestCase(
                "S6",
                new int[]{-5, 0, 5},
                false,
                true,
                "negative and positive values"
            )
        );

        tests.add(
            new TestCase(
                "E1",
                new int[]{},
                false,
                false,
                "empty list"
            )
        );

        tests.add(
            new TestCase(
                "E2",
                null,
                false,
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

        Random random = new Random(20261116L);

        for (
            int iteration = 1;
            iteration <= iterations;
            iteration++
        ) {
            int size = random.nextInt(100) + 1;
            int[] values = new int[size];

            for (int index = 0; index < size; index++) {
                values[index] = random.nextInt(2001) - 1000;
            }

            boolean expected = isPalindromeArray(values);

            Node input = buildList(values);
            Result result = method.algorithm.solve(input);

            boolean success =
                result.valid()
                && result.isPalindrome() == expected
                && !hasCycle(input);

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
                        + expected
                );

                System.out.println(
                    "actual = "
                        + result.isPalindrome()
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

    static boolean isPalindromeArray(int[] values) {
        int left = 0;
        int right = values.length - 1;

        while (left < right) {
            if (values[left] != values[right]) {
                return false;
            }

            left++;
            right--;
        }

        return true;
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
            "################ PALINDROME LINKED LIST ####################"
        );

        System.out.println(
            "############################################################"
        );

        System.out.println();

        List<TestCase> tests = buildTests();

        MethodCase stackMethod =
            new MethodCase(
                "Stack Comparison",
                PalindromeLinkedList
                    ::palindromeLinkedListStack
            );

        MethodCase reverseMethod =
            new MethodCase(
                "Reverse Second Half",
                PalindromeLinkedList
                    ::palindromeLinkedListReverseSecondHalf
            );

        runTests(stackMethod, tests);
        runRandomisedTests(stackMethod, 5000);

        runTests(reverseMethod, tests);
        runRandomisedTests(reverseMethod, 5000);
    }
}
