package core.interfaces.repository;

import core.models.airport.Flight;
import core.models.airport.Passenger;
import java.util.List;

public interface IPassengerRepository {
    boolean addPassenger(Passenger passenger);
    boolean updatePassenger(Passenger passenger);
    Passenger getPassenger(long id);
    List<Passenger> getAllPassengers();
    List<Passenger> getSortedPassengers();
    List<Flight> getFlightsByPassenger(Passenger passenger);
}
