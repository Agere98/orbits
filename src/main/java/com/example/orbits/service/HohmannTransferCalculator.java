package com.example.orbits.service;

import com.example.orbits.model.Orbit;
import lombok.Getter;

/**
 * HohmannTransferCalculator is a class which allows to calculate parameters of Hohmann transfer orbital maneuvers.
 * When both the starting and destination orbits are provided, {@link #calculate} method can be used
 * to obtain and store the results in an instance of this class.
 * <p>
 * The starting orbit's primary body must be the same as the destination orbit's primary body,
 * or the starting orbit's primary body must orbit the same body as the destination orbit's primary body
 * (for example: two orbits around different planets that orbit the same star).
 * If this condition is not met, calculations cannot be performed.
 */
public class HohmannTransferCalculator {

    /**
     * The starting orbit of the Hohmann transfer to calculate.
     */
    @Getter
    private Orbit startingOrbit;

    /**
     * The destination orbit of the Hohmann transfer to calculate.
     */
    @Getter
    private Orbit destinationOrbit;

    private double transferTime;
    private double insertionDeltaV;
    private double arrivalDeltaV;

    /**
     * Creates a calculator object with no data.
     * To perform calculations, the input data must be set.
     */
    public HohmannTransferCalculator() {
    }

    /**
     * Creates a calculator object and initializes the input data.
     *
     * @param startingOrbit    the starting orbit for the Hohmann transfer calculation.
     * @param destinationOrbit the destination orbit for the Hohmann transfer calculation.
     * @see #setStartingOrbit
     * @see #setDestinationOrbit
     */
    public HohmannTransferCalculator(Orbit startingOrbit, Orbit destinationOrbit) {
        this();
        this.startingOrbit = startingOrbit;
        this.destinationOrbit = destinationOrbit;
    }

    /**
     * Sets the starting orbit for the Hohmann transfer calculation.
     * The starting orbit's primary body must be the same as the destination orbit's primary body,
     * or the starting orbit's primary body must orbit the same body as the destination orbit's primary body
     * (for example: two orbits around different planets that orbit the same star).
     * If this condition is not met, calculations cannot be performed.
     *
     * @param startingOrbit the new starting orbit data.
     */
    public void setStartingOrbit(Orbit startingOrbit) {
        this.startingOrbit = startingOrbit;
    }

    /**
     * Sets the destination orbit for the Hohmann transfer calculation.
     * The destination orbit's primary body must be the same as the starting orbit's primary body,
     * or the destination orbit's primary body must orbit the same body as the starting orbit's primary body
     * (for example: two orbits around different planets that orbit the same star).
     * If this condition is not met, calculations cannot be performed.
     *
     * @param destinationOrbit the new destination orbit data.
     */
    public void setDestinationOrbit(Orbit destinationOrbit) {
        this.destinationOrbit = destinationOrbit;
    }

    /**
     * Returns the duration of the most recently calculated Hohmann transfer.
     *
     * @return Duration of the Hohmann transfer.
     */
    public double getTransferTime() {
        return transferTime;
    }

    /**
     * Returns the most recently calculated delta-V needed to enter the transfer orbit from the starting orbit.
     *
     * @return The delta-V needed to enter the transfer orbit.
     */
    public double getInsertionDeltaV() {
        return insertionDeltaV;
    }

    /**
     * Returns the most recently calculated delta-V needed to leave the transfer orbit to the destination orbit.
     *
     * @return The delta-V needed to leave the transfer orbit.
     */
    public double getArrivalDeltaV() {
        return arrivalDeltaV;
    }

    /**
     * Returns the total delta-V needed for the most recently calculated Hohmann transfer.
     * Specifically, returns the sum of the insertion delta-V and the arrival delta-V.
     *
     * @return The total delta-V needed for the Hohmann transfer.
     */
    public double getTotalDeltaV() {
        return insertionDeltaV + arrivalDeltaV;
    }

    /**
     * Calculates the parameters of the Hohmann transfer defined by starting and destination orbits.
     * All results can be then accessed from the respective getter methods until the next call of this method.
     *
     * @throws IllegalStateException if some of the input parameters have not been specified,
     *                               or if the conditions described in {@link #setStartingOrbit}
     *                               and {@link #setDestinationOrbit} are not met.
     */
    public void calculate() {
        if (startingOrbit == null || destinationOrbit == null) {
            throw new IllegalStateException("Orbit data required for calculation has not been set");
        }

        if (startingOrbit.getPrimaryBody().equals(destinationOrbit.getPrimaryBody())) {
            calculateOrbitTransfer(startingOrbit, destinationOrbit);
        } else {
            throw new IllegalStateException("Orbit data required for calculation is not valid");
        }
    }

    private void calculateOrbitTransfer(Orbit startingOrbit, Orbit destinationOrbit) {
        double semiMajorAxis = (startingOrbit.getRadius() + destinationOrbit.getRadius()) / 2d;
        insertionDeltaV = startingOrbit.getOrbitalSpeed()
                * Math.abs(1d - Math.sqrt(destinationOrbit.getRadius() / semiMajorAxis));
        arrivalDeltaV = destinationOrbit.getOrbitalSpeed()
                * Math.abs(1d - Math.sqrt(startingOrbit.getRadius() / semiMajorAxis));
        transferTime = Math.PI * Math.sqrt(Math.pow(semiMajorAxis, 3d) / startingOrbit.getStandardGravitationalParameter());
    }
}
