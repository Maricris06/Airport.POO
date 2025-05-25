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
    
    public void updateAll(Collection<Flight> flightsToUpdate) {
        synchronized (this) {
            flightsToUpdate.forEach(this::update);
        }
    }
    
    public void update(Flight updatedFlight) {
        Objects.requireNonNull(updatedFlight, "Flight no puede ser nulo");
        String flightId = updatedFlight.getId();
    
        if (flightId == null || flightId.trim().isEmpty()) {
            throw new IllegalArgumentException("ID de vuelo inválido");
        }

        synchronized (this) {
            // Buscar el índice del vuelo existente
            int index = -1;
            for (int i = 0; i < flights.size(); i++) {
                if (flightId.equals(flights.get(i).getId())) {
                    index = i;
                    break;
                }
            }
        
            if (index == -1) {
                throw new IllegalArgumentException("Vuelo con ID " + flightId + " no existe");
            }
        
            // Reemplazar el vuelo existente
            flights.set(index, updatedFlight);
            notifyUpdate();
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