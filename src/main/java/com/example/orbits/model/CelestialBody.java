package com.example.orbits.model;

import lombok.Data;

/**
 * A simple data class for modeling celestial bodies.
 */
@Data
public class CelestialBody {

    /**
     * Newton's gravitational constant, in SI base units (m^3 * kg^-1 * s^-2).
     */
    private static final double G = 6.674e-11;

    /**
     * Name of this body.
     */
    private final String name;

    /**
     * Mass of this body, in kilograms.
     */
    private final double mass;

    /**
     * Orbit associated with this body.
     */
    private Orbit orbit;

    /**
     * Calculates the standard gravitational parameter (mu) of this body.
     *
     * @return The standard gravitational parameter of this body.
     */
    public double getStandardGravitationalParameter() {
        return G * mass;
    }
}
