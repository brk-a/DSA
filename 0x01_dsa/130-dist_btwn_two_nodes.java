import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Find the distance between two nodes in a binary tree.
 *
 * The distance is measured in edges.
 *
 * Two implementations are provided:
 *
 * 1. LCA + Path Length
 *
 *      Finds the Lowest Common Ancestor (LCA), then calculates:
 *
 *          distance(a, b)
 *              = distance(LCA, a)
 *              + distance(LCA, b)
 *
 *      Time:  O(n)
 *      Space: O(h)
 *
 * 2. One-Pass
 *
 *      Finds both nodes and their distance during a single recursive
 *      traversal.
 *
 *      Time:  O(n)
 *      Space: O(h)
 *
 * Nodes are identified by their Node reference rather than their value.
 * This is important because a binary tree may contain duplicate values.
 */
public class DistanceBetweenTwoNodes {

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
     * 1. LCA + Path Length
     * **********************************************************************/

    /**
     * Find the distance between two nodes using:
     *
     *      1. Lowest Common Ancestor (LCA)
     *      2. Distance from the LCA to a
     *      3. Distance from the LCA to b
     *
     * Therefore:
     *
     *      distance(a, b)
     *          = distance(LCA, a)
     *          + distance(LCA, b)
     *
     * The distance is measured in edges.
     *
     * Nodes are compared using reference identity (==), not their values.
     *
     * Time:  O(n)
     * Space: O(h)
     */
    static int distancebetweenTwoNodesLCAwPathLength(
            Node root,
            Node a,
            Node b) {

        if (!validNode(root)
                || !validNode(a)
                || !validNode(b)) {

            return -1;
        }

        Node lca =
                lowestCommonAncestor(
                        root,
                        a,
                        b);

        /*
         * No LCA means that at least one of the nodes does not
         * exist in the supplied tree.
         */
        if (lca == null) {
            return -1;
        }

        int distanceA =
                distanceFromNode(
                        lca,
                        a);

        int distanceB =
                distanceFromNode(
                        lca,
                        b);

        /*
         * Both nodes must be reachable from the LCA.
         */
        if (distanceA == -1
                || distanceB == -1) {

            return -1;
        }

        return distanceA + distanceB;
    }

    /**
     * Find the Lowest Common Ancestor of two nodes in a general
     * binary tree.
     *
     * This does not rely on BST ordering.
     *
     * Nodes are compared using reference identity (==).
     */
    static Node lowestCommonAncestor(
            Node root,
            Node a,
            Node b) {

        if (root == null) {
            return null;
        }

        /*
         * If the current node is either target, it is a possible
         * LCA.
         */
        if (root == a
                || root == b) {

            return root;
        }

        Node left =
                lowestCommonAncestor(
                        root.left,
                        a,
                        b);

        Node right =
                lowestCommonAncestor(
                        root.right,
                        a,
                        b);

        /*
         * One target was found in each subtree.
         * Therefore, the current node is their LCA.
         */
        if (left != null
                && right != null) {

            return root;
        }

        /*
         * Otherwise, return whichever subtree found a target.
         */
        return left != null
                ? left
                : right;
    }

    /**
     * Find the distance from root to target.
     *
     * The returned value is measured in edges.
     *
     * Returns -1 when target is not present.
     */
    static int distanceFromNode(
            Node root,
            Node target) {

        if (root == null) {
            return -1;
        }

        if (root == target) {
            return 0;
        }

        int leftDistance =
                distanceFromNode(
                        root.left,
                        target);

        if (leftDistance != -1) {
            return leftDistance + 1;
        }

        int rightDistance =
                distanceFromNode(
                        root.right,
                        target);

        if (rightDistance != -1) {
            return rightDistance + 1;
        }

        return -1;
    }

    /* **********************************************************************
     * 2. One-Pass
     * **********************************************************************/

    /**
     * Find the distance between two nodes using one recursive traversal.
     *
     * Each recursive call returns:
     *
     *      - whether a was found
     *      - whether b was found
     *      - distance from the current node to a
     *      - distance from the current node to b
     *      - the final distance once both nodes have been found
     *
     * Nodes are compared using reference identity (==).
     *
     * Time:  O(n)
     * Space: O(h)
     */
    static int distanceBetweenTwoNodesLACOnePass(
            Node root,
            Node a,
            Node b) {

        if (!validNode(root)
                || !validNode(a)
                || !validNode(b)) {

            return -1;
        }

        DistanceResult result =
                distanceOnePass(
                        root,
                        a,
                        b);

        if (!result.foundA
                || !result.foundB) {

            return -1;
        }

        return result.distance;
    }

    /**
     * Result returned by the one-pass traversal.
     *
     * distanceToA / distanceToB are measured from the current node.
     *
     * distance is the final distance between a and b when both nodes
     * have been found below the current node.
     */
    static class DistanceResult {

        boolean foundA;
        boolean foundB;

        int distanceToA;
        int distanceToB;

        int distance;

        DistanceResult(
                boolean foundA,
                boolean foundB,
                int distanceToA,
                int distanceToB,
                int distance) {

            this.foundA = foundA;
            this.foundB = foundB;
            this.distanceToA = distanceToA;
            this.distanceToB = distanceToB;
            this.distance = distance;
        }
    }

    /**
     * One-pass recursive helper.
     */
    static DistanceResult distanceOnePass(
            Node root,
            Node a,
            Node b) {

        if (root == null) {

            return new DistanceResult(
                    false,
                    false,
                    -1,
                    -1,
                    -1);
        }

        /*
         * Search both subtrees.
         */
        DistanceResult left =
                distanceOnePass(
                        root.left,
                        a,
                        b);

        DistanceResult right =
                distanceOnePass(
                        root.right,
                        a,
                        b);

        boolean foundA =
                root == a
                        || left.foundA
                        || right.foundA;

        boolean foundB =
                root == b
                        || left.foundB
                        || right.foundB;

        /*
         * If a complete answer was already found in the left
         * subtree, propagate it upwards unchanged.
         */
        if (left.distance != -1) {

            return new DistanceResult(
                    foundA,
                    foundB,
                    -1,
                    -1,
                    left.distance);
        }

        /*
         * Likewise for the right subtree.
         */
        if (right.distance != -1) {

            return new DistanceResult(
                    foundA,
                    foundB,
                    -1,
                    -1,
                    right.distance);
        }

        int distanceToA = -1;
        int distanceToB = -1;

        /*
         * If the current node is one of the targets, its distance
         * from itself is zero.
         */
        if (root == a) {
            distanceToA = 0;
        }

        if (root == b) {
            distanceToB = 0;
        }

        /*
         * If a was found in the left subtree, it is one edge farther
         * away from the current node.
         */
        if (left.foundA) {

            distanceToA =
                    left.distanceToA + 1;
        }

        /*
         * If a was found in the right subtree, it is one edge farther
         * away from the current node.
         */
        if (right.foundA) {

            distanceToA =
                    right.distanceToA + 1;
        }

        /*
         * Do the same for b.
         */
        if (left.foundB) {

            distanceToB =
                    left.distanceToB + 1;
        }

        if (right.foundB) {

            distanceToB =
                    right.distanceToB + 1;
        }

        /*
         * If both nodes are known below or at the current node,
         * the current node is their LCA.
         */
        int distance = -1;

        if (distanceToA != -1
                && distanceToB != -1) {

            distance =
                    distanceToA
                            + distanceToB;
        }

        return new DistanceResult(
                foundA,
                foundB,
                distanceToA,
                distanceToB,
                distance);
    }

    /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    static class TestCase {

        final String id;
        final Node root;
        final Node a;
        final Node b;
        final int expected;
        final String description;

        TestCase(
                String id,
                Node root,
                Node a,
                Node b,
                int expected,
                String description) {

            this.id = id;
            this.root = root;
            this.a = a;
            this.b = b;
            this.expected = expected;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {

        int solve(
                Node root,
                Node a,
                Node b);
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

                int actual =
                        method.algorithm.solve(
                                test.root,
                                test.a,
                                test.b);

                if (actual == test.expected) {

                    passed++;

                    System.out.printf(
                            "PASS %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  distance = "
                                    + actual);

                } else {

                    failed++;

                    System.out.printf(
                            "FAIL %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  expected = "
                                    + test.expected);

                    System.out.println(
                            "  actual   = "
                                    + actual);
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

                int first =
                        methods.get(0)
                                .algorithm
                                .solve(
                                        test.root,
                                        test.a,
                                        test.b);

                boolean success =
                        first == test.expected;

                for (int i = 1;
                     i < methods.size();
                     i++) {

                    int actual =
                            methods.get(i)
                                    .algorithm
                                    .solve(
                                            test.root,
                                            test.a,
                                            test.b);

                    if (actual != first) {

                        success = false;

                        System.out.printf(
                                "  %s = %d%n",
                                methods.get(i).name,
                                actual);
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

        Node root =
                node(
                        10,
                        node(5),
                        node(15));

        Node left =
                root.left;

        Node right =
                root.right;

        /*
         * Null root.
         */
        for (MethodCase method :
                methods) {

            int result =
                    method.algorithm.solve(
                            null,
                            left,
                            right);

            if (result == -1) {

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

        /*
         * Null first node.
         */
        for (MethodCase method :
                methods) {

            int result =
                    method.algorithm.solve(
                            root,
                            null,
                            right);

            if (result == -1) {

                passed++;

                System.out.printf(
                        "PASS I2 - %s rejects null first node%n",
                        method.name);

            } else {

                failed++;

                System.out.printf(
                        "FAIL I2 - %s accepts null first node%n",
                        method.name);
            }
        }

        /*
         * Null second node.
         */
        for (MethodCase method :
                methods) {

            int result =
                    method.algorithm.solve(
                            root,
                            left,
                            null);

            if (result == -1) {

                passed++;

                System.out.printf(
                        "PASS I3 - %s rejects null second node%n",
                        method.name);

            } else {

                failed++;

                System.out.printf(
                        "FAIL I3 - %s accepts null second node%n",
                        method.name);
            }
        }

        /*
         * First node is not contained in the tree.
         */
        Node unrelated =
                node(99);

        for (MethodCase method :
                methods) {

            int result =
                    method.algorithm.solve(
                            root,
                            unrelated,
                            right);

            if (result == -1) {

                passed++;

                System.out.printf(
                        "PASS I4 - %s rejects first node outside tree%n",
                        method.name);

            } else {

                failed++;

                System.out.printf(
                        "FAIL I4 - %s accepts first node outside tree%n",
                        method.name);
            }
        }

        /*
         * Second node is not contained in the tree.
         */
        for (MethodCase method :
                methods) {

            int result =
                    method.algorithm.solve(
                            root,
                            left,
                            unrelated);

            if (result == -1) {

                passed++;

                System.out.printf(
                        "PASS I5 - %s rejects second node outside tree%n",
                        method.name);

            } else {

                failed++;

                System.out.printf(
                        "FAIL I5 - %s accepts second node outside tree%n",
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
         * Basic Tree
         * ============================================================
         *
         *             20
         *           /    \
         *         10      30
         *        /  \    /  \
         *       5   15  25  35
         *
         * Distances:
         *
         *      5 -> 10 -> 15 = 2
         *      5 -> 10 -> 20 -> 30 -> 35 = 4
         *      15 -> 20 -> 25 = 2
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

        Node root =
                node(
                        20,
                        n10,
                        n30);

        tests.add(
                new TestCase(
                        "B1",
                        root,
                        n5,
                        n15,
                        2,
                        "nodes share the same parent"));

        tests.add(
                new TestCase(
                        "B2",
                        root,
                        n5,
                        n35,
                        4,
                        "nodes are in opposite subtrees"));

        tests.add(
                new TestCase(
                        "B3",
                        root,
                        n15,
                        root,
                        2,
                        "one node is an ancestor"));

        tests.add(
                new TestCase(
                        "B4",
                        root,
                        root,
                        root,
                        0,
                        "same node"));

        tests.add(
                new TestCase(
                        "B5",
                        root,
                        n25,
                        n35,
                        2,
                        "nodes share right-side parent"));

        tests.add(
                new TestCase(
                        "B6",
                        root,
                        n5,
                        n10,
                        1,
                        "parent and child"));

        /*
         * ============================================================
         * Right Subtree
         * ============================================================
         *
         *          20
         *            \
         *             30
         *            /
         *           25
         */

        Node n25b =
                node(25);

        Node n30b =
                node(
                        30,
                        n25b,
                        null);

        Node root20b =
                node(
                        20,
                        null,
                        n30b);

        tests.add(
                new TestCase(
                        "S1",
                        root20b,
                        root20b,
                        n25b,
                        2,
                        "ancestor and descendant"));

        /*
         * ============================================================
         * Left-Skewed Tree
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

        Node s10 =
                node(10);

        Node s20 =
                node(
                        20,
                        s10,
                        null);

        Node s30 =
                node(
                        30,
                        s20,
                        null);

        Node s40 =
                node(
                        40,
                        s30,
                        null);

        tests.add(
                new TestCase(
                        "A1",
                        s40,
                        s10,
                        s40,
                        3,
                        "left-skewed tree"));

        tests.add(
                new TestCase(
                        "A2",
                        s40,
                        s20,
                        s30,
                        1,
                        "adjacent nodes in skewed tree"));

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
         */

        Node r40 =
                node(40);

        Node r30 =
                node(
                        30,
                        null,
                        r40);

        Node r20 =
                node(
                        20,
                        null,
                        r30);

        Node r10 =
                node(
                        10,
                        null,
                        r20);

        tests.add(
                new TestCase(
                        "A3",
                        r10,
                        r10,
                        r40,
                        3,
                        "right-skewed tree"));

        tests.add(
                new TestCase(
                        "A4",
                        r10,
                        r20,
                        r40,
                        2,
                        "nodes on right-skewed tree"));

        /*
         * ============================================================
         * Single Node
         * ============================================================
         */

        Node single =
                node(100);

        tests.add(
                new TestCase(
                        "S2",
                        single,
                        single,
                        single,
                        0,
                        "single-node tree"));

        /*
         * ============================================================
         * Duplicate Values
         * ============================================================
         *
         * The nodes have the same value but are different objects.
         *
         *             10
         *            /  \
         *          10    10
         *
         * The distance between the two child nodes is 2.
         *
         * This verifies that the algorithms use Node identity rather
         * than the value stored in the node.
         */

        Node duplicateLeft =
                node(10);

        Node duplicateRight =
                node(10);

        Node duplicateRoot =
                node(
                        10,
                        duplicateLeft,
                        duplicateRight);

        tests.add(
                new TestCase(
                        "D1",
                        duplicateRoot,
                        duplicateLeft,
                        duplicateRight,
                        2,
                        "duplicate values use node identity"));

        tests.add(
                new TestCase(
                        "D2",
                        duplicateRoot,
                        duplicateRoot,
                        duplicateLeft,
                        1,
                        "root and duplicate-valued child"));

        /*
         * ============================================================
         * Negative Values
         * ============================================================
         */

        Node negativeLeft =
                node(-20);

        Node negativeRightChild =
                node(-1);

        Node negativeRight =
                node(
                        -5,
                        negativeRightChild,
                        null);

        Node negative =
                node(
                        -10,
                        negativeLeft,
                        negativeRight);

        tests.add(
                new TestCase(
                        "N1",
                        negative,
                        negativeLeft,
                        negativeRightChild,
                        3,
                        "negative values"));

        return tests;
    }

    /* **********************************************************************
     * Random Binary Tree Generation
     * **********************************************************************/

    static Node randomBinaryTree(
            Random random,
            int size) {

        if (size <= 0) {
            return null;
        }

        ArrayList<Node> nodes =
                new ArrayList<>();

        for (int i = 0; i < size; i++) {

            nodes.add(
                    node(
                            random.nextInt(2001)
                                    - 1000));
        }

        /*
         * Attach every node after the root to a randomly selected
         * existing node with an available child position.
         */
        ArrayList<Node> available =
                new ArrayList<>();

        available.add(
                nodes.get(0));

        for (int i = 1;
             i < nodes.size();
             i++) {

            Node parent;

            do {

                parent =
                        available.get(
                                random.nextInt(
                                        available.size()));

            } while (parent.left != null
                    && parent.right != null);

            if (parent.left == null
                    && parent.right == null) {

                if (random.nextBoolean()) {

                    parent.left =
                            nodes.get(i);

                } else {

                    parent.right =
                            nodes.get(i);
                }

            } else if (parent.left == null) {

                parent.left =
                        nodes.get(i);

            } else {

                parent.right =
                        nodes.get(i);
            }

            available.add(
                    nodes.get(i));
        }

        return nodes.get(0);
    }

    /**
     * Collect all nodes in the tree.
     */
    static void collectNodes(
            Node root,
            ArrayList<Node> nodes) {

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

    /**
     * Reference implementation used only by the randomised tests.
     *
     * Finds the path from root to each target and calculates the
     * distance between the two paths.
     */
    static int referenceDistance(
            Node root,
            Node a,
            Node b) {

        ArrayList<Node> pathA =
                new ArrayList<>();

        ArrayList<Node> pathB =
                new ArrayList<>();

        if (!findPath(
                root,
                a,
                pathA)
                || !findPath(
                root,
                b,
                pathB)) {

            return -1;
        }

        int common = 0;

        while (common < pathA.size()
                && common < pathB.size()
                && pathA.get(common)
                == pathB.get(common)) {

            common++;
        }

        return (pathA.size() - common)
                + (pathB.size() - common);
    }

    /**
     * Find the path from root to target.
     */
    static boolean findPath(
            Node root,
            Node target,
            ArrayList<Node> path) {

        if (root == null) {
            return false;
        }

        path.add(root);

        if (root == target) {
            return true;
        }

        if (findPath(
                root.left,
                target,
                path)
                || findPath(
                root.right,
                target,
                path)) {

            return true;
        }

        path.remove(
                path.size() - 1);

        return false;
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
                new Random(20260925L);

        for (int iteration = 1;
             iteration <= iterations;
             iteration++) {

            int size =
                    1 + random.nextInt(50);

            Node root =
                    randomBinaryTree(
                            random,
                            size);

            ArrayList<Node> nodes =
                    new ArrayList<>();

            collectNodes(
                    root,
                    nodes);

            Node a =
                    nodes.get(
                            random.nextInt(
                                    nodes.size()));

            Node b =
                    nodes.get(
                            random.nextInt(
                                    nodes.size()));

            int expected =
                    referenceDistance(
                            root,
                            a,
                            b);

            for (MethodCase method :
                    methods) {

                int actual =
                        method.algorithm.solve(
                                root,
                                a,
                                b);

                if (actual != expected) {

                    System.out.println(
                            "Randomised test FAILED");

                    System.out.println(
                            "iteration = "
                                    + iteration);

                    System.out.println(
                            "algorithm = "
                                    + method.name);

                    System.out.println(
                            "a = "
                                    + a.val);

                    System.out.println(
                            "b = "
                                    + b.val);

                    System.out.println(
                            "expected = "
                                    + expected);

                    System.out.println(
                            "actual = "
                                    + actual);

                    return;
                }
            }
        }

        System.out.printf(
                "All %d randomised tests passed.%n%n",
                iterations);
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
                "############  DISTANCE BETWEEN TWO NODES  ##################");

        System.out.println(
                "############################################################");

        System.out.println();

        List<MethodCase> methods =
                List.of(

                        new MethodCase(
                                "LCA + Path Length",
                                DistanceBetweenTwoNodes
                                        ::distancebetweenTwoNodesLCAwPathLength),

                        new MethodCase(
                                "One-Pass",
                                DistanceBetweenTwoNodes
                                        ::distanceBetweenTwoNodesLACOnePass)
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
