package core.interfaces.repository;

import core.models.airport.Location;
import java.util.List;

public interface ILocationRepository {
    boolean addLocation(Location location);
    Location getLocation(String airportId);
    List<Location> getAllLocations();
    List<Location> getSortedLocations();
}
