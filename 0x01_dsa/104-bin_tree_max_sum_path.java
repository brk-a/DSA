import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Maximum Sum Path in a Binary Tree.
 *
 * Problem:
 *
 * Find the maximum possible sum of node values along any path in a
 * binary tree.
 *
 * A path:
 *     - can start and end at any nodes;
 *     - must follow parent-child connections;
 *     - cannot revisit a node;
 *     - may pass through a node using both its left and right subtrees.
 *
 * Example:
 *
 *              -10
 *              /  \
 *             9   20
 *                /  \
 *               15   7
 *
 * Maximum path:
 *
 *             15 -> 20 -> 7
 *
 * Sum:
 *
 *             15 + 20 + 7 = 42
 *
 * Algorithm:
 *
 * For every node, recursively calculate the maximum "downward gain":
 *
 *     gain(node) =
 *         node.val + max(0, gain(left), gain(right))
 *
 * This represents the maximum sum of a path that starts at the current
 * node and continues downward through at most one child.
 *
 * Separately, every node is considered as the highest point of a complete
 * path:
 *
 *     leftGain + node.val + rightGain
 *
 * The global maximum of those values is the answer.
 *
 * Complexity:
 *
 *     Time:  O(n)
 *     Space: O(h)
 *
 * where:
 *     n = number of nodes
 *     h = height of the tree.
 *
 * The implementation below includes:
 *
 *     - recursive solution;
 *     - fixed test cases;
 *     - negative-value cases;
 *     - single-node cases;
 *     - skewed trees;
 *     - balanced trees;
 *     - randomised cross-checks;
 *     - independent brute-force verification for random small trees.
 */
public class MaxSumPathBinaryTree {

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

    static record Result(int sum, boolean valid) {}

    /* **********************************************************************
     * Validation
     * **********************************************************************/

    static boolean validNode(Node node) {
        return node != null;
    }

    /* **********************************************************************
     * Algorithm Implementation
     * **********************************************************************/

    /**
     * Finds the maximum path sum in the binary tree.
     *
     * The empty tree is considered invalid and returns:
     *
     *     Result(-1, false)
     *
     * For a non-empty tree, negative subtrees are ignored when extending
     * a path because including a negative contribution can never improve
     * a maximum path.
     */
    static Result maxSumPathBinaryTree(Node root) {

        if (!validNode(root)) {
            return new Result(-1, false);
        }

        /*
         * target[0] stores the best complete path found anywhere in the
         * tree.
         *
         * It is initialized with root.val so that trees containing only
         * negative values are handled correctly.
         */
        int[] target = {root.val};

        findMaxRecursion(root, target);

        return new Result(target[0], true);
    }

    /**
     * Returns the maximum downward path sum starting at root.
     *
     * The returned path can use:
     *
     *     root
     *     root -> left subtree
     *     root -> right subtree
     *
     * but cannot use both left and right when returning to the parent,
     * because that would create a branching path.
     *
     * The complete path using both sides is evaluated separately through:
     *
     *     left + root.val + right
     */
    static int findMaxRecursion(
            Node root,
            int[] target) {

        if (root == null) {
            return 0;
        }

        /*
         * Negative child contributions are discarded.
         *
         * Example:
         *
         *     root = 10
         *     left = -20
         *
         * The best downward contribution from the left is 0 rather than
         * -20 because the path is better off stopping at root.
         */
        int left =
                Math.max(
                        0,
                        findMaxRecursion(
                                root.left,
                                target));

        int right =
                Math.max(
                        0,
                        findMaxRecursion(
                                root.right,
                                target));

        /*
         * The current node is the highest point of a path that potentially
         * uses both children:
         *
         *       left
         *         \
         *        root
         *         /
         *      right
         *
         * This is a complete candidate path.
         */
        int currentPath =
                left + root.val + right;

        target[0] =
                Math.max(
                        target[0],
                        currentPath);

        /*
         * When returning to the parent, we can only choose ONE side.
         */
        return root.val + Math.max(left, right);
    }

    /* **********************************************************************
     * Test Harness Types
     * **********************************************************************/

    static class TestCase {

        final String id;
        final Node input;
        final Result expected;
        final String description;

        TestCase(
                String id,
                Node input,
                Result expected,
                String description) {

            this.id = id;
            this.input = input;
            this.expected = expected;
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

    static Node node(
            int val,
            Node left,
            Node right) {

        return new Node(
                left,
                right,
                val);
    }

    static Node leaf(int val) {
        return new Node(val);
    }

    /**
     * Creates a left-only chain:
     *
     *     1
     *    /
     *   2
     *  /
     * 3
     */
    static Node leftChain(int... values) {

        if (values.length == 0) {
            return null;
        }

        Node root = new Node(values[0]);
        Node curr = root;

        for (int i = 1; i < values.length; i++) {

            curr.left = new Node(values[i]);
            curr = curr.left;
        }

        return root;
    }

    /**
     * Creates a right-only chain:
     *
     *     1
     *      \
     *       2
     *        \
     *         3
     */
    static Node rightChain(int... values) {

        if (values.length == 0) {
            return null;
        }

        Node root = new Node(values[0]);
        Node curr = root;

        for (int i = 1; i < values.length; i++) {

            curr.right = new Node(values[i]);
            curr = curr.right;
        }

        return root;
    }

    /* **********************************************************************
     * Tree Comparison / Utility Methods
     * **********************************************************************/

    static Node copyTree(Node root) {

        if (root == null) {
            return null;
        }

        return node(
                root.val,
                copyTree(root.left),
                copyTree(root.right));
    }

    static boolean resultsEqual(
            Result actual,
            Result expected) {

        if (actual == null && expected == null) {
            return true;
        }

        if (actual == null || expected == null) {
            return false;
        }

        return actual.sum() == expected.sum()
                && actual.valid() == expected.valid();
    }

    /**
     * Creates a structural representation useful for debugging failed
     * randomized tests.
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
                        method.solve(
                                copyTree(test.input));

                if (resultsEqual(
                        actual,
                        test.expected)) {

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
                            "  expected = %s%n",
                            test.expected);

                    System.out.printf(
                            "  actual   = %s%n",
                            actual);

                    System.out.printf(
                            "  tree     = %s%n",
                            treeToString(test.input));
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
     * Independent Brute-Force Verification
     * **********************************************************************/

    /**
     * Independently calculates the maximum path sum.
     *
     * This is deliberately implemented differently from the main
     * algorithm so randomized tests are not merely comparing the same
     * logic against itself.
     *
     * For every node, we calculate:
     *
     *     maximum downward path starting at that node
     *
     * and use:
     *
     *     left + node + right
     *
     * as the complete-path candidate.
     */
    static Result bruteForceMaxSumPath(Node root) {

        if (root == null) {
            return new Result(-1, false);
        }

        int[] max = {Integer.MIN_VALUE};

        bruteForceDownward(root, max);

        return new Result(max[0], true);
    }

    static int bruteForceDownward(
            Node node,
            int[] max) {

        if (node == null) {
            return 0;
        }

        int leftGain =
                bruteForceDownward(
                        node.left,
                        max);

        int rightGain =
                bruteForceDownward(
                        node.right,
                        max);

        /*
         * A downward path may stop at this node rather than taking a
         * negative child contribution.
         */
        int bestLeft =
                Math.max(0, leftGain);

        int bestRight =
                Math.max(0, rightGain);

        int throughNode =
                bestLeft
                        + node.val
                        + bestRight;

        max[0] =
                Math.max(
                        max[0],
                        throughNode);

        return node.val
                + Math.max(
                        bestLeft,
                        bestRight);
    }

    /* **********************************************************************
     * Randomised Testing
     * **********************************************************************/

    /**
     * Generates a random binary tree.
     *
     * Values are intentionally allowed to be negative because this is
     * where incorrect implementations commonly fail.
     */
    static Node randomTree(
            Random rng,
            int maxNodes) {

        int budget =
                rng.nextInt(maxNodes) + 1;

        return randomTreeHelper(
                rng,
                new int[]{budget});
    }

    static Node randomTreeHelper(
            Random rng,
            int[] budget) {

        if (budget[0] <= 0) {
            return null;
        }

        budget[0]--;

        int value =
                rng.nextInt(41) - 20;

        Node root =
                new Node(value);

        /*
         * Randomly create a left child.
         */
        if (budget[0] > 0
                && rng.nextBoolean()) {

            root.left =
                    randomTreeHelper(
                            rng,
                            budget);
        }

        /*
         * Randomly create a right child.
         */
        if (budget[0] > 0
                && rng.nextBoolean()) {

            root.right =
                    randomTreeHelper(
                            rng,
                            budget);
        }

        return root;
    }

    static void runRandomisedTests(
            int iterations) {

        System.out.println(
                "======================================================");

        System.out.println(
                "Randomised Cross Checks");

        System.out.println(
                "======================================================");

        Random rng =
                new Random(20260910L);

        for (int i = 1; i <= iterations; i++) {

            Node tree =
                    randomTree(
                            rng,
                            50);

            Result actual =
                    maxSumPathBinaryTree(
                            copyTree(tree));

            Result expected =
                    bruteForceMaxSumPath(
                            copyTree(tree));

            if (!resultsEqual(
                    actual,
                    expected)) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "Test number = " + i);

                System.out.println(
                        "Tree = "
                                + treeToString(tree));

                System.out.println(
                        "Expected = "
                                + expected);

                System.out.println(
                        "Actual = "
                                + actual);

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
                        leaf(5),
                        new Result(5, true),
                        "single positive node"));

        tests.add(
                new TestCase(
                        "B2",
                        leaf(-5),
                        new Result(-5, true),
                        "single negative node"));

        tests.add(
                new TestCase(
                        "B3",
                        leaf(0),
                        new Result(0, true),
                        "single zero node"));

        tests.add(
                new TestCase(
                        "B4",
                        node(
                                1,
                                leaf(2),
                                leaf(3)),
                        new Result(6, true),
                        "path passes through root and both children"));

        tests.add(
                new TestCase(
                        "B5",
                        node(
                                -1,
                                leaf(2),
                                leaf(3)),
                        new Result(4, true),
                        "negative root should not prevent positive child path"));

        /* ============================================================
         * Classic Example
         * ============================================================ */

        /*
         *              -10
         *              /  \
         *             9   20
         *                /  \
         *               15   7
         *
         * Maximum path:
         *
         *             15 -> 20 -> 7
         *
         * Sum = 42
         */
        Node classicExample =
                node(
                        -10,
                        leaf(9),
                        node(
                                20,
                                leaf(15),
                                leaf(7)));

        tests.add(
                new TestCase(
                        "C1",
                        classicExample,
                        new Result(42, true),
                        "classic maximum path sum example"));

        /* ============================================================
         * Negative Values
         * ============================================================ */

        tests.add(
                new TestCase(
                        "N1",
                        node(
                                -10,
                                leaf(-20),
                                leaf(-30)),
                        new Result(-10, true),
                        "all negative values: choose least negative node"));

        tests.add(
                new TestCase(
                        "N2",
                        node(
                                -5,
                                leaf(-2),
                                leaf(-8)),
                        new Result(-2, true),
                        "maximum path is the least negative single node"));

        tests.add(
                new TestCase(
                        "N3",
                        node(
                                -10,
                                node(
                                        -20,
                                        leaf(-30),
                                        null),
                                leaf(-5)),
                        new Result(-5, true),
                        "negative subtree should be discarded"));

        tests.add(
                new TestCase(
                        "N4",
                        node(
                                -1,
                                node(
                                        -2,
                                        leaf(10),
                                        null),
                                leaf(3)),
                        new Result(10, true),
                        "positive node deeper in a negative subtree"));

        /* ============================================================
         * Positive Trees
         * ============================================================ */

        tests.add(
                new TestCase(
                        "P1",
                        node(
                                1,
                                node(
                                        2,
                                        leaf(4),
                                        leaf(5)),
                                node(
                                        3,
                                        leaf(6),
                                        leaf(7))),
                        new Result(18, true),
                        "perfect positive tree: 5 + 2 + 1 + 3 + 7"));

        tests.add(
                new TestCase(
                        "P2",
                        node(
                                10,
                                leaf(5),
                                leaf(6)),
                        new Result(21, true),
                        "root plus both positive children"));

        /* ============================================================
         * Skewed Trees
         * ============================================================ */

        tests.add(
                new TestCase(
                        "S1",
                        leftChain(
                                1,
                                2,
                                3,
                                4,
                                5),
                        new Result(15, true),
                        "positive left-only chain"));

        tests.add(
                new TestCase(
                        "S2",
                        leftChain(
                                -10,
                                -20,
                                -5,
                                -30),
                        new Result(-5, true),
                        "negative left-only chain"));

        tests.add(
                new TestCase(
                        "S3",
                        rightChain(
                                1,
                                2,
                                3,
                                4,
                                5),
                        new Result(15, true),
                        "positive right-only chain"));

        tests.add(
                new TestCase(
                        "S4",
                        rightChain(
                                -10,
                                -2,
                                -5,
                                -1),
                        new Result(-1, true),
                        "negative right-only chain"));

        /* ============================================================
         * Mixed Positive / Negative Paths
         * ============================================================ */

        /*
         *              10
         *             /  \
         *           -5    20
         *                /  \
         *               15  -30
         *
         * Best path:
         *
         *          15 -> 20 -> 10
         *
         * Sum = 45
         */
        Node mixedTree =
                node(
                        10,
                        leaf(-5),
                        node(
                                20,
                                leaf(15),
                                leaf(-30)));

        tests.add(
                new TestCase(
                        "M1",
                        mixedTree,
                        new Result(45, true),
                        "positive path should discard negative branches"));

        /*
         *              5
         *             / \
         *           -10   4
         *           / \
         *          20  -2
         *
         * Best path:
         *
         *          20 -> -10 -> 5 -> 4
         *
         * Sum = 19
         */
        Node mixedTree2 =
                node(
                        5,
                        node(
                                -10,
                                leaf(20),
                                leaf(-2)),
                        leaf(4));

        tests.add(
                new TestCase(
                        "M2",
                        mixedTree2,
                        new Result(19, true),
                        "maximum path may include a negative intermediate node"));

        /* ============================================================
         * Edge Case
         * ============================================================ */

        tests.add(
                new TestCase(
                        "E1",
                        null,
                        new Result(-1, false),
                        "null tree is invalid"));

        /* ============================================================
         * Header
         * ============================================================ */

        System.out.println(
                "############################################################");

        System.out.println(
                "################  MAX SUM PATH BINARY TREE  ################");

        System.out.println(
                "############################################################");

        System.out.println();

        /* ============================================================
         * Methods
         * ============================================================ */

        List<MethodCase> methods =
                List.of(

                        new MethodCase(
                                "Recursive DFS",
                                MaxSumPathBinaryTree
                                        ::maxSumPathBinaryTree)
                );

        /* ============================================================
         * Fixed Tests
         * ============================================================ */

        for (MethodCase method : methods) {

            runTests(
                    method.name,
                    method.algorithm,
                    tests);
        }

        /* ============================================================
         * Randomised Verification
         * ============================================================ */

        runRandomisedTests(5000);
    }
}
