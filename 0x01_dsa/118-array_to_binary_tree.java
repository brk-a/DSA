import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Build a height-balanced Binary Search Tree (BST) from a sorted array.
 *
 * Two implementations are provided:
 *
 * 1. Recursive
 *      Select the middle element as the root and recursively build
 *      the left and right subtrees.
 *
 * 2. Queue
 *      Perform the same construction iteratively using a queue.
 *
 * Important:
 *
 *      The input array must be sorted in ascending order.
 *
 * Example:
 *
 *      [1, 2, 3, 4, 5, 6, 7]
 *
 *              4
 *            /   \
 *           2     6
 *          / \   / \
 *         1   3 5   7
 *
 * Complexity:
 *
 * Recursive:
 *      Time:  O(n)
 *      Space: O(log n) call stack
 *
 * Queue:
 *      Time:  O(n)
 *      Space: O(n) queue
 *
 * where:
 *
 *      n = number of elements in the array
 */
public class ArrayToBST {

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
     * Results
     * **********************************************************************/

    static record Result(
            Node root,
            boolean valid) {
    }

    /* **********************************************************************
     * Queue State
     * **********************************************************************/

    /**
     * Represents a subtree which still needs to be constructed.
     *
     * The parent and leftChild fields determine where the newly
     * constructed node should be attached.
     */
    static record Task(
            Node parent,
            int start,
            int end,
            boolean leftChild) {
    }

    /* **********************************************************************
     * Helpers
     * **********************************************************************/

    /**
     * Checks whether the array is a valid input.
     *
     * A valid input must:
     *
     *      - not be null
     *      - contain at least one element
     */
    static boolean isValidInput(int[] array) {

        return array != null
                && array.length > 0;
    }

    /**
     * Checks whether the array is sorted in ascending order.
     *
     * Duplicate values are permitted.
     */
    static boolean isSorted(int[] array) {

        if (!isValidInput(array)) {
            return false;
        }

        for (int i = 1; i < array.length; i++) {

            if (array[i] < array[i - 1]) {
                return false;
            }
        }

        return true;
    }

    /**
     * Validates that the array can be used to construct a BST.
     */
    static boolean isValidBSTInput(int[] array) {

        return isValidInput(array)
                && isSorted(array);
    }

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

    /**
     * Counts the nodes in a tree.
     */
    static int size(Node root) {

        if (root == null) {
            return 0;
        }

        return 1
                + size(root.left)
                + size(root.right);
    }

    /**
     * Determines whether the tree is height-balanced.
     *
     * A tree is considered balanced when the height difference
     * between the left and right subtree of every node is at most one.
     */
    static boolean isBalanced(Node root) {

        return height(root) >= 0;
    }

    /**
     * Returns the height of the tree.
     *
     * A return value of -1 indicates that the tree is unbalanced.
     */
    static int height(Node root) {

        if (root == null) {
            return 0;
        }

        int leftHeight =
                height(root.left);

        if (leftHeight == -1) {
            return -1;
        }

        int rightHeight =
                height(root.right);

        if (rightHeight == -1) {
            return -1;
        }

        if (Math.abs(leftHeight - rightHeight) > 1) {
            return -1;
        }

        return 1
                + Math.max(
                leftHeight,
                rightHeight);
    }

    /**
     * Determines whether the tree is a valid BST.
     *
     * Duplicate values are permitted and may occur on either side
     * because the source array may contain duplicates.
     *
     * The validation therefore checks that an in-order traversal
     * produces a non-decreasing sequence.
     */
    static boolean isBST(Node root) {

        ArrayList<Integer> values =
                new ArrayList<>();

        inOrder(root, values);

        for (int i = 1; i < values.size(); i++) {

            if (values.get(i) < values.get(i - 1)) {
                return false;
            }
        }

        return true;
    }

    static void inOrder(
            Node root,
            ArrayList<Integer> values) {

        if (root == null) {
            return;
        }

        inOrder(
                root.left,
                values);

        values.add(root.val);

        inOrder(
                root.right,
                values);
    }

    /**
     * Compares two trees structurally and by value.
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
     * Converts a tree into an in-order list.
     *
     * Useful for checking that the resulting BST contains exactly
     * the same values as the input array.
     */
    static ArrayList<Integer> inOrderValues(Node root) {

        ArrayList<Integer> values =
                new ArrayList<>();

        inOrder(
                root,
                values);

        return values;
    }

    /**
     * Compares an array with an in-order traversal of a tree.
     */
    static boolean sameValues(
            int[] array,
            Node root) {

        ArrayList<Integer> values =
                inOrderValues(root);

        if (values.size() != array.length) {
            return false;
        }

        for (int i = 0; i < array.length; i++) {

            if (values.get(i) != array[i]) {
                return false;
            }
        }

        return true;
    }

    /* **********************************************************************
     * 1. Recursive Implementation
     * **********************************************************************/

    /**
     * Builds a height-balanced BST recursively.
     *
     * The input array must be sorted in ascending order.
     */
    static Result arrayToBSTRecursion(
            int[] array) {

        if (!isValidBSTInput(array)) {

            return new Result(
                    null,
                    false);
        }

        Node root =
                buildBST(
                        array,
                        0,
                        array.length - 1);

        return new Result(
                root,
                true);
    }

    /**
     * Recursively constructs a subtree from array[start..end].
     */
    static Node buildBST(
            int[] array,
            int start,
            int end) {

        if (start > end) {
            return null;
        }

        int mid =
                start
                        + (end - start) / 2;

        Node root =
                new Node(array[mid]);

        root.left =
                buildBST(
                        array,
                        start,
                        mid - 1);

        root.right =
                buildBST(
                        array,
                        mid + 1,
                        end);

        return root;
    }

    /* **********************************************************************
     * 2. Queue Implementation
     * **********************************************************************/

    /**
     * Builds a height-balanced BST iteratively using a queue.
     *
     * The queue stores ranges of the input array which still need
     * to be converted into tree nodes.
     *
     * The input array must be sorted in ascending order.
     */
    static Result arrayToBSTQueue(
            int[] array) {

        if (!isValidBSTInput(array)) {

            return new Result(
                    null,
                    false);
        }

        Queue<Task> queue =
                new ArrayDeque<>();

        /*
         * The root is represented by a task with no parent.
         */
        queue.offer(
                new Task(
                        null,
                        0,
                        array.length - 1,
                        false));

        Node root = null;

        while (!queue.isEmpty()) {

            Task task =
                    queue.poll();

            int start =
                    task.start();

            int end =
                    task.end();

            /*
             * This should not normally occur because only valid
             * ranges are added to the queue, but keeping the guard
             * makes the helper robust.
             */
            if (start > end) {
                continue;
            }

            int mid =
                    start
                            + (end - start) / 2;

            Node node =
                    new Node(array[mid]);

            /*
             * Attach the new node to its parent.
             */
            if (task.parent() == null) {

                root = node;

            } else if (task.leftChild()) {

                task.parent().left = node;

            } else {

                task.parent().right = node;
            }

            /*
             * Left subtree.
             */
            queue.offer(
                    new Task(
                            node,
                            start,
                            mid - 1,
                            true));

            /*
             * Right subtree.
             */
            queue.offer(
                    new Task(
                            node,
                            mid + 1,
                            end,
                            false));
        }

        return new Result(
                root,
                root != null);
    }

    /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    static class TestCase {

        final String id;
        final int[] input;
        final String description;

        TestCase(
                String id,
                int[] input,
                String description) {

            this.id = id;
            this.input = input;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {

        Result solve(int[] array);
    }

    static class AlgorithmCase {

        final String name;
        final Algorithm algorithm;

        AlgorithmCase(
                String name,
                Algorithm algorithm) {

            this.name = name;
            this.algorithm = algorithm;
        }
    }

    /* **********************************************************************
     * Standard Tests
     * **********************************************************************/

    static void runTests(
            String algorithmName,
            Algorithm algorithm,
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

                Result result =
                        algorithm.solve(test.input);

                boolean valid =
                        result.valid()
                                && result.root() != null;

                boolean correctSize =
                        valid
                                && size(result.root())
                                == test.input.length;

                boolean correctValues =
                        valid
                                && sameValues(
                                test.input,
                                result.root());

                boolean balanced =
                        valid
                                && isBalanced(
                                result.root());

                boolean bst =
                        valid
                                && isBST(
                                result.root());

                boolean passedTest =
                        valid
                                && correctSize
                                && correctValues
                                && balanced
                                && bst;

                if (passedTest) {

                    passed++;

                    System.out.printf(
                            "PASS %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  input = "
                                    + arrayToString(
                                    test.input));

                    System.out.println(
                            "  root = "
                                    + result.root().val);

                    System.out.println(
                            "  size = "
                                    + size(result.root()));

                    System.out.println(
                            "  height = "
                                    + height(result.root()));

                } else {

                    failed++;

                    System.out.printf(
                            "FAIL %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  input = "
                                    + arrayToString(
                                    test.input));

                    System.out.println(
                            "  valid = "
                                    + result.valid());

                    System.out.println(
                            "  correct size = "
                                    + correctSize);

                    System.out.println(
                            "  correct values = "
                                    + correctValues);

                    System.out.println(
                            "  balanced = "
                                    + balanced);

                    System.out.println(
                            "  BST = "
                                    + bst);
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

        System.out.println();

        System.out.printf(
                "Results: %d passed, %d failed, %d total%n",
                passed,
                failed,
                tests.size());

        System.out.println();
    }

    /* **********************************************************************
     * Invalid Input Tests
     * **********************************************************************/

    static void runInvalidInputTests(
            List<AlgorithmCase> algorithms) {

        System.out.println(
                "======================================================");

        System.out.println(
                "Invalid Input Tests");

        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        /*
         * Null array.
         */
        for (AlgorithmCase algorithm :
                algorithms) {

            Result result =
                    algorithm.algorithm.solve(null);

            if (!result.valid()
                    && result.root() == null) {

                passed++;

                System.out.printf(
                        "PASS I1-%s - null array rejected%n",
                        algorithm.name);

            } else {

                failed++;

                System.out.printf(
                        "FAIL I1-%s - null array should be rejected%n",
                        algorithm.name);
            }
        }

        /*
         * Empty array.
         */
        for (AlgorithmCase algorithm :
                algorithms) {

            Result result =
                    algorithm.algorithm.solve(
                            new int[]{});

            if (!result.valid()
                    && result.root() == null) {

                passed++;

                System.out.printf(
                        "PASS I2-%s - empty array rejected%n",
                        algorithm.name);

            } else {

                failed++;

                System.out.printf(
                        "FAIL I2-%s - empty array should be rejected%n",
                        algorithm.name);
            }
        }

        /*
         * Unsorted array.
         *
         * A BST cannot be constructed directly from this array
         * while preserving the array order as the in-order sequence.
         */
        for (AlgorithmCase algorithm :
                algorithms) {

            int[] unsorted =
                    {1, 4, 2, 3, 5};

            Result result =
                    algorithm.algorithm.solve(
                            unsorted);

            if (!result.valid()
                    && result.root() == null) {

                passed++;

                System.out.printf(
                        "PASS I3-%s - unsorted array rejected%n",
                        algorithm.name);

            } else {

                failed++;

                System.out.printf(
                        "FAIL I3-%s - unsorted array should be rejected%n",
                        algorithm.name);
            }
        }

        /*
         * Descending array.
         */
        for (AlgorithmCase algorithm :
                algorithms) {

            int[] descending =
                    {5, 4, 3, 2, 1};

            Result result =
                    algorithm.algorithm.solve(
                            descending);

            if (!result.valid()
                    && result.root() == null) {

                passed++;

                System.out.printf(
                        "PASS I4-%s - descending array rejected%n",
                        algorithm.name);

            } else {

                failed++;

                System.out.printf(
                        "FAIL I4-%s - descending array should be rejected%n",
                        algorithm.name);
            }
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
     * Cross-Implementation Tests
     * **********************************************************************/

    static void runCrossImplementationTests(
            List<TestCase> tests) {

        System.out.println(
                "======================================================");

        System.out.println(
                "Recursive vs Queue Cross Checks");

        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            Result recursive =
                    arrayToBSTRecursion(
                            test.input);

            Result queue =
                    arrayToBSTQueue(
                            test.input);

            boolean equal =
                    recursive.valid()
                            && queue.valid()
                            && treesEqual(
                            recursive.root(),
                            queue.root());

            if (equal) {

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
                        "  recursive root = "
                                + rootValue(
                                recursive.root()));

                System.out.println(
                        "  queue root = "
                                + rootValue(
                                queue.root()));
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
     * Random Sorted Array Generation
     * **********************************************************************/

    /**
     * Generates a sorted array.
     *
     * Random increments are used rather than simply generating random
     * values and sorting them, which also gives useful coverage for
     * duplicate values.
     */
    static int[] randomSortedArray(
            Random random,
            int size) {

        int[] array =
                new int[size];

        int value =
                random.nextInt(21) - 10;

        for (int i = 0; i < size; i++) {

            if (i > 0) {

                /*
                 * Zero is deliberately permitted so that duplicate
                 * values are generated occasionally.
                 */
                value +=
                        random.nextInt(5);
            }

            array[i] = value;
        }

        return array;
    }

    /* **********************************************************************
     * Randomised Tests
     * **********************************************************************/

    static void runRandomisedTests(
            int iterations) {

        System.out.println(
                "======================================================");

        System.out.println(
                "Randomised Cross Checks");

        System.out.println(
                "======================================================");

        Random random =
                new Random(20260923L);

        int passed = 0;
        int failed = 0;

        for (int iteration = 1;
             iteration <= iterations;
             iteration++) {

            /*
             * Include a range of array sizes, including very small
             * arrays and moderately larger arrays.
             */
            int size =
                    random.nextInt(100) + 1;

            int[] input =
                    randomSortedArray(
                            random,
                            size);

            Result recursive =
                    arrayToBSTRecursion(
                            input);

            Result queue =
                    arrayToBSTQueue(
                            input);

            boolean recursiveValid =
                    recursive.valid()
                            && recursive.root() != null
                            && size(recursive.root())
                            == input.length
                            && sameValues(
                            input,
                            recursive.root())
                            && isBalanced(
                            recursive.root())
                            && isBST(
                            recursive.root());

            boolean queueValid =
                    queue.valid()
                            && queue.root() != null
                            && size(queue.root())
                            == input.length
                            && sameValues(
                            input,
                            queue.root())
                            && isBalanced(
                            queue.root())
                            && isBST(
                            queue.root());

            boolean implementationsEqual =
                    recursiveValid
                            && queueValid
                            && treesEqual(
                            recursive.root(),
                            queue.root());

            if (recursiveValid
                    && queueValid
                    && implementationsEqual) {

                passed++;

            } else {

                failed++;

                System.out.printf(
                        "FAIL random iteration %d%n",
                        iteration);

                System.out.println(
                        "  input = "
                                + arrayToString(input));

                System.out.println(
                        "  recursive valid = "
                                + recursiveValid);

                System.out.println(
                        "  queue valid = "
                                + queueValid);

                System.out.println(
                        "  trees equal = "
                                + implementationsEqual);

                /*
                 * Stop at the first random failure so that the
                 * failing input is easy to reproduce.
                 */
                break;
            }
        }

        System.out.println();

        if (failed == 0) {

            System.out.printf(
                    "All %d randomised tests passed.%n",
                    passed);

        } else {

            System.out.printf(
                    "Results: %d passed, %d failed.%n",
                    passed,
                    failed);
        }

        System.out.println();
    }

    /* **********************************************************************
     * Utility Output
     * **********************************************************************/

    static String rootValue(Node root) {

        if (root == null) {
            return "null";
        }

        return String.valueOf(root.val);
    }

    static String arrayToString(
            int[] array) {

        if (array == null) {
            return "null";
        }

        return java.util.Arrays.toString(array);
    }

    /* **********************************************************************
     * Main Test Suite
     * **********************************************************************/

    public static void main(String[] args) {

        /*
         * ============================================================
         * Test Data
         * ============================================================
         */

        List<TestCase> tests =
                new ArrayList<>();

        /*
         * Single element.
         */
        tests.add(
                new TestCase(
                        "B1",
                        new int[]{1},
                        "single element"));

        /*
         * Two elements.
         */
        tests.add(
                new TestCase(
                        "B2",
                        new int[]{1, 2},
                        "two elements"));

        /*
         * Three elements.
         */
        tests.add(
                new TestCase(
                        "B3",
                        new int[]{1, 2, 3},
                        "three elements"));

        /*
         * Four elements.
         */
        tests.add(
                new TestCase(
                        "B4",
                        new int[]{1, 2, 3, 4},
                        "four elements"));

        /*
         * Five elements.
         */
        tests.add(
                new TestCase(
                        "B5",
                        new int[]{1, 2, 3, 4, 5},
                        "five elements"));

        /*
         * Seven elements.
         */
        tests.add(
                new TestCase(
                        "B6",
                        new int[]{1, 2, 3, 4, 5, 6, 7},
                        "complete balanced input"));

        /*
         * Eight elements.
         */
        tests.add(
                new TestCase(
                        "B7",
                        new int[]{
                                1, 2, 3, 4,
                                5, 6, 7, 8
                        },
                        "eight elements"));

        /*
         * Larger input.
         */
        tests.add(
                new TestCase(
                        "L1",
                        new int[]{
                                1, 2, 3, 4, 5,
                                6, 7, 8, 9, 10,
                                11, 12, 13, 14, 15
                        },
                        "fifteen elements"));

        /*
         * Negative values.
         */
        tests.add(
                new TestCase(
                        "N1",
                        new int[]{
                                -10, -8, -5,
                                -2, 0, 3, 7
                        },
                        "negative and positive values"));

        /*
         * Duplicate values.
         */
        tests.add(
                new TestCase(
                        "D1",
                        new int[]{
                                1, 1, 1, 2,
                                2, 3, 3
                        },
                        "duplicate values"));

        /*
         * All values identical.
         */
        tests.add(
                new TestCase(
                        "D2",
                        new int[]{
                                5, 5, 5, 5, 5
                        },
                        "all duplicate values"));

        /*
         * Values around zero.
         */
        tests.add(
                new TestCase(
                        "N2",
                        new int[]{
                                -5, -4, -3, -2, -1,
                                0,
                                1, 2, 3, 4, 5
                        },
                        "values around zero"));

        /*
         * Integer boundaries.
         */
        tests.add(
                new TestCase(
                        "E1",
                        new int[]{
                                Integer.MIN_VALUE,
                                -1,
                                0,
                                1,
                                Integer.MAX_VALUE
                        },
                        "integer boundary values"));

        /*
         * ============================================================
         * Header
         * ============================================================
         */

        System.out.println(
                "############################################################");

        System.out.println(
                "####################  ARRAY TO BST  ########################");

        System.out.println(
                "############################################################");

        System.out.println();

        /*
         * ============================================================
         * Algorithms
         * ============================================================
         */

        List<AlgorithmCase> algorithms =
                List.of(

                        new AlgorithmCase(
                                "Recursive",
                                ArrayToBST
                                        ::arrayToBSTRecursion),

                        new AlgorithmCase(
                                "Queue",
                                ArrayToBST
                                        ::arrayToBSTQueue)
                );

        /*
         * ============================================================
         * Standard Tests
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
         * Cross-Implementation Tests
         * ============================================================
         */

        runCrossImplementationTests(
                tests);

        /*
         * ============================================================
         * Invalid Input Tests
         * ============================================================
         */

        runInvalidInputTests(
                algorithms);

        /*
         * ============================================================
         * Randomised Tests
         * ============================================================
         */

        runRandomisedTests(5000);

        /*
         * ============================================================
         * Complete
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
