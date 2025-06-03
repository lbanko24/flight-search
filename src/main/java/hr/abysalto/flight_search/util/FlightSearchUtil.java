package hr.abysalto.flight_search.util;

import hr.abysalto.flight_search.entity.Flight;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDateTime;
import java.util.Date;

public class FlightSearchUtil {
    public static Specification<Flight> flightHasDepartureAirport(String airport) {
        return (root, query, cb) -> cb.equal(root.get("departureAirport"), airport);
    }

    public static Specification<Flight> flightHasDestinationAirport(String airport) {
        return (root, query, cb) -> cb.equal(root.get("destinationAirport"), airport);
    }

    public static Specification<Flight> flightHasDepartureDate(Date date) {
        return (root, query, cb) -> cb.equal(root.get("departureDate"), date);
    }

    public static Specification<Flight> flightHasReturnDate(Date date) {
        return (root, query, cb) -> cb.equal(root.get("returnDate"), date);
    }

    public static Specification<Flight> flightHasCurrency(String currency) {
        return (root, query, cb) -> cb.equal(root.get("currency"), currency);
    }

    public static Specification<Flight> flightHasPassengerCount(int passengers) {
        return (root, query, cb) -> cb.equal(root.get("passengers"), passengers);
    }

    public static Specification<Flight> flightIsNotExpired(LocalDateTime expiryThreshold) {
        return (root, query, cb) -> cb.greaterThanOrEqualTo(root.get("createdOn"),
                expiryThreshold);
    }
}
