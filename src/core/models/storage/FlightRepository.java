package core.models.storage;

import core.models.airport.Flight;
import core.interfaces.repository.IFlightRepository;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FlightRepository implements IFlightRepository {

    private final List<Flight> flights = new ArrayList<>();

    @Override
    public boolean addFlight(Flight flight) {
        for (Flight f : flights) {
            if (f.getId().equals(flight.getId())) {
                return false;
            }
        }
        flights.add(flight);
        return true;
    }

    @Override
    public Flight getFlight(String id) {
        for (Flight flight : flights) {
            if (flight.getId().equals(id)) {
                return flight;
            }
        }
        return null;
    }

    @Override
    public List<Flight> getAllFlights() {
        return new ArrayList<>(flights);
    }

    @Override
    public List<Flight> getSortedFlights() {
        ArrayList<Flight> sortedFlights = new ArrayList<>(flights);
        int n = sortedFlights.size();

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                LocalDateTime date1 = sortedFlights.get(j).getDepartureDate();
                LocalDateTime date2 = sortedFlights.get(j + 1).getDepartureDate();
                if (date1.isAfter(date2)) {
                    Flight temp = sortedFlights.get(j);
                    sortedFlights.set(j, sortedFlights.get(j + 1));
                    sortedFlights.set(j + 1, temp);
                }
            }
        }
        return sortedFlights;
    }
    
}
