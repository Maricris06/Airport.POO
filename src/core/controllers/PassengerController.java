/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package core.controllers;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import core.models.airport.Passenger;
import core.models.storage.Storage;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 *
 * @author User invitado
 */
public class PassengerController {
        
    public static Response createPassenger(String idStr, String firstname, String lastname,
                                           String birthDateStr, String country,
                                           String countryPhoneCodeStr, String phoneStr) {
        try {
            // Validación de campos vacíos
            if (idStr == null || idStr.isEmpty()) {
                return new Response("Id must not be empty", Status.BAD_REQUEST);
            }
            if (firstname == null || firstname.isEmpty()) {
                return new Response("Firstname must not be empty", Status.BAD_REQUEST);
            }
            if (lastname == null || lastname.isEmpty()) {
                return new Response("Lastname must not be empty", Status.BAD_REQUEST);
            }
            if (birthDateStr == null || birthDateStr.isEmpty()) {
                return new Response("Birthdate must not be empty", Status.BAD_REQUEST);
            }
            if (country == null || country.isEmpty()) {
                return new Response("Country must not be empty", Status.BAD_REQUEST);
            }
            if (countryPhoneCodeStr == null || countryPhoneCodeStr.isEmpty()) {
                return new Response("Country phone code must not be empty", Status.BAD_REQUEST);
            }
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
                birthDate = LocalDate.parse(birthDateStr); // Formato 
            } catch (Exception e) {
                return new Response("Birthdate must be in format yyyy-MM-dd", Status.BAD_REQUEST);
            }

            // Validar existencia
            Storage storage = Storage.getInstance();
            if (storage.getPassenger(id) != null) {
                return new Response("Passenger already exists", Status.BAD_REQUEST);
            }

            // Crear pasajero
            Passenger passenger = new Passenger(id, firstname, lastname, birthDate, countryPhoneCode, phone, country);
            if (!storage.addPassenger(passenger)) {
                return new Response("Could not add passenger", Status.INTERNAL_SERVER_ERROR);
            }

            return new Response("Passenger created successfully", Status.CREATED);

        } catch (Exception e) {
            return new Response("Unexpected error", Status.INTERNAL_SERVER_ERROR);
        }
    }
}



