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
        CelestialBody primary = new CelestialBody("Sol", 1.989e30);
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
            double expected = 258.86d * 24d * 60d * 60d;
            assertEquals(expected, calculator.getTransferTime(), expected / 100d);
        }

        @Test
        void testOrbitTransferInsertionDeltaV() {
            assertEquals(2900d, calculator.getInsertionDeltaV(), 100d);
        }

        @Test
        void testOrbitTransferArrivalDeltaV() {
            assertEquals(2700d, calculator.getArrivalDeltaV(), 100d);
        }

        @Test
        void testOrbitTransferTotalDeltaV() {
            assertEquals(5600d, calculator.getTotalDeltaV(), 200d);
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
            double expected = 258.86d * 24d * 60d * 60d;
            assertEquals(expected, calculator.getTransferTime(), expected / 100d);
        }

        @Test
        void testOrbitTransferInsertionDeltaV() {
            assertEquals(2700d, calculator.getInsertionDeltaV(), 100d);
        }

        @Test
        void testOrbitTransferArrivalDeltaV() {
            assertEquals(2900d, calculator.getArrivalDeltaV(), 100d);
        }

        @Test
        void testOrbitTransferTotalDeltaV() {
            assertEquals(5600d, calculator.getTotalDeltaV(), 200d);
        }
    }
}
