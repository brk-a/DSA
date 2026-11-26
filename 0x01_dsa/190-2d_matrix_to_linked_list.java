import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class TwoDMatrixToLinkedList {

    /* **********************************************************************
     * Node and result
     * **********************************************************************/

    static class Node {
        final int val;
        Node next;

        Node(int val) {
            this.val = val;
        }

        Node(int val, Node next) {
            this.val = val;
            this.next = next;
        }
    }

    static record Result(Node head, boolean valid) {}

    /* **********************************************************************
     * Conversion methods
     * **********************************************************************/

    /**
     * A valid matrix is non-null, non-empty, rectangular, and contains no
     * null rows or elements.
     */
    static boolean validMatrix(ArrayList<ArrayList<Integer>> matrix) {
        if (matrix == null || matrix.isEmpty()) {
            return false;
        }

        ArrayList<Integer> firstRow = matrix.get(0);
        if (firstRow == null || firstRow.isEmpty()) {
            return false;
        }

        int columns = firstRow.size();

        for (ArrayList<Integer> row : matrix) {
            if (row == null || row.size() != columns) {
                return false;
            }

            for (Integer value : row) {
                if (value == null) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * Converts the matrix to a singly linked list in row-major order.
     * Time: O(rows * columns); call-stack space: O(rows * columns).
     */
    static Result matrixToLinkedListRecursive(
        ArrayList<ArrayList<Integer>> matrix
    ) {
        if (!validMatrix(matrix)) {
            return new Result(null, false);
        }

        Node dummy = new Node(0);
        Node tail = dummy;
        int columns = matrix.get(0).size();

        for (int row = 0; row < matrix.size(); row++) {
            tail = appendRowRecursively(matrix, row, 0, columns, tail);
        }

        return new Result(dummy.next, true);
    }

    private static Node appendRowRecursively(
        ArrayList<ArrayList<Integer>> matrix,
        int row,
        int column,
        int columns,
        Node tail
    ) {
        if (column == columns) {
            return tail;
        }

        tail.next = new Node(matrix.get(row).get(column));
        return appendRowRecursively(
            matrix, row, column + 1, columns, tail.next
        );
    }

    /**
     * Converts the matrix to a singly linked list in row-major order.
     * Time: O(rows * columns); extra space: O(1), excluding the output list.
     */
    static Result matrixToLinkedListIterative(
        ArrayList<ArrayList<Integer>> matrix
    ) {
        if (!validMatrix(matrix)) {
            return new Result(null, false);
        }

        Node dummy = new Node(0);
        Node tail = dummy;

        for (ArrayList<Integer> row : matrix) {
            for (int value : row) {
                tail.next = new Node(value);
                tail = tail.next;
            }
        }

        return new Result(dummy.next, true);
    }

    /* **********************************************************************
     * Compatibility wrappers for the original method names
     * **********************************************************************/

    static Result TwoDMatrixToLinkedListRecursion(
        ArrayList<ArrayList<Integer>> matrix
    ) {
        return matrixToLinkedListRecursive(matrix);
    }

    static Result TwoDMatrixToLinkedListIteration(
        ArrayList<ArrayList<Integer>> matrix
    ) {
        return matrixToLinkedListIterative(matrix);
    }

    /* **********************************************************************
     * Test helpers
     * **********************************************************************/

    static ArrayList<ArrayList<Integer>> matrix(int[][] values) {
        if (values == null) {
            return null;
        }

        ArrayList<ArrayList<Integer>> result = new ArrayList<>();

        for (int[] row : values) {
            if (row == null) {
                result.add(null);
                continue;
            }

            ArrayList<Integer> convertedRow = new ArrayList<>();
            for (int value : row) {
                convertedRow.add(value);
            }
            result.add(convertedRow);
        }

        return result;
    }

    static ArrayList<Integer> toValues(Node head) {
        ArrayList<Integer> values = new ArrayList<>();
        Node current = head;

        while (current != null) {
            values.add(current.val);
            current = current.next;
        }

        return values;
    }

    static ArrayList<Integer> flatten(ArrayList<ArrayList<Integer>> matrix) {
        ArrayList<Integer> values = new ArrayList<>();

        if (matrix == null) {
            return values;
        }

        for (ArrayList<Integer> row : matrix) {
            if (row != null) {
                values.addAll(row);
            }
        }

        return values;
    }

    static boolean hasCycle(Node head) {
        Node slow = head;
        Node fast = head;

        while (fast != null && fast.next != null) {
            slow = slow.next;
            fast = fast.next.next;

            if (slow == fast) {
                return true;
            }
        }

        return false;
    }

    @FunctionalInterface
    interface Algorithm {
        Result solve(ArrayList<ArrayList<Integer>> matrix);
    }

    static record MethodCase(String name, Algorithm algorithm) {}

    static record TestCase(
        String id,
        ArrayList<ArrayList<Integer>> input,
        boolean expectedValid,
        String description
    ) {}

    static void runTests(MethodCase method, List<TestCase> tests) {
        printSeparator();
        System.out.println(method.name());
        printSeparator();

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {
            try {
                Result actual = method.algorithm().solve(test.input());

                boolean success =
                    actual != null
                    && actual.valid() == test.expectedValid()
                    && !hasCycle(actual.head())
                    && (test.expectedValid()
                        ? toValues(actual.head()).equals(flatten(test.input()))
                        : actual.head() == null);

                if (success) {
                    passed++;
                    System.out.printf(
                        "PASS %s (%s)%n  result = %s%n",
                        test.id(),
                        test.description(),
                        toValues(actual.head())
                    );
                } else {
                    failed++;
                    System.out.printf(
                        "FAIL %s (%s)%n  expected valid = %s%n"
                            + "  actual values = %s, valid = %s%n",
                        test.id(),
                        test.description(),
                        test.expectedValid(),
                        actual == null ? "null" : toValues(actual.head()),
                        actual != null && actual.valid()
                    );
                }
            } catch (Exception ex) {
                failed++;
                System.out.printf(
                    "FAIL %s (%s)%n  exception = %s%n",
                    test.id(),
                    test.description(),
                    ex
                );
            }
        }

        printResults(passed, failed, tests.size());
    }

    static List<TestCase> buildTests() {
        return List.of(
            new TestCase(
                "B1", matrix(new int[][]{{1, 2}, {3, 4}}), true,
                "2-by-2 matrix"
            ),
            new TestCase(
                "B2", matrix(new int[][]{{1, 2, 3}, {4, 5, 6}}), true,
                "rectangular matrix"
            ),
            new TestCase(
                "S1", matrix(new int[][]{{42}}), true,
                "single element"
            ),
            new TestCase(
                "S2", matrix(new int[][]{{-5, 0, 5}}), true,
                "one row with negative and positive values"
            ),
            new TestCase(
                "S3", matrix(new int[][]{{7}, {8}, {9}}), true,
                "one column"
            ),
            new TestCase(
                "E1", matrix(new int[][]{}), false,
                "empty matrix"
            ),
            new TestCase(
                "E2", null, false,
                "null matrix"
            ),
            new TestCase(
                "E3", matrix(new int[][]{{}}), false,
                "empty row"
            ),
            new TestCase(
                "E4", matrix(new int[][]{{1, 2}, {3}}), false,
                "non-rectangular matrix"
            ),
            new TestCase(
                "E5", matrix(new int[][]{null}), false,
                "null row"
            )
        );
    }

    static void runRandomisedTests(MethodCase method, int iterations) {
        printSeparator();
        System.out.println("Randomised tests");
        printSeparator();

        Random random = new Random(20261125L);

        for (int iteration = 1; iteration <= iterations; iteration++) {
            int rows = random.nextInt(8) + 1;
            int columns = random.nextInt(8) + 1;
            int[][] values = new int[rows][columns];

            for (int row = 0; row < rows; row++) {
                for (int column = 0; column < columns; column++) {
                    values[row][column] = random.nextInt(2001) - 1000;
                }
            }

            ArrayList<ArrayList<Integer>> input = matrix(values);
            Result result = method.algorithm().solve(input);

            if (result == null
                || !result.valid()
                || hasCycle(result.head())
                || !toValues(result.head()).equals(flatten(input))) {
                System.out.printf(
                    "Randomised test FAILED at iteration %d.%n",
                    iteration
                );
                System.out.println("Input = " + Arrays.deepToString(values));
                System.out.println(
                    "Actual = "
                        + (result == null ? "null" : toValues(result.head()))
                );
                return;
            }
        }

        System.out.printf(
            "All %d randomised tests passed.%n%n",
            iterations
        );
    }

    static void printSeparator() {
        System.out.println(
            "============================================================"
        );
    }

    static void printResults(int passed, int failed, int total) {
        System.out.printf(
            "%nResults: %d passed, %d failed, %d total%n%n",
            passed,
            failed,
            total
        );
    }

    public static void main(String[] args) {
        MethodCase[] methods = {
            new MethodCase(
                "Recursive row-major conversion",
                TwoDMatrixToLinkedList::matrixToLinkedListRecursive
            ),
            new MethodCase(
                "Iterative row-major conversion",
                TwoDMatrixToLinkedList::matrixToLinkedListIterative
            )
        };

        List<TestCase> tests = buildTests();

        for (MethodCase method : methods) {
            runTests(method, tests);
            runRandomisedTests(method, 1_000);
        }
    }
}
