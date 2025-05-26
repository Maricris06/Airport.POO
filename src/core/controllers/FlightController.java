
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
import java.util.Comparator;
import java.util.List;

public class FlightController {

public static Response createFlight(String id, String planeId, String departureLocationId, String arrivalLocationId,
    String departureDateStr, String hoursStr, String minutesStr, String hoursDurationsScale, String minutesDurationsScale, String scaleLocationId) {

    try {
        // Validar el formato del ID del vuelo
        if (!id.matches("[A-Z]{3}[0-9]{3}")) {
            return new Response("Invalid flight ID format.", Status.BAD_REQUEST);
        }

        // Validar duración del vuelo
        int hours, minutes;
        try {
            hours = Integer.parseInt(hoursStr);
            minutes = Integer.parseInt(minutesStr);
            if (hours == 0 && minutes == 0) {
                return new Response("Flight duration cannot be 00:00", Status.BAD_REQUEST);
            }
        } catch (NumberFormatException e) {
            return new Response("Flight duration must be numeric", Status.BAD_REQUEST);
        }

        // Validar duración de escala
        int scaleHours = 0;
        int scaleMinutes = 0;
        boolean hasScale = scaleLocationId != null && !scaleLocationId.isEmpty();
        if (hasScale) {
            try {
                scaleHours = Integer.parseInt(hoursDurationsScale);
                scaleMinutes = Integer.parseInt(minutesDurationsScale);
                if (scaleHours == 0 && scaleMinutes == 0) {
                    return new Response("Scale duration cannot be 00:00 if scale location is provided", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException e) {
                return new Response("Scale duration must be numeric", Status.BAD_REQUEST);
            }
        }

        // Parsear fecha de salida
        LocalDateTime departureDate;
        try {
            departureDate = LocalDateTime.parse(departureDateStr);
        } catch (DateTimeParseException e) {
            return new Response("Invalid date format", Status.BAD_REQUEST);
        }

        // Obtener instancia de almacenamiento
        Storage storage = Storage.getInstance();
        if (storage.getFlight(id) != null) {
            return new Response("Flight already exists", Status.BAD_REQUEST);
        }

        // Obtener entidades
        Plane plane = storage.getPlane(planeId);
        Location dep = storage.getLocation(departureLocationId);
        Location arr = storage.getLocation(arrivalLocationId);
        Location scale = null;

        if (plane == null || dep == null || arr == null) {
            return new Response("Invalid plane or location IDs", Status.BAD_REQUEST);
        }

        if (hasScale) {
            scale = storage.getLocation(scaleLocationId);
            if (scale == null) {
                return new Response("Invalid scale location ID", Status.BAD_REQUEST);
            }
        }

        // Aquí creamos los objetos Duration a partir de las horas y minutos
        java.time.Duration durationToScale = java.time.Duration.ZERO;
        java.time.Duration durationFromScale;

        if (hasScale) {
            durationToScale = java.time.Duration.ofHours(scaleHours).plusMinutes(scaleMinutes);
            durationFromScale = java.time.Duration.ofHours(hours).plusMinutes(minutes);
        } else {
            // Si no hay escala, toda la duración es la duración desde la salida hasta el destino
            durationFromScale = java.time.Duration.ofHours(hours).plusMinutes(minutes);
        }

        // Crear el vuelo usando el constructor correcto
        Flight flight;
        if (hasScale) {
            flight = new Flight(id, plane, dep, scale, arr, departureDate, durationToScale, durationFromScale);
        } else {
            flight = new Flight(id, plane, dep, arr, departureDate, durationFromScale);
        }

        if (!storage.addFlight(flight)) {
            return new Response("Could not add flight", Status.INTERNAL_SERVER_ERROR);
        }

        return new Response("Flight created successfully", Status.CREATED);

    } catch (Exception ex) {
        return new Response("Unexpected error: " + ex.getMessage(), Status.INTERNAL_SERVER_ERROR);
    }
}


    

    public static Response addFlight(String passengerId, String flightId) {
    try {
        long passengerIdLong =Integer.parseInt(passengerId);
        int flightIdInt=Integer.parseInt( flightId);

        // Validar passengerId
        if (passengerId == null || passengerId.trim().isEmpty()) {
            return new Response("Passenger ID must not be empty.", Status.BAD_REQUEST);
        }

        // Validar flightId
        if (flightId == null || flightId.trim().isEmpty()) {
            return new Response("Flight ID must not be empty.", Status.BAD_REQUEST);
        }
        try {
            flightIdInt = Integer.parseInt(flightId.trim());
        } catch (NumberFormatException ex) {
            return new Response("Flight ID must be a number.", Status.BAD_REQUEST);
        }

        // Verificar existencia de pasajero y vuelo
        Passenger passenger = Storage.getInstance().getPassenger(passengerIdLong);
        if (passenger == null) {
            return new Response("Passenger not found.", Status.NOT_FOUND);
        }

        Flight flight = Storage.getInstance().getFlight(flightId);
        if (flight == null) {
            return new Response("Flight not found.", Status.NOT_FOUND);
        }

        // Verificar si el pasajero ya está en el vuelo
        if (passenger.getFlights().contains(flight)) {
            return new Response("Passenger is already assigned to this flight.", Status.BAD_REQUEST);
        }

        // Asignar
        passenger.addFlight(flight);
        flight.addPassenger(passenger);

        return new Response("Passenger added successfully to the flight.", Status.OK);

    } catch (Exception ex) {
        return new Response("Unexpected error.", Status.INTERNAL_SERVER_ERROR);
    }
}

    
    public static Response delayFlight(String flightId, String hours, String minutes) {
    try {
        // Validaciones básicas
        if (flightId == null || flightId.trim().isEmpty()) {
            return new Response("Flight ID cannot be empty.", Status.BAD_REQUEST);
        }

        if (hours == null || hours.trim().isEmpty()) {
            return new Response("Hours must not be empty.", Status.BAD_REQUEST);
        }

        if (minutes == null || minutes.trim().isEmpty()) {
            return new Response("Minutes must not be empty.", Status.BAD_REQUEST);
        }

        // Conversión de datos
        int flightIdInt;
        int hoursInt;
        int minutesInt;

        try {
            flightIdInt = Integer.parseInt(flightId.trim());
        } catch (NumberFormatException ex) {
            return new Response("Flight ID must be a valid number.", Status.BAD_REQUEST);
        }

        try {
            hoursInt = Integer.parseInt(hours.trim());
        } catch (NumberFormatException ex) {
            return new Response("Hours must be a valid number.", Status.BAD_REQUEST);
        }

        try {
            minutesInt = Integer.parseInt(minutes.trim());
        } catch (NumberFormatException ex) {
            return new Response("Minutes must be a valid number.", Status.BAD_REQUEST);
        }

        // Validar que haya al menos algún retraso
        if (hoursInt <= 0 && minutesInt <= 0) {
            return new Response("Delay must be greater than 00:00.", Status.BAD_REQUEST);
        }

        Flight flight = Storage.getInstance().getFlight(flightId);
        if (flight == null) {
            return new Response("Flight not found.", Status.NOT_FOUND);
        }

        flight.delay(hoursInt, minutesInt);
        return new Response("Flight delayed successfully.", Status.OK);

    } catch (Exception ex) {
        return new Response("An unexpected error occurred.", Status.INTERNAL_SERVER_ERROR);
    }
}
    
    public static Response getSortedFlights() {
    try {
        // Obtener lista ordenada de vuelos desde el almacenamiento
        List<Flight> flights = Storage.getInstance().getSortedFlights();
        ArrayList<Flight> flightsCopy = new ArrayList<>();
        // Verificar que la lista no sea nula
        if (flights != null) {
            // Recorrer y clonar cada vuelo si no es nulo
            for (Flight flight : flights) {
                if (flight != null) {
                    flightsCopy.add(flight.clone()); // Clonación del vuelo
                }
            }
        }
        // Retornar respuesta exitosa con la lista copiada
        return new Response("Flights loaded successfully.", Status.OK, flightsCopy);
    } catch (Exception ex) {
        // Manejo de errores inesperados
        System.err.println("Error loading flights: " + ex.getMessage());
        ex.printStackTrace();
        return new Response("Unexpected error occurred while loading flights.", Status.INTERNAL_SERVER_ERROR);
    }
}

}


