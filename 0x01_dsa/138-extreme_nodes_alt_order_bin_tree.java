import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Find the extreme node of every level of a binary tree, alternating
 * between the rightmost and leftmost node.
 *
 * Level 0: rightmost node
 * Level 1: leftmost node
 * Level 2: rightmost node
 * Level 3: leftmost node
 * ...
 *
 * For example:
 *
 *                 1
 *              /     \
 *             2       3
 *           /  \     /  \
 *          4    5   6    7
 *             /
 *            8
 *
 * Level-order:
 *
 *      Level 0: 1
 *      Level 1: 2, 3
 *      Level 2: 4, 5, 6, 7
 *      Level 3: 8
 *
 * Alternating extremes:
 *
 *      1, 2, 7, 8
 *
 * The implementation uses a queue backed by ArrayDeque.
 *
 * Time:  O(n)
 * Space: O(w)
 *
 * where:
 *
 *      n = number of nodes
 *      w = maximum width of the tree
 */
public class ExtremeNodesAltOrder {

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
            ArrayList<ArrayList<Integer>> result,
            boolean valid) {
    }

    /* **********************************************************************
     * Helpers
     * **********************************************************************/

    static boolean validRoot(
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

    /**
     * Build a tree node using the same constructor ordering as the
     * original code.
     */
    static Node node(
            Node left,
            Node right,
            int value) {

        return new Node(
                left,
                right,
                value);
    }

    /* **********************************************************************
     * Extreme Nodes - Level Order
     * **********************************************************************/

    /**
     * Find the extreme node at every level in alternating order.
     *
     * The first level takes the rightmost node.
     *
     * The next level takes the leftmost node.
     *
     * The direction then alternates for every subsequent level.
     *
     * A queue is used for ordinary breadth-first traversal. For each
     * level, the first node and last node encountered are recorded.
     *
     * On a right-extreme level, the last node is selected.
     *
     * On a left-extreme level, the first node is selected.
     *
     * The returned result contains one ArrayList for each level. Each
     * level contains the selected extreme node as a single integer.
     */
    static Result extremeNodesAltOrderLevelOrder(
            Node root) {

        if (!validRoot(root)) {

            return new Result(
                    null,
                    false);
        }

        ArrayList<ArrayList<Integer>> result =
                new ArrayList<>();

        Queue<Node> queue =
                new ArrayDeque<>();

        queue.offer(root);

        /*
         * true  = take rightmost node
         * false = take leftmost node
         */
        boolean takeRightmost = true;

        while (!queue.isEmpty()) {

            int levelSize =
                    queue.size();

            Node firstNode = null;
            Node lastNode = null;

            for (int i = 0;
                 i < levelSize;
                 i++) {

                Node current =
                        queue.poll();

                /*
                 * The first node encountered at this level.
                 */
                if (i == 0) {
                    firstNode = current;
                }

                /*
                 * The last node encountered at this level.
                 */
                if (i == levelSize - 1) {
                    lastNode = current;
                }

                /*
                 * Add children for the next level.
                 */
                if (current.left != null) {

                    queue.offer(
                            current.left);
                }

                if (current.right != null) {

                    queue.offer(
                            current.right);
                }
            }

            Node extreme =
                    takeRightmost
                            ? lastNode
                            : firstNode;

            ArrayList<Integer> levelResult =
                    new ArrayList<>();

            levelResult.add(
                    extreme.val);

            result.add(
                    levelResult);

            /*
             * Alternate the direction for the next level.
             */
            takeRightmost =
                    !takeRightmost;
        }

        return new Result(
                result,
                true);
    }

    /* **********************************************************************
     * Reference Implementation
     * **********************************************************************/

    /**
     * Independent reference implementation.
     *
     * This implementation records every complete level first and then
     * selects the appropriate extreme node.
     *
     * It is deliberately structured differently from the main algorithm
     * so that the randomised tests provide a useful cross-check.
     */
    static Result extremeNodesAltOrderReference(
            Node root) {

        if (!validRoot(root)) {

            return new Result(
                    null,
                    false);
        }

        ArrayList<ArrayList<Integer>> result =
                new ArrayList<>();

        List<Node> currentLevel =
                new ArrayList<>();

        currentLevel.add(root);

        boolean takeRightmost = true;

        while (!currentLevel.isEmpty()) {

            ArrayList<Integer> values =
                    new ArrayList<>();

            List<Node> nextLevel =
                    new ArrayList<>();

            for (Node current :
                    currentLevel) {

                values.add(
                        current.val);

                if (current.left != null) {

                    nextLevel.add(
                            current.left);
                }

                if (current.right != null) {

                    nextLevel.add(
                            current.right);
                }
            }

            int index =
                    takeRightmost
                            ? values.size() - 1
                            : 0;

            ArrayList<Integer> extreme =
                    new ArrayList<>();

            extreme.add(
                    values.get(index));

            result.add(
                    extreme);

            currentLevel =
                    nextLevel;

            takeRightmost =
                    !takeRightmost;
        }

        return new Result(
                result,
                true);
    }

    /* **********************************************************************
     * Level-Order Traversal
     * **********************************************************************/

    static ArrayList<ArrayList<Integer>> levelOrder(
            Node root) {

        ArrayList<ArrayList<Integer>> result =
                new ArrayList<>();

        if (root == null) {
            return result;
        }

        Queue<Node> queue =
                new ArrayDeque<>();

        queue.offer(root);

        while (!queue.isEmpty()) {

            int levelSize =
                    queue.size();

            ArrayList<Integer> level =
                    new ArrayList<>();

            for (int i = 0;
                 i < levelSize;
                 i++) {

                Node current =
                        queue.poll();

                level.add(
                        current.val);

                if (current.left != null) {

                    queue.offer(
                            current.left);
                }

                if (current.right != null) {

                    queue.offer(
                            current.right);
                }
            }

            result.add(level);
        }

        return result;
    }

    /* **********************************************************************
     * Result Helpers
     * **********************************************************************/

    static boolean resultsEqual(
            ArrayList<ArrayList<Integer>> first,
            ArrayList<ArrayList<Integer>> second) {

        if (first == null
                || second == null) {

            return first == second;
        }

        return first.equals(second);
    }

    static String formatResult(
            Result result) {

        if (result == null) {
            return "null";
        }

        return "(result = "
                + result.result()
                + ", valid = "
                + result.valid()
                + ")";
    }

    /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    static class TestCase {

        final String id;
        final Node root;
        final ArrayList<ArrayList<Integer>> expected;
        final boolean expectedValid;
        final String description;

        TestCase(
                String id,
                Node root,
                ArrayList<ArrayList<Integer>> expected,
                boolean expectedValid,
                String description) {

            this.id = id;
            this.root = root;
            this.expected = expected;
            this.expectedValid = expectedValid;
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

        for (TestCase test :
                tests) {

            try {

                Result actual =
                        method.algorithm.solve(
                                test.root);

                boolean success =
                        actual.valid()
                                == test.expectedValid
                                && resultsEqual(
                                        actual.result(),
                                        test.expected);

                if (success) {

                    passed++;

                    System.out.printf(
                            "PASS %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  result = "
                                    + actual.result());

                } else {

                    failed++;

                    System.out.printf(
                            "FAIL %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  expected = "
                                    + test.expected
                                    + ", valid = "
                                    + test.expectedValid);

                    System.out.println(
                            "  actual   = "
                                    + actual.result()
                                    + ", valid = "
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

        for (TestCase test :
                tests) {

            try {

                Result first =
                        methods.get(0)
                                .algorithm
                                .solve(
                                        test.root);

                boolean success =
                        first.valid()
                                == test.expectedValid
                                && resultsEqual(
                                        first.result(),
                                        test.expected);

                for (int i = 1;
                     i < methods.size();
                     i++) {

                    Result actual =
                            methods.get(i)
                                    .algorithm
                                    .solve(
                                            test.root);

                    if (actual.valid()
                            != first.valid()
                            || !resultsEqual(
                                    actual.result(),
                                    first.result())) {

                        success = false;

                        System.out.printf(
                                "  %s = %s%n",
                                methods.get(i).name,
                                formatResult(actual));
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

            if (!result.valid()
                    && result.result() == null) {

                passed++;

                System.out.printf(
                        "PASS I1 - %s rejects null root%n",
                        method.name);

            } else {

                failed++;

                System.out.printf(
                        "FAIL I1 - %s accepts null root%n",
                        method.name);

                System.out.println(
                        "  actual = "
                                + formatResult(result));
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

    static ArrayList<ArrayList<Integer>> expected(
            int... values) {

        ArrayList<ArrayList<Integer>> result =
                new ArrayList<>();

        for (int value :
                values) {

            ArrayList<Integer> level =
                    new ArrayList<>();

            level.add(value);

            result.add(level);
        }

        return result;
    }

    static List<TestCase> buildTests() {

        List<TestCase> tests =
                new ArrayList<>();

        /*
         * ============================================================
         * Basic Complete Tree
         * ============================================================
         *
         *                 1
         *              /     \
         *             2       3
         *           /  \     /  \
         *          4    5   6    7
         *
         * Level 0 -> rightmost = 1
         * Level 1 -> leftmost  = 2
         * Level 2 -> rightmost = 7
         *
         * Expected:
         *
         *      1, 2, 7
         */

        Node n4 =
                node(4);

        Node n5 =
                node(5);

        Node n6 =
                node(6);

        Node n7 =
                node(7);

        Node n2 =
                node(
                        2,
                        n4,
                        n5);

        Node n3 =
                node(
                        3,
                        n6,
                        n7);

        Node root =
                node(
                        1,
                        n2,
                        n3);

        tests.add(
                new TestCase(
                        "B1",
                        root,
                        expected(
                                1,
                                2,
                                7),
                        true,
                        "complete binary tree"));

        /*
         * ============================================================
         * More Complex Tree
         * ============================================================
         *
         *                 1
         *              /     \
         *             2       3
         *            /         \
         *           4           7
         *            \         /
         *             5       8
         *              \
         *               6
         *
         * Levels:
         *
         *      1
         *      2, 3
         *      4, 7
         *      5, 8
         *      6
         *
         * Extremes:
         *
         *      1, 2, 7, 5, 6
         */

        Node c6 =
                node(6);

        Node c5 =
                node(
                        null,
                        c6,
                        5);

        Node c4 =
                node(
                        null,
                        c5,
                        4);

        Node c8 =
                node(8);

        Node c7 =
                node(
                        c8,
                        null,
                        7);

        Node c2 =
                node(
                        c4,
                        null,
                        2);

        Node c3 =
                node(
                        null,
                        c7,
                        3);

        Node complex =
                node(
                        c2,
                        c3,
                        1);

        tests.add(
                new TestCase(
                        "B2",
                        complex,
                        expected(
                                1,
                                2,
                                7,
                                5,
                                6),
                        true,
                        "irregular tree"));

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
                        expected(100),
                        true,
                        "single-node tree"));

        /*
         * ============================================================
         * Left-Skewed Tree
         * ============================================================
         *
         *          5
         *         /
         *        4
         *       /
         *      3
         *     /
         *    2
         *   /
         *  1
         *
         * Every level has one node.
         */

        Node l1 =
                node(1);

        Node l2 =
                node(
                        l1,
                        null,
                        2);

        Node l3 =
                node(
                        l2,
                        null,
                        3);

        Node l4 =
                node(
                        l3,
                        null,
                        4);

        Node l5 =
                node(
                        l4,
                        null,
                        5);

        tests.add(
                new TestCase(
                        "A1",
                        l5,
                        expected(
                                5,
                                4,
                                3,
                                2,
                                1),
                        true,
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
         *             \
         *              5
         */

        Node r5 =
                node(5);

        Node r4 =
                node(
                        null,
                        r5,
                        4);

        Node r3 =
                node(
                        null,
                        r4,
                        3);

        Node r2 =
                node(
                        null,
                        r3,
                        2);

        Node r1 =
                node(
                        null,
                        r2,
                        1);

        tests.add(
                new TestCase(
                        "A2",
                        r1,
                        expected(
                                1,
                                2,
                                3,
                                4,
                                5),
                        true,
                        "right-skewed tree"));

        /*
         * ============================================================
         * Negative Values
         * ============================================================
         *
         *                 -10
         *                /   \
         *              -20   -5
         *             /  \   / \
         *           -30 -15 -7 0
         */

        Node n30 =
                node(-30);

        Node n15neg =
                node(-15);

        Node n7neg =
                node(-7);

        Node n0 =
                node(0);

        Node n20neg =
                node(
                        -20,
                        n30,
                        n15neg);

        Node n5neg =
                node(
                        -5,
                        n7neg,
                        n0);

        Node negative =
                node(
                        -10,
                        n20neg,
                        n5neg);

        tests.add(
                new TestCase(
                        "N1",
                        negative,
                        expected(
                                -10,
                                -20,
                                0),
                        true,
                        "negative and zero values"));

        /*
         * ============================================================
         * Duplicate Values
         * ============================================================
         *
         * Values do not need to be unique for this problem.
         *
         *                 5
         *              /     \
         *             5       5
         *            / \     / \
         *           5   5   5   5
         *
         * The expected result is based on node position, not value
         * identity.
         */

        Node d1 =
                node(5);

        Node d2 =
                node(5);

        Node d3 =
                node(5);

        Node d4 =
                node(5);

        Node d5 =
                node(
                        5,
                        d1,
                        d2);

        Node d6 =
                node(
                        5,
                        d3,
                        d4);

        Node duplicates =
                node(
                        5,
                        d5,
                        d6);

        tests.add(
                new TestCase(
                        "D1",
                        duplicates,
                        expected(
                                5,
                                5,
                                5),
                        true,
                        "duplicate values"));

        return tests;
    }

    /* **********************************************************************
     * Random Tree Generation
     * **********************************************************************/

    /**
     * Generate a random binary tree.
     *
     * The tree is not required to be a BST. This is intentional because
     * the extreme-node problem only depends on the tree structure.
     */
    static Node randomTree(
            Random random,
            int size) {

        if (size <= 0) {
            return null;
        }

        Node root =
                node(
                        random.nextInt(2001)
                                - 1000);

        ArrayList<Node> nodes =
                new ArrayList<>();

        nodes.add(root);

        int created = 1;

        while (created < size) {

            Node parent =
                    nodes.get(
                            random.nextInt(
                                    nodes.size()));

            boolean attachLeft =
                    random.nextBoolean();

            /*
             * If the preferred side is occupied, try the other side.
             */
            if (attachLeft
                    && parent.left != null) {

                attachLeft = false;
            }

            if (!attachLeft
                    && parent.right != null) {

                /*
                 * There is no available child on this parent.
                 */
                continue;
            }

            Node child =
                    node(
                            random.nextInt(2001)
                                    - 1000);

            if (attachLeft) {

                parent.left =
                        child;

            } else {

                parent.right =
                        child;
            }

            nodes.add(child);
            created++;
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
                new Random(20261007L);

        for (int iteration = 1;
             iteration <= iterations;
             iteration++) {

            int size =
                    1 + random.nextInt(100);

            Node root =
                    randomTree(
                            random,
                            size);

            Result expected =
                    extremeNodesAltOrderReference(
                            root);

            for (MethodCase method :
                    methods) {

                Result actual =
                        method.algorithm.solve(
                                root);

                if (actual.valid()
                        != expected.valid()
                        || !resultsEqual(
                                actual.result(),
                                expected.result())) {

                    System.out.println(
                            "Randomised test FAILED");

                    System.out.println(
                            "iteration = "
                                    + iteration);

                    System.out.println(
                            "algorithm = "
                                    + method.name);

                    System.out.println(
                            "tree size = "
                                    + size);

                    System.out.println(
                            "expected = "
                                    + formatResult(
                                            expected));

                    System.out.println(
                            "actual = "
                                    + formatResult(
                                            actual));

                    System.out.println(
                            "level-order = "
                                    + levelOrder(root));

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
                "##############  EXTREME NODES ALT ORDER  ###################");

        System.out.println(
                "############################################################");

        System.out.println();

        List<MethodCase> methods =
                List.of(

                        new MethodCase(
                                "Level-Order using ArrayDeque",
                                ExtremeNodesAltOrder
                                        ::extremeNodesAltOrderLevelOrder),

                        new MethodCase(
                                "Reference Implementation",
                                ExtremeNodesAltOrder
                                        ::extremeNodesAltOrderReference)
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

        System.out.println(
                "Test suite complete.");
    }
}
