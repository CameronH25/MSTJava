/* Professor Moorman | PPL Fall 2023
   Horine, Cameron | Java MST Assignment

   Takes in a weighted, undirected graph as well
   as a root vertex and prints out the MST along
   with the total weight. 

   Functions as intended. Tested with
   the test cases on the discussion board as well
   as with other edge cases. 

   However, modularity and abstraction suffer due
   to an idiotic eschewment of the Object-Oriented
   paradigm. Vertex should be a class. MST is a 
   poor name for a class. There is a great need
   for accessor functions to retrieve data so that
   data retrieval is not directly from an instantiated
   object. 

 */

import java.io.BufferedReader; // Ref 1. 
import java.io.FileReader; // Ref 2. 
import java.io.IOException; // Ref 3. 
import java.util.ArrayList;

public class MST {

    private ArrayList<Edge> mstEdges; 
    private int totalWeight; 

    public MST() {
        this.mstEdges = new ArrayList<>();
        this.totalWeight = 0;
    }

    // readGraphFromFile: Method to read in a graph from a file. Takes in a filename and 
    // parses the start vertex, end vertex, and weight. Takes this info 
    // and adds it to a graph. 
    public Graph readGraphFromFile(String filename) {
        Graph graph = new Graph();

        try (BufferedReader br = new BufferedReader(new FileReader(filename))) { // Ref 1. 
            String line;
            while ((line = br.readLine()) != null) { // Ref 4. 
                String[] parts = line.split(" "); // Ref 5.
                if (parts.length == 3) { 
                    String start = parts[0]; 
                    String end = parts[1]; 
                    int weight = Integer.parseInt(parts[2]); // Ref 6. 
                    graph.addVertex(start); 
                    graph.addVertex(end); 
                    graph.addEdge(start, end, weight); // Ref 7.
                }
            }
        } catch (IOException e) { 
            System.out.println("Error in reading in file.\n"); 
        }

        return graph;
    }

    // findMST: method to init and run MST algorithm
    public void findMST(Graph graph, String rootVertex) {

        ArrayList<String> Q = new ArrayList<>(graph.vertices);
        int[] key = new int[graph.vertices.size()];
        String[] parent = new String[graph.vertices.size()];

        for (int i = 0; i < graph.vertices.size(); i++) {
            key[i] = Integer.MAX_VALUE; 
            parent[i] = "nil";
        }

        int rootIndex = graph.vertices.indexOf(rootVertex); // Ref 9
        if (rootIndex == -1) {
            System.out.println("Error: Root vertex provided is not in the graph.\n");
            return;
        }
        key[rootIndex] = 0;

        while (!Q.isEmpty()) { // Ref 10. 
            int u = extractMin(Q, key, graph); 
            if (u == -1) {
                break;
            }
            updateKeys(Q, key, parent, graph, u); 
        }

        storeMST(parent, graph); 
    }

    // storeMST: Stores the results of the findMST function. Records the edges that 
    // make up the MST and calculate the total weight of the aforementioned edges. 
    private void storeMST(String[] parent, Graph graph) {
        mstEdges.clear(); // Ref 13. 
        totalWeight = 0;
        for (int i = 0; i < graph.vertices.size(); i++) {
            if (!parent[i].equals("nil")) {
                int weight = findEdgeWeight(parent[i], graph.vertices.get(i), graph);
                mstEdges.add(new Edge(parent[i], graph.vertices.get(i), weight)); // Ref 14. 
                totalWeight += weight;
            }
        }
    }

    // extractMin: Finds and removes the vertex with the minimum key value from Q. 
    private int extractMin(ArrayList<String> Q, int[] key, Graph graph) {
        int minIndex = -1;
        int minValue = Integer.MAX_VALUE;
    
        for (int i = 0; i < Q.size(); i++) {
            String vertex = Q.get(i); // Ref 8. 
            int vertexIndex = graph.vertices.indexOf(vertex); // Ref 9. 
            if (key[vertexIndex] < minValue) {
                minIndex = vertexIndex;
                minValue = key[vertexIndex];
            }
        }
    
        if (minIndex != -1) {
            Q.remove(graph.vertices.get(minIndex));
        }
    
        return minIndex;
    }
    
    // updateKeys: Updates the key vals & the parent vertex references for each vertex adjacent 
    // to a given vertex, 'u'. 
    private void updateKeys(ArrayList<String> Q, int[] key, String[] parent, Graph graph, int u) {
        String uVertex = graph.vertices.get(u);
    
        for (Edge edge : graph.edges) { 
            String vVertex;
            if (edge.start.equals(uVertex)) { // Ref 11. 
                vVertex = edge.end; 
            } else if (edge.end.equals(uVertex)) { 
                vVertex = edge.start; 
            } else {
                continue; 
            }
    
            int vIndex = graph.vertices.indexOf(vVertex); // Ref 9
    
            if (Q.contains(vVertex) && edge.weight < key[vIndex]) { // Ref 12
                key[vIndex] = edge.weight;
                parent[vIndex] = uVertex;
            }
        }
    }
    
    // findEdgeWeight: Helper method to find the weight of an edge given two vertices
    private int findEdgeWeight(String u, String v, Graph graph) {
        for (Edge edge : graph.edges) { 
            if ((edge.start.equals(u) && edge.end.equals(v)) || (edge.start.equals(v) && edge.end.equals(u))) { // Ref 11. 
                return edge.weight;
            }
        }
        return -1; 
    }

    // printMST: Prints out the MST. 
    public void printMST() {
        System.out.println("Edges in the MST:\n");
        for (Edge edge : mstEdges) {
            System.out.println(edge.start + " - " + edge.end + ": " + edge.weight);
        }
        System.out.println("Total weight of MST: " + totalWeight);
    }
    
    // main: Checks the amount of arguments given, initializes the filename 
    // and rootVertex, and calls functions. 
    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Not enough arguments.\n");
            return;
        }

        String filename = args[0];
        String rootVertex = args[1];

        MST mst = new MST(); 
        Graph graph = mst.readGraphFromFile(filename);
        mst.findMST(graph, rootVertex);
        mst.printMST(); 
    }
}

/*
References:

1) https://docs.oracle.com/javase/8/docs/api/java/io/BufferedReader.html
2) https://howtodoinjava.com/java/io/java-filereader/
3) https://stackoverflow.com/questions/5819121/understanding-java-ioexception
4) https://www.geeksforgeeks.org/console-readline-method-in-java-with-examples/
5) https://www.geeksforgeeks.org/split-string-java-examples/
6) https://www.tutorialspoint.com/java/number_parseint.htm
7) https://www.codecademy.com/learn/graph-data-structures-java/modules/graphs-java/cheatsheet
8) https://www.codecademy.com/resources/docs/java/array-list/get
9) https://www.w3schools.com/java/ref_string_indexof.asp
10) https://www.w3schools.com/java/ref_string_isempty.asp
11) https://www.w3schools.com/java/ref_string_equals.asp
12) https://www.w3schools.com/java/ref_string_contains.asp
13) https://www.geeksforgeeks.org/set-clear-method-in-java-with-examples/
14) https://www.scaler.com/topics/java-set-add/

 */
