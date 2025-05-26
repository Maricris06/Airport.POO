package core.controllers.services;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.controllers.validators.FlightValidator;
import core.interfaces.repository.IStorage;
import core.models.airport.Flight;
import core.models.airport.Location;
import core.models.airport.Passenger;
import core.models.airport.Plane;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FlightService {
    private final IStorage storage;

    public FlightService(IStorage storage) {
        this.storage = storage;
    }

    public Response createFlight(String id, String planeId, String departureLocationId, String arrivalLocationId,
                                 String departureDateStr, String hoursStr, String minutesStr,
                                 String hoursDurationsScale, String minutesDurationsScale, String scaleLocationId) {

        boolean hasScale = scaleLocationId != null && !scaleLocationId.isEmpty();

        Response validation = FlightValidator.validateFlightData(id, planeId, departureLocationId, arrivalLocationId,
                departureDateStr, hoursStr, minutesStr, hasScale, hoursDurationsScale, minutesDurationsScale, scaleLocationId, storage);
        if (validation != null) return validation;

        try {
            Plane plane = storage.getPlane(planeId);
            Location dep = storage.getLocation(departureLocationId);
            Location arr = storage.getLocation(arrivalLocationId);
            Location scale = hasScale ? storage.getLocation(scaleLocationId) : null;

            LocalDateTime departureDate = LocalDateTime.parse(departureDateStr);
            int hours = Integer.parseInt(hoursStr);
            int minutes = Integer.parseInt(minutesStr);

            Duration durationFromScale = Duration.ofHours(hours).plusMinutes(minutes);
            Duration durationToScale = hasScale ? Duration.ofHours(Integer.parseInt(hoursDurationsScale)).plusMinutes(Integer.parseInt(minutesDurationsScale)) : Duration.ZERO;

            Flight flight = hasScale ?
                    new Flight(id, plane, dep, scale, arr, departureDate, durationToScale, durationFromScale) :
                    new Flight(id, plane, dep, arr, departureDate, durationFromScale);

            if (!storage.addFlight(flight)) {
                return new Response("Could not add flight", Status.INTERNAL_SERVER_ERROR);
            }

            return new Response("Flight created successfully", Status.CREATED);
        } catch (Exception ex) {
            return new Response("Unexpected error: " + ex.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Response addPassengerToFlight(String passengerIdStr, String flightIdStr) {
        try {
            long passengerId = Long.parseLong(passengerIdStr);
            Flight flight = storage.getFlight(flightIdStr);
            Passenger passenger = storage.getPassenger(passengerId);

            if (flight == null) return new Response("Flight not found.", Status.NOT_FOUND);
            if (passenger == null) return new Response("Passenger not found.", Status.NOT_FOUND);
            if (passenger.getFlights().contains(flight)) {
                return new Response("Passenger already assigned to flight.", Status.BAD_REQUEST);
            }

            passenger.addFlight(flight);
            flight.addPassenger(passenger);
            return new Response("Passenger added successfully.", Status.OK);
        } catch (Exception ex) {
            return new Response("Error: " + ex.getMessage(), Status.BAD_REQUEST);
        }
    }

    public Response delayFlight(String flightIdStr, String hoursStr, String minutesStr) {
        try {
            int hours = Integer.parseInt(hoursStr);
            int minutes = Integer.parseInt(minutesStr);
            if (hours == 0 && minutes == 0) {
                return new Response("Delay must be greater than 00:00", Status.BAD_REQUEST);
            }

            Flight flight = storage.getFlight(flightIdStr);
            if (flight == null) return new Response("Flight not found", Status.NOT_FOUND);

            flight.delay(hours, minutes);
            return new Response("Flight delayed successfully", Status.OK);
        } catch (NumberFormatException ex) {
            return new Response("Invalid input format", Status.BAD_REQUEST);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public Response getSortedFlights() {
        try {
            List<Flight> flights = storage.getSortedFlights();
            List<Flight> copy = new ArrayList<>();
            for (Flight flight : flights) {
                if (flight != null) copy.add(flight.clone());
            }
            return new Response("Flights loaded successfully", Status.OK, copy);
        } catch (Exception ex) {
            return new Response("Unexpected error occurred while loading flights.", Status.INTERNAL_SERVER_ERROR);
        }
    }
}
