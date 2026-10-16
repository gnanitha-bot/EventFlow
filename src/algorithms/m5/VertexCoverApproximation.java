package algorithms.m5;

import java.util.*;

/**
 * Vertex Cover 2-Approximation Algorithm.
 *
 * Given an undirected graph where:
 *   - Vertices = Activities
 *   - Edges = Conflicts between activities (e.g., same venue / overlapping time)
 *
 * The algorithm finds a vertex cover (a subset S of vertices such that every edge
 * has at least one endpoint in S) with size ≤ 2 × OPT (guaranteed 2-approximation).
 *
 * Algorithm Steps:
 *   1. Start with empty cover C.
 *   2. Pick any uncovered edge (u, v).
 *   3. Add BOTH u and v to C.
 *   4. Mark all edges incident to u or v as covered.
 *   5. Repeat until all edges are covered.
 *
 * Time Complexity: O(V + E)
 */
public class VertexCoverApproximation {

    /**
     * Represents a conflict edge between two activity indices.
     */
    public static class ConflictEdge {
        public final int u;
        public final int v;
        public final String description;

        public ConflictEdge(int u, int v, String description) {
            this.u = u;
            this.v = v;
            this.description = description;
        }
    }

    /**
     * Result of Vertex Cover approximation.
     */
    public static class VertexCoverResult {
        public final Set<Integer> coverSet;       // Activity indices in the vertex cover
        public final List<ConflictEdge> allEdges;
        public final int totalEdges;
        public final int coverSize;

        public VertexCoverResult(Set<Integer> coverSet, List<ConflictEdge> allEdges) {
            this.coverSet = coverSet;
            this.allEdges = allEdges;
            this.totalEdges = allEdges.size();
            this.coverSize = coverSet.size();
        }

        /** Verifies that every edge has at least one endpoint in the cover. */
        public boolean isCoverValid() {
            for (ConflictEdge e : allEdges) {
                if (!coverSet.contains(e.u) && !coverSet.contains(e.v)) return false;
            }
            return true;
        }
    }

    private final int numVertices;
    private final List<ConflictEdge> edges;

    public VertexCoverApproximation(int numVertices) {
        this.numVertices = numVertices;
        this.edges = new ArrayList<>();
    }

    /**
     * Adds a conflict edge between activity u and activity v.
     */
    public void addConflictEdge(int u, int v, String description) {
        if (u != v && u >= 0 && u < numVertices && v >= 0 && v < numVertices) {
            edges.add(new ConflictEdge(u, v, description));
        }
    }

    /**
     * Runs the 2-Approximation Vertex Cover algorithm.
     *
     * @return VertexCoverResult containing the cover set and conflict metadata.
     */
    public VertexCoverResult computeVertexCover() {
        Set<Integer> cover = new LinkedHashSet<>();
        boolean[] edgeCovered = new boolean[edges.size()];

        // Process edges in order; pick any uncovered edge and add BOTH endpoints
        for (int i = 0; i < edges.size(); i++) {
            if (!edgeCovered[i]) {
                ConflictEdge e = edges.get(i);
                int u = e.u;
                int v = e.v;

                // Add both endpoints to cover (2-approximation core step)
                cover.add(u);
                cover.add(v);

                // Mark all edges incident to u or v as covered
                for (int j = 0; j < edges.size(); j++) {
                    ConflictEdge other = edges.get(j);
                    if (other.u == u || other.v == u || other.u == v || other.v == v) {
                        edgeCovered[j] = true;
                    }
                }
            }
        }

        return new VertexCoverResult(cover, edges);
    }

    public List<ConflictEdge> getEdges() {
        return edges;
    }
}
