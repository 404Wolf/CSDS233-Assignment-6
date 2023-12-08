package org.main;

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

    @org.junit.jupiter.api.Test
    void makeSystem() {
        System.out.println(makeTestSystem());
    }

    @org.junit.jupiter.api.Test
    void shortestDistance() {
        assertEquals(60, makeTestSystem().shortestDistance("Detroit", "Toledo"));
        assertEquals(60, makeTestSystem().shortestDistance("Toledo", "Detroit"));
        assertEquals(181, makeTestSystem().shortestDistance("Indianapolis", "Chicago"));
        assertEquals(0, makeTestSystem().shortestDistance("Chicago", "Chicago"));
        assertEquals(334, makeTestSystem().shortestDistance("Columbus", "Buffalo"));
        assertEquals(368, makeTestSystem().shortestDistance("Detroit", "Indianapolis"));
    }


}