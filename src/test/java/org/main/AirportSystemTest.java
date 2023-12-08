package org.main;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AirportSystemTest {
    AirportSystem makeTestSystem() {
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

    @Test
    void makeSystem() {
        System.out.println(makeTestSystem());
    }

    @Test
    void shortestDistance() {
        assertEquals(60, makeTestSystem().shortestDistance("Detroit", "Toledo"));
        assertEquals(60, makeTestSystem().shortestDistance("Toledo", "Detroit"));
        assertEquals(181, makeTestSystem().shortestDistance("Indianapolis", "Chicago"));
        assertEquals(0, makeTestSystem().shortestDistance("Chicago", "Chicago"));
        assertEquals(334, makeTestSystem().shortestDistance("Columbus", "Buffalo"));
        assertEquals(368, makeTestSystem().shortestDistance("Detroit", "Indianapolis"));
        assertNotEquals(369, makeTestSystem().shortestDistance("Detroit", "Indianapolis"));
    }

    @Test
    void breadthFirstSearch() {
        AirportSystem system = makeTestSystem();
        List<String> searchListOrder = system.breadthFirstSearch("Buffalo");

        assertEquals("Buffalo", searchListOrder.get(0));
        assertEquals("Cleveland", searchListOrder.get(1));
        assertEquals("Pittsburgh", searchListOrder.get(2));
        assertEquals("Columbus", searchListOrder.get(3));
        assertEquals("Toledo", searchListOrder.get(4));
        assertEquals("Cincinnati", searchListOrder.get(5));
        assertEquals("Chicago", searchListOrder.get(6));
        assertEquals("Detroit", searchListOrder.get(7));
        assertEquals("Indianapolis", searchListOrder.get(8));
    }

    @Test
    void minimumSpanningTree() {
        AirportSystem system = makeTestSystem();
        List<AirportSystem.Edge> minimumSpanningTree = system.minimumSpanningTree("Buffalo");
        minimumSpanningTree.forEach(System.out::println);

        int totalPathLength = 0;
        for (AirportSystem.Edge edge : minimumSpanningTree) {
            totalPathLength += edge.getDistance();
        }
        assertEquals(1038, totalPathLength);
    }
}