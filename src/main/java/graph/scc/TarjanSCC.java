package graph.scc;

import graph.Graph;
import graph.Metrics;

import java.util.*;

/**
 * Finds Strongly Connected Components (SCCs) using Tarjan's algorithm.
 * Instruments DFS visits via Metrics.
 */
public class TarjanSCC {
    private final Graph graph;
    private final Metrics metrics; // <-- Добавлено

    private int index = 0;
    private final Stack<Integer> stack = new Stack<>();
    private final boolean[] onStack;
    private final int[] indices;
    private final int[] lowlink;
    private final List<List<Integer>> components = new ArrayList<>();

    public TarjanSCC(Graph graph, Metrics metrics) { // <-- Принимает Metrics
        this.graph = graph;
        this.metrics = metrics;
        int n = graph.getN();
        this.onStack = new boolean[n];
        this.indices = new int[n];
        this.lowlink = new int[n];
        Arrays.fill(indices, -1);
    }

    public List<List<Integer>> findSCCs() {
        for (int v : graph.getVertices()) {
            if (indices[v] == -1) {
                strongConnect(v);
            }
        }
        return new ArrayList<>(components);
    }

    private void strongConnect(int v) {
        // Увеличиваем счётчик при каждом заходе в DFS
        metrics.incrementDfsVisits();

        indices[v] = index;
        lowlink[v] = index;
        index++;
        stack.push(v);
        onStack[v] = true;

        for (Graph.Edge edge : graph.getEdgesFrom(v)) {
            int w = edge.v;

            if (indices[w] == -1) {
                strongConnect(w);
                lowlink[v] = Math.min(lowlink[v], lowlink[w]);
            } else if (onStack[w]) {
                lowlink[v] = Math.min(lowlink[v], indices[w]);
            }
        }

        if (lowlink[v] == indices[v]) {
            List<Integer> component = new ArrayList<>();
            int w;
            do {
                w = stack.pop();
                onStack[w] = false;
                component.add(w);
            } while (w != v);
            components.add(component);
        }
    }

    // Остальные методы без изменений...
    public List<Integer> getComponentSizes() {
        return components.stream().map(List::size).toList();
    }

    public Graph buildCondensationGraph() {
        List<List<Integer>> sccs = findSCCs();
        Map<Integer, Integer> vertexToComponent = new HashMap<>();
        for (int i = 0; i < sccs.size(); i++) {
            for (int v : sccs.get(i)) {
                vertexToComponent.put(v, i);
            }
        }

        Graph condensation = new Graph(sccs.size());
        Set<String> addedEdges = new HashSet<>();

        for (Graph.Edge e : graph.getAllEdges()) {
            int compU = vertexToComponent.get(e.u);
            int compV = vertexToComponent.get(e.v);
            if (compU != compV) {
                String edgeKey = compU + "->" + compV;
                if (!addedEdges.contains(edgeKey)) {
                    condensation.addEdge(compU, compV, 1);
                    addedEdges.add(edgeKey);
                }
            }
        }
        return condensation;
    }

    public List<List<Integer>> getComponents() {
        return new ArrayList<>(components);
    }
}