import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Mirror a binary tree in place.
 *
 * Two implementations are provided:
 *
 * 1. Recursive
 *      depth-first traversal
 *
 * 2. BFS
 *      breadth-first traversal
 *
 * Both implementations:
 *
 *      - modify the original tree in place
 *      - return the original root
 *      - preserve all node values
 *      - swap every node's left and right children
 *
 * Complexity:
 *
 * 1. Recursive
 *      Time:  O(n)
 *      Space: O(h)
 *
 * 2. BFS
 *      Time:  O(n)
 *      Space: O(w)
 *
 * where:
 *
 *      n = number of nodes
 *      h = tree height
 *      w = maximum tree width
 */
public class MirrorTree {

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
     * Swaps the left and right children of a node.
     */
    static void swapChildren(Node node) {

        Node temp = node.left;
        node.left = node.right;
        node.right = temp;
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
     *
     * This is used by the test suite so that the recursive
     * and BFS implementations receive independent trees.
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
     * Counts the number of nodes in a tree.
     */
    static int countNodes(Node root) {

        if (root == null) {
            return 0;
        }

        return 1
                + countNodes(root.left)
                + countNodes(root.right);
    }

    /**
     * Returns the tree as a level-order list.
     *
     * Null children are represented by null values.
     *
     * This is primarily useful for readable test output.
     */
    static List<Integer> levelOrder(Node root) {

        List<Integer> result =
                new ArrayList<>();

        if (root == null) {
            return result;
        }

        Queue<Node> queue =
                new ArrayDeque<>();

        /*
         * ArrayDeque does not permit null values, so null
         * children are not inserted into the queue.
         *
         * The structure is therefore represented by the
         * sequence of non-null nodes visited in BFS order.
         */
        queue.offer(root);

        while (!queue.isEmpty()) {

            Node current =
                    queue.poll();

            result.add(current.val);

            if (current.left != null) {
                queue.offer(current.left);
            }

            if (current.right != null) {
                queue.offer(current.right);
            }
        }

        return result;
    }

    /* **********************************************************************
     * 1. Recursive Mirror
     * **********************************************************************/

    /**
     * Mirrors the tree recursively, in place.
     *
     * Time:  O(n)
     * Space: O(h)
     */
    static Node mirrorTreeRecursive(Node root) {

        if (root == null) {
            return null;
        }

        swapChildren(root);

        mirrorTreeRecursive(root.left);
        mirrorTreeRecursive(root.right);

        return root;
    }

    /* **********************************************************************
     * 2. BFS Mirror
     * **********************************************************************/

    /**
     * Mirrors the tree using breadth-first traversal, in place.
     *
     * Time:  O(n)
     * Space: O(w)
     */
    static Node mirrorTreeBfs(Node root) {

        if (root == null) {
            return null;
        }

        Queue<Node> queue =
                new ArrayDeque<>();

        queue.offer(root);

        while (!queue.isEmpty()) {

            Node current =
                    queue.poll();

            swapChildren(current);

            if (current.left != null) {
                queue.offer(current.left);
            }

            if (current.right != null) {
                queue.offer(current.right);
            }
        }

        return root;
    }

    /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    static class TestCase {

        final String id;
        final Node root;
        final String description;

        TestCase(
                String id,
                Node root,
                String description) {

            this.id = id;
            this.root = root;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface MirrorAlgorithm {

        Node solve(Node root);
    }

    static class MirrorMethodCase {

        final String name;
        final MirrorAlgorithm algorithm;

        MirrorMethodCase(
                String name,
                MirrorAlgorithm algorithm) {

            this.name = name;
            this.algorithm = algorithm;
        }
    }

    /* **********************************************************************
     * Expected Mirror
     * **********************************************************************/

    /**
     * Creates the expected mirrored tree without modifying
     * the input tree.
     *
     * This gives the tests an independent reference
     * implementation.
     */
    static Node expectedMirror(Node root) {

        if (root == null) {
            return null;
        }

        return node(
                root.val,
                expectedMirror(root.right),
                expectedMirror(root.left));
    }

    /* **********************************************************************
     * Mirror Tests
     * **********************************************************************/

    static void runMirrorTests(
            String algorithmName,
            MirrorAlgorithm method,
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

                Node original =
                        copyTree(test.root);

                Node expected =
                        expectedMirror(test.root);

                Node actual =
                        method.solve(original);

                /*
                 * The algorithm should return the same
                 * root object that it received.
                 */
                boolean sameRoot =
                        actual == original;

                boolean correct =
                        treesEqual(
                                actual,
                                expected);

                if (sameRoot && correct) {

                    passed++;

                    System.out.printf(
                            "PASS %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  result = "
                                    + levelOrder(actual));

                } else {

                    failed++;

                    System.out.printf(
                            "FAIL %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  expected = "
                                    + levelOrder(expected));

                    System.out.println(
                            "  actual = "
                                    + levelOrder(actual));

                    System.out.println(
                            "  same root = "
                                    + sameRoot);
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

        /*
         * Recursive implementation.
         */
        Node recursiveResult =
                mirrorTreeRecursive(null);

        if (recursiveResult == null) {

            passed++;

            System.out.println(
                    "PASS N1 - recursive null root");

        } else {

            failed++;

            System.out.println(
                    "FAIL N1 - recursive null root");
        }

        /*
         * BFS implementation.
         */
        Node bfsResult =
                mirrorTreeBfs(null);

        if (bfsResult == null) {

            passed++;

            System.out.println(
                    "PASS N2 - BFS null root");

        } else {

            failed++;

            System.out.println(
                    "FAIL N2 - BFS null root");
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
     * Double Mirror Tests
     * **********************************************************************/

    static void runDoubleMirrorTests(
            List<TestCase> tests) {

        System.out.println(
                "======================================================");

        System.out.println(
                "Double Mirror Tests");

        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            try {

                Node original =
                        copyTree(test.root);

                Node result =
                        mirrorTreeRecursive(original);

                result =
                        mirrorTreeRecursive(result);

                if (treesEqual(
                        result,
                        test.root)) {

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
                            "  original = "
                                    + levelOrder(test.root));

                    System.out.println(
                            "  result = "
                                    + levelOrder(result));
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
     * In-place Tests
     * **********************************************************************/

    static void runInPlaceTests() {

        System.out.println(
                "======================================================");

        System.out.println(
                "In-place Mutation Tests");

        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        /*
         * Build:
         *
         *        1
         *       / \
         *      2   3
         */
        Node root =
                node(
                        1,
                        node(2),
                        node(3));

        Node left =
                root.left;

        Node right =
                root.right;

        Node result =
                mirrorTreeRecursive(root);

        /*
         * The same root object must be returned.
         */
        if (result == root) {

            passed++;

            System.out.println(
                    "PASS P1 - recursive returns same root");

        } else {

            failed++;

            System.out.println(
                    "FAIL P1 - recursive replaced root");
        }

        /*
         * The original child objects should still exist,
         * just in opposite positions.
         */
        if (root.left == right
                && root.right == left) {

            passed++;

            System.out.println(
                    "PASS P2 - recursive swaps child references");

        } else {

            failed++;

            System.out.println(
                    "FAIL P2 - recursive child references incorrect");
        }

        /*
         * Repeat with BFS.
         */
        root =
                node(
                        1,
                        node(2),
                        node(3));

        left =
                root.left;

        right =
                root.right;

        result =
                mirrorTreeBfs(root);

        if (result == root) {

            passed++;

            System.out.println(
                    "PASS P3 - BFS returns same root");

        } else {

            failed++;

            System.out.println(
                    "FAIL P3 - BFS replaced root");
        }

        if (root.left == right
                && root.right == left) {

            passed++;

            System.out.println(
                    "PASS P4 - BFS swaps child references");

        } else {

            failed++;

            System.out.println(
                    "FAIL P4 - BFS child references incorrect");
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
                rng.nextInt(1000);

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

        int completed = 0;

        while (completed < iterations) {

            Node original =
                    randomTree(
                            rng,
                            8,
                            0.75);

            /*
             * Include only non-empty trees.
             */
            if (original == null) {
                continue;
            }

            completed++;

            Node expected =
                    expectedMirror(original);

            /*
             * ========================================================
             * Recursive
             * ========================================================
             */

            Node recursiveInput =
                    copyTree(original);

            Node recursiveResult =
                    mirrorTreeRecursive(
                            recursiveInput);

            if (!treesEqual(
                    recursiveResult,
                    expected)) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "iteration = " + completed);

                System.out.println(
                        "recursive expected = "
                                + levelOrder(expected));

                System.out.println(
                        "recursive actual = "
                                + levelOrder(recursiveResult));

                return;
            }

            /*
             * ========================================================
             * BFS
             * ========================================================
             */

            Node bfsInput =
                    copyTree(original);

            Node bfsResult =
                    mirrorTreeBfs(bfsInput);

            if (!treesEqual(
                    bfsResult,
                    expected)) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "iteration = " + completed);

                System.out.println(
                        "BFS expected = "
                                + levelOrder(expected));

                System.out.println(
                        "BFS actual = "
                                + levelOrder(bfsResult));

                return;
            }

            /*
             * ========================================================
             * Implementations must agree
             * ========================================================
             */

            if (!treesEqual(
                    recursiveResult,
                    bfsResult)) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "iteration = " + completed);

                System.out.println(
                        "recursive = "
                                + levelOrder(recursiveResult));

                System.out.println(
                        "BFS = "
                                + levelOrder(bfsResult));

                return;
            }

            /*
             * ========================================================
             * Node count must remain unchanged
             * ========================================================
             */

            if (countNodes(recursiveResult)
                    != countNodes(original)) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "iteration = " + completed);

                System.out.println(
                        "node count changed after recursive mirror");

                return;
            }

            if (countNodes(bfsResult)
                    != countNodes(original)) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "iteration = " + completed);

                System.out.println(
                        "node count changed after BFS mirror");

                return;
            }
        }

        System.out.printf(
                "All %d randomised tests passed.%n%n",
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
                        node(1),
                        "single node"));

        tests.add(
                new TestCase(
                        "B2",
                        node(
                                1,
                                node(2),
                                node(3)),
                        "root with two children"));

        tests.add(
                new TestCase(
                        "B3",
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
                        "complete three-level tree"));

        /*
         * ============================================================
         * Asymmetric Trees
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "A1",
                        node(
                                1,
                                node(
                                        2,
                                        node(4),
                                        null),
                                null),
                        "left-skewed tree"));

        tests.add(
                new TestCase(
                        "A2",
                        node(
                                1,
                                null,
                                node(
                                        3,
                                        null,
                                        node(6))),
                        "right-skewed tree"));

        tests.add(
                new TestCase(
                        "A3",
                        node(
                                1,
                                node(2),
                                null),
                        "left child only"));

        tests.add(
                new TestCase(
                        "A4",
                        node(
                                1,
                                null,
                                node(3)),
                        "right child only"));

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
                        "negative values"));

        /*
         * ============================================================
         * Zero Values
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "N2",
                        node(
                                0,
                                node(-1),
                                node(1)),
                        "zero and negative/positive values"));

        /*
         * ============================================================
         * Larger Irregular Tree
         * ============================================================
         */

        Node largeTree =
                node(
                        10,
                        node(
                                20,
                                node(40),
                                node(
                                        50,
                                        node(80),
                                        null)),
                        node(
                                30,
                                node(
                                        60,
                                        null,
                                        node(90)),
                                node(70)));

        tests.add(
                new TestCase(
                        "L1",
                        largeTree,
                        "larger irregular tree"));

        /*
         * ============================================================
         * Header
         * ============================================================
         */

        System.out.println(
                "############################################################");

        System.out.println(
                "####################  MIRROR TREE  ########################");

        System.out.println(
                "############################################################");

        System.out.println();

        /*
         * ============================================================
         * Mirror Implementations
         * ============================================================
         */

        List<MirrorMethodCase> methods =
                List.of(

                        new MirrorMethodCase(
                                "Recursive Mirror",
                                MirrorTree
                                        ::mirrorTreeRecursive),

                        new MirrorMethodCase(
                                "BFS Mirror",
                                MirrorTree
                                        ::mirrorTreeBfs)
                );

        for (MirrorMethodCase method :
                methods) {

            runMirrorTests(
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
         * In-place Tests
         * ============================================================
         */

        runInPlaceTests();

        /*
         * ============================================================
         * Double Mirror Tests
         * ============================================================
         */

        runDoubleMirrorTests(tests);

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
