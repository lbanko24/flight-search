package hr.abysalto.flight_search.dto.conversion;

import hr.abysalto.flight_search.dto.FlightOfferDto;
import hr.abysalto.flight_search.dto.SegmentDto;
import hr.abysalto.flight_search.entity.Flight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class FlightMapper {
    private static final Logger logger = LoggerFactory.getLogger(FlightMapper.class);

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public static Flight fromFlightOfferDto(FlightOfferDto dto) {
        Flight flight = new Flight();
        SegmentDto departure = dto.getItineraries().getFirst().getSegments().getFirst();
        SegmentDto destination = dto.getItineraries().getLast().getSegments().getFirst();

        flight.setDepartureAirport(departure.getDeparture().getIataCode());
        flight.setDestinationAirport(destination.getDeparture().getIataCode());

        try {
            flight.setDepartureDate(new Date(formatter.parse(departure.getDeparture().getAt()).getTime()));
            flight.setReturnDate(new Date(formatter.parse(destination.getDeparture().getAt()).getTime()));
        } catch (ParseException ex) {
            logger.error("Error parsing date");
            throw new IllegalArgumentException("Error parsing date");
        }

        flight.setTransfers(dto.getItineraries().getFirst().getSegments().size() - 1);
        flight.setReturnTransfers(dto.getItineraries().getLast().getSegments().size() - 1);
        flight.setPassengers(dto.getTravelerPricings().size());
        flight.setCurrency(dto.getPrice().getCurrency());
        flight.setPrice(dto.getPrice().getTotal());

        return flight;
    }

    public static List<Flight> fromFlightOfferDtoList(List<FlightOfferDto> dtos) {
        return dtos.stream().map(FlightMapper::fromFlightOfferDto).toList();
    }
}
