package core.models.airport.flight;

import java.time.LocalDateTime;

public class FlightSchedule {
    private LocalDateTime departureDate;
    private int hoursDurationArrival;
    private int minutesDurationArrival;
    private int hoursDurationScale;
    private int minutesDurationScale;

    public FlightSchedule(LocalDateTime departureDate, int hoursDurationArrival, int minutesDurationArrival,
                          int hoursDurationScale, int minutesDurationScale) {
        this.departureDate = departureDate;
        this.hoursDurationArrival = hoursDurationArrival;
        this.minutesDurationArrival = minutesDurationArrival;
        this.hoursDurationScale = hoursDurationScale;
        this.minutesDurationScale = minutesDurationScale;
    }

    public LocalDateTime getDepartureDate() {
        return departureDate;
    }

    public void delay(int hours, int minutes) {
        this.departureDate = this.departureDate.plusHours(hours).plusMinutes(minutes);
    }

    public LocalDateTime calculateArrivalDate() {
        return departureDate
                .plusHours(hoursDurationScale + hoursDurationArrival)
                .plusMinutes(minutesDurationScale + minutesDurationArrival);
    }
}
