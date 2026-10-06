import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Find the maximum possible sum of non-adjacent nodes in a binary tree.
 *
 * Two nodes are considered adjacent when one is the direct parent of
 * the other.
 *
 * Therefore, if a node is selected, neither of its children may be
 * selected.
 *
 * Example:
 *
 *              10
 *            /    \
 *           1      2
 *          / \    / \
 *         3   4  5   6
 *
 * One optimal selection is:
 *
 *              10
 *             /  \
 *            3    5
 *
 * Sum = 18
 *
 * Three implementations are provided:
 *
 * 1. Plain Recursion
 *
 *      For every node, try:
 *
 *          a) Include the node.
 *          b) Exclude the node.
 *
 *      Time:  O(2^n) in the worst case
 *      Space: O(h)
 *
 * 2. Memoisation
 *
 *      Same recurrence as plain recursion, but cache the answer for
 *      every node.
 *
 *      Time:  O(n)
 *      Space: O(n)
 *
 * 3. Include / Exclude
 *
 *      For every node calculate two values:
 *
 *          include = maximum sum when this node is selected
 *          exclude = maximum sum when this node is not selected
 *
 *      Time:  O(n)
 *      Space: O(h)
 *
 * For negative values, selecting no nodes is allowed, so the minimum
 * valid answer is 0.
 *
 * Null roots are considered invalid and return:
 *
 *      new Result(-1, false)
 */
public class MaxSumNonAdjacentNodes {

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
            int sum,
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
     * 1. Plain Recursion
     * **********************************************************************/

    /**
     * Find the maximum sum of non-adjacent nodes using plain recursion.
     *
     * At every node there are two choices:
     *
     * 1. Include the current node.
     *
     *      If root is included, neither child can be included.
     *
     *      Therefore:
     *
     *          root.val
     *          + best(root.left.left)
     *          + best(root.left.right)
     *          + best(root.right.left)
     *          + best(root.right.right)
     *
     * 2. Exclude the current node.
     *
     *      If root is excluded, each child can independently be either
     *      included or excluded.
     *
     *      Therefore:
     *
     *          best(root.left)
     *          + best(root.right)
     *
     * The maximum of these two choices is returned.
     *
     * This intentionally does not use memoisation.
     */
    static Result maxSumNonAdjacentNodesRecursion(
            Node root) {

        if (!validNode(root)) {

            return new Result(
                    -1,
                    false);
        }

        int sum =
                maxSumNonAdjacentNodesRecursionValue(
                        root);

        return new Result(
                sum,
                true);
    }

    /**
     * Recursive value calculation.
     *
     * This method assumes that root is non-null.
     */
    static int maxSumNonAdjacentNodesRecursionValue(
            Node root) {

        if (root == null) {
            return 0;
        }

        /*
         * --------------------------------------------------------------
         * Option 1: Include the current node.
         * --------------------------------------------------------------
         *
         * If root is included, its direct children cannot be included.
         *
         * Therefore, we are free to take the best result from each
         * grandchild.
         */

        int include =
                root.val
                        + maxSumNonAdjacentNodesRecursionValue(
                                root.left == null
                                        ? null
                                        : root.left.left)
                        + maxSumNonAdjacentNodesRecursionValue(
                                root.left == null
                                        ? null
                                        : root.left.right)
                        + maxSumNonAdjacentNodesRecursionValue(
                                root.right == null
                                        ? null
                                        : root.right.left)
                        + maxSumNonAdjacentNodesRecursionValue(
                                root.right == null
                                        ? null
                                        : root.right.right);

        /*
         * --------------------------------------------------------------
         * Option 2: Exclude the current node.
         * --------------------------------------------------------------
         *
         * If root is excluded, its children can independently choose
         * their optimal solutions.
         */

        int exclude =
                maxSumNonAdjacentNodesRecursionValue(
                        root.left)
                        + maxSumNonAdjacentNodesRecursionValue(
                        root.right);

        /*
         * Selecting no nodes is allowed, so never return a negative sum.
         */
        return Math.max(
                0,
                Math.max(
                        include,
                        exclude));
    }

    /* **********************************************************************
     * 2. Memoisation
     * **********************************************************************/

    /**
     * Find the maximum sum of non-adjacent nodes using memoisation.
     *
     * The recurrence is the same as the plain recursive implementation,
     * but each node's answer is calculated only once.
     *
     * An IdentityHashMap is used because the identity of a Node matters.
     * Two different Node objects may contain the same value.
     */
    static Result maxSumNonAdjacentNodesMemoisation(
            Node root) {

        if (!validNode(root)) {

            return new Result(
                    -1,
                    false);
        }

        Map<Node, Integer> memo =
                new IdentityHashMap<>();

        int sum =
                maxSumNonAdjacentNodesMemoisationValue(
                        root,
                        memo);

        return new Result(
                sum,
                true);
    }

    /**
     * Memoised version of the recursive recurrence.
     */
    static int maxSumNonAdjacentNodesMemoisationValue(
            Node root,
            Map<Node, Integer> memo) {

        if (root == null) {
            return 0;
        }

        Integer cached =
                memo.get(root);

        if (cached != null) {
            return cached;
        }

        /*
         * Include current node.
         */
        int include =
                root.val
                        + maxSumNonAdjacentNodesMemoisationValue(
                                root.left == null
                                        ? null
                                        : root.left.left,
                                memo)
                        + maxSumNonAdjacentNodesMemoisationValue(
                                root.left == null
                                        ? null
                                        : root.left.right,
                                memo)
                        + maxSumNonAdjacentNodesMemoisationValue(
                                root.right == null
                                        ? null
                                        : root.right.left,
                                memo)
                        + maxSumNonAdjacentNodesMemoisationValue(
                                root.right == null
                                        ? null
                                        : root.right.right,
                                memo);

        /*
         * Exclude current node.
         */
        int exclude =
                maxSumNonAdjacentNodesMemoisationValue(
                        root.left,
                        memo)
                        + maxSumNonAdjacentNodesMemoisationValue(
                        root.right,
                        memo);

        int result =
                Math.max(
                        0,
                        Math.max(
                                include,
                                exclude));

        memo.put(
                root,
                result);

        return result;
    }

    /* **********************************************************************
     * 3. Include / Exclude
     * **********************************************************************/

    /**
     * State containing the two possible answers for a subtree.
     *
     * include:
     *
     *      Maximum sum when the current node IS selected.
     *
     * exclude:
     *
     *      Maximum sum when the current node IS NOT selected.
     */
    static record IncludeExcludeState(
            int include,
            int exclude) {
    }

    /**
     * Find the maximum sum using the include/exclude dynamic programming
     * recurrence.
     *
     * For every node:
     *
     *      include =
     *          root.val
     *          + left.exclude
     *          + right.exclude
     *
     *      exclude =
     *          max(left.include, left.exclude)
     *          + max(right.include, right.exclude)
     *
     * The final answer is:
     *
     *      max(root.include, root.exclude)
     *
     * This avoids the repeated grandchild traversal of the plain
     * recursive solution.
     */
    static Result maxSumNonAdjacentNodesIncludeExclude(
            Node root) {

        if (!validNode(root)) {

            return new Result(
                    -1,
                    false);
        }

        IncludeExcludeState state =
                maxSumNonAdjacentNodesIncludeExcludeState(
                        root);

        int sum =
                Math.max(
                        0,
                        Math.max(
                                state.include(),
                                state.exclude()));

        return new Result(
                sum,
                true);
    }

    /**
     * Calculate include/exclude state for a subtree.
     */
    static IncludeExcludeState maxSumNonAdjacentNodesIncludeExcludeState(
            Node root) {

        if (root == null) {

            return new IncludeExcludeState(
                    0,
                    0);
        }

        IncludeExcludeState left =
                maxSumNonAdjacentNodesIncludeExcludeState(
                        root.left);

        IncludeExcludeState right =
                maxSumNonAdjacentNodesIncludeExcludeState(
                        root.right);

        /*
         * If root is included, neither child can be included.
         */
        int include =
                root.val
                        + left.exclude()
                        + right.exclude();

        /*
         * If root is excluded, each child independently chooses
         * whichever state is better.
         */
        int exclude =
                Math.max(
                        left.include(),
                        left.exclude())
                        + Math.max(
                        right.include(),
                        right.exclude());

        return new IncludeExcludeState(
                include,
                exclude);
    }

    /* **********************************************************************
     * Brute Force Reference Implementation
     * **********************************************************************/

    /**
     * Reference implementation used only by the test suite.
     *
     * It enumerates every subset of the tree and checks whether the
     * selected nodes contain a parent-child pair.
     *
     * This is intentionally inefficient and is used only for small
     * random trees to independently verify the three implementations.
     */
    static int bruteForceMaximum(
            Node root) {

        if (root == null) {
            return 0;
        }

        return bruteForceMaximum(
                root,
                false);
    }

    /**
     * Reference recurrence.
     *
     * previousSelected indicates whether the parent of root was selected.
     */
    static int bruteForceMaximum(
            Node root,
            boolean previousSelected) {

        if (root == null) {
            return 0;
        }

        /*
         * Option 1:
         *
         * Do not select this node.
         */
        int exclude =
                bruteForceMaximum(
                        root.left,
                        false)
                        + bruteForceMaximum(
                        root.right,
                        false);

        /*
         * Option 2:
         *
         * Select this node only if its parent was not selected.
         */
        int include =
                Integer.MIN_VALUE;

        if (!previousSelected) {

            include =
                    root.val
                            + bruteForceMaximum(
                                    root.left,
                                    true)
                            + bruteForceMaximum(
                                    root.right,
                                    true);
        }

        return Math.max(
                0,
                Math.max(
                        include,
                        exclude));
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

        for (TestCase test : tests) {

            try {

                Result actual =
                        method.algorithm.solve(
                                test.root);

                boolean success =
                        actual.valid()
                                == test.expectedValid
                                && actual.sum()
                                == test.expected;

                if (success) {

                    passed++;

                    System.out.printf(
                            "PASS %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  sum = "
                                    + actual.sum());

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
                                    + actual.sum()
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

        for (TestCase test : tests) {

            try {

                Result first =
                        methods.get(0)
                                .algorithm
                                .solve(
                                        test.root);

                boolean success =
                        first.valid()
                                == test.expectedValid
                                && first.sum()
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
                            || actual.sum()
                            != first.sum()) {

                        success = false;

                        System.out.printf(
                                "  %s = (%d, %s)%n",
                                methods.get(i).name,
                                actual.sum(),
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

        /*
         * Null root.
         */
        for (MethodCase method :
                methods) {

            Result result =
                    method.algorithm.solve(
                            null);

            if (!result.valid()
                    && result.sum() == -1) {

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
         * Classic Example
         * ============================================================
         *
         *              10
         *            /    \
         *           1      2
         *          / \    / \
         *         3   4  5   6
         *
         * Optimal:
         *
         *      10 + 3 + 4 + 5 + 6 = 28
         *
         * However, 3 and 4 are both children of 1, and 5 and 6 are
         * both children of 2. Since 1 and 2 are not selected, this
         * selection is valid.
         */

        Node classic =
                node(
                        10,
                        node(
                                1,
                                node(3),
                                node(4)),
                        node(
                                2,
                                node(5),
                                node(6)));

        tests.add(
                new TestCase(
                        "B1",
                        classic,
                        28,
                        true,
                        "classic binary-tree example"));

        /*
         * ============================================================
         * Root vs Children
         * ============================================================
         *
         *          10
         *         /  \
         *        5    5
         *
         * Best = 10
         */

        Node rootVsChildren =
                node(
                        10,
                        node(5),
                        node(5));

        tests.add(
                new TestCase(
                        "B2",
                        rootVsChildren,
                        10,
                        true,
                        "root beats both children"));

        /*
         * ============================================================
         * Children vs Root
         * ============================================================
         *
         *          3
         *         / \
         *        10 10
         *
         * Best = 20
         */

        Node childrenVsRoot =
                node(
                        3,
                        node(10),
                        node(10));

        tests.add(
                new TestCase(
                        "B3",
                        childrenVsRoot,
                        20,
                        true,
                        "children beat root"));

        /*
         * ============================================================
         * Grandchildren
         * ============================================================
         *
         *              10
         *             /  \
         *            1    2
         *           /      \
         *          20       30
         *
         * Best = 10 + 20 + 30 = 60
         */

        Node grandchildren =
                node(
                        10,
                        node(
                                1,
                                node(20),
                                null),
                        node(
                                2,
                                null,
                                node(30)));

        tests.add(
                new TestCase(
                        "B4",
                        grandchildren,
                        60,
                        true,
                        "grandchildren are selected"));

        /*
         * ============================================================
         * Single Node
         * ============================================================
         */

        Node single =
                node(42);

        tests.add(
                new TestCase(
                        "S1",
                        single,
                        42,
                        true,
                        "single-node tree"));

        /*
         * ============================================================
         * Zero
         * ============================================================
         */

        Node zero =
                node(0);

        tests.add(
                new TestCase(
                        "S2",
                        zero,
                        0,
                        true,
                        "single zero node"));

        /*
         * ============================================================
         * Negative Values
         * ============================================================
         *
         * Selecting no nodes is allowed.
         *
         * Therefore the answer is 0.
         */

        Node negative =
                node(
                        -10,
                        node(-20),
                        node(-30));

        tests.add(
                new TestCase(
                        "N1",
                        negative,
                        0,
                        true,
                        "all values negative"));

        /*
         * ============================================================
         * Mixed Positive / Negative
         * ============================================================
         *
         *             10
         *            /  \
         *          -20   30
         *          / \   / \
         *         40 -5 50 60
         *
         * Best:
         *
         *      10 + 40 + 50 + 60 = 160
         *
         * Note that -20 and 30 are excluded.
         */

        Node mixed =
                node(
                        10,
                        node(
                                -20,
                                node(40),
                                node(-5)),
                        node(
                                30,
                                node(50),
                                node(60)));

        tests.add(
                new TestCase(
                        "N2",
                        mixed,
                        160,
                        true,
                        "mixed positive and negative values"));

        /*
         * ============================================================
         * Left-Skewed Tree
         * ============================================================
         *
         *          10
         *         /
         *        20
         *       /
         *      30
         *     /
         *    40
         *
         * Best = 20 + 40 = 60
         */

        Node left40 =
                node(40);

        Node left30 =
                node(
                        30,
                        left40,
                        null);

        Node left20 =
                node(
                        20,
                        left30,
                        null);

        Node left10 =
                node(
                        10,
                        left20,
                        null);

        tests.add(
                new TestCase(
                        "A1",
                        left10,
                        60,
                        true,
                        "left-skewed tree"));

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
         *
         * Best = 20 + 40 = 60
         */

        Node right40 =
                node(40);

        Node right30 =
                node(
                        30,
                        null,
                        right40);

        Node right20 =
                node(
                        20,
                        null,
                        right30);

        Node right10 =
                node(
                        10,
                        null,
                        right20);

        tests.add(
                new TestCase(
                        "A2",
                        right10,
                        60,
                        true,
                        "right-skewed tree"));

        /*
         * ============================================================
         * Perfect Tree
         * ============================================================
         *
         *                 10
         *              /      \
         *             20       30
         *            /  \     /  \
         *           1    2   3    4
         *
         * Best = 20 + 30 + 1 + 2 + 3 + 4
         *
         *             = 60
         *
         * Because 20 and 30 are selected, their children cannot be
         * selected.
         *
         * Therefore actual best is 50.
         */

        Node perfect =
                node(
                        10,
                        node(
                                20,
                                node(1),
                                node(2)),
                        node(
                                30,
                                node(3),
                                node(4)));

        tests.add(
                new TestCase(
                        "P1",
                        perfect,
                        50,
                        true,
                        "perfect binary tree"));

        /*
         * ============================================================
         * Root Excluded, Every Child Selected
         * ============================================================
         *
         *             1
         *           /   \
         *         100   100
         *
         * Best = 200
         */

        Node children =
                node(
                        1,
                        node(100),
                        node(100));

        tests.add(
                new TestCase(
                        "B5",
                        children,
                        200,
                        true,
                        "both children selected"));

        return tests;
    }

    /* **********************************************************************
     * Random Tree Generation
     * **********************************************************************/

    /**
     * Generate a random binary tree.
     *
     * Unlike the BST example, this problem does not require ordering,
     * so nodes are attached randomly as left/right children.
     */
    static Node randomTree(
            Random random,
            int size) {

        if (size <= 0) {
            return null;
        }

        Node root =
                node(
                        random.nextInt(101)
                                - 50);

        List<Node> available =
                new ArrayList<>();

        available.add(root);

        for (int i = 1;
             i < size;
             i++) {

            Node newNode =
                    node(
                            random.nextInt(101)
                                    - 50);

            /*
             * Find a random node that has an available child position.
             */
            while (true) {

                Node parent =
                        available.get(
                                random.nextInt(
                                        available.size()));

                boolean putLeft =
                        random.nextBoolean();

                if (putLeft
                        && parent.left == null) {

                    parent.left =
                            newNode;

                    break;
                }

                if (!putLeft
                        && parent.right == null) {

                    parent.right =
                            newNode;

                    break;
                }
            }

            available.add(
                    newNode);

            /*
             * A node with both child positions occupied no longer needs
             * to remain in the available list.
             */
            available.removeIf(
                    n -> n.left != null
                            && n.right != null);
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
                new Random(20261005L);

        int passed = 0;

        for (int iteration = 1;
             iteration <= iterations;
             iteration++) {

            /*
             * Keep the trees small because the brute-force reference
             * implementation is intentionally exponential.
             */
            int size =
                    1 + random.nextInt(12);

            Node root =
                    randomTree(
                            random,
                            size);

            int expected =
                    bruteForceMaximum(
                            root);

            for (MethodCase method :
                    methods) {

                Result result =
                        method.algorithm.solve(
                                root);

                if (!result.valid()
                        || result.sum() != expected) {

                    System.out.println(
                            "Randomised test FAILED");

                    System.out.println(
                            "iteration = "
                                    + iteration);

                    System.out.println(
                            "tree size = "
                                    + size);

                    System.out.println(
                            "algorithm = "
                                    + method.name);

                    System.out.println(
                            "expected = "
                                    + expected);

                    System.out.println(
                            "actual = "
                                    + result.sum());

                    System.out.println(
                            "valid = "
                                    + result.valid());

                    return;
                }
            }

            passed++;
        }

        System.out.printf(
                "All %d randomised tests passed.%n%n",
                passed);
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
                "############  MAX SUM NON-ADJACENT NODES  ##################");

        System.out.println(
                "############################################################");

        System.out.println();

        List<MethodCase> methods =
                List.of(

                        new MethodCase(
                                "Plain Recursion",
                                MaxSumNonAdjacentNodes
                                        ::maxSumNonAdjacentNodesRecursion),

                        new MethodCase(
                                "Memoisation",
                                MaxSumNonAdjacentNodes
                                        ::maxSumNonAdjacentNodesMemoisation),

                        new MethodCase(
                                "Include / Exclude",
                                MaxSumNonAdjacentNodes
                                        ::maxSumNonAdjacentNodesIncludeExclude)
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
