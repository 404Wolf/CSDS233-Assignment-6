package org.main;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AirportSystemTest {
    AirportSystem makeTestSystemA() {
        AirportSystem system = new AirportSystem();
        assertTrue(system.addEdge("Chicago", "Detroit", 281));
        assertTrue(system.addEdge("Chicago", "Toledo", 244));
        assertTrue(system.addEdge("Chicago", "Indianapolis", 181));
        assertTrue(system.addEdge("Detroit", "Toledo", 60));
        assertTrue(system.addEdge("Indianapolis", "Cincinnati", 110));
        assertTrue(system.addEdge("Cincinnati", "Toledo", 198));
        assertTrue(system.addEdge("Cincinnati", "Columbus", 101));
        assertTrue(system.addEdge("Columbus", "Cleveland", 143));
        assertTrue(system.addEdge("Toledo", "Cleveland", 117));
        assertTrue(system.addEdge("Columbus", "Pittsburgh", 185));
        assertTrue(system.addEdge("Cleveland", "Buffalo", 191));
        assertTrue(system.addEdge("Pittsburgh", "Buffalo", 216));
        assertTrue(system.addEdge("Pittsburgh", "Cleveland", 135));
        return system;
    }

    AirportSystem makeTestSystemB () {
        AirportSystem system = new AirportSystem();
        system.addEdge("1", "5", 4);
        system.addEdge("1", "2", 2);
        system.addEdge("1", "4", 1);
        system.addEdge("5", "4", 9);
        system.addEdge("4", "3", 5);
        system.addEdge("2", "6", 7);
        system.addEdge("6", "3", 8);
        system.addEdge("2", "4", 3);
        system.addEdge("2", "3", 3);
        return system;
    }

    @Test
    void makeSystem() {
        AirportSystem system = makeTestSystemA();
        system.printGraph();
    }

    @Test
    void shortestDistance() {
        AirportSystem system = makeTestSystemA();
        assertEquals(60, system.shortestDistance("Detroit", "Toledo"));
        assertEquals(60, system.shortestDistance("Toledo", "Detroit"));
        assertEquals(181, system.shortestDistance("Indianapolis", "Chicago"));
        assertEquals(0, system.shortestDistance("Chicago", "Chicago"));
        assertEquals(334, system.shortestDistance("Columbus", "Buffalo"));
        assertEquals(368, system.shortestDistance("Detroit", "Indianapolis"));

        system = makeTestSystemB();
        assertEquals(13, system.shortestDistance("6", "5"));
    }

    @Test
    void breadthFirstSearch() {
        AirportSystem system = makeTestSystemA();
        List<String> searchListOrder = system.breadthFirstSearch("Buffalo");

        // Hand drawn graph with dijkstra's shows that this is a valid result, so this result is expected when we start
        // at buffalo
        assertEquals("Buffalo", searchListOrder.get(0));
        assertEquals("Cleveland", searchListOrder.get(1));
        assertEquals("Pittsburgh", searchListOrder.get(2));
        assertEquals("Columbus", searchListOrder.get(3));
        assertEquals("Toledo", searchListOrder.get(4));
        assertEquals("Cincinnati", searchListOrder.get(5));
        assertEquals("Chicago", searchListOrder.get(6));
        assertEquals("Detroit", searchListOrder.get(7));
        assertEquals("Indianapolis", searchListOrder.get(8));

        system = makeTestSystemB();
        searchListOrder = system.breadthFirstSearch("1");

        // Assert all the possible combinations of ordering for the BFS
        // Some of the orderings are dependent on the order within the linked lists, so there is not a specific
        // correct output.

        assertEquals("1", searchListOrder.get(0));

        assertTrue(searchListOrder.get(1).equals("5")
                || searchListOrder.get(1).equals("4")
                || searchListOrder.get(1).equals("2")
        );
        assertTrue(searchListOrder.get(2).equals("5")
                || searchListOrder.get(2).equals("4")
                || searchListOrder.get(2).equals("2")
        );
        assertNotEquals(searchListOrder.get(1), searchListOrder.get(2));
        assertTrue(searchListOrder.get(2).equals("5")
                || searchListOrder.get(2).equals("4")
                || searchListOrder.get(2).equals("2")
        );
        assertNotEquals(searchListOrder.get(1), searchListOrder.get(3));
        assertNotEquals(searchListOrder.get(2), searchListOrder.get(3));
        assertTrue(searchListOrder.get(3).equals("6")
                || searchListOrder.get(3).equals("4")
                || searchListOrder.get(3).equals("2")
        );
        assertTrue(searchListOrder.get(4).equals("6")
                || searchListOrder.get(4).equals("3")
        );
        assertNotEquals(searchListOrder.get(4), searchListOrder.get(5));
        assertTrue(searchListOrder.get(5).equals("6")
                || searchListOrder.get(5).equals("3")
        );

        // For the final test simply assert that there is the correct number of elements in the breadth first search
        // result
        searchListOrder = system.breadthFirstSearch("2");
        assertEquals(system.size(), searchListOrder.size());
    }

    @Test
    void minimumSpanningTree() {
        AirportSystem system = makeTestSystemA();
        List<AirportSystem.Edge> minimumSpanningTree;

        // A minimum spanning tree has the minimal total edge length, so that is the factor that we will use for the
        // first test. Since a Minimum spanning tree should be the same regardless of the root. Test it for the first
        // tree at two different origins.
        minimumSpanningTree= system.minimumSpanningTree("Buffalo");
        minimumSpanningTree.forEach(System.out::println);
        assertEquals(1038, getTotalDistance(minimumSpanningTree));

        // The minimum spanning tree overall length should be the same minimal length for detroit.
        minimumSpanningTree = system.minimumSpanningTree("Detroit");
        minimumSpanningTree.forEach(System.out::println);
        assertEquals(1038, getTotalDistance(minimumSpanningTree));

        system = makeTestSystemB();
        minimumSpanningTree = system.minimumSpanningTree("5");
        minimumSpanningTree.forEach(System.out::println);
        assertEquals(17, getTotalDistance(minimumSpanningTree));

        minimumSpanningTree = system.minimumSpanningTree("1");
        minimumSpanningTree.forEach(System.out::println);
        assertEquals(17, getTotalDistance(minimumSpanningTree));


        minimumSpanningTree = system.minimumSpanningTree("6");
        minimumSpanningTree.forEach(System.out::println);
        assertEquals(17, getTotalDistance(minimumSpanningTree));
    }

    private int getTotalDistance (List<AirportSystem.Edge> edgeArr) {
        int totalPathLength = 0;
        for (AirportSystem.Edge edge : edgeArr) {
            totalPathLength += edge.getDistance();
        }
        return totalPathLength;
    }
}