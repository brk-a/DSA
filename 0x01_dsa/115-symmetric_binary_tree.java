import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Determine whether a binary tree is symmetric around its center.
 *
 * A tree is symmetric when its left and right subtrees are
 * mirror images of one another.
 *
 * Three implementations are provided:
 *
 * 1. Recursive
 *      Compare left subtree with right subtree recursively.
 *
 * 2. Stack
 *      Depth-first comparison using an explicit stack.
 *
 * 3. Queue
 *      Breadth-first comparison using a queue.
 *
 * Complexity:
 *
 * 1. Recursive
 *      Time:  O(n)
 *      Space: O(h)
 *
 * 2. Stack
 *      Time:  O(n)
 *      Space: O(h)
 *
 * 3. Queue
 *      Time:  O(n)
 *      Space: O(w)
 *
 * where:
 *
 *      n = number of nodes
 *      h = tree height
 *      w = maximum tree width
 */
public class SymmetricTree {

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
     * Node Pair
     * **********************************************************************/

    /**
     * Represents two nodes which should be mirror images.
     *
     * A Pair is used instead of putting null directly into an
     * ArrayDeque, because ArrayDeque does not permit null elements.
     */
    static record NodePair(
            Node left,
            Node right) {
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
     * Deep structural comparison of two trees.
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
     * Creates a deep copy of a tree.
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

    /**
     * Counts the nodes in a tree.
     */
    static int countNodes(Node root) {

        if (root == null) {
            return 0;
        }

        return 1
                + countNodes(root.left)
                + countNodes(root.right);
    }

    /* **********************************************************************
     * 1. Recursive Symmetry Check
     * **********************************************************************/

    /**
     * Determines whether the tree is symmetric recursively.
     *
     * Time:  O(n)
     * Space: O(h)
     */
    static boolean symmetricTreeRecursive(Node root) {

        if (root == null) {
            return true;
        }

        return symmetricTreeRecursive(
                root.left,
                root.right);
    }

    /**
     * Compares two trees as mirror images.
     */
    static boolean symmetricTreeRecursive(
            Node left,
            Node right) {

        /*
         * Both sides are empty.
         */
        if (left == null && right == null) {
            return true;
        }

        /*
         * Exactly one side is empty.
         */
        if (left == null || right == null) {
            return false;
        }

        /*
         * Values must match.
         */
        if (left.val != right.val) {
            return false;
        }

        /*
         * Compare:
         *
         * left.left  <-> right.right
         * left.right <-> right.left
         */
        return symmetricTreeRecursive(
                left.left,
                right.right)
                && symmetricTreeRecursive(
                left.right,
                right.left);
    }

    /* **********************************************************************
     * 2. Stack Symmetry Check
     * **********************************************************************/

    /**
     * Determines whether the tree is symmetric using a stack.
     *
     * A pair of nodes is pushed together because ArrayDeque
     * does not permit null elements.
     *
     * Time:  O(n)
     * Space: O(h)
     */
    static boolean symmetricTreeStack(Node root) {

        if (root == null) {
            return true;
        }

        ArrayDeque<NodePair> stack =
                new ArrayDeque<>();

        stack.push(
                new NodePair(
                        root.left,
                        root.right));

        while (!stack.isEmpty()) {

            NodePair pair =
                    stack.pop();

            Node left =
                    pair.left();

            Node right =
                    pair.right();

            /*
             * Both subtrees are empty.
             */
            if (left == null && right == null) {
                continue;
            }

            /*
             * Exactly one subtree is empty.
             */
            if (left == null || right == null) {
                return false;
            }

            /*
             * Values must match.
             */
            if (left.val != right.val) {
                return false;
            }

            /*
             * Push mirrored pairs.
             *
             * left.left  <-> right.right
             * left.right <-> right.left
             */
            stack.push(
                    new NodePair(
                            left.left,
                            right.right));

            stack.push(
                    new NodePair(
                            left.right,
                            right.left));
        }

        return true;
    }

    /* **********************************************************************
     * 3. Queue Symmetry Check
     * **********************************************************************/

    /**
     * Determines whether the tree is symmetric using BFS.
     *
     * Node pairs are stored in the queue so that null children
     * can be represented safely.
     *
     * Time:  O(n)
     * Space: O(w)
     */
    static boolean symmetricTreeQueue(Node root) {

        if (root == null) {
            return true;
        }

        Queue<NodePair> queue =
                new ArrayDeque<>();

        queue.offer(
                new NodePair(
                        root.left,
                        root.right));

        while (!queue.isEmpty()) {

            NodePair pair =
                    queue.poll();

            Node left =
                    pair.left();

            Node right =
                    pair.right();

            /*
             * Both subtrees are empty.
             */
            if (left == null && right == null) {
                continue;
            }

            /*
             * Exactly one subtree is empty.
             */
            if (left == null || right == null) {
                return false;
            }

            /*
             * Values must match.
             */
            if (left.val != right.val) {
                return false;
            }

            /*
             * Add mirrored pairs.
             */
            queue.offer(
                    new NodePair(
                            left.left,
                            right.right));

            queue.offer(
                    new NodePair(
                            left.right,
                            right.left));
        }

        return true;
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

    @FunctionalInterface
    interface SymmetryAlgorithm {

        boolean solve(Node root);
    }

    static class SymmetryMethodCase {

        final String name;
        final SymmetryAlgorithm algorithm;

        SymmetryMethodCase(
                String name,
                SymmetryAlgorithm algorithm) {

            this.name = name;
            this.algorithm = algorithm;
        }
    }

    /* **********************************************************************
     * Symmetry Tests
     * **********************************************************************/

    static void runSymmetryTests(
            String algorithmName,
            SymmetryAlgorithm method,
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

                Node input =
                        copyTree(test.root);

                boolean actual =
                        method.solve(input);

                if (actual == test.expected) {

                    passed++;

                    System.out.printf(
                            "PASS %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  expected = "
                                    + test.expected);

                    System.out.println(
                            "  actual   = "
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
    }

    /* **********************************************************************
     * Null Input Tests
     * **********************************************************************/

    static void runNullInputTests() {

        System.out.println(
                "======================================================");

        System.out.println(
                "Null Input Tests");

        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        boolean recursive =
                symmetricTreeRecursive(null);

        if (recursive) {

            passed++;

            System.out.println(
                    "PASS N1 - recursive null tree");

        } else {

            failed++;

            System.out.println(
                    "FAIL N1 - recursive null tree");
        }

        boolean stack =
                symmetricTreeStack(null);

        if (stack) {

            passed++;

            System.out.println(
                    "PASS N2 - stack null tree");

        } else {

            failed++;

            System.out.println(
                    "FAIL N2 - stack null tree");
        }

        boolean queue =
                symmetricTreeQueue(null);

        if (queue) {

            passed++;

            System.out.println(
                    "PASS N3 - queue null tree");

        } else {

            failed++;

            System.out.println(
                    "FAIL N3 - queue null tree");
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
     * Tree Preservation Tests
     * **********************************************************************/

    static void runTreePreservationTests() {

        System.out.println(
                "======================================================");

        System.out.println(
                "Tree Preservation Tests");

        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        Node root =
                node(
                        1,
                        node(
                                2,
                                node(4),
                                node(5)),
                        node(
                                2,
                                node(5),
                                node(4)));

        Node original =
                copyTree(root);

        /*
         * Recursive must not mutate the tree.
         */
        symmetricTreeRecursive(root);

        if (treesEqual(root, original)) {

            passed++;

            System.out.println(
                    "PASS P1 - recursive does not mutate tree");

        } else {

            failed++;

            System.out.println(
                    "FAIL P1 - recursive mutated tree");
        }

        /*
         * Stack must not mutate the tree.
         */
        symmetricTreeStack(root);

        if (treesEqual(root, original)) {

            passed++;

            System.out.println(
                    "PASS P2 - stack does not mutate tree");

        } else {

            failed++;

            System.out.println(
                    "FAIL P2 - stack mutated tree");
        }

        /*
         * Queue must not mutate the tree.
         */
        symmetricTreeQueue(root);

        if (treesEqual(root, original)) {

            passed++;

            System.out.println(
                    "PASS P3 - queue does not mutate tree");

        } else {

            failed++;

            System.out.println(
                    "FAIL P3 - queue mutated tree");
        }

        /*
         * Node count must remain unchanged.
         */
        int before =
                countNodes(root);

        symmetricTreeRecursive(root);
        symmetricTreeStack(root);
        symmetricTreeQueue(root);

        int after =
                countNodes(root);

        if (before == after) {

            passed++;

            System.out.println(
                    "PASS P4 - node count unchanged");

        } else {

            failed++;

            System.out.println(
                    "FAIL P4 - node count changed");
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
     * Random Tree Generation
     * **********************************************************************/

    static Node randomTree(
            Random rng,
            int depth,
            double nodeProbability) {

        if (depth == 0
                || rng.nextDouble() > nodeProbability) {

            return null;
        }

        int value =
                rng.nextInt(20);

        Node root =
                new Node(value);

        root.left =
                randomTree(
                        rng,
                        depth - 1,
                        nodeProbability);

        root.right =
                randomTree(
                        rng,
                        depth - 1,
                        nodeProbability);

        return root;
    }

    /**
     * Creates a random symmetric tree.
     *
     * The two sides are deliberately constructed as
     * mirror images.
     */
    static Node randomSymmetricTree(
            Random rng,
            int depth,
            double nodeProbability) {

        if (depth == 0
                || rng.nextDouble() > nodeProbability) {

            return null;
        }

        int value =
                rng.nextInt(20);

        Node left =
                randomTree(
                        rng,
                        depth - 1,
                        nodeProbability);

        Node mirroredRight =
                mirrorCopy(left);

        return node(
                value,
                left,
                mirroredRight);
    }

    /**
     * Creates a mirrored copy of a tree.
     */
    static Node mirrorCopy(Node root) {

        if (root == null) {
            return null;
        }

        return node(
                root.val,
                mirrorCopy(root.right),
                mirrorCopy(root.left));
    }

    /* **********************************************************************
     * Independent Symmetry Reference
     * **********************************************************************/

    /**
     * Reference implementation used by randomized tests.
     *
     * This is deliberately kept separate from the three public
     * implementations.
     */
    static boolean referenceSymmetric(Node root) {

        if (root == null) {
            return true;
        }

        return referenceMirrorEqual(
                root.left,
                root.right);
    }

    static boolean referenceMirrorEqual(
            Node left,
            Node right) {

        if (left == null && right == null) {
            return true;
        }

        if (left == null || right == null) {
            return false;
        }

        if (left.val != right.val) {
            return false;
        }

        return referenceMirrorEqual(
                left.left,
                right.right)
                && referenceMirrorEqual(
                left.right,
                right.left);
    }

    /* **********************************************************************
     * Randomised Cross Checks
     * **********************************************************************/

    static void runRandomisedTests(
            int iterations) {

        System.out.println(
                "======================================================");

        System.out.println(
                "Randomised Cross Checks");

        System.out.println(
                "======================================================");

        Random rng =
                new Random(20260920L);

        /*
         * ============================================================
         * Arbitrary Trees
         * ============================================================
         */

        for (int iteration = 1;
             iteration <= iterations;
             iteration++) {

            Node original =
                    randomTree(
                            rng,
                            8,
                            0.75);

            boolean expected =
                    referenceSymmetric(
                            original);

            boolean recursive =
                    symmetricTreeRecursive(
                            copyTree(original));

            boolean stack =
                    symmetricTreeStack(
                            copyTree(original));

            boolean queue =
                    symmetricTreeQueue(
                            copyTree(original));

            if (recursive != expected
                    || stack != expected
                    || queue != expected) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "iteration = "
                                + iteration);

                System.out.println(
                        "expected = "
                                + expected);

                System.out.println(
                        "recursive = "
                                + recursive);

                System.out.println(
                        "stack = "
                                + stack);

                System.out.println(
                        "queue = "
                                + queue);

                return;
            }
        }

        System.out.printf(
                "All %d arbitrary-tree tests passed.%n%n",
                iterations);

        /*
         * ============================================================
         * Guaranteed Symmetric Trees
         * ============================================================
         */

        for (int iteration = 1;
             iteration <= iterations;
             iteration++) {

            Node symmetric =
                    randomSymmetricTree(
                            rng,
                            8,
                            0.75);

            boolean recursive =
                    symmetricTreeRecursive(
                            copyTree(symmetric));

            boolean stack =
                    symmetricTreeStack(
                            copyTree(symmetric));

            boolean queue =
                    symmetricTreeQueue(
                            copyTree(symmetric));

            if (!recursive
                    || !stack
                    || !queue) {

                System.out.println(
                        "Randomised symmetric test FAILED");

                System.out.println(
                        "iteration = "
                                + iteration);

                System.out.println(
                        "recursive = "
                                + recursive);

                System.out.println(
                        "stack = "
                                + stack);

                System.out.println(
                        "queue = "
                                + queue);

                return;
            }
        }

        System.out.printf(
                "All %d symmetric-tree tests passed.%n%n",
                iterations);
    }

    /* **********************************************************************
     * Main Test Suite
     * **********************************************************************/

    public static void main(String[] args) {

        List<TestCase> tests =
                new ArrayList<>();

        /*
         * ============================================================
         * Basic Cases
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "B1",
                        null,
                        true,
                        "empty tree"));

        tests.add(
                new TestCase(
                        "B2",
                        node(1),
                        true,
                        "single node"));

        tests.add(
                new TestCase(
                        "B3",
                        node(
                                1,
                                node(2),
                                node(2)),
                        true,
                        "root with equal children"));

        tests.add(
                new TestCase(
                        "B4",
                        node(
                                1,
                                node(
                                        2,
                                        node(3),
                                        node(4)),
                                node(
                                        2,
                                        node(4),
                                        node(3))),
                        true,
                        "complete symmetric tree"));

        /*
         * ============================================================
         * Asymmetric Structure
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "A1",
                        node(
                                1,
                                node(2),
                                null),
                        false,
                        "left child only"));

        tests.add(
                new TestCase(
                        "A2",
                        node(
                                1,
                                null,
                                node(2)),
                        false,
                        "right child only"));

        tests.add(
                new TestCase(
                        "A3",
                        node(
                                1,
                                node(
                                        2,
                                        node(3),
                                        null),
                                node(
                                        2,
                                        null,
                                        node(3))),
                        true,
                        "asymmetric shape but mirrored"));

        tests.add(
                new TestCase(
                        "A4",
                        node(
                                1,
                                node(
                                        2,
                                        node(3),
                                        null),
                                node(
                                        2,
                                        node(3),
                                        null)),
                        false,
                        "same shape but not mirrored"));

        /*
         * ============================================================
         * Value Mismatch
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "V1",
                        node(
                                1,
                                node(2),
                                node(3)),
                        false,
                        "different child values"));

        tests.add(
                new TestCase(
                        "V2",
                        node(
                                1,
                                node(
                                        2,
                                        node(4),
                                        node(5)),
                                node(
                                        2,
                                        node(4),
                                        node(5))),
                        false,
                        "same values but wrong mirror positions"));

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
                                        node(5),
                                        node(5))),
                        true,
                        "all duplicate values"));

        tests.add(
                new TestCase(
                        "D2",
                        node(
                                5,
                                node(
                                        5,
                                        node(5),
                                        null),
                                node(
                                        5,
                                        node(5),
                                        null))),
                        false,
                        "duplicates with asymmetric structure"));

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
                                node(-2)),
                        true,
                        "negative values"));

        tests.add(
                new TestCase(
                        "N2",
                        node(
                                0,
                                node(-1),
                                node(1)),
                        false,
                        "different negative/positive values"));

        /*
         * ============================================================
         * Larger Symmetric Tree
         * ============================================================
         */

        Node largeSymmetric =
                node(
                        1,
                        node(
                                2,
                                node(
                                        4,
                                        node(8),
                                        node(9)),
                                node(5)),
                        node(
                                2,
                                node(5),
                                node(
                                        4,
                                        node(9),
                                        node(8))));

        tests.add(
                new TestCase(
                        "L1",
                        largeSymmetric,
                        true,
                        "larger symmetric tree"));

        /*
         * ============================================================
         * Larger Non-Symmetric Tree
         * ============================================================
         */

        Node largeNonSymmetric =
                node(
                        1,
                        node(
                                2,
                                node(4),
                                node(5)),
                        node(
                                2,
                                node(5),
                                node(6)));

        tests.add(
                new TestCase(
                        "L2",
                        largeNonSymmetric,
                        false,
                        "larger non-symmetric tree"));

        /*
         * ============================================================
         * Header
         * ============================================================
         */

        System.out.println(
                "############################################################");

        System.out.println(
                "##################  SYMMETRIC TREE  #######################");

        System.out.println(
                "############################################################");

        System.out.println();

        /*
         * ============================================================
         * Implementations
         * ============================================================
         */

        List<SymmetryMethodCase> methods =
                List.of(

                        new SymmetryMethodCase(
                                "Recursive Symmetry",
                                SymmetricTree
                                        ::symmetricTreeRecursive),

                        new SymmetryMethodCase(
                                "Stack Symmetry",
                                SymmetricTree
                                        ::symmetricTreeStack),

                        new SymmetryMethodCase(
                                "Queue Symmetry",
                                SymmetricTree
                                        ::symmetricTreeQueue)
                );

        for (SymmetryMethodCase method :
                methods) {

            runSymmetryTests(
                    method.name,
                    method.algorithm,
                    tests);
        }

        /*
         * ============================================================
         * Null Input Tests
         * ============================================================
         */

        runNullInputTests();

        /*
         * ============================================================
         * Tree Preservation Tests
         * ============================================================
         */

        runTreePreservationTests();

        /*
         * ============================================================
         * Randomised Tests
         * ============================================================
         */

        runRandomisedTests(5000);

        /*
         * ============================================================
         * Finished
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
