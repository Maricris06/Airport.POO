// core/controllers/LocationController.java
package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.airport.Location;
import core.controllers.services.LocationService;
import core.controllers.validators.LocationValidator;

public class LocationController {

    private static final LocationService locationService = new LocationService(core.models.storage.Storage.getInstance());

    public static Response createLocation(String airportId, String name, String city,
                                      String country, String latitudeStr, String longitudeStr) {
        try {
            Location location = LocationValidator.buildValidatedLocation(
                airportId, name, city, country, latitudeStr, longitudeStr
            );

            if (!locationService.addLocation(location)) {
                return new Response("Airport ID already exists", Status.BAD_REQUEST);
            }

            return new Response("Location created successfully", Status.CREATED, location);

        } catch (NumberFormatException e) {
            return new Response("Invalid coordinate format: " + e.getMessage(), Status.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            return new Response(e.getMessage(), Status.BAD_REQUEST);
        } catch (Exception e) {
            return new Response("Unexpected error: " + e.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }


    public static Response getLocation(String airportId) {
        try {
            Location location = locationService.getLocation(airportId);
            if (location == null) {
                return new Response("Location not found", Status.NOT_FOUND);
            }
            return new Response("Location retrieved successfully", Status.OK, location);
        } catch (Exception e) {
            return new Response("Error retrieving location: " + e.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response getAllLocations() {
        try {
            return new Response(
                "Locations retrieved successfully",
                Status.OK,
                locationService.getAllLocations()
            );
        } catch (Exception e) {
            return new Response("Error retrieving locations: " + e.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response getSortedLocations() {
        try {
            return new Response(
                "Locations loaded successfully.",
                Status.OK,
                locationService.getSortedLocations()
            );
        } catch (Exception ex) {
            return new Response("Unexpected error occurred while loading locations.", Status.INTERNAL_SERVER_ERROR);
        }
    }

    // Utilidades
    private static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private static double parseCoordinate(String value, String fieldName, double min, double max)
            throws NumberFormatException, IllegalArgumentException {
        double coord = Double.parseDouble(value);
        if (coord < min || coord > max) {
            throw new IllegalArgumentException(fieldName + " must be between " + min + " and " + max);
        }
        return coord;
    }
}
