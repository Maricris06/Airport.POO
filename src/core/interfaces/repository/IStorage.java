package core.interfaces.repository;

import core.models.airport.Flight;
import core.models.airport.Location;
import core.models.airport.Passenger;
import core.models.airport.Plane;
import java.util.List;

public interface IStorage {
    Flight getFlight(String id);
    boolean addFlight(Flight flight);
    Plane getPlane(String id);
    Location getLocation(String id);
    Passenger getPassenger(long id);
    List<Flight> getSortedFlights();
    boolean addLocation(Location location);
    List<Location> getAllLocations();
    List<Location> getSortedLocations();
}
