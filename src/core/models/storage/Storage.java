/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.models.storage;

import core.models.airport.Flight;
import core.models.airport.Passenger;
import core.models.airport.Plane;
import java.util.ArrayList;

/**
 *
 * @author User invitado
 */
public class Storage {
      private static Storage instance;

    private ArrayList<Flight> flights;
    private ArrayList<Passenger> passengers;
    private ArrayList<Plane> planes;

    private Storage() {
        this.flights = new ArrayList<>();
        this.passengers = new ArrayList<>();
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

    public boolean delFlight(String id) {
        for (Flight flight : this.flights) {
            if (flight.getId().equals(id)) {
                this.flights.remove(flight);
                return true;
            }
        }
        return false;
    }

    public ArrayList<Flight> getAllFlights() {
        return new ArrayList<>(this.flights);
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

    public Passenger getPassenger(long id) {
        for (Passenger passenger : this.passengers) {
            if (passenger.getId() == id) {
                return passenger;
            }
        }
        return null;
    }

    public boolean delPassenger(long id) {
        for (Passenger passenger : this.passengers) {
            if (passenger.getId() == id) {
                this.passengers.remove(passenger);
                return true;
            }
        }
        return false;
    }

    public ArrayList<Passenger> getAllPassengers() {
        return new ArrayList<>(this.passengers);
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
    public boolean delPlane(String id) {
        for (Plane plane : this.planes) {
            if (plane.getId().equals(id)) {
                this.planes.remove(plane);
                return true;
            }
        }
        return false;
    }
    public ArrayList<Plane> getAllPlanes() {
        return new ArrayList<>(this.planes);
    }
}

