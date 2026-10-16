package algorithms.m3;

/**
 * Standard Levenshtein Edit-Distance Dynamic Programming Algorithm.
 * Calculates minimum insertions, deletions, and substitutions to transform str1 into str2.
 *
 * Time Complexity: O(M * N) where M and N are lengths of input strings.
 * Space Complexity: O(M * N)
 */
public class LevenshteinDistance {

    /**
     * Result container for Levenshtein Distance computation.
     */
    public static class LevenshteinResult {
        public int distance;
        public int insertions;
        public int deletions;
        public int substitutions;
        public double similarityPercentage;

        public LevenshteinResult(int distance, int insertions, int deletions, int substitutions, double similarityPercentage) {
            this.distance = distance;
            this.insertions = insertions;
            this.deletions = deletions;
            this.substitutions = substitutions;
            this.similarityPercentage = similarityPercentage;
        }
    }

    /**
     * Computes the Levenshtein edit distance between str1 and str2 using Dynamic Programming.
     * Backtracks to find exact breakdown of insertions, deletions, and substitutions.
     *
     * @param str1 Original reference string
     * @param str2 Candidate / target string
     * @return LevenshteinResult containing distance, operation breakdown, and similarity.
     */
    public static LevenshteinResult compute(String str1, String str2) {
        if (str1 == null) str1 = "";
        if (str2 == null) str2 = "";

        int m = str1.length();
        int n = str2.length();

        int[][] dp = new int[m + 1][n + 1];

        // Base cases
        for (int i = 0; i <= m; i++) {
            dp[i][0] = i; // Deletions from str1
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j] = j; // Insertions into str1
        }

        // DP Transitions
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    int insertCost = dp[i][j - 1] + 1;
                    int deleteCost = dp[i - 1][j] + 1;
                    int substituteCost = dp[i - 1][j - 1] + 1;

                    dp[i][j] = Math.min(insertCost, Math.min(deleteCost, substituteCost));
                }
            }
        }

        // Backtrack to count operations
        int i = m;
        int j = n;
        int insertions = 0;
        int deletions = 0;
        int substitutions = 0;

        while (i > 0 || j > 0) {
            if (i > 0 && j > 0 && str1.charAt(i - 1) == str2.charAt(j - 1)) {
                i--;
                j--;
            } else if (i > 0 && j > 0 && dp[i][j] == dp[i - 1][j - 1] + 1) {
                substitutions++;
                i--;
                j--;
            } else if (j > 0 && dp[i][j] == dp[i][j - 1] + 1) {
                insertions++;
                j--;
            } else if (i > 0 && dp[i][j] == dp[i - 1][j] + 1) {
                deletions++;
                i--;
            } else {
                if (i > 0) i--;
                else if (j > 0) j--;
            }
        }

        int maxLen = Math.max(m, n);
        double similarity = maxLen > 0 ? (1.0 - (double) dp[m][n] / maxLen) * 100.0 : 100.0;

        return new LevenshteinResult(dp[m][n], insertions, deletions, substitutions, Math.max(0.0, similarity));
    }
}
