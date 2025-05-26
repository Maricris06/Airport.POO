// core/validators/LocationValidator.java
package core.controllers.validators;

import core.models.airport.Location;

public class LocationValidator {

    public static void validate(String airportId, String name, String city, String country,
                                String latitudeStr, String longitudeStr) {
        if (airportId == null || !airportId.matches("[A-Z]{3}")) {
            throw new IllegalArgumentException("Airport ID must be 3 uppercase letters (e.g., JFK)");
        }

        if (isEmpty(name) || isEmpty(city) || isEmpty(country)) {
            throw new IllegalArgumentException("Name, city, and country cannot be empty");
        }

        double latitude = parseCoordinate(latitudeStr, "Latitude", -90, 90);
        double longitude = parseCoordinate(longitudeStr, "Longitude", -180, 180);

        // Si todo es válido, se puede retornar el objeto listo para usarse
        // O puedes retornar directamente un `Location` si prefieres:
        // return new Location(airportId, name, city, country, latitude, longitude);
    }

    public static Location buildValidatedLocation(String airportId, String name, String city, String country,
                                                  String latitudeStr, String longitudeStr) {
        validate(airportId, name, city, country, latitudeStr, longitudeStr);

        double latitude = Double.parseDouble(latitudeStr);
        double longitude = Double.parseDouble(longitudeStr);

        return new Location(airportId, name, city, country, latitude, longitude);
    }

    private static boolean isEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private static double parseCoordinate(String value, String fieldName, double min, double max)
            throws NumberFormatException {
        double coord = Double.parseDouble(value);
        if (coord < min || coord > max) {
            throw new IllegalArgumentException(fieldName + " must be between " + min + " and " + max);
        }
        return coord;
    }
}
