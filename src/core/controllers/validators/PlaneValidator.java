package core.controllers.validators;

import core.controllers.utils.Response;
import core.controllers.utils.Status;

public class PlaneValidator {
    public static Response validate(String id, String brand, String model, String capacityStr, String airline) {
        if (id == null || id.length() != 7)
            return new Response("Plane ID must be exactly 7 characters long", Status.BAD_REQUEST);
        if (!Character.isUpperCase(id.charAt(0)) || !Character.isUpperCase(id.charAt(1)))
            return new Response("First two characters must be uppercase letters", Status.BAD_REQUEST);
        for (int i = 2; i < 7; i++) {
            if (!Character.isDigit(id.charAt(i)))
                return new Response("Last five characters must be digits", Status.BAD_REQUEST);
        }
        if (brand == null || brand.isEmpty() || model == null || model.isEmpty() || airline == null || airline.isEmpty())
            return new Response("Brand, model, and airline cannot be empty", Status.BAD_REQUEST);
        try {
            int cap = Integer.parseInt(capacityStr);
            if (cap <= 0) return new Response("Capacity must be positive", Status.BAD_REQUEST);
        } catch (NumberFormatException e) {
            return new Response("Capacity must be numeric", Status.BAD_REQUEST);
        }

        return new Response("Validation passed", Status.OK);
    }
}
