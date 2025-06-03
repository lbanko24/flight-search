package hr.abysalto.flight_search.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SegmentDto {

    private AirportDateDto departure;

    private AirportDateDto arrival;

    public AirportDateDto getDeparture() {
        return departure;
    }

    public void setDeparture(AirportDateDto departure) {
        this.departure = departure;
    }

    public AirportDateDto getArrival() {
        return arrival;
    }

    public void setArrival(AirportDateDto arrival) {
        this.arrival = arrival;
    }
}
