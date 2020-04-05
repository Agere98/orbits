package com.example.orbits.model;

import lombok.Data;
import lombok.NonNull;

/**
 * A simple data class for modeling orbits around celestial bodies.
 */
@Data
public class Orbit {

    private final double radius;
    private final CelestialBody primaryBody;

    /**
     * Creates an object containing orbit data.
     *
     * @param radius      the radius of this orbit (in meters), must be greater than 0.
     * @param primaryBody the physical body around which this orbit is located.
     */
    public Orbit(double radius, @NonNull CelestialBody primaryBody) {
        if (radius <= 0d) {
            throw new IllegalArgumentException("Orbit radius must be greater than 0");
        }
        this.radius = radius;
        this.primaryBody = primaryBody;
    }

    /**
     * Returns the standard gravitational parameter (mu) of this orbit's primary body.
     *
     * @return The standard gravitational parameter of this orbit's primary body.
     */
    public double getStandardGravitationalParameter() {
        return primaryBody.getStandardGravitationalParameter();
    }

    /**
     * Calculates the orbital speed associated with this orbit.
     *
     * @return The orbital speed associated with this orbit.
     */
    public double getOrbitalSpeed() {
        return Math.sqrt(getStandardGravitationalParameter() / radius);
    }
}
