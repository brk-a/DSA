import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Find the Lowest Common Ancestor (LCA) of two values in a Binary
 * Search Tree (BST).
 *
 * The tree must satisfy the BST property:
 *
 *      left subtree  <  node  <  right subtree
 *
 * This implementation provides:
 *
 * 1. Recursive LCA
 *      Time:  O(h)
 *      Space: O(h)
 *
 * 2. Iterative LCA
 *      Time:  O(h)
 *      Space: O(1)
 *
 * where:
 *
 *      h = height of the tree
 *
 * The Result record contains:
 *
 *      ancestor - the LCA when both values exist
 *      found    - true only when both requested values exist
 *
 * Node values are assumed to be unique.
 */
public class LCAofBinarySearchTree {

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
            Node ancestor,
            boolean found) {
    }

    /* **********************************************************************
     * Helpers
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

    /**
     * Search for a value in the BST.
     *
     * Time: O(h)
     * Space: O(h) because this is recursive.
     */
    static boolean containsRecursive(
            Node root,
            int value) {

        if (root == null) {
            return false;
        }

        if (value < root.val) {
            return containsRecursive(
                    root.left,
                    value);
        }

        if (value > root.val) {
            return containsRecursive(
                    root.right,
                    value);
        }

        return true;
    }

    /**
     * Search for a value in the BST.
     *
     * Time: O(h)
     * Space: O(1)
     */
    static boolean containsIterative(
            Node root,
            int value) {

        while (root != null) {

            if (value < root.val) {
                root = root.left;

            } else if (value > root.val) {
                root = root.right;

            } else {
                return true;
            }
        }

        return false;
    }

    /* **********************************************************************
     * 1. Recursive LCA
     * **********************************************************************/

    /**
     * Find the LCA recursively.
     *
     * Both values must exist in the tree for the result to be valid.
     *
     * Time: O(h)
     * Space: O(h)
     */
    static Result lcaRecursive(
            Node root,
            int val1,
            int val2) {

        if (root == null) {
            return new Result(
                    null,
                    false);
        }

        /*
         * Verify that both requested values actually exist.
         *
         * This prevents the algorithm from returning a node as
         * an LCA when one of the requested values is absent.
         */
        if (!containsRecursive(root, val1)
                || !containsRecursive(root, val2)) {

            return new Result(
                    null,
                    false);
        }

        Node ancestor =
                findLcaRecursive(
                        root,
                        val1,
                        val2);

        return new Result(
                ancestor,
                ancestor != null);
    }

    /**
     * Find the LCA after existence has been verified.
     */
    static Node findLcaRecursive(
            Node root,
            int val1,
            int val2) {

        if (root == null) {
            return null;
        }

        /*
         * Both values are smaller than the current node.
         *
         * Therefore the LCA must be in the left subtree.
         */
        if (val1 < root.val
                && val2 < root.val) {

            return findLcaRecursive(
                    root.left,
                    val1,
                    val2);
        }

        /*
         * Both values are larger than the current node.
         *
         * Therefore the LCA must be in the right subtree.
         */
        if (val1 > root.val
                && val2 > root.val) {

            return findLcaRecursive(
                    root.right,
                    val1,
                    val2);
        }

        /*
         * The values are on opposite sides, or one of them is
         * equal to the current node.
         */
        return root;
    }

    /* **********************************************************************
     * 2. Iterative LCA
     * **********************************************************************/

    /**
     * Find the LCA iteratively.
     *
     * Both values must exist in the tree for the result to be valid.
     *
     * Time: O(h)
     * Space: O(1)
     */
    static Result lcaIterative(
            Node root,
            int val1,
            int val2) {

        if (root == null) {
            return new Result(
                    null,
                    false);
        }

        /*
         * Verify that both requested values actually exist.
         */
        if (!containsIterative(root, val1)
                || !containsIterative(root, val2)) {

            return new Result(
                    null,
                    false);
        }

        Node ancestor =
                findLcaIterative(
                        root,
                        val1,
                        val2);

        return new Result(
                ancestor,
                ancestor != null);
    }

    /**
     * Find the LCA after existence has been verified.
     */
    static Node findLcaIterative(
            Node root,
            int val1,
            int val2) {

        while (root != null) {

            /*
             * Both values are smaller than the current node.
             */
            if (val1 < root.val
                    && val2 < root.val) {

                root = root.left;

                continue;
            }

            /*
             * Both values are larger than the current node.
             */
            if (val1 > root.val
                    && val2 > root.val) {

                root = root.right;

                continue;
            }

            /*
             * The values are on opposite sides, or one of them
             * equals the current node.
             */
            return root;
        }

        return null;
    }

    /* **********************************************************************
     * Tree Comparison
     * **********************************************************************/

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

    /* **********************************************************************
     * Test Helpers
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

        if (condition) {
            throw new AssertionError(message);
        }
    }

    static void assertEquals(
            int expected,
            int actual,
            String message) {

        if (expected != actual) {
            throw new AssertionError(
                    message
                            + " expected="
                            + expected
                            + " actual="
                            + actual);
        }
    }

    static void assertNull(
            Object value,
            String message) {

        if (value != null) {
            throw new AssertionError(
                    message
                            + " actual="
                            + value);
        }
    }

    static void assertNode(
            Node expected,
            Result actual,
            String message) {

        assertTrue(
                actual.found(),
                message + " result should be found");

        assertTrue(
                actual.ancestor() != null,
                message + " ancestor should not be null");

        assertEquals(
                expected.val,
                actual.ancestor().val,
                message + " incorrect ancestor");
    }

    static void assertNotFound(
            Result actual,
            String message) {

        assertFalse(
                actual.found(),
                message + " should not be found");

        assertNull(
                actual.ancestor(),
                message + " ancestor should be null");
    }

    /* **********************************************************************
     * Test Cases
     * **********************************************************************/

    static class TestCase {

        final String id;
        final Node root;
        final int val1;
        final int val2;
        final Integer expected;
        final String description;

        TestCase(
                String id,
                Node root,
                int val1,
                int val2,
                Integer expected,
                String description) {

            this.id = id;
            this.root = root;
            this.val1 = val1;
            this.val2 = val2;
            this.expected = expected;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface LcaAlgorithm {

        Result solve(
                Node root,
                int val1,
                int val2);
    }

    static class MethodCase {

        final String name;
        final LcaAlgorithm algorithm;

        MethodCase(
                String name,
                LcaAlgorithm algorithm) {

            this.name = name;
            this.algorithm = algorithm;
        }
    }

    /* **********************************************************************
     * LCA Tests
     * **********************************************************************/

    static void runLcaTests(
            String algorithmName,
            LcaAlgorithm method,
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
                                test.root,
                                test.val1,
                                test.val2);

                if (test.expected == null) {

                    assertNotFound(
                            actual,
                            test.description);

                } else {

                    assertTrue(
                            actual.found(),
                            test.description
                                    + " should be found");

                    assertEquals(
                            test.expected,
                            actual.ancestor().val,
                            test.description);
                }

                passed++;

                System.out.printf(
                        "PASS %s (%s)%n",
                        test.id,
                        test.description);

                if (actual.found()) {

                    System.out.println(
                            "  LCA = "
                                    + actual.ancestor().val);

                } else {

                    System.out.println(
                            "  found = false");
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
     * Cross-check Recursive vs Iterative
     * **********************************************************************/

    static void runCrossCheckTests(
            List<TestCase> tests) {

        System.out.println(
                "======================================================");

        System.out.println(
                "Recursive / Iterative Cross Checks");

        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            try {

                Result recursive =
                        lcaRecursive(
                                test.root,
                                test.val1,
                                test.val2);

                Result iterative =
                        lcaIterative(
                                test.root,
                                test.val1,
                                test.val2);

                assertEquals(
                        recursive.found() ? 1 : 0,
                        iterative.found() ? 1 : 0,
                        test.description
                                + " found mismatch");

                if (recursive.found()) {

                    assertEquals(
                            recursive.ancestor().val,
                            iterative.ancestor().val,
                            test.description
                                    + " ancestor mismatch");
                }

                passed++;

                System.out.printf(
                        "PASS %s (%s)%n",
                        test.id,
                        test.description);

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
     * Random BST Generation
     * **********************************************************************/

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

        } else if (value > root.val) {

            root.right =
                    insert(
                            root.right,
                            value);
        }

        return root;
    }

    static Node randomBst(
            Random rng,
            int size) {

        Node root = null;

        /*
         * Use a wide value range so that duplicate values are
         * unlikely, then explicitly avoid duplicates.
         */
        boolean[] used =
                new boolean[size * 20 + 1];

        for (int i = 0; i < size; i++) {

            int value;

            do {

                value =
                        rng.nextInt(
                                used.length);

            } while (used[value]);

            used[value] = true;

            /*
             * Shift values so that some generated trees contain
             * negative values as well.
             */
            int actualValue =
                    value - size * 10;

            root =
                    insert(
                            root,
                            actualValue);
        }

        return root;
    }

    /* **********************************************************************
     * Collect Values
     * **********************************************************************/

    static void collectValues(
            Node root,
            List<Integer> values) {

        if (root == null) {
            return;
        }

        values.add(root.val);

        collectValues(
                root.left,
                values);

        collectValues(
                root.right,
                values);
    }

    /* **********************************************************************
     * Find Expected LCA Using Parent Paths
     *
     * This implementation is deliberately different from the production
     * LCA algorithms. It gives the randomised tests an independent
     * reference implementation.
     * **********************************************************************/

    static boolean findPath(
            Node root,
            int value,
            List<Node> path) {

        if (root == null) {
            return false;
        }

        path.add(root);

        if (root.val == value) {
            return true;
        }

        if (value < root.val) {

            if (findPath(
                    root.left,
                    value,
                    path)) {

                return true;
            }

        } else {

            if (findPath(
                    root.right,
                    value,
                    path)) {

                return true;
            }
        }

        path.remove(
                path.size() - 1);

        return false;
    }

    static Integer expectedLca(
            Node root,
            int val1,
            int val2) {

        List<Node> path1 =
                new ArrayList<>();

        List<Node> path2 =
                new ArrayList<>();

        if (!findPath(
                root,
                val1,
                path1)) {

            return null;
        }

        if (!findPath(
                root,
                val2,
                path2)) {

            return null;
        }

        int length =
                Math.min(
                        path1.size(),
                        path2.size());

        Node lca = null;

        for (int i = 0; i < length; i++) {

            if (path1.get(i) != path2.get(i)) {
                break;
            }

            lca =
                    path1.get(i);
        }

        return lca == null
                ? null
                : lca.val;
    }

    /* **********************************************************************
     * Randomised Tests
     * **********************************************************************/

    static void runRandomisedTests(
            int iterations) {

        System.out.println(
                "======================================================");

        System.out.println(
                "Randomised LCA Cross Checks");

        System.out.println(
                "======================================================");

        Random rng =
                new Random(20260913L);

        int passed = 0;
        int failed = 0;

        for (int iteration = 1;
             iteration <= iterations;
             iteration++) {

            try {

                int size =
                        2 + rng.nextInt(40);

                Node root =
                        randomBst(
                                rng,
                                size);

                List<Integer> values =
                        new ArrayList<>();

                collectValues(
                        root,
                        values);

                /*
                 * Test two existing values.
                 */
                int val1 =
                        values.get(
                                rng.nextInt(
                                        values.size()));

                int val2 =
                        values.get(
                                rng.nextInt(
                                        values.size()));

                Integer expected =
                        expectedLca(
                                root,
                                val1,
                                val2);

                Result recursive =
                        lcaRecursive(
                                root,
                                val1,
                                val2);

                Result iterative =
                        lcaIterative(
                                root,
                                val1,
                                val2);

                assertTrue(
                        expected != null,
                        "existing values should have LCA");

                assertTrue(
                        recursive.found(),
                        "recursive result should be found");

                assertTrue(
                        iterative.found(),
                        "iterative result should be found");

                assertEquals(
                        expected,
                        recursive.ancestor().val,
                        "recursive LCA incorrect");

                assertEquals(
                        expected,
                        iterative.ancestor().val,
                        "iterative LCA incorrect");

                /*
                 * Test a value that definitely does not exist.
                 */
                int missingValue =
                        Integer.MAX_VALUE;

                Result missingRecursive =
                        lcaRecursive(
                                root,
                                val1,
                                missingValue);

                Result missingIterative =
                        lcaIterative(
                                root,
                                val1,
                                missingValue);

                assertNotFound(
                        missingRecursive,
                        "recursive missing value");

                assertNotFound(
                        missingIterative,
                        "iterative missing value");

                passed++;

            } catch (Exception ex) {

                failed++;

                System.out.printf(
                        "FAIL random iteration %d%n",
                        iteration);

                System.out.println(
                        "  exception = " + ex);

                break;
            }
        }

        System.out.println();

        System.out.printf(
                "Results: %d passed, %d failed, %d total%n",
                passed,
                failed,
                iterations);

        System.out.println();
    }

    /* **********************************************************************
     * Invalid Input Tests
     * **********************************************************************/

    static void runInvalidInputTests() {

        System.out.println(
                "======================================================");

        System.out.println(
                "Invalid / Boundary Input Tests");

        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        /*
         * Null root.
         */
        Result recursiveNull =
                lcaRecursive(
                        null,
                        1,
                        2);

        Result iterativeNull =
                lcaIterative(
                        null,
                        1,
                        2);

        try {

            assertNotFound(
                    recursiveNull,
                    "recursive null root");

            passed++;

            System.out.println(
                    "PASS I1 - recursive null root");

        } catch (Exception ex) {

            failed++;

            System.out.println(
                    "FAIL I1 - recursive null root");
        }

        try {

            assertNotFound(
                    iterativeNull,
                    "iterative null root");

            passed++;

            System.out.println(
                    "PASS I2 - iterative null root");

        } catch (Exception ex) {

            failed++;

            System.out.println(
                    "FAIL I2 - iterative null root");
        }

        /*
         * One-value tree, same value requested twice.
         */
        Node single =
                node(42);

        Result sameRecursive =
                lcaRecursive(
                        single,
                        42,
                        42);

        Result sameIterative =
                lcaIterative(
                        single,
                        42,
                        42);

        try {

            assertNode(
                    single,
                    sameRecursive,
                    "recursive same value");

            passed++;

            System.out.println(
                    "PASS I3 - recursive same value");

        } catch (Exception ex) {

            failed++;

            System.out.println(
                    "FAIL I3 - recursive same value");
        }

        try {

            assertNode(
                    single,
                    sameIterative,
                    "iterative same value");

            passed++;

            System.out.println(
                    "PASS I4 - iterative same value");

        } catch (Exception ex) {

            failed++;

            System.out.println(
                    "FAIL I4 - iterative same value");
        }

        /*
         * Missing first value.
         */
        Node simple =
                node(
                        10,
                        node(5),
                        node(15));

        Result missingFirst =
                lcaRecursive(
                        simple,
                        999,
                        15);

        try {

            assertNotFound(
                    missingFirst,
                    "missing first value");

            passed++;

            System.out.println(
                    "PASS I5 - missing first value");

        } catch (Exception ex) {

            failed++;

            System.out.println(
                    "FAIL I5 - missing first value");
        }

        /*
         * Missing second value.
         */
        Result missingSecond =
                lcaIterative(
                        simple,
                        5,
                        999);

        try {

            assertNotFound(
                    missingSecond,
                    "missing second value");

            passed++;

            System.out.println(
                    "PASS I6 - missing second value");

        } catch (Exception ex) {

            failed++;

            System.out.println(
                    "FAIL I6 - missing second value");
        }

        /*
         * Both values missing.
         */
        Result bothMissing =
                lcaRecursive(
                        simple,
                        100,
                        999);

        try {

            assertNotFound(
                    bothMissing,
                    "both values missing");

            passed++;

            System.out.println(
                    "PASS I7 - both values missing");

        } catch (Exception ex) {

            failed++;

            System.out.println(
                    "FAIL I7 - both values missing");
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
     * Main Test Suite
     * **********************************************************************/

    public static void main(String[] args) {

        /*
         * ============================================================
         * Trees
         * ============================================================
         */

        /*
         *                 20
         *               /    \
         *             10      30
         *            /  \    /  \
         *           5   15 25   35
         *          / \  / \
         *         2  7 12 17
         */
        Node basicTree =
                node(
                        20,
                        node(
                                10,
                                node(
                                        5,
                                        node(2),
                                        node(7)),
                                node(
                                        15,
                                        node(12),
                                        node(17))),
                        node(
                                30,
                                node(25),
                                node(35)));

        /*
         * Left-skewed BST.
         *
         *        50
         *       /
         *      40
         *     /
         *    30
         *   /
         *  20
         */
        Node leftSkewed =
                node(
                        50,
                        node(
                                40,
                                node(
                                        30,
                                        node(20),
                                        null),
                                null),
                        null);

        /*
         * Right-skewed BST.
         *
         * 10
         *   \
         *    20
         *      \
         *       30
         *         \
         *          40
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
                                        node(40)))));

        /*
         * BST containing negative values.
         */
        Node negativeTree =
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
         * Larger irregular BST.
         *
         *                  50
         *                /    \
         *              25      75
         *             /  \    /  \
         *           10   40  60   90
         *          / \   / \   \   / \
         *         5  15 30 45  65 80 100
         */
        Node largeTree =
                node(
                        50,
                        node(
                                25,
                                node(
                                        10,
                                        node(5),
                                        node(15)),
                                node(
                                        40,
                                        node(30),
                                        node(45))),
                        node(
                                75,
                                node(
                                        60,
                                        null,
                                        node(65)),
                                node(
                                        90,
                                        node(80),
                                        node(100))));

        /*
         * ============================================================
         * Test Cases
         * ============================================================
         */

        List<TestCase> tests =
                List.of(

                        new TestCase(
                                "B1",
                                node(42),
                                42,
                                42,
                                42,
                                "single node, same value"),

                        new TestCase(
                                "B2",
                                basicTree,
                                5,
                                7,
                                5,
                                "siblings under node 5"),

                        new TestCase(
                                "B3",
                                basicTree,
                                2,
                                17,
                                10,
                                "values in different branches"),

                        new TestCase(
                                "B4",
                                basicTree,
                                12,
                                17,
                                15,
                                "siblings under node 15"),

                        new TestCase(
                                "B5",
                                basicTree,
                                2,
                                7,
                                5,
                                "deep descendants"),

                        new TestCase(
                                "B6",
                                basicTree,
                                5,
                                35,
                                20,
                                "values across root"),

                        new TestCase(
                                "B7",
                                basicTree,
                                20,
                                17,
                                20,
                                "one value is root"),

                        new TestCase(
                                "B8",
                                basicTree,
                                25,
                                35,
                                30,
                                "right subtree siblings"),

                        new TestCase(
                                "B9",
                                basicTree,
                                2,
                                100,
                                null,
                                "second value missing"),

                        new TestCase(
                                "B10",
                                basicTree,
                                999,
                                1000,
                                null,
                                "both values missing"),

                        new TestCase(
                                "A1",
                                leftSkewed,
                                20,
                                30,
                                30,
                                "left-skewed descendants"),

                        new TestCase(
                                "A2",
                                leftSkewed,
                                20,
                                50,
                                50,
                                "left-skewed root ancestor"),

                        new TestCase(
                                "A3",
                                rightSkewed,
                                20,
                                40,
                                20,
                                "right-skewed descendants"),

                        new TestCase(
                                "A4",
                                rightSkewed,
                                10,
                                40,
                                10,
                                "right-skewed root ancestor"),

                        new TestCase(
                                "N1",
                                negativeTree,
                                -20,
                                -5,
                                -10,
                                "negative values"),

                        new TestCase(
                                "N2",
                                negativeTree,
                                -20,
                                20,
                                0,
                                "negative and positive values"),

                        new TestCase(
                                "N3",
                                negativeTree,
                                5,
                                20,
                                10,
                                "positive subtree"),

                        new TestCase(
                                "L1",
                                largeTree,
                                5,
                                15,
                                10,
                                "deep left subtree"),

                        new TestCase(
                                "L2",
                                largeTree,
                                30,
                                45,
                                40,
                                "deep middle subtree"),

                        new TestCase(
                                "L3",
                                largeTree,
                                65,
                                80,
                                75,
                                "deep right subtree"),

                        new TestCase(
                                "L4",
                                largeTree,
                                5,
                                100,
                                50,
                                "opposite sides of root"),

                        new TestCase(
                                "L5",
                                largeTree,
                                50,
                                100,
                                50,
                                "root and descendant"),

                        new TestCase(
                                "L6",
                                largeTree,
                                1234,
                                100,
                                null,
                                "missing first value")
                );

        /*
         * ============================================================
         * Header
         * ============================================================
         */

        System.out.println(
                "############################################################");

        System.out.println(
                "##############  LCA OF BINARY SEARCH TREE  ################");

        System.out.println(
                "############################################################");

        System.out.println();

        /*
         * ============================================================
         * Algorithm Tests
         * ============================================================
         */

        List<MethodCase> methods =
                List.of(

                        new MethodCase(
                                "Recursive LCA",
                                LCAofBinarySearchTree
                                        ::lcaRecursive),

                        new MethodCase(
                                "Iterative LCA",
                                LCAofBinarySearchTree
                                        ::lcaIterative)
                );

        for (MethodCase method : methods) {

            runLcaTests(
                    method.name,
                    method.algorithm,
                    tests);
        }

        /*
         * ============================================================
         * Recursive vs Iterative
         * ============================================================
         */

        runCrossCheckTests(
                tests);

        /*
         * ============================================================
         * Invalid / Boundary Inputs
         * ============================================================
         */

        runInvalidInputTests();

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
