import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Serialise and deserialise a binary tree.
 *
 * Two serialisation formats are provided:
 *
 * 1. Pre-order
 *      root -> left -> right
 *
 * 2. Level-order
 *      breadth-first traversal
 *
 * Null children are represented by NULL_MARKER so that the
 * original tree structure can be reconstructed exactly.
 *
 * Example:
 *
 *             1
 *           /   \
 *          2     3
 *         / \     \
 *        4   5     6
 *
 * Pre-order:
 *
 *      [1, 2, 4, NULL, NULL, 5, NULL, NULL,
 *       3, NULL, 6, NULL, NULL]
 *
 * Level-order:
 *
 *      [1, 2, 3, 4, 5, NULL, 6]
 *
 * Implementations:
 *
 * 1. Pre-order Serialisation
 *      Time:  O(n)
 *      Space: O(n)
 *
 * 2. Level-order Serialisation
 *      Time:  O(n)
 *      Space: O(w)
 *
 * 3. Pre-order Deserialisation
 *      Time:  O(n)
 *      Space: O(h)
 *
 * 4. Level-order Deserialisation
 *      Time:  O(n)
 *      Space: O(w)
 *
 * where:
 *
 *      n = number of nodes
 *      h = tree height
 *      w = maximum tree width
 */
public class SeraliseDeseraliseBinaryTree {

    /* **********************************************************************
     * Constants
     * **********************************************************************/

    /**
     * Sentinel used to represent a null child.
     *
     * Note:
     * Integer.MIN_VALUE cannot therefore be used as a normal node value
     * with this representation.
     */
    static final int NULL_MARKER = Integer.MIN_VALUE;

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

    static record SerialiseResult(
            ArrayList<ArrayList<Integer>> result,
            boolean valid) {
    }

    static record DeserialiseResult(
            Node result,
            boolean valid) {
    }

    /* **********************************************************************
     * Helpers
     * **********************************************************************/

    static boolean validNode(Node root) {
        return root != null;
    }

    static boolean validArray(
            ArrayList<ArrayList<Integer>> array) {

        return array != null
                && !array.isEmpty()
                && array.get(0) != null
                && !array.get(0).isEmpty();
    }

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
     * The public API uses ArrayList<ArrayList<Integer>> to match
     * the structure from the original implementation.
     *
     * The first inner ArrayList contains the serialised values.
     */
    static ArrayList<ArrayList<Integer>> wrap(
            ArrayList<Integer> values) {

        ArrayList<ArrayList<Integer>> result =
                new ArrayList<>();

        result.add(values);

        return result;
    }

    static ArrayList<Integer> unwrap(
            ArrayList<ArrayList<Integer>> array) {

        if (!validArray(array)) {
            return null;
        }

        return array.get(0);
    }

    /* **********************************************************************
     * 1. Pre-order Serialisation
     * **********************************************************************/

    static SerialiseResult seraliseBinaryTreePreOrder(
            Node root) {

        if (!validNode(root)) {
            return new SerialiseResult(
                    null,
                    false);
        }

        ArrayList<Integer> result =
                new ArrayList<>();

        seralisePreOrder(
                root,
                result);

        return new SerialiseResult(
                wrap(result),
                true);
    }

    static void seralisePreOrder(
            Node root,
            ArrayList<Integer> result) {

        if (root == null) {

            result.add(NULL_MARKER);

            return;
        }

        result.add(root.val);

        seralisePreOrder(
                root.left,
                result);

        seralisePreOrder(
                root.right,
                result);
    }

    /* **********************************************************************
     * 2. Level-order Serialisation
     * **********************************************************************/

    static SerialiseResult seraliseBinaryTreeLevelOrder(
            Node root) {

        if (!validNode(root)) {
            return new SerialiseResult(
                    null,
                    false);
        }

        ArrayList<Integer> result =
                new ArrayList<>();

        /*
         * LinkedList is deliberately used here because its Queue
         * implementation permits null elements.
         *
         * Null children must be included in the queue so that
         * structural information is preserved.
         */
        Queue<Node> queue =
                new LinkedList<>();

        queue.offer(root);

        while (!queue.isEmpty()) {

            Node current =
                    queue.poll();

            if (current == null) {

                result.add(NULL_MARKER);

                continue;
            }

            result.add(current.val);

            queue.offer(current.left);
            queue.offer(current.right);
        }

        /*
         * Trailing null markers do not carry useful information.
         *
         * For example:
         *
         *      [1, 2, 3, NULL, NULL, NULL, NULL]
         *
         * can safely become:
         *
         *      [1, 2, 3]
         */
        while (!result.isEmpty()
                && result.get(result.size() - 1)
                == NULL_MARKER) {

            result.remove(result.size() - 1);
        }

        return new SerialiseResult(
                wrap(result),
                true);
    }

    /* **********************************************************************
     * 3. Pre-order Deserialisation
     * **********************************************************************/

    static DeserialiseResult deseraliseBinaryTreePreOrder(
            ArrayList<ArrayList<Integer>> array) {

        if (!validArray(array)) {
            return new DeserialiseResult(
                    null,
                    false);
        }

        ArrayList<Integer> values =
                unwrap(array);

        int[] index = {0};

        Node root =
                deseralisePreOrder(
                        values,
                        index);

        /*
         * A valid serialisation must consume every value.
         */
        if (root == null
                || index[0] != values.size()) {

            return new DeserialiseResult(
                    null,
                    false);
        }

        return new DeserialiseResult(
                root,
                true);
    }

    static Node deseralisePreOrder(
            ArrayList<Integer> values,
            int[] index) {

        /*
         * If the serialised input ends unexpectedly, the
         * structure is incomplete.
         */
        if (index[0] >= values.size()) {
            return null;
        }

        int value =
                values.get(index[0]++);

        if (value == NULL_MARKER) {
            return null;
        }

        Node root =
                new Node(value);

        root.left =
                deseralisePreOrder(
                        values,
                        index);

        /*
         * If there are no remaining values after constructing
         * the left subtree, the input is incomplete.
         *
         * The final validation in the caller will reject it.
         */
        root.right =
                deseralisePreOrder(
                        values,
                        index);

        return root;
    }

    /* **********************************************************************
     * 4. Level-order Deserialisation
     * **********************************************************************/

    static DeserialiseResult deseraliseBinaryTreeLevelOrder(
            ArrayList<ArrayList<Integer>> array) {

        if (!validArray(array)) {
            return new DeserialiseResult(
                    null,
                    false);
        }

        ArrayList<Integer> values =
                unwrap(array);

        if (values.isEmpty()
                || values.get(0) == NULL_MARKER) {

            return new DeserialiseResult(
                    null,
                    false);
        }

        Node root =
                new Node(values.get(0));

        Queue<Node> queue =
                new ArrayDeque<>();

        queue.offer(root);

        int index = 1;

        while (!queue.isEmpty()) {

            Node current =
                    queue.poll();

            /*
             * Left child.
             */
            if (index < values.size()) {

                int leftValue =
                        values.get(index++);

                if (leftValue != NULL_MARKER) {

                    current.left =
                            new Node(leftValue);

                    queue.offer(current.left);
                }
            }

            /*
             * Right child.
             */
            if (index < values.size()) {

                int rightValue =
                        values.get(index++);

                if (rightValue != NULL_MARKER) {

                    current.right =
                            new Node(rightValue);

                    queue.offer(current.right);
                }
            }
        }

        /*
         * Any remaining values mean the input contains
         * structural information which could not be attached
         * to the tree.
         */
        if (index != values.size()) {

            return new DeserialiseResult(
                    null,
                    false);
        }

        return new DeserialiseResult(
                root,
                true);
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
     * Test Harness
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

    @FunctionalInterface
    interface SerialiseAlgorithm {

        SerialiseResult solve(Node root);
    }

    @FunctionalInterface
    interface DeserialiseAlgorithm {

        DeserialiseResult solve(
                ArrayList<ArrayList<Integer>> array);
    }

    static class SerialiseMethodCase {

        final String name;
        final SerialiseAlgorithm algorithm;

        SerialiseMethodCase(
                String name,
                SerialiseAlgorithm algorithm) {

            this.name = name;
            this.algorithm = algorithm;
        }
    }

    static class DeserialiseMethodCase {

        final String name;
        final DeserialiseAlgorithm algorithm;

        DeserialiseMethodCase(
                String name,
                DeserialiseAlgorithm algorithm) {

            this.name = name;
            this.algorithm = algorithm;
        }
    }

    /* **********************************************************************
     * Serialisation Tests
     * **********************************************************************/

    static void runSerialiseTests(
            String algorithmName,
            SerialiseAlgorithm method,
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

                SerialiseResult actual =
                        method.solve(test.root);

                if (actual.valid()) {

                    passed++;

                    System.out.printf(
                            "PASS %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  serialised = "
                                    + actual.result());

                } else {

                    failed++;

                    System.out.printf(
                            "FAIL %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  serialisation marked invalid");
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
     * Deserialisation Tests
     * **********************************************************************/

    static void runDeserialiseTests(
            String serialiseName,
            SerialiseAlgorithm serialise,
            DeserialiseAlgorithm deserialise,
            List<TestCase> tests) {

        System.out.println(
                "======================================================");

        System.out.println(
                serialiseName + " Round-trip");

        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {

            try {

                SerialiseResult serialised =
                        serialise.solve(test.root);

                if (!serialised.valid()) {

                    failed++;

                    System.out.printf(
                            "FAIL %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  serialisation failed");

                    continue;
                }

                DeserialiseResult deserialised =
                        deserialise.solve(
                                serialised.result());

                boolean equal =
                        deserialised.valid()
                                && treesEqual(
                                test.root,
                                deserialised.result());

                if (equal) {

                    passed++;

                    System.out.printf(
                            "PASS %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  serialised = "
                                    + serialised.result());

                } else {

                    failed++;

                    System.out.printf(
                            "FAIL %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  serialised = "
                                    + serialised.result());

                    System.out.println(
                            "  deserialised valid = "
                                    + deserialised.valid());
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
     * Invalid Input Tests
     * **********************************************************************/

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
         * Null root.
         */
        SerialiseResult nullSerialised =
                seraliseBinaryTreePreOrder(null);

        if (!nullSerialised.valid()) {

            passed++;

            System.out.println(
                    "PASS I1 - null root rejected");

        } else {

            failed++;

            System.out.println(
                    "FAIL I1 - null root should be invalid");
        }

        /*
         * Null array.
         */
        DeserialiseResult nullArray =
                deseraliseBinaryTreePreOrder(null);

        if (!nullArray.valid()) {

            passed++;

            System.out.println(
                    "PASS I2 - null array rejected");

        } else {

            failed++;

            System.out.println(
                    "FAIL I2 - null array should be invalid");
        }

        /*
         * Empty outer array.
         */
        DeserialiseResult emptyOuter =
                deseraliseBinaryTreePreOrder(
                        new ArrayList<>());

        if (!emptyOuter.valid()) {

            passed++;

            System.out.println(
                    "PASS I3 - empty outer array rejected");

        } else {

            failed++;

            System.out.println(
                    "FAIL I3 - empty outer array should be invalid");
        }

        /*
         * Empty inner array.
         */
        ArrayList<ArrayList<Integer>> emptyInner =
                new ArrayList<>();

        emptyInner.add(
                new ArrayList<>());

        DeserialiseResult emptyInnerResult =
                deseraliseBinaryTreePreOrder(
                        emptyInner);

        if (!emptyInnerResult.valid()) {

            passed++;

            System.out.println(
                    "PASS I4 - empty inner array rejected");

        } else {

            failed++;

            System.out.println(
                    "FAIL I4 - empty inner array should be invalid");
        }

        /*
         * Incomplete pre-order input.
         */
        ArrayList<ArrayList<Integer>> incompletePreOrder =
                wrap(
                        new ArrayList<>(
                                List.of(1, 2)));

        DeserialiseResult incompletePreOrderResult =
                deseraliseBinaryTreePreOrder(
                        incompletePreOrder);

        if (!incompletePreOrderResult.valid()) {

            passed++;

            System.out.println(
                    "PASS I5 - incomplete pre-order rejected");

        } else {

            failed++;

            System.out.println(
                    "FAIL I5 - incomplete pre-order should be invalid");
        }

        /*
         * Null root marker.
         */
        ArrayList<ArrayList<Integer>> nullRoot =
                wrap(
                        new ArrayList<>(
                                List.of(NULL_MARKER)));

        DeserialiseResult nullRootResult =
                deseraliseBinaryTreeLevelOrder(
                        nullRoot);

        if (!nullRootResult.valid()) {

            passed++;

            System.out.println(
                    "PASS I6 - null root serialisation rejected");

        } else {

            failed++;

            System.out.println(
                    "FAIL I6 - null root should be invalid");
        }

        /*
         * Extra level-order values.
         */
        ArrayList<ArrayList<Integer>> extraValues =
                wrap(
                        new ArrayList<>(
                                List.of(
                                        1,
                                        2,
                                        3,
                                        NULL_MARKER,
                                        NULL_MARKER,
                                        NULL_MARKER,
                                        NULL_MARKER,
                                        99)));

        DeserialiseResult extraValuesResult =
                deseraliseBinaryTreeLevelOrder(
                        extraValues);

        if (!extraValuesResult.valid()) {

            passed++;

            System.out.println(
                    "PASS I7 - extra level-order values rejected");

        } else {

            failed++;

            System.out.println(
                    "FAIL I7 - extra values should be invalid");
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
     * Random Tree Generation
     * **********************************************************************/

    static Node randomTree(
            Random rng,
            int depth,
            double nodeProbability) {

        if (depth == 0
                || rng.nextDouble() > nodeProbability) {

            return null;
        }

        int value =
                rng.nextInt(1000);

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
            int iterations) {

        System.out.println(
                "======================================================");

        System.out.println(
                "Randomised Round-trip Cross Checks");

        System.out.println(
                "======================================================");

        Random rng =
                new Random(20260913L);

        int completed = 0;

        while (completed < iterations) {

            Node original =
                    randomTree(
                            rng,
                            8,
                            0.75);

            /*
             * The public serialisation API treats a null root
             * as invalid, so generate another tree.
             */
            if (original == null) {
                continue;
            }

            completed++;

            /*
             * ========================================================
             * Pre-order
             * ========================================================
             */

            SerialiseResult preOrderSerialised =
                    seraliseBinaryTreePreOrder(
                            original);

            if (!preOrderSerialised.valid()) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "iteration = " + completed);

                System.out.println(
                        "pre-order serialisation invalid");

                return;
            }

            DeserialiseResult preOrderDeserialised =
                    deseraliseBinaryTreePreOrder(
                            preOrderSerialised.result());

            if (!preOrderDeserialised.valid()
                    || !treesEqual(
                    original,
                    preOrderDeserialised.result())) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "iteration = " + completed);

                System.out.println(
                        "pre-order serialised = "
                                + preOrderSerialised.result());

                System.out.println(
                        "pre-order deserialised valid = "
                                + preOrderDeserialised.valid());

                return;
            }

            /*
             * ========================================================
             * Level-order
             * ========================================================
             */

            SerialiseResult levelOrderSerialised =
                    seraliseBinaryTreeLevelOrder(
                            original);

            if (!levelOrderSerialised.valid()) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "iteration = " + completed);

                System.out.println(
                        "level-order serialisation invalid");

                return;
            }

            DeserialiseResult levelOrderDeserialised =
                    deseraliseBinaryTreeLevelOrder(
                            levelOrderSerialised.result());

            if (!levelOrderDeserialised.valid()
                    || !treesEqual(
                    original,
                    levelOrderDeserialised.result())) {

                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "iteration = " + completed);

                System.out.println(
                        "level-order serialised = "
                                + levelOrderSerialised.result());

                System.out.println(
                        "level-order deserialised valid = "
                                + levelOrderDeserialised.valid());

                return;
            }
        }

        System.out.printf(
                "All %d randomised tests passed.%n%n",
                iterations);
    }

    /* **********************************************************************
     * Main Test Suite
     * **********************************************************************/

    public static void main(String[] args) {

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
                                1,
                                node(2),
                                node(3)),
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
                        "complete three-level tree"));

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
                        "right-skewed tree"));

        tests.add(
                new TestCase(
                        "A3",
                        node(
                                1,
                                node(2),
                                null),
                        "left child only"));

        tests.add(
                new TestCase(
                        "A4",
                        node(
                                1,
                                null,
                                node(3)),
                        "right child only"));

        /*
         * ============================================================
         * Duplicate Values
         * ============================================================
         */

        tests.add(
                new TestCase(
                        "D1",
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
                        "duplicate values"));

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
                        "negative values"));

        /*
         * ============================================================
         * Larger Tree
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
                        "L1",
                        largeTree,
                        "larger irregular tree"));

        /*
         * ============================================================
         * Header
         * ============================================================
         */

        System.out.println(
                "############################################################");

        System.out.println(
                "##########  SERIALISE / DESERIALISE BINARY TREE  ##########");

        System.out.println(
                "############################################################");

        System.out.println();

        /*
         * ============================================================
         * Serialisation Implementations
         * ============================================================
         */

        List<SerialiseMethodCase> serialiseMethods =
                List.of(

                        new SerialiseMethodCase(
                                "Pre-order Serialisation",
                                SeraliseDeseraliseBinaryTree
                                        ::seraliseBinaryTreePreOrder),

                        new SerialiseMethodCase(
                                "Level-order Serialisation",
                                SeraliseDeseraliseBinaryTree
                                        ::seraliseBinaryTreeLevelOrder)
                );

        for (SerialiseMethodCase method :
                serialiseMethods) {

            runSerialiseTests(
                    method.name,
                    method.algorithm,
                    tests);
        }

        /*
         * ============================================================
         * Round-trip Tests
         * ============================================================
         */

        runDeserialiseTests(
                "Pre-order",
                SeraliseDeseraliseBinaryTree
                        ::seraliseBinaryTreePreOrder,
                SeraliseDeseraliseBinaryTree
                        ::deseraliseBinaryTreePreOrder,
                tests);

        runDeserialiseTests(
                "Level-order",
                SeraliseDeseraliseBinaryTree
                        ::seraliseBinaryTreeLevelOrder,
                SeraliseDeseraliseBinaryTree
                        ::deseraliseBinaryTreeLevelOrder,
                tests);

        /*
         * ============================================================
         * Invalid Input Tests
         * ============================================================
         */

        runInvalidInputTests();

        /*
         * ============================================================
         * Randomised Tests
         * ============================================================
         */

        runRandomisedTests(5000);
    }
}
