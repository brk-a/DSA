import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Remove all keys outside a given inclusive range from a Binary Search Tree.
 *
 * The resulting tree remains a valid BST and preserves the relative ordering
 * of all nodes that remain.
 *
 * Two implementations are provided:
 *
 * 1. Post-order
 *
 *      Processes:
 *
 *          left -> right -> root
 *
 *      The two child subtrees are fixed before the current node is considered.
 *
 *      Time:  O(n)
 *      Space: O(h)
 *
 * 2. BST Property Recursion
 *
 *      Uses the BST ordering property to discard entire subtrees when the
 *      current node proves that they cannot contain values in the range.
 *
 *      Time:  O(n) worst case
 *      Space: O(h)
 *
 * The range is inclusive:
 *
 *      [leftVal, rightVal]
 *
 * Result.range() represents the resulting tree in level-order. Each inner
 * ArrayList represents one level. A null value represents a missing child.
 */
public class RemoveBSTKeysOutsideGivenRange {

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
     * range:
     *
     * The resulting tree represented in level-order.
     *
     * For example:
     *
     *          6
     *        /   \
     *      -8     13
     *             /
     *            7
     *
     * is represented as:
     *
     * [
     *     [6],
     *     [-8, 13],
     *     [null, null, 7, null]
     * ]
     *
     * valid:
     *
     * false means that the input request was invalid, for example a null
     * root or an inverted range.
     */
    static record Result(
            ArrayList<ArrayList<Integer>> range,
            boolean valid) {
    }

    /* **********************************************************************
     * Helpers
     * **********************************************************************/

    static boolean validRoot(
            Node root) {

        return root != null;
    }

    static boolean validRange(
            int leftVal,
            int rightVal) {

        return leftVal <= rightVal;
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
     * Each test starts with a fresh tree because both removal algorithms
     * modify the tree in place.
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

    /* **********************************************************************
     * 1. Post-order
     * **********************************************************************/

    /**
     * Remove all keys outside [leftVal, rightVal] using post-order
     * processing.
     *
     * The children are processed before the current node:
     *
     *      left -> right -> root
     *
     * This means that when the current node is examined, both of its
     * subtrees have already been trimmed.
     *
     * If the current value is:
     *
     *      less than leftVal:
     *          discard the current node and return its trimmed right subtree.
     *
     *      greater than rightVal:
     *          discard the current node and return its trimmed left subtree.
     *
     *      inside the range:
     *          retain the current node.
     */
    static Result removeBSTKeysOutsideRangePostOrder(
            Node root,
            int leftVal,
            int rightVal) {

        if (!validRoot(root)
                || !validRange(leftVal, rightVal)) {

            return new Result(
                    new ArrayList<>(),
                    false);
        }

        Node trimmedRoot =
                removePostOrder(
                        root,
                        leftVal,
                        rightVal);

        return new Result(
                levelOrder(trimmedRoot),
                true);
    }

    /**
     * Recursive post-order implementation.
     */
    static Node removePostOrder(
            Node root,
            int leftVal,
            int rightVal) {

        if (root == null) {
            return null;
        }

        /*
         * First fix both subtrees.
         */
        Node left =
                removePostOrder(
                        root.left,
                        leftVal,
                        rightVal);

        Node right =
                removePostOrder(
                        root.right,
                        leftVal,
                        rightVal);

        /*
         * The current node is inside the required range.
         *
         * Reattach the already-trimmed children.
         */
        if (root.val >= leftVal
                && root.val <= rightVal) {

            root.left = left;
            root.right = right;

            return root;
        }

        /*
         * The current node is smaller than the range.
         *
         * Because this is a BST, the left subtree contains values no
         * greater than the current value and therefore cannot contain
         * a valid value.
         *
         * The trimmed right subtree becomes the replacement root.
         */
        if (root.val < leftVal) {

            return right;
        }

        /*
         * The current node is greater than the range.
         *
         * The right subtree cannot contain a valid value.
         *
         * The trimmed left subtree becomes the replacement root.
         */
        return left;
    }

    /* **********************************************************************
     * 2. BST Property Recursion
     * **********************************************************************/

    /**
     * Remove all keys outside [leftVal, rightVal] using the BST property.
     *
     * Unlike the explicit post-order implementation above, this version
     * does not process a subtree that is already known to be completely
     * outside the requested range.
     *
     * If:
     *
     *      root.val < leftVal
     *
     * then everything in root.left is also less than leftVal, so the
     * entire left subtree can be discarded immediately.
     *
     * Similarly, if:
     *
     *      root.val > rightVal
     *
     * the entire right subtree can be discarded immediately.
     */
    static Result removeBSTKeysOutsideRangeRecursion(
            Node root,
            int leftVal,
            int rightVal) {

        if (!validRoot(root)
                || !validRange(leftVal, rightVal)) {

            return new Result(
                    new ArrayList<>(),
                    false);
        }

        Node trimmedRoot =
                removeUsingBSTProperty(
                        root,
                        leftVal,
                        rightVal);

        return new Result(
                levelOrder(trimmedRoot),
                true);
    }

    /**
     * Recursive implementation using the BST ordering property.
     */
    static Node removeUsingBSTProperty(
            Node root,
            int leftVal,
            int rightVal) {

        if (root == null) {
            return null;
        }

        /*
         * Everything in the left subtree is <= root.val.
         *
         * Since root.val is already below the lower bound, the entire
         * left subtree is also outside the range.
         */
        if (root.val < leftVal) {

            return removeUsingBSTProperty(
                    root.right,
                    leftVal,
                    rightVal);
        }

        /*
         * Everything in the right subtree is >= root.val.
         *
         * Since root.val is already above the upper bound, the entire
         * right subtree is also outside the range.
         */
        if (root.val > rightVal) {

            return removeUsingBSTProperty(
                    root.left,
                    leftVal,
                    rightVal);
        }

        /*
         * The current node is valid.
         *
         * Both children still need to be trimmed.
         */
        root.left =
                removeUsingBSTProperty(
                        root.left,
                        leftVal,
                        rightVal);

        root.right =
                removeUsingBSTProperty(
                        root.right,
                        leftVal,
                        rightVal);

        return root;
    }

    /* **********************************************************************
     * Level-order Representation
     * **********************************************************************/

    /**
     * Convert a tree into a level-order representation.
     *
     * Missing children are represented by null.
     *
     * Trailing levels containing only null values are omitted.
     */
    static ArrayList<ArrayList<Integer>> levelOrder(
            Node root) {

        ArrayList<ArrayList<Integer>> result =
                new ArrayList<>();

        if (root == null) {
            return result;
        }

        Queue<Node> queue =
                new ArrayDeque<>();

        /*
         * ArrayDeque does not allow null elements, so we use a separate
         * queue implementation below that can represent null children.
         */
        ArrayList<Node> currentLevel =
                new ArrayList<>();

        currentLevel.add(root);

        while (!currentLevel.isEmpty()) {

            ArrayList<Integer> values =
                    new ArrayList<>();

            ArrayList<Node> nextLevel =
                    new ArrayList<>();

            boolean hasNonNullNode =
                    false;

            for (Node node :
                    currentLevel) {

                if (node == null) {

                    values.add(null);

                    nextLevel.add(null);
                    nextLevel.add(null);

                } else {

                    values.add(node.val);

                    nextLevel.add(node.left);
                    nextLevel.add(node.right);

                    if (node.left != null
                            || node.right != null) {

                        hasNonNullNode = true;
                    }
                }
            }

            result.add(values);

            /*
             * If there are no real nodes at the next level, stop.
             *
             * This prevents an infinite sequence of null-only levels.
             */
            if (!hasNonNullNode) {
                break;
            }

            currentLevel = nextLevel;
        }

        return result;
    }

    /* **********************************************************************
     * In-order Traversal
     * **********************************************************************/

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

    /**
     * Return the in-order values of a tree.
     *
     * This is useful as a simple reference representation in the tests.
     */
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
     * BST Validation
     * **********************************************************************/

    /**
     * Validate the BST ordering using in-order traversal.
     *
     * Duplicate values are allowed on the right, matching the insertion
     * policy used by the test generator.
     */
    static boolean isValidBST(
            Node root) {

        ArrayList<Integer> values =
                inOrder(root);

        for (int i = 1;
             i < values.size();
             i++) {

            if (values.get(i)
                    < values.get(i - 1)) {

                return false;
            }
        }

        return true;
    }

    /* **********************************************************************
     * Test Case
     * **********************************************************************/

    static class TestCase {

        final String id;
        final Node root;
        final int leftVal;
        final int rightVal;
        final ArrayList<Integer> expectedInOrder;
        final String description;

        TestCase(
                String id,
                Node root,
                int leftVal,
                int rightVal,
                ArrayList<Integer> expectedInOrder,
                String description) {

            this.id = id;
            this.root = root;
            this.leftVal = leftVal;
            this.rightVal = rightVal;
            this.expectedInOrder = expectedInOrder;
            this.description = description;
        }
    }

    /* **********************************************************************
     * Algorithm
     * **********************************************************************/

    @FunctionalInterface
    interface Algorithm {

        Result solve(
                Node root,
                int leftVal,
                int rightVal);
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

                Node testRoot =
                        copyTree(test.root);

                Result actual =
                        method.algorithm.solve(
                                testRoot,
                                test.leftVal,
                                test.rightVal);

                ArrayList<Integer> actualInOrder =
                        levelOrderToInOrder(
                                actual.range());

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
                            "  in-order = "
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
     * Convert Level-order Result Back To In-order
     * **********************************************************************/

    /**
     * Reconstruct the tree represented by Result.range().
     *
     * This helper is used only by the test suite.
     *
     * It is deliberately separate from the algorithms being tested.
     */
    static ArrayList<Integer> levelOrderToInOrder(
            ArrayList<ArrayList<Integer>> levels) {

        ArrayList<Integer> result =
                new ArrayList<>();

        if (levels.isEmpty()) {
            return result;
        }

        Node root =
                buildTreeFromLevels(levels);

        inOrder(
                root,
                result);

        return result;
    }

    /**
     * Rebuild a tree from the level-order representation.
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

            ArrayList<Integer> firstValues =
                    null;

            boolean success = true;

            for (MethodCase method :
                    methods) {

                Node testRoot =
                        copyTree(test.root);

                Result actual =
                        method.algorithm.solve(
                                testRoot,
                                test.leftVal,
                                test.rightVal);

                ArrayList<Integer> values =
                        levelOrderToInOrder(
                                actual.range());

                if (!actual.valid()
                        || !values.equals(
                                test.expectedInOrder)) {

                    success = false;

                    System.out.printf(
                            "  %s = %s%n",
                            method.name,
                            values);
                }

                if (firstValues == null) {

                    firstValues = values;

                } else if (!firstValues.equals(values)) {

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

        Node root =
                node(
                        10,
                        node(5),
                        node(15));

        /*
         * Null root.
         */
        for (MethodCase method :
                methods) {

            Result result =
                    method.algorithm.solve(
                            null,
                            0,
                            20);

            if (!result.valid()) {

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

        /*
         * Inverted range.
         */
        for (MethodCase method :
                methods) {

            Result result =
                    method.algorithm.solve(
                            copyTree(root),
                            20,
                            10);

            if (!result.valid()) {

                passed++;

                System.out.printf(
                        "PASS I2 - %s rejects inverted range%n",
                        method.name);

            } else {

                failed++;

                System.out.printf(
                        "FAIL I2 - %s accepts inverted range%n",
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
         * In-order:
         *
         *      5, 10, 15, 20, 25, 30, 35
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
                        5,
                        35,
                        new ArrayList<>(
                                List.of(
                                        5,
                                        10,
                                        15,
                                        20,
                                        25,
                                        30,
                                        35)),
                        "entire tree remains"));

        tests.add(
                new TestCase(
                        "B2",
                        root,
                        10,
                        30,
                        new ArrayList<>(
                                List.of(
                                        10,
                                        15,
                                        20,
                                        25,
                                        30)),
                        "both outer subtrees are trimmed"));

        tests.add(
                new TestCase(
                        "B3",
                        root,
                        15,
                        25,
                        new ArrayList<>(
                                List.of(
                                        15,
                                        20,
                                        25)),
                        "small middle range"));

        tests.add(
                new TestCase(
                        "B4",
                        root,
                        20,
                        20,
                        new ArrayList<>(
                                List.of(20)),
                        "only the root remains"));

        tests.add(
                new TestCase(
                        "B5",
                        root,
                        100,
                        200,
                        new ArrayList<>(),
                        "all nodes are below the range"));

        tests.add(
                new TestCase(
                        "B6",
                        root,
                        -200,
                        -100,
                        new ArrayList<>(),
                        "all nodes are above the range"));

        /*
         * ============================================================
         * Standard Example
         * ============================================================
         *
         *              6
         *            /   \
         *         -13     14
         *            \    / \
         *            -8  13 15
         *                /
         *               7
         *
         * Range: [-10, 13]
         *
         * Remaining values:
         *
         *      -8, 6, 7, 13
         */

        Node example =
                node(
                        6,
                        node(
                                -13,
                                null,
                                node(-8)),
                        node(
                                14,
                                node(
                                        13,
                                        node(7),
                                        null),
                                node(15)));

        tests.add(
                new TestCase(
                        "E1",
                        example,
                        -10,
                        13,
                        new ArrayList<>(
                                List.of(
                                        -8,
                                        6,
                                        7,
                                        13)),
                        "standard range-trimming example"));

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
                        100,
                        100,
                        new ArrayList<>(
                                List.of(100)),
                        "single node inside range"));

        tests.add(
                new TestCase(
                        "S2",
                        single,
                        101,
                        200,
                        new ArrayList<>(),
                        "single node below range"));

        tests.add(
                new TestCase(
                        "S3",
                        single,
                        -200,
                        99,
                        new ArrayList<>(),
                        "single node above range"));

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

        tests.add(
                new TestCase(
                        "N1",
                        negative,
                        -8,
                        -1,
                        new ArrayList<>(
                                List.of(
                                        -7,
                                        -5,
                                        -1)),
                        "negative values"));

        /*
         * ============================================================
         * Range Boundaries
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "R1",
                        root,
                        5,
                        5,
                        new ArrayList<>(
                                List.of(5)),
                        "lower boundary is inclusive"));

        tests.add(
                new TestCase(
                        "R2",
                        root,
                        35,
                        35,
                        new ArrayList<>(
                                List.of(35)),
                        "upper boundary is inclusive"));

        return tests;
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

            int value =
                    random.nextInt(2001)
                            - 1000;

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

            int leftVal =
                    random.nextInt(2201)
                            - 1100;

            int rightVal =
                    random.nextInt(2201)
                            - 1100;

            if (leftVal > rightVal) {

                int temporary =
                        leftVal;

                leftVal =
                        rightVal;

                rightVal =
                        temporary;
            }

            /*
             * The expected result is obtained directly from the original
             * tree's in-order sequence.
             *
             * Because a BST's in-order traversal is sorted, filtering this
             * sequence gives the exact keys that must remain.
             */
            ArrayList<Integer> original =
                    inOrder(root);

            ArrayList<Integer> expected =
                    new ArrayList<>();

            for (Integer value :
                    original) {

                if (value >= leftVal
                        && value <= rightVal) {

                    expected.add(value);
                }
            }

            for (MethodCase method :
                    methods) {

                Node testRoot =
                        copyTree(root);

                Result result =
                        method.algorithm.solve(
                                testRoot,
                                leftVal,
                                rightVal);

                ArrayList<Integer> actual =
                        levelOrderToInOrder(
                                result.range());

                if (!result.valid()
                        || !actual.equals(expected)
                        || !isValidBST(
                                buildTreeFromLevels(
                                        result.range()))) {

                    System.out.println(
                            "Randomised test FAILED");

                    System.out.println(
                            "iteration = "
                                    + iteration);

                    System.out.println(
                            "algorithm = "
                                    + method.name);

                    System.out.println(
                            "range = ["
                                    + leftVal
                                    + ", "
                                    + rightVal
                                    + "]");

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
     * Main
     * **********************************************************************/

    public static void main(
            String[] args) {

        System.out.println(
                "############################################################");

        System.out.println(
                "######## REMOVE BST KEYS OUTSIDE GIVEN RANGE ##############");

        System.out.println(
                "############################################################");

        System.out.println();

        List<MethodCase> methods =
                List.of(

                        new MethodCase(
                                "Post-order",
                                RemoveBSTKeysOutsideGivenRange
                                        ::removeBSTKeysOutsideRangePostOrder),

                        new MethodCase(
                                "BST Property Recursion",
                                RemoveBSTKeysOutsideGivenRange
                                        ::removeBSTKeysOutsideRangeRecursion)
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
