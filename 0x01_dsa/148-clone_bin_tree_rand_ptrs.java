import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class CloneBinaryTreeRandomPointers {

    static class Node {
        int val;
        Node left;
        Node right;
        Node random;

        Node(int val) {
            this.val = val;
        }

        Node(
            Node left,
            Node right,
            Node random,
            int val
        ) {
            this.left = left;
            this.right = right;
            this.random = random;
            this.val = val;
        }
    }

    static record Result(
        Node root,
        boolean valid
    ) {}

    static boolean validNode(Node root) {
        return root != null;
    }

    static Node node(int value) {
        return new Node(value);
    }

    static Node node(
        int value,
        Node left,
        Node right,
        Node random
    ) {
        return new Node(
            left,
            right,
            random,
            value
        );
    }

    /**
     * Deep clone using an original-to-copy HashMap.
     *
     * Pass 1: clone the binary-tree structure.
     * Pass 2: connect random pointers.
     *
     * Time: O(n)
     * Space: O(n)
     */
    static Result cloneBinaryTreeRandomPointerHashMap(Node root) {
        if (!validNode(root)) {
            return new Result(null, false);
        }

        Map<Node, Node> map = new HashMap<>();

        Node clonedRoot = copyLeftRightNodes(root, map);
        copyRandomPointers(root, map);

        return new Result(clonedRoot, true);
    }

    static Node copyLeftRightNodes(
        Node root,
        Map<Node, Node> map
    ) {
        if (root == null) {
            return null;
        }

        Node clonedNode = new Node(root.val);
        map.put(root, clonedNode);

        clonedNode.left =
            copyLeftRightNodes(root.left, map);

        clonedNode.right =
            copyLeftRightNodes(root.right, map);

        return clonedNode;
    }

    static void copyRandomPointers(
        Node root,
        Map<Node, Node> map
    ) {
        if (root == null) {
            return;
        }

        Node clonedNode = map.get(root);

        clonedNode.random =
            root.random == null
                ? null
                : map.get(root.random);

        copyRandomPointers(root.left, map);
        copyRandomPointers(root.right, map);
    }

    static List<Node> collectNodes(Node root) {
        List<Node> nodes = new ArrayList<>();
        collectNodes(root, nodes);
        return nodes;
    }

    static void collectNodes(
        Node root,
        List<Node> nodes
    ) {
        if (root == null) {
            return;
        }

        nodes.add(root);
        collectNodes(root.left, nodes);
        collectNodes(root.right, nodes);
    }

    static boolean isDeepClone(Node original, Node clone) {
        if (original == null || clone == null) {
            return original == clone;
        }

        List<Node> originalNodes =
            collectNodes(original);

        List<Node> clonedNodes =
            collectNodes(clone);

        if (originalNodes.size() != clonedNodes.size()) {
            return false;
        }

        Map<Node, Node> expectedMapping = new HashMap<>();

        for (int i = 0; i < originalNodes.size(); i++) {
            Node originalNode = originalNodes.get(i);
            Node clonedNode = clonedNodes.get(i);

            if (originalNode == clonedNode) {
                return false;
            }

            if (originalNode.val != clonedNode.val) {
                return false;
            }

            expectedMapping.put(originalNode, clonedNode);
        }

        for (int i = 0; i < originalNodes.size(); i++) {
            Node originalNode = originalNodes.get(i);
            Node clonedNode = clonedNodes.get(i);

            if (
                clonedNode.left
                != expectedMapping.get(originalNode.left)
            ) {
                return false;
            }

            if (
                clonedNode.right
                != expectedMapping.get(originalNode.right)
            ) {
                return false;
            }

            if (
                clonedNode.random
                != expectedMapping.get(originalNode.random)
            ) {
                return false;
            }
        }

        return true;
    }

    static class TestCase {
        final String id;
        final Node root;
        final boolean expectedValid;
        final String description;

        TestCase(
            String id,
            Node root,
            boolean expectedValid,
            String description
        ) {
            this.id = id;
            this.root = root;
            this.expectedValid = expectedValid;
            this.description = description;
        }
    }

    static void runTests(
        String algorithm,
        Function<TestCase, Result> method,
        List<TestCase> tests
    ) {
        System.out.println("==================================================");
        System.out.println(algorithm);
        System.out.println("==================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {
            try {
                Result actual = method.apply(test);

                boolean cloneCorrect =
                    test.root == null
                        ? actual.root() == null
                        : isDeepClone(test.root, actual.root());

                if (
                    actual.valid() == test.expectedValid
                    && cloneCorrect
                ) {
                    passed++;
                    System.out.printf(
                        "✓ %s (%s)%n",
                        test.id,
                        test.description
                    );
                } else {
                    failed++;
                    System.out.printf(
                        "✗ %s (%s)%n",
                        test.id,
                        test.description
                    );
                    System.out.printf(
                        "  expected valid = %s%n",
                        test.expectedValid
                    );
                    System.out.printf(
                        "  actual valid   = %s%n",
                        actual.valid()
                    );
                    System.out.printf(
                        "  clone correct  = %s%n",
                        cloneCorrect
                    );
                }
            } catch (Exception ex) {
                failed++;
                System.out.printf(
                    "✗ %s (%s)%n",
                    test.id,
                    test.description
                );
                System.out.printf(
                    "  exception = %s%n",
                    ex.getMessage()
                );
            }
        }

        System.out.printf(
            "%nResults: %d passed, %d failed, %d total%n%n",
            passed,
            failed,
            tests.size()
        );
    }

    public static void main(String[] args) {
        List<TestCase> tests = new ArrayList<>();

        /*
         *              1
         *            /   \
         *           2     3
         *          / \
         *         4   5
         */
        Node basicTree = node(
            1,
            node(
                2,
                node(4, null, null, null),
                node(5, null, null, null),
                null
            ),
            node(3, null, null, null),
            null
        );

        /*
         * Set random pointers after all nodes exist.
         */
        Node node2 = basicTree.left;
        Node node3 = basicTree.right;
        Node node4 = node2.left;
        Node node5 = node2.right;

        basicTree.random = node5;
        node2.random = node3;
        node3.random = basicTree;
        node4.random = node4;
        node5.random = null;

        tests.add(new TestCase(
            "S1",
            basicTree,
            true,
            "Cross-tree, self, and null random pointers"
        ));

        /*
         * Single node pointing to itself.
         */
        Node selfRandom = node(10);
        selfRandom.random = selfRandom;

        tests.add(new TestCase(
            "S2",
            selfRandom,
            true,
            "Single node with self-random pointer"
        ));

        /*
         * All random pointers are null.
         */
        Node nullRandoms = node(
            1,
            node(2),
            node(3),
            null
        );

        tests.add(new TestCase(
            "S3",
            nullRandoms,
            true,
            "All random pointers are null"
        ));

        /*
         * Random pointers point to tree ancestors and descendants.
         */
        Node mixed = node(
            10,
            node(
                5,
                node(2),
                node(7),
                null
            ),
            node(
                15,
                null,
                node(20),
                null
            ),
            null
        );

        mixed.random = mixed.left.left;
        mixed.left.random = mixed.right.right;
        mixed.left.left.random = mixed;
        mixed.right.random = mixed.left;
        mixed.right.right.random = mixed.right;

        tests.add(new TestCase(
            "S4",
            mixed,
            true,
            "Ancestor and descendant random pointers"
        ));

        /*
         * Null input.
         */
        tests.add(new TestCase(
            "E1",
            null,
            false,
            "Null tree"
        ));

        /*
         * Duplicate values must still clone by identity.
         */
        Node duplicateValues = node(
            7,
            node(7),
            node(7),
            null
        );

        duplicateValues.random = duplicateValues.left;
        duplicateValues.left.random = duplicateValues.right;
        duplicateValues.right.random = duplicateValues;

        tests.add(new TestCase(
            "D1",
            duplicateValues,
            true,
            "Duplicate values with identity-based pointers"
        ));

        System.out.println("############################################################");
        System.out.println("######## CLONE BINARY TREE RANDOM POINTERS #################");
        System.out.println("############################################################");
        System.out.println();

        runTests(
            "HashMap Two-Pass Clone O(n)",
            test ->
                cloneBinaryTreeRandomPointerHashMap(test.root),
            tests
        );
    }
}
