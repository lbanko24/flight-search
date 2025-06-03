package hr.abysalto.flight_search.controller;

import hr.abysalto.flight_search.service.FlightCacheService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cache")
public class FlightCacheController {

    private final FlightCacheService flightCacheService;

    public FlightCacheController(FlightCacheService flightCacheService) {
        this.flightCacheService = flightCacheService;
    }

    @DeleteMapping
    public void cleanupCache() {
        flightCacheService.cleanupCache();
    }
}
