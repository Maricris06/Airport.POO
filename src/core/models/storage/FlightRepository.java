package core.models.storage;

import core.interfaces.repository.Repository;
import core.models.airport.Flight;
import core.models.airport.Flight;
import java.util.*;
import java.util.function.Consumer;

public class FlightRepository implements Repository<Flight, String> {
    private final List<Flight> flights = new ArrayList<>();
    private Runnable updateHandler;
    
    public void setUpdateHandler(Runnable handler) {
        this.updateHandler = handler;
    }
    
    private void notifyUpdate() {
        if (updateHandler != null) {
            updateHandler.run();
        }
    }
    
    @Override
    public boolean add(Flight flight) {
        if (flight == null || flight.getId() == null) return false;
        if (flights.stream().anyMatch(f -> f.getId().equals(flight.getId()))) return false;
        
        boolean result = flights.add(flight);
        if (result) notifyUpdate();
        return result;
    }
    
    @Override
    public boolean remove(String id) {
        if (id == null) return false;
        
        boolean result = flights.removeIf(f -> f.getId().equals(id));
        if (result) notifyUpdate();
        return result;
    }
    
    @Override
    public Flight get(String id) {
        return flights.stream()
                     .filter(f -> f.getId().equals(id))
                     .findFirst()
                     .orElse(null);
    }
    
    @Override
    public List<Flight> getAll() {
        return new ArrayList<>(flights);
    }
    
    @Override
    public List<Flight> getAllSorted(Comparator<Flight> comparator) {
        List<Flight> sorted = new ArrayList<>(flights);
        sorted.sort(comparator);
        return sorted;
    }
    
 }