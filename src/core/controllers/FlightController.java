/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

/**
 *
 * @author User invitado
 */
import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.airport.Flight;
import core.models.airport.Location;
import core.models.airport.Plane;
import core.models.airport.Passenger;
import core.models.storage.Storage;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class FlightController {


    public static Response createFlight(String id, String planeId, String depId, String arrId,
                                        String departureDateStr, String hoursStr, String minutesStr) {
        try {
            if (!id.matches("[A-Z]{3}[0-9]{3}")) {
                return new Response("Invalid flight ID format.", Status.BAD_REQUEST);
            }

            int hours, minutes;
            try {
                hours = Integer.parseInt(hoursStr);
                minutes = Integer.parseInt(minutesStr);
                if (hours == 0 && minutes == 0) {
                    return new Response("Flight duration cannot be 00:00", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException e) {
                return new Response("Duration must be numeric", Status.BAD_REQUEST);
            }

            LocalDateTime departureDate;
            try {
                departureDate = LocalDateTime.parse(departureDateStr);
            } catch (DateTimeParseException e) {
                return new Response("Invalid date format.", Status.BAD_REQUEST);
            }

            Storage storage = Storage.getInstance();
            if (storage.getFlight(id) != null) {
                return new Response("Flight already exists", Status.BAD_REQUEST);
            }

            

            if (plane == null || dep == null || arr == null) {
                return new Response("Invalid plane or location IDs", Status.BAD_REQUEST);
            }

            Flight flight = new Flight(id, plane, dep, arr, departureDate, hours, minutes);
            if (!storage.addFlight(flight)) {
                return new Response("Could not add flight", Status.INTERNAL_SERVER_ERROR);
            }

            return new Response("Flight created successfully", Status.CREATED);

        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response readFlight(String id) {
        try {
            Storage storage = Storage.getInstance();
            Flight flight = storage.getFlight(id);
            if (flight == null) {
                return new Response("Flight not found", Status.NOT_FOUND);
            }
            return new Response("Flight found", Status.OK, flight);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response deleteFlight(String id) {
        try {
            Storage storage = Storage.getInstance();
            if (!storage.delFlight(id)) {
                return new Response("Flight not found", Status.NOT_FOUND);
            }
            return new Response("Flight deleted successfully", Status.NO_CONTENT);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response addPassengerToFlight(String flightId, Passenger passenger) {
        try {
            Storage storage = Storage.getInstance();
            Flight flight = storage.getFlight(flightId);
            if (flight == null) {
                return new Response("Flight not found", Status.NOT_FOUND);
            }
            flight.addPassenger(passenger);
            return new Response("Passenger added", Status.OK);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response delayFlight(String flightId, String hoursStr, String minutesStr) {
        try {
            int hours, minutes;
            try {
                hours = Integer.parseInt(hoursStr);
                minutes = Integer.parseInt(minutesStr);
                if (hours == 0 && minutes == 0) {
                    return new Response("Delay time cannot be 00:00", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException e) {
                return new Response("Delay must be numeric", Status.BAD_REQUEST);
            }

            Storage storage = Storage.getInstance();
            Flight flight = storage.getFlight(flightId);
            if (flight == null) {
                return new Response("Flight not found", Status.NOT_FOUND);
            }

            flight.delay(hours, minutes);
            return new Response("Flight delayed", Status.OK);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response listFlights() {
        try {
            Storage storage = Storage.getInstance();
            ArrayList<Flight> flights = storage.getAllFlights();
            return new Response("Flights list retrieved", Status.OK, flights);
        } catch (Exception ex) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }
}


