import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

public class PairSumDoublyLinkedList {

    /* **********************************************************************
     * Node
     * **********************************************************************/

    static class Node {
        int val;
        Node prev;
        Node next;

        Node(int val) {
            this.val = val;
            this.prev = null;
            this.next = null;
        }

        Node(int val, Node prev, Node next) {
            this.val = val;
            this.prev = prev;
            this.next = next;
        }
    }

    /* **********************************************************************
     * Result
     * **********************************************************************/

    static record Result(
        ArrayList<ArrayList<Integer>> sums,
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
        return new Node(val, null, next);
    }

    static ArrayList<ArrayList<Integer>> emptyPairs() {
        return new ArrayList<>();
    }

    static ArrayList<Integer> pair(int first, int second) {
        ArrayList<Integer> values = new ArrayList<>();

        if (first <= second) {
            values.add(first);
            values.add(second);
        } else {
            values.add(second);
            values.add(first);
        }

        return values;
    }

    static void sortPairs(ArrayList<ArrayList<Integer>> pairs) {
        for (ArrayList<Integer> pair : pairs) {
            Collections.sort(pair);
        }

        pairs.sort(
            (left, right) -> {
                int firstComparison =
                    Integer.compare(left.get(0), right.get(0));

                if (firstComparison != 0) {
                    return firstComparison;
                }

                return Integer.compare(left.get(1), right.get(1));
            }
        );
    }

    /* **********************************************************************
     * Algorithms
     * **********************************************************************/

    /**
     * Finds every value pair whose sum equals the target.
     *
     * Works for any doubly linked list, sorted or unsorted.
     *
     * Time: O(n^2)
     * Extra space: O(k), where k is the number of matching pairs
     */
    static Result pairSumBruteForce(Node head, int target) {
        if (!validNode(head)) {
            return new Result(emptyPairs(), false);
        }

        ArrayList<ArrayList<Integer>> result = emptyPairs();

        for (Node first = head; first != null; first = first.next) {
            for (Node second = first.next; second != null; second = second.next) {
                if (first.val + second.val == target) {
                    result.add(pair(first.val, second.val));
                }
            }
        }

        sortPairs(result);

        return new Result(result, true);
    }

    /**
     * Finds every value pair whose sum equals the target.
     *
     * Works for any doubly linked list, sorted or unsorted.
     *
     * Time: O(n)
     * Extra space: O(n)
     */
    static Result pairSumHashing(Node head, int target) {
        if (!validNode(head)) {
            return new Result(emptyPairs(), false);
        }

        HashSet<Integer> seen = new HashSet<>();
        ArrayList<ArrayList<Integer>> result = emptyPairs();

        for (Node current = head; current != null; current = current.next) {
            int complement = target - current.val;

            if (seen.contains(complement)) {
                result.add(pair(complement, current.val));
            }

            seen.add(current.val);
        }

        sortPairs(result);

        return new Result(result, true);
    }

    /**
     * Finds every value pair whose sum equals the target.
     *
     * Requires the doubly linked list to be sorted in ascending order.
     *
     * Time: O(n)
     * Extra space: O(k), where k is the number of matching pairs
     */
    static Result pairSumTwoPointers(Node head, int target) {
        if (!validNode(head)) {
            return new Result(emptyPairs(), false);
        }

        Node left = head;
        Node right = head;

        while (right.next != null) {
            right = right.next;
        }

        ArrayList<ArrayList<Integer>> result = emptyPairs();

        while (left != null && right != null && left != right && left.prev != right) {
            int sum = left.val + right.val;

            if (sum == target) {
                result.add(pair(left.val, right.val));

                left = left.next;
                right = right.prev;
            } else if (sum < target) {
                left = left.next;
            } else {
                right = right.prev;
            }
        }

        sortPairs(result);

        return new Result(result, true);
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
                current.prev = tail;
                tail = current;
            }
        }

        return head;
    }

    static ArrayList<Integer> toValues(Node head) {
        ArrayList<Integer> values = new ArrayList<>();

        for (Node current = head; current != null; current = current.next) {
            values.add(current.val);
        }

        return values;
    }

    static boolean isSortedAscending(Node head) {
        for (Node current = head; current != null && current.next != null; current = current.next) {
            if (current.val > current.next.val) {
                return false;
            }
        }

        return true;
    }

    static boolean hasValidDoubleLinks(Node head) {
        Node previous = null;

        for (Node current = head; current != null; current = current.next) {
            if (current.prev != previous) {
                return false;
            }

            previous = current;
        }

        return true;
    }

    /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    static class TestCase {
        final String id;
        final int[] values;
        final int target;
        final ArrayList<ArrayList<Integer>> expected;
        final boolean expectedValid;
        final String description;

        TestCase(
            String id,
            int[] values,
            int target,
            int[][] expectedPairs,
            boolean expectedValid,
            String description
        ) {
            this.id = id;
            this.values = values;
            this.target = target;
            this.expected = toPairList(expectedPairs);
            this.expectedValid = expectedValid;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {
        Result solve(Node head, int target);
    }

    static class MethodCase {
        final String name;
        final Algorithm algorithm;

        MethodCase(String name, Algorithm algorithm) {
            this.name = name;
            this.algorithm = algorithm;
        }
    }

    static ArrayList<ArrayList<Integer>> toPairList(int[][] pairs) {
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();

        for (int[] pair : pairs) {
            result.add(pair(pair[0], pair[1]));
        }

        sortPairs(result);

        return result;
    }

    static void runTests(MethodCase method, List<TestCase> tests) {
        printSeparator();
        System.out.println(method.name);
        printSeparator();

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {
            Node input = buildList(test.values);

            try {
                Result actual = method.algorithm.solve(input, test.target);

                boolean success =
                    actual.valid() == test.expectedValid
                    && actual.sums().equals(test.expected)
                    && hasValidDoubleLinks(input)
                    && (test.expectedValid || input == null);

                if (success) {
                    passed++;

                    System.out.printf(
                        "PASS %s (%s)%n",
                        test.id,
                        test.description
                    );

                    System.out.println(
                        "  result = " + actual.sums()
                    );
                } else {
                    failed++;

                    System.out.printf(
                        "FAIL %s (%s)%n",
                        test.id,
                        test.description
                    );

                    System.out.println(
                        "  input    = " + Arrays.toString(test.values)
                    );

                    System.out.println(
                        "  target   = " + test.target
                    );

                    System.out.println(
                        "  expected = " + test.expected
                            + ", valid = " + test.expectedValid
                    );

                    System.out.println(
                        "  actual   = " + actual.sums()
                            + ", valid = " + actual.valid()
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
                    "  exception = " + exception
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
                new int[]{1, 2, 3, 4, 5},
                6,
                new int[][]{{1, 5}, {2, 4}},
                true,
                "sorted list with multiple pairs"
            )
        );

        tests.add(
            new TestCase(
                "B2",
                new int[]{1, 2, 3, 4, 5},
                7,
                new int[][]{{2, 5}, {3, 4}},
                true,
                "sorted list with pairs that cross the midpoint"
            )
        );

        tests.add(
            new TestCase(
                "B3",
                new int[]{1, 1, 2, 2},
                3,
                new int[][]{{1, 2}, {1, 2}},
                true,
                "duplicate values"
            )
        );

        tests.add(
            new TestCase(
                "B4",
                new int[]{-5, -2, 0, 3, 8},
                3,
                new int[][]{{-5, 8}, {0, 3}},
                true,
                "negative and positive values"
            )
        );

        tests.add(
            new TestCase(
                "B5",
                new int[]{5, 1, 4, 2},
                6,
                new int[][]{{1, 5}, {2, 4}},
                true,
                "unsorted list, brute force and hashing only"
            )
        );

        tests.add(
            new TestCase(
                "S1",
                new int[]{4},
                8,
                new int[][]{},
                true,
                "single-node list cannot form a pair"
            )
        );

        tests.add(
            new TestCase(
                "S2",
                new int[]{3, 5},
                8,
                new int[][]{{3, 5}},
                true,
                "two-node list"
            )
        );

        tests.add(
            new TestCase(
                "E1",
                new int[]{},
                10,
                new int[][]{},
                false,
                "empty list"
            )
        );

        tests.add(
            new TestCase(
                "E2",
                null,
                10,
                new int[][]{},
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
        int iterations,
        boolean requireSorted
    ) {
        printSeparator();
        System.out.println("Randomised Tests");
        printSeparator();

        Random random = new Random(20261119L);

        for (int iteration = 1; iteration <= iterations; iteration++) {
            int size = random.nextInt(30);
            int[] values = new int[size];

            for (int index = 0; index < size; index++) {
                values[index] = random.nextInt(41) - 20;
            }

            if (requireSorted) {
                Arrays.sort(values);
            }

            int target = random.nextInt(41) - 20;

            Node input = buildList(values);
            Result expected = pairSumBruteForce(input, target);
            Result actual = method.algorithm.solve(input, target);

            boolean success =
                actual.valid() == expected.valid()
                && actual.sums().equals(expected.sums())
                && hasValidDoubleLinks(input);

            if (!success) {
                System.out.println("Randomised test FAILED");

                System.out.println(
                    "iteration = " + iteration
                );

                System.out.println(
                    "input = " + Arrays.toString(values)
                );

                System.out.println(
                    "target = " + target
                );

                System.out.println(
                    "expected = " + expected.sums()
                );

                System.out.println(
                    "actual = " + actual.sums()
                );

                return;
            }
        }

        System.out.printf(
            "All %d randomised tests passed.%n%n",
            iterations
        );
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
            "################ PAIR SUM DOUBLY LINKED LIST ###############"
        );

        System.out.println(
            "############################################################"
        );

        System.out.println();

        List<TestCase> tests = buildTests();

        List<MethodCase> methods = List.of(
            new MethodCase(
                "Brute Force",
                PairSumDoublyLinkedList::pairSumBruteForce
            ),
            new MethodCase(
                "Hashing",
                PairSumDoublyLinkedList::pairSumHashing
            ),
            new MethodCase(
                "Two Pointers",
                PairSumDoublyLinkedList::pairSumTwoPointers
            )
        );

        for (MethodCase method : methods) {
            runTests(method, tests);
        }

        runRandomisedTests(
            methods.get(0),
            2000,
            false
        );

        runRandomisedTests(
            methods.get(1),
            2000,
            false
        );

        runRandomisedTests(
            methods.get(2),
            2000,
            true
        );
    }
}
