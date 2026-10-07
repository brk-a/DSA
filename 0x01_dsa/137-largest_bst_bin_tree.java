import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Find the largest Binary Search Tree (BST) contained within a binary tree.
 *
 * A BST follows the strict ordering property:
 *
 *      all values in the left subtree < root < all values
 *      in the right subtree
 *
 * Two implementations are provided:
 *
 * 1. Brute Force
 *
 *      Checks each subtree independently and determines whether it is a BST.
 *
 *      Time:  O(n^2) worst case
 *      Space: O(h)
 *
 * 2. BST Property
 *
 *      Uses information returned from the children to determine whether the
 *      current subtree is a BST. This avoids repeatedly traversing subtrees.
 *
 *      Time:  O(n)
 *      Space: O(h)
 *
 * Result:
 *
 *      root  = root of the largest BST contained in the supplied tree
 *      valid = whether the supplied tree itself is a BST
 *
 * If multiple BSTs have the same maximum size, the left-most BST is selected.
 */
public class LargestBSTBinaryTree {

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
            Node root,
            boolean valid) {
    }

    /* **********************************************************************
     * Helpers
     * **********************************************************************/

    static boolean validRoot(
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

    /**
     * Determine whether a tree is a BST using strict lower and upper bounds.
     *
     * The bounds are represented by nullable Integers so that the full
     * integer range can be used without overflow.
     */
    static boolean isBST(
            Node root) {

        return isBST(
                root,
                null,
                null);
    }

    static boolean isBST(
            Node root,
            Integer lowerBound,
            Integer upperBound) {

        if (root == null) {
            return true;
        }

        if (lowerBound != null
                && root.val <= lowerBound) {

            return false;
        }

        if (upperBound != null
                && root.val >= upperBound) {

            return false;
        }

        return isBST(
                root.left,
                lowerBound,
                root.val)
                && isBST(
                        root.right,
                        root.val,
                        upperBound);
    }

    /**
     * Count the number of nodes in a tree.
     */
    static int size(
            Node root) {

        if (root == null) {
            return 0;
        }

        return 1
                + size(root.left)
                + size(root.right);
    }

    /**
     * Find the largest BST in the supplied tree using brute force.
     *
     * Every node is considered as a possible root of a BST. For each node,
     * the entire subtree is checked independently.
     *
     * If the current subtree is a BST, its size is compared with the best
     * BST found so far.
     */
    static Result largestBSTBinaryTreeBruteForce(
            Node root) {

        if (!validRoot(root)) {
            return new Result(
                    null,
                    false);
        }

        Node best =
                largestBSTBruteForce(
                        root);

        return new Result(
                best,
                isBST(root));
    }

    /**
     * Brute-force search.
     *
     * The returned node is the root of the largest BST below the supplied
     * node.
     *
     * Ties are resolved in favour of the left-most BST.
     */
    static Node largestBSTBruteForce(
            Node root) {

        if (root == null) {
            return null;
        }

        /*
         * Search the left and right subtrees first. This gives us a
         * deterministic left-most tie-breaking policy.
         */
        Node leftBest =
                largestBSTBruteForce(
                        root.left);

        Node rightBest =
                largestBSTBruteForce(
                        root.right);

        Node best =
                leftBest;

        int bestSize =
                size(best);

        int rightSize =
                size(rightBest);

        if (rightSize > bestSize) {

            best =
                    rightBest;

            bestSize =
                    rightSize;
        }

        /*
         * If the current subtree is itself a BST, it is at least as large
         * as every BST contained within it. Only replace the existing
         * result when it is strictly larger so that ties remain left-most.
         */
        if (isBST(root)) {

            int currentSize =
                    size(root);

            if (currentSize > bestSize) {

                best =
                        root;
            }
        }

        return best;
    }

    /* **********************************************************************
     * 2. BST Property
     * **********************************************************************/

    /**
     * Internal information returned by the linear-time implementation.
     *
     * min       = minimum value in the subtree
     * max       = maximum value in the subtree
     * size      = number of nodes in the subtree
     * bst       = whether the entire subtree is a BST
     * bestRoot  = root of the largest BST contained in the subtree
     * bestSize  = size of that largest BST
     */
    static class BSTInfo {

        final int min;
        final int max;
        final int size;
        final boolean bst;
        final Node bestRoot;
        final int bestSize;

        BSTInfo(
                int min,
                int max,
                int size,
                boolean bst,
                Node bestRoot,
                int bestSize) {

            this.min = min;
            this.max = max;
            this.size = size;
            this.bst = bst;
            this.bestRoot = bestRoot;
            this.bestSize = bestSize;
        }
    }

    /**
     * Find the largest BST using the BST property.
     *
     * Each node is processed once. Information from the left and right
     * children is used to determine whether the current subtree is a BST.
     *
     * Time: O(n)
     * Space: O(h)
     */
    static Result largestBSTBinaryTreeBSTProp(
            Node root) {

        if (!validRoot(root)) {
            return new Result(
                    null,
                    false);
        }

        BSTInfo info =
                largestBSTBSTProp(root);

        return new Result(
                info.bestRoot,
                info.bst);
    }

    /**
     * Bottom-up implementation.
     */
    static BSTInfo largestBSTBSTProp(
            Node root) {

        /*
         * Empty subtrees are valid BSTs. They do not contribute a node to
         * the size and have no candidate root.
         */
        if (root == null) {

            return new BSTInfo(
                    Integer.MAX_VALUE,
                    Integer.MIN_VALUE,
                    0,
                    true,
                    null,
                    0);
        }

        BSTInfo left =
                largestBSTBSTProp(
                        root.left);

        BSTInfo right =
                largestBSTBSTProp(
                        root.right);

        int subtreeSize =
                left.size
                        + right.size
                        + 1;

        /*
         * The current subtree is a BST only when:
         *
         * 1. The left subtree is a BST.
         * 2. The right subtree is a BST.
         * 3. Every left value is smaller than the current value.
         * 4. Every right value is greater than the current value.
         *
         * Strict comparisons deliberately reject duplicates.
         */
        boolean currentIsBST =
                left.bst
                        && right.bst
                        && (root.left == null
                        || left.max < root.val)
                        && (root.right == null
                        || right.min > root.val);

        if (currentIsBST) {

            return new BSTInfo(
                    Math.min(
                            root.val,
                            left.min),
                    Math.max(
                            root.val,
                            right.max),
                    subtreeSize,
                    true,
                    root,
                    subtreeSize);
        }

        /*
         * The current subtree is not a BST.
         *
         * Therefore the largest BST must come entirely from either the
         * left or right subtree.
         *
         * On equal sizes, retain the left subtree to provide deterministic
         * left-most tie-breaking.
         */
        if (left.bestSize >= right.bestSize) {

            return new BSTInfo(
                    0,
                    0,
                    subtreeSize,
                    false,
                    left.bestRoot,
                    left.bestSize);
        }

        return new BSTInfo(
                0,
                0,
                subtreeSize,
                false,
                right.bestRoot,
                right.bestSize);
    }

    /* **********************************************************************
     * Tree Helpers
     * **********************************************************************/

    /**
     * Find a node by value.
     *
     * This helper is intended for test construction only.
     *
     * For arbitrary binary trees, a normal tree traversal is required rather
     * than BST navigation.
     */
    static Node findNode(
            Node root,
            int value) {

        if (root == null) {
            return null;
        }

        if (root.val == value) {
            return root;
        }

        Node left =
                findNode(
                        root.left,
                        value);

        if (left != null) {
            return left;
        }

        return findNode(
                root.right,
                value);
    }

    /**
     * Find a node by reference identity.
     */
    static boolean containsNode(
            Node root,
            Node target) {

        if (root == null
                || target == null) {

            return false;
        }

        if (root == target) {
            return true;
        }

        return containsNode(
                root.left,
                target)
                || containsNode(
                        root.right,
                        target);
    }

    /**
     * Insert a value into a BST.
     *
     * Duplicate values are inserted into the right subtree. Such a tree
     * is deliberately not considered a strict BST by the algorithms above.
     */
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

    /**
     * Generate a random binary tree rather than a random BST.
     *
     * This is useful because the algorithms must find BSTs inside arbitrary
     * binary trees.
     */
    static Node randomBinaryTree(
            Random random,
            int size) {

        if (size == 0) {
            return null;
        }

        Node root =
                node(
                        random.nextInt(2001)
                                - 1000);

        for (int i = 1;
             i < size;
             i++) {

            Node newNode =
                    node(
                            random.nextInt(2001)
                                    - 1000);

            attachRandomly(
                    random,
                    root,
                    newNode);
        }

        return root;
    }

    /**
     * Attach a node at a randomly selected empty child position.
     */
    static void attachRandomly(
            Random random,
            Node root,
            Node newNode) {

        Node current =
                root;

        while (true) {

            if (random.nextBoolean()) {

                if (current.left == null) {

                    current.left =
                            newNode;

                    return;
                }

                current =
                        current.left;

            } else {

                if (current.right == null) {

                    current.right =
                            newNode;

                    return;
                }

                current =
                        current.right;
            }
        }
    }

    /**
     * Collect nodes in-order.
     */
    static void inOrderNodes(
            Node root,
            ArrayList<Node> nodes) {

        if (root == null) {
            return;
        }

        inOrderNodes(
                root.left,
                nodes);

        nodes.add(
                root);

        inOrderNodes(
                root.right,
                nodes);
    }

    /* **********************************************************************
     * Reference Implementation
     * **********************************************************************/

    /**
     * A deliberately simple reference implementation used by the test
     * suite.
     *
     * It examines every node and uses the brute-force BST check to identify
     * the largest BST. This is not used by either production algorithm.
     */
    static Node referenceLargestBST(
            Node root) {

        if (root == null) {
            return null;
        }

        Node best =
                null;

        ArrayList<Node> nodes =
                new ArrayList<>();

        collectPreOrder(
                root,
                nodes);

        for (Node candidate :
                nodes) {

            if (isBST(candidate)) {

                if (best == null
                        || size(candidate)
                        > size(best)) {

                    best =
                            candidate;
                }
            }
        }

        return best;
    }

    static void collectPreOrder(
            Node root,
            ArrayList<Node> nodes) {

        if (root == null) {
            return;
        }

        nodes.add(
                root);

        collectPreOrder(
                root.left,
                nodes);

        collectPreOrder(
                root.right,
                nodes);
    }

    /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    static class TestCase {

        final String id;
        final Node root;
        final int expectedRoot;
        final boolean expectedValid;
        final int expectedSize;
        final String description;

        TestCase(
                String id,
                Node root,
                int expectedRoot,
                boolean expectedValid,
                int expectedSize,
                String description) {

            this.id =
                    id;

            this.root =
                    root;

            this.expectedRoot =
                    expectedRoot;

            this.expectedValid =
                    expectedValid;

            this.expectedSize =
                    expectedSize;

            this.description =
                    description;
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

            this.name =
                    name;

            this.algorithm =
                    algorithm;
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

        int passed =
                0;

        int failed =
                0;

        for (TestCase test :
                tests) {

            try {

                Result actual =
                        method.algorithm.solve(
                                test.root);

                int actualRoot =
                        actual.root() == null
                                ? Integer.MIN_VALUE
                                : actual.root().val;

                int actualSize =
                        size(
                                actual.root());

                boolean success =
                        actual.valid()
                                == test.expectedValid
                                && actualRoot
                                == test.expectedRoot
                                && actualSize
                                == test.expectedSize
                                && (actual.root() == null
                                || isBST(
                                        actual.root()));

                if (success) {

                    passed++;

                    System.out.printf(
                            "PASS %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  root  = "
                                    + actualRoot);

                    System.out.println(
                            "  size  = "
                                    + actualSize);

                    System.out.println(
                            "  valid = "
                                    + actual.valid());

                } else {

                    failed++;

                    System.out.printf(
                            "FAIL %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  expected root  = "
                                    + test.expectedRoot);

                    System.out.println(
                            "  expected size  = "
                                    + test.expectedSize);

                    System.out.println(
                            "  expected valid = "
                                    + test.expectedValid);

                    System.out.println(
                            "  actual root    = "
                                    + actualRoot);

                    System.out.println(
                            "  actual size    = "
                                    + actualSize);

                    System.out.println(
                            "  actual valid   = "
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

        int passed =
                0;

        int failed =
                0;

        for (TestCase test :
                tests) {

            try {

                Result first =
                        methods.get(0)
                                .algorithm
                                .solve(
                                        test.root);

                boolean success =
                        first.valid()
                                == test.expectedValid
                                && size(first.root())
                                == test.expectedSize;

                for (int i = 1;
                     i < methods.size();
                     i++) {

                    Result actual =
                            methods.get(i)
                                    .algorithm
                                    .solve(
                                            test.root);

                    /*
                     * Compare the actual subtree root by reference where
                     * possible. This also verifies that both algorithms
                     * identify the same subtree.
                     */
                    if (actual.root()
                            != first.root()
                            || actual.valid()
                            != first.valid()
                            || size(actual.root())
                            != size(first.root())) {

                        success =
                                false;

                        System.out.printf(
                                "  %s = root %s, size %d, valid %s%n",
                                methods.get(i).name,
                                rootDescription(
                                        actual.root()),
                                size(actual.root()),
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

        int passed =
                0;

        int failed =
                0;

        for (MethodCase method :
                methods) {

            /*
             * Null root.
             */
            Result nullResult =
                    method.algorithm.solve(
                            null);

            if (nullResult.root() == null
                    && !nullResult.valid()) {

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

            /*
             * Single node is a valid BST.
             */
            Node single =
                    node(10);

            Result singleResult =
                    method.algorithm.solve(
                            single);

            if (singleResult.root() == single
                    && singleResult.valid()
                    && size(singleResult.root()) == 1) {

                passed++;

                System.out.printf(
                        "PASS I2 - %s accepts single-node BST%n",
                        method.name);

            } else {

                failed++;

                System.out.printf(
                        "FAIL I2 - %s rejects single-node BST%n",
                        method.name);
            }

            /*
             * A target outside the tree is not relevant here, but this
             * verifies that the returned subtree is always part of the
             * supplied tree.
             */
            Node root =
                    node(
                            10,
                            node(5),
                            node(15));

            Result result =
                    method.algorithm.solve(
                            root);

            if (containsNode(
                    root,
                    result.root())) {

                passed++;

                System.out.printf(
                        "PASS I3 - %s returns a subtree of the input%n",
                        method.name);

            } else {

                failed++;

                System.out.printf(
                        "FAIL I3 - %s returns an external node%n",
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
         * Complete BST
         * ============================================================
         *
         *             20
         *           /    \
         *         10      30
         *        /  \    /  \
         *       5   15  25  35
         *
         * Entire tree is a BST.
         */

        Node n5 =
                node(5);

        Node n15 =
                node(15);

        Node n25 =
                node(25);

        Node n35 =
                node(35);

        Node n10 =
                node(
                        10,
                        n5,
                        n15);

        Node n30 =
                node(
                        30,
                        n25,
                        n35);

        Node completeBST =
                node(
                        20,
                        n10,
                        n30);

        tests.add(
                new TestCase(
                        "B1",
                        completeBST,
                        20,
                        true,
                        7,
                        "entire tree is a BST"));

        /*
         * ============================================================
         * Root Invalid, Largest BST Is Left Subtree
         * ============================================================
         *
         *             10
         *           /    \
         *          5      8
         *         / \
         *        2   7
         *
         * The right child 8 violates the root 10 because it is smaller
         * than 10. The left subtree rooted at 5 is a BST of size 3.
         */

        Node leftBST =
                node(
                        5,
                        node(2),
                        node(7));

        Node invalidLeftWinner =
                node(
                        10,
                        leftBST,
                        node(8));

        tests.add(
                new TestCase(
                        "B2",
                        invalidLeftWinner,
                        5,
                        false,
                        3,
                        "largest BST is the left subtree"));

        /*
         * ============================================================
         * Root Invalid, Largest BST Is Right Subtree
         * ============================================================
         *
         *             10
         *           /    \
         *          20     15
         *                /  \
         *               12   18
         *
         * Left subtree is invalid because 20 > 10.
         * Right subtree rooted at 15 is a BST of size 3.
         */

        Node rightBST =
                node(
                        15,
                        node(12),
                        node(18));

        Node invalidRightWinner =
                node(
                        10,
                        node(20),
                        rightBST);

        tests.add(
                new TestCase(
                        "B3",
                        invalidRightWinner,
                        15,
                        false,
                        3,
                        "largest BST is the right subtree"));

        /*
         * ============================================================
         * Entire Tree Invalid Due To Deep Violation
         * ============================================================
         *
         *             20
         *           /    \
         *         10      30
         *        /  \    /  \
         *       5   25  25  35
         *
         * The 25 beneath 10 violates the left-subtree upper bound of 20.
         *
         * Left subtree rooted at 10 is not a BST.
         * Right subtree rooted at 30 is a BST of size 3.
         */

        Node deepViolation =
                node(
                        20,
                        node(
                                10,
                                node(5),
                                node(25)),
                        node(
                                30,
                                node(25),
                                node(35)));

        tests.add(
                new TestCase(
                        "B4",
                        deepViolation,
                        30,
                        false,
                        3,
                        "deep ordering violation"));

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
                        true,
                        1,
                        "single-node tree"));

        /*
         * ============================================================
         * Left-Skewed BST
         * ============================================================
         *
         *          40
         *         /
         *        30
         *       /
         *      20
         *     /
         *    10
         */

        Node leftSkewed =
                node(
                        40,
                        node(
                                30,
                                node(
                                        20,
                                        node(10),
                                        null),
                                null),
                        null);

        tests.add(
                new TestCase(
                        "S2",
                        leftSkewed,
                        40,
                        true,
                        4,
                        "left-skewed BST"));

        /*
         * ============================================================
         * Right-Skewed BST
         * ============================================================
         *
         *      10
         *        \
         *         20
         *           \
         *            30
         *              \
         *               40
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
                                        node(40))));

        tests.add(
                new TestCase(
                        "S3",
                        rightSkewed,
                        10,
                        true,
                        4,
                        "right-skewed BST"));

        /*
         * ============================================================
         * Right Subtree Contains Smaller Value
         * ============================================================
         *
         *             20
         *               \
         *                30
         *               /
         *              25
         *
         * Entire tree is a BST.
         */

        Node rightWithLeft =
                node(
                        20,
                        null,
                        node(
                                30,
                                node(25),
                                null));

        tests.add(
                new TestCase(
                        "S4",
                        rightWithLeft,
                        20,
                        true,
                        3,
                        "right subtree has left descendant"));

        /*
         * ============================================================
         * Duplicate Value
         * ============================================================
         *
         *          10
         *         /  \
         *       10    20
         *
         * Strict BST ordering rejects the duplicate.
         *
         * Both the left node and right subtree are BSTs of size 1,
         * while the right subtree rooted at 20 has size 1 as well.
         *
         * Left-most tie-breaking therefore selects the left 10.
         */

        Node duplicate =
                node(
                        10,
                        node(10),
                        node(20));

        tests.add(
                new TestCase(
                        "D1",
                        duplicate,
                        10,
                        false,
                        1,
                        "duplicate values are rejected"));

        /*
         * ============================================================
         * Negative Values
         * ============================================================
         */

        Node negative =
                node(
                        -10,
                        node(
                                -20,
                                node(-30),
                                node(-15)),
                        node(
                                -5,
                                node(-7),
                                node(-1)));

        tests.add(
                new TestCase(
                        "N1",
                        negative,
                        -10,
                        true,
                        7,
                        "negative values"));

        /*
         * ============================================================
         * Integer Boundaries
         * ============================================================
         *
         * This specifically checks that Integer.MIN_VALUE and
         * Integer.MAX_VALUE are handled correctly.
         */

        Node boundaries =
                node(
                        0,
                        node(
                                Integer.MIN_VALUE),
                        node(
                                Integer.MAX_VALUE));

        tests.add(
                new TestCase(
                        "N2",
                        boundaries,
                        0,
                        true,
                        3,
                        "integer boundary values"));

        /*
         * ============================================================
         * Equal-Sized BSTs
         * ============================================================
         *
         *              50
         *             /  \
         *           100   200
         *           /      \
         *         101      201
         *
         * Both child subtrees contain two nodes but neither is a BST
         * because 101 > 100 is valid on the right, actually making the
         * left subtree a BST. Likewise the right subtree is a BST.
         *
         * Both candidates have size 2, so the left-most candidate 100
         * must be selected.
         */

        Node equalSizeTie =
                node(
                        50,
                        node(
                                100,
                                null,
                                node(101)),
                        node(
                                200,
                                null,
                                node(201)));

        tests.add(
                new TestCase(
                        "T1",
                        equalSizeTie,
                        100,
                        false,
                        2,
                        "left-most tie-breaking"));

        return tests;
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
                new Random(20261006L);

        for (int iteration = 1;
             iteration <= iterations;
             iteration++) {

            int treeSize =
                    1 + random.nextInt(40);

            Node root =
                    randomBinaryTree(
                            random,
                            treeSize);

            Node reference =
                    referenceLargestBST(
                            root);

            int expectedSize =
                    size(reference);

            /*
             * The reference implementation uses the same deterministic
             * left-most tie-breaking rule.
             */
            for (MethodCase method :
                    methods) {

                Result result =
                        method.algorithm.solve(
                                root);

                int actualSize =
                        size(
                                result.root());

                if (actualSize
                        != expectedSize
                        || result.valid()
                        != isBST(root)
                        || result.root() == null
                        || !isBST(
                                result.root())) {

                    System.out.println(
                            "Randomised test FAILED");

                    System.out.println(
                            "iteration = "
                                    + iteration);

                    System.out.println(
                            "algorithm = "
                                    + method.name);

                    System.out.println(
                            "tree size = "
                                    + treeSize);

                    System.out.println(
                            "expected root = "
                                    + rootDescription(
                                    reference));

                    System.out.println(
                            "expected size = "
                                    + expectedSize);

                    System.out.println(
                            "actual root = "
                                    + rootDescription(
                                    result.root()));

                    System.out.println(
                            "actual size = "
                                    + actualSize);

                    System.out.println(
                            "actual valid = "
                                    + result.valid());

                    return;
                }
            }

            /*
             * Explicitly verify that both implementations return the same
             * node reference.
             */
            Result brute =
                    methods.get(0)
                            .algorithm
                            .solve(root);

            Result property =
                    methods.get(1)
                            .algorithm
                            .solve(root);

            if (brute.root()
                    != property.root()
                    || brute.valid()
                    != property.valid()) {

                System.out.println(
                        "Randomised cross-algorithm test FAILED");

                System.out.println(
                        "iteration = "
                                + iteration);

                System.out.println(
                        "brute-force root = "
                                + rootDescription(
                                brute.root()));

                System.out.println(
                        "BST-property root = "
                                + rootDescription(
                                property.root()));

                System.out.println(
                        "brute-force valid = "
                                + brute.valid());

                System.out.println(
                        "BST-property valid = "
                                + property.valid());

                return;
            }
        }

        System.out.printf(
                "All %d randomised tests passed.%n%n",
                iterations);
    }

    /* **********************************************************************
     * Output Helpers
     * **********************************************************************/

    static String rootDescription(
            Node root) {

        if (root == null) {
            return "null";
        }

        return String.valueOf(
                root.val);
    }

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
                "##############  LARGEST BST BINARY TREE  ###################");

        System.out.println(
                "############################################################");

        System.out.println();

        List<MethodCase> methods =
                List.of(

                        new MethodCase(
                                "Brute Force",
                                LargestBSTBinaryTree
                                        ::largestBSTBinaryTreeBruteForce),

                        new MethodCase(
                                "BST Property",
                                LargestBSTBinaryTree
                                        ::largestBSTBinaryTreeBSTProp)
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
