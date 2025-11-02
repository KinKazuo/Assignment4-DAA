package graph;

import graph.scc.TarjanSCC;
import graph.topo.KahnsTopoSort;
import graph.dagsp.DAGShortestLongestPaths;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        System.out.println(" Assignment 4: Smart City Scheduler");
        Metrics metrics = new Metrics();

        try {
            Graph graph = TaskLoader.loadGraph("data/tasks.json");
            System.out.println("\n Graph loaded excellent:");
            System.out.println(graph);

            metrics.startTimer();
            TarjanSCC tarjan = new TarjanSCC(graph, metrics);
            List<List<Integer>> components = tarjan.findSCCs();
            long sccTime = metrics.getTimeMs();
            int dfsVisits = metrics.getDfsVisits();

            System.out.println("\n Strongly Connected Components:");
            for (int i = 0; i < components.size(); i++) {
                System.out.printf("  Component %d: %s\n", i, components.get(i));
            }

            Graph condensation = tarjan.buildCondensationGraph();
            System.out.println("\n Condensation Graph:");
            System.out.println(condensation);

            // --- 1.2 Topo Sort ---
            KahnsTopoSort kahn = new KahnsTopoSort(condensation, metrics);
            List<Integer> topoOrder = kahn.getTopologicalOrder();
            System.out.println("\n Topological Order of Components:");
            System.out.println("  " + topoOrder);

            // --- 1.3 DAG Shortest & Longest Paths ---
            DAGShortestLongestPaths dagSP = new DAGShortestLongestPaths(graph, new ArrayList<>(graph.getVertices()), 4, metrics);
            dagSP.compute();

            System.out.println("\n Shortest Distances from source (node 4):");
            for (int v = 0; v < graph.getN(); v++) {
                long d = dagSP.getShortestDistance(v);
                System.out.printf("  4 -> %d: %s\n", v, d == Long.MAX_VALUE ? "∞" : d);
            }

            System.out.println("\n Longest Path Distances:");
            long maxLen = Long.MIN_VALUE;
            int farthest = -1;
            for (int v = 0; v < graph.getN(); v++) {
                long l = dagSP.getLongestDistance(v);
                if (l > maxLen) {
                    maxLen = l;
                    farthest = v;
                }
                System.out.printf("  4 -> %d: %s\n", v, l == Long.MIN_VALUE ? "-∞" : l);
            }

            if (farthest != -1) {
                List<Integer> criticalPath = dagSP.reconstructPath(farthest);
                System.out.println("\n Critical Path: " + criticalPath + " → Length: " + maxLen);
            }

            System.out.println("\n FINAL METRICS:");
            System.out.println("  SCC Time: " + sccTime + " ms | DFS Visits: " + dfsVisits);
            System.out.println("  Edge Relaxations: " + metrics.getEdgeRelaxations());
            System.out.println("  Kahn Queue Ops: " + metrics.getQueueOperations());

        } catch (Exception e) {
            System.err.println(" Error: " + e.getMessage());
        }
    }
}