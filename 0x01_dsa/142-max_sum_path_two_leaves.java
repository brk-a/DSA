import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class MaxSumPathTwoLeaves {

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

    static Node node(int val) {
        return new Node(val);
    }

    static Node node(Node left, Node right, int val) {
        return new Node(left, right, val);
    }

    static record Result(int maxSum, boolean valid) {}

    static boolean validNode(Node node) {
        return node != null;
    }

    /**
     * Brute-force solution.
     *
     * Finds all leaves, then evaluates every pair of leaves.
     *
     * Time: O(L^2 * H)
     * Space: O(L + H)
     */
    static Result maxSumPathTwoLeavesBruteForce(Node root) {
        if (!validNode(root)) {
            return new Result(0, false);
        }

        List<Node> leaves = new ArrayList<>();
        collectLeaves(root, leaves);

        if (leaves.size() < 2) {
            return new Result(0, false);
        }

        int best = Integer.MIN_VALUE;

        for (int i = 0; i < leaves.size(); i++) {
            for (int j = i + 1; j < leaves.size(); j++) {
                PathResult path = pathBetweenNodes(root, leaves.get(i), leaves.get(j));
                if (path.valid) {
                    best = Math.max(best, path.sum);
                }
            }
        }

        return new Result(best, true);
    }

    /**
     * Postorder DFS solution.
     *
     * Returns the maximum sum from the current node down to one leaf.
     * When both children exist, their best root-to-leaf paths form
     * a valid leaf-to-leaf path through the current node.
     *
     * Time: O(n)
     * Space: O(h)
     */
    static Result maxSumPathTwoLeavesPostOrderDFS(Node root) {
        if (!validNode(root)) {
            return new Result(0, false);
        }

        DfsResult result = postOrder(root);
        return new Result(result.maxLeafToLeafSum, result.hasLeafToLeafPath);
    }

    static class DfsResult {
        final int maxRootToLeafSum;
        final int maxLeafToLeafSum;
        final boolean hasLeafToLeafPath;

        DfsResult(
            int maxRootToLeafSum,
            int maxLeafToLeafSum,
            boolean hasLeafToLeafPath
        ) {
            this.maxRootToLeafSum = maxRootToLeafSum;
            this.maxLeafToLeafSum = maxLeafToLeafSum;
            this.hasLeafToLeafPath = hasLeafToLeafPath;
        }
    }

    static DfsResult postOrder(Node root) {
        if (root.left == null && root.right == null) {
            return new DfsResult(
                root.val,
                Integer.MIN_VALUE,
                false
            );
        }

        if (root.left == null) {
            DfsResult right = postOrder(root.right);

            return new DfsResult(
                root.val + right.maxRootToLeafSum,
                right.maxLeafToLeafSum,
                right.hasLeafToLeafPath
            );
        }

        if (root.right == null) {
            DfsResult left = postOrder(root.left);

            return new DfsResult(
                root.val + left.maxRootToLeafSum,
                left.maxLeafToLeafSum,
                left.hasLeafToLeafPath
            );
        }

        DfsResult left = postOrder(root.left);
        DfsResult right = postOrder(root.right);

        int throughRoot =
            left.maxRootToLeafSum
            + root.val
            + right.maxRootToLeafSum;

        int bestLeafToLeaf =
            Math.max(
                throughRoot,
                Math.max(
                    left.maxLeafToLeafSum,
                    right.maxLeafToLeafSum
                )
            );

        return new DfsResult(
            root.val + Math.max(
                left.maxRootToLeafSum,
                right.maxRootToLeafSum
            ),
            bestLeafToLeaf,
            true
        );
    }

    static void collectLeaves(Node root, List<Node> leaves) {
        if (root == null) {
            return;
        }

        if (root.left == null && root.right == null) {
            leaves.add(root);
            return;
        }

        collectLeaves(root.left, leaves);
        collectLeaves(root.right, leaves);
    }

    static class PathResult {
        final int sum;
        final boolean valid;

        PathResult(int sum, boolean valid) {
            this.sum = sum;
            this.valid = valid;
        }
    }

    static PathResult pathBetweenNodes(Node root, Node first, Node second) {
        List<Node> pathToFirst = new ArrayList<>();
        List<Node> pathToSecond = new ArrayList<>();

        if (!findPath(root, first, pathToFirst)
            || !findPath(root, second, pathToSecond)) {
            return new PathResult(0, false);
        }

        int common = 0;

        while (
            common < pathToFirst.size()
            && common < pathToSecond.size()
            && pathToFirst.get(common) == pathToSecond.get(common)
        ) {
            common++;
        }

        int sum = 0;

        for (int i = common - 1; i < pathToFirst.size(); i++) {
            sum += pathToFirst.get(i).val;
        }

        for (int i = common; i < pathToSecond.size(); i++) {
            sum += pathToSecond.get(i).val;
        }

        return new PathResult(sum, true);
    }

    static boolean findPath(Node root, Node target, List<Node> path) {
        if (root == null) {
            return false;
        }

        path.add(root);

        if (root == target) {
            return true;
        }

        if (findPath(root.left, target, path)
            || findPath(root.right, target, path)) {
            return true;
        }

        path.remove(path.size() - 1);
        return false;
    }

    static String treeDescription(Node root) {
        return root == null ? "null" : "tree";
    }

    static class TestCase {
        final String id;
        final Node root;
        final int expected;
        final boolean expectedValid;
        final String description;

        TestCase(
            String id,
            Node root,
            int expected,
            boolean expectedValid,
            String description
        ) {
            this.id = id;
            this.root = root;
            this.expected = expected;
            this.expectedValid = expectedValid;
            this.description = description;
        }
    }

    static void runTests(
        String algorithm,
        Function<Node, Result> method,
        List<TestCase> tests
    ) {
        System.out.println("==================================================");
        System.out.println(algorithm);
        System.out.println("==================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {
            try {
                Result actual = method.apply(test.root);

                if (
                    actual.maxSum() == test.expected
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
                        "  expected = Result(%d, %s)%n",
                        test.expected,
                        test.expectedValid
                    );
                    System.out.printf(
                        "  actual   = Result(%d, %s)%n",
                        actual.maxSum(),
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
        List<TestCase> tests = new ArrayList<>();

        /*
         * Standard examples
         */
        tests.add(new TestCase(
            "S1",
            node(
                node(4),
                node(5),
                1
            ),
            10,
            true,
            "Simple root with two leaves"
        ));

        tests.add(new TestCase(
            "S2",
            node(
                node(
                    node(1),
                    node(2),
                    20
                ),
                node(7),
                10
            ),
            40,
            true,
            "Best path uses both sides of the root"
        ));

        tests.add(new TestCase(
            "S3",
            node(
                node(
                    node(15),
                    node(7),
                    20
                ),
                node(9),
                -10
            ),
            42,
            true,
            "Best leaf-to-leaf path is inside a subtree"
        ));

        /*
         * Null and insufficient leaves
         */
        tests.add(new TestCase(
            "E1",
            null,
            0,
            false,
            "Null tree"
        ));

        tests.add(new TestCase(
            "E2",
            node(5),
            0,
            false,
            "Single-node tree"
        ));

        tests.add(new TestCase(
            "E3",
            node(node(5), null, 10),
            0,
            false,
            "Only one leaf"
        ));

        /*
         * Negative values
         */
        tests.add(new TestCase(
            "N1",
            node(
                node(-2),
                node(-3),
                -1
            ),
            -6,
            true,
            "All negative values"
        ));

        tests.add(new TestCase(
            "N2",
            node(
                node(
                    node(-10),
                    node(-20),
                    -5
                ),
                node(-1),
                -3
            ),
            -16,
            true,
            "Negative subtree path"
        ));

        /*
         * More coverage
         */
        tests.add(new TestCase(
            "C1",
            node(
                node(
                    node(1),
                    node(2),
                    3
                ),
                node(
                    node(4),
                    node(5),
                    6
                ),
                7
            ),
            24,
            true,
            "Balanced tree"
        ));

        tests.add(new TestCase(
            "C2",
            node(
                node(
                    node(10),
                    node(20),
                    5
                ),
                node(
                    node(30),
                    node(40),
                    6
                ),
                1
            ),
            77,
            true,
            "Large positive branch values"
        ));

        System.out.println("############################################################");
        System.out.println("############ MAX SUM PATH BETWEEN TWO LEAVES ###############");
        System.out.println("############################################################");
        System.out.println();

        runTests(
            "Brute Force",
            test -> maxSumPathTwoLeavesBruteForce(test.root),
            tests
        );

        runTests(
            "Postorder DFS O(n)",
            test -> maxSumPathTwoLeavesPostOrderDFS(test.root),
            tests
        );
    }
}
