import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Queue implementation using a singly linked list.
 *
 * A queue follows First In, First Out (FIFO) ordering:
 *
 *      enqueue at the rear
 *      dequeue from the front
 *
 * Operations:
 *
 *      enqueue:  O(1)
 *      dequeue:  O(1)
 *      getFront: O(1)
 *      size:     O(1)
 *      isEmpty:  O(1)
 */
public class QueueLinkedList {


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
     * Queue
     * **********************************************************************/


    static class Queue {


        private Node front;
        private Node rear;
        private int currSize;


        Queue() {
            front = null;
            rear = null;
            currSize = 0;
        }


        /**
         * Add a value at the rear of the queue.
         */
        void enqueue(int val) {

            Node newNode = node(val);

            if (isEmpty()) {

                front = newNode;
                rear = newNode;

            } else {

                rear.next = newNode;
                rear = newNode;
            }

            currSize++;
        }


        /**
         * Remove and return the value at the front of the queue.
         *
         * Returns -1 if the queue is empty.
         */
        int dequeue() {

            if (isEmpty()) {

                System.out.println("Queue is empty");
                return -1;
            }

            int removedValue = front.val;
            front = front.next;
            currSize--;

            /*
             * If the queue has become empty, rear must also be cleared.
             * Otherwise, rear would continue to reference a detached node.
             */
            if (front == null) {
                rear = null;
            }

            return removedValue;
        }


        /**
         * Return the value at the front without removing it.
         *
         * Returns -1 if the queue is empty.
         */
        int getFront() {

            if (isEmpty()) {

                System.out.println("Queue is empty");
                return -1;
            }

            return front.val;
        }


        int size() {
            return currSize;
        }


        boolean isEmpty() {
            return currSize == 0;
        }


        /**
         * Return the values in FIFO order.
         */
        List<Integer> toList() {

            List<Integer> values = new ArrayList<>();
            Node current = front;

            while (current != null) {

                values.add(current.val);
                current = current.next;
            }

            return values;
        }


        void clear() {

            front = null;
            rear = null;
            currSize = 0;
        }
    }


    /* **********************************************************************
     * Helpers
     * **********************************************************************/


    static boolean validNode(Node node) {
        return node != null;
    }


    static Node node(int value) {
        return new Node(value);
    }


    static Node node(int value, Node next) {
        return new Node(value, next);
    }


    /* **********************************************************************
     * Test Harness
     * **********************************************************************/


    static class TestCase {


        final String id;
        final String description;
        final Runnable action;
        final List<Integer> expectedState;
        final Integer expectedReturnValue;


        TestCase(
                String id,
                String description,
                Runnable action,
                List<Integer> expectedState,
                Integer expectedReturnValue) {

            this.id = id;
            this.description = description;
            this.action = action;
            this.expectedState = expectedState;
            this.expectedReturnValue = expectedReturnValue;
        }
    }


    static class TestResult {


        final String id;
        final String description;
        final boolean passed;
        final String detail;


        TestResult(
                String id,
                String description,
                boolean passed,
                String detail) {

            this.id = id;
            this.description = description;
            this.passed = passed;
            this.detail = detail;
        }
    }


    /* **********************************************************************
     * Test Runner
     * **********************************************************************/


    static void runTests(List<TestCase> tests) {

        printSeparator();
        System.out.println("Queue Linked List Tests");
        printSeparator();

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            try {

                Queue queue = new Queue();

                Object returnValue = test.action.call(queue);

                boolean stateMatches =
                        queue.toList().equals(test.expectedState);

                boolean returnValueMatches =
                        test.expectedReturnValue == null
                                || test.expectedReturnValue.equals(returnValue);

                boolean success = stateMatches && returnValueMatches;

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

                    System.out.println(
                            "  expected state = "
                                    + test.expectedState);

                    System.out.println(
                            "  actual state   = "
                                    + queue.toList());

                    System.out.println(
                            "  expected return = "
                                    + test.expectedReturnValue);

                    System.out.println(
                            "  actual return   = "
                                    + returnValue);
                }

            } catch (Exception exception) {

                failed++;

                System.out.printf(
                        "FAIL %s (%s)%n",
                        test.id,
                        test.description);

                System.out.println(
                        "  exception = "
                                + exception);
            }
        }

        printResults(passed, failed, tests.size());
    }


    /* **********************************************************************
     * Randomised Tests
     * **********************************************************************/


    static void runRandomisedTests(int iterations) {

        printSeparator();
        System.out.println("Randomised Queue Tests");
        printSeparator();

        Random random = new Random(20261106L);
        Queue queue = new Queue();
        List<Integer> expected = new ArrayList<>();

        for (int iteration = 1; iteration <= iterations; iteration++) {

            boolean shouldEnqueue =
                    expected.isEmpty()
                            || random.nextBoolean();

            if (shouldEnqueue) {

                int value = random.nextInt(2001) - 1000;

                queue.enqueue(value);
                expected.add(value);

            } else {

                int expectedValue = expected.remove(0);
                int actualValue = queue.dequeue();

                if (expectedValue != actualValue) {

                    System.out.println("Randomised test FAILED");
                    System.out.println("iteration = " + iteration);
                    System.out.println("expected dequeue = " + expectedValue);
                    System.out.println("actual dequeue   = " + actualValue);

                    return;
                }
            }

            if (queue.size() != expected.size()) {

                System.out.println("Randomised test FAILED");
                System.out.println("iteration = " + iteration);
                System.out.println("expected size = " + expected.size());
                System.out.println("actual size   = " + queue.size());

                return;
            }
        }

        System.out.printf(
                "All %d randomised operations passed.%n%n",
                iterations);
    }


    /* **********************************************************************
     * Test Data
     * **********************************************************************/


    static List<TestCase> buildTests() {

        List<TestCase> tests = new ArrayList<>();

        tests.add(new TestCase(
                "B1",
                "new queue is empty",
                queue -> null,
                List.of(),
                null));

        tests.add(new TestCase(
                "B2",
                "enqueue one value",
                queue -> {
                    queue.enqueue(10);
                    return null;
                },
                List.of(10),
                null));

        tests.add(new TestCase(
                "B3",
                "enqueue preserves insertion order",
                queue -> {
                    queue.enqueue(10);
                    queue.enqueue(20);
                    queue.enqueue(30);
                    return null;
                },
                List.of(10, 20, 30),
                null));

        tests.add(new TestCase(
                "B4",
                "dequeue removes the oldest value",
                queue -> {
                    queue.enqueue(10);
                    queue.enqueue(20);
                    return queue.dequeue();
                },
                List.of(20),
                10));

        tests.add(new TestCase(
                "B5",
                "dequeue empties a one-element queue",
                queue -> {
                    queue.enqueue(42);
                    return queue.dequeue();
                },
                List.of(),
                42));

        tests.add(new TestCase(
                "B6",
                "dequeue on an empty queue returns -1",
                queue -> queue.dequeue(),
                List.of(),
                -1));

        tests.add(new TestCase(
                "B7",
                "getFront returns without removing",
                queue -> {
                    queue.enqueue(5);
                    queue.enqueue(15);
                    return queue.getFront();
                },
                List.of(5, 15),
                5));

        tests.add(new TestCase(
                "B8",
                "getFront on an empty queue returns -1",
                queue -> queue.getFront(),
                List.of(),
                -1));

        tests.add(new TestCase(
                "B9",
                "size tracks enqueue and dequeue",
                queue -> {
                    queue.enqueue(1);
                    queue.enqueue(2);
                    queue.dequeue();
                    return queue.size();
                },
                List.of(2),
                1));

        tests.add(new TestCase(
                "B10",
                "queue can be reused after becoming empty",
                queue -> {
                    queue.enqueue(1);
                    queue.dequeue();
                    queue.enqueue(7);
                    queue.enqueue(8);
                    return queue.getFront();
                },
                List.of(7, 8),
                7));

        tests.add(new TestCase(
                "N1",
                "negative values are supported",
                queue -> {
                    queue.enqueue(-10);
                    queue.enqueue(-20);
                    return queue.dequeue();
                },
                List.of(-20),
                -10));

        return tests;
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
                "################  QUEUE LINKED LIST  #######################");

        System.out.println(
                "############################################################");
        System.out.println();

        runTests(buildTests());
        runRandomisedTests(5000);
    }
}
