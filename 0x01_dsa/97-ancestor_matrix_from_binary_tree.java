import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class AncestorMatrixFromBinaryTree {

    static record Result(List<List<Integer>> matrix, boolean valid) {}

    static class Node {
        int val;
        Node left;
        Node right;

        Node(int val) {
            this.val = val;
        }
    }

    static boolean validTree(Node root) {
        if (root == null) {
            return false;
        }

        List<Integer> values = new ArrayList<>();
        collectValues(root, values);

        int n = values.size();
        Set<Integer> seen = new HashSet<>();

        for (int value : values) {
            if (value < 0 || value >= n || !seen.add(value)) {
                return false;
            }
        }

        return true;
    }

    static void collectValues(Node root, List<Integer> values) {
        if (root == null) {
            return;
        }

        values.add(root.val);
        collectValues(root.left, values);
        collectValues(root.right, values);
    }

    static int countNodes(Node root) {
        if (root == null) {
            return 0;
        }

        return 1 + countNodes(root.left) + countNodes(root.right);
    }

    static List<List<Integer>> createMatrix(int n) {
        List<List<Integer>> matrix = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            List<Integer> row = new ArrayList<>();

            for (int j = 0; j < n; j++) {
                row.add(0);
            }

            matrix.add(row);
        }

        return matrix;
    }

    static Result ancestorMatrixFromBinaryTreePreOrder(Node root) {
        if (!validTree(root)) {
            return new Result(new ArrayList<>(), false);
        }

        int n = countNodes(root);
        List<List<Integer>> matrix = createMatrix(n);
        List<Integer> ancestors = new ArrayList<>();

        ancestorMatrixRecursionPreOrder(
                root,
                matrix,
                ancestors);

        return new Result(matrix, true);
    }

    static void ancestorMatrixRecursionPreOrder(
            Node root,
            List<List<Integer>> matrix,
            List<Integer> ancestors) {

        if (root == null) {
            return;
        }

        int current = root.val;

        for (int ancestor : ancestors) {
            matrix.get(ancestor).set(current, 1);
        }

        ancestors.add(current);

        ancestorMatrixRecursionPreOrder(
                root.left,
                matrix,
                ancestors);

        ancestorMatrixRecursionPreOrder(
                root.right,
                matrix,
                ancestors);

        ancestors.remove(ancestors.size() - 1);
    }

    static Result ancestorMatrixFromBinaryTreePostOrder(Node root) {
        if (!validTree(root)) {
            return new Result(new ArrayList<>(), false);
        }

        int n = countNodes(root);
        List<List<Integer>> matrix = createMatrix(n);

        ancestorMatrixRecursionPostOrder(
                root,
                matrix);

        return new Result(matrix, true);
    }

    static void ancestorMatrixRecursionPostOrder(
            Node root,
            List<List<Integer>> matrix) {

        if (root == null) {
            return;
        }

        ancestorMatrixRecursionPostOrder(
                root.left,
                matrix);

        ancestorMatrixRecursionPostOrder(
                root.right,
                matrix);

        int current = root.val;

        if (root.left != null) {
            int left = root.left.val;

            matrix.get(current).set(left, 1);

            for (int i = 0; i < matrix.size(); i++) {
                if (matrix.get(left).get(i) == 1) {
                    matrix.get(current).set(i, 1);
                }
            }
        }

        if (root.right != null) {
            int right = root.right.val;

            matrix.get(current).set(right, 1);

            for (int i = 0; i < matrix.size(); i++) {
                if (matrix.get(right).get(i) == 1) {
                    matrix.get(current).set(i, 1);
                }
            }
        }
    }

    static class TestCase {
        final String id;
        final Node input;
        final Result expected;
        final String description;

        TestCase(
                String id,
                Node input,
                Result expected,
                String description) {

            this.id = id;
            this.input = input;
            this.expected = expected;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface Algorithm {
        Result solve(Node root);
    }

    static class MethodCase {
        final String name;
        final Algorithm algorithm;

        MethodCase(String name, Algorithm algorithm) {
            this.name = name;
            this.algorithm = algorithm;
        }
    }

    static Result matrixResult(int[][] matrix) {
        List<List<Integer>> result = new ArrayList<>();

        for (int[] row : matrix) {
            List<Integer> convertedRow = new ArrayList<>();

            for (int value : row) {
                convertedRow.add(value);
            }

            result.add(convertedRow);
        }

        return new Result(result, true);
    }

    static boolean matricesEqual(
            List<List<Integer>> first,
            List<List<Integer>> second) {

        if (first == null || second == null) {
            return first == second;
        }

        if (first.size() != second.size()) {
            return false;
        }

        for (int i = 0; i < first.size(); i++) {
            if (!first.get(i).equals(second.get(i))) {
                return false;
            }
        }

        return true;
    }

    static boolean resultsEqual(Result first, Result second) {
        if (first == null || second == null) {
            return first == second;
        }

        return first.valid() == second.valid()
                && matricesEqual(
                        first.matrix(),
                        second.matrix());
    }

    static String formatMatrix(List<List<Integer>> matrix) {
        if (matrix == null) {
            return "null";
        }

        return matrix.toString();
    }

    static void runTests(
            String algorithmName,
            Algorithm algorithm,
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
                Result actual = algorithm.solve(test.input);

                if (resultsEqual(actual, test.expected)) {
                    passed++;

                    System.out.printf(
                            "✓ %s (%s)%n",
                            test.id,
                            test.description);

                } else {
                    failed++;

                    System.out.printf(
                            "✗ %s (%s)%n",
                            test.id,
                            test.description);

                    System.out.println(
                            "  expected = "
                                    + test.expected);

                    System.out.println(
                            "  actual   = "
                                    + actual);
                }

            } catch (Exception exception) {
                failed++;

                System.out.printf(
                        "✗ %s (%s)%n",
                        test.id,
                        test.description);

                System.out.println(
                        "  exception = "
                                + exception);
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

    static Node createBasicTree() {
        Node root = new Node(0);

        root.left = new Node(1);
        root.right = new Node(2);
        root.left.left = new Node(3);
        root.left.right = new Node(4);

        return root;
    }

    static Node createLeftSkewedTree(int n) {
        if (n <= 0) {
            return null;
        }

        Node root = new Node(0);
        Node current = root;

        for (int i = 1; i < n; i++) {
            current.left = new Node(i);
            current = current.left;
        }

        return root;
    }

    static Node createRightSkewedTree(int n) {
        if (n <= 0) {
            return null;
        }

        Node root = new Node(0);
        Node current = root;

        for (int i = 1; i < n; i++) {
            current.right = new Node(i);
            current = current.right;
        }

        return root;
    }

    static Node createCompleteTree(int n) {
        if (n <= 0) {
            return null;
        }

        Node[] nodes = new Node[n];

        for (int i = 0; i < n; i++) {
            nodes[i] = new Node(i);
        }

        for (int i = 0; i < n; i++) {
            int left = 2 * i + 1;
            int right = 2 * i + 2;

            if (left < n) {
                nodes[i].left = nodes[left];
            }

            if (right < n) {
                nodes[i].right = nodes[right];
            }
        }

        return nodes[0];
    }

    static Node randomTree(Random random, int n) {
        if (n <= 0) {
            return null;
        }

        Node root = new Node(0);
        List<Node> availableParents = new ArrayList<>();
        availableParents.add(root);

        for (int value = 1; value < n; value++) {
            Node newNode = new Node(value);

            while (true) {
                int parentIndex =
                        random.nextInt(availableParents.size());

                Node parent =
                        availableParents.get(parentIndex);

                boolean leftAvailable =
                        parent.left == null;

                boolean rightAvailable =
                        parent.right == null;

                if (!leftAvailable && !rightAvailable) {
                    availableParents.remove(parentIndex);
                    continue;
                }

                if (leftAvailable && rightAvailable) {
                    if (random.nextBoolean()) {
                        parent.left = newNode;
                    } else {
                        parent.right = newNode;
                    }
                } else if (leftAvailable) {
                    parent.left = newNode;
                } else {
                    parent.right = newNode;
                }

                break;
            }

            availableParents.add(newNode);
        }

        return root;
    }

    static void runRandomisedTests(int iterations) {
        System.out.println(
                "======================================================");

        System.out.println(
                "Randomised Cross Checks");

        System.out.println(
                "Preorder vs Postorder");

        System.out.println(
                "======================================================");

        Random random = new Random(20260905L);

        for (int i = 1; i <= iterations; i++) {
            int n = random.nextInt(10) + 1;
            Node root = randomTree(random, n);

            Result preorder =
                    ancestorMatrixFromBinaryTreePreOrder(root);

            Result postorder =
                    ancestorMatrixFromBinaryTreePostOrder(root);

            if (!resultsEqual(preorder, postorder)) {
                System.out.println(
                        "Randomised test FAILED");

                System.out.println(
                        "iteration = " + i);

                System.out.println(
                        "nodeCount = " + n);

                System.out.println(
                        "preorder  = " + preorder);

                System.out.println(
                        "postorder = " + postorder);

                return;
            }
        }

        System.out.printf(
                "All %d Randomised tests passed.%n%n",
                iterations);
    }

    public static void main(String[] args) {

        List<TestCase> tests = new ArrayList<>();

        tests.add(new TestCase(
                "B1",
                new Node(0),
                matrixResult(new int[][]{
                        {0}
                }),
                "single-node tree"));

        tests.add(new TestCase(
                "B2",
                createBasicTree(),
                matrixResult(new int[][]{
                        {0, 1, 1, 1, 1},
                        {0, 0, 0, 1, 1},
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0}
                }),
                "basic five-node tree"));

        tests.add(new TestCase(
                "B3",
                createLeftSkewedTree(4),
                matrixResult(new int[][]{
                        {0, 1, 1, 1},
                        {0, 0, 1, 1},
                        {0, 0, 0, 1},
                        {0, 0, 0, 0}
                }),
                "left-skewed tree"));

        tests.add(new TestCase(
                "B4",
                createRightSkewedTree(4),
                matrixResult(new int[][]{
                        {0, 1, 1, 1},
                        {0, 0, 1, 1},
                        {0, 0, 0, 1},
                        {0, 0, 0, 0}
                }),
                "right-skewed tree"));

        tests.add(new TestCase(
                "B5",
                createCompleteTree(7),
                matrixResult(new int[][]{
                        {0, 1, 1, 1, 1, 1, 1},
                        {0, 0, 0, 1, 1, 0, 0},
                        {0, 0, 0, 0, 0, 1, 1},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0, 0}
                }),
                "complete binary tree"));

        tests.add(new TestCase(
                "B6",
                createCompleteTree(6),
                matrixResult(new int[][]{
                        {0, 1, 1, 1, 1, 1},
                        {0, 0, 0, 1, 1, 0},
                        {0, 0, 0, 0, 0, 1},
                        {0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0},
                        {0, 0, 0, 0, 0, 0}
                }),
                "incomplete complete tree"));

        tests.add(new TestCase(
                "B7",
                createLeftSkewedTree(6),
                matrixResult(new int[][]{
                        {0, 1, 1, 1, 1, 1},
                        {0, 0, 1, 1, 1, 1},
                        {0, 0, 0, 1, 1, 1},
                        {0, 0, 0, 0, 1, 1},
                        {0, 0, 0, 0, 0, 1},
                        {0, 0, 0, 0, 0, 0}
                }),
                "long left-skewed tree"));

        tests.add(new TestCase(
                "E1",
                null,
                new Result(
                        new ArrayList<>(),
                        false),
                "null root"));

        Node invalidRangeTree = new Node(0);
        invalidRangeTree.left = new Node(2);

        tests.add(new TestCase(
                "E2",
                invalidRangeTree,
                new Result(
                        new ArrayList<>(),
                        false),
                "value outside valid range"));

        Node duplicateValueTree = new Node(0);
        duplicateValueTree.left = new Node(1);
        duplicateValueTree.right = new Node(1);

        tests.add(new TestCase(
                "E3",
                duplicateValueTree,
                new Result(
                        new ArrayList<>(),
                        false),
                "duplicate node values"));

        Node negativeValueTree = new Node(0);
        negativeValueTree.left = new Node(-1);

        tests.add(new TestCase(
                "E4",
                negativeValueTree,
                new Result(
                        new ArrayList<>(),
                        false),
                "negative node value"));

        List<MethodCase> methods = List.of(
                new MethodCase(
                        "Preorder",
                        AncestorMatrixFromBinaryTree
                                ::ancestorMatrixFromBinaryTreePreOrder),

                new MethodCase(
                        "Postorder",
                        AncestorMatrixFromBinaryTree
                                ::ancestorMatrixFromBinaryTreePostOrder)
        );

        System.out.println(
                "############################################################");

        System.out.println(
                "########  ANCESTOR MATRIX FROM BINARY TREE  ###############");

        System.out.println(
                "############################################################");

        System.out.println();

        for (MethodCase method : methods) {
            runTests(
                    method.name,
                    method.algorithm,
                    tests);
        }

        runRandomisedTests(5000);
    }
}
