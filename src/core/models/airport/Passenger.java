package core.models.airport;

import java.time.LocalDate;
import java.time.Period;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public final class Passenger implements Cloneable {
    private final long id;                  // ID único (≥0 y ≤15 dígitos)
    private String firstName;         // Nombre (no vacío)
    private String lastName;          // Apellido (no vacío)
    private final LocalDate birthDate;      // Fecha válida (no futura)
    private int countryCode;          // Código telefónico (≥0 y ≤3 dígitos)
    private long phone;               // Teléfono (≥0 y ≤11 dígitos)
    private String country;           // País (no vacío)
    private final List<Flight> flights;     // Lista inmutable de vuelos


    // --- Builder (para construcción flexible) ---
    public static class Builder {
        // Campos requeridos
        private final long id;
        private final String firstName;
        private final String lastName;
        private final LocalDate birthDate;
        private final int countryCode;
        private final long phone;
        private final String country;

        // Campos opcionales
        private List<Flight> flights = Collections.emptyList();

        public Builder(long id, String firstName, String lastName, 
                      LocalDate birthDate, int countryCode, 
                      long phone, String country) {
            this.id = id;
            this.firstName = firstName;
            this.lastName = lastName;
            this.birthDate = birthDate;
            this.countryCode = countryCode;
            this.phone = phone;
            this.country = country;
        }

        public Builder withFlights(List<Flight> flights) {
            this.flights = flights != null ? List.copyOf(flights) : Collections.emptyList();
            return this;
        }

        public Passenger build() {
            return new Passenger(this);
        }
    }

    // --- Constructor privado (usa Builder) ---
    private Passenger(Builder builder) {
        validateId(builder.id);
        validatePhoneDetails(builder.countryCode, builder.phone);
        validatePersonalData(builder.firstName, builder.lastName, builder.country);
        validateBirthDate(builder.birthDate);

        this.id = builder.id;
        this.firstName = builder.firstName.trim();
        this.lastName = builder.lastName.trim();
        this.birthDate = builder.birthDate;
        this.countryCode = builder.countryCode;
        this.phone = builder.phone;
        this.country = builder.country.trim();
        this.flights = Collections.unmodifiableList(builder.flights);
    }

    // --- Validaciones privadas ---
    private void validateId(long id) {
        if (id < 0 || String.valueOf(id).length() > 15) {
            throw new IllegalArgumentException("ID must be ≥0 and have ≤15 digits");
        }
    }

    private void validatePhoneDetails(int countryCode, long phone) {
        if (countryCode < 0 || String.valueOf(countryCode).length() > 3) {
            throw new IllegalArgumentException("Country code must be ≥0 and have ≤3 digits");
        }
        if (phone < 0 || String.valueOf(phone).length() > 11) {
            throw new IllegalArgumentException("Phone must be ≥0 and have ≤11 digits");
        }
    }

    private void validatePersonalData(String firstName, String lastName, String country) {
        if (firstName == null || firstName.trim().isEmpty()) {
            throw new IllegalArgumentException("First name cannot be empty");
        }
        if (lastName == null || lastName.trim().isEmpty()) {
            throw new IllegalArgumentException("Last name cannot be empty");
        }
        if (country == null || country.trim().isEmpty()) {
            throw new IllegalArgumentException("Country cannot be empty");
        }
    }

    private void validateBirthDate(LocalDate birthDate) {
        Objects.requireNonNull(birthDate, "Birth date cannot be null");
        if (birthDate.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException("Birth date cannot be in the future");
        }
    }

    // --- Getters ---
    public long getId() { return id; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public LocalDate getBirthDate() { return birthDate; }
    public int getCountryCode() { return countryCode; }
    public long getPhone() { return phone; }
    public String getCountry() { return country; }
    public List<Flight> getFlights() { return flights; }
    
    // --- Setters ---
    public void setFirstName(String firstName) {this.firstName = firstName;}
    public void setLastName(String lastName) {this.lastName = lastName;}
    public void setCountryCode(int countryCode){this.countryCode = countryCode;}
    public void setPhone(long phone) {this.phone = phone;}
    public void setCountry(String country) {this.country = country;}
    // --- Métodos calculados ---
    public int calculateAge() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getFormattedPhone() {
        return String.format("+%d %d", countryCode, phone);
    }

    // --- Métodos de negocio (inmutables) ---
    public Passenger withUpdatedInfo(String firstName, String lastName, 
                                   LocalDate birthDate, int countryCode, 
                                   long phone, String country) {
        return new Builder(id, firstName, lastName, birthDate, 
                          countryCode, phone, country)
            .withFlights(flights)
            .build();
    }

    // --- Patrón Prototype ---
    @Override
    public Passenger clone() {
        return new Builder(id, firstName, lastName, birthDate, 
                          countryCode, phone, country)
            .withFlights(flights)
            .build();
    }

    // --- Equals & HashCode (basado en ID) ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Passenger passenger = (Passenger) o;
        return id == passenger.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format(
            "Passenger[id=%d, name=%s %s, country=%s, phone=%s]", 
            id, firstName, lastName, country, getFormattedPhone()
        );
    }
}