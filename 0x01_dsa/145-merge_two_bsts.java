import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;
import java.util.function.Function;

public class MergeTwoBSTs {

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
        ArrayList<Integer> mergedBST,
        boolean valid
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
     * Array-based solution.
     *
     * Time: O(n + m)
     * Space: O(n + m)
     */
    static Result mergeTwoBSTsArray(Node root1, Node root2) {
        if (!validNode(root1) && !validNode(root2)) {
            return new Result(new ArrayList<>(), false);
        }

        ArrayList<Integer> values1 = new ArrayList<>();
        ArrayList<Integer> values2 = new ArrayList<>();

        bstToArrayInOrder(root1, values1);
        bstToArrayInOrder(root2, values2);

        ArrayList<Integer> merged =
            mergeSortedLists(values1, values2);

        return new Result(merged, true);
    }

    /**
     * Stack-based solution.
     *
     * Time: O(n + m)
     * Space: O(h1 + h2)
     */
    static Result mergeTwoBSTsStack(Node root1, Node root2) {
        if (!validNode(root1) && !validNode(root2)) {
            return new Result(new ArrayList<>(), false);
        }

        ArrayList<Integer> merged = new ArrayList<>();

        Stack<Node> stack1 = new Stack<>();
        Stack<Node> stack2 = new Stack<>();

        Node current1 = root1;
        Node current2 = root2;

        while (
            current1 != null
            || current2 != null
            || !stack1.isEmpty()
            || !stack2.isEmpty()
        ) {
            while (current1 != null) {
                stack1.push(current1);
                current1 = current1.left;
            }

            while (current2 != null) {
                stack2.push(current2);
                current2 = current2.left;
            }

            if (
                stack2.isEmpty()
                || (
                    !stack1.isEmpty()
                    && stack1.peek().val <= stack2.peek().val
                )
            ) {
                Node node = stack1.pop();
                merged.add(node.val);
                current1 = node.right;
            } else {
                Node node = stack2.pop();
                merged.add(node.val);
                current2 = node.right;
            }
        }

        return new Result(merged, true);
    }

    static void bstToArrayInOrder(
        Node root,
        ArrayList<Integer> array
    ) {
        if (root == null) {
            return;
        }

        bstToArrayInOrder(root.left, array);
        array.add(root.val);
        bstToArrayInOrder(root.right, array);
    }

    static ArrayList<Integer> mergeSortedLists(
        ArrayList<Integer> first,
        ArrayList<Integer> second
    ) {
        ArrayList<Integer> result = new ArrayList<>(
            first.size() + second.size()
        );

        int i = 0;
        int j = 0;

        while (i < first.size() && j < second.size()) {
            if (first.get(i) <= second.get(j)) {
                result.add(first.get(i));
                i++;
            } else {
                result.add(second.get(j));
                j++;
            }
        }

        while (i < first.size()) {
            result.add(first.get(i));
            i++;
        }

        while (j < second.size()) {
            result.add(second.get(j));
            j++;
        }

        return result;
    }

    static class TestCase {
        final String id;
        final Node root1;
        final Node root2;
        final ArrayList<Integer> expected;
        final boolean expectedValid;
        final String description;

        TestCase(
            String id,
            Node root1,
            Node root2,
            ArrayList<Integer> expected,
            boolean expectedValid,
            String description
        ) {
            this.id = id;
            this.root1 = root1;
            this.root2 = root2;
            this.expected = expected;
            this.expectedValid = expectedValid;
            this.description = description;
        }
    }

    static ArrayList<Integer> list(Integer... values) {
        return new ArrayList<>(Arrays.asList(values));
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
                    actual.valid() == test.expectedValid
                    && actual.mergedBST().equals(test.expected)
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
                        "  expected = Result(%s, %s)%n",
                        test.expected,
                        test.expectedValid
                    );
                    System.out.printf(
                        "  actual   = Result(%s, %s)%n",
                        actual.mergedBST(),
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
         *              2
         *             / \
         *            1   4
         *               /
         *              3
         */
        Node tree1 = node(
            2,
            node(1),
            node(
                4,
                node(3),
                null
            )
        );

        /*
         *              3
         *             / \
         *            0   5
         */
        Node tree2 = node(
            3,
            node(0),
            node(5)
        );

        tests.add(new TestCase(
            "S1",
            tree1,
            tree2,
            list(0, 1, 2, 3, 3, 4, 5),
            true,
            "Two ordinary BSTs"
        ));

        tests.add(new TestCase(
            "S2",
            node(1),
            node(2),
            list(1, 2),
            true,
            "Single-node trees"
        ));

        tests.add(new TestCase(
            "S3",
            node(
                2,
                node(1),
                node(3)
            ),
            null,
            list(1, 2, 3),
            true,
            "Second tree is null"
        ));

        tests.add(new TestCase(
            "S4",
            null,
            node(
                5,
                node(4),
                node(6)
            ),
            list(4, 5, 6),
            true,
            "First tree is null"
        ));

        tests.add(new TestCase(
            "E1",
            null,
            null,
            list(),
            false,
            "Both trees are null"
        ));

        tests.add(new TestCase(
            "D1",
            node(
                2,
                node(1),
                node(2)
            ),
            node(
                2,
                node(2),
                node(3)
            ),
            list(1, 2, 2, 2, 2, 3),
            true,
            "Duplicate values are preserved"
        ));

        tests.add(new TestCase(
            "C1",
            node(
                10,
                node(
                    5,
                    node(2),
                    node(7)
                ),
                node(15)
            ),
            node(
                8,
                node(3),
                node(
                    12,
                    node(11),
                    node(20)
                )
            ),
            list(2, 3, 5, 7, 8, 10, 11, 12, 15, 20),
            true,
            "Larger trees"
        ));

        tests.add(new TestCase(
            "C2",
            node(
                1,
                null,
                node(
                    2,
                    null,
                    node(3)
                )
            ),
            node(
                6,
                node(
                    5,
                    node(4),
                    null
                ),
                null
            ),
            list(1, 2, 3, 4, 5, 6),
            true,
            "Highly skewed trees"
        ));

        System.out.println("############################################################");
        System.out.println("################## MERGE TWO BSTS ##########################");
        System.out.println("############################################################");
        System.out.println();

        runTests(
            "Array-Based Merge O(n + m)",
            test ->
                mergeTwoBSTsArray(
                    test.root1,
                    test.root2
                ),
            tests
        );

        runTests(
            "Stack-Based Merge O(n + m)",
            test ->
                mergeTwoBSTsStack(
                    test.root1,
                    test.root2
                ),
            tests
        );
    }
}
