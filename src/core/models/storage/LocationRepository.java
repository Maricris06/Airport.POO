package core.models.storage;

import core.interfaces.repository.Repository;
import core.models.airport.Location;
import java.util.*;
import java.util.function.Consumer;

public class LocationRepository implements Repository<Location, String> {
    private final List<Location> locations = new ArrayList<>();
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
    public boolean add(Location location) {
        if (location == null || location.getAirportId() == null) return false;
        if (locations.stream().anyMatch(l -> l.getAirportId().equals(location.getAirportId()))) return false;
        
        boolean result = locations.add(location);
        if (result) notifyUpdate();
        return result;
    }
    
    @Override
    public boolean remove(String id) {
        if (id == null) return false;
        
        boolean result = locations.removeIf(l -> l.getAirportId().equals(id));
        if (result) notifyUpdate();
        return result;
    }
    
    @Override
    public Location get(String id) {
        return locations.stream()
                      .filter(l -> l.getAirportId().equals(id))
                      .findFirst()
                      .orElse(null);
    }
    
    @Override
    public List<Location> getAll() {
        return new ArrayList<>(locations);
    }
    
    @Override
    public List<Location> getAllSorted(Comparator<Location> comparator) {
        List<Location> sorted = new ArrayList<>(locations);
        sorted.sort(comparator);
        return sorted;
    }
    
    // Método específico para LocationRepository
    public List<Location> getLocationsByCountry(String country) {
        return locations.stream()
                      .filter(l -> l.getCountry().equalsIgnoreCase(country))
                      .toList();
    }
}