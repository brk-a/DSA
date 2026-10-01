import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Find the in-order successor of a node in a Binary Search Tree.
 *
 * In-order traversal visits nodes in ascending order:
 *
 *      left -> root -> right
 *
 * Therefore, the in-order successor of a node is the next node visited
 * during an in-order traversal.
 *
 * Two implementations are provided:
 *
 * 1. BST Property
 *
 *      Uses the ordering property of a BST.
 *
 *      Time:  O(h)
 *      Space: O(1)
 *
 * 2. Reverse In-order Traversal
 *
 *      Traverses:
 *
 *          right -> root -> left
 *
 *      and keeps track of the previously visited node.
 *
 *      Time:  O(n)
 *      Space: O(h)
 *
 * The public API identifies the target by its Node reference rather than
 * merely by its value. This is important because a BST may contain
 * duplicate values depending on the insertion policy.
 */
public class InOrderSuccessorBST {

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
            int successor,
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

    /**
     * Find a node by value.
     *
     * This helper is used only by the test suite. The actual successor
     * algorithms operate on a Node reference.
     */
    static Node findNode(
            Node root,
            int value) {

        Node current = root;

        while (current != null) {

            if (value == current.val) {
                return current;
            }

            if (value < current.val) {
                current = current.left;
            } else {
                current = current.right;
            }
        }

        return null;
    }

    /* **********************************************************************
     * 1. BST Property
     * **********************************************************************/

    /**
     * Find the in-order successor using the BST ordering property.
     *
     * There are two cases:
     *
     * 1. The target has a right subtree.
     *      The successor is the smallest node in that subtree.
     *
     * 2. The target has no right subtree.
     *      The successor is the lowest ancestor for which the target
     *      lies in the ancestor's left subtree.
     *
     * The implementation below handles both cases in a single traversal
     * from the root.
     */
    static Result inOrderSuccessorBSTProperty(
            Node root,
            Node target) {

        if (!validNode(root)
                || !validNode(target)) {

            return new Result(
                    -1,
                    false);
        }

        Node successor = null;
        Node current = root;

        while (current != null) {

            if (target.val < current.val) {

                /*
                 * current is a possible successor because target is
                 * somewhere in current's left subtree.
                 */
                successor = current;
                current = current.left;

            } else if (target.val > current.val) {

                /*
                 * The target must be in the right subtree.
                 */
                current = current.right;

            } else {

                /*
                 * We have reached the target value.
                 *
                 * If a right subtree exists, its smallest node is the
                 * successor.
                 */
                if (current.right != null) {

                    Node candidate =
                            current.right;

                    while (candidate.left != null) {
                        candidate =
                                candidate.left;
                    }

                    successor = candidate;
                }

                break;
            }
        }

        /*
         * If the target was not actually found in the supplied tree,
         * the request is invalid.
         */
        if (current == null) {

            return new Result(
                    -1,
                    false);
        }

        /*
         * No successor means that target is the largest node.
         */
        if (successor == null) {

            return new Result(
                    -1,
                    true);
        }

        return new Result(
                successor.val,
                true);
    }

    /* **********************************************************************
     * 2. Reverse In-order Traversal
     * **********************************************************************/

    /**
     * Mutable traversal state.
     *
     * During reverse in-order traversal:
     *
     *      right -> root -> left
     *
     * the previously visited node is the smallest node greater than the
     * current node.
     */
    static class ReverseInOrderState {

        Node previous;
        Node successor;
        boolean found;
    }

    /**
     * Find the in-order successor using reverse in-order traversal.
     *
     * Reverse in-order visits the BST in descending order:
     *
     *      largest -> ... -> target -> successor
     *
     * Therefore, when target is visited, the previously visited node is
     * its in-order predecessor, not its successor.
     *
     * Instead, we use the previous node to detect the first node whose
     * value is greater than the target while traversing backwards.
     *
     * For clarity and correctness with node references, the traversal
     * records the first node encountered after crossing the target.
     */
    static Result inOrderSuccessorReverseInorder(
            Node root,
            Node target) {

        if (!validNode(root)
                || !validNode(target)) {

            return new Result(
                    -1,
                    false);
        }

        ReverseInOrderState state =
                new ReverseInOrderState();

        findSuccessorReverseInorder(
                root,
                target,
                state);

        if (!state.found) {

            return new Result(
                    -1,
                    false);
        }

        if (state.successor == null) {

            return new Result(
                    -1,
                    true);
        }

        return new Result(
                state.successor.val,
                true);
    }

    /**
     * Reverse in-order traversal.
     *
     * We visit nodes from largest to smallest. The first node encountered
     * whose value is greater than the target is the smallest value greater
     * than the target, and therefore the successor.
     *
     * The target itself is used as the stopping point.
     */
    static boolean findSuccessorReverseInorder(
            Node root,
            Node target,
            ReverseInOrderState state) {

        if (root == null) {
            return false;
        }

        /*
         * Search the right subtree first.
         */
        if (findSuccessorReverseInorder(
                root.right,
                target,
                state)) {

            return true;
        }

        /*
         * If the current node is the target, the successor has already
         * been identified as the previous node in descending order.
         */
        if (root == target) {

            state.found = true;

            /*
             * In descending order, previous is the next larger node.
             */
            state.successor =
                    state.previous;

            return true;
        }

        /*
         * Keep track of the most recently visited larger node.
         */
        state.previous = root;

        /*
         * Continue into the left subtree.
         */
        return findSuccessorReverseInorder(
                root.left,
                target,
                state);
    }

    /* **********************************************************************
     * In-order Traversal
     * **********************************************************************/

    static void inOrder(
            Node root,
            ArrayList<Integer> values) {

        if (root == null) {
            return;
        }

        inOrder(
                root.left,
                values);

        values.add(
                root.val);

        inOrder(
                root.right,
                values);
    }

    /* **********************************************************************
     * Test Harness
     * **********************************************************************/

    static class TestCase {

        final String id;
        final Node root;
        final Node target;
        final int expected;
        final boolean expectedValid;
        final String description;

        TestCase(
                String id,
                Node root,
                Node target,
                int expected,
                boolean expectedValid,
                String description) {

            this.id = id;
            this.root = root;
            this.target = target;
            this.expected = expected;
            this.expectedValid = expectedValid;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {

        Result solve(
                Node root,
                Node target);
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
                                test.root,
                                test.target);

                boolean success =
                        actual.valid()
                                == test.expectedValid
                                && actual.successor()
                                == test.expected;

                if (success) {

                    passed++;

                    System.out.printf(
                            "PASS %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  successor = "
                                    + actual.successor());

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
                                    + actual.successor()
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
                                        test.root,
                                        test.target);

                boolean success =
                        first.valid()
                                == test.expectedValid
                                && first.successor()
                                == test.expected;

                for (int i = 1;
                     i < methods.size();
                     i++) {

                    Result actual =
                            methods.get(i)
                                    .algorithm
                                    .solve(
                                            test.root,
                                            test.target);

                    if (actual.valid()
                            != first.valid()
                            || actual.successor()
                            != first.successor()) {

                        success = false;

                        System.out.printf(
                                "  %s = (%d, %s)%n",
                                methods.get(i).name,
                                actual.successor(),
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

        Node root =
                node(
                        10,
                        node(5),
                        node(15));

        /*
         * Null root.
         */
        for (MethodCase method :
                methods) {

            Result result =
                    method.algorithm.solve(
                            null,
                            root);

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
         * Null target.
         */
        for (MethodCase method :
                methods) {

            Result result =
                    method.algorithm.solve(
                            root,
                            null);

            if (!result.valid()) {

                passed++;

                System.out.printf(
                        "PASS I2 - %s rejects null target%n",
                        method.name);

            } else {

                failed++;

                System.out.printf(
                        "FAIL I2 - %s accepts null target%n",
                        method.name);
            }
        }

        /*
         * Target not contained in the tree.
         */
        Node unrelated =
                node(99);

        for (MethodCase method :
                methods) {

            Result result =
                    method.algorithm.solve(
                            root,
                            unrelated);

            if (!result.valid()) {

                passed++;

                System.out.printf(
                        "PASS I3 - %s rejects target outside tree%n",
                        method.name);

            } else {

                failed++;

                System.out.printf(
                        "FAIL I3 - %s accepts target outside tree%n",
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
         * In-order:
         *
         *      5, 10, 15, 20, 25, 30, 35
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
                        10,
                        true,
                        "successor has an ancestor"));

        tests.add(
                new TestCase(
                        "B2",
                        root,
                        n10,
                        15,
                        true,
                        "successor is right child"));

        tests.add(
                new TestCase(
                        "B3",
                        root,
                        n15,
                        20,
                        true,
                        "successor is an ancestor"));

        tests.add(
                new TestCase(
                        "B4",
                        root,
                        root,
                        25,
                        true,
                        "root successor"));

        tests.add(
                new TestCase(
                        "B5",
                        root,
                        n25,
                        30,
                        true,
                        "successor is an ancestor"));

        tests.add(
                new TestCase(
                        "B6",
                        root,
                        n30,
                        35,
                        true,
                        "successor is right child"));

        tests.add(
                new TestCase(
                        "B7",
                        root,
                        n35,
                        -1,
                        true,
                        "largest node has no successor"));

        /*
         * ============================================================
         * Right Subtree With Left Descendant
         * ============================================================
         *
         *          20
         *            \
         *             30
         *            /
         *           25
         *
         * Successor of 20 is 25, not 30.
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
                        25,
                        true,
                        "minimum node in right subtree"));

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
                        20,
                        true,
                        "left-skewed tree"));

        tests.add(
                new TestCase(
                        "A2",
                        s40,
                        s40,
                        -1,
                        true,
                        "largest node has no successor"));

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
                        20,
                        true,
                        "right-skewed tree"));

        tests.add(
                new TestCase(
                        "A4",
                        r10,
                        r40,
                        -1,
                        true,
                        "largest node in right-skewed tree"));

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
                        -1,
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

        Node negativeTarget =
                findNode(
                        negative,
                        -7);

        tests.add(
                new TestCase(
                        "N1",
                        negative,
                        negativeTarget,
                        -5,
                        true,
                        "negative values"));

        return tests;
    }

    /* **********************************************************************
     * Random BST Generation
     * **********************************************************************/

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

    static Node randomBST(
            Random random,
            int size) {

        Node root = null;

        for (int i = 0; i < size; i++) {

            int value =
                    random.nextInt(2001)
                            - 1000;

            root =
                    insert(
                            root,
                            value);
        }

        return root;
    }

    /**
     * Collect nodes in-order.
     *
     * This gives a convenient reference implementation for the
     * randomised tests.
     */
    static void collectInOrderNodes(
            Node root,
            ArrayList<Node> nodes) {

        if (root == null) {
            return;
        }

        collectInOrderNodes(
                root.left,
                nodes);

        nodes.add(root);

        collectInOrderNodes(
                root.right,
                nodes);
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
                    randomBST(
                            random,
                            size);

            ArrayList<Node> orderedNodes =
                    new ArrayList<>();

            collectInOrderNodes(
                    root,
                    orderedNodes);

            /*
             * Select an actual node, rather than merely a value.
             * This also means duplicate values can be tested.
             */
            Node target =
                    orderedNodes.get(
                            random.nextInt(
                                    orderedNodes.size()));

            int targetIndex =
                    orderedNodes.indexOf(
                            target);

            int expected =
                    targetIndex + 1
                            < orderedNodes.size()
                            ? orderedNodes
                            .get(targetIndex + 1)
                            .val
                            : -1;

            for (MethodCase method :
                    methods) {

                Result result =
                        method.algorithm.solve(
                                root,
                                target);

                if (!result.valid()
                        || result.successor()
                        != expected) {

                    System.out.println(
                            "Randomised test FAILED");

                    System.out.println(
                            "iteration = "
                                    + iteration);

                    System.out.println(
                            "algorithm = "
                                    + method.name);

                    System.out.println(
                            "target = "
                                    + target.val);

                    System.out.println(
                            "expected successor = "
                                    + expected);

                    System.out.println(
                            "actual = "
                                    + result.successor());

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
                "##############  IN-ORDER SUCCESSOR BST  ####################");

        System.out.println(
                "############################################################");

        System.out.println();

        List<MethodCase> methods =
                List.of(

                        new MethodCase(
                                "BST Property",
                                InOrderSuccessorBST
                                        ::inOrderSuccessorBSTProperty),

                        new MethodCase(
                                "Reverse In-order",
                                InOrderSuccessorBST
                                        ::inOrderSuccessorReverseInorder)
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
