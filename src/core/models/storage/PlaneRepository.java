package core.models.storage;

import core.interfaces.repository.Repository;
import core.models.airport.Plane;
import java.util.*;
import java.util.function.Consumer;

public class PlaneRepository implements Repository<Plane, String> {
    private final List<Plane> planes = new ArrayList<>();
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
    public boolean add(Plane plane) {
        if (plane == null || plane.getId() == null) return false;
        if (planes.stream().anyMatch(p -> p.getId().equals(plane.getId()))) return false;
        
        boolean result = planes.add(plane);
        if (result) notifyUpdate();
        return result;
    }
    
    @Override
    public boolean remove(String id) {
        if (id == null) return false;
        
        boolean result = planes.removeIf(p -> p.getId().equals(id));
        if (result) notifyUpdate();
        return result;
    }
    
    @Override
    public Plane get(String id) {
        return planes.stream()
                    .filter(p -> p.getId().equals(id))
                    .findFirst()
                    .orElse(null);
    }
    
    @Override
    public List<Plane> getAll() {
        return new ArrayList<>(planes);
    }
    
    @Override
    public List<Plane> getAllSorted(Comparator<Plane> comparator) {
        List<Plane> sorted = new ArrayList<>(planes);
        sorted.sort(comparator);
        return sorted;
    }
 }