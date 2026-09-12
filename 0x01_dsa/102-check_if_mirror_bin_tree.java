import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;

/**
 * Determine whether two binary trees are mirrors of each other.
 *
 * Two trees are mirrors when:
 * - corresponding nodes contain the same value, and
 * - the left subtree of one is the mirror of the right subtree of the other.
 *
 * Result:
 * - isMirror == true  -> trees are mirrors
 * - isMirror == false -> trees are not mirrors
 * - valid == false    -> the pair has an invalid structural mismatch
 *
 * In this implementation, null/null is valid and represents two empty
 * corresponding subtrees.
 *
 * Implementations:
 *
 * 1. Recursion
 *      Time:  O(n)
 *      Space: O(h) call stack
 *
 * 2. Queue / Breadth First
 *      Time:  O(n)
 *      Space: O(w), where w is maximum tree width
 *
 * 3. Two Stacks / Depth First
 *      Time:  O(n)
 *      Space: O(h)
 */
public class MirrorTrees {

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

        Node(int val, Node left, Node right) {
            this.val = val;
            this.left = left;
            this.right = right;
        }
    }

    /* **********************************************************************
     * Result
     * **********************************************************************/

    static record Result(boolean isMirror, boolean valid) {}

    /* **********************************************************************
     * Helpers
     * **********************************************************************/

    static boolean validPair(Node root1, Node root2) {

        // Both null is a valid pair.
        if (root1 == null && root2 == null) {
            return true;
        }

        // Exactly one null means the structures do not correspond.
        return root1 != null && root2 != null;
    }

    static Node node(int value) {
        return new Node(value);
    }

    static Node node(int value, Node left, Node right) {
        return new Node(value, left, right);
    }

    /* **********************************************************************
     * 1. Recursive Implementation
     * **********************************************************************/

    static Result mirrorTreesRecursion(Node root1, Node root2) {

        if (!validPair(root1, root2)) {
            return new Result(false, false);
        }

        if (root1 == null) {
            return new Result(true, true);
        }

        if (root1.val != root2.val) {
            return new Result(false, true);
        }

        Result left = mirrorTreesRecursion(
                root1.left,
                root2.right);

        if (!left.valid()) {
            return new Result(false, false);
        }

        Result right = mirrorTreesRecursion(
                root1.right,
                root2.left);

        if (!right.valid()) {
            return new Result(false, false);
        }

        return new Result(
                left.isMirror() && right.isMirror(),
                true);
    }

    /* **********************************************************************
     * 2. Queue / Breadth-First Implementation
     * **********************************************************************/

    record NodePair(Node first, Node second) {}

    static Result mirrorTreesQueue(Node root1, Node root2) {

        if (!validPair(root1, root2)) {
            return new Result(false, false);
        }

        if (root1 == null) {
            return new Result(true, true);
        }

        Queue<NodePair> queue = new ArrayDeque<>();

        queue.add(new NodePair(root1, root2));

        while (!queue.isEmpty()) {

            NodePair pair = queue.remove();

            Node first = pair.first();
            Node second = pair.second();

            if (!validPair(first, second)) {
                return new Result(false, false);
            }

            if (first == null) {
                continue;
            }

            if (first.val != second.val) {
                return new Result(false, true);
            }

            /*
             * Mirror relationship:
             *
             * first.left  <-> second.right
             * first.right <-> second.left
             */
            queue.add(new NodePair(
                    first.left,
                    second.right));

            queue.add(new NodePair(
                    first.right,
                    second.left));
        }

        return new Result(true, true);
    }

    /* **********************************************************************
     * 3. Two-Stack / Depth-First Implementation
     * **********************************************************************/

    static Result mirrorTreesTwoStacks(Node root1, Node root2) {

        if (!validPair(root1, root2)) {
            return new Result(false, false);
        }

        if (root1 == null) {
            return new Result(true, true);
        }

        Stack<Node> stack1 = new Stack<>();
        Stack<Node> stack2 = new Stack<>();

        stack1.push(root1);
        stack2.push(root2);

        while (!stack1.empty() && !stack2.empty()) {

            Node first = stack1.pop();
            Node second = stack2.pop();

            if (!validPair(first, second)) {
                return new Result(false, false);
            }

            if (first == null) {
                continue;
            }

            if (first.val != second.val) {
                return new Result(false, true);
            }

            /*
             * Push corresponding mirror pairs.
             *
             * first.left  <-> second.right
             * first.right <-> second.left
             */
            stack1.push(first.left);
            stack2.push(second.right);

            stack1.push(first.right);
            stack2.push(second.left);
        }

        /*
         * If one stack still contains nodes, the structures differ.
         */
        if (!stack1.empty() || !stack2.empty()) {
            return new Result(false, false);
        }

        return new Result(true, true);
    }

    /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    static class TestCase {

        final String id;
        final Node root1;
        final Node root2;
        final Result expected;
        final String description;

        TestCase(
                String id,
                Node root1,
                Node root2,
                Result expected,
                String description) {

            this.id = id;
            this.root1 = root1;
            this.root2 = root2;
            this.expected = expected;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {

        Result solve(Node root1, Node root2);
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
     * Test Utilities
     * **********************************************************************/

    static boolean resultsEqual(Result a, Result b) {

        if (a == null && b == null) {
            return true;
        }

        if (a == null || b == null) {
            return false;
        }

        return a.isMirror() == b.isMirror()
                && a.valid() == b.valid();
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

                Result actual = method.solve(
                        test.root1,
                        test.root2);

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
                            "  expected = %s%n",
                            test.expected);

                    System.out.printf(
                            "  actual   = %s%n",
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
     * Random Tree Generation
     * **********************************************************************/

    static Node randomTree(
            Random rng,
            int depth,
            double nodeProbability) {

        if (depth == 0 || rng.nextDouble() > nodeProbability) {
            return null;
        }

        int value = rng.nextInt(5);

        Node node = new Node(value);

        node.left = randomTree(
                rng,
                depth - 1,
                nodeProbability);

        node.right = randomTree(
                rng,
                depth - 1,
                nodeProbability);

        return node;
    }

    /**
     * Creates the exact mirror of a tree.
     */
    static Node mirrorOf(Node root) {

        if (root == null) {
            return null;
        }

        return new Node(
                root.val,
                mirrorOf(root.right),
                mirrorOf(root.left));
    }

    /**
     * Creates a deep copy of a tree.
     */
    static Node copyTree(Node root) {

        if (root == null) {
            return null;
        }

        return new Node(
                root.val,
                copyTree(root.left),
                copyTree(root.right));
    }

    /* **********************************************************************
     * Randomised Cross Checks
     * **********************************************************************/

    static void runRandomisedTests(int iterations) {

        System.out.println(
                "======================================================");

        System.out.println(
                "Randomised Cross Checks");

        System.out.println(
                "======================================================");

        Random rng = new Random(20260911L);

        for (int i = 1; i <= iterations; i++) {

            Node root1 = randomTree(
                    rng,
                    8,
                    0.75);

            /*
             * Half the time, deliberately create a true mirror.
             * The other half creates an independent random tree.
             */
            Node root2;

            if (i % 2 == 0) {
                root2 = mirrorOf(root1);
            } else {
                root2 = randomTree(
                        rng,
                        8,
                        0.75);
            }

            Result recursive =
                    mirrorTreesRecursion(root1, root2);

            Result queue =
                    mirrorTreesQueue(root1, root2);

            Result stacks =
                    mirrorTreesTwoStacks(root1, root2);

            if (!resultsEqual(recursive, queue)
                    || !resultsEqual(recursive, stacks)) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "iteration = " + i);

                System.out.println(
                        "recursive = " + recursive);

                System.out.println(
                        "queue     = " + queue);

                System.out.println(
                        "stacks    = " + stacks);

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

        List<TestCase> tests = new ArrayList<>();

        /*
         * ============================================================
         * Basic Cases
         * ============================================================
         */

        tests.add(new TestCase(
                "B1",
                null,
                null,
                new Result(true, true),
                "both trees empty"));

        tests.add(new TestCase(
                "B2",
                node(1),
                node(1),
                new Result(true, true),
                "single nodes with same value"));

        tests.add(new TestCase(
                "B3",
                node(1),
                node(2),
                new Result(false, true),
                "single nodes with different values"));

        tests.add(new TestCase(
                "B4",
                node(
                        1,
                        node(2),
                        node(3)),
                node(
                        1,
                        node(3),
                        node(2)),
                new Result(true, true),
                "simple mirror trees"));

        tests.add(new TestCase(
                "B5",
                node(
                        1,
                        node(2),
                        node(3)),
                node(
                        1,
                        node(2),
                        node(3)),
                new Result(false, true),
                "same shape but not mirror orientation"));

        /*
         * ============================================================
         * Deeper Mirror
         * ============================================================
         */

        Node deepTree1 = node(
                1,
                node(
                        2,
                        node(4),
                        node(5)),
                node(
                        3,
                        node(6),
                        node(7)));

        Node deepTree2 = mirrorOf(deepTree1);

        tests.add(new TestCase(
                "D1",
                deepTree1,
                deepTree2,
                new Result(true, true),
                "deep symmetric mirror"));

        /*
         * ============================================================
         * Value Mismatch
         * ============================================================
         */

        Node valueMismatch1 = node(
                1,
                node(2),
                node(3));

        Node valueMismatch2 = node(
                1,
                node(99),
                node(2));

        tests.add(new TestCase(
                "V1",
                valueMismatch1,
                valueMismatch2,
                new Result(false, true),
                "value mismatch in corresponding node"));

        /*
         * ============================================================
         * Structural Mismatches
         * ============================================================
         */

        tests.add(new TestCase(
                "S1",
                node(1),
                null,
                new Result(false, false),
                "one root null and the other non-null"));

        tests.add(new TestCase(
                "S2",
                null,
                node(1),
                new Result(false, false),
                "one root null and the other non-null"));

        tests.add(new TestCase(
                "S3",
                node(
                        1,
                        node(2),
                        null),
                node(
                        1,
                        node(3),
                        node(4)),
                new Result(false, true),
                "values differ before structural mismatch"));

        tests.add(new TestCase(
                "S4",
                node(
                        1,
                        node(2),
                        null),
                node(
                        1,
                        null,
                        node(2)),
                new Result(true, true),
                "valid mirrored asymmetric shape"));

        tests.add(new TestCase(
                "S5",
                node(
                        1,
                        node(2),
                        null),
                node(
                        1,
                        node(2),
                        null),
                new Result(false, true),
                "same asymmetric shape but not mirrored"));

        /*
         * ============================================================
         * Larger Tree
         * ============================================================
         */

        Node largeTree = node(
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

        tests.add(new TestCase(
                "L1",
                largeTree,
                mirrorOf(largeTree),
                new Result(true, true),
                "larger mirrored tree"));

        /*
         * ============================================================
         * Mutated Mirror
         * ============================================================
         */

        Node mutatedMirror = mirrorOf(largeTree);

        mutatedMirror.left.val = 999;

        tests.add(new TestCase(
                "L2",
                largeTree,
                mutatedMirror,
                new Result(false, true),
                "large mirror with one value changed"));

        /*
         * ============================================================
         * Run All Implementations
         * ============================================================
         */

        System.out.println(
                "############################################################");

        System.out.println(
                "####################  MIRROR TREES  ########################");

        System.out.println(
                "############################################################");

        System.out.println();

        List<MethodCase> methods = List.of(

                new MethodCase(
                        "Recursive",
                        MirrorTrees::mirrorTreesRecursion),

                new MethodCase(
                        "Queue / Breadth First",
                        MirrorTrees::mirrorTreesQueue),

                new MethodCase(
                        "Two Stacks / Depth First",
                        MirrorTrees::mirrorTreesTwoStacks)
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
