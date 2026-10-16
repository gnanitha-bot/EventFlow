package algorithms.m4;

import java.util.*;

/**
 * Dinic's Maximum Flow Algorithm.
 * Time Complexity: O(V^2 * E)
 *
 * Uses BFS to construct a Level Graph, and DFS with pointer (ptr) optimization
 * to push blocking flows along shortest paths in the residual network.
 */
public class Dinic {

    /**
     * Graph edge with capacity, flow, and reverse edge reference.
     */
    public static class Edge {
        public int to;
        public int rev;   // Index of reverse edge in graph.get(to)
        public int cap;   // Total capacity
        public int flow;  // Current flow

        public Edge(int to, int rev, int cap) {
            this.to = to;
            this.rev = rev;
            this.cap = cap;
            this.flow = 0;
        }

        public int residualCapacity() {
            return cap - flow;
        }
    }

    private int numNodes;
    private List<List<Edge>> graph;
    private int[] level;
    private int[] ptr;

    public Dinic(int numNodes) {
        this.numNodes = numNodes;
        this.graph = new ArrayList<>();
        for (int i = 0; i < numNodes; i++) {
            graph.add(new ArrayList<>());
        }
        this.level = new int[numNodes];
        this.ptr = new int[numNodes];
    }

    /**
     * Adds a directed edge with capacity to the flow network.
     * Also creates a reverse edge with 0 capacity in the residual graph.
     */
    public void addEdge(int from, int to, int capacity) {
        Edge a = new Edge(to, graph.get(to).size(), capacity);
        Edge b = new Edge(from, graph.get(from).size(), 0);
        graph.get(from).add(a);
        graph.get(to).add(b);
    }

    /**
     * BFS step: Constructs the Level Graph from source s.
     * Returns true if sink t is reachable in the residual graph.
     */
    private boolean bfs(int s, int t) {
        Arrays.fill(level, -1);
        level[s] = 0;
        Queue<Integer> queue = new LinkedList<>();
        queue.offer(s);

        while (!queue.isEmpty()) {
            int u = queue.poll();
            for (Edge e : graph.get(u)) {
                if (e.residualCapacity() > 0 && level[e.to] < 0) {
                    level[e.to] = level[u] + 1;
                    queue.offer(e.to);
                }
            }
        }
        return level[t] >= 0;
    }

    /**
     * DFS step: Pushes blocking flow along level graph edges (level[v] == level[u] + 1).
     */
    private int dfs(int u, int t, int pushed) {
        if (pushed == 0) return 0;
        if (u == t) return pushed;

        for (; ptr[u] < graph.get(u).size(); ptr[u]++) {
            Edge e = graph.get(u).get(ptr[u]);
            int v = e.to;

            if (level[v] != level[u] + 1 || e.residualCapacity() <= 0) {
                continue;
            }

            int tr = dfs(v, t, Math.min(pushed, e.residualCapacity()));
            if (tr == 0) continue;

            e.flow += tr;
            graph.get(v).get(e.rev).flow -= tr;
            return tr;
        }

        return 0;
    }

    /**
     * Computes maximum flow from source s to sink t using Dinic's Algorithm.
     */
    public int maxFlow(int s, int t) {
        int flow = 0;
        while (bfs(s, t)) {
            Arrays.fill(ptr, 0);
            while (true) {
                int pushed = dfs(s, t, Integer.MAX_VALUE);
                if (pushed == 0) break;
                flow += pushed;
            }
        }
        return flow;
    }

    public List<List<Edge>> getGraph() {
        return graph;
    }
}
