import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Count the number of univalue subtrees in a binary tree.
 *
 * A subtree is a univalue subtree when every node in that subtree
 * has the same value.
 *
 * Example:
 *
 *             5
 *           /   \
 *          1     5
 *         / \     \
 *        5   5     5
 *
 * Univalue subtrees:
 *
 *      5       <- leaf
 *      5       <- leaf
 *      5       <- leaf
 *      5       <- leaf
 *      5       <- right subtree
 *
 * Total = 5
 *
 * Implementations:
 *
 * 1. Top-down
 *      For every node, independently check whether its subtree is
 *      univalue.
 *
 *      Time:  O(n^2) worst case
 *      Space: O(w + h)
 *
 * 2. Bottom-up
 *      Recursively determine whether each subtree is univalue and
 *      count it during the same traversal.
 *
 *      Time:  O(n)
 *      Space: O(h)
 *
 * where:
 *
 *      n = number of nodes
 *      h = height of the tree
 *      w = maximum tree width
 *
 * Note:
 *
 * The algorithm does not depend on the binary-search-tree property.
 * It therefore works for any binary tree.
 */
public class CountOfSingleNodesBinaryTree {

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
     * Result returned by the bottom-up algorithm.
     *
     * univalue:
     *      true when the entire subtree has the same value.
     *
     * count:
     *      number of univalue subtrees contained within the subtree.
     */
    static record Result(
            boolean univalue,
            int count) {
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
     * 1. Top-down
     * **********************************************************************/

    /**
     * Counts univalue subtrees using a top-down approach.
     *
     * Each node is visited using breadth-first traversal.
     * For every node, isUniValue() traverses its entire subtree.
     *
     * This can result in O(n^2) time for a highly skewed tree.
     *
     * Time:  O(n^2) worst case
     * Space: O(w + h)
     */
    static int countOfSingleNodesTopDown(Node root) {

        if (root == null) {
            return 0;
        }

        int count = 0;

        Queue<Node> queue =
                new ArrayDeque<>();

        queue.offer(root);

        while (!queue.isEmpty()) {

            Node current =
                    queue.poll();

            if (isUniValue(
                    current,
                    current.val)) {

                count++;
            }

            if (current.left != null) {
                queue.offer(current.left);
            }

            if (current.right != null) {
                queue.offer(current.right);
            }
        }

        return count;
    }

    /**
     * Determines whether every node in a subtree has the
     * specified value.
     */
    static boolean isUniValue(
            Node root,
            int value) {

        if (root == null) {
            return true;
        }

        if (root.val != value) {
            return false;
        }

        return isUniValue(
                root.left,
                value)
                && isUniValue(
                root.right,
                value);
    }

    /* **********************************************************************
     * 2. Bottom-up
     * **********************************************************************/

    /**
     * Counts univalue subtrees using a bottom-up traversal.
     *
     * Each node is processed exactly once.
     *
     * Time:  O(n)
     * Space: O(h)
     */
    static int countOfSingleNodesBottomUp(Node root) {

        return countSingleRecursion(root).count();
    }

    /**
     * Determines whether the current subtree is univalue while
     * simultaneously counting all univalue subtrees below it.
     *
     * A null subtree is considered univalue for the purpose of
     * combining the results of its children.
     */
    static Result countSingleRecursion(Node root) {

        if (root == null) {
            return new Result(
                    true,
                    0);
        }

        Result left =
                countSingleRecursion(
                        root.left);

        Result right =
                countSingleRecursion(
                        root.right);

        /*
         * If either child subtree is not univalue, the current
         * subtree cannot be univalue.
         */
        if (!left.univalue()
                || !right.univalue()) {

            return new Result(
                    false,
                    left.count()
                            + right.count());
        }

        /*
         * Check the left child.
         *
         * A missing child is automatically valid.
         */
        if (root.left != null
                && root.left.val != root.val) {

            return new Result(
                    false,
                    left.count()
                            + right.count());
        }

        /*
         * Check the right child.
         *
         * A missing child is automatically valid.
         */
        if (root.right != null
                && root.right.val != root.val) {

            return new Result(
                    false,
                    left.count()
                            + right.count());
        }

        /*
         * Both child subtrees are univalue and their values match
         * the current node, so the current subtree is also univalue.
         */
        return new Result(
                true,
                left.count()
                        + right.count()
                        + 1);
    }

    /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    static class TestCase {

        final String id;
        final Node root;
        final int expectedCount;
        final String description;

        TestCase(
                String id,
                Node root,
                int expectedCount,
                String description) {

            this.id = id;
            this.root = root;
            this.expectedCount = expectedCount;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface CountAlgorithm {

        int solve(Node root);
    }

    static class MethodCase {

        final String name;
        final CountAlgorithm algorithm;

        MethodCase(
                String name,
                CountAlgorithm algorithm) {

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
         * Empty tree.
         *
         * Count = 0
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
         *      1
         *
         * Every leaf is a univalue subtree.
         *
         * Count = 1
         */
        tests.add(
                new TestCase(
                        "T2",
                        node(1),
                        1,
                        "single node"));

        /*
         * T3
         *
         *      1
         *     /
         *    2
         *
         * Both nodes are leaves from their own subtree perspective.
         *
         * Count = 2
         */
        tests.add(
                new TestCase(
                        "T3",
                        node(
                                1,
                                node(2),
                                null),
                        2,
                        "root with left child"));

        /*
         * T4
         *
         *      1
         *       \
         *        2
         *
         * Count = 2
         */
        tests.add(
                new TestCase(
                        "T4",
                        node(
                                1,
                                null,
                                node(2)),
                        2,
                        "root with right child"));

        /*
         * T5
         *
         *      1
         *     / \
         *    2   3
         *
         * Every node is a leaf except the root.
         * Root is not univalue.
         *
         * Count = 2
         */
        tests.add(
                new TestCase(
                        "T5",
                        node(
                                1,
                                node(2),
                                node(3)),
                        2,
                        "three nodes with different values"));

        /*
         * T6
         *
         *      1
         *     / \
         *    1   1
         *
         * Every subtree is univalue.
         *
         * Count = 3
         */
        tests.add(
                new TestCase(
                        "T6",
                        node(
                                1,
                                node(1),
                                node(1)),
                        3,
                        "three-node univalue tree"));

        /*
         * T7
         *
         *          1
         *         / \
         *        1   1
         *       / \
         *      1   1
         *
         * Every subtree is univalue.
         *
         * Count = 5
         */
        tests.add(
                new TestCase(
                        "T7",
                        node(
                                1,
                                node(
                                        1,
                                        node(1),
                                        node(1)),
                                node(1)),
                        5,
                        "entire tree is univalue"));

        /*
         * T8
         *
         *             5
         *           /   \
         *          1     5
         *         / \     \
         *        5   5     5
         *
         * Univalue subtrees:
         *
         *      left leaf 5
         *      left leaf 5
         *      left subtree rooted at 1? No
         *      right leaf 5
         *      right subtree rooted at 5
         *      bottom-right leaf 5
         *
         * Total = 5
         */
        tests.add(
                new TestCase(
                        "T8",
                        node(
                                5,
                                node(
                                        1,
                                        node(5),
                                        node(5)),
                                node(
                                        5,
                                        null,
                                        node(5))),
                        5,
                        "mixed tree with several univalue subtrees"));

        /*
         * T9
         *
         *          5
         *         / \
         *        5   5
         *       /     \
         *      4       5
         *
         * Univalue subtrees:
         *
         *      4
         *      5
         *      5
         *      right subtree rooted at 5
         *
         * The root is not univalue.
         *
         * Count = 4
         */
        tests.add(
                new TestCase(
                        "T9",
                        node(
                                5,
                                node(
                                        5,
                                        node(4),
                                        null),
                                node(
                                        5,
                                        null,
                                        node(5))),
                        4,
                        "univalue subtree does not include root"));

        /*
         * T10
         *
         *      1
         *     /
         *    1
         *   /
         *  1
         *
         * Every subtree is univalue.
         *
         * Count = 3
         */
        tests.add(
                new TestCase(
                        "T10",
                        node(
                                1,
                                node(
                                        1,
                                        node(1),
                                        null),
                                null),
                        3,
                        "left-skewed univalue tree"));

        /*
         * T11
         *
         *      1
         *       \
         *        1
         *         \
         *          1
         *
         * Every subtree is univalue.
         *
         * Count = 3
         */
        tests.add(
                new TestCase(
                        "T11",
                        node(
                                1,
                                null,
                                node(
                                        1,
                                        null,
                                        node(1))),
                        3,
                        "right-skewed univalue tree"));

        /*
         * T12
         *
         * Duplicate values are allowed.
         *
         *             1
         *           /   \
         *          2     2
         *         / \   / \
         *        2   2 2   2
         *
         * All leaves = 4.
         *
         * Each subtree rooted at 2 = 3.
         *
         * Root = not univalue.
         *
         * Count = 7
         */
        tests.add(
                new TestCase(
                        "T12",
                        node(
                                1,
                                node(
                                        2,
                                        node(2),
                                        node(2)),
                                node(
                                        2,
                                        node(2),
                                        node(2))),
                        7,
                        "duplicate values in separate subtrees"));

        /*
         * T13
         *
         *             1
         *           /   \
         *          2     3
         *         / \     \
         *        2   2     3
         *
         * Univalue subtrees:
         *
         *      four leaves: 2, 2, 3
         *      subtree rooted at left 2
         *      subtree rooted at right 3
         *
         * Count = 6
         */
        tests.add(
                new TestCase(
                        "T13",
                        node(
                                1,
                                node(
                                        2,
                                        node(2),
                                        node(2)),
                                node(
                                        3,
                                        null,
                                        node(3))),
                        6,
                        "multiple univalue subtrees"));

        /*
         * T14
         *
         * INT_MIN is a legitimate node value.
         *
         * There is no sentinel involved in this problem, so it
         * should work normally.
         *
         * Count = 3
         */
        tests.add(
                new TestCase(
                        "T14",
                        node(
                                Integer.MIN_VALUE,
                                node(Integer.MIN_VALUE),
                                node(Integer.MIN_VALUE)),
                        3,
                        "Integer.MIN_VALUE values"));

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

                if (actual == test.expectedCount) {

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
                            test.expectedCount);

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
     * Verifies that the top-down and bottom-up implementations
     * produce the same result.
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

                int topDown =
                        countOfSingleNodesTopDown(
                                test.root);

                int bottomUp =
                        countOfSingleNodesBottomUp(
                                test.root);

                if (topDown == bottomUp) {

                    passed++;

                    System.out.printf(
                            "PASS %s (%s) -> %d%n",
                            test.id,
                            test.description,
                            topDown);

                } else {

                    failed++;

                    System.out.printf(
                            "FAIL %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.printf(
                            "  top-down  = %d%n",
                            topDown);

                    System.out.printf(
                            "  bottom-up = %d%n",
                            bottomUp);
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
     * A small range of values is deliberately used so that duplicate
     * values occur frequently. This gives the univalue-subtree logic
     * more meaningful coverage.
     */
    static Node randomTree(
            Random random,
            int depth,
            double nodeProbability) {

        if (depth == 0
                || random.nextDouble() > nodeProbability) {

            return null;
        }

        /*
         * Values from 0 to 4 make duplicate values common.
         */
        Node root =
                new Node(
                        random.nextInt(5));

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

    /**
     * Cross-checks the top-down and bottom-up implementations
     * against randomly generated trees.
     *
     * A fixed random seed makes failures reproducible.
     */
    static int runRandomisedTests() {

        System.out.println(
                "======================================================");

        System.out.println(
                "Randomised Cross-check Tests");

        System.out.println(
                "======================================================");

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

            int topDown =
                    countOfSingleNodesTopDown(
                            root);

            int bottomUp =
                    countOfSingleNodesBottomUp(
                            root);

            if (topDown == bottomUp) {

                passed++;

            } else {

                failed++;

                System.out.printf(
                        "FAIL R%d%n",
                        i);

                System.out.printf(
                        "  top-down  = %d%n",
                        topDown);

                System.out.printf(
                        "  bottom-up = %d%n",
                        bottomUp);

                /*
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
                                "1. Top-down",
                                CountOfSingleNodesBinaryTree
                                        ::countOfSingleNodesTopDown),

                        new MethodCase(
                                "2. Bottom-up",
                                CountOfSingleNodesBinaryTree
                                        ::countOfSingleNodesBottomUp));

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
