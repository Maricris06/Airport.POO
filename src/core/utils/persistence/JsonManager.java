/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.utils.persistence;

import core.models.airport.Flight;
import core.models.airport.Location;
import core.models.airport.Passenger;
import core.models.airport.Plane;
import core.models.storage.Storage;
import core.utils.events.DataType;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.DateTimeException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class JsonManager {

    private static Map<String, Location> loadedLocationsMap = new HashMap<>();
    private static Map<String, Plane> loadedPlanesMap = new HashMap<>();
    private static Map<Long, Passenger> loadedPassengersMap = new HashMap<>();

    private JsonManager() {
        
    }
    
    private static String readFileAsString(String filePath) throws IOException {
        return new String(Files.readAllBytes(Paths.get(filePath)), StandardCharsets.UTF_8);
    }

    public static void loadAllDataFromFiles(
            String locationsFilePath,
            String planesFilePath,
            String passengersFilePath,
            String flightsFilePath
    ) {
        loadedLocationsMap.clear();
        loadedPlanesMap.clear();
        loadedPassengersMap.clear();
        
        loadLocations(locationsFilePath);
        loadPlanes(planesFilePath);
        loadPassengers(passengersFilePath);
        loadFlights(flightsFilePath);
        
        linkFlights();
        Arrays.stream(DataType.values()).forEach(type -> 
            Storage.getInstance().notifier.notify(type));
    }

    private static void loadLocations(String filePath) {
        try {
            String jsonString = readFileAsString(filePath);

            JSONArray locationsArray = new JSONArray(jsonString);

            for (int i = 0; i < locationsArray.length(); i++) {
                JSONObject locJson = locationsArray.getJSONObject(i);

                String airportId = locJson.getString("airportId");
                String name = locJson.getString("airportName");
                String city = locJson.getString("airportCity");
                String country = locJson.getString("airportCountry");
                double latitude = locJson.getDouble("airportLatitude");
                double longitude = locJson.getDouble("airportLongitude");

                Location location = new Location(
                        airportId, name, city,
                        country, latitude,
                        longitude);

                loadedLocationsMap.put(location.getAirportId(), location);
                Storage.getInstance().locations().add(location);
            }

        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void loadPlanes(String filePath) {
        try {
            String jsonString = readFileAsString(filePath);

            JSONArray planesArray = new JSONArray(jsonString);

            for (int i = 0; i < planesArray.length(); i++) {
                JSONObject planeJson = planesArray.getJSONObject(i);

                String id = planeJson.getString("id");
                String brand = planeJson.getString("brand");
                String model = planeJson.getString("model");
                int maxCapacity = planeJson.getInt("maxCapacity");
                String airline = planeJson.getString("airline");

                Plane plane = new Plane.Builder(id, brand, model, maxCapacity, airline)
                    .withAssignedFlights(Collections.emptyList()) // Inicialmente sin vuelos
                    .build();

                loadedPlanesMap.put(plane.getId(), plane);
                Storage.getInstance().planes().add(plane);
            }

        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static void loadPassengers(String filePath) {
        try {
            String jsonString = readFileAsString(filePath);

            JSONArray passengersArray = new JSONArray(jsonString);

            for (int i = 0; i < passengersArray.length(); i++) {
                JSONObject passengerJson = passengersArray.getJSONObject(i);

                long id = passengerJson.getLong("id");
                String firstname = passengerJson.getString("firstname");
                String lastname = passengerJson.getString("lastname");
                LocalDate birthDate = LocalDate.parse(passengerJson.getString("birthDate"));
                int countryPhoneCode = passengerJson.getInt("countryPhoneCode");
                long phone = passengerJson.getLong("phone");
                String country = passengerJson.getString("country");

                Passenger passenger = new Passenger.Builder(
                        id,
                        firstname,
                        lastname,
                        birthDate,
                        countryPhoneCode,
                        phone,
                        country)
                    .withFlights(Collections.emptyList()) // Inicialmente sin vuelos
                    .build();

                loadedPassengersMap.put(passenger.getId(), passenger);
                Storage.getInstance().passengers().add(passenger);
            }

        } catch (IOException | JSONException ex) {
            ex.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    private static void linkFlights() {
        Storage storage = Storage.getInstance();
        List<Passenger> passengers = storage.passengers().getAll();
        List<Flight> flights = storage.flights().getAll();
        
        if (passengers.isEmpty() || flights.isEmpty()) return;
        
        List<Flight> updatedFlights = new ArrayList<>();
        List<Passenger> updatedPassengers = new ArrayList<>();
        
        for (int i = 0; i < Math.min(passengers.size(), flights.size()); i++) {
            try {
                Passenger passenger = passengers.get(i);
                Flight flight = flights.get(i);

                // Crear nueva lista de vuelos para el pasajero
                List<Flight> passengerFlights = new ArrayList<>(passenger.getFlights());
                if (!passengerFlights.contains(flight)) {
                    passengerFlights.add(flight);
                }   

                // Crear nuevo pasajero actualizado
                Passenger updatedPassenger = new Passenger.Builder(
                    passenger.getId(),
                    passenger.getFirstName(),
                    passenger.getLastName(),
                    passenger.getBirthDate(),
                    passenger.getCountryCode(),
                    passenger.getPhone(),
                    passenger.getCountry())
                    .withFlights(passengerFlights)
                    .build();

                // Crear nueva lista de pasajeros para el vuelo
                List<Passenger> flightPassengers = new ArrayList<>(flight.getPassengers());
                if (!flightPassengers.contains(updatedPassenger)) {
                    flightPassengers.add(updatedPassenger);
                }

                // Crear nuevo vuelo actualizado
                Flight updatedFlight = new Flight.Builder(
                    flight.getId(),
                    flight.getPlane(),
                    flight.getDepartureLocation(),
                    flight.getArrivalLocation(),
                    flight.getDepartureDate(),
                    flight.getFlightDuration())
                    .withScale(flight.getScaleLocation(), flight.getScaleDuration())
                    .withStatus(flight.getStatus())
                    .withPassengers(flightPassengers)
                    .build();
                
                updatedPassengers.add(updatedPassenger);
                updatedFlights.add(updatedFlight);
            
            } catch (Exception e) {
                System.err.println("Error vinculando vuelo " + flights.get(i).getId() + 
                            " con pasajero " + passengers.get(i).getId() + ": " + e.getMessage());
            }
        }
        // Actualización masiva más eficiente
        if (!updatedPassengers.isEmpty()) {
            storage.passengers().updateAll(updatedPassengers);
            storage.flights().updateAll(updatedFlights);
            storage.notifier.notify(DataType.PASSENGER);
            storage.notifier.notify(DataType.FLIGHT);
        }
    }
    
    private static List<Flight> createNewFlightsList(Passenger passenger, Flight newFlight) {
        List<Flight> newFlights = new ArrayList<>(passenger.getFlights());
        newFlights.add(newFlight);
        return newFlights;
    }
    
    private static List<Passenger> createNewPassengersList(Flight flight, Passenger newPassenger) {
        List<Passenger> newPassengers = new ArrayList<>(flight.getPassengers());
        newPassengers.add(newPassenger);
        return newPassengers;
    }
    
    private static List<Passenger> addPassengerToList(List<Passenger> currentPassengers, Passenger newPassenger) {
        List<Passenger> newPassengers = new ArrayList<>(currentPassengers);
        if (!newPassengers.contains(newPassenger)) {
            newPassengers.add(newPassenger);
        }
        return newPassengers;
    }
    
    private static List<Passenger> createUpdatedPassengerList(List<Passenger> currentPassengers, Passenger newPassenger) {
        List<Passenger> updated = new ArrayList<>(currentPassengers);
        if (!updated.contains(newPassenger)) {
            updated.add(newPassenger);
        }
        return updated;
    }
    
    private static List<Flight> createUpdatedFlightList(List<Flight> currentFlights, Flight newFlight) {
        List<Flight> updated = new ArrayList<>(currentFlights);
        if (!updated.contains(newFlight)) {
            updated.add(newFlight);
        }
        return updated;
    }
    
    private static void loadFlights(String filePath) {
        try {
            String jsonString = readFileAsString(filePath);
            JSONArray flightsArray = new JSONArray(jsonString);
            int flightsLoadedCount = 0;

            for (int i = 0; i < flightsArray.length(); i++) {
                JSONObject flightJson = flightsArray.getJSONObject(i);

                String flightId = flightJson.getString("id");
                String planeId = flightJson.getString("plane");
                String depLocId = flightJson.getString("departureLocation");
                String arrLocId = flightJson.getString("arrivalLocation");

                String scaleLocId = null;
                if (flightJson.has("scaleLocation") && !flightJson.isNull("scaleLocation")) {
                    scaleLocId = flightJson.getString("scaleLocation");
                }

                Plane plane = loadedPlanesMap.get(planeId);
                Location departureLocation = loadedLocationsMap.get(depLocId);
                Location arrivalLocation = loadedLocationsMap.get(arrLocId);
                Location scaleLocation = (scaleLocId != null) ? loadedLocationsMap.get(scaleLocId) : null;

                if (plane == null) {
                    System.err.println("  ERROR: Avión ID '" + planeId + "' no encontrado para vuelo '" + flightId + "'. Vuelo omitido.");
                    continue;
                }
                if (departureLocation == null) {
                    System.err.println("  ERROR: Loc Salida ID '" + depLocId + "' no encontrada para vuelo '" + flightId + "'. Vuelo omitido.");
                    continue;
                }
                if (arrivalLocation == null) {
                    System.err.println("  ERROR: Loc Llegada ID '" + arrLocId + "' no encontrada para vuelo '" + flightId + "'. Vuelo omitido.");
                    continue;
                }
                if (scaleLocId != null && scaleLocation == null) {
                    System.err.println("  ERROR: Loc Escala ID '" + scaleLocId + "' especificada pero no encontrada para vuelo '" + flightId + "'. Vuelo omitido.");
                    continue;
                }

                LocalDateTime departureDateTime = LocalDateTime.parse(flightJson.getString("departureDate"));

                int durArrH = flightJson.getInt("hoursDurationArrival");
                int durArrM = flightJson.getInt("minutesDurationArrival");
                int durScaH = flightJson.optInt("hoursDurationScale", 0);
                int durScaM = flightJson.optInt("minutesDurationScale", 0);

                Flight flight;
                if (scaleLocation != null) {
                        flight = new Flight.Builder(
                        flightId,
                        plane,
                        departureLocation,
                        arrivalLocation,
                        departureDateTime,
                        Duration.ofHours(durArrH).plusMinutes(durArrM))
                    .withScale(
                        scaleLocation, 
                        Duration.ofHours(durScaH).plusMinutes(durScaM))
                    .build();
                } else {
                        flight = new Flight.Builder(
                        flightId,
                        plane,
                        departureLocation,
                        arrivalLocation,
                        departureDateTime,
                        Duration.ofHours(durArrH).plusMinutes(durArrM))
                    .build();
                }

                if (flightJson.has("passengerIdsOnFlight")) {
                    JSONArray passIdsArray = flightJson.getJSONArray("passengerIdsOnFlight");
                    for (int j = 0; j < passIdsArray.length(); j++) {
                        long passengerId = passIdsArray.getLong(j);
                        Passenger passenger = loadedPassengersMap.get(passengerId);
                        if (passenger != null) {
                            Flight updatedFlight = new Flight.Builder(
                                    flight.getId(),
                                    flight.getPlane(),
                                    flight.getDepartureLocation(),
                                    flight.getArrivalLocation(),
                                    flight.getDepartureDate(),
                                    flight.getFlightDuration())
                                .withScale(flight.getScaleLocation(), flight.getScaleDuration())
                                .withStatus(flight.getStatus())
                                .withPassengers(createUpdatedPassengerList(flight.getPassengers(), passenger))
                                .build();
                            Passenger updatedPassenger = new Passenger.Builder(
                                    passenger.getId(),
                                    passenger.getFirstName(),
                                    passenger.getLastName(),
                                    passenger.getBirthDate(),
                                    passenger.getCountryCode(),
                                    passenger.getPhone(),
                                    passenger.getCountry())
                                .withFlights(createUpdatedFlightList(passenger.getFlights(), flight))
                                .build();
                        } else {
                            System.err.println("  Advertencia: Pasajero con ID " + passengerId + " no encontrado para el vuelo '" + flightId + "'.");
                        }
                    }
                }

                Storage.getInstance().flights().add(flight);
                flightsLoadedCount++;
            }
            System.out.println("  " + flightsLoadedCount + " vuelos cargados y añadidos a Storage.");
        } catch (IOException | JSONException | DateTimeException e) {
            System.err.println("ERROR cargando/parseando Vuelos desde '" + filePath + "': " + e.getMessage());
        } catch (Exception e) {
            System.err.println("ERROR INESPERADO procesando Vuelos '" + filePath + "': " + e.getMessage());
            e.printStackTrace();
        }
    }

    
}