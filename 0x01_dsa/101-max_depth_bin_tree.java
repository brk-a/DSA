import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Maximum Depth of a Binary Tree.
 *
 * Problem:
 * Given the root of a binary tree, find its maximum depth: the number of
 * EDGES on the longest path from the root down to a leaf. Under this
 * convention (matched by both methods below), a null tree has depth -1,
 * a single-node tree has depth 0, and each additional level adds 1.
 *
 * Notes:
 * - If root is null, Result.valid() == false with depth -1.
 * - Node was a non-static inner class in the original, which meant it
 *   could only ever be instantiated through an existing
 *   MaxDepthBinaryTree instance - confirmed directly: `new
 *   Original.Node(5)` from a static context fails with "an enclosing
 *   instance...is required". Since every method here is static and a
 *   tree has to be built by SOMETHING to be tested at all, Node needed to
 *   be static for the class to be usable.
 *
 * Implementations:
 *
 * 1. Recursion (DFS)
 *      depth(node) = 1 + max(depth(node.left), depth(node.right)),
 *      with depth(null) = -1.
 *      Time: O(n)   Space: O(h) recursion depth, h = tree height.
 *
 * 2. Level-Order Traversal (BFS)
 *      Counts how many full levels are visited, then subtracts 1 to
 *      convert "levels visited" into "edges on the longest path".
 *      Time: O(n)   Space: O(w), w = the tree's widest level.
 *
 * Both are cross-checked against each other for both fixed and randomly
 * generated trees, following the same test-harness shape used for
 * SumOfDiagonalsMatrixTestHarness (TestCase / Algorithm / MethodCase /
 * runTests / randomised cross-checks).
 */
public class MaxDepthBinaryTree {

    static class Node {
        Node left;
        Node right;
        int val;

        Node(int val) {
            this.left = null;
            this.right = null;
            this.val = val;
        }
    }

    static record Result(int depth, boolean valid) {}

    /* **********************************************************************
     * Validation
     * **********************************************************************/

    static boolean validNode(Node node) {
        return node != null;
    }

    /* **********************************************************************
     * Algorithm Implementations
     * **********************************************************************/

    static Result maxDepthBinaryTreeRecursion(Node root) {
        if (!validNode(root)) {
            return new Result(-1, false);
        }

        int leftHeight = maxDepthBinaryTreeRecursion(root.left).depth();
        int rightHeight = maxDepthBinaryTreeRecursion(root.right).depth();
        // +1 accounts for root's own edge to whichever child subtree is
        // deeper; without it every tree would compute the same depth as
        // an empty one, since it's just re-deriving max(leftHeight, rightHeight)
        // with no contribution from the current node at all.
        int finalHeight = 1 + Math.max(leftHeight, rightHeight);

        return new Result(finalHeight, true);
    }

    static Result maxDepthBinaryTreeLevelOrderTraversal(Node root) {
        if (!validNode(root)) {
            return new Result(-1, false);
        }

        Queue<Node> queue = new LinkedList<>();
        queue.add(root);
        int depth = 0;

        while (!queue.isEmpty()) {
            int lvlSize = queue.size();
            for (int i = 0; i < lvlSize; i++) {
                Node curr = queue.poll();
                if (curr.left != null) {
                    queue.add(curr.left);
                }
                if (curr.right != null) {
                    queue.add(curr.right);
                }
            }

            depth++;
        }

        // depth counts LEVELS visited (root's level counts as 1); subtract
        // 1 to convert that into "edges on the longest root-to-leaf path".
        int finalHeight = depth - 1;

        return new Result(finalHeight, true);
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

    static Node node(int val, Node left, Node right) {
        Node n = new Node(val);
        n.left = left;
        n.right = right;
        return n;
    }

    static Node leaf(int val) {
        return new Node(val);
    }

    /** Builds a left-only chain of the given length (length nodes total). */
    static Node leftChain(int length) {
        Node curr = null;
        for (int v = length; v >= 1; v--) {
            Node next = new Node(v);
            next.left = curr;
            curr = next;
        }
        return curr;
    }

    /* **********************************************************************
     * Utilities
     * **********************************************************************/

    static boolean resultsEqual(Result a, Result b) {
        if (a == null && b == null) {
            return true;
        }
        if (a == null || b == null) {
            return false;
        }
        return a.depth() == b.depth() && a.valid() == b.valid();
    }

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

                Result actual = method.solve(test.input);

                if (resultsEqual(actual, test.expected)) {

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
                            "  expected  = %s%n",
                            test.expected);

                    System.out.printf(
                            "  actual    = %s%n",
                            actual);
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

    static Node randomTree(Random rng, int maxNodes) {
        int budget = rng.nextInt(maxNodes) + 1;
        return randomTreeHelper(rng, new int[]{budget});
    }

    /** Consumes from the shared node budget as it randomly grows left/right subtrees. */
    static Node randomTreeHelper(Random rng, int[] budget) {
        if (budget[0] <= 0) {
            return null;
        }
        budget[0]--;
        Node n = new Node(budget[0]);
        if (budget[0] > 0 && rng.nextBoolean()) {
            n.left = randomTreeHelper(rng, budget);
        }
        if (budget[0] > 0 && rng.nextBoolean()) {
            n.right = randomTreeHelper(rng, budget);
        }
        return n;
    }

    static void runRandomisedTests(int iterations) {

        System.out.println(
                "======================================================");
        System.out.println(
                "Randomised Cross Checks (Recursion vs Level-Order)");
        System.out.println(
                "======================================================");

        Random rng = new Random(20260910L);

        for (int i = 1; i <= iterations; i++) {

            Node tree = randomTree(rng, 200);
            if (tree == null) {
                continue;
            }

            Result recursion = maxDepthBinaryTreeRecursion(tree);
            Result levelOrder = maxDepthBinaryTreeLevelOrderTraversal(tree);

            if (!resultsEqual(recursion, levelOrder)) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "recursion  = " + recursion);

                System.out.println(
                        "levelOrder = " + levelOrder);

                return;
            }
        }

        System.out.printf(
                "All %d Randomised tests passed.%n%n",
                iterations);
    }

    /* **********************************************************************
     * Main
     * **********************************************************************/

    public static void main(String[] args) {

        List<TestCase> tests = new ArrayList<>();

        /*
         * ============================================================
         * Basic Cases
         * (expected depths verified independently before being
         * hardcoded here)
         * ============================================================
         */

        tests.add(new TestCase(
                "B1",
                leaf(1),
                new Result(0, true),
                "single node: no edges, depth 0"));

        tests.add(new TestCase(
                "B2",
                node(1, leaf(2), null),
                new Result(1, true),
                "root with only a left child"));

        tests.add(new TestCase(
                "B3",
                node(1, null, leaf(2)),
                new Result(1, true),
                "root with only a right child"));

        tests.add(new TestCase(
                "B4",
                node(1, leaf(2), leaf(3)),
                new Result(1, true),
                "root with both children, balanced at depth 1"));

        tests.add(new TestCase(
                "B5",
                node(1,
                        node(2, leaf(4), leaf(5)),
                        node(3, leaf(6), leaf(7))),
                new Result(2, true),
                "perfectly balanced 7-node tree"));

        /*
         * ============================================================
         * Skewed / Unbalanced Trees
         * (guards the recursion method's missing "+1": without it,
         * every tree computes the same depth as an empty one)
         * ============================================================
         */

        tests.add(new TestCase(
                "R1",
                leftChain(5),
                new Result(4, true),
                "5-node left-only chain (like a linked list)"));

        tests.add(new TestCase(
                "R2",
                node(1, node(2, leaf(3), null), null),
                new Result(2, true),
                "left subtree is deeper than the (absent) right subtree"));

        tests.add(new TestCase(
                "R3",
                node(1,
                        leaf(2),
                        node(3, node(4, leaf(5), null), null)),
                new Result(3, true),
                "right subtree deeper than left: max must pick the taller side"));

        /*
         * ============================================================
         * Edge Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "E1",
                null,
                new Result(-1, false),
                "null tree"));

        System.out.println(
                "############################################################");
        System.out.println(
                "###################  MAX DEPTH BINARY TREE  ################");
        System.out.println(
                "############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "Recursion",
                        MaxDepthBinaryTree::maxDepthBinaryTreeRecursion),

                new MethodCase(
                        "Level-Order Traversal",
                        MaxDepthBinaryTree::maxDepthBinaryTreeLevelOrderTraversal)
        );

        for (MethodCase method : methods) {

            runTests(
                    method.name,
                    method.algorithm,
                    tests);
        }

        runRandomisedTests(5000);
    }
}
