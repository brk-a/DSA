import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Construct Binary Tree from Inorder and Preorder Traversals.
 *
 * Given the inorder and preorder traversal arrays of a binary tree,
 * reconstruct the original binary tree.
 *
 * Example:
 *
 * Preorder:  [1, 2, 4, 5, 3]
 * Inorder:   [4, 2, 5, 1, 3]
 *
 * produces:
 *
 *              1
 *             / \
 *            2   3
 *           / \
 *          4   5
 *
 * Implementations:
 *
 * 1. Recursion
 *      Time:  O(n^2) worst case
 *      Space: O(n) for recursion and the resulting tree
 *
 * 2. Recursion + HashMap
 *      Time:  O(n)
 *      Space: O(n)
 *
 * Validity:
 *
 * The two arrays must:
 *
 *      - not be null
 *      - not be empty
 *      - have the same length
 *      - contain the same values
 *      - contain unique values
 *
 * Unique values are required because otherwise an inorder position
 * cannot be determined unambiguously.
 */
public class ConstructBinaryTree {

    /* **********************************************************************
     * Node / Result
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

    static record Result(Node root, boolean valid) {}

    /* **********************************************************************
     * Validation
     * **********************************************************************/

    static boolean validArray(int[] array) {
        return array != null && array.length > 0;
    }

    static boolean validInput(
            int[] inorder,
            int[] preorder) {

        if (!validArray(inorder)
                || !validArray(preorder)) {

            return false;
        }

        if (inorder.length != preorder.length) {
            return false;
        }

        Set<Integer> inorderValues =
                new HashSet<>();

        for (int value : inorder) {

            if (!inorderValues.add(value)) {
                return false;
            }
        }

        Set<Integer> preorderValues =
                new HashSet<>();

        for (int value : preorder) {

            if (!preorderValues.add(value)) {
                return false;
            }
        }

        return inorderValues.equals(preorderValues);
    }

    /* **********************************************************************
     * Recursive Implementation
     * **********************************************************************/

    /**
     * Constructs the binary tree recursively.
     *
     * Preorder:
     *
     *      root -> left -> right
     *
     * Inorder:
     *
     *      left -> root -> right
     *
     * Therefore, the next unused preorder value is the root.
     *
     * We search for that root inside the current inorder range.
     *
     * Everything before the root belongs to the left subtree.
     * Everything after the root belongs to the right subtree.
     *
     * Time:  O(n^2) worst case
     * Space: O(n)
     */
    static Result constructBinaryTreePreOrder(
            int[] inorder,
            int[] preorder) {

        if (!validInput(inorder, preorder)) {
            return new Result(null, false);
        }

        int[] preorderIndex = {0};

        Result result =
                constructBinaryTreePreOrderHelper(
                        inorder,
                        preorder,
                        0,
                        inorder.length - 1,
                        preorderIndex);

        if (!result.valid()
                || preorderIndex[0] != preorder.length) {

            return new Result(null, false);
        }

        return result;
    }

    static Result constructBinaryTreePreOrderHelper(
            int[] inorder,
            int[] preorder,
            int inorderStart,
            int inorderEnd,
            int[] preorderIndex) {

        /*
         * Empty range means an empty subtree.
         */
        if (inorderStart > inorderEnd) {
            return new Result(null, true);
        }

        /*
         * There must be another preorder value available.
         */
        if (preorderIndex[0] >= preorder.length) {
            return new Result(null, false);
        }

        /*
         * The next preorder value is the root.
         */
        int rootValue =
                preorder[preorderIndex[0]++];

        /*
         * Find the root in the current inorder range.
         */
        int rootIndex = -1;

        for (int i = inorderStart;
             i <= inorderEnd;
             i++) {

            if (inorder[i] == rootValue) {
                rootIndex = i;
                break;
            }
        }

        /*
         * Root was not found in this subtree's inorder range.
         */
        if (rootIndex == -1) {
            return new Result(null, false);
        }

        /*
         * Preorder visits the entire left subtree before
         * visiting the right subtree.
         */
        Result leftResult =
                constructBinaryTreePreOrderHelper(
                        inorder,
                        preorder,
                        inorderStart,
                        rootIndex - 1,
                        preorderIndex);

        if (!leftResult.valid()) {
            return new Result(null, false);
        }

        Result rightResult =
                constructBinaryTreePreOrderHelper(
                        inorder,
                        preorder,
                        rootIndex + 1,
                        inorderEnd,
                        preorderIndex);

        if (!rightResult.valid()) {
            return new Result(null, false);
        }

        Node root =
                new Node(
                        leftResult.root(),
                        rightResult.root(),
                        rootValue);

        return new Result(root, true);
    }

    /* **********************************************************************
     * HashMap Implementation
     * **********************************************************************/

    /**
     * Constructs the binary tree recursively using a HashMap.
     *
     * The HashMap stores:
     *
     *      value -> index in inorder
     *
     * This eliminates the O(n) search performed by the first
     * implementation.
     *
     * Time:  O(n)
     * Space: O(n)
     */
    static Result constructBinaryTreePreOrderHashMap(
            int[] inorder,
            int[] preorder) {

        if (!validInput(inorder, preorder)) {
            return new Result(null, false);
        }

        Map<Integer, Integer> inorderIndex =
                new HashMap<>();

        for (int i = 0;
             i < inorder.length;
             i++) {

            inorderIndex.put(
                    inorder[i],
                    i);
        }

        int[] preorderIndex = {0};

        Result result =
                constructBinaryTreePreOrderHashMapHelper(
                        preorder,
                        inorderIndex,
                        0,
                        inorder.length - 1,
                        preorderIndex);

        if (!result.valid()
                || preorderIndex[0] != preorder.length) {

            return new Result(null, false);
        }

        return result;
    }

    static Result constructBinaryTreePreOrderHashMapHelper(
            int[] preorder,
            Map<Integer, Integer> inorderIndex,
            int inorderStart,
            int inorderEnd,
            int[] preorderIndex) {

        /*
         * Empty range means an empty subtree.
         */
        if (inorderStart > inorderEnd) {
            return new Result(null, true);
        }

        /*
         * There must be another preorder value available.
         */
        if (preorderIndex[0] >= preorder.length) {
            return new Result(null, false);
        }

        /*
         * The next preorder value is the root.
         */
        int rootValue =
                preorder[preorderIndex[0]++];

        /*
         * Look up the root's position in inorder.
         */
        Integer rootIndex =
                inorderIndex.get(rootValue);

        /*
         * Root must exist in inorder.
         */
        if (rootIndex == null) {
            return new Result(null, false);
        }

        /*
         * Root must belong to the current subtree.
         */
        if (rootIndex < inorderStart
                || rootIndex > inorderEnd) {

            return new Result(null, false);
        }

        /*
         * Construct left subtree first.
         */
        Result leftResult =
                constructBinaryTreePreOrderHashMapHelper(
                        preorder,
                        inorderIndex,
                        inorderStart,
                        rootIndex - 1,
                        preorderIndex);

        if (!leftResult.valid()) {
            return new Result(null, false);
        }

        /*
         * Construct right subtree second.
         */
        Result rightResult =
                constructBinaryTreePreOrderHashMapHelper(
                        preorder,
                        inorderIndex,
                        rootIndex + 1,
                        inorderEnd,
                        preorderIndex);

        if (!rightResult.valid()) {
            return new Result(null, false);
        }

        Node root =
                new Node(
                        leftResult.root(),
                        rightResult.root(),
                        rootValue);

        return new Result(root, true);
    }

    /* **********************************************************************
     * Test Harness Types
     * **********************************************************************/

    static class TestCase {

        final String id;
        final int[] inorder;
        final int[] preorder;
        final Node expectedRoot;
        final boolean expectedValid;
        final String description;

        TestCase(
                String id,
                int[] inorder,
                int[] preorder,
                Node expectedRoot,
                boolean expectedValid,
                String description) {

            this.id = id;
            this.inorder = inorder;
            this.preorder = preorder;
            this.expectedRoot = expectedRoot;
            this.expectedValid = expectedValid;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {

        Result solve(
                int[] inorder,
                int[] preorder);
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

    static Node node(
            int val,
            Node left,
            Node right) {

        return new Node(left, right, val);
    }

    static Node leaf(int val) {
        return new Node(val);
    }

    /**
     * Builds:
     *
     *              1
     *             / \
     *            2   3
     *           / \
     *          4   5
     *
     * Inorder:  [4, 2, 5, 1, 3]
     * Preorder: [1, 2, 4, 5, 3]
     */
    static Node standardExample() {

        return node(
                1,
                node(
                        2,
                        leaf(4),
                        leaf(5)),
                leaf(3));
    }

    /**
     * Builds:
     *
     *              4
     *             / \
     *            2   6
     *           / \ / \
     *          1  3 5  7
     *
     * Inorder:  [1, 2, 3, 4, 5, 6, 7]
     * Preorder: [4, 2, 1, 3, 6, 5, 7]
     */
    static Node balancedTree() {

        return node(
                4,
                node(
                        2,
                        leaf(1),
                        leaf(3)),
                node(
                        6,
                        leaf(5),
                        leaf(7)));
    }

    /**
     * Builds a left-only chain:
     *
     *      1
     *     /
     *    2
     *   /
     *  3
     */
    static Node leftChain(int length) {

        Node curr = null;

        for (int value = length;
             value >= 1;
             value--) {

            Node next =
                    new Node(value);

            next.left = curr;

            curr = next;
        }

        return curr;
    }

    /**
     * Builds a right-only chain:
     *
     * 1
     *  \
     *   2
     *    \
     *     3
     */
    static Node rightChain(int length) {

        Node root = null;

        for (int value = length;
             value >= 1;
             value--) {

            root =
                    node(
                            value,
                            null,
                            root);
        }

        return root;
    }

    /* **********************************************************************
     * Traversal Utilities
     * **********************************************************************/

    static void inorderTraversal(
            Node root,
            List<Integer> result) {

        if (root == null) {
            return;
        }

        inorderTraversal(
                root.left,
                result);

        result.add(root.val);

        inorderTraversal(
                root.right,
                result);
    }

    static void preorderTraversal(
            Node root,
            List<Integer> result) {

        if (root == null) {
            return;
        }

        result.add(root.val);

        preorderTraversal(
                root.left,
                result);

        preorderTraversal(
                root.right,
                result);
    }

    static int[] inorderArray(Node root) {

        List<Integer> values =
                new ArrayList<>();

        inorderTraversal(
                root,
                values);

        return toIntArray(values);
    }

    static int[] preorderArray(Node root) {

        List<Integer> values =
                new ArrayList<>();

        preorderTraversal(
                root,
                values);

        return toIntArray(values);
    }

    static int[] toIntArray(
            List<Integer> values) {

        int[] result =
                new int[values.size()];

        for (int i = 0;
             i < values.size();
             i++) {

            result[i] =
                    values.get(i);
        }

        return result;
    }

    /* **********************************************************************
     * Tree Comparison Utilities
     * **********************************************************************/

    static boolean treesEqual(
            Node a,
            Node b) {

        if (a == null && b == null) {
            return true;
        }

        if (a == null || b == null) {
            return false;
        }

        return a.val == b.val
                && treesEqual(a.left, b.left)
                && treesEqual(a.right, b.right);
    }

    static String treeToString(Node root) {

        if (root == null) {
            return "null";
        }

        return "("
                + root.val
                + ",L="
                + treeToString(root.left)
                + ",R="
                + treeToString(root.right)
                + ")";
    }

    /* **********************************************************************
     * Array Utilities
     * **********************************************************************/

    static boolean arraysEqual(
            int[] a,
            int[] b) {

        if (a == null && b == null) {
            return true;
        }

        if (a == null || b == null) {
            return false;
        }

        if (a.length != b.length) {
            return false;
        }

        for (int i = 0;
             i < a.length;
             i++) {

            if (a[i] != b[i]) {
                return false;
            }
        }

        return true;
    }

    static String arrayToString(
            int[] array) {

        if (array == null) {
            return "null";
        }

        StringBuilder builder =
                new StringBuilder("[");

        for (int i = 0;
             i < array.length;
             i++) {

            if (i > 0) {
                builder.append(", ");
            }

            builder.append(array[i]);
        }

        builder.append("]");

        return builder.toString();
    }

    /* **********************************************************************
     * Result Comparison
     * **********************************************************************/

    static boolean resultsEqual(
            Result actual,
            Node expectedRoot,
            boolean expectedValid) {

        if (actual == null) {
            return false;
        }

        if (actual.valid() != expectedValid) {
            return false;
        }

        /*
         * For invalid input, the root is not part of the
         * successful result contract.
         */
        if (!expectedValid) {
            return true;
        }

        return treesEqual(
                actual.root(),
                expectedRoot);
    }

    /* **********************************************************************
     * Test Runner
     * **********************************************************************/

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
                        method.solve(
                                test.inorder,
                                test.preorder);

                if (resultsEqual(
                        actual,
                        test.expectedRoot,
                        test.expectedValid)) {

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
                            "  expected valid = %s%n",
                            test.expectedValid);

                    System.out.printf(
                            "  actual          = %s%n",
                            actual);

                    if (test.expectedValid) {

                        System.out.printf(
                                "  expected tree   = %s%n",
                                treeToString(
                                        test.expectedRoot));

                        if (actual != null) {

                            System.out.printf(
                                    "  actual tree     = %s%n",
                                    treeToString(
                                            actual.root()));
                        }
                    }
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

    /**
     * Generates a random binary tree containing unique values.
     *
     * The generated tree is used to create valid inorder and preorder
     * traversals. Both reconstruction algorithms must then recreate
     * the exact original tree.
     */
    static Node randomTree(
            Random rng,
            int maxNodes) {

        int nodeCount =
                rng.nextInt(maxNodes) + 1;

        List<Integer> values =
                new ArrayList<>();

        for (int i = 1;
             i <= nodeCount;
             i++) {

            values.add(i);
        }

        /*
         * Fisher-Yates shuffle.
         */
        for (int i = values.size() - 1;
             i > 0;
             i--) {

            int j =
                    rng.nextInt(i + 1);

            int temp =
                    values.get(i);

            values.set(
                    i,
                    values.get(j));

            values.set(
                    j,
                    temp);
        }

        return randomTreeFromValues(
                values,
                rng);
    }

    /**
     * Creates a random tree using the supplied unique values.
     */
    static Node randomTreeFromValues(
            List<Integer> values,
            Random rng) {

        if (values.isEmpty()) {
            return null;
        }

        /*
         * Select a random value as the root.
         */
        int rootIndex =
                rng.nextInt(values.size());

        int rootValue =
                values.get(rootIndex);

        List<Integer> leftValues =
                new ArrayList<>(
                        values.subList(
                                0,
                                rootIndex));

        List<Integer> rightValues =
                new ArrayList<>(
                        values.subList(
                                rootIndex + 1,
                                values.size()));

        Node root =
                new Node(rootValue);

        root.left =
                randomTreeFromValues(
                        leftValues,
                        rng);

        root.right =
                randomTreeFromValues(
                        rightValues,
                        rng);

        return root;
    }

    /**
     * Cross-checks both implementations against randomly generated
     * original trees.
     */
    static void runRandomisedTests(
            int iterations) {

        System.out.println(
                "======================================================");

        System.out.println(
                "Randomised Cross Checks");

        System.out.println(
                "======================================================");

        Random rng =
                new Random(20260915L);

        for (int i = 1;
             i <= iterations;
             i++) {

            Node original =
                    randomTree(
                            rng,
                            100);

            int[] inorder =
                    inorderArray(original);

            int[] preorder =
                    preorderArray(original);

            Result recursion =
                    constructBinaryTreePreOrder(
                            inorder,
                            preorder);

            Result hashMap =
                    constructBinaryTreePreOrderHashMap(
                            inorder,
                            preorder);

            boolean recursionCorrect =
                    recursion.valid()
                            && treesEqual(
                                    recursion.root(),
                                    original);

            boolean hashMapCorrect =
                    hashMap.valid()
                            && treesEqual(
                                    hashMap.root(),
                                    original);

            boolean sameValid =
                    recursion.valid()
                            == hashMap.valid();

            boolean sameTree =
                    treesEqual(
                            recursion.root(),
                            hashMap.root());

            if (!recursionCorrect
                    || !hashMapCorrect
                    || !sameValid
                    || !sameTree) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "Test number = " + i);

                System.out.println(
                        "Inorder  = "
                                + arrayToString(inorder));

                System.out.println(
                        "Preorder = "
                                + arrayToString(preorder));

                System.out.println(
                        "Original = "
                                + treeToString(original));

                System.out.println(
                        "Recursion = "
                                + recursion);

                System.out.println(
                        "HashMap = "
                                + hashMap);

                System.out.println(
                        "Recursion tree = "
                                + treeToString(
                                        recursion.root()));

                System.out.println(
                        "HashMap tree = "
                                + treeToString(
                                        hashMap.root()));

                return;
            }
        }

        System.out.printf(
                "All %d randomised tests passed.%n%n",
                iterations);
    }

    /* **********************************************************************
     * Main / Test Suite
     * **********************************************************************/

    public static void main(String[] args) {

        List<TestCase> tests =
                new ArrayList<>();

        /* ============================================================
         * Basic Cases
         * ============================================================ */

        tests.add(
                new TestCase(
                        "B1",
                        new int[]{1},
                        new int[]{1},
                        leaf(1),
                        true,
                        "single node"));

        tests.add(
                new TestCase(
                        "B2",
                        new int[]{2, 1},
                        new int[]{1, 2},
                        node(
                                1,
                                leaf(2),
                                null),
                        true,
                        "root with only a left child"));

        tests.add(
                new TestCase(
                        "B3",
                        new int[]{1, 2},
                        new int[]{1, 2},
                        node(
                                1,
                                null,
                                leaf(2)),
                        true,
                        "root with only a right child"));

        tests.add(
                new TestCase(
                        "B4",
                        new int[]{2, 1, 3},
                        new int[]{1, 2, 3},
                        node(
                                1,
                                leaf(2),
                                leaf(3)),
                        true,
                        "root with two children"));

        tests.add(
                new TestCase(
                        "B5",
                        new int[]{4, 2, 5, 1, 3},
                        new int[]{1, 2, 4, 5, 3},
                        standardExample(),
                        true,
                        "classic 5-node example"));

        tests.add(
                new TestCase(
                        "B6",
                        new int[]{1, 2, 3, 4, 5, 6, 7},
                        new int[]{4, 2, 1, 3, 6, 5, 7},
                        balancedTree(),
                        true,
                        "perfectly balanced 7-node tree"));

        /* ============================================================
         * Skewed / Unbalanced Trees
         * ============================================================ */

        Node leftChain5 =
                leftChain(5);

        tests.add(
                new TestCase(
                        "S1",
                        inorderArray(leftChain5),
                        preorderArray(leftChain5),
                        leftChain5,
                        true,
                        "5-node left-only chain"));

        Node rightChain5 =
                rightChain(5);

        tests.add(
                new TestCase(
                        "S2",
                        inorderArray(rightChain5),
                        preorderArray(rightChain5),
                        rightChain5,
                        true,
                        "5-node right-only chain"));

        /*
         *              1
         *             / \
         *            2   7
         *           / \
         *          3   6
         *         / \
         *        4   5
         */
        Node unbalancedTree =
                node(
                        1,
                        node(
                                2,
                                node(
                                        3,
                                        leaf(4),
                                        leaf(5)),
                                leaf(6)),
                        leaf(7));

        tests.add(
                new TestCase(
                        "S3",
                        inorderArray(unbalancedTree),
                        preorderArray(unbalancedTree),
                        unbalancedTree,
                        true,
                        "deeply unbalanced tree"));

        /* ============================================================
         * Edge / Invalid Cases
         * ============================================================ */

        tests.add(
                new TestCase(
                        "E1",
                        null,
                        null,
                        null,
                        false,
                        "null arrays"));

        tests.add(
                new TestCase(
                        "E2",
                        new int[]{},
                        new int[]{1},
                        null,
                        false,
                        "empty inorder"));

        tests.add(
                new TestCase(
                        "E3",
                        new int[]{1},
                        new int[]{},
                        null,
                        false,
                        "empty preorder"));

        tests.add(
                new TestCase(
                        "E4",
                        new int[]{1, 2},
                        new int[]{1},
                        null,
                        false,
                        "different array lengths"));

        tests.add(
                new TestCase(
                        "E5",
                        new int[]{1},
                        new int[]{2},
                        null,
                        false,
                        "different values"));

        tests.add(
                new TestCase(
                        "E6",
                        new int[]{1, 2, 3},
                        new int[]{1, 3, 2},
                        null,
                        false,
                        "valid traversals of a different tree"));

        tests.add(
                new TestCase(
                        "E7",
                        new int[]{1, 2, 2},
                        new int[]{2, 1, 2},
                        null,
                        false,
                        "duplicate value in inorder"));

        tests.add(
                new TestCase(
                        "E8",
                        new int[]{1, 2, 3},
                        new int[]{2, 2, 3},
                        null,
                        false,
                        "duplicate value in preorder"));

        tests.add(
                new TestCase(
                        "E9",
                        new int[]{1, 2, 3},
                        new int[]{1, 2, 4},
                        null,
                        false,
                        "preorder contains value absent from inorder"));

        tests.add(
                new TestCase(
                        "E10",
                        new int[]{1, 2, 4},
                        new int[]{1, 2, 3},
                        null,
                        false,
                        "inorder contains value absent from preorder"));

        /* ============================================================
         * Header
         * ============================================================ */

        System.out.println(
                "############################################################");

        System.out.println(
                "##############  CONSTRUCT BINARY TREE  ####################");

        System.out.println(
                "############################################################");

        System.out.println();

        /* ============================================================
         * Algorithms
         * ============================================================ */

        List<MethodCase> methods =
                List.of(

                        new MethodCase(
                                "Recursion - O(n^2)",
                                ConstructBinaryTree
                                        ::constructBinaryTreePreOrder),

                        new MethodCase(
                                "HashMap - O(n)",
                                ConstructBinaryTree
                                        ::constructBinaryTreePreOrderHashMap)
                );

        /*
         * Run fixed tests against each implementation.
         */
        for (MethodCase method : methods) {

            runTests(
                    method.name,
                    method.algorithm,
                    tests);
        }

        /* ============================================================
         * Randomised Cross-Check
         * ============================================================ */

        runRandomisedTests(5000);
    }
}
