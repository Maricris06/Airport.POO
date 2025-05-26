package core.interfaces.repository;

import core.models.airport.Flight;
import java.util.List;

public interface IFlightRepository {
    boolean addFlight(Flight flight);
    Flight getFlight(String id);
    List<Flight> getAllFlights();
    List<Flight> getSortedFlights();
}
