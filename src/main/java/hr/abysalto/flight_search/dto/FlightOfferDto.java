package hr.abysalto.flight_search.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class FlightOfferDto {

    private List<ItineraryDto> itineraries;

    private PriceDto price;

    private List<TravelerPricingDto> travelerPricings;

    public List<ItineraryDto> getItineraries() {
        return itineraries;
    }

    public void setItineraries(List<ItineraryDto> itineraries) {
        this.itineraries = itineraries;
    }

    public PriceDto getPrice() {
        return price;
    }

    public void setPrice(PriceDto price) {
        this.price = price;
    }

    public List<TravelerPricingDto> getTravelerPricings() {
        return travelerPricings;
    }

    public void setTravelerPricings(List<TravelerPricingDto> travelerPricings) {
        this.travelerPricings = travelerPricings;
    }
}
