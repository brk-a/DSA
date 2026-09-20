import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Trie (Prefix Tree) implementation.
 *
 * Supported operations:
 *
 * 1. Insert
 *      Insert a word into the Trie.
 *
 * 2. Search
 *      Determine whether an exact word exists.
 *
 * 3. Prefix Search
 *      Determine whether any word starts with a given prefix.
 *
 * The Trie supports lowercase English letters:
 *
 *      a-z
 *
 * Each TrieNode contains an array of 26 children.
 *
 * Complexity:
 *
 *      insert(word)    O(L)
 *      search(word)    O(L)
 *      isPrefix(prefix) O(L)
 *
 * where L is the length of the word/prefix.
 */
public class Trie {

    /* **********************************************************************
     * Constants
     * **********************************************************************/

    private static final int ALPHABET_SIZE = 26;
    private static final char LTR_A = 'a';

    /* **********************************************************************
     * Trie Node
     * **********************************************************************/

    static class TrieNode {

        TrieNode[] children;
        boolean isEndOfWord;

        TrieNode() {
            children = new TrieNode[ALPHABET_SIZE];
            isEndOfWord = false;
        }
    }

    /* **********************************************************************
     * Fields
     * **********************************************************************/

    private final TrieNode root;

    /* **********************************************************************
     * Constructor
     * **********************************************************************/

    public Trie() {
        root = new TrieNode();
    }

    /* **********************************************************************
     * 1. Insert
     * **********************************************************************/

    /**
     * Inserts a word into the Trie.
     *
     * Example:
     *
     *      insert("apple")
     *
     * creates the path:
     *
     *      a -> p -> p -> l -> e
     *
     * The final node is marked as the end of a complete word.
     */
    public void insert(String word) {

        validateWord(word);

        TrieNode curr = root;

        for (char c : word.toCharArray()) {

            int index = indexOf(c);

            if (curr.children[index] == null) {

                curr.children[index] =
                        new TrieNode();
            }

            curr = curr.children[index];
        }

        curr.isEndOfWord = true;
    }

    /* **********************************************************************
     * 2. Search
     * **********************************************************************/

    /**
     * Searches for an exact word.
     *
     * Example:
     *
     *      insert("apple")
     *
     *      search("apple") -> true
     *      search("app")   -> false
     */
    public boolean search(String word) {

        validateWord(word);

        TrieNode node =
                findNode(word);

        return node != null
                && node.isEndOfWord;
    }

    /* **********************************************************************
     * 3. Prefix Search
     * **********************************************************************/

    /**
     * Determines whether the supplied prefix exists.
     *
     * Example:
     *
     *      insert("apple")
     *
     *      isPrefix("app")    -> true
     *      isPrefix("apple")  -> true
     *      isPrefix("apples") -> false
     */
    public boolean isPrefix(String prefix) {

        validateWord(prefix);

        return findNode(prefix) != null;
    }

    /* **********************************************************************
     * Helper: Find Node
     * **********************************************************************/

    /**
     * Traverses the Trie using the supplied string.
     *
     * Returns:
     *
     *      the final TrieNode if the complete path exists
     *      null otherwise
     *
     * This helper is shared by search() and isPrefix().
     */
    private TrieNode findNode(String key) {

        TrieNode curr = root;

        for (char c : key.toCharArray()) {

            int index = indexOf(c);

            if (curr.children[index] == null) {
                return null;
            }

            curr = curr.children[index];
        }

        return curr;
    }

    /* **********************************************************************
     * Helper: Character Index
     * **********************************************************************/

    /**
     * Converts:
     *
     *      a -> 0
     *      b -> 1
     *      ...
     *      z -> 25
     */
    private int indexOf(char c) {

        return c - LTR_A;
    }

    /* **********************************************************************
     * Helper: Input Validation
     * **********************************************************************/

    /**
     * The Trie supports lowercase English letters only.
     *
     * Null and invalid characters are rejected explicitly rather
     * than producing an ArrayIndexOutOfBoundsException later.
     */
    private void validateWord(String word) {

        if (word == null) {

            throw new IllegalArgumentException(
                    "Word cannot be null");
        }

        for (char c : word.toCharArray()) {

            if (c < 'a' || c > 'z') {

                throw new IllegalArgumentException(
                        "Only lowercase letters a-z are supported: "
                                + word);
            }
        }
    }

    /* **********************************************************************
     * Test Helpers
     * **********************************************************************/

    static void assertTrue(
            boolean condition,
            String testName) {

        if (!condition) {

            throw new AssertionError(
                    "Expected true: " + testName);
        }
    }

    static void assertFalse(
            boolean condition,
            String testName) {

        if (condition) {

            throw new AssertionError(
                    "Expected false: " + testName);
        }
    }

    static void assertThrows(
            Runnable action,
            String testName) {

        try {

            action.run();

            throw new AssertionError(
                    "Expected exception: " + testName);

        } catch (IllegalArgumentException expected) {

            // Expected.
        }
    }

    /* **********************************************************************
     * Test Case
     * **********************************************************************/

    static class TestCase {

        final String id;
        final String description;

        TestCase(
                String id,
                String description) {

            this.id = id;
            this.description = description;
        }
    }

    /* **********************************************************************
     * 1. Basic Tests
     * **********************************************************************/

    static void runBasicTests() {

        System.out.println(
                "======================================================");

        System.out.println(
                "Basic Tests");

        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;

        List<TestCase> tests =
                List.of(

                        new TestCase(
                                "B1",
                                "insert and search one word"),

                        new TestCase(
                                "B2",
                                "search missing word"),

                        new TestCase(
                                "B3",
                                "prefix exists"),

                        new TestCase(
                                "B4",
                                "prefix does not exist"),

                        new TestCase(
                                "B5",
                                "word is also a prefix"),

                        new TestCase(
                                "B6",
                                "multiple words with shared prefix")
                );

        for (TestCase test : tests) {

            try {

                Trie trie =
                        new Trie();

                switch (test.id) {

                    case "B1" -> {

                        trie.insert("apple");

                        assertTrue(
                                trie.search("apple"),
                                test.id);
                    }

                    case "B2" -> {

                        trie.insert("apple");

                        assertFalse(
                                trie.search("banana"),
                                test.id);
                    }

                    case "B3" -> {

                        trie.insert("apple");

                        assertTrue(
                                trie.isPrefix("app"),
                                test.id);
                    }

                    case "B4" -> {

                        trie.insert("apple");

                        assertFalse(
                                trie.isPrefix("ban"),
                                test.id);
                    }

                    case "B5" -> {

                        trie.insert("app");

                        assertTrue(
                                trie.search("app"),
                                test.id);

                        assertTrue(
                                trie.isPrefix("app"),
                                test.id);
                    }

                    case "B6" -> {

                        trie.insert("apple");
                        trie.insert("application");
                        trie.insert("app");

                        assertTrue(
                                trie.search("apple"),
                                test.id);

                        assertTrue(
                                trie.search("application"),
                                test.id);

                        assertTrue(
                                trie.search("app"),
                                test.id);

                        assertTrue(
                                trie.isPrefix("appl"),
                                test.id);
                    }
                }

                passed++;

                System.out.printf(
                        "PASS %s (%s)%n",
                        test.id,
                        test.description);

            } catch (Exception ex) {

                failed++;

                System.out.printf(
                        "FAIL %s (%s)%n",
                        test.id,
                        test.description);

                System.out.println(
                        "  exception = " + ex);
            }
        }

        printResults(
                passed,
                failed,
                tests.size());
    }

    /* **********************************************************************
     * 2. Edge Case Tests
     * **********************************************************************/

    static void runEdgeCaseTests() {

        System.out.println(
                "======================================================");

        System.out.println(
                "Edge Case Tests");

        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;
        int total = 0;

        /*
         * Empty string.
         *
         * An empty string is valid in this implementation.
         * It represents the root as an end-of-word node.
         */
        total++;

        try {

            Trie trie =
                    new Trie();

            trie.insert("");

            assertTrue(
                    trie.search(""),
                    "E1");

            assertTrue(
                    trie.isPrefix(""),
                    "E1");

            passed++;

            System.out.println(
                    "PASS E1 - empty string");

        } catch (Exception ex) {

            failed++;

            System.out.println(
                    "FAIL E1 - empty string");

            System.out.println(
                    "  exception = " + ex);
        }

        /*
         * Single character.
         */
        total++;

        try {

            Trie trie =
                    new Trie();

            trie.insert("a");

            assertTrue(
                    trie.search("a"),
                    "E2");

            assertTrue(
                    trie.isPrefix("a"),
                    "E2");

            assertFalse(
                    trie.search("b"),
                    "E2");

            passed++;

            System.out.println(
                    "PASS E2 - single character");

        } catch (Exception ex) {

            failed++;

            System.out.println(
                    "FAIL E2 - single character");

            System.out.println(
                    "  exception = " + ex);
        }

        /*
         * Long word.
         */
        total++;

        try {

            Trie trie =
                    new Trie();

            String word =
                    "abcdefghijklmnopqrstuvwxyz";

            trie.insert(word);

            assertTrue(
                    trie.search(word),
                    "E3");

            assertTrue(
                    trie.isPrefix(
                            "abcdefghijkl"),
                    "E3");

            passed++;

            System.out.println(
                    "PASS E3 - long word");

        } catch (Exception ex) {

            failed++;

            System.out.println(
                    "FAIL E3 - long word");

            System.out.println(
                    "  exception = " + ex);
        }

        /*
         * Duplicate insertion.
         */
        total++;

        try {

            Trie trie =
                    new Trie();

            trie.insert("hello");
            trie.insert("hello");
            trie.insert("hello");

            assertTrue(
                    trie.search("hello"),
                    "E4");

            passed++;

            System.out.println(
                    "PASS E4 - duplicate insertion");

        } catch (Exception ex) {

            failed++;

            System.out.println(
                    "FAIL E4 - duplicate insertion");

            System.out.println(
                    "  exception = " + ex);
        }

        /*
         * Prefix that is longer than an inserted word.
         */
        total++;

        try {

            Trie trie =
                    new Trie();

            trie.insert("cat");

            assertFalse(
                    trie.isPrefix("cats"),
                    "E5");

            passed++;

            System.out.println(
                    "PASS E5 - prefix longer than word");

        } catch (Exception ex) {

            failed++;

            System.out.println(
                    "FAIL E5 - prefix longer than word");

            System.out.println(
                    "  exception = " + ex);
        }

        printResults(
                passed,
                failed,
                total);
    }

    /* **********************************************************************
     * 3. Invalid Input Tests
     * **********************************************************************/

    static void runInvalidInputTests() {

        System.out.println(
                "======================================================");

        System.out.println(
                "Invalid Input Tests");

        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;
        int total = 0;

        /*
         * Null insert.
         */
        total++;

        try {

            Trie trie =
                    new Trie();

            assertThrows(
                    () -> trie.insert(null),
                    "I1");

            passed++;

            System.out.println(
                    "PASS I1 - null insert rejected");

        } catch (Exception ex) {

            failed++;

            System.out.println(
                    "FAIL I1 - null insert rejected");

            System.out.println(
                    "  exception = " + ex);
        }

        /*
         * Null search.
         */
        total++;

        try {

            Trie trie =
                    new Trie();

            assertThrows(
                    () -> trie.search(null),
                    "I2");

            passed++;

            System.out.println(
                    "PASS I2 - null search rejected");

        } catch (Exception ex) {

            failed++;

            System.out.println(
                    "FAIL I2 - null search rejected");

            System.out.println(
                    "  exception = " + ex);
        }

        /*
         * Null prefix.
         */
        total++;

        try {

            Trie trie =
                    new Trie();

            assertThrows(
                    () -> trie.isPrefix(null),
                    "I3");

            passed++;

            System.out.println(
                    "PASS I3 - null prefix rejected");

        } catch (Exception ex) {

            failed++;

            System.out.println(
                    "FAIL I3 - null prefix rejected");

            System.out.println(
                    "  exception = " + ex);
        }

        /*
         * Uppercase character.
         */
        total++;

        try {

            Trie trie =
                    new Trie();

            assertThrows(
                    () -> trie.insert("Apple"),
                    "I4");

            passed++;

            System.out.println(
                    "PASS I4 - uppercase input rejected");

        } catch (Exception ex) {

            failed++;

            System.out.println(
                    "FAIL I4 - uppercase input rejected");

            System.out.println(
                    "  exception = " + ex);
        }

        /*
         * Number.
         */
        total++;

        try {

            Trie trie =
                    new Trie();

            assertThrows(
                    () -> trie.insert("abc123"),
                    "I5");

            passed++;

            System.out.println(
                    "PASS I5 - numeric input rejected");

        } catch (Exception ex) {

            failed++;

            System.out.println(
                    "FAIL I5 - numeric input rejected");

            System.out.println(
                    "  exception = " + ex);
        }

        /*
         * Whitespace.
         */
        total++;

        try {

            Trie trie =
                    new Trie();

            assertThrows(
                    () -> trie.insert("hello world"),
                    "I6");

            passed++;

            System.out.println(
                    "PASS I6 - whitespace rejected");

        } catch (Exception ex) {

            failed++;

            System.out.println(
                    "FAIL I6 - whitespace rejected");

            System.out.println(
                    "  exception = " + ex);
        }

        printResults(
                passed,
                failed,
                total);
    }

    /* **********************************************************************
     * 4. Prefix Relationship Tests
     * **********************************************************************/

    static void runPrefixTests() {

        System.out.println(
                "======================================================");

        System.out.println(
                "Prefix Relationship Tests");

        System.out.println(
                "======================================================");

        int passed = 0;
        int failed = 0;
        int total = 0;

        Trie trie =
                new Trie();

        trie.insert("car");
        trie.insert("card");
        trie.insert("care");
        trie.insert("career");
        trie.insert("cat");

        String[] validPrefixes = {
                "",
                "c",
                "ca",
                "car",
                "card",
                "care",
                "career",
                "cat"
        };

        for (String prefix : validPrefixes) {

            total++;

            try {

                assertTrue(
                        trie.isPrefix(prefix),
                        "P-" + prefix);

                passed++;

                System.out.printf(
                        "PASS P-%s - valid prefix%n",
                        prefix.isEmpty()
                                ? "<empty>"
                                : prefix);

            } catch (Exception ex) {

                failed++;

                System.out.printf(
                        "FAIL P-%s - valid prefix%n",
                        prefix);

                System.out.println(
                        "  exception = " + ex);
            }
        }

        String[] invalidPrefixes = {
                "b",
                "cb",
                "cars",
                "careful",
                "dog"
        };

        for (String prefix : invalidPrefixes) {

            total++;

            try {

                assertFalse(
                        trie.isPrefix(prefix),
                        "P-" + prefix);

                passed++;

                System.out.printf(
                        "PASS P-%s - invalid prefix%n",
                        prefix);

            } catch (Exception ex) {

                failed++;

                System.out.printf(
                        "FAIL P-%s - invalid prefix%n",
                        prefix);

                System.out.println(
                        "  exception = " + ex);
            }
        }

        printResults(
                passed,
                failed,
                total);
    }

    /* **********************************************************************
     * 5. Search Tests
     * **********************************************************************/

    static void runSearchTests() {

        System.out.println(
                "======================================================");

        System.out.println(
                "Search Tests");

        System.out.println(
                "======================================================");

        Trie trie =
                new Trie();

        String[] words = {
                "a",
                "app",
                "apple",
                "application",
                "banana",
                "band",
                "bandana",
                "cat"
        };

        for (String word : words) {
            trie.insert(word);
        }

        int passed = 0;
        int failed = 0;
        int total = 0;

        for (String word : words) {

            total++;

            try {

                assertTrue(
                        trie.search(word),
                        word);

                passed++;

                System.out.printf(
                        "PASS S-%s - word exists%n",
                        word);

            } catch (Exception ex) {

                failed++;

                System.out.printf(
                        "FAIL S-%s - word exists%n",
                        word);

                System.out.println(
                        "  exception = " + ex);
            }
        }

        String[] missingWords = {
                "ap",
                "apples",
                "applications",
                "ban",
                "bandanas",
                "car",
                "dog",
                "cater"
        };

        for (String word : missingWords) {

            total++;

            try {

                assertFalse(
                        trie.search(word),
                        word);

                passed++;

                System.out.printf(
                        "PASS S-%s - word missing%n",
                        word);

            } catch (Exception ex) {

                failed++;

                System.out.printf(
                        "FAIL S-%s - word missing%n",
                        word);

                System.out.println(
                        "  exception = " + ex);
            }
        }

        printResults(
                passed,
                failed,
                total);
    }

    /* **********************************************************************
     * 6. Randomised Tests
     * **********************************************************************/

    /**
     * Randomised cross-check.
     *
     * A java.util.List is used as the reference implementation.
     *
     * For every randomly generated word:
     *
     *      insert into Trie
     *      insert into reference list
     *
     * Then:
     *
     *      search(word)
     *
     * is compared with:
     *
     *      reference.contains(word)
     *
     * Prefix checks are compared against whether at least one
     * reference word starts with that prefix.
     */
    static void runRandomisedTests(
            int iterations) {

        System.out.println(
                "======================================================");

        System.out.println(
                "Randomised Cross Checks");

        System.out.println(
                "======================================================");

        Random random =
                new Random(20260919L);

        Trie trie =
                new Trie();

        List<String> reference =
                new ArrayList<>();

        int passed = 0;
        int failed = 0;

        /*
         * Generate and insert random words.
         */
        for (int i = 0;
             i < iterations;
             i++) {

            String word =
                    randomWord(
                            random,
                            1,
                            10);

            trie.insert(word);

            if (!reference.contains(word)) {
                reference.add(word);
            }

            boolean trieResult =
                    trie.search(word);

            boolean referenceResult =
                    reference.contains(word);

            if (trieResult == referenceResult) {

                passed++;

            } else {

                failed++;

                System.out.println(
                        "FAIL R-" + i
                                + " - search mismatch");

                System.out.println(
                        "  word = " + word);

                System.out.println(
                        "  trie = " + trieResult);

                System.out.println(
                        "  reference = "
                                + referenceResult);
            }
        }

        /*
         * Random search cross-check.
         */
        for (int i = 0;
             i < iterations;
             i++) {

            String word =
                    randomWord(
                            random,
                            1,
                            10);

            boolean trieResult =
                    trie.search(word);

            boolean referenceResult =
                    reference.contains(word);

            if (trieResult == referenceResult) {

                passed++;

            } else {

                failed++;

                System.out.println(
                        "FAIL R-" + i
                                + " - random search mismatch");

                System.out.println(
                        "  word = " + word);

                System.out.println(
                        "  trie = " + trieResult);

                System.out.println(
                        "  reference = "
                                + referenceResult);
            }
        }

        /*
         * Random prefix cross-check.
         */
        for (int i = 0;
             i < iterations;
             i++) {

            String prefix =
                    randomWord(
                            random,
                            0,
                            5);

            boolean trieResult =
                    trie.isPrefix(prefix);

            boolean referenceResult =
                    containsPrefix(
                            reference,
                            prefix);

            if (trieResult == referenceResult) {

                passed++;

            } else {

                failed++;

                System.out.println(
                        "FAIL R-" + i
                                + " - prefix mismatch");

                System.out.println(
                        "  prefix = "
                                + prefix);

                System.out.println(
                        "  trie = "
                                + trieResult);

                System.out.println(
                        "  reference = "
                                + referenceResult);
            }
        }

        System.out.println();

        System.out.printf(
                "Randomised tests: %d passed, %d failed%n",
                passed,
                failed);

        System.out.println();
    }

    /* **********************************************************************
     * Random Word Generator
     * **********************************************************************/

    static String randomWord(
            Random random,
            int minLength,
            int maxLength) {

        int length =
                minLength
                        + random.nextInt(
                        maxLength - minLength + 1);

        StringBuilder word =
                new StringBuilder(length);

        for (int i = 0;
             i < length;
             i++) {

            char c =
                    (char) (
                            'a'
                                    + random.nextInt(
                                    ALPHABET_SIZE));

            word.append(c);
        }

        return word.toString();
    }

    /* **********************************************************************
     * Reference Prefix Search
     * **********************************************************************/

    static boolean containsPrefix(
            List<String> words,
            String prefix) {

        for (String word : words) {

            if (word.startsWith(prefix)) {
                return true;
            }
        }

        return false;
    }

    /* **********************************************************************
     * Test Result Helper
     * **********************************************************************/

    static void printResults(
            int passed,
            int failed,
            int total) {

        System.out.println();

        System.out.printf(
                "Results: %d passed, %d failed, %d total%n",
                passed,
                failed,
                total);

        System.out.println();
    }

    /* **********************************************************************
     * Main Test Suite
     * **********************************************************************/

    public static void main(String[] args) {

        System.out.println(
                "############################################################");

        System.out.println(
                "########################  TRIE  ############################");

        System.out.println(
                "############################################################");

        System.out.println();

        /*
         * ============================================================
         * Basic Tests
         * ============================================================
         */

        runBasicTests();

        /*
         * ============================================================
         * Edge Cases
         * ============================================================
         */

        runEdgeCaseTests();

        /*
         * ============================================================
         * Invalid Inputs
         * ============================================================
         */

        runInvalidInputTests();

        /*
         * ============================================================
         * Prefix Relationships
         * ============================================================
         */

        runPrefixTests();

        /*
         * ============================================================
         * Search
         * ============================================================
         */

        runSearchTests();

        /*
         * ============================================================
         * Randomised Cross Checks
         * ============================================================
         */

        runRandomisedTests(5000);

        /*
         * ============================================================
         * Complete
         * ============================================================
         */

        System.out.println(
                "############################################################");

        System.out.println(
                "###################  TEST SUITE COMPLETE  ##################");

        System.out.println(
                "############################################################");
    }
}
