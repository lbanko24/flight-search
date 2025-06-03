package hr.abysalto.flight_search.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import hr.abysalto.flight_search.dto.conversion.FlightMapper;
import hr.abysalto.flight_search.configuration.AmadeusProperties;
import hr.abysalto.flight_search.dto.FlightOfferDto;
import hr.abysalto.flight_search.entity.Flight;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class AmadeusService {
    private static final Logger logger = LoggerFactory.getLogger(AmadeusService.class);

    private static final String FLIGHT_OFFER_URL = "https://test.api.amadeus.com/v2/shopping/flight-offers"
            + "?originLocationCode={origin}"
            + "&destinationLocationCode={destination}"
            + "&adults={adults}"
            + "&departureDate={departure}"
            + "&returnDate={return}"
            + "&currencyCode={currency}";

    private static final String AUTH_URL = "https://test.api.amadeus.com/v1/security/oauth2/token";

    private final RestTemplate restTemplate;

    private final FlightCacheService flightCacheService;

    private final AmadeusProperties amadeusProperties;

    private final ObjectMapper mapper = new ObjectMapper();

    public AmadeusService(RestTemplate restTemplate, FlightCacheService flightCacheService,
                          AmadeusProperties amadeusProperties) {
        this.restTemplate = restTemplate;
        this.flightCacheService = flightCacheService;
        this.amadeusProperties = amadeusProperties;
    }

    private String fetchBearerToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        String body = String.format("grant_type=client_credentials&client_id=%s&client_secret=%s",
                amadeusProperties.getClientId(), amadeusProperties.getClientSecret()
        );

        ResponseEntity<Map> response = restTemplate.exchange(
                AUTH_URL,
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                Map.class
        );

        return (String) response.getBody().get("access_token");
    }

    public List<Flight> searchFlights(String departureAirport, String destinationAirport,
                                      String departureDate, String returnDate,
                                      int passengers, String currency) {

        List<Flight> cachedFlights = flightCacheService.searchFlights(departureAirport, destinationAirport,
                departureDate, returnDate,
                passengers, currency);

        if (!cachedFlights.isEmpty()) {
            logger.info("Found cached flights");
            return cachedFlights;
        }

        List<Flight> flights = fetchFlights(departureAirport, destinationAirport,
                departureDate, returnDate, passengers, currency);
        flights.forEach(flightCacheService::cacheData);
        return flights;
    }

    private List<Flight> fetchFlights(String departureAirport, String destinationAirport,
                                String departureDate, String returnDate,
                                int passengers, String currency) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(fetchBearerToken());
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        String response = restTemplate.exchange(
                FLIGHT_OFFER_URL,
                HttpMethod.GET,
                new HttpEntity<>(headers),
                String.class,
                departureAirport,
                destinationAirport,
                passengers,
                departureDate,
                returnDate,
                currency
        ).getBody();

        Object data = JsonPath.read(response, "$.data");
        List<FlightOfferDto> offers = mapper.convertValue(data, new TypeReference<>() {});
        return FlightMapper.fromFlightOfferDtoList(offers);
    }
}
