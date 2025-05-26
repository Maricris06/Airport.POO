package core.models.airport;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Flight {

    private final String id;
    private final Plane plane;
    private final Location departureLocation;
    private final Location scaleLocation;  // puede ser null si no hay escala
    private final Location arrivalLocation;
    private LocalDateTime departureDate;
    private final Duration durationToScale;  // Duration hasta escala (0 si no hay escala)
    private final Duration durationFromScale; // Duration desde escala a destino (igual a duración total si no hay escala)
    private final List<Passenger> passengers;

    // Constructor para vuelo sin escala
    public Flight(String id, Plane plane, Location departureLocation, Location arrivalLocation,
                  LocalDateTime departureDate, Duration totalDuration) {
        this(id, plane, departureLocation, null, arrivalLocation, departureDate,
             Duration.ZERO, totalDuration);
    }

    // Constructor para vuelo con escala
    public Flight(String id, Plane plane, Location departureLocation, Location scaleLocation, Location arrivalLocation,
                  LocalDateTime departureDate, Duration durationToScale, Duration durationFromScale) {
        this.id = id;
        this.plane = plane;
        this.departureLocation = departureLocation;
        this.scaleLocation = scaleLocation;
        this.arrivalLocation = arrivalLocation;
        this.departureDate = departureDate;
        this.durationToScale = durationToScale != null ? durationToScale : Duration.ZERO;
        this.durationFromScale = durationFromScale != null ? durationFromScale : Duration.ZERO;
        this.passengers = new ArrayList<>();
    }

    public void addPassenger(Passenger passenger) {
        if (passenger != null && !passengers.contains(passenger)) {
            passengers.add(passenger);
        }
    }

    public String getId() {
        return id;
    }

    public Plane getPlane() {
        return plane;
    }

    public Location getDepartureLocation() {
        return departureLocation;
    }

    public Location getScaleLocation() {
        return scaleLocation;
    }

    public Location getArrivalLocation() {
        return arrivalLocation;
    }

    public LocalDateTime getDepartureDate() {
        return departureDate;
    }

    public void setDepartureDate(LocalDateTime departureDate) {
        this.departureDate = departureDate;
    }

    // Devuelve una lista inmutable para no exponer la lista interna
    public List<Passenger> getPassengers() {
        return Collections.unmodifiableList(passengers);
    }

    public int getNumPassengers() {
        return passengers.size();
    }

    // Calcula la fecha y hora de llegada final considerando escala
    public LocalDateTime calculateArrivalDate() {
        if (scaleLocation == null) {
            return departureDate.plus(durationFromScale);
        }
        // si hay escala, la llegada es salida + duración a escala + duración desde escala
        return departureDate.plus(durationToScale).plus(durationFromScale);
    }

    // Retrasa la salida
    public void delay(int hours, int minutes) {
        this.departureDate = this.departureDate.plusHours(hours).plusMinutes(minutes);
    }

    // Clone: no copia pasajeros ni añade a plane automáticamente
    public Flight clone() {
        return new Flight(
            this.id,
            this.plane,
            this.departureLocation,
            this.scaleLocation,
            this.arrivalLocation,
            this.departureDate,
            this.durationToScale,
            this.durationFromScale
        );
    }

    @Override
    public String toString() {
        return "Flight{" +
               "id='" + id + '\'' +
               ", plane=" + (plane != null ? plane.getId() : "null") +
               ", departureLocation=" + departureLocation.getAirportId() +
               ", scaleLocation=" + (scaleLocation != null ? scaleLocation.getAirportId() : "none") +
               ", arrivalLocation=" + arrivalLocation.getAirportId() +
               ", departureDate=" + departureDate +
               ", passengers=" + passengers.size() +
               '}';
    }
}
