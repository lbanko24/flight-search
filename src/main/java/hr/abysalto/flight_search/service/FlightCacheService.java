package hr.abysalto.flight_search.service;

import hr.abysalto.flight_search.configuration.CacheProperties;
import hr.abysalto.flight_search.entity.Flight;
import hr.abysalto.flight_search.repository.FlightRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static hr.abysalto.flight_search.util.FlightSearchUtil.*;

@Service
public class FlightCacheService {
    private static final Logger logger = LoggerFactory.getLogger(FlightCacheService.class);

    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    private final FlightRepository flightRepository;

    private final CacheProperties cacheProperties;

    public FlightCacheService(FlightRepository flightRepository, CacheProperties cacheProperties) {
        this.flightRepository = flightRepository;
        this.cacheProperties = cacheProperties;
    }

    public List<Flight> searchFlights(String departureAirport, String destinationAirport,
                                      String departureDate, String returnDate,
                                      int passengers, String currency) {
        Date date1;
        Date date2;
        try {
            date1 = formatter.parse(departureDate);
            date2 = formatter.parse(returnDate);
        } catch (ParseException e) {
            logger.error("Error parsing date");
            throw new IllegalArgumentException("Error parsing date");
        }

        Specification<Flight> spec = flightHasDepartureAirport(departureAirport);
        spec = spec.and(flightHasDestinationAirport(destinationAirport));
        spec = spec.and(flightHasDepartureDate(date1));
        spec = spec.and(flightHasReturnDate(date2));
        spec = spec.and(flightHasPassengerCount(passengers));
        spec = spec.and(flightHasCurrency(currency));
        spec = spec.and(flightIsNotExpired(getExpiryThreshold()));

        return flightRepository.findAll(spec);
    }

    @Async
    public void cacheData(Flight flight) {
        logger.info("Caching flight data");
        flightRepository.save(flight);
    }

    private LocalDateTime getExpiryThreshold() {
        return LocalDateTime.now().minus(cacheProperties.getTtl());
    }

    public void cleanupCache() {
        int n = flightRepository.deleteExpired(getExpiryThreshold());
        logger.info("Cleaned up {} entries from cache", n);
    }
}
