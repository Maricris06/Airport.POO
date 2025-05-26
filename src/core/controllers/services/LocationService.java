// core/services/LocationService.java
package core.controllers.services;

import core.interfaces.repository.IStorage;
import core.models.airport.Location;

import java.util.ArrayList;
import java.util.List;

public class LocationService {

    private final IStorage storage;

    public LocationService(IStorage storage) {
        this.storage = storage;
    }

    public boolean addLocation(Location location) {
        return storage.addLocation(location);
    }

    public Location getLocation(String id) {
        return storage.getLocation(id);
    }

    public List<Location> getAllLocations() {
        return storage.getAllLocations();
    }

    public List<Location> getSortedLocations() {
        List<Location> locations = storage.getSortedLocations();
        List<Location> locationsCopy = new ArrayList<>();

        if (locations != null) {
            for (Location loc : locations) {
                if (loc != null) {
                    locationsCopy.add(loc.clone());
                }
            }
        }

        return locationsCopy;
    }
}
