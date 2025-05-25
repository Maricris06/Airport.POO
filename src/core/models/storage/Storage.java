/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models.storage;

import core.models.airport.Flight;
import core.models.airport.Location;
import core.models.airport.Passenger;
import core.models.airport.Plane;
import java.awt.List;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * @author User invitado
 */
public class Storage {
      private static Storage instance;

    private ArrayList<Flight> flights;
    private ArrayList<Passenger> passengers;
    private ArrayList<Plane> planes;
     private ArrayList<Location> locations;

    private Storage() {
        this.flights = new ArrayList<>();
        this.passengers = new ArrayList<>();
        this.planes = new ArrayList<>();
        this.locations = new ArrayList<>();
    }

    public static Storage getInstance() {
        if (instance == null) {
            instance = new Storage();
        }
        return instance;
    }

    // ----------- FLIGHTS -----------

    public boolean addFlight(Flight flight) {
        for (Flight f : this.flights) {
            if (f.getId().equals(flight.getId())) {
                return false;
            }
        }
        this.flights.add(flight);
        return true;
    }

    public Flight getFlight(String id) {
        for (Flight flight : this.flights) {
            if (flight.getId().equals(id)) {
                return flight;
            }
        }
        return null;
    }

    public ArrayList<Flight> getAllFlights() {
        return new ArrayList<>(this.flights);
    }
   

public ArrayList<Flight> getSortedFlights() {
    // Crear una copia de la lista para no modificar la original
    ArrayList<Flight> sortedFlights = new ArrayList<>(flights);
    int n = sortedFlights.size();

    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - i - 1; j++) {
            // Comparar por fecha de salida
            LocalDateTime date1 = sortedFlights.get(j).getDepartureDate();
            LocalDateTime date2 = sortedFlights.get(j + 1).getDepartureDate();

            if (date1.isAfter(date2)) {
                // Intercambiar vuelos
                Flight temp = sortedFlights.get(j);
                sortedFlights.set(j, sortedFlights.get(j + 1));
                sortedFlights.set(j + 1, temp);
            }
        }
    }

    return sortedFlights;
}


    // ----------- PASSENGERS -----------

    public boolean addPassenger(Passenger passenger) {
        for (Passenger p : this.passengers) {
            if (p.getId() == passenger.getId()) {
                return false;
            }
        }
        this.passengers.add(passenger);
        return true;
    }
    
     public boolean updatePassenger(Passenger passenger) {
        for (int i = 0; i < passengers.size(); i++) {
            if (passengers.get(i).getId() == passenger.getId()) {
                passengers.set(i, passenger); // Actualiza el pasajero en la lista
                return true; // Actualización exitosa
            }
        }
        return false; // No se encontró el pasajero para actualizar
    }
     
    public Passenger getPassenger(long id) {
        for (Passenger passenger : this.passengers) {
            if (passenger.getId() == id) {
                return passenger;
            }
        }
        return null;
    }

    public ArrayList<Passenger> getAllPassengers() {
        return new ArrayList<>(this.passengers);
    }
    
     public ArrayList<Passenger> getSortedPassengers() {
    // Crear una copia para no modificar la lista original
    ArrayList<Passenger> sortedPassengers = new ArrayList<>(passengers);
    int n = sortedPassengers.size();
    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - i - 1; j++) {
            // Comparar por ID; cambia getId() por el criterio que necesites
            if (sortedPassengers.get(j).getId() > sortedPassengers.get(j + 1).getId()) {
                // Intercambiar posiciones
                Passenger temp = sortedPassengers.get(j);
                sortedPassengers.set(j, sortedPassengers.get(j + 1));
                sortedPassengers.set(j + 1, temp);
            }
        }
    }
    return sortedPassengers;
}
     
public ArrayList<Flight> getPassengerFlights(Passenger passenger) {
    ArrayList<Flight> passengerFlights = new ArrayList<>();
    for (Flight flight : flights) {
        for (Passenger p : flight.getPassengers()) {
            if (p.getId() == passenger.getId()) {
                passengerFlights.add(flight);
                break; 
            }
        }
    }
    return passengerFlights;
}


    // ----------- PLANES -----------
    public boolean addPlane(Plane plane) {
        for (Plane p : this.planes) {
            if (p.getId().equals(plane.getId())) {
                return false; // Plane already exists
            }
        }
        this.planes.add(plane);
        return true;
    }
    
    public Plane getPlane(String id) {
        for (Plane plane : this.planes) {
            if (plane.getId().equals(id)) {
                return plane;
            }
        }
        return null;
    }

    public ArrayList<Plane> getAllPlanes() {
        return new ArrayList<>(this.planes);
    }

    public ArrayList<Location> getAllLocations() {
        return new ArrayList<>(locations); // Devuelve una copia de la lista de ubicaciones
    }
    // Método para obtener una ubicación específica por su ID
    public Location getLocation(String airportId) {
        for (Location location : locations) {
            if (location.getAirportId().equals(airportId)) {
                return location; // Devuelve la ubicación si se encuentra
            }
        }
        return null; // Devuelve null si no se encuentra la ubicación
    }
    // Método para agregar una nueva ubicación
    public boolean addLocation(Location location) {
        if (location == null || getLocation(location.getAirportId()) != null) {
            return false; // No se puede agregar si la ubicación es nula o ya existe
        }
        locations.add(location); // Agrega la nueva ubicación
        return true; // Retorna true si se agregó con éxito
    }

  public ArrayList<Location> getSortedLocations() {
    // Crear una copia para no modificar la lista original
    ArrayList<Location> sortedLocations = new ArrayList<>(locations);
    int n = sortedLocations.size();
    
    // Ordenar usando el algoritmo de burbuja (puedes cambiar a otro algoritmo si lo prefieres)
    for (int i = 0; i < n - 1; i++) {
        for (int j = 0; j < n - i - 1; j++) {
            // Comparar por ID; cambia getId() por el criterio que necesites
            if (sortedLocations.get(j).getAirportId().compareTo(sortedLocations.get(j + 1).getAirportId()) > 0) {
                // Intercambiar posiciones
                Location temp = sortedLocations.get(j);
                sortedLocations.set(j, sortedLocations.get(j + 1));
                sortedLocations.set(j + 1, temp);
            }
        }
    }
    return sortedLocations;
}


  
}

