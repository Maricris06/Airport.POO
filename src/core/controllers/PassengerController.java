package core.controllers;

import core.controllers.utils.Response;
import core.services.PassengerService;

public class PassengerController {

    private static final PassengerService service = new PassengerService();

    public static Response createPassenger(String id, String firstname, String lastname,
                                           String birthDate, String country,
                                           String countryPhoneCode, String phone) {
        return service.createPassenger(id, firstname, lastname, birthDate, country, countryPhoneCode, phone);
    }

    public static Response updatePassenger(String id, String firstname, String lastname,
                                           String birthDate, String country,
                                           String countryPhoneCode, String phone) {
        return service.updatePassenger(id, firstname, lastname, birthDate, country, countryPhoneCode, phone);
    }

    public static Response showMyFlights(String id) {
        return service.showMyFlights(id);
    }

    public static Response getSortedPassengers() {
        return service.getSortedPassengers();
    }
}
