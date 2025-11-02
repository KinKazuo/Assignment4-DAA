package graph;

import java.util.*;

public class Graph {
    private final int n;
    private final List<List<Edge>> adj;

    public static class Edge {
        public final int u, v, w;

        public Edge(int u, int v, int w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }

        @Override
        public String toString() {
            return String.format("%d -> %d (%d)", u, v, w);
        }
    }

    public Graph(int n) {
        this.n = n;
        this.adj = new ArrayList<>(n);
        for (int i = 0; i < n; i++) {
            adj.add(new ArrayList<>());
        }
    }

    public void addEdge(int u, int v, int w) {
        adj.get(u).add(new Edge(u, v, w));
    }

    public List<Edge> getEdgesFrom(int u) {
        return adj.get(u);
    }

    public int getN() {
        return n;
    }

    public List<Integer> getVertices() {
        List<Integer> vertices = new ArrayList<>();
        for (int i = 0; i < n; i++) vertices.add(i);
        return vertices;
    }

    public List<Edge> getAllEdges() {
        List<Edge> edges = new ArrayList<>();
        for (List<Edge> list : adj) {
            edges.addAll(list);
        }
        return edges;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Graph{\n");
        for (int u = 0; u < n; u++) {
            for (Edge e : adj.get(u)) {
                sb.append("  ").append(e).append("\n");
            }
        }
        sb.append("}");
        return sb.toString();
    }
}