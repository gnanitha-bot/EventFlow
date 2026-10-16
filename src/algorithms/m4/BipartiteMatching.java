package algorithms.m4;

import java.util.*;

/**
 * Maximum Bipartite Matching Algorithm using Augmenting Paths (Kuhn's Algorithm / Hopcroft-Karp).
 * Time Complexity: O(V * E)
 *
 * Finds the maximum cardinality matching in a bipartite graph between Left Nodes (Volunteers)
 * and Right Nodes (Activities).
 */
public class BipartiteMatching {

    /**
     * Result container for Bipartite Matching results.
     */
    public static class MatchingResult {
        public int maxMatches;
        public Map<Integer, Integer> volunteerToActivityMap; // Left index -> Right index
        public Map<Integer, Integer> activityToVolunteerMap; // Right index -> Left index
        public List<Integer> unmatchedVolunteers;
        public List<Integer> unmatchedActivities;

        public MatchingResult(int maxMatches, Map<Integer, Integer> volunteerToActivityMap,
                              Map<Integer, Integer> activityToVolunteerMap,
                              List<Integer> unmatchedVolunteers, List<Integer> unmatchedActivities) {
            this.maxMatches = maxMatches;
            this.volunteerToActivityMap = volunteerToActivityMap;
            this.activityToVolunteerMap = activityToVolunteerMap;
            this.unmatchedVolunteers = unmatchedVolunteers;
            this.unmatchedActivities = unmatchedActivities;
        }
    }

    private int numLeft;  // Number of Volunteers
    private int numRight; // Number of Activities
    private List<List<Integer>> adj;
    private int[] matchR; // Right node -> Left node match (-1 if unmatched)
    private int[] matchL; // Left node -> Right node match (-1 if unmatched)
    private boolean[] visited;

    public BipartiteMatching(int numLeft, int numRight) {
        this.numLeft = numLeft;
        this.numRight = numRight;
        this.adj = new ArrayList<>();
        for (int i = 0; i < numLeft; i++) {
            adj.add(new ArrayList<>());
        }
        this.matchR = new int[numRight];
        Arrays.fill(matchR, -1);
        this.matchL = new int[numLeft];
        Arrays.fill(matchL, -1);
    }

    /**
     * Adds an edge indicating Volunteer (leftNode) is available/suitable for Activity (rightNode).
     */
    public void addEdge(int leftNode, int rightNode) {
        if (leftNode >= 0 && leftNode < numLeft && rightNode >= 0 && rightNode < numRight) {
            adj.get(leftNode).add(rightNode);
        }
    }

    /**
     * DFS augmenting path search from left node u.
     */
    private boolean dfs(int u) {
        for (int v : adj.get(u)) {
            if (!visited[v]) {
                visited[v] = true;
                // If right node v is unmatched or its matched partner has an augmenting path
                if (matchR[v] < 0 || dfs(matchR[v])) {
                    matchR[v] = u;
                    matchL[u] = v;
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Computes the maximum bipartite matching.
     */
    public MatchingResult computeMatching() {
        int maxMatches = 0;
        for (int u = 0; u < numLeft; u++) {
            visited = new boolean[numRight];
            if (dfs(u)) {
                maxMatches++;
            }
        }

        Map<Integer, Integer> v2a = new HashMap<>();
        Map<Integer, Integer> a2v = new HashMap<>();
        List<Integer> unmatchedV = new ArrayList<>();
        List<Integer> unmatchedA = new ArrayList<>();

        for (int u = 0; u < numLeft; u++) {
            if (matchL[u] != -1) {
                v2a.put(u, matchL[u]);
            } else {
                unmatchedV.add(u);
            }
        }

        for (int v = 0; v < numRight; v++) {
            if (matchR[v] != -1) {
                a2v.put(v, matchR[v]);
            } else {
                unmatchedA.add(v);
            }
        }

        return new MatchingResult(maxMatches, v2a, a2v, unmatchedV, unmatchedA);
    }
}
