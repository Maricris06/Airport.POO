package core.models.airport;

public class Location {
    
    private final String airportId;
    private final String airportName;
    private final String airportCity;
    private final String airportCountry;
    private final double airportLatitude;
    private final double airportLongitude;

    public Location(String airportId, String airportName, String airportCity, String airportCountry, double airportLatitude, double airportLongitude) {
        this.airportId = airportId;
        this.airportName = airportName;
        this.airportCity = airportCity;
        this.airportCountry = airportCountry;
        this.airportLatitude = airportLatitude;
        this.airportLongitude = airportLongitude;
    }

    public String getAirportId() {
        return airportId;
    }

    public String getAirportName() {
        return airportName;
    }

    public String getAirportCity() {
        return airportCity;
    }

    public String getAirportCountry() {
        return airportCountry;
    }

    public double getAirportLatitude() {
        return airportLatitude;
    }

    public double getAirportLongitude() {
        return airportLongitude;
    }
    
    public Location clone() {
        return new Location(
            this.airportId,
            this.airportName,
            this.airportCity,
            this.airportCountry,
            this.airportLatitude,
            this.airportLongitude
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Location other = (Location) obj;
        return airportId.equals(other.airportId);
    }

    @Override
    public int hashCode() {
        return airportId.hashCode();
    }

    @Override
    public String toString() {
        return airportName + " (" + airportCity + ", " + airportCountry + ")";
    }
}
