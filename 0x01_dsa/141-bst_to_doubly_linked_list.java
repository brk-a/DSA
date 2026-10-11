import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Converts a BST into a sorted, linear doubly linked list.
 *
 * Each implementation creates new ListNodes, so the original tree is
 * unchanged. In the resulting list, prev points to the preceding value
 * and next points to the following value.
 *
 * Recursive in-order: O(n) time, O(h) auxiliary space.
 * Iterative in-order: O(n) time, O(h) auxiliary space.
 * Morris in-order:    O(n) time, O(1) auxiliary space, excluding output.
 */
public class BSTToDoublyLinkedList {

    static class TreeNode {
        int val;
        TreeNode left;
        TreeNode right;

        TreeNode(int val) {
            this.val = val;
        }

        TreeNode(TreeNode left, TreeNode right, int val) {
            this.left = left;
            this.right = right;
            this.val = val;
        }
    }

    static class ListNode {
        int val;
        ListNode prev;
        ListNode next;

        ListNode(int val) {
            this.val = val;
        }

        ListNode(ListNode prev, ListNode next, int val) {
            this.prev = prev;
            this.next = next;
            this.val = val;
        }
    }

    static record Result(ListNode head, boolean valid) {}

    static boolean validNode(TreeNode node) {
        return node != null;
    }

    static boolean validNode(ListNode node) {
        return node != null;
    }

    static TreeNode treeNode(int val) {
        return new TreeNode(val);
    }

    static TreeNode treeNode(TreeNode left, TreeNode right, int val) {
        return new TreeNode(left, right, val);
    }

    static ListNode listNode(int val) {
        return new ListNode(val);
    }

    static ListNode listNode(ListNode prev, ListNode next, int val) {
        return new ListNode(prev, next, val);
    }

    /**
     * Converts using recursive in-order traversal.
     */
    static Result bSTToDoublyLikedListRecursion(TreeNode root) {
        if (root == null) {
            return new Result(null, true);
        }

        ListBuilder builder = new ListBuilder();
        appendRecursively(root, builder);
        return new Result(builder.head, true);
    }

    private static void appendRecursively(TreeNode node, ListBuilder builder) {
        if (node == null) {
            return;
        }

        appendRecursively(node.left, builder);
        builder.append(node.val);
        appendRecursively(node.right, builder);
    }

    /**
     * Converts using an explicit stack for in-order traversal.
     */
    static Result bSTToDoublyLikedListInOrder(TreeNode root) {
        if (root == null) {
            return new Result(null, true);
        }

        ListBuilder builder = new ListBuilder();
        ArrayDeque<TreeNode> stack = new ArrayDeque<>();
        TreeNode current = root;

        while (current != null || !stack.isEmpty()) {
            while (current != null) {
                stack.push(current);
                current = current.left;
            }

            current = stack.pop();
            builder.append(current.val);
            current = current.right;
        }

        return new Result(builder.head, true);
    }

    /**
     * Converts using Morris in-order traversal.
     *
     * Temporary predecessor threads are removed before traversal proceeds.
     * The traversal requires O(1) auxiliary space, excluding the output list.
     */
    static Result bSTToDoublyLikedListMorris(TreeNode root) {
        if (root == null) {
            return new Result(null, true);
        }

        ListBuilder builder = new ListBuilder();
        TreeNode current = root;

        while (current != null) {
            if (current.left == null) {
                builder.append(current.val);
                current = current.right;
            } else {
                TreeNode predecessor = current.left;

                while (predecessor.right != null
                        && predecessor.right != current) {
                    predecessor = predecessor.right;
                }

                if (predecessor.right == null) {
                    predecessor.right = current;
                    current = current.left;
                } else {
                    predecessor.right = null;
                    builder.append(current.val);
                    current = current.right;
                }
            }
        }

        return new Result(builder.head, true);
    }

    /**
     * Maintains the head and tail while appending values in sorted order.
     */
    private static class ListBuilder {
        ListNode head;
        ListNode tail;

        void append(int value) {
            ListNode node = new ListNode(value);
            node.prev = tail;

            if (tail == null) {
                head = node;
            } else {
                tail.next = node;
            }

            tail = node;
        }
    }

    /* Test harness */

    @FunctionalInterface
    interface Algorithm {
        Result solve(TreeNode root);
    }

    static class MethodCase {
        final String name;
        final Algorithm algorithm;

        MethodCase(String name, Algorithm algorithm) {
            this.name = name;
            this.algorithm = algorithm;
        }
    }

    static class TestCase {
        final String id;
        final TreeNode root;
        final int[] expected;
        final String description;

        TestCase(String id, TreeNode root, int[] expected, String description) {
            this.id = id;
            this.root = root;
            this.expected = expected;
            this.description = description;
        }
    }

    static List<MethodCase> methods() {
        return List.of(
                new MethodCase(
                        "Recursive in-order",
                        BSTToDoublyLinkedList::bSTToDoublyLikedListRecursion),
                new MethodCase(
                        "Iterative in-order",
                        BSTToDoublyLinkedList::bSTToDoublyLikedListInOrder),
                new MethodCase(
                        "Morris in-order",
                        BSTToDoublyLinkedList::bSTToDoublyLikedListMorris)
        );
    }

    static List<TestCase> buildTests() {
        List<TestCase> tests = new ArrayList<>();

        TreeNode root = treeNode(
                treeNode(
                        treeNode(5),
                        treeNode(15),
                        10),
                treeNode(
                        treeNode(25),
                        treeNode(35),
                        30),
                20);

        tests.add(new TestCase(
                "B1", root,
                new int[]{5, 10, 15, 20, 25, 30, 35},
                "balanced BST"));

        tests.add(new TestCase(
                "B2", treeNode(42),
                new int[]{42},
                "single-node tree"));

        tests.add(new TestCase(
                "B3", null,
                new int[]{},
                "empty tree"));

        TreeNode leftSkewed = treeNode(
                treeNode(
                        treeNode(10),
                        null,
                        20),
                null,
                30);

        tests.add(new TestCase(
                "B4", leftSkewed,
                new int[]{10, 20, 30},
                "left-skewed tree"));

        TreeNode rightSkewed = treeNode(
                null,
                treeNode(
                        null,
                        treeNode(30),
                        20),
                10);

        tests.add(new TestCase(
                "B5", rightSkewed,
                new int[]{10, 20, 30},
                "right-skewed tree"));

        TreeNode duplicates = treeNode(
                treeNode(4),
                treeNode(
                        treeNode(6),
                        null,
                        5),
                5);

        tests.add(new TestCase(
                "B6", duplicates,
                new int[]{4, 5, 5, 6},
                "duplicate values"));

        TreeNode negativeValues = treeNode(
                treeNode(-20),
                treeNode(
                        treeNode(-7),
                        treeNode(-1),
                        -5),
                -10);

        tests.add(new TestCase(
                "B7", negativeValues,
                new int[]{-20, -10, -7, -5, -1},
                "negative values"));

        return tests;
    }

    static void runTests(MethodCase method, List<TestCase> tests) {
        int passed = 0;
        int failed = 0;

        System.out.println();
        System.out.println("== " + method.name + " ==");

        for (TestCase test : tests) {
            Result result = method.algorithm.solve(test.root);
            boolean success = result.valid()
                    && listMatches(result.head(), test.expected);

            if (success) {
                passed++;
                System.out.printf("PASS %s — %s%n", test.id, test.description);
            } else {
                failed++;
                System.out.printf("FAIL %s — %s%n", test.id, test.description);
                System.out.println("  expected: " + arrayToString(test.expected));
                System.out.println("  actual:   " + listToString(result.head()));
                System.out.println("  valid:    " + result.valid());
            }
        }

        System.out.printf(
                "Results: %d passed, %d failed, %d total%n",
                passed, failed, tests.size());
    }

    static boolean listMatches(ListNode head, int[] expected) {
        if (expected.length == 0) {
            return head == null;
        }

        if (head == null || head.prev != null) {
            return false;
        }

        ListNode current = head;
        ListNode previous = null;
        int index = 0;

        while (current != null) {
            if (index >= expected.length
                    || current.val != expected[index]
                    || current.prev != previous) {
                return false;
            }

            previous = current;
            current = current.next;
            index++;
        }

        if (index != expected.length || previous == null
                || previous.next != null) {
            return false;
        }

        // Check backward links and values as well.
        current = previous;
        for (int i = expected.length - 1; i >= 0; i--) {
            if (current == null || current.val != expected[i]) {
                return false;
            }
            current = current.prev;
        }

        return current == null;
    }

    static String listToString(ListNode head) {
        StringBuilder result = new StringBuilder("[");
        ListNode current = head;

        while (current != null) {
            if (result.length() > 1) {
                result.append(", ");
            }
            result.append(current.val);
            current = current.next;
        }

        return result.append(']').toString();
    }

    static String arrayToString(int[] values) {
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < values.length; i++) {
            if (i > 0) {
                result.append(", ");
            }
            result.append(values[i]);
        }
        return result.append(']').toString();
    }

    static TreeNode insert(TreeNode root, int value) {
        if (root == null) {
            return treeNode(value);
        }

        if (value < root.val) {
            root.left = insert(root.left, value);
        } else {
            // Equal values go into the right subtree.
            root.right = insert(root.right, value);
        }

        return root;
    }

    static void collectInOrder(TreeNode root, List<Integer> values) {
        if (root == null) {
            return;
        }

        collectInOrder(root.left, values);
        values.add(root.val);
        collectInOrder(root.right, values);
    }

    static int[] randomTreeExpected(TreeNode root) {
        List<Integer> values = new ArrayList<>();
        collectInOrder(root, values);

        int[] expected = new int[values.size()];
        for (int i = 0; i < values.size(); i++) {
            expected[i] = values.get(i);
        }
        return expected;
    }

    static void runRandomisedTests(List<MethodCase> methods, int iterations) {
        Random random = new Random(20261010L);

        for (int iteration = 1; iteration <= iterations; iteration++) {
            TreeNode root = null;
            int size = random.nextInt(61);

            for (int i = 0; i < size; i++) {
                root = insert(root, random.nextInt(201) - 100);
            }

            int[] expected = randomTreeExpected(root);

            for (MethodCase method : methods) {
                Result result = method.algorithm.solve(root);
                if (!result.valid() || !listMatches(result.head(), expected)) {
                    System.out.printf(
                            "Randomised test FAILED at iteration %d (%s)%n",
                            iteration, method.name);
                    System.out.println("  expected: " + arrayToString(expected));
                    System.out.println("  actual:   " + listToString(result.head()));
                    return;
                }
            }
        }

        System.out.printf(
                "All %d randomised tests passed for all algorithms.%n",
                iterations);
    }

    static void runInputPreservationTest(
            List<MethodCase> methods,
            TreeNode root) {
        List<Integer> before = new ArrayList<>();
        collectInOrder(root, before);

        for (MethodCase method : methods) {
            method.algorithm.solve(root);

            List<Integer> after = new ArrayList<>();
            collectInOrder(root, after);

            if (!before.equals(after)) {
                System.out.println(
                        "FAIL — input tree changed by " + method.name);
                return;
            }
        }

        System.out.println("PASS — all methods preserve the input tree");
    }

    public static void main(String[] args) {
        List<MethodCase> methods = methods();
        List<TestCase> tests = buildTests();

        for (MethodCase method : methods) {
            runTests(method, tests);
        }

        System.out.println();
        System.out.println("== Input preservation ==");
        runInputPreservationTest(methods, tests.get(0).root);

        System.out.println();
        System.out.println("== Randomised cross-checks ==");
        runRandomisedTests(methods, 5_000);
    }
}
