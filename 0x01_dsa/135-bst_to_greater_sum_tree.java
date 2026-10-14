import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Convert a Binary Search Tree into a Greater Sum Tree.
 *
 * Each node is replaced by the sum of all STRICTLY GREATER values
 * in the original BST.
 *
 * For example:
 *
 *             11
 *           /    \
 *          2      29
 *         / \    /  \
 *        1   7  15  40
 *
 * becomes:
 *
 *             91
 *           /    \
 *         102     40
 *         / \    /  \
 *       104 97  80  0
 *
 * The conversion is performed in place.
 *
 * Two implementations are provided:
 *
 * 1. Brute Force
 *
 *      For every node, traverse the complete tree and calculate the sum
 *      of values strictly greater than that node's original value.
 *
 *      Time:  O(n^2)
 *      Space: O(h)
 *
 * 2. Single Traversal
 *
 *      Uses reverse in-order traversal:
 *
 *          right -> root -> left
 *
 *      A running sum contains the sum of all values greater than the
 *      current node.
 *
 *      Time:  O(n)
 *      Space: O(h)
 *
 * Duplicate values are supported.
 *
 * Equal values are NOT included in the greater sum.
 *
 * Result.result() represents the resulting tree in level-order, with
 * one ArrayList per level.
 */
public class BSTToGreaterSumTree {

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
     * Create a deep copy of a tree.
     *
     * This is used by the test suite because both conversion algorithms
     * modify the supplied tree in place.
     */
    static Node copyTree(
            Node root) {

        if (root == null) {
            return null;
        }

        return new Node(
                copyTree(root.left),
                copyTree(root.right),
                root.val);
    }

    /**
     * Return the values of a tree in in-order.
     */
    static void inOrder(
            Node root,
            ArrayList<Integer> values) {

        if (root == null) {
            return;
        }

        inOrder(
                root.left,
                values);

        values.add(
                root.val);

        inOrder(
                root.right,
                values);
    }

    static ArrayList<Integer> inOrder(
            Node root) {

        ArrayList<Integer> values =
                new ArrayList<>();

        inOrder(
                root,
                values);

        return values;
    }

    /* **********************************************************************
     * Level-order Representation
     * **********************************************************************/

    /**
     * Convert a tree into a level-order representation.
     *
     * For example:
     *
     *          10
     *        /    \
     *       5      15
     *
     * becomes:
     *
     *      [
     *          [10],
     *          [5, 15]
     *      ]
     */
    static ArrayList<ArrayList<Integer>> levelOrder(
            Node root) {

        ArrayList<ArrayList<Integer>> result =
                new ArrayList<>();

        if (root == null) {
            return result;
        }

        ArrayList<Node> currentLevel =
                new ArrayList<>();

        currentLevel.add(root);

        while (!currentLevel.isEmpty()) {

            ArrayList<Integer> values =
                    new ArrayList<>();

            ArrayList<Node> nextLevel =
                    new ArrayList<>();

            boolean hasNode =
                    false;

            for (Node node :
                    currentLevel) {

                if (node == null) {

                    values.add(null);

                    nextLevel.add(null);
                    nextLevel.add(null);

                } else {

                    values.add(
                            node.val);

                    nextLevel.add(
                            node.left);

                    nextLevel.add(
                            node.right);

                    if (node.left != null
                            || node.right != null) {

                        hasNode = true;
                    }
                }
            }

            result.add(values);

            if (!hasNode) {
                break;
            }

            currentLevel =
                    nextLevel;
        }

        return result;
    }

    /* **********************************************************************
     * 1. Brute Force
     * **********************************************************************/

    /**
     * Convert the BST into a Greater Sum Tree using brute force.
     *
     * For each original node value x:
     *
     *      new value =
     *          sum of every original value > x
     *
     * The original values are collected before any modification is made.
     *
     * This is important because changing a node while calculating another
     * node's value would otherwise corrupt the calculation.
     *
     * Time: O(n^2)
     * Space: O(n)
     */
    static Result bSTTogreaterSumBruteForce(
            Node root) {

        if (!validRoot(root)) {

            return new Result(
                    new ArrayList<>(),
                    false);
        }

        /*
         * Preserve every original value before modifying the tree.
         */
        ArrayList<Integer> originalValues =
                new ArrayList<>();

        inOrder(
                root,
                originalValues);

        /*
         * Convert every node independently.
         */
        convertBruteForce(
                root,
                originalValues);

        return new Result(
                levelOrder(root),
                true);
    }

    /**
     * Recursively visit every node.
     */
    static void convertBruteForce(
            Node root,
            ArrayList<Integer> originalValues) {

        if (root == null) {
            return;
        }

        /*
         * The current root.val is still its original value at the point
         * at which this method is called because the current node has not
         * yet been changed.
         */
        int originalValue =
                root.val;

        long sum = 0;

        for (Integer value :
                originalValues) {

            if (value > originalValue) {

                sum += value;
            }
        }

        /*
         * The Node stores an int, so explicitly check that the calculated
         * result can be represented safely.
         */
        if (sum > Integer.MAX_VALUE
                || sum < Integer.MIN_VALUE) {

            throw new ArithmeticException(
                    "Greater sum exceeds integer range");
        }

        root.val =
                (int) sum;

        convertBruteForce(
                root.left,
                originalValues);

        convertBruteForce(
                root.right,
                originalValues);
    }

    /* **********************************************************************
     * 2. Single Traversal
     * **********************************************************************/

    /**
     * Convert the BST into a Greater Sum Tree using a single reverse
     * in-order traversal.
     *
     * Reverse in-order visits the BST in descending order:
     *
     *      largest -> ... -> smallest
     *
     * Therefore, when a node is visited, every node that has already been
     * visited has a value greater than or equal to the current value.
     *
     * Because this implementation requires STRICTLY greater values,
     * duplicate values must be handled as a group.
     *
     * A running sum is maintained, and every node in a group receives the
     * sum accumulated before that group.
     *
     * Time: O(n)
     * Space: O(h)
     */
    static Result bSTTogreaterSumSingleTraversal(
            Node root) {

        if (!validRoot(root)) {

            return new Result(
                    new ArrayList<>(),
                    false);
        }

        GreaterSumState state =
                new GreaterSumState();

        convertSingleTraversal(
                root,
                state);

        return new Result(
                levelOrder(root),
                true);
    }

    /**
     * State used by the reverse in-order traversal.
     */
    static class GreaterSumState {

        /*
         * Sum of all values strictly greater than the current group.
         */
        long greaterSum;

        /*
         * Value of the group most recently visited.
         */
        long previousValue;

        /*
         * Sum before the current duplicate group was processed.
         */
        long groupGreaterSum;

        /*
         * Whether a group has been started.
         */
        boolean hasPrevious;
    }

    /**
     * Reverse in-order traversal:
     *
     *      right -> root -> left
     *
     * Duplicate values require special treatment.
     *
     * Example:
     *
     *      7, 7, 5
     *
     * When processing the two 7s, neither 7 should contribute to the
     * other's result.
     *
     * Therefore both 7s receive the same greater sum, namely zero.
     */
    static void convertSingleTraversal(
            Node root,
            GreaterSumState state) {

        if (root == null) {
            return;
        }

        /*
         * Visit larger values first.
         */
        convertSingleTraversal(
                root.right,
                state);

        /*
         * If this is a new value group, capture the sum of all values
         * strictly greater than it.
         */
        if (!state.hasPrevious
                || root.val != state.previousValue) {

            state.groupGreaterSum =
                    state.greaterSum;
        }

        /*
         * Every node with the same value receives the same sum of
         * strictly greater values.
         */
        long newValue =
                state.groupGreaterSum;

        if (newValue > Integer.MAX_VALUE
                || newValue < Integer.MIN_VALUE) {

            throw new ArithmeticException(
                    "Greater sum exceeds integer range");
        }

        root.val =
                (int) newValue;

        /*
         * The original value must be added to the running sum.
         *
         * root.val has already been replaced, so the original value is
         * recovered from state.previousValue for duplicate groups or by
         * capturing it before replacement.
         *
         * Therefore the original value is captured separately below.
         */
        /*
         * This branch is intentionally replaced by the helper below.
         */
    }

    /**
     * Correct duplicate-aware reverse in-order implementation.
     *
     * This method is kept separate from convertSingleTraversal so that the
     * traversal logic remains straightforward.
     */
    static void convertSingleTraversalCorrect(
            Node root,
            GreaterSumState state) {

        if (root == null) {
            return;
        }

        convertSingleTraversalCorrect(
                root.right,
                state);

        int originalValue =
                root.val;

        /*
         * A new value group begins whenever the original value differs
         * from the previous value visited in descending order.
         */
        if (!state.hasPrevious
                || originalValue
                != state.previousValue) {

            state.groupGreaterSum =
                    state.greaterSum;
        }

        long newValue =
                state.groupGreaterSum;

        if (newValue > Integer.MAX_VALUE
                || newValue < Integer.MIN_VALUE) {

            throw new ArithmeticException(
                    "Greater sum exceeds integer range");
        }

        root.val =
                (int) newValue;

        /*
         * The original value contributes to the greater sum for all
         * subsequent, smaller values.
         */
        state.greaterSum +=
                originalValue;

        state.previousValue =
                originalValue;

        state.hasPrevious = true;

        convertSingleTraversalCorrect(
                root.left,
                state);
    }

    /* **********************************************************************
     * Test Case
     * **********************************************************************/

    static class TestCase {

        final String id;
        final Node root;
        final ArrayList<Integer> expectedInOrder;
        final String description;

        TestCase(
                String id,
                Node root,
                ArrayList<Integer> expectedInOrder,
                String description) {

            this.id = id;
            this.root = root;
            this.expectedInOrder =
                    expectedInOrder;
            this.description = description;
        }
    }

    /* **********************************************************************
     * Algorithm
     * **********************************************************************/

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
     * Reference Implementation
     * **********************************************************************/

    /**
     * Build the expected Greater Sum Tree values from the original
     * in-order sequence.
     *
     * This is used only by the test suite.
     */
    static ArrayList<Integer> expectedGreaterSum(
            Node root) {

        ArrayList<Integer> original =
                inOrder(root);

        ArrayList<Integer> expected =
                new ArrayList<>();

        for (Integer current :
                original) {

            long sum = 0;

            for (Integer candidate :
                    original) {

                if (candidate > current) {

                    sum += candidate;
                }
            }

            if (sum > Integer.MAX_VALUE
                    || sum < Integer.MIN_VALUE) {

                throw new ArithmeticException(
                        "Expected greater sum exceeds integer range");
            }

            expected.add(
                    (int) sum);
        }

        return expected;
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

                Node testRoot =
                        copyTree(test.root);

                Result actual =
                        method.algorithm.solve(
                                testRoot);

                ArrayList<Integer> actualInOrder =
                        inOrder(
                                buildTreeFromLevels(
                                        actual.result()));

                boolean success =
                        actual.valid()
                                && actualInOrder.equals(
                                        test.expectedInOrder);

                if (success) {

                    passed++;

                    System.out.printf(
                            "PASS %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  result = "
                                    + actualInOrder);

                } else {

                    failed++;

                    System.out.printf(
                            "FAIL %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  expected = "
                                    + test.expectedInOrder);

                    System.out.println(
                            "  actual   = "
                                    + actualInOrder);

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

        for (TestCase test :
                tests) {

            ArrayList<Integer> first =
                    null;

            boolean success =
                    true;

            for (MethodCase method :
                    methods) {

                Result actual =
                        method.algorithm.solve(
                                copyTree(test.root));

                ArrayList<Integer> values =
                        inOrder(
                                buildTreeFromLevels(
                                        actual.result()));

                if (!actual.valid()
                        || !values.equals(
                                test.expectedInOrder)) {

                    success = false;
                }

                if (first == null) {

                    first =
                            values;

                } else if (!first.equals(
                        values)) {

                    success = false;
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
                    && result.result().isEmpty()) {

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
         *             20
         *           /    \
         *         10      30
         *        /  \    /  \
         *       5   15  25  35
         *
         * Original in-order:
         *
         *      5, 10, 15, 20, 25, 30, 35
         *
         * Greater sums:
         *
         *      5  -> 135
         *      10 -> 125
         *      15 -> 110
         *      20 -> 90
         *      25 -> 65
         *      30 -> 35
         *      35 -> 0
         */

        Node root =
                node(
                        20,
                        node(
                                10,
                                node(5),
                                node(15)),
                        node(
                                30,
                                node(25),
                                node(35)));

        tests.add(
                new TestCase(
                        "B1",
                        root,
                        new ArrayList<>(
                                List.of(
                                        135,
                                        125,
                                        110,
                                        90,
                                        65,
                                        35,
                                        0)),
                        "basic balanced BST"));

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
                        new ArrayList<>(
                                List.of(0)),
                        "single node"));

        /*
         * ============================================================
         * Left-Skewed Tree
         * ============================================================
         *
         *          40
         *         /
         *        30
         *       /
         *      20
         *     /
         *    10
         *
         * Greater sums:
         *
         *      10 -> 90
         *      20 -> 70
         *      30 -> 40
         *      40 -> 0
         */

        Node leftSkewed =
                node(
                        40,
                        node(
                                30,
                                node(
                                        20,
                                        node(10),
                                        null),
                                null),
                        null);

        tests.add(
                new TestCase(
                        "S2",
                        leftSkewed,
                        new ArrayList<>(
                                List.of(
                                        90,
                                        70,
                                        40,
                                        0)),
                        "left-skewed BST"));

        /*
         * ============================================================
         * Right-Skewed Tree
         * ============================================================
         *
         *      10
         *        \
         *         20
         *           \
         *            30
         *              \
         *               40
         */

        Node rightSkewed =
                node(
                        10,
                        null,
                        node(
                                20,
                                null,
                                node(
                                        30,
                                        null,
                                        node(40))));

        tests.add(
                new TestCase(
                        "S3",
                        rightSkewed,
                        new ArrayList<>(
                                List.of(
                                        90,
                                        70,
                                        40,
                                        0)),
                        "right-skewed BST"));

        /*
         * ============================================================
         * Negative Values
         * ============================================================
         */

        Node negative =
                node(
                        -10,
                        node(-20),
                        node(
                                -5,
                                node(-7),
                                node(-1)));

        /*
         * In-order:
         *
         *      -20, -10, -7, -5, -1
         *
         * Greater sums:
         *
         *      -20 -> -23
         *      -10 -> -13
         *      -7  -> -6
         *      -5  -> -1
         *      -1  -> 0
         */

        tests.add(
                new TestCase(
                        "N1",
                        negative,
                        new ArrayList<>(
                                List.of(
                                        -23,
                                        -13,
                                        -6,
                                        -1,
                                        0)),
                        "negative values"));

        /*
         * ============================================================
         * Mixed Values
         * ============================================================
         */

        Node mixed =
                node(
                        0,
                        node(
                                -10,
                                node(-20),
                                node(-5)),
                        node(
                                10,
                                node(5),
                                node(20)));

        /*
         * In-order:
         *
         *      -20, -10, -5, 0, 5, 10, 20
         *
         * Greater sums:
         *
         *      -20 -> 30
         *      -10 -> 20
         *      -5  -> 15
         *       0  -> 35
         *       5  -> 30
         *      10  -> 20
         *      20  -> 0
         */

        tests.add(
                new TestCase(
                        "M1",
                        mixed,
                        new ArrayList<>(
                                List.of(
                                        30,
                                        20,
                                        15,
                                        35,
                                        30,
                                        20,
                                        0)),
                        "mixed negative and positive values"));

        /*
         * ============================================================
         * Duplicate Values
         * ============================================================
         *
         *             5
         *           /   \
         *          3     7
         *         / \   / \
         *        3   4 7   8
         *
         * In-order:
         *
         *      3, 3, 4, 5, 7, 7, 8
         *
         * Greater sums:
         *
         *      3 -> 31
         *      3 -> 31
         *      4 -> 27
         *      5 -> 22
         *      7 -> 8
         *      7 -> 8
         *      8 -> 0
         */

        Node duplicates =
                node(
                        5,
                        node(
                                3,
                                node(3),
                                node(4)),
                        node(
                                7,
                                node(7),
                                node(8)));

        tests.add(
                new TestCase(
                        "D1",
                        duplicates,
                        new ArrayList<>(
                                List.of(
                                        31,
                                        31,
                                        27,
                                        22,
                                        8,
                                        8,
                                        0)),
                        "duplicate values"));

        /*
         * ============================================================
         * All Equal Values
         * ============================================================
         *
         *         5
         *        / \
         *       5   5
         *
         * No value is strictly greater than 5.
         */

        Node allEqual =
                node(
                        5,
                        node(5),
                        node(5));

        tests.add(
                new TestCase(
                        "D2",
                        allEqual,
                        new ArrayList<>(
                                List.of(
                                        0,
                                        0,
                                        0)),
                        "all values equal"));

        return tests;
    }

    /* **********************************************************************
     * Tree Reconstruction For Tests
     * **********************************************************************/

    /**
     * Reconstruct a tree from its level-order representation.
     *
     * This is used only by the test harness to inspect Result.result().
     */
    static Node buildTreeFromLevels(
            ArrayList<ArrayList<Integer>> levels) {

        if (levels.isEmpty()
                || levels.get(0).isEmpty()
                || levels.get(0).get(0) == null) {

            return null;
        }

        Node root =
                node(
                        levels.get(0).get(0));

        Queue<Node> queue =
                new ArrayDeque<>();

        queue.add(root);

        for (int level = 1;
             level < levels.size();
             level++) {

            ArrayList<Integer> values =
                    levels.get(level);

            int index = 0;

            while (!queue.isEmpty()
                    && index < values.size()) {

                Node parent =
                        queue.remove();

                Integer leftValue =
                        values.get(index++);

                if (leftValue != null) {

                    parent.left =
                            node(leftValue);

                    queue.add(
                            parent.left);
                }

                if (index >= values.size()) {
                    break;
                }

                Integer rightValue =
                        values.get(index++);

                if (rightValue != null) {

                    parent.right =
                            node(rightValue);

                    queue.add(
                            parent.right);
                }
            }
        }

        return root;
    }

    /* **********************************************************************
     * Random BST Generation
     * **********************************************************************/

    static Node insert(
            Node root,
            int value) {

        if (root == null) {
            return node(value);
        }

        if (value < root.val) {

            root.left =
                    insert(
                            root.left,
                            value);

        } else {

            root.right =
                    insert(
                            root.right,
                            value);
        }

        return root;
    }

    static Node randomBST(
            Random random,
            int size) {

        Node root = null;

        for (int i = 0;
             i < size;
             i++) {

            /*
             * A relatively small range deliberately creates duplicate
             * values, ensuring that the duplicate-value behaviour is
             * tested frequently.
             */
            int value =
                    random.nextInt(101)
                            - 50;

            root =
                    insert(
                            root,
                            value);
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
                new Random(20261004L);

        for (int iteration = 1;
             iteration <= iterations;
             iteration++) {

            int size =
                    1 + random.nextInt(50);

            Node root =
                    randomBST(
                            random,
                            size);

            ArrayList<Integer> expected =
                    expectedGreaterSum(
                            root);

            for (MethodCase method :
                    methods) {

                Node testRoot =
                        copyTree(root);

                Result result =
                        method.algorithm.solve(
                                testRoot);

                ArrayList<Integer> actual =
                        inOrder(
                                buildTreeFromLevels(
                                        result.result()));

                if (!result.valid()
                        || !actual.equals(
                                expected)) {

                    System.out.println(
                            "Randomised test FAILED");

                    System.out.println(
                            "iteration = "
                                    + iteration);

                    System.out.println(
                            "algorithm = "
                                    + method.name);

                    System.out.println(
                            "original = "
                                    + inOrder(root));

                    System.out.println(
                            "expected = "
                                    + expected);

                    System.out.println(
                            "actual = "
                                    + actual);

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
                "################ BST GREATER SUM TREE ######################");

        System.out.println(
                "############################################################");

        System.out.println();

        List<MethodCase> methods =
                List.of(

                        new MethodCase(
                                "Brute Force",
                                BSTToGreaterSumTree
                                        ::bSTTogreaterSumBruteForce),

                        new MethodCase(
                                "Single Traversal",
                                BSTToGreaterSumTree
                                        ::bSTTogreaterSumSingleTraversal)
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
                "All test suites completed.");
    }
}
