package core.models.airport;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Flight {
    // --- Campos inmutables (final) ---
    private final String id;                   // Formato: XXXYYY (3 letras + 3 números)
    private final Plane plane;                 // Avión asignado (debe existir previamente)
    private final Location departureLocation;  // Aeropuerto de salida (debe existir)
    private final Location arrivalLocation;    // Aeropuerto de llegada (debe existir)
    private final LocalDateTime departureDate;  // Fecha-hora de salida (validada)
    private final Duration flightDuration;      // Duración del vuelo (> 00:00)
    private final Location scaleLocation;       // Escala opcional (puede ser null)
    private final Duration scaleDuration;       // Duración de escala (0 si no hay escala)
    private final FlightStatus status;          // Estado del vuelo (enum)
    private final List<Passenger> passengers;   // Lista inmutable de pasajeros

    // --- Enum para estados del vuelo ---
    public enum FlightStatus {
        SCHEDULED, DELAYED, BOARDING, IN_FLIGHT, LANDED, CANCELLED
    }

    // --- Builder (para construcción flexible) ---
    public static class Builder {
        // Campos requeridos
        private final String id;
        private final Plane plane;
        private final Location departureLocation;
        private final Location arrivalLocation;
        private final LocalDateTime departureDate;
        private final Duration flightDuration;

        // Campos opcionales
        private Location scaleLocation = null;
        private Duration scaleDuration = Duration.ZERO;
        private FlightStatus status = FlightStatus.SCHEDULED;
        private List<Passenger> passengers = Collections.emptyList();

        public Builder(String id, Plane plane, Location departureLocation, 
                      Location arrivalLocation, LocalDateTime departureDate,
                      Duration flightDuration) {
            this.id = id;
            this.plane = plane;
            this.departureLocation = departureLocation;
            this.arrivalLocation = arrivalLocation;
            this.departureDate = departureDate;
            this.flightDuration = flightDuration;
        }

        public Builder withScale(Location scaleLocation, Duration scaleDuration) {
            this.scaleLocation = scaleLocation;
            this.scaleDuration = scaleDuration;
            return this;
        }

        public Builder withStatus(FlightStatus status) {
            this.status = status;
            return this;
        }

        public Builder withPassengers(List<Passenger> passengers) {
            this.passengers = passengers != null ? List.copyOf(passengers) : Collections.emptyList();
            return this;
        }

        public Flight build() {
            return new Flight(this);
        }
    }

    // --- Constructor privado (usa Builder) ---
    private Flight(Builder builder) {
        validateFlightId(builder.id);
        validateNonNullComponents(builder.plane, builder.departureLocation, 
                                builder.arrivalLocation, builder.departureDate);
        validateDifferentLocations(builder.departureLocation, builder.arrivalLocation);
        validateFlightDuration(builder.flightDuration);
        
        if (builder.scaleLocation != null) {
            validateScale(builder.scaleLocation, builder.scaleDuration);
        }

        this.id = builder.id.toUpperCase();
        this.plane = builder.plane;
        this.departureLocation = builder.departureLocation;
        this.arrivalLocation = builder.arrivalLocation;
        this.departureDate = builder.departureDate;
        this.flightDuration = builder.flightDuration;
        this.scaleLocation = builder.scaleLocation;
        this.scaleDuration = builder.scaleDuration;
        this.status = builder.status;
        this.passengers = Collections.unmodifiableList(builder.passengers);
    }

    // --- Validaciones privadas ---
    private void validateFlightId(String id) {
        if (id == null || !id.matches("^[A-Z]{3}\\d{3}$")) {
            throw new IllegalArgumentException("Flight ID must be in format XXXYYY (3 letters + 3 digits)");
        }
    }

    private void validateNonNullComponents(Plane plane, Location departure, 
                                        Location arrival, LocalDateTime date) {
        Objects.requireNonNull(plane, "Plane cannot be null");
        Objects.requireNonNull(departure, "Departure location cannot be null");
        Objects.requireNonNull(arrival, "Arrival location cannot be null");
        Objects.requireNonNull(date, "Departure date cannot be null");
    }

    private void validateDifferentLocations(Location departure, Location arrival) {
        if (departure.equals(arrival)) {
            throw new IllegalArgumentException("Departure and arrival locations must be different");
        }
    }

    private void validateFlightDuration(Duration duration) {
        if (duration == null || duration.isNegative() || duration.isZero()) {
            throw new IllegalArgumentException("Flight duration must be positive");
        }
    }

    private void validateScale(Location scaleLocation, Duration scaleDuration) {
        Objects.requireNonNull(scaleLocation, "Scale location cannot be null");
        if (scaleDuration == null || scaleDuration.isNegative()) {
            throw new IllegalArgumentException("Scale duration cannot be negative");
        }
    }

    // --- Getters (no hay setters) ---
    public String getId() { return id; }
    public Plane getPlane() { return plane; }
    public Location getDepartureLocation() { return departureLocation; }
    public Location getArrivalLocation() { return arrivalLocation; }
    public LocalDateTime getDepartureDate() { return departureDate; }
    public Duration getFlightDuration() { return flightDuration; }
    public Location getScaleLocation() { return scaleLocation; }
    public Duration getScaleDuration() { return scaleDuration; }
    public FlightStatus getStatus() { return status; }
    public List<Passenger> getPassengers() { return passengers; }

    // --- Métodos de negocio (inmutables) ---
    public Flight withDelay(Duration delay) {
        if (delay == null || delay.isNegative() || delay.isZero()) {
            throw new IllegalArgumentException("Delay duration must be positive");
        }
        return new Builder(id, plane, departureLocation, arrivalLocation, 
                         departureDate.plus(delay), flightDuration)
            .withScale(scaleLocation, scaleDuration)
            .withStatus(FlightStatus.DELAYED)
            .withPassengers(passengers)
            .build();
    }

    public Flight withCancelledStatus() {
        return new Builder(id, plane, departureLocation, arrivalLocation,
                         departureDate, flightDuration)
            .withScale(scaleLocation, scaleDuration)
            .withStatus(FlightStatus.CANCELLED)
            .withPassengers(Collections.emptyList()) // Elimina pasajeros al cancelar
            .build();
    }

    // --- Métodos calculados ---
    public LocalDateTime getArrivalTime() {
        return departureDate.plus(flightDuration).plus(scaleDuration);
    }

    public boolean hasScale() {
        return scaleLocation != null && !scaleDuration.isZero();
    }

    public boolean isInternational() {
        return !departureLocation.getCountry().equals(arrivalLocation.getCountry());
    }

    // --- Patrón Prototype (para respuestas del controlador) ---
    @Override
    public Flight clone() {
        return new Builder(id, plane, departureLocation, arrivalLocation,
                         departureDate, flightDuration)
            .withScale(scaleLocation, scaleDuration)
            .withStatus(status)
            .withPassengers(passengers)
            .build();
    }

    // --- Equals & HashCode (basado en ID) ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Flight)) return false;
        Flight flight = (Flight) o;
        return id.equals(flight.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format(
            "Flight[id=%s, from=%s to=%s, departure=%s, status=%s]", 
            id, departureLocation.getAirportId(), 
            arrivalLocation.getAirportId(), departureDate, status
        );
    }
}