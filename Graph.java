/* Defines a Graph class with vertices and edges.

    A Graph contains the following properties:
        - An ArrayList of strings representing the names of vertices in the graph.
        - An ArrayList of Edge objects representing the edges in the graph.

    A Graph contains the following methods:
        - addVertex: Adds a new vertex to the graph if it does not already exist.
        - addEdge: Adds a new edge to the graph (represented by an Edge object). 

*/

import java.util.ArrayList;

public class Graph {
    ArrayList<String> vertices;
    ArrayList<Edge> edges;

    public Graph() {
        vertices = new ArrayList<>();
        edges = new ArrayList<>();
    }

    public void addVertex(String vertexName) {
        if (!vertices.contains(vertexName)) {
            vertices.add(vertexName);
        }
    }

    public void addEdge(String start, String end, int weight) {
        edges.add(new Edge(start, end, weight));
    }
}