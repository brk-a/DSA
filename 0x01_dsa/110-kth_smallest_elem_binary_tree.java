import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Find the kth smallest element in a Binary Search Tree (BST).
 *
 * Implementations:
 *
 * 1. Recursive In-order Traversal
 *      Time:  O(h + k) with early termination
 *      Space: O(h)
 *
 * 2. Morris In-order Traversal
 *      Time:  O(n)
 *      Space: O(1)
 *
 * In-order traversal of a BST visits values in sorted order:
 *
 *      left -> root -> right
 *
 * Therefore, the kth visited node is the kth smallest element.
 *
 * k is 1-based.
 *
 * Duplicate values are allowed.
 */
public class KthSmallestElementBinaryTree {

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
     * Result
     * **********************************************************************/

    static record Result(
            int element,
            boolean valid) {
    }

    /* **********************************************************************
     * Helpers
     * **********************************************************************/

    static boolean validNode(Node node) {
        return node != null;
    }

    static Node node(int value) {
        return new Node(value);
    }

    static Node node(
            int value,
            Node left,
            Node right) {

        return new Node(left, right, value);
    }

    /**
     * Validate BST ordering.
     *
     * Duplicates are allowed on either side:
     *
     *      left <= root <= right
     */
    static boolean isBinarySearchTree(Node root) {
        return isBinarySearchTree(
                root,
                Long.MIN_VALUE,
                Long.MAX_VALUE);
    }

    static boolean isBinarySearchTree(
            Node root,
            long min,
            long max) {

        if (root == null) {
            return true;
        }

        if (root.val < min
                || root.val > max) {

            return false;
        }

        return isBinarySearchTree(
                root.left,
                min,
                root.val)
                && isBinarySearchTree(
                root.right,
                root.val,
                max);
    }

    /* **********************************************************************
     * 1. Recursive In-order Traversal
     * **********************************************************************/

    static Result kthSmallestElementBinaryTreeInOrder(
            Node root,
            int k) {

        if (!validNode(root)
                || k <= 0) {

            return new Result(
                    -1,
                    false);
        }

        if (!isBinarySearchTree(root)) {

            return new Result(
                    -1,
                    false);
        }

        int[] count = {0};

        int element =
                kthSmallestRecursion(
                        root,
                        count,
                        k);

        if (element == -1) {

            return new Result(
                    -1,
                    false);
        }

        return new Result(
                element,
                true);
    }

    static int kthSmallestRecursion(
            Node root,
            int[] count,
            int k) {

        if (root == null) {
            return -1;
        }

        /*
         * Search left subtree.
         */
        int left =
                kthSmallestRecursion(
                        root.left,
                        count,
                        k);

        if (left != -1) {
            return left;
        }

        /*
         * Visit current node.
         */
        count[0]++;

        if (count[0] == k) {
            return root.val;
        }

        /*
         * Search right subtree.
         */
        return kthSmallestRecursion(
                root.right,
                count,
                k);
    }

    /* **********************************************************************
     * 2. Morris In-order Traversal
     * **********************************************************************/

    static Result kthSmallestElementBinaryTreeMorrisInOrder(
            Node root,
            int k) {

        if (!validNode(root)
                || k <= 0) {

            return new Result(
                    -1,
                    false);
        }

        if (!isBinarySearchTree(root)) {

            return new Result(
                    -1,
                    false);
        }

        int count = 0;
        Node current = root;

        int answer = -1;
        boolean found = false;

        while (current != null) {

            /*
             * No left subtree.
             */
            if (current.left == null) {

                count++;

                if (!found && count == k) {

                    answer = current.val;
                    found = true;
                }

                current = current.right;

                continue;
            }

            /*
             * Find inorder predecessor.
             */
            Node predecessor =
                    current.left;

            while (predecessor.right != null
                    && predecessor.right != current) {

                predecessor =
                        predecessor.right;
            }

            /*
             * First visit:
             * create temporary thread.
             */
            if (predecessor.right == null) {

                predecessor.right = current;
                current = current.left;

            } else {

                /*
                 * Second visit:
                 * remove temporary thread.
                 */
                predecessor.right = null;

                count++;

                if (!found && count == k) {

                    answer = current.val;
                    found = true;
                }

                current = current.right;
            }
        }

        if (!found) {

            return new Result(
                    -1,
                    false);
        }

        return new Result(
                answer,
                true);
    }

    /* **********************************************************************
     * Reference Implementation
     * **********************************************************************/

    /**
     * Simple inorder implementation used by the test suite.
     *
     * This provides an independent result against which both algorithms
     * are checked.
     */
    static Result kthSmallestReference(
            Node root,
            int k) {

        if (!validNode(root)
                || k <= 0
                || !isBinarySearchTree(root)) {

            return new Result(
                    -1,
                    false);
        }

        List<Integer> values =
                new ArrayList<>();

        collectInOrder(
                root,
                values);

        if (k > values.size()) {

            return new Result(
                    -1,
                    false);
        }

        return new Result(
                values.get(k - 1),
                true);
    }

    static void collectInOrder(
            Node root,
            List<Integer> values) {

        if (root == null) {
            return;
        }

        collectInOrder(
                root.left,
                values);

        values.add(root.val);

        collectInOrder(
                root.right,
                values);
    }

    /* **********************************************************************
     * Tree Comparison
     * **********************************************************************/

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

        return root1.val == root2.val
                && treesEqual(
                root1.left,
                root2.left)
                && treesEqual(
                root1.right,
                root2.right);
    }

    static Node cloneTree(Node root) {

        if (root == null) {
            return null;
        }

        return node(
                root.val,
                cloneTree(root.left),
                cloneTree(root.right));
    }

    /* **********************************************************************
     * Test Case
     * **********************************************************************/

    static class TestCase {

        final String id;
        final Node root;
        final int k;
        final int expected;
        final boolean expectedValid;
        final String description;

        TestCase(
                String id,
                Node root,
                int k,
                int expected,
                boolean expectedValid,
                String description) {

            this.id = id;
            this.root = root;
            this.k = k;
            this.expected = expected;
            this.expectedValid = expectedValid;
            this.description = description;
        }
    }

    /* **********************************************************************
     * Algorithm Abstraction
     * **********************************************************************/

    @FunctionalInterface
    interface KthSmallestAlgorithm {

        Result solve(
                Node root,
                int k);
    }

    static class MethodCase {

        final String name;
        final KthSmallestAlgorithm algorithm;

        MethodCase(
                String name,
                KthSmallestAlgorithm algorithm) {

            this.name = name;
            this.algorithm = algorithm;
        }
    }

    /* **********************************************************************
     * Test Runner
     * **********************************************************************/

    static void runTests(
            String algorithmName,
            KthSmallestAlgorithm algorithm,
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
                                test.root,
                                test.k);

                boolean pass =
                        actual.valid()
                                == test.expectedValid
                                && (!test.expectedValid
                                || actual.element()
                                == test.expected);

                if (pass) {

                    passed++;

                    System.out.printf(
                            "PASS %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  k = " + test.k);

                    System.out.println(
                            "  result = " + actual);

                } else {

                    failed++;

                    System.out.printf(
                            "FAIL %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  k = " + test.k);

                    System.out.println(
                            "  expected = "
                                    + new Result(
                                    test.expected,
                                    test.expectedValid));

                    System.out.println(
                            "  actual = "
                                    + actual);
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

    static void runInvalidInputTests() {

        System.out.println(
                "======================================================");

        System.out.println(
                "Invalid Input Tests");

        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        /*
         * Null root.
         */
        Result nullRoot =
                kthSmallestElementBinaryTreeInOrder(
                        null,
                        1);

        if (!nullRoot.valid()) {

            passed++;

            System.out.println(
                    "PASS I1 - null root rejected");

        } else {

            failed++;

            System.out.println(
                    "FAIL I1 - null root should be invalid");
        }

        /*
         * k == 0.
         */
        Result zeroK =
                kthSmallestElementBinaryTreeInOrder(
                        node(10),
                        0);

        if (!zeroK.valid()) {

            passed++;

            System.out.println(
                    "PASS I2 - k == 0 rejected");

        } else {

            failed++;

            System.out.println(
                    "FAIL I2 - k == 0 should be invalid");
        }

        /*
         * Negative k.
         */
        Result negativeK =
                kthSmallestElementBinaryTreeInOrder(
                        node(10),
                        -1);

        if (!negativeK.valid()) {

            passed++;

            System.out.println(
                    "PASS I3 - negative k rejected");

        } else {

            failed++;

            System.out.println(
                    "FAIL I3 - negative k should be invalid");
        }

        /*
         * k greater than number of nodes.
         */
        Node smallTree =
                node(
                        2,
                        node(1),
                        node(3));

        Result tooLargeK =
                kthSmallestElementBinaryTreeInOrder(
                        smallTree,
                        4);

        if (!tooLargeK.valid()) {

            passed++;

            System.out.println(
                    "PASS I4 - k greater than node count rejected");

        } else {

            failed++;

            System.out.println(
                    "FAIL I4 - excessive k should be invalid");
        }

        /*
         * Invalid BST.
         */
        Node invalidBst =
                node(
                        10,
                        node(20),
                        node(5));

        Result invalidBstResult =
                kthSmallestElementBinaryTreeInOrder(
                        invalidBst,
                        1);

        if (!invalidBstResult.valid()) {

            passed++;

            System.out.println(
                    "PASS I5 - invalid BST rejected");

        } else {

            failed++;

            System.out.println(
                    "FAIL I5 - invalid BST should be rejected");
        }

        /*
         * Invalid BST using Morris.
         */
        Result invalidBstMorris =
                kthSmallestElementBinaryTreeMorrisInOrder(
                        invalidBst,
                        1);

        if (!invalidBstMorris.valid()) {

            passed++;

            System.out.println(
                    "PASS I6 - Morris invalid BST rejected");

        } else {

            failed++;

            System.out.println(
                    "FAIL I6 - Morris invalid BST should be rejected");
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
     * Morris Restoration Tests
     * **********************************************************************/

    static void runMorrisRestorationTests() {

        System.out.println(
                "======================================================");

        System.out.println(
                "Morris Tree Restoration Tests");

        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        /*
         * Test 1:
         * Morris must restore the tree after finding a middle element.
         */
        Node original =
                node(
                        5,
                        node(
                                3,
                                node(2),
                                node(4)),
                        node(
                                7,
                                node(6),
                                node(8)));

        Node expected =
                cloneTree(original);

        Result result =
                kthSmallestElementBinaryTreeMorrisInOrder(
                        original,
                        4);

        if (result.valid()
                && result.element() == 5
                && treesEqual(
                original,
                expected)) {

            passed++;

            System.out.println(
                    "PASS M1 - Morris restores tree structure");

        } else {

            failed++;

            System.out.println(
                    "FAIL M1 - Morris did not restore tree structure");

            System.out.println(
                    "  result = " + result);
        }

        /*
         * Test 2:
         * The kth value can be the first element.
         *
         * This specifically catches implementations that return before
         * removing a temporary Morris thread.
         */
        original =
                node(
                        5,
                        node(
                                3,
                                node(2),
                                node(4)),
                        node(
                                7,
                                node(6),
                                node(8)));

        expected =
                cloneTree(original);

        result =
                kthSmallestElementBinaryTreeMorrisInOrder(
                        original,
                        1);

        if (result.valid()
                && result.element() == 2
                && treesEqual(
                original,
                expected)) {

            passed++;

            System.out.println(
                    "PASS M2 - Morris restores tree after first result");

        } else {

            failed++;

            System.out.println(
                    "FAIL M2 - Morris tree restoration failed");

            System.out.println(
                    "  result = " + result);
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
     * Random BST Generation
     * **********************************************************************/

    /**
     * Inserts into a BST.
     *
     * Duplicate values are inserted into the right subtree.
     */
    static Node insert(
            Node root,
            int value) {

        if (root == null) {
            return new Node(value);
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

    static Node randomBst(
            Random rng,
            int nodeCount) {

        Node root = null;

        for (int i = 0;
             i < nodeCount;
             i++) {

            int value =
                    rng.nextInt(1000) - 500;

            root =
                    insert(
                            root,
                            value);
        }

        return root;
    }

    /* **********************************************************************
     * Randomised Cross Checks
     * **********************************************************************/

    static void runRandomisedTests(
            int iterations) {

        System.out.println(
                "======================================================");

        System.out.println(
                "Randomised Cross Checks");

        System.out.println(
                "======================================================");

        Random rng =
                new Random(20260917L);

        int passed = 0;
        int failed = 0;

        for (int iteration = 1;
             iteration <= iterations;
             iteration++) {

            int nodeCount =
                    1 + rng.nextInt(50);

            Node root =
                    randomBst(
                            rng,
                            nodeCount);

            int k =
                    1 + rng.nextInt(nodeCount);

            Result expected =
                    kthSmallestReference(
                            root,
                            k);

            Result recursive =
                    kthSmallestElementBinaryTreeInOrder(
                            root,
                            k);

            Result morris =
                    kthSmallestElementBinaryTreeMorrisInOrder(
                            root,
                            k);

            boolean correct =
                    expected.equals(recursive)
                            && expected.equals(morris);

            /*
             * Morris must leave the original tree structurally intact.
             */
            boolean treeStillValid =
                    isBinarySearchTree(root);

            if (correct && treeStillValid) {

                passed++;

            } else {

                failed++;

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "iteration = " + iteration);

                System.out.println(
                        "nodeCount = " + nodeCount);

                System.out.println(
                        "k = " + k);

                System.out.println(
                        "expected = " + expected);

                System.out.println(
                        "recursive = " + recursive);

                System.out.println(
                        "morris = " + morris);

                System.out.println(
                        "tree valid after Morris = "
                                + treeStillValid);

                break;
            }
        }

        System.out.printf(
                "Results: %d passed, %d failed, %d total%n",
                passed,
                failed,
                passed + failed);

        System.out.println();
    }

    /* **********************************************************************
     * Main Test Suite
     * **********************************************************************/

    public static void main(String[] args) {

        List<TestCase> tests =
                new ArrayList<>();

        /*
         * ================================================================
         * Basic Cases
         * ================================================================
         */

        Node single =
                node(5);

        tests.add(
                new TestCase(
                        "B1",
                        single,
                        1,
                        5,
                        true,
                        "single node"));

        Node basic =
                node(
                        2,
                        node(1),
                        node(3));

        tests.add(
                new TestCase(
                        "B2",
                        basic,
                        1,
                        1,
                        true,
                        "three-node BST, smallest"));

        tests.add(
                new TestCase(
                        "B3",
                        basic,
                        2,
                        2,
                        true,
                        "three-node BST, middle"));

        tests.add(
                new TestCase(
                        "B4",
                        basic,
                        3,
                        3,
                        true,
                        "three-node BST, largest"));

        /*
         * ================================================================
         * Complete BST
         * ================================================================
         */

        Node complete =
                node(
                        4,
                        node(
                                2,
                                node(1),
                                node(3)),
                        node(
                                6,
                                node(5),
                                node(7)));

        tests.add(
                new TestCase(
                        "C1",
                        complete,
                        1,
                        1,
                        true,
                        "complete BST, first"));

        tests.add(
                new TestCase(
                        "C2",
                        complete,
                        4,
                        4,
                        true,
                        "complete BST, middle"));

        tests.add(
                new TestCase(
                        "C3",
                        complete,
                        7,
                        7,
                        true,
                        "complete BST, last"));

        /*
         * ================================================================
         * Left-skewed BST
         * ================================================================
         */

        Node leftSkewed =
                node(
                        4,
                        node(
                                3,
                                node(
                                        2,
                                        node(1),
                                        null),
                                null),
                        null);

        tests.add(
                new TestCase(
                        "A1",
                        leftSkewed,
                        1,
                        1,
                        true,
                        "left-skewed BST, smallest"));

        tests.add(
                new TestCase(
                        "A2",
                        leftSkewed,
                        4,
                        4,
                        true,
                        "left-skewed BST, largest"));

        /*
         * ================================================================
         * Right-skewed BST
         * ================================================================
         */

        Node rightSkewed =
                node(
                        1,
                        null,
                        node(
                                2,
                                null,
                                node(
                                        3,
                                        null,
                                        node(4))));

        tests.add(
                new TestCase(
                        "A3",
                        rightSkewed,
                        1,
                        1,
                        true,
                        "right-skewed BST, smallest"));

        tests.add(
                new TestCase(
                        "A4",
                        rightSkewed,
                        4,
                        4,
                        true,
                        "right-skewed BST, largest"));

        /*
         * ================================================================
         * Duplicate Values
         * ================================================================
         */

        Node duplicates =
                node(
                        5,
                        node(
                                5,
                                node(5),
                                null),
                        node(
                                5,
                                null,
                                node(5)));

        tests.add(
                new TestCase(
                        "D1",
                        duplicates,
                        1,
                        5,
                        true,
                        "duplicate values, first"));

        tests.add(
                new TestCase(
                        "D2",
                        duplicates,
                        3,
                        5,
                        true,
                        "duplicate values, middle"));

        tests.add(
                new TestCase(
                        "D3",
                        duplicates,
                        5,
                        5,
                        true,
                        "duplicate values, last"));

        /*
         * ================================================================
         * Negative Values
         * ================================================================
         */

        Node negative =
                node(
                        -1,
                        node(-5),
                        node(
                                3,
                                node(0),
                                node(8)));

        tests.add(
                new TestCase(
                        "N1",
                        negative,
                        1,
                        -5,
                        true,
                        "negative and positive values"));

        tests.add(
                new TestCase(
                        "N2",
                        negative,
                        3,
                        0,
                        true,
                        "negative and positive values, middle"));

        tests.add(
                new TestCase(
                        "N3",
                        negative,
                        5,
                        8,
                        true,
                        "negative and positive values, largest"));

        /*
         * ================================================================
         * Integer Boundary Values
         * ================================================================
         */

        Node integerBounds =
                node(
                        0,
                        node(Integer.MIN_VALUE),
                        node(Integer.MAX_VALUE));

        tests.add(
                new TestCase(
                        "V1",
                        integerBounds,
                        1,
                        Integer.MIN_VALUE,
                        true,
                        "Integer.MIN_VALUE"));

        tests.add(
                new TestCase(
                        "V2",
                        integerBounds,
                        3,
                        Integer.MAX_VALUE,
                        true,
                        "Integer.MAX_VALUE"));

        /*
         * ================================================================
         * Invalid k
         * ================================================================
         */

        tests.add(
                new TestCase(
                        "I1",
                        basic,
                        0,
                        -1,
                        false,
                        "k equals zero"));

        tests.add(
                new TestCase(
                        "I2",
                        basic,
                        -1,
                        -1,
                        false,
                        "negative k"));

        tests.add(
                new TestCase(
                        "I3",
                        basic,
                        4,
                        -1,
                        false,
                        "k greater than node count"));

        /*
         * ================================================================
         * Header
         * ================================================================
         */

        System.out.println(
                "############################################################");

        System.out.println(
                "##########  KTH SMALLEST ELEMENT IN BST  ###################");

        System.out.println(
                "############################################################");

        System.out.println();

        /*
         * ================================================================
         * Algorithms
         * ================================================================
         */

        List<MethodCase> methods =
                List.of(

                        new MethodCase(
                                "Recursive In-order",
                                KthSmallestElementBinaryTree
                                        ::kthSmallestElementBinaryTreeInOrder),

                        new MethodCase(
                                "Morris In-order",
                                KthSmallestElementBinaryTree
                                        ::kthSmallestElementBinaryTreeMorrisInOrder)
                );

        /*
         * ================================================================
         * Deterministic Tests
         * ================================================================
         */

        for (MethodCase method : methods) {

            runTests(
                    method.name,
                    method.algorithm,
                    tests);
        }

        /*
         * ================================================================
         * Invalid Input Tests
         * ================================================================
         */

        runInvalidInputTests();

        /*
         * ================================================================
         * Morris Restoration Tests
         * ================================================================
         */

        runMorrisRestorationTests();

        /*
         * ================================================================
         * Randomised Cross Checks
         * ================================================================
         */

        runRandomisedTests(5000);
    }
}
