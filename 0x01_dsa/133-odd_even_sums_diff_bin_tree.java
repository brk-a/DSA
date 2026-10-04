import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Find the difference between the sum of values on odd levels and
 * the sum of values on even levels in a binary tree.
 *
 * The root is considered to be at level 1.
 *
 * Therefore:
 *
 *      difference = odd level sum - even level sum
 *
 * Two implementations are provided:
 *
 * 1. Recursion
 *
 *      Uses a depth-first traversal and tracks the current level.
 *
 *      Time:  O(n)
 *      Space: O(h)
 *
 * 2. Level-order Traversal
 *
 *      Uses a queue to process the tree one level at a time.
 *
 *      Time:  O(n)
 *      Space: O(w)
 *
 * where:
 *
 *      n = number of nodes
 *      h = height of the tree
 *      w = maximum width of the tree
 */
public class OddEvenSumsDiffBinaryTree {

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
            int diff,
            boolean valid) {
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
     * Find the difference between odd-level and even-level sums
     * using recursive depth-first traversal.
     *
     * The root is level 1.
     *
     * Therefore:
     *
     *      oddSum - evenSum
     *
     * is returned.
     *
     * Time:  O(n)
     * Space: O(h)
     */
    static Result oddEvenSumsDiffBinaryTreeRecursion(
            Node root) {

        if (!validNode(root)) {

            return new Result(
                    -1,
                    false);
        }

        int[] oddSum =
                new int[] {0};

        int[] evenSum =
                new int[] {0};

        getDiffRecursion(
                root,
                1,
                oddSum,
                evenSum);

        return new Result(
                oddSum[0] - evenSum[0],
                true);
    }

    /**
     * Recursive depth-first traversal.
     *
     * The current level is passed to each recursive call.
     */
    static void getDiffRecursion(
            Node root,
            int level,
            int[] oddSum,
            int[] evenSum) {

        if (root == null) {
            return;
        }

        if (level % 2 != 0) {

            oddSum[0] +=
                    root.val;

        } else {

            evenSum[0] +=
                    root.val;
        }

        getDiffRecursion(
                root.left,
                level + 1,
                oddSum,
                evenSum);

        getDiffRecursion(
                root.right,
                level + 1,
                oddSum,
                evenSum);
    }

    /* **********************************************************************
     * 2. Level-order Traversal
     * **********************************************************************/

    /**
     * Find the difference between odd-level and even-level sums
     * using level-order traversal.
     *
     * The queue contains one level at a time.
     *
     * Time:  O(n)
     * Space: O(w)
     */
    static Result oddEvenSumsDiffBinaryTreeLevelOrderTraversal(
            Node root) {

        if (!validNode(root)) {

            return new Result(
                    -1,
                    false);
        }

        Queue<Node> queue =
                new ArrayDeque<>();

        queue.add(root);

        int level = 0;

        int oddSum = 0;
        int evenSum = 0;

        while (!queue.isEmpty()) {

            int size =
                    queue.size();

            level++;

            while (size > 0) {

                Node current =
                        queue.remove();

                if (level % 2 != 0) {

                    oddSum +=
                            current.val;

                } else {

                    evenSum +=
                            current.val;
                }

                if (current.left != null) {

                    queue.add(
                            current.left);
                }

                if (current.right != null) {

                    queue.add(
                            current.right);
                }

                size--;
            }
        }

        return new Result(
                oddSum - evenSum,
                true);
    }

    /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    static class TestCase {

        final String id;
        final Node root;
        final int expected;
        final boolean expectedValid;
        final String description;

        TestCase(
                String id,
                Node root,
                int expected,
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
                                && actual.diff()
                                == test.expected;

                if (success) {

                    passed++;

                    System.out.printf(
                            "PASS %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  diff = "
                                    + actual.diff());

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
                                    + actual.diff()
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
                                && first.diff()
                                == test.expected;

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
                            || actual.diff()
                            != first.diff()) {

                        success = false;

                        System.out.printf(
                                "  %s = (%d, %s)%n",
                                methods.get(i).name,
                                actual.diff(),
                                actual.valid());
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
                    && result.diff() == -1) {

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
     * Reference Implementation
     * **********************************************************************/

    /**
     * Simple reference implementation used by the randomised tests.
     *
     * It explicitly calculates the level of every node and accumulates
     * the two sums.
     */
    static int referenceDifference(
            Node root) {

        if (root == null) {
            return -1;
        }

        int[] oddSum =
                new int[] {0};

        int[] evenSum =
                new int[] {0};

        referenceDifference(
                root,
                1,
                oddSum,
                evenSum);

        return oddSum[0]
                - evenSum[0];
    }

    static void referenceDifference(
            Node root,
            int level,
            int[] oddSum,
            int[] evenSum) {

        if (root == null) {
            return;
        }

        if (level % 2 != 0) {

            oddSum[0] +=
                    root.val;

        } else {

            evenSum[0] +=
                    root.val;
        }

        referenceDifference(
                root.left,
                level + 1,
                oddSum,
                evenSum);

        referenceDifference(
                root.right,
                level + 1,
                oddSum,
                evenSum);
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
         *              1
         *            /   \
         *           2     3
         *          / \   / \
         *         4   5 6   7
         *
         * Odd levels:
         *
         *      level 1: 1
         *      level 3: 4 + 5 + 6 + 7
         *
         *      odd = 23
         *
         * Even levels:
         *
         *      level 2: 2 + 3 = 5
         *
         * Difference:
         *
         *      23 - 5 = 18
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
                        18,
                        true,
                        "balanced three-level tree"));

        /*
         * ============================================================
         * Uneven Tree
         * ============================================================
         *
         *              10
         *            /    \
         *           5      20
         *          /
         *         2
         *          \
         *           1
         *
         * Odd levels:
         *
         *      10 + 2 = 12
         *
         * Even levels:
         *
         *      5 + 20 + 1 = 26
         *
         * Difference:
         *
         *      12 - 26 = -14
         */

        Node u1 =
                node(1);

        Node u2 =
                node(
                        2,
                        null,
                        u1);

        Node u5 =
                node(
                        5,
                        u2,
                        null);

        Node u20 =
                node(20);

        Node uneven =
                node(
                        10,
                        u5,
                        u20);

        tests.add(
                new TestCase(
                        "B2",
                        uneven,
                        -14,
                        true,
                        "uneven tree"));

        /*
         * ============================================================
         * Negative Values
         * ============================================================
         *
         *             -1
         *            /  \
         *          -2   -3
         *          / \
         *        -4  -5
         *
         * Odd levels:
         *
         *      -1 + -4 + -5 = -10
         *
         * Even levels:
         *
         *      -2 + -3 = -5
         *
         * Difference:
         *
         *      -10 - (-5) = -5
         */

        Node n4negative =
                node(-4);

        Node n5negative =
                node(-5);

        Node n2negative =
                node(
                        -2,
                        n4negative,
                        n5negative);

        Node n3negative =
                node(-3);

        Node negative =
                node(
                        -1,
                        n2negative,
                        n3negative);

        tests.add(
                new TestCase(
                        "N1",
                        negative,
                        -5,
                        true,
                        "negative values"));

        /*
         * ============================================================
         * Mixed Values
         * ============================================================
         *
         *             10
         *            /  \
         *          -5    20
         *          /       \
         *         4         -10
         *
         * Odd levels:
         *
         *      10 + 4 + -10 = 4
         *
         * Even levels:
         *
         *      -5 + 20 = 15
         *
         * Difference:
         *
         *      4 - 15 = -11
         */

        Node mixed4 =
                node(4);

        Node mixedMinus10 =
                node(-10);

        Node mixedMinus5 =
                node(
                        -5,
                        mixed4,
                        null);

        Node mixed20 =
                node(
                        20,
                        null,
                        mixedMinus10);

        Node mixed =
                node(
                        10,
                        mixedMinus5,
                        mixed20);

        tests.add(
                new TestCase(
                        "N2",
                        mixed,
                        -11,
                        true,
                        "mixed positive and negative values"));

        /*
         * ============================================================
         * Single Node
         * ============================================================
         *
         *      100
         *
         * Odd sum = 100
         * Even sum = 0
         * Difference = 100
         */

        Node single =
                node(100);

        tests.add(
                new TestCase(
                        "S1",
                        single,
                        100,
                        true,
                        "single-node tree"));

        /*
         * ============================================================
         * Single Negative Node
         * ============================================================
         */

        Node singleNegative =
                node(-100);

        tests.add(
                new TestCase(
                        "S2",
                        singleNegative,
                        -100,
                        true,
                        "single negative node"));

        /*
         * ============================================================
         * Left-Skewed Tree
         * ============================================================
         *
         *      1
         *     /
         *    2
         *   /
         *  3
         * /
         * 4
         *
         * Odd levels:
         *
         *      1 + 3 = 4
         *
         * Even levels:
         *
         *      2 + 4 = 6
         *
         * Difference = -2
         */

        Node l4 =
                node(4);

        Node l3 =
                node(
                        3,
                        l4,
                        null);

        Node l2 =
                node(
                        2,
                        l3,
                        null);

        Node l1 =
                node(
                        1,
                        l2,
                        null);

        tests.add(
                new TestCase(
                        "A1",
                        l1,
                        -2,
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
         *
         * Difference = -2
         */

        Node r4 =
                node(4);

        Node r3 =
                node(
                        3,
                        null,
                        r4);

        Node r2 =
                node(
                        2,
                        null,
                        r3);

        Node r1 =
                node(
                        1,
                        null,
                        r2);

        tests.add(
                new TestCase(
                        "A2",
                        r1,
                        -2,
                        true,
                        "right-skewed tree"));

        /*
         * ============================================================
         * Zero Values
         * ============================================================
         *
         *             0
         *            / \
         *           0   0
         *
         * Difference = 0
         */

        Node zeroLeft =
                node(0);

        Node zeroRight =
                node(0);

        Node zero =
                node(
                        0,
                        zeroLeft,
                        zeroRight);

        tests.add(
                new TestCase(
                        "Z1",
                        zero,
                        0,
                        true,
                        "zero values"));

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

        for (int i = 0;
             i < size;
             i++) {

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
                    referenceDifference(
                            root);

            for (MethodCase method :
                    methods) {

                Result result =
                        method.algorithm.solve(
                                root);

                if (!result.valid()
                        || result.diff()
                        != expected) {

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
                                    + result.diff());

                    System.out.println(
                            "valid = "
                                    + result.valid());

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
                "############  ODD EVEN SUMS DIFFERENCE  ####################");

        System.out.println(
                "############################################################");

        System.out.println();

        List<MethodCase> methods =
                List.of(

                        new MethodCase(
                                "Recursion",
                                OddEvenSumsDiffBinaryTree
                                        ::oddEvenSumsDiffBinaryTreeRecursion),

                        new MethodCase(
                                "Level-order Traversal",
                                OddEvenSumsDiffBinaryTree
                                        ::oddEvenSumsDiffBinaryTreeLevelOrderTraversal)
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
