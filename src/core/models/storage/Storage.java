package core.models.storage;

import core.models.airport.Flight;
import core.models.airport.Location;
import core.models.airport.Passenger;
import core.models.airport.Plane;
import core.models.airport.Flight;
import core.models.airport.Location;
import core.models.airport.Passenger;
import core.models.airport.Plane;
import core.utils.events.DataType;
import java.io.*;
import java.util.*;
import core.utils.events.StorageListener;

public final class Storage {
    // Singleton thread-safe mejorado
    private static volatile Storage instance;
    private static final Object lock = new Object();

    // Repositorios concretos (usando las implementaciones que creamos)
    private final FlightRepository flightRepo;
    private final PassengerRepository passengerRepo;
    private final PlaneRepository planeRepo;
    private final LocationRepository locationRepo;
    public final StorageNotifier notifier;

    private Storage() {
        this.notifier = new StorageNotifier();
        this.flightRepo = new FlightRepository();
        this.passengerRepo = new PassengerRepository();
        this.planeRepo = new PlaneRepository();
        this.locationRepo = new LocationRepository();
        
        // Configurar notificaciones automáticas en los repositorios
        configureRepositories();
    }

    private void configureRepositories() {
        // Versión corregida
        flightRepo.setUpdateHandler(() -> notifier.notify(DataType.FLIGHT));
        passengerRepo.setUpdateHandler(() -> notifier.notify(DataType.PASSENGER));
        planeRepo.setUpdateHandler(() -> notifier.notify(DataType.PLANE));
        locationRepo.setUpdateHandler(() -> notifier.notify(DataType.LOCATION));
    }

    public static Storage getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new Storage();
                }
            }
        }
        return instance;
    }

    // Métodos de acceso a repositorios
    public FlightRepository flights() {
        return flightRepo;
    }

    public PassengerRepository passengers() {
        return passengerRepo;
    }

    public PlaneRepository planes() {
        return planeRepo;
    }

    public LocationRepository locations() {
        return locationRepo;
    }

    // Sistema de notificación
    public void subscribe(DataType type, StorageListener listener) {
        notifier.subscribe(type, listener);
    }

    public void unsubscribe(DataType type, StorageListener listener) {
        notifier.unsubscribe(type, listener);
    }

    // Persistencia
    public static class Persistence {
        private static final String DEFAULT_FILE = "storage.dat";

        public static void save() throws IOException {
            save(DEFAULT_FILE);
        }

        public static void save(String filename) throws IOException {
            try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filename))) {
                Map<String, Object> data = new HashMap<>();
                data.put("flights", Storage.getInstance().flightRepo.getAll());
                data.put("passengers", Storage.getInstance().passengerRepo.getAll());
                data.put("planes", Storage.getInstance().planeRepo.getAll());
                data.put("locations", Storage.getInstance().locationRepo.getAll());
                oos.writeObject(data);
            }
        }

        @SuppressWarnings("unchecked")
        public static void load() throws IOException, ClassNotFoundException {
            load(DEFAULT_FILE);
        }

        @SuppressWarnings("unchecked")
        public static void load(String filename) throws IOException, ClassNotFoundException {
            Storage storage = Storage.getInstance();
            
            try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename))) {
                Map<String, Object> data = (Map<String, Object>) ois.readObject();
                
                // Limpiar datos existentes
                storage.flightRepo.getAll().forEach(f -> storage.flightRepo.remove(f.getId()));
                storage.passengerRepo.getAll().forEach(p -> storage.passengerRepo.remove(p.getId()));
                storage.planeRepo.getAll().forEach(p -> storage.planeRepo.remove(p.getId()));
                storage.locationRepo.getAll().forEach(l -> storage.locationRepo.remove(l.getAirportId()));
                
                // Cargar nuevos datos
                ((List<Flight>) data.get("flights")).forEach(storage.flightRepo::add);
                ((List<Passenger>) data.get("passengers")).forEach(storage.passengerRepo::add);
                ((List<Plane>) data.get("planes")).forEach(storage.planeRepo::add);
                ((List<Location>) data.get("locations")).forEach(storage.locationRepo::add);
                
                // Notificar cambios
                Arrays.stream(DataType.values()).forEach(storage.notifier::notify);
            }
        }
    }

    // Métodos adicionales para ordenamiento
    public List<Flight> getSortedFlights(Comparator<Flight> comparator) {
        return flightRepo.getAllSorted(comparator);
    }

    public List<Passenger> getSortedPassengers(Comparator<Passenger> comparator) {
        return passengerRepo.getAllSorted(comparator);
    }

    public List<Plane> getSortedPlanes(Comparator<Plane> comparator) {
        return planeRepo.getAllSorted(comparator);
    }

    public List<Location> getSortedLocations(Comparator<Location> comparator) {
        return locationRepo.getAllSorted(comparator);
    }
}