package com.example.orbits.model;

import lombok.Data;
import lombok.NonNull;

/**
 * A simple data class for modeling orbits around celestial bodies.
 */
@Data
public class Orbit {
    /**
     * Radius of this orbit, in meters.
     */
    private final double radius;

    /**
     * The physical body around which this orbit is located.
     */
    @NonNull
    private final CelestialBody primaryBody;

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
