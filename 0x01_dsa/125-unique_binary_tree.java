/**
 * Count the number of structurally unique Binary Search Trees (BSTs)
 * that can be formed using n distinct keys.
 *
 * For n distinct keys, the number of structurally unique BSTs is the
 * nth Catalan number:
 *
 *                 n
 *                ---
 *                \
 *      C(n) =     >  C(i - 1) * C(n - i)
 *                /
 *                ---
 *                i = 1
 *
 * Base cases:
 *
 *      C(0) = 1
 *      C(1) = 1
 *
 * Examples:
 *
 *      n = 0  -> 1
 *      n = 1  -> 1
 *      n = 2  -> 2
 *      n = 3  -> 5
 *      n = 4  -> 14
 *      n = 5  -> 42
 *
 * Implementations:
 *
 * 1. Recursive
 *      Time:  O(3^n / n^(3/2)) approximately
 *      Space: O(n) recursion stack
 *
 * 2. Memoised
 *      Time:  O(n^2)
 *      Space: O(n)
 *
 * 3. Tabulated
 *      Time:  O(n^2)
 *      Space: O(n)
 *
 * The recursive implementation directly expresses the recurrence but
 * recalculates the same subproblems repeatedly.
 *
 * The memoised implementation stores previously calculated subproblems.
 *
 * The tabulated implementation calculates each result from the smallest
 * subproblems upwards.
 *
 * This implementation uses long rather than int because Catalan numbers
 * grow very quickly.
 *
 * C(35) fits within a signed long.
 * C(36) does not.
 */
public class UniqueBST {

    /* **********************************************************************
     * Constants
     * **********************************************************************/

    /**
     * Largest n for which the Catalan number fits in a signed long.
     */
    static final int MAX_LONG_N = 35;

    /* **********************************************************************
     * Node
     * **********************************************************************/

    /**
     * Binary-tree node.
     *
     * The Node class is included because the Unique BST problem can
     * eventually be extended from counting BSTs to constructing the
     * actual BST structures.
     *
     * The current counting implementations do not require Node objects.
     */
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
     * Helpers
     * **********************************************************************/

    /**
     * Create a node without children.
     */
    static Node node(int value) {

        return new Node(value);
    }

    /**
     * Create a node with optional children.
     */
    static Node node(
            int value,
            Node left,
            Node right) {

        return new Node(left, right, value);
    }

    /**
     * Determine whether a node is a leaf.
     */
    static boolean isLeaf(Node root) {

        return root != null
                && root.left == null
                && root.right == null;
    }

    /**
     * Count the number of nodes in a tree.
     *
     * This helper is included for consistency with the test style used
     * elsewhere in the project and can be useful when the implementation
     * is later extended to construct actual BSTs.
     */
    static int treeSize(Node root) {

        if (root == null) {
            return 0;
        }

        return 1
                + treeSize(root.left)
                + treeSize(root.right);
    }

    /**
     * Create a human-readable representation of a tree.
     */
    static String treeDescription(Node root) {

        if (root == null) {
            return "null";
        }

        if (isLeaf(root)) {
            return String.valueOf(root.val);
        }

        return root.val
                + "("
                + treeDescription(root.left)
                + ", "
                + treeDescription(root.right)
                + ")";
    }

    /* **********************************************************************
     * Validation
     * **********************************************************************/

    /**
     * Validate input.
     *
     * A negative number of nodes is invalid.
     *
     * Values greater than MAX_LONG_N are rejected because their Catalan
     * numbers cannot be represented by a signed long.
     */
    static void validateInput(int n) {

        if (n < 0) {

            throw new IllegalArgumentException(
                    "Number of nodes cannot be negative: " + n);
        }

        if (n > MAX_LONG_N) {

            throw new IllegalArgumentException(
                    "Catalan number C("
                            + n
                            + ") cannot be represented by a long.");
        }
    }

    /* **********************************************************************
     * Recursive Implementation
     * **********************************************************************/

    /**
     * Count the number of structurally unique BSTs using recursion.
     *
     * For every possible root:
     *
     *      left subtree  = root - 1 nodes
     *      right subtree = n - root nodes
     *
     * Therefore:
     *
     *      C(n) =
     *
     *          C(0) * C(n - 1)
     *        + C(1) * C(n - 2)
     *        + ...
     *        + C(n - 1) * C(0)
     *
     * Base cases:
     *
     *      C(0) = 1
     *      C(1) = 1
     */
    static long uniqueBSTRecursion(int n) {

        validateInput(n);

        /*
         * There is exactly one empty BST and one BST containing
         * a single node.
         */
        if (n <= 1) {
            return 1;
        }

        long unique = 0;

        /*
         * Try every node as the root.
         */
        for (int root = 1; root <= n; root++) {

            int leftNodes = root - 1;
            int rightNodes = n - root;

            unique +=
                    uniqueBSTRecursion(leftNodes)
                            * uniqueBSTRecursion(rightNodes);
        }

        return unique;
    }

    /* **********************************************************************
     * Memoised Implementation
     * **********************************************************************/

    /**
     * Count the number of structurally unique BSTs using top-down
     * dynamic programming with memoisation.
     *
     * Each subproblem is calculated at most once.
     */
    static long uniqueBSTMemoised(int n) {

        validateInput(n);

        long[] memo = new long[n + 1];

        /*
         * -1 means that the subproblem has not yet been calculated.
         *
         * Catalan numbers are always non-negative, so -1 is a safe
         * sentinel value.
         */
        java.util.Arrays.fill(memo, -1);

        /*
         * Base cases.
         */
        memo[0] = 1;

        if (n >= 1) {
            memo[1] = 1;
        }

        return uniqueBSTMemoised(n, memo);
    }

    /**
     * Recursive helper for the memoised implementation.
     */
    private static long uniqueBSTMemoised(
            int n,
            long[] memo) {

        /*
         * Return the cached result when it is already available.
         */
        if (memo[n] != -1) {
            return memo[n];
        }

        long unique = 0;

        /*
         * Try every possible root.
         */
        for (int root = 1; root <= n; root++) {

            int leftNodes = root - 1;
            int rightNodes = n - root;

            unique +=
                    uniqueBSTMemoised(leftNodes, memo)
                            * uniqueBSTMemoised(rightNodes, memo);
        }

        /*
         * Store the result so that the same subproblem does not need
         * to be calculated again.
         */
        memo[n] = unique;

        return unique;
    }

    /* **********************************************************************
     * Tabulation Implementation
     * **********************************************************************/

    /**
     * Count the number of structurally unique BSTs using bottom-up
     * dynamic programming.
     *
     * dp[i] represents the number of structurally unique BSTs that
     * can be constructed using exactly i nodes.
     */
    static long uniqueBSTTabulation(int n) {

        validateInput(n);

        long[] dp = new long[n + 1];

        /*
         * Base cases:
         *
         * dp[0] = 1
         * dp[1] = 1
         */
        dp[0] = 1;

        if (n >= 1) {
            dp[1] = 1;
        }

        /*
         * Build the table from smaller trees to larger trees.
         */
        for (int nodes = 2; nodes <= n; nodes++) {

            /*
             * Consider every possible root.
             */
            for (int root = 1; root <= nodes; root++) {

                int leftNodes = root - 1;
                int rightNodes = nodes - root;

                dp[nodes] +=
                        dp[leftNodes]
                                * dp[rightNodes];
            }
        }

        return dp[n];
    }

    /* **********************************************************************
     * Independent Catalan Calculation
     * **********************************************************************/

    /**
     * Calculate the nth Catalan number independently.
     *
     * The recurrence used here is:
     *
     *      C(0) = 1
     *
     *      C(n) = C(n - 1) * (4n - 2) / (n + 1)
     *
     * This method is deliberately separate from the three BST
     * implementations so that the test suite has an independent
     * source of expected values.
     */
    static long catalan(int n) {

        validateInput(n);

        long result = 1;

        for (int i = 1; i <= n; i++) {

            result =
                    result * (4L * i - 2)
                            / (i + 1);
        }

        return result;
    }

    /* **********************************************************************
     * Test Case
     * **********************************************************************/

    static class TestCase {

        final String id;
        final int n;
        final long expected;
        final String description;

        TestCase(
                String id,
                int n,
                long expected,
                String description) {

            this.id = id;
            this.n = n;
            this.expected = expected;
            this.description = description;
        }
    }

    /* **********************************************************************
     * Deterministic Tests
     * **********************************************************************/

    /**
     * Run deterministic tests against all implementations.
     */
    static void runTests() {

        System.out.println(
                "======================================================");

        System.out.println(
                "Unique BST Tests");

        System.out.println(
                "======================================================");

        java.util.List<TestCase> tests =
                java.util.List.of(

                        /*
                         * T1 - Zero nodes.
                         */
                        new TestCase(
                                "T1",
                                0,
                                1,
                                "empty tree"),

                        /*
                         * T2 - One node.
                         */
                        new TestCase(
                                "T2",
                                1,
                                1,
                                "single-node tree"),

                        /*
                         * T3 - Two nodes.
                         *
                         * Two possible structures:
                         *
                         *      1       2
                         *       \     /
                         *        2   1
                         */
                        new TestCase(
                                "T3",
                                2,
                                2,
                                "two nodes"),

                        /*
                         * T4 - Three nodes.
                         */
                        new TestCase(
                                "T4",
                                3,
                                5,
                                "three nodes"),

                        /*
                         * T5 - Four nodes.
                         */
                        new TestCase(
                                "T5",
                                4,
                                14,
                                "four nodes"),

                        /*
                         * T6 - Five nodes.
                         */
                        new TestCase(
                                "T6",
                                5,
                                42,
                                "five nodes"),

                        /*
                         * T7 - Six nodes.
                         */
                        new TestCase(
                                "T7",
                                6,
                                132,
                                "six nodes"),

                        /*
                         * T8 - Seven nodes.
                         */
                        new TestCase(
                                "T8",
                                7,
                                429,
                                "seven nodes"),

                        /*
                         * T9 - Eight nodes.
                         */
                        new TestCase(
                                "T9",
                                8,
                                1430,
                                "eight nodes"),

                        /*
                         * T10 - Ten nodes.
                         */
                        new TestCase(
                                "T10",
                                10,
                                16796,
                                "ten nodes"),

                        /*
                         * T11 - Fifteen nodes.
                         */
                        new TestCase(
                                "T11",
                                15,
                                9694845,
                                "fifteen nodes"),

                        /*
                         * T12 - Twenty nodes.
                         *
                         * This value exceeds Integer.MAX_VALUE,
                         * which is why the implementation uses long.
                         */
                        new TestCase(
                                "T12",
                                20,
                                6564120420L,
                                "twenty nodes"),

                        /*
                         * T13 - Thirty nodes.
                         */
                        new TestCase(
                                "T13",
                                30,
                                3814986502092304L,
                                "thirty nodes"),

                        /*
                         * T14 - Maximum long-safe input.
                         */
                        new TestCase(
                                "T14",
                                35,
                                3116285494907301262L,
                                "maximum long-safe input")
                );

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            /*
             * The naive recursive implementation becomes expensive
             * very quickly.
             *
             * It is therefore tested only up to n = 15.
             */
            if (test.n <= 15) {

                try {

                    long actual =
                            uniqueBSTRecursion(test.n);

                    if (actual == test.expected) {

                        passed++;

                        System.out.printf(
                                "PASS %s - recursive (%s)%n",
                                test.id,
                                test.description);

                    } else {

                        failed++;

                        printFailure(
                                test,
                                "recursive",
                                actual);
                    }

                } catch (Exception ex) {

                    failed++;

                    printException(
                            test,
                            "recursive",
                            ex);
                }
            }

            /*
             * Memoised implementation.
             */
            try {

                long actual =
                        uniqueBSTMemoised(test.n);

                if (actual == test.expected) {

                    passed++;

                    System.out.printf(
                            "PASS %s - memoised (%s)%n",
                            test.id,
                            test.description);

                } else {

                    failed++;

                    printFailure(
                            test,
                            "memoised",
                            actual);
                }

            } catch (Exception ex) {

                failed++;

                printException(
                        test,
                        "memoised",
                        ex);
            }

            /*
             * Tabulated implementation.
             */
            try {

                long actual =
                        uniqueBSTTabulation(test.n);

                if (actual == test.expected) {

                    passed++;

                    System.out.printf(
                            "PASS %s - tabulation (%s)%n",
                            test.id,
                            test.description);

                } else {

                    failed++;

                    printFailure(
                            test,
                            "tabulation",
                            actual);
                }

            } catch (Exception ex) {

                failed++;

                printException(
                        test,
                        "tabulation",
                        ex);
            }
        }

        System.out.println();

        System.out.printf(
                "Results: %d passed, %d failed%n",
                passed,
                failed);

        System.out.println();
    }

    /* **********************************************************************
     * Failure Reporting
     * **********************************************************************/

    /**
     * Print a failed test.
     */
    static void printFailure(
            TestCase test,
            String implementation,
            long actual) {

        System.out.printf(
                "FAIL %s - %s (%s)%n",
                test.id,
                implementation,
                test.description);

        System.out.println(
                "  n        = "
                        + test.n);

        System.out.println(
                "  expected = "
                        + test.expected);

        System.out.println(
                "  actual   = "
                        + actual);
    }

    /**
     * Print an exception raised during a test.
     */
    static void printException(
            TestCase test,
            String implementation,
            Exception ex) {

        System.out.printf(
                "FAIL %s - %s (%s)%n",
                test.id,
                implementation,
                test.description);

        System.out.println(
                "  n         = "
                        + test.n);

        System.out.println(
                "  exception = "
                        + ex);
    }

    /* **********************************************************************
     * Cross-Implementation Tests
     * **********************************************************************/

    /**
     * Verify that the memoised and tabulated implementations produce
     * identical results for every long-safe input.
     */
    static void runCrossImplementationTests() {

        System.out.println(
                "======================================================");

        System.out.println(
                "Cross-Implementation Tests");

        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        for (int n = 0; n <= MAX_LONG_N; n++) {

            try {

                long memoised =
                        uniqueBSTMemoised(n);

                long tabulation =
                        uniqueBSTTabulation(n);

                if (memoised == tabulation) {

                    passed++;

                } else {

                    failed++;

                    System.out.printf(
                            "FAIL C%d - n = %d%n",
                            n,
                            n);

                    System.out.println(
                            "  memoised   = "
                                    + memoised);

                    System.out.println(
                            "  tabulation = "
                                    + tabulation);
                }

            } catch (Exception ex) {

                failed++;

                System.out.printf(
                        "FAIL C%d - n = %d%n",
                        n,
                        n);

                System.out.println(
                        "  exception = "
                                + ex);
            }
        }

        System.out.printf(
                "Cross-check: %d passed, %d failed%n",
                passed,
                failed);

        System.out.println();
    }

    /* **********************************************************************
     * Recursive Cross-Implementation Tests
     * **********************************************************************/

    /**
     * Compare the naive recursive implementation against the memoised
     * implementation for small inputs.
     *
     * This specifically checks that memoisation has not changed the
     * underlying recurrence.
     */
    static void runRecursiveCrossCheck() {

        System.out.println(
                "======================================================");

        System.out.println(
                "Recursive Cross-Check");

        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        /*
         * Keep this deliberately small because the naive recursive
         * implementation has exponential time complexity.
         */
        for (int n = 0; n <= 15; n++) {

            try {

                long recursive =
                        uniqueBSTRecursion(n);

                long memoised =
                        uniqueBSTMemoised(n);

                if (recursive == memoised) {

                    passed++;

                    System.out.printf(
                            "PASS R%d - n = %d%n",
                            n,
                            n);

                } else {

                    failed++;

                    System.out.printf(
                            "FAIL R%d - n = %d%n",
                            n,
                            n);

                    System.out.println(
                            "  recursive = "
                                    + recursive);

                    System.out.println(
                            "  memoised  = "
                                    + memoised);
                }

            } catch (Exception ex) {

                failed++;

                System.out.printf(
                        "FAIL R%d - n = %d%n",
                        n,
                        n);

                System.out.println(
                        "  exception = "
                                + ex);
            }
        }

        System.out.println();

        System.out.printf(
                "Recursive cross-check: %d passed, %d failed%n",
                passed,
                failed);

        System.out.println();
    }

    /* **********************************************************************
     * Invalid Input Tests
     * **********************************************************************/

    /**
     * Verify that invalid inputs are rejected consistently by all
     * implementations.
     */
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
         * I1 - Negative input.
         */
        if (expectIllegalArgumentException(
                "I1",
                -1,
                "negative input")) {

            passed++;

        } else {

            failed++;
        }

        /*
         * I2 - First value beyond long-safe range.
         */
        if (expectIllegalArgumentException(
                "I2",
                36,
                "value beyond long-safe range")) {

            passed++;

        } else {

            failed++;
        }

        /*
         * I3 - Very large input.
         */
        if (expectIllegalArgumentException(
                "I3",
                Integer.MAX_VALUE,
                "extremely large input")) {

            passed++;

        } else {

            failed++;
        }

        System.out.println();

        System.out.printf(
                "Invalid-input results: %d passed, %d failed%n",
                passed,
                failed);

        System.out.println();
    }

    /**
     * Verify that every implementation rejects the supplied input.
     */
    static boolean expectIllegalArgumentException(
            String id,
            int n,
            String description) {

        boolean recursiveRejected = false;
        boolean memoisedRejected = false;
        boolean tabulationRejected = false;

        try {

            uniqueBSTRecursion(n);

        } catch (IllegalArgumentException ex) {

            recursiveRejected = true;
        }

        try {

            uniqueBSTMemoised(n);

        } catch (IllegalArgumentException ex) {

            memoisedRejected = true;
        }

        try {

            uniqueBSTTabulation(n);

        } catch (IllegalArgumentException ex) {

            tabulationRejected = true;
        }

        boolean passed =
                recursiveRejected
                        && memoisedRejected
                        && tabulationRejected;

        if (passed) {

            System.out.printf(
                    "PASS %s (%s)%n",
                    id,
                    description);

        } else {

            System.out.printf(
                    "FAIL %s (%s)%n",
                    id,
                    description);

            System.out.println(
                    "  recursive rejected  = "
                            + recursiveRejected);

            System.out.println(
                    "  memoised rejected   = "
                            + memoisedRejected);

            System.out.println(
                    "  tabulation rejected = "
                            + tabulationRejected);
        }

        return passed;
    }

    /* **********************************************************************
     * Independent Catalan Verification
     * **********************************************************************/

    /**
     * Verify the tabulated implementation against an independent
     * Catalan-number recurrence.
     *
     * This helps catch an error shared by multiple implementations.
     */
    static void runCatalanVerificationTests() {

        System.out.println(
                "======================================================");

        System.out.println(
                "Independent Catalan Verification");

        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        for (int n = 0; n <= MAX_LONG_N; n++) {

            try {

                long expected =
                        catalan(n);

                long actual =
                        uniqueBSTTabulation(n);

                if (actual == expected) {

                    passed++;

                } else {

                    failed++;

                    System.out.printf(
                            "FAIL A%d - n = %d%n",
                            n,
                            n);

                    System.out.println(
                            "  Catalan = "
                                    + expected);

                    System.out.println(
                            "  BST     = "
                                    + actual);
                }

            } catch (Exception ex) {

                failed++;

                System.out.printf(
                        "FAIL A%d - n = %d%n",
                        n,
                        n);

                System.out.println(
                        "  exception = "
                                + ex);
            }
        }

        System.out.println();

        System.out.printf(
                "Catalan verification: %d passed, %d failed%n",
                passed,
                failed);

        System.out.println();
    }

    /* **********************************************************************
     * Edge-Case Tests
     * **********************************************************************/

    /**
     * Verify important boundary values explicitly.
     */
    static void runEdgeCaseTests() {

        System.out.println(
                "======================================================");

        System.out.println(
                "Edge-Case Tests");

        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        /*
         * E1 - Empty tree.
         */
        if (uniqueBSTTabulation(0) == 1) {

            passed++;

            System.out.println(
                    "PASS E1 - empty tree has one representation");

        } else {

            failed++;

            System.out.println(
                    "FAIL E1 - empty tree");
        }

        /*
         * E2 - Single node.
         */
        if (uniqueBSTTabulation(1) == 1) {

            passed++;

            System.out.println(
                    "PASS E2 - single-node tree has one representation");

        } else {

            failed++;

            System.out.println(
                    "FAIL E2 - single-node tree");
        }

        /*
         * E3 - First non-trivial case.
         */
        if (uniqueBSTTabulation(2) == 2) {

            passed++;

            System.out.println(
                    "PASS E3 - two nodes produce two structures");

        } else {

            failed++;

            System.out.println(
                    "FAIL E3 - two nodes");
        }

        /*
         * E4 - Maximum supported value.
         */
        if (uniqueBSTTabulation(MAX_LONG_N)
                == catalan(MAX_LONG_N)) {

            passed++;

            System.out.println(
                    "PASS E4 - maximum long-safe value");

        } else {

            failed++;

            System.out.println(
                    "FAIL E4 - maximum long-safe value");
        }

        System.out.println();

        System.out.printf(
                "Edge-case results: %d passed, %d failed%n",
                passed,
                failed);

        System.out.println();
    }

    /* **********************************************************************
     * Main
     * **********************************************************************/

    public static void main(String[] args) {

        runTests();

        runCrossImplementationTests();

        runRecursiveCrossCheck();

        runInvalidInputTests();

        runCatalanVerificationTests();

        runEdgeCaseTests();

        System.out.println(
                "======================================================");

        System.out.println(
                "All tests completed.");

        System.out.println(
                "======================================================");
    }
}
