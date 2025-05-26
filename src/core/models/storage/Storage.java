package core.models.storage;

import core.models.airport.Flight;
import core.models.airport.Passenger;
import core.models.airport.Plane;
import core.models.airport.Location;
import core.interfaces.repository.IFlightRepository;
import core.interfaces.repository.ILocationRepository;
import core.interfaces.repository.IPassengerRepository;
import core.interfaces.repository.IPlaneRepository;
import core.interfaces.repository.IStorage;

import java.util.List;

public class Storage implements IStorage {

    private static Storage instance;

    private final IFlightRepository flightRepository;
    private final IPassengerRepository passengerRepository;
    private final IPlaneRepository planeRepository;
    private final ILocationRepository locationRepository;

    private Storage() {
        this.flightRepository = new FlightRepository();
        this.passengerRepository = new PassengerRepository();
        this.planeRepository = new PlaneRepository();
        this.locationRepository = new LocationRepository();
    }

    public static Storage getInstance() {
        if (instance == null) {
            instance = new Storage();
        }
        return instance;
    }
    
    public boolean addBookingLink(String flightId, long passengerId) {
        Flight flight = flightRepository.getFlight(flightId);
        Passenger passenger = passengerRepository.getPassenger(passengerId);

        if (flight == null || passenger == null) {
            return false; // No se encontró vuelo o pasajero
        }

        // Añadir pasajero al vuelo si no está ya
        flight.addPassenger(passenger);

        // Añadir vuelo al pasajero si no está ya
        passenger.addFlight(flight);

        // Actualizar pasajero para guardar relación
        return passengerRepository.updatePassenger(passenger);
    }

    // -------- FLIGHTS --------

    public boolean addFlight(Flight flight) {
        return flightRepository.addFlight(flight);
    }

    public Flight getFlight(String id) {
        return flightRepository.getFlight(id);
    }

    public List<Flight> getAllFlights() {
        return flightRepository.getAllFlights();
    }

    public List<Flight> getSortedFlights() {
        return flightRepository.getSortedFlights();
    }

    // -------- PASSENGERS --------

    public boolean addPassenger(Passenger passenger) {
        return passengerRepository.addPassenger(passenger);
    }

    public boolean updatePassenger(Passenger passenger) {
        return passengerRepository.updatePassenger(passenger);
    }

    public Passenger getPassenger(long id) {
        return passengerRepository.getPassenger(id);
    }

    public List<Passenger> getAllPassengers() {
        return passengerRepository.getAllPassengers();
    }

    public List<Passenger> getSortedPassengers() {
        return passengerRepository.getSortedPassengers();
    }
    
    public List<Flight> getPassengerFlights(Passenger passenger) {
        return passengerRepository.getFlightsByPassenger(passenger);
    }

    // -------- PLANES --------

    public boolean addPlane(Plane plane) {
        return planeRepository.addPlane(plane);
    }

    public Plane getPlane(String id) {
        return planeRepository.getPlane(id);
    }

    public List<Plane> getAllPlanes() {
        return planeRepository.getAllPlanes();
    }

    // -------- LOCATIONS --------

    public boolean addLocation(Location location) {
        return locationRepository.addLocation(location);
    }

    public Location getLocation(String airportId) {
        return locationRepository.getLocation(airportId);
    }

    public List<Location> getAllLocations() {
        return locationRepository.getAllLocations();
    }

    public List<Location> getSortedLocations() {
        return locationRepository.getSortedLocations();
    }
}
