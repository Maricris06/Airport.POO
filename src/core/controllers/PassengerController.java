/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.airport.Flight;
import core.models.airport.Passenger;
import core.models.storage.Storage;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * @author User invitado
 */
public class PassengerController {

    public static Response createPassenger(String idStr, String firstname, String lastname,
                                           String birthDateStr, String country,
                                           String countryPhoneCodeStr, String phoneStr) {
        try {
            // Validación de campos vacíos y trim
            idStr = idStr != null ? idStr.trim() : null;
            if (idStr == null || idStr.isEmpty()) {
                return new Response("Id must not be empty", Status.BAD_REQUEST);
            }

            firstname = firstname != null ? firstname.trim() : null;
            if (firstname == null || firstname.isEmpty()) {
                return new Response("Firstname must not be empty", Status.BAD_REQUEST);
            }

            lastname = lastname != null ? lastname.trim() : null;
            if (lastname == null || lastname.isEmpty()) {
                return new Response("Lastname must not be empty", Status.BAD_REQUEST);
            }

            birthDateStr = birthDateStr != null ? birthDateStr.trim() : null;
            if (birthDateStr == null || birthDateStr.isEmpty()) {
                return new Response("Birthdate must not be empty", Status.BAD_REQUEST);
            }

            country = country != null ? country.trim() : null;
            if (country == null || country.isEmpty()) {
                return new Response("Country must not be empty", Status.BAD_REQUEST);
            }
            // Validar que el país no sea un número
            try {
                Integer.parseInt(country); // Intentar convertir a número
                return new Response("Country must not be a number.", Status.BAD_REQUEST);
            } catch (NumberFormatException ex) {
                // Si no es un número, continuamos
            }

            countryPhoneCodeStr = countryPhoneCodeStr != null ? countryPhoneCodeStr.trim() : null;
            if (countryPhoneCodeStr == null || countryPhoneCodeStr.isEmpty()) {
                return new Response("Country phone code must not be empty", Status.BAD_REQUEST);
            }

            phoneStr = phoneStr != null ? phoneStr.trim() : null;
            if (phoneStr == null || phoneStr.isEmpty()) {
                return new Response("Phone must not be empty", Status.BAD_REQUEST);
            }

            // Conversión y validación numérica
            long id;
            try {
                id = Long.parseLong(idStr);
                if (id < 0 || idStr.length() > 15) {
                    return new Response("Id must be positive and max 15 digits", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException e) {
                return new Response("Id must be a valid number", Status.BAD_REQUEST);
            }

            int countryPhoneCode;
            try {
                countryPhoneCode = Integer.parseInt(countryPhoneCodeStr);
                if (countryPhoneCode < 0 || countryPhoneCodeStr.length() > 3) {
                    return new Response("Country phone code must be positive and max 3 digits", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException e) {
                return new Response("Country phone code must be a valid number", Status.BAD_REQUEST);
            }

            long phone;
            try {
                phone = Long.parseLong(phoneStr);
                if (phone < 0 || phoneStr.length() > 11) {
                    return new Response("Phone must be positive and max 11 digits", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException e) {
                return new Response("Phone must be a valid number", Status.BAD_REQUEST);
            }

            // Conversión de la fecha
            LocalDate birthDate;
            birthDateStr = birthDateStr.trim(); // Eliminar espacios en blanco
                
            try {
                // Asegurarse de que la fecha esté en el formato correcto
                
                birthDate = LocalDate.parse(birthDateStr); // Formato esperado: yyyy-MM-dd
                // Imprimir la fecha parseada para depuración
                System.out.println("Fecha parseada: " + birthDate);
            } catch (DateTimeParseException e) {
                return new Response("La fecha debe estar en formato válido (yyyy-MM-dd)", Status.BAD_REQUEST);
            }
            // Validar existencia
            Storage storage = Storage.getInstance();
            if (storage.getPassenger(id) != null) {
                return new Response("Passenger already exists", Status.BAD_REQUEST);
            }

            // Crear pasajero
            Passenger passenger = new Passenger(id, firstname, lastname, birthDate, countryPhoneCode, phone, country);
            if (!Storage.getInstance().addPassenger(passenger)) {
    return new Response("Could not add passenger to storage", Status.INTERNAL_SERVER_ERROR);
}

            return new Response("Passenger created successfully", Status.CREATED);

        } catch (Exception e) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response updatePassenger(String idStr, String firstname, String lastname,
                                           String birthDateStr, String country,
                                           String countryPhoneCodeStr, String phoneStr) {
        try {
            // Validación de campos vacíos y trim
            idStr = idStr != null ? idStr.trim() : null;
            if (idStr == null || idStr.isEmpty()) {
                return new Response("Id must not be empty", Status.BAD_REQUEST);
            }

            firstname = firstname != null ? firstname.trim() : null;
            if (firstname == null || firstname.isEmpty()) {
                return new Response("Firstname must not be empty", Status.BAD_REQUEST);
            }

            lastname = lastname != null ? lastname.trim() : null;
            if (lastname == null || lastname.isEmpty()) {
                return new Response("Lastname must not be empty", Status.BAD_REQUEST);
            }

            birthDateStr = birthDateStr != null ? birthDateStr.trim() : null;
            if (birthDateStr == null || birthDateStr.isEmpty()) {
                return new Response("Birthdate must not be empty", Status.BAD_REQUEST);
            }

            country = country != null ? country.trim() : null;
            if (country == null || country.isEmpty()) {
                return new Response("Country must not be empty", Status.BAD_REQUEST);
            }
            // Validar que el país no sea un número
            try {
                Integer.parseInt(country); // Intentar convertir a número
                return new Response("Country must not be a number.", Status.BAD_REQUEST);
            } catch (NumberFormatException ex) {
                // Si no es un número, continuamos
            }

            countryPhoneCodeStr = countryPhoneCodeStr != null ? countryPhoneCodeStr.trim() : null;
            if (countryPhoneCodeStr == null || countryPhoneCodeStr.isEmpty()) {
                return new Response("Country phone code must not be empty", Status.BAD_REQUEST);
            }

            phoneStr = phoneStr != null ? phoneStr.trim() : null;
            if (phoneStr == null || phoneStr.isEmpty()) {
                return new Response("Phone must not be empty", Status.BAD_REQUEST);
            }

            // Conversión y validación numérica
            long id;
            try {
                id = Long.parseLong(idStr);
                if (id < 0 || idStr.length() > 15) {
                    return new Response("Id must be positive and max 15 digits", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException e) {
                return new Response("Id must be a valid number", Status.BAD_REQUEST);
            }

            int countryPhoneCode;
            try {
                countryPhoneCode = Integer.parseInt(countryPhoneCodeStr);
                if (countryPhoneCode < 0 || countryPhoneCodeStr.length() > 3) {
                    return new Response("Country phone code must be positive and max 3 digits", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException e) {
                return new Response("Country phone code must be a valid number", Status.BAD_REQUEST);
            }

            long phone;
            try {
                phone = Long.parseLong(phoneStr);
                if (phone < 0 || phoneStr.length() > 11) {
                    return new Response("Phone must be positive and max 11 digits", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException e) {
                return new Response("Phone must be a valid number", Status.BAD_REQUEST);
            }

            // Conversión de la fecha
            LocalDate birthDate;
            try {
                birthDate = LocalDate.parse(birthDateStr); // Formato esperado: yyyy-MM-dd
                
            } catch (Exception e) {
                return new Response("Birthdate must be in format yyyy-MM-dd", Status.BAD_REQUEST);
            }
                
            // Validar existencia
            Storage storage = Storage.getInstance();
            Passenger passenger = storage.getPassenger(id);
            if (passenger == null) {
                return new Response("Passenger not found", Status.NOT_FOUND);
            }

            // Actualizar datos utilizando setters
            passenger.setFirstname(firstname);
            passenger.setLastname(lastname);
            passenger.setBirthDate(birthDate);
            passenger.setCountryPhoneCode(countryPhoneCode);
            passenger.setPhone(phone);
            passenger.setCountry(country);

            // Guardar cambios en el almacenamiento, si es necesario
            if (!storage.updatePassenger(passenger)) {
                return new Response("Could not update passenger", Status.INTERNAL_SERVER_ERROR);
            }

            return new Response("Passenger updated successfully", Status.OK);

        } catch (Exception e) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response showMyFlights(String passengerId) {
        try {
            long passengerIdLong;

            // Validación de ID de pasajero
            if (passengerId == null || passengerId.trim().isEmpty()) {
                return new Response("Passenger id must not be empty.", Status.BAD_REQUEST);
            }
            try {
                passengerIdLong = Long.parseLong(passengerId.trim());
                if (passengerIdLong <= 0) {
                    return new Response("Passenger id must be positive.", Status.BAD_REQUEST);
                }
            } catch (NumberFormatException ex) {
                return new Response("Passenger id must be a number.", Status.BAD_REQUEST);
            }
            if (passengerId.trim().length() > 15) {
                return new Response("Passenger id must have a maximum of 15 digits.", Status.BAD_REQUEST);
            }

            // Validar existencia del pasajero
            Passenger passenger = Storage.getInstance().getPassenger(passengerIdLong);
            if (passenger == null) {
                return new Response("Passenger id does not exist.", Status.BAD_REQUEST);
            }

            // Obtener y ordenar vuelos del pasajero
            ArrayList<Flight> flights = Storage.getInstance().getPassengerFlights(passenger);
            if (flights != null) {
                flights.sort(Comparator.comparing(Flight::getDepartureDate));
            }

            // Clonar vuelos para evitar modificaciones externas
            ArrayList<Flight> flightsCopy = new ArrayList<>();
            if (flights != null) {
                for (Flight flight : flights) {
                    if (flight != null) {
                        flightsCopy.add(flight.clone());
                    } else {
                        flightsCopy.add(null);
                    }
                }
            }

            return new Response("Flights loaded successfully.", Status.OK, flightsCopy);

        } catch (Exception ex) {
            return new Response("Unexpected error.", Status.INTERNAL_SERVER_ERROR);
        }
    }

    public static Response getSortedPassengers() {
        try {
            // Obtener lista ordenada de pasajeros desde el almacenamiento
            ArrayList<Passenger> passengers = Storage.getInstance().getSortedPassengers();
            ArrayList<Passenger> passengersCopy = new ArrayList<>();

            // Verificar que la lista no sea nula
            if (passengers != null) {
                // Recorrer y clonar cada pasajero si no es nulo
                for (Passenger passenger : passengers) {
                    if (passenger != null) {
                        passengersCopy.add(passenger.clone()); // Clonación del pasajero
                    }
                }
            }

            // Retornar respuesta exitosa con la lista copiada
            return new Response("Passengers loaded successfully.", Status.OK, passengersCopy);

        } catch (Exception ex) {
            // Manejo de errores inesperados
            return new Response("Unexpected error occurred while loading passengers.", Status.INTERNAL_SERVER_ERROR);
        }
    }
}







