package graph;

public class Metrics {
    private long startTime;
    private int dfsVisits = 0;
    private int edgeRelaxations = 0;
    private int queueOperations = 0; // для Kahn

    public void startTimer() {
        this.startTime = System.nanoTime();
    }

    public long getTimeMs() {
        return (System.nanoTime() - startTime) / 1_000_000;
    }

    public void incrementDfsVisits() { dfsVisits++; }
    public void incrementEdgeRelaxations() { edgeRelaxations++; }
    public void incrementQueueOperations() { queueOperations++; }

    public int getDfsVisits() { return dfsVisits; }
    public int getEdgeRelaxations() { return edgeRelaxations; }
    public int getQueueOperations() { return queueOperations; }

    public void reset() {
        this.dfsVisits = 0;
        this.edgeRelaxations = 0;
        this.queueOperations = 0;
        this.startTime = 0;
    }
}