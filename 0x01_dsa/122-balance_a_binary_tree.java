import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Balance a Binary Search Tree (BST).
 *
 * <p>The algorithm is:</p>
 *
 * <ol>
 *     <li>Traverse the BST in-order to obtain its values in sorted order.</li>
 *     <li>Recursively choose the middle value as the root.</li>
 *     <li>Repeat for the left and right portions of the sorted values.</li>
 * </ol>
 *
 * <p>Example:</p>
 *
 * <pre>
 *             1
 *              \
 *               2
 *                \
 *                 3
 *                  \
 *                   4
 *
 * becomes approximately:
 *
 *                 2
 *               /   \
 *              1     3
 *                     \
 *                      4
 * </pre>
 *
 * <p>Complexity:</p>
 *
 * <ul>
 *     <li>Time: O(n)</li>
 *     <li>Auxiliary space: O(n)</li>
 *     <li>Rebuilt-tree recursion depth: O(log n)</li>
 * </ul>
 *
 * <p>Precondition: the input tree must be a valid BST.</p>
 */
public class BalanceBST {

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
     * Public Algorithm
     * **********************************************************************/

    /**
     * Balances a binary search tree.
     *
     * <p>The original tree is not modified. A new balanced tree is
     * constructed containing the same values.</p>
     *
     * @param root root of the BST
     * @return root of the new balanced BST, or null when root is null
     */
    static Node balanceBST(Node root) {

        if (root == null) {
            return null;
        }

        List<Integer> values = new ArrayList<>();

        storeInOrder(root, values);

        return buildBalancedTree(
                values,
                0,
                values.size() - 1);
    }

    /**
     * Stores BST values in sorted order using iterative in-order
     * traversal.
     *
     * <p>Using an explicit stack avoids recursive traversal of a
     * potentially very deep input tree.</p>
     */
    static void storeInOrder(
            Node root,
            List<Integer> values) {

        ArrayDeque<Node> stack = new ArrayDeque<>();
        Node current = root;

        while (current != null || !stack.isEmpty()) {

            /*
             * Travel as far left as possible.
             */
            while (current != null) {
                stack.push(current);
                current = current.left;
            }

            /*
             * Visit the next smallest value.
             */
            current = stack.pop();

            values.add(current.val);

            /*
             * Continue through the right subtree.
             */
            current = current.right;
        }
    }

    /**
     * Builds a balanced BST from a sorted list.
     */
    static Node buildBalancedTree(
            List<Integer> values,
            int start,
            int end) {

        if (start > end) {
            return null;
        }

        /*
         * Avoid start + end overflowing an int.
         */
        int mid =
                start + (end - start) / 2;

        Node root =
                new Node(values.get(mid));

        root.left =
                buildBalancedTree(
                        values,
                        start,
                        mid - 1);

        root.right =
                buildBalancedTree(
                        values,
                        mid + 1,
                        end);

        return root;
    }

    /* **********************************************************************
     * Node Helpers
     * **********************************************************************/

    static Node node(int value) {
        return new Node(value);
    }

    static Node node(
            int value,
            Node left,
            Node right) {

        return new Node(left, right, value);
    }

    /* **********************************************************************
     * Traversal Helpers
     * **********************************************************************/

    /**
     * Returns the in-order values of a tree.
     */
    static List<Integer> inOrder(Node root) {

        List<Integer> values = new ArrayList<>();

        storeInOrder(root, values);

        return values;
    }

    /**
     * Returns true when two trees have exactly the same structure
     * and values.
     */
    static boolean treesEqual(
            Node first,
            Node second) {

        if (first == null && second == null) {
            return true;
        }

        if (first == null || second == null) {
            return false;
        }

        return first.val == second.val
                && treesEqual(
                        first.left,
                        second.left)
                && treesEqual(
                        first.right,
                        second.right);
    }

    /* **********************************************************************
     * BST Validation
     * **********************************************************************/

    /**
     * Checks whether a tree is a valid BST using strict ordering:
     *
     * left values  < node value < right values
     *
     * Duplicate values are therefore not considered valid BST values
     * by this helper.
     */
    static boolean isValidBST(Node root) {
        return isValidBST(
                root,
                null,
                null);
    }

    static boolean isValidBST(
            Node root,
            Integer min,
            Integer max) {

        if (root == null) {
            return true;
        }

        if (min != null && root.val <= min) {
            return false;
        }

        if (max != null && root.val >= max) {
            return false;
        }

        return isValidBST(
                        root.left,
                        min,
                        root.val)
                && isValidBST(
                        root.right,
                        root.val,
                        max);
    }

    /* **********************************************************************
     * Balance Validation
     * **********************************************************************/

    /**
     * Returns the height of a tree.
     *
     * An empty tree has height 0.
     */
    static int height(Node root) {

        if (root == null) {
            return 0;
        }

        return 1 + Math.max(
                height(root.left),
                height(root.right));
    }

    /**
     * Returns true when every node has left/right subtree heights
     * differing by at most one.
     */
    static boolean isBalanced(Node root) {

        return balancedHeight(root) != -1;
    }

    /**
     * Returns the height when balanced.
     * Returns -1 as a sentinel when an imbalance is found.
     */
    static int balancedHeight(Node root) {

        if (root == null) {
            return 0;
        }

        int leftHeight =
                balancedHeight(root.left);

        if (leftHeight == -1) {
            return -1;
        }

        int rightHeight =
                balancedHeight(root.right);

        if (rightHeight == -1) {
            return -1;
        }

        if (Math.abs(
                leftHeight - rightHeight) > 1) {

            return -1;
        }

        return 1 + Math.max(
                leftHeight,
                rightHeight);
    }

    /* **********************************************************************
     * Test Infrastructure
     * **********************************************************************/

    static class TestCase {

        final String id;
        final Node root;
        final String description;

        TestCase(
                String id,
                Node root,
                String description) {

            this.id = id;
            this.root = root;
            this.description = description;
        }
    }

    static class TestResults {

        int passed;
        int failed;

        void pass() {
            passed++;
        }

        void fail() {
            failed++;
        }

        int total() {
            return passed + failed;
        }
    }

    /* **********************************************************************
     * Assertions
     * **********************************************************************/

    static void assertTrue(
            boolean condition,
            String message) {

        if (!condition) {
            throw new AssertionError(message);
        }
    }

    static void assertFalse(
            boolean condition,
            String message) {

        assertTrue(
                !condition,
                message);
    }

    static void assertEquals(
            Object expected,
            Object actual,
            String message) {

        if (expected == null
                ? actual != null
                : !expected.equals(actual)) {

            throw new AssertionError(
                    message
                            + " | expected="
                            + expected
                            + ", actual="
                            + actual);
        }
    }

    /* **********************************************************************
     * Single Balance Test
     * **********************************************************************/

    static void runBalanceTest(
            TestCase test,
            TestResults results) {

        try {

            Node balanced =
                    balanceBST(test.root);

            List<Integer> originalValues =
                    inOrder(test.root);

            List<Integer> balancedValues =
                    inOrder(balanced);

            /*
             * The same values must be present.
             */
            assertEquals(
                    originalValues,
                    balancedValues,
                    "in-order values changed");

            /*
             * The result must be a valid BST for the
             * strict-BST test cases.
             */
            assertTrue(
                    isValidBST(balanced),
                    "result is not a valid BST");

            /*
             * The resulting tree must be balanced.
             */
            assertTrue(
                    isBalanced(balanced),
                    "result is not balanced");

            /*
             * A non-null input must produce a non-null result.
             */
            if (test.root != null) {

                assertTrue(
                        balanced != null,
                        "non-null input produced null output");
            }

            results.pass();

            System.out.printf(
                    "PASS %-4s %s%n",
                    test.id,
                    test.description);

            System.out.println(
                    "     original in-order = "
                            + originalValues);

            System.out.println(
                    "     balanced in-order = "
                            + balancedValues);

            System.out.println(
                    "     balanced height   = "
                            + height(balanced));

        } catch (Throwable ex) {

            results.fail();

            System.out.printf(
                    "FAIL %-4s %s%n",
                    test.id,
                    test.description);

            System.out.println(
                    "     " + ex.getMessage());
        }
    }

    /* **********************************************************************
     * Original Tree Preservation Tests
     * **********************************************************************/

    static void runPreservationTests() {

        System.out.println(
                "============================================================");

        System.out.println(
                "Original Tree Preservation Tests");

        System.out.println(
                "============================================================");

        TestResults results =
                new TestResults();

        /*
         * The balancing operation constructs a new tree.
         * Therefore the original tree should remain unchanged.
         */
        Node original =
                node(
                        1,
                        null,
                        node(
                                2,
                                null,
                                node(3)));

        List<Integer> before =
                inOrder(original);

        Node balanced =
                balanceBST(original);

        List<Integer> after =
                inOrder(original);

        assertEquals(
                before,
                after,
                "original tree was modified");

        assertFalse(
                original == balanced,
                "method should construct a new tree");

        results.pass();

        System.out.println(
                "PASS P1   original tree remains unchanged");

        System.out.println();

        printResults(results);
    }

    /* **********************************************************************
     * Null and Edge-case Tests
     * **********************************************************************/

    static void runEdgeCaseTests() {

        System.out.println(
                "============================================================");

        System.out.println(
                "Edge-case Tests");

        System.out.println(
                "============================================================");

        TestResults results =
                new TestResults();

        try {

            assertEquals(
                    null,
                    balanceBST(null),
                    "null should return null");

            results.pass();

            System.out.println(
                    "PASS E1   null tree");

        } catch (Throwable ex) {

            results.fail();

            System.out.println(
                    "FAIL E1   null tree");

            System.out.println(
                    "     " + ex.getMessage());
        }

        try {

            Node single =
                    node(42);

            Node result =
                    balanceBST(single);

            assertTrue(
                    result != null,
                    "single-node result is null");

            assertEquals(
                    List.of(42),
                    inOrder(result),
                    "single-node value changed");

            assertTrue(
                    isBalanced(result),
                    "single-node tree is not balanced");

            results.pass();

            System.out.println(
                    "PASS E2   single node");

        } catch (Throwable ex) {

            results.fail();

            System.out.println(
                    "FAIL E2   single node");

            System.out.println(
                    "     " + ex.getMessage());
        }

        try {

            Node extremeValues =
                    node(
                            0,
                            node(Integer.MIN_VALUE),
                            node(Integer.MAX_VALUE));

            Node result =
                    balanceBST(extremeValues);

            assertEquals(
                    List.of(
                            Integer.MIN_VALUE,
                            0,
                            Integer.MAX_VALUE),
                    inOrder(result),
                    "extreme integer values changed");

            assertTrue(
                    isBalanced(result),
                    "extreme-value tree is not balanced");

            results.pass();

            System.out.println(
                    "PASS E3   Integer.MIN_VALUE / MAX_VALUE");

        } catch (Throwable ex) {

            results.fail();

            System.out.println(
                    "FAIL E3   Integer.MIN_VALUE / MAX_VALUE");

            System.out.println(
                    "     " + ex.getMessage());
        }

        System.out.println();

        printResults(results);
    }

    /* **********************************************************************
     * Invalid Input Demonstration
     * **********************************************************************/

    static void runInvalidBSTTest() {

        System.out.println(
                "============================================================");

        System.out.println(
                "Invalid BST Contract Test");

        System.out.println(
                "============================================================");

        /*
         * The balancing method assumes a BST.
         *
         * This test documents that precondition rather than making
         * balanceBST responsible for validating arbitrary trees.
         */
        Node invalidBST =
                node(
                        10,
                        node(20),
                        node(5));

        assertFalse(
                isValidBST(invalidBST),
                "test tree should be an invalid BST");

        System.out.println(
                "PASS V1   invalid BST correctly identified by validator");

        System.out.println(
                "     balanceBST assumes valid BST input.");

        System.out.println();
    }

    /* **********************************************************************
     * Random BST Generation
     * **********************************************************************/

    /**
     * Inserts a value into a strict BST.
     */
    static Node insertBST(
            Node root,
            int value) {

        if (root == null) {
            return node(value);
        }

        if (value < root.val) {

            root.left =
                    insertBST(
                            root.left,
                            value);

        } else if (value > root.val) {

            root.right =
                    insertBST(
                            root.right,
                            value);
        }

        /*
         * Duplicate values are ignored so that this generator
         * produces a strict BST.
         */
        return root;
    }

    /**
     * Generates a random strict BST.
     */
    static Node randomBST(
            Random random,
            int nodeCount) {

        Node root = null;

        /*
         * Use a reasonably large range and regenerate values
         * when duplicates occur.
         */
        boolean[] used =
                new boolean[nodeCount * 10 + 100];

        int generated = 0;

        while (generated < nodeCount) {

            int value =
                    random.nextInt(used.length);

            if (used[value]) {
                continue;
            }

            used[value] = true;

            root =
                    insertBST(
                            root,
                            value);

            generated++;
        }

        return root;
    }

    /* **********************************************************************
     * Randomised Tests
     * **********************************************************************/

    static void runRandomisedTests(
            int iterations) {

        System.out.println(
                "============================================================");

        System.out.println(
                "Randomised Balance Tests");

        System.out.println(
                "============================================================");

        Random random =
                new Random(20260926L);

        TestResults results =
                new TestResults();

        for (int iteration = 1;
             iteration <= iterations;
             iteration++) {

            int nodeCount =
                    1 + random.nextInt(200);

            Node original =
                    randomBST(
                            random,
                            nodeCount);

            try {

                List<Integer> originalValues =
                        inOrder(original);

                Node balanced =
                        balanceBST(original);

                List<Integer> balancedValues =
                        inOrder(balanced);

                assertEquals(
                        originalValues,
                        balancedValues,
                        "in-order values changed");

                assertTrue(
                        isValidBST(balanced),
                        "result is not a valid BST");

                assertTrue(
                        isBalanced(balanced),
                        "result is not balanced");

                results.pass();

            } catch (Throwable ex) {

                results.fail();

                System.out.printf(
                        "FAIL R%-3d nodeCount=%d%n",
                        iteration,
                        nodeCount);

                System.out.println(
                        "     " + ex.getMessage());

                /*
                 * Continue running the remaining tests so that
                 * failures are not hidden.
                 */
            }
        }

        if (results.failed == 0) {

            System.out.printf(
                    "PASS all %d randomised tests%n",
                    iterations);

        } else {

            System.out.printf(
                    "FAIL %d of %d randomised tests%n",
                    results.failed,
                    results.total());
        }

        System.out.println();

        printResults(results);
    }

    /* **********************************************************************
     * Test Suite
     * **********************************************************************/

    static List<TestCase> createTests() {

        List<TestCase> tests =
                new ArrayList<>();

        /*
         * ============================================================
         * Basic Cases
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "B1",
                        node(1),
                        "single node"));

        tests.add(
                new TestCase(
                        "B2",
                        node(
                                2,
                                node(1),
                                node(3)),
                        "already balanced tree"));

        tests.add(
                new TestCase(
                        "B3",
                        node(
                                4,
                                node(
                                        2,
                                        node(1),
                                        node(3)),
                                node(
                                        6,
                                        node(5),
                                        node(7))),
                        "complete seven-node BST"));

        /*
         * ============================================================
         * Left-skewed Trees
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "L1",
                        node(
                                4,
                                node(
                                        3,
                                        node(
                                                2,
                                                node(1),
                                                null),
                                        null),
                                null),
                        "left-skewed tree"));

        tests.add(
                new TestCase(
                        "L2",
                        node(
                                7,
                                node(
                                        6,
                                        node(
                                                5,
                                                node(
                                                        4,
                                                        node(3),
                                                        null),
                                                null),
                                        null),
                                null),
                        "larger left-skewed tree"));

        /*
         * ============================================================
         * Right-skewed Trees
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "R1",
                        node(
                                1,
                                null,
                                node(
                                        2,
                                        null,
                                        node(
                                                3,
                                                null,
                                                node(4)))),
                        "right-skewed tree"));

        tests.add(
                new TestCase(
                        "R2",
                        node(
                                1,
                                null,
                                node(
                                        2,
                                        null,
                                        node(
                                                3,
                                                null,
                                                node(
                                                        4,
                                                        null,
                                                        node(
                                                                5,
                                                                null,
                                                                node(6)))))),
                        "larger right-skewed tree"));

        /*
         * ============================================================
         * Asymmetric Trees
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "A1",
                        node(
                                8,
                                node(
                                        4,
                                        node(2),
                                        node(
                                                6,
                                                node(5),
                                                null)),
                                node(
                                        12,
                                        null,
                                        node(14))),
                        "irregular BST"));

        tests.add(
                new TestCase(
                        "A2",
                        node(
                                10,
                                node(
                                        5,
                                        node(2),
                                        null),
                                node(
                                        20,
                                        node(15),
                                        node(
                                                30,
                                                null,
                                                node(40)))),
                        "asymmetric BST"));

        /*
         * ============================================================
         * Negative Values
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "N1",
                        node(
                                -1,
                                node(
                                        -5,
                                        node(-10),
                                        node(-3)),
                                node(
                                        4,
                                        node(2),
                                        node(8))),
                        "negative and positive values"));

        /*
         * ============================================================
         * Extreme Integer Values
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "X1",
                        node(
                                0,
                                node(Integer.MIN_VALUE),
                                node(Integer.MAX_VALUE)),
                        "integer boundary values"));

        /*
         * ============================================================
         * Larger Deterministic Tree
         * ============================================================
         */

        Node larger =
                null;

        int[] values = {
                50,
                25,
                75,
                10,
                30,
                60,
                90,
                5,
                15,
                27,
                35,
                55,
                65,
                80,
                100,
                1,
                7,
                12,
                20,
                26,
                28,
                33,
                40,
                52,
                58,
                62,
                70,
                78,
                85,
                95
        };

        for (int value : values) {
            larger =
                    insertBST(
                            larger,
                            value);
        }

        tests.add(
                new TestCase(
                        "L3",
                        larger,
                        "larger deterministic BST"));

        return tests;
    }

    /* **********************************************************************
     * Results
     * **********************************************************************/

    static void printResults(
            TestResults results) {

        System.out.println(
                "------------------------------------------------------------");

        System.out.printf(
                "Passed: %d%n",
                results.passed);

        System.out.printf(
                "Failed: %d%n",
                results.failed);

        System.out.printf(
                "Total:  %d%n",
                results.total());

        System.out.println(
                "------------------------------------------------------------");

        System.out.println();
    }

    /* **********************************************************************
     * Main
     * **********************************************************************/

    public static void main(String[] args) {

        System.out.println(
                "############################################################");

        System.out.println(
                "####################  BALANCE BST  #########################");

        System.out.println(
                "############################################################");

        System.out.println();

        System.out.println(
                "Algorithm:");

        System.out.println(
                "  1. Store BST values using in-order traversal.");

        System.out.println(
                "  2. Recursively select the middle value.");

        System.out.println(
                "  3. Build balanced left and right subtrees.");

        System.out.println();

        /*
         * ============================================================
         * Standard Tests
         * ============================================================
         */

        System.out.println(
                "============================================================");

        System.out.println(
                "Standard Balance Tests");

        System.out.println(
                "============================================================");

        TestResults standardResults =
                new TestResults();

        List<TestCase> tests =
                createTests();

        for (TestCase test : tests) {

            runBalanceTest(
                    test,
                    standardResults);
        }

        System.out.println();

        printResults(standardResults);

        /*
         * ============================================================
         * Edge Cases
         * ============================================================
         */

        runEdgeCaseTests();

        /*
         * ============================================================
         * Original Tree Preservation
         * ============================================================
         */

        runPreservationTests();

        /*
         * ============================================================
         * Invalid BST Contract
         * ============================================================
         */

        runInvalidBSTTest();

        /*
         * ============================================================
         * Randomised Tests
         * ============================================================
         */

        runRandomisedTests(5000);

        /*
         * ============================================================
         * Final Summary
         * ============================================================
         */

        System.out.println(
                "############################################################");

        System.out.println(
                "####################  TEST SUITE COMPLETE  ################");

        System.out.println(
                "############################################################");
    }
}
