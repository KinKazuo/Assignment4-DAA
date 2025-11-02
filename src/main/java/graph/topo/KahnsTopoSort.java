package graph.topo;

import graph.Graph;
import graph.Metrics;

import java.util.*;

public class KahnsTopoSort {
    private final Graph graph;
    private final int[] inDegree;
    private final List<Integer> topoOrder;
    private final Metrics metrics;

    public KahnsTopoSort(Graph graph, Metrics metrics) {
        this.graph = graph;
        this.metrics = metrics;
        this.inDegree = new int[graph.getN()];
        this.topoOrder = new ArrayList<>();

        for (int u : graph.getVertices()) {
            for (Graph.Edge e : graph.getEdgesFrom(u)) {
                inDegree[e.v]++;
            }
        }
    }

    public List<Integer> getTopologicalOrder() {
        Queue<Integer> queue = new LinkedList<>();

        for (int i = 0; i < inDegree.length; i++) {
            if (inDegree[i] == 0) {
                queue.offer(i);
                metrics.incrementQueueOperations();
            }
        }

        while (!queue.isEmpty()) {
            int u = queue.poll();
            metrics.incrementQueueOperations();
            topoOrder.add(u);

            for (Graph.Edge e : graph.getEdgesFrom(u)) {
                inDegree[e.v]--;
                if (inDegree[e.v] == 0) {
                    queue.offer(e.v);
                    metrics.incrementQueueOperations();
                }
            }
        }

        return new ArrayList<>(topoOrder);
    }
}