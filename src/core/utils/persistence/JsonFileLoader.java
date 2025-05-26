package core.utils.persistence;

import core.models.airport.*;
import core.models.storage.Storage;
import core.utils.events.DataType;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JsonFileLoader {

    private static final Map<String, Location> loadedLocations = new HashMap<>();
    private static final Map<String, Plane> loadedPlanes = new HashMap<>();
    private static final Map<Long, Passenger> loadedPassengers = new HashMap<>();

    /**
     * Carga todos los datos desde las rutas de archivos JSON proporcionadas y los almacena en el singleton Storage.
     */
    public static void loadAll(String locationsPath, String planesPath, String passengersPath, String flightsPath) {
        loadedLocations.clear();
        loadedPlanes.clear();
        loadedPassengers.clear();

        loadLocations(locationsPath);
        loadPlanes(planesPath);
        loadPassengers(passengersPath);
        loadFlights(flightsPath);
    }

    /**
     * Lee todos los bytes de un archivo en la ruta especificada y los convierte en una cadena de texto.
     */
    private static String readFile(String path) throws IOException {
        return new String(Files.readAllBytes(Paths.get(path)));
    }

    /**
     * Carga objetos Location desde un archivo JSON, almacenándolos en memoria y en Storage.
     */
    private static void loadLocations(String path) {
        try {
            JSONArray array = new JSONArray(readFile(path));
            for (int i = 0; i < array.length(); i++) {
                try {
                    JSONObject obj = array.getJSONObject(i);

                    // Validar presencia de campos requeridos
                    if (!obj.has("airportId") || !obj.has("airportName") || !obj.has("airportCity")
                            || !obj.has("airportCountry") || !obj.has("airportLatitude") || !obj.has("airportLongitude")) {
                        throw new JSONException("Faltan campos requeridos en el objeto location JSON en el índice " + i);
                    }

                    Location loc = new Location(
                            obj.getString("airportId"),
                            obj.getString("airportName"),
                            obj.getString("airportCity"),
                            obj.getString("airportCountry"),
                            obj.getDouble("airportLatitude"),
                            obj.getDouble("airportLongitude")
                    );
                    loadedLocations.put(loc.getAirportId(), loc);
                    Storage.getInstance().addLocation(loc);
                } catch (JSONException e) {
                    System.err.println("Se omitió la location en el índice " + i + " por error en JSON: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error de E/S al cargar locations desde '" + path + "': " + e.getMessage());
        } catch (JSONException e) {
            System.err.println("Error de parseo JSON al cargar locations: " + e.getMessage());
        }
    }

    /**
     * Carga objetos Plane desde un archivo JSON, almacenándolos en memoria y en Storage.
     */
    private static void loadPlanes(String path) {
        try {
            JSONArray array = new JSONArray(readFile(path));
            for (int i = 0; i < array.length(); i++) {
                try {
                    JSONObject obj = array.getJSONObject(i);

                    // Validar presencia de campos requeridos
                    if (!obj.has("id") || !obj.has("brand") || !obj.has("model") || !obj.has("maxCapacity") || !obj.has("airline")) {
                        throw new JSONException("Faltan campos requeridos en el objeto plane JSON en el índice " + i);
                    }

                    Plane plane = new Plane(
                            obj.getString("id"),
                            obj.getString("brand"),
                            obj.getString("model"),
                            obj.getInt("maxCapacity"),
                            obj.getString("airline")
                    );
                    loadedPlanes.put(plane.getId(), plane);
                    Storage.getInstance().addPlane(plane);
                } catch (JSONException e) {
                    System.err.println("Se omitió el plane en el índice " + i + " por error en JSON: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error de E/S al cargar planes desde '" + path + "': " + e.getMessage());
        } catch (JSONException e) {
            System.err.println("Error de parseo JSON al cargar planes: " + e.getMessage());
        }
    }
    
    private static void linkFlights() {
        List<Passenger> passengers = Storage.getInstance().getAllPassengers();
        List<Flight> flights = Storage.getInstance().getAllFlights();

        if (passengers.isEmpty() || flights.isEmpty()) {
            return;
        }

        int links = 0;
        for (int i = 0; i < Math.min(passengers.size(), flights.size()); i++) {
            Passenger p = passengers.get(i);
            Flight f = flights.get(i);

            if (p != null && f != null) {
                Storage.getInstance().addBookingLink(f.getId(), p.getId());
                links++;
            }
        }
    }

    
    /**
     * Carga objetos Passenger desde un archivo JSON, almacenándolos en memoria y en Storage.
     */
    private static void loadPassengers(String path) {
        try {
            JSONArray array = new JSONArray(readFile(path));
            for (int i = 0; i < array.length(); i++) {
                try {
                    JSONObject obj = array.getJSONObject(i);

                    // Validar presencia de campos requeridos
                    if (!obj.has("id") || !obj.has("firstname") || !obj.has("lastname") || !obj.has("birthDate")
                            || !obj.has("countryPhoneCode") || !obj.has("phone") || !obj.has("country")) {
                        throw new JSONException("Faltan campos requeridos en el objeto passenger JSON en el índice " + i);
                    }

                    Passenger passenger = new Passenger(
                            obj.getLong("id"),
                            obj.getString("firstname"),
                            obj.getString("lastname"),
                            LocalDate.parse(obj.getString("birthDate")),
                            obj.getInt("countryPhoneCode"),
                            obj.getLong("phone"),
                            obj.getString("country")
                    );
                    loadedPassengers.put(passenger.getId(), passenger);
                    Storage.getInstance().addPassenger(passenger);
                } catch (JSONException | IllegalArgumentException e) {
                    System.err.println("Se omitió el passenger en el índice " + i + " por error al parsear JSON: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error de E/S al cargar passengers desde '" + path + "': " + e.getMessage());
        } catch (JSONException e) {
            System.err.println("Error de parseo JSON al cargar passengers: " + e.getMessage());
        }
    }

    /**
     * Carga objetos Flight desde un archivo JSON y los almacena en Storage.
     */
    private static void loadFlights(String path) {
        try {
            JSONArray array = new JSONArray(readFile(path));
            for (int i = 0; i < array.length(); i++) {
                try {
                    JSONObject obj = array.getJSONObject(i);

                    // Validar presencia de campos requeridos
                    if (!obj.has("id") || !obj.has("plane") || !obj.has("departureLocation") || !obj.has("arrivalLocation") || !obj.has("departureDate")
                            || !obj.has("hoursDurationArrival") || !obj.has("minutesDurationArrival")) {
                        throw new JSONException("Faltan campos requeridos en el objeto flight JSON en el índice " + i);
                    }

                    String id = obj.getString("id");
                    Plane plane = loadedPlanes.get(obj.getString("plane"));
                    Location departure = loadedLocations.get(obj.getString("departureLocation"));
                    Location arrival = loadedLocations.get(obj.getString("arrivalLocation"));

                    if (plane == null) {
                        throw new JSONException("No se encontró el plane con id '" + obj.getString("plane") + "' para el flight en el índice " + i);
                    }
                    if (departure == null) {
                        throw new JSONException("No se encontró la location de salida con id '" + obj.getString("departureLocation") + "' para el flight en el índice " + i);
                    }
                    if (arrival == null) {
                        throw new JSONException("No se encontró la location de llegada con id '" + obj.getString("arrivalLocation") + "' para el flight en el índice " + i);
                    }

                    Location scale = null;
                    if (obj.has("scaleLocation") && !obj.isNull("scaleLocation")) {
                        String scaleId = obj.getString("scaleLocation");
                        scale = loadedLocations.get(scaleId);
                        if (scale == null) {
                            throw new JSONException("No se encontró la location de escala con id '" + scaleId + "' para el flight en el índice " + i);
                        }
                    }

                    LocalDateTime departureDate = LocalDateTime.parse(obj.getString("departureDate"));

                    Duration durationToScale = Duration.ofHours(obj.optInt("hoursDurationScale", 0))
                            .plusMinutes(obj.optInt("minutesDurationScale", 0));

                    Duration durationToArrival = Duration.ofHours(obj.getInt("hoursDurationArrival"))
                            .plusMinutes(obj.getInt("minutesDurationArrival"));

                    Flight flight;
                    if (scale == null) {
                        flight = new Flight(id, plane, departure, arrival, departureDate, durationToArrival);
                    } else {
                        flight = new Flight(id, plane, departure, scale, arrival, departureDate, durationToScale, durationToArrival);
                    }

                    Storage.getInstance().addFlight(flight);
                } catch (JSONException | IllegalArgumentException e) {
                    System.err.println("Se omitió el flight en el índice " + i + " por error al parsear JSON: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Error de E/S al cargar flights desde '" + path + "': " + e.getMessage());
        } catch (JSONException e) {
            System.err.println("Error de parseo JSON al cargar flights: " + e.getMessage());
        }
    }
}
