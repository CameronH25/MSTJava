/*  Defines an edge class. 

    An Edge has the following properties:
        - start vertex (string)
        - end vertex (string)
        - weight (int)

    An Edge contains the following methods: 
        - Edge, which is passed in the aforementioned 
        properties and sets each passed in val
*/

public class Edge {
    String start;
    String end;
    int weight;

    public Edge(String start, String end, int weight) {
        this.start = start;
        this.end = end;
        this.weight = weight;
    }
}