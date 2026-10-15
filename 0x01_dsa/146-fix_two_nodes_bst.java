import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class FixTwoNodesBST {

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
        ArrayList<Integer> fixed,
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
     * Sorting-based solution.
     *
     * 1. Store the original inorder traversal.
     * 2. Sort a copy of that traversal.
     * 3. Write sorted values back into the tree inorder.
     *
     * Time: O(n log n)
     * Space: O(n)
     */
    static Result fixTwoNodesBSTInorderSorting(Node root) {
        if (!validNode(root)) {
            return new Result(new ArrayList<>(), false);
        }

        ArrayList<Integer> originalInOrder = new ArrayList<>();
        findInOrder(root, originalInOrder);

        ArrayList<Integer> sortedInOrder =
            new ArrayList<>(originalInOrder);

        Collections.sort(sortedInOrder);

        int[] index = {0};
        correctBSTUtil(root, sortedInOrder, index);

        return new Result(sortedInOrder, true);
    }

    /**
     * One-inorder-traversal solution.
     *
     * Detects inversions in the inorder sequence and swaps the
     * corresponding node values.
     *
     * Time: O(n)
     * Space: O(h)
     */
    static Result fixTwoNodesBSTOneTraversal(Node root) {
        if (!validNode(root)) {
            return new Result(new ArrayList<>(), false);
        }

        Node[] first = new Node[1];
        Node[] middle = new Node[1];
        Node[] last = new Node[1];
        Node[] previous = new Node[1];

        correctBSTUtil(
            root,
            first,
            middle,
            last,
            previous
        );

        if (first[0] != null && last[0] != null) {
            swapValues(first[0], last[0]);
        } else if (first[0] != null && middle[0] != null) {
            swapValues(first[0], middle[0]);
        }

        ArrayList<Integer> fixed = treeToArray(root);

        return new Result(fixed, true);
    }

    static void findInOrder(
        Node root,
        ArrayList<Integer> inOrder
    ) {
        if (root == null) {
            return;
        }

        findInOrder(root.left, inOrder);
        inOrder.add(root.val);
        findInOrder(root.right, inOrder);
    }

    static void correctBSTUtil(
        Node root,
        ArrayList<Integer> sortedInOrder,
        int[] index
    ) {
        if (root == null) {
            return;
        }

        correctBSTUtil(root.left, sortedInOrder, index);

        root.val = sortedInOrder.get(index[0]);
        index[0]++;

        correctBSTUtil(root.right, sortedInOrder, index);
    }

    static void correctBSTUtil(
        Node root,
        Node[] first,
        Node[] middle,
        Node[] last,
        Node[] previous
    ) {
        if (root == null) {
            return;
        }

        correctBSTUtil(
            root.left,
            first,
            middle,
            last,
            previous
        );

        if (
            previous[0] != null
            && previous[0].val > root.val
        ) {
            if (first[0] == null) {
                first[0] = previous[0];
                middle[0] = root;
            } else {
                last[0] = root;
            }
        }

        previous[0] = root;

        correctBSTUtil(
            root.right,
            first,
            middle,
            last,
            previous
        );
    }

    static ArrayList<Integer> treeToArray(Node root) {
        ArrayList<Integer> result = new ArrayList<>();
        findInOrder(root, result);
        return result;
    }

    static void swapValues(Node first, Node second) {
        int temporary = first.val;
        first.val = second.val;
        second.val = temporary;
    }

    static class TestCase {
        final String id;
        final Node root;
        final ArrayList<Integer> expected;
        final String description;

        TestCase(
            String id,
            Node root,
            ArrayList<Integer> expected,
            String description
        ) {
            this.id = id;
            this.root = root;
            this.expected = expected;
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
                    actual.valid()
                    && actual.fixed().equals(test.expected)
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
                        "  expected = %s%n",
                        test.expected
                    );
                    System.out.printf(
                        "  actual   = %s%n",
                        actual.fixed()
                    );
                    System.out.printf(
                        "  valid    = %s%n",
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
         * Valid BST:
         *
         *       3
         *      / \
         *     1   4
         *        /
         *       2
         *
         * Inorder: 1, 2, 4, 3
         * Swapped values: 2 and 3
         */
        tests.add(new TestCase(
            "S1",
            node(
                3,
                node(1),
                node(
                    4,
                    node(2),
                    null
                )
            ),
            list(1, 2, 3, 4),
            "Adjacent inversion"
        ));

        /*
         * Valid BST:
         *
         *          6
         *        /   \
         *       3     8
         *      / \   / \
         *     1   4 7   9
         *
         * Swap 3 and 8:
         * Inorder becomes 1, 8, 4, 6, 7, 3, 9
         */
        tests.add(new TestCase(
            "S2",
            node(
                6,
                node(
                    8,
                    node(1),
                    node(4)
                ),
                node(
                    3,
                    node(7),
                    node(9)
                )
            ),
            list(1, 3, 4, 6, 7, 8, 9),
            "Non-adjacent inversion"
        ));

        /*
         * Single-node tree
         */
        tests.add(new TestCase(
            "S3",
            node(5),
            list(5),
            "Single-node tree"
        ));

        /*
         * Already valid BST
         */
        tests.add(new TestCase(
            "S4",
            node(
                2,
                node(1),
                node(3)
            ),
            list(1, 2, 3),
            "Already valid BST"
        ));

        /*
         * Null tree
         */
        tests.add(new TestCase(
            "E1",
            null,
            list(),
            "Null tree"
        ));

        /*
         * Swapped root and leaf
         *
         * Original valid BST:
         *
         *       2
         *      / \
         *     1   3
         *
         * Corrupted tree:
         *
         *       3
         *      / \
         *     1   2
         */
        tests.add(new TestCase(
            "C1",
            node(
                3,
                node(1),
                node(2)
            ),
            list(1, 2, 3),
            "Root and leaf swapped"
        ));

        /*
         * Larger corrupted tree
         */
        tests.add(new TestCase(
            "C2",
            node(
                5,
                node(
                    3,
                    node(1),
                    node(4)
                ),
                node(
                    8,
                    node(6),
                    node(7)
                )
            ),
            list(1, 3, 4, 5, 6, 7, 8),
            "Larger tree with misplaced values"
        ));

        System.out.println("############################################################");
        System.out.println("################## FIX TWO-NODE BST ########################");
        System.out.println("############################################################");
        System.out.println();

        runTests(
            "Inorder Sorting O(n log n)",
            test -> fixTwoNodesBSTInorderSorting(test.root),
            tests
        );

        /*
         * The tests above mutate their trees during the first run.
         * Rebuild fresh trees for the second algorithm.
         */
        List<TestCase> oneTraversalTests = new ArrayList<>();

        oneTraversalTests.add(new TestCase(
            "S1",
            node(
                3,
                node(1),
                node(
                    4,
                    node(2),
                    null
                )
            ),
            list(1, 2, 3, 4),
            "Adjacent inversion"
        ));

        oneTraversalTests.add(new TestCase(
            "S2",
            node(
                6,
                node(
                    8,
                    node(1),
                    node(4)
                ),
                node(
                    3,
                    node(7),
                    node(9)
                )
            ),
            list(1, 3, 4, 6, 7, 8, 9),
            "Non-adjacent inversion"
        ));

        oneTraversalTests.add(new TestCase(
            "S3",
            node(5),
            list(5),
            "Single-node tree"
        ));

        oneTraversalTests.add(new TestCase(
            "S4",
            node(
                2,
                node(1),
                node(3)
            ),
            list(1, 2, 3),
            "Already valid BST"
        ));

        oneTraversalTests.add(new TestCase(
            "E1",
            null,
            list(),
            "Null tree"
        ));

        oneTraversalTests.add(new TestCase(
            "C1",
            node(
                3,
                node(1),
                node(2)
            ),
            list(1, 2, 3),
            "Root and leaf swapped"
        ));

        oneTraversalTests.add(new TestCase(
            "C2",
            node(
                5,
                node(
                    3,
                    node(1),
                    node(4)
                ),
                node(
                    8,
                    node(6),
                    node(7)
                )
            ),
            list(1, 3, 4, 5, 6, 7, 8),
            "Larger tree with misplaced values"
        ));

        runTests(
            "One Inorder Traversal O(n)",
            test -> fixTwoNodesBSTOneTraversal(test.root),
            oneTraversalTests
        );
    }
}
