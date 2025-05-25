/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.airport.Location;
import core.models.storage.Storage;
import java.util.ArrayList;

/**
 *
 * @author User invitado
 */
public class LocationController {
    public static Response createLocation(String airportId, String name, String city, 
                                        String country, String latitudeStr, 
                                        String longitudeStr) {
        try {
            // Validar formato de ID
            if (airportId == null || !airportId.matches("[A-Z]{3}")) {
                return new Response("Airport ID must be 3 uppercase letters (e.g., JFK)", Status.BAD_REQUEST);
            }

            // Validar campos no nulos
            if (name == null || name.isEmpty() || city == null || city.isEmpty() || country == null || country.isEmpty()) {
                return new Response("Name, city, and country cannot be empty", Status.BAD_REQUEST);
            }

            // Convertir y validar coordenadas
            double latitude = parseCoordinate(latitudeStr, "Latitude", -90, 90);
            double longitude = parseCoordinate(longitudeStr, "Longitude", -180, 180);

            // Crear y almacenar la ubicación
            Location location = new Location(airportId, name, city, country, latitude, longitude);
            if (!Storage.getInstance().addLocation(location)) {
                return new Response("Airport ID already exists", Status.BAD_REQUEST);
            }

            return new Response("Location created successfully", Status.CREATED, location);

        } catch (NumberFormatException e) {
            return new Response("Invalid coordinate format: " + e.getMessage(), Status.BAD_REQUEST);
        } catch (IllegalArgumentException e) {
            return new Response(e.getMessage(), Status.BAD_REQUEST);
        } catch (Exception e) {
            return new Response("Unexpected error: " + e.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Obtiene una ubicación por su ID.
     */
    public static Response getLocation(String airportId) {
        try {
            Location location = Storage.getInstance().getLocation(airportId);
            if (location == null) {
                return new Response("Location not found", Status.NOT_FOUND);
            }
            return new Response("Location retrieved successfully", Status.OK, location);
        } catch (Exception e) {
            return new Response("Error retrieving location: " + e.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Lista todas las ubicaciones ordenadas por ID.
     */
    public static Response getAllLocations() {
        try {
            return new Response(
                "Locations retrieved successfully",
                Status.OK,
                Storage.getInstance().getAllLocations()
            );
        } catch (Exception e) {
            return new Response("Error retrieving locations: " + e.getMessage(), Status.INTERNAL_SERVER_ERROR);
        }
    }

    
    private static double parseCoordinate(String value, String fieldName, double min, double max) 
            throws NumberFormatException, IllegalArgumentException {
        double coord = Double.parseDouble(value);
        if (coord < min || coord > max) {
            throw new IllegalArgumentException(fieldName + " must be between " + min + " and " + max);
        }
        return coord;
    }
    
  public static Response getSortedLocations() {
    try {
        // Obtener lista ordenada de ubicaciones desde el almacenamiento
        ArrayList<Location> locations = Storage.getInstance().getSortedLocations();
        ArrayList<Location> locationsCopy = new ArrayList<>();

        // Verificar que la lista no sea nula
        if (locations != null) {
            // Recorrer y clonar cada ubicación si no es nula
            for (Location location : locations) {
                if (location != null) {
                    locationsCopy.add(location.clone()); // Clonación de la ubicación
                }
            }
        }

        // Retornar respuesta exitosa con la lista copiada
        return new Response("Locations loaded successfully.", Status.OK, locationsCopy);

    } catch (Exception ex) {
        // Manejo de errores inesperados
        System.err.println("Error loading locations: " + ex.getMessage());
        ex.printStackTrace();
        return new Response("Unexpected error occurred while loading locations.", Status.INTERNAL_SERVER_ERROR);
    }
}

}

