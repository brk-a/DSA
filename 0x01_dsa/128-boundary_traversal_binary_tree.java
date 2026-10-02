import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Perform a boundary traversal of a Binary Tree.
 *
 * Boundary traversal visits the tree in the following order:
 *
 *      1. Root
 *      2. Left boundary, top -> bottom
 *      3. All leaves, left -> right
 *      4. Right boundary, bottom -> top
 *
 * A leaf is included only once.
 *
 * For example:
 *
 *                 1
 *               /   \
 *              2     3
 *             / \   / \
 *            4   5 6   7
 *
 * Boundary:
 *
 *      1 -> 2 -> 4 -> 5 -> 6 -> 7 -> 3
 *
 * Two implementations are provided:
 *
 * 1. Recursive
 *
 *      Uses the call stack for the left boundary, leaves and right
 *      boundary.
 *
 *      Time:  O(n)
 *      Space: O(h)
 *
 * 2. Morris / Iterative
 *
 *      Uses Morris traversal for leaf collection so that no explicit
 *      traversal stack is required for that part of the algorithm.
 *
 *      The right boundary is temporarily stored so that it can be
 *      appended in reverse order.
 *
 *      Time:  O(n)
 *      Space: O(h) for the right-boundary buffer.
 *
 * The test suite checks:
 *
 *      - Empty trees
 *      - Single-node trees
 *      - Left-skewed trees
 *      - Right-skewed trees
 *      - Balanced trees
 *      - Unbalanced trees
 *      - Trees containing negative values
 *      - Trees where a child is missing
 *      - Randomly generated trees
 *      - Agreement between both implementations
 *      - Restoration of the tree after Morris traversal
 */
public class BoundaryTraversalBinaryTree {

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
     * Result
     * **********************************************************************/

    static record Result(
            ArrayList<Integer> result,
            boolean valid) {
    }

    /* **********************************************************************
     * Helpers
     * **********************************************************************/

    static boolean isLeaf(
            Node node) {

        return node != null
                && node.left == null
                && node.right == null;
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

    /**
     * Create a copy of a tree.
     *
     * The test suite uses this helper when it needs to compare the tree
     * structure before and after an algorithm.
     */
    static Node copyTree(
            Node root) {

        if (root == null) {
            return null;
        }

        return node(
                root.val,
                copyTree(root.left),
                copyTree(root.right));
    }

    /**
     * Compare two trees structurally and by value.
     */
    static boolean sameTree(
            Node first,
            Node second) {

        if (first == null || second == null) {
            return first == second;
        }

        return first.val == second.val
                && sameTree(
                        first.left,
                        second.left)
                && sameTree(
                        first.right,
                        second.right);
    }

    /* **********************************************************************
     * 1. Recursive Boundary Traversal
     * **********************************************************************/

    /**
     * Perform boundary traversal recursively.
     *
     * The traversal is divided into three independent operations:
     *
     *      left boundary
     *      leaves
     *      right boundary
     *
     * The root is handled separately so that it is not duplicated when
     * the root is also a leaf.
     */
    static Result boundaryTraversalBinaryTreeRecursion(
            Node root) {

        if (root == null) {

            return new Result(
                    new ArrayList<>(),
                    false);
        }

        ArrayList<Integer> result =
                new ArrayList<>();

        /*
         * A leaf root is handled by the leaf traversal below.
         * Therefore, only add the root here when it is not a leaf.
         */
        if (!isLeaf(root)) {

            result.add(
                    root.val);
        }

        collectLeftRecursion(
                root.left,
                result);

        collectLeavesRecursion(
                root,
                result);

        collectRightRecursion(
                root.right,
                result);

        return new Result(
                result,
                true);
    }

    /**
     * Collect the left boundary from top to bottom.
     *
     * Leaves are deliberately excluded because they are collected by the
     * dedicated leaf traversal.
     *
     * If a left child exists, it is always preferred. Otherwise, the
     * right child continues the boundary.
     */
    static void collectLeftRecursion(
            Node node,
            ArrayList<Integer> result) {

        if (node == null
                || isLeaf(node)) {

            return;
        }

        result.add(
                node.val);

        if (node.left != null) {

            collectLeftRecursion(
                    node.left,
                    result);

        } else {

            collectLeftRecursion(
                    node.right,
                    result);
        }
    }

    /**
     * Collect all leaves from left to right.
     */
    static void collectLeavesRecursion(
            Node node,
            ArrayList<Integer> result) {

        if (node == null) {
            return;
        }

        if (isLeaf(node)) {

            result.add(
                    node.val);

            return;
        }

        collectLeavesRecursion(
                node.left,
                result);

        collectLeavesRecursion(
                node.right,
                result);
    }

    /**
     * Collect the right boundary from bottom to top.
     *
     * The recursive call is made before adding the current node.
     * Therefore, nodes are appended while the recursion unwinds:
     *
     *      bottom -> top
     *
     * This avoids explicitly reversing the collected boundary.
     */
    static void collectRightRecursion(
            Node node,
            ArrayList<Integer> result) {

        if (node == null
                || isLeaf(node)) {

            return;
        }

        if (node.right != null) {

            collectRightRecursion(
                    node.right,
                    result);

        } else {

            collectRightRecursion(
                    node.left,
                    result);
        }

        result.add(
                node.val);
    }

    /* **********************************************************************
     * 2. Morris / Iterative Boundary Traversal
     * **********************************************************************/

    /**
     * Perform boundary traversal using an iterative approach.
     *
     * The three boundary components are handled separately:
     *
     *      left boundary
     *      leaves using Morris traversal
     *      right boundary
     *
     * Morris traversal temporarily creates threaded links in the tree.
     * Those links are restored before the method returns.
     */
    static Result boundaryTraversalBinaryTreeIterationMorris(
            Node root) {

        if (root == null) {

            return new Result(
                    new ArrayList<>(),
                    false);
        }

        ArrayList<Integer> result =
                new ArrayList<>();

        /*
         * The root is handled separately to avoid duplication when it is
         * also a leaf.
         */
        if (!isLeaf(root)) {

            result.add(
                    root.val);
        }

        collectLeftIteration(
                root.left,
                result);

        collectLeavesIteration(
                root,
                result);

        collectRightIteration(
                root.right,
                result);

        return new Result(
                result,
                true);
    }

    /**
     * Collect the left boundary iteratively.
     *
     * Leaves are excluded because they are collected separately.
     */
    static void collectLeftIteration(
            Node node,
            ArrayList<Integer> result) {

        Node current = node;

        while (current != null) {

            if (!isLeaf(current)) {

                result.add(
                        current.val);
            }

            if (current.left != null) {

                current = current.left;

            } else {

                current = current.right;
            }
        }
    }

    /**
     * Collect leaves using Morris traversal.
     *
     * Morris traversal temporarily links the right-most node of a left
     * subtree back to the current node. This allows the tree to be
     * traversed without an explicit stack.
     *
     * Every temporary thread is removed before traversal continues.
     */
    static void collectLeavesIteration(
            Node root,
            ArrayList<Integer> result) {

        Node current = root;

        while (current != null) {

            /*
             * If there is no left subtree, the current node can only be
             * a leaf when it also has no right subtree.
             */
            if (current.left == null) {

                if (current.right == null) {

                    result.add(
                            current.val);
                }

                current =
                        current.right;

                continue;
            }

            /*
             * Find the predecessor of current in the left subtree.
             */
            Node predecessor =
                    current.left;

            while (predecessor.right != null
                    && predecessor.right != current) {

                predecessor =
                        predecessor.right;
            }

            if (predecessor.right == null) {

                /*
                 * First visit.
                 *
                 * Create the temporary thread and descend into the
                 * left subtree.
                 */
                predecessor.right =
                        current;

                current =
                        current.left;

            } else {

                /*
                 * Second visit.
                 *
                 * Remove the temporary thread and inspect the
                 * predecessor. If it has no left child, it is a leaf.
                 */
                predecessor.right =
                        null;

                if (predecessor.left == null) {

                    result.add(
                            predecessor.val);
                }

                current =
                        current.right;
            }
        }
    }

    /**
     * Collect the right boundary from bottom to top.
     *
     * The boundary is first collected from top to bottom and then
     * appended in reverse order.
     *
     * Leaves are excluded because they have already been collected.
     */
    static void collectRightIteration(
            Node node,
            ArrayList<Integer> result) {

        ArrayList<Integer> rightBoundary =
                new ArrayList<>();

        Node current = node;

        while (current != null) {

            if (!isLeaf(current)) {

                rightBoundary.add(
                        current.val);
            }

            if (current.right != null) {

                current =
                        current.right;

            } else {

                current =
                        current.left;
            }
        }

        /*
         * Reverse the right boundary so that it is appended
         * bottom -> top.
         */
        for (int i = rightBoundary.size() - 1;
             i >= 0;
             i--) {

            result.add(
                    rightBoundary.get(i));
        }
    }

    /* **********************************************************************
     * Reference Boundary Traversal
     * **********************************************************************/

    /**
     * Simple reference implementation used only by the test suite.
     *
     * This implementation is intentionally straightforward rather than
     * optimised. It provides an independent expected result against which
     * the production implementations can be checked.
     */
    static ArrayList<Integer> boundaryTraversalReference(
            Node root) {

        ArrayList<Integer> result =
                new ArrayList<>();

        if (root == null) {
            return result;
        }

        if (!isLeaf(root)) {

            result.add(
                    root.val);
        }

        addLeftBoundaryReference(
                root.left,
                result);

        addLeavesReference(
                root,
                result);

        ArrayList<Integer> rightBoundary =
                new ArrayList<>();

        addRightBoundaryReference(
                root.right,
                rightBoundary);

        for (int i = rightBoundary.size() - 1;
             i >= 0;
             i--) {

            result.add(
                    rightBoundary.get(i));
        }

        return result;
    }

    static void addLeftBoundaryReference(
            Node node,
            ArrayList<Integer> result) {

        Node current = node;

        while (current != null) {

            if (!isLeaf(current)) {

                result.add(
                        current.val);
            }

            if (current.left != null) {

                current =
                        current.left;

            } else {

                current =
                        current.right;
            }
        }
    }

    static void addLeavesReference(
            Node node,
            ArrayList<Integer> result) {

        if (node == null) {
            return;
        }

        if (isLeaf(node)) {

            result.add(
                    node.val);

            return;
        }

        addLeavesReference(
                node.left,
                result);

        addLeavesReference(
                node.right,
                result);
    }

    static void addRightBoundaryReference(
            Node node,
            ArrayList<Integer> result) {

        Node current = node;

        while (current != null) {

            if (!isLeaf(current)) {

                result.add(
                        current.val);
            }

            if (current.right != null) {

                current =
                        current.right;

            } else {

                current =
                        current.left;
            }
        }
    }

    /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    static class TestCase {

        final String id;
        final Node root;
        final ArrayList<Integer> expected;
        final String description;

        TestCase(
                String id,
                Node root,
                ArrayList<Integer> expected,
                String description) {

            this.id = id;
            this.root = root;
            this.expected = expected;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {

        Result solve(
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

                Result actual =
                        method.algorithm.solve(
                                test.root);

                boolean success =
                        actual.valid()
                                && actual.result()
                                .equals(test.expected);

                if (success) {

                    passed++;

                    System.out.printf(
                            "PASS %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  boundary = "
                                    + actual.result());

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
                                    + actual.result());

                    System.out.println(
                            "  valid    = "
                                    + actual.valid());
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

                Result reference =
                        methods.get(0)
                                .algorithm
                                .solve(test.root);

                boolean success =
                        reference.valid()
                                && reference.result()
                                .equals(test.expected);

                for (int i = 1;
                     i < methods.size();
                     i++) {

                    Result actual =
                            methods.get(i)
                                    .algorithm
                                    .solve(test.root);

                    if (actual.valid()
                            != reference.valid()
                            || !actual.result()
                            .equals(reference.result())) {

                        success = false;

                        System.out.printf(
                                "  %s = %s%n",
                                methods.get(i).name,
                                actual.result());
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

        for (MethodCase method :
                methods) {

            Result result =
                    method.algorithm.solve(
                            null);

            boolean success =
                    !result.valid()
                            && result.result()
                            .isEmpty();

            if (success) {

                passed++;

                System.out.printf(
                        "PASS I1 - %s rejects null root%n",
                        method.name);

            } else {

                failed++;

                System.out.printf(
                        "FAIL I1 - %s accepts null root%n",
                        method.name);
            }
        }

        printResults(
                passed,
                failed,
                passed + failed);
    }

    /* **********************************************************************
     * Tree Integrity Tests
     * **********************************************************************/

    /**
     * Verify that Morris traversal restores all temporary links.
     *
     * This is particularly important because Morris traversal temporarily
     * modifies the tree.
     */
    static void runTreeIntegrityTests() {

        printSeparator();

        System.out.println(
                "Morris Tree Integrity Tests");

        printSeparator();

        Node root =
                node(
                        1,
                        node(
                                2,
                                node(4),
                                node(5)),
                        node(
                                3,
                                node(6),
                                node(7)));

        Node original =
                copyTree(root);

        Result result =
                boundaryTraversalBinaryTreeIterationMorris(
                        root);

        boolean success =
                result.valid()
                        && sameTree(
                                root,
                                original);

        if (success) {

            System.out.println(
                    "PASS T1 - tree restored after Morris traversal");

        } else {

            System.out.println(
                    "FAIL T1 - tree was modified by Morris traversal");
        }

        printResults(
                success ? 1 : 0,
                success ? 0 : 1,
                1);
    }

    /* **********************************************************************
     * Test Data
     * **********************************************************************/

    static ArrayList<Integer> values(
            int... values) {

        ArrayList<Integer> result =
                new ArrayList<>();

        for (int value : values) {

            result.add(
                    value);
        }

        return result;
    }

    static List<TestCase> buildTests() {

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
                        values(),
                        "empty tree"));

        /*
         * ============================================================
         * Single Node
         * ============================================================
         */

        Node single =
                node(1);

        tests.add(
                new TestCase(
                        "S1",
                        single,
                        values(1),
                        "single-node tree"));

        /*
         * ============================================================
         * Balanced Tree
         * ============================================================
         *
         *                 1
         *               /   \
         *              2     3
         *             / \   / \
         *            4   5 6   7
         *
         * Boundary:
         *
         *      1, 2, 4, 5, 6, 7, 3
         */

        Node balanced =
                node(
                        1,
                        node(
                                2,
                                node(4),
                                node(5)),
                        node(
                                3,
                                node(6),
                                node(7)));

        tests.add(
                new TestCase(
                        "B1",
                        balanced,
                        values(
                                1,
                                2,
                                4,
                                5,
                                6,
                                7,
                                3),
                        "balanced tree"));

        /*
         * ============================================================
         * Left-Skewed Tree
         * ============================================================
         *
         *          1
         *         /
         *        2
         *       /
         *      3
         *     /
         *    4
         */

        Node leftSkewed =
                node(
                        1,
                        node(
                                2,
                                node(
                                        3,
                                        node(4),
                                        null),
                                null),
                        null);

        tests.add(
                new TestCase(
                        "L1",
                        leftSkewed,
                        values(
                                1,
                                2,
                                3,
                                4),
                        "left-skewed tree"));

        /*
         * ============================================================
         * Right-Skewed Tree
         * ============================================================
         *
         *      1
         *       \
         *        2
         *         \
         *          3
         *           \
         *            4
         */

        Node rightSkewed =
                node(
                        1,
                        null,
                        node(
                                2,
                                null,
                                node(
                                        3,
                                        null,
                                        node(4))));

        tests.add(
                new TestCase(
                        "R1",
                        rightSkewed,
                        values(
                                1,
                                2,
                                3,
                                4),
                        "right-skewed tree"));

        /*
         * ============================================================
         * Missing Left Children
         * ============================================================
         *
         *          1
         *         / \
         *        2   3
         *         \   \
         *          5   7
         *
         * Boundary:
         *
         *      1, 2, 5, 7, 3
         */

        Node missingLeft =
                node(
                        1,
                        node(
                                2,
                                null,
                                node(5)),
                        node(
                                3,
                                null,
                                node(7)));

        tests.add(
                new TestCase(
                        "M1",
                        missingLeft,
                        values(
                                1,
                                2,
                                5,
                                7,
                                3),
                        "missing left children"));

        /*
         * ============================================================
         * Missing Right Children
         * ============================================================
         *
         *          1
         *         / \
         *        2   3
         *       /     \
         *      4       7
         */

        Node missingRight =
                node(
                        1,
                        node(
                                2,
                                node(4),
                                null),
                        node(
                                3,
                                null,
                                node(7)));

        tests.add(
                new TestCase(
                        "M2",
                        missingRight,
                        values(
                                1,
                                2,
                                4,
                                7,
                                3),
                        "missing right children"));

        /*
         * ============================================================
         * Unbalanced Tree
         * ============================================================
         *
         *             1
         *           /   \
         *          2     3
         *         /       \
         *        4         5
         *         \       /
         *          6     7
         */

        Node unbalanced =
                node(
                        1,
                        node(
                                2,
                                node(
                                        4,
                                        null,
                                        node(6)),
                                null),
                        node(
                                3,
                                null,
                                node(
                                        5,
                                        node(7),
                                        null)));

        tests.add(
                new TestCase(
                        "U1",
                        unbalanced,
                        values(
                                1,
                                2,
                                4,
                                6,
                                7,
                                5,
                                3),
                        "unbalanced tree"));

        /*
         * ============================================================
         * Negative Values
         * ============================================================
         *
         *             -10
         *             /  \
         *          -20   -5
         *          /       \
         *        -30       -1
         */

        Node negative =
                node(
                        -10,
                        node(
                                -20,
                                node(-30),
                                null),
                        node(
                                -5,
                                null,
                                node(-1)));

        tests.add(
                new TestCase(
                        "N1",
                        negative,
                        values(
                                -10,
                                -20,
                                -30,
                                -1,
                                -5),
                        "negative values"));

        /*
         * ============================================================
         * Root With Only Left Subtree
         * ============================================================
         */

        Node onlyLeft =
                node(
                        10,
                        node(
                                5,
                                node(2),
                                node(7)),
                        null);

        tests.add(
                new TestCase(
                        "C1",
                        onlyLeft,
                        values(
                                10,
                                5,
                                2,
                                7),
                        "root with only left subtree"));

        /*
         * ============================================================
         * Root With Only Right Subtree
         * ============================================================
         */

        Node onlyRight =
                node(
                        10,
                        null,
                        node(
                                15,
                                node(12),
                                node(20)));

        tests.add(
                new TestCase(
                        "C2",
                        onlyRight,
                        values(
                                10,
                                12,
                                20,
                                15),
                        "root with only right subtree"));

        return tests;
    }

    /* **********************************************************************
     * Random Tree Generation
     * **********************************************************************/

    /**
     * Insert a node at a randomly selected position.
     *
     * This deliberately creates ordinary binary trees rather than BSTs.
     */
    static Node insertRandom(
            Node root,
            Node newNode,
            Random random) {

        if (root == null) {
            return newNode;
        }

        if (random.nextBoolean()) {

            root.left =
                    insertRandom(
                            root.left,
                            newNode,
                            random);

        } else {

            root.right =
                    insertRandom(
                            root.right,
                            newNode,
                            random);
        }

        return root;
    }

    static Node randomTree(
            Random random,
            int size) {

        if (size == 0) {
            return null;
        }

        Node root =
                node(
                        random.nextInt(2001)
                                - 1000);

        for (int i = 1;
             i < size;
             i++) {

            root =
                    insertRandom(
                            root,
                            node(
                                    random.nextInt(2001)
                                            - 1000),
                            random);
        }

        return root;
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
                new Random(20261001L);

        for (int iteration = 1;
             iteration <= iterations;
             iteration++) {

            int size =
                    random.nextInt(50);

            Node root =
                    randomTree(
                            random,
                            size);

            ArrayList<Integer> expected =
                    boundaryTraversalReference(
                            root);

            /*
             * Keep a copy because the Morris implementation temporarily
             * modifies the tree.
             */
            Node original =
                    copyTree(root);

            for (MethodCase method :
                    methods) {

                Result actual =
                        method.algorithm.solve(
                                root);

                if (!actual.valid()
                        || !actual.result()
                        .equals(expected)) {

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
                                    + actual.result());

                    System.out.println(
                            "valid = "
                                    + actual.valid());

                    return;
                }

                /*
                 * Morris traversal must restore the tree.
                 */
                if (method.name.contains("Morris")
                        && !sameTree(
                                root,
                                original)) {

                    System.out.println(
                            "Randomised tree-integrity test FAILED");

                    System.out.println(
                            "iteration = "
                                    + iteration);

                    System.out.println(
                            "algorithm = "
                                    + method.name);

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
                "############  BOUNDARY TRAVERSAL BINARY TREE  #############");

        System.out.println(
                "############################################################");

        System.out.println();

        List<MethodCase> methods =
                List.of(

                        new MethodCase(
                                "Recursive",
                                BoundaryTraversalBinaryTree
                                        ::boundaryTraversalBinaryTreeRecursion),

                        new MethodCase(
                                "Morris / Iterative",
                                BoundaryTraversalBinaryTree
                                        ::boundaryTraversalBinaryTreeIterationMorris)
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
         * Morris Tree Integrity
         * ============================================================
         */

        runTreeIntegrityTests();

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
