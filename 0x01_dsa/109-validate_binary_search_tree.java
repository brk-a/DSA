import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Validate whether a binary tree is a Binary Search Tree (BST).
 *
 * BST definition used by this implementation:
 *
 *      left subtree values  < node value
 *      right subtree values > node value
 *
 * Duplicate values are therefore NOT allowed.
 *
 * Implementations:
 *
 * 1. Min/Max recursion
 *      Time:  O(n)
 *      Space: O(h) call stack
 *
 * 2. In-order traversal
 *      Time:  O(n)
 *      Space: O(h) call stack
 *
 * 3. Morris in-order traversal
 *      Time:  O(n)
 *      Space: O(1) auxiliary space
 *
 * where:
 *
 *      n = number of nodes
 *      h = height of tree
 *
 * Result:
 *
 *      isBinaryTree == true
 *          root is non-null and validation was performed
 *
 *      isBinaryTree == false
 *          root is null
 *
 *      valid == true
 *          tree satisfies the BST rules
 *
 *      valid == false
 *          tree is not a valid BST
 */
public class ValidateBinarySearchTree {

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
            boolean isBinaryTree,
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

    static Node node(
            int value,
            Node left,
            Node right) {

        return new Node(value, left, right);
    }

    /**
     * Creates a valid result for a non-null root.
     */
    static Result validTree(boolean valid) {
        return new Result(true, valid);
    }

    /**
     * Creates the result for a null root.
     *
     * A null root is treated as invalid input for this API.
     */
    static Result invalidRoot() {
        return new Result(false, false);
    }

    /* **********************************************************************
     * 1. Min / Max Recursive Implementation
     * **********************************************************************/

    /**
     * Validates a BST using a minimum/maximum range.
     *
     * long is deliberately used for the bounds so that Integer.MIN_VALUE
     * and Integer.MAX_VALUE can both be valid node values without overflow.
     */
    static Result validateBinarySearchTreeMinMax(Node root) {

        if (!validRoot(root)) {
            return invalidRoot();
        }

        boolean valid = isBSTMinMax(
                root,
                Long.MIN_VALUE,
                Long.MAX_VALUE);

        return validTree(valid);
    }

    static boolean isBSTMinMax(
            Node root,
            long min,
            long max) {

        if (root == null) {
            return true;
        }

        /*
         * The valid range is exclusive:
         *
         *      min < root.val < max
         *
         * This naturally rejects duplicate values.
         */
        if (root.val <= min || root.val >= max) {
            return false;
        }

        return isBSTMinMax(
                root.left,
                min,
                root.val)
                && isBSTMinMax(
                root.right,
                root.val,
                max);
    }

    /* **********************************************************************
     * 2. In-Order Recursive Implementation
     * **********************************************************************/

    /**
     * A valid BST produces values in strictly increasing order
     * during in-order traversal.
     */
    static Result validateBinarySearchTreeInOrderTraversal(Node root) {

        if (!validRoot(root)) {
            return invalidRoot();
        }

        /*
         * hasPrevious is required because Integer.MIN_VALUE is a legitimate
         * node value and therefore cannot safely be used as a sentinel.
         */
        long[] previous = new long[1];
        boolean[] hasPrevious = new boolean[1];

        boolean valid = isBSTInOrder(
                root,
                previous,
                hasPrevious);

        return validTree(valid);
    }

    static boolean isBSTInOrder(
            Node root,
            long[] previous,
            boolean[] hasPrevious) {

        if (root == null) {
            return true;
        }

        if (!isBSTInOrder(
                root.left,
                previous,
                hasPrevious)) {

            return false;
        }

        if (hasPrevious[0]
                && root.val <= previous[0]) {

            return false;
        }

        previous[0] = root.val;
        hasPrevious[0] = true;

        return isBSTInOrder(
                root.right,
                previous,
                hasPrevious);
    }

    /* **********************************************************************
     * 3. Morris In-Order Traversal
     * **********************************************************************/

    /**
     * Validates a BST using Morris traversal.
     *
     * Morris traversal temporarily modifies right pointers to create
     * threads and restores every modified pointer before continuing.
     *
     * Auxiliary space: O(1)
     */
    static Result validateBinarySearchTreeMorrisTraversal(Node root) {

        if (!validRoot(root)) {
            return invalidRoot();
        }

        Node current = root;

        long previous = 0;
        boolean hasPrevious = false;

        boolean valid = true;

        while (current != null) {

            /*
             * Case 1:
             *
             * No left subtree.
             * Visit current immediately.
             */
            if (current.left == null) {

                if (hasPrevious
                        && current.val <= previous) {

                    valid = false;
                }

                previous = current.val;
                hasPrevious = true;

                current = current.right;

            } else {

                /*
                 * Find the rightmost node in the left subtree.
                 */
                Node predecessor = current.left;

                while (predecessor.right != null
                        && predecessor.right != current) {

                    predecessor = predecessor.right;
                }

                /*
                 * First visit to current:
                 *
                 * Create a temporary thread back to current.
                 */
                if (predecessor.right == null) {

                    predecessor.right = current;
                    current = current.left;

                } else {

                    /*
                     * Second visit to current:
                     *
                     * Remove the temporary thread before processing
                     * the current node.
                     */
                    predecessor.right = null;

                    if (hasPrevious
                            && current.val <= previous) {

                        valid = false;
                    }

                    previous = current.val;
                    hasPrevious = true;

                    current = current.right;
                }
            }
        }

        return validTree(valid);
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

    static boolean resultsEqual(
            Result actual,
            Result expected) {

        if (actual == null || expected == null) {
            return actual == expected;
        }

        return actual.isBinaryTree()
                == expected.isBinaryTree()
                && actual.valid()
                == expected.valid();
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

                if (resultsEqual(
                        actual,
                        test.expected)) {

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
     * Values are deliberately allowed to include the complete int range.
     */
    static Node randomTree(
            Random rng,
            int depth,
            double nodeProbability) {

        if (depth == 0
                || rng.nextDouble() > nodeProbability) {

            return null;
        }

        int value = rng.nextInt();

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
     * Builds a valid BST from sorted values.
     *
     * This is useful for generating randomised valid BSTs.
     */
    static Node buildBalancedBST(
            int[] values,
            int low,
            int high) {

        if (low > high) {
            return null;
        }

        int middle =
                low + (high - low) / 2;

        return node(
                values[middle],
                buildBalancedBST(
                        values,
                        low,
                        middle - 1),
                buildBalancedBST(
                        values,
                        middle + 1,
                        high));
    }

    /**
     * Creates an array containing unique sorted values.
     *
     * The values are chosen from a safe range so that sorting is simple
     * and duplicates are avoided.
     */
    static int[] sortedUniqueValues(
            Random rng,
            int size) {

        int[] values = new int[size];

        /*
         * Start with unique values and shuffle them before sorting.
         */
        for (int i = 0; i < size; i++) {
            values[i] = i * 3 - 1000;
        }

        for (int i = size - 1; i > 0; i--) {

            int j = rng.nextInt(i + 1);

            int temp = values[i];
            values[i] = values[j];
            values[j] = temp;
        }

        java.util.Arrays.sort(values);

        return values;
    }

    /**
     * Mutates one node in a tree.
     *
     * Used to turn a valid BST into an invalid tree.
     */
    static boolean mutateFirstNode(
            Node root,
            int newValue) {

        if (root == null) {
            return false;
        }

        root.val = newValue;
        return true;
    }

    /**
     * Counts the number of nodes in a tree.
     */
    static int countNodes(Node root) {

        if (root == null) {
            return 0;
        }

        return 1
                + countNodes(root.left)
                + countNodes(root.right);
    }

    /* **********************************************************************
     * Independent Expected Implementation
     * **********************************************************************/

    /**
     * Independent BST validator used by the randomised tests.
     *
     * This deliberately uses a different strategy from all three
     * implementations under test.
     *
     * It collects the in-order traversal into a list and then checks
     * whether the values are strictly increasing.
     */
    static Result expectedBSTValidation(Node root) {

        if (!validRoot(root)) {
            return invalidRoot();
        }

        ArrayList<Integer> values =
                new ArrayList<>();

        collectInOrder(
                root,
                values);

        for (int i = 1; i < values.size(); i++) {

            if (values.get(i) <= values.get(i - 1)) {
                return validTree(false);
            }
        }

        return validTree(true);
    }

    static void collectInOrder(
            Node root,
            ArrayList<Integer> values) {

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
                new Random(20260916L);

        for (int i = 1; i <= iterations; i++) {

            Node root = randomTree(
                    rng,
                    8,
                    0.75);

            /*
             * Use copies because the Morris implementation temporarily
             * modifies the tree while traversing it.
             */
            Node minMaxTree =
                    copyTree(root);

            Node inOrderTree =
                    copyTree(root);

            Node morrisTree =
                    copyTree(root);

            Result minMax =
                    validateBinarySearchTreeMinMax(
                            minMaxTree);

            Result inOrder =
                    validateBinarySearchTreeInOrderTraversal(
                            inOrderTree);

            Result morris =
                    validateBinarySearchTreeMorrisTraversal(
                            morrisTree);

            Result expected =
                    expectedBSTValidation(root);

            boolean allEqual =
                    resultsEqual(
                            minMax,
                            expected)
                    && resultsEqual(
                            inOrder,
                            expected)
                    && resultsEqual(
                            morris,
                            expected);

            if (!allEqual) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "iteration = " + i);

                System.out.println(
                        "nodes     = "
                                + countNodes(root));

                System.out.println(
                        "min/max   = " + minMax);

                System.out.println(
                        "in-order  = " + inOrder);

                System.out.println(
                        "morris    = " + morris);

                System.out.println(
                        "expected  = " + expected);

                return;
            }
        }

        System.out.printf(
                "All %d randomised tests passed.%n%n",
                iterations);
    }

    /* **********************************************************************
     * Boundary / Regression Tests
     * **********************************************************************/

    static void runBoundaryTests() {

        System.out.println(
                "======================================================");

        System.out.println(
                "Boundary / Regression Tests");

        System.out.println(
                "======================================================");

        List<TestCase> tests =
                new ArrayList<>();

        /*
         * Integer.MIN_VALUE must be allowed.
         */
        tests.add(new TestCase(
                "R1",
                node(
                        Integer.MIN_VALUE,
                        null,
                        node(-1)),
                validTree(true),
                "Integer.MIN_VALUE as root"));

        /*
         * Integer.MAX_VALUE must be allowed.
         */
        tests.add(new TestCase(
                "R2",
                node(
                        Integer.MAX_VALUE,
                        node(1),
                        null),
                validTree(true),
                "Integer.MAX_VALUE as root"));

        /*
         * Both boundaries in the same valid tree.
         */
        tests.add(new TestCase(
                "R3",
                node(
                        0,
                        node(Integer.MIN_VALUE),
                        node(Integer.MAX_VALUE)),
                validTree(true),
                "both integer boundaries"));

        /*
         * Duplicate at the root/left child.
         */
        tests.add(new TestCase(
                "R4",
                node(
                        5,
                        node(5),
                        null),
                validTree(false),
                "duplicate on left"));

        /*
         * Duplicate at the root/right child.
         */
        tests.add(new TestCase(
                "R5",
                node(
                        5,
                        null,
                        node(5)),
                validTree(false),
                "duplicate on right"));

        /*
         * Invalid ancestor relationship:
         *
         *         10
         *        /  \
         *       5    15
         *           /
         *          7
         *
         * 7 is less than 10 but is located in the right subtree.
         */
        tests.add(new TestCase(
                "R6",
                node(
                        10,
                        node(5),
                        node(
                                15,
                                node(7),
                                null)),
                validTree(false),
                "right subtree violates root boundary"));

        /*
         * Invalid ancestor relationship on the left side:
         *
         *         10
         *        /
         *       5
         *        \
         *        12
         *
         * 12 is greater than 10 but is located in the left subtree.
         */
        tests.add(new TestCase(
                "R7",
                node(
                        10,
                        node(
                                5,
                                null,
                                node(12)),
                        null),
                validTree(false),
                "left subtree violates root boundary"));

        runAllMethods(
                tests,
                "Boundary / Regression");
    }

    /* **********************************************************************
     * Test Suite Helpers
     * **********************************************************************/

    static void runAllMethods(
            List<TestCase> tests,
            String suiteName) {

        System.out.println();

        System.out.println(
                "############################################################");

        System.out.println(
                "############  " + suiteName);

        System.out.println(
                "############################################################");

        System.out.println();

        List<MethodCase> methods =
                List.of(

                        new MethodCase(
                                "Min / Max Recursion",
                                ValidateBinarySearchTree
                                        ::validateBinarySearchTreeMinMax),

                        new MethodCase(
                                "In-Order Recursion",
                                ValidateBinarySearchTree
                                        ::validateBinarySearchTreeInOrderTraversal),

                        new MethodCase(
                                "Morris In-Order",
                                ValidateBinarySearchTree
                                        ::validateBinarySearchTreeMorrisTraversal)
                );

        for (MethodCase method : methods) {

            runTests(
                    method.name,
                    method.algorithm,
                    tests);
        }
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
                invalidRoot(),
                "null root"));

        tests.add(new TestCase(
                "B2",
                node(1),
                validTree(true),
                "single node"));

        tests.add(new TestCase(
                "B3",
                node(
                        2,
                        node(1),
                        node(3)),
                validTree(true),
                "valid root with two children"));

        /*
         * ============================================================
         * Valid BSTs
         * ============================================================
         */

        tests.add(new TestCase(
                "V1",
                node(
                        10,
                        node(
                                5,
                                node(2),
                                node(7)),
                        node(
                                15,
                                node(12),
                                node(20))),
                validTree(true),
                "balanced valid BST"));

        tests.add(new TestCase(
                "V2",
                node(
                        10,
                        node(
                                5,
                                node(2),
                                null),
                        null),
                validTree(true),
                "left-skewed valid BST"));

        tests.add(new TestCase(
                "V3",
                node(
                        10,
                        null,
                        node(
                                15,
                                null,
                                node(20))),
                validTree(true),
                "right-skewed valid BST"));

        tests.add(new TestCase(
                "V4",
                node(
                        0,
                        node(Integer.MIN_VALUE),
                        node(Integer.MAX_VALUE)),
                validTree(true),
                "valid BST using integer boundaries"));

        tests.add(new TestCase(
                "V5",
                node(
                        Integer.MIN_VALUE,
                        null,
                        node(-1)),
                validTree(true),
                "Integer.MIN_VALUE with valid right subtree"));

        tests.add(new TestCase(
                "V6",
                node(
                        Integer.MAX_VALUE,
                        node(1),
                        null),
                validTree(true),
                "Integer.MAX_VALUE with valid left subtree"));

        /*
         * ============================================================
         * Invalid BSTs
         * ============================================================
         */

        tests.add(new TestCase(
                "I1",
                node(
                        10,
                        node(15),
                        node(20)),
                validTree(false),
                "left child greater than root"));

        tests.add(new TestCase(
                "I2",
                node(
                        10,
                        node(5),
                        node(7)),
                validTree(false),
                "right child smaller than root"));

        tests.add(new TestCase(
                "I3",
                node(
                        10,
                        node(
                                5,
                                null,
                                node(12)),
                        null),
                validTree(false),
                "left subtree contains value greater than root"));

        tests.add(new TestCase(
                "I4",
                node(
                        10,
                        null,
                        node(
                                15,
                                node(7),
                                null)),
                validTree(false),
                "right subtree contains value smaller than root"));

        /*
         * ============================================================
         * Duplicate Values
         * ============================================================
         */

        tests.add(new TestCase(
                "D1",
                node(
                        5,
                        node(5),
                        node(10)),
                validTree(false),
                "duplicate on left"));

        tests.add(new TestCase(
                "D2",
                node(
                        5,
                        node(1),
                        node(5)),
                validTree(false),
                "duplicate on right"));

        tests.add(new TestCase(
                "D3",
                node(
                        5,
                        node(
                                3,
                                node(3),
                                null),
                        node(7)),
                validTree(false),
                "duplicate deeper in left subtree"));

        tests.add(new TestCase(
                "D4",
                node(
                        5,
                        node(3),
                        node(
                                7,
                                null,
                                node(7))),
                validTree(false),
                "duplicate deeper in right subtree"));

        /*
         * ============================================================
         * Negative Values
         * ============================================================
         */

        tests.add(new TestCase(
                "N1",
                node(
                        -10,
                        node(-20),
                        node(-5)),
                validTree(true),
                "negative values"));

        tests.add(new TestCase(
                "N2",
                node(
                        0,
                        node(-10),
                        node(10)),
                validTree(true),
                "negative and positive values"));

        tests.add(new TestCase(
                "N3",
                node(
                        -10,
                        node(-20),
                        node(-30)),
                validTree(false),
                "invalid negative-value ordering"));

        /*
         * ============================================================
         * More Complex Invalid Trees
         * ============================================================
         */

        tests.add(new TestCase(
                "C1",
                node(
                        20,
                        node(
                                10,
                                node(5),
                                node(15)),
                        node(
                                30,
                                node(25),
                                node(35))),
                validTree(true),
                "larger balanced BST"));

        tests.add(new TestCase(
                "C2",
                node(
                        20,
                        node(
                                10,
                                node(5),
                                node(25)),
                        node(30)),
                validTree(false),
                "deep left subtree violates root boundary"));

        tests.add(new TestCase(
                "C3",
                node(
                        20,
                        node(10),
                        node(
                                30,
                                node(15),
                                node(40))),
                validTree(false),
                "deep right subtree violates root boundary"));

        /*
         * ============================================================
         * Run Deterministic Tests
         * ============================================================
         */

        runAllMethods(
                tests,
                "Deterministic Tests");

        /*
         * ============================================================
         * Boundary Regression Tests
         * ============================================================
         */

        runBoundaryTests();

        /*
         * ============================================================
         * Randomised Cross Checks
         * ============================================================
         */

        runRandomisedTests(5000);

        /*
         * ============================================================
         * Randomised Valid BST Checks
         * ============================================================
         */

        runRandomisedValidBSTTests(5000);

        /*
         * ============================================================
         * Morris Tree Restoration Check
         * ============================================================
         */

        runMorrisRestorationTest();
    }

    /* **********************************************************************
     * Randomised Valid BST Tests
     * **********************************************************************/

    static void runRandomisedValidBSTTests(
            int iterations) {

        System.out.println(
                "======================================================");

        System.out.println(
                "Randomised Valid BST Tests");

        System.out.println(
                "======================================================");

        Random rng =
                new Random(20260916L + 1);

        for (int i = 1; i <= iterations; i++) {

            int size =
                    1 + rng.nextInt(100);

            int[] values =
                    sortedUniqueValues(
                            rng,
                            size);

            Node root =
                    buildBalancedBST(
                            values,
                            0,
                            values.length - 1);

            Result minMax =
                    validateBinarySearchTreeMinMax(
                            copyTree(root));

            Result inOrder =
                    validateBinarySearchTreeInOrderTraversal(
                            copyTree(root));

            Result morris =
                    validateBinarySearchTreeMorrisTraversal(
                            copyTree(root));

            Result expected =
                    validTree(true);

            if (!resultsEqual(
                    minMax,
                    expected)
                    || !resultsEqual(
                    inOrder,
                    expected)
                    || !resultsEqual(
                    morris,
                    expected)) {

                System.out.println(
                        "Randomised valid BST test FAILED");

                System.out.println(
                        "iteration = " + i);

                System.out.println(
                        "size      = " + size);

                System.out.println(
                        "min/max   = " + minMax);

                System.out.println(
                        "in-order  = " + inOrder);

                System.out.println(
                        "morris    = " + morris);

                System.out.println(
                        "expected  = " + expected);

                return;
            }
        }

        System.out.printf(
                "All %d randomised valid BST tests passed.%n%n",
                iterations);
    }

    /* **********************************************************************
     * Morris Restoration Test
     * **********************************************************************/

    /**
     * Verifies that Morris traversal does not leave any temporary
     * threaded pointers behind.
     */
    static void runMorrisRestorationTest() {

        System.out.println(
                "======================================================");

        System.out.println(
                "Morris Tree Restoration Test");

        System.out.println(
                "======================================================");

        Node root =
                node(
                        10,
                        node(
                                5,
                                node(2),
                                node(7)),
                        node(
                                15,
                                node(12),
                                node(20)));

        /*
         * Keep references to all nodes whose right pointers should
         * remain unchanged.
         */
        Node n5 = root.left;
        Node n15 = root.right;

        Node n2 = n5.left;
        Node n7 = n5.right;

        Node n12 = n15.left;
        Node n20 = n15.right;

        Result result =
                validateBinarySearchTreeMorrisTraversal(
                        root);

        boolean restored =
                n2.right == null
                        && n7.right == null
                        && n12.right == null
                        && n20.right == null;

        boolean valid =
                result.isBinaryTree()
                        && result.valid();

        if (valid && restored) {

            System.out.println(
                    "PASS Morris traversal validated the tree "
                            + "and restored all temporary links.");

        } else {

            System.out.println(
                    "FAIL Morris traversal restoration test.");

            System.out.println(
                    "result   = " + result);

            System.out.println(
                    "restored = " + restored);
        }

        System.out.println();
    }
}
