import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Level-order traversal of a binary tree.
 *
 * Example:
 *
 *             1
 *           /   \
 *          2     3
 *         / \     \
 *        4   5     6
 *
 * Result:
 * [[1], [2, 3], [4, 5, 6]]
 *
 * Implementations:
 *
 * 1. Recursion
 *      Time:  O(n)
 *      Space: O(h) call stack + output
 *
 * 2. Queue / Breadth First
 *      Time:  O(n)
 *      Space: O(w) queue + output
 *
 * where:
 *      n = number of nodes
 *      h = height of tree
 *      w = maximum width of tree
 *
 * Result:
 * - valid == true  -> root/tree is valid and traversal completed
 * - valid == false -> root is null
 */
public class LevelOrderTraversalBinTree {

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

    static record Result(
            ArrayList<ArrayList<Integer>> result,
            boolean valid) {
    }

    /* **********************************************************************
     * Helpers
     * **********************************************************************/

    static boolean validRoot(Node root) {
        return root != null;
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

    static Result levelOrderTraversalRecursion(Node root) {

        if (!validRoot(root)) {
            return new Result(null, false);
        }

        ArrayList<ArrayList<Integer>> result = new ArrayList<>();

        levelOrderRecursion(
                root,
                0,
                result);

        return new Result(result, true);
    }

    static void levelOrderRecursion(
            Node root,
            int level,
            ArrayList<ArrayList<Integer>> result) {

        if (root == null) {
            return;
        }

        /*
         * If this is the first node encountered at this level,
         * create the list for that level.
         */
        if (result.size() <= level) {
            result.add(new ArrayList<>());
        }

        result.get(level).add(root.val);

        /*
         * Visit left before right so values appear in normal
         * left-to-right level-order.
         */
        levelOrderRecursion(
                root.left,
                level + 1,
                result);

        levelOrderRecursion(
                root.right,
                level + 1,
                result);
    }

    /* **********************************************************************
     * 2. Queue / Breadth-First Implementation
     * **********************************************************************/

    static Result levelOrderTraversalIteration(Node root) {

        if (!validRoot(root)) {
            return new Result(null, false);
        }

        Queue<Node> queue = new ArrayDeque<>();

        ArrayList<ArrayList<Integer>> result =
                new ArrayList<>();

        queue.offer(root);

        while (!queue.isEmpty()) {

            /*
             * The queue contains the nodes belonging
             * to the current level at this point.
             */
            int levelSize = queue.size();

            ArrayList<Integer> currentLevel =
                    new ArrayList<>(levelSize);

            for (int i = 0; i < levelSize; i++) {

                Node node = queue.poll();

                currentLevel.add(node.val);

                if (node.left != null) {
                    queue.offer(node.left);
                }

                if (node.right != null) {
                    queue.offer(node.right);
                }
            }

            result.add(currentLevel);
        }

        return new Result(result, true);
    }

    /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    static class TestCase {

        final String id;
        final Node root;
        final Result expected;
        final String description;

        TestCase(
                String id,
                Node root,
                Result expected,
                String description) {

            this.id = id;
            this.root = root;
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
     * Test Utilities
     * **********************************************************************/

    static boolean resultsEqual(Result a, Result b) {

        if (a == null && b == null) {
            return true;
        }

        if (a == null || b == null) {
            return false;
        }

        if (a.valid() != b.valid()) {
            return false;
        }

        if (a.result() == null && b.result() == null) {
            return true;
        }

        if (a.result() == null || b.result() == null) {
            return false;
        }

        return a.result().equals(b.result());
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

                Result actual =
                        method.solve(test.root);

                if (resultsEqual(actual, test.expected)) {

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
                        "FAIL %s (%s)%n",
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
     * Tree Utilities
     * **********************************************************************/

    /**
     * Creates a random binary tree.
     *
     * @param rng random number generator
     * @param depth maximum depth
     * @param nodeProbability probability that a node exists
     */
    static Node randomTree(
            Random rng,
            int depth,
            double nodeProbability) {

        if (depth == 0
                || rng.nextDouble() > nodeProbability) {

            return null;
        }

        int value = rng.nextInt(100);

        Node root = new Node(value);

        root.left = randomTree(
                rng,
                depth - 1,
                nodeProbability);

        root.right = randomTree(
                rng,
                depth - 1,
                nodeProbability);

        return root;
    }

    /**
     * Creates a deep copy of a binary tree.
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

    /**
     * Calculates the expected level-order traversal independently
     * using a straightforward queue implementation.
     *
     * This is useful for testing the two implementations against
     * an independent expected result.
     */
    static ArrayList<ArrayList<Integer>> expectedLevelOrder(Node root) {

        ArrayList<ArrayList<Integer>> result =
                new ArrayList<>();

        if (root == null) {
            return result;
        }

        Queue<Node> queue = new ArrayDeque<>();

        queue.offer(root);

        while (!queue.isEmpty()) {

            int levelSize = queue.size();

            ArrayList<Integer> level =
                    new ArrayList<>();

            for (int i = 0; i < levelSize; i++) {

                Node current = queue.poll();

                level.add(current.val);

                if (current.left != null) {
                    queue.offer(current.left);
                }

                if (current.right != null) {
                    queue.offer(current.right);
                }
            }

            result.add(level);
        }

        return result;
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

        Random rng =
                new Random(20260913L);

        for (int i = 1; i <= iterations; i++) {

            Node root = randomTree(
                    rng,
                    8,
                    0.75);

            Result recursive =
                    levelOrderTraversalRecursion(root);

            Result iterative =
                    levelOrderTraversalIteration(root);

            ArrayList<ArrayList<Integer>> expected =
                    expectedLevelOrder(root);

            Result expectedResult;

            if (root == null) {

                expectedResult =
                        new Result(null, false);

            } else {

                expectedResult =
                        new Result(expected, true);
            }

            if (!resultsEqual(
                    recursive,
                    iterative)
                    || !resultsEqual(
                    recursive,
                    expectedResult)) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "iteration = " + i);

                System.out.println(
                        "recursive = " + recursive);

                System.out.println(
                        "iterative = " + iterative);

                System.out.println(
                        "expected  = " + expectedResult);

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

        tests.add(new TestCase(
                "B1",
                null,
                new Result(null, false),
                "null root"));

        tests.add(new TestCase(
                "B2",
                node(1),
                new Result(
                        new ArrayList<>(
                                List.of(
                                        new ArrayList<>(
                                                List.of(1)))),
                        true),
                "single node"));

        tests.add(new TestCase(
                "B3",
                node(
                        1,
                        node(2),
                        node(3)),
                new Result(
                        new ArrayList<>(
                                List.of(
                                        new ArrayList<>(
                                                List.of(1)),
                                        new ArrayList<>(
                                                List.of(2, 3)))),
                        true),
                "root with two children"));

        /*
         * ============================================================
         * Multiple Levels
         * ============================================================
         */

        tests.add(new TestCase(
                "M1",
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
                new Result(
                        new ArrayList<>(
                                List.of(
                                        new ArrayList<>(
                                                List.of(1)),
                                        new ArrayList<>(
                                                List.of(2, 3)),
                                        new ArrayList<>(
                                                List.of(4, 5, 6, 7)))),
                        true),
                "complete three-level tree"));

        /*
         * ============================================================
         * Asymmetric Trees
         * ============================================================
         */

        tests.add(new TestCase(
                "A1",
                node(
                        1,
                        node(
                                2,
                                node(4),
                                null),
                        null),
                new Result(
                        new ArrayList<>(
                                List.of(
                                        new ArrayList<>(
                                                List.of(1)),
                                        new ArrayList<>(
                                                List.of(2)),
                                        new ArrayList<>(
                                                List.of(4)))),
                        true),
                "left-skewed tree"));

        tests.add(new TestCase(
                "A2",
                node(
                        1,
                        null,
                        node(
                                3,
                                null,
                                node(6))),
                new Result(
                        new ArrayList<>(
                                List.of(
                                        new ArrayList<>(
                                                List.of(1)),
                                        new ArrayList<>(
                                                List.of(3)),
                                        new ArrayList<>(
                                                List.of(6)))),
                        true),
                "right-skewed tree"));

        tests.add(new TestCase(
                "A3",
                node(
                        1,
                        node(2),
                        null),
                new Result(
                        new ArrayList<>(
                                List.of(
                                        new ArrayList<>(
                                                List.of(1)),
                                        new ArrayList<>(
                                                List.of(2)))),
                        true),
                "root with left child only"));

        tests.add(new TestCase(
                "A4",
                node(
                        1,
                        null,
                        node(3)),
                new Result(
                        new ArrayList<>(
                                List.of(
                                        new ArrayList<>(
                                                List.of(1)),
                                        new ArrayList<>(
                                                List.of(3)))),
                        true),
                "root with right child only"));

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
                new Result(
                        new ArrayList<>(
                                List.of(
                                        new ArrayList<>(
                                                List.of(10)),
                                        new ArrayList<>(
                                                List.of(20, 30)),
                                        new ArrayList<>(
                                                List.of(40, 50, 60, 70)),
                                        new ArrayList<>(
                                                List.of(80, 90)))),
                        true),
                "larger irregular tree"));

        /*
         * ============================================================
         * Duplicate Values
         * ============================================================
         */

        tests.add(new TestCase(
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
                new Result(
                        new ArrayList<>(
                                List.of(
                                        new ArrayList<>(
                                                List.of(5)),
                                        new ArrayList<>(
                                                List.of(5, 5)),
                                        new ArrayList<>(
                                                List.of(5, 5, 5)),
                                        new ArrayList<>(
                                                List.of(5)))),
                        true),
                "duplicate node values"));

        /*
         * ============================================================
         * Negative Values
         * ============================================================
         */

        tests.add(new TestCase(
                "N1",
                node(
                        -1,
                        node(-2),
                        node(
                                -3,
                                node(-4),
                                null)),
                new Result(
                        new ArrayList<>(
                                List.of(
                                        new ArrayList<>(
                                                List.of(-1)),
                                        new ArrayList<>(
                                                List.of(-2, -3)),
                                        new ArrayList<>(
                                                List.of(-4)))),
                        true),
                "negative node values"));

        /*
         * ============================================================
         * Run All Implementations
         * ============================================================
         */

        System.out.println(
                "############################################################");

        System.out.println(
                "############  LEVEL ORDER TRAVERSAL  #######################");

        System.out.println(
                "############################################################");

        System.out.println();

        List<MethodCase> methods =
                List.of(

                        new MethodCase(
                                "Recursive",
                                LevelOrderTraversalBinTree
                                        ::levelOrderTraversalRecursion),

                        new MethodCase(
                                "Queue / Breadth First",
                                LevelOrderTraversalBinTree
                                        ::levelOrderTraversalIteration)
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
