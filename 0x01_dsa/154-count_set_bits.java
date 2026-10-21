import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class CountSetBits {

    static record Result(
        int result,
        boolean valid
    ) {}

    private static final int[] BYTE_LOOKUP =
        createByteLookup();

    private static final int[] NIBBLE_LOOKUP = {
        0, 1, 1, 2,
        1, 2, 2, 3,
        1, 2, 2, 3,
        2, 3, 3, 4
    };

    static boolean validArray(int[] array) {
        return array != null && array.length > 0;
    }

    /**
     * Checks all 32 bit positions of every integer.
     *
     * Time: O(32n), effectively O(n)
     * Space: O(1)
     */
    static Result countSetBitsBruteForce(int[] array) {
        if (!validArray(array)) {
            return new Result(-1, false);
        }

        int total = 0;

        for (int value : array) {
            for (int bit = 0; bit < Integer.SIZE; bit++) {
                total += (value >>> bit) & 1;
            }
        }

        return new Result(total, true);
    }

    /**
     * Brian Kernighan's algorithm.
     *
     * The expression value & (value - 1) clears one
     * set bit on every iteration.
     *
     * Time: O(total number of set bits)
     * Space: O(1)
     */
    static Result countSetBitsKernighanAlgo(int[] array) {
        if (!validArray(array)) {
            return new Result(-1, false);
        }

        int total = 0;

        for (int value : array) {
            while (value != 0) {
                value &= value - 1;
                total++;
            }
        }

        return new Result(total, true);
    }

    /**
     * Uses a precomputed table for all byte values from 0 to 255.
     *
     * Each int is split into four bytes.
     *
     * Time: O(4n), effectively O(n)
     * Space: O(256)
     */
    static Result countSetBitsLookUpTable(int[] array) {
        if (!validArray(array)) {
            return new Result(-1, false);
        }

        int total = 0;

        for (int value : array) {
            total += BYTE_LOOKUP[value & 0xFF];
            total += BYTE_LOOKUP[(value >>> 8) & 0xFF];
            total += BYTE_LOOKUP[(value >>> 16) & 0xFF];
            total += BYTE_LOOKUP[(value >>> 24) & 0xFF];
        }

        return new Result(total, true);
    }

    /**
     * Splits each integer into eight 4-bit nibbles.
     *
     * Time: O(8n), effectively O(n)
     * Space: O(16)
     */
    static Result countSetBitsMappingNibbles(int[] array) {
        if (!validArray(array)) {
            return new Result(-1, false);
        }

        int total = 0;

        for (int value : array) {
            for (int nibble = 0; nibble < 8; nibble++) {
                int currentNibble =
                    (value >>> (nibble * 4)) & 0x0F;

                total += NIBBLE_LOOKUP[currentNibble];
            }
        }

        return new Result(total, true);
    }

    /**
     * Uses Java's optimized standard-library implementation.
     *
     * Time: O(n)
     * Space: O(1)
     */
    static Result countSetBitsLibraryFunction(int[] array) {
        if (!validArray(array)) {
            return new Result(-1, false);
        }

        int total = 0;

        for (int value : array) {
            total += Integer.bitCount(value);
        }

        return new Result(total, true);
    }

    static int[] createByteLookup() {
        int[] lookup = new int[256];

        for (int value = 0; value < lookup.length; value++) {
            lookup[value] = Integer.bitCount(value);
        }

        return lookup;
    }

    static class TestCase {
        final String id;
        final int[] input;
        final int expected;
        final boolean expectedValid;
        final String description;

        TestCase(
            String id,
            int[] input,
            int expected,
            boolean expectedValid,
            String description
        ) {
            this.id = id;
            this.input = input;
            this.expected = expected;
            this.expectedValid = expectedValid;
            this.description = description;
        }
    }

    static void runTests(
        String algorithm,
        Function<int[], Result> method,
        List<TestCase> tests
    ) {
        System.out.println("==================================================");
        System.out.println(algorithm);
        System.out.println("==================================================");

        int passed = 0;
        int failed = 0;

        for (TestCase test : tests) {
            try {
                Result actual = method.apply(test.input);

                if (
                    actual.valid() == test.expectedValid
                    && actual.result() == test.expected
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
                        "  input    = %s%n",
                        Arrays.toString(test.input)
                    );
                    System.out.printf(
                        "  expected = Result(%d, %s)%n",
                        test.expected,
                        test.expectedValid
                    );
                    System.out.printf(
                        "  actual   = Result(%d, %s)%n",
                        actual.result(),
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

    static List<TestCase> createTests() {
        List<TestCase> tests = new ArrayList<>();

        tests.add(new TestCase(
            "S1",
            new int[]{1, 2, 3},
            4,
            true,
            "Small positive values"
        ));

        tests.add(new TestCase(
            "S2",
            new int[]{0, 0, 0},
            0,
            true,
            "All zero values"
        ));

        tests.add(new TestCase(
            "S3",
            new int[]{7, 15, 31},
            10,
            true,
            "Values with several set bits"
        ));

        tests.add(new TestCase(
            "S4",
            new int[]{-1},
            32,
            true,
            "Negative one has all 32 bits set"
        ));

        tests.add(new TestCase(
            "S5",
            new int[]{Integer.MIN_VALUE},
            1,
            true,
            "Only sign bit is set"
        ));

        tests.add(new TestCase(
            "S6",
            new int[]{Integer.MAX_VALUE},
            31,
            true,
            "All bits except sign bit are set"
        ));

        tests.add(new TestCase(
            "S7",
            new int[]{-1, 0, 1, -2},
            35,
            true,
            "Mixed positive and negative values"
        ));

        tests.add(new TestCase(
            "S8",
            new int[]{0x0F0F0F0F},
            16,
            true,
            "Nibble-pattern value"
        ));

        tests.add(new TestCase(
            "E1",
            null,
            -1,
            false,
            "Null array"
        ));

        tests.add(new TestCase(
            "E2",
            new int[]{},
            -1,
            false,
            "Empty array"
        ));

        return tests;
    }

    public static void main(String[] args) {
        List<TestCase> tests = createTests();

        System.out.println("############################################################");
        System.out.println("##################### COUNT SET BITS #######################");
        System.out.println("############################################################");
        System.out.println();

        runTests(
            "Brute Force O(32n)",
            CountSetBits::countSetBitsBruteForce,
            tests
        );

        runTests(
            "Brian Kernighan O(set bits)",
            CountSetBits::countSetBitsKernighanAlgo,
            tests
        );

        runTests(
            "Byte Lookup Table O(4n)",
            CountSetBits::countSetBitsLookUpTable,
            tests
        );

        runTests(
            "Nibble Mapping O(8n)",
            CountSetBits::countSetBitsMappingNibbles,
            tests
        );

        runTests(
            "Library Function O(n)",
            CountSetBits::countSetBitsLibraryFunction,
            tests
        );
    }
}
