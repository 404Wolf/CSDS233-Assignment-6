package org.main;

import java.util.*;

public class AirportSystem {
    private final Map<String, Vertex> connections = new HashMap<>();

    /**
     * The adjacency list of the cities. Each node is a city, and each connecting line indicates a flight between two
     * cities. The inner List<Edge> represents a vertex.
     *
     * @return The adjacency list of cities.
     */
    public List<Vertex> getConnections() {
        return connections.values().stream().toList();
    }

    /**
     * A specific airport connection.
     *
     * @return The connection with the requested name.
     */
    public Vertex getConnection(String connection) { return connections.get(connection); }

    /**
     * Obtain the number of vertexes in the Airport system.
     *
     * @return Number of vertexes in the airport system.
     */
    public int size () {
        return getConnections().size();
    }

    /**
     * Adds a new edge to the connections list.
     *
     * @param source The source fo the edge.
     * @param destination The destination of the edge.
     * @param weight The length of the edge.
     * @return Return false if the edge already exists or the weight is negative, or true if edge was successfully
     * added.
     */
    public boolean addEdge (String source, String destination, int weight) {
        if (weight < 0 || source.equals(destination))
            return false;

        Vertex sourceVertex;
        Vertex destinationVertex;

        // Use existing source vertex if it already exists, otherwise make a new one
        if (connections.containsKey(source))
            sourceVertex = connections.get(source);
        else
            sourceVertex = new Vertex(source);

        // Use existing destination vertex if it already exists, otherwise make a new one
        if (connections.containsKey(destination))
            destinationVertex = connections.get(destination);
        else
            destinationVertex = new Vertex(destination);
        
        // Check if the sourceVertex already has a connection to the destinationVertex
        for (Edge edge : sourceVertex.edges)
            if (edge.destination == destinationVertex)
                return false;

        // Add the vertexes to the main list of vertexes that we are keeping track of
        connections.put(sourceVertex.id, sourceVertex);
        connections.put(destinationVertex.id, destinationVertex);

        // Add the edges to the vertexes
        sourceVertex.addEdge(new Edge(sourceVertex, destinationVertex, weight));
        destinationVertex.addEdge(new Edge(destinationVertex, sourceVertex, weight));
        
        return true;
    }

    /**
     * Returns the shortest distance between city A and city B.
     *
     * @implNote Uses Dijkstra’s algorithm.
     * @param cityA The starting city.
     * @param cityB The destination city.
     * @return The shortest distance between the two.
     */
    public int shortestDistance(Vertex cityA, Vertex cityB) {
        if (cityA == cityB)
            return 0;

        int shortestDistSoFar = Integer.MAX_VALUE;

        Queue<Journey> pending = new PriorityQueue<>() {
            private final Set<Vertex> visited = new HashSet<>();

            { visited.add(cityA); }

            public boolean add(Journey journey) {
                if (journey == null)
                    return false;
                visited.add(journey.parentVertex());
                return super.add(journey);
            }

            @Override
            public Journey poll() {
                // Since we only want unvisited vertexes we keep polling until a vertex that is not in the visited set
                // has been found.
                Journey path = super.poll();

                // Keep polling until path with unvisited ending vertex is found.
                while (true) {
                    if (path == null) return null;
                    if (!visited.contains(path.endingVertex)) {
                        visited.add(path.endingVertex);
                        return path;
                    };
                    path = super.poll();
                }
            }
        };
        Journey newPath;
        for (Edge edge : cityA.edges) {
            newPath = new Journey(cityA, edge.destination, edge.distance);
            pending.add(newPath);
            if (newPath.pathDistance() < shortestDistSoFar && edge.destination == cityB)
                shortestDistSoFar = newPath.pathDistance();
        }

        Journey currentPath;
        // While there are still paths that we must explore
        while (!pending.isEmpty()) {
            // Pop the shortest path with a visitable end node off the heap of pending paths
            currentPath = pending.poll();
            if (currentPath == null)
                break;

            // Add all the edges of that path to the heap
            for (Edge radiatingEdge: currentPath.endingVertex.edges) {
                newPath = new Journey(
                        radiatingEdge.source,
                        radiatingEdge.destination,
                        currentPath.pathDistance() + radiatingEdge.distance
                );
                pending.add(newPath);
                if (radiatingEdge.destination == cityB)
                    shortestDistSoFar = Math.min(shortestDistSoFar, newPath.pathDistance());
            }
        }

        return shortestDistSoFar;
    }

    public int shortestDistance(String cityA, String cityB) {
        return shortestDistance(connections.get(cityA), connections.get(cityB));
    }

    /**
     * Obtain a minimum spanning tree of the AirportSystem.
     *
     * @implNote Uses Prime's algorithm.
     * @return Minimum spanning tree of the airport system.
     */
    public List<Edge> minimumSpanningTree(Vertex root) {
        List<Edge> spanningTree = new LinkedList<>(root.edges);

        Set<Vertex> radiatingVertexes = new HashSet<>();
        root.edges.forEach(edge -> radiatingVertexes.add(edge.destination));

        Journey optimalJourney = new Journey(null, null, Integer.MAX_VALUE);
        Edge optimalJourneyPath = null;
        while (spanningTree.size() != size()) {
            // Add the minimum from any edge distance vertex to the spanning tree
            for (Vertex radiatingVertex: radiatingVertexes)
                for (Edge candidateEdge: radiatingVertex.edges)
                    if (candidateEdge.distance < optimalJourney.pathDistance()) {
                        optimalJourney = new Journey(
                                candidateEdge.source,
                                candidateEdge.destination,
                                candidateEdge.distance
                        );
                        optimalJourneyPath = candidateEdge;
                    }

            assert optimalJourneyPath != null;
            spanningTree.add(optimalJourneyPath);
            optimalJourneyPath = null;
            optimalJourney = new Journey(null, null, Integer.MAX_VALUE);
        }

        return spanningTree;
    }

    public List<Edge> minimumSpanningTree(String root) {
        return minimumSpanningTree(connections.get(root));
    }

    public List<Edge> minimumSpanningTree() {
        return minimumSpanningTree(connections.values().iterator().next());
    }

    /**
     * This is assuming this start vertex exists.
     * Order of the cities in the same level does not matter.
     * The starting vertex must exist.
     *
     * @param start The root vertex to begin from.
     * @return A list of all the cities from the start using BFS.
     */
    public List<String> breadthFirstSearch(String start) {
        // Elements that are currently being searched through
        Queue<Vertex> pending = new LinkedList<>();
        pending.add(getVertex(start));

        // All the elements that are to be returned
        List<String> visitedIds = new LinkedList<>();
        visitedIds.add(getVertex(start).id);

        // Elements that have already been visited
        Set<Vertex> visited = new HashSet<>();

        while (!pending.isEmpty()) {
            // For each neighboring element to the vertex, add it to the queue to be processed next. Then process this
            // element and move on.
            for (Edge radiatingEdge: pending.poll().edges) {
                if (!visited.contains(radiatingEdge.destination)) {
                    pending.add(radiatingEdge.destination);
                    visited.add(radiatingEdge.source);
                    visitedIds.add(radiatingEdge.destination.id);
                }
            }
        }

        return visitedIds;
    }

    /**
     * Uses overridden toString() method to print out the graph.
     */
    public void printGraph() { System.out.println(this); }

    /**
     * Get a specific vertex given its string id.
     *
     * @param id The id of the vertex being fetched.
     * @return The corresponding Vertex instance.
     */
    private Vertex getVertex(String id) {
        return connections.get(id);
    }

    /**
     * Prints the graph in a readable format, and it is clear which edge belongs to which vertex.
     * Example:
     *      V: A | E: [A,B][A,D]
     *      V: B | E: [B,A][B,C]
     *      V: C | E: [C,B]
     *      V: D | E: [D,A]
     */
    @Override
    public String toString() {
        StringBuilder output = new StringBuilder();
        for (Vertex vertex : connections.values()) {
            output.append("V: ")
                    .append(vertex.id)
                    .append(" | E: ");

            for (Edge edge: vertex.edges)
                output.append("[")
                        .append(edge.source.id)
                        .append(", ")
                        .append(edge.destination.id)
                        .append("]");

            output.append("\n");
        }

        return output.toString();
    }

    private record Journey(
            Vertex parentVertex,
            Vertex endingVertex,
            int pathDistance
    ) implements Comparable<Journey> {
        @Override
        public int compareTo(Journey o) {
            return pathDistance() - o.pathDistance();
        }
    }
    
    public static class Vertex {
        private final String id;
        private final List<Edge> edges = new LinkedList<>();

        private Vertex(String id) {
            this.id = id;
        }

        private void addEdge(Edge edge) {
            edges.add(edge);
        }

        public List<Edge> getEdges() { return edges; }

        @Override
        public String toString() {
            return "Vertex{" +
                    "id='" + id + '\'' +
                    ", edges=" + edges +
                    '}';
        }
    }

    public static class Edge {
        /**
         * The starting location of this edge as a string.
         */
        private final Vertex source;

        /**
         * The end destination of this edge as a string.
         */
        private final Vertex destination;

        /**
         * The distance between start and destination.
         */
        private final int distance;

        private Edge(Vertex source, Vertex destination, int distance) {
            this.source = source;
            this.destination = destination;
            this.distance = distance;
        }

        public Vertex getSource() {
            return source;
        }

        public Vertex getDestination() {
            return destination;
        }

        public int getDistance() {
            return distance;
        }

        @Override
        public String toString() {
            return "Edge{" +
                    "source='" + source.id + '\'' +
                    ", destination='" + destination.id + '\'' +
                    ", distance=" + distance +
                    '}';
        }
    }
}