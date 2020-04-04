package com.example.orbits.service;

import com.example.orbits.model.CelestialBody;
import com.example.orbits.model.Orbit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HohmannTransferCalculatorTest {

    private HohmannTransferCalculator calculator;
    private Orbit orbit;

    @BeforeEach
    void setUp() {
        calculator = new HohmannTransferCalculator();
        CelestialBody primary = new CelestialBody("Sol", 1.988e30);
        orbit = new Orbit(1.496e11, primary);
    }

    @Test
    void testSetStartingOrbit() {
        calculator.setStartingOrbit(orbit);
        assertEquals(orbit, calculator.getStartingOrbit());
    }

    @Test
    void testSetDestinationOrbit() {
        calculator.setDestinationOrbit(orbit);
        assertEquals(orbit, calculator.getDestinationOrbit());
    }

    @Test
    void testCalculateWithNoData() {
        assertThrows(IllegalStateException.class, () -> calculator.calculate());
    }

    @Nested
    class LowerToHigherOrbitTransferTest {

        @BeforeEach
        void setUp() {
            Orbit destination = new Orbit(2.289e11, orbit.getPrimaryBody());
            calculator.setStartingOrbit(orbit);
            calculator.setDestinationOrbit(destination);
            calculator.calculate();
        }

        @Test
        void testOrbitTransferTotalTime() {
            double expected = 259.9d * 24d * 60d * 60d;
            assertEquals(expected, calculator.getTransferTime(), expected / 100d);
        }

        @Test
        void testOrbitTransferInsertionDeltaV() {
            assertEquals(2972d, calculator.getInsertionDeltaV(), 1d);
        }

        @Test
        void testOrbitTransferArrivalDeltaV() {
            assertEquals(2670d, calculator.getArrivalDeltaV(), 1d);
        }

        @Test
        void testOrbitTransferTotalDeltaV() {
            assertEquals(5642d, calculator.getTotalDeltaV(), 2d);
        }
    }

    @Nested
    class HigherToLowerOrbitTransferTest {

        @BeforeEach
        void setUp() {
            Orbit starting = new Orbit(2.289e11, orbit.getPrimaryBody());
            calculator.setStartingOrbit(starting);
            calculator.setDestinationOrbit(orbit);
            calculator.calculate();
        }

        @Test
        void testOrbitTransferTotalTime() {
            double expected = 259.9d * 24d * 60d * 60d;
            assertEquals(expected, calculator.getTransferTime(), expected / 100d);
        }

        @Test
        void testOrbitTransferInsertionDeltaV() {
            assertEquals(2670d, calculator.getInsertionDeltaV(), 1d);
        }

        @Test
        void testOrbitTransferArrivalDeltaV() {
            assertEquals(2972d, calculator.getArrivalDeltaV(), 1d);
        }

        @Test
        void testOrbitTransferTotalDeltaV() {
            assertEquals(5642d, calculator.getTotalDeltaV(), 2d);
        }
    }

    @Nested
    class InterplanetaryTransferTest {

        @BeforeEach
        void setUp() {
            Orbit destinationPlanetOrbit = new Orbit(2.289e11, orbit.getPrimaryBody());
            CelestialBody startingPlanet = new CelestialBody("Terra", 5.972e24);
            startingPlanet.setOrbit(orbit);
            Orbit startingOrbit = new Orbit(6.671e6, startingPlanet);
            CelestialBody destinationPlanet = new CelestialBody("Mars", 6.417e23);
            destinationPlanet.setOrbit(destinationPlanetOrbit);
            Orbit destinationOrbit = new Orbit(3.69e6, destinationPlanet);
            calculator.setStartingOrbit(startingOrbit);
            calculator.setDestinationOrbit(destinationOrbit);
            calculator.calculate();
        }

        @Test
        void testOrbitTransferTotalTime() {
            double expected = 259.9d * 24d * 60d * 60d;
            assertEquals(expected, calculator.getTransferTime(), expected / 100d);
        }

        @Test
        void testOrbitTransferInsertionDeltaV() {
            assertEquals(3598d, calculator.getInsertionDeltaV(), 1d);
        }

        @Test
        void testOrbitTransferArrivalDeltaV() {
            assertEquals(2102d, calculator.getArrivalDeltaV(), 1d);
        }

        @Test
        void testOrbitTransferTotalDeltaV() {
            assertEquals(5700d, calculator.getTotalDeltaV(), 2d);
        }
    }
}
