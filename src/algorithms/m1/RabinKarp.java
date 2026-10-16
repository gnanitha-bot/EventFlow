package algorithms.m1;

import java.util.ArrayList;
import java.util.List;

/**
 * Rabin-Karp Rolling Hash Pattern Matching Algorithm.
 * Time Complexity: Average O(N + M), Worst-case O(N * M) on hash collisions.
 * Space Complexity: O(1) auxiliary space.
 */
public class RabinKarp {

    // Number of characters in the input alphabet (ASCII)
    private static final int ALPHABET_SIZE = 256;
    // Prime number for modulo arithmetic to prevent integer overflow and reduce hash collisions
    private static final long PRIME = 101;

    /**
     * Searches for all occurrences of pattern in text using the Rabin-Karp rolling hash algorithm.
     * Handles hash collisions by explicitly verifying matching characters one by one.
     * Does NOT use String.indexOf(), contains(), regex, or other built-in search methods.
     *
     * @param text Target text string
     * @param pattern Pattern to search for
     * @return List of starting index positions (0-based) of all matches
     */
    public static List<Integer> search(String text, String pattern) {
        List<Integer> matches = new ArrayList<>();
        if (text == null || pattern == null) {
            return matches;
        }

        int n = text.length();
        int m = pattern.length();

        if (m == 0 || n == 0 || m > n) {
            return matches;
        }

        long pHash = 0; // hash value for pattern
        long tHash = 0; // hash value for current window of text
        long h = 1;     // d^(m-1) % PRIME

        // Calculate h = pow(ALPHABET_SIZE, m-1) % PRIME
        for (int i = 0; i < m - 1; i++) {
            h = (h * ALPHABET_SIZE) % PRIME;
        }

        // Calculate initial hash value of pattern and first window of text
        for (int i = 0; i < m; i++) {
            pHash = (ALPHABET_SIZE * pHash + pattern.charAt(i)) % PRIME;
            tHash = (ALPHABET_SIZE * tHash + text.charAt(i)) % PRIME;
        }

        // Slide the pattern over text one character at a time
        for (int i = 0; i <= n - m; i++) {
            // Check hash match; verify character by character to handle collisions
            if (pHash == tHash) {
                boolean match = true;
                for (int j = 0; j < m; j++) {
                    if (text.charAt(i + j) != pattern.charAt(j)) {
                        match = false;
                        break;
                    }
                }
                if (match) {
                    matches.add(i);
                }
            }

            // Calculate rolling hash value for next window of text
            if (i < n - m) {
                tHash = (ALPHABET_SIZE * (tHash - text.charAt(i) * h) + text.charAt(i + m)) % PRIME;

                // Handle negative hash values
                if (tHash < 0) {
                    tHash = (tHash + PRIME) % PRIME;
                }
            }
        }

        return matches;
    }
}
