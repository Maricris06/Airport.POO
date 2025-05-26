package core.controllers.validators;

import core.controllers.utils.Response;
import core.controllers.utils.Status;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class PassengerValidator {

    public static Response validate(String idStr, String firstname, String lastname,
                                    String birthDateStr, String country,
                                    String countryPhoneCodeStr, String phoneStr) {
        if (isNullOrEmpty(idStr)) return error("Id must not be empty");
        if (isNullOrEmpty(firstname)) return error("Firstname must not be empty");
        if (isNullOrEmpty(lastname)) return error("Lastname must not be empty");
        if (isNullOrEmpty(birthDateStr)) return error("Birthdate must not be empty");
        if (isNullOrEmpty(country)) return error("Country must not be empty");
        if (isNumeric(country)) return error("Country must not be a number.");
        if (isNullOrEmpty(countryPhoneCodeStr)) return error("Country phone code must not be empty");
        if (isNullOrEmpty(phoneStr)) return error("Phone must not be empty");

        try {
            long id = Long.parseLong(idStr);
            if (id < 0 || idStr.length() > 15)
                return error("Id must be positive and max 15 digits");
        } catch (NumberFormatException e) {
            return error("Id must be a valid number");
        }

        try {
            int code = Integer.parseInt(countryPhoneCodeStr);
            if (code < 0 || countryPhoneCodeStr.length() > 3)
                return error("Country phone code must be positive and max 3 digits");
        } catch (NumberFormatException e) {
            return error("Country phone code must be a valid number");
        }

        try {
            long phone = Long.parseLong(phoneStr);
            if (phone < 0 || phoneStr.length() > 11)
                return error("Phone must be positive and max 11 digits");
        } catch (NumberFormatException e) {
            return error("Phone must be a valid number");
        }

        try {
            LocalDate.parse(birthDateStr);
        } catch (DateTimeParseException e) {
            return error("Birthdate must be in format yyyy-MM-dd");
        }

        return new Response("Valid", Status.OK);
    }

    private static boolean isNullOrEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    private static boolean isNumeric(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException ex) {
            return false;
        }
    }

    private static Response error(String message) {
        return new Response(message, Status.BAD_REQUEST);
    }
}
