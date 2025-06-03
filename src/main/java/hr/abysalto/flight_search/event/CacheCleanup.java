package hr.abysalto.flight_search.event;

import hr.abysalto.flight_search.service.FlightCacheService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class CacheCleanup {

    private final FlightCacheService flightCacheService;

    public CacheCleanup(FlightCacheService flightCacheService) {
        this.flightCacheService = flightCacheService;
    }

    @Scheduled(fixedRateString = "${app.cache.cleanupRate}")
    public void cleanup() {
        flightCacheService.cleanupCache();
    }
}
