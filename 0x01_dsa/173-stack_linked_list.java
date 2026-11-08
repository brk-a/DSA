import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;

/**
 * A singly linked list implementation of a stack.
 *
 * The most recently inserted element is stored at the top of the stack.
 *
 * Operations:
 *
 *      push(value)     Add an element to the top.
 *      pop()           Remove and return the top element.
 *      peek()          Return, but do not remove, the top element.
 *      isEmpty()       Determine whether the stack contains no elements.
 *      size()          Return the number of elements currently stored.
 *
 * Time complexity:
 *
 *      push:     O(1)
 *      pop:      O(1)
 *      peek:     O(1)
 *      isEmpty:  O(1)
 *      size:     O(1)
 *
 * Space complexity:
 *
 *      O(n), where n is the number of elements stored in the stack.
 */
public class StackLinkedList {


    /* **********************************************************************
     * Node
     * **********************************************************************/


    static class Node<T> {


        final T val;
        Node<T> next;


        Node(T val) {
            this.val = val;
            this.next = null;
        }


        Node(T val, Node<T> next) {
            this.val = val;
            this.next = next;
        }
    }


    /* **********************************************************************
     * NodeStack
     * **********************************************************************/


    static class NodeStack<T> {


        private Node<T> top;
        private int count;


        NodeStack() {
            this.top = null;
            this.count = 0;
        }


        /**
         * Add an element to the top of the stack.
         *
         * Time:  O(1)
         * Space: O(1)
         */
        void push(T val) {

            top = new Node<>(val, top);
            count++;
        }


        /**
         * Remove and return the element at the top of the stack.
         *
         * Time:  O(1)
         * Space: O(1)
         */
        T pop() {

            if (isEmpty()) {
                throw new NoSuchElementException(
                        "Stack under-flow: cannot pop from an empty stack");
            }

            T val = top.val;
            top = top.next;
            count--;

            return val;
        }


        /**
         * Return the element at the top of the stack without removing it.
         *
         * Time:  O(1)
         * Space: O(1)
         */
        T peek() {

            if (isEmpty()) {
                throw new NoSuchElementException(
                        "Stack is empty: cannot peek");
            }

            return top.val;
        }


        /**
         * Determine whether the stack contains no elements.
         *
         * Time:  O(1)
         * Space: O(1)
         */
        boolean isEmpty() {
            return top == null;
        }


        /**
         * Return the number of elements currently stored in the stack.
         *
         * Time:  O(1)
         * Space: O(1)
         */
        int size() {
            return count;
        }
    }


    /* **********************************************************************
     * Test Case
     * **********************************************************************/


    static class TestCase<T> {


        final String id;
        final String description;
        final StackOperation<T> operation;
        final Object expected;
        final Class<? extends Throwable> expectedException;


        TestCase(
                String id,
                String description,
                StackOperation<T> operation,
                Object expected) {

            this(
                    id,
                    description,
                    operation,
                    expected,
                    null);
        }


        TestCase(
                String id,
                String description,
                StackOperation<T> operation,
                Object expected,
                Class<? extends Throwable> expectedException) {

            this.id = id;
            this.description = description;
            this.operation = operation;
            this.expected = expected;
            this.expectedException = expectedException;
        }
    }


    @FunctionalInterface
    interface StackOperation<T> {


        Object run(NodeStack<T> stack);
    }


    /* **********************************************************************
     * Test Runner
     * **********************************************************************/


    static <T> void runTests(
            String stackName,
            List<TestCase<T>> tests) {

        printSeparator();
        System.out.println(stackName);
        printSeparator();

        int passed = 0;
        int failed = 0;

        for (TestCase<T> test : tests) {

            NodeStack<T> stack = new NodeStack<>();

            try {

                Object actual = test.operation.run(stack);
                boolean success =
                        test.expectedException == null
                                && String.valueOf(actual)
                                .equals(String.valueOf(test.expected));

                if (success) {

                    passed++;

                    System.out.printf(
                            "PASS %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  result = "
                                    + actual);

                } else {

                    failed++;

                    System.out.printf(
                            "FAIL %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  expected = "
                                    + test.expected);

                    System.out.println(
                            "  actual   = "
                                    + actual);
                }

            } catch (Throwable thrown) {

                boolean success =
                        test.expectedException != null
                                && test.expectedException
                                .isInstance(thrown);

                if (success) {

                    passed++;

                    System.out.printf(
                            "PASS %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  exception = "
                                    + thrown.getClass()
                                    .getSimpleName());

                } else {

                    failed++;

                    System.out.printf(
                            "FAIL %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  unexpected exception = "
                                    + thrown);
                }
            }
        }

        printResults(passed, failed, tests.size());
    }


    /* **********************************************************************
     * Integer Stack Tests
     * **********************************************************************/


    static List<TestCase<Integer>> buildIntegerTests() {

        List<TestCase<Integer>> tests = new ArrayList<>();

        tests.add(new TestCase<>(
                "I1",
                "a new stack is empty",
                stack -> stack.isEmpty(),
                true));

        tests.add(new TestCase<>(
                "I2",
                "a new stack has size zero",
                stack -> stack.size(),
                0));

        tests.add(new TestCase<>(
                "I3",
                "push increases the size",
                stack -> {

                    stack.push(10);
                    return stack.size();
                },
                1));

        tests.add(new TestCase<>(
                "I4",
                "peek returns the most recently pushed value",
                stack -> {

                    stack.push(10);
                    stack.push(20);
                    return stack.peek();
                },
                20));

        tests.add(new TestCase<>(
                "I5",
                "peek does not remove the top value",
                stack -> {

                    stack.push(10);
                    stack.push(20);
                    stack.peek();

                    return stack.size();
                },
                2));

        tests.add(new TestCase<>(
                "I6",
                "pop returns values in reverse insertion order",
                stack -> {

                    stack.push(10);
                    stack.push(20);
                    stack.push(30);

                    return ""
                            + stack.pop()
                            + ", "
                            + stack.pop()
                            + ", "
                            + stack.pop();
                },
                "30, 20, 10"));

        tests.add(new TestCase<>(
                "I7",
                "pop decreases the size",
                stack -> {

                    stack.push(10);
                    stack.push(20);
                    stack.pop();

                    return stack.size();
                },
                1));

        tests.add(new TestCase<>(
                "I8",
                "the stack becomes empty after removing every element",
                stack -> {

                    stack.push(10);
                    stack.pop();

                    return stack.isEmpty();
                },
                true));

        tests.add(new TestCase<>(
                "I9",
                "pop from an empty stack throws NoSuchElementException",
                stack -> stack.pop(),
                null,
                NoSuchElementException.class));

        tests.add(new TestCase<>(
                "I10",
                "peek from an empty stack throws NoSuchElementException",
                stack -> stack.peek(),
                null,
                NoSuchElementException.class));

        tests.add(new TestCase<>(
                "I11",
                "negative values are stored correctly",
                stack -> {

                    stack.push(-10);
                    stack.push(-20);

                    return stack.pop();
                },
                -20));

        tests.add(new TestCase<>(
                "I12",
                "the stack supports repeated use",
                stack -> {

                    stack.push(1);
                    stack.pop();
                    stack.push(2);

                    return stack.peek();
                },
                2));

        return tests;
    }


    /* **********************************************************************
     * String Stack Tests
     * **********************************************************************/


    static List<TestCase<String>> buildStringTests() {

        List<TestCase<String>> tests = new ArrayList<>();

        tests.add(new TestCase<>(
                "S1",
                "string values preserve insertion order",
                stack -> {

                    stack.push("first");
                    stack.push("second");
                    stack.push("third");

                    return ""
                            + stack.pop()
                            + ", "
                            + stack.pop()
                            + ", "
                            + stack.pop();
                },
                "third, second, first"));

        tests.add(new TestCase<>(
                "S2",
                "empty string is a valid stack value",
                stack -> {

                    stack.push("");
                    return stack.peek();
                },
                ""));

        tests.add(new TestCase<>(
                "S3",
                "null values are supported by the implementation",
                stack -> {

                    stack.push(null);
                    return stack.peek();
                },
                "null"));

        return tests;
    }


    /* **********************************************************************
     * Randomised Tests
     * **********************************************************************/


    static void runRandomisedTests(int iterations) {

        printSeparator();
        System.out.println("Randomised Cross Checks");
        printSeparator();

        Random random = new Random(20261107L);
        List<Integer> expected = new ArrayList<>();

        for (int iteration = 1; iteration <= iterations; iteration++) {

            NodeStack<Integer> stack = new NodeStack<>();
            expected.clear();

            int operations = 1 + random.nextInt(100);

            for (int operation = 0; operation < operations; operation++) {

                boolean shouldPush = expected.isEmpty()
                        || random.nextBoolean();

                if (shouldPush) {

                    int value = random.nextInt(2001) - 1000;

                    stack.push(value);
                    expected.add(value);

                } else {

                    int expectedValue = expected.remove(
                            expected.size() - 1);

                    int actualValue = stack.pop();

                    if (expectedValue != actualValue) {

                        System.out.println("Randomised test FAILED");
                        System.out.println(
                                "iteration = "
                                        + iteration);
                        System.out.println(
                                "expected = "
                                        + expectedValue);
                        System.out.println(
                                "actual = "
                                        + actualValue);

                        return;
                    }
                }

                if (stack.size() != expected.size()) {

                    System.out.println("Randomised test FAILED");
                    System.out.println(
                            "iteration = "
                                    + iteration);
                    System.out.println(
                            "size mismatch");

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
                "#################  LINKED LIST STACK  #####################");
        System.out.println(
                "############################################################");
        System.out.println();

        runTests(
                "Integer Stack Tests",
                buildIntegerTests());

        runTests(
                "String Stack Tests",
                buildStringTests());

        runRandomisedTests(5000);
    }
}
