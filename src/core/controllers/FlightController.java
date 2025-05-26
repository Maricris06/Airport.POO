package core.controllers;

import core.controllers.services.FlightService;
import core.controllers.utils.Response;
import core.models.storage.Storage;

public class FlightController {
    private static final FlightService flightService = new FlightService(Storage.getInstance());

    public static Response createFlight(String id, String planeId, String departureLocationId, String arrivalLocationId,
                                        String departureDateStr, String hoursStr, String minutesStr,
                                        String hoursDurationsScale, String minutesDurationsScale, String scaleLocationId) {
        return flightService.createFlight(id, planeId, departureLocationId, arrivalLocationId,
                departureDateStr, hoursStr, minutesStr, hoursDurationsScale, minutesDurationsScale, scaleLocationId);
    }

    public static Response addFlight(String passengerId, String flightId) {
        return flightService.addPassengerToFlight(passengerId, flightId);
    }

    public static Response delayFlight(String flightId, String hours, String minutes) {
        return flightService.delayFlight(flightId, hours, minutes);
    }

    public static Response getSortedFlights() {
        return flightService.getSortedFlights();
    }
}
