import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Find the diameter of a binary tree.
 *
 * The diameter is the length of the longest path between any two nodes.
 *
 * Diameter is measured in number of edges.
 *
 * Example:
 *
 *             1
 *           /   \
 *          2     3
 *         / \
 *        4   5
 *
 * Longest path:
 *
 *      4 -> 2 -> 1 -> 3
 *
 * Diameter = 3 edges
 *
 * Implementations:
 *
 * 1. Brute-force
 *      Time:  O(n^2) worst case
 *      Space: O(h)
 *
 * 2. One-pass
 *      Time:  O(n)
 *      Space: O(h)
 *
 * where:
 *
 *      n = number of nodes
 *      h = height of the tree
 *
 * Note:
 *
 * The diameter algorithm does not depend on the binary-search-tree
 * property. It therefore works for any binary tree.
 */
public class DiameterOfBinaryTree {

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
     * Contains the diameter and height of a subtree.
     *
     * Height is measured in number of nodes.
     * Diameter is measured in number of edges.
     */
    static record Result(
            int diameter,
            int height) {
    }

    /* **********************************************************************
     * Helpers
     * **********************************************************************/

    static Node node(int value) {
        return new Node(value);
    }

    static Node node(
            int value,
            Node left,
            Node right) {

        return new Node(left, right, value);
    }

    /**
     * Returns the height of a tree.
     *
     * Height is measured in number of nodes.
     *
     * Empty tree = 0
     * Leaf       = 1
     */
    static int height(Node root) {

        if (root == null) {
            return 0;
        }

        return 1 + Math.max(
                height(root.left),
                height(root.right));
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

    /* **********************************************************************
     * 1. Brute-force Diameter
     * **********************************************************************/

    /**
     * Finds the diameter using the brute-force approach.
     *
     * For every node:
     *
     *      diameter through node
     *          = left height + right height
     *
     * The height of the left and right subtrees is calculated repeatedly,
     * which results in O(n^2) worst-case time complexity.
     *
     * Diameter is measured in number of edges.
     */
    static int diameterBruteForce(Node root) {

        if (root == null) {
            return 0;
        }

        int leftHeight =
                height(root.left);

        int rightHeight =
                height(root.right);

        int leftDiameter =
                diameterBruteForce(root.left);

        int rightDiameter =
                diameterBruteForce(root.right);

        int throughRoot =
                leftHeight + rightHeight;

        return Math.max(
                throughRoot,
                Math.max(
                        leftDiameter,
                        rightDiameter));
    }

    /* **********************************************************************
     * 2. One-pass Diameter
     * **********************************************************************/

    /**
     * Finds the diameter in a single traversal.
     *
     * Each recursive call returns both:
     *
     *      - the height of the subtree
     *      - the diameter of the subtree
     *
     * This avoids repeatedly calculating subtree heights.
     *
     * Time:  O(n)
     * Space: O(h)
     *
     * Diameter is measured in number of edges.
     */
    static int diameterOnePass(Node root) {

        return findDiameter(root).diameter();
    }

    /**
     * Finds both the diameter and height of a subtree.
     *
     * Height is measured in number of nodes.
     * Diameter is measured in number of edges.
     */
    static Result findDiameter(Node root) {

        if (root == null) {
            return new Result(
                    0,
                    0);
        }

        Result left =
                findDiameter(root.left);

        Result right =
                findDiameter(root.right);

        /*
         * Height of the current subtree.
         */
        int currentHeight =
                1 + Math.max(
                        left.height(),
                        right.height());

        /*
         * Diameter passing through the current node.
         *
         * Since height is measured in nodes, the number of edges
         * between the deepest nodes is:
         *
         *      left height + right height
         */
        int throughRoot =
                left.height()
                        + right.height();

        /*
         * The diameter of the current subtree is either:
         *
         *      1. entirely in the left subtree
         *      2. entirely in the right subtree
         *      3. passing through the current node
         */
        int currentDiameter =
                Math.max(
                        throughRoot,
                        Math.max(
                                left.diameter(),
                                right.diameter()));

        return new Result(
                currentDiameter,
                currentHeight);
    }

    /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    static class TestCase {

        final String id;
        final Node root;
        final int expectedDiameter;
        final String description;

        TestCase(
                String id,
                Node root,
                int expectedDiameter,
                String description) {

            this.id = id;
            this.root = root;
            this.expectedDiameter = expectedDiameter;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface DiameterAlgorithm {

        int solve(Node root);
    }

    static class MethodCase {

        final String name;
        final DiameterAlgorithm algorithm;

        MethodCase(
                String name,
                DiameterAlgorithm algorithm) {

            this.name = name;
            this.algorithm = algorithm;
        }
    }

    /* **********************************************************************
     * Fixed Test Cases
     * **********************************************************************/

    static List<TestCase> createTests() {

        List<TestCase> tests =
                new ArrayList<>();

        /*
         * T1
         *
         * Empty tree
         *
         * diameter = 0
         */
        tests.add(
                new TestCase(
                        "T1",
                        null,
                        0,
                        "empty tree"));

        /*
         * T2
         *
         * Single node
         *
         *      1
         *
         * diameter = 0
         */
        tests.add(
                new TestCase(
                        "T2",
                        node(1),
                        0,
                        "single node"));

        /*
         * T3
         *
         *      1
         *     /
         *    2
         *
         * diameter = 1
         */
        tests.add(
                new TestCase(
                        "T3",
                        node(
                                1,
                                node(2),
                                null),
                        1,
                        "root with left child"));

        /*
         * T4
         *
         *      1
         *       \
         *        2
         *
         * diameter = 1
         */
        tests.add(
                new TestCase(
                        "T4",
                        node(
                                1,
                                null,
                                node(2)),
                        1,
                        "root with right child"));

        /*
         * T5
         *
         *      1
         *     / \
         *    2   3
         *
         * diameter = 2
         */
        tests.add(
                new TestCase(
                        "T5",
                        node(
                                1,
                                node(2),
                                node(3)),
                        2,
                        "balanced tree with three nodes"));

        /*
         * T6
         *
         *             1
         *           /   \
         *          2     3
         *         / \
         *        4   5
         *
         * diameter = 3
         */
        tests.add(
                new TestCase(
                        "T6",
                        node(
                                1,
                                node(
                                        2,
                                        node(4),
                                        node(5)),
                                node(3)),
                        3,
                        "classic binary tree"));

        /*
         * T7
         *
         *             1
         *           /
         *          2
         *         /
         *        3
         *       /
         *      4
         *
         * diameter = 3
         */
        tests.add(
                new TestCase(
                        "T7",
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
                        3,
                        "left-skewed tree"));

        /*
         * T8
         *
         *      1
         *       \
         *        2
         *         \
         *          3
         *           \
         *            4
         *
         * diameter = 3
         */
        tests.add(
                new TestCase(
                        "T8",
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
                        3,
                        "right-skewed tree"));

        /*
         * T9
         *
         *             1
         *           /   \
         *          2     3
         *         /       \
         *        4         5
         *
         * Longest path:
         *
         *      4 -> 2 -> 1 -> 3 -> 5
         *
         * diameter = 4
         */
        tests.add(
                new TestCase(
                        "T9",
                        node(
                                1,
                                node(
                                        2,
                                        node(4),
                                        null),
                                node(
                                        3,
                                        null,
                                        node(5))),
                        4,
                        "diameter passes through root"));

        /*
         * T10
         *
         *             1
         *           /   \
         *          2     3
         *         /
         *        4
         *       /
         *      5
         *
         * diameter = 4
         */
        tests.add(
                new TestCase(
                        "T10",
                        node(
                                1,
                                node(
                                        2,
                                        node(
                                                4,
                                                node(5),
                                                null),
                                        null),
                                node(3)),
                        4,
                        "deep left subtree"));

        /*
         * T11
         *
         * The longest path does not pass through the root.
         *
         *                 1
         *                /
         *               2
         *              / \
         *             3   4
         *            / \
         *           5   6
         *
         * Longest path:
         *
         *      5 -> 3 -> 6
         *
         * diameter = 4? No:
         *
         *      5 -> 3 -> 2 -> 4 = 3
         *      5 -> 3 -> 6     = 2
         *
         * Therefore diameter = 3.
         */
        tests.add(
                new TestCase(
                        "T11",
                        node(
                                1,
                                node(
                                        2,
                                        node(
                                                3,
                                                node(5),
                                                node(6)),
                                        node(4)),
                                null),
                        3,
                        "longest path is inside a subtree"));

        /*
         * T12
         *
         * Duplicate values are valid because this is a binary tree,
         * not a BST validation test.
         */
        tests.add(
                new TestCase(
                        "T12",
                        node(
                                1,
                                node(
                                        1,
                                        node(1),
                                        node(1)),
                                node(
                                        1,
                                        null,
                                        node(1))),
                        4,
                        "duplicate values"));

        return tests;
    }

    /* **********************************************************************
     * Algorithm Tests
     * **********************************************************************/

    static int runTests(
            MethodCase method,
            List<TestCase> tests) {

        System.out.println(
                "======================================================");

        System.out.println(
                method.name);

        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            try {

                int actual =
                        method.algorithm.solve(test.root);

                if (actual == test.expectedDiameter) {

                    passed++;

                    System.out.printf(
                            "PASS %s (%s) -> %d%n",
                            test.id,
                            test.description,
                            actual);

                } else {

                    failed++;

                    System.out.printf(
                            "FAIL %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.printf(
                            "  expected = %d%n",
                            test.expectedDiameter);

                    System.out.printf(
                            "  actual   = %d%n",
                            actual);
                }

            } catch (Exception ex) {

                failed++;

                System.out.printf(
                        "FAIL %s (%s)%n",
                        test.id,
                        test.description);

                System.out.println(
                        "  exception = " + ex);
            }
        }

        System.out.println();

        System.out.printf(
                "Results: %d passed, %d failed, %d total%n",
                passed,
                failed,
                tests.size());

        System.out.println();

        return failed;
    }

    /* **********************************************************************
     * Cross-implementation Tests
     * **********************************************************************/

    /**
     * Verifies that the brute-force and one-pass implementations
     * always produce the same result.
     */
    static int runCrossImplementationTests(
            List<TestCase> tests) {

        System.out.println(
                "======================================================");

        System.out.println(
                "Cross-implementation Tests");

        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            try {

                int bruteForce =
                        diameterBruteForce(test.root);

                int onePass =
                        diameterOnePass(test.root);

                if (bruteForce == onePass) {

                    passed++;

                    System.out.printf(
                            "PASS %s (%s) -> %d%n",
                            test.id,
                            test.description,
                            bruteForce);

                } else {

                    failed++;

                    System.out.printf(
                            "FAIL %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.printf(
                            "  brute force = %d%n",
                            bruteForce);

                    System.out.printf(
                            "  one pass    = %d%n",
                            onePass);
                }

            } catch (Exception ex) {

                failed++;

                System.out.printf(
                        "FAIL %s (%s)%n",
                        test.id,
                        test.description);

                System.out.println(
                        "  exception = " + ex);
            }
        }

        System.out.println();

        System.out.printf(
                "Results: %d passed, %d failed, %d total%n",
                passed,
                failed,
                tests.size());

        System.out.println();

        return failed;
    }

    /* **********************************************************************
     * Random Tree Generation
     * **********************************************************************/

    /**
     * Generates a random binary tree.
     *
     * nodeProbability controls the likelihood that a node is created.
     *
     * A fixed Random seed is used by the test suite so that failures
     * are reproducible.
     */
    static Node randomTree(
            Random random,
            int depth,
            double nodeProbability) {

        if (depth == 0
                || random.nextDouble() > nodeProbability) {

            return null;
        }

        Node root =
                new Node(
                        random.nextInt(1000));

        root.left =
                randomTree(
                        random,
                        depth - 1,
                        nodeProbability);

        root.right =
                randomTree(
                        random,
                        depth - 1,
                        nodeProbability);

        return root;
    }

    /* **********************************************************************
     * Randomised Tests
     * **********************************************************************/

    static int runRandomisedTests() {

        System.out.println(
                "======================================================");

        System.out.println(
                "Randomised Cross-check Tests");

        System.out.println(
                "======================================================");

        /*
         * Fixed seed means a failure can be reproduced exactly.
         */
        Random random =
                new Random(42);

        int numberOfTests = 1000;
        int passed = 0;
        int failed = 0;

        for (int i = 1;
             i <= numberOfTests;
             i++) {

            Node root =
                    randomTree(
                            random,
                            10,
                            0.65);

            int bruteForce =
                    diameterBruteForce(root);

            int onePass =
                    diameterOnePass(root);

            if (bruteForce == onePass) {

                passed++;

            } else {

                failed++;

                System.out.printf(
                        "FAIL R%d%n",
                        i);

                System.out.printf(
                        "  brute force = %d%n",
                        bruteForce);

                System.out.printf(
                        "  one pass    = %d%n",
                        onePass);

                /*
                 * Stop after the first random failure.
                 *
                 * The fixed seed makes this failure reproducible.
                 */
                break;
            }
        }

        System.out.println();

        System.out.printf(
                "Results: %d passed, %d failed, %d total%n",
                passed,
                failed,
                passed + failed);

        System.out.println();

        return failed;
    }

    /* **********************************************************************
     * Main
     * **********************************************************************/

    public static void main(String[] args) {

        List<TestCase> tests =
                createTests();

        List<MethodCase> methods =
                List.of(
                        new MethodCase(
                                "1. Brute-force Diameter",
                                DiameterOfBinaryTree::diameterBruteForce),

                        new MethodCase(
                                "2. One-pass Diameter",
                                DiameterOfBinaryTree::diameterOnePass));

        int totalFailed = 0;

        /*
         * Run the fixed tests against each implementation.
         */
        for (MethodCase method : methods) {

            totalFailed +=
                    runTests(
                            method,
                            tests);
        }

        /*
         * Verify that both implementations agree.
         */
        totalFailed +=
                runCrossImplementationTests(
                        tests);

        /*
         * Run a larger randomised comparison.
         */
        totalFailed +=
                runRandomisedTests();

        System.out.println(
                "======================================================");

        if (totalFailed == 0) {

            System.out.println(
                    "ALL TESTS PASSED");

        } else {

            System.out.printf(
                    "TESTS FAILED: %d%n",
                    totalFailed);
        }

        System.out.println(
                "======================================================");

        /*
         * Make the process fail when used from a build script or CI
         * environment.
         */
        if (totalFailed > 0) {
            System.exit(1);
        }
    }
}
