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

    private final String name;
    private final double mass;

    /**
     * Orbit associated with this body.
     */
    private Orbit orbit;

    /**
     * Creates an object containing celestial body data.
     *
     * @param name the name of this body.
     * @param mass the mass of this body (in kilograms), must be greater than 0.
     */
    public CelestialBody(String name, double mass) {
        if (mass <= 0d) {
            throw new IllegalArgumentException("Mass must be greater than 0");
        }
        this.name = name;
        this.mass = mass;
    }

    /**
     * Calculates the standard gravitational parameter (mu) of this body.
     *
     * @return The standard gravitational parameter of this body.
     */
    public double getStandardGravitationalParameter() {
        return G * mass;
    }
}
