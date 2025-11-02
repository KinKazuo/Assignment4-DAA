package graph.dagsp;

import graph.Graph;
import graph.Metrics;

import java.util.*;

public class DAGShortestLongestPaths {
    private final Graph graph;
    private final List<Integer> topoOrder;
    private final int source;
    private final Metrics metrics;

    private final Map<Integer, Long> dist;
    private final Map<Integer, Long> longest;
    private final Map<Integer, Integer> prev;

    public DAGShortestLongestPaths(Graph graph, List<Integer> topoOrder, int source, Metrics metrics) {
        this.graph = graph;
        this.topoOrder = topoOrder;
        this.source = source;
        this.metrics = metrics;
        this.dist = new HashMap<>();
        this.longest = new HashMap<>();
        this.prev = new HashMap<>();
    }

    public void compute() {
        for (int v : graph.getVertices()) {
            dist.put(v, Long.MAX_VALUE);
            longest.put(v, Long.MIN_VALUE);
        }
        dist.put(source, 0L);
        longest.put(source, 0L);

        for (int u : topoOrder) {
            if (dist.get(u) != Long.MAX_VALUE) {
                for (Graph.Edge e : graph.getEdgesFrom(u)) {
                    long newDist = dist.get(u) + e.w;
                    if (newDist < dist.get(e.v)) {
                        dist.put(e.v, newDist);
                        prev.put(e.v, u);
                        metrics.incrementEdgeRelaxations();
                    }
                }
            }

            if (longest.get(u) != Long.MIN_VALUE) {
                for (Graph.Edge e : graph.getEdgesFrom(u)) {
                    long newLong = longest.get(u) + e.w;
                    if (newLong > longest.get(e.v)) {
                        longest.put(e.v, newLong);
                        metrics.incrementEdgeRelaxations();
                    }
                }
            }
        }
    }

    public long getShortestDistance(int v) {
        return 0;
    }

    public long getLongestDistance(int v) {
        return 0;
    }

    public List<Integer> reconstructPath(int farthest) {
        return List.of();
    }
}