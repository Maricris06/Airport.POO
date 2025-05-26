package core.models.storage;

import core.models.airport.Passenger;
import core.interfaces.repository.IPassengerRepository;
import core.models.airport.Flight;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PassengerRepository implements IPassengerRepository {

    private List<Passenger> passengers = new ArrayList<>();
    
    @Override
    public List<Flight> getFlightsByPassenger(Passenger passenger) {
        if (passenger == null) {
            return Collections.emptyList();
        }
        return passenger.getFlights();
    }

    @Override
    public boolean addPassenger(Passenger passenger) {
        for (Passenger p : passengers) {
            if (p.getId() == passenger.getId()) {
                return false;
            }
        }
        passengers.add(passenger);
        return true;
    }

    @Override
    public boolean updatePassenger(Passenger passenger) {
        for (int i = 0; i < passengers.size(); i++) {
            if (passengers.get(i).getId() == passenger.getId()) {
                passengers.set(i, passenger);
                return true;
            }
        }
        return false;
    }

    @Override
    public Passenger getPassenger(long id) {
        for (Passenger p : passengers) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null;
    }

    @Override
    public List<Passenger> getAllPassengers() {
        return new ArrayList<>(passengers);
    }

    @Override
    public List<Passenger> getSortedPassengers() {
        List<Passenger> sortedPassengers = new ArrayList<>(passengers);
        int n = sortedPassengers.size();
        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (sortedPassengers.get(j).getId() > sortedPassengers.get(j + 1).getId()) {
                    Passenger temp = sortedPassengers.get(j);
                    sortedPassengers.set(j, sortedPassengers.get(j + 1));
                    sortedPassengers.set(j + 1, temp);
                }
            }
        }
        return sortedPassengers;
    }
}
