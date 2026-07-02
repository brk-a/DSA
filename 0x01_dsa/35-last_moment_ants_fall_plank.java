import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class LastMomentAntsFallPlank {

    static class TestCase {
        final String id;
        final int n;
        final int[] left;
        final int[] right;
        final int expected;
        final String description;

        TestCase(String id, int n, int[] left, int[] right, int expected, String description) {
            this.id = id;
            this.n = n;
            this.left = left;
            this.right = right;
            this.expected = expected;
            this.description = description;
        }
    }

    /**
     * Optimal Solution
     *
     * Time: O(L + R)
     * Space: O(1)
     */
    static int lastMomentAntsFallPlank(int n, int[] left, int[] right) {
        if (n < 0) {
            return -1;
        }

        int result = 0;

        if (left != null) {
            for (int pos : left) {
                result = Math.max(result, pos);
            }
        }

        if (right != null) {
            for (int pos : right) {
                result = Math.max(result, n - pos);
            }
        }

        return result;
    }

    /**
     * Brute Force / Simulation-style verification.
     * Since collisions don't affect the answer, this still reduces to the same logic.
     *
     * Time: O(L + R)
     * Space: O(1)
     */
    static int lastMomentAntsFallPlankBruteForce(int n, int[] left, int[] right) {
        return lastMomentAntsFallPlank(n, left, right);
    }

    static void runTests(
        String algorithm,
        Function<TestCase, Integer> method,
        List<TestCase> tests
    ) {
        System.out.println("==================================================");
        System.out.println(algorithm);
        System.out.println("==================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {
            try {
                int actual = method.apply(test);

                if (actual == test.expected) {
                    passed++;
                    System.out.printf("✓ %s (%s)%n", test.id, test.description);
                } else {
                    failed++;
                    System.out.printf("✗ %s (%s)%n", test.id, test.description);
                    System.out.printf("  n         = %d%n", test.n);
                    System.out.printf("  left      = %s%n", test.left == null ? "null" : Arrays.toString(test.left));
                    System.out.printf("  right     = %s%n", test.right == null ? "null" : Arrays.toString(test.right));
                    System.out.printf("  expected  = %d%n", test.expected);
                    System.out.printf("  actual    = %d%n", actual);
                }
            } catch (Exception ex) {
                failed++;
                System.out.printf("✗ %s (%s)%n", test.id, test.description);
                System.out.printf("  exception = %s%n", ex.getMessage());
            }
        }

        System.out.printf("%nResults: %d passed, %d failed, %d total%n%n", passed, failed, tests.size());
    }

    public static void main(String[] args) {
        List<TestCase> tests = new ArrayList<>();

        /*
         * Standard examples
         */
        tests.add(new TestCase(
            "S1",
            4,
            new int[]{4, 3},
            new int[]{0, 1},
            4,
            "Classic example"
        ));

        tests.add(new TestCase(
            "S2",
            7,
            new int[]{},
            new int[]{0, 1, 2, 5, 6},
            7,
            "Only right-moving ants"
        ));

        tests.add(new TestCase(
            "S3",
            10,
            new int[]{2, 6},
            new int[]{1, 3, 7},
            9,
            "Mixed directions"
        ));

        /*
         * Empty / null cases
         */
        tests.add(new TestCase(
            "E1",
            5,
            null,
            null,
            0,
            "Both arrays null"
        ));

        tests.add(new TestCase(
            "E2",
            5,
            new int[]{},
            new int[]{},
            0,
            "Both arrays empty"
        ));

        tests.add(new TestCase(
            "E3",
            5,
            null,
            new int[]{1, 2},
            4,
            "Left null, right present"
        ));

        tests.add(new TestCase(
            "E4",
            5,
            new int[]{3, 4},
            null,
            4,
            "Right null, left present"
        ));

        /*
         * Edge positions
         */
        tests.add(new TestCase(
            "X1",
            5,
            new int[]{0},
            new int[]{5},
            5,
            "Ants already at edges"
        ));

        tests.add(new TestCase(
            "X2",
            8,
            new int[]{8},
            new int[]{0},
            8,
            "Ants at opposite edges"
        ));

        tests.add(new TestCase(
            "X3",
            8,
            new int[]{1, 2, 3},
            new int[]{4, 5, 6},
            7,
            "General case"
        ));

        /*
         * More coverage
         */
        tests.add(new TestCase(
            "C1",
            9,
            new int[]{2, 4, 6},
            new int[]{1, 3, 8},
            8,
            "Last ant falls from right side"
        ));

        tests.add(new TestCase(
            "C2",
            9,
            new int[]{9},
            new int[]{},
            9,
            "Single ant moving left from far end"
        ));

        tests.add(new TestCase(
            "C3",
            9,
            new int[]{},
            new int[]{0},
            9,
            "Single ant moving right from the start"
        ));

        System.out.println("################################################################################");
        System.out.println("############ LAST MOMENT BEFORE ANTS FALL FROM PLANK PROBLEM ###################");
        System.out.println("################################################################################");
        System.out.println();

        runTests(
            "Optimal O(L + R)",
            test -> lastMomentAntsFallPlank(test.n, test.left, test.right),
            tests
        );
    }
}
