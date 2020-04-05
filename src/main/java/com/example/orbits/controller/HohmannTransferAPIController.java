package com.example.orbits.controller;

import com.example.orbits.model.CelestialBody;
import com.example.orbits.model.Orbit;
import com.example.orbits.service.HohmannTransferCalculator;
import lombok.Data;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HohmannTransferAPIController {

    @Data
    private static class HohmannTransferOutput {
        private final double transferTime;
        private final double insertionDeltaV;
        private final double arrivalDeltaV;
        private final double totalDeltaV;
    }

    @Data
    private static class SimpleHohmannTransferInput {
        private final double primaryBodyMass;
        private final double startingOrbitRadius;
        private final double destinationOrbitRadius;
    }

    @PostMapping(path = "/simple", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public HohmannTransferOutput simpleHohmannTransfer(@RequestBody SimpleHohmannTransferInput input) {
        var primaryBody = new CelestialBody(null, input.getPrimaryBodyMass());
        var startingOrbit = new Orbit(input.getStartingOrbitRadius(), primaryBody);
        var destinationOrbit = new Orbit(input.getDestinationOrbitRadius(), primaryBody);
        var calculator = new HohmannTransferCalculator(startingOrbit, destinationOrbit);
        calculator.calculate();
        return new HohmannTransferOutput(
                calculator.getTransferTime(),
                calculator.getInsertionDeltaV(),
                calculator.getArrivalDeltaV(),
                calculator.getTotalDeltaV());
    }
}
