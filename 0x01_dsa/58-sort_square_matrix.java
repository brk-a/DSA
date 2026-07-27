import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SortSquareMatrix {

    static boolean validSquareMatrix(List<List<Integer>> matrix) {
        if (matrix == null || matrix.isEmpty()) {
            return false;
        }
        int n = matrix.size();
        for (List<Integer> row : matrix) {
            if (row == null || row.size() != n) {
                return false;
            }
        }
        return true;
    }

    static ArrayList<ArrayList<Integer>> cloneMatrix(List<List<Integer>> matrix) {
        ArrayList<ArrayList<Integer>> clone = new ArrayList<>();
        if (matrix == null) {
            return clone;
        }
        for (List<Integer> row : matrix) {
            if (row == null) {
                clone.add(null);
            } else {
                clone.add(new ArrayList<>(row));
            }
        }
        return clone;
    }

    static ArrayList<ArrayList<Integer>> emptyMatrix() {
        return new ArrayList<>();
    }

    static ArrayList<ArrayList<Integer>> sortSquareMatrix(List<List<Integer>> matrix) {
        if (!validSquareMatrix(matrix)) {
            return emptyMatrix();
        }

        int n = matrix.size();
        ArrayList<Integer> values = new ArrayList<>(n * n);

        for (List<Integer> row : matrix) {
            values.addAll(row);
        }

        values.sort(Integer::compareTo);

        ArrayList<ArrayList<Integer>> result = new ArrayList<>(n);
        int idx = 0;
        for (int i = 0; i < n; i++) {
            ArrayList<Integer> row = new ArrayList<>(n);
            for (int j = 0; j < n; j++) {
                row.add(values.get(idx++));
            }
            result.add(row);
        }

        return result;
    }

    static boolean matricesEqual(List<List<Integer>> a, List<List<Integer>> b) {
        if (a == b) {
            return true;
        }
        if (a == null || b == null || a.size() != b.size()) {
            return false;
        }
        for (int i = 0; i < a.size(); i++) {
            List<Integer> ra = a.get(i);
            List<Integer> rb = b.get(i);
            if (ra == rb) {
                continue;
            }
            if (ra == null || rb == null || ra.size() != rb.size()) {
                return false;
            }
            if (!ra.equals(rb)) {
                return false;
            }
        }
        return true;
    }

    static String formatMatrix(List<? extends List<Integer>> matrix) {
        if (matrix == null) {
            return "null";
        }
        return matrix.toString();
    }

    static class TestCase {
        final String id;
        final List<List<Integer>> matrix;
        final ArrayList<ArrayList<Integer>> expected;
        final String description;

        TestCase(String id, List<List<Integer>> matrix, ArrayList<ArrayList<Integer>> expected, String description) {
            this.id = id;
            this.matrix = matrix;
            this.expected = expected;
            this.description = description;
        }
    }

    @FunctionalInterface
    interface MatrixAlgorithm {
        ArrayList<ArrayList<Integer>> solve(List<List<Integer>> matrix);
    }

    static class MethodCase {
        final String name;
        final MatrixAlgorithm algorithm;

        MethodCase(String name, MatrixAlgorithm algorithm) {
            this.name = name;
            this.algorithm = algorithm;
        }
    }

    static void runTests(String algorithm, MatrixAlgorithm method, List<TestCase> tests) {
        System.out.println("======================================================");
        System.out.println(algorithm);
        System.out.println("======================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {
            try {
                ArrayList<ArrayList<Integer>> actual = method.solve(cloneMatrix(test.matrix));
                if (matricesEqual(actual, test.expected)) {
                    passed++;
                    System.out.printf("✓ %s (%s)%n", test.id, test.description);
                } else {
                    failed++;
                    System.out.printf("✗ %s (%s)%n", test.id, test.description);
                    System.out.printf("  matrix    = %s%n", formatMatrix(test.matrix));
                    System.out.printf("  expected  = %s%n", formatMatrix(test.expected));
                    System.out.printf("  actual    = %s%n", formatMatrix(actual));
                }
            } catch (Exception ex) {
                failed++;
                System.out.printf("✗ %s (%s)%n", test.id, test.description);
                System.out.printf("  matrix    = %s%n", formatMatrix(test.matrix));
                System.out.printf("  exception = %s%n", ex.toString());
            }
        }

        System.out.println();
        System.out.printf("Results: %d passed, %d failed, %d total%n", passed, failed, tests.size());
        System.out.println();
    }

    static List<List<Integer>> makeMatrix(int[][] values) {
        ArrayList<List<Integer>> matrix = new ArrayList<>();
        for (int[] row : values) {
            ArrayList<Integer> r = new ArrayList<>();
            for (int v : row) {
                r.add(v);
            }
            matrix.add(r);
        }
        return matrix;
    }

    static ArrayList<ArrayList<Integer>> makeExpected(int[][] values) {
        ArrayList<ArrayList<Integer>> matrix = new ArrayList<>();
        for (int[] row : values) {
            ArrayList<Integer> r = new ArrayList<>();
            for (int v : row) {
                r.add(v);
            }
            matrix.add(r);
        }
        return matrix;
    }

    static int[][] randomSquareValues(Random rng, int n, int minValue, int maxValue) {
        int[][] values = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                values[i][j] = minValue + rng.nextInt(maxValue - minValue + 1);
            }
        }
        return values;
    }

    static List<List<Integer>> randomSquareMatrix(Random rng, int n, int minValue, int maxValue) {
        return makeMatrix(randomSquareValues(rng, n, minValue, maxValue));
    }

    static ArrayList<ArrayList<Integer>> sortedExpectedFromValues(int[][] values) {
        ArrayList<Integer> flat = new ArrayList<>();
        for (int[] row : values) {
            for (int v : row) {
                flat.add(v);
            }
        }
        flat.sort(Integer::compareTo);

        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        int idx = 0;
        int n = values.length;
        for (int i = 0; i < n; i++) {
            ArrayList<Integer> row = new ArrayList<>();
            for (int j = 0; j < n; j++) {
                row.add(flat.get(idx++));
            }
            result.add(row);
        }
        return result;
    }

    static void runRandomizedTests(int iterations) {
        System.out.println("======================================================");
        System.out.println("Randomized cross-checks");
        System.out.println("======================================================");

        Random rng = new Random(123456789L);
        for (int t = 1; t <= iterations; t++) {
            int n = 1 + rng.nextInt(6);
            int[][] values = randomSquareValues(rng, n, -20, 20);
            List<List<Integer>> matrix = makeMatrix(values);
            ArrayList<ArrayList<Integer>> expected = sortedExpectedFromValues(values);
            ArrayList<ArrayList<Integer>> actual = sortSquareMatrix(cloneMatrix(matrix));

            if (!matricesEqual(actual, expected)) {
                System.out.println("Randomized test FAILED");
                System.out.println("matrix   = " + formatMatrix(matrix));
                System.out.println("expected = " + formatMatrix(expected));
                System.out.println("actual   = " + formatMatrix(actual));
                return;
            }
        }
        System.out.printf("All %d randomized tests passed.%n%n", iterations);
    }

    public static void main(String[] args) {
        List<TestCase> tests = new ArrayList<>();

        tests.add(new TestCase(
                "S1",
                makeMatrix(new int[][]{
                        {9, 1},
                        {4, 7}
                }),
                makeExpected(new int[][]{
                        {1, 4},
                        {7, 9}
                }),
                "Basic 2x2 matrix"
        ));

        tests.add(new TestCase(
                "S2",
                makeMatrix(new int[][]{
                        {5}
                }),
                makeExpected(new int[][]{
                        {5}
                }),
                "Single element matrix"
        ));

        tests.add(new TestCase(
                "S3",
                makeMatrix(new int[][]{
                        {3, 2, 1},
                        {6, 5, 4},
                        {9, 8, 7}
                }),
                makeExpected(new int[][]{
                        {1, 2, 3},
                        {4, 5, 6},
                        {7, 8, 9}
                }),
                "Already square, reverse sorted"
        ));

        tests.add(new TestCase(
                "D1",
                makeMatrix(new int[][]{
                        {1, 1},
                        {1, 1}
                }),
                makeExpected(new int[][]{
                        {1, 1},
                        {1, 1}
                }),
                "All values identical"
        ));

        tests.add(new TestCase(
                "D2",
                makeMatrix(new int[][]{
                        {-3, 0},
                        {2, -1}
                }),
                makeExpected(new int[][]{
                        {-3, -1},
                        {0, 2}
                }),
                "Negative and positive values"
        ));

        tests.add(new TestCase(
                "E1",
                null,
                emptyMatrix(),
                "Null matrix"
        ));

        tests.add(new TestCase(
                "E2",
                new ArrayList<>(),
                emptyMatrix(),
                "Empty matrix"
        ));

        tests.add(new TestCase(
                "E3",
                makeMatrix(new int[][]{
                        {}
                }),
                emptyMatrix(),
                "Single empty row"
        ));

        ArrayList<List<Integer>> ragged = new ArrayList<>();
        ragged.add(new ArrayList<>(List.of(1, 2)));
        ragged.add(new ArrayList<>(List.of(3)));
        tests.add(new TestCase(
                "E4",
                ragged,
                emptyMatrix(),
                "Ragged matrix"
        ));

        ArrayList<List<Integer>> nullRow = new ArrayList<>();
        nullRow.add(null);
        nullRow.add(new ArrayList<>(List.of(1)));
        tests.add(new TestCase(
                "E5",
                nullRow,
                emptyMatrix(),
                "Null row"
        ));

        tests.add(new TestCase(
                "X1",
                makeMatrix(new int[][]{
                        {8, 3, 5},
                        {4, 9, 1},
                        {7, 6, 2}
                }),
                makeExpected(new int[][]{
                        {1, 2, 3},
                        {4, 5, 6},
                        {7, 8, 9}
                }),
                "Cross-check"
        ));

        tests.add(new TestCase(
                "X2",
                makeMatrix(new int[][]{
                        {-10, 50},
                        {0, 7}
                }),
                makeExpected(new int[][]{
                        {-10, 0},
                        {7, 50}
                }),
                "Cross-check with gaps"
        ));

        System.out.println("############################################################");
        System.out.println("################### SORT SQUARE MATRIX #####################");
        System.out.println("############################################################");
        System.out.println();

        List<MethodCase> methods = List.of(
                new MethodCase("Refactored Sort", SortSquareMatrix::sortSquareMatrix)
        );

        for (MethodCase method : methods) {
            runTests(method.name, method.algorithm, tests);
        }

        runRandomizedTests(500);
    }
}