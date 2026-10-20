import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Connect nodes at the same level in a binary tree.
 *
 * Two implementations are provided:
 *
 * 1. Level-order traversal
 *
 *      Uses a queue to visit the tree level by level.
 *
 *      Time:  O(n)
 *      Space: O(w)
 *
 *      where w is the maximum width of the tree.
 *
 * 2. Set next-right pointers
 *
 *      Connects every node to the next node on the same level using a
 *      nextRight pointer.
 *
 *      Time:  O(n)
 *      Space: O(1) auxiliary space.
 *
 * The returned Result contains the values grouped by level.
 */
public class ConnectNodesAtSameLevel {

    /* **********************************************************************
     * Node
     * **********************************************************************/

    static class Node {

        int val;
        Node left;
        Node right;
        Node nextRight;

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
            ArrayList<ArrayList<Integer>> result,
            boolean valid) {
    }

    /* **********************************************************************
     * Helpers
     * **********************************************************************/

    static boolean validNode(
            Node node) {

        return node != null;
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
     * Compare two results.
     */
    static boolean sameResult(
            Result first,
            Result second) {

        if (first.valid() != second.valid()) {
            return false;
        }

        if (!first.valid()) {
            return true;
        }

        return first.result().equals(
                second.result());
    }

    /**
     * Reference level-order implementation.
     *
     * This is deliberately kept separate from the algorithms under test.
     * It is used by the randomised test suite as an independent reference.
     */
    static ArrayList<ArrayList<Integer>> referenceLevelOrder(
            Node root) {

        ArrayList<ArrayList<Integer>> result =
                new ArrayList<>();

        if (root == null) {
            return result;
        }

        Queue<Node> queue =
                new ArrayDeque<>();

        queue.offer(root);

        while (!queue.isEmpty()) {

            int levelSize =
                    queue.size();

            ArrayList<Integer> level =
                    new ArrayList<>();

            for (int i = 0;
                 i < levelSize;
                 i++) {

                Node current =
                        queue.poll();

                level.add(
                        current.val);

                if (current.left != null) {
                    queue.offer(
                            current.left);
                }

                if (current.right != null) {
                    queue.offer(
                            current.right);
                }
            }

            result.add(level);
        }

        return result;
    }

    /**
     * Remove all nextRight pointers from the tree.
     */
    static void clearNextRights(
            Node root) {

        if (root == null) {
            return;
        }

        Queue<Node> queue =
                new ArrayDeque<>();

        queue.offer(root);

        while (!queue.isEmpty()) {

            Node current =
                    queue.poll();

            current.nextRight = null;

            if (current.left != null) {
                queue.offer(
                        current.left);
            }

            if (current.right != null) {
                queue.offer(
                        current.right);
            }
        }
    }

    /* **********************************************************************
     * 1. Level-order Traversal
     * **********************************************************************/

    /**
     * Group nodes by level using a queue.
     *
     * Each iteration processes exactly one level.
     */
    static Result connectNodesAtSameLevelLevelOrder(
            Node root) {

        if (!validNode(root)) {

            return new Result(
                    null,
                    false);
        }

        ArrayList<ArrayList<Integer>> result =
                new ArrayList<>();

        Queue<Node> queue =
                new ArrayDeque<>();

        queue.offer(root);

        while (!queue.isEmpty()) {

            int levelSize =
                    queue.size();

            ArrayList<Integer> level =
                    new ArrayList<>();

            for (int i = 0;
                 i < levelSize;
                 i++) {

                Node current =
                        queue.poll();

                level.add(
                        current.val);

                if (current.left != null) {
                    queue.offer(
                            current.left);
                }

                if (current.right != null) {
                    queue.offer(
                            current.right);
                }
            }

            result.add(level);
        }

        return new Result(
                result,
                true);
    }

    /* **********************************************************************
     * 2. Set Next-right Pointers
     * **********************************************************************/

    /**
     * Connect nodes at the same level using nextRight pointers.
     *
     * This implementation does not require a queue.
     *
     * While traversing the current level through nextRight pointers,
     * the children are connected from left to right to form the next
     * level.
     *
     * The algorithm works for arbitrary binary trees, including sparse
     * and incomplete trees.
     */
    static Result connectNodesAtSameLevelSetNextRights(
            Node root) {

        if (!validNode(root)) {

            return new Result(
                    null,
                    false);
        }

        clearNextRights(root);

        ArrayList<ArrayList<Integer>> result =
                new ArrayList<>();

        Node levelStart =
                root;

        while (levelStart != null) {

            ArrayList<Integer> level =
                    new ArrayList<>();

            Node current =
                    levelStart;

            Node nextLevelStart =
                    null;

            Node previousChild =
                    null;

            while (current != null) {

                level.add(
                        current.val);

                /*
                 * Process the left child.
                 */
                if (current.left != null) {

                    if (nextLevelStart == null) {

                        nextLevelStart =
                                current.left;
                    }

                    if (previousChild != null) {

                        previousChild.nextRight =
                                current.left;
                    }

                    previousChild =
                            current.left;
                }

                /*
                 * Process the right child.
                 */
                if (current.right != null) {

                    if (nextLevelStart == null) {

                        nextLevelStart =
                                current.right;
                    }

                    if (previousChild != null) {

                        previousChild.nextRight =
                                current.right;
                    }

                    previousChild =
                            current.right;
                }

                current =
                        current.nextRight;
            }

            /*
             * The final node on the level must point to null.
             */
            if (previousChild != null) {

                previousChild.nextRight =
                        null;
            }

            result.add(level);

            levelStart =
                    nextLevelStart;
        }

        return new Result(
                result,
                true);
    }

    /* **********************************************************************
     * Next-right Validation
     * **********************************************************************/

    /**
     * Verify that every nextRight pointer points to the next node on the
     * same level, or null for the final node on that level.
     */
    static boolean validateNextRights(
            Node root) {

        if (root == null) {
            return false;
        }

        Queue<Node> queue =
                new ArrayDeque<>();

        queue.offer(root);

        while (!queue.isEmpty()) {

            int levelSize =
                    queue.size();

            ArrayList<Node> level =
                    new ArrayList<>();

            for (int i = 0;
                 i < levelSize;
                 i++) {

                Node current =
                        queue.poll();

                level.add(
                        current);

                if (current.left != null) {
                    queue.offer(
                            current.left);
                }

                if (current.right != null) {
                    queue.offer(
                            current.right);
                }
            }

            for (int i = 0;
                 i < level.size();
                 i++) {

                Node expectedNext =
                        i + 1 < level.size()
                                ? level.get(i + 1)
                                : null;

                if (level.get(i).nextRight
                        != expectedNext) {

                    return false;
                }
            }
        }

        return true;
    }

    /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    static class TestCase {

        final String id;
        final Node root;
        final ArrayList<ArrayList<Integer>> expected;
        final boolean expectedValid;
        final String description;

        TestCase(
                String id,
                Node root,
                ArrayList<ArrayList<Integer>> expected,
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

        for (TestCase test :
                tests) {

            try {

                clearNextRights(
                        test.root);

                Result actual =
                        method.algorithm.solve(
                                test.root);

                boolean success =
                        actual.valid()
                                == test.expectedValid;

                if (success
                        && test.expectedValid) {

                    success =
                            actual.result().equals(
                                    test.expected);
                }

                if (success
                        && method.name.equals(
                                "Set Next-right Pointers")) {

                    success =
                            validateNextRights(
                                    test.root);
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

                    System.out.println(
                            "  expected = "
                                    + test.expected
                                    + ", valid = "
                                    + test.expectedValid);

                    System.out.println(
                            "  actual   = "
                                    + actual.result()
                                    + ", valid = "
                                    + actual.valid());

                    if (method.name.equals(
                            "Set Next-right Pointers")) {

                        System.out.println(
                                "  nextRight valid = "
                                        + validateNextRights(
                                        test.root));
                    }
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

        for (TestCase test :
                tests) {

            try {

                clearNextRights(
                        test.root);

                Result first =
                        methods.get(0)
                                .algorithm
                                .solve(
                                        test.root);

                boolean success =
                        first.valid()
                                == test.expectedValid
                                && (!test.expectedValid
                                || first.result().equals(
                                test.expected));

                for (int i = 1;
                     i < methods.size();
                     i++) {

                    clearNextRights(
                            test.root);

                    Result actual =
                            methods.get(i)
                                    .algorithm
                                    .solve(
                                            test.root);

                    if (!sameResult(
                            first,
                            actual)) {

                        success = false;

                        System.out.printf(
                                "  %s = (%s, %s)%n",
                                methods.get(i).name,
                                actual.result(),
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

        for (MethodCase method :
                methods) {

            Result result =
                    method.algorithm.solve(
                            null);

            if (!result.valid()
                    && result.result() == null) {

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
         * Verify that stale nextRight pointers are overwritten.
         */
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

        root.nextRight =
                root;

        root.left.nextRight =
                root.right;

        root.right.nextRight =
                root;

        Result result =
                methods.get(1)
                        .algorithm
                        .solve(
                                root);

        ArrayList<ArrayList<Integer>> expected =
                levels(
                        List.of(
                                List.of(10),
                                List.of(5, 15),
                                List.of(2, 7, 12, 20)));

        if (result.valid()
                && result.result().equals(
                expected)
                && validateNextRights(
                root)) {

            passed++;

            System.out.println(
                    "PASS I2 - stale nextRight pointers are overwritten");

        } else {

            failed++;

            System.out.println(
                    "FAIL I2 - stale nextRight pointers are not handled");
        }

        printResults(
                passed,
                failed,
                passed + failed);
    }

    /* **********************************************************************
     * Test Data Helpers
     * **********************************************************************/

    static ArrayList<ArrayList<Integer>> levels(
            List<List<Integer>> values) {

        ArrayList<ArrayList<Integer>> result =
                new ArrayList<>();

        for (List<Integer> level :
                values) {

            result.add(
                    new ArrayList<>(
                            level));
        }

        return result;
    }

    /* **********************************************************************
     * Test Data
     * **********************************************************************/

    static List<TestCase> buildTests() {

        List<TestCase> tests =
                new ArrayList<>();

        /*
         * ============================================================
         * Complete Binary Tree
         * ============================================================
         *
         *             1
         *           /   \
         *          2     3
         *         / \   / \
         *        4   5 6   7
         */

        Node n4 =
                node(4);

        Node n5 =
                node(5);

        Node n6 =
                node(6);

        Node n7 =
                node(7);

        Node n2 =
                node(
                        2,
                        n4,
                        n5);

        Node n3 =
                node(
                        3,
                        n6,
                        n7);

        Node root =
                node(
                        1,
                        n2,
                        n3);

        tests.add(
                new TestCase(
                        "B1",
                        root,
                        levels(
                                List.of(
                                        List.of(1),
                                        List.of(2, 3),
                                        List.of(4, 5, 6, 7))),
                        true,
                        "complete binary tree"));

        /*
         * ============================================================
         * Sparse Binary Tree
         * ============================================================
         *
         *             1
         *           /   \
         *          2     3
         *           \     \
         *            5     7
         *           /
         *          8
         */

        Node s8 =
                node(8);

        Node s5 =
                node(
                        5,
                        s8,
                        null);

        Node s2 =
                node(
                        2,
                        null,
                        s5);

        Node s7 =
                node(7);

        Node s3 =
                node(
                        3,
                        null,
                        s7);

        Node sparseRoot =
                node(
                        1,
                        s2,
                        s3);

        tests.add(
                new TestCase(
                        "B2",
                        sparseRoot,
                        levels(
                                List.of(
                                        List.of(1),
                                        List.of(2, 3),
                                        List.of(5, 7),
                                        List.of(8))),
                        true,
                        "sparse binary tree"));

        /*
         * ============================================================
         * Left-Skewed Tree
         * ============================================================
         */

        Node l1 =
                node(1);

        Node l2 =
                node(
                        2,
                        l1,
                        null);

        Node l3 =
                node(
                        3,
                        l2,
                        null);

        Node l4 =
                node(
                        4,
                        l3,
                        null);

        tests.add(
                new TestCase(
                        "S1",
                        l4,
                        levels(
                                List.of(
                                        List.of(4),
                                        List.of(3),
                                        List.of(2),
                                        List.of(1))),
                        true,
                        "left-skewed tree"));

        /*
         * ============================================================
         * Right-Skewed Tree
         * ============================================================
         */

        Node r4 =
                node(4);

        Node r3 =
                node(
                        3,
                        null,
                        r4);

        Node r2 =
                node(
                        2,
                        null,
                        r3);

        Node r1 =
                node(
                        1,
                        null,
                        r2);

        tests.add(
                new TestCase(
                        "S2",
                        r1,
                        levels(
                                List.of(
                                        List.of(1),
                                        List.of(2),
                                        List.of(3),
                                        List.of(4))),
                        true,
                        "right-skewed tree"));

        /*
         * ============================================================
         * Single Node
         * ============================================================
         */

        Node single =
                node(100);

        tests.add(
                new TestCase(
                        "S3",
                        single,
                        levels(
                                List.of(
                                        List.of(100))),
                        true,
                        "single-node tree"));

        /*
         * ============================================================
         * Negative Values
         * ============================================================
         */

        Node negative =
                node(
                        -10,
                        node(-20),
                        node(
                                -5,
                                node(-7),
                                node(-1)));

        tests.add(
                new TestCase(
                        "N1",
                        negative,
                        levels(
                                List.of(
                                        List.of(-10),
                                        List.of(-20, -5),
                                        List.of(-7, -1))),
                        true,
                        "negative values"));

        /*
         * ============================================================
         * Uneven Tree
         * ============================================================
         *
         *             10
         *            /  \
         *           5    20
         *            \   /
         *             7 15
         *                  \
         *                   17
         */

        Node u17 =
                node(17);

        Node u15 =
                node(
                        15,
                        null,
                        u17);

        Node u7 =
                node(7);

        Node u5 =
                node(
                        5,
                        null,
                        u7);

        Node u20 =
                node(
                        20,
                        u15,
                        null);

        Node uneven =
                node(
                        10,
                        u5,
                        u20);

        tests.add(
                new TestCase(
                        "U1",
                        uneven,
                        levels(
                                List.of(
                                        List.of(10),
                                        List.of(5, 20),
                                        List.of(7, 15),
                                        List.of(17))),
                        true,
                        "uneven tree with gaps"));

        return tests;
    }

    /* **********************************************************************
     * Random Tree Generation
     * **********************************************************************/

    static Node randomTree(
            Random random,
            int size) {

        if (size <= 0) {
            return null;
        }

        Node root =
                node(
                        random.nextInt(2001)
                                - 1000);

        ArrayList<Node> available =
                new ArrayList<>();

        available.add(root);

        for (int i = 1;
             i < size;
             i++) {

            Node parent =
                    available.get(
                            random.nextInt(
                                    available.size()));

            Node child =
                    node(
                            random.nextInt(2001)
                                    - 1000);

            if (parent.left == null
                    && parent.right == null) {

                if (random.nextBoolean()) {

                    parent.left =
                            child;

                } else {

                    parent.right =
                            child;
                }

            } else if (parent.left == null) {

                parent.left =
                        child;

            } else {

                parent.right =
                        child;
            }

            available.add(
                    child);

            if (parent.left != null
                    && parent.right != null) {

                available.remove(
                        parent);
            }
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
                new Random(
                        20261008L);

        for (int iteration = 1;
             iteration <= iterations;
             iteration++) {

            int size =
                    1 + random.nextInt(100);

            Node root =
                    randomTree(
                            random,
                            size);

            ArrayList<ArrayList<Integer>> expected =
                    referenceLevelOrder(
                            root);

            for (MethodCase method :
                    methods) {

                clearNextRights(
                        root);

                Result result =
                        method.algorithm.solve(
                                root);

                boolean success =
                        result.valid()
                                && result.result().equals(
                                expected);

                if (method.name.equals(
                        "Set Next-right Pointers")) {

                    success =
                            success
                                    && validateNextRights(
                                    root);
                }

                if (!success) {

                    System.out.println(
                            "Randomised test FAILED");

                    System.out.println(
                            "iteration = "
                                    + iteration);

                    System.out.println(
                            "algorithm = "
                                    + method.name);

                    System.out.println(
                            "expected = "
                                    + expected);

                    System.out.println(
                            "actual = "
                                    + result.result());

                    System.out.println(
                            "valid = "
                                    + result.valid());

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
                "############  CONNECT NODES AT SAME LEVEL  #################");

        System.out.println(
                "############################################################");

        System.out.println();

        List<MethodCase> methods =
                List.of(

                        new MethodCase(
                                "Level-order Traversal",
                                ConnectNodesAtSameLevel
                                        ::connectNodesAtSameLevelLevelOrder),

                        new MethodCase(
                                "Set Next-right Pointers",
                                ConnectNodesAtSameLevel
                                        ::connectNodesAtSameLevelSetNextRights)
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
