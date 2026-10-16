package algorithms.m2;

/**
 * Longest Common Prefix (LCP) Construction using Kasai's Algorithm
 * and Common Substring Analysis between documents.
 *
 * Time Complexity: O(N) to build LCP array from Suffix Array.
 */
public class LCP {

    /**
     * Builds the LCP (Longest Common Prefix) array from text and its Suffix Array using Kasai's Algorithm.
     * lcp[i] stores the length of the longest common prefix between suffix sa[i] and suffix sa[i+1].
     *
     * @param text Input text string
     * @param sa Suffix array of text
     * @return int array lcp of size N
     */
    public static int[] buildLCP(String text, int[] sa) {
        if (text == null || sa == null || text.length() == 0 || sa.length == 0) {
            return new int[0];
        }

        int n = text.length();
        int[] lcp = new int[n];
        int[] rank = new int[n];

        // Compute rank array: rank[sa[i]] = i
        for (int i = 0; i < n; i++) {
            rank[sa[i]] = i;
        }

        int k = 0; // Length of current LCP

        // Traverse all suffixes in order of text index
        for (int i = 0; i < n; i++) {
            if (rank[i] == n - 1) {
                k = 0;
                continue;
            }

            // j is the starting index of the suffix adjacent to suffix i in the sorted Suffix Array
            int j = sa[rank[i] + 1];

            // Compare characters starting at i + k and j + k
            while (i + k < n && j + k < n && text.charAt(i + k) == text.charAt(j + k)) {
                k++;
            }

            lcp[rank[i]] = k;

            if (k > 0) {
                k--;
            }
        }

        return lcp;
    }

    /**
     * Data structure holding the result of Longest Common Substring analysis between two documents.
     */
    public static class CommonSubstringResult {
        public String longestCommonSubstring;
        public int length;
        public int docAIndex;
        public int docBIndex;
        public int maxLcpValue;
        public double averageLcp;
        public int totalLcpMatches;

        public CommonSubstringResult(String longestCommonSubstring, int length, int docAIndex, int docBIndex,
                                     int maxLcpValue, double averageLcp, int totalLcpMatches) {
            this.longestCommonSubstring = longestCommonSubstring;
            this.length = length;
            this.docAIndex = docAIndex;
            this.docBIndex = docBIndex;
            this.maxLcpValue = maxLcpValue;
            this.averageLcp = averageLcp;
            this.totalLcpMatches = totalLcpMatches;
        }
    }

    /**
     * Finds the Longest Common Substring between two document texts using
     * Generalized Suffix Array and Kasai's LCP algorithm.
     *
     * @param docA Text of Document A
     * @param docB Text of Document B
     * @return CommonSubstringResult containing the longest common substring, length, indices, and LCP stats.
     */
    public static CommonSubstringResult findLongestCommonSubstring(String docA, String docB) {
        if (docA == null) docA = "";
        if (docB == null) docB = "";

        if (docA.isEmpty() || docB.isEmpty()) {
            return new CommonSubstringResult("", 0, -1, -1, 0, 0.0, 0);
        }

        int lenA = docA.length();
        // Concatenate docA + '#' + docB + '$'
        String concat = docA + "#" + docB + "$";
        int totalLen = concat.length();

        // 1. Build Suffix Array for concatenated text
        int[] sa = SuffixArray.buildSuffixArray(concat);

        // 2. Build LCP array using Kasai's algorithm
        int[] lcp = buildLCP(concat, sa);

        int maxLen = 0;
        int maxIndex = -1;
        int docAIndex = -1;
        int docBIndex = -1;

        long sumLcp = 0;
        int countLcpPairs = 0;

        // 3. Find adjacent suffixes in SA belonging to different documents
        for (int i = 0; i < totalLen - 1; i++) {
            int sa1 = sa[i];
            int sa2 = sa[i + 1];

            // Determine document ownership: doc 1 if index < lenA, doc 2 if index > lenA
            boolean isDoc1Sa1 = sa1 < lenA;
            boolean isDoc1Sa2 = sa2 < lenA;
            boolean isDoc2Sa1 = sa1 > lenA && sa1 < totalLen - 1;
            boolean isDoc2Sa2 = sa2 > lenA && sa2 < totalLen - 1;

            if ((isDoc1Sa1 && isDoc2Sa2) || (isDoc2Sa1 && isDoc1Sa2)) {
                int docAStart = isDoc1Sa1 ? sa1 : sa2;
                int docBStart = (isDoc2Sa1 ? sa1 : sa2) - (lenA + 1);
                int maxValidLcp = Math.min(lenA - docAStart, docB.length() - docBStart);
                int currentLcp = Math.min(lcp[i], maxValidLcp);

                if (currentLcp > 0) {
                    sumLcp += currentLcp;
                    countLcpPairs++;
                }

                if (currentLcp > maxLen) {
                    maxLen = currentLcp;
                    maxIndex = docAStart;
                    docAIndex = docAStart;
                    docBIndex = docBStart;
                }
            }
        }

        String lcs = (maxLen > 0 && maxIndex != -1) ? concat.substring(maxIndex, maxIndex + maxLen) : "";
        double avgLcp = countLcpPairs > 0 ? (double) sumLcp / countLcpPairs : 0.0;

        return new CommonSubstringResult(lcs, maxLen, docAIndex, docBIndex, maxLen, avgLcp, countLcpPairs);
    }
}
