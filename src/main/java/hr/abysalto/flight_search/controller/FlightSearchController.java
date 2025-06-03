package hr.abysalto.flight_search.controller;

import hr.abysalto.flight_search.entity.Flight;
import hr.abysalto.flight_search.service.AmadeusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/search")
public class FlightSearchController {

    private final AmadeusService amadeusService;

    public FlightSearchController(AmadeusService amadeusService) {
        this.amadeusService = amadeusService;
    }

    @GetMapping
    public ResponseEntity<List<Flight>> searchFlights(@RequestParam String originLocationCode,
                                                      @RequestParam String destinationLocationCode,
                                                      @RequestParam String departureDate,
                                                      @RequestParam  String returnDate,
                                                      @RequestParam int adults,
                                                      @RequestParam String currencyCode){

        return ResponseEntity.ok(amadeusService.searchFlights(
                originLocationCode, destinationLocationCode,
                departureDate, returnDate,
                adults, currencyCode
        ));
    }
}
