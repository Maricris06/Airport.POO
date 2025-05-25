/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.airport.Plane;
import core.models.storage.Storage;
import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * @author User invitado
 */
public class PlaneController {
   public static Response createPlane(String id, String brand, String model, String capacityStr, String airline) {
    try {
        // Validación de ID: formato XXYYYYY
        if (id == null || id.length() != 7) {
            return new Response("Plane ID must be exactly 7 characters long", Status.BAD_REQUEST);
        } else {
            if (!Character.isUpperCase(id.charAt(0)) || !Character.isUpperCase(id.charAt(1))) {
                return new Response("The first two characters must be uppercase letters", Status.BAD_REQUEST);
            }
            for (int i = 2; i < 7; i++) {
                if (!Character.isDigit(id.charAt(i))) {
                    return new Response("The last five characters must be digits", Status.BAD_REQUEST);
                }
            }
        }

        // Validación de campos vacíos
        if (brand == null || brand.isEmpty() || model == null || model.isEmpty() || airline == null || airline.isEmpty()) {
            return new Response("Brand, model and airline cannot be empty", Status.BAD_REQUEST);
        }

        // Validación de capacidad
        int maxCapacity;
        try {
            maxCapacity = Integer.parseInt(capacityStr);
            if (maxCapacity <= 0) {
                return new Response("Capacity must be a positive number", Status.BAD_REQUEST);
            }
        } catch (NumberFormatException e) {
            return new Response("Capacity must be numeric", Status.BAD_REQUEST);
        }

        Storage storage = Storage.getInstance();

        if (storage.getPlane(id) != null) {
            return new Response("Plane with given ID already exists", Status.BAD_REQUEST);
        }

        Plane plane = new Plane(id, brand, model, maxCapacity, airline);

        if (!storage.addPlane(plane)) {
            return new Response("Could not add plane", Status.INTERNAL_SERVER_ERROR);
        }

        return new Response("Plane created successfully", Status.CREATED);
    } catch (Exception e) {
        return new Response("Unexpected error: " + e.getMessage(), Status.INTERNAL_SERVER_ERROR);
    }
}


    public static Response listPlanes() {
        try {
            Storage storage = Storage.getInstance();
            ArrayList<Plane> planes = storage.getAllPlanes();
            planes.sort(Comparator.comparing(Plane::getId));
            return new Response("Plane list retrieved", Status.OK, planes);
        } catch (Exception e) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }
    
    public static Response getSortedPlanes() {
    try {
        // Obtener lista original de aviones desde el almacenamiento
        ArrayList<Plane> planes = Storage.getInstance().getAllPlanes();
        
        // Crear una copia para trabajar (protección del estado original)
        ArrayList<Plane> planesCopy = new ArrayList<>();
        
        //Verificar y clonar cada avión
        if (planes != null) {
            for (Plane plane : planes) {
                if (plane != null) {
                    planesCopy.add(plane.clone()); // Clonación del avión
                }
            }
        }
        
        return new Response("Planes loaded successfully.", Status.OK, planesCopy);
        
    } catch (Exception ex) {
        // Manejo de errores con logging
        System.err.println("Error loading planes: " + ex.getMessage());
        ex.printStackTrace();
        return new Response("Unexpected error occurred while loading planes.", 
                          Status.INTERNAL_SERVER_ERROR);
    }
}
}
