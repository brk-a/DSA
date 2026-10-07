import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;
import java.util.Random;

/**
 * Zigzag (spiral) level-order traversal of a binary tree.
 *
 * Three implementations are provided:
 *
 * 1. Recursive
 *      Calculates the tree height and traverses one level at a time.
 *
 * 2. Two Stacks
 *      Uses two Deques as stacks to process alternating levels.
 *
 * 3. Deque
 *      Uses a single Deque and processes each level from alternating
 *      ends.
 *
 * Important:
 *
 *      The algorithm does NOT depend on BST ordering.
 *      Zigzag traversal works on any binary tree.
 *
 * Example:
 *
 *              1
 *            /   \
 *           2     3
 *          / \   / \
 *         4   5 6   7
 *
 * Zigzag traversal:
 *
 *      [1, 3, 2, 4, 5, 6, 7]
 *
 * Complexity:
 *
 * Recursive:
 *      Time:  O(n^2) worst case
 *      Space: O(h) call stack + O(n) result
 *
 * Two Stacks:
 *      Time:  O(n)
 *      Space: O(n)
 *
 * Deque:
 *      Time:  O(n)
 *      Space: O(n)
 *
 * where:
 *
 *      n = number of nodes
 *      h = height of the tree
 */
public class ZigZagTraversal {

    /* **********************************************************************
     * Node
     * **********************************************************************/

    static class Node {

        int val;
        Node left;
        Node right;

        Node(int val) {
            this.val = val;
        }

        Node(Node left, Node right, int val) {
            this.left = left;
            this.right = right;
            this.val = val;
        }
    }

    /* **********************************************************************
     * Results
     * **********************************************************************/

    /**
     * Represents the result of a traversal.
     *
     * valid:
     *
     *      true  = traversal completed successfully
     *      false = invalid input
     *
     * values:
     *
     *      The zigzag traversal values.
     */
    static record Result(
            ArrayList<Integer> values,
            boolean valid) {
    }

    /* **********************************************************************
     * Helpers
     * **********************************************************************/

    /**
     * Checks whether a root node is valid.
     *
     * A null root represents an empty tree and is treated as
     * invalid for the Result-based API.
     */
    static boolean isValidInput(Node root) {

        return root != null;
    }

    static Node node(int value) {

        return new Node(value);
    }

    static Node node(
            int value,
            Node left,
            Node right) {

        return new Node(
                left,
                right,
                value);
    }

    /**
     * Calculates the number of nodes in the tree.
     */
    static int size(Node root) {

        if (root == null) {
            return 0;
        }

        return 1
                + size(root.left)
                + size(root.right);
    }

    /**
     * Calculates the height of the tree.
     *
     * An empty tree has height 0.
     */
    static int height(Node root) {

        if (root == null) {
            return 0;
        }

        return 1
                + Math.max(
                height(root.left),
                height(root.right));
    }

    /**
     * Compares two lists of traversal values.
     */
    static boolean valuesEqual(
            List<Integer> values1,
            List<Integer> values2) {

        if (values1 == null || values2 == null) {
            return values1 == values2;
        }

        return values1.equals(values2);
    }

    /**
     * Checks whether the traversal contains the expected values.
     */
    static boolean valuesEqual(
            ArrayList<Integer> actual,
            int... expected) {

        if (actual == null) {
            return false;
        }

        if (actual.size() != expected.length) {
            return false;
        }

        for (int i = 0; i < expected.length; i++) {

            if (actual.get(i) != expected[i]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Compares two trees structurally and by value.
     */
    static boolean treesEqual(
            Node root1,
            Node root2) {

        if (root1 == null && root2 == null) {
            return true;
        }

        if (root1 == null || root2 == null) {
            return false;
        }

        return root1.val == root2.val
                && treesEqual(
                root1.left,
                root2.left)
                && treesEqual(
                root1.right,
                root2.right);
    }

    /**
     * Converts a tree into a level-order list.
     *
     * This is useful for debugging generated test trees.
     */
    static ArrayList<Integer> levelOrderValues(Node root) {

        ArrayList<Integer> values =
                new ArrayList<>();

        if (root == null) {
            return values;
        }

        Deque<Node> queue =
                new ArrayDeque<>();

        queue.addLast(root);

        while (!queue.isEmpty()) {

            Node current =
                    queue.removeFirst();

            values.add(current.val);

            if (current.left != null) {
                queue.addLast(current.left);
            }

            if (current.right != null) {
                queue.addLast(current.right);
            }
        }

        return values;
    }

    /**
     * Converts a tree into a readable string.
     */
    static String treeToString(Node root) {

        return levelOrderValues(root).toString();
    }

    /* **********************************************************************
     * 1. Recursive Implementation
     * **********************************************************************/

    /**
     * Performs zigzag traversal recursively.
     *
     * The algorithm:
     *
     *      1. Calculate the tree height.
     *      2. Visit every level.
     *      3. Alternate traversal direction at each level.
     *
     * The implementation is intentionally kept as a reference
     * implementation for comparison with the iterative solutions.
     */
    static Result zigZagTraversalRecursion(
            Node root) {

        if (!isValidInput(root)) {

            return new Result(
                    null,
                    false);
        }

        ArrayList<Integer> values =
                new ArrayList<>();

        int treeHeight =
                height(root);

        for (int level = 1;
             level <= treeHeight;
             level++) {

            if (level % 2 == 1) {

                traverseLeftToRight(
                        root,
                        level,
                        values);

            } else {

                traverseRightToLeft(
                        root,
                        level,
                        values);
            }
        }

        return new Result(
                values,
                true);
    }

    /**
     * Traverses a level from left to right.
     */
    static void traverseLeftToRight(
            Node root,
            int level,
            ArrayList<Integer> values) {

        if (root == null) {
            return;
        }

        if (level == 1) {

            values.add(root.val);

            return;
        }

        traverseLeftToRight(
                root.left,
                level - 1,
                values);

        traverseLeftToRight(
                root.right,
                level - 1,
                values);
    }

    /**
     * Traverses a level from right to left.
     */
    static void traverseRightToLeft(
            Node root,
            int level,
            ArrayList<Integer> values) {

        if (root == null) {
            return;
        }

        if (level == 1) {

            values.add(root.val);

            return;
        }

        traverseRightToLeft(
                root.right,
                level - 1,
                values);

        traverseRightToLeft(
                root.left,
                level - 1,
                values);
    }

    /* **********************************************************************
     * 2. Two-Stack Implementation
     * **********************************************************************/

    /**
     * Performs zigzag traversal using two stacks.
     *
     * currentLevel:
     *
     *      Nodes currently being processed.
     *
     * nextLevel:
     *
     *      Nodes belonging to the next level.
     *
     * The order in which children are pushed determines the order
     * in which the next level will be processed.
     */
    static Result zigZagTraversalStacks(
            Node root) {

        if (!isValidInput(root)) {

            return new Result(
                    null,
                    false);
        }

        ArrayList<Integer> values =
                new ArrayList<>();

        Deque<Node> currentLevel =
                new ArrayDeque<>();

        Deque<Node> nextLevel =
                new ArrayDeque<>();

        currentLevel.push(root);

        boolean leftToRight = true;

        while (!currentLevel.isEmpty()) {

            Node current =
                    currentLevel.pop();

            values.add(current.val);

            if (leftToRight) {

                /*
                 * Push left first and right second.
                 *
                 * Because this is a stack, right will be processed
                 * first from nextLevel, producing right-to-left order.
                 */
                if (current.left != null) {
                    nextLevel.push(current.left);
                }

                if (current.right != null) {
                    nextLevel.push(current.right);
                }

            } else {

                /*
                 * Reverse the insertion order.
                 *
                 * This causes the next level to be processed
                 * left-to-right.
                 */
                if (current.right != null) {
                    nextLevel.push(current.right);
                }

                if (current.left != null) {
                    nextLevel.push(current.left);
                }
            }

            /*
             * Once the current level is exhausted, swap the stacks.
             */
            if (currentLevel.isEmpty()) {

                Deque<Node> temp =
                        currentLevel;

                currentLevel =
                        nextLevel;

                nextLevel =
                        temp;

                leftToRight =
                        !leftToRight;
            }
        }

        return new Result(
                values,
                true);
    }

    /* **********************************************************************
     * 3. Deque Implementation
     * **********************************************************************/

    /**
     * Performs zigzag traversal using a single Deque.
     *
     * leftToRight:
     *
     *      Remove nodes from the front and append children
     *      to the back.
     *
     * rightToLeft:
     *
     *      Remove nodes from the back and prepend children
     *      to the front.
     *
     * The level size is captured before processing each level so
     * that nodes added for the next level are not processed early.
     */
    static Result zigZagTraversalDeque(
            Node root) {

        if (!isValidInput(root)) {

            return new Result(
                    null,
                    false);
        }

        ArrayList<Integer> values =
                new ArrayList<>();

        Deque<Node> deque =
                new ArrayDeque<>();

        deque.addLast(root);

        boolean leftToRight = true;

        while (!deque.isEmpty()) {

            int levelSize =
                    deque.size();

            for (int i = 0;
                 i < levelSize;
                 i++) {

                if (leftToRight) {

                    Node current =
                            deque.removeFirst();

                    values.add(current.val);

                    /*
                     * Children are added at the back in natural order.
                     */
                    if (current.left != null) {
                        deque.addLast(current.left);
                    }

                    if (current.right != null) {
                        deque.addLast(current.right);
                    }

                } else {

                    Node current =
                            deque.removeLast();

                    values.add(current.val);

                    /*
                     * Children are added at the front in reverse
                     * order so that the next right-to-left level
                     * remains correctly ordered.
                     */
                    if (current.right != null) {
                        deque.addFirst(current.right);
                    }

                    if (current.left != null) {
                        deque.addFirst(current.left);
                    }
                }
            }

            leftToRight =
                    !leftToRight;
        }

        return new Result(
                values,
                true);
    }

    /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    static class TestCase {

        final String id;
        final Node root;
        final int[] expected;
        final String description;

        TestCase(
                String id,
                Node root,
                int[] expected,
                String description) {

            this.id = id;
            this.root = root;
            this.expected = expected;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {

        Result solve(Node root);
    }

    static class AlgorithmCase {

        final String name;
        final Algorithm algorithm;

        AlgorithmCase(
                String name,
                Algorithm algorithm) {

            this.name = name;
            this.algorithm = algorithm;
        }
    }

    /* **********************************************************************
     * Standard Tests
     * **********************************************************************/

    static void runTests(
            String algorithmName,
            Algorithm algorithm,
            List<TestCase> tests) {

        System.out.println(
                "======================================================");

        System.out.println(
                algorithmName);

        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            try {

                Result result =
                        algorithm.solve(test.root);

                boolean valid =
                        result.valid()
                                && result.values() != null;

                boolean correctValues =
                        valid
                                && valuesEqual(
                                result.values(),
                                test.expected);

                boolean passedTest =
                        valid
                                && correctValues;

                if (passedTest) {

                    passed++;

                    System.out.printf(
                            "PASS %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  tree = "
                                    + treeToString(
                                    test.root));

                    System.out.println(
                            "  expected = "
                                    + Arrays.toString(
                                    test.expected));

                    System.out.println(
                            "  actual = "
                                    + result.values());

                } else {

                    failed++;

                    System.out.printf(
                            "FAIL %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  tree = "
                                    + treeToString(
                                    test.root));

                    System.out.println(
                            "  expected = "
                                    + Arrays.toString(
                                    test.expected));

                    System.out.println(
                            "  actual = "
                                    + result.values());

                    System.out.println(
                            "  valid = "
                                    + result.valid());
                }

            } catch (Exception ex) {

                failed++;

                System.out.printf(
                        "FAIL %s (%s)%n",
                        test.id,
                        test.description);

                System.out.println(
                        "  exception = "
                                + ex);
            }
        }

        System.out.println();

        System.out.printf(
                "Results: %d passed, %d failed, %d total%n",
                passed,
                failed,
                tests.size());

        System.out.println();
    }

    /* **********************************************************************
     * Invalid Input Tests
     * **********************************************************************/

    static void runInvalidInputTests(
            List<AlgorithmCase> algorithms) {

        System.out.println(
                "======================================================");

        System.out.println(
                "Invalid Input Tests");

        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        for (AlgorithmCase algorithm :
                algorithms) {

            Result result =
                    algorithm.algorithm.solve(null);

            if (!result.valid()
                    && result.values() == null) {

                passed++;

                System.out.printf(
                        "PASS I1-%s - null root rejected%n",
                        algorithm.name);

            } else {

                failed++;

                System.out.printf(
                        "FAIL I1-%s - null root should be rejected%n",
                        algorithm.name);
            }
        }

        System.out.println();

        System.out.printf(
                "Results: %d passed, %d failed, %d total%n",
                passed,
                failed,
                passed + failed);

        System.out.println();
    }

    /* **********************************************************************
     * Cross-Implementation Tests
     * **********************************************************************/

    static void runCrossImplementationTests(
            List<TestCase> tests) {

        System.out.println(
                "======================================================");

        System.out.println(
                "Cross-Implementation Checks");

        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            Result recursive =
                    zigZagTraversalRecursion(
                            test.root);

            Result stacks =
                    zigZagTraversalStacks(
                            test.root);

            Result deque =
                    zigZagTraversalDeque(
                            test.root);

            boolean equal =
                    recursive.valid()
                            && stacks.valid()
                            && deque.valid()
                            && valuesEqual(
                            recursive.values(),
                            stacks.values())
                            && valuesEqual(
                            recursive.values(),
                            deque.values());

            if (equal) {

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
                        "  recursive = "
                                + recursive.values());

                System.out.println(
                        "  stacks = "
                                + stacks.values());

                System.out.println(
                        "  deque = "
                                + deque.values());
            }
        }

        System.out.println();

        System.out.printf(
                "Results: %d passed, %d failed, %d total%n",
                passed,
                failed,
                tests.size());

        System.out.println();
    }

    /* **********************************************************************
     * Random Tree Generation
     * **********************************************************************/

    /**
     * Generates a random binary tree containing exactly `size` nodes.
     *
     * The generated tree is not necessarily balanced and is not a BST.
     *
     * This is intentional: zigzag traversal should work for any
     * binary tree.
     */
    static Node randomTree(
            Random random,
            int size) {

        if (size <= 0) {
            return null;
        }

        Node root =
                new Node(
                        random.nextInt(201) - 100);

        ArrayList<Node> available =
                new ArrayList<>();

        available.add(root);

        int nodesCreated = 1;

        while (nodesCreated < size) {

            /*
             * Select a node that still has at least one empty child.
             */
            int index =
                    random.nextInt(
                            available.size());

            Node parent =
                    available.get(index);

            boolean attachLeft;

            if (parent.left == null
                    && parent.right == null) {

                attachLeft =
                        random.nextBoolean();

            } else {

                attachLeft =
                        parent.left == null;
            }

            Node child =
                    new Node(
                            random.nextInt(201) - 100);

            if (attachLeft) {
                parent.left = child;
            } else {
                parent.right = child;
            }

            available.add(child);

            nodesCreated++;

            /*
             * Once a node has two children it can no longer be selected.
             */
            if (parent.left != null
                    && parent.right != null) {

                available.remove(index);
            }
        }

        return root;
    }

    /* **********************************************************************
     * Randomised Tests
     * **********************************************************************/

    static void runRandomisedTests(
            int iterations) {

        System.out.println(
                "======================================================");

        System.out.println(
                "Randomised Cross Checks");

        System.out.println(
                "======================================================");

        Random random =
                new Random(20260929L);

        int passed = 0;
        int failed = 0;

        for (int iteration = 1;
             iteration <= iterations;
             iteration++) {

            /*
             * Include empty trees occasionally.
             */
            int size =
                    random.nextInt(100);

            Node root =
                    randomTree(
                            random,
                            size);

            Result recursive =
                    zigZagTraversalRecursion(
                            root);

            Result stacks =
                    zigZagTraversalStacks(
                            root);

            Result deque =
                    zigZagTraversalDeque(
                            root);

            /*
             * Null trees are intentionally considered invalid by
             * the Result-based API.
             */
            if (root == null) {

                boolean invalid =
                        !recursive.valid()
                                && !stacks.valid()
                                && !deque.valid()
                                && recursive.values() == null
                                && stacks.values() == null
                                && deque.values() == null;

                if (invalid) {
                    passed++;
                } else {
                    failed++;
                }

                continue;
            }

            boolean recursiveValid =
                    recursive.valid()
                            && recursive.values() != null
                            && recursive.values().size()
                            == size;

            boolean stacksValid =
                    stacks.valid()
                            && stacks.values() != null
                            && stacks.values().size()
                            == size;

            boolean dequeValid =
                    deque.valid()
                            && deque.values() != null
                            && deque.values().size()
                            == size;

            boolean implementationsEqual =
                    recursiveValid
                            && stacksValid
                            && dequeValid
                            && valuesEqual(
                            recursive.values(),
                            stacks.values())
                            && valuesEqual(
                            recursive.values(),
                            deque.values());

            if (implementationsEqual) {

                passed++;

            } else {

                failed++;

                System.out.printf(
                        "FAIL random iteration %d%n",
                        iteration);

                System.out.println(
                        "  node count = "
                                + size);

                System.out.println(
                        "  tree = "
                                + treeToString(root));

                System.out.println(
                        "  recursive = "
                                + recursive.values());

                System.out.println(
                        "  stacks = "
                                + stacks.values());

                System.out.println(
                        "  deque = "
                                + deque.values());

                /*
                 * Stop at the first random failure so the failing
                 * case can easily be reproduced.
                 */
                break;
            }
        }

        System.out.println();

        if (failed == 0) {

            System.out.printf(
                    "All %d randomised tests passed.%n",
                    passed);

        } else {

            System.out.printf(
                    "Results: %d passed, %d failed.%n",
                    passed,
                    failed);
        }

        System.out.println();
    }

    /* **********************************************************************
     * Main Test Suite
     * **********************************************************************/

    public static void main(String[] args) {

        /*
         * ============================================================
         * Test Trees
         * ============================================================
         */

        List<TestCase> tests =
                new ArrayList<>();

        /*
         * Empty tree.
         *
         * This is tested separately as an invalid input because the
         * Result API deliberately treats null as invalid.
         */

        /*
         * Single node.
         *
         *      1
         */
        tests.add(
                new TestCase(
                        "B1",
                        node(1),
                        new int[]{1},
                        "single node"));

        /*
         * Two nodes.
         *
         *      1
         *       \
         *        2
         */
        tests.add(
                new TestCase(
                        "B2",
                        node(
                                1,
                                null,
                                node(2)),
                        new int[]{1, 2},
                        "two nodes"));

        /*
         * Three nodes.
         *
         *       1
         *      / \
         *     2   3
         */
        tests.add(
                new TestCase(
                        "B3",
                        node(
                                1,
                                node(2),
                                node(3)),
                        new int[]{1, 3, 2},
                        "three nodes"));

        /*
         * Complete binary tree.
         *
         *              1
         *            /   \
         *           2     3
         *          / \   / \
         *         4   5 6   7
         */
        tests.add(
                new TestCase(
                        "B4",
                        node(
                                1,
                                node(
                                        2,
                                        node(4),
                                        node(5)),
                                node(
                                        3,
                                        node(6),
                                        node(7))),
                        new int[]{
                                1,
                                3, 2,
                                4, 5, 6, 7
                        },
                        "complete binary tree"));

        /*
         * Deeper complete tree.
         *
         *              1
         *          /       \
         *         2         3
         *        / \       / \
         *       4   5     6   7
         *      / \ / \   / \ / \
         *     8  9 10 11 12 13 14 15
         */
        tests.add(
                new TestCase(
                        "B5",
                        node(
                                1,
                                node(
                                        2,
                                        node(
                                                4,
                                                node(8),
                                                node(9)),
                                        node(
                                                5,
                                                node(10),
                                                node(11))),
                                node(
                                        3,
                                        node(
                                                6,
                                                node(12),
                                                node(13)),
                                        node(
                                                7,
                                                node(14),
                                                node(15)))),
                        new int[]{
                                1,
                                3, 2,
                                4, 5, 6, 7,
                                15, 14, 13, 12,
                                11, 10, 9, 8
                        },
                        "four-level complete tree"));

        /*
         * Left-skewed tree.
         *
         *      1
         *     /
         *    2
         *   /
         *  3
         * /
         * 4
         */
        tests.add(
                new TestCase(
                        "S1",
                        node(
                                1,
                                node(
                                        2,
                                        node(
                                                3,
                                                node(4),
                                                null),
                                        null),
                                null),
                        new int[]{
                                1, 2, 3, 4
                        },
                        "left-skewed tree"));

        /*
         * Right-skewed tree.
         *
         * 1
         *  \
         *   2
         *    \
         *     3
         *      \
         *       4
         */
        tests.add(
                new TestCase(
                        "S2",
                        node(
                                1,
                                null,
                                node(
                                        2,
                                        null,
                                        node(
                                                3,
                                                null,
                                                node(4))))),
                        new int[]{
                                1, 2, 3, 4
                        },
                        "right-skewed tree"));

        /*
         * Zigzag-shaped sparse tree.
         *
         *      1
         *       \
         *        2
         *       /
         *      3
         *       \
         *        4
         *       /
         *      5
         */
        tests.add(
                new TestCase(
                        "S3",
                        node(
                                1,
                                null,
                                node(
                                        2,
                                        node(
                                                3,
                                                null,
                                                node(
                                                        4,
                                                        node(5),
                                                        null)),
                                        null))),
                        new int[]{
                                1, 2, 3, 4, 5
                        },
                        "sparse zigzag-shaped tree"));

        /*
         * Sparse tree.
         *
         *          1
         *        /   \
         *       2     3
         *        \     \
         *         5     7
         *        /
         *       9
         */
        tests.add(
                new TestCase(
                        "S4",
                        node(
                                1,
                                node(
                                        2,
                                        null,
                                        node(
                                                5,
                                                node(9),
                                                null)),
                                node(
                                        3,
                                        null,
                                        node(7))),
                        new int[]{
                                1,
                                3, 2,
                                5, 7,
                                9
                        },
                        "sparse binary tree"));

        /*
         * Negative and positive values.
         */
        tests.add(
                new TestCase(
                        "V1",
                        node(
                                0,
                                node(
                                        -5,
                                        node(-10),
                                        node(-2)),
                                node(
                                        5,
                                        node(2),
                                        node(10))),
                        new int[]{
                                0,
                                5, -5,
                                -10, -2, 2, 10
                        },
                        "negative and positive values"));

        /*
         * Duplicate values.
         */
        tests.add(
                new TestCase(
                        "V2",
                        node(
                                5,
                                node(
                                        5,
                                        node(5),
                                        node(5)),
                                node(
                                        5,
                                        node(5),
                                        node(5))),
                        new int[]{
                                5,
                                5, 5,
                                5, 5, 5, 5
                        },
                        "duplicate values"));

        /*
         * Integer boundaries.
         */
        tests.add(
                new TestCase(
                        "V3",
                        node(
                                Integer.MIN_VALUE,
                                node(-1),
                                node(
                                        Integer.MAX_VALUE,
                                        node(0),
                                        null)),
                        new int[]{
                                Integer.MIN_VALUE,
                                Integer.MAX_VALUE, -1,
                                0
                        },
                        "integer boundary values"));

        /*
         * ============================================================
         * Algorithms
         * ============================================================
         */

        List<AlgorithmCase> algorithms =
                List.of(

                        new AlgorithmCase(
                                "Recursive",
                                ZigZagTraversal
                                        ::zigZagTraversalRecursion),

                        new AlgorithmCase(
                                "Two Stacks",
                                ZigZagTraversal
                                        ::zigZagTraversalStacks),

                        new AlgorithmCase(
                                "Deque",
                                ZigZagTraversal
                                        ::zigZagTraversalDeque)
                );

        /*
         * ============================================================
         * Header
         * ============================================================
         */

        System.out.println(
                "############################################################");

        System.out.println(
                "#################  ZIGZAG TRAVERSAL  ######################");

        System.out.println(
                "############################################################");

        System.out.println();

        /*
         * ============================================================
         * Standard Tests
         * ============================================================
         */

        for (AlgorithmCase algorithm :
                algorithms) {

            runTests(
                    algorithm.name,
                    algorithm.algorithm,
                    tests);
        }

        /*
         * ============================================================
         * Cross-Implementation Tests
         * ============================================================
         */

        runCrossImplementationTests(
                tests);

        /*
         * ============================================================
         * Invalid Input Tests
         * ============================================================
         */

        runInvalidInputTests(
                algorithms);

        /*
         * ============================================================
         * Randomised Tests
         * ============================================================
         */

        runRandomisedTests(5000);

        /*
         * ============================================================
         * Complete
         * ============================================================
         */

        System.out.println(
                "############################################################");

        System.out.println(
                "####################  TESTS COMPLETE  #####################");

        System.out.println(
                "############################################################");
    }
}
