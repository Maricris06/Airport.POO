package core.models.airport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Plane {
    
    private final String id;
    private final String brand;
    private final String model;
    private final int maxCapacity;
    private final String airline;
    private final List<Flight> flights;

    public Plane(String id, String brand, String model, int maxCapacity, String airline) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.maxCapacity = maxCapacity;
        this.airline = airline;
        this.flights = new ArrayList<>();
    }

    public void addFlight(Flight flight) {
        if (flight != null && !flights.contains(flight)) {
            flights.add(flight);
        }
    }
    
    public String getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public String getAirline() {
        return airline;
    }

    public List<Flight> getFlights() {
        return Collections.unmodifiableList(flights);
    }
    
    public int getNumFlights() {
        return flights.size();
    }
    
    public Plane clone() {
        // Copia superficial; la lista flights queda vacía en el clon para evitar efectos colaterales
        return new Plane(
            this.id,
            this.brand,
            this.model,
            this.maxCapacity,
            this.airline
        );
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Plane)) return false;
        Plane plane = (Plane) o;
        return id.equals(plane.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "Plane{" +
               "id='" + id + '\'' +
               ", brand='" + brand + '\'' +
               ", model='" + model + '\'' +
               ", maxCapacity=" + maxCapacity +
               ", airline='" + airline + '\'' +
               ", numFlights=" + flights.size() +
               '}';
    }
}
