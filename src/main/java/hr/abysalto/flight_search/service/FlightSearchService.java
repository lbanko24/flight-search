package hr.abysalto.flight_search.service;

import hr.abysalto.flight_search.entity.Flight;

import java.util.List;


public interface FlightSearchService {
    List<Flight> searchFlights(String departureAirport, String destinationAirport,
                               String departureDate, String returnDate,
                               int passengers, String currency);
}
