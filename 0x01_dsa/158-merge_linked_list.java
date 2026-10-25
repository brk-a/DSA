import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

public class MergeTwoLinkedLists {

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
     * Merge by collecting values, sorting them, and rebuilding a list.
     *
     * This creates a new list and does not mutate the inputs.
     *
     * Time: O((n + m) log(n + m))
     * Space: O(n + m)
     */
    static Result mergeTwoLinkedListsArray(
        Node headA,
        Node headB
    ) {
        if (headA == null && headB == null) {
            return new Result(null, false);
        }

        ArrayList<Integer> values =
            new ArrayList<>();

        for (
            Node current = headA;
            current != null;
            current = current.next
        ) {
            values.add(current.val);
        }

        for (
            Node current = headB;
            current != null;
            current = current.next
        ) {
            values.add(current.val);
        }

        Collections.sort(values);

        Node dummy = node(-1);
        Node current = dummy;

        for (int value : values) {
            current.next = node(value);
            current = current.next;
        }

        return new Result(dummy.next, true);
    }

    /**
     * Recursive in-place merge.
     *
     * Assumes both input lists are already sorted.
     *
     * Time: O(n + m)
     * Space: O(n + m) recursion stack
     */
    static Result mergeTwoLinkedListsRecursion(
        Node headA,
        Node headB
    ) {
        if (headA == null && headB == null) {
            return new Result(null, false);
        }

        if (headA == null) {
            return new Result(headB, true);
        }

        if (headB == null) {
            return new Result(headA, true);
        }

        Node mergedHead =
            mergeRecursively(headA, headB);

        return new Result(mergedHead, true);
    }

    static Node mergeRecursively(
        Node headA,
        Node headB
    ) {
        if (headA == null) {
            return headB;
        }

        if (headB == null) {
            return headA;
        }

        if (headA.val <= headB.val) {
            headA.next =
                mergeRecursively(
                    headA.next,
                    headB
                );

            return headA;
        }

        headB.next =
            mergeRecursively(
                headA,
                headB.next
            );

        return headB;
    }

    /**
     * Iterative in-place merge using a dummy node.
     *
     * Assumes both input lists are already sorted.
     *
     * Time: O(n + m)
     * Space: O(1)
     */
    static Result mergeTwoLinkedListsIteration(
        Node headA,
        Node headB
    ) {
        if (headA == null && headB == null) {
            return new Result(null, false);
        }

        if (headA == null) {
            return new Result(headB, true);
        }

        if (headB == null) {
            return new Result(headA, true);
        }

        Node dummy = node(-1);
        Node tail = dummy;

        while (
            headA != null
            && headB != null
        ) {
            if (headA.val <= headB.val) {
                tail.next = headA;
                headA = headA.next;
            } else {
                tail.next = headB;
                headB = headB.next;
            }

            tail = tail.next;
        }

        tail.next =
            headA != null
                ? headA
                : headB;

        return new Result(dummy.next, true);
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

    static ArrayList<Node> toNodes(Node head) {
        ArrayList<Node> nodes =
            new ArrayList<>();

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
        final int[] valuesA;
        final int[] valuesB;
        final int[] expected;
        final boolean expectedValid;
        final String description;

        TestCase(
            String id,
            int[] valuesA,
            int[] valuesB,
            int[] expected,
            boolean expectedValid,
            String description
        ) {
            this.id = id;
            this.valuesA = valuesA;
            this.valuesB = valuesB;
            this.expected = expected;
            this.expectedValid = expectedValid;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {
        Result solve(
            Node headA,
            Node headB
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
            Node headA =
                buildList(test.valuesA);

            Node headB =
                buildList(test.valuesB);

            try {
                Result actual =
                    method.algorithm.solve(
                        headA,
                        headB
                    );

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
                Node headA =
                    buildList(test.valuesA);

                Node headB =
                    buildList(test.valuesB);

                Result result =
                    method.algorithm.solve(
                        headA,
                        headB
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
                method.algorithm.solve(
                    null,
                    null
                );

            if (
                !result.valid()
                && result.head() == null
            ) {
                passed++;

                System.out.printf(
                    "PASS I1 - %s rejects two null lists%n",
                    method.name
                );
            } else {
                failed++;

                System.out.printf(
                    "FAIL I1 - %s accepts two null lists%n",
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
            int sizeA =
                random.nextInt(50);

            int sizeB =
                random.nextInt(50);

            int[] valuesA =
                randomSortedList(random, sizeA);

            int[] valuesB =
                randomSortedList(random, sizeB);

            int[] expected =
                mergeSortedArrays(valuesA, valuesB);

            for (MethodCase method : methods) {
                Node headA =
                    buildList(valuesA);

                Node headB =
                    buildList(valuesB);

                Result result =
                    method.algorithm.solve(
                        headA,
                        headB
                    );

                boolean success =
                    result.valid()
                        == (sizeA + sizeB > 0)
                    && toValues(result.head()).equals(
                        toIntegerList(expected)
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
                        "listA = "
                            + Arrays.toString(valuesA)
                    );

                    System.out.println(
                        "listB = "
                            + Arrays.toString(valuesB)
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

    static int[] mergeSortedArrays(
        int[] first,
        int[] second
    ) {
        int[] merged =
            new int[first.length + second.length];

        int i = 0;
        int j = 0;
        int index = 0;

        while (
            i < first.length
            && j < second.length
        ) {
            if (first[i] <= second[j]) {
                merged[index++] = first[i++];
            } else {
                merged[index++] = second[j++];
            }
        }

        while (i < first.length) {
            merged[index++] = first[i++];
        }

        while (j < second.length) {
            merged[index++] = second[j++];
        }

        return merged;
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
                new int[]{1, 2, 4},
                new int[]{1, 3, 4},
                new int[]{1, 1, 2, 3, 4, 4},
                true,
                "overlapping sorted lists"
            )
        );

        tests.add(
            new TestCase(
                "B2",
                new int[]{1, 3, 5},
                new int[]{2, 4, 6},
                new int[]{1, 2, 3, 4, 5, 6},
                true,
                "interleaved sorted lists"
            )
        );

        tests.add(
            new TestCase(
                "B3",
                new int[]{},
                new int[]{1, 2, 3},
                new int[]{1, 2, 3},
                true,
                "first list is empty"
            )
        );

        tests.add(
            new TestCase(
                "B4",
                new int[]{1, 2, 3},
                new int[]{},
                new int[]{1, 2, 3},
                true,
                "second list is empty"
            )
        );

        tests.add(
            new TestCase(
                "S1",
                new int[]{},
                new int[]{},
                new int[]{},
                false,
                "both lists are empty"
            )
        );

        tests.add(
            new TestCase(
                "S2",
                new int[]{5},
                new int[]{5},
                new int[]{5, 5},
                true,
                "single-node lists with equal values"
            )
        );

        tests.add(
            new TestCase(
                "S3",
                new int[]{1, 1, 1},
                new int[]{1, 1},
                new int[]{1, 1, 1, 1, 1},
                true,
                "duplicate values"
            )
        );

        tests.add(
            new TestCase(
                "S4",
                new int[]{-10, -5, 0},
                new int[]{-7, 3, 10},
                new int[]{-10, -7, -5, 0, 3, 10},
                true,
                "negative and positive values"
            )
        );

        tests.add(
            new TestCase(
                "S5",
                new int[]{1, 2, 3},
                new int[]{4, 5, 6},
                new int[]{1, 2, 3, 4, 5, 6},
                true,
                "non-overlapping ranges"
            )
        );

        tests.add(
            new TestCase(
                "S6",
                new int[]{4, 5, 6},
                new int[]{1, 2, 3},
                new int[]{1, 2, 3, 4, 5, 6},
                true,
                "second list precedes first list"
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
            "############## MERGE TWO LINKED LISTS ######################"
        );

        System.out.println(
            "############################################################"
        );

        System.out.println();

        List<MethodCase> methods =
            List.of(
                new MethodCase(
                    "Array Sort",
                    MergeTwoLinkedLists
                        ::mergeTwoLinkedListsArray
                ),
                new MethodCase(
                    "Recursion",
                    MergeTwoLinkedLists
                        ::mergeTwoLinkedListsRecursion
                ),
                new MethodCase(
                    "Iteration",
                    MergeTwoLinkedLists
                        ::mergeTwoLinkedListsIteration
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
