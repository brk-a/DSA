import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class ScheduleMeeting {

    static class TestCase {
        final String id;
        final int[][] slot1;
        final int[][] slot2;
        final int d;
        final int[] expected;
        final String description;

        TestCase(
            String id,
            int[][] slot1,
            int[][] slot2,
            int d,
            int[] expected,
            String description
        ) {
            this.id = id;
            this.slot1 = slot1;
            this.slot2 = slot2;
            this.d = d;
            this.expected = expected;
            this.description = description;
        }
    }

    static int[] scheduleMeeting(int[][] slot1, int[][] slot2, int d) {
        if (slot1 == null || slot2 == null || d < 0) {
            return new int[0];
        }

        Arrays.sort(slot1, (x, y) -> Integer.compare(x[0], y[0]));
        Arrays.sort(slot2, (x, y) -> Integer.compare(x[0], y[0]));

        int p1 = 0;
        int p2 = 0;

        while (p1 < slot1.length && p2 < slot2.length) {
            int left = Math.max(slot1[p1][0], slot2[p2][0]);
            int right = Math.min(slot1[p1][1], slot2[p2][1]);

            if (right - left >= d) {
                return new int[]{left, left + d};
            }

            if (slot1[p1][1] < slot2[p2][1]) {
                p1++;
            } else {
                p2++;
            }
        }

        return new int[0];
    }

    static boolean arraysEqual(int[] a, int[] b) {
        return Arrays.equals(a, b);
    }

    static String formatIntervals(int[][] slots) {
        return slots == null ? "null" : Arrays.deepToString(slots);
    }

    static void runTests(
        String algorithm,
        Function<TestCase, int[]> method,
        List<TestCase> tests
    ) {
        System.out.println("==================================================");
        System.out.println(algorithm);
        System.out.println("==================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {
            try {
                int[] actual = method.apply(test);

                if (arraysEqual(actual, test.expected)) {
                    passed++;
                    System.out.printf("✓ %s (%s)%n", test.id, test.description);
                } else {
                    failed++;
                    System.out.printf("✗ %s (%s)%n", test.id, test.description);
                    System.out.printf("  slot1     = %s%n", formatIntervals(test.slot1));
                    System.out.printf("  slot2     = %s%n", formatIntervals(test.slot2));
                    System.out.printf("  d         = %d%n", test.d);
                    System.out.printf("  expected  = %s%n", Arrays.toString(test.expected));
                    System.out.printf("  actual    = %s%n", Arrays.toString(actual));
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
            new int[][]{
                {10, 50}, {60, 120}, {140, 210}
            },
            new int[][]{
                {0, 15}, {60, 70}
            },
            8,
            new int[]{60, 68},
            "Classic meeting slot example"
        ));

        tests.add(new TestCase(
            "S2",
            new int[][]{
                {10, 40}, {50, 90}
            },
            new int[][]{
                {5, 15}, {60, 70}
            },
            12,
            new int[]{60, 72},
            "Another standard example"
        ));

        /*
         * No possible meeting
         */
        tests.add(new TestCase(
            "N1",
            new int[][]{
                {1, 2}
            },
            new int[][]{
                {3, 4}
            },
            1,
            new int[0],
            "No overlap"
        ));

        /*
         * Exact fit
         */
        tests.add(new TestCase(
            "E1",
            new int[][]{
                {1, 5}
            },
            new int[][]{
                {2, 6}
            },
            3,
            new int[]{2, 5},
            "Meeting fits exactly in overlap"
        ));

        /*
         * Null / empty
         */
        tests.add(new TestCase(
            "X1",
            null,
            new int[][]{
                {1, 2}
            },
            1,
            new int[0],
            "Null first schedule"
        ));

        tests.add(new TestCase(
            "X2",
            new int[][]{},
            new int[][]{
                {1, 2}
            },
            1,
            new int[0],
            "Empty first schedule"
        ));

        tests.add(new TestCase(
            "X3",
            new int[][]{
                {1, 2}
            },
            new int[][]{},
            1,
            new int[0],
            "Empty second schedule"
        ));

        /*
         * More coverage
         */
        tests.add(new TestCase(
            "C1",
            new int[][]{
                {7, 10}, {1, 3}, {20, 25}
            },
            new int[][]{
                {2, 4}, {6, 8}, {15, 18}
            },
            2,
            new int[]{7, 9},
            "Unsorted input with a valid overlap"
        ));

        tests.add(new TestCase(
            "C2",
            new int[][]{
                {1, 10}
            },
            new int[][]{
                {2, 3}, {4, 5}
            },
            1,
            new int[]{2, 3},
            "First available slot is earliest valid"
        ));

        System.out.println("############################################################");
        System.out.println("############ SCHEDULE MEETING PROBLEM ######################");
        System.out.println("############################################################");
        System.out.println();

        runTests(
            "Two Pointers O(m log m + n log n)",
            test -> scheduleMeeting(test.slot1, test.slot2, test.d),
            tests
        );
    }
}
