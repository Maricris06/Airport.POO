package core.controllers;

import core.controllers.services.PlaneService;
import core.controllers.utils.Response;

public class PlaneController {
    private static final PlaneService service = new PlaneService();

    public static Response createPlane(String id, String brand, String model, String capacity, String airline) {
        return service.createPlane(id, brand, model, capacity, airline);
    }

    public static Response listPlanes() {
        return service.listPlanes();
    }

    public static Response getSortedPlanes() {
        return service.getSortedPlanes();
    }
}
