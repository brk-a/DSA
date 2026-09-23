/**
 * Check whether a binary tree satisfies the Children Sum Property.
 *
 * The Children Sum Property states that for every non-leaf node:
 *
 *      node.value = left.child.value + right.child.value
 *
 * A missing child contributes zero.
 *
 * Example:
 *
 *             10
 *           /    \
 *          8      2
 *         / \      \
 *        3   5      2
 *
 * This tree satisfies the property:
 *
 *      10 = 8 + 2
 *       8 = 3 + 5
 *       2 = 0 + 2
 *
 * Implementations:
 *
 * 1. Recursive Children Sum Property
 *      Time:  O(n)
 *      Space: O(h)
 *
 * where:
 *
 *      n = number of nodes
 *      h = tree height
 *
 * The recursive implementation is appropriate for normal binary-tree
 * usage. For extremely deep or adversarial trees, an iterative approach
 * may be preferable to avoid stack overflow.
 */
public class ChildrenSumPropertyBinaryTree {

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
     * Determine whether a node is a leaf.
     */
    static boolean isLeaf(Node root) {

        return root != null
                && root.left == null
                && root.right == null;
    }

    /**
     * Count the number of nodes in a tree.
     *
     * This is primarily used by the test harness.
     */
    static int treeSize(Node root) {

        if (root == null) {
            return 0;
        }

        return 1
                + treeSize(root.left)
                + treeSize(root.right);
    }

    /**
     * Create a human-readable representation of a tree.
     *
     * This is useful when a test fails.
     */
    static String treeDescription(Node root) {

        if (root == null) {
            return "null";
        }

        if (isLeaf(root)) {
            return String.valueOf(root.val);
        }

        return root.val
                + "("
                + treeDescription(root.left)
                + ", "
                + treeDescription(root.right)
                + ")";
    }

    /* **********************************************************************
     * Children Sum Property
     * **********************************************************************/

    /**
     * Determine whether the binary tree satisfies the Children Sum
     * Property.
     *
     * Empty trees and leaf nodes satisfy the property by definition.
     *
     * A missing child contributes zero to the sum.
     */
    static boolean hasChildrenSumProperty(Node root) {

        /*
         * An empty tree and a leaf node both satisfy the property.
         */
        if (root == null || isLeaf(root)) {
            return true;
        }

        int leftValue =
                root.left != null
                        ? root.left.val
                        : 0;

        int rightValue =
                root.right != null
                        ? root.right.val
                        : 0;

        /*
         * The current node must satisfy the property and both
         * subtrees must also satisfy it.
         *
         * Java's short-circuit evaluation means that child
         * subtrees are not traversed if the current node already
         * violates the property.
         */
        return root.val == leftValue + rightValue
                && hasChildrenSumProperty(root.left)
                && hasChildrenSumProperty(root.right);
    }

    /* **********************************************************************
     * Test Harness
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
     * Deterministic Tests
     * **********************************************************************/

    static void runTests() {

        System.out.println(
                "======================================================");

        System.out.println(
                "Children Sum Property Tests");

        System.out.println(
                "======================================================");

        java.util.List<TestCase> tests =
                java.util.List.of(

                        /*
                         * T1 - Empty tree.
                         */
                        new TestCase(
                                "T1",
                                null,
                                true,
                                "empty tree"),

                        /*
                         * T2 - Single node.
                         */
                        new TestCase(
                                "T2",
                                node(10),
                                true,
                                "single-node tree"),

                        /*
                         * T3 - Two-node tree.
                         *
                         *      10
                         *     /
                         *    10
                         */
                        new TestCase(
                                "T3",
                                node(
                                        10,
                                        node(10),
                                        null),
                                true,
                                "single child with matching value"),

                        /*
                         * T4 - Two-node violation.
                         *
                         *      10
                         *     /
                         *     5
                         */
                        new TestCase(
                                "T4",
                                node(
                                        10,
                                        node(5),
                                        null),
                                false,
                                "single child with incorrect value"),

                        /*
                         * T5 - Basic valid tree.
                         *
                         *          10
                         *         /  \
                         *        8    2
                         */
                        new TestCase(
                                "T5",
                                node(
                                        10,
                                        node(8),
                                        node(2)),
                                true,
                                "basic valid tree"),

                        /*
                         * T6 - Basic violation.
                         *
                         *          10
                         *         /  \
                         *        7    2
                         */
                        new TestCase(
                                "T6",
                                node(
                                        10,
                                        node(7),
                                        node(2)),
                                false,
                                "root violates property"),

                        /*
                         * T7 - Valid multi-level tree.
                         *
                         *             20
                         *           /    \
                         *          10     10
                         *         / \    / \
                         *        4   6  5   5
                         */
                        new TestCase(
                                "T7",
                                node(
                                        20,
                                        node(
                                                10,
                                                node(4),
                                                node(6)),
                                        node(
                                                10,
                                                node(5),
                                                node(5))),
                                true,
                                "valid multi-level tree"),

                        /*
                         * T8 - Deep violation.
                         *
                         *             20
                         *           /    \
                         *          10     10
                         *         / \    / \
                         *        4   7  5   5
                         *
                         * 10 != 4 + 7
                         */
                        new TestCase(
                                "T8",
                                node(
                                        20,
                                        node(
                                                10,
                                                node(4),
                                                node(7)),
                                        node(
                                                10,
                                                node(5),
                                                node(5))),
                                false,
                                "violation in lower level"),

                        /*
                         * T9 - Valid right-only path.
                         *
                         *      10
                         *        \
                         *         5
                         *           \
                         *            5
                         */
                        new TestCase(
                                "T9",
                                node(
                                        10,
                                        null,
                                        node(
                                                5,
                                                null,
                                                node(5))),
                                true,
                                "right-only path"),

                        /*
                         * T10 - Invalid right-only path.
                         *
                         *      10
                         *        \
                         *         5
                         *           \
                         *            4
                         */
                        new TestCase(
                                "T10",
                                node(
                                        10,
                                        null,
                                        node(
                                                5,
                                                null,
                                                node(4))),
                                false,
                                "invalid right-only path"),

                        /*
                         * T11 - Valid negative values.
                         *
                         *       -10
                         *       /  \
                         *      -4   -6
                         */
                        new TestCase(
                                "T11",
                                node(
                                        -10,
                                        node(-4),
                                        node(-6)),
                                true,
                                "valid negative values"),

                        /*
                         * T12 - Invalid negative values.
                         *
                         *       -10
                         *       /  \
                         *      -4   -5
                         */
                        new TestCase(
                                "T12",
                                node(
                                        -10,
                                        node(-4),
                                        node(-5)),
                                false,
                                "invalid negative values"),

                        /*
                         * T13 - Zero values.
                         *
                         *       0
                         *      / \
                         *     0   0
                         */
                        new TestCase(
                                "T13",
                                node(
                                        0,
                                        node(0),
                                        node(0)),
                                true,
                                "zero values"),

                        /*
                         * T14 - Larger valid tree.
                         *
                         *              30
                         *            /    \
                         *          14      16
                         *         /  \    /  \
                         *        6    8  7    9
                         */
                        new TestCase(
                                "T14",
                                node(
                                        30,
                                        node(
                                                14,
                                                node(6),
                                                node(8)),
                                        node(
                                                16,
                                                node(7),
                                                node(9))),
                                true,
                                "larger valid tree"),

                        /*
                         * T15 - Valid tree with mixed one-child nodes.
                         *
                         *          20
                         *         /
                         *        20
                         *          \
                         *           20
                         */
                        new TestCase(
                                "T15",
                                node(
                                        20,
                                        node(
                                                20,
                                                null,
                                                node(20)),
                                        null),
                                true,
                                "mixed single-child nodes")
                );

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            try {

                boolean actual =
                        hasChildrenSumProperty(test.root);

                if (actual == test.expected) {

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
                            "  expected = "
                                    + test.expected);

                    System.out.println(
                            "  actual   = "
                                    + actual);

                    System.out.println(
                            "  tree     = "
                                    + treeDescription(test.root));
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
     * Mutation Tests
     * **********************************************************************/

    /**
     * Verify that changing a valid tree so that one node violates the
     * property causes the algorithm to reject it.
     *
     * This provides an additional check that the algorithm does not
     * simply validate the root node.
     */
    static void runMutationTests() {

        System.out.println(
                "======================================================");

        System.out.println(
                "Mutation Tests");

        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        /*
         * Start with a valid tree:
         *
         *             20
         *           /    \
         *          10     10
         *         / \    / \
         *        4   6  5   5
         */
        Node root =
                node(
                        20,
                        node(
                                10,
                                node(4),
                                node(6)),
                        node(
                                10,
                                node(5),
                                node(5)));

        /*
         * Verify the original tree.
         */
        boolean original =
                hasChildrenSumProperty(root);

        if (original) {

            passed++;

            System.out.println(
                    "PASS M1 - original tree is valid");

        } else {

            failed++;

            System.out.println(
                    "FAIL M1 - original tree should be valid");
        }

        /*
         * Mutate a leaf.
         *
         * 4 -> 5 means:
         *
         * 10 != 5 + 6
         */
        root.left.left.val = 5;

        boolean mutated =
                hasChildrenSumProperty(root);

        if (!mutated) {

            passed++;

            System.out.println(
                    "PASS M2 - lower-level mutation detected");

        } else {

            failed++;

            System.out.println(
                    "FAIL M2 - lower-level mutation not detected");
        }

        /*
         * Restore the tree.
         */
        root.left.left.val = 4;

        boolean restored =
                hasChildrenSumProperty(root);

        if (restored) {

            passed++;

            System.out.println(
                    "PASS M3 - restored tree is valid");

        } else {

            failed++;

            System.out.println(
                    "FAIL M3 - restored tree should be valid");
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
     * Main
     * **********************************************************************/

    public static void main(String[] args) {

        runTests();

        runMutationTests();

        System.out.println(
                "All tests completed.");
    }
}
