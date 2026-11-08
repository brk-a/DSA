import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Random;
import java.util.function.Function;

public class MergeKLinkedLists {

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

    static boolean validArray(Node[] array) {
        return array != null && array.length > 0;
    }

    static Node node(int val) {
        return new Node(val);
    }

    static Node node(int val, Node next) {
        return new Node(val, next);
    }

    /**
     * Repeatedly selects the smallest current head.
     *
     * Time: O(Nk)
     * Space: O(1)
     */
    static Result mergeKLinkedListsSelectMin(Node[] array) {
        if (!validArray(array)) {
            return new Result(null, false);
        }

        Node[] heads = array.clone();

        Node dummy = node(0);
        Node tail = dummy;

        while (true) {
            int minIndex = -1;

            for (int i = 0; i < heads.length; i++) {
                if (
                    heads[i] != null
                    && (
                        minIndex == -1
                        || heads[i].val
                            < heads[minIndex].val
                    )
                ) {
                    minIndex = i;
                }
            }

            if (minIndex == -1) {
                break;
            }

            tail.next = heads[minIndex];
            tail = tail.next;

            heads[minIndex] =
                heads[minIndex].next;
        }

        tail.next = null;

        return new Result(dummy.next, true);
    }

    /**
     * Min-heap solution.
     *
     * Time: O(N log k)
     * Space: O(k)
     */
    static Result mergeKLinkedListsHeap(Node[] array) {
        if (!validArray(array)) {
            return new Result(null, false);
        }

        PriorityQueue<Node> minHeap =
            new PriorityQueue<>(
                (a, b) ->
                    Integer.compare(a.val, b.val)
            );

        for (Node head : array) {
            if (head != null) {
                minHeap.offer(head);
            }
        }

        Node dummy = node(0);
        Node tail = dummy;

        while (!minHeap.isEmpty()) {
            Node smallest = minHeap.poll();

            tail.next = smallest;
            tail = smallest;

            if (smallest.next != null) {
                minHeap.offer(smallest.next);
            }
        }

        tail.next = null;

        return new Result(dummy.next, true);
    }

    /**
     * Divide-and-conquer solution.
     *
     * Repeatedly merges adjacent pairs until one list remains.
     *
     * Time: O(N log k)
     * Space: O(1) beyond the input array
     */
    static Result mergeKLinkedListsDivAndConq(Node[] array) {
        if (!validArray(array)) {
            return new Result(null, false);
        }

        Node[] lists = array.clone();

        if (lists.length == 0) {
            return new Result(null, false);
        }

        int count = lists.length;

        while (count > 1) {
            int writeIndex = 0;

            for (
                int i = 0;
                i < count;
                i += 2
            ) {
                Node first = lists[i];
                Node second =
                    i + 1 < count
                        ? lists[i + 1]
                        : null;

                lists[writeIndex++] =
                    mergeTwoLists(first, second);
            }

            count = writeIndex;
        }

        return new Result(lists[0], true);
    }

    static Node mergeTwoLists(
        Node first,
        Node second
    ) {
        Node dummy = node(0);
        Node tail = dummy;

        while (
            first != null
            && second != null
        ) {
            if (first.val <= second.val) {
                tail.next = first;
                first = first.next;
            } else {
                tail.next = second;
                second = second.next;
            }

            tail = tail.next;
        }

        tail.next =
            first != null
                ? first
                : second;

        return dummy.next;
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

    static boolean isSorted(ArrayList<Integer> values) {
        for (
            int i = 1;
            i < values.size();
            i++
        ) {
            if (
                values.get(i - 1)
                > values.get(i)
            ) {
                return false;
            }
        }

        return true;
    }

    /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    static class TestCase {
        final String id;
        final int[][] lists;
        final int[] expected;
        final boolean expectedValid;
        final String description;

        TestCase(
            String id,
            int[][] lists,
            int[] expected,
            boolean expectedValid,
            String description
        ) {
            this.id = id;
            this.lists = lists;
            this.expected = expected;
            this.expectedValid = expectedValid;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {
        Result solve(Node[] lists);
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
            Node[] input = buildLists(test.lists);

            try {
                Result actual =
                    method.algorithm.solve(input);

                boolean success =
                    actual.valid()
                        == test.expectedValid
                    && toValues(actual.head()).equals(
                        toIntegerList(test.expected)
                    )
                    && !hasCycle(actual.head())
                    && isSorted(
                        toValues(actual.head())
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
                Node[] input =
                    buildLists(test.lists);

                Result result =
                    method.algorithm.solve(input);

                ArrayList<Integer> values =
                    toValues(result.head());

                results.add(values);

                if (
                    result.valid()
                        != test.expectedValid
                    || !values.equals(
                        toIntegerList(test.expected)
                    )
                    || !isSorted(values)
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
                method.algorithm.solve(null);

            if (
                !result.valid()
                && result.head() == null
            ) {
                passed++;

                System.out.printf(
                    "PASS I1 - %s rejects null array%n",
                    method.name
                );
            } else {
                failed++;

                System.out.printf(
                    "FAIL I1 - %s accepts null array%n",
                    method.name
                );
            }

            result =
                method.algorithm.solve(new Node[0]);

            if (
                !result.valid()
                && result.head() == null
            ) {
                passed++;

                System.out.printf(
                    "PASS I2 - %s rejects empty array%n",
                    method.name
                );
            } else {
                failed++;

                System.out.printf(
                    "FAIL I2 - %s accepts empty array%n",
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
            new Random(20261024L);

        for (
            int iteration = 1;
            iteration <= iterations;
            iteration++
        ) {
            int listCount =
                random.nextInt(10) + 1;

            Node[] lists =
                new Node[listCount];

            ArrayList<Integer> expected =
                new ArrayList<>();

            for (int i = 0; i < listCount; i++) {
                int size =
                    random.nextInt(20);

                int[] values =
                    randomSortedList(random, size);

                lists[i] =
                    buildList(values);

                expected.addAll(
                    toIntegerList(values)
                );
            }

            expected.sort(null);

            for (MethodCase method : methods) {
                Node[] input =
                    lists.clone();

                Result result =
                    method.algorithm.solve(input);

                boolean success =
                    result.valid()
                    && toValues(result.head()).equals(
                        expected
                    )
                    && isSorted(
                        toValues(result.head())
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
                        "expected = "
                            + expected
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

    static int[] randomSortedList(
        Random random,
        int size
    ) {
        int[] values =
            new int[size];

        for (int i = 0; i < size; i++) {
            values[i] =
                random.nextInt(201) - 100;
        }

        Arrays.sort(values);
        return values;
    }

    static Node[] buildLists(int[][] lists) {
        Node[] result =
            new Node[lists.length];

        for (int i = 0; i < lists.length; i++) {
            result[i] =
                buildList(lists[i]);
        }

        return result;
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
                new int[][]{
                    {1, 4, 5},
                    {1, 3, 4},
                    {2, 6}
                },
                new int[]{1, 1, 2, 3, 4, 4, 5, 6},
                true,
                "classic three-list merge"
            )
        );

        tests.add(
            new TestCase(
                "B2",
                new int[][]{
                    {1, 3, 5},
                    {2, 4, 6}
                },
                new int[]{1, 2, 3, 4, 5, 6},
                true,
                "two interleaved lists"
            )
        );

        tests.add(
            new TestCase(
                "B3",
                new int[][]{
                    {},
                    {1, 2, 3}
                },
                new int[]{1, 2, 3},
                true,
                "one null head"
            )
        );

        tests.add(
            new TestCase(
                "B4",
                new int[][]{
                    {},
                    {},
                    {}
                },
                new int[]{},
                true,
                "all lists are empty"
            )
        );

        tests.add(
            new TestCase(
                "S1",
                new int[][]{
                    {5}
                },
                new int[]{5},
                true,
                "single list"
            )
        );

        tests.add(
            new TestCase(
                "S2",
                new int[][]{
                    {7},
                    {7},
                    {7}
                },
                new int[]{7, 7, 7},
                true,
                "duplicate values"
            )
        );

        tests.add(
            new TestCase(
                "S3",
                new int[][]{
                    {-10, -5},
                    {0},
                    {-7, 3}
                },
                new int[]{-10, -7, -5, 0, 3},
                true,
                "negative and positive values"
            )
        );

        tests.add(
            new TestCase(
                "S4",
                new int[][]{
                    {1, 2, 3},
                    {4, 5, 6}
                },
                new int[]{1, 2, 3, 4, 5, 6},
                true,
                "non-overlapping ranges"
            )
        );

        tests.add(
            new TestCase(
                "S5",
                new int[][]{
                    {4, 5, 6},
                    {1, 2, 3}
                },
                new int[]{1, 2, 3, 4, 5, 6},
                true,
                "second list precedes first"
            )
        );

        tests.add(
            new TestCase(
                "E1",
                new int[][]{},
                new int[]{},
                false,
                "empty array"
            )
        );

        tests.add(
            new TestCase(
                "E2",
                null,
                new int[]{},
                false,
                "null array"
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
            "############## MERGE K LINKED LISTS ########################"
        );

        System.out.println(
            "############################################################"
        );

        System.out.println();

        List<MethodCase> methods =
            List.of(
                new MethodCase(
                    "Select Minimum",
                    MergeKLinkedLists
                        ::mergeKLinkedListsSelectMin
                ),
                new MethodCase(
                    "Min Heap",
                    MergeKLinkedLists
                        ::mergeKLinkedListsHeap
                ),
                new MethodCase(
                    "Divide and Conquer",
                    MergeKLinkedLists
                        ::mergeKLinkedListsDivAndConq
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
