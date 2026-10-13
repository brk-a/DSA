import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class NumberOfTurnsBinaryTree {

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

    static record Result(int turns, boolean valid) {}

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
     * Brute-force path solution.
     *
     * Finds root-to-target paths, removes their common prefix,
     * constructs the path between targets, and counts direction changes.
     *
     * Time: O(n)
     * Space: O(h)
     */
    static Result numberOfTurnsBinaryTreeBruteForce(
        Node root,
        int start,
        int target
    ) {
        if (!validNode(root)) {
            return new Result(-1, false);
        }

        List<Node> pathStart = new ArrayList<>();
        List<Node> pathTarget = new ArrayList<>();

        boolean foundStart = findPath(root, start, pathStart);
        boolean foundTarget = findPath(root, target, pathTarget);

        if (!foundStart || !foundTarget) {
            return new Result(-1, false);
        }

        if (start == target) {
            return new Result(-1, true);
        }

        int commonPrefix = 0;

        while (
            commonPrefix < pathStart.size()
            && commonPrefix < pathTarget.size()
            && pathStart.get(commonPrefix) == pathTarget.get(commonPrefix)
        ) {
            commonPrefix++;
        }

        List<Node> path = new ArrayList<>();

        /*
         * Move upward from start to the lowest common ancestor.
         */
        for (int i = pathStart.size() - 1; i >= commonPrefix - 1; i--) {
            path.add(pathStart.get(i));
        }

        /*
         * Move downward from the LCA to target.
         */
        for (int i = commonPrefix; i < pathTarget.size(); i++) {
            path.add(pathTarget.get(i));
        }

        int turns = countTurns(path);

        return new Result(
            turns == 0 ? -1 : turns,
            true
        );
    }

    /**
     * LCA/path-direction solution.
     *
     * Time: O(n)
     * Space: O(h)
     */
    static Result numberOfTurnsBinaryTreeLCA(
        Node root,
        int start,
        int target
    ) {
        if (!validNode(root)) {
            return new Result(-1, false);
        }

        if (start == target) {
            return containsValue(root, start)
                ? new Result(-1, true)
                : new Result(-1, false);
        }

        Node lca = findLCA(root, start, target);

        if (lca == null) {
            return new Result(-1, false);
        }

        StringBuilder pathStart = new StringBuilder();
        StringBuilder pathTarget = new StringBuilder();

        boolean foundStart = findDirectionPath(lca, start, pathStart);
        boolean foundTarget = findDirectionPath(lca, target, pathTarget);

        if (!foundStart || !foundTarget) {
            return new Result(-1, false);
        }

        /*
         * If the LCA is one of the targets, there is only one
         * downward path to count.
         */
        if (lca.val == start || lca.val == target) {
            String path =
                lca.val == start
                    ? pathTarget.toString()
                    : pathStart.toString();

            int turns = countTurns(path);

            return new Result(
                turns == 0 ? -1 : turns,
                true
            );
        }

        /*
         * The path goes upward to the LCA and then downward.
         * The direction changes at the LCA, so add one turn.
         */
        int turns =
            countTurns(pathStart.toString())
            + countTurns(pathTarget.toString())
            + 1;

        return new Result(
            turns == 0 ? -1 : turns,
            true
        );
    }

    static boolean findPath(
        Node root,
        int target,
        List<Node> path
    ) {
        if (root == null) {
            return false;
        }

        path.add(root);

        if (root.val == target) {
            return true;
        }

        if (findPath(root.left, target, path)
            || findPath(root.right, target, path)) {
            return true;
        }

        path.remove(path.size() - 1);
        return false;
    }

    static Node findLCA(Node root, int start, int target) {
        if (root == null) {
            return null;
        }

        if (root.val == start || root.val == target) {
            return root;
        }

        Node left = findLCA(root.left, start, target);
        Node right = findLCA(root.right, start, target);

        if (left != null && right != null) {
            return root;
        }

        return left != null ? left : right;
    }

    static boolean findDirectionPath(
        Node root,
        int target,
        StringBuilder path
    ) {
        if (root == null) {
            return false;
        }

        if (root.val == target) {
            return true;
        }

        path.append('L');
        if (findDirectionPath(root.left, target, path)) {
            return true;
        }
        path.deleteCharAt(path.length() - 1);

        path.append('R');
        if (findDirectionPath(root.right, target, path)) {
            return true;
        }
        path.deleteCharAt(path.length() - 1);

        return false;
    }

    static int countTurns(List<Node> path) {
        if (path.size() < 3) {
            return 0;
        }

        int turns = 0;
        int previousDirection = direction(path.get(0), path.get(1));

        for (int i = 1; i < path.size() - 1; i++) {
            int currentDirection =
                direction(path.get(i), path.get(i + 1));

            if (currentDirection != previousDirection) {
                turns++;
            }

            previousDirection = currentDirection;
        }

        return turns;
    }

    static int countTurns(String path) {
        int turns = 0;

        for (int i = 1; i < path.length(); i++) {
            if (path.charAt(i) != path.charAt(i - 1)) {
                turns++;
            }
        }

        return turns;
    }

    static int direction(Node parent, Node child) {
        if (parent.left == child) {
            return 0;
        }

        if (parent.right == child) {
            return 1;
        }

        throw new IllegalArgumentException(
            "Nodes are not directly connected"
        );
    }

    static boolean containsValue(Node root, int target) {
        if (root == null) {
            return false;
        }

        return root.val == target
            || containsValue(root.left, target)
            || containsValue(root.right, target);
    }

    static class TestCase {
        final String id;
        final Node root;
        final int start;
        final int target;
        final int expectedTurns;
        final boolean expectedValid;
        final String description;

        TestCase(
            String id,
            Node root,
            int start,
            int target,
            int expectedTurns,
            boolean expectedValid,
            String description
        ) {
            this.id = id;
            this.root = root;
            this.start = start;
            this.target = target;
            this.expectedTurns = expectedTurns;
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

                if (
                    actual.turns() == test.expectedTurns
                    && actual.valid() == test.expectedValid
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
                        "  start/target = %d -> %d%n",
                        test.start,
                        test.target
                    );
                    System.out.printf(
                        "  expected     = Result(%d, %s)%n",
                        test.expectedTurns,
                        test.expectedValid
                    );
                    System.out.printf(
                        "  actual       = Result(%d, %s)%n",
                        actual.turns(),
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
         *          / \     \
         *         4   5     6
         */
        Node unevenTree = node(
            1,
            node(
                2,
                node(4),
                node(5)
            ),
            node(
                3,
                null,
                node(6)
            )
        );

        List<TestCase> tests = new ArrayList<>();

        /*
         * Standard cases
         */
        tests.add(new TestCase(
            "S1",
            completeTree,
            4,
            5,
            1,
            true,
            "Left-to-right path through LCA"
        ));

        tests.add(new TestCase(
            "S2",
            completeTree,
            4,
            6,
            2,
            true,
            "Path L, up, R, L"
        ));

        tests.add(new TestCase(
            "S3",
            completeTree,
            4,
            7,
            2,
            true,
            "Path L, up, R, R"
        ));

        tests.add(new TestCase(
            "S4",
            completeTree,
            4,
            2,
            -1,
            true,
            "Ancestor path with no turns"
        ));

        tests.add(new TestCase(
            "S5",
            completeTree,
            1,
            7,
            1,
            true,
            "Root to descendant"
        ));

        /*
         * Same node
         */
        tests.add(new TestCase(
            "E1",
            completeTree,
            4,
            4,
            -1,
            true,
            "Same start and target"
        ));

        /*
         * Missing values
         */
        tests.add(new TestCase(
            "E2",
            completeTree,
            4,
            99,
            -1,
            false,
            "Target does not exist"
        ));

        tests.add(new TestCase(
            "E3",
            null,
            1,
            2,
            -1,
            false,
            "Null tree"
        ));

        /*
         * Straight paths
         */
        tests.add(new TestCase(
            "P1",
            unevenTree,
            4,
            2,
            -1,
            true,
            "Child to ancestor"
        ));

        tests.add(new TestCase(
            "P2",
            unevenTree,
            2,
            5,
            1,
            true,
            "Left then right"
        ));

        tests.add(new TestCase(
            "P3",
            unevenTree,
            3,
            6,
            -1,
            true,
            "Straight right path"
        ));

        /*
         * Duplicate-value note:
         * This implementation assumes values identify unique nodes.
         */
        tests.add(new TestCase(
            "D1",
            node(
                node(2),
                node(3),
                1
            ),
            2,
            3,
            1,
            true,
            "Simple two-child tree"
        ));

        System.out.println("############################################################");
        System.out.println("############ NUMBER OF TURNS BINARY TREE ###################");
        System.out.println("############################################################");
        System.out.println();

        runTests(
            "Brute Force Path O(n)",
            test ->
                numberOfTurnsBinaryTreeBruteForce(
                    test.root,
                    test.start,
                    test.target
                ),
            tests
        );

        runTests(
            "LCA Direction Paths O(n)",
            test ->
                numberOfTurnsBinaryTreeLCA(
                    test.root,
                    test.start,
                    test.target
                ),
            tests
        );
    }
}
