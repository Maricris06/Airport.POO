package core.models.storage;

import core.models.airport.Plane;
import core.interfaces.repository.IPlaneRepository;
import java.util.ArrayList;
import java.util.List;

public class PlaneRepository implements IPlaneRepository {

    private final List<Plane> planes = new ArrayList<>();

    public PlaneRepository() {
        // constructor vacío
    }

    @Override
    public boolean addPlane(Plane plane) {
        for (Plane p : planes) {
            if (p.getId().equals(plane.getId())) {
                return false;
            }
        }
        planes.add(plane);
        return true;
    }

    @Override
    public Plane getPlane(String id) {
        for (Plane plane : planes) {
            if (plane.getId().equals(id)) {
                return plane;
            }
        }
        return null;
    }

    @Override
    public List<Plane> getAllPlanes() {
        return new ArrayList<>(planes);
    }
}
