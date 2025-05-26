package core.controllers.services;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.controllers.validators.PlaneValidator;
import core.models.airport.Plane;
import core.models.storage.Storage;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PlaneService {
    private final Storage storage = Storage.getInstance();

    public Response createPlane(String id, String brand, String model, String capacityStr, String airline) {
        Response validation = PlaneValidator.validate(id, brand, model, capacityStr, airline);
        if (validation.getStatus() != Status.OK) return validation;

        int capacity = Integer.parseInt(capacityStr);
        if (storage.getPlane(id) != null)
            return new Response("Plane with given ID already exists", Status.BAD_REQUEST);

        Plane plane = new Plane(id, brand, model, capacity, airline);
        if (!storage.addPlane(plane))
            return new Response("Could not add plane", Status.INTERNAL_SERVER_ERROR);

        return new Response("Plane created successfully", Status.CREATED);
    }

    public Response listPlanes() {
        List<Plane> planes = storage.getAllPlanes();
        planes.sort(Comparator.comparing(Plane::getId));
        return new Response("Plane list retrieved", Status.OK, planes);
    }

    public Response getSortedPlanes() {
        List<Plane> planes = storage.getAllPlanes();
        ArrayList<Plane> result = new ArrayList<>();
        for (Plane p : planes) result.add(p.clone());
        return new Response("Planes loaded successfully.", Status.OK, result);
    }
}
