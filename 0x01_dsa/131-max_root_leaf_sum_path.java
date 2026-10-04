import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Find the maximum root-to-leaf path sum in a binary tree.
 *
 * A root-to-leaf path starts at the root and ends at a leaf.
 *
 * Two implementations are provided:
 *
 * 1. Recursion
 *
 *      Recursively calculates the maximum sum from each node to a leaf.
 *
 *      Time:  O(n)
 *      Space: O(h)
 *
 * 2. HashMap
 *
 *      Uses a HashMap to store the maximum root-to-current-node sum
 *      encountered for each node.
 *
 *      Time:  O(n)
 *      Space: O(n)
 *
 * Both implementations support negative values.
 */
public class MaxRootLeafSumPath {

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

        Node(
                Node left,
                Node right,
                int val) {

            this.left = left;
            this.right = right;
            this.val = val;
        }
    }

    /* **********************************************************************
     * Helpers
     * **********************************************************************/

    static boolean validNode(
            Node root) {

        return root != null;
    }

    static Node node(
            int value) {

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

    /* **********************************************************************
     * 1. Recursion
     * **********************************************************************/

    /**
     * Find the maximum root-to-leaf path sum using recursion.
     *
     * A leaf is a node with no left or right child.
     *
     * Important:
     *
     * We cannot simply use:
     *
     *      value + max(leftSum, rightSum)
     *
     * when one child is null, because that can produce an incorrect
     * result when the tree contains negative values.
     *
     * Instead, the implementation explicitly handles:
     *
     *      - leaf nodes
     *      - nodes with only a left child
     *      - nodes with only a right child
     *      - nodes with two children
     *
     * Time:  O(n)
     * Space: O(h)
     */
    static int maxRootLeafSumPathRecursion(
            Node root) {

        if (!validNode(root)) {
            return 0;
        }

        /*
         * Leaf node.
         */
        if (root.left == null
                && root.right == null) {

            return root.val;
        }

        /*
         * Only a left child exists.
         */
        if (root.right == null) {

            return root.val
                    + maxRootLeafSumPathRecursion(
                            root.left);
        }

        /*
         * Only a right child exists.
         */
        if (root.left == null) {

            return root.val
                    + maxRootLeafSumPathRecursion(
                            root.right);
        }

        /*
         * Both children exist.
         */
        return root.val
                + Math.max(
                        maxRootLeafSumPathRecursion(
                                root.left),
                        maxRootLeafSumPathRecursion(
                                root.right));
    }

    /* **********************************************************************
     * 2. HashMap
     * **********************************************************************/

    /**
     * Find the maximum root-to-leaf path sum using a HashMap.
     *
     * The HashMap stores:
     *
     *      Node -> maximum sum from the root to that node
     *
     * Once a leaf is reached, its stored sum represents the sum of
     * that particular root-to-leaf path.
     *
     * The maximum leaf sum is the answer.
     *
     * Time:  O(n)
     * Space: O(n)
     */
    static int maxRootLeafSumPathHashMap(
            Node root) {

        if (!validNode(root)) {
            return 0;
        }

        Map<Node, Integer> pathSums =
                new HashMap<>();

        pathSums.put(
                root,
                root.val);

        return maxRootLeafSumPathHashMap(
                root,
                pathSums);
    }

    /**
     * Recursive traversal used by the HashMap implementation.
     */
    static int maxRootLeafSumPathHashMap(
            Node root,
            Map<Node, Integer> pathSums) {

        if (root == null) {
            return Integer.MIN_VALUE;
        }

        /*
         * Retrieve the sum from the root to the current node.
         */
        int currentSum =
                pathSums.get(root);

        /*
         * A leaf represents a complete root-to-leaf path.
         */
        if (root.left == null
                && root.right == null) {

            return currentSum;
        }

        /*
         * Propagate the current path sum to the children.
         */
        if (root.left != null) {

            pathSums.put(
                    root.left,
                    currentSum
                            + root.left.val);
        }

        if (root.right != null) {

            pathSums.put(
                    root.right,
                    currentSum
                            + root.right.val);
        }

        int leftMax =
                maxRootLeafSumPathHashMap(
                        root.left,
                        pathSums);

        int rightMax =
                maxRootLeafSumPathHashMap(
                        root.right,
                        pathSums);

        return Math.max(
                leftMax,
                rightMax);
    }

    /* **********************************************************************
     * Reference Implementation
     * **********************************************************************/

    /**
     * Reference implementation used by the randomised tests.
     *
     * This enumerates every root-to-leaf path and calculates its sum.
     * It is deliberately simple rather than optimised.
     */
    static int referenceMaxRootLeafSum(
            Node root) {

        if (root == null) {
            return 0;
        }

        return referenceMaxRootLeafSum(
                root,
                root.val);
    }

    static int referenceMaxRootLeafSum(
            Node root,
            int currentSum) {

        if (root.left == null
                && root.right == null) {

            return currentSum;
        }

        int leftMax =
                Integer.MIN_VALUE;

        int rightMax =
                Integer.MIN_VALUE;

        if (root.left != null) {

            leftMax =
                    referenceMaxRootLeafSum(
                            root.left,
                            currentSum
                                    + root.left.val);
        }

        if (root.right != null) {

            rightMax =
                    referenceMaxRootLeafSum(
                            root.right,
                            currentSum
                                    + root.right.val);
        }

        return Math.max(
                leftMax,
                rightMax);
    }

    /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    static class TestCase {

        final String id;
        final Node root;
        final int expected;
        final String description;

        TestCase(
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

    @FunctionalInterface
    interface Algorithm {

        int solve(
                Node root);
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
     * Test Runner
     * **********************************************************************/

    static void runTests(
            MethodCase method,
            List<TestCase> tests) {

        printSeparator();

        System.out.println(
                method.name);

        printSeparator();

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            try {

                int actual =
                        method.algorithm.solve(
                                test.root);

                if (actual == test.expected) {

                    passed++;

                    System.out.printf(
                            "PASS %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  maximum sum = "
                                    + actual);

                } else {

                    failed++;

                    System.out.printf(
                            "FAIL %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  expected = "
                                    + test.expected);

                    System.out.println(
                            "  actual   = "
                                    + actual);
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

        printResults(
                passed,
                failed,
                tests.size());
    }

    /* **********************************************************************
     * Cross-Algorithm Tests
     * **********************************************************************/

    static void runCrossCheckTests(
            List<MethodCase> methods,
            List<TestCase> tests) {

        printSeparator();

        System.out.println(
                "Cross-Algorithm Tests");

        printSeparator();

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            try {

                int first =
                        methods.get(0)
                                .algorithm
                                .solve(
                                        test.root);

                boolean success =
                        first == test.expected;

                for (int i = 1;
                     i < methods.size();
                     i++) {

                    int actual =
                            methods.get(i)
                                    .algorithm
                                    .solve(
                                            test.root);

                    if (actual != first) {

                        success = false;

                        System.out.printf(
                                "  %s = %d%n",
                                methods.get(i).name,
                                actual);
                    }
                }

                if (success) {

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

        printResults(
                passed,
                failed,
                tests.size());
    }

    /* **********************************************************************
     * Invalid Input Tests
     * **********************************************************************/

    static void runInvalidInputTests(
            List<MethodCase> methods) {

        printSeparator();

        System.out.println(
                "Invalid Input Tests");

        printSeparator();

        int passed = 0;
        int failed = 0;

        /*
         * Empty tree.
         *
         * The public methods return 0 for an empty tree.
         */
        for (MethodCase method :
                methods) {

            int result =
                    method.algorithm.solve(
                            null);

            if (result == 0) {

                passed++;

                System.out.printf(
                        "PASS I1 - %s handles null root%n",
                        method.name);

            } else {

                failed++;

                System.out.printf(
                        "FAIL I1 - %s returns %d for null root%n",
                        method.name,
                        result);
            }
        }

        printResults(
                passed,
                failed,
                passed + failed);
    }

    /* **********************************************************************
     * Test Data
     * **********************************************************************/

    static List<TestCase> buildTests() {

        List<TestCase> tests =
                new ArrayList<>();

        /*
         * ============================================================
         * Basic Tree
         * ============================================================
         *
         *              10
         *            /    \
         *           5      20
         *          / \    /  \
         *         2   8  15  30
         *
         * Root-to-leaf sums:
         *
         *      10 + 5 + 2  = 17
         *      10 + 5 + 8  = 23
         *      10 + 20 + 15 = 45
         *      10 + 20 + 30 = 60
         *
         * Expected: 60
         */

        Node n2 =
                node(2);

        Node n8 =
                node(8);

        Node n15 =
                node(15);

        Node n30 =
                node(30);

        Node n5 =
                node(
                        5,
                        n2,
                        n8);

        Node n20 =
                node(
                        20,
                        n15,
                        n30);

        Node root =
                node(
                        10,
                        n5,
                        n20);

        tests.add(
                new TestCase(
                        "B1",
                        root,
                        60,
                        "basic balanced tree"));

        /*
         * ============================================================
         * Negative Values
         * ============================================================
         *
         *              -10
         *             /   \
         *           -20   -5
         *           / \     \
         *         -30 -15   -1
         *
         * Sums:
         *
         *      -10 - 20 - 30 = -60
         *      -10 - 20 - 15 = -45
         *      -10 - 5 - 1   = -16
         *
         * Expected: -16
         */

        Node negative30 =
                node(-30);

        Node negative15 =
                node(-15);

        Node negative1 =
                node(-1);

        Node negative20 =
                node(
                        -20,
                        negative30,
                        negative15);

        Node negative5 =
                node(
                        -5,
                        null,
                        negative1);

        Node negative =
                node(
                        -10,
                        negative20,
                        negative5);

        tests.add(
                new TestCase(
                        "N1",
                        negative,
                        -16,
                        "all negative values"));

        /*
         * ============================================================
         * Mixed Positive and Negative Values
         * ============================================================
         *
         *             5
         *           /   \
         *         -10    4
         *         /     / \
         *        20    -5  10
         *
         * Sums:
         *
         *      5 - 10 + 20 = 15
         *      5 + 4 - 5   = 4
         *      5 + 4 + 10  = 19
         *
         * Expected: 19
         */

        Node mixed20 =
                node(20);

        Node mixedMinus5 =
                node(-5);

        Node mixed10 =
                node(10);

        Node mixedMinus10 =
                node(
                        -10,
                        mixed20,
                        null);

        Node mixed4 =
                node(
                        4,
                        mixedMinus5,
                        mixed10);

        Node mixed =
                node(
                        5,
                        mixedMinus10,
                        mixed4);

        tests.add(
                new TestCase(
                        "N2",
                        mixed,
                        19,
                        "mixed positive and negative values"));

        /*
         * ============================================================
         * Single Node
         * ============================================================
         */

        Node single =
                node(100);

        tests.add(
                new TestCase(
                        "S1",
                        single,
                        100,
                        "single-node tree"));

        Node singleNegative =
                node(-100);

        tests.add(
                new TestCase(
                        "S2",
                        singleNegative,
                        -100,
                        "single negative node"));

        /*
         * ============================================================
         * Left-Skewed Tree
         * ============================================================
         *
         *      10
         *     /
         *    20
         *   /
         *  -5
         * /
         * 30
         *
         * Sum = 55
         */

        Node left30 =
                node(30);

        Node leftMinus5 =
                node(
                        -5,
                        left30,
                        null);

        Node left20 =
                node(
                        20,
                        leftMinus5,
                        null);

        Node left10 =
                node(
                        10,
                        left20,
                        null);

        tests.add(
                new TestCase(
                        "A1",
                        left10,
                        55,
                        "left-skewed tree"));

        /*
         * ============================================================
         * Right-Skewed Tree
         * ============================================================
         *
         *      10
         *        \
         *         -5
         *           \
         *            20
         *              \
         *               30
         *
         * Sum = 55
         */

        Node right30 =
                node(30);

        Node right20 =
                node(
                        20,
                        null,
                        right30);

        Node rightMinus5 =
                node(
                        -5,
                        null,
                        right20);

        Node right10 =
                node(
                        10,
                        null,
                        rightMinus5);

        tests.add(
                new TestCase(
                        "A2",
                        right10,
                        55,
                        "right-skewed tree"));

        /*
         * ============================================================
         * One Child Must Be Followed
         * ============================================================
         *
         *             10
         *            /
         *          100
         *            \
         *            -50
         *              \
         *              100
         *
         * The node 100 is not a leaf because it has a child.
         *
         * Sum = 10 + 100 - 50 + 100 = 160
         */

        Node child100 =
                node(100);

        Node childMinus50 =
                node(
                        -50,
                        null,
                        child100);

        Node child100Middle =
                node(
                        100,
                        null,
                        childMinus50);

        Node oneChild =
                node(
                        10,
                        child100Middle,
                        null);

        tests.add(
                new TestCase(
                        "C1",
                        oneChild,
                        160,
                        "nodes with one child are not leaves"));

        return tests;
    }

    /* **********************************************************************
     * Random Binary Tree Generation
     * **********************************************************************/

    static Node randomBinaryTree(
            Random random,
            int size) {

        if (size <= 0) {
            return null;
        }

        ArrayList<Node> nodes =
                new ArrayList<>();

        for (int i = 0; i < size; i++) {

            nodes.add(
                    node(
                            random.nextInt(2001)
                                    - 1000));
        }

        ArrayList<Node> available =
                new ArrayList<>();

        available.add(
                nodes.get(0));

        for (int i = 1;
             i < nodes.size();
             i++) {

            Node parent;

            do {

                parent =
                        available.get(
                                random.nextInt(
                                        available.size()));

            } while (parent.left != null
                    && parent.right != null);

            if (parent.left == null
                    && parent.right == null) {

                if (random.nextBoolean()) {

                    parent.left =
                            nodes.get(i);

                } else {

                    parent.right =
                            nodes.get(i);
                }

            } else if (parent.left == null) {

                parent.left =
                        nodes.get(i);

            } else {

                parent.right =
                        nodes.get(i);
            }

            available.add(
                    nodes.get(i));
        }

        return nodes.get(0);
    }

    /* **********************************************************************
     * Randomised Tests
     * **********************************************************************/

    static void runRandomisedTests(
            List<MethodCase> methods,
            int iterations) {

        printSeparator();

        System.out.println(
                "Randomised Cross Checks");

        printSeparator();

        Random random =
                new Random(20260925L);

        for (int iteration = 1;
             iteration <= iterations;
             iteration++) {

            int size =
                    1 + random.nextInt(50);

            Node root =
                    randomBinaryTree(
                            random,
                            size);

            int expected =
                    referenceMaxRootLeafSum(
                            root);

            for (MethodCase method :
                    methods) {

                int actual =
                        method.algorithm.solve(
                                root);

                if (actual != expected) {

                    System.out.println(
                            "Randomised test FAILED");

                    System.out.println(
                            "iteration = "
                                    + iteration);

                    System.out.println(
                            "algorithm = "
                                    + method.name);

                    System.out.println(
                            "expected = "
                                    + expected);

                    System.out.println(
                            "actual = "
                                    + actual);

                    return;
                }
            }
        }

        System.out.printf(
                "All %d randomised tests passed.%n%n",
                iterations);
    }

    /* **********************************************************************
     * Output Helpers
     * **********************************************************************/

    static void printSeparator() {

        System.out.println(
                "============================================================");
    }

    static void printResults(
            int passed,
            int failed,
            int total) {

        System.out.println();

        System.out.printf(
                "Results: %d passed, %d failed, %d total%n",
                passed,
                failed,
                total);

        System.out.println();
    }

    /* **********************************************************************
     * Main Test Suite
     * **********************************************************************/

    public static void main(
            String[] args) {

        System.out.println(
                "############################################################");

        System.out.println(
                "###############  MAX ROOT-LEAF SUM PATH  ###################");

        System.out.println(
                "############################################################");

        System.out.println();

        List<MethodCase> methods =
                List.of(

                        new MethodCase(
                                "Recursion",
                                MaxRootLeafSumPath
                                        ::maxRootLeafSumPathRecursion),

                        new MethodCase(
                                "HashMap",
                                MaxRootLeafSumPath
                                        ::maxRootLeafSumPathHashMap)
                );

        List<TestCase> tests =
                buildTests();

        /*
         * ============================================================
         * Individual Algorithm Tests
         * ============================================================
         */

        for (MethodCase method :
                methods) {

            runTests(
                    method,
                    tests);
        }

        /*
         * ============================================================
         * Cross-Algorithm Tests
         * ============================================================
         */

        runCrossCheckTests(
                methods,
                tests);

        /*
         * ============================================================
         * Invalid Input Tests
         * ============================================================
         */

        runInvalidInputTests(
                methods);

        /*
         * ============================================================
         * Randomised Tests
         * ============================================================
         */

        runRandomisedTests(
                methods,
                5000);
    }
}
