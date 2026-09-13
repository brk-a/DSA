import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Flip Binary Tree (Upside-Down Binary Tree).
 *
 * Given a binary tree where every node has at most one left child, flip
 * the tree upside down.
 *
 * Example:
 *
 *              1
 *             / \
 *            2   3
 *           / \
 *          4   5
 *
 * becomes:
 *
 *              4
 *             / \
 *            5   2
 *               / \
 *              3   1
 *
 * The flip is performed in place.
 *
 * For each node on the original left spine:
 *
 *     original left child -> new parent
 *     original right child -> new left child
 *     original parent -> new right child
 *
 * Implementations:
 *
 * 1. Recursion
 *      Time:  O(h), where h is the length of the left spine.
 *      Space: O(h) for the recursion stack.
 *
 * 2. Iteration
 *      Time:  O(h).
 *      Space: O(1).
 *
 * Validity:
 *
 * The standard upside-down binary-tree operation requires that a node
 * cannot have a right child without also having a left child.
 *
 * Therefore:
 *
 *              1
 *             / \
 *            2   3
 *
 * is valid, while:
 *
 *              1
 *               \
 *                2
 *
 * is invalid.
 */
public class FlipBinaryTree {

    /* **********************************************************************
     * Node / Result
     * **********************************************************************/

    static class Node {

        int val;
        Node left;
        Node right;

        Node(int val) {
            this.val = val;
            this.left = null;
            this.right = null;
        }

        Node(Node left, Node right, int val) {
            this.left = left;
            this.right = right;
            this.val = val;
        }
    }

    static record Result(Node flippedRoot, boolean valid) {}

    /* **********************************************************************
     * Validation
     * **********************************************************************/

    static boolean validNode(Node node) {
        return node != null;
    }

    /**
     * Checks whether the tree satisfies the structural requirements
     * for an upside-down binary-tree flip.
     *
     * A node with a right child must also have a left child.
     */
    static boolean validTree(Node root) {

        if (!validNode(root)) {
            return false;
        }

        return validTreeHelper(root);
    }

    static boolean validTreeHelper(Node node) {

        if (node == null) {
            return true;
        }

        if (node.left == null && node.right != null) {
            return false;
        }

        return validTreeHelper(node.left)
                && validTreeHelper(node.right);
    }

    /* **********************************************************************
     * Recursive Implementation
     * **********************************************************************/

    /**
     * Flips the tree recursively.
     *
     * Example:
     *
     *          root
     *         /    \
     *       left   right
     *
     * becomes:
     *
     *        left
     *        /  \
     *     right root
     *
     * The left subtree is flipped first because its deepest left node
     * becomes the new root.
     */
    static Result flipBinaryTreeRecursion(Node root) {

        if (!validNode(root)) {
            return new Result(null, false);
        }

        if (!validTree(root)) {
            return new Result(root, false);
        }

        /*
         * A leaf is already the root of its flipped subtree.
         */
        if (root.left == null) {
            return new Result(root, true);
        }

        /*
         * Save these before changing any pointers.
         */
        Node originalLeft = root.left;
        Node originalRight = root.right;

        /*
         * The deepest node on the original left spine becomes the
         * new root.
         */
        Result result =
                flipBinaryTreeRecursion(originalLeft);

        if (!result.valid()) {
            return new Result(root, false);
        }

        /*
         * Rewire:
         *
         *     originalLeft.left  = originalRight
         *     originalLeft.right = originalRoot
         *
         * The old root becomes a leaf.
         */
        originalLeft.left = originalRight;
        originalLeft.right = root;

        root.left = null;
        root.right = null;

        return new Result(
                result.flippedRoot(),
                true);
    }

    /* **********************************************************************
     * Iterative Implementation
     * **********************************************************************/

    /**
     * Flips the tree iteratively using O(1) additional space.
     *
     * The algorithm walks down the original left spine.
     *
     * At every node:
     *
     *     next      = original left child
     *     currRight = original right child
     *
     * Then:
     *
     *     curr.left  = previous right child
     *     curr.right = previous node
     *
     * The previous right child is carried forward because it becomes
     * the new left child of the next node on the original left spine.
     */
    static Result flipBinaryTreeIteration(Node root) {

        if (!validNode(root)) {
            return new Result(null, false);
        }

        if (!validTree(root)) {
            return new Result(root, false);
        }

        Node curr = root;
        Node prev = null;
        Node prevRight = null;

        while (curr != null) {

            /*
             * Save the original pointers before overwriting them.
             */
            Node next = curr.left;
            Node currRight = curr.right;

            /*
             * Rewire the current node.
             *
             * The previous node becomes the new right child.
             * The previous right child becomes the new left child.
             */
            curr.left = prevRight;
            curr.right = prev;

            /*
             * Carry the old right child forward.
             */
            prevRight = currRight;

            /*
             * Move down the original left spine.
             */
            prev = curr;
            curr = next;
        }

        /*
         * curr is null after the loop.
         * prev is the deepest node on the original left spine and
         * therefore the new root.
         */
        return new Result(prev, true);
    }

    /* **********************************************************************
     * Test Harness Types
     * **********************************************************************/

    static class TestCase {

        final String id;
        final Node input;
        final Node expectedRoot;
        final boolean expectedValid;
        final String description;

        TestCase(
                String id,
                Node input,
                Node expectedRoot,
                boolean expectedValid,
                String description) {

            this.id = id;
            this.input = input;
            this.expectedRoot = expectedRoot;
            this.expectedValid = expectedValid;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {

        Result solve(Node root);
    }

    static class MethodCase {

        final String name;
        final Algorithm algorithm;

        MethodCase(
                String name,
                Algorithm algorithm) {

            this.name = name;
            this.algorithm = algorithm;
        }
    }

    /* **********************************************************************
     * Tree-Building Utilities
     * **********************************************************************/

    static Node node(int val, Node left, Node right) {
        return new Node(left, right, val);
    }

    static Node leaf(int val) {
        return new Node(val);
    }

    /**
     * Builds:
     *
     *          1
     *         / \
     *        2   3
     *       / \
     *      4   5
     */
    static Node standardExample() {

        return node(
                1,
                node(
                        2,
                        leaf(4),
                        leaf(5)),
                leaf(3));
    }

    /**
     * Expected result of standardExample():
     *
     *          4
     *         / \
     *        5   2
     *           / \
     *          3   1
     */
    static Node standardExampleExpected() {

        return node(
                4,
                leaf(5),
                node(
                        2,
                        leaf(3),
                        leaf(1)));
    }

    /**
     * Builds a left-only chain:
     *
     *     1
     *    /
     *   2
     *  /
     * 3
     *
     * The resulting flipped tree is:
     *
     *     3
     *      \
     *       2
     *        \
     *         1
     */
    static Node leftChain(int length) {

        Node curr = null;

        for (int value = length; value >= 1; value--) {

            Node next = new Node(value);
            next.left = curr;

            curr = next;
        }

        return curr;
    }

    /**
     * Builds the expected result of flipping a left-only chain.
     */
    static Node expectedLeftChain(int length) {

        Node root = new Node(length);
        Node curr = root;

        for (int value = length - 1; value >= 1; value--) {

            curr.right = new Node(value);
            curr = curr.right;
        }

        return root;
    }

    /* **********************************************************************
     * Tree Comparison Utilities
     * **********************************************************************/

    /**
     * Compares both value and structure.
     */
    static boolean treesEqual(Node a, Node b) {

        if (a == null && b == null) {
            return true;
        }

        if (a == null || b == null) {
            return false;
        }

        return a.val == b.val
                && treesEqual(a.left, b.left)
                && treesEqual(a.right, b.right);
    }

    /**
     * Produces a structural representation useful when a test fails.
     */
    static String treeToString(Node root) {

        if (root == null) {
            return "null";
        }

        return "("
                + root.val
                + ",L="
                + treeToString(root.left)
                + ",R="
                + treeToString(root.right)
                + ")";
    }

    /**
     * Deep-copies a tree.
     *
     * This is required because both flip implementations mutate the
     * tree in place.
     */
    static Node copyTree(Node root) {

        if (root == null) {
            return null;
        }

        return node(
                root.val,
                copyTree(root.left),
                copyTree(root.right));
    }

    /* **********************************************************************
     * Result Comparison
     * **********************************************************************/

    static boolean resultsEqual(
            Result actual,
            Node expectedRoot,
            boolean expectedValid) {

        if (actual == null) {
            return false;
        }

        if (actual.valid() != expectedValid) {
            return false;
        }

        /*
         * For invalid input, the root structure is not part of the
         * successful result contract.
         */
        if (!expectedValid) {
            return true;
        }

        return treesEqual(
                actual.flippedRoot(),
                expectedRoot);
    }

    /* **********************************************************************
     * Test Runner
     * **********************************************************************/

    static void runTests(
            String algorithmName,
            Algorithm method,
            List<TestCase> tests) {

        System.out.println(
                "======================================================");

        System.out.println(algorithmName);

        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            try {

                Result actual =
                        method.solve(test.input);

                if (resultsEqual(
                        actual,
                        test.expectedRoot,
                        test.expectedValid)) {

                    passed++;

                    System.out.printf(
                            "✓ %s (%s)%n",
                            test.id,
                            test.description);

                } else {

                    failed++;

                    System.out.printf(
                            "✗ %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.printf(
                            "  expected valid = %s%n",
                            test.expectedValid);

                    System.out.printf(
                            "  actual          = %s%n",
                            actual);

                    if (test.expectedValid) {

                        System.out.printf(
                                "  expected tree   = %s%n",
                                treeToString(test.expectedRoot));

                        if (actual != null) {

                            System.out.printf(
                                    "  actual tree     = %s%n",
                                    treeToString(
                                            actual.flippedRoot()));
                        }
                    }
                }

            } catch (Exception ex) {

                failed++;

                System.out.printf(
                        "✗ %s (%s)%n",
                        test.id,
                        test.description);

                System.out.printf(
                        "  exception = %s%n",
                        ex);
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
     * Randomised Testing
     * **********************************************************************/

    /**
     * Generates a random tree that satisfies the upside-down-tree
     * structural constraint.
     *
     * A node may have:
     *
     *     - no children
     *     - only a left child
     *     - both a left and right child
     *
     * It can never have a right child without a left child.
     */
    static Node randomValidTree(
            Random rng,
            int maxNodes) {

        int budget =
                rng.nextInt(maxNodes) + 1;

        return randomValidTreeHelper(
                rng,
                new int[]{budget});
    }

    static Node randomValidTreeHelper(
            Random rng,
            int[] budget) {

        if (budget[0] <= 0) {
            return null;
        }

        budget[0]--;

        Node root =
                new Node(budget[0]);

        if (budget[0] <= 0) {
            return root;
        }

        /*
         * Decide whether the current node has a left child.
         */
        if (!rng.nextBoolean()) {
            return root;
        }

        root.left =
                randomValidTreeHelper(
                        rng,
                        budget);

        /*
         * A right child is only possible when the node already has
         * a left child.
         */
        if (budget[0] > 0 && rng.nextBoolean()) {

            root.right =
                    randomValidTreeHelper(
                            rng,
                            budget);
        }

        return root;
    }

    /**
     * Cross-checks the recursive and iterative implementations over
     * randomly generated valid trees.
     *
     * Each implementation receives an independent copy because both
     * methods mutate their input.
     */
    static void runRandomisedTests(int iterations) {

        System.out.println(
                "======================================================");

        System.out.println(
                "Randomised Cross Checks (Recursion vs Iteration)");

        System.out.println(
                "======================================================");

        Random rng =
                new Random(20260910L);

        for (int i = 1; i <= iterations; i++) {

            Node original =
                    randomValidTree(rng, 200);

            Node recursionTree =
                    copyTree(original);

            Node iterationTree =
                    copyTree(original);

            Result recursion =
                    flipBinaryTreeRecursion(
                            recursionTree);

            Result iteration =
                    flipBinaryTreeIteration(
                            iterationTree);

            boolean sameValid =
                    recursion.valid()
                            == iteration.valid();

            boolean sameTree =
                    treesEqual(
                            recursion.flippedRoot(),
                            iteration.flippedRoot());

            if (!sameValid || !sameTree) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "Test number = " + i);

                System.out.println(
                        "Recursion result = "
                                + recursion);

                System.out.println(
                        "Iteration result = "
                                + iteration);

                System.out.println(
                        "Recursion tree = "
                                + treeToString(
                                        recursion.flippedRoot()));

                System.out.println(
                        "Iteration tree = "
                                + treeToString(
                                        iteration.flippedRoot()));

                return;
            }
        }

        System.out.printf(
                "All %d randomised tests passed.%n%n",
                iterations);
    }

    /* **********************************************************************
     * Main / Test Suite
     * **********************************************************************/

    public static void main(String[] args) {

        List<TestCase> tests =
                new ArrayList<>();

        /* ============================================================
         * Basic Cases
         * ============================================================ */

        tests.add(
                new TestCase(
                        "B1",
                        leaf(1),
                        leaf(1),
                        true,
                        "single node: already flipped"));

        tests.add(
                new TestCase(
                        "B2",
                        node(
                                1,
                                leaf(2),
                                null),
                        node(
                                2,
                                null,
                                leaf(1)),
                        true,
                        "root with only a left child"));

        tests.add(
                new TestCase(
                        "B3",
                        node(
                                1,
                                leaf(2),
                                leaf(3)),
                        node(
                                2,
                                leaf(3),
                                leaf(1)),
                        true,
                        "root with left child and right sibling"));

        tests.add(
                new TestCase(
                        "B4",
                        standardExample(),
                        standardExampleExpected(),
                        true,
                        "classic 5-node upside-down binary tree"));

        /* ============================================================
         * Skewed / Unbalanced Trees
         * ============================================================ */

        tests.add(
                new TestCase(
                        "S1",
                        leftChain(2),
                        expectedLeftChain(2),
                        true,
                        "2-node left-only chain"));

        tests.add(
                new TestCase(
                        "S2",
                        leftChain(5),
                        expectedLeftChain(5),
                        true,
                        "5-node left-only chain"));

        tests.add(
                new TestCase(
                        "S3",
                        leftChain(10),
                        expectedLeftChain(10),
                        true,
                        "10-node left-only chain"));

        /*
         * Larger unbalanced tree:
         *
         *              1
         *             / \
         *            2   3
         *           / \
         *          4   5
         *         / \
         *        8   9
         *
         * becomes:
         *
         *              8
         *             / \
         *            9   4
         *               / \
         *              5   2
         *                 / \
         *                3   1
         */
        Node largerTree =
                node(
                        1,
                        node(
                                2,
                                node(
                                        4,
                                        leaf(8),
                                        leaf(9)),
                                leaf(5)),
                        leaf(3));

        Node largerExpected =
                node(
                        8,
                        leaf(9),
                        node(
                                4,
                                leaf(5),
                                node(
                                        2,
                                        leaf(3),
                                        leaf(1))));

        tests.add(
                new TestCase(
                        "S4",
                        largerTree,
                        largerExpected,
                        true,
                        "unbalanced tree with a deeper left spine"));

        /* ============================================================
         * Edge / Invalid Cases
         * ============================================================ */

        tests.add(
                new TestCase(
                        "E1",
                        null,
                        null,
                        false,
                        "null tree"));

        tests.add(
                new TestCase(
                        "E2",
                        node(
                                1,
                                null,
                                leaf(2)),
                        null,
                        false,
                        "right-only child is invalid"));

        tests.add(
                new TestCase(
                        "E3",
                        node(
                                1,
                                node(
                                        2,
                                        null,
                                        leaf(3)),
                                null),
                        null,
                        false,
                        "right-only child deeper in the tree is invalid"));

        /* ============================================================
         * Header
         * ============================================================ */

        System.out.println(
                "############################################################");

        System.out.println(
                "###################  FLIP BINARY TREE  #####################");

        System.out.println(
                "############################################################");

        System.out.println();

        /* ============================================================
         * Algorithms
         * ============================================================ */

        List<MethodCase> methods =
                List.of(

                        new MethodCase(
                                "Recursion",
                                FlipBinaryTree
                                        ::flipBinaryTreeRecursion),

                        new MethodCase(
                                "Iteration",
                                FlipBinaryTree
                                        ::flipBinaryTreeIteration)
                );

        /*
         * Run fixed tests against each algorithm.
         *
         * A fresh copy is supplied because flipping mutates the
         * input tree.
         */
        for (MethodCase method : methods) {

            List<TestCase> methodTests =
                    new ArrayList<>();

            for (TestCase test : tests) {

                methodTests.add(
                        new TestCase(
                                test.id,
                                copyTree(test.input),
                                test.expectedRoot,
                                test.expectedValid,
                                test.description));
            }

            runTests(
                    method.name,
                    method.algorithm,
                    methodTests);
        }

        /* ============================================================
         * Randomised Cross-Check
         * ============================================================ */

        runRandomisedTests(5000);
    }
}
