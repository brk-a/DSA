import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Random;
import java.util.function.BiFunction;

public class BinaryTreeToDoublyLinkedList {

    /* **********************************************************************
     * Linked List Node
     * **********************************************************************/

    static class LNode {
        int val;
        LNode prev;
        LNode next;

        LNode(int val) {
            this.val = val;
            this.prev = null;
            this.next = null;
        }

        LNode(int val, LNode prev, LNode next) {
            this.val = val;
            this.prev = prev;
            this.next = next;
        }
    }

    /* **********************************************************************
     * Tree Node
     * **********************************************************************/

    static class TNode {
        int val;
        TNode left;
        TNode right;

        TNode(int val) {
            this.val = val;
            this.left = null;
            this.right = null;
        }

        TNode(int val, TNode left, TNode right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    /* **********************************************************************
     * Result
     * **********************************************************************/

    static record Result(
        LNode head,
        boolean valid
    ) {}

    /* **********************************************************************
     * Helpers
     * **********************************************************************/

    static boolean validNode(TNode node) {
        return node != null;
    }

    static LNode lnode(int val) {
        return new LNode(val);
    }

    static LNode lnode(int val, LNode prev, LNode next) {
        return new LNode(val, prev, next);
    }

    static TNode tnode(int val) {
        return new TNode(val);
    }

    static TNode tnode(int val, TNode left, TNode right) {
        return new TNode(val, left, right);
    }

    /**
     * Converts a binary tree into a doubly linked list using recursive
     * in-order traversal.
     *
     * Time: O(n)
     * Extra space: O(h), where h is the height of the tree.
     */
    static Result binTreeToDoublyLListRecursion(TNode root) {
        if (!validNode(root)) {
            return new Result(null, false);
        }

        LNode[] tail = new LNode[1];
        LNode head = buildRecursively(root, tail);

        return new Result(head, true);
    }

    private static LNode buildRecursively(
        TNode root,
        LNode[] tail
    ) {
        if (root == null) {
            return null;
        }

        LNode head = buildRecursively(root.left, tail);
        LNode current = lnode(root.val);

        if (tail[0] == null) {
            head = current;
        } else {
            tail[0].next = current;
            current.prev = tail[0];
        }

        tail[0] = current;

        buildRecursively(root.right, tail);

        return head;
    }

    /**
     * Converts a binary tree into a doubly linked list using iterative
     * in-order traversal and an explicit stack.
     *
     * Time: O(n)
     * Extra space: O(h), where h is the height of the tree.
     */
    static Result binTreeToDoublyLListPrevPointer(TNode root) {
        if (!validNode(root)) {
            return new Result(null, false);
        }

        Deque<TNode> stack = new ArrayDeque<>();
        TNode current = root;
        LNode head = null;
        LNode previous = null;

        while (current != null || !stack.isEmpty()) {
            while (current != null) {
                stack.push(current);
                current = current.left;
            }

            current = stack.pop();
            LNode node = lnode(current.val);

            if (previous == null) {
                head = node;
            } else {
                previous.next = node;
                node.prev = previous;
            }

            previous = node;
            current = current.right;
        }

        return new Result(head, true);
    }

    /**
     * Converts a binary tree into a doubly linked list using Morris
     * in-order traversal.
     *
     * Time: O(n)
     * Extra space: O(1)
     */
    static Result binTreeToDoublyLListMorrisTraversal(TNode root) {
        if (!validNode(root)) {
            return new Result(null, false);
        }

        TNode current = root;
        LNode head = null;
        LNode previous = null;

        while (current != null) {
            if (current.left == null) {
                LNode node = lnode(current.val);

                if (previous == null) {
                    head = node;
                } else {
                    previous.next = node;
                    node.prev = previous;
                }

                previous = node;
                current = current.right;
            } else {
                TNode predecessor = current.left;

                while (
                    predecessor.right != null
                    && predecessor.right != current
                ) {
                    predecessor = predecessor.right;
                }

                if (predecessor.right == null) {
                    predecessor.right = current;
                    current = current.left;
                } else {
                    predecessor.right = null;

                    LNode node = lnode(current.val);

                    if (previous == null) {
                        head = node;
                    } else {
                        previous.next = node;
                        node.prev = previous;
                    }

                    previous = node;
                    current = current.right;
                }
            }
        }

        return new Result(head, true);
    }

    /* **********************************************************************
     * Tree Helpers
     * **********************************************************************/

    static TNode buildTree(Integer... values) {
        if (values == null || values.length == 0) {
            return null;
        }

        TNode root = tnode(values[0]);
        Deque<TNode> queue = new ArrayDeque<>();
        queue.add(root);

        int index = 1;

        while (!queue.isEmpty() && index < values.length) {
            TNode current = queue.poll();

            if (index < values.length) {
                Integer leftValue = values[index++];

                if (leftValue != null) {
                    current.left = tnode(leftValue);
                    queue.add(current.left);
                }
            }

            if (index < values.length) {
                Integer rightValue = values[index++];

                if (rightValue != null) {
                    current.right = tnode(rightValue);
                    queue.add(current.right);
                }
            }
        }

        return root;
    }

    static ArrayList<Integer> toValues(LNode head) {
        ArrayList<Integer> values = new ArrayList<>();

        LNode current = head;

        while (current != null) {
            values.add(current.val);
            current = current.next;
        }

        return values;
    }

    static ArrayList<LNode> toNodes(LNode head) {
        ArrayList<LNode> nodes = new ArrayList<>();

        LNode current = head;

        while (current != null) {
            nodes.add(current);
            current = current.next;
        }

        return nodes;
    }

    static boolean isProperlyLinked(LNode head) {
        if (head == null) {
            return true;
        }

        if (head.prev != null) {
            return false;
        }

        LNode current = head;

        while (current.next != null) {
            if (current.next.prev != current) {
                return false;
            }

            current = current.next;
        }

        return true;
    }

    static boolean hasCycle(LNode head) {
        LNode slow = head;
        LNode fast = head;

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

    static ArrayList<Integer> inorderValues(TNode root) {
        ArrayList<Integer> values = new ArrayList<>();
        collectInorder(root, values);

        return values;
    }

    private static void collectInorder(
        TNode root,
        ArrayList<Integer> values
    ) {
        if (root == null) {
            return;
        }

        collectInorder(root.left, values);
        values.add(root.val);
        collectInorder(root.right, values);
    }

    /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    static class TestCase {
        final String id;
        final Integer[] values;
        final int[] expected;
        final boolean expectedValid;
        final String description;

        TestCase(
            String id,
            Integer[] values,
            int[] expected,
            boolean expectedValid,
            String description
        ) {
            this.id = id;
            this.values = values;
            this.expected = expected;
            this.expectedValid = expectedValid;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {
        Result solve(TNode root);
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
            TNode input = buildTree(test.values);

            try {
                Result actual = method.algorithm.solve(input);

                boolean success =
                    actual.valid() == test.expectedValid
                    && toValues(actual.head()).equals(
                        toIntegerList(test.expected)
                    )
                    && isProperlyLinked(actual.head())
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
        List<TestCase> tests = new ArrayList<>();

        tests.add(
            new TestCase(
                "B1",
                new Integer[]{4, 2, 6, 1, 3, 5, 7},
                new int[]{1, 2, 3, 4, 5, 6, 7},
                true,
                "complete binary tree"
            )
        );

        tests.add(
            new TestCase(
                "B2",
                new Integer[]{2, 1, 3},
                new int[]{1, 2, 3},
                true,
                "three-node binary tree"
            )
        );

        tests.add(
            new TestCase(
                "B3",
                new Integer[]{1, null, 2, null, 3},
                new int[]{1, 2, 3},
                true,
                "right-skewed tree"
            )
        );

        tests.add(
            new TestCase(
                "B4",
                new Integer[]{3, 2, null, 1},
                new int[]{1, 2, 3},
                true,
                "left-skewed tree"
            )
        );

        tests.add(
            new TestCase(
                "S1",
                new Integer[]{42},
                new int[]{42},
                true,
                "single-node tree"
            )
        );

        tests.add(
            new TestCase(
                "S2",
                new Integer[]{10, 5, 20, 3, 8, 15, 25},
                new int[]{3, 5, 8, 10, 15, 20, 25},
                true,
                "binary search tree"
            )
        );

        tests.add(
            new TestCase(
                "S3",
                new Integer[]{7, 7, 7},
                new int[]{7, 7, 7},
                true,
                "duplicate values"
            )
        );

        tests.add(
            new TestCase(
                "S4",
                new Integer[]{-5, -10, 0},
                new int[]{-10, -5, 0},
                true,
                "negative and positive values"
            )
        );

        tests.add(
            new TestCase(
                "E1",
                new Integer[]{},
                new int[]{},
                false,
                "empty tree"
            )
        );

        tests.add(
            new TestCase(
                "E2",
                null,
                new int[]{},
                false,
                "null tree"
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

        Random random = new Random(20261124L);

        for (
            int iteration = 1;
            iteration <= iterations;
            iteration++
        ) {
            int size = random.nextInt(100) + 1;
            Integer[] values = new Integer[size];

            for (int index = 0; index < size; index++) {
                values[index] = random.nextInt(2001) - 1000;
            }

            TNode input = buildTree(values);
            int[] expected = toIntArray(inorderValues(input));

            Result result = method.algorithm.solve(input);

            boolean success =
                result.valid()
                && toValues(result.head()).equals(
                    toIntegerList(expected)
                )
                && isProperlyLinked(result.head())
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
                    "input = "
                        + Arrays.toString(values)
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

    static int[] toIntArray(ArrayList<Integer> values) {
        int[] result = new int[values.size()];

        for (int index = 0; index < values.size(); index++) {
            result[index] = values.get(index);
        }

        return result;
    }

    static ArrayList<Integer> toIntegerList(int[] values) {
        ArrayList<Integer> result = new ArrayList<>();

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
            "############ BINARY TREE TO DOUBLY LINKED LIST #############"
        );

        System.out.println(
            "############################################################"
        );

        System.out.println();

        List<MethodCase> methods = new ArrayList<>();

        methods.add(
            new MethodCase(
                "Recursive In-order Traversal",
                BinaryTreeToDoublyLinkedList
                    ::binTreeToDoublyLListRecursion
            )
        );

        methods.add(
            new MethodCase(
                "Iterative Previous Pointer",
                BinaryTreeToDoublyLinkedList
                    ::binTreeToDoublyLListPrevPointer
            )
        );

        methods.add(
            new MethodCase(
                "Morris Traversal",
                BinaryTreeToDoublyLinkedList
                    ::binTreeToDoublyLListMorrisTraversal
            )
        );

        List<TestCase> tests = buildTests();

        for (MethodCase method : methods) {
            runTests(method, tests);
            runRandomisedTests(method, 1000);
        }
    }
}
