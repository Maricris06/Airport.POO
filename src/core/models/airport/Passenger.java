package core.models.airport;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Passenger {
    
    private long id;
    private String firstname;
    private String lastname;
    private LocalDate birthDate;
    private int countryPhoneCode;
    private long phone;
    private String country;
    private final List<Flight> flights;

    public Passenger(long id, String firstname, String lastname, LocalDate birthDate, int countryPhoneCode, long phone, String country) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthDate = birthDate;
        this.countryPhoneCode = countryPhoneCode;
        this.phone = phone;
        this.country = country;
        this.flights = new ArrayList<>();
    }

    // Constructor copia con copia profunda de vuelos
    public Passenger(Passenger passenger) {
        this.id = passenger.id;
        this.firstname = passenger.firstname;
        this.lastname = passenger.lastname;
        this.birthDate = passenger.birthDate;
        this.countryPhoneCode = passenger.countryPhoneCode;
        this.phone = passenger.phone;
        this.country = passenger.country;
        
        this.flights = new ArrayList<>();
        if (passenger.flights != null) {
            for (Flight flight : passenger.flights) {
                this.flights.add(flight != null ? flight.clone() : null);
            }
        }
    }

    // Getters
    public long getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public int getCountryPhoneCode() {
        return countryPhoneCode;
    }

    public long getPhone() {
        return phone;
    }

    public String getCountry() {
        return country;
    }

    public List<Flight> getFlights() {
        return Collections.unmodifiableList(flights);
    }

    // Setters
    public void setId(long id) {
        this.id = id;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setCountryPhoneCode(int countryPhoneCode) {
        this.countryPhoneCode = countryPhoneCode;
    }

    public void setPhone(long phone) {
        this.phone = phone;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void addFlight(Flight flight) {
        if (flight != null) {
            flights.add(flight);
        }
    }

    public String getFullname() {
        return firstname + " " + lastname;
    }
    
    public String generateFullPhone() {
        return "+" + countryPhoneCode + " " + phone;
    }
    
    public int calculateAge() {
        return Period.between(birthDate, LocalDate.now()).getYears();
    }
    
    public int getNumFlights() {
        return flights.size();
    }
    
    public Passenger clone() {
        return new Passenger(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Passenger)) return false;
        Passenger that = (Passenger) o;
        return id == that.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Passenger{" +
               "id=" + id +
               ", fullname='" + getFullname() + '\'' +
               ", country='" + country + '\'' +
               '}';
    }
}
