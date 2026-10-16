package algorithms.m1;

import java.util.ArrayList;
import java.util.List;

/**
 * Knuth-Morris-Pratt (KMP) Pattern Matching Algorithm.
 * Performs fast O(N + M) substring search by precomputing the Longest Prefix Suffix (LPS) array.
 */
public class KMP {

    /**
     * Builds the Longest Prefix Suffix (LPS) array for the given pattern.
     * lps[i] stores the length of the longest proper prefix of pattern[0..i]
     * that is also a suffix of pattern[0..i].
     *
     * @param pattern Pattern string
     * @return int array containing LPS values
     */
    public static int[] buildLPS(String pattern) {
        if (pattern == null) {
            return new int[0];
        }
        int m = pattern.length();
        int[] lps = new int[m];
        if (m == 0) {
            return lps;
        }

        int len = 0;
        int i = 1;
        lps[0] = 0;

        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

    /**
     * Searches for all occurrences of pattern in text using the KMP algorithm.
     * Returns starting positions (0-based) of all exact pattern matches.
     * Does NOT use String.indexOf(), contains(), regex, or other built-in search methods.
     *
     * @param text Target text string
     * @param pattern Pattern to search for
     * @return List of starting index positions of all matches
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

        int[] lps = buildLPS(pattern);

        int i = 0; // index for text
        int j = 0; // index for pattern

        while (i < n) {
            if (pattern.charAt(j) == text.charAt(i)) {
                i++;
                j++;
            }

            if (j == m) {
                matches.add(i - j);
                j = lps[j - 1];
            } else if (i < n && pattern.charAt(j) != text.charAt(i)) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }

        return matches;
    }
}
