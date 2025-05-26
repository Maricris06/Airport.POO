package core.controllers.validators;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.interfaces.repository.IStorage;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class FlightValidator {

    public static Response validateFlightData(String id, String planeId, String departureLocationId, String arrivalLocationId,
                                              String departureDateStr, String hoursStr, String minutesStr,
                                              boolean hasScale, String hoursDurationsScale, String minutesDurationsScale, String scaleLocationId,
                                              IStorage storage) {

        if (!id.matches("[A-Z]{3}[0-9]{3}")) {
            return new Response("Invalid flight ID format.", Status.BAD_REQUEST);
        }

        try {
            int hours = Integer.parseInt(hoursStr);
            int minutes = Integer.parseInt(minutesStr);
            if (hours == 0 && minutes == 0) {
                return new Response("Flight duration cannot be 00:00", Status.BAD_REQUEST);
            }
        } catch (NumberFormatException e) {
            return new Response("Flight duration must be numeric", Status.BAD_REQUEST);
        }

        if (hasScale) {
            try {
                int scaleHours = Integer.parseInt(hoursDurationsScale);
                int scaleMinutes = Integer.parseInt(minutesDurationsScale);
                if (scaleHours == 0 && scaleMinutes == 0) {
                    return new Response("Scale duration cannot be 00:00", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException e) {
                return new Response("Scale duration must be numeric", Status.BAD_REQUEST);
            }
        }

        try {
            LocalDateTime.parse(departureDateStr);
        } catch (DateTimeParseException e) {
            return new Response("Invalid date format", Status.BAD_REQUEST);
        }

        if (storage.getFlight(id) != null) {
            return new Response("Flight already exists", Status.BAD_REQUEST);
        }

        if (storage.getPlane(planeId) == null || storage.getLocation(departureLocationId) == null ||
            storage.getLocation(arrivalLocationId) == null) {
            return new Response("Invalid plane or location IDs", Status.BAD_REQUEST);
        }

        if (hasScale && storage.getLocation(scaleLocationId) == null) {
            return new Response("Invalid scale location ID", Status.BAD_REQUEST);
        }

        return null; // validación exitosa
    }
}
