import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Subtree of Another Binary Tree.
 *
 * Given two binary trees, determine whether root2 is a subtree of root1.
 *
 * A tree is considered a subtree when an entire tree rooted at some node
 * in root1 is structurally identical to root2.
 *
 * Example:
 *
 *              1
 *             / \
 *            2   3
 *           / \
 *          4   5
 *
 * root2:
 *
 *            2
 *           / \
 *          4   5
 *
 * root2 is a subtree of root1.
 *
 * Three implementations are provided:
 *
 * 1. Recursive tree matching
 *      Search every possible node in root1 and compare trees directly.
 *
 *      Time:  O(n * m) in the worst case
 *      Space: O(h1 + h2) for recursion
 *
 * 2. Serialisation + substring matching
 *      Serialise both trees using explicit null markers and use
 *      String.contains() to search for root2's serialisation.
 *
 *      Time:  O(n + m) for serialisation plus the substring search
 *             performed by the Java String implementation.
 *      Space: O(n + m)
 *
 * 3. Serialisation + KMP
 *      Serialise both trees and use the Knuth-Morris-Pratt algorithm
 *      to search for root2's serialisation inside root1's serialisation.
 *
 *      Time:  O(n + m)
 *      Space: O(n + m)
 *
 * Empty-tree convention:
 *
 *     An empty root2 is considered a subtree of any tree.
 *     An empty root1 is a subtree only when root2 is also empty.
 *
 * Null markers are essential during serialisation.
 *
 * For example, these trees are different:
 *
 *          1          1
 *         /            \
 *        2              2
 *
 * Their serialisations must not be allowed to become identical.
 */
public class SubTreeOfAnotherBinaryTree {

    /* **********************************************************************
     * Node / Result
     * **********************************************************************/

    static class Node {

        int val;
        Node left;
        Node right;

        Node(int val) {
            this.val = val;
            this.left = null;
            this.right = null;
        }

        Node(Node left, Node right, int val) {
            this.left = left;
            this.right = right;
            this.val = val;
        }
    }

    /**
     * Result returned by every subtree implementation.
     *
     * isSubTree:
     *     true when root2 is a subtree of root1.
     *
     * valid:
     *     true when the input satisfies the method's input contract.
     *
     * In this problem, a null root is a valid tree input. The valid flag
     * therefore describes whether the input is acceptable rather than
     * whether the subtree exists.
     */
    static record Result(boolean isSubTree, boolean valid) {}

    /* **********************************************************************
     * Validation
     * **********************************************************************/

    /**
     * Determines whether a node reference is non-null.
     */
    static boolean validRoot(Node root) {
        return root != null;
    }

    /**
     * Determines whether the pair of trees is a valid input.
     *
     * Both null and non-null roots are valid for this problem.
     */
    static boolean validInput(Node root1, Node root2) {
        return true;
    }

    /* **********************************************************************
     * Implementation 1: Recursive Tree Matching
     * **********************************************************************/

    /**
     * Determines whether root2 is a subtree of root1 using direct
     * recursive tree comparison.
     *
     * The algorithm searches every node in root1. At each node it checks
     * whether the tree rooted there is identical to root2.
     *
     * Example:
     *
     *              1
     *             / \
     *            2   3
     *           / \
     *          4   5
     *
     * If root2 is:
     *
     *            2
     *           / \
     *          4   5
     *
     * the algorithm first compares root1 with root2, then root1.left
     * with root2, where the trees are identical.
     */
    static Result subTreeOfBinaryTreeRecursion(
            Node root1,
            Node root2) {

        if (!validInput(root1, root2)) {
            return new Result(false, false);
        }

        /*
         * An empty tree is a subtree of every tree.
         */
        if (root2 == null) {
            return new Result(true, true);
        }

        /*
         * root2 is non-null but root1 is empty.
         */
        if (root1 == null) {
            return new Result(false, true);
        }

        /*
         * Check whether root2 is exactly the tree rooted at root1.
         */
        if (areIdenticalPreOrder(root1, root2)) {
            return new Result(true, true);
        }

        /*
         * Otherwise search the left and right subtrees of root1.
         */
        Result leftResult =
                subTreeOfBinaryTreeRecursion(
                        root1.left,
                        root2);

        if (leftResult.isSubTree()) {
            return new Result(true, true);
        }

        Result rightResult =
                subTreeOfBinaryTreeRecursion(
                        root1.right,
                        root2);

        return new Result(
                rightResult.isSubTree(),
                true);
    }

    /**
     * Determines whether two trees are structurally identical and contain
     * the same values.
     */
    static boolean areIdenticalPreOrder(
            Node root1,
            Node root2) {

        /*
         * Both trees have ended at the same position.
         */
        if (root1 == null && root2 == null) {
            return true;
        }

        /*
         * Exactly one tree has ended.
         */
        if (root1 == null || root2 == null) {
            return false;
        }

        /*
         * Values must match and both child subtrees must match.
         */
        return root1.val == root2.val
                && areIdenticalPreOrder(
                        root1.left,
                        root2.left)
                && areIdenticalPreOrder(
                        root1.right,
                        root2.right);
    }

    /* **********************************************************************
     * Serialisation
     * **********************************************************************/

    /**
     * Serialises a tree using preorder traversal.
     *
     * A null marker is included for every missing child.
     *
     * Example:
     *
     *              1
     *             / \
     *            2   3
     *
     * becomes conceptually:
     *
     *     1,2,#,#,3,#,#,
     *
     * The null markers preserve the tree's structure.
     *
     * The comma delimiter also prevents values such as 1 and 11 from
     * accidentally matching as arbitrary substrings.
     */
    static void serialise(
            Node root,
            StringBuilder string) {

        if (root == null) {
            string.append("#,");
            return;
        }

        string
                .append(root.val)
                .append(',');

        serialise(root.left, string);
        serialise(root.right, string);
    }

    /**
     * Returns the serialised representation of a tree.
     */
    static String serialise(Node root) {

        StringBuilder string =
                new StringBuilder();

        serialise(root, string);

        return string.toString();
    }

    /* **********************************************************************
     * Implementation 2: Serialisation + Substring Matching
     * **********************************************************************/

    /**
     * Determines whether root2 is a subtree of root1 by serialising both
     * trees and searching for root2's serialisation within root1's
     * serialisation.
     *
     * Explicit null markers make the serialisation structurally unique.
     */
    static Result subTreeOfBinaryTreeSubStringMatching(
            Node root1,
            Node root2) {

        if (!validInput(root1, root2)) {
            return new Result(false, false);
        }

        /*
         * An empty root2 is a subtree of every tree.
         */
        if (root2 == null) {
            return new Result(true, true);
        }

        /*
         * A non-empty root2 cannot occur in an empty root1.
         */
        if (root1 == null) {
            return new Result(false, true);
        }

        String string1 =
                serialise(root1);

        String string2 =
                serialise(root2);

        return new Result(
                string1.contains(string2),
                true);
    }

    /* **********************************************************************
     * Implementation 3: Serialisation + KMP
     * **********************************************************************/

    /**
     * Determines whether root2 is a subtree of root1 using serialisation
     * followed by the Knuth-Morris-Pratt string-search algorithm.
     */
    static Result subTreeOfBinaryTreeKMPAlgo(
            Node root1,
            Node root2) {

        if (!validInput(root1, root2)) {
            return new Result(false, false);
        }

        /*
         * An empty root2 is a subtree of every tree.
         */
        if (root2 == null) {
            return new Result(true, true);
        }

        /*
         * A non-empty root2 cannot occur in an empty root1.
         */
        if (root1 == null) {
            return new Result(false, true);
        }

        String string1 =
                serialise(root1);

        String string2 =
                serialise(root2);

        return kmpSearch(
                string1,
                string2);
    }

    /**
     * Searches for pattern inside text using the KMP algorithm.
     *
     * KMP avoids repeatedly comparing characters that have already been
     * matched.
     */
    static Result kmpSearch(
            String text,
            String pattern) {

        if (pattern.isEmpty()) {
            return new Result(true, true);
        }

        if (text.isEmpty()) {
            return new Result(false, true);
        }

        int[] lps =
                buildLPS(pattern);

        int i = 0;
        int j = 0;

        while (i < text.length()) {

            if (text.charAt(i) == pattern.charAt(j)) {
                i++;
                j++;

                if (j == pattern.length()) {
                    return new Result(true, true);
                }

            } else if (j != 0) {

                /*
                 * Use the longest proper prefix that is also a suffix.
                 */
                j = lps[j - 1];

            } else {

                i++;
            }
        }

        return new Result(false, true);
    }

    /**
     * Builds the Longest Proper Prefix which is also a Suffix array used
     * by KMP.
     */
    static int[] buildLPS(String pattern) {

        int n =
                pattern.length();

        int[] lps =
                new int[n];

        int length = 0;
        int i = 1;

        while (i < n) {

            if (pattern.charAt(i)
                    == pattern.charAt(length)) {

                length++;

                lps[i] =
                        length;

                i++;

            } else if (length != 0) {

                length =
                        lps[length - 1];

            } else {

                lps[i] = 0;

                i++;
            }
        }

        return lps;
    }

    /* **********************************************************************
     * Test Harness Types
     * **********************************************************************/

    static class TestCase {

        final String id;
        final Node root1;
        final Node root2;
        final boolean expectedIsSubTree;
        final boolean expectedValid;
        final String description;

        TestCase(
                String id,
                Node root1,
                Node root2,
                boolean expectedIsSubTree,
                boolean expectedValid,
                String description) {

            this.id = id;
            this.root1 = root1;
            this.root2 = root2;
            this.expectedIsSubTree = expectedIsSubTree;
            this.expectedValid = expectedValid;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {

        Result solve(Node root1, Node root2);
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

        return new Node(
                left,
                right,
                val);
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
     * Returns:
     *
     *            2
     *           / \
     *          4   5
     */
    static Node standardExampleSubTree() {

        return node(
                2,
                leaf(4),
                leaf(5));
    }

    /**
     * Returns a subtree that occurs at the root.
     */
    static Node rootSubTreeExample() {

        return node(
                1,
                leaf(2),
                leaf(3));
    }

    /**
     * Builds a left-only chain:
     *
     *     1
     *    /
     *   2
     *  /
     * 3
     */
    static Node leftChain(int length) {

        if (length <= 0) {
            return null;
        }

        Node curr = null;

        for (int value = length;
             value >= 1;
             value--) {

            Node next =
                    new Node(value);

            next.left =
                    curr;

            curr =
                    next;
        }

        return curr;
    }

    /**
     * Builds a right-only chain:
     *
     *     1
     *      \
     *       2
     *        \
     *         3
     */
    static Node rightChain(int length) {

        if (length <= 0) {
            return null;
        }

        Node root =
                new Node(1);

        Node curr =
                root;

        for (int value = 2;
             value <= length;
             value++) {

            curr.right =
                    new Node(value);

            curr =
                    curr.right;
        }

        return root;
    }

    /**
     * Builds a tree where the subtree occurs only below the root.
     *
     *              1
     *             / \
     *            2   3
     *           / \
     *          4   5
     *
     * root2:
     *
     *            2
     *           / \
     *          4   5
     */
    static Node nestedTree() {

        return standardExample();
    }

    /**
     * Builds a tree with duplicate values.
     *
     *              1
     *             / \
     *            2   2
     *           /     \
     *          3       4
     */
    static Node duplicateValueTree() {

        return node(
                1,
                node(
                        2,
                        leaf(3),
                        null),
                node(
                        2,
                        null,
                        leaf(4)));
    }

    /* **********************************************************************
     * Tree Comparison Utilities
     * **********************************************************************/

    /**
     * Compares both value and structure.
     */
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

    /**
     * Produces a structural representation useful when a test fails.
     */
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

    /**
     * Deep-copies a tree.
     *
     * The current algorithms do not mutate their input, but copying the
     * inputs keeps the test harness independent and makes it safe if an
     * implementation is changed later.
     */
    static Node copyTree(Node root) {

        if (root == null) {
            return null;
        }

        return node(
                root.val,
                copyTree(root.left),
                copyTree(root.right));
    }

    /* **********************************************************************
     * Result Comparison
     * **********************************************************************/

    static boolean resultsEqual(
            Result actual,
            boolean expectedIsSubTree,
            boolean expectedValid) {

        if (actual == null) {
            return false;
        }

        return actual.isSubTree()
                == expectedIsSubTree
                && actual.valid()
                == expectedValid;
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

        System.out.println(
                algorithmName);

        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            try {

                Result actual =
                        method.solve(
                                copyTree(test.root1),
                                copyTree(test.root2));

                if (resultsEqual(
                        actual,
                        test.expectedIsSubTree,
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
                            "  expected subtree = %s%n",
                            test.expectedIsSubTree);

                    System.out.printf(
                            "  actual subtree   = %s%n",
                            actual.isSubTree());

                    System.out.printf(
                            "  expected valid   = %s%n",
                            test.expectedValid);

                    System.out.printf(
                            "  actual valid     = %s%n",
                            actual.valid());

                    System.out.printf(
                            "  root1            = %s%n",
                            treeToString(test.root1));

                    System.out.printf(
                            "  root2            = %s%n",
                            treeToString(test.root2));
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
     * Randomised Tree Generation
     * **********************************************************************/

    /**
     * Generates a random binary tree.
     *
     * The generated tree may contain duplicate values. Duplicate values
     * are useful because subtree algorithms must compare structure as well
     * as values.
     */
    static Node randomTree(
            Random rng,
            int maxNodes) {

        if (maxNodes <= 0) {
            return null;
        }

        int budget =
                rng.nextInt(maxNodes + 1);

        if (budget == 0) {
            return null;
        }

        return randomTreeHelper(
                rng,
                new int[]{budget});
    }

    static Node randomTreeHelper(
            Random rng,
            int[] budget) {

        if (budget[0] <= 0) {
            return null;
        }

        budget[0]--;

        /*
         * Keep the value range deliberately small so duplicate values
         * occur regularly.
         */
        Node root =
                new Node(
                        rng.nextInt(8) - 3);

        if (budget[0] <= 0) {
            return root;
        }

        /*
         * Randomly decide whether to create the left child.
         */
        if (rng.nextBoolean()) {

            root.left =
                    randomTreeHelper(
                            rng,
                            budget);
        }

        /*
         * Randomly decide whether to create the right child.
         */
        if (budget[0] > 0
                && rng.nextBoolean()) {

            root.right =
                    randomTreeHelper(
                            rng,
                            budget);
        }

        return root;
    }

    /**
     * Selects a random node from a tree.
     *
     * The returned node is converted into an independent tree copy before
     * being used as a test input.
     */
    static Node randomSubTree(
            Node root,
            Random rng) {

        if (root == null) {
            return null;
        }

        List<Node> nodes =
                new ArrayList<>();

        collectNodes(root, nodes);

        Node selected =
                nodes.get(
                        rng.nextInt(nodes.size()));

        return copyTree(selected);
    }

    static void collectNodes(
            Node root,
            List<Node> nodes) {

        if (root == null) {
            return;
        }

        nodes.add(root);

        collectNodes(
                root.left,
                nodes);

        collectNodes(
                root.right,
                nodes);
    }

    /* **********************************************************************
     * Randomised Cross-Checks
     * **********************************************************************/

    /**
     * Cross-checks all three implementations against one another.
     *
     * For each generated tree:
     *
     * 1. A guaranteed subtree is selected from root1.
     * 2. A random independent tree is also generated as root2.
     * 3. All three implementations must agree.
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
                new Random(20260914L);

        for (int i = 1;
             i <= iterations;
             i++) {

            Node root1 =
                    randomTree(
                            rng,
                            50);

            /*
             * If root1 is null, use an independent random root2.
             * Otherwise half of the time use a guaranteed subtree.
             */
            Node root2;

            if (root1 != null
                    && rng.nextBoolean()) {

                root2 =
                        randomSubTree(
                                root1,
                                rng);

            } else {

                root2 =
                        randomTree(
                                rng,
                                15);
            }

            Result recursion =
                    subTreeOfBinaryTreeRecursion(
                            copyTree(root1),
                            copyTree(root2));

            Result substring =
                    subTreeOfBinaryTreeSubStringMatching(
                            copyTree(root1),
                            copyTree(root2));

            Result kmp =
                    subTreeOfBinaryTreeKMPAlgo(
                            copyTree(root1),
                            copyTree(root2));

            boolean sameRecursionSubstring =
                    recursion.isSubTree()
                            == substring.isSubTree();

            boolean sameRecursionKMP =
                    recursion.isSubTree()
                            == kmp.isSubTree();

            boolean sameValidity =
                    recursion.valid()
                            == substring.valid()
                            && recursion.valid()
                            == kmp.valid();

            if (!sameRecursionSubstring
                    || !sameRecursionKMP
                    || !sameValidity) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "Test number = "
                                + i);

                System.out.println(
                        "root1 = "
                                + treeToString(root1));

                System.out.println(
                        "root2 = "
                                + treeToString(root2));

                System.out.println(
                        "Recursion = "
                                + recursion);

                System.out.println(
                        "Substring = "
                                + substring);

                System.out.println(
                        "KMP = "
                                + kmp);

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
                        leaf(1),
                        leaf(1),
                        true,
                        true,
                        "single node is a subtree of an identical single node"));

        tests.add(
                new TestCase(
                        "B2",
                        leaf(1),
                        leaf(2),
                        false,
                        true,
                        "single nodes with different values are not identical"));

        tests.add(
                new TestCase(
                        "B3",
                        standardExample(),
                        standardExampleSubTree(),
                        true,
                        true,
                        "classic nested subtree"));

        tests.add(
                new TestCase(
                        "B4",
                        standardExample(),
                        rootSubTreeExample(),
                        true,
                        true,
                        "root2 is identical to the entire root1 tree"));

        tests.add(
                new TestCase(
                        "B5",
                        node(
                                1,
                                leaf(2),
                                leaf(3)),
                        leaf(2),
                        true,
                        true,
                        "leaf occurs as the left subtree"));

        tests.add(
                new TestCase(
                        "B6",
                        node(
                                1,
                                leaf(2),
                                leaf(3)),
                        leaf(3),
                        true,
                        true,
                        "leaf occurs as the right subtree"));

        /* ============================================================
         * Structural Cases
         * ============================================================ */

        tests.add(
                new TestCase(
                        "S1",
                        node(
                                1,
                                leaf(2),
                                null),
                        node(
                                1,
                                leaf(2),
                                null),
                        true,
                        true,
                        "same values and same left-child structure"));

        tests.add(
                new TestCase(
                        "S2",
                        node(
                                1,
                                leaf(2),
                                null),
                        node(
                                1,
                                null,
                                leaf(2)),
                        false,
                        true,
                        "same values but different child structure"));

        tests.add(
                new TestCase(
                        "S3",
                        node(
                                1,
                                null,
                                leaf(2)),
                        node(
                                1,
                                leaf(2),
                                null),
                        false,
                        true,
                        "mirror structures are not identical"));

        tests.add(
                new TestCase(
                        "S4",
                        standardExample(),
                        node(
                                2,
                                leaf(4),
                                null),
                        false,
                        true,
                        "partial structure is not considered a subtree"));

        tests.add(
                new TestCase(
                        "S5",
                        standardExample(),
                        node(
                                2,
                                null,
                                leaf(5)),
                        false,
                        true,
                        "incorrect child direction is not a match"));

        /* ============================================================
         * Duplicate Values
         * ============================================================ */

        tests.add(
                new TestCase(
                        "D1",
                        duplicateValueTree(),
                        node(
                                2,
                                leaf(3),
                                null),
                        true,
                        true,
                        "duplicate root values with matching structure"));

        tests.add(
                new TestCase(
                        "D2",
                        duplicateValueTree(),
                        node(
                                2,
                                null,
                                leaf(3)),
                        false,
                        true,
                        "duplicate value but incorrect structure"));

        tests.add(
                new TestCase(
                        "D3",
                        duplicateValueTree(),
                        node(
                                2,
                                null,
                                leaf(4)),
                        true,
                        true,
                        "second duplicate-valued subtree"));

        /* ============================================================
         * Skewed Trees
         * ============================================================ */

        tests.add(
                new TestCase(
                        "K1",
                        leftChain(5),
                        leaf(5),
                        true,
                        true,
                        "root of a left-only chain"));

        tests.add(
                new TestCase(
                        "K2",
                        leftChain(5),
                        node(
                                3,
                                leaf(4),
                                null),
                        true,
                        true,
                        "nested subtree in a left-only chain"));

        tests.add(
                new TestCase(
                        "K3",
                        leftChain(5),
                        rightChain(3),
                        false,
                        true,
                        "left-only and right-only chains are different"));

        /* ============================================================
         * Edge Cases
         * ============================================================ */

        tests.add(
                new TestCase(
                        "E1",
                        null,
                        null,
                        true,
                        true,
                        "empty root2 is a subtree of empty root1"));

        tests.add(
                new TestCase(
                        "E2",
                        standardExample(),
                        null,
                        true,
                        true,
                        "empty root2 is a subtree of a non-empty tree"));

        tests.add(
                new TestCase(
                        "E3",
                        null,
                        leaf(1),
                        false,
                        true,
                        "non-empty root2 cannot be a subtree of an empty tree"));

        tests.add(
                new TestCase(
                        "E4",
                        null,
                        node(
                                1,
                                null,
                                null),
                        false,
                        true,
                        "non-empty root2 cannot occur in an empty root1"));

        /* ============================================================
         * Serialisation-Specific Cases
         * ============================================================ */

        tests.add(
                new TestCase(
                        "X1",
                        node(
                                12,
                                leaf(3),
                                null),
                        leaf(2),
                        false,
                        true,
                        "value 2 must not falsely match value 12"));

        tests.add(
                new TestCase(
                        "X2",
                        node(
                                1,
                                node(
                                        2,
                                        null,
                                        null),
                                null),
                        node(
                                1,
                                null,
                                node(
                                        2,
                                        null,
                                        null)),
                        false,
                        true,
                        "preorder values alone must not ignore structure"));

        /* ============================================================
         * Header
         * ============================================================ */

        System.out.println(
                "############################################################");

        System.out.println(
                "#############  SUBTREE OF ANOTHER BINARY TREE ############");

        System.out.println(
                "############################################################");

        System.out.println();

        /* ============================================================
         * Algorithms
         * ============================================================ */

        List<MethodCase> methods =
                List.of(

                        new MethodCase(
                                "Recursive Tree Matching",
                                SubTreeOfAnotherBinaryTree
                                        ::subTreeOfBinaryTreeRecursion),

                        new MethodCase(
                                "Serialisation + Substring Matching",
                                SubTreeOfAnotherBinaryTree
                                        ::subTreeOfBinaryTreeSubStringMatching),

                        new MethodCase(
                                "Serialisation + KMP",
                                SubTreeOfAnotherBinaryTree
                                        ::subTreeOfBinaryTreeKMPAlgo)
                );

        /* ============================================================
         * Fixed Tests
         * ============================================================ */

        for (MethodCase method : methods) {

            runTests(
                    method.name,
                    method.algorithm,
                    tests);
        }

        /* ============================================================
         * Randomised Cross-Checks
         * ============================================================ */

        runRandomisedTests(5000);
    }
}
