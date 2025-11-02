package graph;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TaskLoader {

    public static Graph loadGraph(String filepath) throws IOException {
        String content = Files.readString(Paths.get(filepath));
        JSONObject json = new JSONObject(content);

        int n = json.getInt("n");
        Graph graph = new Graph(n);

        JSONArray edges = json.getJSONArray("edges");
        for (int i = 0; i < edges.length(); i++) {
            JSONObject e = edges.getJSONObject(i);
            int u = e.getInt("u");
            int v = e.getInt("v");
            int w = e.getInt("w");
            graph.addEdge(u, v, w);
        }

        return graph;
    }
}