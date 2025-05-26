package core.models.airport.flight;

import java.util.ArrayList;
import java.util.List;
import core.models.airport.Passenger;

public class PassengerList {
    private final List<Passenger> passengers = new ArrayList<>();

    public void addPassenger(Passenger passenger) {
        passengers.add(passenger);
    }

    public int count() {
        return passengers.size();
    }

    public List<Passenger> getAll() {
        return new ArrayList<>(passengers); // Protección contra modificación externa
    }
}
