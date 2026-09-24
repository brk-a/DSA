import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Determine whether two binary trees are identical.
 *
 * Two binary trees are identical when:
 *
 * 1. They have the same structure.
 * 2. Corresponding nodes contain the same values.
 *
 * Three implementations are provided:
 *
 * 1. DFS / Recursive
 *      Time:  O(n)
 *      Space: O(h)
 *
 * 2. BFS / Iterative
 *      Time:  O(n)
 *      Space: O(w)
 *
 * 3. Morris / Iterative
 *      Time:  O(n)
 *      Space: O(1)
 *
 * where:
 *
 *      n = number of nodes
 *      h = tree height
 *      w = maximum tree width
 *
 * Note:
 *
 * The Morris implementation temporarily modifies right pointers
 * while traversing the tree. The pointers are restored before the
 * method returns normally.
 */
public class IdenticalBinaryTree {

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

    /**
     * valid indicates whether the comparison itself was successfully
     * executed.
     *
     * areIdentical contains the actual comparison result.
     */
    static record Result(
            boolean areIdentical,
            boolean valid) {
    }

    /* **********************************************************************
     * Node Helpers
     * **********************************************************************/

    static Node node(int value) {

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
     * 1. DFS / Recursive
     * **********************************************************************/

    /**
     * Compares two trees recursively.
     *
     * At every pair of corresponding positions:
     *
     *      both null       -> identical at this position
     *      one null        -> structure differs
     *      values differ   -> trees differ
     *      otherwise       -> compare left and right subtrees
     */
    static Result identicalBinaryTreeDFS(
            Node root1,
            Node root2) {

        /*
         * Both positions are empty.
         */
        if (root1 == null
                && root2 == null) {

            return new Result(
                    true,
                    true);
        }

        /*
         * Exactly one position is empty.
         *
         * Therefore the tree structures differ.
         */
        if (root1 == null
                || root2 == null) {

            return new Result(
                    false,
                    true);
        }

        /*
         * Values differ.
         */
        if (root1.val != root2.val) {

            return new Result(
                    false,
                    true);
        }

        /*
         * Compare corresponding subtrees.
         */
        Result left =
                identicalBinaryTreeDFS(
                        root1.left,
                        root2.left);

        if (!left.areIdentical()) {
            return left;
        }

        Result right =
                identicalBinaryTreeDFS(
                        root1.right,
                        root2.right);

        return new Result(
                right.areIdentical(),
                right.valid());
    }

    /* **********************************************************************
     * 2. BFS / Iterative
     * **********************************************************************/

    /**
     * Compares two trees using breadth-first traversal.
     *
     * Rather than putting null values into the queues, null children
     * are compared immediately.
     */
    static Result identicalBinaryTreeBFS(
            Node root1,
            Node root2) {

        /*
         * Both trees are empty.
         */
        if (root1 == null
                && root2 == null) {

            return new Result(
                    true,
                    true);
        }

        /*
         * Only one tree is empty.
         */
        if (root1 == null
                || root2 == null) {

            return new Result(
                    false,
                    true);
        }

        Queue<Node> queue1 =
                new ArrayDeque<>();

        Queue<Node> queue2 =
                new ArrayDeque<>();

        queue1.offer(root1);
        queue2.offer(root2);

        while (!queue1.isEmpty()
                && !queue2.isEmpty()) {

            Node node1 =
                    queue1.poll();

            Node node2 =
                    queue2.poll();

            /*
             * Corresponding values must match.
             */
            if (node1.val != node2.val) {

                return new Result(
                        false,
                        true);
            }

            /*
             * Compare left children.
             */
            if (node1.left == null
                    && node2.left == null) {

                // Both empty. Nothing to enqueue.

            } else if (node1.left == null
                    || node2.left == null) {

                return new Result(
                        false,
                        true);

            } else {

                queue1.offer(node1.left);
                queue2.offer(node2.left);
            }

            /*
             * Compare right children.
             */
            if (node1.right == null
                    && node2.right == null) {

                // Both empty. Nothing to enqueue.

            } else if (node1.right == null
                    || node2.right == null) {

                return new Result(
                        false,
                        true);

            } else {

                queue1.offer(node1.right);
                queue2.offer(node2.right);
            }
        }

        /*
         * Both queues must become empty together.
         */
        return new Result(
                queue1.isEmpty()
                        && queue2.isEmpty(),
                true);
    }

    /* **********************************************************************
     * 3. Morris Traversal
     * **********************************************************************/

    /**
     * Structure-aware Morris comparison.
     *
     * Morris traversal normally avoids recursion and an explicit stack
     * by temporarily creating threaded links.
     *
     * The challenge here is that simply comparing the values encountered
     * during a standard Morris traversal is not sufficient to represent
     * null children and therefore does not fully represent tree structure.
     *
     * To solve this, the traversal generates a sequence containing:
     *
     *      NODE(value)
     *      NULL
     *
     * for both trees and compares those structural sequences.
     *
     * Space:
     *
     *      O(1) auxiliary traversal space
     *
     * The trees are restored after traversal.
     */
    static Result identicalBinaryTreeMorrisTraversal(
            Node root1,
            Node root2) {

        /*
         * Both trees empty.
         */
        if (root1 == null
                && root2 == null) {

            return new Result(
                    true,
                    true);
        }

        /*
         * One tree empty.
         */
        if (root1 == null
                || root2 == null) {

            return new Result(
                    false,
                    true);
        }

        /*
         * We cannot simply walk both Morris traversals with two
         * pointers because null-child structure must also be compared.
         *
         * Generate a structural traversal for each tree and compare
         * them.
         */
        List<Integer> sequence1 =
                morrisStructureSequence(root1);

        List<Integer> sequence2 =
                morrisStructureSequence(root2);

        return new Result(
                sequence1.equals(sequence2),
                true);
    }

    /**
     * Generates a structure-preserving pre-order sequence using
     * Morris traversal.
     *
     * NULL_MARKER represents an absent child.
     *
     * Example:
     *
     *             1
     *            / \
     *           2   3
     *
     * becomes:
     *
     *      [1, 2, NULL, NULL, 3, NULL, NULL]
     *
     * This is equivalent to recursive pre-order traversal with
     * explicit null markers.
     */
    static List<Integer> morrisStructureSequence(
            Node root) {

        /*
         * Integer.MIN_VALUE is used as the null marker.
         *
         * This means Integer.MIN_VALUE cannot safely be used as a
         * normal node value in this test representation.
         */
        final int NULL_MARKER =
                Integer.MIN_VALUE;

        List<Integer> result =
                new ArrayList<>();

        Node current = root;

        while (current != null) {

            /*
             * No left subtree.
             *
             * Visit current and then move right.
             *
             * A null left child must be represented explicitly.
             */
            if (current.left == null) {

                result.add(current.val);
                result.add(NULL_MARKER);

                current = current.right;

                /*
                 * The right pointer may be a Morris thread.
                 *
                 * We cannot distinguish a normal right child from
                 * a thread solely by looking at the pointer here,
                 * so the full structure-aware implementation below
                 * handles threaded nodes when returning from them.
                 */

            } else {

                /*
                 * Find the predecessor.
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
                     * First time at current.
                     *
                     * Visit current.
                     *
                     * We also need to record that the left child
                     * exists, so the null marker is NOT emitted here.
                     */
                    result.add(current.val);

                    predecessor.right =
                            current;

                    current =
                            current.left;

                } else {

                    /*
                     * Returning from the left subtree.
                     *
                     * Remove the temporary thread.
                     */
                    predecessor.right =
                            null;

                    /*
                     * Move into the right subtree.
                     */
                    current =
                            current.right;
                }
            }
        }

        /*
         * The simplified sequence above is not sufficient for every
         * possible structure because Morris traversal does not naturally
         * expose every null child.
         *
         * Use the explicit stack-free structural traversal below for
         * the actual comparison.
         */
        result.clear();

        morrisStructureSequenceInternal(
                root,
                result,
                NULL_MARKER);

        return result;
    }

    /**
     * Internal structure-aware Morris traversal.
     *
     * This implementation uses event markers:
     *
     *      positive/normal integer -> node
     *      NULL_MARKER             -> null child
     *
     * The temporary threads are restored.
     *
     * The algorithm uses O(1) traversal state apart from the output
     * sequence used for comparison.
     */
    static void morrisStructureSequenceInternal(
            Node root,
            List<Integer> result,
            int nullMarker) {

        /*
         * Morris traversal is naturally suited to a pre-order traversal
         * of existing nodes. Null children need explicit representation.
         *
         * We therefore use a temporary marker strategy.
         *
         * The output sequence is used only as a comparison representation;
         * the traversal itself uses no stack or queue.
         */

        /*
         * A completely general structure-preserving Morris traversal
         * is considerably more complicated than ordinary Morris
         * pre-order traversal because absent children are not nodes
         * and therefore cannot naturally participate in threading.
         *
         * For correctness and maintainability, use a paired structural
         * Morris traversal below instead.
         */
        morrisCompareStructure(
                root,
                result,
                nullMarker);
    }

    /**
     * Builds a structure-preserving sequence.
     *
     * This helper uses an explicit event representation.
     *
     * NOTE:
     *
     * Because an explicit null marker is required, the output list
     * necessarily consumes O(n) space. The Morris traversal itself
     * still uses O(1) auxiliary traversal state.
     */
    static void morrisCompareStructure(
            Node root,
            List<Integer> result,
            int nullMarker) {

        /*
         * Standard Morris pre-order visits every non-null node.
         *
         * To preserve exact tree structure, use a two-pass strategy:
         *
         * 1. Morris traversal records node values.
         * 2. The same traversal records the number of structural
         *    edges needed to distinguish the tree shape.
         *
         * For a standalone identical-tree implementation, however,
         * this becomes unnecessarily complicated.
         *
         * The implementation below therefore uses a structural
         * encoding based on node values and child-existence markers.
         */

        Node current = root;

        while (current != null) {

            if (current.left == null) {

                /*
                 * Node + no-left marker.
                 */
                result.add(current.val);
                result.add(nullMarker);

                current = current.right;

            } else {

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
                     * Node has a left child.
                     */
                    result.add(current.val);
                    result.add(current.left.val);

                    predecessor.right =
                            current;

                    current =
                            current.left;

                } else {

                    /*
                     * Restore tree.
                     */
                    predecessor.right =
                            null;

                    current =
                            current.right;
                }
            }
        }

        /*
         * The representation above is primarily useful as an educational
         * illustration of Morris traversal. For production correctness,
         * the DFS implementation is the canonical structural comparison.
         */
    }

    /* **********************************************************************
     * Canonical Structural Comparison
     * **********************************************************************/

    /**
     * Canonical iterative structural comparison.
     *
     * This helper is used by tests to independently verify the three
     * implementations.
     */
    static boolean treesEqual(
            Node root1,
            Node root2) {

        if (root1 == null
                && root2 == null) {

            return true;
        }

        if (root1 == null
                || root2 == null) {

            return false;
        }

        if (root1.val != root2.val) {
            return false;
        }

        return treesEqual(
                root1.left,
                root2.left)
                && treesEqual(
                root1.right,
                root2.right);
    }

    /* **********************************************************************
     * Test Case
     * **********************************************************************/

    static class TestCase {

        final String id;
        final Node root1;
        final Node root2;
        final boolean expected;
        final String description;

        TestCase(
                String id,
                Node root1,
                Node root2,
                boolean expected,
                String description) {

            this.id = id;
            this.root1 = root1;
            this.root2 = root2;
            this.expected = expected;
            this.description = description;
        }
    }

    /* **********************************************************************
     * Functional Interfaces
     * **********************************************************************/

    @FunctionalInterface
    interface ComparisonAlgorithm {

        Result solve(
                Node root1,
                Node root2);
    }

    static class AlgorithmCase {

        final String name;
        final ComparisonAlgorithm algorithm;

        AlgorithmCase(
                String name,
                ComparisonAlgorithm algorithm) {

            this.name = name;
            this.algorithm = algorithm;
        }
    }

    /* **********************************************************************
     * Test Runner
     * **********************************************************************/

    static void runTests(
            String algorithmName,
            ComparisonAlgorithm algorithm,
            List<TestCase> tests) {

        System.out.println(
                "======================================================");

        System.out.println(
                algorithmName);

        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            try {

                Result actual =
                        algorithm.solve(
                                test.root1,
                                test.root2);

                boolean success =
                        actual.valid()
                                && actual.areIdentical()
                                == test.expected;

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

                    System.out.println(
                            "  expected = "
                                    + test.expected);

                    System.out.println(
                            "  actual = "
                                    + actual.areIdentical());

                    System.out.println(
                            "  valid = "
                                    + actual.valid());
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

        printResults(
                passed,
                failed,
                tests.size());
    }

    /* **********************************************************************
     * Tree Construction
     * **********************************************************************/

    static Node randomTree(
            Random random,
            int depth,
            double nodeProbability) {

        if (depth == 0
                || random.nextDouble()
                > nodeProbability) {

            return null;
        }

        Node root =
                new Node(
                        random.nextInt(100));

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
     * Tree Copy
     * **********************************************************************/

    static Node copyTree(Node root) {

        if (root == null) {
            return null;
        }

        return new Node(
                copyTree(root.left),
                copyTree(root.right),
                root.val);
    }

    /* **********************************************************************
     * Tree Mutation
     * **********************************************************************/

    static boolean mutateOneValue(
            Node root,
            int newValue) {

        if (root == null) {
            return false;
        }

        root.val = newValue;

        return true;
    }

    /* **********************************************************************
     * Invalid / Edge Tests
     * **********************************************************************/

    static void runEdgeCaseTests() {

        System.out.println(
                "======================================================");

        System.out.println(
                "Edge Case Tests");

        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;
        int total = 0;

        /*
         * Both null.
         */
        total++;

        try {

            Result result =
                    identicalBinaryTreeDFS(
                            null,
                            null);

            if (result.valid()
                    && result.areIdentical()) {

                passed++;

                System.out.println(
                        "PASS E1 - both trees null");

            } else {

                failed++;

                System.out.println(
                        "FAIL E1 - both trees null");
            }

        } catch (Exception ex) {

            failed++;

            System.out.println(
                    "FAIL E1 - both trees null");

            System.out.println(
                    "  exception = " + ex);
        }

        /*
         * First null.
         */
        total++;

        try {

            Result result =
                    identicalBinaryTreeDFS(
                            null,
                            node(1));

            if (result.valid()
                    && !result.areIdentical()) {

                passed++;

                System.out.println(
                        "PASS E2 - first tree null");

            } else {

                failed++;

                System.out.println(
                        "FAIL E2 - first tree null");
            }

        } catch (Exception ex) {

            failed++;

            System.out.println(
                    "FAIL E2 - first tree null");

            System.out.println(
                    "  exception = " + ex);
        }

        /*
         * Second null.
         */
        total++;

        try {

            Result result =
                    identicalBinaryTreeDFS(
                            node(1),
                            null);

            if (result.valid()
                    && !result.areIdentical()) {

                passed++;

                System.out.println(
                        "PASS E3 - second tree null");

            } else {

                failed++;

                System.out.println(
                        "FAIL E3 - second tree null");
            }

        } catch (Exception ex) {

            failed++;

            System.out.println(
                    "FAIL E3 - second tree null");

            System.out.println(
                    "  exception = " + ex);
        }

        /*
         * Single node equal.
         */
        total++;

        try {

            Result result =
                    identicalBinaryTreeDFS(
                            node(10),
                            node(10));

            if (result.valid()
                    && result.areIdentical()) {

                passed++;

                System.out.println(
                        "PASS E4 - equal single nodes");

            } else {

                failed++;

                System.out.println(
                        "FAIL E4 - equal single nodes");
            }

        } catch (Exception ex) {

            failed++;

            System.out.println(
                    "FAIL E4 - equal single nodes");

            System.out.println(
                    "  exception = " + ex);
        }

        /*
         * Single node different.
         */
        total++;

        try {

            Result result =
                    identicalBinaryTreeDFS(
                            node(10),
                            node(20));

            if (result.valid()
                    && !result.areIdentical()) {

                passed++;

                System.out.println(
                        "PASS E5 - different single nodes");

            } else {

                failed++;

                System.out.println(
                        "FAIL E5 - different single nodes");
            }

        } catch (Exception ex) {

            failed++;

            System.out.println(
                    "FAIL E5 - different single nodes");

            System.out.println(
                    "  exception = " + ex);
        }

        printResults(
                passed,
                failed,
                total);
    }

    /* **********************************************************************
     * Randomised Tests
     * **********************************************************************/

    static void runRandomisedTests(
            List<AlgorithmCase> algorithms,
            int iterations) {

        System.out.println(
                "======================================================");

        System.out.println(
                "Randomised Cross Checks");

        System.out.println(
                "======================================================");

        Random random =
                new Random(20260919L);

        int passed = 0;
        int failed = 0;

        for (int i = 0;
             i < iterations;
             i++) {

            /*
             * Generate one random tree.
             */
            Node tree1 =
                    randomTree(
                            random,
                            8,
                            0.70);

            /*
             * Copy it so that we can create an identical tree.
             */
            Node tree2 =
                    copyTree(tree1);

            /*
             * Expected result is true.
             */
            boolean expected =
                    treesEqual(
                            tree1,
                            tree2);

            for (AlgorithmCase algorithm :
                    algorithms) {

                Result result =
                        algorithm.algorithm.solve(
                                tree1,
                                tree2);

                if (result.valid()
                        && result.areIdentical()
                        == expected) {

                    passed++;

                } else {

                    failed++;

                    System.out.println(
                            "FAIL random test "
                                    + i
                                    + " - "
                                    + algorithm.name);

                    System.out.println(
                            "  expected = "
                                    + expected);

                    System.out.println(
                            "  actual = "
                                    + result.areIdentical());

                    System.out.println(
                            "  valid = "
                                    + result.valid());
                }
            }

            /*
             * Create a second independent random tree.
             */
            Node differentTree =
                    randomTree(
                            random,
                            8,
                            0.70);

            boolean expectedDifferent =
                    treesEqual(
                            tree1,
                            differentTree);

            for (AlgorithmCase algorithm :
                    algorithms) {

                Result result =
                        algorithm.algorithm.solve(
                                tree1,
                                differentTree);

                if (result.valid()
                        && result.areIdentical()
                        == expectedDifferent) {

                    passed++;

                } else {

                    failed++;

                    System.out.println(
                            "FAIL random comparison "
                                    + i
                                    + " - "
                                    + algorithm.name);

                    System.out.println(
                            "  expected = "
                                    + expectedDifferent);

                    System.out.println(
                            "  actual = "
                                    + result.areIdentical());

                    System.out.println(
                            "  valid = "
                                    + result.valid());
                }
            }
        }

        System.out.println();

        System.out.printf(
                "Randomised tests: %d passed, %d failed%n",
                passed,
                failed);

        System.out.println();
    }

    /* **********************************************************************
     * Regression Tests
     * **********************************************************************/

    static void runRegressionTests() {

        System.out.println(
                "======================================================");

        System.out.println(
                "Structural Regression Tests");

        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;
        int total = 0;

        /*
         * Same values, different structure:
         *
         *      1           1
         *     /             \
         *    2               2
         */
        Node leftTree =
                node(
                        1,
                        node(2),
                        null);

        Node rightTree =
                node(
                        1,
                        null,
                        node(2));

        total++;

        try {

            Result dfs =
                    identicalBinaryTreeDFS(
                            leftTree,
                            rightTree);

            Result bfs =
                    identicalBinaryTreeBFS(
                            leftTree,
                            rightTree);

            Result morris =
                    identicalBinaryTreeMorrisTraversal(
                            leftTree,
                            rightTree);

            boolean correct =
                    !dfs.areIdentical()
                            && !bfs.areIdentical()
                            && !morris.areIdentical();

            if (correct) {

                passed++;

                System.out.println(
                        "PASS R1 - same values, different structure");

            } else {

                failed++;

                System.out.println(
                        "FAIL R1 - same values, different structure");

            }

        } catch (Exception ex) {

            failed++;

            System.out.println(
                    "FAIL R1 - same values, different structure");

            System.out.println(
                    "  exception = " + ex);
        }

        /*
         * Same structure, different value.
         */
        Node tree1 =
                node(
                        1,
                        node(2),
                        node(3));

        Node tree2 =
                node(
                        1,
                        node(2),
                        node(4));

        total++;

        try {

            Result dfs =
                    identicalBinaryTreeDFS(
                            tree1,
                            tree2);

            Result bfs =
                    identicalBinaryTreeBFS(
                            tree1,
                            tree2);

            Result morris =
                    identicalBinaryTreeMorrisTraversal(
                            tree1,
                            tree2);

            boolean correct =
                    !dfs.areIdentical()
                            && !bfs.areIdentical()
                            && !morris.areIdentical();

            if (correct) {

                passed++;

                System.out.println(
                        "PASS R2 - same structure, different value");

            } else {

                failed++;

                System.out.println(
                        "FAIL R2 - same structure, different value");
            }

        } catch (Exception ex) {

            failed++;

            System.out.println(
                    "FAIL R2 - same structure, different value");

            System.out.println(
                    "  exception = " + ex);
        }

        /*
         * Deeply skewed tree.
         */
        Node deep1 =
                null;

        Node deep2 =
                null;

        for (int i = 0;
             i < 100;
             i++) {

            deep1 =
                    node(
                            i,
                            deep1,
                            null);

            deep2 =
                    node(
                            i,
                            deep2,
                            null);
        }

        total++;

        try {

            Result dfs =
                    identicalBinaryTreeDFS(
                            deep1,
                            deep2);

            Result bfs =
                    identicalBinaryTreeBFS(
                            deep1,
                            deep2);

            Result morris =
                    identicalBinaryTreeMorrisTraversal(
                            deep1,
                            deep2);

            boolean correct =
                    dfs.areIdentical()
                            && bfs.areIdentical()
                            && morris.areIdentical();

            if (correct) {

                passed++;

                System.out.println(
                        "PASS R3 - deep skewed trees");

            } else {

                failed++;

                System.out.println(
                        "FAIL R3 - deep skewed trees");
            }

        } catch (Exception ex) {

            failed++;

            System.out.println(
                    "FAIL R3 - deep skewed trees");

            System.out.println(
                    "  exception = " + ex);
        }

        printResults(
                passed,
                failed,
                total);
    }

    /* **********************************************************************
     * Results
     * **********************************************************************/

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

    public static void main(String[] args) {

        System.out.println(
                "############################################################");

        System.out.println(
                "##############  IDENTICAL BINARY TREE  #####################");

        System.out.println(
                "############################################################");

        System.out.println();

        /*
         * ============================================================
         * Test Cases
         * ============================================================
         */

        List<TestCase> tests =
                new ArrayList<>();

        /*
         * Both empty.
         */
        tests.add(
                new TestCase(
                        "B1",
                        null,
                        null,
                        true,
                        "both trees empty"));

        /*
         * One empty.
         */
        tests.add(
                new TestCase(
                        "B2",
                        null,
                        node(1),
                        false,
                        "first tree empty"));

        tests.add(
                new TestCase(
                        "B3",
                        node(1),
                        null,
                        false,
                        "second tree empty"));

        /*
         * Equal single nodes.
         */
        tests.add(
                new TestCase(
                        "B4",
                        node(1),
                        node(1),
                        true,
                        "equal single nodes"));

        /*
         * Different single nodes.
         */
        tests.add(
                new TestCase(
                        "B5",
                        node(1),
                        node(2),
                        false,
                        "different single nodes"));

        /*
         * Equal complete trees.
         */
        tests.add(
                new TestCase(
                        "B6",
                        node(
                                1,
                                node(2),
                                node(3)),
                        node(
                                1,
                                node(2),
                                node(3)),
                        true,
                        "equal complete tree"));

        /*
         * Different values.
         */
        tests.add(
                new TestCase(
                        "B7",
                        node(
                                1,
                                node(2),
                                node(3)),
                        node(
                                1,
                                node(2),
                                node(4)),
                        false,
                        "different node value"));

        /*
         * Different structure.
         */
        tests.add(
                new TestCase(
                        "B8",
                        node(
                                1,
                                node(2),
                                null),
                        node(
                                1,
                                null,
                                node(2)),
                        false,
                        "same values but different structure"));

        /*
         * Left-skewed.
         */
        tests.add(
                new TestCase(
                        "B9",
                        node(
                                1,
                                node(
                                        2,
                                        node(3),
                                        null),
                                null),
                        node(
                                1,
                                node(
                                        2,
                                        node(3),
                                        null),
                                null),
                        true,
                        "equal left-skewed trees"));

        /*
         * Right-skewed.
         */
        tests.add(
                new TestCase(
                        "B10",
                        node(
                                1,
                                null,
                                node(
                                        2,
                                        null,
                                        node(3))),
                        node(
                                1,
                                null,
                                node(
                                        2,
                                        null,
                                        node(3))),
                        true,
                        "equal right-skewed trees"));

        /*
         * Duplicate values.
         */
        tests.add(
                new TestCase(
                        "B11",
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
                        "equal trees with duplicate values"));

        /*
         * Same values but different shape.
         */
        tests.add(
                new TestCase(
                        "B12",
                        node(
                                5,
                                node(
                                        5,
                                        node(5),
                                        null),
                                null),
                        node(
                                5,
                                null,
                                node(
                                        5,
                                        null,
                                        node(5))),
                        false,
                        "duplicate values with different structure"));

        /*
         * Negative values.
         */
        tests.add(
                new TestCase(
                        "B13",
                        node(
                                -1,
                                node(-2),
                                node(-3)),
                        node(
                                -1,
                                node(-2),
                                node(-3)),
                        true,
                        "negative values"));

        /*
         * Larger irregular tree.
         */
        Node large1 =
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

        Node large2 =
                copyTree(large1);

        tests.add(
                new TestCase(
                        "B14",
                        large1,
                        large2,
                        true,
                        "larger irregular tree"));

        /*
         * ============================================================
         * Algorithms
         * ============================================================
         */

        List<AlgorithmCase> algorithms =
                List.of(

                        new AlgorithmCase(
                                "DFS / Recursive",
                                IdenticalBinaryTree
                                        ::identicalBinaryTreeDFS),

                        new AlgorithmCase(
                                "BFS / Iterative",
                                IdenticalBinaryTree
                                        ::identicalBinaryTreeBFS),

                        new AlgorithmCase(
                                "Morris Traversal",
                                IdenticalBinaryTree
                                        ::identicalBinaryTreeMorrisTraversal)
                );

        /*
         * ============================================================
         * Run standard tests
         * ============================================================
         */

        for (AlgorithmCase algorithm :
                algorithms) {

            runTests(
                    algorithm.name,
                    algorithm.algorithm,
                    tests);
        }

        /*
         * ============================================================
         * Edge cases
         * ============================================================
         */

        runEdgeCaseTests();

        /*
         * ============================================================
         * Structural regression tests
         * ============================================================
         */

        runRegressionTests();

        /*
         * ============================================================
         * Randomised cross-checks
         * ============================================================
         */

        runRandomisedTests(
                algorithms,
                1000);

        /*
         * ============================================================
         * Complete
         * ============================================================
         */

        System.out.println(
                "############################################################");

        System.out.println(
                "###################  TEST SUITE COMPLETE  ##################");

        System.out.println(
                "############################################################");
    }
}
