import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Find all nodes at a given distance from a target node in a binary tree.
 *
 * Three approaches are provided:
 *
 * 1. Brute force
 *      For every node, calculate its distance from the target.
 *
 *      Time:  O(n^2) worst case
 *      Space: O(h)
 *
 * 2. Recursive
 *      Locate the target recursively and collect nodes at the required
 *      distance while unwinding the recursion.
 *
 *      Time:  O(n)
 *      Space: O(h)
 *
 * 3. Breadth-first search with parent links
 *      Build parent links and then perform BFS from the target node.
 *
 *      Time:  O(n)
 *      Space: O(n)
 *
 * Target semantics:
 *
 *      target is a node value.
 *
 * If duplicate values exist, the first node with that value encountered
 * during level-order traversal is used as the target. This definition is
 * shared by all three implementations so that their results are
 * deterministic and directly comparable.
 *
 * Returned nodes are sorted in ascending order.
 */
public class NodesAtGivenDistance {

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
     * Results
     * **********************************************************************/

    static record Result(
            ArrayList<Integer> nodes,
            boolean valid) {
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
     * Find the first node with the requested value using level-order
     * traversal.
     *
     * This also gives all implementations the same behaviour when
     * duplicate values are present.
     */
    static Node findTarget(
            Node root,
            int target) {

        if (root == null) {
            return null;
        }

        Queue<Node> queue =
                new ArrayDeque<>();

        queue.offer(root);

        while (!queue.isEmpty()) {

            Node current =
                    queue.poll();

            if (current.val == target) {
                return current;
            }

            if (current.left != null) {
                queue.offer(current.left);
            }

            if (current.right != null) {
                queue.offer(current.right);
            }
        }

        return null;
    }

    static boolean validRequest(
            Node root,
            int k) {

        return root != null
                && k >= 0;
    }

    static void sortResult(
            ArrayList<Integer> nodes) {

        nodes.sort(Integer::compareTo);
    }

    /* **********************************************************************
     * 1. Brute Force
     * **********************************************************************/

    /**
     * Brute-force solution.
     *
     * The target is located first. For every node in the tree, the
     * distance to the target is then calculated independently.
     */
    static Result nodesAtGivenDistanceBruteForce(
            Node root,
            int target,
            int k) {

        if (!validRequest(root, k)) {
            return new Result(
                    null,
                    false);
        }

        Node targetNode =
                findTarget(
                        root,
                        target);

        if (targetNode == null) {
            return new Result(
                    null,
                    false);
        }

        ArrayList<Integer> nodes =
                new ArrayList<>();

        collectBruteForce(
                root,
                root,
                targetNode,
                k,
                nodes);

        sortResult(nodes);

        return new Result(
                nodes,
                true);
    }

    /**
     * Visit every node and calculate its distance from the target.
     */
    static void collectBruteForce(
            Node current,
            Node treeRoot,
            Node targetNode,
            int k,
            ArrayList<Integer> nodes) {

        if (current == null) {
            return;
        }

        int distance =
                distance(
                        treeRoot,
                        current,
                        targetNode);

        if (distance == k) {
            nodes.add(current.val);
        }

        collectBruteForce(
                current.left,
                treeRoot,
                targetNode,
                k,
                nodes);

        collectBruteForce(
                current.right,
                treeRoot,
                targetNode,
                k,
                nodes);
    }

    /**
     * Calculate the distance between two nodes using their lowest
     * common ancestor.
     */
    static int distance(
            Node treeRoot,
            Node first,
            Node second) {

        Node ancestor =
                findLCA(
                        treeRoot,
                        first,
                        second);

        if (ancestor == null) {
            return -1;
        }

        int firstDistance =
                findDistanceFromAncestor(
                        ancestor,
                        first,
                        0);

        int secondDistance =
                findDistanceFromAncestor(
                        ancestor,
                        second,
                        0);

        if (firstDistance == -1
                || secondDistance == -1) {

            return -1;
        }

        return firstDistance + secondDistance;
    }

    /**
     * Find the lowest common ancestor of two node references.
     *
     * Node references, rather than values, are deliberately used here.
     * This is important when duplicate values exist.
     */
    static Node findLCA(
            Node root,
            Node first,
            Node second) {

        if (root == null
                || root == first
                || root == second) {

            return root;
        }

        Node left =
                findLCA(
                        root.left,
                        first,
                        second);

        Node right =
                findLCA(
                        root.right,
                        first,
                        second);

        if (left != null && right != null) {
            return root;
        }

        return left != null
                ? left
                : right;
    }

    /**
     * Find the number of edges between an ancestor and a descendant.
     */
    static int findDistanceFromAncestor(
            Node root,
            Node target,
            int distance) {

        if (root == null) {
            return -1;
        }

        if (root == target) {
            return distance;
        }

        int left =
                findDistanceFromAncestor(
                        root.left,
                        target,
                        distance + 1);

        if (left != -1) {
            return left;
        }

        return findDistanceFromAncestor(
                root.right,
                target,
                distance + 1);
    }

    /* **********************************************************************
     * 2. Recursive
     * **********************************************************************/

    /**
     * Linear-time recursive solution.
     *
     * The return value from solveRecursion represents the distance from
     * the current node to the target:
     *
     *      -1 = target is not in this subtree
     *       1 = current node is one edge from the target
     *       2 = current node is two edges from the target
     *       ...
     *
     * When the target itself is found, nodes at distance k are collected
     * from that subtree.
     */
    static Result nodesAtGivenDistanceRecursion(
            Node root,
            int target,
            int k) {

        if (!validRequest(root, k)) {
            return new Result(
                    null,
                    false);
        }

        /*
         * Locate the exact target node first. This is necessary to make
         * duplicate values deterministic.
         */
        Node targetNode =
                findTarget(
                        root,
                        target);

        if (targetNode == null) {
            return new Result(
                    null,
                    false);
        }

        ArrayList<Integer> nodes =
                new ArrayList<>();

        solveRecursion(
                root,
                targetNode,
                k,
                nodes);

        sortResult(nodes);

        return new Result(
                nodes,
                true);
    }

    /**
     * Returns the distance from the current node to the target, measured
     * as number of edges plus one when returning to the parent.
     *
     * A return value of -1 means the target is not in this subtree.
     */
    static int solveRecursion(
            Node root,
            Node targetNode,
            int k,
            ArrayList<Integer> nodes) {

        if (root == null) {
            return -1;
        }

        /*
         * Target found.
         */
        if (root == targetNode) {

            findNodes(
                    root,
                    k,
                    nodes);

            return 1;
        }

        /*
         * Search the left subtree.
         */
        int left =
                solveRecursion(
                        root.left,
                        targetNode,
                        k,
                        nodes);

        if (left != -1) {

            /*
             * root is exactly k edges from the target.
             */
            if (k - left == 0) {

                nodes.add(root.val);

            } else {

                /*
                 * The target is left of root, so the only unexplored
                 * branch at this distance is root.right.
                 */
                findNodes(
                        root.right,
                        k - left - 1,
                        nodes);
            }

            return left + 1;
        }

        /*
         * Search the right subtree.
         */
        int right =
                solveRecursion(
                        root.right,
                        targetNode,
                        k,
                        nodes);

        if (right != -1) {

            /*
             * root is exactly k edges from the target.
             */
            if (k - right == 0) {

                nodes.add(root.val);

            } else {

                /*
                 * The target is right of root, so the only unexplored
                 * branch at this distance is root.left.
                 */
                findNodes(
                        root.left,
                        k - right - 1,
                        nodes);
            }

            return right + 1;
        }

        return -1;
    }

    /**
     * Collect all nodes exactly dist edges below root.
     */
    static void findNodes(
            Node root,
            int dist,
            ArrayList<Integer> nodes) {

        if (root == null
                || dist < 0) {

            return;
        }

        if (dist == 0) {

            nodes.add(root.val);

            return;
        }

        findNodes(
                root.left,
                dist - 1,
                nodes);

        findNodes(
                root.right,
                dist - 1,
                nodes);
    }

    /* **********************************************************************
     * 3. Breadth-first Search with Parent Links
     * **********************************************************************/

    /**
     * BFS solution.
     *
     * The tree is treated as an undirected graph by adding a parent link
     * for every child. BFS can then move:
     *
     *      left
     *      right
     *      parent
     *
     * without revisiting nodes.
     */
    static Result nodesAtGivenDistanceBFS(
            Node root,
            int target,
            int k) {

        if (!validRequest(root, k)) {
            return new Result(
                    null,
                    false);
        }

        HashMap<Node, Node> parents =
                new HashMap<>();

        Node targetNode =
                markParents(
                        root,
                        target,
                        parents);

        if (targetNode == null) {
            return new Result(
                    null,
                    false);
        }

        ArrayList<Integer> nodes =
                new ArrayList<>();

        HashSet<Node> visited =
                new HashSet<>();

        Queue<Node> queue =
                new ArrayDeque<>();

        queue.offer(targetNode);
        visited.add(targetNode);

        int distance = 0;

        while (!queue.isEmpty()
                && distance < k) {

            int size =
                    queue.size();

            while (size-- > 0) {

                Node current =
                        queue.poll();

                /*
                 * Left child.
                 */
                if (current.left != null
                        && visited.add(current.left)) {

                    queue.offer(current.left);
                }

                /*
                 * Right child.
                 */
                if (current.right != null
                        && visited.add(current.right)) {

                    queue.offer(current.right);
                }

                /*
                 * Parent.
                 */
                Node parent =
                        parents.get(current);

                if (parent != null
                        && visited.add(parent)) {

                    queue.offer(parent);
                }
            }

            distance++;
        }

        /*
         * Every node currently in the queue is exactly k edges from
         * the target.
         */
        while (!queue.isEmpty()) {

            nodes.add(
                    queue.poll().val);
        }

        sortResult(nodes);

        return new Result(
                nodes,
                true);
    }

    /**
     * Build parent links and return the first target encountered in
     * level-order traversal.
     */
    static Node markParents(
            Node root,
            int target,
            HashMap<Node, Node> parents) {

        Node targetNode = null;

        Queue<Node> queue =
                new ArrayDeque<>();

        queue.offer(root);

        while (!queue.isEmpty()) {

            Node current =
                    queue.poll();

            if (targetNode == null
                    && current.val == target) {

                targetNode = current;
            }

            if (current.left != null) {

                parents.put(
                        current.left,
                        current);

                queue.offer(
                        current.left);
            }

            if (current.right != null) {

                parents.put(
                        current.right,
                        current);

                queue.offer(
                        current.right);
            }
        }

        return targetNode;
    }

    /* **********************************************************************
     * Tree Comparison
     * **********************************************************************/

    static boolean treesEqual(
            Node first,
            Node second) {

        if (first == null
                && second == null) {

            return true;
        }

        if (first == null
                || second == null) {

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
     * Test Harness
     * **********************************************************************/

    static class TestCase {

        final String id;
        final Node root;
        final int target;
        final int k;
        final ArrayList<Integer> expected;
        final String description;

        TestCase(
                String id,
                Node root,
                int target,
                int k,
                List<Integer> expected,
                String description) {

            this.id = id;
            this.root = root;
            this.target = target;
            this.k = k;
            this.expected =
                    new ArrayList<>(expected);
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {

        Result solve(
                Node root,
                int target,
                int k);
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
     * Standard Tests
     * **********************************************************************/

    static void runAlgorithmTests(
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
                                test.root,
                                test.target,
                                test.k);

                boolean success =
                        actual.valid()
                                && actual.nodes() != null
                                && actual.nodes().equals(
                                test.expected);

                if (success) {

                    passed++;

                    System.out.printf(
                            "PASS %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  result = "
                                    + actual.nodes());

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
                                    + (actual.valid()
                                    ? actual.nodes()
                                    : "invalid"));
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

                ArrayList<Integer> expected =
                        null;

                boolean allEqual = true;

                for (MethodCase method :
                        methods) {

                    Result result =
                            method.algorithm.solve(
                                    test.root,
                                    test.target,
                                    test.k);

                    if (!result.valid()) {

                        allEqual = false;

                        System.out.printf(
                                "  %s returned invalid%n",
                                method.name);

                        break;
                    }

                    if (expected == null) {

                        expected =
                                result.nodes();

                    } else if (!expected.equals(
                            result.nodes())) {

                        allEqual = false;

                        System.out.printf(
                                "  %s = %s%n",
                                method.name,
                                result.nodes());
                    }
                }

                if (allEqual
                        && expected != null
                        && expected.equals(
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

                    System.out.println(
                            "  expected = "
                                    + test.expected);
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
        for (MethodCase method : methods) {

            Result result =
                    method.algorithm.solve(
                            null,
                            1,
                            0);

            if (!result.valid()) {

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
         * Negative distance.
         */
        Node simple =
                node(
                        1,
                        node(2),
                        node(3));

        for (MethodCase method : methods) {

            Result result =
                    method.algorithm.solve(
                            simple,
                            1,
                            -1);

            if (!result.valid()) {

                passed++;

                System.out.printf(
                        "PASS I2 - %s rejects negative distance%n",
                        method.name);

            } else {

                failed++;

                System.out.printf(
                        "FAIL I2 - %s accepts negative distance%n",
                        method.name);
            }
        }

        /*
         * Target does not exist.
         */
        for (MethodCase method : methods) {

            Result result =
                    method.algorithm.solve(
                            simple,
                            99,
                            1);

            if (!result.valid()) {

                passed++;

                System.out.printf(
                        "PASS I3 - %s rejects missing target%n",
                        method.name);

            } else {

                failed++;

                System.out.printf(
                        "FAIL I3 - %s accepts missing target%n",
                        method.name);
            }
        }

        printResults(
                passed,
                failed,
                passed + failed);
    }

    /* **********************************************************************
     * Random Tree Generation
     * **********************************************************************/

    static Node randomTree(
            Random rng,
            int depth,
            double nodeProbability) {

        if (depth == 0
                || rng.nextDouble()
                > nodeProbability) {

            return null;
        }

        /*
         * Deliberately use a small value range so that duplicate values
         * occur frequently and therefore test target semantics.
         */
        int value =
                rng.nextInt(21) - 10;

        Node root =
                new Node(value);

        root.left =
                randomTree(
                        rng,
                        depth - 1,
                        nodeProbability);

        root.right =
                randomTree(
                        rng,
                        depth - 1,
                        nodeProbability);

        return root;
    }

    /* **********************************************************************
     * Randomised Cross Checks
     * **********************************************************************/

    static void runRandomisedTests(
            List<MethodCase> methods,
            int iterations) {

        printSeparator();

        System.out.println(
                "Randomised Cross Checks");

        printSeparator();

        Random rng =
                new Random(20260925L);

        int completed = 0;

        while (completed < iterations) {

            Node root =
                    randomTree(
                            rng,
                            8,
                            0.75);

            /*
             * The public API treats a null root as invalid.
             */
            if (root == null) {
                continue;
            }

            completed++;

            /*
             * Select a value which definitely exists in the tree.
             *
             * Using the root value also means duplicate values are
             * deliberately exercised.
             */
            int target =
                    root.val;

            int k =
                    rng.nextInt(7);

            ArrayList<Integer> expected =
                    null;

            boolean success = true;

            for (MethodCase method :
                    methods) {

                Result result =
                        method.algorithm.solve(
                                root,
                                target,
                                k);

                if (!result.valid()) {

                    success = false;

                    System.out.println(
                            "Randomised test FAILED");

                    System.out.println(
                            "iteration = "
                                    + completed);

                    System.out.println(
                            "algorithm = "
                                    + method.name);

                    System.out.println(
                            "target = "
                                    + target);

                    System.out.println(
                            "k = "
                                    + k);

                    break;
                }

                if (expected == null) {

                    expected =
                            result.nodes();

                } else if (!expected.equals(
                        result.nodes())) {

                    success = false;

                    System.out.println(
                            "Randomised test FAILED");

                    System.out.println(
                            "iteration = "
                                    + completed);

                    System.out.println(
                            "algorithm = "
                                    + method.name);

                    System.out.println(
                            "target = "
                                    + target);

                    System.out.println(
                            "k = "
                                    + k);

                    System.out.println(
                            "expected = "
                                    + expected);

                    System.out.println(
                            "actual = "
                                    + result.nodes());

                    break;
                }
            }

            if (!success) {
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
     * Test Data
     * **********************************************************************/

    static List<TestCase> buildTests() {

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
                        1,
                        0,
                        List.of(1),
                        "target itself"));

        tests.add(
                new TestCase(
                        "B2",
                        node(
                                1,
                                node(2),
                                node(3)),
                        1,
                        1,
                        List.of(2, 3),
                        "root with two children"));

        tests.add(
                new TestCase(
                        "B3",
                        node(
                                1,
                                node(
                                        2,
                                        node(4),
                                        node(5)),
                                node(
                                        3,
                                        node(6),
                                        node(7))),
                        1,
                        2,
                        List.of(4, 5, 6, 7),
                        "complete three-level tree"));

        /*
         * ============================================================
         * Target in Left Subtree
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "L1",
                        node(
                                1,
                                node(
                                        2,
                                        node(4),
                                        node(5)),
                                node(3)),
                        2,
                        1,
                        List.of(1, 4, 5),
                        "target with parent and children"));

        tests.add(
                new TestCase(
                        "L2",
                        node(
                                1,
                                node(
                                        2,
                                        node(4),
                                        node(
                                                5,
                                                node(8),
                                                node(9))),
                                node(3)),
                        5,
                        2,
                        List.of(1, 8, 9),
                        "target deeper in left subtree"));

        /*
         * ============================================================
         * Target in Right Subtree
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "R1",
                        node(
                                1,
                                node(2),
                                node(
                                        3,
                                        node(6),
                                        node(7))),
                        3,
                        1,
                        List.of(1, 6, 7),
                        "target in right subtree"));

        /*
         * ============================================================
         * Asymmetric Trees
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "A1",
                        node(
                                1,
                                node(
                                        2,
                                        node(4),
                                        null),
                                null),
                        4,
                        2,
                        List.of(1),
                        "left-skewed tree"));

        tests.add(
                new TestCase(
                        "A2",
                        node(
                                1,
                                null,
                                node(
                                        3,
                                        null,
                                        node(6))),
                        6,
                        2,
                        List.of(1),
                        "right-skewed tree"));

        tests.add(
                new TestCase(
                        "A3",
                        node(
                                1,
                                node(2),
                                null),
                        1,
                        1,
                        List.of(2),
                        "left child only"));

        tests.add(
                new TestCase(
                        "A4",
                        node(
                                1,
                                null,
                                node(3)),
                        1,
                        1,
                        List.of(3),
                        "right child only"));

        /*
         * ============================================================
         * Distance Larger Than Tree
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "D1",
                        node(
                                1,
                                node(2),
                                node(3)),
                        2,
                        5,
                        List.of(),
                        "distance larger than tree"));

        /*
         * ============================================================
         * Duplicate Values
         * ============================================================
         *
         * The first value 5 encountered in level-order traversal is
         * the root. Therefore target=5 means the root node.
         */

        tests.add(
                new TestCase(
                        "D2",
                        node(
                                5,
                                node(
                                        5,
                                        node(5),
                                        node(5)),
                                node(
                                        5,
                                        null,
                                        node(5))),
                        5,
                        1,
                        List.of(5, 5),
                        "duplicate target values"));

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
                                node(-2),
                                node(
                                        -3,
                                        node(-4),
                                        null)),
                        -3,
                        1,
                        List.of(-1, -4),
                        "negative values"));

        /*
         * ============================================================
         * Larger Irregular Tree
         * ============================================================
         */

        Node largeTree =
                node(
                        10,
                        node(
                                20,
                                node(40),
                                node(
                                        50,
                                        node(80),
                                        null)),
                        node(
                                30,
                                node(
                                        60,
                                        null,
                                        node(90)),
                                node(70)));

        tests.add(
                new TestCase(
                        "L3",
                        largeTree,
                        20,
                        2,
                        List.of(40, 50, 30),
                        "larger irregular tree"));

        return tests;
    }

    /* **********************************************************************
     * Main Test Suite
     * **********************************************************************/

    public static void main(
            String[] args) {

        System.out.println(
                "############################################################");

        System.out.println(
                "############  NODES AT GIVEN DISTANCE  ####################");

        System.out.println(
                "############################################################");

        System.out.println();

        List<MethodCase> methods =
                List.of(

                        new MethodCase(
                                "Brute Force",
                                NodesAtGivenDistance
                                        ::nodesAtGivenDistanceBruteForce),

                        new MethodCase(
                                "Recursive",
                                NodesAtGivenDistance
                                        ::nodesAtGivenDistanceRecursion),

                        new MethodCase(
                                "BFS with Parent Links",
                                NodesAtGivenDistance
                                        ::nodesAtGivenDistanceBFS)
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

            runAlgorithmTests(
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
