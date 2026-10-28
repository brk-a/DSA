import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class BurnBinaryTree {

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

    static record Result(
        ArrayList<ArrayList<Integer>> burnSequence,
        boolean valid
    ) {}

    static record ParentInfo(
        Map<Node, Node> parents,
        Node targetNode
    ) {}

    static boolean validNode(Node root) {
        return root != null;
    }

    static Node node(int value) {
        return new Node(value);
    }

    static Node node(int value, Node left, Node right) {
        return new Node(left, right, value);
    }

    /**
     * Recursive distance-based solution.
     *
     * First builds parent links and identifies the target.
     * Then recursively assigns each node to the burn level
     * corresponding to its distance from the target.
     *
     * Time: O(n)
     * Space: O(n)
     */
    static Result burnBinaryTreeRecursion(Node root, Node target) {
        if (!validNode(root) || !validNode(target)) {
            return new Result(new ArrayList<>(), false);
        }

        ParentInfo parentInfo = buildParentMap(root, target);

        if (parentInfo.targetNode() == null) {
            return new Result(new ArrayList<>(), false);
        }

        ArrayList<ArrayList<Integer>> burnSequence =
            new ArrayList<>();

        Set<Node> visited = new HashSet<>();

        burnRecursively(
            parentInfo.targetNode(),
            null,
            0,
            parentInfo.parents(),
            visited,
            burnSequence
        );

        return new Result(burnSequence, true);
    }

    static void burnRecursively(
        Node current,
        Node previous,
        int time,
        Map<Node, Node> parents,
        Set<Node> visited,
        ArrayList<ArrayList<Integer>> burnSequence
    ) {
        if (
            current == null
            || current == previous
            || visited.contains(current)
        ) {
            return;
        }

        visited.add(current);

        while (burnSequence.size() <= time) {
            burnSequence.add(new ArrayList<>());
        }

        burnSequence.get(time).add(current.val);

        burnRecursively(
            current.left,
            current,
            time + 1,
            parents,
            visited,
            burnSequence
        );

        burnRecursively(
            current.right,
            current,
            time + 1,
            parents,
            visited,
            burnSequence
        );

        burnRecursively(
            parents.get(current),
            current,
            time + 1,
            parents,
            visited,
            burnSequence
        );
    }

    /**
     * Undirected-graph BFS solution.
     *
     * Parent mapping turns the tree into an undirected graph.
     * Each BFS level represents one second of burning.
     *
     * Time: O(n)
     * Space: O(n)
     */
    static Result burnBinaryTreeLevelOrder(Node root, Node target) {
        if (!validNode(root) || !validNode(target)) {
            return new Result(new ArrayList<>(), false);
        }

        ParentInfo parentInfo = buildParentMap(root, target);

        if (parentInfo.targetNode() == null) {
            return new Result(new ArrayList<>(), false);
        }

        ArrayList<ArrayList<Integer>> burnSequence =
            new ArrayList<>();

        Queue<Node> queue = new ArrayDeque<>();
        Set<Node> visited = new HashSet<>();

        queue.offer(parentInfo.targetNode());
        visited.add(parentInfo.targetNode());

        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            ArrayList<Integer> currentLevel = new ArrayList<>();

            for (int i = 0; i < levelSize; i++) {
                Node current = queue.poll();
                currentLevel.add(current.val);

                addIfUnvisited(
                    current.left,
                    visited,
                    queue
                );

                addIfUnvisited(
                    current.right,
                    visited,
                    queue
                );

                addIfUnvisited(
                    parentInfo.parents().get(current),
                    visited,
                    queue
                );
            }

            burnSequence.add(currentLevel);
        }

        return new Result(burnSequence, true);
    }

    static ParentInfo buildParentMap(Node root, Node target) {
        Map<Node, Node> parents = new HashMap<>();
        Node targetNode = null;

        Queue<Node> queue = new ArrayDeque<>();
        queue.offer(root);
        parents.put(root, null);

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            if (current == target) {
                targetNode = current;
            }

            if (current.left != null) {
                parents.put(current.left, current);
                queue.offer(current.left);
            }

            if (current.right != null) {
                parents.put(current.right, current);
                queue.offer(current.right);
            }
        }

        return new ParentInfo(parents, targetNode);
    }

    static void addIfUnvisited(
        Node node,
        Set<Node> visited,
        Queue<Node> queue
    ) {
        if (node != null && visited.add(node)) {
            queue.offer(node);
        }
    }

    static ArrayList<ArrayList<Integer>> sequence(
        int[]... levels
    ) {
        ArrayList<ArrayList<Integer>> result =
            new ArrayList<>();

        for (int[] level : levels) {
            ArrayList<Integer> current =
                new ArrayList<>();

            for (int value : level) {
                current.add(value);
            }

            result.add(current);
        }

        return result;
    }

    static class TestCase {
        final String id;
        final Node root;
        final Node target;
        final ArrayList<ArrayList<Integer>> expected;
        final boolean expectedValid;
        final String description;

        TestCase(
            String id,
            Node root,
            Node target,
            ArrayList<ArrayList<Integer>> expected,
            boolean expectedValid,
            String description
        ) {
            this.id = id;
            this.root = root;
            this.target = target;
            this.expected = expected;
            this.expectedValid = expectedValid;
            this.description = description;
        }
    }

    static void runTests(
        String algorithm,
        java.util.function.Function<TestCase, Result> method,
        java.util.List<TestCase> tests
    ) {
        System.out.println("==================================================");
        System.out.println(algorithm);
        System.out.println("==================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {
            try {
                Result actual = method.apply(test);

                if (
                    actual.valid() == test.expectedValid
                    && actual.burnSequence().equals(test.expected)
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
                        "  expected = %s, valid=%s%n",
                        test.expected,
                        test.expectedValid
                    );
                    System.out.printf(
                        "  actual   = %s, valid=%s%n",
                        actual.burnSequence(),
                        actual.valid()
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
        /*
         *              1
         *            /   \
         *           2     3
         *          / \   / \
         *         4   5 6   7
         */
        Node completeTree = node(
            1,
            node(
                2,
                node(4),
                node(5)
            ),
            node(
                3,
                node(6),
                node(7)
            )
        );

        /*
         *              1
         *            /   \
         *           2     3
         *          /       \
         *         4         5
         */
        Node unevenTree = node(
            1,
            node(
                2,
                node(4),
                null
            ),
            node(
                3,
                null,
                node(5)
            )
        );

        java.util.List<TestCase> tests = new ArrayList<>();

        tests.add(new TestCase(
            "S1",
            completeTree,
            completeTree.left,
            sequence(
                new int[]{2},
                new int[]{4, 5, 1},
                new int[]{3},
                new int[]{6, 7}
            ),
            true,
            "Burn from an internal node"
        ));

        tests.add(new TestCase(
            "S2",
            completeTree,
            completeTree.left.left,
            sequence(
                new int[]{4},
                new int[]{2},
                new int[]{1, 5},
                new int[]{3},
                new int[]{6, 7}
            ),
            true,
            "Burn from a leaf"
        ));

        tests.add(new TestCase(
            "S3",
            completeTree,
            completeTree,
            sequence(
                new int[]{1},
                new int[]{2, 3},
                new int[]{4, 5, 6, 7}
            ),
            true,
            "Burn from the root"
        ));

        tests.add(new TestCase(
            "S4",
            completeTree,
            completeTree.right.right,
            sequence(
                new int[]{7},
                new int[]{3},
                new int[]{1, 6},
                new int[]{2},
                new int[]{4, 5}
            ),
            true,
            "Burn from the opposite leaf"
        ));

        tests.add(new TestCase(
            "S5",
            unevenTree,
            unevenTree.left.left,
            sequence(
                new int[]{4},
                new int[]{2},
                new int[]{1},
                new int[]{3},
                new int[]{5}
            ),
            true,
            "Burn through an uneven tree"
        ));

        tests.add(new TestCase(
            "E1",
            null,
            null,
            new ArrayList<>(),
            false,
            "Null root and target"
        ));

        tests.add(new TestCase(
            "E2",
            completeTree,
            null,
            new ArrayList<>(),
            false,
            "Null target"
        ));

        tests.add(new TestCase(
            "E3",
            completeTree,
            node(99),
            new ArrayList<>(),
            false,
            "Target is not part of the tree"
        ));

        /*
         * Same value, different object:
         * This should be invalid because target identity is used.
         */
        tests.add(new TestCase(
            "E4",
            node(
                1,
                node(2),
                node(3)
            ),
            node(2),
            new ArrayList<>(),
            false,
            "Equal value but unrelated target object"
        ));

        runTests(
            "Recursive Distance Traversal",
            test ->
                burnBinaryTreeRecursion(
                    test.root,
                    test.target
                ),
            tests
        );

        runTests(
            "Level-Order Graph BFS",
            test ->
                burnBinaryTreeLevelOrder(
                    test.root,
                    test.target
                ),
            tests
        );
    }
}
