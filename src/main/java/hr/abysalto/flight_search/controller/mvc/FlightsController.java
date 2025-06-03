package hr.abysalto.flight_search.controller.mvc;

import hr.abysalto.flight_search.entity.Flight;
import hr.abysalto.flight_search.service.AmadeusService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class FlightsController {

    private final AmadeusService amadeusService;

    public FlightsController(AmadeusService amadeusService) {
        this.amadeusService = amadeusService;
    }

    @GetMapping("/flights")
    public String getFlights(@RequestParam String originLocationCode,
                             @RequestParam String destinationLocationCode,
                             @RequestParam String departureDate,
                             @RequestParam  String returnDate,
                             @RequestParam int adults,
                             @RequestParam String currencyCode,
                             Model model) {

        try {
            List<Flight> flights = amadeusService.searchFlights(
                    originLocationCode, destinationLocationCode,
                    departureDate, returnDate,
                    adults, currencyCode
            );

            model.addAttribute("flights", flights);
        } catch (Exception ex) {
            model.addAttribute("error", "Unable to search flights: " + ex.getMessage());
            return "index";
        }

        return "flights";
    }
}
