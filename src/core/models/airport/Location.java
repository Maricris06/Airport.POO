package core.models.airport;

import java.util.Objects;

public final class Location implements Cloneable {
    private final String airportId;
    private final String name;
    private final String city;
    private final String country;
    private final double latitude;
    private final double longitude;

    public Location(String airportId, String name, String city, 
                    String country, double latitude, double longitude) {
        validateId(airportId);
        validateTextFields(name, city, country);
        validateCoordinates(latitude, longitude);

        this.airportId = airportId.toUpperCase();
        this.name = name.trim();
        this.city = city.trim();
        this.country = country.trim();
        this.latitude = roundCoordinate(latitude);
        this.longitude = roundCoordinate(longitude);
    }

    // --- Validaciones privadas ---
    private void validateId(String id) {
        if (id == null || !id.matches("^[A-Z]{3}$")) {
            throw new IllegalArgumentException("Airport ID must be exactly 3 uppercase letters (e.g., JFK)");
        }
    }

    private void validateTextFields(String name, String city, String country) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }
        if (city == null || city.trim().isEmpty()) {
            throw new IllegalArgumentException("City cannot be null or empty");
        }
        if (country == null || country.trim().isEmpty()) {
            throw new IllegalArgumentException("Country cannot be null or empty");
        }
    }

    private void validateCoordinates(double lat, double lon) {
        if (lat < -90 || lat > 90) {
            throw new IllegalArgumentException("Latitude must be between -90 and 90 degrees");
        }
        if (lon < -180 || lon > 180) {
            throw new IllegalArgumentException("Longitude must be between -180 and 180 degrees");
        }
    }

    private double roundCoordinate(double value) {
        return Math.round(value * 10000) / 10000.0;
    }

    // --- Getters ---
    public String getAirportId() { return airportId; }
    public String getName() { return name; }
    public String getCity() { return city; }
    public String getCountry() { return country; }
    public double getLatitude() { return latitude; }
    public double getLongitude() { return longitude; }

    public String getFullLocation() {
        return String.format("%s (%s, %s)", name, city, country);
    }

    @Override
    public Location clone() {
        try {
            return (Location) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError("Clone not supported", e);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Location)) return false;
        Location that = (Location) o;
        return airportId.equals(that.airportId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(airportId);
    }

    @Override
    public String toString() {
        return String.format(
            "Location[id=%s, name=%s, city=%s, country=%s, coords=(%.4f, %.4f)]",
            airportId, name, city, country, latitude, longitude
        );
    }
}
