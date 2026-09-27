import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Determine whether a binary tree is height-balanced.
 *
 * A binary tree is balanced when, for every node, the difference
 * between the height of its left subtree and the height of its
 * right subtree is no greater than 1.
 *
 * Two implementations are provided:
 *
 * 1. DFS + Height Calculation
 *      For every node, calculate the height of its left and right
 *      subtrees independently.
 *
 *      Time:  O(n^2) worst case
 *      Space: O(h)
 *
 * 2. Single Traversal
 *      Calculate subtree heights while simultaneously checking
 *      whether the tree is balanced.
 *
 *      Time:  O(n)
 *      Space: O(h)
 *
 * where:
 *
 *      n = number of nodes
 *      h = height of the tree
 *
 * The single-traversal implementation is the preferred approach.
 */
public class BalancedBinaryTree {

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
     * Result returned by the test runner.
     */
    static record TestResult(
            int passed,
            int failed) {

        int total() {
            return passed + failed;
        }

        boolean allPassed() {
            return failed == 0;
        }
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
     * Calculate the height of a binary tree.
     *
     * Height convention:
     *
     *      null tree = 0
     *      leaf      = 1
     */
    static int height(Node node) {

        if (node == null) {
            return 0;
        }

        return 1 + Math.max(
                height(node.left),
                height(node.right));
    }

    /**
     * Compare two trees structurally and by value.
     */
    static boolean treesEqual(
            Node first,
            Node second) {

        if (first == null && second == null) {
            return true;
        }

        if (first == null || second == null) {
            return false;
        }

        return first.val == second.val
                && treesEqual(
                first.left,
                second.left)
                && treesEqual(
                first.right,
                second.right);
    }

    /**
     * Count the number of nodes in a tree.
     */
    static int nodeCount(Node root) {

        if (root == null) {
            return 0;
        }

        return 1
                + nodeCount(root.left)
                + nodeCount(root.right);
    }

    /* **********************************************************************
     * 1. DFS + Height Calculation
     * **********************************************************************/

    /**
     * Determine whether the tree is balanced using DFS and independent
     * height calculations.
     *
     * For every node:
     *
     *      1. Calculate the left subtree height.
     *      2. Calculate the right subtree height.
     *      3. Check their difference.
     *      4. Recursively check both children.
     *
     * Time:
     *      O(n^2) worst case.
     *
     * Space:
     *      O(h) recursion stack.
     */
    static boolean isBalancedDFS(Node root) {

        if (root == null) {
            return true;
        }

        int leftHeight =
                height(root.left);

        int rightHeight =
                height(root.right);

        if (Math.abs(leftHeight - rightHeight) > 1) {
            return false;
        }

        return isBalancedDFS(root.left)
                && isBalancedDFS(root.right);
    }

    /* **********************************************************************
     * 2. Single Traversal
     * **********************************************************************/

    /**
     * Determine whether the tree is balanced using a single traversal.
     *
     * checkHeight() returns:
     *
     *      >= 0  -> subtree height
     *      -1    -> subtree is unbalanced
     *
     * This allows an unbalanced subtree to propagate the -1 result
     * immediately to its ancestors.
     *
     * Time:
     *      O(n).
     *
     * Space:
     *      O(h) recursion stack.
     */
    static boolean isBalanced(Node root) {
        return checkHeight(root) != -1;
    }

    /**
     * Return the height of a balanced subtree.
     *
     * Return -1 if the subtree is unbalanced.
     */
    private static int checkHeight(Node node) {

        if (node == null) {
            return 0;
        }

        int leftHeight =
                checkHeight(node.left);

        if (leftHeight == -1) {
            return -1;
        }

        int rightHeight =
                checkHeight(node.right);

        if (rightHeight == -1) {
            return -1;
        }

        if (Math.abs(leftHeight - rightHeight) > 1) {
            return -1;
        }

        return 1 + Math.max(
                leftHeight,
                rightHeight);
    }

    /* **********************************************************************
     * Test Case
     * **********************************************************************/

    static class TestCase {

        final String id;
        final Node root;
        final boolean expected;
        final String description;

        TestCase(
                String id,
                Node root,
                boolean expected,
                String description) {

            this.id = id;
            this.root = root;
            this.expected = expected;
            this.description = description;
        }
    }

    /* **********************************************************************
     * Test Interfaces
     * **********************************************************************/

    @FunctionalInterface
    interface BalancedAlgorithm {

        boolean solve(Node root);
    }

    static class AlgorithmTestCase {

        final String name;
        final BalancedAlgorithm algorithm;

        AlgorithmTestCase(
                String name,
                BalancedAlgorithm algorithm) {

            this.name = name;
            this.algorithm = algorithm;
        }
    }

    /* **********************************************************************
     * Test Runner
     * **********************************************************************/

    static TestResult runTests(
            String algorithmName,
            BalancedAlgorithm algorithm,
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

                boolean actual =
                        algorithm.solve(test.root);

                if (actual == test.expected) {

                    passed++;

                    System.out.printf(
                            "PASS %s (%s) -> expected=%s, actual=%s%n",
                            test.id,
                            test.description,
                            test.expected,
                            actual);

                } else {

                    failed++;

                    System.out.printf(
                            "FAIL %s (%s) -> expected=%s, actual=%s%n",
                            test.id,
                            test.description,
                            test.expected,
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
                passed + failed);

        System.out.println();

        return new TestResult(
                passed,
                failed);
    }

    /* **********************************************************************
     * Cross-Check Tests
     * **********************************************************************/

    /**
     * Verify that both implementations produce the same result
     * for every test case.
     */
    static TestResult runCrossCheckTests(
            List<TestCase> tests) {

        System.out.println(
                "======================================================");

        System.out.println(
                "Implementation Cross-Check");

        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            try {

                boolean dfsResult =
                        isBalancedDFS(test.root);

                boolean singleTraversalResult =
                        isBalanced(test.root);

                boolean consistent =
                        dfsResult == singleTraversalResult;

                boolean expected =
                        dfsResult == test.expected;

                if (consistent && expected) {

                    passed++;

                    System.out.printf(
                            "PASS %s (%s) -> DFS=%s, SingleTraversal=%s%n",
                            test.id,
                            test.description,
                            dfsResult,
                            singleTraversalResult);

                } else {

                    failed++;

                    System.out.printf(
                            "FAIL %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.printf(
                            "  expected = %s%n",
                            test.expected);

                    System.out.printf(
                            "  DFS = %s%n",
                            dfsResult);

                    System.out.printf(
                            "  SingleTraversal = %s%n",
                            singleTraversalResult);
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
                passed + failed);

        System.out.println();

        return new TestResult(
                passed,
                failed);
    }

    /* **********************************************************************
     * Height Tests
     * **********************************************************************/

    static TestResult runHeightTests() {

        System.out.println(
                "======================================================");

        System.out.println(
                "Height Tests");

        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        class HeightTest {

            final String id;
            final Node root;
            final int expected;
            final String description;

            HeightTest(
                    String id,
                    Node root,
                    int expected,
                    String description) {

                this.id = id;
                this.root = root;
                this.expected = expected;
                this.description = description;
            }
        }

        List<HeightTest> tests =
                List.of(

                        new HeightTest(
                                "H1",
                                null,
                                0,
                                "empty tree"),

                        new HeightTest(
                                "H2",
                                node(1),
                                1,
                                "single node"),

                        new HeightTest(
                                "H3",
                                node(
                                        1,
                                        node(2),
                                        node(3)),
                                2,
                                "two-level tree"),

                        new HeightTest(
                                "H4",
                                node(
                                        1,
                                        node(
                                                2,
                                                node(4),
                                                node(5)),
                                        node(3)),
                                3,
                                "three-level tree"),

                        new HeightTest(
                                "H5",
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
                                4,
                                "left-skewed tree"),

                        new HeightTest(
                                "H6",
                                node(
                                        1,
                                        null,
                                        node(
                                                2,
                                                null,
                                                node(
                                                        3,
                                                        null,
                                                        node(4)))),
                                4,
                                "right-skewed tree")
                );

        for (HeightTest test : tests) {

            int actual =
                    height(test.root);

            if (actual == test.expected) {

                passed++;

                System.out.printf(
                        "PASS %s (%s) -> expected=%d, actual=%d%n",
                        test.id,
                        test.description,
                        test.expected,
                        actual);

            } else {

                failed++;

                System.out.printf(
                        "FAIL %s (%s) -> expected=%d, actual=%d%n",
                        test.id,
                        test.description,
                        test.expected,
                        actual);
            }
        }

        System.out.println();

        System.out.printf(
                "Results: %d passed, %d failed, %d total%n",
                passed,
                failed,
                passed + failed);

        System.out.println();

        return new TestResult(
                passed,
                failed);
    }

    /* **********************************************************************
     * Random Tree Generation
     * **********************************************************************/

    /**
     * Generate a random binary tree.
     *
     * depth:
     *      Maximum remaining depth.
     *
     * nodeProbability:
     *      Probability that a node exists at a particular position.
     */
    static Node randomTree(
            Random random,
            int depth,
            double nodeProbability) {

        if (depth == 0
                || random.nextDouble() > nodeProbability) {

            return null;
        }

        int value =
                random.nextInt(2001) - 1000;

        Node root =
                new Node(value);

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
     * Randomised Cross-Check Tests
     * **********************************************************************/

    static TestResult runRandomisedTests(
            int iterations) {

        System.out.println(
                "======================================================");

        System.out.println(
                "Randomised Cross-Checks");

        System.out.println(
                "======================================================");

        /*
         * A fixed seed makes failures reproducible.
         */
        Random random =
                new Random(20260921L);

        int passed = 0;
        int failed = 0;

        for (int iteration = 1;
             iteration <= iterations;
             iteration++) {

            Node root =
                    randomTree(
                            random,
                            10,
                            0.75);

            boolean dfsResult =
                    isBalancedDFS(root);

            boolean singleTraversalResult =
                    isBalanced(root);

            if (dfsResult == singleTraversalResult) {

                passed++;

            } else {

                failed++;

                System.out.printf(
                        "FAIL random iteration %d%n",
                        iteration);

                System.out.printf(
                        "  node count = %d%n",
                        nodeCount(root));

                System.out.printf(
                        "  height = %d%n",
                        height(root));

                System.out.printf(
                        "  DFS = %s%n",
                        dfsResult);

                System.out.printf(
                        "  SingleTraversal = %s%n",
                        singleTraversalResult);

                /*
                 * Stop immediately so that the failing case can
                 * be investigated.
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

        return new TestResult(
                passed,
                failed);
    }

    /* **********************************************************************
     * Main Test Suite
     * **********************************************************************/

    public static void main(String[] args) {

        /*
         * ============================================================
         * Test Cases
         * ============================================================
         */

        List<TestCase> tests =
                new ArrayList<>();

        /*
         * ============================================================
         * Empty Tree
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "E1",
                        null,
                        true,
                        "empty tree"));

        /*
         * ============================================================
         * Single Node
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "S1",
                        node(1),
                        true,
                        "single node"));

        /*
         * ============================================================
         * Balanced Trees
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "B1",
                        node(
                                1,
                                node(2),
                                node(3)),
                        true,
                        "root with two children"));

        tests.add(
                new TestCase(
                        "B2",
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
                        true,
                        "complete three-level tree"));

        tests.add(
                new TestCase(
                        "B3",
                        node(
                                1,
                                node(
                                        2,
                                        node(4),
                                        null),
                                node(
                                        3,
                                        null,
                                        node(6))),
                        true,
                        "irregular but balanced tree"));

        tests.add(
                new TestCase(
                        "B4",
                        node(
                                1,
                                node(
                                        2,
                                        node(4),
                                        node(5)),
                                null),
                        true,
                        "left subtree height two"));

        tests.add(
                new TestCase(
                        "B5",
                        node(
                                1,
                                null,
                                node(
                                        3,
                                        node(5),
                                        node(6))),
                        true,
                        "right subtree height two"));

        /*
         * ============================================================
         * Unbalanced Trees
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "U1",
                        node(
                                1,
                                node(
                                        2,
                                        node(4),
                                        null),
                                null),
                        false,
                        "left-skewed tree"));

        tests.add(
                new TestCase(
                        "U2",
                        node(
                                1,
                                null,
                                node(
                                        3,
                                        null,
                                        node(6))),
                        false,
                        "right-skewed tree"));

        tests.add(
                new TestCase(
                        "U3",
                        node(
                                1,
                                node(
                                        2,
                                        node(
                                                4,
                                                node(8),
                                                null),
                                        null),
                                node(3)),
                        false,
                        "left subtree too deep"));

        tests.add(
                new TestCase(
                        "U4",
                        node(
                                1,
                                node(2),
                                node(
                                        3,
                                        null,
                                        node(
                                                6,
                                                null,
                                                node(7)))),
                        false,
                        "right subtree too deep"));

        /*
         * ============================================================
         * Boundary Cases
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "C1",
                        node(
                                1,
                                node(
                                        2,
                                        node(4),
                                        node(5)),
                                node(3)),
                        true,
                        "height difference exactly one"));

        tests.add(
                new TestCase(
                        "C2",
                        node(
                                1,
                                node(2),
                                node(
                                        3,
                                        node(6),
                                        node(7))),
                        true,
                        "height difference exactly one on right"));

        /*
         * ============================================================
         * Duplicate Values
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "D1",
                        node(
                                5,
                                node(
                                        5,
                                        node(5),
                                        node(5)),
                                node(
                                        5,
                                        null,
                                        node(5))),
                        true,
                        "duplicate values"));

        /*
         * ============================================================
         * Negative Values
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "N1",
                        node(
                                -1,
                                node(-2),
                                node(
                                        -3,
                                        node(-4),
                                        null)),
                        true,
                        "negative values"));

        /*
         * ============================================================
         * Larger Balanced Tree
         * ============================================================
         */

        Node largeBalancedTree =
                node(
                        10,
                        node(
                                20,
                                node(
                                        40,
                                        node(80),
                                        node(90)),
                                node(50)),
                        node(
                                30,
                                node(60),
                                node(
                                        70,
                                        null,
                                        node(100))));

        tests.add(
                new TestCase(
                        "L1",
                        largeBalancedTree,
                        true,
                        "larger irregular balanced tree"));

        /*
         * ============================================================
         * Larger Unbalanced Tree
         * ============================================================
         */

        Node largeUnbalancedTree =
                node(
                        10,
                        node(
                                20,
                                node(
                                        40,
                                        node(
                                                80,
                                                node(160),
                                                null),
                                        null),
                                null),
                        node(30));

        tests.add(
                new TestCase(
                        "L2",
                        largeUnbalancedTree,
                        false,
                        "larger unbalanced tree"));

        /*
         * ============================================================
         * Header
         * ============================================================
         */

        System.out.println(
                "############################################################");

        System.out.println(
                "################  BALANCED BINARY TREE  ####################");

        System.out.println(
                "############################################################");

        System.out.println();

        /*
         * ============================================================
         * Algorithm Tests
         * ============================================================
         */

        List<AlgorithmTestCase> algorithms =
                List.of(

                        new AlgorithmTestCase(
                                "DFS + Height Calculation",
                                BalancedBinaryTree
                                        ::isBalancedDFS),

                        new AlgorithmTestCase(
                                "Single Traversal",
                                BalancedBinaryTree
                                        ::isBalanced)
                );

        int totalPassed = 0;
        int totalFailed = 0;

        for (AlgorithmTestCase algorithm :
                algorithms) {

            TestResult result =
                    runTests(
                            algorithm.name,
                            algorithm.algorithm,
                            tests);

            totalPassed += result.passed();
            totalFailed += result.failed();
        }

        /*
         * ============================================================
         * Height Tests
         * ============================================================
         */

        TestResult heightResult =
                runHeightTests();

        totalPassed += heightResult.passed();
        totalFailed += heightResult.failed();

        /*
         * ============================================================
         * Cross-Check
         * ============================================================
         */

        TestResult crossCheckResult =
                runCrossCheckTests(tests);

        totalPassed += crossCheckResult.passed();
        totalFailed += crossCheckResult.failed();

        /*
         * ============================================================
         * Randomised Tests
         * ============================================================
         */

        TestResult randomResult =
                runRandomisedTests(5000);

        totalPassed += randomResult.passed();
        totalFailed += randomResult.failed();

        /*
         * ============================================================
         * Final Summary
         * ============================================================
         */

        System.out.println(
                "############################################################");

        System.out.println(
                "####################  FINAL SUMMARY  #######################");

        System.out.println(
                "############################################################");

        System.out.printf(
                "Passed: %d%n",
                totalPassed);

        System.out.printf(
                "Failed: %d%n",
                totalFailed);

        System.out.printf(
                "Total:  %d%n",
                totalPassed + totalFailed);

        System.out.println();

        if (totalFailed == 0) {

            System.out.println(
                    "ALL TESTS PASSED");

        } else {

            System.out.println(
                    "SOME TESTS FAILED");
        }
    }
}
