package com.example.orbits.controller;

import com.example.orbits.model.CelestialBody;
import com.example.orbits.model.Orbit;
import com.example.orbits.service.HohmannTransferCalculator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * An API controller that enables access to a Hohmann transfer calculation service through HTTP requests.
 */
@RestController
public class HohmannTransferAPIController {

    private HohmannTransferCalculator calculator;

    public HohmannTransferAPIController(HohmannTransferCalculator calculator) {
        this.calculator = calculator;
    }

    @Data
    private static class HohmannTransferOutput {
        private final double transferTime;
        private final double insertionDeltaV;
        private final double arrivalDeltaV;
        private final double totalDeltaV;
    }

    @Data
    private static class SimpleHohmannTransferInput {
        private double primaryBodyMass;
        private double startingOrbitRadius;
        private double destinationOrbitRadius;
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    private static class InterplanetaryHohmannTransferInput extends SimpleHohmannTransferInput {
        private double startingPlanetOrbitRadius;
        private double startingPlanetMass;
        private double destinationPlanetOrbitRadius;
        private double destinationPlanetMass;
    }

    /**
     * Handles an HTTP POST request containing input data for Hohmann transfer calculation.
     * Generates a JSON response containing the results.
     * <p>
     * The input JSON must be provided in the following format:
     * <pre>
     * {@code
     * {
     *     "primaryBodyMass": Number,
     *     "startingOrbitRadius": Number,
     *     "destinationOrbitRadius": Number
     * }
     * }
     * </pre>
     * The response body has the following format:
     * <pre>
     * {@code
     * {
     *     "transferTime": Number,
     *     "insertionDeltaV": Number,
     *     "arrivalDeltaV": Number,
     *     "totalDeltaV": Number
     * }
     * }
     * </pre>
     * All input and output parameters are assumed to be expressed in base SI units.
     *
     * @param input input parameters of a Hohmann transfer.
     * @return Calculated parameters for the Hohmann transfer.
     */
    @PostMapping(path = "/simple", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HohmannTransferOutput simpleHohmannTransfer(@RequestBody SimpleHohmannTransferInput input) {
        var primaryBody = new CelestialBody(null, input.getPrimaryBodyMass());
        var startingOrbit = new Orbit(input.getStartingOrbitRadius(), primaryBody);
        var destinationOrbit = new Orbit(input.getDestinationOrbitRadius(), primaryBody);
        return getOutput(startingOrbit, destinationOrbit);
    }

    /**
     * Handles an HTTP POST request containing input data for interplanetary Hohmann transfer calculation.
     * Generates a JSON response containing the results.
     * <p>
     * The input JSON must be provided in the following format:
     * <pre>
     * {@code
     * {
     *     "primaryBodyMass": Number,
     *     "startingOrbitRadius": Number,
     *     "destinationOrbitRadius": Number,
     *     "startingPlanetMass": Number,
     *     "startingPlanetOrbitRadius": Number,
     *     "destinationPlanetMass": Number,
     *     "destinationPlanetOrbitRadius": Number
     * }
     * }
     * </pre>
     * The response body has the following format:
     * <pre>
     * {@code
     * {
     *     "transferTime": Number,
     *     "insertionDeltaV": Number,
     *     "arrivalDeltaV": Number,
     *     "totalDeltaV": Number
     * }
     * }
     * </pre>
     * All input and output parameters are assumed to be expressed in base SI units.
     *
     * @param input input parameters of an interplanetary Hohmann transfer.
     * @return Calculated parameters for the Hohmann transfer.
     */
    @PostMapping(path = "/interplanetary", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HohmannTransferOutput interplanetaryHohmannTransfer(@RequestBody InterplanetaryHohmannTransferInput input) {
        var primaryBody = new CelestialBody(null, input.getPrimaryBodyMass());
        var startingPlanet = new CelestialBody(null, input.getStartingPlanetMass());
        startingPlanet.setOrbit(new Orbit(input.getStartingPlanetOrbitRadius(), primaryBody));
        var destinationPlanet = new CelestialBody(null, input.getDestinationPlanetMass());
        destinationPlanet.setOrbit(new Orbit(input.getDestinationPlanetOrbitRadius(), primaryBody));
        var startingOrbit = new Orbit(input.getStartingOrbitRadius(), startingPlanet);
        var destinationOrbit = new Orbit(input.getDestinationOrbitRadius(), destinationPlanet);
        return getOutput(startingOrbit, destinationOrbit);
    }

    private HohmannTransferOutput getOutput(Orbit startingOrbit, Orbit destinationOrbit) {
        calculator.setStartingOrbit(startingOrbit);
        calculator.setDestinationOrbit(destinationOrbit);
        calculator.calculate();
        return new HohmannTransferOutput(
                calculator.getTransferTime(),
                calculator.getInsertionDeltaV(),
                calculator.getArrivalDeltaV(),
                calculator.getTotalDeltaV());
    }
}
