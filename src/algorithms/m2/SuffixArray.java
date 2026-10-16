package algorithms.m2;

import java.util.Arrays;

/**
 * Suffix Array Construction Algorithm using Prefix Doubling.
 * Time Complexity: O(N log^2 N)
 * Space Complexity: O(N)
 *
 * Constructs an array of integers representing 0-based starting indices
 * of all sorted suffixes of a given text.
 */
public class SuffixArray {

    /**
     * Helper class representing a suffix and its rank pair for sorting.
     */
    public static class Suffix implements Comparable<Suffix> {
        public int index;
        public int rank1;
        public int rank2;

        public Suffix(int index, int rank1, int rank2) {
            this.index = index;
            this.rank1 = rank1;
            this.rank2 = rank2;
        }

        @Override
        public int compareTo(Suffix other) {
            if (this.rank1 != other.rank1) {
                return Integer.compare(this.rank1, other.rank1);
            }
            return Integer.compare(this.rank2, other.rank2);
        }
    }

    /**
     * Builds the Suffix Array for a given input string using prefix doubling.
     * Does NOT use built-in suffix array library functions.
     *
     * @param text Input text string
     * @return Integer array sa where sa[i] is the starting index of the i-th lexicographically smallest suffix.
     */
    public static int[] buildSuffixArray(String text) {
        if (text == null || text.isEmpty()) {
            return new int[0];
        }

        int n = text.length();
        Integer[] suffixes = new Integer[n];
        int[] ranks = new int[n];
        int[] newRanks = new int[n];

        for (int i = 0; i < n; i++) {
            suffixes[i] = i;
            ranks[i] = text.charAt(i);
        }

        for (int k = 1; k < n; k *= 2) {
            final int offset = k;
            final int[] currentRanks = ranks;
            Arrays.sort(suffixes, (left, right) -> {
                int firstComparison = Integer.compare(currentRanks[left], currentRanks[right]);
                if (firstComparison != 0) {
                    return firstComparison;
                }

                int leftSecondRank = left + offset < n ? currentRanks[left + offset] : -1;
                int rightSecondRank = right + offset < n ? currentRanks[right + offset] : -1;
                return Integer.compare(leftSecondRank, rightSecondRank);
            });

            newRanks[suffixes[0]] = 0;
            for (int i = 1; i < n; i++) {
                int previous = suffixes[i - 1];
                int current = suffixes[i];
                int previousSecondRank = previous + k < n ? ranks[previous + k] : -1;
                int currentSecondRank = current + k < n ? ranks[current + k] : -1;

                boolean differentPair = ranks[previous] != ranks[current]
                        || previousSecondRank != currentSecondRank;
                newRanks[current] = newRanks[previous] + (differentPair ? 1 : 0);
            }

            System.arraycopy(newRanks, 0, ranks, 0, n);
            if (ranks[suffixes[n - 1]] == n - 1) {
                break;
            }
        }

        int[] sa = new int[n];
        for (int i = 0; i < n; i++) {
            sa[i] = suffixes[i];
        }

        return sa;
    }
}
