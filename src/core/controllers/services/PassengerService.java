package core.services;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.controllers.validators.PassengerValidator;
import core.models.airport.Flight;
import core.models.airport.Passenger;
import core.models.storage.Storage;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PassengerService {

    private final Storage storage = Storage.getInstance();

    public Response createPassenger(String idStr, String firstname, String lastname,
                                    String birthDateStr, String country,
                                    String countryPhoneCodeStr, String phoneStr) {
        Response validation = PassengerValidator.validate(idStr, firstname, lastname, birthDateStr, country, countryPhoneCodeStr, phoneStr);
        if (validation.getStatus() != Status.OK) return validation;

        long id = Long.parseLong(idStr.trim());
        if (storage.getPassenger(id) != null)
            return new Response("Passenger already exists", Status.BAD_REQUEST);

        Passenger passenger = mapPassenger(idStr, firstname, lastname, birthDateStr, country, countryPhoneCodeStr, phoneStr);
        if (!storage.addPassenger(passenger))
            return new Response("Could not add passenger to storage", Status.INTERNAL_SERVER_ERROR);

        return new Response("Passenger created successfully", Status.CREATED);
    }

    public Response updatePassenger(String idStr, String firstname, String lastname,
                                    String birthDateStr, String country,
                                    String countryPhoneCodeStr, String phoneStr) {
        Response validation = PassengerValidator.validate(idStr, firstname, lastname, birthDateStr, country, countryPhoneCodeStr, phoneStr);
        if (validation.getStatus() != Status.OK) return validation;

        long id = Long.parseLong(idStr.trim());
        Passenger existing = storage.getPassenger(id);
        if (existing == null)
            return new Response("Passenger not found", Status.NOT_FOUND);

        Passenger updated = mapPassenger(idStr, firstname, lastname, birthDateStr, country, countryPhoneCodeStr, phoneStr);
        if (!storage.updatePassenger(updated))
            return new Response("Could not update passenger", Status.INTERNAL_SERVER_ERROR);

        return new Response("Passenger updated successfully", Status.OK);
    }

    public Response showMyFlights(String idStr) {
        if (idStr == null || idStr.trim().isEmpty())
            return new Response("Passenger id must not be empty.", Status.BAD_REQUEST);

        long id;
        try {
            id = Long.parseLong(idStr.trim());
            if (id <= 0 || idStr.trim().length() > 15)
                return new Response("Passenger id must be positive and at most 15 digits.", Status.BAD_REQUEST);
        } catch (NumberFormatException e) {
            return new Response("Passenger id must be a number.", Status.BAD_REQUEST);
        }

        Passenger passenger = storage.getPassenger(id);
        if (passenger == null)
            return new Response("Passenger not found", Status.NOT_FOUND);

        List<Flight> flights = storage.getPassengerFlights(passenger);
        if (flights != null) flights.sort(Comparator.comparing(Flight::getDepartureDate));

        ArrayList<Flight> copy = new ArrayList<>();
        if (flights != null) {
            for (Flight f : flights) copy.add(f.clone());
        }

        return new Response("Flights loaded successfully.", Status.OK, copy);
    }

    public Response getSortedPassengers() {
        List<Passenger> passengers = storage.getSortedPassengers();
        ArrayList<Passenger> result = new ArrayList<>();
        for (Passenger p : passengers) result.add(p.clone());
        return new Response("Passengers loaded successfully.", Status.OK, result);
    }

    private Passenger mapPassenger(String idStr, String firstname, String lastname,
                                   String birthDateStr, String country,
                                   String countryPhoneCodeStr, String phoneStr) {
        long id = Long.parseLong(idStr.trim());
        int code = Integer.parseInt(countryPhoneCodeStr.trim());
        long phone = Long.parseLong(phoneStr.trim());
        LocalDate birthDate = LocalDate.parse(birthDateStr.trim());
        return new Passenger(id, firstname.trim(), lastname.trim(), birthDate, code, phone, country.trim());
    }
}
