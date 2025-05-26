package core.models.storage;

import core.models.airport.Location;
import core.interfaces.repository.ILocationRepository;
import java.util.ArrayList;
import java.util.List;

public class LocationRepository implements ILocationRepository {

    private List<Location> locations = new ArrayList<>();

    @Override
    public boolean addLocation(Location location) {
        if (location == null || getLocation(location.getAirportId()) != null) {
            return false;
        }
        locations.add(location);
        return true;
    }

    @Override
    public Location getLocation(String airportId) {
        for (Location loc : locations) {
            if (loc.getAirportId().equals(airportId)) {
                return loc;
            }
        }
        return null;
    }

    @Override
    public List<Location> getAllLocations() {
        return new ArrayList<>(locations);
    }

    @Override
    public List<Location> getSortedLocations() {
        List<Location> sortedLocations = new ArrayList<>(locations);
        int n = sortedLocations.size();

        for (int i = 0; i < n - 1; i++) {
            for (int j = 0; j < n - i - 1; j++) {
                if (sortedLocations.get(j).getAirportId()
                        .compareTo(sortedLocations.get(j + 1).getAirportId()) > 0) {
                    Location temp = sortedLocations.get(j);
                    sortedLocations.set(j, sortedLocations.get(j + 1));
                    sortedLocations.set(j + 1, temp);
                }
            }
        }

        return sortedLocations;
    }
}
