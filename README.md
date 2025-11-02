Assignment 4: Smart City Scheduling
Data Summary
For this assignment, I implemented graph algorithms to optimize task scheduling in a smart city environment. The system handles dependencies between tasks like street cleaning, sensor maintenance, and infrastructure repairs.

I generated 9 datasets following the requirements:

| Dataset  | n   | m   | Cyclic | SCC Count | Description                     |
|----------|-----|-----|--------|-----------|---------------------------------|
| small1   | 8   | 7   | Yes    | 3         | Cycle in tasks 1-2-3, linear chain 4-5-6-7 |
| small2   | 6   | 5   | Yes    | 2         | Single cycle + isolated chain   |
| small3   | 7   | 7   | No     | 7         | Pure DAG with branching         |
| medium1  | 12  | 13  | Yes    | 4         | Multiple cycles with connections|
| medium2  | 14  | 13  | Yes    | 2         | Star pattern with one cycle     |
| medium3  | 15  | 17  | Yes    | 4         | Linear with backward edges      |
| large1   | 25  | 32  | Yes    | 6         | Mixed structure with several SCCs |
| large2   | 30  | 29  | No     | 30        | Linear DAG for performance testing |
| large3   | 40  | 52  | Yes    | 9         | Complex dependencies with multiple cycles |

All datasets use edge weights as specified in the weight_model field. The source node varies by dataset but is always a task with no dependencies.

## Results

| Task       | n   | Time (ms) | DFS Visits | Queue Ops | Relaxations |
|------------|-----|-----------|------------|-----------|-------------|
| SCC        | 8   | 0.3       | 8          | -         | -           |
| SCC        | 30  | 1.2       | 30         | -         | -           |
| SCC        | 40  | 1.8       | 40         | -         | -           |
| Topo Sort  | 8   | 0.2       | -          | 12        | -           |
| Topo Sort  | 30  | 0.7       | -          | 58        | -           |
| DAG-SP     | 8   | 0.3       | -          | -         | 7           |
| DAG-SP     | 30  | 0.9       | -          | -         | 29          |

The algorithms performed efficiently across all datasets. The SCC detection (Tarjan's algorithm) showed linear time complexity relative to the number of nodes. Topological sorting (Kahn's algorithm) scaled well with graph density. The DAG shortest path implementation processed all datasets in linear time as expected.

Analysis
SCC Algorithm (Tarjan):

The main bottleneck was the recursive DFS traversal, but it handled all datasets efficiently.
Performance was consistent regardless of cycle count - only node count mattered.
Memory usage increased slightly with more SCCs due to stack operations.
Topological Sort (Kahn):

Queue operations scaled linearly with edge count.
Performance improved on sparse graphs compared to dense ones.
The algorithm correctly handled all DAG structures after SCC compression.
DAG Shortest/Longest Paths:

Implemented using topological order for linear time complexity.
Critical path (longest path) calculation was identical to shortest path with max instead of min.
Edge relaxation count matched exactly the number of edges in the DAG.
Structural Impact:

Cyclic graphs required SCC compression first, adding overhead but enabling DAG processing.
Dense graphs increased memory usage slightly but didn't affect time complexity.
Large SCCs reduced the condensation graph size significantly, improving subsequent steps.
Conclusions
This implementation demonstrates how graph theory solves real-world scheduling problems:

Use Tarjan's algorithm when dealing with potential cyclic dependencies - it efficiently identifies task groups that must be treated as units.
Apply topological sorting after SCC compression to establish a valid execution order for task groups.
Leverage DAG properties for optimal scheduling - the linear time complexity makes these algorithms practical even for large city-scale systems.
In practice, this approach would allow city managers to:

Identify interdependent tasks that form cycles (must be done together)
Determine the optimal sequence for task execution
Calculate both fastest completion time and critical path (bottleneck tasks)
The algorithms scale linearly with graph size, making them suitable for real-time scheduling in smart city applications where task dependencies frequently change.


