import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class MaxDistanceOccupiedSeats {

    static class TestCase {
        final String id;
        final String seats;
        final int expected;
        final String description;

        TestCase(String id, String seats, int expected, String description) {
            this.id = id;
            this.seats = seats;
            this.expected = expected;
            this.description = description;
        }
    }

    /**
     * Extra-array solution.
     *
     * Time: O(n)
     * Space: O(n)
     */
    static int maxDistanceOccupiedSeatsExtraArray(String seats) {
        if (seats == null || seats.isEmpty()) {
            return -1;
        }

        int n = seats.length();
        int[] distanceLeft = new int[n];
        int[] distanceRight = new int[n];

        int nearest = -n;
        for (int i = 0; i < n; i++) {
            if (seats.charAt(i) == '1') {
                nearest = i;
                distanceLeft[i] = 0;
            } else {
                distanceLeft[i] = i - nearest;
            }
        }

        nearest = 2 * n;
        for (int i = n - 1; i >= 0; i--) {
            if (seats.charAt(i) == '1') {
                nearest = i;
                distanceRight[i] = 0;
            } else {
                distanceRight[i] = nearest - i;
            }
        }

        int result = 0;
        for (int i = 0; i < n; i++) {
            if (seats.charAt(i) == '0') {
                result = Math.max(result, Math.min(distanceLeft[i], distanceRight[i]));
            }
        }

        return result;
    }

    /**
     * One-pass solution.
     *
     * Time: O(n)
     * Space: O(1)
     */
    static int maxDistanceOccupiedSeatsOneVariable(String seats) {
        if (seats == null || seats.isEmpty()) {
            return -1;
        }

        int n = seats.length();
        int prevOne = -1;
        int result = 0;

        for (int i = 0; i < n; i++) {
            if (seats.charAt(i) == '1') {
                if (prevOne == -1) {
                    result = Math.max(result, i);
                } else {
                    result = Math.max(result, (i - prevOne) / 2);
                }
                prevOne = i;
            }
        }

        result = Math.max(result, n - 1 - prevOne);
        return result;
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
                    System.out.printf("  seats     = %s%n", test.seats == null ? "null" : test.seats);
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
            "10001",
            2,
            "Classic middle gap"
        ));

        tests.add(new TestCase(
            "S2",
            "00100",
            2,
            "Single occupied seat in the middle"
        ));

        tests.add(new TestCase(
            "S3",
            "1",
            0,
            "Single occupied seat"
        ));

        tests.add(new TestCase(
            "S4",
            "10",
            1,
            "Trailing empty seats"
        ));

        tests.add(new TestCase(
            "S5",
            "01",
            1,
            "Leading empty seats"
        ));

        /*
         * Null / empty
         */
        tests.add(new TestCase(
            "E1",
            null,
            -1,
            "Null string"
        ));

        tests.add(new TestCase(
            "E2",
            "",
            -1,
            "Empty string"
        ));

        /*
         * All occupied / all empty-like invalid cases
         */
        tests.add(new TestCase(
            "A1",
            "1111",
            0,
            "All seats occupied"
        ));

        tests.add(new TestCase(
            "A2",
            "0000",
            0,
            "No occupied seats; algorithm returns 0 by construction"
        ));

        /*
         * More coverage
         */
        tests.add(new TestCase(
            "C1",
            "100001",
            4,
            "Long middle gap"
        ));

        tests.add(new TestCase(
            "C2",
            "1000001",
            3,
            "Even-length middle gap"
        ));

        tests.add(new TestCase(
            "C3",
            "100100010001",
            2,
            "Multiple gaps"
        ));

        tests.add(new TestCase(
            "C4",
            "0001000",
            3,
            "Occupied seat with both side gaps"
        ));

        tests.add(new TestCase(
            "C5",
            "1000000000",
            9,
            "Trailing gap dominates"
        ));

        tests.add(new TestCase(
            "C6",
            "0000000001",
            9,
            "Leading gap dominates"
        ));

        System.out.println("############################################################");
        System.out.println("############ MAX DISTANCE OCCUPIED SEATS ###################");
        System.out.println("############################################################");
        System.out.println();

        runTests(
            "Extra Array O(n)",
            test -> maxDistanceOccupiedSeatsExtraArray(test.seats),
            tests
        );

        runTests(
            "One Variable O(n)",
            test -> maxDistanceOccupiedSeatsOneVariable(test.seats),
            tests
        );
    }
}
