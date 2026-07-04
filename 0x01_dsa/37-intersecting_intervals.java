import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class IntersectingIntervals {

    static class TestCase {
        final String id;
        final ArrayList<ArrayList<Integer>> nums1;
        final ArrayList<ArrayList<Integer>> nums2;
        final ArrayList<ArrayList<Integer>> expected;
        final String description;

        TestCase(
            String id,
            ArrayList<ArrayList<Integer>> nums1,
            ArrayList<ArrayList<Integer>> nums2,
            ArrayList<ArrayList<Integer>> expected,
            String description
        ) {
            this.id = id;
            this.nums1 = nums1;
            this.nums2 = nums2;
            this.expected = expected;
            this.description = description;
        }
    }

    static ArrayList<ArrayList<Integer>> interval(int start, int end) {
        ArrayList<ArrayList<Integer>> list = new ArrayList<>();
        list.add(new ArrayList<>(Arrays.asList(start, end)));
        return list;
    }

    static ArrayList<ArrayList<Integer>> toIntervals(int[][] arr) {
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();
        if (arr == null) return result;
        for (int[] interval : arr) {
            result.add(new ArrayList<>(Arrays.asList(interval[0], interval[1])));
        }
        return result;
    }

    /**
     * Two pointers solution.
     *
     * Time: O(m + n)
     * Space: O(1) excluding output
     */
    static ArrayList<ArrayList<Integer>> intersectingIntervals(
        ArrayList<ArrayList<Integer>> nums1,
        ArrayList<ArrayList<Integer>> nums2
    ) {
        ArrayList<ArrayList<Integer>> result = new ArrayList<>();

        if (nums1 == null || nums2 == null || nums1.isEmpty() || nums2.isEmpty()) {
            return result;
        }

        int i = 0;
        int j = 0;

        while (i < nums1.size() && j < nums2.size()) {
            int start1 = nums1.get(i).get(0);
            int end1 = nums1.get(i).get(1);
            int start2 = nums2.get(j).get(0);
            int end2 = nums2.get(j).get(1);

            int left = Math.max(start1, start2);
            int right = Math.min(end1, end2);

            if (left <= right) {
                result.add(new ArrayList<>(Arrays.asList(left, right)));
            }

            if (end1 < end2) {
                i++;
            } else {
                j++;
            }
        }

        return result;
    }

    static boolean intervalsEqual(
        ArrayList<ArrayList<Integer>> a,
        ArrayList<ArrayList<Integer>> b
    ) {
        if (a == null || b == null) return a == b;
        if (a.size() != b.size()) return false;
        for (int i = 0; i < a.size(); i++) {
            if (!a.get(i).equals(b.get(i))) return false;
        }
        return true;
    }

    static String formatIntervals(ArrayList<ArrayList<Integer>> intervals) {
        return intervals == null ? "null" : intervals.toString();
    }

    static void runTests(
        String algorithm,
        Function<TestCase, ArrayList<ArrayList<Integer>>> method,
        List<TestCase> tests
    ) {
        System.out.println("==================================================");
        System.out.println(algorithm);
        System.out.println("==================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {
            try {
                ArrayList<ArrayList<Integer>> actual = method.apply(test);

                if (intervalsEqual(actual, test.expected)) {
                    passed++;
                    System.out.printf("✓ %s (%s)%n", test.id, test.description);
                } else {
                    failed++;
                    System.out.printf("✗ %s (%s)%n", test.id, test.description);
                    System.out.printf("  nums1     = %s%n", formatIntervals(test.nums1));
                    System.out.printf("  nums2     = %s%n", formatIntervals(test.nums2));
                    System.out.printf("  expected  = %s%n", formatIntervals(test.expected));
                    System.out.printf("  actual    = %s%n", formatIntervals(actual));
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
            toIntervals(new int[][]{
                {0, 2}, {5, 10}, {13, 23}, {24, 25}
            }),
            toIntervals(new int[][]{
                {1, 5}, {8, 12}, {15, 24}, {25, 26}
            }),
            toIntervals(new int[][]{
                {1, 2}, {5, 5}, {8, 10}, {15, 23}, {24, 24}, {25, 25}
            }),
            "Classic interval list intersection"
        ));

        tests.add(new TestCase(
            "S2",
            toIntervals(new int[][]{
                {1, 3}, {5, 9}
            }),
            toIntervals(new int[][]{
                {2, 5}, {7, 10}
            }),
            toIntervals(new int[][]{
                {2, 3}, {5, 5}, {7, 9}
            }),
            "Common overlapping intervals"
        ));

        /*
         * Touching endpoints
         */
        tests.add(new TestCase(
            "T1",
            toIntervals(new int[][]{
                {1, 2}
            }),
            toIntervals(new int[][]{
                {2, 3}
            }),
            toIntervals(new int[][]{
                {2, 2}
            }),
            "Touching closed intervals"
        ));

        /*
         * Empty / null cases
         */
        tests.add(new TestCase(
            "E1",
            null,
            toIntervals(new int[][]{
                {1, 2}
            }),
            new ArrayList<>(),
            "First list null"
        ));

        tests.add(new TestCase(
            "E2",
            toIntervals(new int[][]{
                {1, 2}
            }),
            new ArrayList<>(),
            new ArrayList<>(),
            "Second list empty"
        ));

        tests.add(new TestCase(
            "E3",
            new ArrayList<>(),
            new ArrayList<>(),
            new ArrayList<>(),
            "Both lists empty"
        ));

        /*
         * No intersections
         */
        tests.add(new TestCase(
            "N1",
            toIntervals(new int[][]{
                {1, 2}, {5, 6}
            }),
            toIntervals(new int[][]{
                {3, 4}, {7, 8}
            }),
            new ArrayList<>(),
            "Disjoint lists"
        ));

        /*
         * More coverage
         */
        tests.add(new TestCase(
            "C1",
            toIntervals(new int[][]{
                {1, 7}
            }),
            toIntervals(new int[][]{
                {3, 5}
            }),
            toIntervals(new int[][]{
                {3, 5}
            }),
            "One interval contains the other"
        ));

        tests.add(new TestCase(
            "C2",
            toIntervals(new int[][]{
                {1, 2}, {3, 4}, {5, 6}
            }),
            toIntervals(new int[][]{
                {0, 10}
            }),
            toIntervals(new int[][]{
                {1, 2}, {3, 4}, {5, 6}
            }),
            "Second list contains all intervals"
        ));

        tests.add(new TestCase(
            "C3",
            toIntervals(new int[][]{
                {0, 0}, {2, 2}, {4, 4}
            }),
            toIntervals(new int[][]{
                {0, 1}, {2, 3}, {4, 5}
            }),
            toIntervals(new int[][]{
                {0, 0}, {2, 2}, {4, 4}
            }),
            "Point intersections"
        ));

        System.out.println("############################################################");
        System.out.println("############ INTERSECTING INTERVALS PROBLEM ################");
        System.out.println("############################################################");
        System.out.println();

        runTests(
            "Two Pointers O(m + n)",
            test -> intersectingIntervals(test.nums1, test.nums2),
            tests
        );
    }
}
