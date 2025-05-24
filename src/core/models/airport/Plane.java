package core.models.airport;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Plane implements Cloneable {
    // --- Campos inmutables ---
    private final String id;          // Formato XXYYYYY (2 letras + 5 dígitos)
    private final String brand;
    private final String model;
    private final int maxCapacity;    // > 0
    private final String airline;
    private final List<Flight> assignedFlights; // Lista inmutable

    // --- Builder ---
    public static class Builder {
        // Campos requeridos
        private final String id;
        private final String brand;
        private final String model;
        private final int maxCapacity;
        private final String airline;

        // Campos opcionales
        private List<Flight> assignedFlights = Collections.emptyList();

        public Builder(String id, String brand, String model, 
                     int maxCapacity, String airline) {
            this.id = id;
            this.brand = brand;
            this.model = model;
            this.maxCapacity = maxCapacity;
            this.airline = airline;
        }

        public Builder withAssignedFlights(List<Flight> flights) {
            this.assignedFlights = flights != null ? List.copyOf(flights) : Collections.emptyList();
            return this;
        }

        public Plane build() {
            return new Plane(this);
        }
    }

    // --- Constructor privado ---
    private Plane(Builder builder) {
        validateId(builder.id);
        validateCapacity(builder.maxCapacity);
        validateTextFields(builder.brand, builder.model, builder.airline);

        this.id = builder.id.toUpperCase();
        this.brand = builder.brand.trim();
        this.model = builder.model.trim();
        this.maxCapacity = builder.maxCapacity;
        this.airline = builder.airline.trim();
        this.assignedFlights = Collections.unmodifiableList(builder.assignedFlights);
    }

    // --- Validaciones ---
    private void validateId(String id) {
        if (id == null || !id.matches("^[A-Z]{2}\\d{5}$")) {
            throw new IllegalArgumentException(
                "Plane ID must be in format XXYYYYY (2 uppercase letters + 5 digits)");
        }
    }

    private void validateCapacity(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Max capacity must be positive");
        }
    }

    private void validateTextFields(String brand, String model, String airline) {
        if (brand == null || brand.trim().isEmpty()) {
            throw new IllegalArgumentException("Brand cannot be null or empty");
        }
        if (model == null || model.trim().isEmpty()) {
            throw new IllegalArgumentException("Model cannot be null or empty");
        }
        if (airline == null || airline.trim().isEmpty()) {
            throw new IllegalArgumentException("Airline cannot be null or empty");
        }
    }

    // --- Getters ---
    public String getId() { return id; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public int getMaxCapacity() { return maxCapacity; }
    public String getAirline() { return airline; }
    public List<Flight> getAssignedFlights() { return assignedFlights; }

    // --- Métodos de negocio (inmutables) ---
    public Plane withUpdatedInfo(String brand, String model, String airline) {
        return new Builder(id, brand, model, maxCapacity, airline)
            .withAssignedFlights(assignedFlights)
            .build();
    }

    public Plane withAddedFlight(Flight flight) {
        Objects.requireNonNull(flight, "Flight cannot be null");
        List<Flight> newFlights = new java.util.ArrayList<>(assignedFlights);
        newFlights.add(flight);
        return new Builder(id, brand, model, maxCapacity, airline)
            .withAssignedFlights(newFlights)
            .build();
    }

    public Plane withRemovedFlight(Flight flight) {
        Objects.requireNonNull(flight, "Flight cannot be null");
        List<Flight> newFlights = new java.util.ArrayList<>(assignedFlights);
        newFlights.remove(flight);
        return new Builder(id, brand, model, maxCapacity, airline)
            .withAssignedFlights(newFlights)
            .build();
    }

    // --- Métodos calculados ---
    public int getCurrentUtilization() {
        return assignedFlights.size();
    }

    public boolean isAtFullCapacity() {
        return getCurrentUtilization() >= maxCapacity;
    }

    // --- Patrón Prototype ---
    @Override
    public Plane clone() {
        return new Builder(id, brand, model, maxCapacity, airline)
            .withAssignedFlights(assignedFlights)
            .build();
    }

    // --- Equals & HashCode ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Plane plane = (Plane) o;
        return id.equals(plane.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format(
            "Plane[id=%s, brand=%s, model=%s, capacity=%d/%d, airline=%s]", 
            id, brand, model, getCurrentUtilization(), maxCapacity, airline
        );
    }
}