package core.interfaces.repository;

import core.models.airport.Plane;
import java.util.List;

public interface IPlaneRepository {
    boolean addPlane(Plane plane);
    Plane getPlane(String id);
    List<Plane> getAllPlanes();
}
